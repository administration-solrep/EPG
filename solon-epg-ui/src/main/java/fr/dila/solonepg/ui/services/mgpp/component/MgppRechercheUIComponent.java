package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppRechercheUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppRechercheUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppRechercheUIComponent
    extends ServiceEncapsulateComponent<MgppRechercheUIService, MgppRechercheUIServiceImpl> {

    /**
     * Default constructor
     */
    public MgppRechercheUIComponent() {
        super(MgppRechercheUIService.class, new MgppRechercheUIServiceImpl());
    }
}
