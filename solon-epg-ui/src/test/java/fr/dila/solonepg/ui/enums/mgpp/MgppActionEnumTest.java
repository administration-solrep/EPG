package fr.dila.solonepg.ui.enums.mgpp;

import static org.assertj.core.api.Assertions.assertThat;

import fr.dila.st.core.AbstractTestSortableEnum;
import org.junit.Test;

public class MgppActionEnumTest extends AbstractTestSortableEnum<MgppActionEnum> {

    @Override
    protected Class<MgppActionEnum> getEnumClass() {
        return MgppActionEnum.class;
    }

    @Test
    public void actionEnumFromValue() {
        assertThat(MgppActionEnum.fromValue("toto")).isNull();

        assertThat(MgppActionEnum.fromValue("ADMIN_MENU_PARAM_PARAM_MGPP"))
            .isEqualTo(MgppActionEnum.ADMIN_MENU_PARAM_PARAM_MGPP);

        assertThat(MgppActionEnum.fromValue("toto", MgppActionEnum.ADMIN_MENU_PARAM_PARAM_MGPP))
            .isEqualTo(MgppActionEnum.ADMIN_MENU_PARAM_PARAM_MGPP);

        assertThat(MgppActionEnum.fromValue("mgpp_decret", MgppActionEnum.ADMIN_MENU_PARAM_PARAM_MGPP))
            .isEqualTo(MgppActionEnum.DOSSIER_DECRET);
    }
}
