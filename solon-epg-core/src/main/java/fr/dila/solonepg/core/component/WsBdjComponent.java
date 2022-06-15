package fr.dila.solonepg.core.component;

import fr.dila.solonepg.api.service.WsBdjService;
import fr.dila.solonepg.core.service.WsBdjServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class WsBdjComponent extends ServiceEncapsulateComponent<WsBdjService, WsBdjServiceImpl> {

    public WsBdjComponent() {
        super(WsBdjService.class, new WsBdjServiceImpl());
    }
}
