package fr.dila.solonepg.api.constant;

/**
 * Constante des documents paramètres spécifique à l'applicaiton Solon Epg
 *
 * @author arolin
 */
public class SolonEpgParametreConstant {
    /**
     * Format des références pour les lois - expression régulière.
     */
    public static final String FORMAT_REFERENCE_LOI = "format-reference-loi";

    /**
     * Format des références pour les ordonnances - expression régulière.
     */
    public static final String FORMAT_REFERENCE_ORDONNANCE = "format-reference-ordonnance";

    /**
     * Format du numéro d'ordre - expression régulière.
     */
    public static final String FORMAT_NUMERO_ORDRE = "format-numero-ordre";

    /**
     * Objet du mail envoyé à l'administrateur technique lors d'une erreur java outOfMemory heapspace avec webservice
     */
    public static final String NOTIFICATION_ERREUR_HEAP_SPACE_OBJET = "objet-mel-notification-erreur-heap-space";

    /**
     * Texte du mail envoyé à l'administrateur technique lors d'une erreur java outOfMemory heapspace avec webservice
     */
    public static final String NOTIFICATION_ERREUR_HEAP_SPACE_TEXTE = "texte-mel-notification-erreur-heap-space";

    /**
     * Objet du mail envoyé à l'administrateur technique en cas d'erreur lors de la validation technique d'un dossier
     */
    public static final String NOTIFICATION_ERREUR_VALIDATION_AUTOMATIQUE_OBJET =
        "objet-mel-notification-erreur-validation-automatique";

    /**
     * Texte du mail envoyé à l'administrateur technique en cas d'erreur lors de la validation technique d'un dossier
     */
    public static final String NOTIFICATION_ERREUR_VALIDATION_AUTOMATIQUE_TEXTE =
        "texte-mel-notification-erreur-validation-automatique";

    /**
     * Texte du mail envoyé dans le cas de dossiers dont les nor ne correspondent pas aux lettres de leur ministère ou
     * direction responsable
     */
    public static final String TEXTE_MAIL_NOR_DOSSIERS_INCOHERENT = "texte-mel-nor-dossier-incoherent";

    /**
     * Objet du mail envoyé dans le cas de dossiers dont les nor ne correspondent pas aux lettres de leur ministère ou
     * direction responsable
     */
    public static final String OBJET_MAIL_NOR_DOSSIERS_INCOHERENT = "objet-mel-nor-dossier-incoherent";

    /**
     * Texte du mail envoyé dans le cas d'échec de demande de publication
     */
    public static final String TEXTE_MAIL_ECHEC_DEMANDE_PUBLI = "texte-mel-echec-demande-publi";

    /**
     * Objet du mail envoyé dans le cas d'échec de demande de publication
     */
    public static final String OBJET_MAIL_ECHEC_DEMANDE_PUBLI = "objet-mel-echec-demande-publi";

    /**
     * Texte du mail envoyé dans le cas d'échec du rejeu de la demande de publication
     */
    public static final String TEXTE_MAIL_ECHEC_REJEU_NOTIF = "texte-mel-echec-rejeu-notif";

    /**
     * Objet du mail envoyé dans le cas d'échec du rejeu de la demande de publication
     */
    public static final String OBJET_MAIL_ECHEC_REJEU_NOTIF = "objet-mel-echec-rejeu-notif";

    /**
     * Objet du mail envoyé dans le cas d'abandon de la demande de publication
     */
    public static final String OBJET_MAIL_ABANDON_NOTIF = "objet-mel-abandon-notif";

    /**
     * Texte du mail envoyé dans le cas d'abandon de la demande de publication
     */
    public static final String TEXTE_MAIL_ABANDON_NOTIF = "texte-mel-abandon-notif";

    /**
     * Objet du mail envoyé pour envoi demande de publication suivante
     */
    public static final String OBJET_MAIL_DEMANDE_PUBLI_SUIVANTE = "objet-mel-demande-publi-suivante";

