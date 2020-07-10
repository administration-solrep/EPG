package fr.dila.solonepg.web.statistiques;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.NXCore;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunCallback;
import org.nuxeo.ecm.core.security.SecurityService;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.ui.web.util.BaseURL;
import org.nuxeo.ecm.platform.uidgen.service.UIDSequencerImpl;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.administration.BulletinOfficiel;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgStatistiquesConstant;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.enumeration.EpgRapportStatistique;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.SolonEpgUserManager;
import fr.dila.solonepg.core.dto.DossierArchivageStatDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.DossierArchivageStatPageProvider;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.SSUtilisateurConnectionMonitorService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.web.birt.BirtReportActionsBean;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.service.MailboxService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.context.NavigationContextBean;

@Name("statistiquesAction")
@Scope(CONVERSATION)
public class StatistiquesActionBean {

	private static final STLogger				LOGGER					= STLogFactory.getLog(StatistiquesActionBean.class);

	@In(required = true, create = true)
	protected SSPrincipal						ssPrincipal;

	@In(create = true, required = true)
	protected transient BirtReportActionsBean	birtReportingActions;

	@In(create = true, required = true)
	protected transient CoreSession				documentManager;

	@In(create = true, required = false)
	protected transient FacesMessages			facesMessages;
	
	@In(create = true, required = false)
	protected transient ContentViewActions		contentViewActions;

	@In(create = true, required = false)
	protected transient ResourcesAccessor		resourcesAccessor;
	
	@In(create = true, required = false)
	protected transient NavigationContextBean	navigationContext;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	private Map<String, String>					parametersMap		= new HashMap<String, String>();

	private StatistiquesItem					statistiqueCourante	= null;

	private Map<Integer, StatistiquesItem>		statistiquesMap		= null;

	private String								principalStr		= "";

	private String								permissionsStr		= "";

	protected boolean							readyToProcess		= false;
	
	protected Date 								dateDebutArchivage	= null;
	
	protected Date 								dateFinArchivage	= null;
	
	protected String							statutArchivage		= null;

	/**
	 * 
	 * @return
	 */
	public Map<Integer, StatistiquesItem> getStatistquesMap() {

		if (statistiquesMap == null) {
			StatMapBuilder builder = new StatMapBuilder();
			String[] principals = SecurityService.getPrincipalsToCheck(ssPrincipal);
			for (String principal : principals) {
				principalStr = principalStr + principal + "&";
			}
			String[] permissions = NXCore.getSecurityService().getPermissionsToCheck(SecurityConstants.BROWSE);

			for (String permission : permissions) {
				permissionsStr = permissionsStr + permission + "&";
			}
			if (canViewAdminStat()) {
				builder.setCurrentCategory(SolonEpgStatistiquesConstant.STATISTIQUE_CATEGORY_ADMIN);
				builder.putStat(EpgRapportStatistique.NOMBRE_UTILISATEUR_CONNECTES);
				builder.putStat(EpgRapportStatistique.NOMS_UTILISATEUR_CONNECTES);
				builder.putStat(EpgRapportStatistique.LISTE_UTILISATEURS_PAS_CONNECTES_DUREE);
			}
			
			if (hasRightAdminMinStat()) {
				builder.setCurrentCategory(SolonEpgStatistiquesConstant.STATISTIQUE_CATEGORY_ADMIN);
				builder.putStat(EpgRapportStatistique.LISTE_UTILISATEURS_PAS_CONNECTES_DUREE);
			}
			
			if (this.canViewSggStat()) {
				builder.setCurrentCategory(SolonEpgStatistiquesConstant.STATISTIQUE_CATEGORY_SGG);
				builder.putStat(EpgRapportStatistique.LISTE_TEXTES_EN_COURS_COMMUNICATION_VALIDATION);
				builder.putStat(EpgRapportStatistique.LISTE_ORIGINAUX_TEXTES_ARRIVES_SGG);
				builder.putStat(EpgRapportStatistique.LISTE_TEXTES_SIGNATURE_PREMIER_MINISTRE_PRESIDENT);
				builder.putStat(EpgRapportStatistique.LISTE_TEXTES_AMPLIATIONS);
				builder.putStat(EpgRapportStatistique.LISTE_TEXTES_CHEMINANT_DEMATERIALISEE);
				builder.putStat(EpgRapportStatistique.LISTE_DOSSIERS_TRANSMIS_DILA_POUR_JO_BO);
				builder.putStat(EpgRapportStatistique.LISTE_DOSSIERS_TRANSMIS_DILA_POUR_EPR_VEC);
				builder.putStat(EpgRapportStatistique.LISTE_EPREUVES_PAR_VECT_PUBLI);
			}
			builder.setCurrentCategory(SolonEpgStatistiquesConstant.STATISTIQUE_CATEGORY_GLOBAL);
			builder.putStat(EpgRapportStatistique.TEXTES_CORBEILLES_TRAVAILLES);
			builder.putStat(EpgRapportStatistique.NOMBRE_ACTES_TYPE);
			builder.putStat(EpgRapportStatistique.NOMBRE_ACTES_CREES_MINISTERE);
			builder.putStat(EpgRapportStatistique.NOMBRE_ACTES_CREES_DIRECTION);
			builder.putStat(EpgRapportStatistique.NOMBRE_ACTES_CREES_POSTE);
			builder.putStat(EpgRapportStatistique.TAUX_INDEXATION_TEXTE);
			builder.putStat(EpgRapportStatistique.TAUX_TEXTES_RETOUR_SGG);
			builder.putStat(EpgRapportStatistique.TAUX_TEXTES_RETOUR_BUREAU);
			builder.putStat(EpgRapportStatistique.DOSSIER_ARCHIVAGE);

			statistiquesMap = builder.getMap();
		}
		return statistiquesMap;
	}

