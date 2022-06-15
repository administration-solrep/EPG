package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgModeleFdrListUIService;
import fr.dila.solonepg.ui.services.impl.EpgModeleFdrListUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgModeleFdrListUIComponent
    extends ServiceEncapsulateComponent<EpgModeleFdrListUIService, EpgModeleFdrListUIServiceImpl> {

    public EpgModeleFdrListUIComponent() {
        super(EpgModeleFdrListUIService.class, new EpgModeleFdrListUIServiceImpl());
    }
}
