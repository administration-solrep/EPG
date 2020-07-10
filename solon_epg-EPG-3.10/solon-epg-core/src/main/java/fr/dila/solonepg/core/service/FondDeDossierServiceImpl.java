package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.Path;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.VersioningOption;
import org.nuxeo.ecm.core.api.impl.blob.ByteArrayBlob;
import org.nuxeo.ecm.core.versioning.VersioningService;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFile;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.CorbeilleService;
import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.ss.api.exception.SSException;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.tree.SSTreeFile;
import fr.dila.ss.api.tree.SSTreeFolder;
import fr.dila.ss.api.tree.SSTreeNode;
import fr.dila.ss.core.service.SSFondDeDossierServiceImpl;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.JetonService;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.sword.xsd.solon.epg.TypeModification;

/**
 * Implémentation service fond de dossier pour epg
 * 
 */
public class FondDeDossierServiceImpl extends SSFondDeDossierServiceImpl implements FondDeDossierService {

	private static final long		serialVersionUID	= 8292406959483724550L;

	private static final Log		LOG					= LogFactory.getLog(FondDeDossierServiceImpl.class);
	/**
	 * Logger formalisé en surcouche du logger apache/log4j
	 */
	private static final STLogger	LOGGER				= STLogFactory.getLog(FondDeDossierServiceImpl.class);
	
	/**
	 * La requête remonte les fdd files du dossier en param <br>
	 * SELECT id FROM FileSolonEpg WHERE filetypeId=4 AND isChildOf(f.id, select d.id from Dossier where d.ecm:uuid = ?) = 1 AND f.ecm:currentLifeCycleState != 'deleted'
	 */
	private static final String FDD_FILES_IN_DOSSIER_QUERY = "SELECT f.ecm:uuid AS id FROM "
			+ SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE
			+ " AS f WHERE f." + DossierSolonEpgConstants.FILE_SOLON_EPG_PREFIX + ":" + DossierSolonEpgConstants.FILE_SOLON_EPG_FILETYPE_ID
			+ " = " + VocabularyConstants.FILETYPEID_FONDDOSSIER + " AND isChildOf(f.ecm:uuid, select d.ecm:uuid from Dossier AS d WHERE d.ecm:uuid = ?) = 1"
			+" AND f." + NOT_DELETED;
	
	private static final String FDD_FILES_IN_FOLDER_QUERY = "SELECT f.ecm:uuid as id FROM FileSolonEpg AS f WHERE f.filepg:filetypeId = 4 " 
							+ "AND f.ecm:currentLifeCycleState != 'deleted' " 
							+ "AND isChildOf(f.ecm:uuid, SELECT r.ecm:uuid FROM FondDeDossierFolder AS r WHERE r.dc:title = ? " 
							+ "AND isChildOf(r.ecm:uuid, select d.ecm:uuid from Dossier AS d WHERE d.ecm:uuid = ?) = 1) = 1";

	/**
	 * Default constructor
	 */
	public FondDeDossierServiceImpl() {
		super();
	}

	@Override
	public void createAndFillFondDossier(final Dossier dossier, final CoreSession session) throws ClientException {
		// récupération du type d'acte
		final String typeActe = dossier.getTypeActe();
		if (typeActe == null) {
			throw new ClientException("Le type d'acte du dossier est nul !");
		}
		// récupération des formats autorisés
		List<String> formatsAutorises = null;
		final FondDeDossierModelService modelService = SolonEpgServiceLocator.getFondDeDossierModelService();
		formatsAutorises = modelService.getFormatsAutorises(session);
		// création du fond de dossier
		final DocumentModel fondDeDossierDoc = createFondDossierDocument(dossier.getDocument(), session,
				formatsAutorises);
		final DocumentRef fondDossierRef = fondDeDossierDoc.getRef();
		// on définit l'id du parapheur depuis le dossier
		dossier.setDocumentFddId(fondDeDossierDoc.getId());
		try {
			// récupération du modèle de fond de dossier associé au document
			final DocumentModel fondDossierModel = modelService.getFondDossierModelFromTypeActe(session, typeActe);
			// récupération des répertoires racines du fondDossier
			// parcourt des répertoires racines et recopie des répertoires
			LOG.debug("Début de la copie du modèle " + typeActe);
			final List<DocumentRef> childrenRefs = session.getChildrenRefs(fondDossierModel.getRef(), null);
			session.copy(childrenRefs, fondDossierRef);
			LOG.debug("Fin de la copie du modèle " + typeActe);
		} catch (final ClientException e) {
			throw new ClientException("Erreur lors de de l'appel de la méthode getFondDossierModelFromTypeActe", e);
		}
	}

