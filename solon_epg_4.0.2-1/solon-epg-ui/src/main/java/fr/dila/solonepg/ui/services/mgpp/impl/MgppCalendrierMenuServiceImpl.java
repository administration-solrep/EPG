package fr.dila.solonepg.ui.services.mgpp.impl;

import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.services.mgpp.MgppCalendrierMenuService;
import fr.dila.st.ui.services.impl.FragmentServiceImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.HashMap;
import java.util.Map;

public class MgppCalendrierMenuServiceImpl extends FragmentServiceImpl implements MgppCalendrierMenuService {

    @Override
    public Map<String, Object> getData(SpecificContext context) {
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("calendriersActions", context.getActions(MgppActionCategory.MGPP_CALENDRIER_MENU));
        return mapData;
    }
}
