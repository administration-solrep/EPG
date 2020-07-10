package fr.dila.solonepg.api.constant;

/**
 * Vues de l'application SOLON EPG.
 * 
 * @author jtremeaux
 */
public class SolonEpgViewConstant {

    // *************************************************************
    // Espace de traitement
    // *************************************************************
    /**
     * Espace de traitement
     */
    public static final String ESPACE_TRAITEMENT_VIEW = "view_espace_traitement";

    // *************************************************************
    // Espace de création
    // *************************************************************
    /**
     * Espace de création
     */
    public static final String ESPACE_CREATION_VIEW = "view_espace_creation";

    // *************************************************************
    // Espace de suivi
    // *************************************************************
    /**
     * Espace de suivi
     */
    public static final String ESPACE_SUIVI_VIEW = "view_espace_suivi";

    /**
     * Espace de suivi : dossier signalés
     */
    public static final String ESPACE_SUIVI_DOSSIER_SIGNALES_VIEW = "view_espace_suivi_dossiers_signales";
    
    /**
     * Espace de suivi : mes alertes
     */
    public static final String ESPACE_SUIVI_MES_ALERTES_VIEW = "view_espace_suivi_mes_alertes";
    
    /**
     * Espace de suivi : Statistiques
     */
    public static final String ESPACE_SUIVI_STATISTIQUES_VIEW = "view_espace_suivi_statistiques";
    
    /**
     * Espace de suivi : Statistiques
     */
    public static final String ESPACE_SUIVI_STATISTIQUES_CONTENT_VIEW_DOSSIER_ARCHIVE = "espace_suivi_statistique_dossier_archivage_content";
    
    /**
     * Espace de suivi : Statistiques
     */
    public static final String ESPACE_SUIVI_STATISTIQUES_VIEW_DOSSIER_ARCHIVE = "view_current_dossiers_archivage_stat";
    /**
     * Dossiers similaires
     */
    public static final String DOSSIERS_SIMILAIRES_VIEW = "view_dossiers_similaires";
    
    /**
     * Bordereau dossier similaire
     */
    public static final String DOSSIER_SIMILAIRE_BORDEREAU_VIEW = "view_dossier_similaire_bordereau";

    /**
     * Liste des dossiers de l'espace de suivi : textes signales.
     */
    public static final String ESPACE_SUIVI_TEXTES_SIGNALES_CONTENT_VIEW = "espace_suivi_textes_signales_content";

    /**
     * Vue des Bulletins Officiels
     */
    public static final String BULLETIN_OFFICIEL_VIEW = "view_bulletin_officiel";
    
    /**
     * Vue du paramétrage Adamant
     */
    public static final String PARAM_ADAMANT_VIEW = "view_param_adamant";
    
    /**
     * Vue de gestion de l'indexation
     */
    public static final String GESTION_INDEXATION_VIEW = "view_gestion_indexation";

    // *************************************************************
    // Espace d'administration
    // *************************************************************
    /**
     * Vue des modèles de fond de dossier.
     */
    public static final String MODELES_PARAPHEUR_VIEW = "view_modeles_parapheur";

    /**
     * Vue des modèles de fond de dossier.
     */
    public static final String MODELES_FOND_DOSSIER_VIEW = "view_modeles_fond_dossier";
    
    /**
     * Vue du paramétrage de l'application.
     */
    public static final String PARAMETRAGE_APPLICATION_VIEW = "view_parametrage_application";

    /**
     * Vue du changement de gouvernement.
     */
    public static final String CHANGEMENT_GOUVERNEMENT_VIEW = "view_migration_gouvernement";

    
    /**
     * Vue du historuque des migrations.
     */
    public static final String HISTORIQUE_MIGRATIONS_VIEW = "view_historique_migrations";

    /**
     * Vue de dossier a supprimer.
     */
    public static final String ADMIN_SUPPRESSION_VIEW = "view_admin_suppression";
    
    /**
     * 
     */
    public static final String ADMIN_SUPPRESSION_CONTENT = "admin_suppression_dossier_content";
    
    /**
     * Vue de dossier transmi au administrateur ministerielle a supprimer.
     */
    public static final String ADMIN_MINISTERIEL_SUPPRESSION_VIEW = "view_admin_ministrielle_suppression";
    
    /**
     * 
     */
    public static final String ADMIN_MINISTERIEL_SUPPRESSION_CONTENET = "admin_ministrielle_suppression_dossier_content";
    
    /**
     * Vue des dossiers a abandonner.
     */
    public static final String ADMIN_ABANDON_VIEW = "view_admin_abandon";
    
