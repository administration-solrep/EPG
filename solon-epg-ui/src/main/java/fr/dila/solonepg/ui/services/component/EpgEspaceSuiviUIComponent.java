package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgEspaceSuiviUIService;
import fr.dila.solonepg.ui.services.impl.EpgEspaceSuiviUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgEspaceSuiviUIComponent
    extends ServiceEncapsulateComponent<EpgEspaceSuiviUIService, EpgEspaceSuiviUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgEspaceSuiviUIComponent() {
        super(EpgEspaceSuiviUIService.class, new EpgEspaceSuiviUIServiceImpl());
    }
}
