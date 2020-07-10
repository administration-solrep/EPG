package fr.dila.solonepg.web.admin.parametres;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.platform.ui.web.component.list.UIEditableList;
import org.nuxeo.ecm.platform.ui.web.util.ComponentUtils;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.richfaces.component.html.HtmlInputText;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgTableReferenceConstants;
import fr.dila.solonepg.api.dto.tablereference.ModeParutionDTO;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.solonepg.core.dto.tablereference.ModeParutionDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.suggestion.TypeActeSuggestionActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;

@Name("tablesReferenceActions")
@Scope(ScopeType.CONVERSATION)
public class TablesReferenceActionsBean implements Serializable {

    /**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -8022975211085900758L;
	
	private static final STLogger LOGGER = STLogFactory.getLog(TablesReferenceActionsBean.class);

	@In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;
    
    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;
    
    @In(create = true)
    private transient TypeActeSuggestionActionsBean typeActeSuggestionActions;

    private DocumentModel tableReferenceDoc;
    
    private List<ModeParutionDTO> listModeParution;

    protected String freeInputUST;

    @RequestParameter
    protected String organigrammeSelectionListId;

    @RequestParameter
    protected String freeInputText;

    public void setTableReference(DocumentModel tableReference) {
        this.tableReferenceDoc = tableReference;
    }

    public DocumentModel getTableReference() throws ClientException {
        if (tableReferenceDoc == null) {
            TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
            tableReferenceDoc = tableReferenceService.getTableReference(documentManager);
        }
        return tableReferenceDoc;
    }

    public void save() throws ClientException {
    	TableReference tableReference = tableReferenceDoc.getAdapter(TableReference.class);
    	tableReference.setTypesActe(typeActeSuggestionActions.getCurSelection());
    	tableReference.save(documentManager);
    }

    public void cancel() {
        tableReferenceDoc = null;
    }

    public void addFreeInputUST(ActionEvent event) {
        UIComponent component = event.getComponent();
        if (component == null) {
            return;
        }
        UIEditableList list = ComponentUtils.getComponent(component, organigrammeSelectionListId, UIEditableList.class);

        HtmlInputText inputText = ComponentUtils.getComponent(component, freeInputText, HtmlInputText.class);
        String freeInput = (String) inputText.getValue();
        if (list != null && freeInput != null && !freeInput.isEmpty()) {
            String selectedValue = freeInput;
            list.addValue(selectedValue);
            inputText.setValue(null);
        }
    }
    
    /**
     * Controle l'accès à la vue correspondante
     * 
     */
    public boolean isAccessAuthorized() {
    	SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
    	return (ssPrincipal.isAdministrator() || ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_TABLES_REFERENCE_READER));
    }
    
    public void addModeParution() throws ClientException {
    	DocumentModel modelDesired = documentManager.createDocumentModel(SolonEpgTableReferenceConstants.MODE_PARUTION_DOCUMENT_TYPE);
        modelDesired.setPathInfo(SolonEpgTableReferenceConstants.TABLE_REFERENCE_PATH, UUID.randomUUID().toString());
        ModeParution mode = modelDesired.getAdapter(ModeParution.class);
        getListModeParution().add(new ModeParutionDTOImpl(mode));
        final String message = resourcesAccessor.getMessages().get("feedback.solonepg.table.reference.addModeParution");
        facesMessages.add(StatusMessage.Severity.INFO, message);
    }
    
    public void saveModeParution() {
    	TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
    	
    	for (ModeParutionDTO mode : listModeParution) {
    		String id = mode.getId();
    		try {
	    		if (id != null && documentManager.exists(new IdRef(id))) {
	    			tableReferenceService.updateModeParution(documentManager, mode);
	    		} else {
	    			tableReferenceService.createModeParution(documentManager, mode);
	    		}
    		} catch (ClientException exc) {
    			final String message = resourcesAccessor.getMessages().get("feedback.solonepg.table.reference.failSaveModeParution");
    	        facesMessages.add(StatusMessage.Severity.ERROR, message);
    	        LOGGER.error(documentManager, EpgLogEnumImpl.FAIL_SAVE_MODE_PARUTION_FONC, exc);
    		}
    	}
    	final String message = resourcesAccessor.getMessages().get("feedback.solonepg.table.reference.saveModeParution");
        facesMessages.add(StatusMessage.Severity.INFO, message);
    }
    
    public void cancelModeParution() {
    	listModeParution.clear();
    	TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
    	List<DocumentModel> modeParutionDocList = new ArrayList<DocumentModel>();
    	try {
    		modeParutionDocList.addAll(tableReferenceService.getModesParutionList(documentManager));
    	} catch (ClientException exc) {
    		final String message = resourcesAccessor.getMessages().get("feedback.solonepg.table.reference.failGetModeParution");
	        facesMessages.add(StatusMessage.Severity.ERROR, message);
	        LOGGER.error(documentManager, EpgLogEnumImpl.FAIL_GET_MODE_PARUTION_FONC, exc);
    	}
    	for (DocumentModel modeDoc : modeParutionDocList) {
    		listModeParution.add(new ModeParutionDTOImpl(modeDoc.getAdapter(ModeParution.class)));
    	}
    	
    	final String message = resourcesAccessor.getMessages().get("feedback.solonepg.table.reference.cancelModeParution");
        facesMessages.add(StatusMessage.Severity.INFO, message);
    }

	public List<ModeParutionDTO> getListModeParution() {
		if (listModeParution == null) {
			listModeParution = new ArrayList<ModeParutionDTO>();
			TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
	    	List<DocumentModel> modeParutionDocList = new ArrayList<DocumentModel>();
	    	try {
	    		modeParutionDocList.addAll(tableReferenceService.getModesParutionList(documentManager));
	    	} catch (ClientException exc) {
	    		final String message = resourcesAccessor.getMessages().get("feedback.solonepg.table.reference.failGetModeParution");
		        facesMessages.add(StatusMessage.Severity.ERROR, message);
		        LOGGER.error(documentManager, EpgLogEnumImpl.FAIL_GET_MODE_PARUTION_FONC, exc);
	    	}
	    	for (DocumentModel modeDoc : modeParutionDocList) {
	    		listModeParution.add(new ModeParutionDTOImpl(modeDoc.getAdapter(ModeParution.class)));
	    	}
		}
		
		return listModeParution;
	}
}
