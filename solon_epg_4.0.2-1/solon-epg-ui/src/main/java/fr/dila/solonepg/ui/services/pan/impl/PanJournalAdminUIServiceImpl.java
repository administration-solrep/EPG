package fr.dila.solonepg.ui.services.pan.impl;

import fr.dila.solonepg.ui.services.pan.PanJournalAdminUIService;
import fr.dila.ss.ui.services.impl.SSJournalAdminUIServiceImpl;

public class PanJournalAdminUIServiceImpl extends SSJournalAdminUIServiceImpl implements PanJournalAdminUIService {

    @Override
    protected String getProviderName() {
        return "PAN_JOURNAL";
    }
}
