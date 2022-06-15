package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgMailboxListComponentService;
import fr.dila.solonepg.ui.services.impl.EpgMailboxListComponentServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgMailboxListComponentComponent
    extends ServiceEncapsulateComponent<EpgMailboxListComponentService, EpgMailboxListComponentServiceImpl> {

    /**
     * Default constructor
     */
    public EpgMailboxListComponentComponent() {
        super(EpgMailboxListComponentService.class, new EpgMailboxListComponentServiceImpl());
    }
}
