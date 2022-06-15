package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.st.ui.bean.SuggestionDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface MgppSuggestionUIService {
    List<SuggestionDTO> getSuggestions(SpecificContext context);
}
