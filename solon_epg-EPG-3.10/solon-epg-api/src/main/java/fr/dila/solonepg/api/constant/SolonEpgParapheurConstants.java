package fr.dila.solonepg.api.constant;

import fr.dila.ss.api.constant.SSParapheurConstants;

/**
 * Constantes du parapheur Solon EPG
 * 
 * @author antoine Rolin
 * 
 */
public final class SolonEpgParapheurConstants extends SSParapheurConstants {

	// *************************************************************
	// Parapheur et parapheur model
	// *************************************************************

	public static final String	DEFAULT_PARAPHEUR_FOLDER_NAME						= "Nouveau répertoire";

	/**
	 * Nom (ecm:name) du DocumentModel "Parapheur" situé dans le Dossier
	 */
	public static final String	DEFAULT_PARAPHEUR_NAME								= "Parapheur";

	/**
	 * Nom de l'événement qui signale que le modèle de parapheur courant doit être réinitialisé
	 */
	public static final String	PARAPHEUR_MODEL_RESET_EVENT							= "ParapheurModelResetEvent";

	/**
	 * Nom de l'événement qui signale que la racine du modèle de parapheur courant a changé
	 */
	public static final String	PARAPHEUR_MODEL_ROOT_CHANGED_EVENT					= "ParapheurModelRootChangedEvent";

	/**
	 * Nom de l'événement qui signale que le modèle de parapheur courant a changé
	 */
	public static final String	PARAPHEUR_MODEL_CHANGED_EVENT						= "ParapheurModelChangedEvent";

	public static final String	PARAPHEUR_MODEL_DOCUMENT_TYPE						= "ModeleParapheur";

	public static final String	PARAPHEUR_MODEL_DOCUMENT_SCHEMA						= "modele_parapheur_solon_epg";

	/**
	 * Les répertoires des modèles de parapheur ont le même type que les répertoires de parapheur.
	 */
	public static final String	PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE				= "ParapheurFolder";

	/**
	 * Les répertoires des modèles de parapheur ont le même schéma que les répertoires de parapheur.
	 */
	public static final String	PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA				= "parapheur_folder_solon_epg";

	public static final String	PARAPHEUR_FOLDER_DOCUMENT_TYPE						= "ParapheurFolder";

	public static final String	PARAPHEUR_FOLDER_DOCUMENT_SCHEMA					= "parapheur_folder_solon_epg";

	/**
	 * Racine des modèles de parapheur.
	 */
	public static final String	PARAPHEUR_MODEL_ROOT_DOCUMENT_TYPE					= "ParapheurModelRoot";

	// *************************************************************
	// Parapheur property
	// *************************************************************

	// *************************************************************
	// Parapheur Model property
	// *************************************************************

	public static final String	PARAPHEUR_MODEL_TYPE_ACTE_PROPERTY					= "typeActe";

	// *************************************************************
	// Parapheur Model Folder property
	// *************************************************************

	/**
	 * info pour savoir si le répertoire est supprimable ou pas dans l'arborescence des répertoires du modèle de
	 * parapheur.
	 */
	public static final String	PARAPHEUR_MODEL_FOLDER_UNDELETABLE					= "ModeleParapheurFolderUndeletable";

	// *************************************************************
	// Parapheur Message erreur
	// *************************************************************

	/**
	 * message signalant qu'une exception est survenu lors de la lecture d'un feuille de style
	 */
	public static final String	PARAPHEUR_ERROR_MSG_EXCEPTION_THROW					= "Le titre et la date de signature n'ont pas pu être reportés dans le bordereau du dossier";

	/**
	 * message signalant que la date du type d'acte ou extrait n'est pas au bon format et n'as pas pu être reportée dans
	 * le bordereau du dossier
	 */
	public static final String	PARAPHEUR_ERROR_MSG_DATE_BAD_FORMAT					= "La date de signature n'a pas pu être reportée dans le bordereau du dossier";

