package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgAlerteUIService;
import fr.dila.solonepg.ui.services.impl.EpgAlerteUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgAlerteUIComponent extends ServiceEncapsulateComponent<EpgAlerteUIService, EpgAlerteUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgAlerteUIComponent() {
        super(EpgAlerteUIService.class, new EpgAlerteUIServiceImpl());
    }
}
