package fr.dila.solonepg.api.constant;

/**
 * Constantes des schémas de l'application SOLON EPG.
 * 
 * @author jtremeaux
 */
public class SolonEpgSchemaConstant {
    // *************************************************************
    // Schéma comment
    // *************************************************************
    /**
     * Nom du schéma comment.
     */
    public static final String COMMENT_SCHEMA = "comment";
    
    /**
     * Préfixe du schéma comment.
     */
    public static final String COMMENT_SCHEMA_PREFIX = "comment";
    
    /**
     * Propriété du schéma comment : Identifiant technique du ministère qui peut voir ce commentaire.
     */
    public static final String COMMENT_VISIBLE_TO_MINISTERE_ID_PROPERTY = "visibleToMinistereId";
    
    /**
     * Propriété du schéma comment : Identifiant technique de l'unité structurelle qui peut voir ce commentaire.
     */
    public static final String COMMENT_VISIBLE_TO_UNITE_STRUCTURELLE_ID_PROPERTY = "visibleToUniteStructurelleId";

    /**
     * Propriété du schéma comment : Identifiant technique du poste qui peut voir ce commentaire.
     */
    public static final String COMMENT_VISIBLE_TO_POSTE_ID_PROPERTY = "visibleToPosteId";
    
    /**
     * Propriété du schéma comment : auteur du commentaire.
     */
    public static final String COMMENT_AUTHOR_PROPERTY = "author";
    
    /**
     * Propriété du schéma comment : text du commentaire.
     */
    public static final String COMMENT_TEXT_PROPERTY = "text";
    
    /**
     * Propriété du schéma comment : date de creation du commentaire.
     */
    public static final String COMMENT_CREATION_DATE_PROPERTY = "creationDate";
    
	// *************************************************************
    // Feuilles de route
    // *************************************************************
    /**
     * Propriété du schéma feuille de route : Direction.
     */
    public static final String SOLON_EPG_FEUILLE_ROUTE_DIRECTION_PROPERTY = "direction";

    /**
     * Propriété du schéma feuille de route : Type d'acte.
     */
    public static final String SOLON_EPG_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY = "typeActe";

    /**
     * Propriété du schéma feuille de route : Numéro.
     */
    public static final String SOLON_EPG_FEUILLE_ROUTE_NUMERO_PROPERTY = "numero";
    
    /**
     * Propriété du schéma feuille de route : demandeValidation.
     */
    public static final String SOLON_EPG_FEUILLE_ROUTE_DEMANDE_VALIDATION_PROPERTY = "demandeValidation";

    // *************************************************************
    // Schéma étape de feuille de route
    // *************************************************************
    /**
     * Propriété du schéma étape de feuille de route : numéro de version.
     */
    public static final String ROUTING_TASK_NUMERO_VERSION_PROPERTY = "numeroVersion";
    
    /**
     * Valeur de la propriété du schéma routing task : État de validation "avis favorable avec correction".
     */
    public static final String ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_CORRECTION_VALUE = "10";

    /**
     * Valeur de la propriété du schéma routing task : État de validation "retour pour modification".
     */
    public static final String ROUTING_TASK_VALIDATION_STATUS_RETOUR_MODIFICATION_VALUE = "15";
    
    // valeur de validation ou de refus de validation dans le case de l'avis CE
    
    /**
     * Valeur de la propriété du schéma routing task : État de validation "avis favorable : pour avis rendu".
     */
    public static final String ROUTING_TASK_VALIDATION_STATUS_POUR_AVIS_RENDU_VALUE = "20";
    

    
    /**
     * Valeur de la propriété du schéma routing task : État de validation "avis défavorable : déssaisissement".
     */
    public static final String ROUTING_TASK_VALIDATION_STATUS_DESSAISISSEMENT_VALUE = "22";
    
    /**
     * Valeur de la propriété du schéma routing task : État de validation "avis défavorable : retrait".
     */
    public static final String ROUTING_TASK_VALIDATION_STATUS_RETRAIT_VALUE = "23";
    
