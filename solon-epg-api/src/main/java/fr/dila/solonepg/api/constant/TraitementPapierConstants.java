package fr.dila.solonepg.api.constant;

public class TraitementPapierConstants {
    public static final String TRAITEMENT_PAPIER_SCHEMA = "traitement_papier";

    public static final String TRAITEMENT_PAPIER_SCHEMA_SCHEMA_PREFIX = "tp";

    // *************************************************************
    // Dossier traitement papier property
    // *************************************************************

    // *************************************************************
    // Dossier traitement papier : onglet référence property
    // *************************************************************
    public static final String TRAITEMENT_PAPIER_DATE_ARRIVE_PAPIER_PROPERTY = "dateArrivePapier";

    public static final String TRAITEMENT_PAPIER_COMMENTAIRE_REFERENCE_PROPERTY = "commentaireReference";

    public static final String TRAITEMENT_PAPIER_TEXTE_A_PUBLIER_PROPERTY = "texteAPublier";

    public static final String TRAITEMENT_PAPIER_TEXTE_SOUMIS_A_VALIDATION = "texteSoumisAValidation";

    public static final String TRAITEMENT_PAPIER_SIGNATAIRE_PROPERTY = "signataire";

    public static final String TRAITEMENT_PAPIER_COMMENTAIRE_REFERENCE_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_COMMENTAIRE_REFERENCE_PROPERTY;

    public static final String TRAITEMENT_PAPIER_DATE_ARRIVE_PAPIER_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_DATE_ARRIVE_PAPIER_PROPERTY;

    public static final String TRAITEMENT_PAPIER_TEXTE_A_PUBLIER_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_TEXTE_A_PUBLIER_PROPERTY;

    public static final String TRAITEMENT_PAPIER_TEXTE_SOUMIS_A_VALIDATION_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_TEXTE_SOUMIS_A_VALIDATION;

    public static final String TRAITEMENT_PAPIER_SIGNATAIRE_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_SIGNATAIRE_PROPERTY;

    // *************************************************************
    // Dossier traitement papier : onglet communication property
    // *************************************************************

    public static final String TRAITEMENT_PAPIER_PRIORITE_PROPERTY = "priorite";

    public static final String TRAITEMENT_PAPIER_PRIORITE_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_PRIORITE_PROPERTY;

    public static final String TRAITEMENT_PAPIER_PERSONNES_NOMMEE_PROPERTY = "personnesNommees";

    public static final String TRAITEMENT_PAPIER_PERSONNES_NOMMEE_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_PERSONNES_NOMMEE_PROPERTY;

    public static final String TRAITEMENT_PAPIER_CABINET_PM_COMMUNICATION_PROPERTY = "cabinetPmCommunication";

    public static final String TRAITEMENT_PAPIER_CABINET_PM_COMMUNICATION_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_CABINET_PM_COMMUNICATION_PROPERTY;

    public static final String TRAITEMENT_PAPIER_CHARGE_MISSION_COMMUNICATION_PROPERTY = "chargeMissionCommunication";

    public static final String TRAITEMENT_PAPIER_CHARGE_MISSION_COMMUNICATION_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_CHARGE_MISSION_COMMUNICATION_PROPERTY;

    public static final String TRAITEMENT_PAPIER_AUTRES_DESTINATAIRES_COMMUNICATION_PROPERTY =
        "autresDestinatairesCommunication";

    public static final String TRAITEMENT_PAPIER_AUTRES_DESTINATAIRES_COMMUNICATION_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_AUTRES_DESTINATAIRES_COMMUNICATION_PROPERTY;

    public static final String TRAITEMENT_PAPIER_CABINET_SG_COMMUNICATION_PROPERTY = "cabinetSgCommunication";

    public static final String TRAITEMENT_PAPIER_CABINET_SG_COMMUNICATION_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_CABINET_SG_COMMUNICATION_PROPERTY;

    public static final String TRAITEMENT_PAPIER_DESTINATAIRES_COMMUNICATION_PROPERTY = "destinatairesCommunication";

    public static final String TRAITEMENT_PAPIER_DESTINATAIRE_COMMUNICATION_PROPERTY = "destinataireCommunication";

    public static final String TRAITEMENT_PAPIER_NOM_DESTINATAIRE_COMMUNICATION_PROPERTY =
        "nomDestinataireCommunication";

    public static final String TRAITEMENT_PAPIER_DATE_ENVOI_COMMUNICATION_PROPERTY = "dateEnvoiCommunication";

    public static final String TRAITEMENT_PAPIER_DATE_RETOUR_COMMUNICATION_PROPERTY = "dateRetourCommunication";

    public static final String TRAITEMENT_PAPIER_DATE_RELANCE_COMMUNICATION_PROPERTY = "dateRelanceCommunication";

