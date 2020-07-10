package fr.dila.solonepg.elastic.batch;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.model.NoSuchDocumentException;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.runtime.transaction.TransactionHelper;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.internal.Maps;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.elastic.indexing.mapping.IDocumentMapper;
import fr.dila.solonepg.elastic.indexing.mapping.IDossierMapper;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentEmptyException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentMissingException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentNotParsedException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.DocumentTooLargeException;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.MappingException;
import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.indexing.IndexationDossierStatistics;
import fr.dila.solonepg.elastic.models.json.IndexationObjectMapperProviderUtil;
import fr.dila.solonepg.elastic.persistence.service.IIndexationPersistenceService;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SessionUtil;

public class IndexationService extends DefaultComponent implements IIndexationService {

	/**
	 * Logger formalisé en surcouche du logger apache/log4j
	 */
	private static final STLogger			LOGGER				= STLogFactory.getLog(IndexationDossiersBatch.class);

	/**
	 * Logger géré dans un fichier à part.
	 */
	private static final org.slf4j.Logger	LOGGER_BATCH			= LoggerFactory
																		.getLogger("INDEXATION-BATCH");

	/**
	 * Logger pour les cas particuliers d'erreur d'indexation de fichier (fichier manquant, erreur d'extraction du texte,
	 * fichier vide, fichiers trop volumineux).
	 * 
	 * Loggers gérés dans des fichiers à part.
	 */
	private static final STLogger	LOGGER_MISSING			= STLogFactory.getLog("INDEXATION-MISSING");
	private static final STLogger	LOGGER_PARSE			= STLogFactory.getLog("INDEXATION-PARSE");
	private static final STLogger	LOGGER_EMPTY			= STLogFactory.getLog("INDEXATION-EMPTY");
	private static final STLogger	LOGGER_LARGE			= STLogFactory.getLog("INDEXATION-LARGE");

	/** partie du pattern de date utilisé pour nommer les répertoires */
	private static final String JSON_MINUTE_PART_PATTERN = "yyyy-MM-dd_HH_mm";
	/** pattern de date utilisé pour nommer les fichiers */
	private static final String JSON_FILE_PATTERN = String.format("%s_ss'T'Z", JSON_MINUTE_PART_PATTERN);
	/** nom de répertoire pour construire le chemin inséré dans le champ 'path' pour les fichiers du parapheur */
	private static final String PATH_PARAPHEUR = "parapheur";
	/** nom de répertoire pour construire le chemin inséré dans le champ 'path' pour les fichiers du fond de dossier */
	private static final String PATH_FOND_DE_DOSSIER = "fond de dossier";

	private ConfigService					configService;
	private IDossierMapper					dossierMapper;
	private IDocumentMapper					documentMapper;
	private ParapheurService				parapheurService;
	private IIndexationPersistenceService	indexationPersistenceService;
	private File 							exportFolder;
	private File 							temporaryFolder;

	/** composants internes */
	private ObjectMapper					objectMapper;

	@Override
	public void activate(ComponentContext context) throws Exception {
		configService = STServiceLocator.getConfigService();
		documentMapper = Framework.getService(IDocumentMapper.class);
		dossierMapper = Framework.getService(IDossierMapper.class);
		indexationPersistenceService = Framework.getService(IIndexationPersistenceService.class);
		parapheurService = SolonEpgServiceLocator.getParapheurService();
		objectMapper = IndexationObjectMapperProviderUtil.newIndexationDossierInstance();
		exportFolder = initExportFolder();
		temporaryFolder = initTemporaryFolder();
	}

	/**
	 * Libération de toutes les ressources, en particulier, arrêt de l'executor et attente de la fin de l'indexation
	 */
	@Override
	public void deactivate(ComponentContext context) {
		objectMapper = null;
		exportFolder = null;
		temporaryFolder = null;
		configService = null;
		documentMapper = null;
		dossierMapper = null;
		parapheurService = null;
	}

