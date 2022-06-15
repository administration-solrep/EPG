package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EPGSmartNXQLQueryUIService;
import fr.dila.solonepg.ui.services.impl.EPGSmartNXQLQueryUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EPGSmartNXQLQueryUIComponent
    extends ServiceEncapsulateComponent<EPGSmartNXQLQueryUIService, EPGSmartNXQLQueryUIServiceImpl> {

    /**
     * Default constructor
     */
    public EPGSmartNXQLQueryUIComponent() {
        super(EPGSmartNXQLQueryUIService.class, new EPGSmartNXQLQueryUIServiceImpl());
    }
}
