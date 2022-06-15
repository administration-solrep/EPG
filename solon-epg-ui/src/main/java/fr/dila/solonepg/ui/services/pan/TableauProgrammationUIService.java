package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.api.exception.WsBdjException;
import fr.dila.solonepg.core.dto.activitenormative.LigneProgrammationDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.IOException;
import java.util.List;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

public interface TableauProgrammationUIService {
    /**
     * Construction du tableau de programmation de la loi
     */
    List<LigneProgrammationDTO> getCurrentListProgrammationLoi(SpecificContext context);

    /**
     * Construction du tableau de suivi de la loi
     */
    List<LigneProgrammationDTO> getCurrentListSuiviLoi(SpecificContext context);

    /**
     * Verouillage du tableau de programmation
     */
    String lockCurrentProgrammationLoi(SpecificContext context);

    /**
     * Deverouillage du tableau de programmation
     */
    String unlockCurrentProgrammationLoi(SpecificContext context);

    String getTableauProgrammationLockInfo(SpecificContext context);

    boolean isCurrentProgrammationLoiLocked(SpecificContext context);

    /**
     * Verouillage du tableau de programmation
     */
    String sauvegarderTableauSuivi(SpecificContext context);

    void publierTableauSuivi(SpecificContext context)
        throws WsBdjException, IOException, FactoryConfigurationError, XMLStreamException;

    String getTableauSuiviPublicationInfo(SpecificContext context);
}
