package fr.dila.solonepg.core.event.batch;

import static fr.dila.solonepg.api.constant.SolonEpgConfigConstant.SOLONEPG_STATS_REMOVE_ORPHAN_BINARIES;
import static fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil.getRequiredService;

import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.api.service.StatsGenerationResultatService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.SuiviBatchService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.operation.utils.GarbageCollectBinariesOperation;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SolonDateConverter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.BooleanUtils;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.runtime.transaction.TransactionHelper;

/**
 * Batch de generation de resultat des statistiques avec birt
 *
 * @author Fabio Esposito
 */
public class StatsGenerationResultatBatchListener extends AbstractBatchEventListener {
    private static final STLogger LOGGER = STLogFactory.getLog(StatsGenerationResultatBatchListener.class);

    public StatsGenerationResultatBatchListener() {
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_STAT_GENERATION_RESULTAT);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_GENERATE_STATS_TEC);
        final long startTime = Calendar.getInstance().getTimeInMillis();
        final StatsGenerationResultatService statsGenerationService = SolonEpgServiceLocator.getStatsGenerationResultatService();
        final SolonEpgParametreService anParamService = SolonEpgServiceLocator.getSolonEpgParametreService();
        final ActiviteNormativeService anService = SolonEpgServiceLocator.getActiviteNormativeService();
        try {
            String ministeresParam = statsGenerationService.getMinisteresListBirtReportParam(session);
            String directionsParam = statsGenerationService.getDirectionsListBirtReportParam();

            Map<String, Serializable> inputValues = new HashMap<>();

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

            final String dateDebutLegislatureEC = SolonDateConverter.DATE_SLASH.format(
                paramAn.getLegislatureEnCoursDateDebut()
            );
            final String dateDebutLegislaturePrec = SolonDateConverter.DATE_SLASH.format(
                paramAn.getLegislaturePrecedenteDateDebut()
            );

            ANReportEnum[] values = ANReportEnum.values();
            // Pour debug inte/demo: values = Arrays.stream(ANReportEnum.values()).filter(anReportEnum -> anReportEnum.getRptdesignId().equals("pan_stat34")).collect(Collectors.toList()).toArray(new ANReportEnum[]{});
            for (ANReportEnum aNReport : values) {
                if (aNReport.isBatchTriggerable()) {
                    long startReportTime = Calendar.getInstance().getTimeInMillis();
                    LOGGER.info(
                        session,
                        SSLogEnumImpl.BIRT_BATCH_TEC,
                        "TRIGGERING REPORT : " + aNReport.getRptdesignId() + "START"
                    );

                    generateOneReport(
                        session,
                        anService,
                        inputValues,
                        paramAn,
                        dateDebutLegislatureEC,
                        dateDebutLegislaturePrec,
                        aNReport
                    );

                    LOGGER.info(
                        session,
                        STLogEnumImpl.LOG_INFO_TEC,
                        "ENDING REPORT : " +
                        aNReport.getRptdesignId() +
                        " DURATION : " +
                        (System.currentTimeMillis() - startReportTime) +
                        " milliseconds"
                    );
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
            List<DocumentModel> documentModelList = anService.getAllActiviteNormative(
                session,
                ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId(),
                false
            );
            documentModelList.addAll(anService.getAllAplicationOrdonnanceDossiers(session, false));
            for (DocumentModel documentModel : documentModelList) {
                anService.generateANRepartitionMinistereHtml(
                    session,
                    documentModel.getAdapter(ActiviteNormative.class),
                    false
                );
                anService.publierTableauSuiviHTML(documentModel, session, false, true, false);
                session.save();
            }

            // Puis pour la législature courante
            documentModelList =
                anService.getAllActiviteNormative(session, ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId(), true);
            documentModelList.addAll(anService.getAllAplicationOrdonnanceDossiers(session, true));
            for (DocumentModel documentModel : documentModelList) {
                anService.generateANRepartitionMinistereHtml(
                    session,
                    documentModel.getAdapter(ActiviteNormative.class),
                    true
                );
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
            suiviBatchService.createBatchResultFor(
                batchLoggerModel,
                "Generation de resultat des statistiques avec birt",
                endTime - startTime
            );
        } catch (Throwable e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
        }

        removeOrphanBinaries(session);

        LOGGER.info(session, EpgLogEnumImpl.END_B_GENERATE_STATS_TEC);
    }

    private static void removeOrphanBinaries(CoreSession session) {
        Boolean removeOrphanBinariesIsEnable = STServiceLocator
            .getConfigService()
            .getBooleanValue(SOLONEPG_STATS_REMOVE_ORPHAN_BINARIES);
        if (BooleanUtils.isTrue(removeOrphanBinariesIsEnable)) {
            try {
                getRequiredService(AutomationService.class)
                    .run(new OperationContext(), GarbageCollectBinariesOperation.ID);
            } catch (OperationException e) {
                LOGGER.error(session, STLogEnumImpl.DEFAULT, e);
            }
        }
    }

    private void generateOneReport(
        CoreSession session,
        final ActiviteNormativeService anService,
        Map<String, Serializable> inputValues,
        final ParametrageAN paramAn,
        final String dateDebutLegislatureEC,
        final String dateDebutLegislaturePrec,
        ANReportEnum aNReport
    ) {
        try {
            // On génère une première fois pour la législature précédente
            inputValues.putAll(assignParameters(aNReport, false, paramAn));
            inputValues.put("LEGISLATURE_PARAM", dateDebutLegislaturePrec);
            LOGGER.info(session, SSLogEnumImpl.BIRT_BATCH_TEC, "ADDED LEGISLATURE_PARAM : " + dateDebutLegislaturePrec);

            if (aNReport.isForMinistere()) {
                generateReportForMin(aNReport, inputValues, session, false);
            } else if (aNReport.isForLoi()) {
                List<DocumentModel> applicationLoisLegisPrec = anService.getAllActiviteNormative(
                    session,
                    ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId(),
                    false
                );
                generateReportForLoi(aNReport, inputValues, session, applicationLoisLegisPrec, false);
            } else if (aNReport.isForTexte()) {
                List<DocumentModel> applicationLoisLegisPrec = anService.getAllAplicationOrdonnanceDossiers(
                    session,
                    false
                );
                generateReportForLoi(aNReport, inputValues, session, applicationLoisLegisPrec, false);
            } else if (aNReport.isReportCourbe()) {
                generateReportCourbeTous(aNReport, inputValues, session, false);
            } else {
                generateAllReportResult(session, aNReport.getFilename(), aNReport, inputValues, false);
            }

            // Puis une seconde fois pour la législature courante
            inputValues.putAll(assignParameters(aNReport, true, paramAn));
            inputValues.put("LEGISLATURE_PARAM", dateDebutLegislatureEC);

            if (aNReport.isForMinistere()) {
                generateReportForMin(aNReport, inputValues, session, true);
            } else if (aNReport.isForLoi()) {
                List<DocumentModel> applicationLoisLegisCourante = anService.getAllActiviteNormative(
                    session,
                    ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId(),
                    true
                );
                generateReportForLoi(aNReport, inputValues, session, applicationLoisLegisCourante, true);
            } else if (aNReport.isForTexte()) {
                List<DocumentModel> applicationLoisLegisCourante = anService.getAllAplicationOrdonnanceDossiers(
                    session,
                    true
                );
                generateReportForLoi(aNReport, inputValues, session, applicationLoisLegisCourante, true);
            } else if (aNReport.isReportCourbe()) {
                generateReportCourbeTous(aNReport, inputValues, session, true);
            } else {
                generateAllReportResult(session, aNReport.getFilename(), aNReport, inputValues, true);
            }

            TransactionHelper.commitOrRollbackTransaction();
            TransactionHelper.startTransaction();
        } catch (Throwable exc) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_GENERATE_STATS_TEC, aNReport.toString(), exc);
            errorCount++;
        }
    }

    private void generateAllReportResult(
        CoreSession session,
        String reportName,
        ANReportEnum report,
        Map<String, Serializable> inputValues,
        boolean curLegis
    ) {
        final long startTime = Calendar.getInstance().getTimeInMillis();
        final SuiviBatchService suiviBatchService = STServiceLocator.getSuiviBatchService();
        StatsGenerationResultatService statGenerationService = SolonEpgServiceLocator.getStatsGenerationResultatService();
        try {
            try {
                statGenerationService.generateAllReportResult(
                    session,
                    reportName,
                    SSServiceLocator.getBirtGenerationService().getReport(report.getRptdesignId()).getFile(),
                    inputValues,
                    report,
                    batchLoggerModel,
                    curLegis
                );
            } catch (Exception exc) {
                LOGGER.error(session, SSLogEnumImpl.FAIL_CREATE_BIRT_TEC, reportName, exc);
                errorCount++;
                try {
                    final long endTime = Calendar.getInstance().getTimeInMillis();
                    suiviBatchService.createBatchResultFor(
                        batchLoggerModel,
                        "Erreur lors de la generation du rapport : " + reportName,
                        endTime - startTime
                    );
                } catch (Exception e) {
                    LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
                }
            }
            try {
                final long endTime = Calendar.getInstance().getTimeInMillis();
                suiviBatchService.createBatchResultFor(
                    batchLoggerModel,
                    "Succès de la generation du rapport : " + reportName,
                    endTime - startTime
                );
                session.save();
            } catch (Exception e) {
                LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
            }
        } catch (Exception exc) {
            LOGGER.error(session, SSLogEnumImpl.FAIL_SAVE_BIRT_TEC, reportName, exc);
            errorCount++;
            try {
                final long endTime = Calendar.getInstance().getTimeInMillis();
                suiviBatchService.createBatchResultFor(
                    batchLoggerModel,
                    "Erreur lors de la sauvegarde du rapport : " + reportName,
                    endTime - startTime
                );
            } catch (Exception e) {
                LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
            }
        }
    }

    private Map<String, Serializable> assignParameters(
        ANReportEnum archAnReportEnum,
        boolean curLegis,
        ParametrageAN paramAn
    ) {
        Map<String, Serializable> inputValues = new HashMap<>();

        String debutLegislature;

        paramAn.assignParameters(archAnReportEnum.getPublishPanParamAnType(), inputValues, curLegis);

        if (curLegis) {
            debutLegislature = SolonDateConverter.DATE_SLASH.format(paramAn.getLegislatureEnCoursDateDebut());
            inputValues.put("LEGISLATURES", paramAn.getLegislatureEnCours());
            inputValues.put("LEGISLATURES_LABEL", "la " + paramAn.getLegislatureEnCours() + " législature");
        } else {
            debutLegislature = SolonDateConverter.DATE_SLASH.format(paramAn.getLegislaturePrecedenteDateDebut());
            int lastIndex = paramAn.getLegislatures().indexOf(paramAn.getLegislatureEnCours()) - 1;
            inputValues.put("LEGISLATURES", paramAn.getLegislatures().get(lastIndex));
            inputValues.put("LEGISLATURES_LABEL", "la " + paramAn.getLegislatures().get(lastIndex) + " législature");
        }

        inputValues.put("DEBUTLEGISLATURE_PARAM", debutLegislature);

        return inputValues;
    }

    private void generateReportForMin(
        ANReportEnum aNReport,
        Map<String, Serializable> inputValues,
        CoreSession session,
        boolean curLegis
    ) {
        final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
        List<String> ministeresList = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .getAllAplicationMinisteresList(session, ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId(), curLegis);

        LOGGER.info(session, SSLogEnumImpl.BIRT_BATCH_TEC, "GENERATE ALL MINISTERE");
        for (String ministere : ministeresList) {
            OrganigrammeNode currentMinistere = ministeresService.getEntiteNode(ministere);
            if (currentMinistere != null) {
                inputValues.put("MINISTEREPILOTE_PARAM", ministere);
                inputValues.put("MINISTEREPILOTELABEL_PARAM", currentMinistere.getLabel());

                LOGGER.info(session, SSLogEnumImpl.BIRT_BATCH_TEC, "\tMINISTEREPILOTE_PARAM : " + ministere);
                LOGGER.info(
                    session,
                    SSLogEnumImpl.BIRT_BATCH_TEC,
                    "\tREPORT NAME : " + aNReport.getFilename() + "-" + ministere
                );
                generateAllReportResult(
                    session,
                    aNReport.getFilename() + "-" + ministere,
                    aNReport,
                    inputValues,
                    curLegis
                );
            }
        }
    }

    private void generateReportForLoi(
        ANReportEnum aNReport,
        Map<String, Serializable> inputValues,
        CoreSession session,
        List<DocumentModel> applicationLoiList,
        boolean curLegis
    ) {
        LOGGER.info(session, SSLogEnumImpl.BIRT_BATCH_TEC, "GENERATE ALL LOI");
        for (DocumentModel documentModel : applicationLoiList) {
            String numeroNor = documentModel.getAdapter(TexteMaitre.class).getNumeroNor();
            inputValues.put("DOSSIERID_PARAM", documentModel.getId());
            LOGGER.info(session, SSLogEnumImpl.BIRT_BATCH_TEC, "\tDOSSIERID_PARAM : " + documentModel.getId());
            LOGGER.info(
                session,
                SSLogEnumImpl.BIRT_BATCH_TEC,
                "\tREPORT NAME : " + aNReport.getFilename() + "-" + numeroNor
            );
            generateAllReportResult(session, aNReport.getFilename() + "-" + numeroNor, aNReport, inputValues, curLegis);
        }
    }

    private void generateReportCourbeTous(
        ANReportEnum aNReport,
        Map<String, Serializable> inputValues,
        CoreSession session,
        boolean curLegis
    ) {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        String reportNameMonth = aNReport.getFilename() + "-" + year + "-" + month;
        String reportNameYear = aNReport.getFilename() + "-" + year;
        inputValues.put("PERIODE_PARAM", "A");
        inputValues.put("ANNEE_PARAM", String.valueOf(year));
        generateAllReportResult(session, reportNameYear, aNReport, inputValues, curLegis);

        inputValues.put("PERIODE_PARAM", "M");
        inputValues.put("ANNEE_PARAM", String.valueOf(year));
        inputValues.put("MOIS_PARAM", String.valueOf(month));
        generateAllReportResult(session, reportNameMonth, aNReport, inputValues, curLegis);
    }
}
