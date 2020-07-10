/**
 * 
 */
package fr.dila.solonepg.api.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Constantes liés à l'écran de paramètrage de l'application.
 * 
 * @author antoine Rolin
 *
 */
public final class SolonEpgParametrageAdamantConstants {

	//*************************************************************
	// Paramètrage evenement
	//*************************************************************

	/**
	 * Nom de l'événement qui signale que les paramètrages d'Adamant ont changé
	 */
	public static final String PARAMETRAGE_ADAMANT_CHANGED_EVENT = "ParametrageAdamantChangedEvent";


	//*************************************************************
	// Paramètrage application
	//*************************************************************

	/**
	 * nom du document parametrage adamant 
	 */
	public static final String PARAMETRAGE_ADAMANT_DOCUMENT_NAME = "parametrage-adamant";

	/**
	 * schema parametrage adamant
	 */
	public static final String PARAMETRAGE_ADAMANT_SCHEMA = "parametrage_adamant";
	/**
	 * type de document parametrage adamant
	 */
	public static final String PARAMETRAGE_ADAMANT_DOCUMENT_TYPE = "ParametrageAdamant";

	/**
	 *  parametrage adamant préfixe
	 */
	public static final String PARAMETRAGE_ADAMANT_PREFIX = "pad";

	/**
	 * Colonnes par défault disponible pour les utilisateurs dans la corbeille de l'espace de traitement.
	 */
	public static final List<String> PARAMETRAGE_ADAMANT_DEFAULT_ESPACE_TRAITEMENT_COLUMN_LIST = Collections.unmodifiableList(Arrays.asList(new String[] {SolonEpgProfilUtilisateurConstants.NUMERO_NOR_PROPERTY, SolonEpgProfilUtilisateurConstants.TITRE_ACTE_PROPERTY, SolonEpgProfilUtilisateurConstants.DERNIER_CONTRIBUTEUR_PROPERTY, SolonEpgProfilUtilisateurConstants.DATE_ARRIVEE_DOSSIER_LINK_PROPERTY}));


	//*************************************************************
	// Paramètrage éléments du manifest
	//*************************************************************

	public static final String PARAMETRAGE_ADAMANT_NUMERO_SOLON_PROPERTY 						= "numeroSolon";

	public static final String PARAMETRAGE_ADAMANT_ORIGINATING_AGENCY_BLOC_IDENTIFIER_PROPERTY 	= "originAgencyBlocIdentifier";

	public static final String PARAMETRAGE_ADAMANT_SUBMISSION_AGENCY_BLOC_IDENTIFIER_PROPERTY 	= "submisAgencyBlocIdentifier";

	public static final String PARAMETRAGE_ADAMANT_ARCHIVAL_PROFILE_PROPERTY 					= "archivalProfile";

	public static final String PARAMETRAGE_ADAMANT_ORIGINATING_AGENCY_IDENTIFIER_PROPERTY 		= "originatingAgencyIdentifier";

	public static final String PARAMETRAGE_ADAMANT_SUBMISSION_AGENCY_IDENTIFIER_PROPERTY 		= "submissionAgencyIdentifier";

	//*************************************************************
	// Paramètrage de l'extraction
	//*************************************************************

	public static final String PARAMETRAGE_ADAMANT_DELAI_ELIGIBILITE_PROPERTY 					= "delaiEligibilite";

	public static final String PARAMETRAGE_ADAMANT_NB_DOSSIER_EXTRACTION_PROPERTY 				= "nbDossierExtraction";

	// Constant utility class
	private SolonEpgParametrageAdamantConstants() {
	}
}
