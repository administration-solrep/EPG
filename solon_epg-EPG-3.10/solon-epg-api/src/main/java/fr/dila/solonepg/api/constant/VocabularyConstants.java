package fr.dila.solonepg.api.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Constantes des vocabulaires de SOLON EPG.
 * 
 * @author jtremeaux
 */
public class VocabularyConstants {

	/**
	 * Vocabulaire colonnes instances feuille route.
	 */
	public static final String	FDR_COLUMN											= "feuille_route_column";

	/**
	 * Vocabulaire format de fichier
	 */
	public static final String	FORMAT_FICHIER										= "format_fichier_autorise";

	/**
	 * TODO vocabulaire reponse : voir si nécessaire
	 */

	public static final String	GROUPE_POLITIQUE									= "groupe_politique";
	public static final String	VERROU												= "verrou";
	public static final String	NIVEAU_VISIBILITE									= "niveau_visibilite";
	public static final String	LEGISLATURE											= "legislature";

	/**
	 * Vocabulaire statut du dossier
	 */
	public static final String	STATUT_DOSSIER_DIRECTORY_NAME						= "statut_dossier";

	public static final String	STATUT_INITIE										= "1";
	public static final String	STATUT_LANCE										= "2";
	public static final String	STATUT_ABANDONNE									= "3";
	public static final String	STATUT_PUBLIE										= "4";
	public static final String	STATUT_NOR_ATTRIBUE									= "5";
	public static final String	STATUT_TERMINE_SANS_PUBLICATION						= "6";
	public static final String	STATUT_CLOS											= "7";

	/**
	 * Vocabulaire statut du du candidature dossier
	 */
	public static final String	CANDIDAT_AUCUN										= "1";
	public static final String	CANDIDAT_ELIMINATION_ADMIN_FONCTIONNEL				= "2";
	public static final String	CANDIDAT_ELIMINATION_ADMIN_MINISTERIEL				= "3";
	public static final String	CANDIDAT_ABANDON									= "4";

	/**
	 * Vocabulaire statut d'archivage
	 */
	public static final String	STATUT_ARCHIVAGE_AUCUN								= "1";

	/**
	 * Dossier candidat à l'archivage intermédiaire
	 */
	public static final String	STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE		= "2";

	/**
	 * Dossier présent dans la base d'archivage intermédiaire
	 */
	public static final String	STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE					= "3";

	/**
	 * Dossier candidat à l'archivage définitif
	 */
	public static final String	STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE			= "4";
	
	/**
	 * Dossier en cours d'extraction (ADAMANT)
	 */
	public static final String	STATUT_ARCHIVAGE_EXTRACTION_EN_COURS				= "5";
	
	/**
	 * Statut dossier généré (ADAMANT)
	 */
	public static final String	STATUT_ARCHIVAGE_DOSSIER_GENERE						= "6";
	
	/**
	 * Statut dossier livré (ADAMANT)
	 */
	public static final String	STATUT_ARCHIVAGE_DOSSIER_LIVRE						= "7";
	
	/**
	 * Statut dossier archivé (ADAMANT)
	 */
	public static final String	STATUT_ARCHIVAGE_DOSSIER_ARCHIVE					= "8";
	
	/**
	 * Statut dossier en erreur de génération du SIP (ADAMANT)
	 */
	public static final String	STATUT_ARCHIVAGE_DOSSIER_ERREUR_GEN_SIP				= "90";
	
	/**
	 * Statut dossier en erreur de livraison (ADAMANT)
	 */
	public static final String	STATUT_ARCHIVAGE_DOSSIER_ERREUR_LIVRAISON			= "91";
	
	/**
	 * Statut dossier en erreur d'archivage (ADAMANT)
	 */
	public static final String	STATUT_ARCHIVAGE_DOSSIER_ERREUR_ARCHIVAGE			= "92";

	/**
	 * Champ d'optimisation : archive aucun ou base candidat intermédiaire
	 */
	public static final Integer	ARCHIVE_AUCUN_ET_CANDIDAT_INTERMEDIAIRE				= 0;

	/**
	 * Champ d'optimisation : archive intermédiaire et candidat archivage définitif
	 */
	public static final Integer	ARCHIVE_INTERMEDIAIRE_ET_CANDIDAT_DEFINITIF			= 1;
	
