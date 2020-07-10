package fr.dila.solonepg.web.admin;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.st.api.administration.EtatApplication;
import fr.dila.st.api.service.EtatApplicationService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.action.DocumentActionsBean;
import fr.dila.st.web.administration.EtatApplicationActionsBean;

@Name("etatApplicationActions")
@Install(precedence = Install.FRAMEWORK +1)
public class SolonEpgEtatApplicationActionsBean extends EtatApplicationActionsBean{

    @In(create = true)
    protected transient DocumentActionsBean documentActions;
    
    public boolean hasUserRigthToUpdateAccess(){
        NuxeoPrincipal nuxeoPrincipal = (NuxeoPrincipal) documentManager.getPrincipal();
        if(!nuxeoPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ACCESS_UNRESTRICTED_UPDATER)){
            return false;
        }
        return true;
    }
    
    public String editEtatApplication() throws ClientException {
        EtatApplicationService etatApplicationService = STServiceLocator.getEtatApplicationService();
        EtatApplication etatApplication = etatApplicationService.getEtatApplicationDocument(documentManager);
        navigationContext.setCurrentDocument(etatApplication.getDocument());
        navigationContext.setChangeableDocument(etatApplication.getDocument());
        return "gestion_acces";
    }
    
    public String saveEtatApplication() throws ClientException {
        documentActions.updateDocument();      
        return "gestion_acces";
    }
}
