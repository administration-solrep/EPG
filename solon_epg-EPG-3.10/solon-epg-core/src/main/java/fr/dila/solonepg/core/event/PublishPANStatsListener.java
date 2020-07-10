package fr.dila.solonepg.core.event;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.api.service.StatsGenerationResultatService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.SessionUtil;

public class PublishPANStatsListener implements PostCommitEventListener {

	private static final STLogger	LOGGER	= STLogFactory.getLog(PublishPANStatsListener.class);

	private boolean					curLegis;

	@Override
	public void handleEvent(EventBundle events) throws ClientException {
		if (!events.containsEventName(SolonEpgANEventConstants.PUBLISH_STATS_EVENT)) {
			return;
		}
		for (final Event event : events) {
			if (SolonEpgANEventConstants.PUBLISH_STATS_EVENT.equals(event.getName())) {
				publishStats(event);
			}
		}

	}

	private void publishStats(Event event) {
		final EventContext eventCtx = event.getContext();
		// récupération des propriétés de l'événement
		final Map<String, Serializable> eventProperties = eventCtx.getProperties();
		curLegis = ((Boolean) eventProperties.get(SolonEpgANEventConstants.EXPORT_PAN_CURLEGISLATURE)) == null ? true
				: (Boolean) eventProperties.get(SolonEpgANEventConstants.EXPORT_PAN_CURLEGISLATURE);
		final String user = (String) eventProperties.get(SolonEpgANEventConstants.USER_PROPERTY);
		final String dateRequest = DateUtil.formatddmmyyyyahhmm(Calendar.getInstance().getTime());

		Map<String, String> inputValues = new HashMap<String, String>();

		final ActiviteNormativeService anService = SolonEpgServiceLocator.getActiviteNormativeService();
		final StatsGenerationResultatService statGenerationService = SolonEpgServiceLocator
				.getStatsGenerationResultatService();
		final SolonEpgParametreService anParamService = SolonEpgServiceLocator.getSolonEpgParametreService();
		CoreSession session = null;

		try {
			session = SessionUtil.getCoreSession();
			String ministeresParam = statGenerationService.getMinisteresListBirtReportParam(session);
			String directionsParam = statGenerationService.getDirectionsListBirtReportParam();
			int errors = 0;
			StringBuilder errorBuilder = new StringBuilder();

			final ParametrageAN paramAn = anParamService.getDocAnParametre(session);
			final String legislatureEnCours = paramAn.getLegislatureEnCours();
			paramAn.setLegislaturePublication(legislatureEnCours);
			int lastIndex = paramAn.getLegislatures().indexOf(legislatureEnCours) - 1;
			if (lastIndex >= 0) {
				paramAn.setLegislaturePrecPublication(paramAn.getLegislatures().get(lastIndex));
			}
			paramAn.save(session);

			for (ANReportEnum aNReport : ANReportEnum.values()) {
				if (statGenerationService.isRapportPubliable(aNReport.getId())) {
					try {
						inputValues.clear();
						inputValues.put("MINISTERES_PARAM", ministeresParam);
						inputValues.put("DIRECTIONS_PARAM", directionsParam);
						inputValues.putAll(assignParameters(session, aNReport, curLegis, paramAn));

						if (aNReport.getId().endsWith("_MIN") || aNReport.getId().endsWith("_MIN_ALL")
								|| aNReport.getId().endsWith("_MIN_APP_ORDO_ALL")
								|| aNReport.getId().endsWith("_MIN_APP_ORDO")) {
							generateReportForMin(aNReport, inputValues, session);

						} else if (aNReport.getId().endsWith("_LOI") || aNReport.getId().endsWith("_LOI_ALL")) {
							List<DocumentModel> applicationLoisLegisCourante = anService.getAllAplicationLoiDossiers(
									session, curLegis);
							generateReportForTexte(aNReport, inputValues, session, applicationLoisLegisCourante);
						} else if (aNReport.equals(ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES)
								|| aNReport.equals(ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES_ALL)
								|| aNReport.equals(ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_SUB_APP_ORDONNANCES)
								|| aNReport.equals(ANReportEnum.TAB_AN_TAUX_APPLICATION_ORDO)
								|| aNReport.equals(ANReportEnum.TAB_AN_TAUX_APPLICATION_SUB_APP_ORDONNANCES_ALL)
								|| aNReport.equals(ANReportEnum.TAB_AN_FIL_EAU_ORDO_TOUS_APP_ORDONNANCE)
								|| aNReport.equals(ANReportEnum.TAB_AN_FIL_EAU_ORDO_TOUS_APP_ORDONNANCE_ALL)) {
							List<DocumentModel> applicationLoisLegisCourante = anService
									.getAllAplicationOrdonnanceDossiers(session, curLegis);
							generateReportForTexte(aNReport, inputValues, session, applicationLoisLegisCourante);
						} else if (aNReport.equals(ANReportEnum.TAB_AN_COURBE_TOUS)
								|| aNReport.equals(ANReportEnum.TAB_AN_COURBE_TOUS_APP_ORDO)) {
							generateReportCourbeTous(aNReport, inputValues, session);

						} else {
							generateReportResult(session, aNReport.getName(), aNReport, inputValues);
						}

					} catch (Exception exc) {
						LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_GENERATE_STATS_TEC, exc);
						errors++;
						errorBuilder.append("Impossible de publier la statistique \"").append(aNReport.getLibelle())
								.append("\", cause : ").append(exc.getMessage()).append("\n");
					}
				}
			}

			anService.updateLoiListePubliee(session, curLegis);
			anService.updateOrdonnancesListePubliee(session, curLegis);
			anService.updateHabilitationListePubliee(session, curLegis);
			List<DocumentModel> documentModelList = anService.getAllAplicationLoiDossiers(session, curLegis);
			LOGGER.info(STLogEnumImpl.DEFAULT,
					"On s'apprête à publier les statistiques pour " + documentModelList.size()
							+ " applications des lois");
			for (DocumentModel documentModel : documentModelList) {
				anService.generateANRepartitionMinistereHtml(session,
						documentModel.getAdapter(ActiviteNormative.class), curLegis);
				anService.publierTableauSuiviHTML(documentModel, session, false, false, curLegis);

				session.save();
			}

			if (errors > 0) {
				sendMailKO(session, user, dateRequest, errorBuilder.toString());
			} else {
				sendMailOK(session, user, dateRequest);
			}

		} catch (Exception e) {
			LOGGER.error(SSLogEnumImpl.FAIL_CREATE_BIRT_TEC, e);
			sendMailKO(session, user, dateRequest, e.getMessage());
		} finally {
			if (session != null) {
				SessionUtil.close(session);
			}
		}
	}

	private Map<String, String> assignParameters(CoreSession session, ANReportEnum archAnReportEnum, boolean curLegis,
			ParametrageAN paramAn) throws ClientException {
		Map<String, String> inputValues = new HashMap<String, String>();

		String debutPromulgation = null;
		String finPromulgation = null;
		String debutPublication = null;
		String finPublication = null;
		String debutLegislature = null;

		switch (archAnReportEnum) {
			case TAB_AN_BILAN_SEM_LOI_TOUS:
			case TAB_AN_BILAN_SEM_MIN_TOUS:
			case TAB_AN_BILAN_SEM_NATURE:
			case TAB_AN_BILAN_SEM_VOTE:
			case TAB_AN_BILAN_SEMESTRIEL_LOI:
			case TAB_AN_BILAN_SEMESTRIEL_MIN:
				if (curLegis) {
					debutPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLECBSDatePromulBorneInf());
					finPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLECBSDatePromulBorneSup());
					debutPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLECBSDatePubliBorneInf());
					finPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLECBSDatePubliBorneSup());
				} else {
					debutPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLPBSDatePromulBorneInf());
					finPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLPBSDatePromulBorneSup());
					debutPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLPBSDatePubliBorneInf());
					finPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLPBSDatePubliBorneSup());
				}
				break;

			case TAB_AN_BILAN_SEM_ORDONNANCE_TOUS:
			case TAB_AN_BILAN_SEM_MIN_TOUS_ORDONNANCE:
			case TAB_AN_BILAN_SEMESTRIEL_SUB_APP_ORDONNANCES:
			case TAB_AN_BILAN_SEMESTRIEL_MIN_APP_ORDO:

				if (curLegis) {
					debutPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLECBSDatePubliOrdoBorneInf());
					finPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLECBSDatePubliOrdoBorneSup());
					debutPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLECBSDatePubliDecretOrdoBorneInf());
					finPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLECBSDatePubliDecretOrdoBorneSup());
				} else {
					debutPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLPBSDatePubliOrdoBorneInf());
					finPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLPBSDatePubliOrdoBorneSup());
					debutPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLPBSDatePubliDecretOrdoBorneInf());
					finPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLPBSDatePubliDecretOrdoBorneSup());
				}
				break;
			case TAB_AN_STAT_DEBUT_LEGISLATURE:
			case TAB_AN_DEBUT_LEGISLATURE_MIN:
			case TAB_AN_STAT_MISE_APPLICATION:
				if (curLegis) {
					debutPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLECTauxDebutLegisDatePromulBorneInf());
					finPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLECTauxDebutLegisDatePromulBorneSup());
					debutPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLECTauxDebutLegisDatePubliBorneInf());
					finPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLECTauxDebutLegisDatePubliBorneSup());
				} else {
					debutPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLPTauxDebutLegisDatePromulBorneInf());
					finPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLPTauxDebutLegisDatePromulBorneSup());
					debutPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLPTauxDebutLegisDatePubliBorneInf());
					finPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLPTauxDebutLegisDatePubliBorneSup());
				}
				break;

			case TAB_AN_INDIC_APP_ORDO_DEBUT_LEGISLATURE:
			case TAB_AN_DEBUT_LEGISLATURE_MIN_APP_ORDO:
			case TAB_AN_INDIC_APP_ORDO_MISE_APPLI:
				if (curLegis) {
					debutPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn
							.getLECTauxDebutLegisDatePubliOrdoBorneInf());
					finPromulgation = DateUtil
							.formatDDMMYYYYSlash(paramAn.getLECTauxDebutLegisDatePubliOrdoBorneSup());
					debutPublication = DateUtil.formatDDMMYYYYSlash(paramAn
							.getLECTauxDebutLegisDatePubliDecretOrdoBorneInf());
					finPublication = DateUtil.formatDDMMYYYYSlash(paramAn
							.getLECTauxDebutLegisDatePubliDecretOrdoBorneSup());
				} else {
					debutPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn
							.getLPTauxDebutLegisDatePubliOrdoBorneInf());
					finPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLPTauxDebutLegisDatePubliOrdoBorneSup());
					debutPublication = DateUtil.formatDDMMYYYYSlash(paramAn
							.getLPTauxDebutLegisDatePubliDecretOrdoBorneInf());
					finPublication = DateUtil.formatDDMMYYYYSlash(paramAn
							.getLPTauxDebutLegisDatePubliDecretOrdoBorneSup());
				}
				break;

			case TAB_AN_STAT_DERNIERE_SESSION:

				if (curLegis) {
					debutPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLECTauxSPDatePromulBorneInf());
					finPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLECTauxSPDatePromulBorneSup());
					debutPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLECTauxSPDatePubliBorneInf());
					finPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLECTauxSPDatePubliBorneSup());
				} else {
					debutPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLPTauxSPDatePromulBorneInf());
					finPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLPTauxSPDatePromulBorneSup());
					debutPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLPTauxSPDatePubliBorneInf());
					finPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLPTauxSPDatePubliBorneSup());
				}

				break;
			case TAB_AN_INDIC_APP_ORDO_DERNIERE_SESSION:
				if (curLegis) {
					debutPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLECTauxSPDatePubliOrdoBorneInf());
					finPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLECTauxSPDatePubliOrdoBorneSup());
					debutPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLECTauxSPDatePubliDecretOrdoBorneInf());
					finPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLECTauxSPDatePubliDecretOrdoBorneSup());
				} else {
					debutPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLPTauxSPDatePubliOrdoBorneInf());
					finPromulgation = DateUtil.formatDDMMYYYYSlash(paramAn.getLPTauxSPDatePubliOrdoBorneSup());
					debutPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLPTauxSPDatePubliDecretOrdoBorneInf());
					finPublication = DateUtil.formatDDMMYYYYSlash(paramAn.getLPTauxSPDatePubliDecretOrdoBorneSup());
				}
				break;
			default:
				break;
		}

		if (curLegis) {
			debutLegislature = DateUtil.formatDDMMYYYYSlash(paramAn.getLegislatureEnCoursDateDebut());
			inputValues.put("LEGISLATURES", "'" + paramAn.getLegislatureEnCours() + "'");
			inputValues.put("LEGISLATURES_LABEL", "la " + paramAn.getLegislatureEnCours() + " législature");
		} else {
			debutLegislature = DateUtil.formatDDMMYYYYSlash(paramAn.getLegislaturePrecedenteDateDebut());

			int lastIndex = (paramAn.getLegislatures().size() - 2 >= 0 ? paramAn.getLegislatures().indexOf(
					paramAn.getLegislatureEnCours()) - 1 : 0);
			inputValues.put("LEGISLATURES", "'" + paramAn.getLegislatures().get(lastIndex) + "'");
			inputValues.put("LEGISLATURES_LABEL", "la " + paramAn.getLegislatures().get(lastIndex) + " législature");
		}

		inputValues.put("DEBUT_INTERVALLE1_PARAM", debutPromulgation);
		inputValues.put("FIN_INTERVALLE1_PARAM", finPromulgation);
		inputValues.put("DEBUT_INTERVALLE2_PARAM", debutPublication);
		inputValues.put("FIN_INTERVALLE2_PARAM", finPublication);
		inputValues.put("DEBUTLEGISLATURE_PARAM", debutLegislature);

		return inputValues;

	}

	private void generateReportForMin(ANReportEnum aNReport, Map<String, String> inputValues, CoreSession session)
			throws ClientException {

		final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
		List<String> ministeresList = SolonEpgServiceLocator.getActiviteNormativeService()
				.getAllAplicationMinisteresList(session, curLegis);

		for (String ministere : ministeresList) {
			OrganigrammeNode currentMinistere = ministeresService.getEntiteNode(ministere);
			if (currentMinistere != null) {
				inputValues.put("MINISTEREPILOTE_PARAM", ministere);
				inputValues.put("MINISTEREPILOTELABEL_PARAM", currentMinistere.getLabel());
				generateReportResult(session, aNReport.getName() + "-" + ministere, aNReport, inputValues);
			}
		}
	}

	private void generateReportForTexte(ANReportEnum aNReport, Map<String, String> inputValues, CoreSession session,
			List<DocumentModel> applicationLoiList) throws ClientException {

		for (DocumentModel documentModel : applicationLoiList) {
			String numeroNor = documentModel.getAdapter(TexteMaitre.class).getNumeroNor();
			inputValues.put("DOSSIERID_PARAM", documentModel.getId());
			generateReportResult(session, aNReport.getName() + "-" + numeroNor, aNReport, inputValues);
		}
	}

	private void generateReportCourbeTous(ANReportEnum aNReport, Map<String, String> inputValues, CoreSession session)
			throws ClientException {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);

		String reportNameMonth = aNReport.getName() + "-" + year + "-" + month;
		String reportNameYear = aNReport.getName() + "-" + year;
		inputValues.put("PERIODE_PARAM", "A");
		inputValues.put("ANNEE_PARAM", String.valueOf(year));
		generateReportResult(session, reportNameYear, aNReport, inputValues);

		inputValues.put("PERIODE_PARAM", "M");
		inputValues.put("ANNEE_PARAM", String.valueOf(year));
		inputValues.put("MOIS_PARAM", String.valueOf(month));
		generateReportResult(session, reportNameMonth, aNReport, inputValues);
	}

	private void generateReportResult(CoreSession session, String reportName, ANReportEnum report,
			Map<String, String> inputValues) throws ClientException {

		StatsGenerationResultatService statGenerationService = SolonEpgServiceLocator
				.getStatsGenerationResultatService();

		if (statGenerationService.isRapportPubliable(report.getId())) {
			Blob tempHtmlContent = statGenerationService.generateReportResult(reportName, report.getFile(),
					inputValues, "html");
			SolonEpgServiceLocator.getStatsGenerationResultatService().publierReportResulat(reportName,
					tempHtmlContent, curLegis, session);
		}

	}

	private void sendMailOK(CoreSession session, String user, String dateRequest) {
		String userMail = STServiceLocator.getSTUserService().getUserInfo(user, "m");
		String objet = "[SOLON-EPG] Votre demande de publication des statistiques ";
		String corpsTemplate = "Bonjour,\n\nLa publication des statistiques, demandée le " + dateRequest
				+ ", est terminée.";
		sendMail(session, userMail, objet, corpsTemplate);
	}

	private void sendMailKO(CoreSession session, String user, String dateRequest, String messageStack) {
		String userMail = STServiceLocator.getSTUserService().getUserInfo(user, "m");
		String objet = "[SOLON-EPG] Votre demande de publication des statistiques ";
		String corpsTemplate = "Bonjour,\n\nLa publication des statistiques, demandée le " + dateRequest
				+ ", a échoué. " + "Le message remonté est le suivant : \n" + messageStack;

		sendMail(session, userMail, objet, corpsTemplate);
	}

	private void sendMail(CoreSession session, String adresse, String objet, String corps) {
		final STMailService mailService = STServiceLocator.getSTMailService();
		try {
			if (adresse != null) {
				mailService.sendTemplateMail(adresse, objet, corps, null);
			} else {
				LOGGER.warn(session, STLogEnumImpl.FAIL_GET_MAIL_TEC);
			}
		} catch (final ClientException exc) {
			LOGGER.error(session, STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
		}
	}

}
