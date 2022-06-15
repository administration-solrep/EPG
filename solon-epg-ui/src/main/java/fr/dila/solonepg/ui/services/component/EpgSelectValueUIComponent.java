package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgSelectValueUIService;
import fr.dila.solonepg.ui.services.impl.EpgSelectValueUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgSelectValueUIComponent
    extends ServiceEncapsulateComponent<EpgSelectValueUIService, EpgSelectValueUIServiceImpl> {

    public EpgSelectValueUIComponent() {
        super(EpgSelectValueUIService.class, new EpgSelectValueUIServiceImpl());
    }
}
