package fr.dila.solonepg.ui.services.pan.impl;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.activitenormative.ANReport;
import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.birt.BirtRefreshFichier;
import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.ActiviteNormativeStatsService;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.PANExportParametreDTO;
import fr.dila.solonepg.ui.bean.pan.ReportResultatData;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.ActiviteNormativeStatsUIService;
import fr.dila.solonepg.ui.services.pan.PanUIService;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Bean Seam de l'espace d'activite normative
 *
 * @author arammal
 */
public class ActiviteNormativeStatsUIServiceImpl implements ActiviteNormativeStatsUIService {
    private static final String LEGISLATURES_LABEL = "LEGISLATURES_LABEL";
    private static final String LEGISLATURES = "LEGISLATURES";
    private static final String LEGISLATURE_PARAM = "LEGISLATURE_PARAM";
    private static final String MINISTEREPILOTELABEL_PARAM = "MINISTEREPILOTELABEL_PARAM";
    private static final String MINISTEREPILOTE_PARAM = "MINISTEREPILOTE_PARAM";
    private static final String DIRECTIONS_PARAM = "DIRECTIONS_PARAM";
    private static final STLogger LOGGER = STLogFactory.getLog(ActiviteNormativeStatsUIServiceImpl.class);

    @Override
    public ReportResultatData initReportResultatData(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        ANReport currentReport = context.getFromContextData(PanContextDataKey.AN_REPORT);
        ParametrageAN parameterStatsDoc = SolonEpgServiceLocator
            .getSolonEpgParametreService()
            .getDocAnParametre(documentManager);
        PANExportParametreDTO statDefaultValue = context.getFromContextData(PanContextDataKey.PAN_EXPORT_PARAMETRE_DTO);

        ReportResultatData rrd = new ReportResultatData();
        final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
        if (anStatsService.existBirtRefreshForCurrentUser(documentManager, currentReport)) {
            DocumentModel birtRefreshFichier = anStatsService.getBirtRefreshDocForCurrentUser(
                documentManager,
                currentReport
            );
            if (birtRefreshFichier != null) {
                BirtRefreshFichier birtRefresh = birtRefreshFichier.getAdapter(BirtRefreshFichier.class);
                rrd.setHtmlContent(birtRefresh.getHtmlContent());
                rrd.setPdfContent(birtRefresh.getPdfContent());
                rrd.setXlsContent(birtRefresh.getXlsContent());
                rrd.setParamList(birtRefresh.getParamList());
            }
        } else {
            DocumentModel birtResultatFichier = SolonEpgServiceLocator
                .getStatsGenerationResultatService()
                .getBirtResultatFichier(documentManager, currentReport.getName());
            if (birtResultatFichier != null) {
                rrd.setHtmlContent(
                    (Blob) birtResultatFichier.getProperty(
                        ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
                        ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PROPERTY
                    )
                );
                rrd.setPdfContent(
                    (Blob) birtResultatFichier.getProperty(
                        ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
                        ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PDF_PROPERTY
                    )
                );
                rrd.setXlsContent(
                    (Blob) birtResultatFichier.getProperty(
                        ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
                        ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_CSV_PROPERTY
                    )
                );
            }
        }

        if (parameterStatsDoc != null) {
            refreshStatDefaultValue(statDefaultValue, parameterStatsDoc);
        }

        return rrd;
    }

    public void saveResultat(CoreSession documentManager, ANReport currentReport, ReportResultatData rrd) {
        SolonEpgServiceLocator
            .getStatsGenerationResultatService()
            .saveReportResulat(
                documentManager,
                currentReport.getName(),
                rrd.getHtmlContent(),
                rrd.getPdfContent(),
                rrd.getXlsContent()
            );
    }

