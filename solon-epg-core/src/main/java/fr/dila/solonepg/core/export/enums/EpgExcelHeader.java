package fr.dila.solonepg.core.export.enums;

import fr.dila.st.core.export.enums.ExcelHeader;

public enum EpgExcelHeader implements ExcelHeader {
    CREE_PAR("cree.par"),
    DATE_ARRIVEE("date.arrivee"),
    DATE_CREATION("date.creation"),
    DATE_PUBLICATION("date.publication"),
    DERNIER_CONTRIBUTEUR("dernier.contributeur"),
    MINISTERE_ATTACHE("ministere.attache"),
    MINISTERE_RESPONSABLE("ministere.responsable"),
    NOR,
    RESPONSABLE_DOSSIER("responsable.dossier"),
    TITRE_ACTE("titre.acte"),
    TYPE_ACTE("type.acte");

    private final String specificLabelKey;

    EpgExcelHeader() {
        this(null);
    }

    EpgExcelHeader(String specificLabelKey) {
        this.specificLabelKey = specificLabelKey;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getSpecificLabelKey() {
        return specificLabelKey;
    }
}
