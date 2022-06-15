package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.SelectionToolComponentService;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.platform.actions.Action;

public class SelectionToolComponentServiceImpl implements SelectionToolComponentService {

    @Override
    public Map<String, Object> getData(SpecificContext context) {
        EpgUIServiceLocator.getEpgSelectionToolUIService().initSelectionActionContext(context);
        List<Action> actions = context.getActions(EpgActionCategory.SELECTION_TOOL_ACTIONS);

        // On renvoie dans la map de données le DTO et l'item actif
        Map<String, Object> map = new HashMap<>();
        map.put(
            EpgTemplateConstants.SELECTION_TOOL_TYPE,
            EpgUIServiceLocator.getEpgSelectionToolUIService().getSelectionType(context)
        );
        map.put(
            EpgTemplateConstants.SELECTION_TOOL_ITEMS,
            EpgUIServiceLocator.getEpgSelectionToolUIService().getSelectionList(context)
        );
        map.put(EpgTemplateConstants.SELECTION_TOOL_ACTIONS, actions);

        return map;
    }
}
