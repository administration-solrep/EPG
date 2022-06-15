package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.parapheur.ParapheurInstance;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgDocumentDTO;
import fr.dila.solonepg.ui.bean.EpgDossierDTO;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.SourceFichierEnum;
import fr.dila.solonepg.ui.services.EpgParapheurUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.ss.ui.bean.FondDTO;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.DossierDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.th.enums.AlertType;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.schema.FacetNames;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.dto.CopyFileStatusDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import java.util.Optional;
public class EpgParapheurUIServiceImpl extends AbstractEpgTreeManagerUIService implements EpgParapheurUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(EpgParapheurUIServiceImpl.class);

    private static final String SUCCESS_MSG_ADD_FILE = "parapheur.add.file.success";
    private static final String SUCCESS_MSG_DELETE_FILE = "parapheur.delete.file.success";
    private static final String ERROR_MSG_ADD_FILE = "parapheur.add.file.error";

    public EpgParapheurUIServiceImpl() {
        super(EpgDocumentDTO.class);
    }

    @Override
    public FondDTO getParapheurDTO(SpecificContext context) {
        DocumentModel parapheurDoc = getParapheurDocument(context);
        List<ParapheurFolder> repertoiresRacine = SolonEpgServiceLocator
            .getParapheurService()
            .getParapheurRootNode(context.getSession(), parapheurDoc);

        EpgUIServiceLocator.getEpgDossierUIService().setActionsInContext(context);

        FondDTO parapheurDTO = new FondDTO(Boolean.TRUE);
        List<DossierDTO> parapheurDTOList = repertoiresRacine
            .stream()
            .map(folder -> toDossierDto(folder, context))
            .collect(Collectors.toList());
        parapheurDTO.setDossiers(parapheurDTOList);
        parapheurDTO.setFondExportAction(context.getAction(EpgActionEnum.PARAPHEUR_EXPORT));

        return parapheurDTO;
    }

    private static DocumentModel getParapheurDocument(SpecificContext context) {
        // the Dossier document is the current Document
        DocumentModel doc = context.getCurrentDocument();
        Dossier dossier = doc.getAdapter(Dossier.class);
        ParapheurInstance parapheur = dossier.getParapheur(context.getSession());
        return parapheur.getDocument();
    }

    private EpgDossierDTO toDossierDto(ParapheurFolder folder, SpecificContext context) {
        EpgDossierDTO dfd = new EpgDossierDTO();
        dfd.setId(folder.getId());
        dfd.setNom(folder.getName());
        if (canUserAddFileInFolder(context, folder.getName())) {
            // Il y a un contrôle à faire sur certains dossiers où il y a un nb max (=1) de fichiers par dossier
            dfd.setLstActions(context.getActions(EpgActionCategory.PARAPHEUR_FOLDER_ACTIONS));
        } else {
            Optional.ofNullable(context.getAction(EpgActionEnum.COLLER_DOCUMENT_PARAPHEUR)).ifPresent(action -> dfd.setLstActions(Collections.singletonList(action)));
        }
        dfd.setAcceptedFileTypes(getAcceptedTypes(context, folder.getDocument().getId()));
        // Récupération des fichiers du répertoire
        dfd.setLstDocuments(
            SolonEpgServiceLocator
                .getParapheurService()
                .getChildrenFile(context.getSession(), folder.getDocument())
                .stream()
                .map(ssTreeFile -> (FileSolonEpg) ssTreeFile)
                .map(fileEpg -> toParapheurDocumentDTO(context, fileEpg))
                .collect(Collectors.toList())
        );

        return dfd;
    }

    private EpgDocumentDTO toParapheurDocumentDTO(SpecificContext context, FileSolonEpg file) {
        context.putInContextData(ACTION_CATEGORY_KEY, EpgActionCategory.PARAPHEUR_FILE_ACTIONS);
        EpgDocumentDTO docDto = (EpgDocumentDTO) toDocumentDTO(context, file);
        docDto.setEntite(file.getEntite());
        docDto.setModifEnCours(file.isEditing());
        docDto.setSourceFichier(SourceFichierEnum.PARAPHEUR);
        return docDto;
    }

    @Override
    public boolean canUserAddFileInFolder(SpecificContext context, String folderName) {
        DocumentModel dossier = context.getCurrentDocument();
        DocumentModel parapheurDoc = SolonEpgServiceLocator
            .getParapheurService()
            .getParapheurFolder(dossier, context.getSession(), folderName);

        int nbCurrentDoc = SolonEpgServiceLocator
            .getParapheurService()
            .getChildrenFile(context.getSession(), parapheurDoc)
            .size();

        // Si la limite de nombre maximal de fichier a été atteinte on desactive l'upload
        Long nbDocMax = parapheurDoc.getAdapter(ParapheurFolder.class).getNbDocAccepteMax();
        // Certains répertoires n'ont pas de nombre max
        return (nbDocMax == null || nbCurrentDoc < nbDocMax);
    }

    @Override
    public DocumentModel createBareFile(SpecificContext context) {
        return SolonEpgServiceLocator.getParapheurService().createBareParapheurFile(context.getSession());
    }

    @Override
    protected List<DocumentModel> getEpreuvesFiles(CoreSession session, Dossier dossier) {
        return SolonEpgServiceLocator.getParapheurService().getEpreuvesFiles(session, dossier);
    }

    @Override
    protected String getErrorMessageDeleteFileEpreuvage() {
        return "parapheur.delete.file.error.epreuvage";
    }

    @Override
    protected void deleteAllFileVersion(CoreSession session, DocumentModel doc, DocumentModel dossierDoc) {
        ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
        parapheurService.deleteFile(session, doc, dossierDoc);
        parapheurService.checkParapheurComplet(dossierDoc, session);
    }

    @Override
    protected void deleteFileAndRestoreToPreviousVersion(
        CoreSession session,
        String selectedNodeId,
        DocumentModel dossierDoc
    ) {
        SolonEpgServiceLocator.getParapheurService().restoreToPreviousVersion(session, selectedNodeId, dossierDoc);
    }

    @Override
    protected String getSuccessMessageDeleteFile() {
        return SUCCESS_MSG_DELETE_FILE;
    }

    @Override
    protected List<String> getAllowedFileTypes(SpecificContext context) {
        return getAcceptedTypes(context, context.getFromContextData(STContextDataKey.ID));
    }

    @Override
    public void createSpecificFile(Blob content, String filename, SpecificContext context) throws IOException {
        final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
        CoreSession session = context.getSession();
        ParapheurFolder parapheurFolder = getSelectedDocument(context).getAdapter(ParapheurFolder.class);
        // récupération des métadonnées du dossier
        InputStream fichierStream = null;

        try {
            fichierStream = content.getStream();
        } catch (IOException exc) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_STREAM_FONC, exc);
        }

        // On vérifie si le fichier existe déjà pour ne pas en ajouter un du même nom : contrainte STILA
        if (
            !parapheurService.checkNameUnicity(
                session,
                filename,
                parapheurFolder.getName(),
                context.getCurrentDocument()
            )
        ) {
            throw new EPGException(ResourceHelper.getString("feedback.solonepg.document.filename.double"));
        }

        String feuilleStyleError = null;
        if (fichierStream != null) {
            feuilleStyleError =
                SolonEpgServiceLocator
                    .getFeuilleStyleService()
                    .updateDossierMetadataFromParapheurFile(
                        session,
                        fichierStream,
                        filename,
                        getSelectedDocument(session, context.getFromContextData(STContextDataKey.ID)),
                        context.getCurrentDocument()
                    );
        }
        parapheurService.createParapheurFile(
            session,
            filename,
            content.getByteArray(),
            parapheurFolder.getName(),
            context.getCurrentDocument()
        );

        if (feuilleStyleError != null) {
            String message = ResourceHelper.getString(feuilleStyleError);
            if (feuilleStyleError.equals(SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_DATE_BAD_FORMAT)) {
                // Affiche un message pour signaler à l'utilisateur que la date de signature n'a pas pu être reportée
                // dans le bordereau
                message = ResourceHelper.getString(SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_DATE_BAD_FORMAT);
            } else if (feuilleStyleError.equals(SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_EXCEPTION_THROW)) {
                // Affiche un message pour signaler à l'utilisateur que le titre et la date de signature n'ont pas pu
                // être reportés dans le bordereau
                message = ResourceHelper.getString(SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_EXCEPTION_THROW);
            } else if (feuilleStyleError.equals(SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_TITRE_ACTE_NOT_FOUND)) {
                // Affiche un message pour signaler à l'utilisateur que le titre et la date de signature n'ont pas pu
                // être reportés dans le bordereau
                message = ResourceHelper.getString(SolonEpgParapheurConstants.PARAPHEUR_ERROR_MSG_TITRE_ACTE_NOT_FOUND);
            }
            context.getMessageQueue().addMessageToQueue(message, AlertType.ALERT_INFO);
        }
    }

    /**
     * récupère les formats autorisés pour ajouter un fichier.
     *
     * @return acceptedTypes
     */
    private static List<String> getAcceptedTypes(SpecificContext context, String folderId) {
        DocumentModel selectedFolder = getSelectedDocument(context.getSession(), folderId);
        List<String> acceptedTypes = Collections.emptyList();
        if (selectedFolder != null) {
            // récupération de la liste des formats de fichiers autorisés
            if (
                selectedFolder.hasFacet(FacetNames.VERSIONABLE) ||
                SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE.equals(selectedFolder.getType())
            ) {
                // récupération du répertoire parent du fichier
                selectedFolder = context.getSession().getParentDocument(selectedFolder.getRef());
            }
            // récupération des formats autorisés pour le répertoire
            final ParapheurFolder parapheurFolder = selectedFolder.getAdapter(ParapheurFolder.class);
            acceptedTypes = parapheurFolder.getFormatsAutorises();
        }
        return acceptedTypes;
    }

    @Override
    protected String getAddFileSuccessMessage() {
        return SUCCESS_MSG_ADD_FILE;
    }

    @Override
    protected String getAddFileErrorMessage() {
        return ERROR_MSG_ADD_FILE;
    }

    @Override
    public void moveFileToAnotherFolder(SpecificContext context) {
        String fileId = context.getFromContextData(EpgContextDataKey.ID_FILE);
        String folderId = context.getFromContextData(EpgContextDataKey.ID_FOLDER);
        CoreSession session = context.getSession();

        CopyFileStatusDTO status = SolonEpgServiceLocator
                .getDndService()
                .processMove(session, fileId, folderId, session.getPrincipal(), context.getCurrentDocument());
        if (status.isSuccess()) {
            context.getMessageQueue().addToastSuccess(status.getMessage());
            UserSessionHelper.putUserSessionParameter(context, SolonEpgConstant.CUT_FILE_SESSION_KEY, null);
        } else {
            context.getMessageQueue().addErrorToQueue(status.getMessage());
        }
    }
}
