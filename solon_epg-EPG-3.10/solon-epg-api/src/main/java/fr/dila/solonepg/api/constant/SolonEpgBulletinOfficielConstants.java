package fr.dila.solonepg.api.constant;

/**
 * Constantes liées aux bulletins oficiels et à ses éléments (schéma,type,métadonnées).
 * 
 * @author asatre
 *
 */
public final class SolonEpgBulletinOfficielConstants {

    public static final String BULLETIN_OFFICIEL_SCHEMA = "bulletin_officiel";
    public static final String BULLETIN_OFFICIEL_PATH = "/case-management/bulletin-officiel-root";
    
    public static final String BULLETIN_OFFICIEL_DOCUMENT_TYPE = "BulletinOfficiel";
    
    
    public static final String BULLETIN_OFFICIEL_NOR = "boNor";
    public static final String BULLETIN_OFFICIEL_INTITULE = "boIntitule";
    public static final String BULLETIN_OFFICIEL_ETAT = "boEtat";
    
    // Constant utility class
    private SolonEpgBulletinOfficielConstants() {
    }
    
}
