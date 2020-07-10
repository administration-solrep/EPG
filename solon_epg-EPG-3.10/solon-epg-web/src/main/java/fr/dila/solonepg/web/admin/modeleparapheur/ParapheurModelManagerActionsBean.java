package fr.dila.solonepg.web.admin.modeleparapheur;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remove;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.platform.ui.web.util.files.FileUtils;
import org.nuxeo.ecm.webapp.filemanager.UploadItemHolder;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.parapheur.ParapheurModel;
import fr.dila.solonepg.api.service.FeuilleStyleService;
import fr.dila.solonepg.api.service.ParapheurModelService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.recherche.SolonEpgVocSugUI;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Bean seam de gestion de l'arborescence des modèles de parapheur.
 * 
 * @author ARN
 */
@Name("parapheurModelManagerActions")
@Scope(ScopeType.SESSION)
public class ParapheurModelManagerActionsBean implements Serializable {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -3486558978767123945L;

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(ParapheurModelManagerActionsBean.class);

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected FacesMessages facesMessages;

    @In(create = true)
    protected ResourcesAccessor resourcesAccessor;

    /**
     * utilisé pour récupérer la liste des ficheirs des feuilles de style.
     */
    @In(create = true, required = false)
    protected UploadItemHolder fileUploadHolder;

    // utilisé pour récupérer le modèle de parapheur courant
    @In(create = true, required = false)
    protected transient NavigationContext navigationContext;

    /**
     * Identifiant du noeud selectionné lors de la sélection.
     */
    protected String selectedNodeId;

    /**
     * Identifiant du noeud selectionné pour la suppression (utilisé dans la popup de confirmation).
     */
    protected String selectedNodeIdPopupDelete;

    /**
     * DocumentModel selectionné pour la suppression (utilisé dans la popup de confirmation).
     */
    protected DocumentModel selectedNodeDocumentPopupDelete;

    /**
     * Liste du format de fichier courant.
     */
    protected String currentFormatFichier;

    /**
     * Liste des identifiants des formats de fichiers autorisés pour les modèles de parapheur.
     */
    protected List<String> formatFichierAutoriseFddIds;

    /**
     * Modèle de parapheur sélectionné.
     */
    protected DocumentModel currentModel;

    /**
     * Type d'acte
     */
    protected String typeActe;

    /**
     * Label du Type d'acte
     */
    protected String typeActeLabel;

    /**
     * Répertoire de l'arborescence du modèle de parapheur sélectionné.
     */
    protected DocumentModel currentModelRepository;

    /**
     * Signale qu'un répertoire du parapheur est sélectionné et est éditable : attribut utilisé pour masquer les propriétés éditables du répertoire du modèle.
     */
    protected Boolean isModelRepositoryEditable;

    /**
     * Signale qu'un modèle est sélectionné et est éditable : attribut utilisé pour masquer les propriétés éditables du modèle.
     */
    protected Boolean isModelEditable;

    /**
     * Liste des fichiers du répertoire courant.
     */
    protected Collection<UploadItem> uploadData;

    /**
     * Nom du fichier de feuille de styleCourant
     */
    protected String currentFileName;

    /**
     * Propriété liées à l'affichage des erreurs.
     */
    protected String errorName;

    @Remove
    @Destroy
    public void destroy() {
        log.debug("Removing user workspace actions bean");
    }

    /**
     * Actions de l'espace d'administration des modèles de parapheur.
     */

    /**
     * Enleve un format de fichier dans la liste des formats de fichier autorisé pour les modèles de parapheur
     */
    public void deleteFormatFichierToRacine(final String idVocabulaire) {
        // récupération de la liste des fichiers autorisés
        final ParapheurModelService parapheurModelService = SolonEpgServiceLocator.getParapheurModelService();
        List<String> listFormatAutorise;
        try {
            listFormatAutorise = parapheurModelService.getFormatFile(currentModelRepository);
            listFormatAutorise.remove(idVocabulaire);
            parapheurModelService.updateFormatFile(currentModelRepository, listFormatAutorise);
        } catch (final ClientException e) {
            throw new RuntimeException("Erreur lors de la suppression d'un format de fichier", e);
        }
        Events.instance().raiseEvent(SolonEpgParapheurConstants.PARAPHEUR_MODEL_CHANGED_EVENT);
    }