    public void publierResultat(
        CoreSession documentManager,
        ANReport currentReport,
        ReportResultatData rrd,
        OrganigrammeNode currentMinistere,
        String legislatureSelectionnee,
        PANExportParametreDTO statDefaultValue,
        ParametrageAN parameterStatsDoc
    ) {
        saveResultat(documentManager, currentReport, rrd);

        ANReportEnum report = currentReport.getType();
        if (
            report == ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN ||
            report == ANReportEnum.TAB_AN_MESURE_APPLICATION_MIN_APP_ORDO
        ) {
            publierResultatMinistere(
                documentManager,
                currentMinistere,
                legislatureSelectionnee,
                statDefaultValue,
                report
            );
        } else {
            SolonEpgServiceLocator
                .getStatsGenerationResultatService()
                .publierReportResulat(currentReport.getName(), rrd.getHtmlContent(), true, documentManager);

            ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();

            if (
                activiteNormativeService.isPublicationBilanSemestrielsBdjActivated(documentManager) &&
                rrd.getParamList() != null
            ) {
                try {
                    JSONObject paramListStr = new JSONObject(rrd.getParamList());
                    Date debutIntervalle1 = SolonDateConverter
                        .DATE_SLASH.parseToCalendar((String) paramListStr.get("DEBUT_INTERVALLE1_PARAM"))
                        .getTime();
                    Date finIntervalle1 = SolonDateConverter
                        .DATE_SLASH.parseToCalendar((String) paramListStr.get("FIN_INTERVALLE1_PARAM"))
                        .getTime();
                    Date debutIntervalle2 = SolonDateConverter
                        .DATE_SLASH.parseToCalendar((String) paramListStr.get("DEBUT_INTERVALLE2_PARAM"))
                        .getTime();
                    Date finIntervalle2 = SolonDateConverter
                        .DATE_SLASH.parseToCalendar((String) paramListStr.get("FIN_INTERVALLE2_PARAM"))
                        .getTime();

                    if (currentReport.getType() == ANReportEnum.TAB_AN_BILAN_SEM_LOI_TOUS) {
                        activiteNormativeService.publierBilanSemestrielLoiBDJ(
                            documentManager,
                            parameterStatsDoc.getLegislatureEnCours(),
                            debutIntervalle1,
                            finIntervalle1,
                            debutIntervalle2,
                            finIntervalle2
                        );
                    } else if (currentReport.getType() == ANReportEnum.TAB_AN_BILAN_SEM_ORDONNANCE_TOUS) {
                        activiteNormativeService.publierBilanSemestrielOrdonnanceBDJ(
                            documentManager,
                            parameterStatsDoc.getLegislatureEnCours(),
                            debutIntervalle1,
                            finIntervalle1,
                            debutIntervalle2,
                            finIntervalle2
                        );
                    }
                } catch (JSONException e) {
                    LOGGER.error(EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e);
                }
            }
        }

        // Ajout dans le journal du PAN
        STServiceLocator
            .getJournalService()
            .journaliserActionPAN(
                documentManager,
                null,
                SolonEpgEventConstant.PUBLI_STAT_EVENT,
                SolonEpgEventConstant.PUBLI_STAT_COMMENT + " [" + currentReport.getType().getLibelle() + "]",
                SolonEpgEventConstant.CATEGORY_LOG_PAN_GENERAL
            );
    }

    private void publierResultatMinistere(
        CoreSession documentManager,
        OrganigrammeNode currentMinistere,
        String legislatureSelectionnee,
        PANExportParametreDTO statDefaultValue,
        ANReportEnum report
    ) {
        Map<String, Serializable> inputValues = new HashMap<>();
        inputValues.put(
            DIRECTIONS_PARAM,
            SolonEpgServiceLocator.getStatsGenerationResultatService().getDirectionsListBirtReportParam()
        );
        inputValues.put(MINISTEREPILOTE_PARAM, currentMinistere.getId());
        inputValues.put(MINISTEREPILOTELABEL_PARAM, currentMinistere.getLabel());
        inputValues.put(
            LEGISLATURE_PARAM,
            SolonDateConverter.DATE_SLASH.format(statDefaultValue.getDateDebutLegislature())
        );

        String legisValue = StringUtils.defaultIfEmpty(legislatureSelectionnee, " ");

        inputValues.put(LEGISLATURES, legisValue);
        inputValues.put(LEGISLATURES_LABEL, buildLegislatureParamLabel(legislatureSelectionnee));
        String reportName = report.getFilename() + "-" + currentMinistere.getId();
        Blob tempHtmlContent = SolonEpgServiceLocator
            .getStatsGenerationResultatService()
            .generateReportResult(
                reportName,
                SSServiceLocator.getBirtGenerationService().getReport(report.getRptdesignId()).getFile(),
                inputValues,
                BirtOutputFormat.HTML
            );
        SolonEpgServiceLocator
            .getStatsGenerationResultatService()
            .publierReportResulat(reportName, tempHtmlContent, true, documentManager);
    }

