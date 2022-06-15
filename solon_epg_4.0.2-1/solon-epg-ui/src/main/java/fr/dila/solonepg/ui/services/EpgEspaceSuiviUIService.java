package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.EpgSuiviTreeElementDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Map;
import java.util.Set;

public interface EpgEspaceSuiviUIService {
    EpgSuiviTreeElementDTO addChildrenNode(SpecificContext context);

    EpgSuiviTreeElementDTO loadRootNode(SpecificContext context);

    EpgSuiviTreeElementDTO buildTree(SpecificContext context);

    Map<String, Set<String>> fillMailbox(SpecificContext context);
}