    /**
     * Enleve un fichier feuille de style de la liste des feuilles de style du répertoire sélectionné.
     * 
     * @author antoine Rolin
     * 
     * @param fileToDelete
     */
    public void deleteCurrentModelFichier(final Object fileToDelete) {
        final ParapheurFolder parapModelFolderDoc = currentModelRepository.getAdapter(ParapheurFolder.class);
        // récupération de la liste de fichier
        final List<Map<String, Serializable>> currentList = parapModelFolderDoc.getFeuilleStyleFiles();
        // suppression le fichier de la liste
        currentList.remove(fileToDelete);
        // on met à jour la liste de fichier dans le document
        parapModelFolderDoc.setFeuilleStyleFiles(currentList);
        //
        Events.instance().raiseEvent(SolonEpgParapheurConstants.PARAPHEUR_MODEL_CHANGED_EVENT);
    }

    /**
     * Sauvegarde la liste des formats de fichier dans la liste des formats de fichier autorisé pour les modèles de parapheur
     */
    public void saveFormatFichierList() {
        // récupération du type de format à ajouter
        final String formatAAjouter = getCurrentFormatFichier();
        final ParapheurModelService parapheurModelService = SolonEpgServiceLocator.getParapheurModelService();
        // récupération de la liste des fichiers autorisés
        List<String> listFormatAutorise;
        try {
            listFormatAutorise = parapheurModelService.getFormatFile(currentModelRepository);
            // on ajoute le format à la liste des formats autorisés si il n'est pas déjà contenu dans la liste
            if (!listFormatAutorise.contains(formatAAjouter) && formatAAjouter != null && !formatAAjouter.isEmpty()) {
                listFormatAutorise.add(formatAAjouter);
                // mise à jour de la liste des ficheris autorisés dans le répertoire courant
                parapheurModelService.updateFormatFile(currentModelRepository, listFormatAutorise);
                // on met à jour l'affichage
                setFormatFichierAutoriseFddIds(listFormatAutorise);
            }
        } catch (final ClientException e) {
            throw new RuntimeException("Erreur lors de l'ajout d'un format de fichier", e);
        }
        // on met à jour l'affichage
        setCurrentFormatFichier("");
        // Events.instance().raiseEvent(SolonEpgParapheurConstants.PARAPHEUR_MODEL_CHANGED_EVENT);
    }

    /**
     * Récupère le modèle de parapheur lié au type d'acte sélectionné, et affiche l'arborescence de répertorie lié à ce modèle.
     */
    public void editParapheurModel(final SolonEpgVocSugUI suggestion) {
        // on récupère le type d'acte sélectionné
        final String typeActe = suggestion.getMotCleId();
        if (typeActe != null) {
            try {
                // on récupère le modèle de parapheur lié à ce type d'acte
                final ParapheurModelService parapheurModelService = SolonEpgServiceLocator.getParapheurModelService();
                final DocumentModel modeleFddDoc = parapheurModelService.getParapheurModelFromTypeActe(documentManager, typeActe);
                // on définit ce modèle de parapheur comme document courant
                currentModel = modeleFddDoc;
                isModelEditable = true;
                // on signale que le modèle de parapheur a changé et on reinitialise les variables liés au l'ancien document courant de l'arborescence.
                resetParapheurModelRepositoryValue();
                Events.instance().raiseEvent(SolonEpgParapheurConstants.PARAPHEUR_MODEL_CHANGED_EVENT);
            } catch (final ClientException e) {
                throw new RuntimeException("impossible de récupérer le modèle lié au type d'acte", e);
            }
        }
    }

    /**
     * Enregistre les modifications apportées dans les propriétés d’un répertoire du parapheur.
     */
    public void validerParapheurModelRepository() {
        final ParapheurModelService parapheurModelService = SolonEpgServiceLocator.getParapheurModelService();
        try {
            // //on mets à jour les fichiers du formulaire
            validateMultipleUploadForDocument();
            parapheurModelService.saveFolder(currentModelRepository, documentManager, getCurrentTypeActeLabel());
        } catch (final ClientException e) {
            throw new RuntimeException("erreur lors de la validation du document", e);
        }

        // on réinitialise le formulaire d'édition du répertoire
        resetParapheurModelRepositoryValue();
        // on signale que le modèle de parapheur a changé
        Events.instance().raiseEvent(SolonEpgParapheurConstants.PARAPHEUR_MODEL_CHANGED_EVENT);
    }

