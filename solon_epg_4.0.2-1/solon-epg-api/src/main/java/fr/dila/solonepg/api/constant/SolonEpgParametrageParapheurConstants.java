package fr.dila.solonepg.api.constant;

public final class SolonEpgParametrageParapheurConstants {
    public static final String PARAMETRAGE_PARAPHEUR_DOCUMENT_TYPE = "ParapheurFolder";
    public static final String PARAMETRAGE_PARAPHEUR_PREFIX = "pf";

    public static final String PARAMETRAGE_PARAPHEUR_EST_NON_VIDE = "estNonVide";
    public static final String PARAMETRAGE_PARAPHEUR_NB_DOC_ACCEPTE_MAX = "nbDocAccepteMax";
    public static final String PARAMETRAGE_PARAPHEUR_FEUILLE_STYLE_FILES = "feuilleStyleFiles";
    public static final String PARAMETRAGE_PARAPHEUR_FORMAT_AUTORISE = "formatAutorise";

    public static final String PARAMETRAGE_PARAPHEUR_XPATH_EST_NON_VIDE =
        PARAMETRAGE_PARAPHEUR_PREFIX + ":" + PARAMETRAGE_PARAPHEUR_EST_NON_VIDE;

    public static final String PARAMETRAGE_PARAPHEUR_XPATH_NB_DOC_ACCEPTE_MAX =
        PARAMETRAGE_PARAPHEUR_PREFIX + ":" + PARAMETRAGE_PARAPHEUR_NB_DOC_ACCEPTE_MAX;

    public static final String PARAMETRAGE_PARAPHEUR_XPATH_FEUILLE_STYLE_FILES =
        PARAMETRAGE_PARAPHEUR_PREFIX + ":" + PARAMETRAGE_PARAPHEUR_FEUILLE_STYLE_FILES;

    public static final String PARAMETRAGE_PARAPHEUR_XPATH_FORMAT_AUTORISE =
        PARAMETRAGE_PARAPHEUR_PREFIX + ":" + PARAMETRAGE_PARAPHEUR_FORMAT_AUTORISE;
}
