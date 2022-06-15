package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getFondDeDossierService;
import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getParapheurService;
import static fr.dila.solonepg.ui.jaxrs.webobject.ajax.EpgDossierAjax.DOCUMENT_NAME;
import static fr.dila.ss.core.service.SSServiceLocator.getSSTreeService;
import static fr.dila.st.core.util.ResourceHelper.getString;
import static fr.dila.st.ui.enums.STContextDataKey.ID;

import com.fasterxml.jackson.core.format.InputAccessor;
import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.fonddossier.*;
import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.vocabulary.TypeActeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgDocumentDTO;
import fr.dila.solonepg.ui.bean.EpgDossierDTO;
import fr.dila.solonepg.ui.bean.SaisineRectificativeDTO;
import fr.dila.solonepg.ui.bean.dossier.fdd.AdminSqueletteFondDTO;
import fr.dila.solonepg.ui.enums.EpgAdminFddPositionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.SourceFichierEnum;
import fr.dila.solonepg.ui.services.EpgFondDeDossierUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.services.actions.EpgDossierDistributionActionService;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.SSTreeService;
import fr.dila.ss.api.tree.SSTreeFolder;
import fr.dila.ss.ui.bean.FondDTO;
import fr.dila.ss.ui.enums.SSActionCategory;
import fr.dila.ss.ui.enums.SSActionEnum;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.schema.FileSchemaUtils;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.function.FourConsumer;
import fr.dila.st.ui.bean.DossierDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentNotFoundException;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.schema.FacetNames;
import org.nuxeo.ecm.platform.usermanager.exceptions.GroupAlreadyExistsException;

/**
 * Action Service de gestion de l'arborescence du fond de dossier.
 */
