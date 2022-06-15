package fr.dila.solonepg.core.export.enums;

import static org.assertj.core.api.Assertions.assertThat;

import fr.dila.st.core.AbstractTestSortableEnum;
import org.junit.Test;

public class EpgExcelHeaderTest extends AbstractTestSortableEnum<EpgExcelHeader> {

    @Override
    protected Class<EpgExcelHeader> getEnumClass() {
        return EpgExcelHeader.class;
    }

    @Test
    public void getLabel() {
        assertThat(EpgExcelHeader.CREE_PAR.getLabel()).isEqualTo("export.header.cree.par");
        assertThat(EpgExcelHeader.DATE_PUBLICATION.getLabel()).isEqualTo("export.header.date.publication");
        assertThat(EpgExcelHeader.DERNIER_CONTRIBUTEUR.getLabel()).isEqualTo("export.header.dernier.contributeur");
        assertThat(EpgExcelHeader.NOR.getLabel()).isEqualTo("export.header.nor");
    }
}
