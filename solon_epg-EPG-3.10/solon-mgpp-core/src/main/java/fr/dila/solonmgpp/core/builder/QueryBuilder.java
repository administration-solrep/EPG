package fr.dila.solonmgpp.core.builder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.SortInfo;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.core.dto.RechercheDTO;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;

/**
 * Builder de requete pour les recherches mggp (message/dossier/oep/avi)
 * 
 * @author asatre
 * 
 */
public class QueryBuilder {

	/************************
	 *** ATTRIBUTS WS EPP ***
	 ************************/
	private static final String	NOR											= "nor";
	private static final String	NOR_LOI										= "norLoi";
	private static final String	IDENTIFIANTS_DOSSIERS_LEGISLATIFS_CONCERNES	= "identifiants_dossiers_legislatifs_concernes";
	private static final String	DATE_CONGRES								= "date_congres";
	private static final String	RAPPORT_PARLEMENT							= "rapport_parlement";
	private static final String	DOSSIER_CIBLE								= "dossier_cible";
	private static final String	DATE_CADUCITE								= "date_caducite";
	private static final String	ABSTENTION									= "abstention";
	private static final String	VOTE_CONTRE									= "vote_contre";
	private static final String	VOTE_POUR									= "vote_pour";
	private static final String	BULLETIN_BLANC								= "bulletin_blanc";
	private static final String	NOMBRE_SUFFRAGE								= "nombre_suffrage";
	private static final String	SENS_AVIS									= "sens_avis";
	private static final String	ECHEANCE									= "echeance";
	private static final String	URL_PUBLICATION								= "url_publication";
	private static final String	NUMERO_RUBRIQUE								= "numero_rubrique";
	private static final String	DATE_JO										= "date_jo";
	private static final String	ANNEE_JO									= "annee_jo";
	private static final String	PAGE_JO										= "page_jo";
	private static final String	NUMERO_JO									= "numero_jo";
	private static final String	DATE_DESIGNATION							= "date_designation";
	private static final String	DATE_CONVOCATION							= "date_convocation";
	private static final String	DATE_ACTE									= "date_acte";
	private static final String	TYPE_ACTE									= "type_acte";
	private static final String	NUMERO_LOI									= "numero_loi";
	private static final String	DATE_PUBLICATION							= "date_publication";
	private static final String	DATE_PROMULGATION							= "date_promulgation";
	private static final String	SUPPLEANT_NOM								= "suppleant.nom";
	private static final String	TITULAIRES_NOM								= "titulaires.nom";
	private static final String	BASE_LEGALE									= "base_legale";
	private static final String	URL_BASE_LEGALE								= "url_base_legale";
	private static final String	ANNEE_RAPPORT								= "annee_rapport";
	private static final String	DATE										= "date";
	private static final String	MOTIF_IRRECEVABILITE						= "motif_irrecevabilite";
	private static final String	NATURE_RAPPORT								= "nature_rapport";
	private static final String	REDEPOT										= "redepot";
	private static final String	POSITION_ALERTE								= "position_alerte";
	private static final String	SORT_ADOPTION								= "sort_adoption";
	private static final String	NUMERO_TEXTE_ADOPTE							= "numero_texte_adopte";
	private static final String	DATE_ADOPTION								= "date_adoption";
	private static final String	DATE_ENGAGEMENT_PROCEDURE					= "date_engagement_procedure";
	private static final String	LIBELLE_ANNEXES								= "libelle_annexes";
	private static final String	DATE_REFUS									= "date_refus";
	private static final String	DEPOT_RAPPORT_NUMERO						= "depot_rapport.numero";
	private static final String	DEPOT_RAPPORT_DATE							= "depot_rapport.date";
	private static final String	TITRE										= "titre";
	private static final String	RAPPORTEUR_NOM								= "rapporteur.nom";
	private static final String	DATE_DISTRIBUTION							= "date_distribution";
	private static final String	DATE_RETRAIT								= "date_retrait";
	private static final String	COMMISSION_SAISIE_POUR_AVIS_NOM				= "commission.saisie_pour_avis.nom";
	private static final String	COMMISSION_SAISIE_AU_FOND_NOM				= "commission.saisie_au_fond.nom";
	private static final String	DATE_CMP									= "dateCMP";
	private static final String	DATE_DEMANDE								= "dateDemande";
	private static final String	RESULTAT_CMP								= "resultat_cmp";
	private static final String	DEPOT_NUMERO								= "depot.numero";
	private static final String	DEPOT_DATE									= "depot.date";
	private static final String	CO_SIGNATAIRE_COLLECTIF						= "co_signataire_collectif";
	private static final String	URL_DOSSIER_SENAT							= "url_dossier_senat";
	private static final String	URL_DOSSIER_AN								= "url_dossier_an";
	private static final String	COMMENTAIRE									= "commentaire";
	private static final String	INTITULE									= "intitule";
	private static final String	CO_AUTEUR_NOM								= "co_auteur.nom";
	private static final String	AUTEUR_NOM									= "auteur.nom";
	private static final String	NATURE_LOI									= "nature_loi";
	private static final String	TYPE_LOI									= "type_loi";
	private static final String	ID_EVENEMENT								= "id_evenement";
	private static final String	TYPE_EVENEMENT								= "type_evenement";
	private static final String	ETAT_EVENEMENT								= "etat";
	private static final String	OBJET										= "objet";
	private static final String	ID_DOSSIER									= "id_dossier";
	private static final String	MSG_DATE_EVENEMENT							= "date_evenement";
	private static final String	NIVEAU_LECTURE								= "niveau_lecture.niveau";
	private static final String	CODE_LECTURE								= "niveau_lecture.code";
	private static final String	ID_CORBEILLE								= "id_corbeille";
	private static final String	VISA_INTERNE								= "visa_interne";
	private static final String	PRESENCE_PIECE_JOINTE						= "presence_piece_jointe";
	private static final String	ETAT_MESSAGE								= "etat_message";
	private static final String	DATE_TRAITEMENT								= "date_traitement";
	private static final String	ID_EVENEMENT_PRECEDENT						= "id_evenement_precedent";
	private static final String	DESTINATAIRE								= "destinataire";
	private static final String	EMETTEUR									= "emetteur";
	private static final String	COPIE										= "copie";
	private static final String	HORODATAGE									= "horodatage";
	private static final String	DATE_AR										= "date_ar";
	private static final String	IDENTIFIANT_METIER							= "identifiant_metier";
	private static final String	RECTIFICATIF								= "rectificatif";
	private static final String	LIBELLE_PJ									= "libelle_pj";
	private static final String	CONTENU_PJ									= "contenu_pj";
	private static final String	DATE_PRESENTATION							= "date_presentation";
	private static final String	DATE_LETTRE_PM								= "date_lettre_pm";
	private static final String	DATE_VOTE									= "date_vote";
	private static final String	DATE_DECLARATION							= "date_declaration";
	private static final String	DEMANDE_VOTE								= "demande_vote";
	private static final String	GROUPE_PARLEMENTAIRE						= "groupe_parlementaire.nom";
	private static final String	ORGANISME									= "organisme";
	private static final String	PERSONNE									= "personne";
	private static final String	FONCTION									= "fonction";
	private static final String	DATE_AUDITION								= "date_audition";
	private static final String	COMMISSIONS									= "commissions";
	private static final String	DATE_REFUS_PROC_ACC							= "date_refus_engagement_procedure";
	private static final String	DATE_REFUS_PROC_ACC_ASS1					= "date_refus_assemblee1";
	private static final String	DATE_CONFERENCE_PRES_ASS2					= "date_conference_assemblee2";
	private static final String	DECISION_PROC_ACC_ASS2						= "decision_proc_acc";

