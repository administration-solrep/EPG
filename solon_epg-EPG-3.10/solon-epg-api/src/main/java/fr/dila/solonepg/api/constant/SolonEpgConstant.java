package fr.dila.solonepg.api.constant;

/**
 * Constantes de l'application Réponses.
 * 
 * @author jtremeaux
 */
public class SolonEpgConstant {

	// *************************************************************
	// Listes de selection
	// *************************************************************
	/**
	 * Liste de selection des corbeilles
	 */
	public static final String	CORBEILLE_SELECTION											= "CORBEILLE_SELECTION";

	/**
	 * Liste de selection des alertes, dans l'espace de suivi.
	 */
	public static final String	ESPACE_SUIVI_ALERT_SELECTION								= "ESPACE_SUIVI_ALERT_SELECTION";

	/**
	 * Liste de selection des dossiers signales
	 */
	public static final String	DOSSIERS_SIGNALES_SELECTION									= "DOSSIERS_SIGNALES_SELECTION";

	// *************************************************************
	// Espace collaboratif
	// *************************************************************
	/**
	 * Type de document de l'espace collaboratif.
	 */
	public static final String	SOLON_EPG_USER_WORKSPACE_ROOT_DOCUMENT_TYPE					= "UserWorkspacesRoot";

	/**
	 * Type de document de l'espace collaboratif d'un utilisateur.
	 */
	public static final String	SOLON_EPG_USER_WORKSPACE_DOCUMENT_TYPE						= "UserWorkspace";

	/**
	 * Type de document répertoire des dossiers signalés d'un utilisateur.
	 */
	public static final String	SOLON_EPG_USER_DOSSIERS_SIGNALES_FOLDER_DOCUMENT_TYPE		= "DossiersSignalesRoot";
	// *************************************************************
	// Recherche
	// *************************************************************
	// TODO utiliser fichier de conf pour permettre e changer dynamiqement ce paramètre
	/**
	 * Type de fichier autorisé pour le téléchargement dans le fond de dossier.
	 */
	public static final String	ALLOWED_UPLOAD_FILE_TYPE									= "jpg, png, odt, pdf, rtf, ods, odp, vsd, doc, xls, ppt, docx, xlsx, pptx, zip";

	// *************************************************************
	// Journal
	// *************************************************************
	/**
	 * Filtre sur les docType
	 */
	public static final String	FILTER_DOCTYPE												= "docType";
	/**
	 * Filtre sur les actions
	 */
	public static final String	FILTER_ACTIONS												= "eventId";
	/**
	 * Filtre sur le nom des utilisateurs
	 */
	public static final String	FILTER_USER													= "principalName";

	// *************************************************************
	// Reponses Mailbox
	// *************************************************************
	/**
	 * Type de document Mailbox SOLON EPG.
	 */
	public static final String	SOLON_EPG_MAILBOX_TYPE										= "SolonEpgMailbox";

	/**
	 * Schéma Mailbox SOLON EPG.
	 */
	public static final String	SOLON_EPG_MAILBOX_SCHEMA									= "mailbox_solon_epg";

	/**
	 * Propriété du schéma SolonEpgMailbox : nombre de dossiers pour rédaction.
	 */
	public static final String	MAILBOX_POUR_REDACTION										= "pourRedaction";

	/**
	 * Propriété du schéma SolonEpgMailbox : nombre de dossiers pour attribution.
	 */
	public static final String	MAILBOX_POUR_ATTRIBUTION									= "pourAttribution";

	/**
	 * Propriété du schéma SolonEpgMailbox : nombre de dossiers pour signature.
	 */
	public static final String	MAILBOX_POUR_SIGNATURE										= "pourSignature";

	/**
	 * Propriété du schéma SolonEpgMailbox : nombre de dossiers pour information.
	 */
	public static final String	MAILBOX_POUR_INFORMATION									= "pourInformation";

	/**
	 * Propriété du schéma SolonEpgMailbox : nombre de dossiers pour avis.
	 */
	public static final String	MAILBOX_POUR_AVIS											= "pourAvis";

	/**
	 * Propriété du schéma SolonEpgMailbox : nombre de dossiers pour validation PM.
	 */
	public static final String	MAILBOX_POUR_VALIDATION_PM									= "pourValidationPM";

	/**
	 * Propriété du schéma SolonEpgMailbox : nombre de dossiers pour réattribution.
	 */
	public static final String	MAILBOX_POUR_REATTRIBUTION									= "pourReattribution";

	/**
	 * Propriété du schéma SolonEpgMailbox : nombre de dossiers pour réorientation.
	 */
	public static final String	MAILBOX_POUR_REORIENTATION									= "pourReorientation";

	/**
	 * Propriété du schéma SolonEpgMailbox : nombre de dossiers pour impression.
	 */
	public static final String	MAILBOX_POUR_IMPRESSION										= "pourImpression";

	/**
	 * Propriété du schéma SolonEpgMailbox : nombre de dossiers pour transmission aux assemblées.
	 */
	public static final String	MAILBOX_POUR_TRANSMISSION_ASSEMBLEES						= "pourTransmissionAuxAssemblees";

	/**
	 * Propriété du schéma SolonEpgMailbox : nombre de dossiers pour arbitrage.
	 */
	public static final String	MAILBOX_POUR_ARBITRAGE										= "pourArbitrage";

