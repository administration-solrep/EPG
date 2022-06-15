package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.api.dto.OrdonnanceHabilitationDTO;
import fr.dila.solonepg.core.dto.activitenormative.HabilitationDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreLoiDTOImpl;
import fr.dila.solonepg.ui.bean.pan.HabilitationsPanUnsortedList;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.Map;

public interface TexteMaitreHabilitationUIService {
    TexteMaitreLoiDTOImpl getCurrentTexteMaitre(SpecificContext context);

    HabilitationsPanUnsortedList getListHabilitation(SpecificContext context);

    void setListOrdonnance(SpecificContext context);
    List<OrdonnanceHabilitationDTO> getListOrdonnance(SpecificContext context);

    String addNewHabilitation(SpecificContext context);

    String removeHabilitation(SpecificContext context);

    void updateHabilitation(SpecificContext context);

    String addNewOrdonnance(SpecificContext context);
    String removeOrdonnanceHabilitation(SpecificContext context);

    /**
     * reload des habilitations
     */
    List<HabilitationDTOImpl> reloadHabilitation(SpecificContext context);

    /**
     * reload des ordonnances habilitations
     */
    Map<String, OrdonnanceHabilitationDTO> reloadOrdonnanceHabilitation(SpecificContext context);

    String saveOrdonnanceHabilitation(SpecificContext context);

    void updateOrdonnanceHabilitation(SpecificContext context);
}
