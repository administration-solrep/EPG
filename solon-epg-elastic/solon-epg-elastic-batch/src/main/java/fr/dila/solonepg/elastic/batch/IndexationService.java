package fr.dila.solonepg.elastic.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
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
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.schema.PrefetchInfo;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

public class IndexationService extends DefaultComponent implements IIndexationService {
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(IndexationDossiersBatch.class);

    /**
     * Logger géré dans un fichier à part.
     */
    private static final STLogger LOGGER_BATCH = STLogFactory.getLog("INDEXATION-BATCH");

    /**
     * Logger pour les cas particuliers d'erreur d'indexation de fichier (fichier manquant, erreur d'extraction du texte,
     * fichier vide, fichiers trop volumineux).
     *
     * Loggers gérés dans des fichiers à part.
     */
    private static final STLogger LOGGER_MISSING = STLogFactory.getLog("INDEXATION-MISSING");
    private static final STLogger LOGGER_PARSE = STLogFactory.getLog("INDEXATION-PARSE");
    private static final STLogger LOGGER_EMPTY = STLogFactory.getLog("INDEXATION-EMPTY");
    private static final STLogger LOGGER_LARGE = STLogFactory.getLog("INDEXATION-LARGE");

    /** partie du pattern de date utilisé pour nommer les répertoires */
    private static final String JSON_MINUTE_PART_PATTERN = "yyyy-MM-dd_HH_mm";
    /** pattern de date utilisé pour nommer les fichiers */
    private static final String JSON_FILE_PATTERN = String.format("%s_ss'T'Z", JSON_MINUTE_PART_PATTERN);

    private ConfigService configService;
    private IDossierMapper dossierMapper;
    private IDocumentMapper documentMapper;
    private IIndexationPersistenceService indexationPersistenceService;
    private File exportFolder;
    private File temporaryFolder;

    /** composants internes */
    private ObjectMapper objectMapper;

    @Override
    public void activate(ComponentContext context) {
        configService = STServiceLocator.getConfigService();
        documentMapper = Framework.getService(IDocumentMapper.class);
        dossierMapper = Framework.getService(IDossierMapper.class);
        indexationPersistenceService = Framework.getService(IIndexationPersistenceService.class);
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
    }

    @Override
    public boolean isIndexationContinueEnabled() {
        return configService.getBooleanValue(
            SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_CONTINUE_ENABLED
        );
    }

    /**
     * Récupération des identifiants des dossiers à indexer
     */
    @Override
    public List<String> listDossiersIdsAIndexer(CoreSession session) {
        List<String> ids = null;
        String query = String.format(
            "SELECT * FROM %s AS d WHERE dos:deleted = 0 ORDER BY dos:created DESC",
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
        );
        int batchLimit = getBatchLimit();
        int batchOffset = getBatchOffset();

        long start = System.currentTimeMillis();
        ids = QueryUtils.doQueryForIds(session, query, batchLimit, batchOffset);
        LOGGER_BATCH.info(
            session,
            EpgLogEnumImpl.PROCESS_B_EXPORT_DOSSIERS_TEC,
            String.format(
                "Récupération de %d identifiants de dossiers en %.3f s.",
                ids.size(),
                (float) (System.currentTimeMillis() - start) / 1000
            )
        );

        return ids;
    }

