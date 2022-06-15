package fr.dila.solonmgpp.api.constant;

/**
 * @see EPP solonepp-corbeille-type-contrib.xml
 * @author asatre
 *
 */
public final class SolonMgppCorbeilleConstant {
    /************************
     * Procédure législative
     ************************/
    public static final String SECTION_GOUVERNEMENT_PROCEDURE_LEGISLATIVE =
        "SECTION_GOUVERNEMENT_PROCEDURE_LEGISLATIVE";
    public static final String CORBEILLE_GOUVERNEMET_PROC_LEG_RECEPTION = "CORBEILLE_GOUVERNEMET_PROC_LEG_RECEPTION";
    public static final String CORBEILLE_GOUVERNEMET_PROC_LEG_ATTENTE = "CORBEILLE_GOUVERNEMET_PROC_LEG_ATTENTE";
    public static final String CORBEILLE_GOUVERNEMET_PROC_LEG_EMIS = "CORBEILLE_GOUVERNEMET_PROC_LEG_EMIS";
    public static final String CORBEILLE_GOUVERNEMET_PROC_LEG_HISTORIQUE = "CORBEILLE_GOUVERNEMET_PROC_LEG_HISTORIQUE";
    public static final String CORBEILLE_GOUVERNEMENT_CENSURE = "CORBEILLE_GOUVERNEMENT_CENSURE";

    /************************
     * Publication
     ************************/
    public static final String CORBEILLE_GOUVERNEMENT_PUBLICATION = "CORBEILLE_GOUVERNEMENT_PUBLICATION";

    /************************
     * Intervention extérieure
     ************************/
    public static final String CORBEILLE_GOUVERNEMENT_INTERVENTION_EXT_EMIS =
        "CORBEILLE_GOUVERNEMENT_INTERVENTION_EXT_EMIS";
    public static final String CORBEILLE_GOUVERNEMENT_INTERVENTION_EXT_RECU =
        "CORBEILLE_GOUVERNEMENT_INTERVENTION_EXT_RECU";

    /************************
     * Intervention extérieure
     ************************/
    public static final String CORBEILLE_GOUVERNEMENT_RESOLUTION_34_1_EMIS =
        "CORBEILLE_GOUVERNEMENT_RESOLUTION_34-1_EMIS";
    public static final String CORBEILLE_GOUVERNEMENT_RESOLUTION_34_1_RECU =
        "CORBEILLE_GOUVERNEMENT_RESOLUTION_34-1_RECU";

    /************************
     *
     ************************/
    public static final String CORBEILLE_GOUVERNEMENT_RAPPORT = "CORBEILLE_GOUVERNEMENT_RAPPORT";
    public static final String CORBEILLE_GOUVERNEMENT_OEP = "CORBEILLE_GOUVERNEMENT_OEP";
    public static final String CORBEILLE_GOUVERNEMENT_NOMINATION = "CORBEILLE_GOUVERNEMENT_NOMINATION";
    public static final String CORBEILLE_GOUVERNEMENT_DECRET = "CORBEILLE_GOUVERNEMENT_DECRET";
    public static final String CORBEILLE_GOUVERNEMENT_RESOLUTION_34_1 = "CORBEILLE_GOUVERNEMENT_RESOLUTION_34-1";
    public static final String CORBEILLE_GOUVERNEMENT_EMETTEUR = "CORBEILLE_GOUVERNEMENT_EMETTEUR";
    public static final String CORBEILLE_GOUVERNEMENT_DESTINATAIRE = "CORBEILLE_GOUVERNEMENT_EMETTEUR";
    public static final String CORBEILLE_GOUVERNEMENT_COPIE = "CORBEILLE_GOUVERNEMENT_COPIE";

    public static final String ROOT_NODE = "ROOT_NODE";
    public static final String CORBEILLE_NODE = "CORBEILLE_NODE";
    public static final String MGPP_NODE = "MGPP_NODE";

    /**
     * Suffixe des corbeilles d'historique
     */
    public static final String HISTORIQUE_SUFFIXE = "_HISTORIQUE";

    /* **************************************
     * Constantes TABLE MGPP_INFO_CORBEILLE *
     ****************************************/
    public static final String MGPP_INFO_CORBEILLE_TABLE_NAME = "MGPP_INFO_CORBEILLE";
    public static final String COL_CORBEILLE_MGPP_INFO_CORBEILLE = "CORBEILLE";
    public static final String COL_HASMESS_MGPP_INFO_CORBEILLE = "HASMESS";

    private SolonMgppCorbeilleConstant() {
        // default private constructor
    }
}
