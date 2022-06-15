package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.PanJournalUIService;
import fr.dila.solonepg.ui.services.pan.impl.PanJournalUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class PanJournalUIComponent extends ServiceEncapsulateComponent<PanJournalUIService, PanJournalUIServiceImpl> {

    public PanJournalUIComponent() {
        super(PanJournalUIService.class, new PanJournalUIServiceImpl());
    }
}
