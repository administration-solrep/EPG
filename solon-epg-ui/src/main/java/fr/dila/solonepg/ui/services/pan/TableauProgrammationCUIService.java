package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.core.dto.activitenormative.LigneProgrammationHabilitationDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface TableauProgrammationCUIService {
    /**
     * Construction du tableau de programmation de la loi
     */
    List<LigneProgrammationHabilitationDTO> getCurrentListProgrammationHabilitation(SpecificContext context);

    /**
     * Construction du tableau de suivi de la loi
     */
    List<LigneProgrammationHabilitationDTO> getCurrentListSuiviHabilitation(SpecificContext context);

    /**
     * Verouillage du tableau de programmation
     */
    String lockCurrentProgrammationHabilitation(SpecificContext context);

    /**
     * Deverouillage du tableau de programmation
     */
    String unlockCurrentProgrammationHabilitation(SpecificContext context);

    String getTableauProgrammationLockInfo(SpecificContext context);

    Boolean isCurrentProgrammationHabilitationLocked(SpecificContext context);

    /**
     * publication du tableau de suivi
     */
    String publierTableauSuiviHabilitation(SpecificContext context);

    String getTableauSuiviPublicationInfo(SpecificContext context);
}
