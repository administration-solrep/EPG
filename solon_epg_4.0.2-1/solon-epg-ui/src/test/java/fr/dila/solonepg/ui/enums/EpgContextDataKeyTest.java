package fr.dila.solonepg.ui.enums;

import static org.assertj.core.api.Assertions.assertThat;

import fr.dila.solonepg.ui.bean.EpgDossierActionDTO;
import fr.dila.st.core.AbstractTestSortableEnum;
import org.junit.Test;

public class EpgContextDataKeyTest extends AbstractTestSortableEnum<EpgContextDataKey> {

    @Override
    protected Class<EpgContextDataKey> getEnumClass() {
        return EpgContextDataKey.class;
    }

    @Test
    public void getTypeValue() {
        assertThat(EpgContextDataKey.DOSSIER_ACTIONS.getValueType()).isEqualTo(EpgDossierActionDTO.class);
    }
}
