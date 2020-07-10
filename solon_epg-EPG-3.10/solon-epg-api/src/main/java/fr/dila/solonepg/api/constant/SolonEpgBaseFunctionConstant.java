package fr.dila.solonepg.api.constant;

/**
 * Liste des fonctions unitaires de l'application SOLON EPG. Ces fonctions déterminent la possibilité de cliquer sur un
 * bouton, afficher un menu, accéder à un document où à une vue.
 * 
 * @author jtremeaux
 */
public class SolonEpgBaseFunctionConstant {

	// TODO à mettre dans le socle solon
	/**
	 * Fonction unitaire permettant de créer un dossier.
	 */
	public static final String	DOSSIER_CREATOR												= "DossierCreator";

	/**
	 * Dossier : donne accès en lecture aux mesures nominatives (sert uniquement à positionner
	 * DossierMesureNominativeDenyReader par négation).
	 */
	public static final String	DOSSIER_MESURE_NOMINATIVE_READER							= "DossierMesureNominativeReader";

	/**
	 * Dossier : restreint l'accès en lecture aux mesures nominatives.
	 */
	public static final String	DOSSIER_MESURE_NOMINATIVE_DENY_READER						= "DossierMesureNominativeDenyReader";

	/**
	 * Dossier : donne accès à la lecture du dossier.
	 */
	public static final String	DOSSIER_RECHERCHE_READER									= "DossierRechercheReader";

	/**
	 * Dossier : possibilité d'agir sur tous les dossiers sans être destinataire de la distribution.
	 */
	public static final String	DOSSIER_ADMIN_UPDATER										= "DossierAdminUpdater";

	/**
	 * Dossier : possibilité d'agir sur les dossiers de son ministère sans être destinataire de la distribution.
	 */
	public static final String	DOSSIER_ADMIN_MIN_UPDATER									= "DossierAdminMinUpdater";

	/**
	 * Dossiers : actions sur la feuille de route de tous les dossiers.
	 */
	public static final String	DOSSIER_DISTRIBUTION_ADMIN_UPDATER							= "DossierDistributionAdminUpdater";

	/**
	 * Visibilité des dossiers distribués via la feuille de route dans un des ministères de l'utilisateur.
	 */
	public static final String	DOSSIER_DISTRIBUTION_MINISTERE_READER						= "DossierDistributionMinistereReader";

	/**
	 * Actions de distribution des dossiers distribués via la feuille de route dans un des ministères de l'utilisateur.
	 */
	public static final String	DOSSIER_DISTRIBUTION_MINISTERE_UPDATER						= "DossierDistributionMinistereUpdater";

	/**
	 * Visibilité des dossiers distribués via la feuille de route dans une des directions de l'utilisateur.
	 */
	public static final String	DOSSIER_DISTRIBUTION_DIRECTION_READER						= "DossierDistributionDirectionReader";

	/**
	 * Visibilité des dossiers rattachés à un des ministères de l'utilisateur.
	 */
	public static final String	DOSSIER_RATTACHEMENT_MINISTERE_READER						= "DossierRattachementMinistereReader";

	/**
	 * Visibilité des dossiers rattachés à une des directions de l'utilisateur.
	 */
	public static final String	DOSSIER_RATTACHEMENT_DIRECTION_READER						= "DossierRattachementDirectionReader";

	/**
	 * Dossiers : modification du fond de dossier.
	 */
	public static final String	DOSSIER_FOND_DOSSIER_UPDATER								= "DossierFondDossierUpdater";

	/**
	 * Dossiers : modification du parapheur.
	 */
	public static final String	DOSSIER_PARAPHEUR_UPDATER									= "DossierParapheurUpdater";

	/**
	 * Dossiers : suppression d'un document dans le parapheur ou le fond de dossier.
	 */
	public static final String	DOSSIER_PARAPHEUR_OR_FOND_DOSSIER_DELETER					= "DossierParapheurOrFondDossierDeleter";

	/**
	 * Dossiers : modification des champs du bordereau relatif à l'activité normative.
	 */
	public static final String	DOSSIER_ACTIVITE_NORMATIVE_UPDATER							= "DossierActiviteNormativeUpdater";

	/**
	 * Dossiers : modification des champs du bordereau relatif à l'indexation.
	 */
	public static final String	DOSSIER_INDEXATION_UPDATER									= "DossierIndexationUpdater";

	/**
	 * Feuilles de route : création des types d'étapes "pour bon à tirer".
	 */
	public static final String	ETAPE_BON_A_TIRER_CREATOR									= "EtapeBonATirerCreator";