    /**
     * Indexation d'un dossier (récupération de l'objet dossier, des objets documents, combinaison et écriture json)
     *
     * @throws NuxeoException Erreur de persistence du résultat
     */
    @Override
    public IndexationDossierStatistics doIndexDossier(
        CoreSession session,
        Map<String, String> vecteurPublications,
        DocumentModel dossierDoc,
        boolean deletion
    ) {
        IndexationDossierStatistics s = IndexationDossierStatistics.create();
        String dossierId = dossierDoc.getId();
        try {
            s.start();
            IndexationDossierStatistics.set(s);
            // en cas d'erreur de chargement -> NuxeoException
            final ElasticDossier dossier;
            if (deletion) {
                // pour une suppression, on génère un document avec comme seules propriétés
                // l'identifiant la propriété deleted à true
                dossier = new ElasticDossier();
                dossier.setUid(dossierId);
                dossier.setDeleted(true);
            } else {
                dossier = exportDossier(dossierDoc, session, vecteurPublications);
                exportDocuments(dossierDoc, dossier, session);
            }
            writeDossierJson(dossier);

            // indexation réussie avec potentiellement des avertissements
            String message = s.warningMessage();
            if (message.length() == 0) {
                indexationPersistenceService.markIndexationSuccess(dossierId, deletion);
            } else {
                indexationPersistenceService.markIndexationSuccessWarning(dossierId, deletion, message);
            }
        } catch (MappingException | RuntimeException e) {
            LOGGER_BATCH.error(
                session,
                EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC,
                String.format("Erreur d'indexation du dossier %s", dossierId),
                e
            );
            IndexationDossierStatistics.error();
            indexationPersistenceService.markIndexationError(dossierId, e.getMessage());
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
            if (!finalPrefixPath.exists()) {
                finalPrefixPath.mkdir();
                // on ne peut pas utiliser le booléen de mkdir car on peut avoir des création concurrentes
                if (!finalPrefixPath.exists()) {
                    throw new IOException(
                        String.format("Echec de la création du répertoire %s", finalPrefixPath.getPath())
                    );
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
                if (!moved) {
                    if (thrownException == null) {
                        LOGGER_BATCH.error(
                            EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC,
                            String.format("Erreur de déplacement %s -> %s", zipPath.getPath(), finalPath.getPath())
                        );
                    } else {
                        LOGGER_BATCH.error(
                            EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC,
                            thrownException,
                            String.format("Erreur de déplacement %s -> %s", zipPath.getPath(), finalPath.getPath())
                        );
                    }
                }
            }
        } catch (IOException ioe) {
            LOGGER_BATCH.error(
                EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC,
                ioe,
                String.format("Erreur de génération du json pour le dossier %s", dossier.getUid())
            );
            IndexationDossierStatistics.jsonError();
        } finally {
            try {
                if (zos != null) {
                    zos.flush();
                    zos.close();
                }
            } catch (IOException e) {
                LOGGER_BATCH.error(
                    EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC,
                    e,
                    String.format("Erreur de fermeture de %s", zos)
                );
            }
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                LOGGER_BATCH.error(
                    EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC,
                    e,
                    String.format("Erreur de fermeture de %s", bos)
                );
            }
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException e) {
                LOGGER_BATCH.error(
                    EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC,
                    e,
                    String.format("Erreur de fermeture de %s", zos)
                );
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
            return Integer.parseInt(
                configService.getValue(SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_OFFSET)
            );
        } catch (Exception e) {}
        return 0; // Pas de décalage
    }

    /**
     * Récupération du nombre total de dossiers souhaités sur la globalité du batch.
     *
     * @return un int compris entre 0 et Integer.MAX_VALUE
     */
    private int getBatchLimit() {
        try {
            return Integer.parseInt(
                configService.getValue(SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_LIMITE)
            );
        } catch (Exception e) {}
        return Integer.MAX_VALUE; // Pas de limite
    }

    /**
     * Initialisation du répertoire d'export pour l'export courant.
     */
    private File initExportFolder() {
        // Désignation du répertoire
        String exportRootFolder = configService.getValue(
            SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_DESTINATION
        );
        return initFolder(exportRootFolder);
    }

    /**
     * Initialisation du répertoire temporaire pour les exports.
     */
    private File initTemporaryFolder() {
        // Désignation du répertoire
        String temporaryRootFolder = configService.getValue(
            SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_DESTINATION_TEMPORAIRE
        );
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
        if (!exportFolder.exists()) {
            try {
                if (!exportFolder.mkdirs()) {
                    throw new IllegalStateException(
                        String.format("Création du répertoire %s impossible", exportFolder.getAbsolutePath())
                    );
                }
            } catch (SecurityException se) {
                throw new IllegalStateException(
                    String.format("Création du répertoire %s impossible", exportFolder.getAbsolutePath()),
                    se
                );
            }
        } else if (!exportFolder.isDirectory()) {
            throw new IllegalStateException(
                String.format(
                    "Le chemin cible %s existe déjà et n'est pas un répertoire",
                    exportFolder.getAbsolutePath()
                )
            );
        }

        return exportFolder;
    }

