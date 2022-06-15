package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppCalendrierUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppCalendrierUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppCalendrierUIComponent
    extends ServiceEncapsulateComponent<MgppCalendrierUIService, MgppCalendrierUIServiceImpl> {

    /**
     * Default constructor
     */
    public MgppCalendrierUIComponent() {
        super(MgppCalendrierUIService.class, new MgppCalendrierUIServiceImpl());
    }
}
