package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppDossierUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppDossierUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppDossierUIComponent
    extends ServiceEncapsulateComponent<MgppDossierUIService, MgppDossierUIServiceImpl> {

    /**
     * Default constructor
     */
    public MgppDossierUIComponent() {
        super(MgppDossierUIService.class, new MgppDossierUIServiceImpl());
    }
}
