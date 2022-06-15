package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.st.ui.services.impl.HeaderServiceImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Map;

public class EpgHeaderServiceImpl extends HeaderServiceImpl {

    @Override
    public Map<String, Object> getData(SpecificContext context) {
        Map<String, Object> map = super.getData(context);
        map.put(EpgContextDataKey.NOR.getName(), context.getFromContextData(EpgContextDataKey.NOR));
        return map;
    }
}
