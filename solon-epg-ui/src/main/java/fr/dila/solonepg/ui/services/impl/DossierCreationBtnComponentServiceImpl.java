package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.services.DossierCreationBtnComponentService;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.platform.actions.Action;

public class DossierCreationBtnComponentServiceImpl implements DossierCreationBtnComponentService {

    @Override
    public Map<String, Object> getData(SpecificContext context) {
        List<Action> actions = context.getActions(EpgActionCategory.DOSSIER_CREATION_BUTTON_ACTION);

        // On renvoie dans la map de données le DTO
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("actions", actions);

        return returnMap;
    }
}
