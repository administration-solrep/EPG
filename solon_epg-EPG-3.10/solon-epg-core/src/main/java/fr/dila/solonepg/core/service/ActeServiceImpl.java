package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import fr.dila.solonepg.api.constant.ActeVisibilityConstants;
import fr.dila.solonepg.api.constant.TypeActe;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.service.ActeService;

/**
 * Implémentation service des actes EPG
 * 
 */
public class ActeServiceImpl implements ActeService {

	/**
	 * serial version UID
	 */
	private static final long					serialVersionUID					= 5252369563996863441L;

	private static final String					SUFFIXE_A							= "A";
	private static final String					SUFFIXE_X							= "X";
	private static final String					SUFFIXE_D							= "D";
	private static final String					SUFFIXE_L							= "L";

	private static final Map<String, TypeActe>	TYPES_ACTES							= Collections
																							.unmodifiableMap(initActes());

	/**
	 * Liste les types d'actes qui ont des répertoires spécifiques par défaut dans les modèles de fond de dossier.
	 */
	private static final Set<String>			TYPES_ACTES_EXTEND_FOLDER_FDD		= Collections
																							.unmodifiableSet(initTypeActeListExtendFolderFondDeDossier());

	/**
	 * Liste les types d'actes qui ont des format de fichier défini par défaut dans les modèles de parapheur.
	 */
	private static final Set<String>			TYPES_ACTES_EXTEND_FORMAT_PARAPH	= Collections
																							.unmodifiableSet(initTypeActeListExtendFormatParapheur());

	/**
	 * Liste les types d'actes dont les dossiers doivent avoir un extrait pour être validés (acte individuel)
	 */
	private static final Set<String>			TYPES_ACTES_EXTRAIT_OBLIG			= Collections
																							.unmodifiableSet(initTypeActeListExtraitObligatoire());

	/**
	 * Liste les types d'actes pour lesquelles les dossiers seront considérés comme des mesures nominatives
	 */
	private static final Set<String>			TYPES_ACTES_MESURE_NOMINATIVE		= Collections
																							.unmodifiableSet(initTypeActeListMesureNominative());

	private static final Set<String>			TYPES_ACTES_LOI						= Collections
																							.unmodifiableSet(initTypeActeLoiSet());

	private static final Set<String>			TYPES_ACTES_ORDONNANCE				= Collections
																							.unmodifiableSet(Collections
																									.singleton(TypeActeConstants.TYPE_ACTE_ORDONNANCE));

	private static final Set<String>			TYPES_ACTES_ARRETE					= Collections
																							.unmodifiableSet(initArreteList());

	private static final Set<String>			TYPES_ACTES_DECRET					= Collections
																							.unmodifiableSet(initDecretList());

	private static final Set<String>			TYPES_ACTES_DECRET_PR				= Collections
																							.unmodifiableSet(initDecretPRList());

	private static final Set<String>			TYPES_ACTES_NON_REGLEMENTAIRE		= Collections
																							.unmodifiableSet(initNonReglementaireList());

	private static final Set<String>			TYPES_ACTES_CONVENTION_COLLECTIVE	= Collections
																							.unmodifiableSet(initConventionCollective());
	/**
	 * Sous-ensemble de type pour lesquels il n'y a pas de publication de texte intégral
	 */
	private static final Set<String>			TYPES_ACTES_NOT_PUBLI_INT			= Collections
																							.unmodifiableSet(initTypeActeNotVisiblePublicationIntegraleComp());

	/**
	 * Sous-ensemble de type pour lesquels il y a visibilté sur le Numéro texte JO
	 */
	private static final Set<String>			TYPES_ACTES_VISIBLE_NUM_TXT_JO		= Collections
																							.unmodifiableSet(initTypeActeVisibleNumeroTexteJOComp());

	/**
	 * Sous-ensemble de type pour lesquesl il y a visibilité CE
	 */
	private static final Set<String>			TYPES_ACTES_VISIBLE_CE				= Collections
																							.unmodifiableSet(initTypeActeVisibleCEComp());

	/**
	 * Sous ensemble de type pour lesquel il y a dossier Saisine rectificative dans fond de dossier
	 */
	private static final Set<String>			TYPE_ACTES_SAISINE_RECTIFICATIVE	= Collections
																							.unmodifiableSet(initTypeActeListFDDSaisineRectificative());

