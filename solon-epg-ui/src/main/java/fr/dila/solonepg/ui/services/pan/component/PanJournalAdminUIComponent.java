package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.PanJournalAdminUIService;
import fr.dila.solonepg.ui.services.pan.impl.PanJournalAdminUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class PanJournalAdminUIComponent
    extends ServiceEncapsulateComponent<PanJournalAdminUIService, PanJournalAdminUIServiceImpl> {

    public PanJournalAdminUIComponent() {
        super(PanJournalAdminUIService.class, new PanJournalAdminUIServiceImpl());
    }
}
