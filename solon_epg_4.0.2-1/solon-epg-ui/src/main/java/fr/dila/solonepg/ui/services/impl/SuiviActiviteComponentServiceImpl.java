package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.services.SuiviActiviteComponentService;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.platform.actions.Action;

public class SuiviActiviteComponentServiceImpl implements SuiviActiviteComponentService {

    @Override
    public Map<String, Object> getData(SpecificContext context) {
        List<Action> actions = context.getActions(EpgActionCategory.SUIVI_ACTIVITE_ACTIONS);

        // On renvoie dans la map de données le DTO et l'item actif
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("suiviActiviteActions", actions);

        return returnMap;
    }
}