	public ActeServiceImpl() {
		// Default constructor
	}

	/**
	 * Initialisation des actes
	 */
	private static Map<String, TypeActe> initActes() {
		Map<String, TypeActe> actes = new HashMap<String, TypeActe>();

		actes.put(TypeActeConstants.TYPE_ACTE_AMNISTIE, new TypeActe(TypeActeConstants.TYPE_ACTE_AMNISTIE, "Amnistie",
				"Y"));
		actes.put(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL, new TypeActe(
				TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL, "Arrêté ministériel", SUFFIXE_A));
		actes.put(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL, new TypeActe(
				TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL, "Arrêté interministériel", SUFFIXE_A));
		actes.put(TypeActeConstants.TYPE_ACTE_ARRETE_PM, new TypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_PM,
				"Arrêté PM", SUFFIXE_A));
		actes.put(TypeActeConstants.TYPE_ACTE_ARRETE_PR, new TypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_PR,
				"Arrêté PR", SUFFIXE_A));
		actes.put(TypeActeConstants.TYPE_ACTE_AVENANT, new TypeActe(TypeActeConstants.TYPE_ACTE_AVENANT, "Avenant",
				SUFFIXE_X));
		actes.put(TypeActeConstants.TYPE_ACTE_AVIS, new TypeActe(TypeActeConstants.TYPE_ACTE_AVIS, "Avis", "V"));
		actes.put(TypeActeConstants.TYPE_ACTE_CIRCULAIRE, new TypeActe(TypeActeConstants.TYPE_ACTE_CIRCULAIRE,
				"Circulaire", "C"));
		actes.put(TypeActeConstants.TYPE_ACTE_CITATION, new TypeActe(TypeActeConstants.TYPE_ACTE_CITATION, "Citation",
				"T"));
		actes.put(TypeActeConstants.TYPE_ACTE_COMMUNIQUE, new TypeActe(TypeActeConstants.TYPE_ACTE_COMMUNIQUE,
				"Communiqué", SUFFIXE_X));
		actes.put(TypeActeConstants.TYPE_ACTE_CONVENTION, new TypeActe(TypeActeConstants.TYPE_ACTE_CONVENTION,
				"Convention", SUFFIXE_X));
		actes.put(TypeActeConstants.TYPE_ACTE_DECISION, new TypeActe(TypeActeConstants.TYPE_ACTE_DECISION, "Décision",
				"S"));
		actes.put(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37, new TypeActe(
				TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37, "Décret CE art. 37", SUFFIXE_D));
		actes.put(TypeActeConstants.TYPE_ACTE_DECRET_CE, new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CE,
				"Décret CE", SUFFIXE_D));
		actes.put(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM, new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM,
				"Décret CE CM", SUFFIXE_D));
		actes.put(TypeActeConstants.TYPE_ACTE_DECRET_CM, new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CM,
				"Décret CM", SUFFIXE_D));
		actes.put(TypeActeConstants.TYPE_ACTE_DECRET_PR, new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_PR,
				"Décret PR", SUFFIXE_D));
		actes.put(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE, new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE,
				"Décret simple", SUFFIXE_D));
		actes.put(TypeActeConstants.TYPE_ACTE_DECRET_DE_PUBLICATION_DE_TRAITE_OU_ACCORD, new TypeActe(
				TypeActeConstants.TYPE_ACTE_DECRET_DE_PUBLICATION_DE_TRAITE_OU_ACCORD,
				"Décret de publication de traité ou accord", SUFFIXE_D));
		actes.put(TypeActeConstants.TYPE_ACTE_DELIBERATION, new TypeActe(TypeActeConstants.TYPE_ACTE_DELIBERATION,
				"Délibération", SUFFIXE_X));
		actes.put(TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE, new TypeActe(
				TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE, "Demande Avis CE", SUFFIXE_X));
		actes.put(TypeActeConstants.TYPE_ACTE_DIVERS, new TypeActe(TypeActeConstants.TYPE_ACTE_DIVERS, "Divers",
				SUFFIXE_X));
		actes.put(TypeActeConstants.TYPE_ACTE_EXEQUATUR, new TypeActe(TypeActeConstants.TYPE_ACTE_EXEQUATUR,
				"Exequatur", "E"));
		actes.put(TypeActeConstants.TYPE_ACTE_INSTRUCTION, new TypeActe(TypeActeConstants.TYPE_ACTE_INSTRUCTION,
				"Instruction", "J"));
		actes.put(TypeActeConstants.TYPE_ACTE_LISTE, new TypeActe(TypeActeConstants.TYPE_ACTE_LISTE, "Liste", "K"));
		actes.put(TypeActeConstants.TYPE_ACTE_LOI, new TypeActe(TypeActeConstants.TYPE_ACTE_LOI, "Loi", SUFFIXE_L));
		actes.put(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION, new TypeActe(
				TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION, "Loi art. 53 de la Constitution", SUFFIXE_L));
		actes.put(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE, new TypeActe(
				TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE, "Loi constitutionnelle", SUFFIXE_L));
		actes.put(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE, new TypeActe(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE,
				"Loi organique", SUFFIXE_L));
		actes.put(TypeActeConstants.TYPE_ACTE_NOTE, new TypeActe(TypeActeConstants.TYPE_ACTE_NOTE, "Note", "N"));
		actes.put(TypeActeConstants.TYPE_ACTE_ORDONNANCE, new TypeActe(TypeActeConstants.TYPE_ACTE_ORDONNANCE,
				"Ordonnance", "R"));
		actes.put(TypeActeConstants.TYPE_ACTE_RAPPORT,
				new TypeActe(TypeActeConstants.TYPE_ACTE_RAPPORT, "Rapport", "P"));
		actes.put(TypeActeConstants.TYPE_ACTE_RECTIFICATIF, new TypeActe(TypeActeConstants.TYPE_ACTE_RECTIFICATIF,
				"Rectificatif", "Z"));
		actes.put(TypeActeConstants.TYPE_ACTE_TABLEAU,
				new TypeActe(TypeActeConstants.TYPE_ACTE_TABLEAU, "Tableau", "B"));
		actes.put(TypeActeConstants.TYPE_ACTE_TACHE_GENERIQUE, new TypeActe(
				TypeActeConstants.TYPE_ACTE_TACHE_GENERIQUE, "Tâche Générique", SUFFIXE_X));
		actes.put(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND, new TypeActe(
				TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND, "Arrêté ministériel individuel", SUFFIXE_A));
		actes.put(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND, new TypeActe(
				TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND, "Arrêté interministériel individuel",
				SUFFIXE_A));
		actes.put(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND, new TypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND,
				"Arrêté PM individuel", SUFFIXE_A));
		actes.put(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND, new TypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND,
				"Arrêté PR individuel", SUFFIXE_A));
		actes.put(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND, new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND,
				"Décret CE individuel", SUFFIXE_D));
		actes.put(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND, new TypeActe(
				TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND, "Décret CE CM individuel", SUFFIXE_D));
		actes.put(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND, new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND,
				"Décret CM individuel", SUFFIXE_D));
		actes.put(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND, new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND,
				"Décret PR individuel", SUFFIXE_D));
		actes.put(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND, new TypeActe(
				TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND, "Décret simple individuel", SUFFIXE_D));
		actes.put(TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT, new TypeActe(
				TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT, "Rapport au Parlement", SUFFIXE_X));
		actes.put(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE, new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE,
				"Décret PR CE", SUFFIXE_D));
		actes.put(TypeActeConstants.TYPE_ACTE_ARRETE_CE, new TypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_CE,
				"Arrêté CE", SUFFIXE_A));
		actes.put(TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE, new TypeActe(
				TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE, "Texte non publié", "U"));
		actes.put(TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES, new TypeActe(
				TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES, "Informations parlementaires", SUFFIXE_X));

		return actes;
	}

	private static Set<String> initTypeActeListExtendFolderFondDeDossier() {
		Set<String> typesActesExtendFDD = new HashSet<String>();
		typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_AMNISTIE);
		typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37);
		typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DECRET_CE);
		typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE);
		typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM);
		typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE);
		typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_LOI);
		typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION);
		typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE);
		typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE);
		typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_ORDONNANCE);
		typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
		typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);
		return typesActesExtendFDD;
	}

	private static Set<String> initTypeActeListExtendFormatParapheur() {
		Set<String> typesActesExtendParaph = new HashSet<String>();
		typesActesExtendParaph.add(TypeActeConstants.TYPE_ACTE_LOI);
		typesActesExtendParaph.add(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION);
		typesActesExtendParaph.add(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE);
		typesActesExtendParaph.add(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE);
		return typesActesExtendParaph;
	}

	/**
	 * Liste les types d'actes dont les dossiers doivent avoir un extrait pour être validés (acte individuel)
	 */
	private static Set<String> initTypeActeListExtraitObligatoire() {
		Set<String> typeActeListExtraitObligatoire = new HashSet<String>();
		typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND);
		typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND);
		typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND);
		typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND);
		typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
		typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);
		typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND);
		typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND);
		typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND);
		return typeActeListExtraitObligatoire;
	}

	/**
	 * Liste les types d'actes pour lesquelles les dossiers seront considérés comme des mesures nominatives
	 */
	private static Set<String> initTypeActeListMesureNominative() {
		Set<String> typeActeListMesureNominative = new HashSet<String>();
		typeActeListMesureNominative.add(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND);
		return typeActeListMesureNominative;
	}

	private static Set<String> initTypeActeLoiSet() {
		Set<String> actesLoi = new HashSet<String>();
		actesLoi.add(TypeActeConstants.TYPE_ACTE_LOI);
		actesLoi.add(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE);
		actesLoi.add(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE);
		actesLoi.add(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION);
		return actesLoi;
	}

	private static Set<String> initArreteList() {
		Set<String> actesArrete = new HashSet<String>();
		actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL);
		actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL);
		actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_PM);
		actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_PR);
		actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND);
		actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND);
		actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND);
		actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND);
		return actesArrete;
	}

	private static Set<String> initDecretList() {
		Set<String> actesDecret = new HashSet<String>();
		actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CE);
		actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37);
		actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM);
		actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CM);
		actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_DE_PUBLICATION_DE_TRAITE_OU_ACCORD);
		actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_PR);
		actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE);
		actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
		actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);
		actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND);
		actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND);
		actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND);
		actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE);
		return actesDecret;
	}

	private static Set<String> initConventionCollective() {
		Set<String> conventionCollective = new HashSet<String>();

		conventionCollective.add(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL);
		conventionCollective.add(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND);
		conventionCollective.add(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL);
		conventionCollective.add(TypeActeConstants.TYPE_ACTE_ARRETE_PM);
		conventionCollective.add(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND);
		conventionCollective.add(TypeActeConstants.TYPE_ACTE_ARRETE_PR);
		conventionCollective.add(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND);
		conventionCollective.add(TypeActeConstants.TYPE_ACTE_ARRETE_CE);
		conventionCollective.add(TypeActeConstants.TYPE_ACTE_AVIS);

		return conventionCollective;
	}

	private static Set<String> initNonReglementaireList() {
		Set<String> nonReglementaire = new HashSet<String>();
		nonReglementaire.add(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND);
		nonReglementaire.add(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND);
		nonReglementaire.add(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND);
		nonReglementaire.add(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND);
		nonReglementaire.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
		nonReglementaire.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);
		nonReglementaire.add(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND);
		nonReglementaire.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND);
		nonReglementaire.add(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND);
		return nonReglementaire;
	}

	private static Set<String> initDecretPRList() {
		Set<String> decretPR = new HashSet<String>();
		decretPR.add(TypeActeConstants.TYPE_ACTE_DECRET_PR);
		decretPR.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND);
		return decretPR;
	}

	private static Set<String> initTypeActeNotVisiblePublicationIntegraleComp() {
		Set<String> exclusionList = new HashSet<String>();
		exclusionList.add(TypeActeConstants.TYPE_ACTE_NOTE);
		exclusionList.add(TypeActeConstants.TYPE_ACTE_TABLEAU);
		exclusionList.add(TypeActeConstants.TYPE_ACTE_AMNISTIE);
		exclusionList.add(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION);
		exclusionList.add(TypeActeConstants.TYPE_ACTE_LOI);
		exclusionList.add(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE);
		exclusionList.add(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE);
		exclusionList.add(TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE);
		return exclusionList;
	}

	private static Set<String> initTypeActeVisibleNumeroTexteJOComp() {
		Set<String> inclusion = new HashSet<String>();
		inclusion.add(TypeActeConstants.TYPE_ACTE_CIRCULAIRE);
		inclusion.add(TypeActeConstants.TYPE_ACTE_CITATION);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DIVERS);
		inclusion.add(TypeActeConstants.TYPE_ACTE_EXEQUATUR);
		inclusion.add(TypeActeConstants.TYPE_ACTE_INSTRUCTION);
		inclusion.add(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION);
		inclusion.add(TypeActeConstants.TYPE_ACTE_NOTE);
		inclusion.add(TypeActeConstants.TYPE_ACTE_ORDONNANCE);
		inclusion.add(TypeActeConstants.TYPE_ACTE_RECTIFICATIF);
		inclusion.add(TypeActeConstants.TYPE_ACTE_LOI);
		inclusion.add(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE);
		inclusion.add(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DECRET_CE);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DECRET_CM);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DECRET_DE_PUBLICATION_DE_TRAITE_OU_ACCORD);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DECRET_PR);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND);
		inclusion.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE);

		return inclusion;
	}

	private static Set<String> initTypeActeVisibleCEComp() {
		Set<String> inclusionList = new HashSet<String>();
		inclusionList.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37);
		inclusionList.add(TypeActeConstants.TYPE_ACTE_DECRET_CE);
		inclusionList.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE);
		inclusionList.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM);
		inclusionList.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
		inclusionList.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);
		inclusionList.add(TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE);
		inclusionList.add(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION);
		inclusionList.add(TypeActeConstants.TYPE_ACTE_ORDONNANCE);
		inclusionList.add(TypeActeConstants.TYPE_ACTE_ARRETE_CE);
		return inclusionList;
	}

	@Override
	public TypeActe getActe(String typeActe) {
		return TYPES_ACTES.get(typeActe);
	}

	@Override
	public Collection<TypeActe> findAll() {
		return TYPES_ACTES.values();
	}

	@Override
	public TypeActe getActeByLabel(String label) {
		for (Entry<String, TypeActe> entry : TYPES_ACTES.entrySet()) {
			TypeActe typeActe = entry.getValue();
			if (label.equals(typeActe.getLabel())) {
				return typeActe;
			}
		}
		return null;
	}

	@Override
	public Set<String> getLoiList() {
		return TYPES_ACTES_LOI;
	}

	@Override
	public boolean isLoi(String typeActe) {
		return TYPES_ACTES_LOI.contains(typeActe);
	}

	@Override
	public boolean isOrdonnance(String typeActe) {
		return TYPES_ACTES_ORDONNANCE.contains(typeActe);
	}

	@Override
	public boolean isArrete(String typeActe) {
		return TYPES_ACTES_ARRETE.contains(typeActe);
	}

	private Set<String> getDecretList() {
		return TYPES_ACTES_DECRET;
	}

	@Override
	public boolean isDecret(String typeActe) {
		return getDecretList().contains(typeActe);
	}

	@Override
	public boolean isNonReglementaire(String typeActe) {
		return TYPES_ACTES_NON_REGLEMENTAIRE.contains(typeActe);
	}

	@Override
	public boolean isConventionCollective(String typeActe) {
		return TYPES_ACTES_CONVENTION_COLLECTIVE.contains(typeActe);
	}

	@Override
	public boolean isRectificatif(String typeActe) {
		return TypeActeConstants.TYPE_ACTE_RECTIFICATIF.equals(typeActe);
	}

	@Override
	public Set<String> getTypeActeListExtendFolderFondDeDossier() {
		return TYPES_ACTES_EXTEND_FOLDER_FDD;
	}

	@Override
	public boolean hasTypeActeExtendedFolder(String typeActe) {
		return TYPES_ACTES_EXTEND_FOLDER_FDD.contains(typeActe);
	}

	@Override
	public Set<String> getTypeActeListExtendFormatParapheur() {
		return TYPES_ACTES_EXTEND_FORMAT_PARAPH;
	}

	@Override
	public boolean hasTypeActeExtendedFormat(String typeActe) {
		return TYPES_ACTES_EXTEND_FORMAT_PARAPH.contains(typeActe);
	}

	@Override
	public boolean hasExtraitObligatoire(String typeActe) {
		return TYPES_ACTES_EXTRAIT_OBLIG.contains(typeActe);
	}

	@Override
	public boolean hasTypeActeMesureNominative(String typeActe) {
		return TYPES_ACTES_MESURE_NOMINATIVE.contains(typeActe);
	}

	@Override
	public boolean isVisibleCE(String typeActe) {
		return isLoi(typeActe) || TYPES_ACTES_VISIBLE_CE.contains(typeActe);
	}

	@Override
	public boolean isVisibleBaseLegale(String typeActe) {
		return !TypeActeConstants.TYPE_ACTE_RECTIFICATIF.equals(typeActe);
	}

	@Override
	public boolean isVisibleApplicationLoi(String typeActe) {
		return getDecretList().contains(typeActe);
	}

	@Override
	public boolean isVisibleTranspositionOrdonnance(String typeActe) {
		return getDecretList().contains(typeActe);
	}

	@Override
	public boolean isVisibleHabilitation(String typeActe) {
		return TypeActeConstants.TYPE_ACTE_ORDONNANCE.equals(typeActe);
	}

	@Override
	public boolean isVisibleTranspositionDirective(String typeActe) {
		return TypeActeConstants.TYPE_ACTE_ORDONNANCE.equals(typeActe) || isDecret(typeActe) || isArrete(typeActe)
				|| isLoi(typeActe) || isActeTexteNonPublie(typeActe);
	}

	@Override
	public boolean isVisiblePublicationRapport(String typeActe) {
		return TypeActeConstants.TYPE_ACTE_ORDONNANCE.equals(typeActe) || getDecretList().contains(typeActe);
	}

	@Override
	public boolean isVisiblePublicationIntegrale(String typeActe) {
		return !TYPES_ACTES_NOT_PUBLI_INT.contains(typeActe);
	}

	@Override
	public boolean isVisibleDecretNumerote(String typeActe) {
		return getDecretList().contains(typeActe);
	}

	@Override
	public boolean isVisibleNumeroTexteJO(String typeActe) {
		return TYPES_ACTES_VISIBLE_NUM_TXT_JO.contains(typeActe);
	}

	@Override
	public boolean hasInjectionType(String typeActe) {
		return TypeActeConstants.TYPE_ACTE_INJECTION.equals(typeActe);
	}

	@Override
	public List<TypeActe> getAllTypeActe() {
		List<TypeActe> typeActeList = new ArrayList<TypeActe>();
		// récupération des types d'actes
		for (Entry<String, TypeActe> entry : TYPES_ACTES.entrySet()) {
			typeActeList.add(entry.getValue());
		}
		// tri par ordre par label
		Collections.sort(typeActeList);
		return typeActeList;
	}

	@Override
	public boolean isDecretPR(String typeActe) {
		return TYPES_ACTES_DECRET_PR.contains(typeActe);
	}

	@Override
	public Boolean isRapportAuParlement(String typeActe) {
		return TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT.equals(typeActe);
	}

	@Override
	public Boolean isPublicationVisible(String typeActe) {
		return !(isRapportAuParlement(typeActe) || isActeTexteNonPublie(typeActe));
	}

	@Override
	public Boolean isSggDilaVisible(String typeActe) {
		return isPublicationVisible(typeActe);
	}

	@Override
	public Boolean isParutionVisible(String typeActe) {
		return isPublicationVisible(typeActe);
	}

	@Override
	public Boolean isJorfVisible(String typeActe) {
		return isPublicationVisible(typeActe);
	}

	@Override
	public Boolean isActeTexteNonPublie(String typeActe) {
		return TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE.equals(typeActe);
	}

	@Override
	public Boolean isInformationsParlementaires(String typeActe) {
		return TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES.equals(typeActe);
	}

	@Override
	public boolean hasTypeActeSaisineRectificative(String typeActe) {
		return TYPE_ACTES_SAISINE_RECTIFICATIVE.contains(typeActe);
	}

	@Override
	public Map<String, Boolean> getActeVisibility(String typeActe) {

		Map<String, Boolean> acteVisibility = new HashMap<String, Boolean>();
		if (StringUtils.isNotBlank(typeActe)) {
			acteVisibility.put(ActeVisibilityConstants.CE, isVisibleCE(typeActe));
			acteVisibility.put(ActeVisibilityConstants.BASE_LEGALE, isVisibleBaseLegale(typeActe));
			acteVisibility.put(ActeVisibilityConstants.PUBLICATION_RAPPORT, isVisiblePublicationRapport(typeActe));
			acteVisibility.put(ActeVisibilityConstants.PUBLICATION_INTEGRALE, isVisiblePublicationIntegrale(typeActe));
			acteVisibility.put(ActeVisibilityConstants.DECRET_NUMEROTE, isVisibleDecretNumerote(typeActe));
			acteVisibility.put(ActeVisibilityConstants.NUMERO_TEXTE_JO, isVisibleNumeroTexteJO(typeActe));
			acteVisibility.put(ActeVisibilityConstants.APPLICATION_LOI, isVisibleApplicationLoi(typeActe));
			acteVisibility.put(ActeVisibilityConstants.HABILITATION, isVisibleHabilitation(typeActe));
			acteVisibility.put(ActeVisibilityConstants.TRANSPOSITION_DIRECTIVE,
					isVisibleTranspositionDirective(typeActe));
			acteVisibility.put(ActeVisibilityConstants.TRANSPOSITION_ORDONNANCE,
					isVisibleTranspositionOrdonnance(typeActe));

			Boolean isRapportAuParlement = isRapportAuParlement(typeActe);

			acteVisibility.put(ActeVisibilityConstants.DATE_SIGNATURE, !isRapportAuParlement);
			acteVisibility.put(ActeVisibilityConstants.PUBLICATION, isPublicationVisible(typeActe));
			acteVisibility.put(ActeVisibilityConstants.INDEXATION, !isRapportAuParlement);
			acteVisibility.put(ActeVisibilityConstants.TRANSPOSITION_APPLICATION, !isRapportAuParlement);
			acteVisibility.put(ActeVisibilityConstants.PARUTION, isParutionVisible(typeActe));
			acteVisibility.put(ActeVisibilityConstants.JORF, isJorfVisible(typeActe));
			acteVisibility.put(ActeVisibilityConstants.PARUTION_BO, isParutionVisible(typeActe));
			acteVisibility.put(ActeVisibilityConstants.SGG_DILA, isSggDilaVisible(typeActe));

			acteVisibility.put(ActeVisibilityConstants.PERIODICITE_RAPPORT, isRapportAuParlement);
			acteVisibility.put(ActeVisibilityConstants.CATEGORY_ACTE, isRapportAuParlement);

			acteVisibility.put(ActeVisibilityConstants.PUBLICATION_RAPPORT_PRESENTATION,
					isVisiblePublicationRapport(typeActe));

			acteVisibility.put(ActeVisibilityConstants.INFORMATIONS_PARLEMENTAIRES,
					isInformationsParlementaires(typeActe));
		}

		return acteVisibility;
	}

	private static Set<String> initTypeActeListFDDSaisineRectificative() {
		Set<String> typesActesFDDSaisineRectificative = new HashSet<String>();
		typesActesFDDSaisineRectificative.add(TypeActeConstants.TYPE_ACTE_LOI);
		typesActesFDDSaisineRectificative.add(TypeActeConstants.TYPE_ACTE_ORDONNANCE);
		typesActesFDDSaisineRectificative.add(TypeActeConstants.TYPE_ACTE_DECRET_CE);
		typesActesFDDSaisineRectificative.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
		typesActesFDDSaisineRectificative.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37);
		typesActesFDDSaisineRectificative.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM);
		typesActesFDDSaisineRectificative.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);
		typesActesFDDSaisineRectificative.add(TypeActeConstants.TYPE_ACTE_ARRETE_CE);
		typesActesFDDSaisineRectificative.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE);
		typesActesFDDSaisineRectificative.add(TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE);
		typesActesFDDSaisineRectificative.add(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE);
		typesActesFDDSaisineRectificative.add(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION);
		typesActesFDDSaisineRectificative.add(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE);
		return typesActesFDDSaisineRectificative;
	}
}
