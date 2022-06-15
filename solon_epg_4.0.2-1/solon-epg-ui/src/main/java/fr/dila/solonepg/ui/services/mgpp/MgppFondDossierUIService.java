package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.st.ui.bean.DocumentDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface MgppFondDossierUIService {
    boolean isFondDossierVisible(SpecificContext context);

    List<DocumentDTO> getFondDeDossierDocuments(SpecificContext context);

    void addFile(SpecificContext context);

    void deleteFile(SpecificContext context);
}
