package fr.dila.solonepg.api.constant;

/**
 * Les constantes pour les requêtes de dossier
 * @author jgomez
 * 
 */
public class SolonEpgRequeteDossierConstants {
    
    /**
     * type de document requête dossier
     */
    public static final String REQUETE_DOSSIER_DOCUMENT_TYPE = "RequeteDossier";
    
    public static final String REQUETE_DOSSIER_SCHEMA = "requete_dossier";
    
    //*************************************************************
    // Propriétés
    //*************************************************************

    public static final String DANS_BASE_PRODUCTION = "dansBaseProduction";

    public static final String DANS_BASE_ARCHIVAGE_INTERMEDIAIRE = "dansBaseArchivageIntermediaire";

    public static final String SENSIBILITE_CASSE = "dansBaseArchivageIntermediaire";
    
    
    //*************************************************************
    // Catégorie
    //*************************************************************
 
    public static final String CATEGORIE_TRAITEMENT_PAPIER = "traitement_papier";
    
    public static final String CATEGORIE_ACTIVITE_NORMATIVE_LOI = "activite_normative_loi";
    
    public static final String CATEGORIE_ACTIVITE_NORMATIVE_ORDONNANCE = "activite_normative_ordonnance";
    
    public static final String CATEGORIE_TEXTE_SIGNALE = "texte_signale";
    
    
    private SolonEpgRequeteDossierConstants() {
     //Private default constructor
    }
}