    /**
     * Valeur de la propriété du schéma routing task : État de validation "avis défavorable : refus".
     */
    public static final String ROUTING_TASK_VALIDATION_STATUS_REFUS_VALUE = "24";
    
    /**
     * Valeur de la propriété du schéma routing task : État de validation "avis défavorable : renvoi".
     */
    public static final String ROUTING_TASK_VALIDATION_STATUS_RENVOI_VALUE = "25";
    
    // *************************************************************
    // Schéma étape de squelette de feuille de route
    // *************************************************************
    /**
     * Propriété du schéma étape de feuille de route : type de destinataire (dans le cas du squelette)
     */
    public static final String ROUTING_TASK_TYPE_DESTINATAIRE_PROPERTY = "typeDestinataire";
    
    // *************************************************************
    // Schéma favori de recherche
    // *************************************************************
    /**
     * Nom du schéma favoris_recherche.
     */
    public static final String FAVORIS_RECHERCHE_SCHEMA = "favoris_recherche";

    /**
     * Propriété du schéma favoris_recherche : type de recherche (Dossier, ModeleFeuilleRoute, User).
     */
    public static final String FAVORIS_RECHERCHE_TYPE_PROPERTY = "type";

    /**
     * Propriété du schéma favoris_recherche : Valeur dossier de la propriété type de favori.
     */
    public static final String FAVORIS_RECHERCHE_TYPE_DOSSIER_VALUE = "Dossier";

    /**
      * Propriété du schéma favoris_recherche : Valeur dossier simple de la propriété type de favori.
     */
    public static final String FAVORIS_RECHERCHE_TYPE_DOSSIER_SIMPLE_VALUE = "DossierSimple";
   
	/**
	 * Propriété du schéma favoris_recherche : Valeur dossier simple de la propriété type de favori.
	 */
    public static final String FAVORIS_RECHERCHE_TYPE_ACTIVITE_NORMATIVE_VALUE = "ActiviteNormative";

    /**
     * Propriété du schéma favoris_recherche : Valeur utilisateur de la propriété type de favori.
     */
    public static final String FAVORIS_RECHERCHE_TYPE_USER_VALUE = "User";

    /**
     * Propriété du schéma favoris_recherche : Valeur modèle de feuille de route de la propriété type de favori.
     */
    public static final String FAVORIS_RECHERCHE_TYPE_MODELE_FEUILLE_ROUTE_VALUE = "ModeleFeuilleRoute";

    /**
     * Propriété du schéma favoris_recherche : Requête complète.
     */
    public static final String FAVORIS_RECHERCHE_QUERY_PROPERTY = "fullquery";

    /**
     * Propriété du schéma favoris_recherche : Intitulé de la feuille de route.
     */
    public static final String FAVORIS_RECHERCHE_FEUILLE_ROUTE_TITLE_PROPERTY = "feuilleRouteTitle";

    /**
     * Propriété du schéma favoris_recherche : Identifiant technique de l'utilisateur qui a créé la feuille de route.
     */
    public static final String FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_UTILISATEUR_PROPERTY = "feuilleRouteCreationUtilisateur";

    /**
     * Propriété du schéma favoris_recherche : Date de création de la feuille de route (date minimum).
     */
    public static final String FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_DATE_MIN_PROPERTY = "feuilleRouteCreationDateMin";

    /**
     * Propriété du schéma favoris_recherche : Date de création de la feuille de route (date maximum).
     */
    public static final String FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_DATE_MAX_PROPERTY = "feuilleRouteCreationDateMax";

    /**
     * Propriété du schéma favoris_recherche : Numéro de le feuille de route.
     */
    public static final String FAVORIS_RECHERCHE_FEUILLE_ROUTE_NUMERO_PROPERTY = "feuilleRouteNumero";

    /**
     * Propriété du schéma favoris_recherche : Type d'acte.
     */
    public static final String FAVORIS_RECHERCHE_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY = "feuilleRouteTypeActe";

    /**
     * Propriété du schéma favoris_recherche : Ministère propriétaire du modèle.
     */
    public static final String FAVORIS_RECHERCHE_FEUILLE_ROUTE_MINISTERE_PROPERTY = "feuilleRouteMinistere";

