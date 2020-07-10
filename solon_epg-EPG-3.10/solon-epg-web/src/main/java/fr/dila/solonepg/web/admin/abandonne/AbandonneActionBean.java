package fr.dila.solonepg.web.admin.abandonne;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.admin.AdministrationActionsBean;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.core.service.STServiceLocator;

@Name("abandonneActions")
@Scope(CONVERSATION)
public class AbandonneActionBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;
    @In(create = true)
    protected transient DocumentsListsManager documentsListsManager;
    @In(create = true)
    protected transient NuxeoPrincipal currentUser;
    @In(create = true)
    protected transient AdministrationActionsBean administrationActions;

    /**    
     * 
     */
    public String userName;

    /**
     * 
     */
    public String password;
    /**
     * 
     */
    public String typeAction;
    /**
     * 
     */
    public String authenticationSuccess;

    public boolean isButtonToDisplay() {
        return !documentsListsManager.isWorkingListEmpty(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
    }

    /**
     * Methode qui retire le(s) dossiers selectionnees de la liste des dossiers a abandonne
     * 
     * @return
     * @throws ClientException
     */
    public String retirerSelection() throws ClientException {
        String result = "";
        authenticationSuccess = "check";
        List<DocumentModel> docs = documentsListsManager.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
        final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        for (DocumentModel dossierDoc : docs) {
            dossierService.retirerAbandonDossier(documentManager, dossierDoc);
        }
        Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
        result = administrationActions.navigateToAbandon();
        documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
        return result;
    }
    
    /**
     * Controle l'accès à la vue correspondante
     * 
     */
    public boolean isAccessAuthorized() {
    	SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
    	return (ssPrincipal.isAdministrator() || ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_FONCTIONNEL_ABANDON) 
    			|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_MINISTERIEL_ABANDON));
    }

    /**
     * authentifier l'acces de l'utilisateur saisie par l'utilisateur
     * 
     * @return
     */
    public boolean authentifier() {
        boolean isAuthentifiedOk = false;
        if (currentUser == null) {
            isAuthentifiedOk = false;
        } else {
            UserManager userManager = STServiceLocator.getUserManager();
            if (userName != null && password != null && userManager.authenticate(userName, password)) {
                isAuthentifiedOk = true;
            }
        }
        if (isAuthentifiedOk) {
            authenticationSuccess = "check";
        } else {
            authenticationSuccess = "uncheck";
        }
        return isAuthentifiedOk;
    }

    public String getUserName() {
        userName = currentUser.getName();
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(String typeAction) {
        this.typeAction = typeAction;
    }

    public void setToDoAction(String typeAction) {
        this.typeAction = typeAction;
        this.authenticationSuccess = null;
    }

    public String getAuthenticationSuccess() {
        return authenticationSuccess;
    }

    public void setAuthenticationSuccess(String authenticationSuccess) {
        this.authenticationSuccess = authenticationSuccess;
    }

    public String resetAuthenticationSuccess() {
        authenticationSuccess = null;
        return "check";
    }
}
