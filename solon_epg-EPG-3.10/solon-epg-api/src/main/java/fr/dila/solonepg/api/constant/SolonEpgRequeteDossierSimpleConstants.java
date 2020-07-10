package fr.dila.solonepg.api.constant;

/**
 * Les constantes pour les requêtes de dossier
 * @author jgomez
 * 
 */
public class SolonEpgRequeteDossierSimpleConstants {
    
    /**
     * type de document requête dossier
     */
    public static final String REQUETE_DOSSIER_SIMPLE_DOCUMENT_TYPE = "RequeteDossierSimple";
    
    
    public static final String REQUETE_DOSSIER_SIMPLE_CRITERES_PRINCIPAUX_SCHEMA = "requete_dossier_simple_criteres_principaux";
    
    public static final String REQUETE_DOSSIER_SIMPLE_CRITERES_SECONDAIRES_SCHEMA = "requete_dossier_simple_criteres_secondaires";
    
    public static final String REQUETE_DOSSIER_SIMPLE_CRITERES_ETAPES_SCHEMA = "requete_dossier_simple_criteres_etapes";
    
    public static final String REQUETE_DOSSIER_SIMPLE_TEXTE_INTEGRAL_SCHEMA = "requete_dossier_simple_texte_integral";
    
    //*************************************************************
    // Propriétés des critères principaux
    //*************************************************************

    public static final String ID_STATUT_DOSSIER = "idStatutDossier";

    public static final String NUMERO_NOR = "numeroNor";
    
    // Le numéro de NOR qui est réellement utilisé pour faire des recherches sans modifier le champ NOR de l'utilisateur.
    public static final String NUMERO_NOR_MODIFIED = "numeroNorModified";

    public static final String ID_AUTEUR = "idAuteur";
    
    public static final String ID_MINISTERE_RESPONSABLE = "idMinistereResponsable";
    
    public static final String ID_DIRECTION_RESPONSABLE = "idDirectionResponsable";
    
    /** Les métadonnées de la requête sont mal nommées, à cause d'une spec erronée **/
    public static final String TITRE_ACTE = "objet";
    
    public static final String TITRE_ACTE_MODIFIED = "objetModifie";

    public static final String ID_TYPE_ACTE = "idTypeActe";
    
    /**
     * Propriétés qui donne le statut d'archivage des dossiers à rechercher.
     */
    public static final String ID_STATUT_ARCHIVAGE_DOSSIER = "idStatutArchivage";

    //*************************************************************
    // Propriétés des critères secondaires
    //*************************************************************

    public static final String ID_CATEGORY_ACTE = "idCategorieActe";

    public static final String TRANSPOSITION_DIRECTIVE = "transpositionDirective";
    
    public static final String TRANSPOSITION_ORDONNANCE = "transpositionOrdonnance";
    
    public static final String APPLICATION_LOI = "applicationLoi";
    
    public static final String INDEXATION_RUBRIQUE = "indexationRubrique";

    public static final String INDEXATION_RUBRIQUE_MODIFIED = "indexationRubriqueModified";
    
    public static final String INDEXATION_MOTS_CLEFS = "indexationMotsClefs";
    
    public static final String INDEXATION_MOTS_CLEFS_MODIFIED = "indexationMotsClefsModified";
    
    public static final String INDEXATION_CHAMP_LIBRE = "indexationChampLibre";
    
    public static final String INDEXATION_CHAMP_LIBRE_MODIFIED = "indexationChampLibreModified";
     
    public static final String DATE_SIGNATURE_1 = "dateSignature_1";
    
    public static final String DATE_SIGNATURE_2 = "dateSignature_2";
    
    public static final String DATE_PUBLICATION_1 = "datePublication_1";
    
    public static final String DATE_PUBLICATION_2 = "datePublication_2";
   
    public static final String NUMERO_TEXTE = "numeroTexte";
    

    
    //*************************************************************
    // Propriétés des critères étapes
    //*************************************************************

    public static final String ID_ACTION = "idAction";
    
    public static final String ID_STATUT = "idStatut";
    
    public static final String DATE_ACTIVATION_1 = "dateActivation_1";
    
    public static final String DATE_ACTIVATION_2 = "dateActivation_2";
    
    public static final String DATE_VALIDATION_1 = "dateValidation_1";
    
    public static final String DATE_VALIDATION_2 = "dateValidation_2";
    
    public static final String ID_POSTE = "idPoste";
    
    public static final String DISTRIBUTION_MAILBOX_ID = "distributionMailboxId";
    
    public static final String NOTE = "note";
    
    public static final String NOTE_MODIFIED ="noteModified";
    
    public static final String VALIDATION_STATUT_ID = "validationStatus";
    
    public static final String CURRENT_LIFE_CYCLE_STATE = "currentLifeCycleState";
    
    //*************************************************************
    // Propriétés du texte intégral
    //*************************************************************

    public static final String TEXTE_INTEGRAL = "texteIntegral";
    
    public static final String SOUS_CLAUSE_TEXTE_INTEGRAL = "sousClauseTexteIntegral";
    
    public static final String DANS_ACTE = "dansActe";
    
    public static final String DANS_EXTRAIT = "dansExtrait";
    
    public static final String DANS_AUTRE_PIECE_PARAPHEUR = "dansAutrePieceParapheur";
    
    public static final String DANS_FOND_DOSSIER = "dansFondDossier";

    
    private SolonEpgRequeteDossierSimpleConstants() {
        // Private default constructor
    }
}