	/**
	 * Récupération des identifiants des dossiers à indexer
	 */
	@Override
	public List<String> listDossiersIdsAIndexer() throws ClientException {
		CoreSession session = null;
		try {
			TransactionHelper.startTransaction();
			session = SessionUtil.getCoreSession();
			List<String> ids = null;
			String query = String.format("SELECT * FROM %s AS d ORDER BY dos:created DESC",
					DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
			int batchLimit = getBatchLimit();
			int batchOffset = getBatchOffset();
		
			long start = System.currentTimeMillis();
			ids = QueryUtils.doQueryForIds(session, query, batchLimit, batchOffset);
			LOGGER_BATCH.info(String.format("Récupération de %d identifiants de dossiers en %.3f s.", ids.size(),
					(float) (System.currentTimeMillis() - start) / 1000));
			
			return ids;
		} finally {
			if (session != null) {
				SessionUtil.close(session);
			}
			TransactionHelper.commitOrRollbackTransaction();
		}
	}

	@Override
	public void doIndexDossier(String dossierId) {
		doTransactionIndexDossier(dossierId, false);
	}

	@Override
	public void doUnindexDossier(String dossierId) {
		doTransactionIndexDossier(dossierId, true);
	}

	private void doTransactionIndexDossier(String dossierId, boolean deletion) {
		// prépare une transaction est une session pour réaliser l'indexation
		CoreSession session = null;
		try {
			TransactionHelper.startTransaction();
			session = SessionUtil.getCoreSession();
			// le résultat de l'indexation est tracé en bdd ou loggué en cas d'échec
			// pas de retour particulier de la méthode
			doIndexDossier(session, dossierId, deletion);
		} catch (ClientException e) {
			LOGGER_BATCH.error(String.format("Erreur d'établissement de la session"), e);
		} finally {
			if (session != null) {
				SessionUtil.close(session);
			}
			TransactionHelper.commitOrRollbackTransaction();
		}
	}

	/**
	 * Indexation d'un dossier (récupération de l'objet dossier, des objets documents, combinaison et écriture json)
	 * 
	 * @throws ClientException Erreur de persistence du résultat
	 */
	@Override
	public IndexationDossierStatistics doIndexDossier(CoreSession session, String dossierId, boolean deletion) {
		IndexationDossierStatistics s = IndexationDossierStatistics.create();
		try {
			s.start();
			IndexationDossierStatistics.set(s);
			// en cas d'erreur de chargement -> ClientException
			final ElasticDossier dossier;
			if (deletion) {
				// pour une suppression, on génère un document avec comme seules propriétés
				// l'identifiant la propriété deleted à true
				dossier = new ElasticDossier();
				dossier.setUid(dossierId);
				dossier.setDeleted(true);
			} else {
				DocumentModel documentModel = session.getDocument(new IdRef(dossierId));
				dossier = exportDossier(documentModel, session);
				exportDocuments(documentModel, dossier, session);
			}
			writeDossierJson(dossier);

			// indexation réussie avec potentiellement des avertissements
			String message = s.warningMessage();
			if (message.length() == 0) {
				indexationPersistenceService.markIndexationSuccess(dossierId, deletion);
			} else {
				indexationPersistenceService.markIndexationSuccessWarning(dossierId, deletion, message.toString());
			}
		} catch (MappingException e) {
			LOGGER_BATCH.error("Erreur d'indexation du dossier {}", dossierId, e);
			IndexationDossierStatistics.error();
			indexationPersistenceService.markIndexationError(dossierId, e.getMessage());
		} catch (RuntimeException re) {
			LOGGER_BATCH.error("Erreur d'indexation du dossier {}", dossierId, re);
			IndexationDossierStatistics.error();
			indexationPersistenceService.markIndexationError(dossierId, re.getMessage());
		} catch (ClientException e) {
			if (e.getCause() instanceof NoSuchDocumentException) {
				indexationPersistenceService.markIndexationError(dossierId, "Document non trouvé");
			} else {
				indexationPersistenceService.markIndexationError(dossierId, e.getMessage());
			}
		} finally {
			IndexationDossierStatistics.unset();
		}
		return s;
	}

	/**
	 * Ecriture d'un document json {@link ElasticDossier}
	 */
	private void writeDossierJson(ElasticDossier dossier) {
		DateFormat df = new SimpleDateFormat(JSON_FILE_PATTERN);
		String timestamp = df.format(new Date());
		String filename = String.format("%s-%s.json", timestamp, dossier.getUid());
		String filenameZip = String.format("%s.zip", filename);
		String prefix = filename.substring(0, JSON_MINUTE_PART_PATTERN.length()); // pattern jusqu'à l'heure

		File prefixPath = new File(temporaryFolder.getAbsolutePath());
		File zipPath = new File(prefixPath.getPath() + File.separator + filenameZip);

		File finalPrefixPath = new File(exportFolder.getAbsolutePath() + File.separator + prefix);
		File finalPath = new File(finalPrefixPath.getPath() + File.separator + filenameZip);

		FileOutputStream output = null;
		BufferedOutputStream bos = null;
		ZipOutputStream zos = null;
		try {
			// création du répertoire cible
			if (! finalPrefixPath.exists()) {
				finalPrefixPath.mkdir();
				// on ne peut pas utiliser le booléen de mkdir car on peut avoir des création concurrentes
				if (! finalPrefixPath.exists()) {
					throw new IOException(String.format("Echec de la création du répertoire %s", finalPrefixPath.getPath()));
				}
				finalPrefixPath.setWritable(true, false);
			}
			output = new FileOutputStream(zipPath);
			bos = new BufferedOutputStream(output, 50000);
			zos = new ZipOutputStream(bos);
			ZipEntry zippedFile = new ZipEntry(filename);
			zos.putNextEntry(zippedFile);
			objectMapper.writeValue(zos, dossier);

			// déplacement atomique
			{
				boolean moved = false;
				Exception thrownException = null;
				try {
					moved = zipPath.renameTo(finalPath);
				} catch (RuntimeException e) {
					moved = false;
					thrownException = e;
				}
				if (! moved) {
					if (thrownException == null) {
						LOGGER_BATCH.error(String.format("Erreur de déplacement %s -> %s", zipPath.getPath(), finalPath.getPath()));
					} else {
						LOGGER_BATCH.error(String.format("Erreur de déplacement %s -> %s", zipPath.getPath(), finalPath.getPath()), thrownException);
					}
				}
			}
		} catch (IOException ioe) {
			LOGGER_BATCH.error(String.format("Erreur de génération du json pour le dossier %s", dossier.getUid()), ioe);
			IndexationDossierStatistics.jsonError();
		} finally {
			try {
				if (zos != null) {
					zos.flush();
					zos.close();
				}
			} catch (IOException e) {
				LOGGER_BATCH.error(String.format("Erreur de fermeture de %s", zos), e);
			}
			try {
				if (bos != null) {
					bos.flush();
					bos.close();
				}
			} catch (IOException e) {
				LOGGER_BATCH.error(String.format("Erreur de fermeture de %s", bos), e);
			}
			try {
				if (output != null) {
					output.flush();
					output.close();
				}
			} catch (IOException e) {
				LOGGER_BATCH.error(String.format("Erreur de fermeture de %s", zos), e);
			}
		}
	}

	/**
	 * Récupération du décalage sur les dossiers. On n'exporte pas les X premiers dossiers retournés par la requête.
	 * 
	 * @return un int supérieur ou égal à 0 (pas de décalage)
	 */
	private int getBatchOffset() {
		try {
			return Integer.parseInt(configService.getValue(
					SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_OFFSET));
		} catch (Exception e) {
		}
		return 0; // Pas de décalage
	}

	/**
	 * Récupération du nombre total de dossiers souhaités sur la globalité du batch.
	 * 
	 * @return un int compris entre 0 et Integer.MAX_VALUE
	 */
	private int getBatchLimit() {
		try {
			return Integer.parseInt(configService.getValue(
					SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_LIMITE));
		} catch (Exception e) {
		}
		return Integer.MAX_VALUE; // Pas de limite
	}

	/**
	 * Initialisation du répertoire d'export pour l'export courant.
	 */
	private File initExportFolder() {
		// Désignation du répertoire
		String exportRootFolder = configService.getValue(
				SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_DESTINATION);
		return initFolder(exportRootFolder);
	}

	/**
	 * Initialisation du répertoire temporaire pour les exports.
	 */
	private File initTemporaryFolder() {
		// Désignation du répertoire
		String temporaryRootFolder = configService.getValue(
				SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_DESTINATION_TEMPORAIRE);
		return initFolder(temporaryRootFolder);
	}

	/**
	 * Initialisation du répertoire d'export.
	 */
	private File initFolder(String folder) {
		if (!folder.endsWith(File.separator)) {
			folder = folder + File.separator;
		}

		File exportFolder = new File(folder);
		if (! exportFolder.exists()) {
			try {
				if (! exportFolder.mkdirs()) {
					throw new IllegalStateException(
							String.format("Création du répertoire %s impossible", exportFolder.getAbsolutePath()));
				}
			} catch (SecurityException se) {
				throw new IllegalStateException(
						String.format("Création du répertoire %s impossible", exportFolder.getAbsolutePath()), se);
			}
		} else if (! exportFolder.isDirectory()) {
			throw new IllegalStateException(
					String.format("Le chemin cible %s existe déjà et n'est pas un répertoire", exportFolder.getAbsolutePath()));
		}

		return exportFolder;
	}

	/**
	 * Mapping d'un {@link DocumentModel} en {@link ElasticDossier}
	 */
	private ElasticDossier exportDossier(DocumentModel documentModel, CoreSession session) throws MappingException {
		ElasticDossier exportableDossier = null;
		exportableDossier = dossierMapper.from(documentModel, session);
		return exportableDossier;
	}

	/**
	 * Constitution de la liste des {@link ElasticDocument} pour un {@link DocumentModel} de type dossier
	 */
	private void exportDocuments(DocumentModel documentModel, ElasticDossier dossier, CoreSession session) throws MappingException {
		Map<String, ElasticDocument> documents = Maps.newHashMap();
		// Parapheur
		exportParapheurDocuments(documentModel, documents, session);
		// Fond de dossier
		exportFondDeDossierDocuments(documentModel, documents, session);
		
		dossier.getDocuments().addAll(documents.values());
	}

	/**
	 * Export des documents parapheur pour un {@link DocumentModel} de type dossier
	 */
	private void exportParapheurDocuments(DocumentModel documentModel,
			Map<String, ElasticDocument> documents, CoreSession session) throws MappingException {
		List<ParapheurFolder> parapheurFolderList;
		try {
			String parapheurDosId = (String) documentModel.getPropertyValue(ElasticDossier.DOS_ID_DOCUMENT_PARAPHEUR);
			parapheurFolderList = parapheurService.getChildrenParapheurNode(parapheurDosId, session);
		} catch (ClientException e) {
			throw new MappingException(String.format("Erreur de récupération du parapheur sur le dossier %s", documentModel.getId()), e);
		}
		String path = PATH_PARAPHEUR;
		for (ParapheurFolder folder : parapheurFolderList) {
			browseAndExportDocuments(folder.getDocument(), documents, session, path);
		}
	}

	/**
	 * Export des documents fond de dossier pour un {@link DocumentModel} de type dossier
	 */
	private void exportFondDeDossierDocuments(DocumentModel documentModel, Map<String, ElasticDocument> documents,
			CoreSession session) throws MappingException {
		List<DocumentModel> documentModelList = getAllFddFolder(session, documentModel);
		String path = PATH_FOND_DE_DOSSIER;
		for (DocumentModel childDocumentModel : documentModelList) {
			browseAndExportDocuments(childDocumentModel, documents, session, path);
		}
	}

	/**
	 * Récupération des répertoires du fond de dossier
	 */
	private List<DocumentModel> getAllFddFolder(final CoreSession session, DocumentModel documentModel) throws MappingException {
		// on récupère tous les répertoires du fond de dossier correspondant au type d'acte choisi
		final StringBuilder query = new StringBuilder("SELECT l.ecm:uuid AS id FROM ");
		query.append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE);
		query.append(" AS l WHERE isChildOf(l.ecm:uuid, select r.ecm:uuid from ");
		query.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
		query.append(" AS r WHERE r.ecm:uuid = ?) = 1");

		try {
			final List<DocumentModel> docList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE, query.toString(),
					new String[] { documentModel.getId() });
	
			return docList;
		} catch (ClientException e) {
			throw new MappingException(String.format(
					"Erreur de détermination de répertoires Fond de dossier pour le dossier %s",
					documentModel.getId()
				), e);
		}
	}

	/**
	 * Récupère les enfants du répertoire donné en paramètre. Pour chacun d'eux, si c'est un répertoire, on fait un
	 * appel récursif sur la même méthode. Si ce n'est pas un répertoire, on exporte le document.
	 */
	private void browseAndExportDocuments(DocumentModel folderDocModel, Map<String, ElasticDocument> documents,
			CoreSession session, String path) throws MappingException {
		DocumentModelList documentModelList;
		try {
			documentModelList = session.getChildren(new IdRef(folderDocModel.getId()));
		} catch (ClientException e) {
			throw new MappingException(String.format("Erreur de récupération des noeuds enfants de %s", folderDocModel.getId()), e);
		}
		
		for (DocumentModel documentModel : documentModelList) {
			if (documentModel.isFolder()) {
				// C'est un folder : appel récursif
				browseAndExportDocuments(documentModel, documents, session,
						path + File.separator + documentModel.getName());
			} else {
				List<DocumentModel> documentVersionsList;
				try {
					documentVersionsList = session.getVersions(documentModel.getRef());
				} catch (ClientException e) {
					throw new MappingException(String.format("Erreur de récupération des version du document %s", documentModel.getId()), e);
				}
				Collections.reverse(documentVersionsList);
				boolean lastVersion = true;
				for (DocumentModel documentVersion : documentVersionsList) {
					mapDocument(folderDocModel, documents, session, path, documentModel, documentVersion, lastVersion);
					// seul le premier document est la dernière version
					lastVersion = false;
				}
			}
		}
	}

	/**
	 * Mapping du {@link DocumentModel} d'un fichier associé à un dossier en {@link ElasticDocument}
	 */
	private void mapDocument(DocumentModel folderDocModel, Map<String, ElasticDocument> documents, CoreSession session,
			String path, DocumentModel documentModel, DocumentModel documentVersion, boolean lastVersion) {
		IndexationDossierStatistics.document();
		try {
			if (documents.containsKey(documentVersion.getId())) {
				IndexationDossierStatistics.documentDoublons();
			} else {
				// on définit les méta-données et on ajoute le document
				ElasticDocument exportableDocument = documentMapper.from(documentVersion, path
						+ File.separator + folderDocModel.getName(), session, lastVersion);
				documents.put(documentVersion.getId(), exportableDocument);
				
				// puis on tente l'extraction. Si le document n'est pas extrait, on aura quand même
				// les métadonnées indexées
				try {
					documentMapper.mapFileProperties(documentVersion, exportableDocument, lastVersion, session);
					IndexationDossierStatistics.documentSuccess();
				} catch (DocumentEmptyException e) {
					LOGGER_EMPTY.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC, e);
					IndexationDossierStatistics.documentEmpty();
				} catch (DocumentTooLargeException e) {
					LOGGER_LARGE.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC, e);
					IndexationDossierStatistics.documentTooLarge();
				} catch (DocumentNotParsedException e) {
					LOGGER_PARSE.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC, e);
					IndexationDossierStatistics.documentNotParsed();
				} catch (DocumentMissingException e) {
					LOGGER_MISSING.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC, e);
					IndexationDossierStatistics.documentMissing();
				}
			}
		} catch (RuntimeException e) {
			LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC, e);
			IndexationDossierStatistics.documentError();
		} catch (MappingException e) {
			LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC, e);
			IndexationDossierStatistics.documentError();
		} catch (ClientException e) {
			LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC, e);
			IndexationDossierStatistics.documentError();
		}
	}

}
