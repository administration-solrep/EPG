package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppFondDossierUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppFondDossierUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppFondDossierUIComponent
    extends ServiceEncapsulateComponent<MgppFondDossierUIService, MgppFondDossierUIServiceImpl> {

    /**
     * Default constructor
     */
    public MgppFondDossierUIComponent() {
        super(MgppFondDossierUIService.class, new MgppFondDossierUIServiceImpl());
    }
}
