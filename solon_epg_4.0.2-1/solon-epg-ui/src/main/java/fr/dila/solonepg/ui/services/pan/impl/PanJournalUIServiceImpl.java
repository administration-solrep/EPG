package fr.dila.solonepg.ui.services.pan.impl;

import com.google.common.collect.ImmutableSet;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.ui.services.pan.PanJournalUIService;
import fr.dila.ss.ui.services.impl.SSJournalUIServiceImpl;
import java.util.Set;

public class PanJournalUIServiceImpl extends SSJournalUIServiceImpl implements PanJournalUIService {

    @Override
    public Set<String> getCategoryList() {
        return ImmutableSet.of(
            SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_LOIS,
            SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_ORDO,
            SolonEpgEventConstant.CATEGORY_LOG_PAN_SUIVI_HABILITATIONS,
            SolonEpgEventConstant.CATEGORY_LOG_PAN_TRAITES_ACCORD,
            SolonEpgEventConstant.CATEGORY_LOG_PAN_TRANSPO_DIRECTIVES,
            SolonEpgEventConstant.CATEGORY_LOG_PAN_GENERAL,
            SolonEpgEventConstant.CATEGORY_LOG_PAN_RATIFICATION_ORDO
        );
    }
}
