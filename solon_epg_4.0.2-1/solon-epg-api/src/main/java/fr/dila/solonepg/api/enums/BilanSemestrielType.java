package fr.dila.solonepg.api.enums;

public enum BilanSemestrielType {
    LOI,
    ORDONNANCE;

    public String getLabel() {
        return name().toLowerCase();
    }
}