    /**
     * Annule les modifications apportées dans les propriétés d’un répertoire du parapheur.
     */
    public void annulerParapheurModelRepository() {
        // on réinitialise le formulaire d'édition du répertoire
        resetParapheurModelRepositoryValue();
    }

    /**
     * renvoie vrai si le répertoire courant est un répertoire de type acte ou extrait
     * 
     * @return vrai si le répertoire courant est un répertoire de type acte ou extrait
     * @throws ClientException
     */
    public boolean isRepertoireActeOuExtrait() throws ClientException {
        final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
        return parapheurService.isFolderExtraitOrFolderActe(currentModelRepository, documentManager);
    }

    /**
     * Met à jour le repertoire courant sélectionné dans l'arborescence du parapheur.
     * 
     * @author antoine Rolin
     */
    public void editFolder() {
        final DocumentModel selectedFolder = getSelectedFolder();
        if (selectedFolder != null) {
            // on met à jour le repertoire courant sélectionné dans l'arborescence du parapheur
            setCurrentModelRepository(selectedFolder);
            final ParapheurFolder parapheurModel = selectedFolder.getAdapter(ParapheurFolder.class);
            // on met à jour les types de fichiers autorisés
            formatFichierAutoriseFddIds = parapheurModel.getFormatsAutorises();

            // on signale qu'un répertoire est sélectionné et éditable
            setIsModelRepositoryEditable(true);
            // on définit l'id sélectionné à nul
            selectedNodeId = null;
            // suppression de toutes les feuilles de styles courantes
            resetCurrentFeuilleStyle();
            // nettoyage des erreurs
            errorName = "";
        }
    }

    /**
     * Actions du répertoire de l'arborescence des modèles de parapheur.
     */

    protected DocumentModel getSelectedFolder() {
        DocumentModel selectedFolder = null;
        // on récupère le document à l'aide de son identifiant
        final String selectedNodeId = getSelectedNodeId();
        if (selectedNodeId != null) {
            try {
                selectedFolder = documentManager.getDocument(new IdRef(selectedNodeId));
            } catch (final ClientException e) {
                throw new RuntimeException("impossible de récupérer le répertoire sélectionné", e);
            }
        }
        return selectedFolder;
    }

    /**
     * réinitialise les variable liés à l'édition d'un modèle de parapheur
     * 
     * @author antoine Rolin
     */
    @Observer(value = { SolonEpgParapheurConstants.PARAPHEUR_MODEL_RESET_EVENT })
    public void resetParapheurModelValue() {
        this.currentModel = null;
        this.typeActe = null;
        this.typeActeLabel = null;
        this.isModelEditable = false;
        resetParapheurModelRepositoryValue();
        this.errorName = null;
    }

    /**
     * réinitialise les variable liés à l'édition d'un répertoire du modèle de parapheur
     * 
     * @author antoine Rolin
     */
    public void resetParapheurModelRepositoryValue() {
        this.selectedNodeId = null;
        this.selectedNodeIdPopupDelete = null;
        this.selectedNodeDocumentPopupDelete = null;
        this.isModelRepositoryEditable = false;
        this.errorName = null;
        resetCurrentFeuilleStyle();

    }

    public void clearUploadData(final ActionEvent event) {
        try {
            // test si il faut supprimer un ou tout les fichiers de feuille de style
            if (currentFileName != null && !"".equals(currentFileName)) {
                // HACK le caractère & est le seul qui soit pas décodé via la paramètre "noEscape" du composant a4j:actionparam
                final String fileToClear = currentFileName.replace("&amp;", "&");
                UploadItem fileToDelete = null;
                for (final UploadItem file : uploadData) {
                    final String uploadedFileName = file.getFileName();
                    if (fileToClear.equals(uploadedFileName)) {
                        fileToDelete = file;
                        break;
                    }
                }
                if (null != fileToDelete) {
                    fileToDelete.getFile().delete();
                    uploadData.remove(fileToDelete);
                }
                currentFileName = null;
                // nettoyage des erreurs
                errorName = "";
            }

            else {
                // suppression de toutes les feuilles de styles courantes
                resetCurrentFeuilleStyle();
                // nettoyage des erreurs
                errorName = "";
            }
        } catch (final Exception e) {
            log.debug(e.getMessage());
        }
    }

    /**
     * suppression de toutes les feuilles de styles courantes
     */
    protected void resetCurrentFeuilleStyle() {
        if (uploadData != null) {
            for (final UploadItem item : uploadData) {
                item.getFile().delete();
            }
            uploadData.clear();
        }
    }

