package fr.dila.solonepg.web.admin.parametres;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ui.web.component.list.UIEditableList;
import org.nuxeo.ecm.platform.ui.web.model.EditableModel;
import org.nuxeo.ecm.platform.ui.web.util.ComponentUtils;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.richfaces.component.html.HtmlInputText;
import org.richfaces.event.UploadEvent;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.constant.SolonEpgParametrageApplicationConstants;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.admin.AdministrationActionsBean;
import fr.dila.solonepg.web.converter.DocumentModelVocabularyConverter;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.core.util.StringUtil;

/**
 * Bean seam de gestion du paramètrage de l'application : edition des propriétés du paramètrage de l'application .
 * 
 * @author arn
 */
@Name("parametrageApplicationAdministrationActions")
@Scope(ScopeType.PAGE)
public class ParametrageApplicationAdministrationActionsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog(ParametrageApplicationAdministrationActionsBean.class);

    private static final int MAX_SIZE_UPLOAD = 100000;

	private static final String						PASS_WS_EPP_PROP	= SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_PREFIX
																				+ ":"
																				+ SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_PASS_EPP_INFOS_PARL_PROPERTY;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true)
    protected transient AdministrationActionsBean administrationActions;
    
    protected DocumentModel parametrageApplication;

    @RequestParameter
    private String inputSuffixeList;

    @RequestParameter
    private String inputSuffixeText;
    
    /**
     * Liste de toutes les métadonnées pouvant être utilisé comme colonnes dans la vue des corbeilles 
     */
    protected List<DocumentModel> sourceValue;
    
    /**
     * Liste des métadonnéess proposées au utilisateurs comme colonnes dans la vue des corbeilles
     */
    protected List<DocumentModel> targetValue;
    
    /**
     * Champ utilisé pour afficher les erreurs de validation
     */
    protected String errorName = null;
    
    public String valider() throws ClientException {
        List<String> metaDataDisponible = new ArrayList<String>();
        if(targetValue != null && targetValue.size()>0){
            for (DocumentModel vocabularyDocument : targetValue) {
                metaDataDisponible.add((String)vocabularyDocument.getProperty(STVocabularyConstants.VOCABULARY, STVocabularyConstants.COLUMN_ID));
            }
        }
        ParametrageApplication parametrageApplicationDoc = parametrageApplication.getAdapter(ParametrageApplication.class);
        parametrageApplicationDoc.setMetadonneDisponibleColonneCorbeille(metaDataDisponible);

        //on vérifie que les champs obligatoire sont remplis
        if (parametrageApplicationDoc.getNbDerniersResultatsConsultes() == null 
            || parametrageApplicationDoc.getNbDossierPage() == null 
            || parametrageApplicationDoc.getNbDossiersSignales() == null 
            || parametrageApplicationDoc.getNbFavorisConsultation() == null 
            || parametrageApplicationDoc.getNbFavorisRecherche() == null 
            || parametrageApplicationDoc.getNbTableauxDynamiques() == null 
            || parametrageApplicationDoc.getDateRafraichissementCorbeille() == null 
            || parametrageApplicationDoc.getDelaiEnvoiMailMaintienAlerte() == null) {
            String message = resourcesAccessor.getMessages().get("label.epg.parametrage.application.error");
            facesMessages.add(StatusMessage.Severity.WARN, message);
            LOGGER.warn(message);
            errorName=message;
            return null;
        }
        
		try {
			parametrageApplicationDoc.setPassEppInfosParl((String) parametrageApplication.getPropertyValue(PASS_WS_EPP_PROP));
		} catch (Exception e) {
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			errorName=e.getMessage();
			return null;
		}

        //log les modifications des paramètrages de l'application hors ministère
        final ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
        parametreApplicationService.logAllDocumentUpdate(documentManager, parametrageApplication);
        
        //enregistrement des modifications
        documentManager.saveDocument(parametrageApplicationDoc.getDocument());
        Events.instance().raiseEvent(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_CHANGED_EVENT);
        return administrationActions.navigateToParametrageApplication();
    }
    
    public String annuler() throws ClientException {
        parametrageApplication=null;
        return administrationActions.navigateToEspaceAdministration();
    }
    
    /**
     * Controle l'accès à la vue correspondante
     * 
     */
    public boolean isAccessAuthorized() {
    	SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
    	return (ssPrincipal.isAdministrator() || ssPrincipal.isMemberOf(STBaseFunctionConstant.ADMINISTRATION_PARAMETRE_APPLICATION_READER));
    }
    
    // Getter & Setter

    public Converter getConverter(){
        return new DocumentModelVocabularyConverter(STVocabularyConstants.BORDEREAU_LABEL);
    }
    
    /**
     * Get the DocumentModel ParametrageApplication
     * @throws ClientException 
     */
    public DocumentModel getParametrageApplication() throws ClientException {
        if(parametrageApplication== null){
            ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
			ParametrageApplication param = parametreApplicationService.getParametreApplicationDocument(documentManager);
			parametrageApplication = param.getDocument();
			parametrageApplication.setPropertyValue(PASS_WS_EPP_PROP, param.getPassEppInfosParl());
        }
        return parametrageApplication;
    }

    public void setParametrageApplication(DocumentModel parametrageApplication) {
        this.parametrageApplication = parametrageApplication;
    }

    public List<DocumentModel> getSourceValue() throws ClientException {
        if(sourceValue== null){
            ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
            sourceValue = parametreApplicationService.getAllNonAvailableColumnsDocument(getParametrageApplication());
        }
        return sourceValue;
    }

    public void setSourceValue(List<DocumentModel> sourceValue) {
        this.sourceValue = sourceValue;
    }

    public List<DocumentModel> getTargetValue() throws ClientException {
        if(targetValue== null){
            ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
            targetValue = parametreApplicationService.getAvailablesColumnsDocument(getParametrageApplication());
        }
        return targetValue;
    }

    public void setTargetValue(List<DocumentModel> targetValue) {
        this.targetValue = targetValue;
    }

    public String getErrorName() {
        return errorName;
    }

    public void validateSufixeMail(FacesContext facesContext, UIComponent uIComponent, Object object)
			throws ValidatorException {
        String value = (String) object;
        if (!isMailSuffixeOK(value)) {
			FacesMessage message = new FacesMessage(resourcesAccessor.getMessages().get("parametrage.error.mail.wrong"));
			facesContext.addMessage(uIComponent.getClientId(facesContext), message);
			throw new ValidatorException(message);
		}
	}

    /**
     * Retourne vrai si le suffixe doit être rejeté
     * @param value
     * @return
     */
    private boolean isMailSuffixeOK(String value) {
		return StringUtil.isNotBlank(value) && value.trim().matches("^[a-zA-Z0-9._-]{2,}\\.[a-zA-Z]{2,4}$");
    }

    /**
     * incrémente la liste des suffixes avec la nouvelle valeur
     * @param event
     */
    public void addSuffixe(ActionEvent event) {
        UIComponent component = event.getComponent();
        if (component == null) {
            return;
        }
        UIEditableList list = ComponentUtils.getComponent(component, inputSuffixeList, UIEditableList.class);

        HtmlInputText htmlInputText = ComponentUtils.getComponent(component, inputSuffixeText, HtmlInputText.class);
        String value = (String) htmlInputText.getValue();
        
        if (list != null && StringUtil.isNotBlank(value)) {
            addValue(list, value);
            htmlInputText.setValue(null);
        }
    }

    /**
     * Ajoute la valeur si non présente dans la liste
     * @param list
     * @param value
     */
    private void addValue(UIEditableList list, String value) {
    	EditableModel model = list.getEditableModel();
    	@SuppressWarnings("unchecked")
		List<String> data = (List<String>) model.getWrappedData();
    	if (!data.contains(value)) {
    		list.addValue(value);
    	}
    }

    /**
     * Listener sur l'upload d'un fichier : on ajoute le fichier dans la liste
     *
     */
    public void fileUploadListener(final UploadEvent event) throws Exception {
        LOGGER.info("fileUploadListener fileSelected on");
        if (event == null || event.getUploadItem() == null || event.getUploadItem().getFileName() == null) {
        	return;
        }
        
        final List<String> wrongLines = new ArrayList<String>();
        final List<String> correctLines = new ArrayList<String>();
        final UIComponent component = event.getComponent();
        final String listId = (String) component.getAttributes().get("inputSuffixeListId");
        final UIEditableList list = ComponentUtils.getComponent(event.getComponent(), listId, UIEditableList.class);
        
        int actualSize = event.getUploadItem().getFileSize();
        if (actualSize > MAX_SIZE_UPLOAD) {
        	String message = resourcesAccessor.getMessages().get("parametrage.error.mail.file.content.maxSize");
            errorName = MessageFormat.format(message, actualSize/1000, MAX_SIZE_UPLOAD/1000);
            LOGGER.info(errorName);
            return;
        }
        
        BufferedReader br = null;
        try {
            final File file = event.getUploadItem().getFile();
            br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if (isMailSuffixeOK(line)) {
					if (list != null && StringUtil.isNotBlank(line)) {
						correctLines.add(line);
					}
				} else {
					wrongLines.add(line);
				}
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}

        if (!correctLines.isEmpty() && list != null) {
        	EditableModel model = list.getEditableModel();
        	@SuppressWarnings("unchecked")
    		List<String> data = (List<String>) model.getWrappedData();
        	data.clear();
        	data.addAll(correctLines);
        	model.setWrappedData(data);
        }
        if (!wrongLines.isEmpty()) {
            String message = resourcesAccessor.getMessages().get("parametrage.error.mail.file.content.wrong");
            errorName = MessageFormat.format(message, StringUtil.join(wrongLines,", ", ""));
            LOGGER.info(errorName);
        }
    }

    private static HttpServletResponse getHttpServletResponse() {
		ServletResponse response = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			response = (ServletResponse) facesContext.getExternalContext().getResponse();
		}

		if (response != null && response instanceof HttpServletResponse) {
			return (HttpServletResponse) response;
		}
		return null;
	}

    public void exportMailSuffixes() throws Exception {
		// Récupération de la réponse Http
		HttpServletResponse response = getHttpServletResponse();
		if (response == null) {
			return;
		}
		response.reset();
		OutputStream outputStream = response.getOutputStream();

		ParametrageApplication parametrageApplicationDoc = parametrageApplication.getAdapter(ParametrageApplication.class);
        List<String> data = parametrageApplicationDoc.getSuffixesMailsAutorises();

		response.setHeader("Content-Disposition", "attachment; filename=\"export_noms_domaines.txt\";");
		response.setHeader("Content-Type", "text/plain; charset=utf-8");

    	BufferedWriter bw = null;
    	try {
    		bw = new BufferedWriter(new OutputStreamWriter(outputStream));
    		for (String value : data) {
    			bw.write(value);
    			bw.newLine();
    		}
    	} catch (IOException exc) {
    		LOGGER.error("Fail to export data", exc);
    	} finally {
			if (bw != null) {
				bw.close();
			}
		}
    	outputStream.flush();
		FacesContext.getCurrentInstance().responseComplete();
	}
}
