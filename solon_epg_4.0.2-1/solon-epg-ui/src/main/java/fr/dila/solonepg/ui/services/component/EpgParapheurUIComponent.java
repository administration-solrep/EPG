package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgParapheurUIService;
import fr.dila.solonepg.ui.services.impl.EpgParapheurUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgParapheurUIComponent
    extends ServiceEncapsulateComponent<EpgParapheurUIService, EpgParapheurUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgParapheurUIComponent() {
        super(EpgParapheurUIService.class, new EpgParapheurUIServiceImpl());
    }
}
