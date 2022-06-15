package fr.dila.solonepg.ui.enums;

public enum EpgOngletAttenteSignatureEnum {
    LOIS("1"),
    AUTRES("0");

    private final String id;

    EpgOngletAttenteSignatureEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
