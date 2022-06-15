package fr.dila.solonepg.ui.enums.pan;

import static org.assertj.core.api.Assertions.assertThat;

import fr.dila.solonepg.api.dto.HabilitationDTO;
import fr.dila.st.core.AbstractTestSortableEnum;
import org.junit.Test;

public class PanContextDataKeyTest extends AbstractTestSortableEnum<PanContextDataKey> {

    @Override
    protected Class<PanContextDataKey> getEnumClass() {
        return PanContextDataKey.class;
    }

    @Test
    public void getTypeValue() {
        assertThat(PanContextDataKey.HABILITATION_DTO.getValueType()).isEqualTo(HabilitationDTO.class);
    }
}
