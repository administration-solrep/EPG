package fr.dila.solonepg.ui.helper;

import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;

public class PanHelper {

    private PanHelper() {}

    public static LoisSuiviesForm getPaginateFormFromUserSession(
        Map<String, Serializable> mapSearch,
        SpecificContext context
    ) {
        LoisSuiviesForm loisSuiviForm;
        if (MapUtils.isEmpty(mapSearch)) {
            loisSuiviForm =
                ObjectHelper.requireNonNullElseGet(
                    UserSessionHelper.getUserSessionParameter(context, getPaginateFormSessionKey(context)),
                    LoisSuiviesForm::new
                );
        } else {
            loisSuiviForm = new LoisSuiviesForm(mapSearch);
        }
        return loisSuiviForm;
    }

    public static String getPaginateFormSessionKey(SpecificContext context) {
        String currentTab = context.getFromContextData(PanContextDataKey.CURRENT_TAB);
        String currentSection = context.getFromContextData(PanContextDataKey.CURRENT_SECTION);
        return PanUserSessionKey.PAGINATE_FORM + currentTab + currentSection;
    }
}
