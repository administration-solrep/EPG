package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.api.dto.TexteTranspositionDTO;
import fr.dila.solonepg.api.dto.TranspositionDirectiveDTO;
import fr.dila.solonepg.ui.bean.pan.TextesTranspositionsPanUnsortedList;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface TranspositionDirectiveUIService {
    String saveTransposition(SpecificContext context);

    TranspositionDirectiveDTO getCurrentTranspositionDTO(SpecificContext context);

    String addNewText(SpecificContext context);

    String removeTexteTransposition(SpecificContext context);

    String updateTexteTransposition(SpecificContext context);

    String saveTexteTransposition(SpecificContext context);

    List<TexteTranspositionDTO> refreshTexteTransposition(SpecificContext context);

    TextesTranspositionsPanUnsortedList getTextesTransposition(SpecificContext context);
}
