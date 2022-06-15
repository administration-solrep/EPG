package fr.dila.solonepg.ui.services;

import fr.dila.ss.ui.bean.SSConsultDossierDTO;
import fr.dila.ss.ui.services.SSDossierUIService;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.io.IOException;

public interface EpgDossierUIService extends SSDossierUIService<SSConsultDossierDTO> {
    boolean canUserUpdateParapheur(SpecificContext context);

    boolean canUserUpdateFdd(SpecificContext context);

    void setActionsInContext(SpecificContext context);

    void initDossierActionContext(SpecificContext context);

    void deleteDossiers(SpecificContext context);

    void retirerDossiersCandidatsArchivageIntermediaire(SpecificContext context);
    void retirerAbandonDossiers(SpecificContext context);

    void removeDossiersCandidatElimination(SpecificContext context);

    void verserDossierBaseArchivageIntermediaire(SpecificContext context);

    void createAndAssignLotToDossiersByIds(SpecificContext context);

    void validateDossiersCandidatElimination(SpecificContext context);

    void retirerDossiersArchivageDefinitive(SpecificContext context);

    SolonStatus checkUserAndDeleteDossierForMassAction(SpecificContext context);

    File generateDossierPdf(SpecificContext context) throws IOException;
}