	/**
	 * message signalant que le titre de l'acte est nul et n'as pas pu être reportée dans le bordereau du dossier
	 */
	public static final String	PARAPHEUR_ERROR_MSG_TITRE_ACTE_NOT_FOUND			= "Le titre de l'acte n'a pas pu être reporté dans le bordereau du dossier";

	// *************************************************************
	// Parapheur divers
	// *************************************************************

	/**
	 * Nom du répertoire contenant les actes du parapheur
	 */
	public static final String	PARAPHEUR_FOLDER_ACTE_NAME							= "Acte intégral";

	/**
	 * Nom du répertoire contenant les extraits du parapheur
	 */
	public static final String	PARAPHEUR_FOLDER_EXTRAIT_NAME						= "Extrait";

	/**
	 * Nom du répertoire contenant les pièces complémentaires du parapheur
	 */
	public static final String	PARAPHEUR_FOLDER_PIECE_COMPLEMENTAIRE_NAME			= "Pièces complémentaires à publier";

	/**
	 * Nom du répertoire contenant les fichiers pour épreuvage
	 */
	public static final String	PARAPHEUR_FOLDER_EPREUVES_NAME						= "Epreuve";

	/**
	 * Nom du répertoire contenant les rapports
	 */
	public static final String	PARAPHEUR_FOLDER_RAPPORT_NAME						= "Rapport";

	// *************************************************************
	// Parapheur Node IHM
	// *************************************************************

	/**
	 * info utilisé pour l'affichage de l'arborescence du parapheur : type de répertoire non vide avec possibilité
	 * d'ajouter des documents
	 */
	public static final String	PARAPHEUR_FOLDER_NON_VIDE_DOC_AJOUTABLE				= "ParapheurFolderNonVideDocAjoutable";

	/**
	 * info utilisé pour l'affichage de l'arborescence du parapheur : type de répertoire non vide sans possibilité
	 * d'ajouter des documents
	 */
	public static final String	PARAPHEUR_FOLDER_NON_VIDE_DOC_NON_AJOUTABLE			= "ParapheurFolderNonVideDocNonAjoutable";

	/**
	 * info utilisé pour l'affichage de l'arborescence du parapheur : type de répertoire vide avec possibilité d'ajouter
	 * des documents
	 */
	public static final String	PARAPHEUR_FOLDER_VIDE_DOC_NON_AJOUTABLE				= "ParapheurFolderVideDocNonAjoutable";

	/**
	 * info utilisé pour l'affichage de l'arborescence du parapheur : type de répertoire vide sans possibilité d'ajouter
	 * des documents
	 */
	public static final String	PARAPHEUR_FOLDER_VIDE_DOC_AJOUTABLE					= "ParapheurFolderVideDocAjoutable";

	/**
	 * info utilisé pour l'affichage de l'arborescence du parapheur : type de répertoire fichier
	 */
	public static final String	PARAPHEUR_FILE										= "ParapheurFichier";
	/**
	 * informations concernant l'affichage du document.
	 */
	public static final String	PARAPHEUR_MODEL_FOLDER_EST_NON_VIDE_PROPERTY		= "estNonVide";

	public static final String	PARAPHEUR_MODEL_FOLDER_NB_DOC_ACCEPTE_MAX_PROPERTY	= "nbDocAccepteMax";

	public static final String	PARAPHEUR_MODEL_FOLDER_FEUILLE_STYLE_FILES_PROPERTY	= "feuilleStyleFiles";

	public static final String	PARAPHEUR_MODEL_FOLDER_FORMAT_AUTORISE_PROPERTY		= "formatAutorise";

	public static final String	PARAPHEUR_FILE_DOCUMENT_TYPE						= "FileSolonEpg";

	public static final String	PARAPHEUR_FILE_SCHEMA								= "file_solon_epg";

	// Constant utility class
	private SolonEpgParapheurConstants() {
		super();
	}
}