public class EpgFondDeDossierUIServiceImpl
    extends AbstractEpgTreeManagerUIService
    implements EpgFondDeDossierUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(EpgFondDeDossierUIServiceImpl.class);
    private static final String SUCCESS_MSG_RENAME_DOCUMENT = "dossier.rename.document.success";

    public EpgFondDeDossierUIServiceImpl() {
        super(EpgDocumentDTO.class);
    }

    @Override
    public FondDTO getFondDTO(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();
        DocumentModel doc = context.getCurrentDocument();
        List<FondDeDossierFolder> repertoiresRacine = getFondDeDossierService()
            .getAllVisibleRootFolder(session, doc, ssPrincipal);

        EpgUIServiceLocator.getEpgDossierUIService().setActionsInContext(context);

        FondDTO fondDTO = new FondDTO(Boolean.TRUE);
        List<DossierDTO> fddDTOList = repertoiresRacine
            .stream()
            .map(folder -> toDossierDto(folder, context))
            .collect(Collectors.toList());
        fondDTO.setDossiers(fddDTOList);
        fondDTO.setAcceptedFileTypes(getAcceptedTypes(context));
        fondDTO.setFondExportAction(context.getAction(SSActionEnum.FOND_DOSSIER_EXPORT));

        return fondDTO;
    }

    private EpgDossierDTO toDossierDto(FondDeDossierFolder fddFolder, SpecificContext context) {
        EpgDossierDTO dossierRacine = new EpgDossierDTO();
        dossierRacine.setId(fddFolder.getId());
        dossierRacine.setNom(fddFolder.getName());
        dossierRacine.setLstActions(context.getActions(SSActionCategory.FOND_DOSSIER_FOLDER_ACTIONS));

        // Récupération des fichiers du répertoire
        dossierRacine.setLstDocuments(
            getFondDeDossierService()
                .getChildrenFile(context.getSession(), fddFolder.getDocument())
                .stream()
                .map(FileSolonEpg.class::cast)
                .map(fileEpg -> toFddDocumentDTO(context, fileEpg))
                .collect(Collectors.toList())
        );
        // Récupération des sous-dossiers du répertoire
        dossierRacine.setLstFolders(
            getFondDeDossierService()
                .getChildrenFolder(context.getSession(), fddFolder.getDocument())
                .stream()
                .map(ssTreeFolder -> toSousDossierDTO(context, ssTreeFolder))
                .collect(Collectors.toList())
        );

        return dossierRacine;
    }

    private EpgDossierDTO toSousDossierDTO(SpecificContext context, SSTreeFolder ssTreeFolder) {
        EpgDossierDTO sousDossier = new EpgDossierDTO();
        sousDossier.setId(ssTreeFolder.getId());
        sousDossier.setNom(ssTreeFolder.getTitle());
        // Récupération des fichiers du répertoire
        DocumentModel treeFolderDoc = ssTreeFolder.getDocument();
        sousDossier.setLstDocuments(
            getFondDeDossierService()
                .getChildrenFile(context.getSession(), treeFolderDoc)
                .stream()
                .map(ssTreeFile -> (FileSolonEpg) ssTreeFile)
                .map(fileEpg -> toFddDocumentDTO(context, fileEpg))
                .collect(Collectors.toList())
        );
        // Récupération des sous-dossiers du répertoire
        sousDossier.setLstFolders(
            getFondDeDossierService()
                .getChildrenFolder(context.getSession(), treeFolderDoc)
                .stream()
                .map(child -> toSousDossierDTO(context, child))
                .collect(Collectors.toList())
        );
        context.putInContextData(EpgContextDataKey.IS_FDD_DELETABLE, isDeletableFdd(treeFolderDoc));
        sousDossier.setLstActions(context.getActions(SSActionCategory.FOND_DOSSIER_FOLDER_ACTIONS));
        return sousDossier;
    }

    @Override
    public Boolean isDeletableFdd(SpecificContext context) {
        return isDeletableFdd(
            context.getSession().getDocument(new IdRef(context.getFromContextData(STContextDataKey.ID)))
        );
    }

    private Boolean isDeletableFdd(DocumentModel doc) {
        return doc.getAdapter(FondDeDossierFolder.class).isDeletable();
    }

    private EpgDocumentDTO toFddDocumentDTO(SpecificContext context, FileSolonEpg file) {
        context.putInContextData(ACTION_CATEGORY_KEY, SSActionCategory.FOND_DOSSIER_FILE_ACTIONS);
        EpgDocumentDTO docDto = (EpgDocumentDTO) toDocumentDTO(context, file);
        docDto.setNonTransmis(isATransmettreSaisineOuPieceComplementaire(context, file));
        docDto.setEntite(file.getEntite());
        docDto.setModifEnCours(file.isEditing());
        docDto.setSourceFichier(SourceFichierEnum.FOND_DOSSIER);
        return docDto;
    }

    @Override
    public DocumentModel createBareFile(SpecificContext context) {
        return getFondDeDossierService().createBareFondDeDossierFichier(context.getSession());
    }

    @Override
    protected List<String> getAllowedFileTypes(SpecificContext context) {
        return getAcceptedTypes(context);
    }

    @Override
    public void createSpecificFile(Blob content, String filename, SpecificContext context) {
        final FondDeDossierService fddService = getFondDeDossierService();

        // On vérifie si le fichier existe déjà pour ne pas en ajouter un du
        // même nom : contrainte STILA
        if (
            !fddService.checkNameUnicity(
                context.getSession(),
                filename,
                context.getFromContextData(ID),
                context.getCurrentDocument()
            )
        ) {
            final String message = ResourceHelper.getString("feedback.solonepg.document.filename.double");
            throw new EPGException(message);
        }

        fddService.createFile(
            context.getSession(),
            context.getSession().getPrincipal(),
            filename,
            content,
            context.getFromContextData(ID),
            context.getCurrentDocument()
        );
    }

    @Override
    protected List<DocumentModel> getEpreuvesFiles(CoreSession session, Dossier dossier) {
        return getFondDeDossierService().getEpreuvesFiles(session, dossier);
    }

    @Override
    protected String getErrorMessageDeleteFileEpreuvage() {
        return "fondDossier.delete.file.error.epreuvage";
    }

    @Override
    protected void deleteAllFileVersion(CoreSession session, DocumentModel doc, DocumentModel dossierDoc) {
        getFondDeDossierService()
            .deleteFileWithType(
                session,
                doc,
                dossierDoc,
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE
            );
    }

    @Override
    protected void deleteFileAndRestoreToPreviousVersion(
        CoreSession session,
        String selectedNodeId,
        DocumentModel dossierDoc
    ) {
        getFondDeDossierService().restoreToPreviousVersion(session, selectedNodeId, dossierDoc);
    }

    @Override
    protected String getSuccessMessageDeleteFile() {
        return SUCCESS_MSG_DELETE_FILE;
    }

    /**
     * récupère les formats autorisés pour ajouter un fichier.
     *
     * @return acceptedTypes
     */
    private static List<String> getAcceptedTypes(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        List<String> acceptedTypes = Collections.emptyList();
        if (dossierDoc != null) {
            // récupération de la liste des formats de fichiers autorisés
            Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            acceptedTypes = dossier.getFondDeDossier(context.getSession()).getFormatsAutorises();
        }
        return acceptedTypes;
    }

    @Override
    public void createFolder(SpecificContext context) {
        super.createFolder(
            context,
            context.getSession(),
            getSelectedDocument(context),
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE,
            SolonEpgFondDeDossierConstants.DEFAULT_FDD_FOLDER_NAME
        );
    }

    /**
     * Renommage d'un répertoire.
     *
     */
    @Override
    public void renameFolder(SpecificContext context) {
        DocumentModel doc = getSelectedDocument(context);
        String newName = context.getFromContextData(EpgContextDataKey.DOSSIER_NOM);
        super.renameFolder(context, context.getSession(), doc, newName);
    }

    @Override
    public void renameFile(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentFileDoc = context.getCurrentDocument();
        FileSolonEpg currentFile = currentFileDoc.getAdapter(FileSolonEpg.class);
        String idDossier = currentFile.getRelatedDocument();
        DocumentModel document = QueryHelper.getDocument(session, idDossier);
        DocumentModel documentFolder = session.getDocument(currentFileDoc.getParentRef());
        final Blob blob = FileSchemaUtils.getContent(currentFileDoc);
        String newFileName =
            context.getFromContextData(DOCUMENT_NAME) + FileUtils.getExtensionWithSeparator(currentFile.getFilename());

        if (verifyNewFileName(newFileName, context, null)) {
            try {
                blob.setFilename(newFileName);
                if (documentFolder.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE)) {
                    getParapheurService().updateFile(session, currentFileDoc, blob, session.getPrincipal(), document);
                } else {
                    getFondDeDossierService()
                        .updateFile(session, currentFileDoc, blob, session.getPrincipal(), document);
                }
                context.getMessageQueue().addToastSuccess(getString(SUCCESS_MSG_RENAME_DOCUMENT));
            } catch (EPGException e) {
                LOGGER.error(session, STLogEnumImpl.UPDATE_FILE_FONC, e);
                context.getMessageQueue().addErrorToQueue(getString(ERROR_RENAME));
            }
        }
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
    public DossierDTO getBordereauVersementDTO(SpecificContext context) {
        CoreSession session = context.getSession();
        Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
        List<FondDeDossierFolder> repertoiresRacine = getFondDeDossierService().getAllRootFolder(session, dossier);

        return repertoiresRacine
            .stream()
            .filter(
                folder ->
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_BORDEREAU_VERSEMENT.equals(
                        folder.getName()
                    )
            )
            .map(folder -> toDossierDto(folder, context))
            .findFirst()
            .orElse(new EpgDossierDTO());
    }

    @Override
    public void editFile(SpecificContext context) {
        CoreSession session = context.getSession();
        LOGGER.info(session, STLogEnumImpl.UPDATE_FILE_FONC);

        DocumentModel oldFileDoc = context.getCurrentDocument();
        String fondDeDossierFolderId = context.getFromContextData(ID);
        DocumentModel fondDeDossierFolderDoc = getSelectedDocument(session, fondDeDossierFolderId);
        String idDossier = oldFileDoc.getAdapter(FileSolonEpg.class).getRelatedDocument();
        DocumentModel document = QueryHelper.getDocument(session, idDossier);

        ImmutablePair<String, Blob> newFile = getFile(context);
        String newFileName = newFile.getLeft();

        updateFile(context, document, fondDeDossierFolderDoc, oldFileDoc, newFileName, newFile.getRight());
        String origine = context.getFromContextData(EpgContextDataKey.ORIGINE_EDIT_DOSSIER);
        context.getMessageQueue().addSuccessToQueue(getString(SUCCESS_MSG_EDIT_FILE, origine));
    }

    @Override
    public List<String> getAdminFondFormatsAutorises(SpecificContext context) {
        FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
        CoreSession session = context.getSession();
        DocumentModel fondDossierModelRootDoc = fondDeDossierModelService.getFondDossierModelRoot(session);
        FondDeDossierModelRoot fondDossierModelRoot = fondDossierModelRootDoc.getAdapter(FondDeDossierModelRoot.class);
        return fondDossierModelRoot.getFormatsAutorises();
    }

    @Override
    public AdminSqueletteFondDTO getAdminFondDTO(SpecificContext context) {
        String typeActe = context.getFromContextData(EpgContextDataKey.FDD_TYPE);

        if (StringUtils.isBlank(typeActe)) {
            context.getMessageQueue().addErrorToQueue(getString("admin.fdd.type.acte.empty"));
            return new AdminSqueletteFondDTO();
        }

        TypeActeService typeActeService = SolonEpgServiceLocator.getTypeActeService();
        String typeActeId = typeActeService.getId(typeActe);
        // on récupère le modèle de fond de dossier lié à ce type d'acte
        final FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
        CoreSession session = context.getSession();
        final DocumentModel modeleFddDoc = fondDeDossierModelService.getFondDossierModelFromTypeActe(
            session,
            typeActeId
        );

        if (modeleFddDoc == null) {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("admin.fdd.not.found"));
        }

        return toAdminSqueletteFondDTO(session, modeleFddDoc);
    }

    private AdminSqueletteFondDTO toAdminSqueletteFondDTO(CoreSession session, DocumentModel fddModeleDocument) {
        AdminSqueletteFondDTO root = new AdminSqueletteFondDTO();
        FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
        loadChildren(
            session,
            root,
            fondDeDossierModelService.getFondDeDossierModelRootNode(session, fddModeleDocument),
            n -> fondDeDossierModelService.getChildrenFondDeDossierModelNode(n.getId(), session)
        );
        return root;
    }

    private void loadChildren(
        CoreSession session,
        AdminSqueletteFondDTO noeud,
        List<FondDeDossierModelNode> childNodes,
        Function<AdminSqueletteFondDTO, List<FondDeDossierModelNode>> grandChildrenGetter
    ) {
        childNodes
            .stream()
            .map(
                c -> {
                    AdminSqueletteFondDTO dto = new AdminSqueletteFondDTO(
                        c.getId(),
                        c.getName(),
                        c.getType() == FondDeDossierModelType.FOND_DOSSIER_FOLDER_DELETABLE
                    );
                    this.loadChildren(session, dto, grandChildrenGetter.apply(dto), grandChildrenGetter);
                    return dto;
                }
            )
            .forEach(noeud.getChildren()::add);
    }

    @Override
    public void addAdminFond(SpecificContext context) {
        Optional<EpgAdminFddPositionEnum> addPosition = context.getFromContextData(EpgContextDataKey.FDD_ADD_POSITION);

        FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
        Map<EpgAdminFddPositionEnum, FourConsumer<CoreSession, DocumentModel, String, String>> createFolderActions = ImmutableMap.of(
            EpgAdminFddPositionEnum.BEFORE,
            fondDeDossierModelService::createNewFolderNodeBefore,
            EpgAdminFddPositionEnum.IN,
            fondDeDossierModelService::createNewDefaultFolderInTree,
            EpgAdminFddPositionEnum.AFTER,
            fondDeDossierModelService::createNewFolderNodeAfter
        );

        addPosition.ifPresent(p -> this.createNewFolder(context, createFolderActions.get(p)));
    }

    public void createNewFolder(
        SpecificContext context,
        FourConsumer<CoreSession, DocumentModel, String, String> createNodeConsumer
    ) {
        DocumentModel selectedFolder = getSelectedFolder(context);
        if (selectedFolder == null) {
            return;
        }

        createNodeConsumer.accept(
            context.getSession(),
            selectedFolder,
            context.getFromContextData(EpgContextDataKey.FDD_NAME),
            context.getFromContextData(EpgContextDataKey.FDD_TYPE)
        );
        context.getMessageQueue().addToastSuccess(getString("admin.fdd.toast.ajout"));
    }

    private DocumentModel getSelectedFolder(SpecificContext context) {
        CoreSession session = context.getSession();
        String folderId = context.getFromContextData(EpgContextDataKey.FDD_ID);
        try {
            return session.getDocument(new IdRef(folderId));
        } catch (DocumentNotFoundException e) {
            LOGGER.debug(STLogEnumImpl.DEFAULT, e);
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("admin.fdd.folder.not.found"));
            return null;
        }
    }

    @Override
    public void updateAdminFond(SpecificContext context) {
        DocumentModel selectedFolder = getSelectedFolder(context);
        if (selectedFolder == null) {
            return;
        }

        final FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
        fondDeDossierModelService.editFolder(
            selectedFolder,
            context.getSession(),
            context.getFromContextData(EpgContextDataKey.FDD_NAME),
            context.getFromContextData(EpgContextDataKey.FDD_TYPE)
        );

        context.getMessageQueue().addToastSuccess(getString("admin.fdd.toast.maj"));
    }

    @Override
    public void deleteAdminFond(SpecificContext context) {
        DocumentModel selectedFolder = getSelectedFolder(context);
        if (selectedFolder == null) {
            return;
        }

        final FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
        fondDeDossierModelService.deleteFolder(
            selectedFolder,
            context.getSession(),
            context.getFromContextData(EpgContextDataKey.FDD_TYPE)
        );

        context.getMessageQueue().addToastSuccess(getString("admin.fdd.toast.suppression"));
    }

    @Override
    public void updateAdminFondFormats(SpecificContext context) {
        List<String> formats = context.getFromContextData(EpgContextDataKey.FDD_FORMATS);

        FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
        CoreSession session = context.getSession();
        DocumentModel fondDossierModelRootDoc = fondDeDossierModelService.getFondDossierModelRoot(session);
        FondDeDossierModelRoot fondDossierModelRoot = fondDossierModelRootDoc.getAdapter(FondDeDossierModelRoot.class);

        if (!CollectionUtils.isEqualCollection(fondDossierModelRoot.getFormatsAutorises(), formats)) {
            fondDossierModelRoot.setFormatsAutorises(formats);
            session.saveDocument(fondDossierModelRootDoc);
            context.getMessageQueue().addToastSuccess(getString("admin.fdd.formats.sauvegardes"));
        }
    }

    private void updateFile(
        SpecificContext context,
        DocumentModel dossierDoc,
        DocumentModel fondDeDossierFolderDoc,
        DocumentModel oldFileDoc,
        String newFileName,
        Blob newFileContent
    ) {
        CoreSession session = context.getSession();

        DocumentModel newFileDoc = getFondDeDossierService()
            .createBareFondDeDossierFichier(session, fondDeDossierFolderDoc, newFileName, newFileContent);

        FileSolonEpg file = context.getCurrentDocument().getAdapter(FileSolonEpg.class);
        Dossier dossier = session.getDocument(new IdRef(file.getRelatedDocument())).getAdapter(Dossier.class);
        List<String> acceptedTypes = dossier.getFondDeDossier(context.getSession()).getFormatsAutorises();

        if (newFileDoc.getAdapter(BlobHolder.class).getBlob().getFilename() == null) {
            context.getMessageQueue().addErrorToQueue(getString(ERROR_MSG_NO_FILE_SELECTED));
        } else if (verifyNewFileName(newFileName, context, acceptedTypes)) {
            try {
                if (
                    fondDeDossierFolderDoc.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE)
                ) {
                    getParapheurService()
                        .updateFile(session, oldFileDoc, newFileContent, session.getPrincipal(), dossierDoc);
                } else {
                    getFondDeDossierService()
                        .updateFile(session, oldFileDoc, newFileContent, session.getPrincipal(), dossierDoc);
                }
            } catch (GroupAlreadyExistsException gaee) {
                LOGGER.error(session, STLogEnumImpl.UPDATE_FILE_FONC, gaee);
                context.getMessageQueue().addErrorToQueue(ERROR_MSG_NO_VISIBILITY_SELECTED);
            }
        }
    }

    @Override
    public List<EpgDocumentDTO> getFileVersionListDto(SpecificContext context) {
        List<DocumentModel> fileVersionList = null;
        final DocumentModel currentFile = context.getCurrentDocument();
        if (currentFile != null && currentFile.hasFacet(FacetNames.VERSIONABLE)) {
            // récupération de toute les versions du documents
            fileVersionList = context.getSession().getVersions(currentFile.getRef());
            // on renvoie les élément du plus récent auplus anciens
            Collections.reverse(fileVersionList);
        }
        if (fileVersionList != null) {
            return fileVersionList
                .stream()
                .map(doc -> doc.getAdapter(FileSolonEpg.class))
                .map(doc -> toFddDocumentDTO(context, doc))
                .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @Override
    public List<SaisineRectificativeDTO> getDocumentsSaisineRectificative(SpecificContext context) {
        return SolonEpgUIServiceLocator
            .getEpgDossierDistributionUIService()
            .prepareSaisineRectificative(context)
            .stream()
            .map(this::mapFondDossierFileToSaisine)
            .collect(Collectors.toList());
    }

    @Override
    public List<SaisineRectificativeDTO> getDocumentsPiecesComplementairesSaisine(SpecificContext context) {
        return SolonEpgUIServiceLocator
            .getEpgDossierDistributionUIService()
            .prepareEnvoiPieceComplementaire(context)
            .stream()
            .map(this::mapFondDossierFileToSaisine)
            .collect(Collectors.toList());
    }

    private SaisineRectificativeDTO mapFondDossierFileToSaisine(FondDeDossierFile file) {
        SaisineRectificativeDTO dto = new SaisineRectificativeDTO();
        dto.setFichier(FilenameUtils.getName(file.getSafeFilename()));
        dto.setAuteur(file.getCreator());
        dto.setDate(file.getModifiedDate());
        dto.setVersion(file.getMajorVersion().toString());
        dto.setRepertoire(getNomParentFolderForFichier(file));
        return dto;
    }

    /**
     * Retourne le nom du dossier parent pour un fichier donné
     *
     * @param item
     * @return
     */
    private String getNomParentFolderForFichier(FileSolonEpg item) {
        String nomDossierParent = StringUtils.EMPTY;
        String itemPath = item.getDocument().getPath().toString();
        String[] listPath = itemPath.split("/");
        if (listPath.length > 2) {
            nomDossierParent = listPath[listPath.length - 2];
        }
        return nomDossierParent;
    }

    /**
     * Pour l'étape 'Pour avis CE' fonction qui regarde si nécessité de mettre une étoile devant le nom du fichier pour
     * signifier à transmettre pour saisine rectificative ou pièce complémentaire
     *
     * @param context
     * @param item
     * @return
     */
    private boolean isATransmettreSaisineOuPieceComplementaire(SpecificContext context, FileSolonEpg item) {
        EpgDossierDistributionActionService dossierDistributionActions = SolonEpgActionServiceLocator.getEpgDossierDistributionActionService();
        // On regarde si le dossier est dans l'étape pour avis CE
        if (SolonEpgActionServiceLocator.getEpgCorbeilleActionService().isEtapePourAvisCE(context)) {
            // On récupère le dossier lié au document
            DocumentModel dossierDoc = context.getCurrentDocument();
            Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            // recherche du nom du dossier parent
            String nomDossierParent = getNomParentFolderForFichier(item);
            if (
                nomDossierParent.equals(
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_SAISINE_RECTIFICATIVE
                )
            ) {
                // On regarde si le document doit être transmis pour saisine rectificative si le document est dans le
                // dossier
                Calendar dateSaisineRectificative = dossierDistributionActions.getDateDerniereSaisineRectificative(
                    context,
                    dossier
                );
                return isFileModifiedByDate(item, dateSaisineRectificative);
            } else {
                // On regarde si le document doit être transmis si le document est dans les autres dossiers
                // Dans ce cas il s'agit d'un envoi de pièces complémentaires. Ce n'est pas de la saisine rectificative
                Calendar dateEnvoiPiecesComplementaires = dossierDistributionActions.getDateDerniereTransmissionPiecesComplementaires(
                    context,
                    dossier
                );
                return isFileModifiedByDate(item, dateEnvoiPiecesComplementaires);
            }
        }
        return false;
    }
    private boolean isFileModifiedByDate(FileSolonEpg item, Calendar dateSaisineRectificative) {
        return (
            dateSaisineRectificative != null &&
            (
                item.getModifiedDate() == null &&
                item.getDateDernierTraitement() != null &&
                item.getDateDernierTraitement().compareTo(dateSaisineRectificative) > 0
            ) ||
            (item.getModifiedDate() != null && item.getModifiedDate().compareTo(dateSaisineRectificative) > 0)
        );
    }



    @Override
    public void deleteFolder(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel folder = getSelectedDocument(context);
        String idfolder = folder.getId();
        if(folder.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)){

            if (idfolder != null) {
                DocumentModel vardc = session.getDocument(new IdRef(idfolder)) ;
                getFondDeDossierService().deleteFolder(session, vardc);
            }
        }else {
            super.deleteDocument(context.getSession(), idfolder);
            context.getMessageQueue().addToastSuccess(SUCCESS_MSG_DELETE_FOLDER);
        }
    }

}


// Journalisation avec le numeroNor