	/** Builder pour la map des statistiques, afin de rendre le code plus lisible et maintenable **/
	class StatMapBuilder {

		final Map<Integer, StatistiquesItem>	map;
		String									currentCategory;

		public StatMapBuilder() {
			map = new TreeMap<Integer, StatistiquesItem>();
		}

		public void setCurrentCategory(String cat) {
			currentCategory = cat;
		}

		public void putStat(EpgRapportStatistique rapp) {
			int idRapport = rapp.ordinal();
			map.put(idRapport, new StatistiquesItem(idRapport, rapp.bundleKey, rapp.birtReport, currentCategory));
		}

		public Map<Integer, StatistiquesItem> getMap() {
			return map;
		}
	}

	public String afficherResultatStatistique() throws ClientException {

		if (statistiqueCourante != null) {

			EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(statistiqueCourante.getId());
			switch (rapport) {
				case TAUX_INDEXATION_TEXTE:
					if ((getRubrique() == null || "".equals(getRubrique()))
							&& (getMotsCles() == null || "".equals(getMotsCles()))
							&& (getChampsLibre() == null || "".equals(getChampsLibre()))) {
						String message = resourcesAccessor.getMessages().get(
								"espace.suivi.statistique.taux_indexation_texte.erreur");
						facesMessages.add(StatusMessage.Severity.ERROR, message);
						return "view_parametre_taux_indexation_texte";
					}
					break;
				case NOMBRE_ACTES_TYPE:
					setReadyToProcess(false);
					UIDSequencerImpl.getOrCreatePersistenceProvider().run(true, new RunCallback<String>() {
						@Override
						public String runWith(EntityManager entityManager) {

							Query nativeQuery = entityManager.createNativeQuery(getActTypeCountQuery());
							nativeQuery.setParameter("fromDate", parametersMap.get("DATEARRIVEPAPIER_BI_PARAM"));
							nativeQuery.setParameter("toDate", parametersMap.get("DATEARRIVEPAPIER_BS_PARAM"));

							long startQ = System.currentTimeMillis();

							List<?> resultList = nativeQuery.getResultList();
							long endQ = System.currentTimeMillis();
							long resultQ = endQ - startQ;
							LOGGER.info(documentManager, STLogEnumImpl.LOG_INFO_TEC, "Temps d'execution de la requête : " + resultQ);

							if (resultList != null) {
								long startJ = System.currentTimeMillis();
								typeActeToJson(resultList);
								long endJ = System.currentTimeMillis();
								long resultJ = endJ - startJ;
								LOGGER.info(documentManager, STLogEnumImpl.LOG_INFO_TEC, "Temps de préparation du JSON ( " + resultList.size() + " rec) : " + resultJ);
							}
							setReadyToProcess(true);
							return null;
						}
					});

					while (!isReadyToProcess()) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException exc) {
							LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_STAT_FONC, "Afficher Resultat Statistique " + exc.getMessage(), exc);
						}
					}
				default:
					break;
			}
		}
		return "view_resultat_statistiques";
	}

	private void typeActeToJson(List<?> resultList) {

		StringBuilder jsonBuilder = new StringBuilder();

		Map<BigDecimal, Integer> map = new HashMap<BigDecimal, Integer>();

		for (Object item : resultList) {

			BigDecimal key = (BigDecimal) item;
			Integer count = 0;
			if (map.containsKey(key)) {
				count = map.get(key);
			}
			count++;
			map.put(key, count);

		}

		// Map result is ready => now prepare the json
		ActeService acteService = SolonEpgServiceLocator.getActeService();

		StringBuilder itemsBuilder = new StringBuilder();

		for (Map.Entry<BigDecimal, Integer> entry : map.entrySet()) {
			String strCount = entry.getValue().toString();
			String strId = acteService.getActe(entry.getKey().toString()).getId();

			if (itemsBuilder.length() != 0) {
				itemsBuilder.append(",");
			}

			itemsBuilder.append("{");

			// Add ID to the Json
			itemsBuilder.append("\"");
			itemsBuilder.append("id");
			itemsBuilder.append("\"");
			itemsBuilder.append(":");
			itemsBuilder.append("\"");
			itemsBuilder.append(strId);
			itemsBuilder.append("\"");
			itemsBuilder.append(",");

			// Add count to the json
			itemsBuilder.append("\"");
			itemsBuilder.append("count");
			itemsBuilder.append("\"");
			itemsBuilder.append(":");
			itemsBuilder.append("\"");
			itemsBuilder.append(strCount);
			itemsBuilder.append("\"");
			itemsBuilder.append("}");
		}

		jsonBuilder.append("[");
		jsonBuilder.append(itemsBuilder.toString());
		jsonBuilder.append("]");

		parametersMap.put("TYPE_ACTE_COUNT", jsonBuilder.toString());

	}

	private String getActTypeCountQuery() {
		// Prepare Users
		String users = this.dataToParameters(SecurityService.getPrincipalsToCheck(ssPrincipal));

		StringBuilder builder = new StringBuilder();

		builder.append("SELECT ")
				.append(" TO_NUMBER(TYPEACTE) AS TYPEACTE ")
				.append(" from V_STATS_NOMBRE_ACTE_PAR_TYPE v_stats ")
				.append(" INNER JOIN HIERARCHY_READ_ACL read_acl ON v_stats.id = read_acl.id ")
				.append(" WHERE ")
				.append("read_acl.acl_id IN (SELECT COLUMN_VALUE FROM TABLE(nx_get_read_acls_for(NX_STRING_TABLE(")
				.append(users)
				.append(" )))) ")
				.append(" AND ")
				.append(" DATEDOSSIERCREE BETWEEN TO_DATE (:fromDate, 'DD/MM/YYYY HH24:MI') AND TO_DATE (:toDate, 'DD/MM/YYYY HH24:MI') ");

		return builder.toString();

	}

	private String dataToParameters(String[] principals) {
		StringBuilder result = new StringBuilder();
		for (String principal : principals) {
			if (result.length() != 0) {
				result.append(",");
			}
			result.append("'");
			result.append(principal);
			result.append("'");
		}
		return result.toString();
	}

	public String navigateToStatisticNextDestination(Integer StatistiquesItemId) throws ClientException {

		parametersMap = new HashMap<String, String>();

		parametersMap.put("USERS_PARAM", principalStr);
		parametersMap.put("PERMISSIONS_PARAM", permissionsStr);

		setStatistiqueCourante(statistiquesMap.get(StatistiquesItemId));
		EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(StatistiquesItemId);

		return getNextDestination(rapport);
	}

	private String getNextDestination(EpgRapportStatistique rapport) throws ClientException {
		switch (rapport) {
			case LISTE_UTILISATEURS_PAS_CONNECTES_DUREE:
				// Cette page comporte en fait un champ de saisie d'une date
				return "view_parametre_utilisateurs_connectes_duree";
			case NOMBRE_UTILISATEUR_CONNECTES:
			case NOMS_UTILISATEUR_CONNECTES:
			case LISTE_TEXTES_EN_COURS_COMMUNICATION_VALIDATION:
			case LISTE_TEXTES_SIGNATURE_PREMIER_MINISTRE_PRESIDENT:
			case LISTE_TEXTES_AMPLIATIONS:
			case LISTE_TEXTES_CHEMINANT_DEMATERIALISEE:
				return afficherResultatStatistique();
			case LISTE_ORIGINAUX_TEXTES_ARRIVES_SGG:
			case NOMBRE_ACTES_TYPE:
			case TAUX_TEXTES_RETOUR_BUREAU:
				return "view_parametre_choisir_periode";
			case TEXTES_CORBEILLES_TRAVAILLES:
				return "view_parametre_texte_corbeille";
			case NOMBRE_ACTES_CREES_MINISTERE:
				return "view_parametre_nbr_acte_ministere_periode";
			case NOMBRE_ACTES_CREES_DIRECTION:
				return "view_parametre_nbr_acte_direction_periode";
			case NOMBRE_ACTES_CREES_POSTE:
				return "view_parametre_nbr_acte_poste_periode";
			case TAUX_INDEXATION_TEXTE:
				return "view_parametre_taux_indexation_texte";
			case TAUX_TEXTES_RETOUR_SGG:
				return "view_parametre_taux_retour_sgg";
			case LISTE_DOSSIERS_TRANSMIS_DILA_POUR_EPR_VEC:
			case LISTE_DOSSIERS_TRANSMIS_DILA_POUR_JO_BO:
				return "view_parametre_vecteur_date";
			case LISTE_EPREUVES_PAR_VECT_PUBLI:
				return "view_parametre_vecteur_publication";
			case DOSSIER_ARCHIVAGE :
				return "view_statistique_dossier_archivage";
			default:
				return null;
		}
	}

	public String navigateToStatisticMainPageDestination() {
		return SolonEpgViewConstant.ESPACE_SUIVI_STATISTIQUES_VIEW;
	}

	/**
	 * retourner la liste de tous les mailBox poste
	 * 
	 * @return la liste de tous les mailBox poste
	 * @throws ClientException
	 */

	public List<Mailbox> getAllMailboxPoste() throws ClientException {
		final MailboxService mailboxService = STServiceLocator.getMailboxService();
		List<Mailbox> mailBoxList = mailboxService.getAllMailboxPoste(documentManager);
		documentManager.getPermissionsToCheck("");
		return mailBoxList;
	}

	public String getUrlView(String view_name) {
		String base = BaseURL.getBaseURL(getHttpServletRequest());
		return base + "espace_suivi/statistiques/" + view_name + "?conversationId=" + Conversation.instance().getId();

	}

	public void genererStatistiquesHtml() throws Exception {
		setStatReportParams();
		birtReportingActions.generatehtml(getStatistiqueCourante().getRapportBirt(), parametersMap,
				getStatistiqueCourante().getLabel());
	}

	public void genererStatistiquesPdf() throws Exception {
		birtReportingActions.generatePdf(getStatistiqueCourante().getRapportBirt(), parametersMap,
				getStatistiqueCourante().getLabel());
	}

	public void genererStatistiquesExcel() throws Exception {
		birtReportingActions.generateXls(getStatistiqueCourante().getRapportBirt(), parametersMap, "statistique.xls");
	}

	public boolean canGenerateExcel() {
		EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(getStatistiqueCourante().getId());
		switch (rapport) {
			case NOMBRE_ACTES_TYPE:
			case NOMBRE_ACTES_CREES_MINISTERE:
			case NOMBRE_ACTES_CREES_DIRECTION:
			case NOMBRE_ACTES_CREES_POSTE:
				return false;
			default:
				return true;
		}
	}

	public void setStatistiqueCourante(StatistiquesItem statistiqueCourante) {
		this.statistiqueCourante = statistiqueCourante;
	}

	public StatistiquesItem getStatistiqueCourante() {
		return statistiqueCourante;
	}

	public Date getDateDeDebut() {
		EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(getStatistiqueCourante().getId());
		switch (rapport) {
			case LISTE_UTILISATEURS_PAS_CONNECTES_DUREE:
				return parseDateFromString(parametersMap.get("DATE_DEPUIS_PARAM"));
			case LISTE_ORIGINAUX_TEXTES_ARRIVES_SGG:
				return parseDateFromString(parametersMap.get("DATEARRIVEPAPIER_BI_PARAM"));
			case NOMBRE_ACTES_TYPE:
				return parseDateFromString(parametersMap.get("DATEARRIVEPAPIER_BI_PARAM"));
			case TAUX_TEXTES_RETOUR_SGG:
				return parseDateFromString(parametersMap.get("DATERETOUR_BI_PARAM"));
			case TAUX_TEXTES_RETOUR_BUREAU:
				return parseDateFromString(parametersMap.get("DATERETOUR_BI_PARAM"));
			case NOMBRE_ACTES_CREES_MINISTERE:
			case NOMBRE_ACTES_CREES_DIRECTION:
			case NOMBRE_ACTES_CREES_POSTE:
				return parseDateFromString(parametersMap.get("DATEDOSSIERCREE_BI_PARAM"));

			case LISTE_DOSSIERS_TRANSMIS_DILA_POUR_EPR_VEC:
			case LISTE_DOSSIERS_TRANSMIS_DILA_POUR_JO_BO:
				return parseDateFromString(parametersMap.get("DATEDOSSIER_PARAM"));
			default:
				return null;
		}
	}

	public void setDateDeDebut(Date dateDeDebut) {
		if (dateDeDebut != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateDeDebut);
			String dateDeDebutStr = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/"
					+ cal.get(Calendar.YEAR);

			EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(getStatistiqueCourante().getId());

			switch (rapport) {
				case LISTE_UTILISATEURS_PAS_CONNECTES_DUREE:
					parametersMap.put("DATE_DEPUIS_PARAM", dateDeDebutStr);
					break;
				case LISTE_ORIGINAUX_TEXTES_ARRIVES_SGG:
					parametersMap.put("DATEARRIVEPAPIER_BI_PARAM", dateDeDebutStr);
					break;
				case NOMBRE_ACTES_TYPE:
					parametersMap.put("DATEARRIVEPAPIER_BI_PARAM", dateDeDebutStr);
					break;
				case TAUX_TEXTES_RETOUR_SGG:
					parametersMap.put("DATERETOUR_BI_PARAM", dateDeDebutStr);
					break;
				case TAUX_TEXTES_RETOUR_BUREAU:
					parametersMap.put("DATERETOUR_BI_PARAM", dateDeDebutStr);
					break;
				case NOMBRE_ACTES_CREES_MINISTERE:
				case NOMBRE_ACTES_CREES_DIRECTION:
				case NOMBRE_ACTES_CREES_POSTE:
					parametersMap.put("DATEDOSSIERCREE_BI_PARAM", dateDeDebutStr);
					break;
				case LISTE_DOSSIERS_TRANSMIS_DILA_POUR_EPR_VEC:
				case LISTE_DOSSIERS_TRANSMIS_DILA_POUR_JO_BO:
					SimpleDateFormat formatter = DateUtil.simpleDateFormat("dd/MM/yyyy");

					parametersMap.put("DATEDOSSIER_PARAM", formatter.format(dateDeDebut));
					break;
				default:
					break;
			}
		}
	}

	public Date getDateDeFin() {
		EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(getStatistiqueCourante().getId());

		switch (rapport) {
			case LISTE_ORIGINAUX_TEXTES_ARRIVES_SGG:
				return parseDateFromString(parametersMap.get("DATEARRIVEPAPIER_BS_PARAM"));
			case NOMBRE_ACTES_TYPE:
				return parseDateFromString(parametersMap.get("DATEARRIVEPAPIER_BS_PARAM"));	
			case TAUX_TEXTES_RETOUR_SGG:
				return parseDateFromString(parametersMap.get("DATERETOUR_BS_PARAM"));
			case TAUX_TEXTES_RETOUR_BUREAU:
				return parseDateFromString(parametersMap.get("DATERETOUR_BS_PARAM"));
			case NOMBRE_ACTES_CREES_MINISTERE:
			case NOMBRE_ACTES_CREES_DIRECTION:
			case NOMBRE_ACTES_CREES_POSTE:
				return parseDateFromString(parametersMap.get("DATEDOSSIERCREE_BS_PARAM"));
			default:
				return null;
		}
	}

	public void setDateDeFin(Date dateDeFin) {
		if (dateDeFin != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateDeFin);
			String dateDeFinStr = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/"
					+ cal.get(Calendar.YEAR);

			EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(getStatistiqueCourante().getId());

			switch (rapport) {
				case LISTE_ORIGINAUX_TEXTES_ARRIVES_SGG:
					parametersMap.put("DATEARRIVEPAPIER_BS_PARAM", dateDeFinStr);
					break;
				case NOMBRE_ACTES_TYPE:
					parametersMap.put("DATEARRIVEPAPIER_BS_PARAM", dateDeFinStr);
					break;
				case TAUX_TEXTES_RETOUR_SGG:
					parametersMap.put("DATERETOUR_BS_PARAM", dateDeFinStr);
					break;
				case TAUX_TEXTES_RETOUR_BUREAU:
					parametersMap.put("DATERETOUR_BS_PARAM", dateDeFinStr);
					break;
				case NOMBRE_ACTES_CREES_MINISTERE:
				case NOMBRE_ACTES_CREES_DIRECTION:
				case NOMBRE_ACTES_CREES_POSTE:
					parametersMap.put("DATEDOSSIERCREE_BS_PARAM", dateDeFinStr);
				default:
					break;
			}
		}
	}

	public String getMinistereId() {
		if (getStatistiqueCourante().getId() == EpgRapportStatistique.TAUX_TEXTES_RETOUR_SGG.ordinal()) {
			return parametersMap.get("MINISTEREATTACHE");
		} else {
			return parametersMap.get("MINISTERERESP_PARAM");
		}
	}

	public void setMinistereId(String ministereId) throws ClientException {
		if (ministereId.trim().length() != 0) {
			EntiteNode ministereNode = STServiceLocator.getSTMinisteresService().getEntiteNode(ministereId);
			if (getStatistiqueCourante().getId() == EpgRapportStatistique.TAUX_TEXTES_RETOUR_SGG.ordinal()) {

				parametersMap.put("MINISTEREATTACHE_PARAM", ministereId);
				parametersMap.put("MINISTEREATTACHELABEL_PARAM", ministereNode.getLabel());
			} else {
				parametersMap.put("MINISTERERESP_PARAM", ministereId);
				parametersMap.put("MINISTERERESPLABEL_PARAM", ministereNode.getLabel());
			}
		} else {
			if (parametersMap.containsKey("MINISTERERESP_PARAM")) {
				parametersMap.remove("MINISTERERESP_PARAM");
				parametersMap.remove("MINISTERERESPLABEL_PARAM");
			} else if (parametersMap.containsKey("MINISTEREATTACHE_PARAM")) {
				parametersMap.remove("MINISTEREATTACHE_PARAM");
				parametersMap.remove("MINISTEREATTACHELABEL_PARAM");
			}
		}
	}

	public String getDirectionId() {
		return parametersMap.get("DIRECTIONRESP_PARAM");
	}

	public void setDirectionId(String directionId) throws ClientException {
		if (directionId.trim().length() != 0) {
			parametersMap.put("DIRECTIONRESP_PARAM", directionId);
			OrganigrammeNode orgaNode = STServiceLocator.getSTUsAndDirectionService().getUniteStructurelleNode(
					directionId);
			parametersMap.put("DIRECTIONRESPLABEL_PARAM", orgaNode.getLabel());
		} else {
			if (parametersMap.containsKey("DIRECTIONRESP_PARAM")) {
				parametersMap.remove("DIRECTIONRESP_PARAM");
				parametersMap.remove("DIRECTIONRESPLABEL_PARAM");
			}
		}
	}

	public String getVecteurPublication() {
		return parametersMap.get("VECTEURPUBLICATION_PARAM");
	}

	public void setVecteurPublication(String vecteurPublication) {
		parametersMap.put("VECTEURPUBLICATION_PARAM", vecteurPublication);
	}

	public String getPeriodeType() {
		String result = "M";
		if (parametersMap.containsKey("TYPEPERIODE_PARAM")) {
			result = parametersMap.get("TYPEPERIODE_PARAM");
		}

		return result;
	}

	public void setPeriodeType(String periodeType) {
		parametersMap.put("TYPEPERIODE_PARAM", periodeType);
	}

	public String getPeriodeValue() {
		String result = "1";
		if (parametersMap.containsKey("VALUEPERIODE_PARAM")) {
			result = parametersMap.get("VALUEPERIODE_PARAM");
		}

		return result;
	}

	public void setPeriodeValue(String periodeValue) {
		int periodeValueInt = 0;
		try {
			periodeValueInt = Integer.parseInt(periodeValue.trim());
		} catch (Exception exc) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_PARSE_FONC, periodeValue, exc);
		}

		if (periodeValueInt > 0) {
			parametersMap.put("VALUEPERIODE_PARAM", String.valueOf(periodeValueInt));
		}
	}

	public String getPosteId() {
		EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(getStatistiqueCourante().getId());
		switch (rapport) {
			case TEXTES_CORBEILLES_TRAVAILLES:
				if (parametersMap.containsKey("DISTRIBUTIONMAILBOXID_PARAM")) {
					return parametersMap.get("DISTRIBUTIONMAILBOXID_PARAM").substring(6);
				}
				break;
			case NOMBRE_ACTES_CREES_POSTE:
				if (parametersMap.containsKey("POSTECREATOR_PARAM")) {
					return parametersMap.get("POSTECREATOR_PARAM").substring(6);
				}
			default:
				break;
		}

		return null;
	}

	public void setPosteId(String posteId) throws ClientException {
		EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(getStatistiqueCourante().getId());
		if (posteId.trim().length() != 0) {
			OrganigrammeNode orgaNode = STServiceLocator.getSTPostesService().getPoste(posteId);
			switch (rapport) {
				case TEXTES_CORBEILLES_TRAVAILLES:
					parametersMap.put("DISTRIBUTIONMAILBOXID_PARAM", STConstant.PREFIX_POSTE + posteId);
					parametersMap.put("DISTRIBUTIONMAILBOXLABEL_PARAM", orgaNode.getLabel());
					break;
				case NOMBRE_ACTES_CREES_POSTE:
					parametersMap.put("POSTECREATOR_PARAM", STConstant.PREFIX_POSTE + posteId);
					parametersMap.put("POSTECREATORLABEL_PARAM", orgaNode.getLabel());
				default:
					break;
			}
		} else {
			switch (rapport) {
				case TEXTES_CORBEILLES_TRAVAILLES:
					if (parametersMap.containsKey("DISTRIBUTIONMAILBOXID_PARAM")) {
						parametersMap.remove("DISTRIBUTIONMAILBOXID_PARAM");
						parametersMap.remove("DISTRIBUTIONMAILBOXLABEL_PARAM");
					}
					break;
				case NOMBRE_ACTES_CREES_POSTE:
					if (parametersMap.containsKey("POSTECREATOR_PARAM")) {
						parametersMap.remove("POSTECREATOR_PARAM");
						parametersMap.remove("POSTECREATORLABEL_PARAM");
					}
				default:
					break;
			}
		}
	}

	public String getRubrique() {
		String result = null;
		if (getStatistiqueCourante().getId() == EpgRapportStatistique.TAUX_INDEXATION_TEXTE.ordinal()) {
			result = parametersMap.get("RUBRIQUES_PARAM");
		}

		return result;
	}

	public void setRubrique(String rubrique) {
		if (getStatistiqueCourante().getId() == EpgRapportStatistique.TAUX_INDEXATION_TEXTE.ordinal()) {
			if (rubrique.trim().length() > 0) {
				parametersMap.put("RUBRIQUES_PARAM", rubrique.trim());
			}
		}
	}

	public String getMotsCles() {
		String result = null;
		if (getStatistiqueCourante().getId() == EpgRapportStatistique.TAUX_INDEXATION_TEXTE.ordinal()) {
			result = parametersMap.get("MOTSCLES_PARAM");
		}

		return result;
	}

	public void setMotsCles(String motsCles) {
		if (getStatistiqueCourante().getId() == EpgRapportStatistique.TAUX_INDEXATION_TEXTE.ordinal()) {
			if (motsCles.trim().length() > 0) {
				parametersMap.put("MOTSCLES_PARAM", motsCles.trim());
			}
		}
	}

	public String getChampsLibre() {
		String result = null;
		if (getStatistiqueCourante().getId() == EpgRapportStatistique.TAUX_INDEXATION_TEXTE.ordinal()) {

			result = parametersMap.get("LIBRE_PARAM");
		}

		return result;
	}

	public void setChampsLibre(String champsLibre) {
		if (getStatistiqueCourante().getId() == EpgRapportStatistique.TAUX_INDEXATION_TEXTE.ordinal()) {
			if (champsLibre.trim().length() > 0) {
				parametersMap.put("LIBRE_PARAM", champsLibre.trim());
			}
		}
	}

	private void setStatReportParams() throws ClientException {

		EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(getStatistiqueCourante().getId());
		String listPosteBdc;
		Date dateSelectionee = null;
		if (parametersMap.containsKey("DATE_DEPUIS_PARAM")) {
			dateSelectionee = parseDateFromString(parametersMap.get("DATE_DEPUIS_PARAM"));
		}
		dateSelectionee = dateSelectionee == null ? new Date() : dateSelectionee;
		switch (rapport) {
			case LISTE_UTILISATEURS_PAS_CONNECTES_DUREE:
				String usersParam = this.getOldUsersList(dateSelectionee);
				parametersMap.put("NOMSUTILISATEURS_PARAM", usersParam);
				break;
			case TAUX_TEXTES_RETOUR_SGG:
				listPosteBdc = getListPosteBdc();
				if (listPosteBdc.trim().length() > 0) {
					parametersMap.put("DISTRIBUTIONMAILBOXID_PARAM", listPosteBdc);
				}
				break;
			case TAUX_TEXTES_RETOUR_BUREAU:
				listPosteBdc = getListPosteBdc();
				if (listPosteBdc.trim().length() > 0) {
					parametersMap.put("DISTRIBUTIONMAILBOXID_PARAM", listPosteBdc);
				}
				break;
			case LISTE_TEXTES_EN_COURS_COMMUNICATION_VALIDATION:
			case LISTE_TEXTES_CHEMINANT_DEMATERIALISEE:
				String ministeresParam = SolonEpgServiceLocator.getStatsGenerationResultatService()
						.getMinisteresListBirtReportParam(documentManager);
				if (ministeresParam.trim().length() > 0) {
					parametersMap.put("MINISTERES_PARAM", ministeresParam);
				}
			default:
				break;
		}
	}

	private static HttpServletRequest getHttpServletRequest() {
		ServletRequest request = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			request = (ServletRequest) facesContext.getExternalContext().getRequest();
		}

		if (request != null && request instanceof HttpServletRequest) {
			return (HttpServletRequest) request;
		}
		return null;
	}

	/*
	 * get la liste des users Bdc
	 * @return
	 */
	private String getListPosteBdc() throws ClientException {
		StringBuilder result = new StringBuilder();
		List<PosteNode> bdcPosteList;
		bdcPosteList = STServiceLocator.getSTPostesService().getPosteBdcNodeList();
		// Récupération de la liste des poste bdc
		for (OrganigrammeNode node : bdcPosteList) {
			result.append(SSConstant.MAILBOX_POSTE_ID_PREFIX).append(node.getId()).append("&");
		}
		return result.toString();
	}

	/**
	 * Retourne une chaine de caractère au format JSON pour la statistique des utilisateurs ne s'étant pas connecté depuis une certaines durée.
	 * @param usersList
	 * @return
	 */
	private String usersToJSON(DocumentModelList usersList) {
		String firstName = "";
		String lastName = "";
		String userName = "";
		String courriel = "";
		String telephone = "";
		String dateCreation = "";
		String dateDerniereConnexion = "";
		String poste = "";
		String ministere = "";
		String direction = "";

		STUserService stUserService = STServiceLocator.getSTUserService();
		SolonEpgUserManager userManger = (SolonEpgUserManager) STServiceLocator.getUserManager();

		StringBuilder usersBuilder = new StringBuilder("[");
		if (usersList != null) {
			boolean ministereFilter = hasRightAdminMinStat() && !canViewAdminStat();
			Set<String> userMinisteresIds = ssPrincipal.getMinistereIdSet();
			for (DocumentModel user : usersList) {
				if (ministereFilter && !checkSameMinistere(stUserService, user, userMinisteresIds)) {
					// Si on doit filtrer sur le ministere, et que les deux utilisateurs ne sont pas du même ministere,
					// On passe à la prochaine valeur du for
					continue;
				}
				
				STUser sTUser = user.getAdapter(STUser.class);
				firstName = sTUser.getFirstName();
				lastName = sTUser.getLastName();
				userName = sTUser.getUsername();
				courriel = sTUser.getEmail() == null ? "" : sTUser.getEmail();
				telephone = sTUser.getTelephoneNumber() == null ? "" : sTUser.getTelephoneNumber();
				dateCreation = sTUser.getDateDebut() == null ? "" : DateUtil.formatWithHour(sTUser.getDateDebut()
						.getTime());
				dateDerniereConnexion = sTUser.getDateLastConnection() == null ? "" : DateUtil.formatWithHour(sTUser
						.getDateLastConnection().getTime());
				try {
					ministere = stUserService.getUserMinisteres(user.getId()) == null ? "" : stUserService
							.getUserMinisteres(user.getId());
				} catch (ClientException e) {
					ministere = "";
				}

				try {
					direction = stUserService.getAllDirectionsRattachement(user.getId()) == null ? "" : stUserService
							.getAllDirectionsRattachement(user.getId()).replace("\"", "\\\"");
				} catch (ClientException e) {
					direction = "";
				}

				try {
					poste = stUserService.getUserProfils(user.getId()) == null ? "" : stUserService.getUserProfils(
							user.getId()).replace("\"", "\\\"");
				} catch (ClientException e) {
					poste = "";
				}

				if (userManger.getAdministratorIds() == null || !userManger.getAdministratorIds().contains(userName)) {

					if (firstName == null) {
						firstName = "";
					}
					if (lastName == null) {
						lastName = "";
					}
					if (usersBuilder.length() != 1) {
						usersBuilder.append(",");
					}

					boolean skip = userName.isEmpty() && firstName.isEmpty() && lastName.isEmpty();
					if (!skip) {
						usersBuilder.append("{\"userName\" : \"");
						usersBuilder.append(userName);
						usersBuilder.append("\", \"firstName\":\"");
						usersBuilder.append(firstName);
						usersBuilder.append("\",\"lastName\":\"");
						usersBuilder.append(lastName);
						usersBuilder.append("\",\"courriel\" : \"");
						usersBuilder.append(courriel);
						usersBuilder.append("\",\"telephone\" : \"");
						usersBuilder.append(telephone);
						usersBuilder.append("\",\"dateCreation\" : \"");
						usersBuilder.append(dateCreation);
						usersBuilder.append("\",\"dateDerniereConnexion\" : \"");
						usersBuilder.append(dateDerniereConnexion);
						usersBuilder.append("\",\"poste\" : \"");
						usersBuilder.append(poste);
						usersBuilder.append("\",\"ministere\" : \"");
						usersBuilder.append(ministere);
						usersBuilder.append("\",\"direction\" : \"");
						usersBuilder.append(direction);
						usersBuilder.append("\"}");
					}
				}
			}
		}
		usersBuilder.append("]");
		return usersBuilder.toString();
	}
	
	/**
	 * Retourne vrai si l'un des ministeres de user est contenu dans ministereIdsTarget
	 * @param stUserService
	 * @param user
	 * @param ministereIdsTarget
	 * @return
	 */
	private boolean checkSameMinistere(STUserService stUserService, DocumentModel user, Set<String> ministereIdsTarget) {
		List<String> ministereIds = null;
		try {
			ministereIds = stUserService.getAllUserMinisteresId(user.getId());
		} catch (ClientException exc) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_PROPERTY_TEC, user.getId(), exc);
			return false;
		}
		boolean match = false;
		for (String id : ministereIds) {
			match = ministereIdsTarget.contains(id);
			if (match) {
				break;
			}
		}			
		return match;
	}

	private String getformatedUsersList(DocumentModelList usersList, Date dateDeConnexion) throws ClientException {

		String result = "";
		String userName = "";
		if (dateDeConnexion != null) {
			final SSUtilisateurConnectionMonitorService utilisateurConnectionMonitorService = SSServiceLocator
					.getUtilisateurConnectionMonitorService();
			Map<String, DocumentModel> ldapUsersMap = new HashMap<String, DocumentModel>();

			if (usersList != null) {
				for (DocumentModel user : usersList) {
					userName = user.getAdapter(STUser.class).getUsername();
					ldapUsersMap.put(userName, user);
				}
				usersList.clear();

				List<String> infoUtilisateurList = utilisateurConnectionMonitorService
						.getListInfoUtilisateurConnection(documentManager, dateDeConnexion);
				for (String un : infoUtilisateurList) {
					if (ldapUsersMap.containsKey(un)) {
						ldapUsersMap.remove(un);
					}
				}
				usersList.addAll(ldapUsersMap.values());
				result = usersToJSON(usersList);
			}
		}
		return result;
	}

	public Map<String, String> getListeDesMinisteres() throws ClientException {
		Map<String, String> listeDesMinisteres = new HashMap<String, String>();
		List<EntiteNode> ministeresList = null;

		if (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_FONCTIONNEL_STATISTIQUES)) {
			ministeresList = STServiceLocator.getSTMinisteresService().getCurrentMinisteres();
		} else {
			Set<String> ministereSet = ssPrincipal.getMinistereIdSet();
			ministeresList = STServiceLocator.getSTMinisteresService().getEntiteNodes(ministereSet);
		}
		for (OrganigrammeNode ministereNode : ministeresList) {
			listeDesMinisteres.put(ministereNode.getId(), ministereNode.getLabel());
		}

		return listeDesMinisteres;
	}

	public boolean canViewAdminStat() {
		return ssPrincipal.isAdministrator()
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_FONCTIONNEL_STATISTIQUES);
	}
	
	public boolean hasRightAdminMinStat() {
		return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_MINISTERIEL_STATISTIQUES);
	}

	public boolean canViewSggStat() {
		return ssPrincipal.isAdministrator()
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_FONCTIONNEL_STATISTIQUES)
				|| ssPrincipal.isMemberOf(STBaseFunctionConstant.PROFIL_SGG);
	}

	public Map<Integer, StatistiquesItem> getStatistquesMap(String type) {
		Map<Integer, StatistiquesItem> map = new TreeMap<Integer, StatistiquesItem>();
		for (Map.Entry<Integer, StatistiquesItem> entry : this.getStatistquesMap().entrySet()) {
			StatistiquesItem statistiquesItem = entry.getValue();
			if (statistiquesItem.getCategory().equals(type)) {
				map.put(entry.getKey(), statistiquesItem);
			}
		}

		return map;

	}

	/**
	 * Parse String en Date Cette String doit avoir le format jj/mm/aaaa
	 * 
	 * @param dateStr
	 * @return null if error
	 */
	private Date parseDateFromString(String dateStr) {
		SimpleDateFormat formatter = DateUtil.simpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date dateValue = null;
		if (dateStr != null) {
			try {
				dateValue = formatter.parse(dateStr + " 00:00:00");
			} catch (Exception exc) {
				LOGGER.warn(documentManager, STLogEnumImpl.FAIL_CONVERT_DATE_TEC, "Erreur lors du formattage de la date " + dateStr, exc);
			}
		}
		return dateValue;
	}

	public List<String> getVecteurPublicationList() throws ClientException {
		final List<String> vecteurs = new ArrayList<String>();

		final BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
		final List<DocumentModel> allBulletinOrdered = bulletinOfficielService
				.getAllActiveBulletinOfficielOrdered(documentManager);
		List<DocumentModel> lstVecteurs = bulletinOfficielService.getAllActifVecteurPublication(documentManager);

		// récupère la liste des vecteurs de publication
		for (DocumentModel doc : lstVecteurs) {
			VecteurPublication vect = doc.getAdapter(VecteurPublication.class);
			vecteurs.add(vect.getIntitule());
		}

		for (final DocumentModel docModel : allBulletinOrdered) {
			vecteurs.add(docModel.getAdapter(BulletinOfficiel.class).getIntitule());
		}
		return vecteurs;
	}

	public synchronized boolean isReadyToProcess() {
		return readyToProcess;
	}

	public synchronized void setReadyToProcess(boolean readyToProcess) {
		this.readyToProcess = readyToProcess;
	}

	private String getOldUsersList(Date lastDateConnection) throws ClientException {
		String result = "";
		DocumentModelList usersList = SolonEpgServiceLocator.getStatistiquesService().getUsers(documentManager,
				lastDateConnection);
		result = getformatedUsersList(usersList, lastDateConnection);
		return result;
	}
	
	public String showNor(String numeroNor) {

		DocumentModel currentNor;
		try {
			currentNor = SolonEpgServiceLocator.getNORService().getDossierFromNOR(documentManager, numeroNor);
			navigationContext.setCurrentDocument(currentNor);
			routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_SUIVI_STATISTIQUES_VIEW_DOSSIER_ARCHIVE);
		} catch (ClientException e) {
			// TODO si on n'arrive pas à récupérer le dossier, afficher un message
			LOGGER.info(STLogEnumImpl.FAIL_GET_DOCUMENT_TEC);
		}
		return SolonEpgViewConstant.ESPACE_SUIVI_STATISTIQUES_VIEW_DOSSIER_ARCHIVE;
	}
	
	public String getLibelleStatutArchivage(Integer statutId) {
		final Map<String, String> libelleStatutArchivage = VocabularyConstants.LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID;
		return libelleStatutArchivage.get(statutId.toString());
	}
	
	public String afficherResultatStatistiqueArchivage(String contentViewName) throws ClientException {
		
		contentViewActions.resetPageProvider(contentViewName);
		return contentViewName;
	}
	
	public void exportListResultats(DossierArchivageStatPageProvider pageProvider) {
		
		try {
			List<DossierArchivageStatDTO> listDossiersArchives = pageProvider.getListFullResultDossierDTO();
			String recipient = ssPrincipal.getEmail();
			if (StringUtil.isBlank(recipient)) {
				facesMessages.add(StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get("feedback.solonepg.no.mail"));
			} else {
				// Post commit event
				EventProducer eventProducer = STServiceLocator.getEventProducer();
				Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
				eventProperties.put(SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_LIST_DOS_PROPERTY,
						(Serializable) listDossiersArchives);
				eventProperties.put(SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_RECIPIENT_PROPERTY, recipient);
				eventProperties.put(SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_STARTDATE_PROPERTY, dateDebutArchivage);
				eventProperties.put(SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_ENDDATE_PROPERTY, dateFinArchivage);

				InlineEventContext eventContext = new InlineEventContext(documentManager, ssPrincipal, eventProperties);
				eventProducer.fireEvent(eventContext.newEvent(SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_EVENT));

				facesMessages.add(StatusMessage.Severity.INFO,
						resourcesAccessor.getMessages().get("feedback.solonepg.export"));
			}
		} catch (ClientException e) {
			facesMessages.add(StatusMessage.Severity.ERROR,
					resourcesAccessor.getMessages().get("Erreur dans la génération de l'export"));
		}
	}

	public Date getDateDebutArchivage() {
		return dateDebutArchivage;
	}

	public void setDateDebutArchivage(Date dateDebutArchivage) {
		this.dateDebutArchivage = dateDebutArchivage;
	}

	public Date getDateFinArchivage() {
		return dateFinArchivage;
	}

	public void setDateFinArchivage(Date dateFinArchivage) {
		this.dateFinArchivage = dateFinArchivage;
	}

	public String getStatutArchivage() {
		return statutArchivage;
	}

	public void setStatutArchivage(String statutArchivage) {
		this.statutArchivage = statutArchivage;
	}

}
