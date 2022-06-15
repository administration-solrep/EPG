package fr.dila.solonepg.ui.enums;

import static org.assertj.core.api.Assertions.assertThat;

import fr.dila.st.core.AbstractTestSortableEnum;
import org.junit.Test;

public class EpgColumnEnumTest extends AbstractTestSortableEnum<EpgColumnEnum> {

    @Override
    protected Class<EpgColumnEnum> getEnumClass() {
        return EpgColumnEnum.class;
    }

    @Test
    public void testSpecificColumnsAreAlwaysVisible() {
        assertThat(EpgColumnEnum.values())
            .allSatisfy(
                col ->
                    assertThat(
                            col == EpgColumnEnum.TITRE_ACTE ||
                            col == EpgColumnEnum.NUMERO_NOR ||
                            !col.isAlwaysDisplayed()
                        )
                        .isTrue()
            );
    }
}
