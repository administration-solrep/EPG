package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.api.dto.DecretApplicationDTO;
import fr.dila.solonepg.ui.bean.pan.LoisDeRatificationPanUnsortedList;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.Map;

public interface TexteMaitreOrdonnanceUIService {
    String reloadOrdonnance(SpecificContext context);

    String saveOrdonnance(SpecificContext context);

    String saveLoiRatification(SpecificContext context);

    String addLoiRatification(SpecificContext context);

    String removeLoiDeRatification(SpecificContext context);

    String updateLoiDeRatification(SpecificContext context);

    void setListDecret(SpecificContext context);

    List<DecretApplicationDTO> getListDecret(SpecificContext context);

    String addNewDecret(SpecificContext context);

    Map<String, DecretApplicationDTO> removeDecret(SpecificContext context);

    Map<String, DecretApplicationDTO> reloadDecrets(SpecificContext context);

    String saveDecret(SpecificContext context);

    String getQuery(SpecificContext context);

    LoisDeRatificationPanUnsortedList getLoiDeRatificationList(SpecificContext context);
}
