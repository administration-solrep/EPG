package fr.dila.solonepg.ui.th.impl;

import fr.dila.solonepg.ui.th.EpgThEngineService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgThEngineComponent extends ServiceEncapsulateComponent<EpgThEngineService, EpgThEngineServiceImpl> {

    public EpgThEngineComponent() {
        super(EpgThEngineService.class, new EpgThEngineServiceImpl());
    }
}
