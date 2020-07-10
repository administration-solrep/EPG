package fr.dila.solonepg.api.constant;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.GregorianCalendar;

import org.nuxeo.ecm.core.query.sql.NXQL;

/**
 * Constantes liés au dossier et à ses éléments (schéma,type,métadonnées).
 * 
 * @author Antoine ROLIN
 */
public final class DossierSolonEpgConstants {

	// *************************************************************
	// Format de fichier
	// *************************************************************

	public static final String			FORMAT_FICHIER_ODG										= "odg";

	public static final String			FORMAT_FICHIER_ODI										= "odi";

	public static final String			FORMAT_FICHIER_JPG										= "jpg";

	public static final String			FORMAT_FICHIER_PNG										= "png";

	public static final String			FORMAT_FICHIER_PDF										= "pdf";

	public static final String			FORMAT_FICHIER_RTF										= "rtf";

	public static final String			FORMAT_FICHIER_ODT										= "odt";

	public static final String			FORMAT_FICHIER_DOC										= "doc";

	public static final String			FORMAT_FICHIER_DOCX										= "docx";

	public static final String			FORMAT_FICHIER_ODS										= "ods";

	public static final String			FORMAT_FICHIER_XLS										= "xls";

	public static final String			FORMAT_FICHIER_XLSX										= "xlsx";

	public static final String			FORMAT_FICHIER_ODP										= "odp";

	public static final String			FORMAT_FICHIER_PPT										= "ppt";

	public static final String			FORMAT_FICHIER_PPTX										= "pptx";

	public static final String			FORMAT_FICHIER_ODC										= "odc";

	public static final String			FORMAT_FICHIER_VSD										= "vsd";

	public static final String			FORMAT_FICHIER_ZIP										= "zip";

	/**
	 * Liste tout les format autorisés de solon EPG.
	 */
	public static final List<String>	FormatAutoriseList										= Collections
																										.unmodifiableList(Arrays
																												.asList(new String[] {
			FORMAT_FICHIER_ODG, FORMAT_FICHIER_ODI, FORMAT_FICHIER_JPG, FORMAT_FICHIER_PNG, FORMAT_FICHIER_PDF,
			FORMAT_FICHIER_RTF, FORMAT_FICHIER_ODT, FORMAT_FICHIER_DOC, FORMAT_FICHIER_DOCX, FORMAT_FICHIER_ODS,
			FORMAT_FICHIER_XLS, FORMAT_FICHIER_XLSX, FORMAT_FICHIER_ODP, FORMAT_FICHIER_PPT, FORMAT_FICHIER_PPTX,
			FORMAT_FICHIER_ODC, FORMAT_FICHIER_VSD, FORMAT_FICHIER_ZIP											}));

	/**
	 * Liste les formats de fichier par defaut pour les modèles de fond de dossier.
	 */
	public static final List<String>	FormatAutoriseListExtendFolderFondDeDossier				= FormatAutoriseList;

	/**
	 * Liste les formats de fichier pour les modèles du parapheur ayant un type d'acte de type Loi.
	 */
	public static final List<String>	FormatAutoriseListExtendFolderParapheur					= Collections
																										.unmodifiableList(Arrays
																												.asList(new String[] { FORMAT_FICHIER_DOC }));

	// *************************************************************
	// Feuille de style
	// *************************************************************

	public static final String			FEUILLE_STYLE_ID_PROPERTY								= "idFeuilleStyle";

	public static final String			FEUILLE_STYLE_FILE_PROPERTY								= "file";

	public static final String			FEUILLE_STYLE_FILENAME_PROPERTY							= "filename";

	// *************************************************************
	// Dossier
	// *************************************************************

	public static final String			DOSSIER_SCHEMA											= "dossier_solon_epg";

	public static final String			DOSSIER_SCHEMA_PREFIX									= "dos";

	public static final String			DOSSIER_DOCUMENT_TYPE									= "Dossier";

	// *************************************************************
	// Dossier property
	// *************************************************************

	public static final String			DOSSIER_DOCUMENT_PARAPHEUR_ID							= "idDocumentParapheur";

