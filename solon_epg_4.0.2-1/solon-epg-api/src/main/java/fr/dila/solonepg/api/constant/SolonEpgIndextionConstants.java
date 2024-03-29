package fr.dila.solonepg.api.constant;

/**
 * Constantes liées à l'indexation (Rubriques et MotsClés) et à ses éléments (schéma,type,métadonnées).
 *
 * @author asatre
 *
 */
public final class SolonEpgIndextionConstants {
    public static final String INDEXATION_PATH = "/case-management/indexation-root";

    public static final String INDEXATION_RUBRIQUE_SCHEMA = "indexation_rubrique_solon_epg";
    public static final String INDEXATION_RUBRIQUE_DOCUMENT_TYPE = "IndexationRubrique";
    public static final String INDEXATION_RUBRIQUE_INTITULE = "intitule";

    public static final String INDEXATION_MOT_CLE_SCHEMA = "indexation_mot_cle_solon_epg";
    public static final String INDEXATION_MOT_CLE_DOCUMENT_TYPE = "IndexationMotCle";
    public static final String INDEXATION_MOT_CLE_INTITULE = "intitule";
    public static final String INDEXATION_MOT_CLE_MINISTERE_IDS = "ministereIds";
    public static final String INDEXATION_MOT_CLE_MOTS_CLES = "mots_cles";
    public static final String INDEXATION_MOT_CLE_CREATOR = "creator";

    public static final String INDEXATION_RUBRIQUE_INTITULE_XPATH =
        INDEXATION_RUBRIQUE_SCHEMA + ":" + INDEXATION_RUBRIQUE_INTITULE;

    public static final String INDEXATION_MOT_CLE_INTITULE_XPATH =
        INDEXATION_MOT_CLE_SCHEMA + ":" + INDEXATION_MOT_CLE_INTITULE;
    public static final String INDEXATION_MOT_CLE_MINISTERE_IDS_XPATH =
        INDEXATION_MOT_CLE_SCHEMA + ":" + INDEXATION_MOT_CLE_MINISTERE_IDS;
    public static final String INDEXATION_MOT_CLE_MOTS_CLES_XPATH =
        INDEXATION_MOT_CLE_SCHEMA + ":" + INDEXATION_MOT_CLE_MOTS_CLES;
    public static final String INDEXATION_MOT_CLE_CREATOR_XPATH =
        INDEXATION_MOT_CLE_SCHEMA + ":" + INDEXATION_MOT_CLE_CREATOR;

    // Constant utility class
    private SolonEpgIndextionConstants() {}
}
