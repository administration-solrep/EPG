package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.SuiviActiviteComponentService;
import fr.dila.solonepg.ui.services.impl.SuiviActiviteComponentServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class SuiviActiviteComponentComponent
    extends ServiceEncapsulateComponent<SuiviActiviteComponentService, SuiviActiviteComponentServiceImpl> {

    /**
     * Default constructor
     */
    public SuiviActiviteComponentComponent() {
        super(SuiviActiviteComponentService.class, new SuiviActiviteComponentServiceImpl());
    }
}
