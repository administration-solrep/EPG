package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.FavorisRechercheModeleFeuilleRouteUIService;
import fr.dila.solonepg.ui.services.impl.FavorisRechercheModeleFeuilleRouteUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class FavorisRechercheModeleFeuilleRouteUIComponent
    extends ServiceEncapsulateComponent<FavorisRechercheModeleFeuilleRouteUIService, FavorisRechercheModeleFeuilleRouteUIServiceImpl> {

    public FavorisRechercheModeleFeuilleRouteUIComponent() {
        super(FavorisRechercheModeleFeuilleRouteUIService.class, new FavorisRechercheModeleFeuilleRouteUIServiceImpl());
    }
}
