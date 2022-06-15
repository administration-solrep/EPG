package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.services.DossierCreationComponentService;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.platform.actions.Action;

public class DossierCreationComponentServiceImpl implements DossierCreationComponentService {

    @Override
    public Map<String, Object> getData(SpecificContext context) {
        List<Action> actions = context.getActions(EpgActionCategory.DOSSIER_CREATION_ACTIONS);

        // On renvoie dans la map de données le DTO et l'item actif
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("dossierCreationActions", actions);

        return returnMap;
    }
}