    @Override
    public boolean isBilanSemestriel(SpecificContext context) {
        ANReport currentReport = context.getFromContextData(PanContextDataKey.AN_REPORT);
        return (
            currentReport.getType() == ANReportEnum.TAB_AN_BILAN_SEM_LOI_TOUS ||
            currentReport.getType() == ANReportEnum.TAB_AN_BILAN_SEM_ORDONNANCE_TOUS
        );
    }

    @Override
    public String refreshBirtResultat(SpecificContext context) {
        CoreSession documentManager = context.getSession();

        SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
        ANReport currentReport = context.getFromContextData(PanContextDataKey.AN_REPORT);
        PANExportParametreDTO statDefaultValue = context.getFromContextData(PanContextDataKey.PAN_EXPORT_PARAMETRE_DTO);
        SolonEpgServiceLocator.getSolonEpgParametreService().getDocAnParametre(documentManager);
        boolean periodeParMois = context.getFromContextData(PanContextDataKey.IS_PERIODE_PAR_MOIS);
        int mois = context.getFromContextData(PanContextDataKey.MOIS);

        int annee = context.getFromContextData(PanContextDataKey.ANNEE);
        String legislatureSelectionnee = context.getFromContextData(PanContextDataKey.LEGISLATURE_INPUT);

        String dossierId = context.getFromContextData(PanContextDataKey.DOSSIER_ID);
        OrganigrammeNode currentMinistere = STServiceLocator
            .getSTMinisteresService()
            .getEntiteNode(context.getFromContextData(PanContextDataKey.MINISTERE_PILOTE_ID));

        final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
        final String currentUser = ssPrincipal.getName();
        final String userWorkspacePath = STServiceLocator
            .getUserWorkspaceService()
            .getCurrentUserPersonalWorkspace(documentManager)
            .getPathAsString();
        if (
            !anStatsService.isCurrentlyRefreshing(documentManager, currentReport, currentUser) &&
            anStatsService.flagRefreshFor(documentManager, currentReport, currentUser, userWorkspacePath)
        ) {
            // On peut lancer l'évènement de rafraichissement pour ce rapport et rendre la
            // main à l'utilisateur
            // en lui indiquant que sa demande a été prise en compte et qu'il recevra un
            // mail (si dispo)
            // quand le rafraichissement sera terminé.
            Map<String, String> inputValues = new HashMap<>();
            String ministeresParam = SolonEpgServiceLocator
                .getStatsGenerationResultatService()
                .getMinisteresListBirtReportParam(documentManager);
            String directionsParam = SolonEpgServiceLocator
                .getStatsGenerationResultatService()
                .getDirectionsListBirtReportParam();
            Date promulgationDebut = null;
            Date promulgationFin = null;
            Date publicationDebut = null;
            Date publicationFin = null;

            inputValues.put("MINISTERES_PARAM", ministeresParam);
            inputValues.put(DIRECTIONS_PARAM, directionsParam);
            inputValues.put(
                "DEBUTLEGISLATURE_PARAM",
                SolonDateConverter.DATE_SLASH.format(statDefaultValue.getDateDebutLegislature())
            );
            inputValues.put("PERIODE_PARAM", periodeParMois ? "M" : "A");
            inputValues.put("MOIS_PARAM", String.valueOf(mois));
            inputValues.put("ANNEE_PARAM", String.valueOf(annee));

            // Legislature en cours parametre
            inputValues.put(
                LEGISLATURE_PARAM,
                SolonDateConverter.DATE_SLASH.format(statDefaultValue.getDateDebutLegislature())
            );
            String legisValue = StringUtils.defaultIfEmpty(legislatureSelectionnee, " ");

            inputValues.put(LEGISLATURES, legisValue);
            inputValues.put(LEGISLATURES_LABEL, buildLegislatureParamLabel(legislatureSelectionnee));

            if (
                ANReportEnum.TAB_AN_STAT_DEBUT_LEGISLATURE.equals(currentReport.getType()) ||
                ANReportEnum.TAB_AN_DEBUT_LEGISLATURE_MIN.equals(currentReport.getType()) ||
                ANReportEnum.TAB_AN_STAT_MISE_APPLICATION.equals(currentReport.getType())
            ) {
                promulgationDebut = statDefaultValue.getTauxPromulgationDebut();
                promulgationFin = statDefaultValue.getTauxPromulgationFin();
                publicationDebut = statDefaultValue.getTauxPublicationDebut();
                publicationFin = statDefaultValue.getTauxPublicationFin();
            } else if (
                ANReportEnum.TAB_AN_INDIC_APP_ORDO_DEBUT_LEGISLATURE.equals(currentReport.getType()) ||
                ANReportEnum.TAB_AN_INDIC_APP_ORDO_MISE_APPLI.equals(currentReport.getType()) ||
                ANReportEnum.TAB_AN_DEBUT_LEGISLATURE_MIN_APP_ORDO.equals(currentReport.getType())
            ) {
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
            } else if (
                ANReportEnum.TAB_AN_BILAN_SEM_LOI_TOUS.equals(currentReport.getType()) ||
                ANReportEnum.TAB_AN_BILAN_SEM_MIN_TOUS.equals(currentReport.getType()) ||
                ANReportEnum.TAB_AN_BILAN_SEM_NATURE.equals(currentReport.getType()) ||
                ANReportEnum.TAB_AN_BILAN_SEM_VOTE.equals(currentReport.getType()) ||
                ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_LOI.equals(currentReport.getType()) ||
                ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_MIN.equals(currentReport.getType())
            ) {
                promulgationDebut = statDefaultValue.getBilanPromulgationDebut();
                promulgationFin = statDefaultValue.getBilanPromulgationFin();
                publicationDebut = statDefaultValue.getBilanPublicationDebut();
                publicationFin = statDefaultValue.getBilanPublicationFin();
            } else if (
                ANReportEnum.TAB_AN_BILAN_SEM_ORDONNANCE_TOUS.equals(currentReport.getType()) ||
                ANReportEnum.TAB_AN_BILAN_SEM_MIN_TOUS_ORDONNANCE.equals(currentReport.getType()) ||
                ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_SUB_APP_ORDONNANCES.equals(currentReport.getType()) ||
                ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_MIN_APP_ORDO.equals(currentReport.getType())
            ) {
                promulgationDebut = statDefaultValue.getBilanPublicationDebutOrdo();
                promulgationFin = statDefaultValue.getBilanPublicationFinOrdo();
                publicationDebut = statDefaultValue.getBilanPublicationDebutOrdoDecret();
                publicationFin = statDefaultValue.getBilanPublicationFinOrdoDecret();
            }

            inputValues.put("DEBUT_INTERVALLE1_PARAM", SolonDateConverter.DATE_SLASH.format(promulgationDebut));
            inputValues.put("FIN_INTERVALLE1_PARAM", SolonDateConverter.DATE_SLASH.format(promulgationFin));
            inputValues.put("DEBUT_INTERVALLE2_PARAM", SolonDateConverter.DATE_SLASH.format(publicationDebut));
            inputValues.put("FIN_INTERVALLE2_PARAM", SolonDateConverter.DATE_SLASH.format(publicationFin));

            if (currentReport.getType().isForLoi()) {
                inputValues.put("DOSSIERID_PARAM", dossierId);
            } else if (
                ANReportEnum.TAB_AN_BILAN_SEMESTRIEL_SUB_APP_ORDONNANCES == currentReport.getType() ||
                ANReportEnum.TAB_AN_TAUX_APPLICATION_ORDO == currentReport.getType() ||
                ANReportEnum.TAB_AN_TAUX_APPLICATION_SUB_APP_ORDONNANCES_ALL == currentReport.getType() ||
                ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES_ALL == currentReport.getType() ||
                ANReportEnum.TAB_AN_MESURES_APP_ORDONNANCES == currentReport.getType()
            ) {
                inputValues.put("DOSSIERID_PARAM", dossierId);
            } else if (currentReport.getType().isForMinistere()) {
                inputValues.put(MINISTEREPILOTE_PARAM, currentMinistere.getId());
                inputValues.put(MINISTEREPILOTELABEL_PARAM, currentMinistere.getLabel());
            }

            return "";
        } else {
            // On affiche à l'utilisateur qu'un rafraichissement a déjà été demandé et est
            // en cours
            String dateRequest = anStatsService.getHorodatageRequest(documentManager, currentReport, currentUser);
            return "La mise à jour a déjà été demandée le " + dateRequest;
        }
    }

