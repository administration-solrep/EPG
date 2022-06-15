package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.impl.EpgJournalUIServiceImpl;
import fr.dila.ss.ui.services.SSJournalUIService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgJournalUIComponent extends ServiceEncapsulateComponent<SSJournalUIService, EpgJournalUIServiceImpl> {

    public EpgJournalUIComponent() {
        super(SSJournalUIService.class, new EpgJournalUIServiceImpl());
    }
}
