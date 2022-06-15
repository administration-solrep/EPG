package fr.dila.solonepg.api.parapheur;

import static fr.dila.solonepg.api.parapheur.ParapheurModelType.PARAPHEUR_MODEL_FOLDER_UNDELETABLE;
import static org.assertj.core.api.Assertions.assertThat;

import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import org.junit.Test;

public class ParapheurModelTypeTest {

    @Test
    public void getEnum() {
        ParapheurModelType type = ParapheurModelType.getEnum(
            SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_UNDELETABLE
        );

        assertThat(type).isEqualTo(PARAPHEUR_MODEL_FOLDER_UNDELETABLE);
    }

    @Test
    public void getEnumWithUnknownValue() {
        ParapheurModelType type = ParapheurModelType.getEnum("Unknown value");

        assertThat(type).isNull();
    }
}
