package fr.dila.solonepg.api.constant;

/**
 * Content Views de SOLON EPG.
 *
 * @author jtremeaux
 */
public class SolonEpgContentViewConstant {

    private SolonEpgContentViewConstant() {}

    // *************************************************************
    // Espace de suivi
    // *************************************************************
    /**
     * Content view de la liste des dossiers signales de l'espace de suivi.
     */
    public static final String ESPACE_SUIVI_DOSSIERS_SIGNALES_CONTENT_VIEW = "espace_suivi_dossiers_signales_content";

    /**
     * Content view de la liste des dossiers de l'espace de suivi.
     */
    public static final String ESPACE_SUIVI_DOSSIERS_CONTENT_VIEW = "espace_suivi_dossier_content";

    // *************************************************************
    // Espace de création
    // *************************************************************

    /**
     * Content view de la liste des dossiers de l'espace de création.
     */
    public static final String ESPACE_CREATION_DOSSIERS_CONTENT_VIEW = "espace_creation_dossier_content";

    // *************************************************************
    // Espace de recherche
    // *************************************************************
    /**
     * Content view de la liste des résultats de recherche des dossiers.
     */
    public static final String ESPACE_RECHERCHE_DOSSIER_REQUETE = "recherche_requeteur";

    /**
     * Content view de la liste des résultats de recherche des dossiers par NOR.
     */
    public static final String ESPACE_RECHERCHE_DOSSIER_NOR = "recherche_nor";

    /**
     * Content view de la liste des modèles de feuille de route.
     */
    public static final String RECHERCHE_MODELE_FEUILLE_ROUTE_RESULTAT_CONTENT_VIEW =
        "recherche_modele_feuille_route_resultat";

    /**
     * Content view de la liste des derniers modèles de feuille de route consultés.
     */
    public static final String RECHERCHE_RESULTATS_CONSULTES_MODELE_FEUILLE_ROUTE_CONTENT_VIEW =
        "recherche_resultats_consultes_modele_feuille_route";

    /**
     * Content view de la liste des favoris de consultation des modèles de feuille de route.
     */
    public static final String RECHERCHE_FAVORIS_CONSULTATION_MODELE_FEUILLE_ROUTE_CONTENT_VIEW =
        "recherche_favoris_consultation_modele_feuille_route";

    /**
     * Content view de la liste des résultats de recherche des utilisateurs.
     */
    public static final String ESPACE_RECHERCHE_UTILISATEUR_REQUETE = "recherche_user_resultat";

    /**
     * Content view du journal technique
     */
    public static final String ESPACE_JOURNAL = "ADMIN_JOURNAL_DOSSIER";

    /**
     * Content view du journal technique du PAN
     */
    public static final String PAN_JOURNAL = "PAN_JOURNAL";

    /**
     * Content view de la liste des squelettes de feuille de route.
     */
    public static final String FEUILLE_ROUTE_SQUELETTE_FOLDER_CONTENT_VIEW = "feuille_route_squelette_folder_content";
}