    /**
     * Mapping d'un {@link DocumentModel} en {@link ElasticDossier}
     */
    private ElasticDossier exportDossier(
        DocumentModel documentModel,
        CoreSession session,
        Map<String, String> vecteurPublications
    )
        throws MappingException {
        ElasticDossier exportableDossier = null;
        exportableDossier = dossierMapper.from(documentModel, session, vecteurPublications);
        return exportableDossier;
    }

    /**
     * Constitution de la liste des {@link ElasticDocument} pour un {@link DocumentModel} de type dossier
     */
    private void exportDocuments(DocumentModel documentModel, ElasticDossier dossier, CoreSession session)
        throws MappingException {
        Map<String, ElasticDocument> documents = new HashMap<>();
        Dossier dossierDoc = documentModel.getAdapter(Dossier.class);
        // Parapheur
        exportFilesDocuments(dossierDoc.getParapheur(session).getDocument(), documents, session);
        // Fond de dossier
        exportFilesDocuments(dossierDoc.getFondDeDossier(session).getDocument(), documents, session);

        dossier.getDocuments().addAll(documents.values());
    }

    @Override
    public int getQueryLimit() {
        try {
            return Integer.parseInt(
                configService.getValue(SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_LIMITE_REQUETE)
            );
        } catch (Exception e) {
            LOGGER_BATCH.error(
                EpgLogEnumImpl.PROCESS_B_EXPORT_DOSSIERS_TEC,
                e,
                "Erreur de récupération " + SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_LIMITE_REQUETE
            );
        }
        return Integer.MAX_VALUE; // Pas de limite
    }

    /**
     * Export des documents parapheur pour un {@link DocumentModel} de type dossier
     */
    private void exportFilesDocuments(
        DocumentModel documentModel,
        Map<String, ElasticDocument> documents,
        CoreSession session
    )
        throws MappingException {
        List<DocumentModel> documentModelList = QueryHelper.doUFNXQLQueryAndFetchForDocuments(
            session,
            "SELECT d.ecm:uuid as id FROM File as d WHERE isChildOf(d.ecm:uuid,select r.ecm:uuid from " +
            documentModel.getType() +
            " AS r WHERE r.ecm:uuid = ?)=1",
            new String[] { documentModel.getId() },
            0,
            0,
            new PrefetchInfo(STSchemaConstant.FILE_SCHEMA)
        );
        for (DocumentModel fileDoc : documentModelList) {
            List<DocumentModel> documentVersionsList;
            try {
                documentVersionsList = session.getVersions(fileDoc.getRef());
            } catch (NuxeoException e) {
                throw new MappingException(
                    String.format("Erreur de récupération des version du document %s", fileDoc.getId()),
                    e
                );
            }
            Collections.reverse(documentVersionsList);
            boolean lastVersion = true;
            for (DocumentModel documentVersion : documentVersionsList) {
                mapDocument(fileDoc, documents, session, fileDoc.getPathAsString(), documentVersion, lastVersion);
                // seul le premier document est la dernière version
                lastVersion = false;
            }
        }
    }

    /**
     * Mapping du {@link DocumentModel} d'un fichier associé à un dossier en {@link ElasticDocument}
     */
    private void mapDocument(
        DocumentModel folderDocModel,
        Map<String, ElasticDocument> documents,
        CoreSession session,
        String path,
        DocumentModel documentVersion,
        boolean lastVersion
    ) {
        IndexationDossierStatistics.document();
        try {
            if (documents.containsKey(documentVersion.getId())) {
                IndexationDossierStatistics.documentDoublons();
            } else {
                // on définit les méta-données et on ajoute le document
                ElasticDocument exportableDocument = documentMapper.from(
                    documentVersion,
                    path + File.separator + folderDocModel.getName(),
                    session,
                    lastVersion
                );
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
        } catch (RuntimeException | MappingException e) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC, e);
            IndexationDossierStatistics.documentError();
        }
    }
}