	/**
	 * Libelle du statut d'archivage candidat a l'archivage intermédiaire
	 */
	public static final String	LABEL_STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE		= "Dossier candidat à l'archivage intermédiaire";
	
	/**
	 * Libelle du statut d'archivage intermédiaire
	 */
	public static final String	LABEL_STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE				= "Dossier en archivage intermédiaire";
	
	/**
	 * Libelle du statut d'archivage candidat a l'archivage définitif
	 */
	public static final String	LABEL_STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE			= "Dossier candidat à l'archivage définitif";
	
	/**
	 * Libelle du statut d'archivage  définitif
	 */
	public static final String	LABEL_STATUT_ARCHIVAGE_BASE_DEFINITIVE					= "Dossier en archivage définitif";
	
	/**
	 * Libelle du statut d'archivage  définitif
	 */
	public static final String	LABEL_STATUT_ARCHIVAGE_EXTRACTION_EN_COURS				= "Dossier en cours d'extraction";
	
	/**
	 * Libelle du statut dossier généré
	 */
	public static final String	LABEL_STATUT_ARCHIVAGE_DOSSIER_GENERE					= "Dossier généré";
	
	/**
	 * Libelle du statut dossier livré
	 */
	public static final String	LABEL_STATUT_ARCHIVAGE_DOSSIER_LIVRE					= "Dossier livré";
	
	/**
	 * Libelle du statut dossier archivé
	 */
	public static final String	LABEL_STATUT_ARCHIVAGE_DOSSIER_ARCHIVE					= "Dossier archivé";
	
	/**
	 * Libelle du statut dossier erreur généréation SIP
	 */
	public static final String	LABEL_STATUT_ARCHIVAGE_ERREUR_GEN_SIP					= "Dossier en erreur de génération du SIP";
	
	/**
	 * Libelle du statut dossier erreur livraison
	 */
	public static final String	LABEL_STATUT_ARCHIVAGE_ERREUR_LIVRAISON					= "Dossier en erreur de livraison";
	
	/**
	 * Libelle du statut dossier erreur archivage
	 */
	public static final String	LABEL_STATUT_ARCHIVAGE_ERREUR_ARCHIVAGE					= "Dossier en erreur d'archivage";
	
	/**
	 * List des libellé d'archivage a afficher dans le bordereau du dossier
	 */
	public static final HashMap<String, String>	LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID	= new HashMap<String, String>();
	static {
		LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID.put(STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE, LABEL_STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE); 	// statut 2
		LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID.put(STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE, LABEL_STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE);						// statut 3
		LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID.put(STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE, LABEL_STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE);			// statut 4
		LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID.put(STATUT_ARCHIVAGE_EXTRACTION_EN_COURS, LABEL_STATUT_ARCHIVAGE_EXTRACTION_EN_COURS);						// statut 5
		LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID.put(STATUT_ARCHIVAGE_DOSSIER_GENERE, LABEL_STATUT_ARCHIVAGE_DOSSIER_GENERE);								// statut 6
		LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID.put(STATUT_ARCHIVAGE_DOSSIER_LIVRE, LABEL_STATUT_ARCHIVAGE_DOSSIER_LIVRE);									// statut 7
		LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID.put(STATUT_ARCHIVAGE_DOSSIER_ARCHIVE, LABEL_STATUT_ARCHIVAGE_DOSSIER_ARCHIVE);								// statut 8
		LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID.put(STATUT_ARCHIVAGE_DOSSIER_ERREUR_GEN_SIP, LABEL_STATUT_ARCHIVAGE_ERREUR_GEN_SIP);						// statut 90
		LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID.put(STATUT_ARCHIVAGE_DOSSIER_ERREUR_LIVRAISON, LABEL_STATUT_ARCHIVAGE_ERREUR_LIVRAISON);					// statut 91
		LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID.put(STATUT_ARCHIVAGE_DOSSIER_ERREUR_ARCHIVAGE, LABEL_STATUT_ARCHIVAGE_ERREUR_ARCHIVAGE);					// statut 92
	}

	/**
	 * Vocabulaire de facets statut d'archivage
	 */
	public static final String	STATUT_ARCHIVAGE_FACET_VOCABULARY					= "statut_archivage_facet";