	@Override
	public void updateFondDossierTree(final Dossier dossier, final String newTypeActe, final CoreSession session)
			throws ClientException {
		// récupération des répertoires courants du fond de dossier courant
		final List<DocumentModel> fddCurrentFolderList = getAllFddFolder(session, dossier);

		// récupération de la liste des répertoires du modèle du fond de dossier correspondant au nouveau type d'acte
		final FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator
				.getFondDeDossierModelService();
		final List<DocumentModel> fddModelFolderList = fondDeDossierModelService
				.getListFondDossierFolderModelFromTypeActe(session, newTypeActe);

		// 1) on parcourt les répertoires du fond de dossier courant afin de supprimer ceux qui sont déjà présent

		// Liste des chemins des répertoires du modèle du fond de dossier
		final Set<String> fddModelFolderPathNameList = new HashSet<String>();
		// Liste des chemins des répertoires existants du fond de dossier courant qui ne vont pas être supprimés
		final Set<String> fddCurrentFolderPathNameList = new HashSet<String>();
		// Map contenant les DocumentRef et les chemins des répertoires existants du fond de dossier courant qui ne vont
		// pas être supprimés
		final Map<String, DocumentRef> fddCurrentFolderDocRefList = new HashMap<String, DocumentRef>();

		// on parcourt les répertoire du modèle de fond de dossier et on enregistre leurs chemins en enlevant la partie
		// commune aux autres répertoires
		for (final DocumentModel fddModelFolder : fddModelFolderList) {
			fddModelFolderPathNameList.add(getFddFolderModelIdentificationPath(fddModelFolder));
		}

		// liste des références de répertoire à supprimer
		final List<DocumentRef> docRefFddFolderToRemove = new ArrayList<DocumentRef>();

		// on parcourt les répertoires courants du fond de dossier
		for (final DocumentModel currentModelRep : fddCurrentFolderList) {
			// on récupère le chemin du répertoire dans l'arborescence en enlevant la partie commune aux autres
			// répertoires
			final String docPath = currentModelRep.getPathAsString();
			int lastIndex = docPath.lastIndexOf(SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME);
			final String docPathName = docPath.substring(lastIndex
					+ SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME.length());

			// si ce chemin est pas présent dans la liste on note son chemin dans la liste des chemins de répertoires
			// conservés
			if (fddModelFolderPathNameList.contains(docPathName)) {
				fddCurrentFolderPathNameList.add(docPathName);
				fddCurrentFolderDocRefList.put(docPathName, currentModelRep.getRef());
			} else {
				// sinon ce chemin n'est pas présent dans la liste on regarde le chemin de son répertoire parent
				lastIndex = docPathName.lastIndexOf("/");
				final String docParentPath = docPathName.substring(0, lastIndex);

				// si le chemin de son répertoire parent est dans la liste, on l'ajoute dans la liste des répertoires à
				// supprimer
				if (fddModelFolderPathNameList.contains(docParentPath)) {
					docRefFddFolderToRemove.add(currentModelRep.getRef());
				}
			}
		}

		// on supprime les répertoires
		final DocumentRef[] docsRef = docRefFddFolderToRemove.toArray(new DocumentRef[docRefFddFolderToRemove.size()]);
		LOGGER.info(session, EpgLogEnumImpl.DEL_REP_FDD_TEC, docsRef);
		for (final DocumentRef docRef : docRefFddFolderToRemove) {
			session.removeDocument(docRef);
		}

		// 2) on parcourt les répertoires du fond de dossier correspondant au nouveau type d'acte afin de les ajouter

		for (final DocumentModel modelFolder : fddModelFolderList) {
			final String docPath = getFddFolderModelIdentificationPath(modelFolder);

			// si le chemin du répertoire n'existe pas dans la liste on regarde le chemin de son répertoire parent
			if (!fddCurrentFolderPathNameList.contains(docPath)) {
				int lastIndex = docPath.lastIndexOf('/');
				String docParentPath = docPath.substring(0, lastIndex);

				// si le chemin de son répertoire parent est dans la liste, on ajoute le nouveau répertoire et ses fils
				if (fddCurrentFolderPathNameList.contains(docParentPath)) {
					session.copy(modelFolder.getRef(), fddCurrentFolderDocRefList.get(docParentPath), null);
				}
			}
		}

		// on enregistre les modifications
		session.save();
	}

	@Override
	public void deleteFile(final CoreSession session, final DocumentModel document, final DocumentModel dossierDoc)
			throws ClientException {
		super.deleteFile(session, document);
		journalisationAction(SolonEpgEventConstant.ACTION_DELETE, document.getType(), session, dossierDoc,
				document.getTitle(), session.getDocument(document.getParentRef()));
	}

	@Override
	public void deleteFileWithType(final CoreSession session, final DocumentModel document,
			final DocumentModel dossierDoc, final String fileType) throws ClientException {
		super.deleteFileWithType(session, document, fileType);
		journalisationAction(SolonEpgEventConstant.ACTION_DELETE, document.getType(), session, dossierDoc,
				document.getTitle(), session.getDocument(document.getParentRef()));
	}

	@Override
	public void restoreToPreviousVersion(final CoreSession session, final String currentDocId,
			final DocumentModel dossierDoc) throws ClientException {
		super.restoreToPreviousVersion(session, currentDocId, dossierDoc);
		DocumentRef currentDocRef = new IdRef(currentDocId);
		DocumentModel currentFileDoc = session.getDocument(currentDocRef);
		journalisationAction(SolonEpgEventConstant.ACTION_UPDATE, currentFileDoc.getType(), session, dossierDoc,
				currentFileDoc.getTitle(), session.getDocument(currentFileDoc.getParentRef()));
	}

	@Override
	public void updateFile(final CoreSession session, DocumentModel fichier, final String fileName, final Blob blob,
			final NuxeoPrincipal currentUser, final DocumentModel dossierDocument) throws ClientException {

		// force unlock liveEdit
		STServiceLocator.getSTLockService().unlockDocUnrestricted(session, fichier);

		// on récupère le nom du fichier
		final String docName = StringUtil.removeSpacesAndAccent(fileName);
		// set document name
		DublincoreSchemaUtils.setTitle(fichier, fileName);
		// set document file properties
		fichier.setProperty(STSchemaConstant.FILE_SCHEMA, STSchemaConstant.FILE_FILENAME_PROPERTY, fileName);
		fichier.setProperty(STSchemaConstant.FILE_SCHEMA, STSchemaConstant.FILE_CONTENT_PROPERTY, blob);
		// set entite
		fichier.setProperty(DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
				DossierSolonEpgConstants.FILE_SOLON_EPG_ENTITE_PROPERTY, getEntite(currentUser));
		// incrementation du numero de version
		fichier.putContextData(VersioningService.VERSIONING_OPTION, VersioningOption.MAJOR);
		// create document in session
		session.move(fichier.getRef(), fichier.getParentRef(), docName);
		fichier = session.saveDocument(fichier);
		// commit modification
		session.save();
		// récupération du répertoire parent
		final DocumentModel repertoireParent = session.getParentDocument(fichier.getRef());
		// journalisation de l'action dans les logs
		journalisationAction(SolonEpgEventConstant.ACTION_UPDATE, fichier.getType(), session, dossierDocument,
				fileName, repertoireParent);
	}