    public static final String TRAITEMENT_PAPIER_SENS_AVIS_COMMUNICATION_PROPERTY = "sensAvisCommunication";

    public static final String TRAITEMENT_PAPIER_OBJET_COMMUNICATION_PROPERTY = "objetCommunication";

    public static final String TRAITEMENT_PAPIER_NOMS_SIGNATAIRES_COMMUNICATION_PROPERTY =
        "nomsSignatairesCommunication";

    public static final String TRAITEMENT_PAPIER_ID_COMMUNICATION_PROPERTY = "idCommunication";

    public static final String TRAITEMENT_PAPIER_NOMS_SIGNATAIRES_COMMUNICATION_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_NOMS_SIGNATAIRES_COMMUNICATION_PROPERTY;

    // *************************************************************
    // Dossier traitement papier : onglet signature property
    // *************************************************************

    public static final String TRAITEMENT_PAPIER_SIGNATURE_PM_PROPERTY = "signaturePm";

    public static final String TRAITEMENT_PAPIER_SIGNATURE_PM_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_SIGNATURE_PM_PROPERTY;

    public static final String TRAITEMENT_PAPIER_SIGNATURE_SGG_PROPERTY = "signatureSgg";

    public static final String TRAITEMENT_PAPIER_SIGNATURE_SGG_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_SIGNATURE_SGG_PROPERTY;

    public static final String TRAITEMENT_PAPIER_SIGNATURE_PR_PROPERTY = "signaturePr";

    public static final String TRAITEMENT_PAPIER_SIGNATURE_PR_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_SIGNATURE_PR_PROPERTY;

    public static final String TRAITEMENT_PAPIER_DONNEES_SIGNATAIRE_PROPERTY = "donneesSignataire";

    public static final String TRAITEMENT_PAPIER_DATE_ENVOI_SIGNATURE_PROPERTY = "dateEnvoiSignature";

    public static final String TRAITEMENT_PAPIER_DATE_RETOUR_SIGNATURE_PROPERTY = "dateRetourSignature";

    public static final String TRAITEMENT_PAPIER_COMMENTAIRE_SIGNATURE_PROPERTY = "commentaireSignature";

    public static final String TRAITEMENT_PAPIER_CHEMIN_CROIX_PROPERTY = "cheminCroix";

    public static final String TRAITEMENT_PAPIER_CHEMIN_CROIX_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_CHEMIN_CROIX_PROPERTY;

    public static final String TRAITEMENT_PAPIER_SIGNATURE_DESTINATAIRE_SGG_PROPERTY = "signatureDestinataireSGG";

    public static final String TRAITEMENT_PAPIER_SIGNATURE_DESTINATAIRE_SGG_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_SIGNATURE_DESTINATAIRE_SGG_PROPERTY;

    public static final String TRAITEMENT_PAPIER_SIGNATURE_DESTINATAIRE_CPM_PROPERTY = "signatureDestinataireCPM";

    public static final String TRAITEMENT_PAPIER_SIGNATURE_DESTINATAIRE_CPM_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_SIGNATURE_DESTINATAIRE_CPM_PROPERTY;

    public static final String TRAITEMENT_PAPIER_NUMEROS_LISTE_SIGNATURE_FIELD_PROPERTY = "numerosListeSignatureField";

    public static final String TRAITEMENT_PAPIER_NUMEROS_LISTE_SIGNATURE_PROPERTY = "numerosListeSignature";

    public static final String TRAITEMENT_PAPIER_INFO_NUMERO_LISTE_SIGNATURE_PROPERTY = "infoNumeroListeSignature";

    public static final String TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_SIGNATURE_PROPERTY =
        "dateGenerationListeSignature";

    public static final String TRAITEMENT_PAPIER_NUMERO_LISTE_SIGNATURE_PROPERTY = "numeroListeSignature";

    // *************************************************************
    // Dossier traitement papier : onglet retour
    // *************************************************************

    public static final String TRAITEMENT_PAPIER_RETOUR_A_PROPERTY = "retourA";

    public static final String TRAITEMENT_PAPIER_RETOUR_A_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_RETOUR_A_PROPERTY;

    public static final String TRAITEMENT_PAPIER_DATE_RETOUR_PROPERTY = "dateRetour";

    public static final String TRAITEMENT_PAPIER_DATE_RETOUR_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_DATE_RETOUR_PROPERTY;

    public static final String TRAITEMENT_PAPIER_MOTIF_RETOUR_PROPERTY = "motifRetour";

    public static final String TRAITEMENT_PAPIER_MOTIF_RETOUR_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_MOTIF_RETOUR_PROPERTY;

    public static final String TRAITEMENT_PAPIER_NOMS_SIGNATAIRES_RETOUR_PROPERTY = "nomsSignatairesRetour";