	/*********************
	 *** PREFIX WS EPP ***
	 *********************/
	private static final String	MSG_PREFIX									= "msg:";
	private static final String	EVT_PREFIX									= "evt:";
	// private static final String VERSION_PREFIX = "ver:";

	private static final String	ORDER_BY									= " ORDER BY ";
	private static final String	VIRGULE										= ", ";
	private static final String	AND											= " AND ";
	private static final String	OR											= " OR ";

	private static QueryBuilder	instance;

	private QueryBuilder() {
		// default private constructor
	}

	public static QueryBuilder getInstance() {
		if (instance == null) {
			instance = new QueryBuilder();
		}

		return instance;
	}

	public static String getDefaultOrderClause() {
		return MSG_PREFIX + MSG_DATE_EVENEMENT;
	}

	public RechercheDTO buildQueryAVI(CritereRechercheDTO critereRechercheDTO) {

		RechercheDTO rechercheDTO = new RechercheDTO();

		buildIdDossierAVI(critereRechercheDTO.getIdDossier(), rechercheDTO);
		buildNomAVI(critereRechercheDTO.getObjet(), rechercheDTO);

		String whereClause = rechercheDTO.getWhereClause().toString();

		if (whereClause.endsWith(AND)) {
			whereClause = whereClause.substring(0, whereClause.length() - AND.length());
		}

		rechercheDTO.setWhereClause(new StringBuilder(whereClause));

		return rechercheDTO;
	}
	
	public RechercheDTO buildQueryDecret(CritereRechercheDTO critereRechercheDTO) {

		RechercheDTO rechercheDTO = new RechercheDTO();

		buildIdDossierDecret(critereRechercheDTO.getNumeroNor(), rechercheDTO);
		//buildNomDecret(critereRechercheDTO.getObjet(), rechercheDTO);

		String whereClause = rechercheDTO.getWhereClause().toString();

		if (whereClause.endsWith(AND)) {
			whereClause = whereClause.substring(0, whereClause.length() - AND.length());
		}

		rechercheDTO.setWhereClause(new StringBuilder(whereClause));

		return rechercheDTO;
	}

	public RechercheDTO buildQueryDOC(CritereRechercheDTO critereRechercheDTO) {

		RechercheDTO rechercheDTO = new RechercheDTO();

		buildIdDossierDOC(critereRechercheDTO.getIdDossier(), rechercheDTO);
		buildObjetDOC(critereRechercheDTO.getObjet(), rechercheDTO);

		String whereClause = rechercheDTO.getWhereClause().toString();

		if (whereClause.endsWith(AND)) {
			whereClause = whereClause.substring(0, whereClause.length() - AND.length());
		}

		rechercheDTO.setWhereClause(new StringBuilder(whereClause));

		return rechercheDTO;

	}

	public RechercheDTO buildQueryDPG(CritereRechercheDTO critereRechercheDTO) {

		RechercheDTO rechercheDTO = new RechercheDTO();

		buildIdDossierDPG(critereRechercheDTO.getIdDossier(), rechercheDTO);
		buildObjetDPG(critereRechercheDTO.getObjet(), rechercheDTO);

		String whereClause = rechercheDTO.getWhereClause().toString();

		if (whereClause.endsWith(AND)) {
			whereClause = whereClause.substring(0, whereClause.length() - AND.length());
		}

		rechercheDTO.setWhereClause(new StringBuilder(whereClause));

		return rechercheDTO;
	}

	public RechercheDTO buildQuerySD(CritereRechercheDTO critereRechercheDTO) {

		RechercheDTO rechercheDTO = new RechercheDTO();

		buildIdDossierSD(critereRechercheDTO.getIdDossier(), rechercheDTO);
		buildObjetSD(critereRechercheDTO.getObjet(), rechercheDTO);

		String whereClause = rechercheDTO.getWhereClause().toString();

		if (whereClause.endsWith(AND)) {
			whereClause = whereClause.substring(0, whereClause.length() - AND.length());
		}

		rechercheDTO.setWhereClause(new StringBuilder(whereClause));

		return rechercheDTO;
	}

