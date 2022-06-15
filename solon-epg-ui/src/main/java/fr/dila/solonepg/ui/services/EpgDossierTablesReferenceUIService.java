package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.dossier.tablesreference.AdminModeParutionDTO;
import fr.dila.solonepg.ui.bean.dossier.tablesreference.AdminTableReferenceDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface EpgDossierTablesReferenceUIService {
    AdminTableReferenceDTO getReferences(SpecificContext context);

    void saveReferences(SpecificContext context);

    void createModeParution(SpecificContext context);

    List<AdminModeParutionDTO> getListModeParution(SpecificContext context);

    void saveModeParution(SpecificContext context);

    void deleteModeParution(SpecificContext context);
}
