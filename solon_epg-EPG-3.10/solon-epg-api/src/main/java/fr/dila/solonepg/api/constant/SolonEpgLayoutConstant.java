package fr.dila.solonepg.api.constant;

/**
 * Layout de l'application SOLON EPG.
 * 
 * @author arolin
 */
public final class SolonEpgLayoutConstant {

    /**
     * Nom du layout de l'espace de traitement : layout affichant des dossiers link
     */
    public static final String ESPACE_TRAITEMENT_LAYOUT = "espace_traitement_listing";

    /**
     * Nom du layout générique pour afficher une liste de dossier (utilisé dans la liste des dossiers à indexer et dans l'espace de suivi)
     */
    public static final String DOSSIER_LISTING_LAYOUT = "dossier_listing";

    /**
     * Nom du layout générique pour une liste de dossier dans l'espace d'administration (utilisé pour les listes de suppression, d'archiavge et d'abandon de dossier )
     */
    public static final String DOSSIER_LISTING_ADMIN_LAYOUT = "dossier_listing_admin";
    
    /**
     * Nom du layout de l'espace de création : affiche la liste des dossiers avec de plus la colonne "Complet"
     */
    public static final String ESPACE_CREATION_LAYOUT = "espace_creation_listing";
    
    /**
     * Nom du layout de l'espace de recherche : affiche la liste des dossiers avec une grande possibilité de tri
     */
    public static final String RECHERCHE_DOSSIER_LAYOUT = "recherche_dossier_listing";
    
    /**
     * Nom du layout des instances de feuille de route
     */
    public static final String FEUILLE_ROUTE_LAYOUT = "feuille_route_instance_listing";
    
    
    public static final String DOSSIER_LISTING_DTO_LAYOUT = "dossier_listing_dto";
    
    /** constant utility class */
    private SolonEpgLayoutConstant(){
        
    }
    
}
