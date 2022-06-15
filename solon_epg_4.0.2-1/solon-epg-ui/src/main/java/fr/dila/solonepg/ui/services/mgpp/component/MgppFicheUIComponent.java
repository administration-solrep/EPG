package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppFicheUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppFicheUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppFicheUIComponent extends ServiceEncapsulateComponent<MgppFicheUIService, MgppFicheUIServiceImpl> {

    /**
     * Default constructor
     */
    public MgppFicheUIComponent() {
        super(MgppFicheUIService.class, new MgppFicheUIServiceImpl());
    }
}
