package fr.dila.solonepg.web.activitenormative;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.json.JSONObject;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.ui.web.util.BaseURL;
import org.nuxeo.ecm.webapp.action.WebActionsBean;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.activitenormative.ANReport;
import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.activitenormative.ArchiAnReportEnum;
import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.birt.BirtRefreshFichier;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.ActiviteNormativeStatsService;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.client.PANExportParametreDTO;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean Seam de l'espace d'activite normative
 * 
 * @author arammal
 */
@Name("activiteNormativeStatsActions")
@Scope(ScopeType.CONVERSATION)
public class ActiviteNormativeStatsActionsBean implements Serializable {

	private static final long							serialVersionUID				= 2776349205588506388L;

	private static final STLogger						LOGGER							= STLogFactory
																								.getLog(ActiviteNormativeStatsActionsBean.class);

	@In(create = true, required = false)
	protected transient WebActionsBean					webActions;

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = false)
	protected transient TexteMaitreActionsBean			texteMaitreActions;

	@In(create = true, required = false)
	protected transient ActiviteNormativeActionsBean	activiteNormativeActions;

	@In(create = true, required = false)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(required = true, create = true)
	protected SSPrincipal								ssPrincipal;

	private List<Action>								subTabsActionsList;

	private Action										currentSubTabAction;

	private ANReport									currentReport;

	private boolean										dipslayMesuresApplicationAll	= false;

	private Blob										htmlContent;

	private Blob										pdfContent;

	private Blob										xlsContent;
	
	private String										paramList;

	private ParametrageAN								parameterStatsDoc;

	protected PANExportParametreDTO						statDefaultValue;

	private boolean										periodeParMois;

	private int											mois;

	private int											annee;

	private String										legislatureSelectionnee			= null;

	/**
	 * initialiser le contenu du resulat du rapport
	 * 
	 * @throws ClientException
	 */
	public void initReportResultatData() throws ClientException {
		final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
		if (anStatsService.existBirtRefreshForCurrentUser(documentManager, currentReport)) {
			DocumentModel birtRefreshFichier = anStatsService.getBirtRefreshDocForCurrentUser(documentManager,
					currentReport);
			if (birtRefreshFichier == null) {
				htmlContent = null;
				pdfContent = null;
				xlsContent = null;
				paramList = null;
			} else {
				BirtRefreshFichier birtRefresh = birtRefreshFichier.getAdapter(BirtRefreshFichier.class);
				htmlContent = birtRefresh.getHtmlContent();
				pdfContent = birtRefresh.getPdfContent();
				xlsContent = birtRefresh.getXlsContent();
				paramList = birtRefresh.getParamList();
			}
		} else {
			DocumentModel birtResultatFichier = SolonEpgServiceLocator.getStatsGenerationResultatService()
					.getBirtResultatFichier(documentManager, currentReport.getName());
			if (birtResultatFichier == null) {
				htmlContent = null;
				pdfContent = null;
				xlsContent = null;
				paramList = null;
			} else {
				htmlContent = (Blob) birtResultatFichier.getProperty(
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PROPERTY);
				pdfContent = (Blob) birtResultatFichier.getProperty(
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PDF_PROPERTY);
				xlsContent = (Blob) birtResultatFichier.getProperty(
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
						ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_CSV_PROPERTY);
			}
		}

		if (parameterStatsDoc == null) {
			getParameterStatsDoc();
		} else {
			refreshStatDefaultValue();
		}
	}

	public String getUrlView(String view_name) {
		String base = BaseURL.getBaseURL(getHttpServletRequest());
		return base + "espace_activite_normative/subview/" + view_name + "?conversationId="
				+ Conversation.instance().getId();
	}

	/**
	 * 
	 * @throws ClientException
	 */
	public void resetCurrentSubTabAction() throws ClientException {
		if (activiteNormativeActions.getCurrentTabAction().getId().equals("TAB_AN_TABLEAU_BORD")) {
			subTabsActionsList = webActions.getActionsList("SUBTAB_AN_TABLEAU_BORD");
		} else if (activiteNormativeActions.getCurrentTabAction().getId().equals("TAB_AN_STATISTIQUES")
				|| activiteNormativeActions.getCurrentTabAction().getId().equals("TAB_AN_INDICATEUR_LOLF")
				|| "TAB_AN_INDICATEUR_ORDONNANCES".equals(activiteNormativeActions.getCurrentTabAction().getId())) {
			subTabsActionsList = webActions.getActionsList("SUBTAB_AN_STATISTIQUES");
		} else if (activiteNormativeActions.getCurrentTabAction().getId().equals("TAB_AN_BILAN_SEMESTRIEL")) {
			subTabsActionsList = webActions.getActionsList("SUBTAB_AN_BILAN_SEMESTRIELS");
		} else if (activiteNormativeActions.getCurrentTabAction().getId()
				.equals("TAB_AN_BILAN_SEMESTRIEL_APP_ORDONNANCES")) {
			subTabsActionsList = webActions.getActionsList("SUBTAB_AN_BILAN_SEMESTRIELS");
		} else if (activiteNormativeActions.getCurrentTabAction().getId().equals("TAB_AN_TAUX_APPLICATION")
				|| activiteNormativeActions.getCurrentTabAction().getId()
						.equals("TAB_AN_TAUX_APPLICATION_APP_ORDONNANCES")) {
			subTabsActionsList = webActions.getActionsList("SUBTAB_AN_FIL_EAU");
		} else if (activiteNormativeActions.getCurrentTabAction().getId().equals("TAB_AN_DELAIS_MOYEN")
				|| activiteNormativeActions.getCurrentTabAction().getId().equals("TAB_AN_DELAIS_MOYEN_APP_ORDONNANCES")) {
			subTabsActionsList = webActions.getActionsList("SUBTAB_AN_BILAN_DELAI");
		} else if (activiteNormativeActions.getCurrentTabAction().getId().equals("TAB_AN_COURBES")
				|| activiteNormativeActions.getCurrentTabAction().getId().equals("TAB_AN_COURBE_APP_ORDONNANCES")) {
			subTabsActionsList = webActions.getActionsList("SUBTAB_AN_PUBLICATION_COURBE");
			Calendar cal = Calendar.getInstance();
			setPeriodeParMois(true);
			setMois(cal.get(Calendar.MONTH) + 1);
			setAnnee(cal.get(Calendar.YEAR));
		}

		if (subTabsActionsList == null || subTabsActionsList.isEmpty()) {
			this.currentSubTabAction = null;
		} else {
			this.setCurrentSubTabAction(subTabsActionsList.get(0));
		}
	}

	/**
	 * sauvegarder le resultat du rapport birt
	 * 
	 * @throws Exception
	 */
	public void saveResultat() throws Exception {
		SolonEpgServiceLocator.getStatsGenerationResultatService().saveReportResulat(documentManager,
				currentReport.getName(), htmlContent, pdfContent, xlsContent);
	}

	/**
	 * publier le resultat du rapport birt
	 * 
	 * @throws Exception
	 */
	public void publierResultat() throws Exception {
		saveResultat();
		if (currentReport.getType().getId().equals(ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN.getId())) {
			Map<String, String> inputValues = new HashMap<String, String>();
			inputValues.put("DIRECTIONS_PARAM", SolonEpgServiceLocator.getStatsGenerationResultatService()
					.getDirectionsListBirtReportParam());
			inputValues.put("MINISTEREPILOTE_PARAM", texteMaitreActions.getCurrentMinistere().getId());
			inputValues.put("MINISTEREPILOTELABEL_PARAM", texteMaitreActions.getCurrentMinistere().getLabel());
			inputValues.put("LEGISLATURE_PARAM", parseDateToString(statDefaultValue.getDateDebutLegislature()));
			String legisValue = ("".equals(legislatureSelectionnee) ? " " : legislatureSelectionnee);

			inputValues.put("LEGISLATURES", "'" + legisValue + "'");
			inputValues.put("LEGISLATURES_LABEL", buildLegislatureParamLabel());
			String reportName = ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN.getName() + "-"
					+ texteMaitreActions.getCurrentMinistere().getId();
			Blob tempHtmlContent = SolonEpgServiceLocator.getStatsGenerationResultatService().generateReportResult(
					reportName, ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN.getFile(), inputValues, "html");
			SolonEpgServiceLocator.getStatsGenerationResultatService().publierReportResulat(reportName,
					tempHtmlContent, true, documentManager);
		} else if (currentReport.getType().getId().equals(ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_APP_ORDO.getId())) {
			Map<String, String> inputValues = new HashMap<String, String>();
			inputValues.put("DIRECTIONS_PARAM", SolonEpgServiceLocator.getStatsGenerationResultatService()
					.getDirectionsListBirtReportParam());
			inputValues.put("MINISTEREPILOTE_PARAM", texteMaitreActions.getCurrentMinistere().getId());
			inputValues.put("MINISTEREPILOTELABEL_PARAM", texteMaitreActions.getCurrentMinistere().getLabel());
			inputValues.put("LEGISLATURE_PARAM", parseDateToString(statDefaultValue.getDateDebutLegislature()));
			String legisValue = ("".equals(legislatureSelectionnee) ? " " : legislatureSelectionnee);

			inputValues.put("LEGISLATURES", "'" + legisValue + "'");
			inputValues.put("LEGISLATURES_LABEL", buildLegislatureParamLabel());
			String reportName = ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_APP_ORDO.getName() + "-"
					+ texteMaitreActions.getCurrentMinistere().getId();
			Blob tempHtmlContent = SolonEpgServiceLocator.getStatsGenerationResultatService().generateReportResult(
					reportName, ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_APP_ORDO.getFile(), inputValues, "html");
			SolonEpgServiceLocator.getStatsGenerationResultatService().publierReportResulat(reportName,
					tempHtmlContent, true, documentManager);

		} else {
			SolonEpgServiceLocator.getStatsGenerationResultatService().publierReportResulat(currentReport.getName(),
					htmlContent, true, documentManager);
			
			ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();
			
			if (activiteNormativeService.isPublicationBilanSemestrielsBdjActivated(documentManager) && paramList!=null) {
				JSONObject paramListStr = new JSONObject(paramList);
				Date debutIntervalle1 = DateUtil.parse((String)paramListStr.get("DEBUT_INTERVALLE1_PARAM")).getTime();
				Date finIntervalle1 = DateUtil.parse((String)paramListStr.get("FIN_INTERVALLE1_PARAM")).getTime();
				Date debutIntervalle2 = DateUtil.parse((String)paramListStr.get("DEBUT_INTERVALLE2_PARAM")).getTime();
				Date finIntervalle2 = DateUtil.parse((String)paramListStr.get("FIN_INTERVALLE2_PARAM")).getTime();
				
				if (currentReport.getType().getId().equals(ANReportEnum.TAB_AN_BILAN_SEM_LOI_TOUS.getId())) {
					activiteNormativeService.publierBilanSemestrielLoiBDJ(documentManager, 
							parameterStatsDoc.getLegislatureEnCours(), debutIntervalle1, 
							finIntervalle1, debutIntervalle2, finIntervalle2);
					
				} else if (currentReport.getType().getId().equals(ANReportEnum.TAB_AN_BILAN_SEM_ORDONNANCE_TOUS.getId())) {
					activiteNormativeService.publierBilanSemestrielOrdonnanceBDJ(documentManager, 
							parameterStatsDoc.getLegislatureEnCours(), debutIntervalle1, 
							finIntervalle1, debutIntervalle2, finIntervalle2);
				}
			}
		}

		facesMessages.add(StatusMessage.Severity.INFO,
				resourcesAccessor.getMessages().get("feedback.solonepg.publication"));

		// Ajout dans le journal du PAN
		STServiceLocator.getJournalService().journaliserActionPAN(documentManager, null,
				SolonEpgEventConstant.PUBLI_STAT_EVENT,
				SolonEpgEventConstant.PUBLI_STAT_COMMENT + " [" + currentReport.getType().getLibelle() + "]",
				SolonEpgEventConstant.CATEGORY_LOG_PAN_GENERAL);
	}
	
	public boolean isBilanSemestriel() throws ClientException {
		return (currentReport.getType().getId().equals(ANReportEnum.TAB_AN_BILAN_SEM_LOI_TOUS.getId()) || 
				currentReport.getType().getId().equals(ANReportEnum.TAB_AN_BILAN_SEM_ORDONNANCE_TOUS.getId()));
	}
	
	/**
	 * refresh le resultat du rapport birt
	 * 
	 * @throws Exception
	 */
	public void refreshBirtResultat() throws Exception {
		final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
		final String currentUser = ssPrincipal.getName();
		final String userWorkspacePath = STServiceLocator.getUserWorkspaceService()
				.getCurrentUserPersonalWorkspace(documentManager, null).getPathAsString();
		if (!anStatsService.isCurrentlyRefreshing(documentManager, currentReport, currentUser)
				&& anStatsService.flagRefreshFor(documentManager, currentReport, currentUser, userWorkspacePath)) {
			// On peut lancer l'évènement de rafraichissement pour ce rapport et rendre la main à l'utilisateur
			// en lui indiquant que sa demande a été prise en compte et qu'il recevra un mail (si dispo)
			// quand le rafraichissement sera terminé.
			Map<String, String> inputValues = new HashMap<String, String>();
			String ministeresParam = SolonEpgServiceLocator.getStatsGenerationResultatService()
					.getMinisteresListBirtReportParam(documentManager);
			String directionsParam = SolonEpgServiceLocator.getStatsGenerationResultatService()
					.getDirectionsListBirtReportParam();
			Date promulgationDebut = null;
			Date promulgationFin = null;
			Date publicationDebut = null;
			Date publicationFin = null;

			inputValues.put("MINISTERES_PARAM", ministeresParam);
			inputValues.put("DIRECTIONS_PARAM", directionsParam);
			inputValues.put("DEBUTLEGISLATURE_PARAM", parseDateToString(statDefaultValue.getDateDebutLegislature()));
			inputValues.put("PERIODE_PARAM", (isPeriodeParMois() ? "M" : "A"));
			inputValues.put("MOIS_PARAM", String.valueOf(getMois()));
			inputValues.put("ANNEE_PARAM", String.valueOf(getAnnee()));

			// Legislature en cours parametre
			inputValues.put("LEGISLATURE_PARAM", parseDateToString(statDefaultValue.getDateDebutLegislature()));
			String legisValue = ("".equals(legislatureSelectionnee) ? " " : legislatureSelectionnee);

			inputValues.put("LEGISLATURES", "'" + legisValue + "'");
			inputValues.put("LEGISLATURES_LABEL", buildLegislatureParamLabel());

			if (ANReportEnum.TAB_AN_STAT_DEBUT_LEGISLATURE.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_DEBUT_LEGISLATURE_MIN.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_STAT_MISE_APPLICATION.equals(currentReport.getType())) {
				promulgationDebut = statDefaultValue.getTauxPromulgationDebut();
				promulgationFin = statDefaultValue.getTauxPromulgationFin();
				publicationDebut = statDefaultValue.getTauxPublicationDebut();
				publicationFin = statDefaultValue.getTauxPublicationFin();

			} else if (ANReportEnum.TAB_AN_INDIC_APP_ORDO_DEBUT_LEGISLATURE.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_INDIC_APP_ORDO_MISE_APPLI.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_DEBUT_LEGISLATURE_MIN_APP_ORDO.equals(currentReport.getType())) {
				promulgationDebut = statDefaultValue.getTauxDLPublicationOrdosDebut();
				promulgationFin = statDefaultValue.getTauxDLPublicationOrdosFin();
				publicationDebut = statDefaultValue.getTauxDLPublicationDecretsOrdosDebut();
				publicationFin = statDefaultValue.getTauxDLPublicationDecretsOrdosFin();

			} else if (ANReportEnum.TAB_AN_STAT_DERNIERE_SESSION.equals(currentReport.getType())) {
				promulgationDebut = statDefaultValue.getLolfPromulgationDebut();
				promulgationFin = statDefaultValue.getLolfPromulgationFin();
				publicationDebut = statDefaultValue.getLolfPublicationDebut();
				publicationFin = statDefaultValue.getLolfPublicationFin();
				inputValues.put("DERNIERE_SESSION_PARAM", "true");
			} else if (ANReportEnum.TAB_AN_INDIC_APP_ORDO_DERNIERE_SESSION.equals(currentReport.getType())) {
				promulgationDebut = statDefaultValue.getTauxSPPublicationOrdosDebut();
				promulgationFin = statDefaultValue.getTauxSPPublicationOrdosFin();
				publicationDebut = statDefaultValue.getTauxSPPublicationDecretsOrdosDebut();
				publicationFin = statDefaultValue.getTauxSPPublicationDecretsOrdosFin();
				inputValues.put("DERNIERE_SESSION_PARAM", "true");
			} else if (ANReportEnum.TAB_AN_BILAN_SEM_LOI_TOUS.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_BILAN_SEM_MIN_TOUS.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_BILAN_SEM_NATURE.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_BILAN_SEM_VOTE.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_LOI.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_MIN.equals(currentReport.getType())) {
				promulgationDebut = statDefaultValue.getBilanPromulgationDebut();
				promulgationFin = statDefaultValue.getBilanPromulgationFin();
				publicationDebut = statDefaultValue.getBilanPublicationDebut();
				publicationFin = statDefaultValue.getBilanPublicationFin();
			} else if (ANReportEnum.TAB_AN_BILAN_SEM_ORDONNANCE_TOUS.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_BILAN_SEM_MIN_TOUS_ORDONNANCE.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_SUB_APP_ORDONNANCES.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_MIN_APP_ORDO.equals(currentReport.getType())) {
				promulgationDebut = statDefaultValue.getBilanPublicationDebutOrdo();
				promulgationFin = statDefaultValue.getBilanPublicationFinOrdo();
				publicationDebut = statDefaultValue.getBilanPublicationDebutOrdoDecret();
				publicationFin = statDefaultValue.getBilanPublicationFinOrdoDecret();
			}

			inputValues.put("DEBUT_INTERVALLE1_PARAM", parseDateToString(promulgationDebut));
			inputValues.put("FIN_INTERVALLE1_PARAM", parseDateToString(promulgationFin));
			inputValues.put("DEBUT_INTERVALLE2_PARAM", parseDateToString(publicationDebut));
			inputValues.put("FIN_INTERVALLE2_PARAM", parseDateToString(publicationFin));

			if (currentReport.getType().getId().endsWith("_LOI")
					|| currentReport.getType().getId().endsWith("_LOI_ALL")) {
				inputValues.put("DOSSIERID_PARAM", navigationContext.getCurrentDocument().getId());
			} else if (ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_SUB_APP_ORDONNANCES.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_TAUX_APPLICATION_ORDO.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_TAUX_APPLICATION_SUB_APP_ORDONNANCES_ALL.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES_ALL.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES.equals(currentReport.getType())
					|| ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_SUB_APP_ORDONNANCES.equals(currentReport.getType())) {
				inputValues.put("DOSSIERID_PARAM", navigationContext.getCurrentDocument().getId());
			} else if (currentReport.getType().getId().endsWith("_MIN")
					|| currentReport.getType().getId().endsWith("_MIN_ALL")
					|| currentReport.getType().getId().endsWith("_MIN_APP_ORDO")
					|| currentReport.getType().getId().endsWith("_MIN_APP_ORDO_ALL")) {
				inputValues.put("MINISTEREPILOTE_PARAM", texteMaitreActions.getCurrentMinistere().getId());
				inputValues.put("MINISTEREPILOTELABEL_PARAM", texteMaitreActions.getCurrentMinistere().getLabel());
			}

			// Post commit event
			EventProducer eventProducer = STServiceLocator.getEventProducer();
			Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
			eventProperties.put(SolonEpgANEventConstants.CURRENT_REPORT_PROPERTY, currentReport);
			eventProperties.put(SolonEpgANEventConstants.INPUT_VALUES_PROPERTY, (Serializable) inputValues);
			eventProperties.put(SolonEpgANEventConstants.USER_PROPERTY, currentUser);
			eventProperties.put(SolonEpgANEventConstants.USER_WS_PATH_PROPERTY, userWorkspacePath);
			InlineEventContext eventContext = new InlineEventContext(documentManager, ssPrincipal, eventProperties);
			eventProducer.fireEvent(eventContext.newEvent(SolonEpgANEventConstants.REFRESH_STATS_EVENT));

		} else {
			// On affiche à l'utilisateur qu'un rafraichissement a déjà été demandé et est en cours
			String dateRequest = anStatsService.getHorodatageRequest(documentManager, currentReport, currentUser);
			String info = "La mise à jour a déjà été demandée le " + dateRequest;
			facesMessages.add(StatusMessage.Severity.INFO, info);
		}
	}

	private String buildLegislatureParamLabel() {
		StringBuilder builder = new StringBuilder("la ");
		if (StringUtil.isBlank(legislatureSelectionnee)) {
			builder.append("législature non renseignée");
		} else {
			builder.append(legislatureSelectionnee + " législature");
		}
		return builder.toString();
	}

	public boolean canBeRefresh() throws ClientException {
		final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
		final String currentUser = ssPrincipal.getName();

		return !anStatsService.isCurrentlyRefreshing(documentManager, currentReport, currentUser);

	}

	public String getHorodatageRequest() throws ClientException {
		final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
		final String currentUser = ssPrincipal.getName();
		return anStatsService.getHorodatageRequest(documentManager, currentReport, currentUser);
	}

	/**
	 * afficher le contenu de resultat de birt en format html
	 * 
	 * @return
	 * @throws Exception
	 */
	public String displayBirtResultatHtml() throws Exception {

		if (htmlContent == null) {
			return "espace_activite_normative_stats_non_disponible";
		} else {
			OutputStream outputStream = null;
			InputStream inputStream = null;
			BufferedInputStream fif = null;

			try {

				HttpServletResponse response = getHttpServletResponse();
				if (response == null) {
					return "";
				}

				response.reset();
				response.setContentType("text/html;charset=utf-8");

				// récupération réponse
				outputStream = response.getOutputStream();
				inputStream = htmlContent.getStream();
				fif = new BufferedInputStream(inputStream);
				// copie le fichier dans le flux de sortie
				int data;
				while ((data = fif.read()) != -1) {
					outputStream.write(data);
				}

			} catch (Exception e) {
				LOGGER.error(STLogEnumImpl.FAIL_GENERATE_HTML_TEC, e);
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
				if (fif != null) {
					fif.close();
				}
			}

			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}

			FacesContext.getCurrentInstance().responseComplete();
			return "";
		}
	}

	/**
	 * afficher le contenu de resultat de birt en format pdf
	 * 
	 * @return
	 * @throws Exception
	 */
	public void displayBirtResultatPdf() throws Exception {

		if (pdfContent != null) {

			HttpServletResponse response = getHttpServletResponse();
			if (response == null) {
				return;
			}

			OutputStream outputStream = null;
			InputStream inputStream = null;
			BufferedInputStream fif = null;
			try {

				response.reset();
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + currentReport.getName() + ".pdf");

				// récupération réponse
				outputStream = response.getOutputStream();

				inputStream = pdfContent.getStream();
				fif = new BufferedInputStream(inputStream);
				// copie le fichier dans le flux de sortie
				int data;
				while ((data = fif.read()) != -1) {
					outputStream.write(data);
				}

			} catch (Exception e) {

				LOGGER.error(STLogEnumImpl.FAIL_CREATE_PDF_TEC, e);
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
				if (fif != null) {
					fif.close();
				}
			}
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}

			FacesContext.getCurrentInstance().responseComplete();
		}
	}

	private String findReportName() {
		String name = null;
		if (this.currentReport != null) {

			ArchiAnReportEnum archRep = ArchiAnReportEnum.findArchiAnReportEnum(this.currentReport.getType());
			if (archRep != null) {

				OrganigrammeNode currentMinistere = texteMaitreActions.getCurrentMinistere();
				EntiteNode node = currentMinistere == null ? null : (EntiteNode) currentMinistere;
				String ministerNor = node == null ? null : node.getNorMinistere();
				return archRep.getFileName(ministerNor);
			}
		}

		return name;

	}

	/**
	 * afficher le contenu de resultat de birt en format pdf
	 * 
	 * @return
	 * @throws Exception
	 */
	public void displayBirtResultatXsl() throws Exception {

		if (xlsContent != null) {
			HttpServletResponse response = getHttpServletResponse();
			if (response == null) {
				return;
			}
			OutputStream outputStream = null;
			InputStream inputStream = null;
			BufferedInputStream fif = null;

			try {

				response.reset();
				response.setContentType("application/vnd.ms-excel;charset=utf-8");

				response.addHeader("Content-Disposition", "attachment; filename=\"" + this.findReportName() + "\"");
				// récupération réponse
				outputStream = response.getOutputStream();

				inputStream = xlsContent.getStream();
				fif = new BufferedInputStream(inputStream);
				// copie le fichier dans le flux de sortie
				int data;
				while ((data = fif.read()) != -1) {
					outputStream.write(data);
				}

			} catch (Exception e) {

				LOGGER.error(STLogEnumImpl.FAIL_CREATE_EXCEL_TEC, e);
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}

				if (fif != null) {
					fif.close();
				}
			}

			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
			FacesContext.getCurrentInstance().responseComplete();
		}
	}

	public List<Action> getSubTabsActionsList() {

		return subTabsActionsList;
	}

	public void setSubTabsActionsList(List<Action> tabsActionsList) {

		this.subTabsActionsList = tabsActionsList;
	}

	public Action getCurrentSubTabAction() {
		return currentSubTabAction;
	}

	public void setCurrentSubTabAction(Action currentSubTabAction) throws ClientException {
		if (ANReportEnum.getById(currentSubTabAction.getId()) != null) {
			if ("TAB_AN_MESURES_APPLICATION_LOI".equals(currentSubTabAction.getId())
					|| "TAB_AN_MESURES_APP_ORDONNANCES".equals(currentSubTabAction.getId())
					|| "TAB_AN_TAUX_APPLICATION_LOI".equals(currentSubTabAction.getId())
					|| "TAB_AN_FIL_EAU_LOI_TOUS".equals(currentSubTabAction.getId())
					|| "TAB_AN_TAUX_APPLICATION_SUB_APP_ORDONNANCES".equals(currentSubTabAction.getId())) {
				setCurrentReport(currentSubTabAction.getId() + "_ALL");
				dipslayMesuresApplicationAll = true;
			} else {
				setCurrentReport(currentSubTabAction.getId());
				dipslayMesuresApplicationAll = false;
			}
			// setReportDefaultParameters(currentSubTabAction.getId());
			this.currentSubTabAction = currentSubTabAction;
			initReportResultatData();
		}
	}

	/**
	 * afficher les MesuresApplication avec decret publiees
	 * 
	 * @throws Exception
	 */
	public void dipslayMesuresApplication() throws Exception {
		dipslayMesuresApplicationAll = !dipslayMesuresApplicationAll;
		if (dipslayMesuresApplicationAll == false) {
			setCurrentReport(this.getCurrentSubTabAction().getId());
		} else {
			setCurrentReport(this.getCurrentSubTabAction().getId() + "_ALL");
		}
	}

	public void dipslayCourbeDeLaPeriodeSaisie() throws Exception {
		setCurrentReport(this.getCurrentSubTabAction().getId());
	}

	/**
	 * test s'il faut afficher decret publiees
	 * 
	 * @return
	 */
	public boolean isDipslayMesuresApplicationAll() {
		return dipslayMesuresApplicationAll;
	}

	/**
	 * tester s'il faut afficher le lien (masquer ou afficher les decret publics)
	 * 
	 * @return
	 */
	public boolean isDisplayLienPubliee() {

		boolean result = false;
		if (currentReport != null && currentReport.getType() != null) {
			if (currentReport.getType().getId().startsWith(ANReportEnum.TAB_AN_TABLEAU_BORD_ACTIVE.getId())
					|| currentReport.getType().getId().startsWith(ANReportEnum.TAB_AN_MESURES_APPLICATION_LOI.getId())
					|| currentReport.getType().getId().startsWith(ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES.getId())
					|| currentReport.getType().getId().startsWith(ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN.getId())
					|| currentReport.getType().getId()
							.startsWith(ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_APP_ORDO.getId())
					|| currentReport.getType().getId().startsWith(ANReportEnum.TAB_AN_TABLEAU_BORD_DEPASSE.getId())
					|| currentReport.getType().getId().startsWith(ANReportEnum.TAB_AN_TB_APP_ORDO_DEPASSE.getId())
					|| currentReport.getType().getId().startsWith(ANReportEnum.TAB_AN_TB_APP_ORDO_ACTIVE.getId())
					|| currentReport.getType().getId().startsWith(ANReportEnum.TAB_AN_RATIFICATION_741_MIN.getId())
					|| currentReport.getType().getId().startsWith(ANReportEnum.TAB_AN_RATIFICATION_38C_MIN.getId())
					|| currentReport.getType().getId().startsWith(ANReportEnum.TAB_AN_HABILITATION_FLTR_MIN.getId())
					|| currentReport.getType().getId().startsWith(ANReportEnum.TAB_AN_HABILITATION_SS_FLTR_MIN.getId())
					|| currentReport.getType().getId()
							.startsWith(ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_38C.getId())
					|| currentReport.getType().getId()
							.startsWith(ANReportEnum.TAB_AN_TABLEAU_BORD_RATIFICATION_741.getId())
					|| currentReport.getType().getId()
							.startsWith(ANReportEnum.TAB_AN_TABLEAU_BORD_HABILITATION_FILTRE.getId())
					|| currentReport.getType().getId()
							.startsWith(ANReportEnum.TAB_AN_TABLEAU_BORD_HABILITATION_SS_FILTRE.getId())) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * tester s'il faut afficher lien imprimer
	 * 
	 * @return
	 */
	public boolean isDisplayImprimerLink() {
		return pdfContent != null;
	}

	/**
	 * tester s'il faut afficher le lien generer au format excel
	 * 
	 * @return
	 */
	public boolean isDisplayExcelLink() {
		return xlsContent != null;
	}

	/**
	 * tester s'il faut afficher le lien publie
	 * 
	 * @return
	 */
	public boolean isDisplayPublierLink() {
		boolean result = false;
		if ((htmlContent != null) && currentReport != null && currentReport.getType() != null) {
			if (activiteNormativeActions.isInApplicationDesLois()
					&& !currentReport.getType().equals(ANReportEnum.TAB_AN_DELAI_NATURE_TOUS)) {

				result = true;

			} else if (activiteNormativeActions.isInTransposition()) {
				result = true;

			} else if (activiteNormativeActions.isInOrdonnances() || activiteNormativeActions.isInOrdonnances38C()
					|| activiteNormativeActions.isInTraiteAccord()) {
				result = true;
			} else if (activiteNormativeActions.isInApplicationDesOrdonnances()) {
				result = true;
			}
		}
		return result;
	}

	public boolean isActiviteNormativeUpdater() {
		return (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ACTIVITE_NORMATIVE_UPDATER)
				|| (activiteNormativeActions.isInApplicationDesLois() && ssPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER))
				|| (activiteNormativeActions.isInOrdonnances() && ssPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_UPDATER))
				|| (activiteNormativeActions.isInOrdonnances38C() && ssPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_UPDATER))
				|| (activiteNormativeActions.isInTraiteAccord() && ssPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_UPDATER))
				|| (activiteNormativeActions.isInTransposition() && ssPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_UPDATER)) || (activiteNormativeActions
				.isInApplicationDesOrdonnances() && ssPrincipal
				.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_UPDATER)));
	}

	public boolean isActiviteNormativeReader() {
		return ((activiteNormativeActions.isInApplicationDesLois() && ssPrincipal
				.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER))
				|| (activiteNormativeActions.isInOrdonnances() && ssPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_READER))
				|| (activiteNormativeActions.isInOrdonnances38C() && ssPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_READER))
				|| (activiteNormativeActions.isInTraiteAccord() && ssPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_READER))
				|| (activiteNormativeActions.isInTransposition() && ssPrincipal
						.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_READER)) || (activiteNormativeActions
				.isInApplicationDesOrdonnances() && ssPrincipal
				.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_READER)));
	}

	/**
	 * tester s'il faut afficher le lien sauvgarder
	 * 
	 * @return
	 */
	public boolean isDisplaySaveLink() {
		return htmlContent != null;
	}

	public boolean isPeriodeParMois() {
		return periodeParMois;
	}

	public void setPeriodeParMois(boolean periodeParMois) {
		this.periodeParMois = periodeParMois;
	}

	public int getMois() {
		return mois;
	}

	public void setMois(int mois) {
		this.mois = mois;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public int getAnneeDeDepart() throws Exception {
		int retYear = 2000;
		STParametreService paramService = STServiceLocator.getSTParametreService();

		try {
			retYear = Integer.parseInt(paramService.getParametreValue(documentManager,
					"annee-depart-statistique-mesure-publication"));
		} catch (Exception e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_PARAM_TEC,
					"Paramètre : \"annee-depart-statistique-mesure-publication\"", e);
		}

		return retYear;
	}

	public int getAnneeCourant() throws Exception {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	/**
	 * setter le rapport birt courante
	 * 
	 * @param currentTabId
	 */
	private void setCurrentReport(String currentTabId) throws ClientException {
		ANReportEnum reportType = ANReportEnum.getById(currentTabId);
		if (reportType != null) {
			StringBuilder reportName = new StringBuilder(reportType.getName());
			if (currentTabId.endsWith("_MIN") || currentTabId.endsWith("_MIN_ALL")
					|| currentTabId.endsWith("_MIN_APP_ORDO_ALL") || currentTabId.endsWith("_MIN_APP_ORDO")) {
				reportName.append("-").append(texteMaitreActions.getCurrentMinistere().getId());
			} else if (currentTabId.endsWith("_LOI") || currentTabId.endsWith("_LOI_ALL")
					|| currentTabId.endsWith("TAB_AN_TAUX_APPLICATION_SUB_APP_ORDONNANCES")
					|| currentTabId.endsWith("TAB_AN_TAUX_APPLICATION_SUB_APP_ORDONNANCES_ALL")
					|| currentTabId.endsWith("TAB_AN_MESURES_APP_ORDONNANCES_ALL")
					|| currentTabId.endsWith("TAB_AN_MESURES_APP_ORDONNANCES")
					|| currentTabId.endsWith("TAB_AN_BILAN_SEMESTRIEL_SUB_APP_ORDONNANCES")) {
				reportName.append("-").append(
						navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class).getNumeroNor());
			} else if ("TAB_AN_COURBE_TOUS".equals(currentTabId) || "TAB_AN_COURBE_TOUS_APP_ORDO".equals(currentTabId)) {
				reportName.append("-").append(getAnnee());
				if (isPeriodeParMois()) {
					reportName.append("-").append(getMois());
				}
			}
			currentReport = new ANReport(reportName.toString(), reportType);
		}
		initReportResultatData();
	}

	private static HttpServletResponse getHttpServletResponse() {
		ServletResponse response = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			response = (ServletResponse) facesContext.getExternalContext().getResponse();
		}
		if (response != null && response instanceof HttpServletResponse) {
			return (HttpServletResponse) response;
		}
		return null;
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

	private String parseDateToString(Date dateValue) {
		if (dateValue == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateValue);
		return DateUtil.adjustDatePart(cal.get(Calendar.DAY_OF_MONTH)) + "/"
				+ DateUtil.adjustDatePart(cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
	}

	public ParametrageAN getParameterStatsDoc() {
		if (parameterStatsDoc == null) {
			statDefaultValue = new PANExportParametreDTO();
			SolonEpgParametreService paramEPGservice = SolonEpgServiceLocator.getSolonEpgParametreService();
			ParametrageAN myDoc;
			try {
				myDoc = paramEPGservice.getDocAnParametre(documentManager);
				this.parameterStatsDoc = myDoc;
				refreshStatDefaultValue();

			} catch (ClientException e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOCUMENT_FONC, e);
			}
		}
		return parameterStatsDoc;
	}

	private void refreshStatDefaultValue() {
		// Taux d'exécution des lois (depuis le début de la législature)
		statDefaultValue.setTauxPromulgationDebut(parameterStatsDoc.getLECTauxDebutLegisDatePromulBorneInf());
		statDefaultValue.setTauxPromulgationFin(parameterStatsDoc.getLECTauxDebutLegisDatePromulBorneSup());

		statDefaultValue.setTauxPublicationDebut(parameterStatsDoc.getLECTauxDebutLegisDatePubliBorneInf());
		statDefaultValue.setTauxPublicationFin(parameterStatsDoc.getLECTauxDebutLegisDatePubliBorneSup());

		// Taux d'exécution des lois (au cours de la dernière session parlementaire)
		statDefaultValue.setLolfPromulgationDebut(parameterStatsDoc.getLECTauxSPDatePromulBorneInf());
		statDefaultValue.setLolfPromulgationFin(parameterStatsDoc.getLECTauxSPDatePromulBorneSup());

		statDefaultValue.setLolfPublicationDebut(parameterStatsDoc.getLECTauxSPDatePubliBorneInf());
		statDefaultValue.setLolfPublicationFin(parameterStatsDoc.getLECTauxSPDatePubliBorneSup());

		// Bilan semestriel
		statDefaultValue.setBilanPromulgationDebut(parameterStatsDoc.getLECBSDatePromulBorneInf());
		statDefaultValue.setBilanPromulgationFin(parameterStatsDoc.getLECBSDatePromulBorneSup());

		statDefaultValue.setBilanPublicationDebut(parameterStatsDoc.getLECBSDatePubliBorneInf());
		statDefaultValue.setBilanPublicationFin(parameterStatsDoc.getLECBSDatePubliBorneSup());

		// Cas des ordonnances
		// Bilan semestriel
		statDefaultValue.setBilanPublicationDebutOrdo(parameterStatsDoc.getLECBSDatePubliOrdoBorneInf());
		statDefaultValue.setBilanPublicationFinOrdo(parameterStatsDoc.getLECBSDatePubliOrdoBorneSup());

		statDefaultValue.setBilanPublicationDebutOrdoDecret(parameterStatsDoc.getLECBSDatePubliDecretOrdoBorneInf());
		statDefaultValue.setBilanPublicationFinOrdoDecret(parameterStatsDoc.getLECBSDatePubliDecretOrdoBorneSup());

		// Taux d'exécution des lois (depuis le début de la législature)
		statDefaultValue.setTauxDLPublicationOrdosDebut(parameterStatsDoc.getLECTauxDebutLegisDatePubliOrdoBorneInf());
		statDefaultValue.setTauxDLPublicationOrdosFin(parameterStatsDoc.getLECTauxDebutLegisDatePubliOrdoBorneSup());

		statDefaultValue.setTauxDLPublicationDecretsOrdosDebut(parameterStatsDoc
				.getLECTauxDebutLegisDatePubliDecretOrdoBorneInf());
		statDefaultValue.setTauxDLPublicationDecretsOrdosFin(parameterStatsDoc
				.getLECTauxDebutLegisDatePubliDecretOrdoBorneSup());

		// Taux d'exécution des lois (au cours de la dernière session parlementaire)
		statDefaultValue.setTauxSPPublicationOrdosDebut(parameterStatsDoc.getLECTauxSPDatePubliOrdoBorneInf());
		statDefaultValue.setTauxSPPublicationOrdosFin(parameterStatsDoc.getLECTauxSPDatePubliOrdoBorneSup());

		statDefaultValue.setTauxSPPublicationDecretsOrdosDebut(parameterStatsDoc
				.getLECTauxSPDatePubliDecretOrdoBorneInf());
		statDefaultValue.setTauxSPPublicationDecretsOrdosFin(parameterStatsDoc
				.getLECTauxSPDatePubliDecretOrdoBorneSup());

		statDefaultValue.setDateDebutLegislature(parameterStatsDoc.getLegislatureEnCoursDateDebut());
		legislatureSelectionnee = null;
	}

	public void setParameterStatsDoc(ParametrageAN parameterStatsDoc) {
		this.parameterStatsDoc = parameterStatsDoc;
	}

	public PANExportParametreDTO getStatDefaultValue() {
		if (parameterStatsDoc == null) {
			getParameterStatsDoc();
		}
		return statDefaultValue;
	}

	public void setStatDefaultValue(PANExportParametreDTO statDefaultValue) {
		this.statDefaultValue = statDefaultValue;
	}

	public String getLegislatureSelectionnee() {
		if (parameterStatsDoc == null) {
			getParameterStatsDoc();
		}

		if (legislatureSelectionnee == null) {
			legislatureSelectionnee = parameterStatsDoc.getLegislatureEnCours();
		}

		return legislatureSelectionnee;
	}

	public void setLegislatureSelectionnee(String legislatureSelectionnee) {
		this.legislatureSelectionnee = legislatureSelectionnee;
	}

}
