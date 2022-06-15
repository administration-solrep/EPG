package fr.dila.solonepg.api.constant;

public final class ModeParutionConstants {
    // *************************************************************
    // Properties mode de parution
    // *************************************************************
    public static final String MODE_PARUTION_SCHEMA = "mode_parution";
    public static final String MODE_PARUTION_PREFIX = "mod";

    public static final String MODE_PARUTION_MODE_PROPERTY = "mode";

    public static final String MODE_PARUTION_MODE_XPATH = MODE_PARUTION_SCHEMA + ":" + MODE_PARUTION_MODE_PROPERTY;

    public static final String MODE_PARUTION_DATE_DEBUT_PROPERTY = "dateDebut";

    public static final String MODE_PARUTION_DATE_DEBUT_XPATH =
        MODE_PARUTION_SCHEMA + ":" + MODE_PARUTION_DATE_DEBUT_PROPERTY;

    public static final String MODE_PARUTION_DATE_FIN_PROPERTY = "dateFin";

    public static final String MODE_PARUTION_DATE_FIN_XPATH =
        MODE_PARUTION_SCHEMA + ":" + MODE_PARUTION_DATE_FIN_PROPERTY;

    public static final String MODE_PARUTION_DOCUMENT_TYPE = "ModeParution";
}
