package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgArchiveUIService;
import fr.dila.solonepg.ui.services.impl.EpgArchiveUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgArchiveUIComponent extends ServiceEncapsulateComponent<EpgArchiveUIService, EpgArchiveUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgArchiveUIComponent() {
        super(EpgArchiveUIService.class, new EpgArchiveUIServiceImpl());
    }
}
