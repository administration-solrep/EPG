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
	public static final String	FORMAT_REFERENCE_LOI								= "format-reference-loi";

	/**
	 * Format des références pour les ordonnances - expression régulière.
	 */
	public static final String	FORMAT_REFERENCE_ORDONNANCE							= "format-reference-ordonnance";

	/**
	 * Format du numéro d'ordre - expression régulière.
	 */
	public static final String	FORMAT_NUMERO_ORDRE									= "format-numero-ordre";

	/**
	 * Objet du mail envoyé à l'administrateur technique lors d'une erreur java outOfMemory heapspace avec webservice
	 */
	public static final String	NOTIFICATION_ERREUR_HEAP_SPACE_OBJET				= "objet-mel-notification-erreur-heap-space";

	/**
	 * Texte du mail envoyé à l'administrateur technique lors d'une erreur java outOfMemory heapspace avec webservice
	 */
	public static final String	NOTIFICATION_ERREUR_HEAP_SPACE_TEXTE				= "texte-mel-notification-erreur-heap-space";

	/**
	 * Objet du mail envoyé à l'administrateur technique en cas d'erreur lors de la validation technique d'un dossier
	 */
	public static final String	NOTIFICATION_ERREUR_VALIDATION_AUTOMATIQUE_OBJET	= "objet-mel-notification-erreur-validation-automatique";

	/**
	 * Texte du mail envoyé à l'administrateur technique en cas d'erreur lors de la validation technique d'un dossier
	 */
	public static final String	NOTIFICATION_ERREUR_VALIDATION_AUTOMATIQUE_TEXTE	= "texte-mel-notification-erreur-validation-automatique";

	/**
	 * Texte du mail envoyé dans le cas de dossiers dont les nor ne correspondent pas aux lettres de leur ministère ou
	 * direction responsable
	 */
	public static final String	TEXTE_MAIL_NOR_DOSSIERS_INCOHERENT					= "texte-mel-nor-dossier-incoherent";

	/**
	 * Objet du mail envoyé dans le cas de dossiers dont les nor ne correspondent pas aux lettres de leur ministère ou
	 * direction responsable
	 */
	public static final String	OBJET_MAIL_NOR_DOSSIERS_INCOHERENT					= "objet-mel-nor-dossier-incoherent";

	/**
	 * Texte du mail envoyé dans le cas d'échec de demande de publication
	 */
	public static final String	TEXTE_MAIL_ECHEC_DEMANDE_PUBLI						= "texte-mel-echec-demande-publi";

	/**
	 * Objet du mail envoyé dans le cas d'échec de demande de publication
	 */
	public static final String	OBJET_MAIL_ECHEC_DEMANDE_PUBLI						= "objet-mel-echec-demande-publi";

	/**
	 * Texte du mail envoyé dans le cas d'échec du rejeu de la demande de publication
	 */
	public static final String	TEXTE_MAIL_ECHEC_REJEU_NOTIF						= "texte-mel-echec-rejeu-notif";

	/**
	 * Objet du mail envoyé dans le cas d'échec du rejeu de la demande de publication
	 */
	public static final String	OBJET_MAIL_ECHEC_REJEU_NOTIF						= "objet-mel-echec-rejeu-notif";

	/**
	 * Objet du mail envoyé dans le cas d'abandon de la demande de publication
	 */
	public static final String	OBJET_MAIL_ABANDON_NOTIF							= "objet-mel-abandon-notif";

	/**
	 * Texte du mail envoyé dans le cas d'abandon de la demande de publication
	 */
	public static final String	TEXTE_MAIL_ABANDON_NOTIF							= "texte-mel-abandon-notif";

	/**
	 * Objet du mail envoyé pour envoi demande de publication suivante
	 */
	public static final String	OBJET_MAIL_DEMANDE_PUBLI_SUIVANTE					= "objet-mel-demande-publi-suivante";

	/**
	 * Texte du mail envoyé pour envoi demande de publication suivante
	 */
	public static final String	TEXTE_MAIL_DEMANDE_PUBLI_SUIVANTE					= "texte-mel-demande-publi-suivante";

	/**
	 * Texte du mail envoyé pour les résultats d'une alerte
	 */
	public static final String	TEXTE_MAIL_ALERT									= "texte-mel-alert";

	/**
	 * Objet du mail envoyé pour les résultats d'une alerte
	 */
	public static final String	OBJET_MAIL_ALERT									= "objet-mel-alert";

	/**
	 * Texte du mail envoyé pour les résultats d'une alerte
	 */
	public static final String	TEXTE_MAIL_DILA_PARAMETERS							= "texte-mel-dila-parameters";

	/**
	 * Objet du mail envoyé pour les résultats d'une alerte
	 */
	public static final String	OBJET_MAIL_DILA_PARAMETERS							= "objet-mel-dila-parameters";

	/**
	 * Objet du mail envoyé pour confirmation du maintien d'alerte
	 */
	public static final String	OBJET_MAIL_CONFIRM_ALERT							= "objet-mel-confirm-alert";

	/**
	 * Texte du mail envoyé pour confirmation du maintien d'alerte
	 */
	public static final String	TEXTE_MAIL_CONFIRM_ALERT							= "texte-mel-confirm-alert";

	/**
	 * Lien vers la page de suivi de l'application des lois
	 */
	public static final String	PAGE_SUIVI_APPLICATION_LOIS							= "page-suivi-application-lois";

	/**
	 * Délai après signature pour la cloture d'un dossier acte texte non publié
	 */
	public static final String	DELAI_CLOTURE_TXT_NON_PUB							= "delai-cloture-txt-non-pub";
	
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
	public static final String ACTIVATION_PUBLICATION_BILAN_SEMESTRIEL_BDJ = "flag-activation-publication-bilansemestriel-bdj";

	/**
	 * URL du webservice de publication de l'échéancier d'un texte maitre a la
	 * BDJ
	 */
	public static final String URL_WEBSERVICE_PUBLICATION_BILAN_SEMESTRIEL_BDJ = "url-webservice-publication-bilansemestriel-bdj";
	
	/**
	 * Activation de la fonctionnalité d'envoi des informations de publication au CE par le webservice chercherModificationDossier
	 * 
	 */
	public static final String ACTIVATION_ENVOI_INFO_PUBLICATION_CE = "flag-activation-envoi-info-publication-CE";
	
	private SolonEpgParametreConstant() {
		// Private default constructor
	}
}