	public RechercheDTO buildQueryJSS(CritereRechercheDTO critereRechercheDTO) {

		RechercheDTO rechercheDTO = new RechercheDTO();

		buildIdDossierJSS(critereRechercheDTO.getIdDossier(), rechercheDTO);
		buildObjetJSS(critereRechercheDTO.getObjet(), rechercheDTO);

		String whereClause = rechercheDTO.getWhereClause().toString();

		if (whereClause.endsWith(AND)) {
			whereClause = whereClause.substring(0, whereClause.length() - AND.length());
		}

		rechercheDTO.setWhereClause(new StringBuilder(whereClause));

		return rechercheDTO;
	}

	public RechercheDTO buildQueryAUD(CritereRechercheDTO critereRechercheDTO) {

		RechercheDTO rechercheDTO = new RechercheDTO();

		buildIdDossierAUD(critereRechercheDTO.getIdDossier(), rechercheDTO);
		buildNomOrganismeAUD(critereRechercheDTO.getNomOrganisme(), rechercheDTO);

		String whereClause = rechercheDTO.getWhereClause().toString();

		if (whereClause.endsWith(AND)) {
			whereClause = whereClause.substring(0, whereClause.length() - AND.length());
		}

		rechercheDTO.setWhereClause(new StringBuilder(whereClause));

		return rechercheDTO;
	}