    private String buildLegislatureParamLabel(String legislatureSelectionnee) {
        StringBuilder builder = new StringBuilder("la ");
        if (StringUtils.isBlank(legislatureSelectionnee)) {
            builder.append("législature non renseignée");
        } else {
            builder.append(legislatureSelectionnee + " législature");
        }
        return builder.toString();
    }

    @Override
    public boolean canBeRefresh(SpecificContext context) {
        CoreSession documentManager = context.getSession();

        SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
        ANReport currentReport = context.getFromContextData(PanContextDataKey.AN_REPORT);
        final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
        final String currentUser = ssPrincipal.getName();

        return !anStatsService.isCurrentlyRefreshing(documentManager, currentReport, currentUser);
    }

    @Override
    public String getHorodatageRequest(SpecificContext context) {
        CoreSession documentManager = context.getSession();

        SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
        ANReport currentReport = context.getFromContextData(PanContextDataKey.AN_REPORT);
        final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
        final String currentUser = ssPrincipal.getName();
        return anStatsService.getHorodatageRequest(documentManager, currentReport, currentUser);
    }

    /**
     * afficher le contenu de resultat de birt en format html
     */
    public String displayBirtResultatHtml(Blob htmlContent, HttpServletResponse response) {
        if (htmlContent == null) {
            return "espace_activite_normative_stats_non_disponible";
        } else {
            if (response == null) {
                return "";
            }
            response.reset();
            response.setContentType("text/html;charset=utf-8");
            try (
                OutputStream outputStream = response.getOutputStream();
                InputStream inputStream = htmlContent.getStream();
            ) {
                BufferedInputStream fif = new BufferedInputStream(inputStream);
                // copie le fichier dans le flux de sortie
                int data;
                while ((data = fif.read()) != -1) {
                    outputStream.write(data);
                }
                if (outputStream != null) {
                    outputStream.flush();
                }
            } catch (Exception e) {
                LOGGER.error(STLogEnumImpl.FAIL_GENERATE_HTML_TEC, e);
            }

            return "";
        }
    }

