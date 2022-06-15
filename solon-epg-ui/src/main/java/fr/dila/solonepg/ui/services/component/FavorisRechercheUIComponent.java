package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.FavorisRechercheUIService;
import fr.dila.solonepg.ui.services.impl.FavorisRechercheUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class FavorisRechercheUIComponent
    extends ServiceEncapsulateComponent<FavorisRechercheUIService, FavorisRechercheUIServiceImpl> {

    public FavorisRechercheUIComponent() {
        super(FavorisRechercheUIService.class, new FavorisRechercheUIServiceImpl());
    }
}