    /**
     * Préviens une exception el si le nom de fichier contient les caractères suivant '#{' ou '${'
     * 
     * @param fileName
     * @return
     */
    public String displayFileName(String fileName) {
        // Hack prévient une exception el
        fileName = fileName.replace("#{", "# {");
        fileName = fileName.replace("${", "$ {");
        return fileName;
    }
    
    /**
     * Controle l'accès à la vue correspondante
     * 
     */
    public boolean isAccessAuthorized() {
    	SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
    	return (ssPrincipal.isAdministrator() || ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_PARAPHEUR));
    }

    /**
     * Met à jour la liste des documents courants.
     * 
     * @author antoine Rolin
     */
    public void validateMultipleUploadForDocument() throws ClientException {
        try {
            // récupération de la site des fichiers à partir du répertoire sélectionné
            final ParapheurFolder parapheurModel = currentModelRepository.getAdapter(ParapheurFolder.class);

            List<Map<String, Serializable>> filesList = parapheurModel.getFeuilleStyleFiles();
            if (filesList == null) {
                filesList = new ArrayList<Map<String, Serializable>>();
            }
            // on parcourt les fichiers téléchargés
            if (uploadData != null) {
                final FeuilleStyleService feuilleStyleService = SolonEpgServiceLocator.getFeuilleStyleService();
                // on récupère le type de répertoire
                final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
                final boolean isActeIntegral = parapheurService.isActeIntegral(parapheurModel, documentManager);
                for (final UploadItem uploadItem : uploadData) {
                    // get file name
                    final String filename = FileUtils.getCleanFileName(uploadItem.getFileName());
                    final File feuilleStyleFile = uploadItem.getFile();
                    // on prend en parametre le fichier + la session et on renvoie un fichier

                    String uniqueId;
                    try {
                        uniqueId = feuilleStyleService.validateFeuilleStyle(feuilleStyleFile, documentManager, isActeIntegral);
                        if (uniqueId == null || uniqueId.isEmpty()) {
                            // on met à jour un message pour signaler que le fichier n'as pas pu être enregistré
                            final String message = resourcesAccessor.getMessages().get("feedback.solonepg.document.admin.feuilleStyleNonEnregistree");
                            facesMessages.add(StatusMessage.Severity.WARN, message, filename);
                        } else {
                            final Blob blob = FileUtils.createSerializableBlob(new FileInputStream(feuilleStyleFile), filename, null);
                            final Map<String, Serializable> fileMap = new HashMap<String, Serializable>();
                            fileMap.put(DossierSolonEpgConstants.FEUILLE_STYLE_ID_PROPERTY, uniqueId);
                            fileMap.put(DossierSolonEpgConstants.FEUILLE_STYLE_FILE_PROPERTY, (Serializable) blob);
                            fileMap.put(DossierSolonEpgConstants.FEUILLE_STYLE_FILENAME_PROPERTY, filename);
                            filesList.add(fileMap);
                        }
                    } catch (final Exception e) {
                        log.warn("erreur lors de la création de l'identifiant unique de la feuille de style");
                        throw new ClientException(e);
                    }

                }
            }
            parapheurModel.setFeuilleStyleFiles(filesList);
        } finally {
            if (uploadData != null) {
                for (final UploadItem uploadItem : uploadData) {
                    uploadItem.getFile().delete();
                }
                uploadData.clear();
            }
        }
    }

    /**
     * Listener sur l'upload d'un fichier : on ajoute le fichier dans la liste
     * 
     */
    public void fileUploadListener(final UploadEvent event) throws Exception {
        log.info("fileUploadListener fileSelected on");
        if (event != null && event.getUploadItem() != null && event.getUploadItem().getFileName() != null) {
            // recupèration du fichier
            if (uploadData == null) {
                uploadData = new ArrayList<UploadItem>();
            }
            // on vérifie que la feuille de style est bien un document texte openoffice et a bien les propriétés souhaitées
            final FeuilleStyleService feuilleStyleService = SolonEpgServiceLocator.getFeuilleStyleService();
            // on récupère le type de répertoire
            final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
            final ParapheurFolder parapheurFolder = currentModelRepository.getAdapter(ParapheurFolder.class);
            final boolean isActeIntegral = parapheurService.isActeIntegral(parapheurFolder, documentManager);
            if (feuilleStyleService.checkFeuilleStyleValid(event.getUploadItem().getFile(), event.getUploadItem().getFileName(), documentManager,
                    isActeIntegral)) {
                uploadData.add(event.getUploadItem());
                errorName = "";
            } else {
                // on envoie un mesage pour signaler que la feuille de style n'est pas valide
                final String message = resourcesAccessor.getMessages().get("feedback.solonepg.document.admin.feuilleStyleNonValide");
                facesMessages.add(StatusMessage.Severity.WARN, message);
                errorName = "La feuille de style proposée n'est pas valide et ne sera pas enregistrée en l'état!";
            }

        }

    }

    /**
     * Getter/setter pour les fichiers de feuille de style.
     */

    public Collection<UploadItem> getUploadedFiles() {
        if (fileUploadHolder != null) {
            return fileUploadHolder.getUploadedFiles();
        } else {
            return null;
        }
    }

    public void setUploadedFiles(final Collection<UploadItem> uploadedFiles) {
        if (fileUploadHolder != null) {
            fileUploadHolder.setUploadedFiles(uploadedFiles);
        }
    }

    /**
     * Getter/setter
     */

    public String getSelectedNodeId() {
        return selectedNodeId;
    }

    public void setSelectedNodeId(final String selectedNodeId) {
        this.selectedNodeId = selectedNodeId;
    }

    public void setSelectedNodeDocumentPopupDelete(final DocumentModel selectedNodeDocumentPopupDelete) {
        this.selectedNodeDocumentPopupDelete = selectedNodeDocumentPopupDelete;
    }

    public String getCurrentTypeActe() {
        if (!StringUtils.isEmpty(typeActe)) {
            return typeActe;
        }
        if (getCurrentModel() == null) {
            return null;
        }
        final ParapheurModel parapheurModel = getCurrentModel().getAdapter(ParapheurModel.class);
        return parapheurModel.getTypeActe();
    }

    public String getCurrentTypeActeLabel() {
        if (!StringUtils.isEmpty(typeActeLabel)) {
            return typeActeLabel;
        }
        if (StringUtils.isEmpty(getCurrentTypeActe())) {
            return null;
        }
        final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
        return vocabularyService.getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY, getCurrentTypeActe());
    }

    public String getCurrentFormatFichier() {
        return currentFormatFichier;
    }

    public void setCurrentFormatFichier(final String currentFormatFichier) {
        this.currentFormatFichier = currentFormatFichier;
    }

    public List<String> getFormatFichierAutoriseFddIds() {
        return formatFichierAutoriseFddIds;
    }

    public void setFormatFichierAutoriseFddIds(final List<String> formatFichierAutoriseFddIds) {
        this.formatFichierAutoriseFddIds = formatFichierAutoriseFddIds;
    }

    @Factory(value = "currentParapheurModel")
    @Observer(value = { SolonEpgParapheurConstants.PARAPHEUR_MODEL_CHANGED_EVENT })
    public DocumentModel getCurrentModel() {
        if (currentModel == null) {
            final DocumentModel docModel = navigationContext.getCurrentDocument();
            if (docModel != null && docModel.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_TYPE)) {
                currentModel = navigationContext.getCurrentDocument();
            }
        }
        return currentModel;
    }

    public void setCurrentModel(final DocumentModel currentModel) {
        this.currentModel = currentModel;
    }

    public DocumentModel getCurrentModelRepository() {
        return currentModelRepository;
    }

    public void setCurrentModelRepository(final DocumentModel currentModelRepository) {
        this.currentModelRepository = currentModelRepository;
    }

    public Boolean getIsModelRepositoryEditable() {
        return isModelRepositoryEditable;
    }

    public void setIsModelRepositoryEditable(final Boolean isModelRepositoryEditable) {
        this.isModelRepositoryEditable = isModelRepositoryEditable;
    }

    public Boolean getIsModelEditable() {
        return isModelEditable;
    }

    public void setIsModelEditable(final Boolean isModelEditable) {
        this.isModelEditable = isModelEditable;
    }

    public Collection<UploadItem> getUploadData() {
        return uploadData;
    }

    public void setUploadData(final Collection<UploadItem> uploadData) {
        this.uploadData = uploadData;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public void setCurrentFileName(final String currentFileName) {
        this.currentFileName = currentFileName;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(final String errorName) {
        this.errorName = errorName;
    }

}
