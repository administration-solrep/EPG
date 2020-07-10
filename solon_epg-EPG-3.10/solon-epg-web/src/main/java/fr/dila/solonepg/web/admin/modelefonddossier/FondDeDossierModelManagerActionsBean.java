package fr.dila.solonepg.web.admin.modelefonddossier;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Remove;

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
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModel;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModelRoot;
import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.recherche.SolonEpgVocSugUI;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Bean seam de gestion de l'arborescence des modèles de fond de dossier.
 * 
 * @author ARN
 */
@Name("fondDeDossierModelManagerActions")
@Scope(ScopeType.CONVERSATION)
public class FondDeDossierModelManagerActionsBean implements Serializable {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -3486558978767123945L;

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(FondDeDossierModelManagerActionsBean.class);

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected FacesMessages facesMessages;

    @In(create = true)
    protected ResourcesAccessor resourcesAccessor;

    // utilisé pour récupérer le modèle de fond de dossier courant
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
     * Répertoire Racine de l'espace d'administration contenant tous le smodèles de fond de dossier.
     */
    protected DocumentModel fondDossierModelRoot;

    /**
     * Liste du format de fichier courant.
     */
    protected String currentFormatFichier;

    /**
     * Liste des identifiants des formats de fichiers autorisés pour les modèles de fond de dossier.
     */
    protected List<String> formatFichierAutoriseFddIds;

    /**
     * Modèle de fond de dossier courant sélectionné.
     */
    protected DocumentModel currentFondDossierModel;

    /**
     * Type d'acte
     */
    protected String typeActe;

    /**
     * Label du Type d'acte
     */
    protected String typeActeLabel;

    /**
     * Répertoire de l'arborescence du modèle de fond de dossier sélectionné.
     */
    protected DocumentModel currentFondDeDossierModelRepository;

    /**
     * Signale qu"un répertoire du fond de dossier est sélectionné et est éditable : attribut utilisé pour masquer les propriétés éditables du répertoire du modèle fond de dossier.
     */
    protected Boolean isFondDossierModelRepositoryEditable;

    /**
     * Signale qu'un modèle de fond de dossier est sélectionné et est éditable : attribut utilisé pour masquer les propriétés éditables du modèle de fond de dossier.
     */
    protected Boolean isFondDossierModelEditable;

    @Remove
    @Destroy
    public void destroy() {
        log.debug("Removing user workspace actions bean");
    }

    /**
     * Actions de l'espace d'administration des modèles de fond de dossier.
     */

    /**
     * Ajoute un format de fichier dans la liste des formats de fichier autorisé pour les modèles de fond de dossier
     */
    public void majFormatFichierList(final String idVocabulaire) {
        // récupération du type de format à ajouter
        setCurrentFormatFichier(idVocabulaire);
    }

    /**
     * Sauvegade la liste des formats de fichier dans la liste des formats de fichier autorisé pour les modèles de fond de dossier
     */
    public void saveFormatFichierList() throws ClientException {
        // récupération du type de format à ajouter
        final String formatAAjouter = getCurrentFormatFichier();
        // récupération de la liste des fichiers autorisés
        final List<String> listFormatAutorise = getFormatFichierAutoriseFddIds();
        // on ajoute le format à la liste des formats autorisés si il n'est pas déjà contenu dans la liste
        if (!listFormatAutorise.contains(formatAAjouter) && formatAAjouter != null && !formatAAjouter.isEmpty()) {
            listFormatAutorise.add(formatAAjouter);
            // récupération de la racine
            final DocumentModel docModel = getFondDossierModelRoot();
            final FondDeDossierModelRoot racine = docModel.getAdapter(FondDeDossierModelRoot.class);
            // ajout de la nouvelle liste des formats autorisés
            racine.setFormatsAutorises(listFormatAutorise);
            // on enregistre les modifications
            racine.save(documentManager);
        }
        // on met à jour l'affichage
        setCurrentFormatFichier("");
        setFormatFichierAutoriseFddIds(listFormatAutorise);
        // on signale que la racine du fond de dossier a changé
        Events.instance().raiseEvent(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_ROOT_CHANGED_EVENT);
    }

