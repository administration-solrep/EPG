package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgRechercheMenuService;
import fr.dila.solonepg.ui.services.impl.EpgRechercheMenuServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgRechercheMenuComponent
    extends ServiceEncapsulateComponent<EpgRechercheMenuService, EpgRechercheMenuServiceImpl> {

    /**
     * Default constructor
     */
    public EpgRechercheMenuComponent() {
        super(EpgRechercheMenuService.class, new EpgRechercheMenuServiceImpl());
    }
}
