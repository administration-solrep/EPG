package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgSuiviLibreFooterService;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Map;
import org.apache.commons.collections4.map.HashedMap;

public class EpgSuiviLibreFooterServiceImpl implements EpgSuiviLibreFooterService {

    @Override
    public Map<String, Object> getData(SpecificContext context) {
        boolean showFooter = context.computeFromContextDataIfAbsent(
            EpgContextDataKey.SHOW_SUIVI_LIBRE_FOOTER,
            () -> false
        );
        Map<String, Object> map = new HashedMap<>();
        map.put(EpgTemplateConstants.SHOW_SUIVI_LIBRE_FOOTER, showFooter);
        String urlSuiviApplicationLois = STServiceLocator
            .getSTParametreService()
            .getParametreWithoutSession(SolonEpgParametreConstant.PAGE_SUIVI_APPLICATION_LOIS);
        map.put(EpgTemplateConstants.URL_SUIVI_APPLICATION_LOIS, urlSuiviApplicationLois);
        return map;
    }
}