    /**
     * Enleve un format de fichier dans la liste des formats de fichier autorisé pour les modèles de fond de dossier
     */
    public void deleteFormatFichierToRacine(final String idVocabulaire) throws ClientException {
        // récupération de la liste des fichiers autorisés
        final List<String> listFormatAutorise = getFormatFichierAutoriseFddIds();
        listFormatAutorise.remove(idVocabulaire);

        // récupération de la racine
        final DocumentModel docModel = getFondDossierModelRoot();
        final FondDeDossierModelRoot racine = docModel.getAdapter(FondDeDossierModelRoot.class);
        // ajout de la nouvelle liste des formats autorisés
        racine.setFormatsAutorises(listFormatAutorise);
        // on enregistre les modifications
        racine.save(documentManager);
        // on met à jour l'affichage
        setFormatFichierAutoriseFddIds(listFormatAutorise);
        Events.instance().raiseEvent(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_ROOT_CHANGED_EVENT);
    }

    /**
     * Récupère le modèle de fond de dossier lié au type d'acte sélectionné, et affiche l'arborescence de répertorie lié à ce modèle.
     */
    public void editFondDeDossierModel(final SolonEpgVocSugUI suggestion) {
        // on récupère le type d'acte sélectionné
        final String typeActe = suggestion.getMotCleId();

        if (typeActe != null) {
            try {
                // on récupère le modèle de fond de dossier lié à ce type d'acte
                final FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
                final DocumentModel modeleFddDoc = fondDeDossierModelService.getFondDossierModelFromTypeActe(documentManager, typeActe);
                // on définit ce modèle de fond de dossier comme document courant
                currentFondDossierModel = modeleFddDoc;
                isFondDossierModelEditable = true;
                // on signale que le modèle de fond de dossier a changé et on reinitialise les variables liés au l'ancien document courant de l'arborescence.
                resetFondDossierModelRepositoryValue();
                Events.instance().raiseEvent(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_CHANGED_EVENT);
            } catch (final ClientException e) {
                throw new RuntimeException("impossible de récupérer le modèle lié au type d'acte", e);
            }
        }
    }

    /**
     * Enregistre les modifications apportées dans les propriétés d’un répertoire du fond de dossier.
     */
    public void validerFondDeDossierModelRepository() {
        final FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
        try {
            fondDeDossierModelService.editFolder(currentFondDeDossierModelRepository, documentManager,
                    currentFondDeDossierModelRepository.getTitle(), getCurrentTypeActeLabel());
        } catch (final ClientException e) {
            throw new RuntimeException("erreur lors de la validation du document", e);
        }

        // on réinitialise le formulaire d'édition du répertoire
        resetFondDossierModelRepositoryValue();
        // on signale que le modèle de fond de dossier a changé
        Events.instance().raiseEvent(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_CHANGED_EVENT);
    }

    /**
     * Annule les modifications apportées dans les propriétés d’un répertoire du fond de dossier.
     */
    public void annulerFondDeDossierModelRepository() {
        // on réinitialise le formulaire d'édition du répertoire
        resetFondDossierModelRepositoryValue();
    }

    /**
     * Actions du répertoire de l'arborescence des modèles de fond de dossier.
     */

    /**
     * creation d'un nouveau répertoire dans le répertoire sélectionné.
     * 
     * @author antoine Rolin
     */
    public void createNewFolderNodeInto() throws ClientException {
        final DocumentModel selectedFolder = getSelectedFolder();
        if (selectedFolder != null) {
            final FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
            fondDeDossierModelService.createNewDefaultFolderInTree(selectedFolder, documentManager, getCurrentTypeActeLabel());
            // on signale que le modèle de fond de dossier a changé
            Events.instance().raiseEvent(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_CHANGED_EVENT);
            // on deselectionner le répertoire : remise à zero des variables concernant le répertoire sélectionné
            resetFondDossierModelRepositoryValue();
        }
    }

    /**
     * creation d'un nouveau répertoire avant le répertoire sélectionné.
     * 
     * @author antoine Rolin
     */
    public void createNewFolderNodeBefore() throws ClientException {
        final DocumentModel selectedFolder = getSelectedFolder();
        if (selectedFolder != null) {
            final FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
            fondDeDossierModelService.createNewFolderNodeBefore(selectedFolder, documentManager, getCurrentTypeActeLabel());
            // on signale que le modèle de fon de dossier a changé
            Events.instance().raiseEvent(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_CHANGED_EVENT);
            // on deselectionner le répertoire : remise à zero des variables concernant le répertoire sélectionné
            resetFondDossierModelRepositoryValue();
        }
    }

