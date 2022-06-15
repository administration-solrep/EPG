package fr.dila.solonepg.core.component;

import fr.dila.solonepg.api.service.EPGMailboxService;
import fr.dila.solonepg.core.service.EPGMailboxServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EPGMailboxComponent extends ServiceEncapsulateComponent<EPGMailboxService, EPGMailboxServiceImpl> {

    public EPGMailboxComponent() {
        super(EPGMailboxService.class, new EPGMailboxServiceImpl());
    }
}
