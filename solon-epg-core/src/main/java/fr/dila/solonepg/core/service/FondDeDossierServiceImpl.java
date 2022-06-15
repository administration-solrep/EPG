package fr.dila.solonepg.core.service;

import com.google.common.collect.Iterables;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.fonddossier.FondDeDossier;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFile;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModel;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.ss.api.exception.SSException;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.SSTreeService;
import fr.dila.ss.api.tree.SSTreeFile;
import fr.dila.ss.api.tree.SSTreeFolder;
import fr.dila.ss.api.tree.SSTreeNode;
import fr.dila.ss.core.service.SSFondDeDossierServiceImpl;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.domain.STDomainObject;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.JetonService;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.schema.FileSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringHelper;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import fr.sword.xsd.solon.epg.TypeModification;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.Path;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.VersioningOption;
import org.nuxeo.ecm.core.api.impl.blob.ByteArrayBlob;
import org.nuxeo.ecm.core.api.versioning.VersioningService;

import static fr.dila.cm.cases.CaseConstants.TITLE_PROPERTY_NAME;
import static fr.dila.ss.core.service.SSServiceLocator.getSSTreeService;

/**
 * Implémentation service fond de dossier pour epg
 *
 */
public class FondDeDossierServiceImpl extends SSFondDeDossierServiceImpl implements FondDeDossierService {
    private static final long serialVersionUID = 8292406959483724550L;

    private static final Log LOG = LogFactory.getLog(FondDeDossierServiceImpl.class);
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(FondDeDossierServiceImpl.class);

    private static final String FDD_QUERY_START =
        "SELECT f.ecm:uuid AS id FROM " +
        SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE +
        " AS f WHERE f." +
        DossierSolonEpgConstants.FILE_SOLON_EPG_PREFIX +
        ":" +
        DossierSolonEpgConstants.FILE_SOLON_EPG_FILETYPE_ID +
        " = " +
        VocabularyConstants.FILETYPEID_FONDDOSSIER +
        " AND f." +
        NOT_DELETED;
    /**
     * La requête remonte les fdd files du dossier en param <br>
     * SELECT id FROM FileSolonEpg WHERE filetypeId=4 AND isChildOf(f.id, select d.id from Dossier where d.ecm:uuid = ?) = 1 AND f.ecm:currentLifeCycleState != 'deleted'
     */
    private static final String FDD_FILES_IN_DOSSIER_QUERY =
        FDD_QUERY_START + " AND isChildOf(f.ecm:uuid, select d.ecm:uuid from Dossier AS d WHERE d.ecm:uuid = ?) = 1";

    private static final String FDD_FILES_IN_FOLDER_QUERY =
        FDD_QUERY_START +
        " AND f." +
        DossierSolonEpgConstants.FILE_SOLON_EPG_PREFIX +
        ":" +
        DossierSolonEpgConstants.FILE_SOLON_EPG_RELATED_DOCUMENT_PROPERTY +
        " = ? " +
        "AND isChildOf(f.ecm:uuid, SELECT r.ecm:uuid FROM FondDeDossierFolder AS r WHERE r.dc:title = ?) = 1";

    /**
     * Default constructor
     */
    public FondDeDossierServiceImpl() {
        super();
    }