	public static final String			DOSSIER_DOCUMENT_FOND_DOSSIER_ID						= "idDocumentFDD";

	// /////////////////
	// info generales dossier property
	// ////////////////

	public static final String			DOSSIER_NUMERO_NOR_PROPERTY								= "numeroNor";

	public static final String			DOSSIER_ID_DOSSIER_PROPERTY								= "idDossier";

	public static final String			DOSSIER_NB_DOSSIER_RECTIFIE_PROPERTY					= "nbDossierRectifie";

	public static final String			DOSSIER_STATUT_PROPERTY									= "statut";

	public static final String			DOSSIER_CANDIDAT_PROPERTY								= "candidat";

	public static final String			DOSSIER_IS_DOSSIER_ISSU_INJECTION_PROPERTY				= "isDossierIssuInjection";

	public static final String			DOSSIER_IS_URGENT_PROPERTY								= "isUrgent";

	public static final String			DOSSIER_DATE_CANDIDATURE_PROPERTY						= "dateCandidature";

	public static final String			DOSSIER_DATE_DE_MAINTIEN_EN_PRODCUTION					= "dateDeMaintienEnProduction";

	public static final String			DOSSIER_DATE_VERSEMENT_ARCHIVAGE_INTERMEDIAIRE			= "dateVersementArchivageIntermediaire";

	public static final String			DOSSIER_STATUT_ARCHIVAGE								= "statutArchivage";

	public static final String			DOSSIER_TYPE_ACTE_PROPERTY								= "typeActe";

	public static final String			DOSSIER_TITRE_ACTE_PROPERTY								= "titreActe";

	public static final String			DOSSIER_IS_PARAPHEUR_COMPLET_PROPERTY					= "isParapheurComplet";

	public static final String			DOSSIER_IS_PARAPHEUR_FICHIER_INFO_NON_RECUPERE_PROPERTY	= "isParapheurFichierInfoNonRecupere";

	public static final String			DOSSIER_IS_PARAPHEUR_TYPE_ACTE_UPDATED_PROPERTY			= "isParapheurTypeActeUpdated";

	public static final String			DOSSIER_MESURE_NOMINATIVE_PROPERTY						= "mesureNominative";

	public static final String			DOSSIER_NUMERO_VERSION_ACTE_OU_EXTRAIT_PROPERTY			= "numeroVersionActeOuExtrait";

	public static final String			DOSSIER_IS_ACTE_REFERENCE_FOR_NUMERO_VERSION_PROPERTY	= "isActeReferenceForNumeroVersion";

	// /////////////////
	// info responsables dossier property
	// ////////////////

	public static final String			DOSSIER_MINISTERE_RESPONSABLE_PROPERTY					= "ministereResp";

	public static final String			DOSSIER_DIRECTION_RESPONSABLE_PROPERTY					= "directionResp";

	public static final String			DOSSIER_MINISTERE_ATTACHE_PROPERTY						= "ministereAttache";

	public static final String			DOSSIER_DIRECTION_ATTACHE_PROPERTY						= "directionAttache";

	public static final String			DOSSIER_NOM_RESPONSABLE_PROPERTY						= "nomRespDossier";

	public static final String			DOSSIER_PRENOM_RESPONSABLE_PROPERTY						= "prenomRespDossier";

	public static final String			DOSSIER_NOM_COMPLET_RESPONSABLE_PROPERTY				= "nomCompletRespDossier";

	public static final String			DOSSIER_QUALITE_RESPONSABLE_PROPERTY					= "qualiteRespDossier";

	public static final String			DOSSIER_TEL_RESPONSABLE_PROPERTY						= "telRespDossier";

	public static final String			DOSSIER_MAIL_RESPONSABLE_PROPERTY						= "mailRespDossier";

	public static final String			DOSSIER_ID_CREATEUR_PROPERTY							= "idCreateurDossier";

	// /////////////////
	// info diverse property
	// ////////////////

	public static final String			DOSSIER_CATEGORIE_ACTE_PROPERTY							= "categorieActe";

