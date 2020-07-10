package fr.dila.solonepg.api.constant;

public final class RetourDilaConstants {

    public static final String RETOUR_DILA_SCHEMA = "retour_dila";
    public static final String RETOUR_DILA_SCHEMA_PREFIX = "retdila";

    /**
     * Champ technique pour savoir si la première demande "Pour Epreuvage / Pour Publication" a déjà été envoyée
     */
    public static final String RETOUR_DILA_IS_PUBLICATION_EPREUVAGE_DEMANDE_SUIVANTE_PROPERTY = "isPublicationEpreuvageDemandeSuivante";
    
    /**
     * Date de parution au JO
     */
    public static final String RETOUR_DILA_DATE_PARUTION_JORF_PROPERTY = "dateParutionJorf";

    /**
     * Numero de texte au JO
     */
    public static final String RETOUR_DILA_NUMERO_TEXTE_PARUTION_JORF_PROPERTY = "numeroTexteParutionJorf";

    /**
     * Page de parution au JO
     */
    public static final String RETOUR_DILA_PAGE_PARUTION_JORF_PROPERTY = "pageParutionJorf";
    
    /**
     * Mode de parution
     */
    public static final String RETOUR_DILA_MODE_PARUTION_PROPERTY = "modeParution";
    
    public static final String RETOUR_DILA_DATE_PROMULGATION = "datePromulgation";
    
    public static final String RETOUR_DILA_TITRE_OFFICIEL = "titreOfficiel";

    public static final String RETOUR_DILA_LEGISLATURE_PUBLICATION = "legislaturePublication";
    
    /**
     * Données parution Bo 
     */
    
    /**
     * Propriété contenant la liste des parutions Bo
     */
    public static final String RETOUR_DILA_PARUTION_BO_PROPERTY = "parutionBo";
    
    public static final String RETOUR_DILA_NUMERO_TEXTE_PARUTION_BO_PROPERTY = "numeroTexteParutionBo";

    public static final String RETOUR_DILA_DATE_PARUTION_BO_PROPERTY = "dateParutionBo";
    
    public static final String RETOUR_DILA_PAGE_BO_PROPERTY = "pageParutionBo";
    
    private RetourDilaConstants() {
        // private empty constructor
    }

}