	/**
	 * Propriété du schéma SolonEpgMailbox : nombre de dossiers pour rédaction interfacée.
	 */
	public static final String	MAILBOX_POUR_REDACTION_INTERFACEE							= "pourRedactionInterfacee";

	// *************************************************************
	// Favoris de recherche
	// *************************************************************
	public static final String	FAVORIS_RECHERCHE_DOCUMENT_TYPE								= "FavorisRecherche";

	// *************************************************************
	// Rapport BIRT
	// *************************************************************
	public static final String	BIRT_REPORT_ROOT											= "birtReports/";
	public static final String	BIRT_REPORT_TRAITEMENT_PAPIER								= "traitementpapier/";
	public static final String	BIRT_REPORT_ADMIN											= "admin/";

	public static final String	BIRT_REPORT_LISTE_DE_GESTION_MISE_EN_SIGNATURE				= BIRT_REPORT_ROOT
																									+ BIRT_REPORT_TRAITEMENT_PAPIER
																									+ "gestionDeListeSignature.rptdesign";
	public static final String	BIRT_REPORT_LISTE_DE_GESTION_DEMANDE_PUBLICATION			= BIRT_REPORT_ROOT
																									+ BIRT_REPORT_TRAITEMENT_PAPIER
																									+ "gestionDeListePublication.rptdesign";
	public static final String	BIRT_REPORT_LISTE_DE_GESTION_DEMANDE_EPREUVE				= BIRT_REPORT_ROOT
																									+ BIRT_REPORT_TRAITEMENT_PAPIER
																									+ "gestionDeListeEpreuve.rptdesign";
	public static final String	BIRT_REPORT_BORDEREAU_ENVOI_EPREUVE_EN_RELECTURE			= BIRT_REPORT_ROOT
																									+ BIRT_REPORT_TRAITEMENT_PAPIER
																									+ "bordereauEnvoiEpreuveRelecture.rptdesign";
	public static final String	BIRT_REPORT_BORDEREAU_CHEMIN_DE_CROIX						= BIRT_REPORT_ROOT
																									+ BIRT_REPORT_TRAITEMENT_PAPIER
																									+ "cheminDeCroixDecret.rptdesign";
	public static final String	BIRT_REPORT_BORDEREAU_ENVOI_DE_RETOUR						= BIRT_REPORT_ROOT
																									+ BIRT_REPORT_TRAITEMENT_PAPIER
																									+ "bordereauRetour.rptdesign";
	public static final String	BIRT_REPORT_BORDEREAU_DE_COMMUNICATION						= BIRT_REPORT_ROOT
																									+ BIRT_REPORT_TRAITEMENT_PAPIER
																									+ "bordereauDeCommunication.rptdesign";
	public static final String	BIRT_REPORT_BORDEREAU_DE_COMMUNICATION_CABINET_SG			= BIRT_REPORT_ROOT
																									+ BIRT_REPORT_TRAITEMENT_PAPIER
																									+ "bordereauDeCommunicationArreteDeNomination.rptdesign";
	public static final String	BIRT_REPORT_BORDEREAU_DE_COMMUNICATION_SOUMIS_A_VALIDATION	= BIRT_REPORT_ROOT
																									+ BIRT_REPORT_TRAITEMENT_PAPIER
																									+ "bordereauDeCommunicationSoumisValidation.rptdesign";
	public static final String	BIRT_REPORT_BORDEREAU_DE_COMMUNICATION_RELANCE				= BIRT_REPORT_ROOT
																									+ BIRT_REPORT_TRAITEMENT_PAPIER
																									+ "bordereauDeCommunicationRelance.rptdesign";
	public static final String	BIRT_REPORT_BORDEREAU_ARCHIVAGE								= BIRT_REPORT_ROOT
																									+ BIRT_REPORT_ADMIN
																									+ "elimination.rptdesign";

	// Migration
	public static final String	EN_COURS_STATUS												= "EN_COURS";
	public static final String	TERMINEE_STATUS												= "TERMINEE";
	public static final String	FAILED_STATUS												= "FAILED";

	// *************************************************************
	// Parametrage Elastic Search
	// *************************************************************
	/**
	 * Type de document paramètre.
	 */
	public static final String	PARAMETRE_ES_DOCUMENT_TYPE									= "ParametreES";

	/**
	 * Type de document racine des modèles de paramètre.
	 */
	public static final String	PARAMETRE_ES_FOLDER_DOCUMENT_TYPE							= "ParametreESFolder";

	/**
	 * Valeur du droit ajouté pour permettre la recherche par les admins fonctionnels
	 */
	public static final String	ES_DROITS_ADMINISTRATORS									= "administrators";

	// ***************************************************************
	// Feuille de route
	// ***************************************************************
	/**
	 * Type de document racine des squelettes de feuille.
	 */
	public static final String	FEUILLE_ROUTE_SQUELETTE_FOLDER_DOCUMENT_TYPE	= "FeuilleRouteSqueletteFolder";
	
	/**
	 * Type de document squelette de feuille de route.
	 */
	public static final String	FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE					= "FeuilleRouteSquelette";
	
	/**
	 * Type de document étape de squelette de feuille de route.
	 */
	public static final String	ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE					= "RouteStepSquelette";


	private SolonEpgConstant() {
		// Private default constructor
	}
}
