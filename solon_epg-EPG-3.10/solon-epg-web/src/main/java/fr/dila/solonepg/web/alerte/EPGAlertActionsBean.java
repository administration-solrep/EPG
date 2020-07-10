package fr.dila.solonepg.web.alerte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.ui.web.api.UserAction;
import org.nuxeo.ecm.platform.ui.web.api.WebActions;
import org.nuxeo.ecm.platform.ui.web.component.list.UIEditableList;
import org.nuxeo.ecm.platform.ui.web.util.ComponentUtils;
import org.nuxeo.ecm.platform.userworkspace.web.ejb.UserWorkspaceManagerActionsBean;
import org.nuxeo.ecm.webapp.contentbrowser.DocumentActions;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManagerBean;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.richfaces.component.html.HtmlInputText;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.alert.SolonEpgAlert;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.EpgAlertService;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.st.api.alert.Alert;
import fr.dila.st.api.constant.STAlertConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.alert.STAlertActionsBean;
import fr.dila.st.web.context.NavigationContextBean;

//TODO JGZ: créer un service Alert dans le socle après avoir fait les tests selenium et remonter les méthodes.
@Name("alertActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.APPLICATION + 2)
public class EPGAlertActionsBean extends STAlertActionsBean implements Serializable {

    private static final String CREATE_ALERT_ACTIVITE_NORMATIVE = "create_alert_activite_normative";

	private static final String MESALERTES_PATH = "/mesalertes/";

    private static final long serialVersionUID = 1L;

    @In(create = true, required = true)
    protected DocumentActions documentActions;

    @In(create = true, required = true)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = true)
    protected ContentViewActions contentViewActions;

    @In(create = true, required = true)
    protected transient ActionManager actionManager;

    @In(create = true, required = false)
    protected transient WebActions webActions;

    @In(create = true, required = false)
    protected transient UserWorkspaceManagerActionsBean userWorkspaceManagerActions;

    @In(create = true, required = false)
    protected transient DocumentsListsManagerBean documentsListsManager;
    
    @In(create = true)
    protected transient NuxeoPrincipal currentUser;

    private static final Log log = LogFactory.getLog(EPGAlertActionsBean.class);
    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOGGER = STLogFactory.getLog(EPGAlertActionsBean.class);

    private String alertUuid;
    
    @RequestParameter
    protected String inputList;

    @RequestParameter
    protected String inputText;

    /**
     * Création d'une alerte, qui est attaché à une requete de dossier.
     * 
     * @param requeteExperte
     * @return Le formulaire de création d'une alerte.
     * @throws ClientException
     */
    public String createAlertFromRequete(DocumentModel requete) throws ClientException {
        navigateToUserAlertRoot();
        if (requete == null) {
            throw new ClientException();
        }
        DocumentModel changeableAlertDoc = documentManager.createDocumentModel(STAlertConstant.ALERT_DOCUMENT_TYPE);
        navigationContext.setChangeableDocument(changeableAlertDoc);
        Alert alert = changeableAlertDoc.getAdapter(Alert.class);
        alert.setRequeteId(requete.getId());
        alert.setIsActivated(true);
        List<String> recipients = new ArrayList<String>();     
        recipients.add(currentUser.getName());
        alert.setRecipientIds(recipients);
        return navigationContext.getActionResult(changeableAlertDoc, UserAction.CREATE);
    }
    

    /**
     * Création d'une alerte, qui est attaché à une requete de dossier.
     * 
     * @param requeteExperte
     * @return Le formulaire de création d'une alerte.
     * @throws ClientException
     */
    public String createAlertFromRequeteForAN(DocumentModel requete) throws ClientException {
    	createAlertFromRequete(requete);
        return CREATE_ALERT_ACTIVITE_NORMATIVE;
    }
    
    
    /**
     * Va vers le document racine des alertes de l'utilisateur.
     * 
     * @throws ClientException
     */
    public void navigateToUserAlertRoot() throws ClientException {
        String alertRootPath = getAlertRootPath();
        navigationContext.navigateToRef(new PathRef(alertRootPath));
    }

    /**
     * Retourne le chemin vers la racine des alertes
     * 
     * @return
     * @throws ClientException
     */
    public String getAlertRootPath() throws ClientException {
        return userWorkspaceManagerActions.getCurrentUserPersonalWorkspace().getPathAsString() + MESALERTES_PATH;
    }

    /**
     * Création du document Alerte dans l'espace dossier
     * 
     * @return
     * @throws ClientException
     */
    public String saveDocument() throws ClientException {
        return saveDocument(SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER);
    }


	private String saveDocument(String view) throws ClientException {
		// on initialise la date de demande de confirmation
        DocumentModel changeableDocument = navigationContext.getCurrentDocument();
        if (!changeableDocument.hasSchema(STAlertConstant.ALERT_SCHEMA)) {
            changeableDocument = navigationContext.getChangeableDocument();
        }
        SolonEpgAlert alert = changeableDocument.getAdapter(SolonEpgAlert.class);
        if (alert.getRecipientIds().isEmpty()) {
            facesMessages.add(StatusMessage.Severity.WARN, resourcesAccessor.getMessages().get("alert.messages.warn.needRecipients"));
            return "";
        }
        if(alert.getDateValidityBegin().after(alert.getDateValidityEnd())) {
        	facesMessages.add(StatusMessage.Severity.WARN, resourcesAccessor.getMessages().get("alert.messages.warn.invalidExecutionDate"));
            return "";
        }
        alert.setDateDemandeConfirmation(Calendar.getInstance());
        // création de l'alerte
        if (StringUtils.isBlank(alert.getDocument().getId())) {
            changeableDocument.setPathInfo(getAlertRootPath(),
                    StringUtils.isBlank(alert.getTitle()) ? UUID.randomUUID().toString() : alert.getTitle().replace('/', '-'));
            documentManager.createDocument(alert.getDocument());
        } else {
            documentManager.saveDocument(alert.getDocument());
        }

        Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
        return view;
	}
    
    /**
     * Création du document Alerte
     * 
     * @return
     * @throws ClientException
     */
    public String saveDocumentEspaceActiviteNormative() throws ClientException {
    	return saveDocument(SolonEpgViewConstant.VIEW_ESPACE_ACTIVITE_NORMATIVE);
    }

    /**
     * Retourne une liste d'alerte à partir de la sélection courante.
     * 
     * @return la liste des alertes
     * @throws ClientException
     */
    public List<Alert> getAlertsFromSelection() {
        List<Alert> alerts = new ArrayList<Alert>();
        for (DocumentModel alertDoc : getAlertWorkingList()) {
            Alert alert = alertDoc.getAdapter(Alert.class);
            alerts.add(alert);
        }
        return alerts;
    }

    // "ESPACE_SUIVI_ALERT_SELECTION"
    private List<DocumentModel> getAlertWorkingList() {
        return documentsListsManager.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
    }

    private void clearAlertWorkingList() {
        documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
    }

    /**
     * Enregistre les modifications faites sur sélection courante.
     * 
     * @return la liste des alertes
     * @throws ClientException
     */
    public void saveSelection() throws ClientException {
        List<DocumentModel> docs = getAlertWorkingList();
        documentManager.saveDocuments(docs.toArray(new DocumentModel[docs.size()]));
        documentManager.save();
    }

    /**
     * Active la sélection des alertes.
     * 
     * @throws ClientException
     */
    public void bulkActivateFromSelection() throws ClientException {
        List<Alert> alerts = getAlertsFromSelection();
        for (Alert alert : alerts) {
            alert.setIsActivated(true);
        }
        saveSelection();
        clearAlertWorkingList();
        facesMessages.add(StatusMessage.Severity.INFO,
				resourcesAccessor.getMessages().get("alert.messages.info.multiple.alertActivated"));
    }

    /**
     * Met en pause la sélection des alertes.
     * 
     * @throws ClientException
     */
    public void bulkSuspendFromSelection() throws ClientException {
        List<Alert> alerts = getAlertsFromSelection();
        for (Alert alert : alerts) {
            alert.setIsActivated(false);
        }
        saveSelection();
        clearAlertWorkingList();
        facesMessages.add(StatusMessage.Severity.INFO,
				resourcesAccessor.getMessages().get("alert.messages.info.multiple.alertSuspended"));
    }

    public void deleteSelected() {
        List<Alert> alerts = getAlertsFromSelection();
        for (Alert alert : alerts) {
            delete(alert.getDocument());
        }
        clearAlertWorkingList();
    }

    @Override
    public String suspend() throws ClientException {
        Alert alert = getAlertDoc().getAdapter(Alert.class);
        alert.setIsActivated(false);
        documentManager.saveDocument(alert.getDocument());
        facesMessages.add(StatusMessage.Severity.INFO, resourcesAccessor.getMessages().get("alert.messages.info.alertSuspended"));
        return ACTION_ALERT_DESACTIVATED;
    }

    @Override
    public String activate() throws ClientException {
        Alert alert = getAlertDoc().getAdapter(Alert.class);
        alert.setIsActivated(true);
        documentManager.saveDocument(alert.getDocument());
        facesMessages.add(StatusMessage.Severity.INFO, resourcesAccessor.getMessages().get("alert.messages.info.alertActivated"));
        return ACTION_ALERT_ACTIVATED;
    }

    public String delete() throws ClientException {
        return super.delete(getAlertDoc());
    }

    /**
     * Suppression du document alerte et de la requête qui lui est associée
     * 
     * @return
     * @throws ClientException
     */
    @Override
    public String delete(DocumentModel doc) {
        final EpgAlertService alertService = SolonEpgServiceLocator.getAlertConfirmationSchedulerService();
        // supprimer l'alerte
        LOGGER.info(documentManager, EpgLogEnumImpl.DEL_ALERT_EPG_FONC, doc);
        boolean isDeleted = alertService.deleteAlert(documentManager, doc);
        if (!isDeleted) {
            log.error("Echec à la suppression d'une alerte");
            facesMessages.add(StatusMessage.Severity.INFO, resourcesAccessor.getMessages().get("alert.error.alertDeletion"));
        }
        return null;
    }

    public DocumentModel getAlertDoc() throws ClientException {
        return documentManager.getDocument(new IdRef(alertUuid));
    }

    public String navigateToEdit() throws ClientException {
        return navigationContext.navigateToDocumentWithView(getAlertDoc(), "edit");
    }

    public void setAlertUuid(String alertUuid) {
        this.alertUuid = alertUuid;
    }

    public String getAlertUuid() {
        return alertUuid;
    }

    /**
     * incrémente la liste des destinataires externes avec le nouveau mail entré
     * @param event
     */
    public void addExternalRecipient(ActionEvent event) {
        UIComponent component = event.getComponent();
        if (component == null) {
            return;
        }
        UIEditableList list = ComponentUtils.getComponent(component, inputList, UIEditableList.class);

        HtmlInputText htmlInputText = ComponentUtils.getComponent(component, inputText, HtmlInputText.class);
        String value = (String) htmlInputText.getValue();
        
        if (list != null && StringUtil.isNotBlank(value)) {
            String selectedValue = value;
            list.addValue(selectedValue);
            htmlInputText.setValue(null);
        }
    }
    
    public void validateExternalRecipient(FacesContext facesContext, UIComponent uIComponent, Object object)
			throws ValidatorException {
        String value = (String) object;
    	if (StringUtil.isBlank(value)
				|| !value.trim().matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]{2,}\\.[a-zA-Z]{2,4}$")) {
    		throwValidatorExc(facesContext, uIComponent, "alert.error.mail.wrong");
		}
    	
    	validateSuffixe(facesContext, uIComponent, value);
	}
    
    private void validateSuffixe(FacesContext facesContext, UIComponent uIComponent, String value) throws ValidatorException {
    	ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
    	ParametrageApplication paramDoc = null;
        try {
			paramDoc = parametreApplicationService.getParametreApplicationDocument(documentManager);
		} catch (ClientException exc) {
			LOGGER.warn(documentManager, STLogEnumImpl.FAIL_UPDATE_ALERT_TEC, exc);			
		}
        if (paramDoc == null) {
        	throwValidatorExc(facesContext, uIComponent, "parametrage.error.get.document");
        }
        
        List<String> suffixesAutorises = paramDoc.getSuffixesMailsAutorises();
        String suffixeValueTested = value.substring(value.indexOf("@") + 1);
        boolean suffixeValidated = false;
        for (String suffixe : suffixesAutorises) {
        	if (suffixe.equalsIgnoreCase(suffixeValueTested)) {
        		suffixeValidated = true;
        		break;
        	}
        }
        if (!suffixeValidated) {
        	throwValidatorExc(facesContext, uIComponent, "alert.error.mail.suffixe.wrong");
        }    	
    }
    
    private void throwValidatorExc(FacesContext facesContext, UIComponent uIComponent, String messageValue) {
    	FacesMessage message = new FacesMessage(resourcesAccessor.getMessages().get(messageValue));
		facesContext.addMessage(uIComponent.getClientId(facesContext), message);
		throw new ValidatorException(message);
    }
}