    /**
     * afficher le contenu de resultat de birt en format pdf
     */
    public void displayBirtResultatPdf(Blob pdfContent, ANReport currentReport, HttpServletResponse response) {
        if (pdfContent != null) {
            if (response == null) {
                return;
            }

            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + currentReport.getName() + ".pdf");

            BufferedInputStream fif = null;
            try (
                OutputStream outputStream = response.getOutputStream();
                InputStream inputStream = pdfContent.getStream();
            ) {
                // récupération réponse
                fif = new BufferedInputStream(inputStream);

                // copie le fichier dans le flux de sortie
                int data;
                while ((data = fif.read()) != -1) {
                    outputStream.write(data);
                }
                if (outputStream != null) {
                    outputStream.flush();
                }
            } catch (Exception e) {
                LOGGER.error(STLogEnumImpl.FAIL_CREATE_PDF_TEC, e);
            }
        }
    }

    private String findReportName(ANReport currentReport, OrganigrammeNode currentMinistere) {
        if (currentReport != null) {
            EntiteNode node = currentMinistere == null ? null : (EntiteNode) currentMinistere;
            String ministerNor = node == null ? null : node.getNorMinistere();
            return currentReport.getType().getFileName(ministerNor);
        }

        return null;
    }

    /**
     * afficher le contenu de resultat de birt en format pdf
     */
    public void displayBirtResultatXsl(
        ReportResultatData rrd,
        ANReport currentReport,
        OrganigrammeNode currentMinistere,
        HttpServletResponse response
    ) {
        if (rrd.getXlsContent() != null) {
            if (response == null) {
                return;
            }
            BufferedInputStream fif = null;

            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");

            response.addHeader(
                "Content-Disposition",
                "attachment; filename=\"" + findReportName(currentReport, currentMinistere) + "\""
            );

            try (
                OutputStream outputStream = response.getOutputStream();
                InputStream inputStream = rrd.getXlsContent().getStream();
            ) {
                // récupération réponse
                fif = new BufferedInputStream(inputStream);
                // copie le fichier dans le flux de sortie
                int data;
                while ((data = fif.read()) != -1) {
                    outputStream.write(data);
                }
                if (outputStream != null) {
                    outputStream.flush();
                }
            } catch (Exception e) {
                LOGGER.error(STLogEnumImpl.FAIL_CREATE_EXCEL_TEC, e);
            }
        }
    }

    @Override
    public boolean isDisplayLienPubliee(SpecificContext context) {
        ANReport currentReport = context.getFromContextData(PanContextDataKey.AN_REPORT);
        return (
            currentReport != null && currentReport.getType() != null && currentReport.getType().isDisplayLienPubliee()
        );
    }

    /**
     * tester s'il faut afficher le lien publie
     */
    public boolean isDisplayPublierLink(SpecificContext context, ANReport currentReport, ReportResultatData rrd) {
        PanUIService service = PanUIServiceLocator.getPanUIService();
        return (
            rrd.getHtmlContent() != null &&
            currentReport != null &&
            currentReport.getType() != null &&
            isDisplayPublierLinkInOneSection(context, currentReport, service)
        );
    }

    private boolean isDisplayPublierLinkInOneSection(
        SpecificContext context,
        ANReport currentReport,
        PanUIService service
    ) {
        return (
            isDisplayPublierLinkApplicationLois(context, currentReport, service) ||
            service.isInTransposition(context) ||
            service.isInOrdonnances(context) ||
            service.isInOrdonnances38C(context) ||
            service.isInTraiteAccord(context) ||
            service.isInApplicationDesOrdonnances(context)
        );
    }

    private boolean isDisplayPublierLinkApplicationLois(
        SpecificContext context,
        ANReport currentReport,
        PanUIService service
    ) {
        return (
            service.isInApplicationDesLois(context) &&
            !currentReport.getType().equals(ANReportEnum.TAB_AN_DELAI_NATURE_TOUS)
        );
    }

    @Override
    public boolean isActiviteNormativeUpdater(SpecificContext context) {
        CoreSession documentManager = context.getSession();

        SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
        PanUIService service = PanUIServiceLocator.getPanUIService();
        return (
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ACTIVITE_NORMATIVE_UPDATER) ||
            isANSectionUpdater(context, ssPrincipal, service)
        );
    }

    private boolean isANSectionUpdater(SpecificContext context, SSPrincipal ssPrincipal, PanUIService service) {
        return (
            isApplicationDesLoisUpdater(context, ssPrincipal, service) ||
            isRatificationOrdonnancesUpdater(context, ssPrincipal, service) ||
            isOrdonnances38CUpdater(context, ssPrincipal, service) ||
            isTraitesUpdater(context, ssPrincipal, service) ||
            isTranspositionUpdater(context, ssPrincipal, service) ||
            isApplicationDesOrdonnacesUpdater(context, ssPrincipal, service)
        );
    }

    private boolean isApplicationDesOrdonnacesUpdater(
        SpecificContext context,
        SSPrincipal ssPrincipal,
        PanUIService service
    ) {
        return (
            service.isInApplicationDesOrdonnances(context) &&
            ssPrincipal.isMemberOf(
                SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_UPDATER
            )
        );
    }

    private boolean isTranspositionUpdater(SpecificContext context, SSPrincipal ssPrincipal, PanUIService service) {
        return (
            service.isInTransposition(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_UPDATER)
        );
    }

    private boolean isTraitesUpdater(SpecificContext context, SSPrincipal ssPrincipal, PanUIService service) {
        return (
            service.isInTraiteAccord(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_UPDATER)
        );
    }

    private boolean isOrdonnances38CUpdater(SpecificContext context, SSPrincipal ssPrincipal, PanUIService service) {
        return (
            service.isInOrdonnances38C(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_UPDATER)
        );
    }

    private boolean isRatificationOrdonnancesUpdater(
        SpecificContext context,
        SSPrincipal ssPrincipal,
        PanUIService service
    ) {
        return (
            service.isInOrdonnances(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_UPDATER)
        );
    }

    private boolean isApplicationDesLoisUpdater(
        SpecificContext context,
        SSPrincipal ssPrincipal,
        PanUIService service
    ) {
        return (
            service.isInApplicationDesLois(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER)
        );
    }

    @Override
    public boolean isActiviteNormativeReader(SpecificContext context) {
        CoreSession documentManager = context.getSession();

        SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
        PanUIService service = PanUIServiceLocator.getPanUIService();
        return (
            isApplicationLoisReader(context, ssPrincipal, service) ||
            isRatificationReader(context, ssPrincipal, service) ||
            isOrdonnances38CReader(context, ssPrincipal, service) ||
            isTraitesReader(context, ssPrincipal, service) ||
            isTranspositionsReader(context, ssPrincipal, service) ||
            isApplicationOrdonnancesReader(context, ssPrincipal, service)
        );
    }

    private boolean isApplicationOrdonnancesReader(
        SpecificContext context,
        SSPrincipal ssPrincipal,
        PanUIService service
    ) {
        return (
            service.isInApplicationDesOrdonnances(context) &&
            ssPrincipal.isMemberOf(
                SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_READER
            )
        );
    }

    private boolean isTranspositionsReader(SpecificContext context, SSPrincipal ssPrincipal, PanUIService service) {
        return (
            service.isInTransposition(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_READER)
        );
    }

    private boolean isTraitesReader(SpecificContext context, SSPrincipal ssPrincipal, PanUIService service) {
        return (
            service.isInTraiteAccord(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_READER)
        );
    }

    private boolean isOrdonnances38CReader(SpecificContext context, SSPrincipal ssPrincipal, PanUIService service) {
        return (
            service.isInOrdonnances38C(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_READER)
        );
    }

    private boolean isRatificationReader(SpecificContext context, SSPrincipal ssPrincipal, PanUIService service) {
        return (
            service.isInOrdonnances(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_READER)
        );
    }

    private boolean isApplicationLoisReader(SpecificContext context, SSPrincipal ssPrincipal, PanUIService service) {
        return (
            service.isInApplicationDesLois(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER)
        );
    }

    @Override
    public int getAnneeDeDepart(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        int retYear = 2000;
        STParametreService paramService = STServiceLocator.getSTParametreService();

        try {
            retYear =
                Integer.parseInt(
                    paramService.getParametreValue(documentManager, "annee-depart-statistique-mesure-publication")
                );
        } catch (Exception e) {
            LOGGER.error(
                documentManager,
                STLogEnumImpl.FAIL_GET_PARAM_TEC,
                "Paramètre : \"annee-depart-statistique-mesure-publication\"",
                e
            );
        }

        return retYear;
    }

    @Override
    public ParametrageAN getParameterStatsDoc(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        ParametrageAN parameterStatsDoc = null;

        PANExportParametreDTO statDefaultValue = new PANExportParametreDTO();
        SolonEpgParametreService paramEPGservice = SolonEpgServiceLocator.getSolonEpgParametreService();
        parameterStatsDoc = paramEPGservice.getDocAnParametre(documentManager);
        refreshStatDefaultValue(statDefaultValue, parameterStatsDoc);

        return parameterStatsDoc;
    }

    private void refreshStatDefaultValue(PANExportParametreDTO statDefaultValue, ParametrageAN parameterStatsDoc) {
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

        statDefaultValue.setTauxDLPublicationDecretsOrdosDebut(
            parameterStatsDoc.getLECTauxDebutLegisDatePubliDecretOrdoBorneInf()
        );
        statDefaultValue.setTauxDLPublicationDecretsOrdosFin(
            parameterStatsDoc.getLECTauxDebutLegisDatePubliDecretOrdoBorneSup()
        );

        // Taux d'exécution des lois (au cours de la dernière session parlementaire)
        statDefaultValue.setTauxSPPublicationOrdosDebut(parameterStatsDoc.getLECTauxSPDatePubliOrdoBorneInf());
        statDefaultValue.setTauxSPPublicationOrdosFin(parameterStatsDoc.getLECTauxSPDatePubliOrdoBorneSup());

        statDefaultValue.setTauxSPPublicationDecretsOrdosDebut(
            parameterStatsDoc.getLECTauxSPDatePubliDecretOrdoBorneInf()
        );
        statDefaultValue.setTauxSPPublicationDecretsOrdosFin(
            parameterStatsDoc.getLECTauxSPDatePubliDecretOrdoBorneSup()
        );

        statDefaultValue.setDateDebutLegislature(parameterStatsDoc.getLegislatureEnCoursDateDebut());
    }
}
