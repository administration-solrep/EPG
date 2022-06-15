package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppSelectValueUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppSelectValueUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppSelectValueUIComponent
    extends ServiceEncapsulateComponent<MgppSelectValueUIService, MgppSelectValueUIServiceImpl> {

    public MgppSelectValueUIComponent() {
        super(MgppSelectValueUIService.class, new MgppSelectValueUIServiceImpl());
    }
}