    /**
     * Vue des dossiers a abandonner.
     */
    public static final String ADMIN_ABANDON_CONTENT = "admin_abandon_dossier_content";
    
    /**
     * Vue des dossiers transmit au administrateur ministrielle par l'administrateur fonctionelle.
     */
    public static final String ADMIN_SUPPRESSION_VIEW_SUIVI = "view_admin_suppression_suivi";

    /**
     * Vue des dossier a archiver.
     */
    public static final String ADMIN_ARCHIVAGE_ADAMANT_VIEW = "view_admin_archivage_adamant";
    public static final String ADMIN_ARCHIVAGE_ADAMANT_CONTENT = "admin_archivage_adamant_dossier_content";
    public static final String ADMIN_ARCHIVAGE_INTERMEDIAIRE_VIEW = "view_admin_archivage_intermediaire";
    public static final String ADMIN_ARCHIVAGE_INTERMEDIAIRE_CONTENT = "admin_archivage_intermediaire_dossier_content";
    public static final String ADMIN_ARCHIVAGE_DEFINITIVE_VIEW = "view_admin_archivage_definitive";
    public static final String ADMIN_ARCHIVAGE_DEFINITIVE_CONTENT = "admin_archivage_definitive_dossier_content";
    
    /**
     * Vue pour le transfert des dossiers clos à un autre ministere.
     */
    public static final String ADMIN_TRANSFERT_VIEW = "view_admin_transfert";
    
    /**
     * Vue des substitution de postes dans les modèles de feuille de route.
     */
    public static final String ADMIN_MODELE_FEUILLE_ROUTE_MASS_SUBSTITUER_POSTE = "admin_modele_feuille_route_mass_substituer_poste";
    
    /**
     * Espace Table de référence
     */
   public static final String TABLE_REFERENCE_VIEW = "view_table_reference";

    /**
     * Espace suivi  Infocentre SGG
     */
    public static final String ESPACE_SUIVI_INFOCENTRE_SGG = "view_espace_suivi_infocentre_sgg";
    
    /**
     * Espace suivi  Infocentre SGG
     */
    public static final String ESPACE_SUIVI_INFOCENTRE_SGG_TEXTE_SIGNALE = "view_espace_suivi_infocentre_sgg_texte_signale";
    
    /**
     * Espace adminsistration : Batch
     */
    public static final String ESPACE_ADMIN_BATCH = "view_administration_batch";
    
    // *********************************************************
    // Admin des squelettes de feuilles de route (FEV525)
    // *********************************************************

    /**
	 * Vue de la liste des squelettes de feuille de route.
	 */
	public static final String	SQUELETTES_FEUILLE_ROUTE_VIEW				= "view_squelettes_feuille_route";

	/**
	 * Vue du détail d'un squelette de feuille de route.
	 */
	public static final String	SQUELETTE_FEUILLE_ROUTE_VIEW				= "view_squelette_feuille_route";

    /**
     * Vue des creation en masse de modele de feuille de route par utilisation des squelettes
     */
    public static final String CREATE_MODELE_FEUILLE_ROUTE_VIA_SQUELETTE = "create_modele_feuille_route_via_squelette";
	
    
    /**
     * Espace Activite Normative : Application des lois
     */
    public static final String VIEW_ESPACE_ACTIVITE_NORMATIVE = "view_espace_activite_normative";

    // *************************************************************
    // Espace de recherche
    // *************************************************************
    public static final String RECHERCHE_VIEW_PREFIX = "view_recherche_";
    
    /**
     * Les résultats de la recherche NOR
     */
    public static final String RECHERCHE_RESULTATS_NOR_VIEW = RECHERCHE_VIEW_PREFIX + "resultats_nor";
   
    /**
     * La vue du requeteur.
     */
    public static final String REQUETEUR_DOSSIER_VIEW = "view_espace_recherche_dossier";

    /**
     * Les résultats de la recherche par le requeteur de dossier expert
     */
    public static final String RECHERCHE_RESULTATS_REQUETEUR_VIEW = RECHERCHE_VIEW_PREFIX + "resultats_requeteur";
    
    /**
     * Les résultats de la recherche par le requeteur de dossier simple
     */
    public static final String REQUETEUR_DOSSIER_SIMPLE_RESULTATS = "requeteur_dossier_simple_resultats";
    
    /**
     * Les résultats de la recherche par favoris du requêteur de dossier simple
     */
    public static final String REQUETEUR_DOSSIER_SIMPLE_FAVORIS_RESULTATS = "requeteur_dossier_simple_resultats_favoris";
    
