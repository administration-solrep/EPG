package fr.dila.solonepg.ui.services.actions.component;

import fr.dila.solonepg.ui.services.actions.EpgModeleFeuilleRouteActionService;
import fr.dila.solonepg.ui.services.actions.impl.EpgModeleFeuilleRouteActionServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgModeleFeuilleRouteActionComponent
    extends ServiceEncapsulateComponent<EpgModeleFeuilleRouteActionService, EpgModeleFeuilleRouteActionServiceImpl> {

    /**
     * Default constructor
     */
    public EpgModeleFeuilleRouteActionComponent() {
        super(EpgModeleFeuilleRouteActionService.class, new EpgModeleFeuilleRouteActionServiceImpl());
    }
}