	public static final String			DOSSIER_TEXTE_ENTREPRISE_PROPERTY						= "texteEntreprise";

	public static final String			DOSSIER_DATE_EFFET_PROPERTY								= "dateEffet";

	public static final String			DOSSIER_DATE_EFFET_DOCUMENT_TYPE						= "dateEffet";

	public static final String			DOSSIER_DATE_EFFET_SCHEMA								= "dateEffet";

	// base legale
	public static final String			DOSSIER_BASE_LEGALE_ACTE_PROPERTY						= "baseLegaleActe";

	public static final String			DOSSIER_BASE_LEGALE_NATURE_TEXTE_PROPERTY				= "baseLegaleNatureTexte";

	public static final String			DOSSIER_BASE_LEGALE_NUMERO_TEXTE_PROPERTY				= "baseLegaleNumeroTexte";

	public static final String			DOSSIER_BASE_LEGALE_DATE_PROPERTY						= "baseLegaleDate";

	public static final String			DOSSIER_DATE_PUBLICATION_PROPERTY						= "datePublication";

	public static final String			DOSSIER_PUBLICATION_RAPPORT_PRESENTATION_PROPERTY		= "publicationRapportPresentation";

	// /////////////////
	// info Conseil Etat (CE)
	// ////////////////

	public static final String			CONSEIL_ETAT_SCHEMA_PREFIX								= "consetat";

	public static final String			DOSSIER_SECTION_CE_PROPERTY								= "sectionCe";

	public static final String			DOSSIER_RAPPORTEUR_CE_PROPERTY							= "rapporteurCe";

	public static final String			DOSSIER_DATE_TRANSMISSION_SECTION_CE_PROPERTY			= "dateTransmissionSectionCe";

	public static final String			DOSSIER_DATE_SECTION_CE_PROPERTY						= "dateSectionCe";

	public static final String			DOSSIER_DATE_AG_CE_PROPERTY								= "dateAgCe";

	public static final String			DOSSIER_NUMERO_ISA_PROPERTY								= "numeroISA";

	public static final String			DOSSIER_PRIORITE_CE_PROPERTY							= "priorite";

	// /////////////////
	// validation et signature
	// ////////////////

	public static final String			DOSSIER_NOM_COMPLET_CHARGES_MISSIONS_PROPERTY			= "nomCompletChargesMissions";

	public static final String			DOSSIER_NOM_COMPLET_CONSEILLERS_PMS_PROPERTY			= "nomCompletConseillersPms";

	public static final String			DOSSIER_DATE_SIGNATURE_PROPERTY							= "dateSignature";

	// /////////////////
	// publication
	// ////////////////

	public static final String			DOSSIER_POUR_FOURNITURE_EPREUVE_PROPERTY				= "pourFournitureEpreuve";

	public static final String			DOSSIER_VECTEUR_PUBLICATION_PROPERTY					= "vecteurPublication";

	public static final String			DOSSIER_PUBLICATION_CONJOINTE_PROPERTY					= "publicationsConjointes";

	public static final String			DOSSIER_PUBLICATION_INTEGRALE_OU_EXTRAIT_PROPERTY		= "publicationIntegraleOuExtrait";

	/**
	 * Identifiant de la valeur "In Extenso" du champ Publication Intégrale ou par Extrait
	 */
	public static final String			DOSSIER_IN_EXTENSO_PROPERTY_VALUE						= "1";

	/**
	 * Identifiant de la valeur "Extrait" du champ Publication Intégrale ou par Extrait
	 */
	public static final String			DOSSIER_EXTRAIT_PROPERTY_VALUE							= "2";

	public static final String			DOSSIER_DECRET_NUMEROTE_PROPERTY						= "decretNumerote";

	public static final String			DOSSIER_DELAI_PUBLICATION_PROPERTY						= "delaiPublication";

	public static final String			DOSSIER_DATE_PRECISEE_PUBLICATION_PROPERTY				= "datePreciseePublication";

	public static final String			DOSSIER_ZONE_COM_SGG_DILA_PROPERTY						= "zoneComSggDila";

	// *************************************************************
	// Dossier Indexation property
	// *************************************************************