    @Override
    public void createAndFillFondDossier(final Dossier dossier, final CoreSession session) {
        // récupération du type d'acte
        final String typeActe = dossier.getTypeActe();
        if (typeActe == null) {
            throw new NuxeoException("Le type d'acte du dossier est nul !");
        }
        // récupération des formats autorisés
        List<String> formatsAutorises = null;
        final FondDeDossierModelService modelService = SolonEpgServiceLocator.getFondDeDossierModelService();
        formatsAutorises = modelService.getFormatsAutorises(session);
        // création du fond de dossier
        final DocumentModel fondDeDossierDoc = createFondDossierDocument(
            dossier.getDocument(),
            session,
            formatsAutorises
        );
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
        } catch (final NuxeoException e) {
            throw new NuxeoException("Erreur lors de de l'appel de la méthode getFondDossierModelFromTypeActe", e);
        }
    }

    @Override
    public void updateFondDossierTree(final Dossier dossier, final String newTypeActe, final CoreSession session) {
        // récupération des répertoires courants du fond de dossier courant
        final List<DocumentModel> fddCurrentFolderList = getAllFddFolder(session, dossier);

        // récupération de la liste des répertoires du modèle du fond de dossier correspondant au nouveau type d'acte
        final FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
        final List<DocumentModel> fddModelFolderList = fondDeDossierModelService.getListFondDossierFolderModelFromTypeActe(
            session,
            newTypeActe
        );

        // 1) on parcourt les répertoires du fond de dossier courant afin de supprimer ceux qui sont déjà présent

        // Liste des chemins des répertoires du modèle du fond de dossier
        final Set<String> fddModelFolderPathNameList = new HashSet<>();
        // Liste des chemins des répertoires existants du fond de dossier courant qui ne vont pas être supprimés
        final Set<String> fddCurrentFolderPathNameList = new HashSet<>();
        // Map contenant les DocumentRef et les chemins des répertoires existants du fond de dossier courant qui ne vont
        // pas être supprimés
        final Map<String, DocumentRef> fddCurrentFolderDocRefList = new HashMap<>();

        // on parcourt les répertoire du modèle de fond de dossier et on enregistre leurs chemins en enlevant la partie
        // commune aux autres répertoires
        for (final DocumentModel fddModelFolder : fddModelFolderList) {
            fddModelFolderPathNameList.add(getFddFolderModelIdentificationPath(fddModelFolder));
        }

        // liste des références de répertoire à supprimer
        final List<DocumentRef> docRefFddFolderToRemove = new ArrayList<>();

        // on parcourt les répertoires courants du fond de dossier
        for (final DocumentModel currentModelRep : fddCurrentFolderList) {
            // on récupère le chemin du répertoire dans l'arborescence en enlevant la partie commune aux autres
            // répertoires
            final String docPath = currentModelRep.getPathAsString();
            int lastIndex = docPath.lastIndexOf(SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME);
            final String docPathName = docPath.substring(
                lastIndex + SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME.length()
            );

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
    public void deleteFile(final CoreSession session, final DocumentModel document, final DocumentModel dossierDoc) {
        super.deleteFile(session, document);
        journalisationAction(
            SolonEpgEventConstant.ACTION_DELETE,
            document.getType(),
            session,
            dossierDoc,
            document.getTitle(),
            session.getDocument(document.getParentRef())
        );
    }

    @Override
    public void deleteFileWithType(
        final CoreSession session,
        final DocumentModel document,
        final DocumentModel dossierDoc,
        final String fileType
    ) {
        super.deleteFileWithType(session, document, fileType);
        journalisationAction(
            SolonEpgEventConstant.ACTION_DELETE,
            document.getType(),
            session,
            dossierDoc,
            document.getTitle(),
            session.getDocument(document.getParentRef())
        );
    }

    @Override
    public void restoreToPreviousVersion(
        final CoreSession session,
        final String currentDocId,
        final DocumentModel dossierDoc
    ) {
        super.restoreToPreviousVersion(session, currentDocId, dossierDoc);
        DocumentRef currentDocRef = new IdRef(currentDocId);
        DocumentModel currentFileDoc = session.getDocument(currentDocRef);
        journalisationAction(
            SolonEpgEventConstant.ACTION_UPDATE,
            currentFileDoc.getType(),
            session,
            dossierDoc,
            currentFileDoc.getTitle(),
            session.getDocument(currentFileDoc.getParentRef())
        );
    }

    @Override
    public void updateFile(
        final CoreSession session,
        DocumentModel fichier,
        final Blob blob,
        final NuxeoPrincipal currentUser,
        final DocumentModel dossierDocument
    ) {
        // force unlock liveEdit
        STServiceLocator.getSTLockService().unlockDocUnrestricted(session, fichier);

        // on récupère le nom du fichier
        final String docName = StringHelper.removeSpacesAndAccent(blob.getFilename());
        // set document name
        DublincoreSchemaUtils.setTitle(fichier, blob.getFilename());
        DublincoreSchemaUtils.setCreator(fichier, session.getPrincipal().getName());

        // set document file properties
        FileSchemaUtils.setContent(fichier, blob);
        // set entite
        fichier.setProperty(
            DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
            DossierSolonEpgConstants.FILE_SOLON_EPG_ENTITE_PROPERTY,
            getEntite(currentUser)
        );
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
        journalisationAction(
            SolonEpgEventConstant.ACTION_UPDATE,
            fichier.getType(),
            session,
            dossierDocument,
            blob.getFilename(),
            repertoireParent
        );
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
     */
    private void journalisationAction(
        final String actionType,
        final String documentType,
        final CoreSession session,
        final DocumentModel dossierDocument,
        final String docName,
        final DocumentModel repertoireParent,
        final String typeActe
    ) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(typeActe)) {
            // LOG des actions d'administration (modèles de fond de dossier et de parapheur)
            if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_FOLDER_DOCUMENT_TYPE)) {
                // LOG pour les modèles de répertoire du fond de dossier
                if (actionType.equals(SolonEpgEventConstant.ACTION_CREATE)) {
                    journalisationAdministrationAction(
                        session,
                        SolonEpgEventConstant.CREATE_MODELE_FDD_EVENT,
                        docName,
                        typeActe
                    );
                } else if (actionType.equals(SolonEpgEventConstant.ACTION_UPDATE)) {
                    journalisationAdministrationAction(
                        session,
                        SolonEpgEventConstant.UPDATE_MODELE_FDD_EVENT,
                        docName,
                        typeActe
                    );
                } else if (actionType.equals(SolonEpgEventConstant.ACTION_DELETE)) {
                    journalisationAdministrationAction(
                        session,
                        SolonEpgEventConstant.DELETE_MODELE_FDD_EVENT,
                        docName,
                        typeActe
                    );
                }
            } else if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
                // LOG pour les modèles de répertoire du fond de dossier
                if (actionType.equals(SolonEpgEventConstant.ACTION_DELETE)) {
                    journalisationAdministrationAction(
                        session,
                        SolonEpgEventConstant.DELETE_MODELE_FDD_EVENT,
                        docName,
                        typeActe
                    );
                }
            }

            return;
        }

        String path = "";
        if (repertoireParent != null) {
            path = repertoireParent.getPathAsString();
        }

        String eventCategory = STEventConstant.CATEGORY_FDD;

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
        if (repertoireParent != null) {
            repertoireParentTitle = repertoireParent.getTitle();
        }


        // journalisation de l'action dans les logs en fonction du type d'action et du type de document
        if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)) {
            // LOG pour les fichier du fond de dossier
            if (actionType.equals(SolonEpgEventConstant.ACTION_CREATE)) {
                journalisationAction(
                    session,
                    dossierDocument,
                    SolonEpgEventConstant.CREATE_FILE_FDD,
                    SolonEpgEventConstant.COMMENT_CREATE_FILE_FDD +
                    docName +
                    "' dans le répertoire '" +
                    repertoireParentTitle +
                    "'",
                    eventCategory
                );
            } else if (actionType.equals(SolonEpgEventConstant.ACTION_UPDATE)) {
                journalisationAction(
                    session,
                    dossierDocument,
                    SolonEpgEventConstant.UPDATE_FILE_FDD,
                    SolonEpgEventConstant.COMMENT_UPDATE_FILE_FDD +
                    docName +
                    "' dans le répertoire '" +
                    repertoireParentTitle +
                    "'",
                    eventCategory
                );
            } else if (actionType.equals(SolonEpgEventConstant.ACTION_DELETE)) {
                journalisationAction(
                    session,
                    dossierDocument,
                    SolonEpgEventConstant.DELETE_FILE_FDD,
                    SolonEpgEventConstant.COMMENT_DELETE_FILE_FDD +
                    docName +
                    "' dans le répertoire '" +
                    repertoireParentTitle +
                    "'",
                    eventCategory
                );
            }

        } else if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
            // LOG pour les répertoire du fond de dossier
            if (actionType.equals(SolonEpgEventConstant.ACTION_CREATE)) {
                journalisationAction(
                    session,
                    dossierDocument,
                    SolonEpgEventConstant.CREATE_FOLDER_FDD,
                    SolonEpgEventConstant.COMMENT_CREATE_FOLDER_FDD,
                    eventCategory
                );
            } else if (actionType.equals(SolonEpgEventConstant.ACTION_UPDATE)) {
                journalisationAction(
                    session,
                    dossierDocument,
                    SolonEpgEventConstant.UPDATE_FOLDER_FDD,
                    SolonEpgEventConstant.COMMENT_UPDATE_FOLDER_FDD + docName,
                    eventCategory
                );
            } else if (actionType.equals(SolonEpgEventConstant.ACTION_DELETE)) {
                journalisationAction(
                    session,
                    dossierDocument,
                    SolonEpgEventConstant.DELETE_FOLDER_FDD,
                    SolonEpgEventConstant.COMMENT_DELETE_FOLDER_FDD + docName,
                    eventCategory
                );
            }
        }
    }

    private void journalisationAction(
        final String actionType,
        final String documentType,
        final CoreSession session,
        final DocumentModel dossierDocument,
        final String docName,
        final DocumentModel repertoireParent
    ) {
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
     */
    private void journalisationAction(
        final CoreSession session,
        final DocumentModel docModel,
        final String eventName,
        final String commentaire,
        final String categorie
    ) {
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
     */
    private void journalisationAdministrationAction(
        final CoreSession session,
        final String eventName,
        final String docName,
        final String typeActe
    ) {
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
     */
    private DocumentModel createFondDossierDocument(
        final DocumentModel dossier,
        final CoreSession session,
        final List<String> formatsAutorises
    ) {
        try {
            LOG.debug("creation du document" + SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME);

            final DocumentModel newModel = session.createDocumentModel(
                dossier.getPath().toString(),
                SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME,
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_DOCUMENT_TYPE
            );

            // On affecte un titre par défaut au parapheur
            final String numeroNor = (String) dossier.getProperty(
                DossierSolonEpgConstants.DOSSIER_SCHEMA,
                DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY
            );
            if (numeroNor == null) {
                DublincoreSchemaUtils.setTitle(newModel, SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME);
            }
            final String title = SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME + " " + numeroNor;
            DublincoreSchemaUtils.setTitle(newModel, title);

            // on affecte les format de fichier autorises
            newModel.setProperty(
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_DOCUMENT_SCHEMA,
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FORMAT_AUTORISE_PROPERTY,
                formatsAutorises
            );

            // sauvegarde du parapheur
            final DocumentModel fondDossierModel = session.createDocument(newModel);

            return fondDossierModel;
        } catch (final NuxeoException e) {
            throw new NuxeoException("erreur lors de la creation du document ", e);
        }
    }

    @Override
    public List<FondDeDossierFolder> getAllVisibleRootFolder(
        final CoreSession session,
        final DocumentModel dossierModel,
        final NuxeoPrincipal currentUser
    ) {
        final Dossier dossier = dossierModel.getAdapter(Dossier.class);
        final DocumentModel fondDeDossierDoc = dossier.getFondDeDossier(session).getDocument();

        // on récupère l'identifiant du ministère d'appartenance de l'acte
        final String idMinistere = dossier.getMinistereAttache();
        // on récupère les droits de l'utilisateur
        List<String> groups = new ArrayList<>();
        Set<String> ministereIds = new HashSet<>();
        if (currentUser != null) {
            groups = currentUser.getGroups();
            // on récupère le ministère d'appartenance de l'utilisateur
            final SSPrincipal ssPrincipal = (SSPrincipal) currentUser;
            ministereIds = ssPrincipal.getMinistereIdSet();
        }

        final List<FondDeDossierFolder> folders = getChildrenFolder(session, fondDeDossierDoc);
        final List<FondDeDossierFolder> foldersReturn = new ArrayList<>(folders);
        for (final FondDeDossierFolder folder : folders) {
            if (!isRepertoireVisible(idMinistere, folder.getDocument(), ministereIds, groups)) {
                foldersReturn.remove(folder);
            }
        }
        return foldersReturn;
    }

    @Override
    public List<FondDeDossierFolder> getAllRootFolder(final CoreSession session, final Dossier dossierDoc) {
        final DocumentModel fondDossier = dossierDoc.getFondDeDossier(session).getDocument();
        return getChildrenFolder(session, fondDossier);
    }

    @Override
    public List<DocumentModel> storeAllChildren(
        final CoreSession session,
        final List<FondDeDossierFolder> repertoireVisibleList
    ) {
        final List<String> repertoireGroupsVisibleIdList = new ArrayList<>();
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
    protected boolean isRepertoireVisible(
        final String idMinistereRattachement,
        final DocumentModel repList,
        final Set<String> userMinistereIds,
        final List<String> userGroups
    ) {
        // pour chaque type de répertoire on signale si l'utilisateur a les droits de visibilité sur le répertoire
        Boolean isVisible = false;
        final String repname = repList.getName();
        if (repname.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR)) {
            // si l'utilisateur fait parti du ministère d'appartenance de l'acte, il peut visualiser le répertoire
            isVisible = userMinistereIds.contains(idMinistereRattachement);
        } else if (repname.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG)) {
            // si l'utilisateur fait parti d'un profil SGG, il peut visualiser le répertoire
            isVisible = userGroups.contains(SolonEpgBaseFunctionConstant.FOND_DOSSIER_REPERTOIRE_SGG);
        } else if (
            repname.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG)
        ) {
            // si l'utilisateur fait parti d'un profil SGG ou si l'utilisateur fait parti du ministère de rattachement,
            // il peut visualiser le répertoire
            isVisible =
                userMinistereIds.contains(idMinistereRattachement) ||
                userGroups.contains(SolonEpgBaseFunctionConstant.FOND_DOSSIER_REPERTOIRE_SGG);
        } else if (repname.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER)) {
            // répertoire accessible à tous les utilisateurs
            isVisible = true;
        }
        return isVisible;
    }

    @Override
    public List<FondDeDossierFolder> getChildrenFondDeDossierNodeFromdocList(
        final String documentRef,
        final CoreSession session,
        final List<DocumentModel> fddChildParentIdList
    ) {
        try {
            return getChildrenFolder(session, new IdRef(documentRef));
        } catch (final NuxeoException e) {
            LOG.warn("erreur lors de la recuperation de repertoire fils du document", e);
            throw new NuxeoException("erreur lors de la recuperation de repertoire fils du document", e);
        }
    }

    @Override
    public List<DocumentModel> getFddDocuments(final CoreSession session, final Dossier dossier) {
        final DocumentModel fddDoc = dossier.getFondDeDossier(session).getDocument();
        final List<DocumentModel> docs = new ArrayList<>();

        // récupération des répertoires racine du fond de dossier
        final List<DocumentModel> folders = session.getChildren(fddDoc.getRef());
        final List<String> folderVisibleIds = new ArrayList<>();

        // on récupère les ministères d'appartenances et les fonction unitaires de l'utilisateur de l'utilisateur
        final NuxeoPrincipal currentUser = session.getPrincipal();
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
    public List<DocumentModel> getPublicFddFiles(final CoreSession session, final Dossier dossier) {
        // récupération du répertoire "Répertoire accessible à tous les utilisateurs" à partir du chemin
        Path path = dossier.getDocument().getPath();
        path =
            path
                .append(SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME)
                .append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER);
        final DocumentModel repertoirePublicFdd = session.getDocument(new PathRef(path.toString()));
        // on récupère tous les fichiers du répertoire
        return getFilesFromFondDossierFolder(session, dossier, repertoirePublicFdd);
    }

    @Override
    public List<DocumentModel> getRapportPresentationFiles(final CoreSession session, final Dossier dossier) {
        // récupération du répertoire "Rapport de Présentation" à partir du chemin
        Path path = dossier.getDocument().getPath();
        path =
            path
                .append(SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME)
                .append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER)
                .append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE)
                .append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RAPPORT_PRESENTATION);
        final DocumentModel rapportPresentationFolder = session.getDocument(new PathRef(path.toString()));
        // récupération des fichiers du répertoire
        return getFilesFromFondDossierFolder(session, dossier, rapportPresentationFolder);
    }

    @Override
    public List<DocumentModel> getEpreuvesFiles(final CoreSession session, final Dossier dossier) {
        // récupération du répertoire "Epreuves" dans le fond de dossier vu qu'on ne sait pas où il peut être, on le
        // cherche dans toute l'arbo
        try {
            List<DocumentModel> repertoiresFDD = getAllFddFolder(session, dossier);

            DocumentModel epreuveFolder = null;
            for (DocumentModel fddf : repertoiresFDD) {
                FondDeDossierFolder fddRepert = fddf.getAdapter(FondDeDossierFolder.class);

                if (
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES.equals(fddRepert.getName()) ||
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES.equals(fddRepert.getTitle())
                ) {
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
        return new ArrayList<>();
    }

    @Override
    public List<DocumentModel> getAvisCEFiles(final CoreSession session, final Dossier dossier) {
        // récupération du répertoire "Avis du Conseil d'Etat" dans le fond de dossier
        try {
            List<DocumentModel> repertoiresFDD = getAllFddFolder(session, dossier);

            DocumentModel avisCEFolder = null;
            for (DocumentModel fddf : repertoiresFDD) {
                FondDeDossierFolder fddRepert = fddf.getAdapter(FondDeDossierFolder.class);

                if (
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE.equals(fddRepert.getName()) ||
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE.equals(fddRepert.getTitle())
                ) {
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
        return new ArrayList<>();
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
     */
    @Override
    public List<DocumentModel> getFilesFromFondDossierFolder(
        final CoreSession session,
        final Dossier dossier,
        final DocumentModel folder
    ) {
        // on récupère tous les documents fils du répertoire
        final StringBuilder query = new StringBuilder("SELECT l.ecm:uuid AS id FROM ");
        query.append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE);
        query.append(" AS l WHERE isChildOf(l.ecm:uuid, select r.ecm:uuid from ");
        query.append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE);
        query.append(" AS r WHERE r.ecm:uuid = ? ) = 1 and l." + NOT_DELETED);

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE,
            query.toString(),
            new String[] { folder.getId() }
        );
    }

    @Override
    public List<DocumentModel> getAllFddFolder(final CoreSession session, final Dossier dossier) {
        // on récupère tous les répertoires du fond de dossier correspondant au type d'acte choisi
        final StringBuilder query = new StringBuilder("SELECT l.ecm:uuid AS id FROM ");
        query.append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE);
        query.append(" AS l WHERE isChildOf(l.ecm:uuid, select r.ecm:uuid from ");
        query.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
        query.append(" AS r WHERE r.ecm:uuid = ?) = 1");

        final List<DocumentModel> docList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE,
            query.toString(),
            new String[] { dossier.getDocument().getId() }
        );

        return docList;
    }

    /**
     * Récupère tout les document du fond de dossier à partir da la liste des identifiants des répertoires racines
     * visible.
     *
     * @param session
     * @return
     */
    private List<DocumentModel> getAllVisibleFddDocument(
        final CoreSession session,
        final List<String> folderVisibleIds
    ) {
        // on récupère tous les documents fils des répertoires racines visibles
        final StringBuilder getAllFolderAndFileQuery = new StringBuilder("SELECT l.ecm:uuid AS id FROM Document AS l ")
            .append("WHERE isChildOf(l.ecm:uuid, select r.ecm:uuid from FondDeDossierFolder AS r WHERE r.ecm:uuid IN (")
            .append(StringHelper.getQuestionMark(folderVisibleIds.size()))
            .append(")) = 1 and l." + NOT_DELETED);

        final List<DocumentModel> docList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            "Document",
            getAllFolderAndFileQuery.toString(),
            folderVisibleIds.toArray(new String[folderVisibleIds.size()])
        );
        return docList;
    }

    @Override
    public DocumentModel getFondDossierFolder(
        final DocumentModel dossierdocModel,
        final CoreSession session,
        final String folderName,
        boolean createIfNeeded
    ) {
        // on récupère le répertoire du Fond de Dossier via une requete FNXQL
        final StringBuilder getFondDossierFolderQuery = new StringBuilder("SELECT l.ecm:uuid AS id FROM ")
            .append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)
            .append(" AS l WHERE l.dc:title = ? and isChildOf(l.ecm:uuid, select r.ecm:uuid from ")
            .append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE)
            .append(" AS r WHERE r.ecm:uuid = ? ) = 1");
        final List<String> paramList = new ArrayList<>();
        paramList.add(folderName);
        paramList.add(dossierdocModel.getId());

        final List<DocumentModel> docList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE,
            getFondDossierFolderQuery.toString(),
            paramList.toArray(new String[0])
        );

        if (CollectionUtils.isEmpty(docList) && createIfNeeded) {
            FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();
            FondDeDossier fdd = dossierdocModel.getAdapter(Dossier.class).getFondDeDossier(session);
            DocumentModel myNewFolder = fddService.createNewFolder(
                session,
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE,
                fdd.getDocument(),
                folderName
            );
            return session.saveDocument(myNewFolder);
        } else if (CollectionUtils.isEmpty(docList)) {
            throw new EPGException("Aucun répertoire dans le fond de dossier avec le nom : " + folderName);
        }

        return docList.get(0);
    }

    @Override
    public DocumentModel createFondDeDossierFile(
        final CoreSession session,
        final String fileName,
        final Blob content,
        final String folderName,
        final DocumentModel dossier
    ) {
        DocumentModel fileDoc = null;
        try {
            // Création fichier
            DocumentModel repertoireParentDoc = getFondDossierFolder(dossier, session, folderName, false);
            fileDoc =
                createBareFileInFolder(
                    session,
                    repertoireParentDoc,
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE,
                    fileName,
                    content
                );
            fileDoc = persistFileInFolder(session, fileDoc);
            // journalisation de l'action dans les logs
            journalisationAction(
                SolonEpgEventConstant.ACTION_CREATE,
                fileDoc.getType(),
                session,
                dossier,
                fileName,
                repertoireParentDoc
            );
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_FILE_TEC, e);
            throw new SSException("Erreur lors de la création du fichier", e);
        }
        return fileDoc;
    }

    @Override
    public DocumentModel createFile(
        final CoreSession session,
        final NuxeoPrincipal principal,
        final String fileName,
        final Blob content,
        final String folderId,
        final DocumentModel dossier
    ) {
        DocumentModel docModel = null;
        // création du DocumentModel
        try {
            DocumentModel repertoireParent = session.getDocument(new IdRef(folderId));
            docModel =
                createBareFileInFolder(
                    session,
                    repertoireParent,
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE,
                    fileName,
                    content
                );
            final FondDeDossierFile fichier = docModel.getAdapter(FondDeDossierFile.class);
            fichier.setEntite(getEntite(principal));
            docModel = persistFileInFolder(session, fichier.getDocument());
            // journalisation de l'action dans les logs
            journalisationAction(
                SolonEpgEventConstant.ACTION_CREATE,
                docModel.getType(),
                session,
                dossier,
                fileName,
                repertoireParent
            );
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_FILE_TEC, e);
            throw new SSException("Erreur lors de la création du fichier", e);
        }
        return docModel;
    }

    @Override
    public DocumentModel createFondDeDossierFile(
        final CoreSession session,
        final String fileName,
        final byte[] content,
        final String folderName,
        final DocumentModel dossier
    ) {
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
    public List<FondDeDossierFolder> getChildrenFolder(CoreSession session, DocumentModel repertoireParent) {
        return (List<FondDeDossierFolder>) sortDocumentationJointeFolder(
            super.getChildrenFolder(session, repertoireParent),
            repertoireParent
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FondDeDossierFolder> getChildrenFolder(CoreSession session, DocumentRef repertoireParentRef) {
        return (List<FondDeDossierFolder>) sortDocumentationJointeFolder(
            super.getChildrenFolder(session, repertoireParentRef),
            session.getDocument(repertoireParentRef)
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FondDeDossierFile> getChildrenFile(CoreSession session, DocumentModel repertoireParent) {
        return (List<FondDeDossierFile>) sortDocumentationJointeFolder(
            super.getChildrenFile(session, repertoireParent),
            repertoireParent
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FondDeDossierFile> getChildrenFile(CoreSession session, DocumentRef repertoireParentRef) {
        return (List<FondDeDossierFile>) sortDocumentationJointeFolder(
            super.getChildrenFile(session, repertoireParentRef),
            session.getDocument(repertoireParentRef)
        );
    }

    protected List<? extends SSTreeNode> sortDocumentationJointeFolder(
        List<? extends SSTreeNode> folders,
        DocumentModel repertoireParent
    ) {
        if (
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE.equals(repertoireParent.getName()) ||
            repertoireParent
                .getPathAsString()
                .contains(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE)
        ) {
            Collections.sort(folders, new AlphabeticalNameComparator());
        }
        return folders;
    }

    @Override
    public boolean checkNameUnicity(CoreSession session, String filename, String folderId, DocumentModel dossierDoc) {
        DocumentModel folderDoc = session.getDocument(new IdRef(folderId));

        if (folderDoc != null) {
            // On vérifie que le fichier n'existe pas et qu'il n'a pas été ajouté lors de l'étape pour avis CE
            return (
                (!session.exists(new PathRef(folderDoc.getPathAsString() + "/" + filename))) &&
                (
                    !session.exists(
                        new PathRef(
                            folderDoc.getPathAsString() +
                            "/" +
                            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_CONSTANTE_DOC_MODIF_POUR_AVIS_CE +
                            filename
                        )
                    )
                )
            );
        }
        return true;
    }

    @Override
    public boolean isDossierEtapePourAvisCE(final DocumentModel dossier, final CoreSession session) {
        // Si le dossier est à l'étape 'Pour avis CE' création jeton pour le WS chercherModificationDossier (FEV 505)
        // récupère le dossierLink lié au dossier
        try {
            final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
            return corbeilleService.existsPourAvisCEStep(session, dossier.getId());
        } catch (final NuxeoException e) {
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
        final CoreSession session,
        final Dossier dossier,
        final Calendar dateDernierSaisineRectificative
    ) {
        List<DocumentModel> docs = getFilesInSaisine(session, dossier.getDocument());
        List<String> idsFilesInSaisine = new ArrayList<>();
        for (DocumentModel doc : docs) {
            idsFilesInSaisine.add(doc.getId());
        }
        return getFilesUpdated(
            getFilesNotInSaisine(session, dossier.getDocument(), idsFilesInSaisine),
            dateDernierSaisineRectificative
        );
    }

    private List<FondDeDossierFile> getFilesUpdated(
        final List<DocumentModel> listDocsFDD,
        final Calendar lastUpdateCal
    ) {
        List<FondDeDossierFile> fichiersDansFDD = new ArrayList<>();
        for (DocumentModel docFdd : listDocsFDD) {
            final FondDeDossierFile fichier = docFdd.getAdapter(FondDeDossierFile.class);
            if (fichier.getModifiedDate() == null && fichier.getDateDernierTraitement().compareTo(lastUpdateCal) > 0) {
                fichiersDansFDD.add(fichier);
            } else if (fichier.getModifiedDate() != null && fichier.getModifiedDate().compareTo(lastUpdateCal) > 0) {
                fichiersDansFDD.add(fichier);
            }
        }
        return fichiersDansFDD;
    }

    private List<DocumentModel> getFilesNotInSaisine(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final List<String> idsFilesInSaisine
    ) {
        StringBuilder query = new StringBuilder(FDD_FILES_IN_DOSSIER_QUERY);
        if (idsFilesInSaisine != null && !idsFilesInSaisine.isEmpty()) {
            query
                .append(" AND f.ecm:uuid NOT IN (")
                .append(StringHelper.getQuestionMark(idsFilesInSaisine.size()))
                .append(")");
        }
        List<Object> params = new ArrayList<>();
        params.add(dossierDoc.getId());
        params.addAll(idsFilesInSaisine);

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE,
            query.toString(),
            params.toArray(new Object[params.size()])
        );
    }

    private List<DocumentModel> getFilesInSaisine(final CoreSession session, final DocumentModel dossierDoc) {
        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE,
            FDD_FILES_IN_FOLDER_QUERY,
            new Object[] {
                dossierDoc.getId(),
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_SAISINE_RECTIFICATIVE
            }
        );
    }

    @Override
    public List<FondDeDossierFile> getAllUpdatedFilesFDDInSaisine(
        final CoreSession session,
        final Dossier dossier,
        final Calendar dateDernierSaisineRectificative
    ) {
        return getFilesUpdated(getFilesInSaisine(session, dossier.getDocument()), dateDernierSaisineRectificative);
    }

    @Override
    public Calendar getStartStepPourAvisCE(final DocumentModel dossier, CoreSession session) {
        final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
        return corbeilleService.getStartDateForRunningStep(
            session,
            dossier.getId(),
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE
        );
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
        Dossier dossier,
        CoreSession documentManager,
        String typeModification
    ) {
        StringBuilder chercherJetonQuery = new StringBuilder("SELECT j.ecm:uuid As id FROM ")
            .append(STConstant.JETON_DOC_TYPE)
            .append(" AS j WHERE j.jtd:id_doc = ?")
            .append(" AND j.jtd:type_webservice = ? ")
            .append(" AND j.jtd:type_modification = ? ");
        final List<String> params = new ArrayList<>();
        params.add(dossier.getDocument().getId());
        params.add(STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER);
        params.add(typeModification);
        List<DocumentModel> jetonListDoc = new ArrayList<>();
        try {
            jetonListDoc =
                QueryUtils.doUnrestrictedUFNXQLQueryAndFetchForDocuments(
                    documentManager,
                    chercherJetonQuery.toString(),
                    params.toArray(new String[0])
                );
        } catch (final NuxeoException e) {
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
     */
    @Override
    public List<DocumentModel> getAllFoldersWithDocuments(final CoreSession session, final Dossier dossier) {
        List<DocumentModel> fddDocList = getFddDocuments(session, dossier);
        List<DocumentModel> listFDDTrie = new ArrayList<>();
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
    public List<DocumentModel> getAllFilesFromFondDeDossierForArchivage(
        final CoreSession session,
        final Dossier dossier,
        final String folderName
    ) {
        List<DocumentModel> foldersFDD = getAllFddFolder(session, dossier); // tous les répertoires de haut niveau
        List<DocumentModel> listDocuments = new ArrayList<>();
        for (DocumentModel folder : foldersFDD) {
            if (
                folder.getTitle().toUpperCase().trim().equals(folderName.toUpperCase().trim()) ||
                folder.getName().toUpperCase().trim().equals(folderName.toUpperCase().trim())
            ) {
                List<DocumentModel> listDocsFDD = new ArrayList<>();
                try { // recherche de tous les documents qui sont à l'intérieur
                    listDocsFDD = session.getChildren(folder.getRef());
                    for (DocumentModel docFdd : listDocsFDD) {
                        getFilesFromHierarchy(session, listDocuments, docFdd);
                    }
                    break;
                } catch (NuxeoException e) {
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
    public List<DocumentModel> getAllFilesFromFondDeDossierAllFolder(final CoreSession session, final Dossier dossier) {
        List<DocumentModel> foldersFDD = getAllFddFolder(session, dossier); // tous les répertoires de haut niveau
        List<DocumentModel> listDocuments = new ArrayList<>();
        for (DocumentModel folder : foldersFDD) {
            List<DocumentModel> listDocsFDD = new ArrayList<>();
            try { // recherche de tous les documents qui sont à l'intérieur
                listDocsFDD = session.getChildren(folder.getRef());
                for (DocumentModel docFdd : listDocsFDD) {
                    getFilesFromHierarchy(session, listDocuments, docFdd);
                }
            } catch (NuxeoException e) {
                continue;
            }
        }
        return listDocuments;
    }

    /**
     * Rempli une liste avec les fichiers d'une hierarchie de DocumentModel
     * @param listDocuments : liste à remplir
     * @param DocumentModel à parcourir
     */
    private void getFilesFromHierarchy(
        final CoreSession session,
        List<DocumentModel> listDocuments,
        DocumentModel document
    ) {
        if (
            document.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE) &&
            !listDocuments.contains(document)
        ) {
            listDocuments.add(document);
        } else if (document.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
            List<DocumentModel> listChildren = session.getChildren(document.getRef());
            for (DocumentModel child : listChildren) {
                getFilesFromHierarchy(session, listDocuments, child);
            }
        }
    }

    @Override
    public List<DocumentModel> getAllFilesFromFondDeDossierForArchivageOutOfFolder(
        final CoreSession session,
        final Dossier dossier,
        final List<String> folderNames
    ) {
        List<DocumentModel> foldersFDD = getAllFddFolder(session, dossier);
        List<DocumentModel> listDocuments = new ArrayList<>();
        List<String> folderNamesUpper = listFolderToUpper(folderNames);
        for (DocumentModel folder : foldersFDD) {
            if (
                !folderNamesUpper.contains(folder.getName().trim().toUpperCase()) &&
                !folderNamesUpper.contains(folder.getTitle().trim().toUpperCase())
            ) {
                List<DocumentModel> listDocsFDD = new ArrayList<>();
                try {
                    listDocsFDD = getFilesFromFondDossierFolder(session, dossier, folder);
                    if (listDocsFDD != null) {
                        for (DocumentModel docFdd : listDocsFDD) {
                            if (
                                docFdd
                                    .getType()
                                    .equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE) &&
                                !listDocuments.contains(docFdd)
                            ) {
                                listDocuments.add(docFdd);
                            }
                        }
                    }
                } catch (NuxeoException e) {
                    continue;
                }
            }
        }
        return listDocuments;
    }

    // retoure true si des fichiers sont contenus dans la liste de folders
    @Override
    public Boolean hasFichiersInFoldersInFDD(
        final CoreSession session,
        final Dossier dossier,
        final List<String> folderNames
    ) {
        List<String> folderNamesUpper = listFolderToUpper(folderNames);
        List<DocumentModel> foldersFDD = getAllFddFolder(session, dossier);
        for (DocumentModel folder : foldersFDD) {
            if (
                folderNamesUpper.contains(folder.getName().trim().toUpperCase()) ||
                folderNamesUpper.contains(folder.getTitle().trim().toUpperCase())
            ) {
                List<DocumentModel> listDocsFDD = new ArrayList<>();
                try {
                    listDocsFDD = getFilesFromFondDossierFolder(session, dossier, folder);
                    if (listDocsFDD != null && !listDocsFDD.isEmpty()) {
                        return true;
                    }
                } catch (NuxeoException e) {
                    continue;
                }
            }
        }
        return false;
    }

    // Retourne la liste des dossiers en majuscules
    private List<String> listFolderToUpper(List<String> listFolderNames) {
        List<String> listFolderNamesUpper = new ArrayList<>();
        for (String folderName : listFolderNames) {
            listFolderNamesUpper.add(folderName.toUpperCase().trim());
        }
        return listFolderNamesUpper;
    }

    @Override
    public void sendSaisineRectificative(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final List<FondDeDossierFile> filesForCE,
        final String startComment
    ) {
        sendFilesToCE(
            session,
            dossierDoc,
            filesForCE,
            startComment,
            TypeModification.SAISINE_RECTIFICATIVE.name(),
            SolonEpgEventConstant.AFTER_SAISINE_RECTIFICATIVE
        );
    }

    @Override
    public void sendPiecesComplementaires(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final List<FondDeDossierFile> filesForCE,
        final String startComment
    ) {
        sendFilesToCE(
            session,
            dossierDoc,
            filesForCE,
            startComment,
            TypeModification.PIECE_COMPLEMENTAIRE.name(),
            SolonEpgEventConstant.AFTER_ENVOI_PIECES_COMPLEMENTAIRES
        );
    }

    private void sendFilesToCE(
        final CoreSession session,
        DocumentModel dossierDoc,
        List<FondDeDossierFile> filesForCE,
        String startComment,
        String typeModification,
        String eventName
    ) {
        // Là on va créer le jeton pour l'action envoi de pièces complémentaires/saisine rectificative
        // On récupère le dossier concerné
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        List<String> idsFichiers = filesForCE
            .stream()
            .map(fichierFDD -> getLastVersionOfDocument(session, fichierFDD))
            .filter(Objects::nonNull)
            .map(DocumentModel::getId)
            .collect(Collectors.toList());

        JetonService jetonService = STServiceLocator.getJetonService();
        jetonService.addDocumentInBasket(
            session,
            STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER,
            TableReference.MINISTERE_CE,
            dossier.getDocument(),
            dossier.getNumeroNor(),
            typeModification,
            idsFichiers
        );

        // Enregistrement dans le journal technique
        JournalService journalService = STServiceLocator.getJournalService();

        String comment = startComment;

        if (CollectionUtils.isNotEmpty(filesForCE)) {
            comment += filesForCE.stream().map(STDomainObject::getTitle).collect(Collectors.joining("', '", " '", "'"));
        }

        String eventCategory = SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER;
        journalService.journaliserAction(session, dossierDoc, eventName, comment, eventCategory);
        dossier.save(session);
        session.save();
    }

    private static DocumentModel getLastVersionOfDocument(CoreSession session, FondDeDossierFile fichierFDD) {
        DocumentModel lastVersionDocument = null;
        List<DocumentModel> fileVersionList = session.getVersions(fichierFDD.getDocument().getRef());
        if (CollectionUtils.isNotEmpty(fileVersionList)) {
            // La dernière version du document se trouve à la fin de la liste
            lastVersionDocument = Iterables.getLast(fileVersionList);
        }

        return lastVersionDocument;
    }
//Dossier dossier = doc.getAdapter(Dossier.class);
    @Override
    public void deleteFolder(CoreSession session, DocumentModel folderDoc) {
        String numeroNor = null;
        String folderDocTitle = (String) folderDoc.getPropertyValue(TITLE_PROPERTY_NAME);
        final DocumentModel repertoireParent = session.getParentDocument(folderDoc.getRef());
        if (repertoireParent != null) {
            boolean found = false;
            DocumentModel docDossier = repertoireParent;
            // remonte sur le dossier
            while (!found) {
                docDossier = session.getParentDocument(docDossier.getRef());
                if (docDossier == null) {
                    break;
                } else {
                    found = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE.equals(docDossier.getType());
                }
            }
            if(found) {
                // dossier trouvé
                Dossier dossier = docDossier.getAdapter(Dossier.class);
                numeroNor = dossier.getNumeroNor();
            }
        }
        super.deleteFolder(session, folderDoc);

        //Methode ajoutée qui va ajouter la suppression d'un repertoire dans la journalisation
        journalisationAction(
                session,
                folderDoc,
                SolonEpgEventConstant.DELETE_FOLDER_FDD,
                SolonEpgEventConstant.COMMENT_DELETE_FOLDER_FDD +
                        "[" + folderDocTitle + "]" + " depuis le dossier " + numeroNor,
                STEventConstant.CATEGORY_FDD

        );
    }

}