	private void buildIdDossierAUD(String nor, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(nor)) {
			nor = nor.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationAUD.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationAUD.ID_DOSSIER);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(nor);
		}

	}

	private void buildNomOrganismeAUD(String nor, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(nor)) {
			nor = nor.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationAUD.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationAUD.NOM_ORGANISME);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(nor);
		}

	}

	private void buildIdDossierAVI(String nor, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(nor)) {
			nor = nor.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationAVI.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationAVI.ID_DOSSIER);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(nor);
		}

	}
	
	private void buildIdDossierDecret(String nor, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(nor)) {
			nor = nor.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationDecret.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationDecret.NOR);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(nor);
		}

	}

	private void buildIdDossierDOC(String nor, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(nor)) {
			nor = nor.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationDOC.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationDOC.ID_DOSSIER);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(nor);
		}

	}

	private void buildObjetDOC(String objet, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(objet)) {
			objet = objet.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationDOC.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationDOC.OBJET);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(objet);
		}

	}

	private void buildIdDossierDPG(String nor, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(nor)) {
			nor = nor.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationDPG.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationDPG.ID_DOSSIER);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(nor);
		}

	}

	private void buildObjetDPG(String objet, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(objet)) {
			objet = objet.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationDPG.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationDPG.OBJET);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(objet);
		}

	}

	private void buildIdDossierSD(String nor, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(nor)) {
			nor = nor.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationSD.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationSD.ID_DOSSIER);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(nor);
		}

	}

	private void buildObjetSD(String objet, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(objet)) {
			objet = objet.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationSD.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationSD.OBJET);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(objet);
		}
	}

	private void buildIdDossierJSS(String nor, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(nor)) {
			nor = nor.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationJSS.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationJSS.ID_DOSSIER);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(nor);
		}

	}

	private void buildObjetJSS(String objet, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(objet)) {
			objet = objet.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationJSS.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationJSS.OBJET);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(objet);
		}
	}

	private void buildNomAVI(String nomAvi, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(nomAvi)) {
			nomAvi = nomAvi.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationAVI.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationAVI.NOM_ORGANISME);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(nomAvi);
		}

	}

	public RechercheDTO buildQuery(CritereRechercheDTO critereRechercheDTO) {

		RechercheDTO rechercheDTO = new RechercheDTO();

		buildElement(MSG_PREFIX, TYPE_EVENEMENT, critereRechercheDTO.getTypeEvenement(), rechercheDTO);
		buildElement(EVT_PREFIX, ETAT_EVENEMENT, critereRechercheDTO.getEtatEvenement(), rechercheDTO);
		buildElement(EVT_PREFIX, ID_EVENEMENT_PRECEDENT, critereRechercheDTO.getIdEvenementPrecedent(), rechercheDTO);
		buildElement(EVT_PREFIX, ID_EVENEMENT, critereRechercheDTO.getIdEvenement(), rechercheDTO);

		buildElement(EVT_PREFIX, ID_DOSSIER, critereRechercheDTO.getIdDossier(), rechercheDTO);
		buildElement(EVT_PREFIX, ID_DOSSIER, critereRechercheDTO.getNumeroNor(), rechercheDTO);
		buildElement(EVT_PREFIX, NOR, critereRechercheDTO.getNumeroNor(), rechercheDTO);
		buildElement(EVT_PREFIX, NOR, critereRechercheDTO.getNor(), rechercheDTO);

		buildElement(EVT_PREFIX, EMETTEUR, critereRechercheDTO.getEmetteurs(), rechercheDTO);
		buildElement(EVT_PREFIX, DESTINATAIRE, critereRechercheDTO.getDestinataires(), rechercheDTO);
		buildElement(EVT_PREFIX, COPIE, critereRechercheDTO.getCopies(), rechercheDTO);
		buildElement(MSG_PREFIX, OBJET, critereRechercheDTO.getObjet(), rechercheDTO);
		buildElement(EVT_PREFIX, NOR, critereRechercheDTO.getNor(), rechercheDTO, Boolean.TRUE);
		buildElement(EVT_PREFIX, NOR_LOI, critereRechercheDTO.getNorLoi(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, CODE_LECTURE, critereRechercheDTO.getNiveauLectureCode(), rechercheDTO);
		buildElement(EVT_PREFIX, NIVEAU_LECTURE, critereRechercheDTO.getNiveauLectureNiveau(), rechercheDTO);
		buildElement(EVT_PREFIX, CODE_LECTURE, critereRechercheDTO.getCodeLecture(), rechercheDTO);
		buildElement(EVT_PREFIX, NIVEAU_LECTURE, critereRechercheDTO.getNiveauLecture(), rechercheDTO);

		buildElement(EVT_PREFIX, EMETTEUR, critereRechercheDTO.getEmetteur(), rechercheDTO);
		buildElement(EVT_PREFIX, DESTINATAIRE, critereRechercheDTO.getDestinataire(), rechercheDTO);
		buildElement(EVT_PREFIX, COPIE, critereRechercheDTO.getCopie(), rechercheDTO);
		buildElement(MSG_PREFIX, PRESENCE_PIECE_JOINTE, critereRechercheDTO.getPresencePieceJointe(), rechercheDTO);
		buildElement(MSG_PREFIX, ETAT_MESSAGE, critereRechercheDTO.getEtatMessages(), rechercheDTO);
		buildElement(MSG_PREFIX, ETAT_MESSAGE, critereRechercheDTO.getEtatMessage(), rechercheDTO);
		buildElement(MSG_PREFIX, ID_CORBEILLE, critereRechercheDTO.getIdsCorbeille(), rechercheDTO);
		buildElement(MSG_PREFIX, VISA_INTERNE, critereRechercheDTO.getVisaInterne(), rechercheDTO, Boolean.TRUE);

		if (critereRechercheDTO.isEnAttente() != null) {
			buildEnAttente(critereRechercheDTO.isEnAttente(), rechercheDTO);
		}

		buildElement(MSG_PREFIX, MSG_DATE_EVENEMENT, critereRechercheDTO.getDateEvenementMin(), rechercheDTO);
		buildElement(MSG_PREFIX, MSG_DATE_EVENEMENT, critereRechercheDTO.getDateEvenementMax(), rechercheDTO,
				Boolean.TRUE);

		buildElement(EVT_PREFIX, HORODATAGE, critereRechercheDTO.getHorodatage(), rechercheDTO);
		buildElement(EVT_PREFIX, HORODATAGE, critereRechercheDTO.getHorodatageMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_AR, critereRechercheDTO.getDateAr(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_AR, critereRechercheDTO.getDateArMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, TYPE_LOI, critereRechercheDTO.getTypeLoi(), rechercheDTO);
		buildElement(EVT_PREFIX, NATURE_LOI, critereRechercheDTO.getNatureLoi(), rechercheDTO);

		buildElement(EVT_PREFIX, AUTEUR_NOM, critereRechercheDTO.getAuteurs(), rechercheDTO, Boolean.TRUE);
		buildElement(EVT_PREFIX, CO_AUTEUR_NOM, critereRechercheDTO.getCoAuteurs(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, INTITULE, critereRechercheDTO.getIntitule(), rechercheDTO);
		buildElement(EVT_PREFIX, COMMENTAIRE, critereRechercheDTO.getCommentaire(), rechercheDTO);

		buildElement(EVT_PREFIX, URL_DOSSIER_AN, critereRechercheDTO.getUrlDossierAn(), rechercheDTO);
		buildElement(EVT_PREFIX, URL_DOSSIER_SENAT, critereRechercheDTO.getUrlDossierSenat(), rechercheDTO);

		buildElement(EVT_PREFIX, CO_SIGNATAIRE_COLLECTIF, critereRechercheDTO.getCoSignataireCollectif(), rechercheDTO);

		buildElement(EVT_PREFIX, DEPOT_DATE, critereRechercheDTO.getDateDepotTexte(), rechercheDTO);
		buildElement(EVT_PREFIX, DEPOT_DATE, critereRechercheDTO.getDateDepotTexteMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DEPOT_NUMERO, critereRechercheDTO.getDepotNumero(), rechercheDTO);

		buildElement(EVT_PREFIX, RESULTAT_CMP, critereRechercheDTO.getResultatCMP(), rechercheDTO);

		buildElement(EVT_PREFIX, DATE_CMP, critereRechercheDTO.getDateCMP(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_CMP, critereRechercheDTO.getDateCMPMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, COMMISSION_SAISIE_AU_FOND_NOM, critereRechercheDTO.getCommissionSaisieAuFond(),
				rechercheDTO, Boolean.TRUE);
		buildElement(EVT_PREFIX, COMMISSION_SAISIE_POUR_AVIS_NOM, critereRechercheDTO.getCommissionSaisiePourAvis(),
				rechercheDTO, Boolean.TRUE);

		// FIXME meta disparue attente reponse EPP
		// buildElement(EVT_PREFIX, "?", critereRechercheDTO.getDateSaisine(), rechercheDTO);
		// buildElement(EVT_PREFIX, "?", critereRechercheDTO.getDateSaisineMax(), rechercheDTO, Boolean.TRUE);
		// buildElement(EVT_PREFIX, "?", critereRechercheDTO.getAttributionCommission(), rechercheDTO);

		buildElement(EVT_PREFIX, DATE_RETRAIT, critereRechercheDTO.getDateRetrait(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_RETRAIT, critereRechercheDTO.getDateRetraitMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_DISTRIBUTION, critereRechercheDTO.getDateDistributionElectronique(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_DISTRIBUTION, critereRechercheDTO.getDateDistributionElectroniqueMax(),
				rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, RAPPORTEUR_NOM, critereRechercheDTO.getRapporteur(), rechercheDTO, Boolean.TRUE);
		buildElement(EVT_PREFIX, TITRE, critereRechercheDTO.getTitre(), rechercheDTO);

		buildElement(EVT_PREFIX, DEPOT_RAPPORT_DATE, critereRechercheDTO.getDateDepotRapport(), rechercheDTO);
		buildElement(EVT_PREFIX, DEPOT_RAPPORT_DATE, critereRechercheDTO.getDateDepotRapport(), rechercheDTO,
				Boolean.TRUE);

		buildElement(EVT_PREFIX, DEPOT_RAPPORT_NUMERO, critereRechercheDTO.getNumeroDepotRapport(), rechercheDTO);

		// FIXME commission.nom en double attente reponse EPP
		buildElement(EVT_PREFIX, COMMISSIONS, critereRechercheDTO.getCommissionSaisie(), rechercheDTO, Boolean.TRUE);
		buildElement(EVT_PREFIX, COMMISSIONS, critereRechercheDTO.getCommissions(), rechercheDTO);

		buildElement(EVT_PREFIX, DATE_REFUS, critereRechercheDTO.getDateRefus(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_REFUS, critereRechercheDTO.getDateRefusMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, LIBELLE_ANNEXES, critereRechercheDTO.getLibelleAnnexe(), rechercheDTO);

		buildElement(EVT_PREFIX, IDENTIFIANT_METIER, critereRechercheDTO.getIdentifiantMetier(), rechercheDTO);

		buildElement(EVT_PREFIX, DATE_ENGAGEMENT_PROCEDURE, critereRechercheDTO.getDateEngagementProcedure(),
				rechercheDTO);
		buildElement(EVT_PREFIX, DATE_ENGAGEMENT_PROCEDURE, critereRechercheDTO.getDateEngagementProcedureMax(),
				rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_REFUS_PROC_ACC, critereRechercheDTO.getDateRefusEngagementProcAcc(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_REFUS_PROC_ACC, critereRechercheDTO.getDateRefusEngagementProcAccAssMax(),
				rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_ADOPTION, critereRechercheDTO.getDateAdoption(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_ADOPTION, critereRechercheDTO.getDateAdoptionMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, NUMERO_TEXTE_ADOPTE, critereRechercheDTO.getNumeroTexteAdopte(), rechercheDTO);

		buildElement(EVT_PREFIX, SORT_ADOPTION, critereRechercheDTO.getSortAdoption(), rechercheDTO);

		buildElement(EVT_PREFIX, POSITION_ALERTE, critereRechercheDTO.getPositionAlerte(), rechercheDTO);
		buildElement(EVT_PREFIX, REDEPOT, critereRechercheDTO.getRedepot(), rechercheDTO);
		buildElement(EVT_PREFIX, RECTIFICATIF, critereRechercheDTO.getRectificatif(), rechercheDTO);

		buildElement(EVT_PREFIX, NATURE_RAPPORT, critereRechercheDTO.getNatureRapport(), rechercheDTO);
		buildElement(EVT_PREFIX, MOTIF_IRRECEVABILITE, critereRechercheDTO.getMotifIrrecevabilite(), rechercheDTO);

		// FIXME date en multiple attente reponse EPP
		buildElement(EVT_PREFIX, DATE, critereRechercheDTO.getDate(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE, critereRechercheDTO.getDateMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, ANNEE_RAPPORT, critereRechercheDTO.getAnneeRapport(), rechercheDTO);
		buildElement(EVT_PREFIX, URL_BASE_LEGALE, critereRechercheDTO.getUrlBaseLegale(), rechercheDTO);
		buildElement(EVT_PREFIX, BASE_LEGALE, critereRechercheDTO.getBaseLegale(), rechercheDTO);

		buildElement(EVT_PREFIX, TITULAIRES_NOM, critereRechercheDTO.getTitulaires(), rechercheDTO, Boolean.TRUE);
		buildElement(EVT_PREFIX, SUPPLEANT_NOM, critereRechercheDTO.getSuppleant(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_PROMULGATION, critereRechercheDTO.getDatePromulgation(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_PROMULGATION, critereRechercheDTO.getDatePromulgationMax(), rechercheDTO,
				Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_PUBLICATION, critereRechercheDTO.getDatePublication(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_PUBLICATION, critereRechercheDTO.getDatePublicationMax(), rechercheDTO,
				Boolean.TRUE);

		buildElement(EVT_PREFIX, NUMERO_LOI, critereRechercheDTO.getNumeroLoi(), rechercheDTO);
		buildElement(EVT_PREFIX, TYPE_ACTE, critereRechercheDTO.getTypeActe(), rechercheDTO);

		buildElement(EVT_PREFIX, DATE_ACTE, critereRechercheDTO.getDateActe(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_ACTE, critereRechercheDTO.getDateActeMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_CONVOCATION, critereRechercheDTO.getDateConvocation(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_CONVOCATION, critereRechercheDTO.getDateConvocationMax(), rechercheDTO,
				Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_DESIGNATION, critereRechercheDTO.getDateDesignation(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_DESIGNATION, critereRechercheDTO.getDateDesignationMax(), rechercheDTO,
				Boolean.TRUE);

		buildElement(EVT_PREFIX, NUMERO_JO, critereRechercheDTO.getNumeroJo(), rechercheDTO);
		buildElement(EVT_PREFIX, PAGE_JO, critereRechercheDTO.getPageJo(), rechercheDTO);
		buildElement(EVT_PREFIX, ANNEE_JO, critereRechercheDTO.getAnneeJo(), rechercheDTO);

		buildElement(EVT_PREFIX, DATE_JO, critereRechercheDTO.getDateJo(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_JO, critereRechercheDTO.getDateJoMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_DEMANDE, critereRechercheDTO.getDateDemande(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_DEMANDE, critereRechercheDTO.getDateDemandeMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, NUMERO_RUBRIQUE, critereRechercheDTO.getNumeroRubrique(), rechercheDTO);
		buildElement(EVT_PREFIX, URL_PUBLICATION, critereRechercheDTO.getUrlPublication(), rechercheDTO);
		buildElement(EVT_PREFIX, ECHEANCE, critereRechercheDTO.getEcheance(), rechercheDTO);
		buildElement(EVT_PREFIX, SENS_AVIS, critereRechercheDTO.getSensAvis(), rechercheDTO);
		buildElement(EVT_PREFIX, NOMBRE_SUFFRAGE, critereRechercheDTO.getSuffrageExprime(), rechercheDTO);

		buildElement(EVT_PREFIX, VOTE_POUR, critereRechercheDTO.getVotePour(), rechercheDTO);
		buildElement(EVT_PREFIX, VOTE_CONTRE, critereRechercheDTO.getVoteContre(), rechercheDTO);
		buildElement(EVT_PREFIX, ABSTENTION, critereRechercheDTO.getAbstention(), rechercheDTO);
		buildElement(EVT_PREFIX, BULLETIN_BLANC, critereRechercheDTO.getBulletinBlanc(), rechercheDTO);

		buildElement(EVT_PREFIX, DATE_CADUCITE, critereRechercheDTO.getDateCaducite(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_CADUCITE, critereRechercheDTO.getDateCaduciteMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DOSSIER_CIBLE, critereRechercheDTO.getDossierCible(), rechercheDTO);
		buildElement(EVT_PREFIX, RAPPORT_PARLEMENT, critereRechercheDTO.getRapportParlement(), rechercheDTO);

		buildElement(EVT_PREFIX, DATE_CONGRES, critereRechercheDTO.getDateCongres(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_CONGRES, critereRechercheDTO.getDateCongresMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, IDENTIFIANTS_DOSSIERS_LEGISLATIFS_CONCERNES,
				critereRechercheDTO.getDossierLegislatif(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, LIBELLE_PJ, critereRechercheDTO.getPieceJointeLabel(), rechercheDTO);
		buildElement(EVT_PREFIX, CONTENU_PJ, critereRechercheDTO.getPieceJointeFullText(), rechercheDTO);

		buildElement(EVT_PREFIX, DATE_PRESENTATION, critereRechercheDTO.getDatePresentation(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_PRESENTATION, critereRechercheDTO.getDatePresentationMax(), rechercheDTO,
				Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_LETTRE_PM, critereRechercheDTO.getDateLettrePm(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_LETTRE_PM, critereRechercheDTO.getDateLettrePmMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_VOTE, critereRechercheDTO.getDateVote(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_VOTE, critereRechercheDTO.getDateVoteMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_DECLARATION, critereRechercheDTO.getDateDeclaration(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_DECLARATION, critereRechercheDTO.getDateDeclarationMax(), rechercheDTO,
				Boolean.TRUE);

		buildElement(EVT_PREFIX, DEMANDE_VOTE, critereRechercheDTO.getDemandeVote(), rechercheDTO);

		buildElement(EVT_PREFIX, GROUPE_PARLEMENTAIRE, critereRechercheDTO.getGroupeParlementaire(), rechercheDTO,
				Boolean.TRUE);

		buildElement(EVT_PREFIX, ORGANISME, critereRechercheDTO.getOrganisme(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_AUDITION, critereRechercheDTO.getDateAudition(), rechercheDTO);
		buildElement(EVT_PREFIX, DATE_AUDITION, critereRechercheDTO.getDateAuditionMax(), rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, PERSONNE, critereRechercheDTO.getPersonne(), rechercheDTO);
		buildElement(EVT_PREFIX, FONCTION, critereRechercheDTO.getFonction(), rechercheDTO);

		buildElement(EVT_PREFIX, DATE_REFUS_PROC_ACC_ASS1, critereRechercheDTO.getDateRefusEngagementProcAccAss1(),
				rechercheDTO);
		buildElement(EVT_PREFIX, DATE_REFUS_PROC_ACC_ASS1, critereRechercheDTO.getDateRefusEngagementProcAccAss1Max(),
				rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DATE_CONFERENCE_PRES_ASS2, critereRechercheDTO.getDateConfPresidentsAss2(),
				rechercheDTO);
		buildElement(EVT_PREFIX, DATE_CONFERENCE_PRES_ASS2, critereRechercheDTO.getDateConfPresidentsAss2Max(),
				rechercheDTO, Boolean.TRUE);

		buildElement(EVT_PREFIX, DECISION_PROC_ACC_ASS2, critereRechercheDTO.getDecisionEngProcAcc(), rechercheDTO);

		// build spécifique pour anciennete message
		buildAncienneteMessage(critereRechercheDTO.getAncienneteMessage(), rechercheDTO);

		String whereClause = rechercheDTO.getWhereClause().toString();

		if (whereClause.endsWith(AND)) {
			whereClause = whereClause.substring(0, whereClause.length() - AND.length());
		}

		rechercheDTO.setWhereClause(new StringBuilder(whereClause));

		// add order clause
		addOrderByClause(rechercheDTO, critereRechercheDTO.getSortInfos());

		return rechercheDTO;

	}

	private void buildElement(String prefix, String property, Object value, RechercheDTO rechercheDTO) {
		buildElement(prefix, property, value, rechercheDTO, null);
	}

	private void buildElement(String prefix, String property, Object value, RechercheDTO rechercheDTO, Boolean isSpecial) {
		if (value != null) {

			if (String.class.isAssignableFrom(value.getClass()) && StringUtils.isNotBlank((String) value)) {
				String val = ((String) value).trim().replaceAll("\\*", "%");
				buildStringElement(prefix, property, rechercheDTO, val);

			} else if (Boolean.class.isAssignableFrom(value.getClass())) {
				Boolean val = (Boolean) value;
				buildBooleanElement(prefix, property, rechercheDTO, val);

			} else if (List.class.isAssignableFrom(value.getClass())) {
				@SuppressWarnings("unchecked")
				List<String> list = (List<String>) value;
				if (!list.isEmpty()) {
					buildListElement(prefix, property, rechercheDTO, list, isSpecial);
				}

			} else if (Date.class.isAssignableFrom(value.getClass())) {
				buildDateElement(prefix, property, value, rechercheDTO, isSpecial);

			} else if (Integer.class.isAssignableFrom(value.getClass())
					|| Long.class.isAssignableFrom(value.getClass())) {
				buildIntegerElement(prefix, property, value, rechercheDTO);

			}
		}

	}

	private void buildIntegerElement(String prefix, String property, Object value, RechercheDTO rechercheDTO) {

		rechercheDTO.getWhereClause().append(" ");
		rechercheDTO.getWhereClause().append(prefix);
		rechercheDTO.getWhereClause().append(property);
		rechercheDTO.getWhereClause().append(" = ? ");
		rechercheDTO.getWhereClause().append(AND);

		rechercheDTO.getParamList().add(value);
	}

	private void buildDateElement(String prefix, String property, Object value, RechercheDTO rechercheDTO, Boolean isMax) {
		rechercheDTO.getWhereClause().append(" ");
		rechercheDTO.getWhereClause().append(prefix);
		rechercheDTO.getWhereClause().append(property);
		if (isMax != null && isMax) {
			// date evenement max
			rechercheDTO.getWhereClause().append(" <= ? ");
		} else {
			// date evenement min
			rechercheDTO.getWhereClause().append(" >= ? ");
		}

		rechercheDTO.getWhereClause().append(AND);

		rechercheDTO.getParamList().add(DateUtil.dateToGregorianCalendar((Date) value));
	}

	private void buildBooleanElement(String prefix, String property, RechercheDTO rechercheDTO, Boolean val) {
		rechercheDTO.getWhereClause().append(" ");
		rechercheDTO.getWhereClause().append(prefix);
		rechercheDTO.getWhereClause().append(property);
		rechercheDTO.getWhereClause().append(" = ? ");
		rechercheDTO.getWhereClause().append(AND);

		rechercheDTO.getParamList().add(val ? 1 : 0);
	}

	private void buildListElement(String prefix, String property, RechercheDTO rechercheDTO, List<String> list,
			Boolean isSpecial) {

		if (isSpecial != null && isSpecial) {
			List<String> newList = new ArrayList<String>();

			for (String elem : list) {
				newList.add(elem.trim().replaceAll("\\*", "%"));
			}
			rechercheDTO.getWhereClause().append(" ( ");
			for (int i = 0; i < newList.size(); i++) {
				rechercheDTO.getWhereClause().append(" ");
				rechercheDTO.getWhereClause().append(prefix);
				rechercheDTO.getWhereClause().append(property);
				rechercheDTO.getWhereClause().append(" ILIKE ? ");
				if (i != newList.size() - 1) {
					rechercheDTO.getWhereClause().append(OR);
				}
			}
			rechercheDTO.getWhereClause().append(" ) ");

			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().addAll(newList);

		} else {
			rechercheDTO.getWhereClause().append(" ");
			rechercheDTO.getWhereClause().append(prefix);
			rechercheDTO.getWhereClause().append(property);

			if (list.size() == 1) {
				rechercheDTO.getWhereClause().append(" = ? ");
			} else {
				rechercheDTO.getWhereClause().append(" IN (");
				rechercheDTO.getWhereClause().append(StringUtil.getQuestionMark(list.size()));
				rechercheDTO.getWhereClause().append(") ");
			}

			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().addAll(list);
		}

	}

	private void buildStringElement(String prefix, String property, RechercheDTO rechercheDTO, Object val) {

		rechercheDTO.getWhereClause().append(" ");
		rechercheDTO.getWhereClause().append(prefix);
		rechercheDTO.getWhereClause().append(property);
		rechercheDTO.getWhereClause().append(" ILIKE ? ");
		rechercheDTO.getWhereClause().append(AND);

		rechercheDTO.getParamList().add(val);
	}

	/**
	 * date_traitement >= Calendar.getInstance() - ancienneteMessage
	 * 
	 * @param ancienneteMessage
	 * @param rechercheDTO
	 */
	private void buildAncienneteMessage(Long ancienneteMessage, RechercheDTO rechercheDTO) {

		if (ancienneteMessage != null) {

			Calendar calMin = Calendar.getInstance();
			calMin.set(Calendar.HOUR_OF_DAY, 0);
			calMin.set(Calendar.MINUTE, 0);
			calMin.set(Calendar.SECOND, 0);
			calMin.set(Calendar.MILLISECOND, 0);
			calMin.add(Calendar.DAY_OF_MONTH, -ancienneteMessage.intValue());

			rechercheDTO.getWhereClause().append(" ");
			rechercheDTO.getWhereClause().append(MSG_PREFIX);
			rechercheDTO.getWhereClause().append(DATE_TRAITEMENT);
			rechercheDTO.getWhereClause().append(" >= ? ");

			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(calMin);

		}

	}

	private void addOrderByClause(RechercheDTO rechercheDTO, List<SortInfo> sortInfos) {
		if (!sortInfos.isEmpty()) {
			rechercheDTO.getWhereClause().append(ORDER_BY);
			for (SortInfo sortInfo : sortInfos) {
				for (String order : sortInfo.getSortColumn().split(VIRGULE)) {
					rechercheDTO.getWhereClause().append(order);
					rechercheDTO.getWhereClause().append(" ");
					rechercheDTO.getWhereClause().append(sortInfo.getSortAscending() ? "ASC" : "DESC");
					rechercheDTO.getWhereClause().append(VIRGULE);
				}
			}
		}

		String whereClause = rechercheDTO.getWhereClause().toString();

		if (whereClause.endsWith(VIRGULE)) {
			whereClause = whereClause.substring(0, whereClause.length() - VIRGULE.length());
		}

		rechercheDTO.setWhereClause(new StringBuilder(whereClause));

	}

	/**
	 * Permet de build la query cherchant les rapports.
	 */
	public RechercheDTO buildQueryDR(CritereRechercheDTO critereRechercheDTO) {

		RechercheDTO rechercheDTO = new RechercheDTO();

		buildIdDossierDR(critereRechercheDTO.getNumeroNor(), rechercheDTO);

		String whereClause = rechercheDTO.getWhereClause().toString();

		if (whereClause.endsWith(AND)) {
			whereClause = whereClause.substring(0, whereClause.length() - AND.length());
		}

		rechercheDTO.setWhereClause(new StringBuilder(whereClause));

		return rechercheDTO;

	}

	private void buildIdDossierDR(String nor, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(nor)) {
			nor = nor.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationDR.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationDR.ID_DOSSIER);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(nor);
		}

	}

	public RechercheDTO buildQueryOEP(CritereRechercheDTO critereRechercheDTO) {

		RechercheDTO rechercheDTO = new RechercheDTO();

		buildNomOEP(critereRechercheDTO.getObjet(), rechercheDTO);
		buildIdDossierOep(critereRechercheDTO.getIdDossier(), rechercheDTO);

		String whereClause = rechercheDTO.getWhereClause().toString();

		if (whereClause.endsWith(AND)) {
			whereClause = whereClause.substring(0, whereClause.length() - AND.length());
		}

		rechercheDTO.setWhereClause(new StringBuilder(whereClause));

		return rechercheDTO;

	}

	private void buildIdDossierOep(String nor, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(nor)) {
			nor = nor.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationOEP.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationOEP.ID_DOSSIER);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(nor);
		}

	}

	private void buildNomOEP(String nomOep, RechercheDTO rechercheDTO) {
		if (StringUtils.isNotBlank(nomOep)) {
			nomOep = nomOep.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(FichePresentationOEP.PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(FichePresentationOEP.NOM_ORGANISME);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(nomOep);
		}

	}

	public RechercheDTO buildQueryDossier(CritereRechercheDTO critereRechercheDTO) {

		RechercheDTO rechercheDTO = new RechercheDTO();

		buildTypeActeDossier(critereRechercheDTO, rechercheDTO);
		buildTitreActeDossier(critereRechercheDTO.getTitreActe(), rechercheDTO);
		buildNumeroNorDossier(critereRechercheDTO.getNumeroNor(), rechercheDTO);

		String whereClause = rechercheDTO.getWhereClause().toString();

		if (whereClause.endsWith(AND)) {
			whereClause = whereClause.substring(0, whereClause.length() - AND.length());
		}

		rechercheDTO.setWhereClause(new StringBuilder(whereClause));

		return rechercheDTO;

	}

	private void buildTypeActeDossier(CritereRechercheDTO critereRechercheDTO, RechercheDTO rechercheDTO) {

		rechercheDTO.getWhereClause().append(" d.");
		rechercheDTO.getWhereClause().append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
		rechercheDTO.getWhereClause().append(":");
		rechercheDTO.getWhereClause().append(DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_PROPERTY);
		rechercheDTO.getWhereClause().append(" IN (");
		rechercheDTO.getWhereClause().append(StringUtil.getQuestionMark(critereRechercheDTO.getTypeActes().size()));
		rechercheDTO.getWhereClause().append(") ");
		rechercheDTO.getWhereClause().append(AND);

		rechercheDTO.getParamList().addAll(critereRechercheDTO.getTypeActes());

	}

	private void buildNumeroNorDossier(String numeroNor, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(numeroNor)) {
			numeroNor = numeroNor.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(numeroNor);
		}

	}

	private void buildTitreActeDossier(String titreActe, RechercheDTO rechercheDTO) {

		if (StringUtils.isNotBlank(titreActe)) {
			titreActe = titreActe.trim().replaceAll("\\*", "%");

			rechercheDTO.getWhereClause().append(" d.");
			rechercheDTO.getWhereClause().append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
			rechercheDTO.getWhereClause().append(":");
			rechercheDTO.getWhereClause().append(DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_PROPERTY);
			rechercheDTO.getWhereClause().append(" ILIKE ? ");
			rechercheDTO.getWhereClause().append(AND);

			rechercheDTO.getParamList().add(titreActe);

		}

	}

	public void buildEnAttente(boolean enAttente, RechercheDTO rechercheDTO) {
		rechercheDTO.getWhereClause().append(" ");
		rechercheDTO.getWhereClause().append(MSG_PREFIX);
		rechercheDTO.getWhereClause().append(VISA_INTERNE);
		if (!enAttente) {
			rechercheDTO.getWhereClause().append(" NOT");
		}
		rechercheDTO.getWhereClause().append(" ILIKE 'EN_ATTENTE' ");
		rechercheDTO.getWhereClause().append(" AND ");
	}

	public boolean isWhereClauseEmptyMessage(CritereRechercheDTO critereRechercheDTO) {
		return isEmpty(buildQuery(critereRechercheDTO));
	}

	public boolean isWhereClauseEmptyDossier(CritereRechercheDTO critereRechercheDTO) {
		return isEmpty(buildQueryDossier(critereRechercheDTO));
	}

	public boolean isWhereClauseEmptyAVI(CritereRechercheDTO critereRechercheDTO) {
		return isEmpty(buildQueryAVI(critereRechercheDTO));
	}

	public boolean isWhereClauseEmptyMessageOEP(CritereRechercheDTO critereRechercheDTO) {
		return isEmpty(buildQueryOEP(critereRechercheDTO));
	}

	private boolean isEmpty(RechercheDTO rechercheDTO) {
		String whereClause = rechercheDTO.getWhereClause().toString();
		return StringUtils.isBlank(whereClause) || whereClause.startsWith(ORDER_BY);
	}

}