    /**
     * creation d'un nouveau répertoire après le répertoire sélectionné.
     * 
     * @author antoine Rolin
     */
    public void createNewFolderNodeAfter() throws ClientException {
        final DocumentModel selectedFolder = getSelectedFolder();
        if (selectedFolder != null) {
            final FondDeDossierModelService ondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
            ondDeDossierModelService.createNewFolderNodeAfter(selectedFolder, documentManager, getCurrentTypeActeLabel());
            // on signale que le modèle de fon de dossier a changé
            Events.instance().raiseEvent(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_CHANGED_EVENT);
            // on deselectionner le répertoire : remise à zero des variables concernant le répertoire sélectionné
            resetFondDossierModelRepositoryValue();
        }
    }

    /**
     * Met à jour le repertoire courant sélectionné dans l'arborescence du fond de dossier.
     * 
     * @author antoine Rolin
     */
    public void editFolder() {
        final DocumentModel selectedFolder = getSelectedFolder();
        if (selectedFolder != null) {
            // on met à jour le repertoire courant sélectionné dans l'arborescence du fond de dossier
            setCurrentFondDeDossierModelRepository(selectedFolder);
            // on signale qu'un répertoire est sélectionné et éditable
            setIsFondDossierModelRepositoryEditable(true);
            // on définit l'id sélectionné à nul
            selectedNodeId = null;
        }
    }

    /**
     * Supprime le répertoire sélectionné.
     * 
     * @author antoine Rolin
     */
    public void deleteFolder() throws ClientException {
        final DocumentModel selectedFolder = getSelectedNodeDocumentPopupDelete();
        if (selectedFolder != null) {
            final FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
            fondDeDossierModelService.deleteFolder(selectedFolder, documentManager, getCurrentTypeActeLabel());
            // on signale que le modèle de fond de dossier a changé
            Events.instance().raiseEvent(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_CHANGED_EVENT);
            // on deselectionner le répertoire : remise à zero des variables concernant le répertoire sélectionné
            resetFondDossierModelRepositoryValue();
        }
    }

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

    protected DocumentModel getSelectedFolderToDelete() {
        DocumentModel selectedFolder = null;
        // on récupère le document à l'aide de son identifiant
        final String selectedNodeIdPopupDelete = getSelectedNodeIdPopupDelete();
        if (selectedNodeIdPopupDelete != null) {
            try {
                selectedFolder = documentManager.getDocument(new IdRef(selectedNodeIdPopupDelete));
            } catch (final ClientException e) {
                throw new RuntimeException("impossible de récupérer le répertoire sélectionné", e);
            }
        }
        return selectedFolder;
    }

    /**
     * réinitialise les variable liés à l'édition d'un modèle de fond de dossier
     * 
     * @author antoine Rolin
     */
    @Observer(value = { SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_RESET_EVENT })
    public void resetFondDossierModelValue() {
        this.currentFondDossierModel = null;
        this.isFondDossierModelEditable = false;
        resetFondDossierModelRepositoryValue();
    }

    /**
     * réinitialise les variable liés à l'édition d'un répertoire du modèle de fond de dossier
     * 
     * @author antoine Rolin
     */
    public void resetFondDossierModelRepositoryValue() {
        this.selectedNodeId = null;
        this.selectedNodeIdPopupDelete = null;
        this.selectedNodeDocumentPopupDelete = null;
        this.isFondDossierModelRepositoryEditable = false;
        this.currentFondDeDossierModelRepository = null;
    }
    
    /**
     * Controle l'accès à la vue correspondante
     * 
     */
    public boolean isAccessAuthorized() {
    	SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
    	return (ssPrincipal.isAdministrator() || ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_FOND_DE_DOSSIER));
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

    public String getSelectedNodeIdPopupDelete() {
        return selectedNodeIdPopupDelete;
    }

    public void setSelectedNodeIdPopupDelete(final String selectedNodeIdPopupDelete) {
        this.selectedNodeIdPopupDelete = selectedNodeIdPopupDelete;
    }

