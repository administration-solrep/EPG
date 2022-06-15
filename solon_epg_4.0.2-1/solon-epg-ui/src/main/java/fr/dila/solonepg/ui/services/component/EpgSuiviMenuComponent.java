package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgSuiviMenuService;
import fr.dila.solonepg.ui.services.impl.EpgSuiviMenuServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgSuiviMenuComponent extends ServiceEncapsulateComponent<EpgSuiviMenuService, EpgSuiviMenuServiceImpl> {

    public EpgSuiviMenuComponent() {
        super(EpgSuiviMenuService.class, new EpgSuiviMenuServiceImpl());
    }
}
