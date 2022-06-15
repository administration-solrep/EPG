package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.impl.EpgRoutingTaskFilterServiceImpl;
import fr.dila.ss.ui.services.SSRoutingTaskFilterService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgRoutingTaskFilterComponent
    extends ServiceEncapsulateComponent<SSRoutingTaskFilterService, EpgRoutingTaskFilterServiceImpl> {

    /**
     * Default constructor
     */
    public EpgRoutingTaskFilterComponent() {
        super(SSRoutingTaskFilterService.class, new EpgRoutingTaskFilterServiceImpl());
    }
}