    public DocumentModel getSelectedNodeDocumentPopupDelete() {
        if (selectedNodeDocumentPopupDelete == null) {
            if (selectedNodeIdPopupDelete != null) {
                // récupération du document sélectionné pour la suprression à l'aide de son identifiant
                selectedNodeDocumentPopupDelete = getSelectedFolderToDelete();
            }
        }
        return selectedNodeDocumentPopupDelete;
    }

    public void setSelectedNodeDocumentPopupDelete(final DocumentModel selectedNodeDocumentPopupDelete) {
        this.selectedNodeDocumentPopupDelete = selectedNodeDocumentPopupDelete;
    }

    public String getCurrentTypeActe() {
        if (!StringUtils.isEmpty(typeActe)) {
            return typeActe;
        }
        if (getCurrentFondDossierModel() == null) {
            return null;
        }
        final FondDeDossierModel fddModel = getCurrentFondDossierModel().getAdapter(FondDeDossierModel.class);
        return fddModel.getTypeActe();
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

    @Factory(value = "currentFondDeDossierModelFolder")
    @Observer(value = { SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_ROOT_CHANGED_EVENT })
    public DocumentModel getFondDossierModelRoot() {
        if (fondDossierModelRoot == null) {
            // récupération de la racine des modèles de fond de dossier
            final FondDeDossierModelService fondDossierService = SolonEpgServiceLocator.getFondDeDossierModelService();
            try {
                fondDossierModelRoot = fondDossierService.getFondDossierModelRoot(documentManager);
            } catch (final ClientException e) {
                throw new RuntimeException("erreur lors de la récupération de la racine du modèle de fond de dossier", e);
            }
        }
        return fondDossierModelRoot;
    }

    public void setFondDossierModelRoot(final DocumentModel fondDossierModelRoot) {
        this.fondDossierModelRoot = fondDossierModelRoot;
    }

    public String getCurrentFormatFichier() {
        return currentFormatFichier;
    }

    public void setCurrentFormatFichier(final String currentFormatFichier) {
        this.currentFormatFichier = currentFormatFichier;
    }

    public List<String> getFormatFichierAutoriseFddIds() {
        if (formatFichierAutoriseFddIds == null) {
            // récupération des formats autorisés
            final FondDeDossierModelService modelService = SolonEpgServiceLocator.getFondDeDossierModelService();
            formatFichierAutoriseFddIds = modelService.getFormatsAutorises(documentManager);
        }
        return formatFichierAutoriseFddIds;
    }

    public void setFormatFichierAutoriseFddIds(final List<String> formatFichierAutoriseFddIds) {
        this.formatFichierAutoriseFddIds = formatFichierAutoriseFddIds;
    }

    @Factory(value = "currentFondDeDossierModel")
    @Observer(value = { SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_CHANGED_EVENT })
    public DocumentModel getCurrentFondDossierModel() {
        if (currentFondDossierModel == null) {
            final DocumentModel docModel = navigationContext.getCurrentDocument();
            if (docModel != null && docModel.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_TYPE)) {
                currentFondDossierModel = navigationContext.getCurrentDocument();
            }
        }
        return currentFondDossierModel;
    }

    public void setCurrentFondDossierModel(final DocumentModel currentFondDossierModel) {
        this.currentFondDossierModel = currentFondDossierModel;
    }

    public DocumentModel getCurrentFondDeDossierModelRepository() {
        return currentFondDeDossierModelRepository;
    }

    public void setCurrentFondDeDossierModelRepository(final DocumentModel currentFondDeDossierModelRepository) {
        this.currentFondDeDossierModelRepository = currentFondDeDossierModelRepository;
    }

    public Boolean getIsFondDossierModelRepositoryEditable() {
        return isFondDossierModelRepositoryEditable;
    }

    public void setIsFondDossierModelRepositoryEditable(final Boolean isFondDossierModelRepositoryEditable) {
        this.isFondDossierModelRepositoryEditable = isFondDossierModelRepositoryEditable;
    }

    public Boolean getIsFondDossierModelEditable() {
        return isFondDossierModelEditable;
    }

    public void setIsFondDossierModelEditable(final Boolean isFondDossierModelEditable) {
        this.isFondDossierModelEditable = isFondDossierModelEditable;
    }

}