	/**
	 * Liste des statuts d'archivage qui correspondent au facet "Initial"
	 */
	public static final List<String>	STATUT_ARCHIVAGE_INITIAL_FACET					= new ArrayList<String>();
	static {
		STATUT_ARCHIVAGE_INITIAL_FACET.add(STATUT_ARCHIVAGE_AUCUN);
		STATUT_ARCHIVAGE_INITIAL_FACET.add(STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE);
		STATUT_ARCHIVAGE_INITIAL_FACET.add(STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE);
		STATUT_ARCHIVAGE_INITIAL_FACET.add(STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE);
	}

	/**
	 * Vocabulaire délai de publication.
	 */
	public static final String	DELAI_PUBLICATION									= "delai_publication";

	public static final String	DELAI_PUBLICATION_A_DATE_PRECISEE					= "1";
	public static final String	DELAI_PUBLICATION_AUCUN								= "2";
	public static final String	DELAI_PUBLICATION_DE_RIGUEUR						= "3";
	public static final String	DELAI_PUBLICATION_URGENT							= "4";
	public static final String	DELAI_PUBLICATION_SANS_TARDER						= "5";
	public static final String	DELAI_PUBLICATION_SOUS_EMBARGO						= "6";

	/**
	 * Vocabulaire mode de parution.
	 */
	public static final String	MODE_PARUTION										= "mode_parution";

	public static final String	MODE_PARUTION_ELECTRONIQUE							= "1";
	public static final String	MODE_PARUTION_MIXTE									= "2";
	public static final String	MODE_PARUTION_PAPIER								= "3";

	/**
	 * Vocabulaire type de publication intégral ou extrait.
	 */
	public static final String	TYPE_PUBLICATION									= "type_publication";

	public static final String	TYPE_PUBLICATION_IN_EXTENSO							= "1";
	public static final String	TYPE_PUBLICATION_EXTRAIT							= "2";

	/**
	 * Vocabulaire catégorie (nature) de l'acte
	 */
	public static final String	NATURE												= "categorie_acte";
	public static final String CATEGORY_ACTE_CONVENTION_COLLECTIVE = "category_acte_convention_collective";

	public static final String	NATURE_REGLEMENTAIRE								= "1";
	public static final String	NATURE_NON_REGLEMENTAIRE							= "2";
	public static final String	NATURE_CONVENTION_COLLECTIVE						= "3";

	/**
	 * Vocabulaire type de noeud organigramme
	 */
	public static final String	ORGANIGRAMME_TYPE_NODE								= "organigramme_type_node";

	/**
	 * type de noeud ministère.
	 */
	public static final String	MIN_TYPE											= "MIN_TYPE";

	/**
	 * type de noeud direction.
	 */
	public static final String	DIR_TYPE											= "DIR_TYPE";

	/**
	 * type de noeud unité structurelle.
	 */
	public static final String	UST_TYPE											= "UST_TYPE";

	/**
	 * type de noeud poste.
	 */
	public static final String	POSTE_TYPE											= "POSTE_TYPE";

	/**
	 * type de noeud utilisateur.
	 */
	public static final String	USER_TYPE											= "USER_TYPE";

	// *************************************************************
	// Vocabulaire booléen
	// *************************************************************
	/**
	 * Vocabulaire booléen.
	 */
	public static final String	BOOLEAN_VOCABULARY									= "boolean_voc";

	/**
	 * Vocabulaire booléen : valeur vraie.
	 */
	public static final String	BOOLEAN_TRUE										= "TRUE";

	/**
	 * Vocabulaire booléen : valeur fausse.
	 */
	public static final String	BOOLEAN_FALSE										= "FALSE";

	// *************************************************************
	// Feuilles de route : types d'acte
	// *************************************************************
	/**
	 * Vocabulaire type d'acte.
	 */
	public static final String	TYPE_ACTE_VOCABULARY								= "type_acte";

	/**
	 * Type d'acte "Décret CM individuel".
	 */
	public static final String	TYPE_ACTE_DECRET_CM_INDIVIDUEL						= "42";

	// *************************************************************
	// Feuilles de route : types d'étapes
	// *************************************************************
	/**
	 * Type d'étape de feuille de route "Pour rédaction".
	 */
	public static final String	ROUTING_TASK_TYPE_INITIALISATION					= "1";

	/**
	 * Type d'étape de feuille de route "Pour avis".
	 */
	public static final String	ROUTING_TASK_TYPE_AVIS								= "2";