    public static final String TRAITEMENT_PAPIER_NOMS_SIGNATAIRES_RETOUR_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_NOMS_SIGNATAIRES_RETOUR_PROPERTY;

    // *************************************************************
    // Dossier traitement papier : onglet epreuve
    // *************************************************************

    public static final String TRAITEMENT_PAPIER_EPREUVE_PROPERTY = "epreuve";

    public static final String TRAITEMENT_PAPIER_EPREUVE_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_EPREUVE_PROPERTY;

    public static final String TRAITEMENT_PAPIER_NOUVELLE_DEMANDE_EPREUVES_PROPERTY = "nouvelleDemandeEpreuves";

    public static final String TRAITEMENT_PAPIER_NOUVELLE_DEMANDE_EPREUVES_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_NOUVELLE_DEMANDE_EPREUVES_PROPERTY;

    public static final String TRAITEMENT_PAPIER_INFO_EPREUVE_PROPERTY = "infoEpreuve";

    public static final String TRAITEMENT_PAPIER_EPREUVE_DEMANDE_LE_PROPERTY = "epreuveDemandeLe";

    public static final String TRAITEMENT_PAPIER_EPREUVE_POUR_LE_PROPERTY = "epreuvePourLe";

    public static final String TRAITEMENT_PAPIER_EPREUVE_NUMERO_LISTE_PROPERTY = "epreuveNumeroListe";

    public static final String TRAITEMENT_PAPIER_EPREUVE_ENVOI_RELECTURE_LE_PROPERTY = "epreuveEnvoiRelectureLe";

    public static final String TRAITEMENT_PAPIER_EPREUVE_DESTINATAIRE_ENTETE_PROPERTY = "epreuveDestinataireEntete";

    public static final String TRAITEMENT_PAPIER_EPREUVE_NOMS_SIGNATAIRES_PROPERTY = "epreuveNomsSignataires";

    public static final String TRAITEMENT_PAPIER_RETOUR_DU_BON_A_TITRE_LE_PROPERTY = "retourDuBonaTitrerLe";

    public static final String TRAITEMENT_PAPIER_RETOUR_DU_BON_A_TITRE_LE_XPATH =
        TRAITEMENT_PAPIER_SCHEMA + ":" + TRAITEMENT_PAPIER_RETOUR_DU_BON_A_TITRE_LE_PROPERTY;

    // *************************************************************
    // Dossier traitement papier : onglet publication
    // *************************************************************

    public static final String TRAITEMENT_PAPIER_PUBLICATION_DATE_ENVOI_JO_PROPERTY = "publicationDateEnvoiJo";

    public static final String TRAITEMENT_PAPIER_PUBLICATION_NOM_PUBLICATION_PROPERTY = "publicationNomPublication";

    public static final String TRAITEMENT_PAPIER_PUBLICATION_MODE_PUBLICATION_PROPERTY = "publicationModePublication";

    public static final String TRAITEMENT_PAPIER_PUBLICATION_EPREUVE_EN_RETOUR_PROPERTY = "publicationEpreuveEnRetour";

    public static final String TRAITEMENT_PAPIER_PUBLICATION_DELAI_PROPERTY = "publicationDelai";

    public static final String TRAITEMENT_PAPIER_PUBLICATION_DATE_DEMANDE_PROPERTY = "publicationDateDemande";

    public static final String TRAITEMENT_PAPIER_PUBLICATION_NUMERO_LISTE_PROPERTY = "publicationNumeroListe";

    public static final String TRAITEMENT_PAPIER_PUBLICATION_DATE_PROPERTY = "publicationDate";

    // *************************************************************
    // Dossier traitement papier : onglet ampliation
    // *************************************************************

    public static final String TRAITEMENT_PAPIER_AMPLIATION_DESTINATAIRE_MAILS_PROPERTY = "ampliationDestinataireMails";

    public static final String TRAITEMENT_PAPIER_AMPLIATION_HISTORIQUE_PROPERTY = "ampliationHistorique";

    public static final String TRAITEMENT_PAPIER_INFOS_HISTORIQUE_AMPLIATION_PROPERTY = "infosHistoriqueAmpliation";

    public static final String TRAITEMENT_PAPIER_INFO_HISTORIQUE_AMPLIATION_PROPERTY = "infoHistoriqueAmpliation";

    public static final String TRAITEMENT_PAPIER_DATE_ENVOI_AMPLIATION_PROPERTY = "dateEnvoiAmpliation";

    public static final String TRAITEMENT_PAPIER_AMPLIATION_PAPIER_ARCHIVE_PROPERTY = "papierArchive";

    private TraitementPapierConstants() {
        // Private default constructor
    }
}
