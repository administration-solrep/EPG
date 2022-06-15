package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgParametrageParapheurUIService;
import fr.dila.solonepg.ui.services.impl.EpgParametrageParapheurUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgParametrageParapheurUIComponent
    extends ServiceEncapsulateComponent<EpgParametrageParapheurUIService, EpgParametrageParapheurUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgParametrageParapheurUIComponent() {
        super(EpgParametrageParapheurUIService.class, new EpgParametrageParapheurUIServiceImpl());
    }
}
