package fr.dila.solonepg.ui.services;

import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface EpgDossierCreationUIService {
    String creerDossier(SpecificContext context);

    String getDefaultDirection(SpecificContext context);

    String getDefaultMinistere(SpecificContext context);

    List<SelectValueDTO> getNorPrmList(SpecificContext context);

    List<SelectValueDTO> getUserPostes(SpecificContext context);
}