    /**
     * Propriété du schéma favoris_recherche : Feuille de route validée (true, false, non renseigné).
     */
    public static final String FAVORIS_RECHERCHE_FEUILLE_ROUTE_DIRECTION_PROPERTY = "feuilleRouteDirection";

    /**
     * Propriété du schéma favoris_recherche : Feuille de route validée (true, false, non renseigné).
     */
    public static final String FAVORIS_RECHERCHE_FEUILLE_ROUTE_VALIDEE_PROPERTY = "feuilleRouteValidee";

    /**
     * Propriété du schéma favoris_recherche : Type d'étape de la feuille de route (pour attribution, pour information...).
     */
    public static final String FAVORIS_RECHERCHE_ROUTE_STEP_ROUTING_TASK_TYPE_PROPERTY = "routeStepRoutingTaskType";

    /**
     * Propriété du schéma favoris_recherche : Mailbox de distribution de l'étape de la feuille de route.
     */
    public static final String FAVORIS_RECHERCHE_ROUTE_STEP_DISTRIBUTION_MAILBOX_ID_PROPERTY = "routeStepDistributionMailboxId";

    /**
     * Propriété du schéma favoris_recherche : Échéance indicative en nombre de jours.
     */
    public static final String FAVORIS_RECHERCHE_ROUTE_STEP_ECHEANCE_INDICATIVE_PROPERTY = "routeStepEcheanceIndicative";

    /**
     * Propriété du schéma favoris_recherche : Validation automatique de l'étape (true, false, non renseigné).
     */
    public static final String FAVORIS_RECHERCHE_ROUTE_STEP_AUTOMATIC_VALIDATION_PROPERTY = "routeStepAutomaticValidation";

    /**
     * Propriété du schéma favoris_recherche : Obligatoire SGG (true, false, non renseigné).
     */
    public static final String FAVORIS_RECHERCHE_ROUTE_STEP_OBLIGATOIRE_SGG_PROPERTY = "routeStepObligatoireSgg";

    /**
     * Propriété du schéma favoris_recherche : Obligatoire ministère (true, false, non renseigné).
     */
    public static final String FAVORIS_RECHERCHE_ROUTE_STEP_OBLIGATOIRE_MINISTERE_PROPERTY = "routeStepObligatoireMinistere";

    // *************************************************************
    // Schéma liées au demande de publication via webService
    // *************************************************************
    
    /**
     * 
     */
    public static final String SPE_TYPE = "WsSpe";
    
    public static final String SPE_FOLDER_TYPE = "WsSpeRoot";
    
    public static final String SPE_SCHEMA = "wsSpe";
    
    /**
     * Propriété du schéma WsSpe : Identifiant du poste.
     */
    public static final String SPE_POSTE_ID_PROPERTY = "posteId";
    
    /**
     * Propriété du schéma WsSpe : nom du webservice utilisé.
     */
    public static final String SPE_WEBSERVICE_PROPERTY = "webservice";
    
    /**
     * Propriété du schéma WsSpe : nom d'essai de renvoi de la requête via webservice.
     */
    public static final String SPE_NB_ESSAIS_PROPERTY = "nbEssais";
    
    /**
     * Propriété du schéma WsSpe : identifiant du dossier.
     */
    public static final String SPE_ID_DOSSIER_PROPERTY = "idDossier";
    
    /**
     * Propriété du schéma WsSpe : type de publication (publication JO, publication BO, Epreuvage )
     */
    public static final String SPE_TYPE_PUBLICATION_PROPERTY = "typePublication";

    // *************************************************************
    // Schéma liées au schéma de fichier de solon epg
    // *************************************************************
    /**
     * Schema file_solon_epg
     */
    public static final String FILE_SOLON_EPG_SCHEMA = "file_solon_epg";

    // *************************************************************
    // Schéma mode de parution
    // *************************************************************
    public static final String MODE_PARUTION_SCHEMA = "mode_parution";
    
    private SolonEpgSchemaConstant() {
     //Private default constructor
    }
}
