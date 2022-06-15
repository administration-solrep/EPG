package fr.dila.solonepg.ui.jaxrs.webobject.page;

import static fr.dila.ss.ui.enums.SSContextDataKey.IS_STAT_GRAPH;

import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.impl.StatistiquesItem;
import fr.dila.ss.api.birt.BirtReport;
import fr.dila.ss.ui.jaxrs.webobject.page.AbstractSSStatistiques;
import fr.dila.st.core.exception.STAuthorizationException;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Map;

public class BaseEpgStatistiques extends AbstractSSStatistiques {

    protected void checkReportAccess(BirtReport birtReport, String resourceName) {
        if (birtReport == null) {
            throw new STAuthorizationException(resourceName);
        }
    }

    protected void setDownloadExcelStatAction(SpecificContext context, String idStat) {
        Map<Integer, StatistiquesItem> statsGlobalMap = UserSessionHelper.getUserSessionParameter(
            context,
            "statsGlobalMap"
        );
        if (statsGlobalMap == null) {
            statsGlobalMap = EpgUIServiceLocator.getEpgStatistiquesUIService().getStatistquesMapGlobal(context);
        }
        StatistiquesItem statItem = statsGlobalMap.get(Integer.valueOf(idStat));
        if (statItem != null) {
            context.putInContextData(IS_STAT_GRAPH, statItem.isStatGraphBirt());
        } else {
            context.putInContextData(IS_STAT_GRAPH, false);
        }
    }
}
