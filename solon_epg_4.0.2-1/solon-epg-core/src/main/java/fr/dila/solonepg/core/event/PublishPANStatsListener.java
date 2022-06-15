package fr.dila.solonepg.core.event;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
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
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SolonDateConverter;
import fr.sword.naiad.nuxeo.commons.core.util.SessionUtil;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.BooleanUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

public class PublishPANStatsListener implements PostCommitEventListener {
    private static final STLogger LOGGER = STLogFactory.getLog(PublishPANStatsListener.class);

    private boolean curLegis;

    @Override
    public void handleEvent(EventBundle events) {
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

        curLegis =
            BooleanUtils.isNotFalse((Boolean) eventProperties.get(SolonEpgANEventConstants.EXPORT_PAN_CURLEGISLATURE));

        final String user = (String) eventProperties.get(SolonEpgANEventConstants.USER_PROPERTY);
        final String dateRequest = SolonDateConverter.DATETIME_SLASH_A_MINUTE_COLON.formatNow();

        Map<String, Serializable> inputValues = new HashMap<>();

        final ActiviteNormativeService anService = SolonEpgServiceLocator.getActiviteNormativeService();
        final SolonEpgParametreService anParamService = SolonEpgServiceLocator.getSolonEpgParametreService();
        CoreSession session = null;

        try {
            session = SessionUtil.openSession();

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

            ANReportEnum[] values = ANReportEnum.values();
            // Pour debug inte/demo: values = Arrays.stream(ANReportEnum.values()).filter(anReportEnum -> anReportEnum.getRptdesignId().equals("pan_stat34")).collect(Collectors.toList()).toArray(new ANReportEnum[]{});
            for (ANReportEnum aNReport : values) {
                if (aNReport.isBatchTriggerable()) {
                    errors = publishStat(inputValues, anService, session, errors, errorBuilder, paramAn, aNReport);
                }
            }

            anService.updateLoiListePubliee(session, curLegis);
            anService.updateOrdonnancesListePubliee(session, curLegis);
            anService.updateHabilitationListePubliee(session, curLegis);
            List<DocumentModel> documentModelList = anService.getAllActiviteNormative(
                session,
                ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId(),
                curLegis
            );
            LOGGER.info(
                STLogEnumImpl.DEFAULT,
                "On s'apprête à publier les statistiques pour " + documentModelList.size() + " applications des lois"
            );
            for (DocumentModel documentModel : documentModelList) {
                anService.generateANRepartitionMinistereHtml(
                    session,
                    documentModel.getAdapter(ActiviteNormative.class),
                    curLegis
                );
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
                SessionUtil.closeSession(session);
            }
        }
    }

    private int publishStat(
        Map<String, Serializable> inputValues,
        final ActiviteNormativeService anService,
        CoreSession session,
        int errors,
        StringBuilder errorBuilder,
        final ParametrageAN paramAn,
        ANReportEnum aNReport
    ) {
        final StatsGenerationResultatService statGenerationService = SolonEpgServiceLocator.getStatsGenerationResultatService();
        String ministeresParam = statGenerationService.getMinisteresListBirtReportParam(session);
        String directionsParam = statGenerationService.getDirectionsListBirtReportParam();
        if (aNReport.isBatchPubliable()) {
            try {
                inputValues.clear();
                inputValues.put("MINISTERES_PARAM", ministeresParam);
                inputValues.put("DIRECTIONS_PARAM", directionsParam);
                inputValues.putAll(assignParameters(aNReport, curLegis, paramAn));

                if (aNReport.isForMinistere()) {
                    generateReportForMin(aNReport, inputValues, session);
                } else if (aNReport.isForLoi()) {
                    List<DocumentModel> applicationLoisLegisCourante = anService.getAllActiviteNormative(
                        session,
                        ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId(),
                        curLegis
                    );
                    generateReportForTexte(aNReport, inputValues, session, applicationLoisLegisCourante);
                } else if (aNReport.isForTexte()) {
                    List<DocumentModel> applicationLoisLegisCourante = anService.getAllAplicationOrdonnanceDossiers(
                        session,
                        curLegis
                    );
                    generateReportForTexte(aNReport, inputValues, session, applicationLoisLegisCourante);
                } else if (aNReport.isReportCourbe()) {
                    generateReportCourbeTous(aNReport, inputValues, session);
                } else {
                    generateReportResult(session, aNReport.getFilename(), aNReport, inputValues);
                }
            } catch (Exception exc) {
                LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_GENERATE_STATS_TEC, exc);
                errors++;
                errorBuilder
                    .append("Impossible de publier la statistique \"")
                    .append(aNReport.getLibelle())
                    .append("\", cause : ")
                    .append(exc.getMessage())
                    .append("\n");
            }
        }
        return errors;
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

