package fr.dila.solonepg.core.export.enums;

import com.google.common.collect.ImmutableList;
import fr.dila.ss.core.export.enums.SSExcelHeader;
import fr.dila.st.core.export.enums.ExcelHeader;
import fr.dila.st.core.export.enums.ExcelSheetName;
import fr.dila.st.core.util.ResourceHelper;
import java.util.List;
import java.util.Optional;

public enum EpgExcelSheetName implements ExcelSheetName {
    ADMIN_DOSSIER(
        "dossier",
        ImmutableList.of(
            EpgExcelHeader.NOR,
            EpgExcelHeader.TITRE_ACTE,
            EpgExcelHeader.DATE_ARRIVEE,
            EpgExcelHeader.DATE_PUBLICATION,
            EpgExcelHeader.TYPE_ACTE
        )
    ),
    DOSSIER(
        ImmutableList.of(
            EpgExcelHeader.NOR,
            EpgExcelHeader.TITRE_ACTE,
            EpgExcelHeader.MINISTERE_RESPONSABLE,
            EpgExcelHeader.MINISTERE_ATTACHE,
            EpgExcelHeader.RESPONSABLE_DOSSIER
        )
    ),
    RECHERCHE_DOSSIER(
        "dossier",
        ImmutableList.of(
            EpgExcelHeader.NOR,
            EpgExcelHeader.TITRE_ACTE,
            EpgExcelHeader.DATE_CREATION,
            EpgExcelHeader.DERNIER_CONTRIBUTEUR,
            EpgExcelHeader.CREE_PAR,
            SSExcelHeader.STATUT,
            EpgExcelHeader.TYPE_ACTE
        )
    );

    private final String labelKey;
    private final List<ExcelHeader> headers;

    EpgExcelSheetName(List<ExcelHeader> headers) {
        this(null, headers);
    }

    EpgExcelSheetName(String labelKey, List<ExcelHeader> headers) {
        this.labelKey = labelKey;
        this.headers = headers;
    }

    @Override
    public String getLabel() {
        return ResourceHelper.getString(
            "export." + Optional.ofNullable(labelKey).orElse(name().toLowerCase()) + ".sheet.name"
        );
    }

    @Override
    public List<ExcelHeader> getHeaders() {
        return headers;
    }
}
