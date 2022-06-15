package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgRechercheTypeMenuService;
import fr.dila.solonepg.ui.services.impl.EpgRechercheTypeMenuServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgRechercheTypeMenuComponent
    extends ServiceEncapsulateComponent<EpgRechercheTypeMenuService, EpgRechercheTypeMenuServiceImpl> {

    /**
     * Default constructor
     */
    public EpgRechercheTypeMenuComponent() {
        super(EpgRechercheTypeMenuService.class, new EpgRechercheTypeMenuServiceImpl());
    }
}