            int lastIndex = paramAn.getLegislatures().size() - 2 >= 0
                ? paramAn.getLegislatures().indexOf(paramAn.getLegislatureEnCours()) - 1
                : 0;
            inputValues.put("LEGISLATURES", paramAn.getLegislatures().get(lastIndex));
            inputValues.put("LEGISLATURES_LABEL", "la " + paramAn.getLegislatures().get(lastIndex) + " législature");
        }

        inputValues.put("DEBUTLEGISLATURE_PARAM", debutLegislature);

        return inputValues;
    }

    private void generateReportForMin(
        ANReportEnum aNReport,
        Map<String, Serializable> inputValues,
        CoreSession session
    ) {
        final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
        List<String> ministeresList = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .getAllAplicationMinisteresList(session, ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId(), curLegis);

        for (String ministere : ministeresList) {
            OrganigrammeNode currentMinistere = ministeresService.getEntiteNode(ministere);
            if (currentMinistere != null) {
                inputValues.put("MINISTEREPILOTE_PARAM", ministere);
                inputValues.put("MINISTEREPILOTELABEL_PARAM", currentMinistere.getLabel());
                generateReportResult(session, aNReport.getFilename() + "-" + ministere, aNReport, inputValues);
            }
        }
    }

    private void generateReportForTexte(
        ANReportEnum aNReport,
        Map<String, Serializable> inputValues,
        CoreSession session,
        List<DocumentModel> applicationLoiList
    ) {
        for (DocumentModel documentModel : applicationLoiList) {
            String numeroNor = documentModel.getAdapter(TexteMaitre.class).getNumeroNor();
            inputValues.put("DOSSIERID_PARAM", documentModel.getId());
            generateReportResult(session, aNReport.getFilename() + "-" + numeroNor, aNReport, inputValues);
        }
    }

    private void generateReportCourbeTous(
        ANReportEnum aNReport,
        Map<String, Serializable> inputValues,
        CoreSession session
    ) {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        String reportNameMonth = aNReport.getFilename() + "-" + year + "-" + month;
        String reportNameYear = aNReport.getFilename() + "-" + year;
        inputValues.put("PERIODE_PARAM", "A");
        inputValues.put("ANNEE_PARAM", String.valueOf(year));
        generateReportResult(session, reportNameYear, aNReport, inputValues);

        inputValues.put("PERIODE_PARAM", "M");
        inputValues.put("ANNEE_PARAM", String.valueOf(year));
        inputValues.put("MOIS_PARAM", String.valueOf(month));
        generateReportResult(session, reportNameMonth, aNReport, inputValues);
    }

    private void generateReportResult(
        CoreSession session,
        String reportName,
        ANReportEnum report,
        Map<String, Serializable> inputValues
    ) {
        StatsGenerationResultatService statGenerationService = SolonEpgServiceLocator.getStatsGenerationResultatService();

        if (report.isBatchPubliable()) {
            Blob tempHtmlContent = statGenerationService.generateReportResult(
                reportName,
                SSServiceLocator.getBirtGenerationService().getReport(report.getRptdesignId()).getFile(),
                inputValues,
                BirtOutputFormat.HTML
            );
            SolonEpgServiceLocator
                .getStatsGenerationResultatService()
                .publierReportResulat(reportName, tempHtmlContent, curLegis, session);
        }
    }

    private void sendMailOK(CoreSession session, String user, String dateRequest) {
        String userMail = STServiceLocator.getSTUserService().getUserInfo(user, "m");
        String objet = "[SOLON-EPG] Votre demande de publication des statistiques ";
        String corpsTemplate =
            "Bonjour,\n\nLa publication des statistiques, demandée le " + dateRequest + ", est terminée.";
        sendMail(session, userMail, objet, corpsTemplate);
    }

    private void sendMailKO(CoreSession session, String user, String dateRequest, String messageStack) {
        String userMail = STServiceLocator.getSTUserService().getUserInfo(user, "m");
        String objet = "[SOLON-EPG] Votre demande de publication des statistiques ";
        String corpsTemplate =
            "Bonjour,\n\nLa publication des statistiques, demandée le " +
            dateRequest +
            ", a échoué. " +
            "Le message remonté est le suivant : \n" +
            messageStack;

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
        } catch (final NuxeoException exc) {
            LOGGER.error(session, STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
        }
    }
}
