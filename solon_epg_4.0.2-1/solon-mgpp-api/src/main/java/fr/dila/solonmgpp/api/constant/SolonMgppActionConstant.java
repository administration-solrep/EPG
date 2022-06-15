package fr.dila.solonmgpp.api.constant;

public final class SolonMgppActionConstant {
    /**
     * Menu espace parlementaire
     */
    public static final String ESPACE_PARLEMENTAIRE = "espace_parlementaire";

    /****************
     * Corbeilles
     ****************/
    public static final String PROCEDURE_LEGISLATIVE = "mgpp_procedureLegislative";
    public static final String PUBLICATION = "mgpp_publication";
    public static final String DEPOT_DE_RAPPORT = "mgpp_depotDeRapport";
    public static final String DESIGNATION_OEP = "mgpp_designationOEP";
    public static final String AVIS_NOMINATION = "mgpp_avisNomination";
    public static final String DECRET = "mgpp_decret";
    public static final String INTERVENTION_EXTERIEURE = "mgpp_interventionExterieure";
    public static final String RESOLUTION_ARTICLE_341 = "mgpp_resolutionArticle341";
    public static final String SUIVI = "mgpp_suivi";
    public static final String RECHERCHE = "mgpp_recherche";
    public static final String RECHERCHE_FICHE_LOI = "mgpp_recherche_fiche_loi";
    public static final String RECHERCHE_FICHE_OEP = "mgpp_recherche_fiche_presentation_oep";
    public static final String RECHERCHE_FICHE_AVI = "mgpp_recherche_fiche_presentation_avi";
    public static final String RECHERCHE_FICHE_DR = "mgpp_recherche_fiche_presentation_dr";
    public static final String RECHERCHE_FICHE_DPG = "mgpp_recherche_fiche_presentation_dpg";
    public static final String RECHERCHE_FICHE_SD = "mgpp_recherche_fiche_presentation_sd";
    public static final String RECHERCHE_FICHE_JSS = "mgpp_recherche_fiche_presentation_jss";
    public static final String RECHERCHE_FICHE_AUD = "mgpp_recherche_fiche_presentation_aud";
    public static final String RECHERCHE_FICHE_DOC = "mgpp_recherche_fiche_presentation_doc";
    public static final String RECHERCHE_FICHE_DECRET = "mgpp_recherche_fiche_presentation_d3cret";
    public static final String RECHERCHE_FICHE_341 = "mgpp_recherche_fiche_presentation_341";
    public static final String RECHERCHE_FICHE_IE = "mgpp_recherche_fiche_presentation_ie";
    public static final String DECLARATION_DE_POLITIQUE_GENERALE = "mgpp_declarationDePolitiqueGenerale";
    public static final String DECLARATION_SUR_UN_SUJET_DETERMINE = "mgpp_declarationSurUnSujetDetermine";
    public static final String DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C = "mgpp_demandeMiseEnOeuvreArticle283C";
    public static final String DEMANDE_AUDITION = "mgpp_demandeAudition";
    public static final String AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES = "mgpp_autresDocumentsTransmisAuxAssemblees";

    /**
     * Tabs
     */
    public static final String TAB_MESSAGE_HISTORIQUE_EPP = "TAB_MESSAGE_HISTORIQUE_EPP";

    /**
     * Menu gauche de l'espace corbeille
     */
    public static final String LEFT_MENU_ESPACE_CORBEILLE = "LEFT_MENU_ESPACE_CORBEILLE";

    /**
     * Catégorie d'action sur les evenements
     */
    public static final String VERSION_ACTION_CATEGORY = "VERSION_ACTIONS";

    /**
     * Action de creation de l'alerte
     */
    public static final String CREER_ALERTE = "CREER_ALERTE";

    /**
     * Action possible sur une version : mettre le message en attente.
     */
    public static final String VERSION_ACTION_METTRE_EN_ATTENTE = "METTRE_EN_ATTENTE";

    /**
     * Action possible sur une version : mettre le message en attente.
     */
    public static final String VERSION_LIAISON_OEP = "LIER_UN_OEP";

    /**
     * Action possible sur une version : relancer le message en attente.
     */
    public static final String VERSION_ACTION_RELANCER_MESSAGE = "RELANCER";

    private SolonMgppActionConstant() {
        // private default constructor
    }
}