	/**
	 * Journalise l'action de création/modification de fichier.
	 * 
	 * @param actionType
	 * @param documentType
	 * @param session
	 * @param dossierDocument
	 * @param docName
	 * @param repertoireParent
	 * @throws ClientException
	 */
	private void journalisationAction(final String actionType, final String documentType, final CoreSession session,
			final DocumentModel dossierDocument, final String docName, final DocumentModel repertoireParent,
			final String typeActe) throws ClientException {
		if (org.apache.commons.lang.StringUtils.isNotEmpty(typeActe)) {
			// LOG des actions d'administration (modèles de fond de dossier et de parapheur)
			if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_FOLDER_DOCUMENT_TYPE)) {
				// LOG pour les modèles de répertoire du fond de dossier
				if (actionType.equals(SolonEpgEventConstant.ACTION_CREATE)) {
					journalisationAdministrationAction(session, SolonEpgEventConstant.CREATE_MODELE_FDD_EVENT, docName,
							typeActe);
				} else if (actionType.equals(SolonEpgEventConstant.ACTION_UPDATE)) {
					journalisationAdministrationAction(session, SolonEpgEventConstant.UPDATE_MODELE_FDD_EVENT, docName,
							typeActe);
				} else if (actionType.equals(SolonEpgEventConstant.ACTION_DELETE)) {
					journalisationAdministrationAction(session, SolonEpgEventConstant.DELETE_MODELE_FDD_EVENT, docName,
							typeActe);
				}
			} else if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
				// LOG pour les modèles de répertoire du fond de dossier
				if (actionType.equals(SolonEpgEventConstant.ACTION_DELETE)) {
					journalisationAdministrationAction(session, SolonEpgEventConstant.DELETE_MODELE_FDD_EVENT, docName,
							typeActe);
				}
			}

			return;
		}

		String path = "";
		if (repertoireParent != null) {
			path = repertoireParent.getPathAsString();
		}

		String eventCategory = "Category not found";

		if (path.contains(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG)) {
			eventCategory = SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG;
		} else if (path.contains(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR)) {
			eventCategory = SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR;
		} else if (path.contains(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG)) {
			eventCategory = SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG;
		} else if (path.contains(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER)) {
			eventCategory = SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER;
		}
		
		String repertoireParentTitle = "";
		if(repertoireParent!=null) {
			repertoireParentTitle = repertoireParent.getTitle();
		}

		// journalisation de l'action dans les logs en fonction du type d'action et du type de document
		if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)) {
			// LOG pour les fichier du fond de dossier
			if (actionType.equals(SolonEpgEventConstant.ACTION_CREATE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.CREATE_FILE_FDD,
						SolonEpgEventConstant.COMMENT_CREATE_FILE_FDD + docName + "' dans le répertoire '"
								+ repertoireParentTitle + "'", eventCategory);
			} else if (actionType.equals(SolonEpgEventConstant.ACTION_UPDATE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.UPDATE_FILE_FDD,
						SolonEpgEventConstant.COMMENT_UPDATE_FILE_FDD + docName + "' dans le répertoire '"
								+ repertoireParentTitle + "'", eventCategory);
			} else if (actionType.equals(SolonEpgEventConstant.ACTION_DELETE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.DELETE_FILE_FDD,
						SolonEpgEventConstant.COMMENT_DELETE_FILE_FDD + docName + "' dans le répertoire '"
								+ repertoireParentTitle + "'", eventCategory);
			}
		} else if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
			// LOG pour les répertoire du fond de dossier
			if (actionType.equals(SolonEpgEventConstant.ACTION_CREATE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.CREATE_FOLDER_FDD,
						SolonEpgEventConstant.COMMENT_CREATE_FOLDER_FDD, eventCategory);
			} else if (actionType.equals(SolonEpgEventConstant.ACTION_UPDATE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.UPDATE_FOLDER_FDD,
						SolonEpgEventConstant.COMMENT_UPDATE_FOLDER_FDD + docName, eventCategory);
			} else if (actionType.equals(SolonEpgEventConstant.ACTION_DELETE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.DELETE_FOLDER_FDD,
						SolonEpgEventConstant.COMMENT_DELETE_FOLDER_FDD + docName, eventCategory);
			}
		}
	}

	private void journalisationAction(final String actionType, final String documentType, final CoreSession session,
			final DocumentModel dossierDocument, final String docName, final DocumentModel repertoireParent)
			throws ClientException {
		journalisationAction(actionType, documentType, session, dossierDocument, docName, repertoireParent, null);
	}

	/**
	 * Journalise un evenement d'un dossier
	 * 
	 * @param session
	 * @param docModel
	 * @param eventName
	 * @param commentaire
	 * @param categorie
	 * @throws ClientException
	 */
	private void journalisationAction(final CoreSession session, final DocumentModel docModel, final String eventName,
			final String commentaire, final String categorie) throws ClientException {
		final JournalService journalService = STServiceLocator.getJournalService();
		journalService.journaliserAction(session, docModel, eventName, commentaire, categorie);
	}

	/**
	 * Journalise un evenement de l'espace d'administration (événement des modèles de fond de dossier et de parapheur)
	 * 
	 * @param session
	 * @param eventName
	 * @param commentaire
	 * @param categorie
	 * @throws ClientException
	 */
	private void journalisationAdministrationAction(final CoreSession session, final String eventName,
			final String docName, final String typeActe) throws ClientException {
		final JournalService journalService = STServiceLocator.getJournalService();

		final StringBuilder commentaire = new StringBuilder();
		if (SolonEpgEventConstant.CREATE_MODELE_FDD_EVENT.equals(eventName)) {
			commentaire.append("Création d'un nouveau répertoire dans les modèles de fond de dossier '");
		} else if (SolonEpgEventConstant.UPDATE_MODELE_FDD_EVENT.equals(eventName)) {
			commentaire.append("Mise à jour d'un répertoire dans les modèles de fond de dossier '");
		} else if (SolonEpgEventConstant.DELETE_MODELE_FDD_EVENT.equals(eventName)) {
			commentaire.append("Suppression du répertoire dans les modèles de fond de dossier '");
		}
		if (!SolonEpgEventConstant.CREATE_MODELE_FDD_EVENT.equals(eventName)) {
			commentaire.append(docName).append("'");
		}
		commentaire.append(" pour le type d'acte ").append(typeActe);

		journalService.journaliserActionAdministration(session, eventName, commentaire.toString());
	}

	/**
	 * récupère le chemin du répertoire dans l'arborescence en enlevant la partie commune aux autres répertoires du
	 * Modèle de Fond de Dossier
	 * 
	 * @param dddFolderModel
	 * @return le chemin du répertoire dans l'arborescence en enlevant la partie commune aux autres répertoires du
	 *         Modèle de Fond de Dossier
	 */
	protected String getFddFolderModelIdentificationPath(final DocumentModel fddFolderModel) {
		String docPath = fddFolderModel.getPathAsString();
		int lastIndex = docPath.lastIndexOf("ModeleFondDeDossier");
		docPath = docPath.substring(lastIndex);
		lastIndex = docPath.indexOf("/");
		return docPath.substring(lastIndex);
	}

	/**
	 * création du document parapheur à partir du dossier
	 * 
	 * @param dossier
	 * @param session
	 * @return ParapheurInstance
	 * @throws ClientException
	 */
	private DocumentModel createFondDossierDocument(final DocumentModel dossier, final CoreSession session,
			final List<String> formatsAutorises) throws ClientException {
		try {
			LOG.debug("creation du document" + SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME);

			final DocumentModel newModel = session.createDocumentModel(dossier.getPath().toString(),
					SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_DOCUMENT_TYPE);

			// On affecte un titre par défaut au parapheur
			final String numeroNor = (String) dossier.getProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
					DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY);
			if (numeroNor == null) {
				DublincoreSchemaUtils.setTitle(newModel, SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME);
			}
			final String title = SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME + " " + numeroNor;
			DublincoreSchemaUtils.setTitle(newModel, title);

			// on affecte les format de fichier autorises
			newModel.setProperty(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_DOCUMENT_SCHEMA,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FORMAT_AUTORISE_PROPERTY, formatsAutorises);

			// sauvegarde du parapheur
			final DocumentModel fondDossierModel = session.createDocument(newModel);

			return fondDossierModel;
		} catch (final ClientException e) {
			throw new ClientException("erreur lors de la creation du document ", e);
		}
	}

	@Override
	public List<FondDeDossierFolder> getAllVisibleRootFolder(final CoreSession session,
			final DocumentModel dossierModel, final NuxeoPrincipal currentUser) throws ClientException {
		final Dossier dossier = dossierModel.getAdapter(Dossier.class);
		final DocumentModel fondDeDossierDoc = dossier.getFondDeDossier(session).getDocument();

		// on récupère l'identifiant du ministère d'appartenance de l'acte
		final String idMinistere = dossier.getMinistereAttache();
		// on récupère les droits de l'utilisateur
		List<String> groups = new ArrayList<String>();
		Set<String> ministereIds = new HashSet<String>();
		if (currentUser != null) {
			groups = currentUser.getGroups();
			// on récupère le ministère d'appartenance de l'utilisateur
			final SSPrincipal ssPrincipal = (SSPrincipal) currentUser;
			ministereIds = ssPrincipal.getMinistereIdSet();
		}

		final List<FondDeDossierFolder> folders = getChildrenFolder(session, fondDeDossierDoc);
		final List<FondDeDossierFolder> foldersReturn = new ArrayList<FondDeDossierFolder>(folders);
		for (final FondDeDossierFolder folder : folders) {
			if (!isRepertoireVisible(idMinistere, folder.getDocument(), ministereIds, groups)) {
				foldersReturn.remove(folder);
			}
		}
		return foldersReturn;
	}

	@Override
	public List<FondDeDossierFolder> getAllRootFolder(final CoreSession session, final Dossier dossierDoc)
			throws ClientException {
		final DocumentModel fondDossier = dossierDoc.getFondDeDossier(session).getDocument();
		return getChildrenFolder(session, fondDossier);
	}

	@Override
	public List<DocumentModel> storeAllChildren(final CoreSession session,
			final List<FondDeDossierFolder> repertoireVisibleList) throws ClientException {
		final List<String> repertoireGroupsVisibleIdList = new ArrayList<String>();
		for (final FondDeDossierFolder repertoireVisible : repertoireVisibleList) {
			repertoireGroupsVisibleIdList.add(repertoireVisible.getId());
		}

		// récupération des documents du fond de dossier en tenant compte des droits de visibilité
		return getAllVisibleFddDocument(session, repertoireGroupsVisibleIdList);
	}

	/**
	 * 
	 * 
	 * @param dossier
	 * @param repList
	 * @param ministereIds
	 *            identifiant de l'utilisateur
	 * @param groups
	 *            fonctions unitaires de l'utilisateur
	 * @return
	 */
	protected boolean isRepertoireVisible(final String idMinistereRattachement, final DocumentModel repList,
			final Set<String> userMinistereIds, final List<String> userGroups) {
		// pour chaque type de répertoire on signale si l'utilisateur a les droits de visibilité sur le répertoire
		Boolean isVisible = false;
		final String repname = repList.getName();
		if (repname.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR)) {
			// si l'utilisateur fait parti du ministère d'appartenance de l'acte, il peut visualiser le répertoire
			isVisible = userMinistereIds.contains(idMinistereRattachement);
		} else if (repname.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG)) {
			// si l'utilisateur fait parti d'un profil SGG, il peut visualiser le répertoire
			isVisible = userGroups.contains(SolonEpgBaseFunctionConstant.FOND_DOSSIER_REPERTOIRE_SGG);
		} else if (repname
				.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG)) {
			// si l'utilisateur fait parti d'un profil SGG ou si l'utilisateur fait parti du ministère de rattachement,
			// il peut visualiser le répertoire
			isVisible = userMinistereIds.contains(idMinistereRattachement)
					|| userGroups.contains(SolonEpgBaseFunctionConstant.FOND_DOSSIER_REPERTOIRE_SGG);
		} else if (repname.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER)) {
			// répertoire accessible à tous les utilisateurs
			isVisible = true;
		}
		return isVisible;
	}

	@Override
	public List<FondDeDossierFolder> getChildrenFondDeDossierNodeFromdocList(final String documentRef,
			final CoreSession session, final List<DocumentModel> fddChildParentIdList) throws ClientException {
		try {
			return getChildrenFolder(session, new IdRef(documentRef));
		} catch (final ClientException e) {
			LOG.warn("erreur lors de la recuperation de repertoire fils du document", e);
			throw new ClientException("erreur lors de la recuperation de repertoire fils du document", e);
		}
	}

	@Override
	public List<DocumentModel> getFddDocuments(final CoreSession session, final Dossier dossier) throws ClientException {
		final DocumentModel fddDoc = dossier.getFondDeDossier(session).getDocument();
		final List<DocumentModel> docs = new ArrayList<DocumentModel>();

		// récupération des répertoires racine du fond de dossier
		final List<DocumentModel> folders = session.getChildren(fddDoc.getRef());
		final List<String> folderVisibleIds = new ArrayList<String>();

		// on récupère les ministères d'appartenances et les fonction unitaires de l'utilisateur de l'utilisateur
		final NuxeoPrincipal currentUser = (NuxeoPrincipal) session.getPrincipal();
		final SSPrincipal ssPrincipal = (SSPrincipal) currentUser;
		final Set<String> ministereIds = ssPrincipal.getMinistereIdSet();
		final List<String> groups = ssPrincipal.getGroups();
		final String ministereAttache = dossier.getMinistereAttache();
		for (final DocumentModel folder : folders) {
			if (isRepertoireVisible(ministereAttache, folder, ministereIds, groups)) {
				// ajout du répertoire à la liste
				docs.add(folder);
				// ajout de l'id du répertoire à la liste des identifiants de répertoires visibles
				folderVisibleIds.add(folder.getId());
			}
		}

		return getAllVisibleFddDocument(session, folderVisibleIds);

	}

	@Override
	public List<DocumentModel> getPublicFddFiles(final CoreSession session, final Dossier dossier)
			throws ClientException {
		// récupération du répertoire "Répertoire accessible à tous les utilisateurs" à partir du chemin
		Path path = dossier.getDocument().getPath();
		path = path.append(SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME).append(
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER);
		final DocumentModel repertoirePublicFdd = session.getDocument(new PathRef(path.toString()));
		// on récupère tous les fichiers du répertoire
		return getFilesFromFondDossierFolder(session, dossier, repertoirePublicFdd);
	}

	@Override
	public List<DocumentModel> getRapportPresentationFiles(final CoreSession session, final Dossier dossier)
			throws ClientException {
		// récupération du répertoire "Rapport de Présentation" à partir du chemin
		Path path = dossier.getDocument().getPath();
		path = path.append(SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME)
				.append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER)
				.append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE)
				.append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RAPPORT_PRESENTATION);
		final DocumentModel rapportPresentationFolder = session.getDocument(new PathRef(path.toString()));
		// récupération des fichiers du répertoire
		return getFilesFromFondDossierFolder(session, dossier, rapportPresentationFolder);
	}

	@Override
	public List<DocumentModel> getEpreuvesFiles(final CoreSession session, final Dossier dossier)
			throws ClientException {
		// récupération du répertoire "Epreuves" dans le fond de dossier vu qu'on ne sait pas où il peut être, on le
		// cherche dans toute l'arbo
		try {
			List<DocumentModel> repertoiresFDD = getAllFddFolder(session, dossier);

			DocumentModel epreuveFolder = null;
			for (DocumentModel fddf : repertoiresFDD) {
				FondDeDossierFolder fddRepert = fddf.getAdapter(FondDeDossierFolder.class);

				if (SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES.equals(fddRepert.getName())
						|| SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES.equals(fddRepert
								.getTitle())) {
					epreuveFolder = fddf;
				}
			}
			// Si on a trouvé un répertoire, on va chercher les fichiers
			if (epreuveFolder != null) {
				// récupération des fichiers du répertoire
				return getFilesFromFondDossierFolder(session, dossier, epreuveFolder);
			}
		} catch (Exception e) {
			LOGGER.warn(STLogEnumImpl.FAIL_GET_FILE_FONC);
		}
		return new ArrayList<DocumentModel>();
	}
	
	@Override
	public List<DocumentModel> getAvisCEFiles(final CoreSession session, final Dossier dossier)
			throws ClientException {
		// récupération du répertoire "Avis du Conseil d'Etat" dans le fond de dossier
		try {
			List<DocumentModel> repertoiresFDD = getAllFddFolder(session, dossier);

			DocumentModel avisCEFolder = null;
			for (DocumentModel fddf : repertoiresFDD) {
				FondDeDossierFolder fddRepert = fddf.getAdapter(FondDeDossierFolder.class);

				if (SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE.equals(fddRepert.getName())
						|| SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE.equals(fddRepert
								.getTitle())) {
					avisCEFolder = fddf;
				}
			}
			// Si on a trouvé un répertoire, on va chercher les fichiers
			if (avisCEFolder != null) {
				// récupération des fichiers du répertoire
				return getFilesFromFondDossierFolder(session, dossier, avisCEFolder);
			}
		} catch (Exception e) {
			LOGGER.warn(STLogEnumImpl.FAIL_GET_FILE_FONC);
		}
		return new ArrayList<DocumentModel>();
	}

	/**
	 * Renvoie les fichiers du répertoire du fond de dossier donné.
	 * 
	 * @param session
	 *            session
	 * @param dossier
	 *            dossier
	 * @param folder
	 *            répertoire dont on récupère les fichiers
	 * @return les fichiers du répertoire du fond de dossier donné.
	 * @throws ClientException
	 */
	public List<DocumentModel> getFilesFromFondDossierFolder(final CoreSession session, final Dossier dossier,
			final DocumentModel folder) throws ClientException {
		// on récupère tous les documents fils du répertoire
		final StringBuilder query = new StringBuilder("SELECT l.ecm:uuid AS id FROM ");
		query.append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE);
		query.append(" AS l WHERE isChildOf(l.ecm:uuid, select r.ecm:uuid from ");
		query.append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE);
		query.append(" AS r WHERE r.ecm:uuid = ? ) = 1 and l." + NOT_DELETED);

		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE, query.toString(),
				new String[] { folder.getId() });
	}

	@Override
	public List<DocumentModel> getAllFddFolder(final CoreSession session, final Dossier dossier) throws ClientException {
		// on récupère tous les répertoires du fond de dossier correspondant au type d'acte choisi
		final StringBuilder query = new StringBuilder("SELECT l.ecm:uuid AS id FROM ");
		query.append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE);
		query.append(" AS l WHERE isChildOf(l.ecm:uuid, select r.ecm:uuid from ");
		query.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
		query.append(" AS r WHERE r.ecm:uuid = ?) = 1");

		final List<DocumentModel> docList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE, query.toString(),
				new String[] { dossier.getDocument().getId() });

		return docList;
	}

	/**
	 * Récupère tout les document du fond de dossier à partir da la liste des identifiants des répertoires racines
	 * visible.
	 * 
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	private List<DocumentModel> getAllVisibleFddDocument(final CoreSession session, final List<String> folderVisibleIds)
			throws ClientException {
		// on récupère tous les documents fils des répertoires racines visibles
		final StringBuilder getAllFolderAndFileQuery = new StringBuilder("SELECT l.ecm:uuid AS id FROM Document AS l ")
				.append("WHERE isChildOf(l.ecm:uuid, select r.ecm:uuid from FondDeDossierFolder AS r WHERE r.ecm:uuid IN (")
				.append(StringUtil.getQuestionMark(folderVisibleIds.size())).append(")) = 1 and l." + NOT_DELETED);

		final List<DocumentModel> docList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, null,
				getAllFolderAndFileQuery.toString(), folderVisibleIds.toArray(new String[folderVisibleIds.size()]));
		return docList;
	}

	/**
	 * Récupère un répertoire du fond de dossier à partir de son titre.
	 * 
	 * @param dossierdocModel
	 * @param session
	 * @param folderName
	 * @return un répertoire du fond de dossier à partir de son titre.
	 * @throws ClientException
	 */
	private DocumentModel getFondDossierFolder(final DocumentModel dossierdocModel, final CoreSession session,
			final String folderName) throws ClientException {
		// on récupère le répertoire du Fond de Dossier via une requete FNXQL
		final StringBuilder getFondDossierFolderQuery = new StringBuilder("SELECT l.ecm:uuid AS id FROM ")
				.append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)
				.append(" AS l WHERE l.dc:title = ? and isChildOf(l.ecm:uuid, select r.ecm:uuid from ")
				.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE).append(" AS r WHERE r.ecm:uuid = ? ) = 1");
		final List<String> paramList = new ArrayList<String>();
		paramList.add(folderName);
		paramList.add(dossierdocModel.getId());

		final List<DocumentModel> docList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE,
				getFondDossierFolderQuery.toString(), paramList.toArray(new String[0]));

		if (docList == null || docList.size() < 1) {
			throw new EPGException("Aucun répertoire dans le fond de dossier avec le nom : " + folderName);
		}

		return docList.get(0);
	}

	@Override
	public DocumentModel createFondDeDossierFile(final CoreSession session, final String fileName, final Blob content,
			final String folderName, final DocumentModel dossier) throws ClientException {
		DocumentModel fileDoc = null;
		try {
			// Création fichier
			DocumentModel repertoireParentDoc = getFondDossierFolder(dossier, session, folderName);
			fileDoc = createBareFileInFolder(session, repertoireParentDoc,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE, fileName, content);
			final FondDeDossierFile fichier = fileDoc.getAdapter(FondDeDossierFile.class);
			fileDoc = persistFileInFolder(session, fichier.getDocument());
			// journalisation de l'action dans les logs
			journalisationAction(SolonEpgEventConstant.ACTION_CREATE, fileDoc.getType(), session, dossier, fileName,
					repertoireParentDoc);
		} catch (final ClientException e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_FILE_TEC, e);
			throw new SSException("Erreur lors de la création du fichier", e);
		}
		return fileDoc;
	}

	@Override
	public DocumentModel createFile(final CoreSession session, final NuxeoPrincipal principal, final String fileName,
			final Blob content, final String folderId, final DocumentModel dossier) throws ClientException {
		DocumentModel docModel = null;
		// création du DocumentModel
		try {
			DocumentModel repertoireParent = session.getDocument(new IdRef(folderId));
			docModel = createBareFileInFolder(session, repertoireParent,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE, fileName, content);
			final FondDeDossierFile fichier = docModel.getAdapter(FondDeDossierFile.class);
			fichier.setEntite(getEntite(principal));
			docModel = persistFileInFolder(session, fichier.getDocument());
			// journalisation de l'action dans les logs
			journalisationAction(SolonEpgEventConstant.ACTION_CREATE, docModel.getType(), session, dossier, fileName,
					repertoireParent);
		} catch (final ClientException e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_FILE_TEC, e);
			throw new SSException("Erreur lors de la création du fichier", e);
		}
		return docModel;
	}

	@Override
	public DocumentModel createFondDeDossierFile(final CoreSession session, final String fileName,
			final byte[] content, final String folderName, final DocumentModel dossier) throws ClientException {
		return this.createFondDeDossierFile(session, fileName, new ByteArrayBlob(content), folderName, dossier);
	}

	@Override
	public String getFondDeDossierRepertoireType() {
		return SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE;
	}

	@Override
	protected SSTreeFolder getFolderImplFromDoc(final DocumentModel doc) {
		return doc.getAdapter(FondDeDossierFolder.class);
	}

	@Override
	protected SSTreeFile getFileImplFromDoc(final DocumentModel doc) {
		return doc.getAdapter(FondDeDossierFile.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FondDeDossierFolder> getChildrenFolder(CoreSession session, DocumentModel repertoireParent)
			throws ClientException {
		return (List<FondDeDossierFolder>) sortDocumentationJointeFolder(
				super.getChildrenFolder(session, repertoireParent), repertoireParent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FondDeDossierFolder> getChildrenFolder(CoreSession session, DocumentRef repertoireParentRef)
			throws ClientException {
		return (List<FondDeDossierFolder>) sortDocumentationJointeFolder(
				super.getChildrenFolder(session, repertoireParentRef), session.getDocument(repertoireParentRef));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FondDeDossierFile> getChildrenFile(CoreSession session, DocumentModel repertoireParent)
			throws ClientException {
		return (List<FondDeDossierFile>) sortDocumentationJointeFolder(
				super.getChildrenFile(session, repertoireParent), repertoireParent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FondDeDossierFile> getChildrenFile(CoreSession session, DocumentRef repertoireParentRef)
			throws ClientException {
		return (List<FondDeDossierFile>) sortDocumentationJointeFolder(
				super.getChildrenFile(session, repertoireParentRef), session.getDocument(repertoireParentRef));
	}

	protected List<? extends SSTreeNode> sortDocumentationJointeFolder(List<? extends SSTreeNode> folders,
			DocumentModel repertoireParent) {
		if (SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE.equals(repertoireParent.getName())
				|| repertoireParent.getPathAsString().contains(
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE)) {
			Collections.sort(folders, new AlphabeticalNameComparator());
		}
		return folders;
	}

	@Override
	public boolean checkNameUnicity(CoreSession session, String filename, String folderId, DocumentModel dossierDoc)
			throws ClientException {
		DocumentModel folderDoc = session.getDocument(new IdRef(folderId));

		if (folderDoc != null) {
			// On vérifie que le fichier n'existe pas et qu'il n'a pas été ajouté lors de l'étape pour avis CE
			return (!session.exists(new PathRef(folderDoc.getPathAsString() + "/" + filename)))
					&& (!session.exists(new PathRef(folderDoc.getPathAsString() + "/"
							+ SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_CONSTANTE_DOC_MODIF_POUR_AVIS_CE
							+ filename)));
		}
		return true;
	}

	@Override
	public boolean isDossierEtapePourAvisCE(final DocumentModel dossier, final CoreSession session) {
		// Si le dossier est à l'étape 'Pour avis CE' création jeton pour le WS chercherModificationDossier (FEV 505)
		// récupère le dossierLink lié au dossier
		try {
			final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
			return corbeilleService.existsPourAvisCEStep(session, dossier.getId());
		} catch (final ClientException e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC, dossier, e);
			return false;
		}
	}

	/**
	 * Méthode utilisée lors de l'action saisine rectificative pour récupérer tous les docs modifiés du FDD durant étape
	 * pour avis CE sauf ceux dans dossier Saisine rectificative
	 */
	@Override
	public List<FondDeDossierFile> getAllUpdatedFilesFDDNotSaisine(
			final CoreSession session, final Dossier dossier, final Calendar dateDernierSaisineRectificative)
			throws ClientException {
			List<DocumentModel> docs = getFilesInSaisine(session, dossier.getDocument());
			List<String> idsFilesInSaisine = new ArrayList<String>();
			for (DocumentModel doc : docs) {
				idsFilesInSaisine.add(doc.getId());
			}
		return getFilesUpdated(getFilesNotInSaisine(session, dossier.getDocument(), idsFilesInSaisine), dateDernierSaisineRectificative);
	}
	
	private List<FondDeDossierFile> getFilesUpdated(final List<DocumentModel> listDocsFDD, final Calendar lastUpdateCal) {
		List<FondDeDossierFile> fichiersDansFDD = new ArrayList<FondDeDossierFile>();
		for (DocumentModel docFdd : listDocsFDD) {
			final FondDeDossierFile fichier = docFdd.getAdapter(FondDeDossierFile.class);
			if (fichier.getModifiedDate() == null
					&& fichier.getDateDernierTraitement().compareTo(lastUpdateCal) > 0) {
				fichiersDansFDD.add(fichier);
			} else if (fichier.getModifiedDate() != null
					&& fichier.getModifiedDate().compareTo(lastUpdateCal) > 0) {
				fichiersDansFDD.add(fichier);
			}
		}
		return fichiersDansFDD;
	}
	
	
	private List<DocumentModel> getFilesNotInSaisine(final CoreSession session, final DocumentModel dossierDoc, final List<String> idsFilesInSaisine) throws ClientException {
		StringBuilder query = new StringBuilder(FDD_FILES_IN_DOSSIER_QUERY);
		if (idsFilesInSaisine != null && !idsFilesInSaisine.isEmpty()) {
			query.append(" AND f.ecm:uuid NOT IN (")
				.append(StringUtil.getQuestionMark(idsFilesInSaisine.size()))
				.append(")");
		}
		List<Object> params = new ArrayList<Object>();
		params.add(dossierDoc.getId());
		params.addAll(idsFilesInSaisine);
		
		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE, query.toString(), params.toArray(new Object[params.size()]));
	}
	
	private List<DocumentModel> getFilesInSaisine(final CoreSession session, final DocumentModel dossierDoc) throws ClientException {
		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE, FDD_FILES_IN_FOLDER_QUERY, new Object[] { "Saisine rectificative", dossierDoc.getId() });
	}
	
	

	@Override
	public List<FondDeDossierFile> getAllUpdatedFilesFDDInSaisine(final CoreSession session,
			final Dossier dossier, final Calendar dateDernierSaisineRectificative) throws ClientException {
		return getFilesUpdated(getFilesInSaisine(session, dossier.getDocument()), dateDernierSaisineRectificative);
	}

	@Override
	public Calendar getStartStepPourAvisCE(final DocumentModel dossier, CoreSession session) {
		final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
		return corbeilleService.getStartDateForRunningStep(session, dossier.getId(), VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE);
	}

	/**
	 * Retourne la liste des saisines rectificatives ou des envois de pièces complémentaires effectuées pour un dossier
	 * donné
	 * 
	 * @param dossier
	 * @param documentManager
	 * @return
	 */
	@Override
	public List<DocumentModel> getListSaisineRectificativeOuListTransmissionPiecesComplementaireForDossier(
			Dossier dossier, CoreSession documentManager, String typeModification) {
		StringBuilder chercherJetonQuery = new StringBuilder("SELECT j.ecm:uuid As id FROM ")
				.append(STConstant.JETON_DOC_TYPE).append(" AS j WHERE j.jtd:id_doc = ?")
				.append(" AND j.jtd:type_webservice = ? ").append(" AND j.jtd:type_modification = ? ");
		final List<String> params = new ArrayList<String>();
		params.add(dossier.getDocument().getId());
		params.add(STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER);
		params.add(typeModification);
		List<DocumentModel> jetonListDoc = new ArrayList<DocumentModel>();
		try {
			jetonListDoc = QueryUtils.doUnrestrictedUFNXQLQueryAndFetchForDocuments(documentManager,
					STConstant.JETON_DOC_TYPE, chercherJetonQuery.toString(), params.toArray(new String[0]));
		} catch (final ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_JETON_TEC, dossier, e);
		}

		return jetonListDoc;
	}

	/**
	 * Renvoi l'arborescence des répertoires mais seulement avec ceux qui ne sont pas vides
	 * 
	 * @param session
	 * @param dossier
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<DocumentModel> getAllFoldersWithDocuments(final CoreSession session, final Dossier dossier)
			throws ClientException {
		List<DocumentModel> fddDocList = getFddDocuments(session, dossier);
		List<DocumentModel> listFDDTrie = new ArrayList<DocumentModel>();
		for (DocumentModel fdd : fddDocList) {
			if (fdd.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
				List<DocumentModel> listChildrenFolder = session.getChildren(fdd.getRef());
				Boolean hasFichier = false;
				for (DocumentModel child : listChildrenFolder) {
					if (child.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)) {
						hasFichier = true;
					}
				}
				if (hasFichier) {
					listFDDTrie.add(fdd);
				}
			} else {
				listFDDTrie.add(fdd);
			}
		}
		return listFDDTrie;
	}

	/**
	 * Retourne les documents d'une arborescence de répertoires d'un dossier
	 * @param dossier Dossier danslequel les documents sont cherchés
	 * @param folderName : Répertoire racine de l'arborescence dans laquelle chercher les fichiers
	 */
	@Override
	public List<DocumentModel> getAllFilesFromFondDeDossierForArchivage(final CoreSession session,
			final Dossier dossier, final String folderName) throws ClientException {
		List<DocumentModel> foldersFDD = getAllFddFolder(session, dossier); // tous les répertoires de haut niveau
		List<DocumentModel> listDocuments = new ArrayList<DocumentModel>();
		for (DocumentModel folder : foldersFDD) {
			if (folder.getTitle().toUpperCase().trim().equals(folderName.toUpperCase().trim())
					|| folder.getName().toUpperCase().trim().equals(folderName.toUpperCase().trim())) {
				List<DocumentModel> listDocsFDD = new ArrayList<DocumentModel>();
				try { // recherche de tous les documents qui sont à l'intérieur
					listDocsFDD = session.getChildren(folder.getRef());
					for (DocumentModel docFdd : listDocsFDD) {
						getFilesFromHierarchy(session, listDocuments, docFdd);
					}
					break;
				} catch (ClientException e) {
					continue;
				}
			}

		}
		return listDocuments;
	}
	
	/**
	 * Retourne les documents d'une arborescence de répertoires d'un dossier
	 * @param dossier Dossier danslequel les documents sont cherchés
	 * @param folderName : Répertoire racine de l'arborescence dans laquelle chercher les fichiers
	 */
	@Override
	public List<DocumentModel> getAllFilesFromFondDeDossierAllFolder(final CoreSession session,
			final Dossier dossier) throws ClientException {
		List<DocumentModel> foldersFDD = getAllFddFolder(session, dossier); // tous les répertoires de haut niveau
		List<DocumentModel> listDocuments = new ArrayList<DocumentModel>();
		for (DocumentModel folder : foldersFDD) {
			List<DocumentModel> listDocsFDD = new ArrayList<DocumentModel>();
			try { // recherche de tous les documents qui sont à l'intérieur
				listDocsFDD = session.getChildren(folder.getRef());
				for (DocumentModel docFdd : listDocsFDD) {
					getFilesFromHierarchy(session, listDocuments, docFdd);
				}
			} catch (ClientException e) {
				continue;
			}

		}
		return listDocuments;
	}
	
	/**
	 * Rempli une liste avec les fichiers d'une hierarchie de DocumentModel
	 * @param listDocuments : liste à remplir
	 * @param DocumentModel à parcourir
	 * @throws ClientException 
	 */
	private void getFilesFromHierarchy(final CoreSession session, List<DocumentModel> listDocuments,
		DocumentModel document) throws ClientException {
		if (document.getType().equals(
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)
				&& !listDocuments.contains(document)) {
			listDocuments.add(document);
		} else if (document.getType().equals(
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
			List<DocumentModel> listChildren = session.getChildren(document.getRef());
			for (DocumentModel child : listChildren) {
				getFilesFromHierarchy(session, listDocuments, child);
			}
		}	
	}
	
	@Override
	public List<DocumentModel> getAllFilesFromFondDeDossierForArchivageOutOfFolder(final CoreSession session,
			final Dossier dossier, final List<String> folderNames) throws ClientException {
		List<DocumentModel> foldersFDD = getAllFddFolder(session, dossier);
		List<DocumentModel> listDocuments = new ArrayList<DocumentModel>();
		List<String> folderNamesUpper = listFolderToUpper(folderNames);
		for (DocumentModel folder : foldersFDD) {
			if (!folderNamesUpper.contains(folder.getName().trim().toUpperCase())
					&& !folderNamesUpper.contains(folder.getTitle().trim().toUpperCase())) {
				List<DocumentModel> listDocsFDD = new ArrayList<DocumentModel>();
				try {
					listDocsFDD = getFilesFromFondDossierFolder(session, dossier, folder);
					if (listDocsFDD != null) {
						for (DocumentModel docFdd : listDocsFDD) {
							if (docFdd.getType().equals(
									SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)
									&& !listDocuments.contains(docFdd)) {
								listDocuments.add(docFdd);
							}
						}
					}
				} catch (ClientException e) {
					continue;
				}
			}
		}
		return listDocuments;
	}

	// retoure true si des fichiers sont contenus dans la liste de folders
	@Override
	public Boolean hasFichiersInFoldersInFDD(final CoreSession session, final Dossier dossier,
			final List<String> folderNames) throws ClientException {
		List<String> folderNamesUpper = listFolderToUpper(folderNames);
		List<DocumentModel> foldersFDD = getAllFddFolder(session, dossier);
		for (DocumentModel folder : foldersFDD) {
			if (folderNamesUpper.contains(folder.getName().trim().toUpperCase())
					|| folderNamesUpper.contains(folder.getTitle().trim().toUpperCase())) {
				List<DocumentModel> listDocsFDD = new ArrayList<DocumentModel>();
				try {
					listDocsFDD = getFilesFromFondDossierFolder(session, dossier, folder);
					if (listDocsFDD != null && !listDocsFDD.isEmpty()) {
						return true;
					}
				} catch (ClientException e) {
					continue;
				}
			}
		}
		return false;
	}

	// Retourne la liste des dossiers en majuscules
	private List<String> listFolderToUpper(List<String> listFolderNames) {
		List<String> listFolderNamesUpper = new ArrayList<String>();
		for (String folderName : listFolderNames) {
			listFolderNamesUpper.add(folderName.toUpperCase().trim());
		}
		return listFolderNamesUpper;
	}

	@Override
	public void sendSaisineRectificative(final CoreSession session, final DocumentModel dossierDoc, final List<FondDeDossierFile> filesForCE, final String startComment) throws ClientException {
		sendFilesToCE(session, dossierDoc, filesForCE, startComment,
				TypeModification.SAISINE_RECTIFICATIVE.name(),
				SolonEpgEventConstant.AFTER_SAISINE_RECTIFICATIVE);
	}

	@Override
	public void sendPiecesComplementaires(final CoreSession session, final DocumentModel dossierDoc, final List<FondDeDossierFile> filesForCE, final String startComment) throws ClientException {
		sendFilesToCE(session, dossierDoc, filesForCE, startComment,
				TypeModification.PIECE_COMPLEMENTAIRE.name(),
				SolonEpgEventConstant.AFTER_ENVOI_PIECES_COMPLEMENTAIRES);
	}

	private void sendFilesToCE(final CoreSession session, DocumentModel dossierDoc, List<FondDeDossierFile> filesForCE, String startComment, String typeModification, String eventName) throws ClientException {
		final JetonService jetonService = STServiceLocator.getJetonService();
		final JournalService journalService = STServiceLocator.getJournalService();
		// Là on va créer le jeton pour l'action envoi de pièces complémentaires/saisine rectificative
		// On récupère le dossier concerné
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		final StringBuilder comment = new StringBuilder(startComment);
		final List<String> idsFichiers = new ArrayList<String>();
		String prefix = " ";
		// On parcours chacun des documents concernés par l'envoi de pièces complémentaires
		for (FondDeDossierFile fichierFDD : filesForCE) {
			List<DocumentModel> fileVersionList = session.getVersions(fichierFDD.getDocument().getRef());
			if (!fileVersionList.isEmpty()) {
				// La dernière version du document se trouve à la fin de la liste
				DocumentModel file = fileVersionList.get(fileVersionList.size() -1);
				idsFichiers.add(file.getId());
			}
			comment.append(prefix);
			prefix = ", ";
			comment.append(fichierFDD.getTitle());
		}

		jetonService.addDocumentInBasket(session, STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER,
				TableReference.MINISTERE_CE, dossier.getDocument(), dossier.getNumeroNor(), typeModification, idsFichiers);

		// Enregistrement dans le journal technique
		String eventCategory = SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER;
		journalService.journaliserAction(session, dossierDoc, eventName, comment.toString(), eventCategory);
		dossier.save(session);
		session.save();
	}
}
