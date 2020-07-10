package fr.dila.solonmgpp.web.admin;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.ParametreMgppService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STViewConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.security.principal.STPrincipal;
import fr.dila.st.core.factory.STLogFactory;

/**
 * Gestion du parametrage du MGPP
 * 
 * @author asatre
 * 
 */
@Name("parametrageMgppActions")
@Scope(ScopeType.PAGE)
public class ParametrageMgppActionsBean implements Serializable {

    private static final long serialVersionUID = 5627631583969874391L;

	private static final String			PASS_WS_EPP_PROP	= ParametrageMgpp.PREFIX + ":"
																	+ ParametrageMgpp.PASSWORD_EPP;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    protected DocumentModel parametrageMgpp;

    /**
     * logger
     */
    private static final STLogger LOGGER = STLogFactory.getLog(ParametrageMgppActionsBean.class);
    
    /**
     * Get the DocumentModel ParametrageApplication
     * 
     * @throws ClientException
     */
    public DocumentModel getParametrageMgpp() throws ClientException {
        if (parametrageMgpp == null) {
            ParametreMgppService parametreMgppService = SolonMgppServiceLocator.getParametreMgppService();
			ParametrageMgpp param = parametreMgppService.findParametrageMgpp(documentManager);
			parametrageMgpp = param.getDocument();
			parametrageMgpp.setPropertyValue(PASS_WS_EPP_PROP, param.getPassEpp());
        }
        return parametrageMgpp;
    }

    public String save(Boolean checkConnexion) {
        try {
            ParametreMgppService parametreMgppService = SolonMgppServiceLocator.getParametreMgppService();
			ParametrageMgpp param = parametrageMgpp.getAdapter(ParametrageMgpp.class);
			param.setPassEpp((String) parametrageMgpp.getPropertyValue(PASS_WS_EPP_PROP));
			parametrageMgpp = parametreMgppService.saveParametrageMgpp(documentManager, param,
                    checkConnexion).getDocument();
            SolonMgppServiceLocator.getDossierService().updateListeOEPPubliee(documentManager);
            facesMessages.add(StatusMessage.Severity.INFO, "Paramétrage enregistré.");
        } catch (ClientException e) {
            LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
        }

        return null;
    }

    public String cancel() {
        return STViewConstant.EMPTY_VIEW;
    }
    
    /**
     * Controle l'accès à la vue correspondante
     * 
     */
    public boolean isAccessAuthorized() {
    	STPrincipal stPrincipal = (STPrincipal) documentManager.getPrincipal();
    	return (stPrincipal.isAdministrator() || stPrincipal.isMemberOf(STBaseFunctionConstant.ADMINISTRATION_PARAMETRE_APPLICATION_READER));
    }

    public void setParametrageMgpp(DocumentModel parametrageMgpp) {
        this.parametrageMgpp = parametrageMgpp;
    }

}