	public static final String			DOSSIER_INDEXATION_RUBRIQUE_PROPERTY					= "rubriques";
	
	/** 
	 * Rubrique indiquant que le dossier est une reprise de la V1. 
	 */
	public static final String			RUBRIQUE_REPRISE										= "REPRISE";

	public static final String			DOSSIER_INDEXATION_MOTS_CLES_PROPERTY					= "motscles";

	public static final String			DOSSIER_INDEXATION_CHAMP_LIBRE_PROPERTY					= "libre";

	public static final String			DOSSIER_INDEXATION_SGG_PROPERTY							= "indexationSgg";

	public static final String			DOSSIER_INDEXATION_SGG_PUB_PROPERTY						= "indexationSggPub";

	public static final String			DOSSIER_INDEXATION_MIN_PROPERTY							= "indexationMin";

	public static final String			DOSSIER_INDEXATION_MIN_PUB_PROPERTY						= "indexationMinPub";

	public static final String			DOSSIER_INDEXATION_DIR_PROPERTY							= "indexationDir";

	public static final String			DOSSIER_INDEXATION_DIR_PUB_PROPERTY						= "indexationDirPub";

	// *************************************************************
	// Dossier Applications et transposition property
	// *************************************************************

	public static final String			DOSSIER_APPLICATION_LOI_PROPERTY						= "applicationLoi";

	public static final String			DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY				= "transpositionOrdonnance";

	public static final String			DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY				= "transpositionDirective";

	public static final String			DOSSIER_COMPLEXE_TYPE_REF_PROPERTY						= "ref";

	public static final String			DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY					= "titre";

	public static final String			DOSSIER_COMPLEXE_TYPE_NUMERO_PROPERTY					= "numeroArticles";

	public static final String			DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY				= "commentaire";

	public static final String			DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY				= "refMesure";

	public static final String			DOSSIER_TRANSPO_DIRECTIVE_REF_ANNEE_PROPERTY			= "directiveAnnee";

	public static final String			DOSSIER_HABILITATION_DISPOSITION_PROPERTY				= "dispositionHabilitation";

	public static final String			DOSSIER_HABILITATION_REF_LOI_PROPERTY					= "habilitationRefLoi";

	public static final String			DOSSIER_HABILITATION_NUMERO_ARTICLES_PROPERTY			= "habilitationNumeroArticles";

	public static final String			DOSSIER_HABILITATION_COMMENTAIRE_PROPERTY				= "habilitationCommentaire";

	public static final String			DOSSIER_HABILITATION_NUMERO_ORDRE_PROPERTY				= "habilitationNumeroOrdre";

	public static final String			DOSSIER_HABILITATION_DATE_TERME_PROPERTY				= "habilitationDateTerme";

	public static final String			DOSSIER_HABILITATION_TERME_PROPERTY						= "habilitationTerme";
	
	// /////////////////
	// Adamant
	// ////////////////
	
	public static final String			DOSSIER_ID_EXTRACTION_LOT								= "id_extraction_lot";

	// *************************************************************
	// Dossier Link
	// *************************************************************

	public static final String			DOSSIER_LINK_ACTIONABLE_SCHEMA							= "actionnable_case_link";

	public static final String			DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX					= "acslk";

	public static final String			DOSSIER_LINK_DOCUMENT_TYPE								= "DossierLink";

	// *************************************************************
	// Dossier Link property
	// *************************************************************

	public static final String			DOSSIER_LINK_CASE_LIFE_CYCLE_STATE_PROPERTY				= "caseLifeCycleState";

	public static final String			DOSSIER_LINK_STATUT_ARCHIVAGE_PROPERTY					= "statutArchivage";

	public static final String			DOSSIER_LINK__TITRE_ACTE_PROPERTY						= "titreActe";

	public static final String			DOSSIER_LINK_CASE_DOCUMENT_ID							= "caseDocumentId";

	// *************************************************************
	// Feuille de style
	// *************************************************************

	public static final String			ADMIN_WORKSPACE_DOCUMENT_TYPE							= "AdminWorkspace";

