package fr.dila.solonepg.ui.enums;

public enum EpgUndeletableCauseEnum {
    EXISTING_NOR("epg.dossiers.creation.existing.nor"),
    NOT_INIT("epg.dossiers.creation.not.init"),
    MINISTERE("epg.dossiers.creation.ministere"),
    VERROU("epg.dossiers.creation.verrou");

    private String value;

    EpgUndeletableCauseEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
