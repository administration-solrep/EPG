package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.st.ui.bean.DossierHistoriqueEPP;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossierSimpleHistoriqueEPPAjax")
public class MgppDossierSimpleHistoriqueEPPAjax extends AbstractMgppDossierHistoriqueEPPAjax {

    public MgppDossierSimpleHistoriqueEPPAjax() {
        super();
    }

    @Override
    protected DossierHistoriqueEPP getDossierHistoriqueEpp(String id) {
        context.setCurrentDocument(id);
        return MgppUIServiceLocator.getMgppHistoriqueTreeUIService().getHistoriqueEPPFiche(context);
    }
}
