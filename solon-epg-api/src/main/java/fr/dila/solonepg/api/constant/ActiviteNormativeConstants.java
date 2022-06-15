package fr.dila.solonepg.api.constant;

public final class ActiviteNormativeConstants {
    public static final String PAN_CONTEXT_MINISTERE = "ministere";
    public static final String PAN_CONTEXT_TEXTEM = "textemaitre";

    public static final String LEFT_MENU_ACTIVITE_NORMATIVE_ACTION = "LEFT_MENU_ESPACE_ACTIVITE_NORMATIVE";
    public static final String MAIN_MENU_ACTIVITE_NORMATIVE_ACTION = "espace_activite_normative";
    public static final String ACTIVITE_NORMATIVE_ACTION_TAB = "TAB_LIST_ACTIVITE_NORMATIVE";
    public static final String ACTIVITE_NORMATIVE_ACTION_SUB_TAB = "SUBTAB_LIST_ACTIVITE_NORMATIVE";
    public static final String ACTIVITE_NORMATIVE_ACTION_TOOLBAR = "TOOLBAR_ACTIVITE_NORMATIVE";
    public static final String TAB_AN_TEXTE_MAITRE = "texte-maitre";
    public static final String TAB_AN_TABLEAU_PROGRAMMATION = "tableau-programmation";
    public static final String TAB_AN_TABLEAU_SUIVI = "tableau-suivi";
    public static final String TAB_AN_TABLEAU_LOIS = "tableau-lois";
    public static final String TAB_AN_TABLEAU_MAITRE = "tableau-maitre";
    public static final String TAB_AN_TABLEAU_ORDONNANCES = "tableau-ordonnances";
    public static final String TAB_AN_RECHERCHE = "an-recherche";
    public static final String TAB_AN_RESULTATS_RECHERCHE = "TAB_AN_RESULTATS_RECHERCHE";

    public static final String MENU_0 = "MENU_0";
    public static final String MENU_1 = "MENU_1";
    public static final String MENU_2 = "MENU_2";
    public static final String MENU_3 = "MENU_3";
    public static final String MENU_4 = "MENU_4";
    public static final String MENU_5 = "MENU_5";

    public static final String ACTIVITE_NORMATIVE_TABLEAU_MAITRE_APPLICATION = "tableau_maitre_application_lois";
    public static final String ACTIVITE_NORMATIVE_TABLEAU_MAITRE_ORDONNANCES = "tableau_maitre_ordonnances";
    public static final String ACTIVITE_NORMATIVE_TABLEAU_MAITRE_ORDONNANCES_38C = "tableau_maitre_ordonnances_38C";
    public static final String ACTIVITE_NORMATIVE_TABLEAU_MAITRE_TRAITES = "tableau_maitre_traites";
    public static final String ACTIVITE_NORMATIVE_TABLEAU_MAITRE_TRANSPOSITION = "tableau_maitre_transpositions";
    public static final String ACTIVITE_NORMATIVE_TABLEAU_MAITRE_APP_ORDONNANCES =
        "tableau_maitre_application_ordonnances";

    public static final String ACTIVITE_NORMATIVE_ENABLE = "1";
    public static final String ACTIVITE_NORMATIVE_DISABLE = "0";

    public static final String TYPE_OTHER = "0";
    public static final String TYPE_APPLICATION_LOIS = "1";
    public static final String TYPE_ORDONNANCES = "2";
    public static final String TYPE_TRAITES_ACCORDS = "3";
    public static final String TYPE_TRANSPOSITIONS = "4";
    public static final String TYPE_ORDONNANCES_38C = "5";
    public static final String TYPE_APPLICATION_ORDONNANCES = "6";

    public static final String ATTRIBUT_APPLICATION_LOIS = "applicationLoi";
    public static final String ATTRIBUT_APPLICATION_ORDONNANCES = "applicationOrdonnance";
    public static final String ATTRIBUT_TRANSPOSITION = "transposition";
    public static final String ATTRIBUT_ORDONNANCE_38C = "ordonnance38C";
    public static final String ATTRIBUT_ORDONNANCE = "ordonnance";
    public static final String ATTRIBUT_TRAITE_ACCORD = "traite";

    // ////////////////////////////////
    // SHEMA CONSTANTS
    // ///////////////////////////////

    public static final String ACTIVITE_NORMATIVE_SCHEMA = "activite_normative";
    public static final String ACTIVITE_NORMATIVE_SCHEMA_PREFIX = "norma";
    public static final String P_APPLICATION_LOI = "applicationLoi";
    public static final String P_ORDONNANCE = "ordonnance";
    public static final String P_ORDONNANCE_38C = "ordonnance38C";
    public static final String P_TRAITE = "traite";
    public static final String P_TRANSPOSITION = "transposition";
    public static final String P_APPLICATION_ORDONNANCE = "applicationOrdonnance";

    public static final String ACTIVITE_NORMATIVE_PATH = "/case-management/activite-normative-root";
    public static final String ACTIVITE_NORMATIVE_PREFIX = "norma";
    public static final String ACTIVITE_NORMATIVE_DOCUMENT_TYPE = "ActiviteNormative";

    /**
     * Mises à jour ministérielles
     */

    public static final String MAJ_MIN_SCHEMA = "maj_ministerielle";
    public static final String MAJ_MIN_SHEMA_PREFIX = "majm";
    public static final String MAJ_MIN_DOCUMENT_TYPE = "MajMin";
    public static final String MAJ_MIN_PATH = "/case-management/maj-min-root";
    public static final String MAJ_MIN_SELECTION_LIST = "MAJ_MIN_SELECTION";

    public enum MAJ_TYPE {
        AJOUT,
        SUPPRESSION,
        MODIFICATION,
        MINISTERE,
        UNKNOW
    }

    public enum MAJ_TARGET {
        ORDONNANCE,
        TRANSPOSITION,
        APPLICATION_LOI,
        HABILITATION,
        APPLICATION_ORDONNANCE
    }

    public static final String MAJ_MIN_ID_DOSSIER = "idDossier";
    public static final String MAJ_MIN_NOR_DOSSIER = "norDossier";
    public static final String MAJ_MIN_TYPE = "type";
    public static final String MAJ_MIN_REF = "ref";
    public static final String MAJ_MIN_TITRE = "titre";
    public static final String MAJ_MIN_NUMERO_ARTICLE = "numeroArticles";
    public static final String MAJ_MIN_NUMERO_ORDRE = "numeroOrdre";
    public static final String MAJ_MIN_COMMENT_MAJ = "commentaire";
    public static final String MAJ_MIN_DATE_CREATION = "dateCreation";
    public static final String MAJ_MIN_MODIFICATION = "modification";
    public static final String MAJ_MIN_REF_MESURE = "refMesure";

    public static final String RECHERCHE_AN_CATEGORY_MESURE = "mesure";

    public static final String RECHERCHE_AN_CATEGORY_DECRET = "decret";

    public static final String MESURE_FILTER = "10000";

    public enum DEFAULT_TEXM_SORT_COLUMN {
        PROMULGATION,
        PUBLICATION,
        ADOPTION
    }

    private ActiviteNormativeConstants() {
        // private empty constructor
    }
}