    /**
     * Texte du mail envoyé pour envoi demande de publication suivante
     */
    public static final String TEXTE_MAIL_DEMANDE_PUBLI_SUIVANTE = "texte-mel-demande-publi-suivante";

    /**
     * Texte du mail envoyé pour les résultats d'une alerte
     */
    public static final String TEXTE_MAIL_ALERT = "texte-mel-alert";

    /**
     * Objet du mail envoyé pour les résultats d'une alerte
     */
    public static final String OBJET_MAIL_ALERT = "objet-mel-alert";

    /**
     * Texte du mail envoyé pour les résultats d'une alerte
     */
    public static final String TEXTE_MAIL_DILA_PARAMETERS = "texte-mel-dila-parameters";

    /**
     * Objet du mail envoyé pour les résultats d'une alerte
     */
    public static final String OBJET_MAIL_DILA_PARAMETERS = "objet-mel-dila-parameters";

    /**
     * Objet du mail envoyé pour confirmation du maintien d'alerte
     */
    public static final String OBJET_MAIL_CONFIRM_ALERT = "objet-mel-confirm-alert";

    /**
     * Texte du mail envoyé pour confirmation du maintien d'alerte
     */
    public static final String TEXTE_MAIL_CONFIRM_ALERT = "texte-mel-confirm-alert";

    /**
     * Lien vers la page de suivi de l'application des lois
     */
    public static final String PAGE_SUIVI_APPLICATION_LOIS = "page-suivi-application-lois";

    /**
     * Délai après signature pour la cloture d'un dossier acte texte non publié
     */
    public static final String DELAI_CLOTURE_TXT_NON_PUB = "delai-cloture-txt-non-pub";

    /**
     * Activation de la fonctionnalité de publication de l'échéancier d'un texte
     * maitre a la BDJ
     */
    public static final String ACTIVATION_PUBLICATION_ECHEANCIER_BDJ = "flag-activation-publication-echeancier-bdj";

    /**
     * URL du webservice de publication de l'échéancier d'un texte maitre a la
     * BDJ
     */
    public static final String URL_WEBSERVICE_PUBLICATION_ECHEANCIER_BDJ = "url-webservice-publication-echeancier-bdj";

    /**
     * Activation de la fonctionnalité de publication de l'échéancier d'un texte
     * maitre a la BDJ
     */
    public static final String ACTIVATION_PUBLICATION_BILAN_SEMESTRIEL_BDJ =
        "flag-activation-publication-bilansemestriel-bdj";

    /**
     * URL du webservice de publication de l'échéancier d'un texte maitre a la
     * BDJ
     */
    public static final String URL_WEBSERVICE_PUBLICATION_BILAN_SEMESTRIEL_BDJ =
        "url-webservice-publication-bilansemestriel-bdj";

    /**
     * Activation de la fonctionnalité d'envoi des informations de publication au CE par le webservice chercherModificationDossier
     *
     */
    public static final String ACTIVATION_ENVOI_INFO_PUBLICATION_CE = "flag-activation-envoi-info-publication-CE";

    /**
     * Batch hebdomadaire de purge des tentatives de connexions
     */
    public static final String BATCH_HEBDO_PURGE_TENTATIVES_CONNEXION_EVENT =
        "batch-hebdo-purgeTentativesConnexionEvent";

    /**
     * Batch mensuel d'abandon des dossiers candidats
     */
    public static final String BATCH_MENSUEL_DOSSIER_CANDIDAT_ABANDON = "batch-mensuel-dossierCandidatAbandon";

    /**
     * Objet mail de notification de nouveau mot de passe
     */
    public static final String OBJET_MEL_NOUVEAU_MDP = "objet-mel-nouveau-mdp";

    /**
     * Année de départ des statistiques sur les mesures de publication
     */
    public static final String ANNEE_DEPART_STATISTIQUE_MESURE_PUBLICATION =
        "annee-depart-statistique-mesure-publication";

