package fr.dila.solonepg.api.constant;

/**
 * Constantes permettant de construire les ACL / ACE de l'application SOLON EPG.
 * 
 * @author jtremeaux
 */
public class SolonEpgAclConstant {
    // *************************************************************
    // ACL
    // *************************************************************
    /**
     * ACL des mesures nominatives.
     */
    public final static String MESURE_NOMINATIVE_ACL = "mesure_nominative";

    /**
     * ACL du rattachement du dossier.
     */
    public final static String RATTACHEMENT_ACL = "rattachement";

    /**
     * ACL indexation du dossier.
     */
    public final static String INDEXATION_ACL = "indexation";

    // *************************************************************
    // ACE des dossiers
    // *************************************************************
    /**
     * Préfixe des ACE permettant de voir les dossiers via la distribution dans les ministères.
     */
    public final static String DOSSIER_DIST_MIN_ACE_PREFIX = "dossier_dist_min-";

    /**
     * Préfixe des ACE permettant de voir les dossiers via la distribution dans les ministères.
     */
    public final static String DOSSIER_DIST_DIR_ACE_PREFIX = "dossier_dist_dir-";

    /**
     * Préfixe des ACE permettant de voir les dossiers via la distribution dans les ministères.
     */
    public final static String DOSSIER_RATTACH_MIN_ACE_PREFIX = "dossier_rattach_min-";

    /**
     * Préfixe des ACE permettant de voir les dossiers via la distribution dans les ministères.
     */
    public final static String DOSSIER_RATTACH_DIR_ACE_PREFIX = "dossier_rattach_dir-";

    /**
     * Préfixe des ACE permettant de voir les dossiers du ministère dans la corbeille "pour indexation"
     */
    public final static String INDEXATION_MIN_ACE_PREFIX = "dossier_index_min-";

    /**
     * Préfixe des ACE permettant de voir les dossiers publié du ministère dans la corbeille "pour indexation"
     */
    public final static String INDEXATION_MIN_PUBLI_ACE_PREFIX = "dossier_index_publi_min-";

    /**
     * Préfixe des ACE permettant de voir les dossiers de la direction dans la corbeille "pour indexation"
     */
    public final static String INDEXATION_DIR_ACE_PREFIX = "dossier_index_dir-";

    /**
     * Préfixe des ACE permettant de voir les dossiers publié de la direction dans la corbeille "pour indexation"
     */
    public final static String INDEXATION_DIR_PUBLI_ACE_PREFIX = "dossier_index_publi_dir-";
    
    
    private SolonEpgAclConstant() {
     //Private default constructor
    }
}
