/**
 *
 */
package fr.dila.solonepg.api.constant;

/**
 * Constantes liés au type de document liste traitement papier.
 *
 * @author antoine Rolin
 *
 */
public final class SolonEpgListeTraitementPapierConstants {
    //*************************************************************
    // ListeTraitementPapier
    //*************************************************************
    /**
     * Préfixe du schéma ListeTraitementPapier.
     */
    public static final String LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX = "lis";

    public static final String LISTE_TRAITEMENT_PAPIER_SCHEMA = "liste_traitement_papier_solon_epg";

    public static final String LISTE_TRAITEMENT_PAPIER_DOCUMENT_TYPE = "ListeTraitementPapier";

    //*************************************************************
    // ListeTraitementPapier property
    //*************************************************************

    ///////////////////
    // ListeTraitementPapier  property
    //////////////////
    /**
     * numéro de la liste.
     */
    public static final String LISTE_TRAITEMENT_PAPIER_NUMERO_LISTE_PROPERTY = "numeroListe";

    /**
     * liste des dossiers de la liste.
     */
    public static final String LISTE_TRAITEMENT_PAPIER_IDS_DOSSIER_PROPERTY = "idsDossier";

    /**
     * type de liste : "mise en signature", "demandes d’épreuves" ou "demandes de publication".
     */
    public static final String LISTE_TRAITEMENT_PAPIER_TYPE_LISTE_PROPERTY = "typeListe";

    /**
     * type de signature : "SGG", "PR" ou "PM" pour la liste de de mise en signature
     */
    public static final String LISTE_TRAITEMENT_PAPIER_TYPE_SIGNATURE = "typeSignature";

    /**
     * date de génération de la liste.
     */
    public static final String LISTE_TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_PROPERTY = "dateGenerationListe";

    public static final String LISTE_TRAITEMENT_PAPIER_PATH = "/case-management/liste-traitement-papier";

    public static final String LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE = "1";

    public static final String LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE = "2";

    public static final String LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION = "3";

    public static final String LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PR = "PR";

    public static final String LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PM = "PM";

    public static final String LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_SGG = "SGG";

    public static final String LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE_LABEL = "E";

    public static final String LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION_LABEL = "P";

    /*
     * Rapport Birt
     */
    public static final String BIRT_REPORT_LISTE_TRAITEMENT_PAPIER_SIGNATURE = "impressionGestionDeListeSignature";

    public static final String BIRT_REPORT_LISTE_TRAITEMENT_PAPIER_EPREUVE = "impressionGestionDeListeEpreuve";

    public static final String BIRT_REPORT_LISTE_TRAITEMENT_PAPIER_PUBLICATION = "impressionGestionDeListePublication";

    // Constant utility class
    private SolonEpgListeTraitementPapierConstants() {}
}
