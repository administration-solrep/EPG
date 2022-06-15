package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgFeuilleRouteUIService;
import fr.dila.solonepg.ui.services.impl.EpgFeuilleRouteUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgFeuilleRouteUIComponent
    extends ServiceEncapsulateComponent<EpgFeuilleRouteUIService, EpgFeuilleRouteUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgFeuilleRouteUIComponent() {
        super(EpgFeuilleRouteUIService.class, new EpgFeuilleRouteUIServiceImpl());
    }
}
