package fr.dila.solonepg.api.enumeration;

import static org.assertj.core.api.Assertions.assertThat;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import java.util.List;
import org.junit.Test;

public class ActiviteNormativeEnumTest {

    @Test
    public void getRightsForProfileWithAllProfiles() {
        List<String> rights = ActiviteNormativeEnum.APPLICATION_DES_LOIS.getRightsForProfile(true, true, true);
        assertThat(rights)
            .containsExactly(
                SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER,
                SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER,
                SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE
            );
    }

    @Test
    public void getRightsForProfileWithOneProfile() {
        List<String> rights = ActiviteNormativeEnum.APPLICATION_DES_LOIS.getRightsForProfile(true, false, false);
        assertThat(rights)
            .containsExactly(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER);
    }

    @Test
    public void getRightsForProfileWithTwoProfileButOneNotSupported() {
        List<String> rights = ActiviteNormativeEnum.ORDONNANCES.getRightsForProfile(true, false, true);
        assertThat(rights).containsExactly(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_READER);
    }
}