	/**
	 * Type d'étape de feuille de route "Pour avis CE".
	 */
	public static final String	ROUTING_TASK_TYPE_AVIS_CE							= "3";

	/**
	 * Type d'étape de feuille de route "Pour attribution".
	 */
	public static final String	ROUTING_TASK_TYPE_ATTRIBUTION						= "4";

	/**
	 * Type d'étape de feuille de route "Pour attribution SGG".
	 */
	public static final String	ROUTING_TASK_TYPE_ATTRIBUTION_SGG					= "5";

	/**
	 * Type d'étape de feuille de route "Pour attribution secteur parlementaire".
	 */
	public static final String	ROUTING_TASK_TYPE_ATTRIBUTION_SECTEUR_PARLEMENTAIRE	= "6";

	/**
	 * Type d'étape de feuille de route "Pour signature".
	 */
	public static final String	ROUTING_TASK_TYPE_SIGNATURE							= "7";

	/**
	 * Type d'étape de feuille de route "Pour contreseing".
	 */
	public static final String	ROUTING_TASK_TYPE_CONTRESEING						= "8";

	/**
	 * Type d'étape de feuille de route "Pour information".
	 */
	public static final String	ROUTING_TASK_TYPE_INFORMATION						= "9";

	/**
	 * Type d'étape de feuille de route "Pour impression".
	 */
	public static final String	ROUTING_TASK_TYPE_IMPRESSION						= "10";

	/**
	 * Type d'étape de feuille de route "Pour bon à tirer".
	 */
	public static final String	ROUTING_TASK_TYPE_BON_A_TIRER						= "11";

	/**
	 * Type d'étape de feuille de route "Pour fourniture épreuves".
	 */
	public static final String	ROUTING_TASK_TYPE_FOURNITURE_EPREUVE				= "12";

	/**
	 * Type d'étape de feuille de route "Pour publication DILA JO".
	 */
	public static final String	ROUTING_TASK_TYPE_PUBLICATION_DILA_JO				= "13";

	/**
	 * Type d'étape de feuille de route "Pour publication DILA BO".
	 */
	public static final String	ROUTING_TASK_TYPE_PUBLICATION_DILA_BO				= "14";

	/**
	 * Type d'étape de feuille de route "Retour pour modification".
	 */
	public static final String	ROUTING_TASK_TYPE_RETOUR_MODIFICATION				= "15";

	/**
	 * Type d'étape de feuille de route "Pour publication BO".
	 */
	public static final String	ROUTING_TASK_TYPE_PUBLICATION_BO					= "16";

	/**
	 * Schema vocabulaire des type d'avis (Traitement PApier/Communication/Cabinet PM)
	 */
	public static final String	VOCABULARY_TYPE_AVIS_TP_SCHEMA						= "vocabularyTypeAvisTP";

	/**
	 * Directory vocabulaire des type d'avis (Traitement PApier/Communication/Cabinet PM)
	 */
	public static final String	VOCABULARY_TYPE_AVIS_TP_DIRECTORY					= "type_avis_traitement_papier";

	/**
	 * Type des type d'avis (Traitement PApier/Communication/Cabinet PM)
	 */
	public static final String	VOCABULARY_TYPE_AVIS_TP_TYPE						= "type";

	/**
	 * Schema vocabulaire des type d'avis
	 */
	public static final String	VOCABULARY_TYPE_ACTE_SCHEMA							= "vocabularyTypeActe";

	/**
	 * Directory vocabulaire des type d'avis
	 */
	public static final String	VOCABULARY_TYPE_ACTE_DIRECTORY						= "type_acte";

	/**
	 * Classification des type d'acte 0 => arretés et autres textes 1 => Lois, ordonnances, décrets, arrêtés PM et
	 * textes conjoints
	 */
	public static final String	VOCABULARY_TYPE_ACTE_CLASSIFICATION					= "classification";

	/**
	 * Activite Normative des type d'acte
	 */
	public static final String	VOCABULARY_TYPE_ACTE_ACTIVITE_NORMATIVE				= "activiteNormative";

	/**
	 * 1 si decret, 0 sinon
	 */
	public static final String	VOCABULARY_TYPE_ACTE_IS_DECRET						= "isDecret";

	public static final String	TYPE_MESURE_VOCABULARY								= "type_mesure";
	public static final String	TYPE_MESURE_VOCABULARY_ACTIVE						= "1";
	public static final String	TYPE_MESURE_VOCABULARY_EVENTUELLE					= "4";
	public static final String	TYPE_MESURE_VOCABULARY_DIFFERE						= "6";