	/**
	 * Feuilles de route : création des types d'étapes "pour fourniture d'épreuves".
	 */
	public static final String	ETAPE_FOURNITURE_EPREUVE_CREATOR							= "EtapeFournitureEpreuveCreator";

	/**
	 * Feuilles de route : création des types d'étapes "pour publication à la DILA JO".
	 */
	public static final String	ETAPE_PUBLICATION_DILA_JO_CREATOR							= "EtapePublicationDilaJoCreator";

	/**
	 * Administration : lecture et mise à jour de l'espace d'Administration des fonds de dossier.
	 */
	public static final String	ADMINISTRATION_FOND_DE_DOSSIER								= "AdministrationFondDeDossier";

	/**
	 * Administration : lecture et mise à jour de l'espace d'Administration des parapheurs.
	 */
	public static final String	ADMINISTRATION_PARAPHEUR									= "AdministrationParapheur";

	/**
	 * Dossier : lecture et mise à jour du répertoire réservé au SGG dans le fond de dossier.
	 */
	public static final String	FOND_DOSSIER_REPERTOIRE_SGG									= "FondDossierRepertoireSGG";

	/**
	 * Dossier : Bordereau : mise à jour du champ zone commentaire SGG Dila (RG-DOS-TRT-33)
	 */
	public static final String	ZONE_COMMENTAIRE_SGG_DILA_UPDATER							= "ZoneCommentaireSggDilaUpdater";

	/**
	 * Dossier : Bordereau : mise à jour du champ date signature (RG-DOS-TRT-25)
	 */
	public static final String	BORDEREAU_DATE_SIGNATURE_UPDATER							= "BordereauDateSignatureUpdater";

	/**
	 * Lecture de la corbeille "pour indexation" de l'espace de traitement
	 */
	public static final String	CORBEILLE_INDEXATION_READER									= "CorbeilleIndexationReader";

	/**
	 * Corbeille indexation : affichage de tous les dossiers
	 */
	public static final String	INDEXATION_SGG_UPDATER										= "IndexationSGGUpdater";

	/**
	 * Corbeille indexation : affichage des dossiers publié
	 */
	public static final String	INDEXATION_SGG_PUBLI_UPDATER								= "IndexationSGGPubliUpdater";

	/**
	 * Corbeille indexation : affichage de tous les dossiers du ministère
	 */
	public static final String	INDEXATION_MIN_UPDATER										= "IndexationMinUpdater";

	/**
	 * Corbeille indexation : affichage des dossiers publié du ministère
	 */
	public static final String	INDEXATION_MIN_PUBLI_UPDATER								= "IndexationMinPubliUpdater";

	/**
	 * Corbeille indexation : affichage de tous les dossiers de la direction
	 */
	public static final String	INDEXATION_DIR_UPDATER										= "IndexationDirUpdater";

	/**
	 * Corbeille indexation : affichage des dossiers publié de la direction
	 */
	public static final String	INDEXATION_DIR_PUBLI_UPDATER								= "IndexationDirPubliUpdater";

	/**
	 * Droit de suppression admin fonctionnel
	 */
	public static final String	ADMIN_FONCTIONNEL_SUPPRESSION								= "AdminFonctionnelDossierSuppression";

	/**
	 * Droit de suppression admin ministériel
	 */
	public static final String	ADMIN_MINISTERIEL_SUPPRESSION								= "AdminMinisterielDossierSuppression";

	/**
	 * Droit d'abandon admin ministériel
	 */
	public static final String	ADMIN_MINISTERIEL_ABANDON									= "AdminMinisterielDossierAbandon";

	/**
	 * Droit d'abandon admin fonctionnel
	 */
	public static final String	ADMIN_FONCTIONNEL_ABANDON									= "AdminFonctionnelDossierAbandon";

	/**
	 * Droit d'accès au param Adamant
	 */
	public static final String	ADMIN_PARAM_ADAMANT											= "AdministrationParamAdamant";

	/**
	 * Droit d'accès a l'infocentre général ministere de l'utilisateur seulement
	 */
	public static final String	INFOCENTRE_GENERAL_READER									= "InfocentreGeneralReader";

	/**
	 * Droit d'accès a l'infocentre général tous les ministeres
	 */
	public static final String	INFOCENTRE_GENERAL_FULL_READER								= "InfocentreGeneralFullReader";

	/**
	 * Droit d'accès a l'infocentre général (Direction uniquement)
	 */
	public static final String	INFOCENTRE_GENERAL_DIR_READER								= "InfocentreGeneralDirReader";

