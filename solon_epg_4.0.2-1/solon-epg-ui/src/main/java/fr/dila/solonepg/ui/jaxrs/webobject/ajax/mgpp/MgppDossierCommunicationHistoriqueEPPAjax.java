package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.st.ui.bean.DossierHistoriqueEPP;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossierCommunicationHistoriqueEPPAjax")
public class MgppDossierCommunicationHistoriqueEPPAjax extends AbstractMgppDossierHistoriqueEPPAjax {

    public MgppDossierCommunicationHistoriqueEPPAjax() {
        super();
    }

    @Override
    protected DossierHistoriqueEPP getDossierHistoriqueEpp(String id) {
        context.putInContextData(MgppContextDataKey.COMMUNICATION_ID, id);
        return MgppUIServiceLocator.getMgppHistoriqueTreeUIService().getHistoriqueEPP(context);
    }
}