	public static final List<String>	LIST_MESURE_STATS							= new ArrayList<String>();
	static {
		LIST_MESURE_STATS.add(VocabularyConstants.TYPE_MESURE_VOCABULARY_ACTIVE);
		LIST_MESURE_STATS.add(VocabularyConstants.TYPE_MESURE_VOCABULARY_EVENTUELLE);
		LIST_MESURE_STATS.add(VocabularyConstants.TYPE_MESURE_VOCABULARY_DIFFERE);
	}

	/**
	 * Vocabulaire délai de publication.
	 */
	public static final String	DOSSIER_METADATA_DIRECTORY_NAME						= "dossier_metadata";

	/**
	 * Vocabulaire type_signataire_traitement_papier.
	 */
	public static final String	TYPE_SIGNATAIRE_TP_DIRECTORY_NAME					= "type_signataire_traitement_papier";

	/**
	 * Vocabulaire niveau_priorite_traitement_papier.
	 */
	public static final String	NIVEAU_PRIORITE_TP_DIRECTORY_NAME					= "niveau_priorite_traitement_papier";

	/**
	 * Vocabulaire type_avis_traitement_papier.
	 */
	public static final String	TYPE_AVIS_TP_DIRECTORY_NAME							= "type_avis_traitement_papier";

	// TODO voir si utilisé

	/**
	 * Vocabulaire ministeres.
	 */
	public static final String	MINISTERES_DIRECTORY_NAME							= "ministeres";

	/**
	 * Vocabulaire nature_texte.
	 */
	public static final String	NATURE_TEXTE_DIRECTORY								= "nature_texte";
	public static final String	NATURE_VOCABULARY_PROJ_LOI_ID						= "0";
	public static final String	NATURE_VOCABULARY_PROP_LOI_ID						= "1";
	public static final String	NATURE_VOCABULARY_ARTICLE38C_ID						= "2";
	public static final String	NATURE_VOCABULARY_ARTICLE741_ID						= "3";

	/**
	 * Vocabulaire procedure_vote.
	 */
	public static final String	PROCEDURE_VOTE_DIRECTORY							= "procedure_vote";
	public static final String	PROC_VOTE_VOCABULARY_ACCELEREE_ID					= "0";
	public static final String	PROC_VOTE_VOCABULARY_NORMAL_ID						= "1";
	public static final String	PROC_VOTE_VOCABULARY_URGENCE_ID						= "2";

	/**
	 * Vocabulaire statut_etape_recherche (utilisé pour la recherche).
	 */
	public static final String	STATUT_ETAPE_RECHERCHE_DIRECTORY					= "statut_etape_recherche";

	/**
	 * Vocabulaire type_habilitation (Activite Normative / Habilitation).
	 */
	public static final String	TYPE_HABILITATION_DIRECTORY							= "type_habilitation";

	public static final String	TYPE_ACTE_IS_DECRET									= "1";
	public static final String	TYPE_MESURE_ACTIVE									= "1";
	public static final String	TYPE_HABILITATION_ACTIVE							= "0";

	public static final String	VOC_TYPE_HABILITATION								= "type_habilitation";

	/** Les identifiants des différents types de fichier **/

	public static final Integer		FILETYPEID_ACTE										= 1;
	public static final Integer		FILETYPEID_EXTRAIT									= 2;
	public static final Integer		FILETYPEID_AUTREPARAPHEUR							= 3;
	public static final Integer		FILETYPEID_FONDDOSSIER								= 4;

	/**
	 * Vocabulaire directive_etat (Activite Normative / Transposition des directives).
	 */
	public static final String	VOC_DIRECTIVE_ETAT									= "directive_etat";

	/**
	 * Vocabulaire priorité conseil d'état
	 */
	public static final String	VOC_PRIORITE										= "priorite_ce";
	
	/**
	 * Vocabulaire Adamant
	 */
	public static final String	ACCESS_RULE_ACC_00002								= "ACC-00002";
	public static final String	ACCESS_RULE_ACC_00003								= "ACC-00003";
	public static final String	ACCESS_RULE_ACC_00020								= "ACC-00020";
	public static final String	ACCESS_RULE_ACC_00025								= "ACC-00025";

}