	/**
	 * Droit d'accès a l'infocentre du SGG
	 */
	public static final String	INFOCENTRE_SGG_READER										= "InfocentreSGGReader";

	/**
	 * Traitement papier : lecture
	 */
	public static final String	TRAITEMENT_PAPIER_READER									= "TraitementPapierReader";

	/**
	 * Traitement papier : ecriture
	 */
	public static final String	TRAITEMENT_PAPIER_WRITER									= "TraitementPapierWriter";

	/**
	 * Admistration bulletin officiel : reader
	 */
	public static final String	ADMINISTRATION_BULLETIN_READER								= "AdministrationBulletinReader";

	/**
	 * Admistration bulletin officiel : reader
	 */
	public static final String	ADMINISTRATION_INDEXATION_READER							= "AdministrationIndexationReader";

	/**
	 * Administration bulletin officiel : updater
	 */
	public static final String	ADMINISTRATION_INDEXATION_UPDATER							= "AdministrationIndexationUpdater";

	/**
	 * Administration tables de référence : reader
	 */
	public static final String	ADMINISTRATION_TABLES_REFERENCE_READER						= "AdministrationReferenceReader";

	/**
	 * Administration organigramme : reader
	 */
	public static final String	ADMINISTRATION_ORGANIGRAMME_READER							= "OrganigrammeReader";

	/**
	 * Administration organigramme : updater
	 */
	public static final String	ADMINISTRATION_ORGANIGRAMME_UPDATER							= "OrganigrammeUpdater";
	
	/**
	 * Administration organigramme ministère : updater
	 */
	public static final String	MINISTERE_ORGANIGRAMME_UPDATER								= "OrganigrammeMinistereUpdater";

	/**
	 * Administration archivage intermédiaire : reader
	 */
	public static final String	ADMINISTRATION_ARCHIVAGE_ADAMANT_READER				= "DossierArchivageAdamantReader";

	/**
	 * Administration archivage intermédiaire : reader
	 */
	public static final String	ADMINISTRATION_ARCHIVAGE_INTERMEDIAIRE_READER				= "DossierArchivageIntermediaireReader";

	/**
	 * Administration archivage définitif : reader
	 */
	public static final String	ADMINISTRATION_ARCHIVAGE_DEFINITIF_READER					= "DossierArchivageDefinitifReader";

	/**
	 * Administration changement organigramme
	 */
	public static final String	ADMINISTRATION_CHANGEMENT_ORGANIGRAMME						= "AdministrationChangementGouvernement";

	/**
	 * Email : réception des méls destinés aux administrateurs ministériels
	 */
	public static final String	ADMIN_MINISTERIEL_EMAIL_RECEIVER							= "AdminMinisterielEmailReceiver";

	/**
	 * Transfert de dossier
	 */
	public static final String	ADMIN_FONCTIONNEL_TRANSFERT_DOSSIER							= "AdminFonctionnelTransfertExecutor";

	/**
	 * Statistique
	 */
	public static final String	ADMIN_FONCTIONNEL_STATISTIQUES								= "AdminFonctionnelStatReader";
	
	/**
	 * Statistiques ministérielle
	 */
	public static final String ADMIN_MINISTERIEL_STATISTIQUES								= "AdminMinisterielStatReader";

	/**
	 * Tableau dynamique : visibilité des destinataires
	 */
	public static final String	ADMIN_FONCTIONNEL_TABLEAU_DYNAMIQUE_DESINATAIRE_VIEWER		= "AdminFonctionnelTableauDynamiqueDestinataireViewer";

	/**
	 * Autorise l'accès à l'application quand l'accès restreint est actif
	 */
	public static final String	ADMIN_ACCESS_UNRESTRICTED									= "AdminAccessUnrestricted";

	/**
	 * Donne le droit de restreindre / restaurer l'accès à l'application
	 */
	public static final String	ACCESS_UNRESTRICTED_UPDATER									= "AccessUnrestrictedUpdater";

	/**
	 * Donne le droit d'ajouter un profil webservice à un utilisateur.
	 */
	public static final String	PROFIL_WEBSERVICE_UPDATER									= "ProfilWebserviceUpdater";

	/**
	 * Donne le droit de modifier le type d'acte au sein des dossiers de type décrets.
	 */
	public static final String	DOSSIER_TYPE_ACTE_DECRET_UPDATER							= "DossierTypeActeDecretUpdater";