    /**
     * Année de départ des statistiques sur les mesures de publication
     */
    public static final String FLAG_AFFICHAGE_PUBLIER_DOSSIER = "flag-affichage-publier-dossier";

    public static final String SOLON_EDIT_VERSION_NAME = "solon-edit-version";
    public static final String SOLON_EDIT_VERSION_TITRE = "SOLON-Edit : numéro de version courant";
    public static final String SOLON_EDIT_VERSION_DESCRIPTION =
        "Numéro de la dernière version de SOLON-Edit disponible depuis l'application. Le format à renseigner doit être exactement de la forme suivante (où n est un chiffre ou nombre) : n.n.n.n";
    public static final String SOLON_EDIT_VERSION_UNIT = "texte";
    public static final String SOLON_EDIT_VERSION_VALUE = "1.1.1.0";

    /**
     * Durée d'utilité administrative
     */
    public static final String DUA = "DUA";
    public static final String DUA_1 = "DUA-1";
    public static final String DUA_2 = "DUA-2";
    public static final String DUA_3 = "DUA-3";
    public static final String DUA_4 = "DUA-4";
    public static final String DUA_5 = "DUA-5";
    public static final String DUA_6 = "DUA-6";
    public static final String DUA_7 = "DUA-7";
    public static final String DUA_8 = "DUA-8";
    public static final String DUA_9 = "DUA-9";
    public static final String DUA_10 = "DUA-10";
    public static final String DUA_11 = "DUA-11";
    public static final String DUA_12 = "DUA-12";
    public static final String DUA_13 = "DUA-13";
    public static final String DUA_14 = "DUA-14";
    public static final String DUA_15 = "DUA-15";
    public static final String DUA_16 = "DUA-16";
    public static final String DUA_17 = "DUA-17";
    public static final String DUA_18 = "DUA-18";
    public static final String DUA_19 = "DUA-19";
    public static final String DUA_20 = "DUA-20";
    public static final String DUA_21 = "DUA-21";
    public static final String DUA_22 = "DUA-22";
    public static final String DUA_23 = "DUA-23";
    public static final String DUA_24 = "DUA-24";
    public static final String DUA_25 = "DUA-25";
    public static final String DUA_26 = "DUA-26";
    public static final String DUA_27 = "DUA-27";
    public static final String DUA_28 = "DUA-28";
    public static final String DUA_29 = "DUA-29";
    public static final String DUA_30 = "DUA-30";
    public static final String DUA_31 = "DUA-31";
    public static final String DUA_32 = "DUA-32";
    public static final String DUA_33 = "DUA-33";
    public static final String DUA_34 = "DUA-34";
    public static final String DUA_36 = "DUA-36";
    public static final String DUA_37 = "DUA-37";
    public static final String DUA_38 = "DUA-38";
    public static final String DUA_39 = "DUA-39";
    public static final String DUA_40 = "DUA-40";
    public static final String DUA_41 = "DUA-41";
    public static final String DUA_42 = "DUA-42";
    public static final String DUA_43 = "DUA-43";
    public static final String DUA_44 = "DUA-44";
    public static final String DUA_45 = "DUA-45";
    public static final String DUA_46 = "DUA-46";
    public static final String DUA_47 = "DUA-47";
    public static final String DUA_48 = "DUA-48";
    public static final String DUA_49 = "DUA-49";
    public static final String DUA_50 = "DUA-50";
    public static final String DUA_51 = "DUA-51";

    public static final String ADRESSE_URL_SLQD_TITRE = "Divers : URL du portail du SLQD";
    public static final String ADRESSE_URL_SLQD_DESC =
        "Page affichée par le lien « Portail de la qualité et de la simplification du droit » de la page d'accueil";
    public static final String ADRESSE_URL_SLQD_UNIT = "url";
    public static final String ADRESSE_URL_SLQD_VALUE = "https://extraqual.pm.ader.gouv.fr/index.html";

    private SolonEpgParametreConstant() {
        // Private default constructor
    }
}
