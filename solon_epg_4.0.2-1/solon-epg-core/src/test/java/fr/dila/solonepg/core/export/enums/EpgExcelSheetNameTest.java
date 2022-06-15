package fr.dila.solonepg.core.export.enums;

import static org.assertj.core.api.Assertions.assertThat;

import fr.dila.ss.core.export.enums.SSExcelHeader;
import fr.dila.st.core.AbstractTestSortableEnum;
import org.junit.Test;

public class EpgExcelSheetNameTest extends AbstractTestSortableEnum<EpgExcelSheetName> {

    @Override
    protected Class<EpgExcelSheetName> getEnumClass() {
        return EpgExcelSheetName.class;
    }

    @Test
    public void getLabel() {
        assertThat(EpgExcelSheetName.ADMIN_DOSSIER.getLabel()).isEqualTo("export.dossier.sheet.name");
        assertThat(EpgExcelSheetName.DOSSIER.getLabel()).isEqualTo("export.dossier.sheet.name");
        assertThat(EpgExcelSheetName.RECHERCHE_DOSSIER.getLabel()).isEqualTo("export.dossier.sheet.name");
    }

    @Test
    public void getHeaders() {
        assertThat(EpgExcelSheetName.ADMIN_DOSSIER.getHeaders())
            .containsExactly(
                EpgExcelHeader.NOR,
                EpgExcelHeader.TITRE_ACTE,
                EpgExcelHeader.DATE_ARRIVEE,
                EpgExcelHeader.DATE_PUBLICATION,
                EpgExcelHeader.TYPE_ACTE
            );
        assertThat(EpgExcelSheetName.DOSSIER.getHeaders())
            .containsExactly(
                EpgExcelHeader.NOR,
                EpgExcelHeader.TITRE_ACTE,
                EpgExcelHeader.MINISTERE_RESPONSABLE,
                EpgExcelHeader.MINISTERE_ATTACHE,
                EpgExcelHeader.RESPONSABLE_DOSSIER
            );
        assertThat(EpgExcelSheetName.RECHERCHE_DOSSIER.getHeaders())
            .containsExactly(
                EpgExcelHeader.NOR,
                EpgExcelHeader.TITRE_ACTE,
                EpgExcelHeader.DATE_CREATION,
                EpgExcelHeader.DERNIER_CONTRIBUTEUR,
                EpgExcelHeader.CREE_PAR,
                SSExcelHeader.STATUT,
                EpgExcelHeader.TYPE_ACTE
            );
    }

    @Test
    public void getHeadersSize() {
        assertThat(EpgExcelSheetName.ADMIN_DOSSIER.getHeadersSize()).isEqualTo(5);
        assertThat(EpgExcelSheetName.DOSSIER.getHeadersSize()).isEqualTo(5);
        assertThat(EpgExcelSheetName.RECHERCHE_DOSSIER.getHeadersSize()).isEqualTo(7);
    }

    @Test
    public void getHeadersLabels() {
        assertThat(EpgExcelSheetName.ADMIN_DOSSIER.getHeadersLabels())
            .containsExactly(
                "export.header.nor",
                "export.header.titre.acte",
                "export.header.date.arrivee",
                "export.header.date.publication",
                "export.header.type.acte"
            );
        assertThat(EpgExcelSheetName.DOSSIER.getHeadersLabels())
            .containsExactly(
                "export.header.nor",
                "export.header.titre.acte",
                "export.header.ministere.responsable",
                "export.header.ministere.attache",
                "export.header.responsable.dossier"
            );
        assertThat(EpgExcelSheetName.RECHERCHE_DOSSIER.getHeadersLabels())
            .containsExactly(
                "export.header.nor",
                "export.header.titre.acte",
                "export.header.date.creation",
                "export.header.dernier.contributeur",
                "export.header.cree.par",
                "export.header.statut",
                "export.header.type.acte"
            );
    }
}
