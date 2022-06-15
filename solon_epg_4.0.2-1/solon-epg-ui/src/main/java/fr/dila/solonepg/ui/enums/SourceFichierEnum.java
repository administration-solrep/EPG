package fr.dila.solonepg.ui.enums;

public enum SourceFichierEnum {
    PARAPHEUR("parapheur"),
    FOND_DOSSIER("fond de dossier");

    private String value;

    SourceFichierEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