    /**
     * Les résultats de la recherche par le requeteur de dossier libre ( elastic search )
     */
    public static final String REQUETEUR_DOSSIER_LIBRE_RESULTATS = "requeteur_dossier_libre_resultats";

    /**
     * Liste des derniers résultats consultés dans l'espace de recherche.
     */
    public static final String ESPACE_RECHERCHE_RESULTATS_CONSULTES_VIEW = "view_recherche_resultats_consultes";
    
    /**
     * Préfixe des vues les liste de favoris de consultation.
     */
    public static final String ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_VIEW = "view_recherche_favoris_consultations";
    
    /**
     * Liste des favoris de consultation des dossiers.
     */
    public static final String ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_DOSSIER_VIEW = ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_VIEW + "_dossier";
    
    /**
     * Liste des favoris de consultation des feuilles de route.
     */
    public static final String ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_FDR_VIEW = ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_VIEW + "_fdr";
    
    /**
     * Liste des favoris de consultation des utilisateurs.
     */
    public static final String ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_USER_VIEW = ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_VIEW + "_user";
    
    /**
     * ContentView espace recherche favoris.
     */
    public static final String RECHERCHE_FAVORIS_CONSULTATION_DOSSIER = "recherche_favoris_consultation_dossier";
    
    /**
     * vue des resultats de recherche utilisaeurs.
     */
    public static final String ESPACE_RECHERCHE_USER_RESULTAT = "requeteur_user_resultat";
    
    /**
     * Liste des derniers résultats consultés dans l'espace de recherche.
     */
    public static final String ESPACE_RECHERCHE_FAVORIS_RECHERCHE_CONTENT_VIEW = "view_recherche_favoris_recherche";
    
    /**
     * Saisie des critères de recherche des dossiers.
     */
    public static final String ESPACE_RECHERCHE_DOSSIER = "view_espace_recherche_dossier";
    
    /**
     * Saisie des critères de recherche des modèles de feuilles de route.
     */
    public static final String VIEW_ESPACE_RECHERCHE_FDR = "view_espace_recherche_fdr";
    
    /**
     * Saisie des critères de recherche d'utilisateur.
     */
    public static final String ESPACE_RECHERCHE_UTILISATEUR = "view_espace_recherche_user";
    
    public static final String ESPACE_RECHERCHE_DOSSIER_RESUTAT = "view_recherche_resultats_requeteur";


    /**
     * Résultats de recherche des modèles de feuille de route.
     */
    public static final String RECHERCHE_MODELE_FEUILLE_ROUTE_RESULTAT = "recherche_modele_feuille_route_resultat";
    
    /**
     * Recherche Simple Dossier.
     */
    public static final String ESPACE_RECHERCHE_DOSSIER_SIMPLE = ESPACE_RECHERCHE_DOSSIER + "_simple";
    
    /**
     * Recherche Experte dossier.
     */
    public static final String ESPACE_RECHERCHE_DOSSIER_EXPERTE = ESPACE_RECHERCHE_DOSSIER + "_experte";

    /**
     * Resultat recherche utilisateur.
     */
    public static final String ESPACE_RECHERCHE_RESULTAT_UTILISATEUR = "view_recherche_utilisateur_resultats";
    
    /**
     * Vue des derniers résultats consultés d'utilisateurs.
     */
    public static final String ESPACE_RECHERCHE_UTILISATEUR_RESULTATS_CONSULTES = "view_recherche_resultats_consultes_user";
    
    /**
     * Vue des derniers résultats consultés de feuilles de route.
     */
    public static final String ESPACE_RECHERCHE_RESULTATS_CONSULTES_FDR = "view_recherche_resultats_consultes_fdr";
    
    /**
     * vue des utilisateurs en favoris de consultations.
     */
    public static final String ESPACE_RECHERCHE_UTILISATEUR_FAVORIS_CONSULTATIONS = "view_recherche_favoris_consultations_user";
    
    /**
     * Vue d'ajout d'un favoris de recherche.
     */
    public static final String ESPACE_RECHERCHE_ADD_FAVORIS_RECHERCHE = "view_recherche_add_favoris_recherche";
    
    /**
     * Vue d'ajout d'un tableau dynamique.
     */
    public static final String ESPACE_RECHERCHE_ADD_TABLEAU_DYNAMIQUE = "view_recherche_add_tableau_dynamique";
    
    /**
     * Vue de modification d'un tableau dynamique.
     */
    public static final String ESPACE_RECHERCHE_EDIT_TABLEAU_DYNAMIQUE = "view_recherche_edit_tableau_dynamique";


    private SolonEpgViewConstant() {
     // private default constructor
    }
    
}