	// *************************************************************
	// Type Dossier signale
	// *************************************************************

	/**
	 * Nom du répertoire racine des dossier signales de l'espace personnel utilisateur
	 */
	public static final String			DOSSIER_SIGNALES_ROOT									= "dossiers-signales";

	public static final String			DOSSIER_SIGNALE_DOCUMENT_TYPE							= "DossierSignale";

	public static final String			DOSSIERS_SIGNALES_SCHEMA								= "dossiers_signales";
	public static final String			DOSSIERS_SIGNALES_TITLE									= "dossiersSignales";
	public static final String			DOSSIERS_SIGNALES_DOCUMENT_TYPE							= "DossiersSignales";

	public static final String			AMPLIATION_FILE_DOCUMENT_TYPE							= "AmpliationFichier";

	// *************************************************************
	// Type nuxeo
	// *************************************************************

	/**
	 * Nom des propriétés associées aux schémas nuxeo.
	 */
	public static final String			CASE_LINK_DOSSIER_ID									= "caseDocumentId";

	// *************************************************************
	// Type fichier
	// *************************************************************

	/**
	 * Nom des propriétés associées au schéma fichier utilisé dans solon epg pour les parapheurs et les fonds de
	 * dossiers.
	 */
	public static final String			FILE_SOLON_EPG_DOCUMENT_TYPE							= "FileSolonEpg";

	public static final String			FILE_SOLON_EPG_DOCUMENT_SCHEMA							= "file_solon_epg";

	public static final String			FILE_SOLON_EPG_PREFIX									= "filepg";

	public static final String			FILE_SOLON_EPG_ENTITE_PROPERTY							= "entite";

	public static final String			FILE_SOLON_EPG_RELATED_DOCUMENT_PROPERTY				= "relatedDocument";

	public static final String			FILE_SOLON_EPG_FILETYPE_ID								= "filetypeId";

	public static final String			DOSSIER_TRIABLE_ORDRE									= "ordre";

	public static final String			DOSSIER_TRIABLE_ID										= "dossierId";

	/**
	 * Nom des propriétés utilisées pour créer un lien vers un fichier
	 */

	/**
	 * url du fichier.
	 */
	public static final String			FILE_URL_PROPERTY										= "url";

	/**
	 * nom du fichier.
	 */
	public static final String			FILE_NAME_PROPERTY										= "fileName";

	/**
	 * extension du fichier.
	 */
	public static final String			FILE_EXTENSION_PROPERTY									= "fileExtension";

	// *************************************************************
	// Divers
	// *************************************************************

	/**
	 * Nom de l'événement qui signale que l'on doit afficher les métadonnées du bordereau
	 */
	public static final String			DISPLAY_REQUIRED_METADONNEE_BORDEREAU_EVENT				= "DisplayRequiredMetadonneBordereauEvent";
	
	public static final Calendar		DATE_01_01_1900											=  new GregorianCalendar(1900, 01, 01);

	// *************************************************************
	// TexteSignale
	// *************************************************************
	public static final String			DOSSIER_ARRIVEE_SOLON_TS								= "arriveeSolonTS";
	public static final String			DOSSIER_DATE_VERSEMENT_TS								= "dateVersementTS";
	public static final String			DOSSIER_DATE_ENVOI_JO_TS								= "dateEnvoiJoTS";

	public static final String			DOSSIER_POSTE_CREATOR_PROPERTY							= "posteCreator";

	// *************************************************************
	// DossierListingDTO
	// *************************************************************

	public static final String			DOSSIER_APPLICATION_LOI_REF_PROPERTY					= "applicationLoiRef";

	public static final String			DOSSIER_TRANSPOSITION_ORDONNANCE_REF_PROPERTY			= "transpositionOrdonnanceRef";

	public static final String			DOSSIER_TRANSPOSITION_DIRECTIVE_REF_PROPERTY			= "transpositionDirectiveRef";

	/**
	 * liste des numero de transposition de directive
	 */
	public static final String			TRANSPOSITION_DIRECTIVE_NUMERO							= "transpositionDirectiveNumero";

