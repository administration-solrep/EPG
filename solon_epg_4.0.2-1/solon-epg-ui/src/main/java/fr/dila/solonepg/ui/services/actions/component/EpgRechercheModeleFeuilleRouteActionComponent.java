package fr.dila.solonepg.ui.services.actions.component;

import fr.dila.solonepg.ui.services.actions.EpgRechercheModeleFeuilleRouteActionService;
import fr.dila.solonepg.ui.services.actions.impl.EpgRechercheModeleFeuilleRouteActionServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgRechercheModeleFeuilleRouteActionComponent
    extends ServiceEncapsulateComponent<EpgRechercheModeleFeuilleRouteActionService, EpgRechercheModeleFeuilleRouteActionServiceImpl> {

    /**
     * Default constructor
     */
    public EpgRechercheModeleFeuilleRouteActionComponent() {
        super(EpgRechercheModeleFeuilleRouteActionService.class, new EpgRechercheModeleFeuilleRouteActionServiceImpl());
    }
}
