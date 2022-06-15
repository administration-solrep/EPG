package fr.dila.solonepg.ui.enums.mgpp;

/**
 * Enum du type de champ utilisé dans le format json à destination du client
 */
public enum MgppTypeChamp {
    TEXT("text"),
    FILE_MULTI("file-multi");

    private final String name;

    MgppTypeChamp(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