	/**
	 * liste des refs des applications loi
	 */
	public static final String			APPLICATION_LOI_REFS									= "applicationLoiRefs";

	/**
	 * liste des refs des applications ordonnance
	 */
	public static final String			APPLICATION_ORDONNANCE_REFS								= "applicationLoiRefs";

	/**
	 * liste des refs des transpositions d'ordonnance
	 */
	public static final String			TRANSPOSITION_ORDONNANCE_REFS							= "transpositionOrdoRefs";

	// *************************************************************
	// Informations Parlementaires
	// *************************************************************

	/**
	 * Emetteur de la communication
	 */
	public static final String			DOSSIER_EMETTEUR_PROPERTY								= "emetteur";

	/**
	 * Rubrique de la communication
	 */
	public static final String			DOSSIER_RUBRIQUE_PROPERTY								= "rubrique";

	/**
	 * Commentaire de la communication
	 */
	public static final String			DOSSIER_COMMENTAIRE_PROPERTY							= "commentaire";

	/**
	 * IdEvenement de la communication
	 */
	public static final String			DOSSIER_ID_EVENEMENT_PROPERTY							= "idEvenement";

	// *************************************************************
	// Recherche fullText sur le numéro Nor du dossier
	// *************************************************************
	/**
	 * signale que le requête commence par une étoile et ne peut pas être executée.
	 */
	public static final String			QUERY_BEGIN_STAR_ERROR									= "queryBeginStarError";

	public static final String			DOSSIER_NUMERO_NOR_QUERY_LIKE							= DOSSIER_SCHEMA_PREFIX
																										+ ":"
																										+ DOSSIER_NUMERO_NOR_PROPERTY
																										+ " LIKE";

	public static final String			DOSSIER_NUMERO_NOR_PREFIX_FULL_TEXT						= NXQL.ECM_FULLTEXT
																										+ "."
																										+ DOSSIER_NUMERO_NOR_QUERY_LIKE;

	// DENORMALISAITON
	public static final String			NOR_ATTRIBUE_DOSSIER_PROPERTY							= "norAttribue";

	public static final String			PUBLIE_DOSSIER_PROPERTY									= "publie";

	public static final String			ARCHIVE_DOSSIER_PROPERTY								= "archive";

	public static final String			DELETED													= "deleted";

	public static final String			PERIODICITE_RAPPORT										= "periodiciteRapport";
	public static final String			PERIODICITE												= "periodicite";

	public static final String			RAPPORT_PERIODIQUE_PERIODICITE_ID						= "RAPPORT_PERIODIQUE";

	public static final String			IS_AFTER_DEMANDE_PUBLICATION							= "isAfterDemandePublication";

	public static final String			ADOPTION												= "adoption";

	public static final Set<String>		BORDEREAU_EDITABLE_FIELDS								= new HashSet<String>();

	static {
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_TITRE_ACTE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_PUBLICATION_RAPPORT_PRESENTATION_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_NOM_RESPONSABLE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_PRENOM_RESPONSABLE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_QUALITE_RESPONSABLE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_TEL_RESPONSABLE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_MAIL_RESPONSABLE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_DATE_PUBLICATION_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_DATE_SIGNATURE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_POUR_FOURNITURE_EPREUVE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_VECTEUR_PUBLICATION_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_DATE_PRECISEE_PUBLICATION_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_PUBLICATION_INTEGRALE_OU_EXTRAIT_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_DECRET_NUMEROTE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_PUBLICATION_CONJOINTE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add(RetourDilaConstants.RETOUR_DILA_SCHEMA_PREFIX + ":"
				+ RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_DELAI_PUBLICATION_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_ZONE_COM_SGG_DILA_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_BASE_LEGALE_ACTE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_INDEXATION_RUBRIQUE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_INDEXATION_MOTS_CLES_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_INDEXATION_CHAMP_LIBRE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_APPLICATION_LOI_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY);
		BORDEREAU_EDITABLE_FIELDS.add("dos:" + DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY);
	}

	// Constant utility class
	private DossierSolonEpgConstants() {
	}

}
