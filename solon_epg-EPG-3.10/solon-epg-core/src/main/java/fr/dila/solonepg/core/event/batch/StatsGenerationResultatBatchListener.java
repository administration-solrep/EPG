package fr.dila.solonepg.core.event.batch;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.api.service.StatsGenerationResultatService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.SuiviBatchService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;

/**
 * Batch de generation de resultat des statistiques avec birt
 * 
 * @author Fabio Esposito
 * 
 */
public class StatsGenerationResultatBatchListener extends AbstractBatchEventListener {

	private static final STLogger	LOGGER	= STLogFactory.getLog(StatsGenerationResultatBatchListener.class);

	public StatsGenerationResultatBatchListener() {
		super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_STAT_GENERATION_RESULTAT);
	}

	@Override
	protected void processEvent(CoreSession session, Event event) throws ClientException {
		LOGGER.info(session, EpgLogEnumImpl.INIT_B_GENERATE_STATS_TEC);
		final long startTime = Calendar.getInstance().getTimeInMillis();
		final StatsGenerationResultatService statsGenerationService = SolonEpgServiceLocator
				.getStatsGenerationResultatService();
		final SolonEpgParametreService anParamService = SolonEpgServiceLocator.getSolonEpgParametreService();
		final ActiviteNormativeService anService = SolonEpgServiceLocator.getActiviteNormativeService();
		try {
			String ministeresParam = statsGenerationService.getMinisteresListBirtReportParam(session);
			String directionsParam = statsGenerationService.getDirectionsListBirtReportParam();

			Map<String, String> inputValues = new HashMap<String, String>();

			inputValues.put("MINISTERES_PARAM", ministeresParam);
			inputValues.put("DIRECTIONS_PARAM", directionsParam);

			final ParametrageAN paramAn = anParamService.getDocAnParametre(session);
			final String legislatureEnCours = paramAn.getLegislatureEnCours();
			paramAn.setLegislaturePublication(legislatureEnCours);
			int lastIndex = paramAn.getLegislatures().indexOf(legislatureEnCours) - 1;
			if (lastIndex >= 0) {
				paramAn.setLegislaturePrecPublication(paramAn.getLegislatures().get(lastIndex));
			}

			paramAn.save(session);

			final String dateDebutLegislatureEC = DateUtil
					.formatDDMMYYYYSlash(paramAn.getLegislatureEnCoursDateDebut());
			final String dateDebutLegislaturePrec = DateUtil.formatDDMMYYYYSlash(paramAn
					.getLegislaturePrecedenteDateDebut());

			for (ANReportEnum aNReport : ANReportEnum.values()) {

				try {

					// On génère une première fois pour la législature précédente
					inputValues.putAll(assignParameters(session, aNReport, false, paramAn));
					inputValues.put("LEGISLATURE_PARAM", dateDebutLegislaturePrec);

					if (aNReport.getId().endsWith("_MIN") || aNReport.getId().endsWith("_MIN_ALL")
							|| aNReport.getId().endsWith("_MIN_APP_ORDO_ALL")
							|| aNReport.getId().endsWith("_MIN_APP_ORDO")) {
						generateReportForMin(aNReport, inputValues, session, false);
					} else if (aNReport.getId().endsWith("_LOI") || aNReport.getId().endsWith("_LOI_ALL")) {
						List<DocumentModel> applicationLoisLegisPrec = anService.getAllAplicationLoiDossiers(session,
								false);
						generateReportForLoi(aNReport, inputValues, session, applicationLoisLegisPrec, false);
					} else if (aNReport.equals(ANReportEnum.TAB_AN_TAUX_APPLICATION_ORDO)
							|| aNReport.equals(ANReportEnum.TAB_AN_TAUX_APPLICATION_SUB_APP_ORDONNANCES_ALL)
							|| aNReport.equals(ANReportEnum.TAB_AN_FIL_EAU_ORDO_TOUS_APP_ORDONNANCE)
							|| aNReport.equals(ANReportEnum.TAB_AN_FIL_EAU_ORDO_TOUS_APP_ORDONNANCE_ALL)
							|| aNReport.equals(ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_SUB_APP_ORDONNANCES)
							|| aNReport.equals(ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES)) {
						List<DocumentModel> applicationLoisLegisPrec = anService.getAllAplicationOrdonnanceDossiers(session,
								false);
						generateReportForLoi(aNReport, inputValues, session, applicationLoisLegisPrec, false);
					} else if (aNReport.equals(ANReportEnum.TAB_AN_COURBE_TOUS)
							|| aNReport.equals(ANReportEnum.TAB_AN_COURBE_TOUS_APP_ORDO)) {
						generateReportCourbeTous(aNReport, inputValues, session, false);
					} else {
						generateAllReportResult(session, aNReport.getName(), aNReport, inputValues, false);
					}
					// Puis une seconde fois pour la législature courante
					inputValues.putAll(assignParameters(session, aNReport, true, paramAn));
					inputValues.put("LEGISLATURE_PARAM", dateDebutLegislatureEC);

					if (aNReport.getId().endsWith("_MIN") || aNReport.getId().endsWith("_MIN_ALL")
							|| aNReport.getId().endsWith("_MIN_APP_ORDO_ALL")
							|| aNReport.getId().endsWith("_MIN_APP_ORDO")) {
						generateReportForMin(aNReport, inputValues, session, true);
					} else if (aNReport.getId().endsWith("_LOI") || aNReport.getId().endsWith("_LOI_ALL")) {
						List<DocumentModel> applicationLoisLegisCourante = anService.getAllAplicationLoiDossiers(
								session, true);
						generateReportForLoi(aNReport, inputValues, session, applicationLoisLegisCourante, true);
					} else if (aNReport.equals(ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES)
							|| aNReport.equals(ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES_ALL)
							|| aNReport.equals(ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_SUB_APP_ORDONNANCES)
							|| aNReport.equals(ANReportEnum.TAB_AN_TAUX_APPLICATION_ORDO)
							|| aNReport.equals(ANReportEnum.TAB_AN_TAUX_APPLICATION_SUB_APP_ORDONNANCES_ALL)
							|| aNReport.equals(ANReportEnum.TAB_AN_FIL_EAU_ORDO_TOUS_APP_ORDONNANCE)
							|| aNReport.equals(ANReportEnum.TAB_AN_FIL_EAU_ORDO_TOUS_APP_ORDONNANCE_ALL)) {
						List<DocumentModel> applicationLoisLegisCourante = anService
								.getAllAplicationOrdonnanceDossiers(session, true);
						generateReportForLoi(aNReport, inputValues, session, applicationLoisLegisCourante, true);
					} else if (aNReport.equals(ANReportEnum.TAB_AN_COURBE_TOUS)
							|| aNReport.equals(ANReportEnum.TAB_AN_COURBE_TOUS_APP_ORDO)) {
						generateReportCourbeTous(aNReport, inputValues, session, true);
					} else {
						generateAllReportResult(session, aNReport.getName(), aNReport, inputValues, true);
					}

					TransactionHelper.commitOrRollbackTransaction();
					TransactionHelper.startTransaction();

				} catch (Throwable exc) {
					LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_GENERATE_STATS_TEC, aNReport.toString(), exc);
					errorCount++;
				}
			}
			// Publier liste des loi + repartition ministere
			anService.updateLoiListePubliee(session, false);
			anService.updateOrdonnancesListePubliee(session, false);
			anService.updateHabilitationListePubliee(session, false);
			anService.updateLoiListePubliee(session, true);
			anService.updateOrdonnancesListePubliee(session, true);
			anService.updateHabilitationListePubliee(session, true);

			// On fait la publication pour les rapports des législatures précédentes
			List<DocumentModel> documentModelList = anService.getAllAplicationLoiDossiers(session, false);
			documentModelList.addAll(anService.getAllAplicationOrdonnanceDossiers(session, false));
			for (DocumentModel documentModel : documentModelList) {
				anService.generateANRepartitionMinistereHtml(session,
						documentModel.getAdapter(ActiviteNormative.class), false);
				anService.publierTableauSuiviHTML(documentModel, session, false, true, false);
				session.save();
			}

			// Puis pour la législature courante
			documentModelList = anService.getAllAplicationLoiDossiers(session, true);
			documentModelList.addAll(anService.getAllAplicationOrdonnanceDossiers(session, true));
			for (DocumentModel documentModel : documentModelList) {
				anService.generateANRepartitionMinistereHtml(session,
						documentModel.getAdapter(ActiviteNormative.class), true);
				anService.publierTableauSuiviHTML(documentModel, session, false, true, true);
				session.save();
			}

		} catch (Throwable exc) {
			LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_GENERATE_STATS_TEC, exc);
			errorCount++;
		}
		final long endTime = Calendar.getInstance().getTimeInMillis();
		final SuiviBatchService suiviBatchService = STServiceLocator.getSuiviBatchService();
		try {
			suiviBatchService.createBatchResultFor(batchLoggerModel,
					"Generation de resultat des statistiques avec birt", endTime - startTime);
		} catch (Throwable e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
		}
		LOGGER.info(session, EpgLogEnumImpl.END_B_GENERATE_STATS_TEC);
	}

	private void generateAllReportResult(CoreSession session, String reportName, ANReportEnum report,
			Map<String, String> inputValues, boolean curLegis) {
		final long startTime = Calendar.getInstance().getTimeInMillis();
		final SuiviBatchService suiviBatchService = STServiceLocator.getSuiviBatchService();
		StatsGenerationResultatService statGenerationService = SolonEpgServiceLocator
				.getStatsGenerationResultatService();
		try {
			try {
				statGenerationService.generateAllReportResult(session, reportName, report.getFile(), inputValues,
						report.getId(), batchLoggerModel, curLegis);
			} catch (Exception exc) {
				LOGGER.error(session, SSLogEnumImpl.FAIL_CREATE_BIRT_TEC, reportName, exc);
				errorCount++;
				try {
					final long endTime = Calendar.getInstance().getTimeInMillis();
					suiviBatchService.createBatchResultFor(batchLoggerModel,
							"Erreur lors de la generation du rapport : " + reportName, endTime - startTime);
				} catch (Exception e) {
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
				}
			}
			try {
				final long endTime = Calendar.getInstance().getTimeInMillis();
				suiviBatchService.createBatchResultFor(batchLoggerModel, "Succès de la generation du rapport : "
						+ reportName, endTime - startTime);
				session.save();
			} catch (Exception e) {
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
			}

		} catch (Exception exc) {
			LOGGER.error(session, SSLogEnumImpl.FAIL_SAVE_BIRT_TEC, reportName, exc);
			errorCount++;
			try {
				final long endTime = Calendar.getInstance().getTimeInMillis();
				suiviBatchService.createBatchResultFor(batchLoggerModel, "Erreur lors de la sauvegarde du rapport : "
						+ reportName, endTime - startTime);
			} catch (Exception e) {
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
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
			int lastIndex = paramAn.getLegislatures().indexOf(paramAn.getLegislatureEnCours()) - 1;
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

	private void generateReportForMin(ANReportEnum aNReport, Map<String, String> inputValues, CoreSession session,
			boolean curLegis) throws ClientException {

		final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
		List<String> ministeresList = SolonEpgServiceLocator.getActiviteNormativeService()
				.getAllAplicationMinisteresList(session, curLegis);

		for (String ministere : ministeresList) {
			OrganigrammeNode currentMinistere = ministeresService.getEntiteNode(ministere);
			if (currentMinistere != null) {
				inputValues.put("MINISTEREPILOTE_PARAM", ministere);
				inputValues.put("MINISTEREPILOTELABEL_PARAM", currentMinistere.getLabel());
				generateAllReportResult(session, aNReport.getName() + "-" + ministere, aNReport, inputValues, curLegis);
			}
		}
	}

	private void generateReportForLoi(ANReportEnum aNReport, Map<String, String> inputValues, CoreSession session,
			List<DocumentModel> applicationLoiList, boolean curLegis) throws ClientException {

		for (DocumentModel documentModel : applicationLoiList) {
			String numeroNor = documentModel.getAdapter(TexteMaitre.class).getNumeroNor();
			inputValues.put("DOSSIERID_PARAM", documentModel.getId());
			generateAllReportResult(session, aNReport.getName() + "-" + numeroNor, aNReport, inputValues, curLegis);
		}
	}

	private void generateReportCourbeTous(ANReportEnum aNReport, Map<String, String> inputValues, CoreSession session,
			boolean curLegis) {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);

		String reportNameMonth = aNReport.getName() + "-" + year + "-" + month;
		String reportNameYear = aNReport.getName() + "-" + year;
		inputValues.put("PERIODE_PARAM", "A");
		inputValues.put("ANNEE_PARAM", String.valueOf(year));
		generateAllReportResult(session, reportNameYear, aNReport, inputValues, curLegis);

		inputValues.put("PERIODE_PARAM", "M");
		inputValues.put("ANNEE_PARAM", String.valueOf(year));
		inputValues.put("MOIS_PARAM", String.valueOf(month));
		generateAllReportResult(session, reportNameMonth, aNReport, inputValues, curLegis);
	}

}