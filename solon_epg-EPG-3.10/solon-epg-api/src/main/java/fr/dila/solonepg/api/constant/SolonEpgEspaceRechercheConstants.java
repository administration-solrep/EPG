package fr.dila.solonepg.api.constant;

/**
 * Constantes liées aux bulletins oficiels et à ses éléments (schéma,type,métadonnées).
 * 
 * @author asatre
 *
 */
public final class SolonEpgEspaceRechercheConstants {

    public static final String RESPACE_RECHERCHE_ROOT_NAME = "espace-recherche";
    public static final String RESPACE_RECHERCHE_ROOT_NAME_DOCUMENT_ROOT_TYPE = "EspaceRechercheRoot";
    public static final String ESPACE_RECHERCHE_PATH = "/"+RESPACE_RECHERCHE_ROOT_NAME+"/";
    
    public static final String RESULTAT_CONSULTE_SCHEMA = "resultat_consulte";
    public static final String RESULTAT_CONSULTE_DOCUMENT_TYPE = "ResultatConsulte";
    public static final String RESULTAT_CONSULTE_TITLE = "resultats-consultes";

    public static final String FAVORIS_CONSULTATION_DOCUMENT_TYPE = "FavorisConsultation";
    public static final String FAVORIS_CONSULTATION_SCHEMA = "favoris_consultation";
    public static final String FAVORIS_CONSULTATION_TITLE = "favoris-consultation";
    
    public static final String ESPACE_RECHERCHE_DOSSIERSID = "dossiersIds";
    public static final String ESPACE_RECHERCHE_FDRSID = "fdrsIds";
    public static final String ESPACE_RECHERCHE_USERSNAME = "usersNames";
    
    public static final String TABLEAU_DYNAMIQUE_DOCUMENT_TYPE = "TableauDynamique";
    public static final String TABLEAU_DYNAMIQUE_SCHEMA = "tableau_dynamique";
    public static final String TABLEAU_DYNAMIQUE_QUERY = "query";
    public static final String TABLEAU_DYNAMIQUE_USERSNAME = "usersNames";
    public static final String TABLEAU_DYNAMIQUE_DESTINATAIRES = "destinatairesId";
    public static final String TABLEAU_DYNAMIQUE_PATH = "/case-management/tableau-dynamique-root";
	
    /**
     * Event lors d'une création/modification/suppression d'un tableau dynamique
     */
    public static final String TABLEAU_DYNAMIQUE_CHANGED_EVENT = "TABLEAU_DYNAMIQUE_CHANGED_EVENT";
    
    // Constant utility class
    private SolonEpgEspaceRechercheConstants() {
    }
    
}
