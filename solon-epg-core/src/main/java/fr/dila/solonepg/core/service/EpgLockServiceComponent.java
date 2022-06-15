package fr.dila.solonepg.core.service;

import fr.dila.st.api.service.STLockService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgLockServiceComponent extends ServiceEncapsulateComponent<STLockService, EpgLockServiceImpl> {

    public EpgLockServiceComponent() {
        super(STLockService.class, new EpgLockServiceImpl());
    }
}