	/**
	 * Donne le droit de modifier les champs "Chargés de mission SGG" et "Conseiller du Premier ministre" d'un poste.
	 */
	public static final String	POSTE_FIELDS_UPDATER										= "PosteFieldsUpdater";

	/**
	 * Donne le droit de modifier les champs du Conseil d'Etat dans le bordereau.
	 */
	public static final String	CONSEIL_ETAT_UPDATER										= "ConseilEtatUpdater";

	public static final String	ETAPE_POUR_ATTRIBUTION_SGG_EXECUTOR							= "EtapePourAttributionSggExecutor";

	public static final String	ETAPE_POUR_SIGNATURE_EXECUTOR								= "EtapePourSignatureExecutor";

	public static final String	ETAPE_POUR_CONTRESEING_EXECUTOR								= "EtapePourContreseingExecutor";

	/**
	 * Donne le droit de supprimer des notes d'étapes
	 */
	public static final String	NOTE_ETAPE_DELETER											= "NoteEtapeDeleter";

	/**
	 * Donne le droit de saisine rectificative et d'envoi de pièces complémentaires pour le Conseil d'Etat
	 */
	public static final String	CONSEIL_ETAT_ENVOI_SAISINE_PIECE_COMPLEMENTAIRE_EXECUTOR	= "EnvoiSaisinePieceComplementaireExecutor";

	/*********************************************
	 * Droit de l'espace d'activite normative
	 *********************************************/
	public static final String	ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER			= "EspaceActiviteNormativeAppLoisReader";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_READER	= "EspaceActiviteNormativeAppOrdoReader";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_READER				= "EspaceActiviteNormativeRatOrdoReader";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_SUIVIS_READER						= "EspaceActiviteNormativeSuivHabReader";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_TRAITE_READER						= "EspaceActiviteNormativeTraitAccReader";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_READER				= "EspaceActiviteNormativeTranspositionReader";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER			= "EspaceActiviteNormativeAppLoisUpdater";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_UPDATER	= "EspaceActiviteNormativeAppOrdoUpdater";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_UPDATER				= "EspaceActiviteNormativeRatOrdoUpdater";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_SUIVIS_UPDATER					= "EspaceActiviteNormativeSuivHabUpdater";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_TRAITE_UPDATER					= "EspaceActiviteNormativeTraitAccUpdater";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_UPDATER				= "EspaceActiviteNormativeTranspositionUpdater";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_EXPORT_DATA_UPDATER				= "EspaceActiviteNormativeExportDataUpdater";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_PARAM_LEGIS_UPDATER				= "EspaceActiviteNormativeParamLegisUpdater";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_PUBLI_LEGIS_EXEC					= "EspaceActiviteNormativePublicationExecutor";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE					= "EspaceActiviteNormativeMinistereApplicationLoi";
	public static final String	ESPACE_ACTIVITE_NORMATIVE_APP_ORDONNANCE_MINISTERE			= "EspaceActiviteNormativeMinistereApplicationOrdonnance";

	/*********************************************
	 * Droit pour accéder au différents espaces de solon epg
	 *********************************************/

	public static final String	ESPACE_TRAITEMENT_READER									= "EspaceTraitementReader";

	public static final String	ESPACE_CREATION_READER										= "EspaceCreationReader";

	public static final String	ESPACE_SUIVI_READER											= "EspaceSuiviReader";

	public static final String	ESPACE_RECHERCHE_READER										= "EspaceRechercheReader";

	public static final String	ESPACE_PARLEMENTAIRE_READER									= "EspaceParlementaireReader";

	public static final String	ESPACE_ADMINISTRATION_READER								= "EspaceAdministrationReader";

	public static final String	DECRET_ARRIVE_READER										= "DecretArriveReader";

	/**
	 * Modifcation des étapes obligatoires SGG
	 */
	public static final String	FEUILLE_ROUTE_OBLIGATOIRE_SGG_UPDATER						= "EtapeObligatoireUpdater";

	/**
	 * Dossier : unlock .
	 */
	public static final String	DOSSIER_ADMIN_UNLOCKER										= "DossierAdminUnlocker";
	
	/**
	 * Gestion des squelettes de feuilles de route.
	 */
	public static final String	FEUILLE_DE_ROUTE_SQUELETTE_UDPATER				= "FDRSqueletteUpdater";

	/**
	 * Création en masse de modèles de feuilles de route à partir des squelettes de de feuilles de route
	 */
	public static final String	FEUILLE_DE_ROUTE_MODEL_MASS_CREATOR				= "FDRModelMassCreator";

	private SolonEpgBaseFunctionConstant() {
		// Private default constructor
	}
}
