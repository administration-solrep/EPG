package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.solonepg.ui.bean.ModeleCourrierDTO;
import fr.dila.solonepg.ui.th.bean.ModeleCourrierConsultationForm;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.util.List;

public interface ModeleCourrierUIService {
    ModeleCourrierDTO getModeleCourrierDTO(SpecificContext context);

    void createModeleCourrier(SpecificContext context);

    void updateModeleCourrier(SpecificContext context);

    ModeleCourrierConsultationForm convertToModeleCourrierConsultationForm(SpecificContext context);

    File buildModeleCourrierForCommunication(SpecificContext context);

    List<SelectValueDTO> getAvailableModels(SpecificContext context);

    File buildModeleCourrierForTraitementPapier(SpecificContext context);
}
