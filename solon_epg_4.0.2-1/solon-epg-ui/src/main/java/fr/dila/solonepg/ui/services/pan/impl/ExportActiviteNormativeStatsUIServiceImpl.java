package fr.dila.solonepg.ui.services.pan.impl;

import com.google.common.collect.Lists;
import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.birt.ExportPANStat;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.service.ActiviteNormativeStatsService;
import fr.dila.solonepg.core.service.ActiviteNormativeReportsGenerator;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.PANExportParametreDTO;
import fr.dila.solonepg.ui.bean.pan.PanTreeNode;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.ExportActiviteNormativeStatsUIService;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

/**
 * Bean Seam de export pan stats
 *
 * @author mchahine
 */
public class ExportActiviteNormativeStatsUIServiceImpl implements ExportActiviteNormativeStatsUIService {
    private static final String NON_RENSEIGNE = "NR";
    private static final STLogger LOGGER = STLogFactory.getLog(ExportActiviteNormativeStatsUIServiceImpl.class);

    private static final Collection<String> ALLOWED_TO_VIEW_JOURNAL_TECHNIQUE = Lists.newArrayList(
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_READER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_READER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_READER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_READER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_READER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_UPDATER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_UPDATER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_UPDATER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_UPDATER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_UPDATER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_EXPORT_DATA_UPDATER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_PARAM_LEGIS_UPDATER,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_PUBLI_LEGIS_EXEC,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE,
        SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_ORDONNANCE_MINISTERE
    );

    @Override
    public void exportPanStats(SpecificContext context) {
        boolean forceExportLegisPrec = context.getFromContextData(PanContextDataKey.FORCE_EXPORT_LEGIS_PREC);
        CoreSession documentManager = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
        List<String> exportedLegislatures = context.getFromContextData(PanContextDataKey.LEGISLATURE_LIST);
        PANExportParametreDTO exportDefaultParam = context.getFromContextData(
            PanContextDataKey.PAN_EXPORT_PARAMETRE_DTO
        );
        boolean isPeriodeParMois = context.getFromContextData(PanContextDataKey.IS_PERIODE_PAR_MOIS);
        int mois = context.getFromContextData(PanContextDataKey.MOIS);
        int annee = context.getFromContextData(PanContextDataKey.ANNEE);

        String message = "";
        final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
        final String currentUser = ssPrincipal.getName();
        final String userWorkspacePath = STServiceLocator
            .getUserWorkspaceService()
            .getCurrentUserPersonalWorkspace(documentManager)
            .getPathAsString();

        if (forceExportLegisPrec) {
            context.putInContextData(PanContextDataKey.FORCE_EXPORT_LEGIS_PREC, false);
            initExportParameters(context);
        }

        DocumentModel exportPanStatDoc = anStatsService.getExportPanStatDoc(
            documentManager,
            currentUser,
            exportedLegislatures
        );
        ExportPANStat exportLegislature = null;

        if (exportPanStatDoc != null) {
            exportLegislature = exportPanStatDoc.getAdapter(ExportPANStat.class);
        }
        String dateRequest = null;
        if (exportLegislature == null || !exportLegislature.isExporting()) {
            if (anStatsService.flagExportFor(documentManager, currentUser, userWorkspacePath, exportedLegislatures)) {
                // Post commit event
                EventProducer eventProducer = STServiceLocator.getEventProducer();
                Map<String, Serializable> eventProperties = new HashMap<>();
                eventProperties.put(
                    SolonEpgANEventConstants.INPUT_VALUES_PROPERTY,
                    (Serializable) assignParameters(
                        documentManager,
                        exportDefaultParam,
                        exportedLegislatures,
                        isPeriodeParMois,
                        mois,
                        annee
                    )
                );
                eventProperties.put(SolonEpgANEventConstants.USER_PROPERTY, currentUser);
                eventProperties.put(SolonEpgANEventConstants.USER_WS_PATH_PROPERTY, userWorkspacePath);
                eventProperties.put(
                    SolonEpgANEventConstants.EXPORTED_LEGIS,
                    StringUtils.join(exportedLegislatures, ",")
                );

                InlineEventContext eventContext = new InlineEventContext(documentManager, ssPrincipal, eventProperties);
                eventProducer.fireEvent(eventContext.newEvent(SolonEpgANEventConstants.EXPORT_STATS_EVENT));
            }
            dateRequest = SolonDateConverter.DATETIME_SLASH_MINUTE_COLON.formatNow();
        } else {
            // On affiche à l'utilisateur qu'un rafraichissement a déjà été
            // demandé et est en cours
            dateRequest = SolonDateConverter.DATETIME_SLASH_MINUTE_COLON.format(exportLegislature.getDateRequest());
        }
        message = ResourceHelper.getString("pan.espace.an.stats.export.msg", dateRequest);

        context.getMessageQueue().addToastSuccess(message);
    }

    private String buildLegislatureParamLabel(List<String> exportedLegislatures) {
        StringBuilder builder = new StringBuilder();
        if (!exportedLegislatures.isEmpty()) {
            if (exportedLegislatures.size() == 1) {
                String legislatureSelectionnee = exportedLegislatures.get(0);
                builder.append("la ");

                if (StringUtils.isBlank(legislatureSelectionnee) || legislatureSelectionnee.equals(NON_RENSEIGNE)) {
                    builder.append("législature non renseignée");
                } else {
                    builder.append(legislatureSelectionnee + " législature");
                }
            } else {
                builder.append("les ");
                int i = 0;
                while (i < exportedLegislatures.size() - 1) {
                    String legislature = exportedLegislatures.get(i);
                    if (StringUtils.isBlank(legislature) || legislature.equals(NON_RENSEIGNE)) {
                        legislature = "non renseignée";
                    }

                    builder.append(legislature);
                    builder.append(", ");
                    i++;
                }
                String lastLegislature = exportedLegislatures.get(exportedLegislatures.size() - 1);
                if (StringUtils.isBlank(lastLegislature) || lastLegislature.equals(NON_RENSEIGNE)) {
                    lastLegislature = "non renseignée";
                }
                builder.append(lastLegislature);

                builder.append(" législatures");
            }
        }
        return builder.toString();
    }

    private Map<String, String> assignParameters(
        CoreSession documentManager,
        PANExportParametreDTO exportDefaultParam,
        List<String> exportedLegislatures,
        boolean isPeriodeParMois,
        int mois,
        int annee
    ) {
        Map<String, String> inputValues = new HashMap<>();
        String ministeresParam = SolonEpgServiceLocator
            .getStatsGenerationResultatService()
            .getMinisteresListBirtReportParam(documentManager);
        String directionsParam = SolonEpgServiceLocator
            .getStatsGenerationResultatService()
            .getDirectionsListBirtReportParam();

        inputValues.put("MINISTERES_PARAM", ministeresParam);
        inputValues.put("DIRECTIONS_PARAM", directionsParam);
        inputValues.put(
            "DEBUTLEGISLATURE_PARAM",
            SolonDateConverter.DATE_SLASH.format(exportDefaultParam.getDateDebutLegislature())
        );
        inputValues.put("PERIODE_PARAM", (isPeriodeParMois ? "M" : "A"));
        inputValues.put("MOIS_PARAM", String.valueOf(mois));
        inputValues.put("ANNEE_PARAM", String.valueOf(annee));
        inputValues.put(
            "LEGISLATURE_PARAM",
            SolonDateConverter.DATE_SLASH.format(exportDefaultParam.getDateDebutLegislature())
        );

        inputValues.put("LEGISLATURES", StringHelper.joinValueToBlank(exportedLegislatures, ", ", "'", NON_RENSEIGNE));
        inputValues.put("LEGISLATURES_LABEL", buildLegislatureParamLabel(exportedLegislatures));

        return inputValues;
    }

    @Override
    public boolean isExporting(SpecificContext context) {
        List<ExportPANStat> lstExports = context.getFromContextData(PanContextDataKey.EXPORT_PAN_STAT);
        boolean isExporting = false;
        int i = 0;

        if (lstExports != null) {
            while (i < lstExports.size() && !isExporting) {
                if (lstExports.get(i).isExporting()) {
                    isExporting = true;
                }
                i++;
            }
        }
        return isExporting;
    }

    @Override
    public void initExportParameters(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        boolean legisEnCours = context.getFromContextData(PanContextDataKey.LEGISLATURE_EN_COURS);

        PANExportParametreDTO exportDefaultParam = context.getFromContextData(
            PanContextDataKey.PAN_EXPORT_PARAMETRE_DTO
        );
        ParametrageAN parameterStatsDoc = SolonEpgServiceLocator
            .getSolonEpgParametreService()
            .getDocAnParametre(documentManager);
        List<String> exportedLegislatures = context.getFromContextData(PanContextDataKey.LEGISLATURE_LIST);

        if (documentManager != null) {
            if (legisEnCours) {
                // Taux d'exécution des lois (depuis le début de la législature)
                exportDefaultParam.setTauxPromulgationDebut(parameterStatsDoc.getLECTauxDebutLegisDatePromulBorneInf());
                exportDefaultParam.setTauxPromulgationFin(parameterStatsDoc.getLECTauxDebutLegisDatePromulBorneSup());

                exportDefaultParam.setTauxPublicationDebut(parameterStatsDoc.getLECTauxDebutLegisDatePubliBorneInf());
                exportDefaultParam.setTauxPublicationFin(parameterStatsDoc.getLECTauxDebutLegisDatePubliBorneSup());

                // Taux d'exécution des lois (au cours de la dernière session
                // parlementaire)
                exportDefaultParam.setLolfPromulgationDebut(parameterStatsDoc.getLECTauxSPDatePromulBorneInf());
                exportDefaultParam.setLolfPromulgationFin(parameterStatsDoc.getLECTauxSPDatePromulBorneSup());

                exportDefaultParam.setLolfPublicationDebut(parameterStatsDoc.getLECTauxSPDatePubliBorneInf());
                exportDefaultParam.setLolfPublicationFin(parameterStatsDoc.getLECTauxSPDatePubliBorneSup());

                // Bilan semestriel
                exportDefaultParam.setBilanPromulgationDebut(parameterStatsDoc.getLECBSDatePromulBorneInf());
                exportDefaultParam.setBilanPromulgationFin(parameterStatsDoc.getLECBSDatePromulBorneSup());

                exportDefaultParam.setBilanPublicationDebut(parameterStatsDoc.getLECBSDatePubliBorneInf());
                exportDefaultParam.setBilanPublicationFin(parameterStatsDoc.getLECBSDatePubliBorneSup());

                exportDefaultParam.setDateDebutLegislature(parameterStatsDoc.getLegislatureEnCoursDateDebut());

                // Cas des ordonnances
                // Bilan semestriel
                exportDefaultParam.setBilanPublicationDebutOrdo(parameterStatsDoc.getLECBSDatePubliOrdoBorneInf());
                exportDefaultParam.setBilanPublicationFinOrdo(parameterStatsDoc.getLECBSDatePubliOrdoBorneSup());

                exportDefaultParam.setBilanPublicationDebutOrdoDecret(
                    parameterStatsDoc.getLECBSDatePubliDecretOrdoBorneInf()
                );
                exportDefaultParam.setBilanPublicationFinOrdoDecret(
                    parameterStatsDoc.getLECBSDatePubliDecretOrdoBorneSup()
                );

                // Taux d'exécution des lois (depuis le début de la législature)
                exportDefaultParam.setTauxDLPublicationOrdosDebut(
                    parameterStatsDoc.getLECTauxDebutLegisDatePubliOrdoBorneInf()
                );
                exportDefaultParam.setTauxDLPublicationOrdosFin(
                    parameterStatsDoc.getLECTauxDebutLegisDatePubliOrdoBorneSup()
                );

                exportDefaultParam.setTauxDLPublicationDecretsOrdosDebut(
                    parameterStatsDoc.getLECTauxDebutLegisDatePubliDecretOrdoBorneInf()
                );
                exportDefaultParam.setTauxDLPublicationDecretsOrdosFin(
                    parameterStatsDoc.getLECTauxDebutLegisDatePubliDecretOrdoBorneSup()
                );

                // Taux d'exécution des lois (au cours de la dernière session
                // parlementaire)
                exportDefaultParam.setTauxSPPublicationOrdosDebut(
                    parameterStatsDoc.getLECTauxSPDatePubliOrdoBorneInf()
                );
                exportDefaultParam.setTauxSPPublicationOrdosFin(parameterStatsDoc.getLECTauxSPDatePubliOrdoBorneSup());

                exportDefaultParam.setTauxSPPublicationDecretsOrdosDebut(
                    parameterStatsDoc.getLECTauxSPDatePubliDecretOrdoBorneInf()
                );
                exportDefaultParam.setTauxSPPublicationDecretsOrdosFin(
                    parameterStatsDoc.getLECTauxSPDatePubliDecretOrdoBorneSup()
                );

                exportedLegislatures.clear();
                exportedLegislatures.add(parameterStatsDoc.getLegislatureEnCours());
            } else {
                // Taux d'exécution des lois (depuis le début de la législature)
                exportDefaultParam.setTauxPromulgationDebut(parameterStatsDoc.getLPTauxDebutLegisDatePromulBorneInf());
                exportDefaultParam.setTauxPromulgationFin(parameterStatsDoc.getLPTauxDebutLegisDatePromulBorneSup());

                exportDefaultParam.setTauxPublicationDebut(parameterStatsDoc.getLPTauxDebutLegisDatePubliBorneInf());
                exportDefaultParam.setTauxPublicationFin(parameterStatsDoc.getLPTauxDebutLegisDatePubliBorneSup());

                // Taux d'exécution des lois (au cours de la dernière session
                // parlementaire)
                exportDefaultParam.setLolfPromulgationDebut(parameterStatsDoc.getLPTauxSPDatePromulBorneInf());
                exportDefaultParam.setLolfPromulgationFin(parameterStatsDoc.getLPTauxSPDatePromulBorneSup());

                exportDefaultParam.setLolfPublicationDebut(parameterStatsDoc.getLPTauxSPDatePubliBorneInf());
                exportDefaultParam.setLolfPublicationFin(parameterStatsDoc.getLPTauxSPDatePubliBorneSup());

                // Bilan semestriel
                exportDefaultParam.setBilanPromulgationDebut(parameterStatsDoc.getLPBSDatePromulBorneInf());
                exportDefaultParam.setBilanPromulgationFin(parameterStatsDoc.getLPBSDatePromulBorneSup());

                exportDefaultParam.setBilanPublicationDebut(parameterStatsDoc.getLPBSDatePubliBorneInf());
                exportDefaultParam.setBilanPublicationFin(parameterStatsDoc.getLPBSDatePubliBorneSup());

                // Cas des Ordonnances
                // Bilan semestriel
                exportDefaultParam.setBilanPublicationDebutOrdo(parameterStatsDoc.getLPBSDatePubliOrdoBorneInf());
                exportDefaultParam.setBilanPublicationFinOrdo(parameterStatsDoc.getLPBSDatePubliOrdoBorneSup());

                exportDefaultParam.setBilanPublicationDebutOrdoDecret(
                    parameterStatsDoc.getLPBSDatePubliDecretOrdoBorneInf()
                );
                exportDefaultParam.setBilanPublicationFinOrdoDecret(
                    parameterStatsDoc.getLPBSDatePubliDecretOrdoBorneSup()
                );

                // Taux d'exécution des lois (depuis le début de la législature)
                exportDefaultParam.setTauxDLPublicationOrdosDebut(
                    parameterStatsDoc.getLPTauxDebutLegisDatePubliOrdoBorneInf()
                );
                exportDefaultParam.setTauxDLPublicationOrdosFin(
                    parameterStatsDoc.getLPTauxDebutLegisDatePubliOrdoBorneSup()
                );

                exportDefaultParam.setTauxDLPublicationDecretsOrdosDebut(
                    parameterStatsDoc.getLPTauxDebutLegisDatePubliDecretOrdoBorneInf()
                );
                exportDefaultParam.setTauxDLPublicationDecretsOrdosFin(
                    parameterStatsDoc.getLPTauxDebutLegisDatePubliDecretOrdoBorneSup()
                );

                // Taux d'exécution des lois (au cours de la dernière session
                // parlementaire)
                exportDefaultParam.setTauxSPPublicationOrdosDebut(parameterStatsDoc.getLPTauxSPDatePubliOrdoBorneInf());
                exportDefaultParam.setTauxSPPublicationOrdosFin(parameterStatsDoc.getLPTauxSPDatePubliOrdoBorneSup());

                exportDefaultParam.setTauxSPPublicationDecretsOrdosDebut(
                    parameterStatsDoc.getLPTauxSPDatePubliDecretOrdoBorneInf()
                );
                exportDefaultParam.setTauxSPPublicationDecretsOrdosFin(
                    parameterStatsDoc.getLPTauxSPDatePubliDecretOrdoBorneSup()
                );

                exportDefaultParam.setDateDebutLegislature(parameterStatsDoc.getLegislaturePrecedenteDateDebut());

                exportedLegislatures.clear();
                exportedLegislatures.add(getLegislaturePrecedenteLabel(parameterStatsDoc));
            }
        }
    }

    @Override
    public Boolean isAllowedToExportAN(SpecificContext context) {
        CoreSession documentManager = context.getSession();

        SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
        if (ssPrincipal != null) {
            return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_EXPORT_DATA_UPDATER);
        }
        return Boolean.FALSE;
    }

    @Override
    public void createZipFile(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentDocument = context.getCurrentDocument();
        ExportPANStat exportPanStatDoc = currentDocument.getAdapter(ExportPANStat.class);

        String downloadFile =
            ActiviteNormativeReportsGenerator.getMainDirectory(exportPanStatDoc.isCurLegis(session)) +
            session.getPrincipal().getName() +
            File.separator +
            exportPanStatDoc.getName() +
            ".zip";
        File file = new File(downloadFile);

        OutputStream responseOutputStream = context.getFromContextData(SSContextDataKey.OUTPUTSTREAM);
        try {
            InputStream zipInputStream = new FileInputStream(file);

            byte[] bytesBuffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = zipInputStream.read(bytesBuffer)) > 0) {
                responseOutputStream.write(bytesBuffer, 0, bytesRead);
            }

            responseOutputStream.flush();

            zipInputStream.close();
            responseOutputStream.close();
        } catch (IOException e) {
            LOGGER.error(STLogEnumImpl.FAIL_CLOSE_STREAM_TEC, e);
        }
    }

    private ExportPANStat findByLegislature(List<ExportPANStat> lstExports, String legislature, boolean exactMatch) {
        ExportPANStat export = null;
        boolean found = false;
        int i = 0;
        if (lstExports != null) {
            while (i < lstExports.size() && !found) {
                if (
                    lstExports.get(i).getExportedLegislatures().contains(legislature) &&
                    (lstExports.get(i).getExportedLegislatures().size() == 1 || !exactMatch)
                ) {
                    export = lstExports.get(i);
                }
                i++;
            }
        }

        return export;
    }

    @Override
    public List<PanTreeNode> getExportTreeNodes(SpecificContext context) {
        CoreSession documentManager = context.getSession();

        SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
        ParametrageAN parameterStatsDoc = SolonEpgServiceLocator
            .getSolonEpgParametreService()
            .getDocAnParametre(documentManager);

        List<PanTreeNode> nodes = new ArrayList<>();

        List<ExportPANStat> lstAllExports = getAllExportPanStatDoc(documentManager, ssPrincipal);
        context.putInContextData(PanContextDataKey.EXPORT_PAN_STAT, lstAllExports);

        StringBuilder builder = new StringBuilder("export-global-");
        String enCoursStatus = this.isExporting(context) ? "disabled" : "enabled";
        ExportPANStat exportPrec = findByLegislature(
            lstAllExports,
            getLegislaturePrecedenteLabel(parameterStatsDoc),
            true
        );

        builder.append(enCoursStatus);
        if (isAllowedToExportAN(context)) {
            PanTreeNode mainNode = new PanTreeNode("Export Global", builder.toString(), null);
            PanTreeNode precNode = new PanTreeNode(
                "Exporter la législature précédente",
                "export-prec-" + enCoursStatus,
                null
            );

            initChildrenForExport(mainNode, true, lstAllExports);
            mainNode.setOpened(true);
            nodes.add(mainNode);
            nodes.add(precNode);
        }

        if (exportPrec != null && !exportPrec.isExporting()) {
            PanTreeNode legisPrecNode = new PanTreeNode(
                "Publier la législature précédente",
                "publier-prec-legis",
                null
            );
            nodes.add(legisPrecNode);
        }

        return nodes;
    }

    private void initChildrenForExport(PanTreeNode mainNode, boolean enCours, List<ExportPANStat> exportPanStatDoc) {
        String prefix = enCours ? "cur" : "prec";
        String detailTitle;
        String detailKey;

        if (exportPanStatDoc != null) {
            for (ExportPANStat stat : exportPanStatDoc) {
                if (stat.isExporting()) {
                    detailTitle = "génération en cours...";
                    detailKey = prefix + "export-detail-en-cours";
                } else {
                    detailTitle = stat.getName();
                    detailKey = prefix + "export-detail-terminee";
                }
                PanTreeNode childNode = new PanTreeNode(detailTitle, detailKey, null);
                if (stat.getExportedLegislatures() != null) {
                    childNode.setLegislatures(stat.getExportedLegislatures());
                }
                childNode.setOpened(true);
                mainNode.getChildren().add(childNode);
            }
        }
    }

    @Override
    public List<ExportPANStat> getAllExportPanStatDoc(CoreSession documentManager, SSPrincipal ssPrincipal) {
        final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
        List<ExportPANStat> doc = anStatsService.getAllExportPanStatDocForUser(documentManager, ssPrincipal.getName());
        if (doc != null) {
            return doc;
        }
        return new ArrayList<>();
    }

    private ExportPANStat getExportPanStatDoc(
        List<String> legislatures,
        CoreSession documentManager,
        SSPrincipal ssPrincipal
    ) {
        final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
        DocumentModel doc = anStatsService.getExportPanStatDoc(documentManager, ssPrincipal.getName(), legislatures);
        return doc.getAdapter(ExportPANStat.class);
    }

    @Override
    public void publierLegisPrec(SpecificContext context) {
        CoreSession documentManager = context.getSession();

        SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();

        EventProducer eventProducer = STServiceLocator.getEventProducer();
        Map<String, Serializable> eventProperties = new HashMap<>();
        eventProperties.put(SolonEpgANEventConstants.USER_PROPERTY, ssPrincipal.getName());
        eventProperties.put(SolonEpgANEventConstants.EXPORT_PAN_CURLEGISLATURE, Boolean.valueOf(false));

        InlineEventContext eventContext = new InlineEventContext(documentManager, ssPrincipal, eventProperties);
        eventProducer.fireEvent(eventContext.newEvent(SolonEpgANEventConstants.PUBLISH_STATS_EVENT));
        context
            .getMessageQueue()
            .addToastSuccess(ResourceHelper.getString("pan.stats.exports.publication.legislature.publiee"));
    }

    private String getLegislaturePrecedenteLabel(ParametrageAN parameterStatsDoc) {
        if (
            CollectionUtils.isNotEmpty(parameterStatsDoc.getLegislatures()) &&
            parameterStatsDoc.getLegislatures().indexOf(parameterStatsDoc.getLegislatureEnCours()) > 0
        ) {
            int lastIndex = parameterStatsDoc.getLegislatures().indexOf(parameterStatsDoc.getLegislatureEnCours()) - 1;
            return parameterStatsDoc.getLegislatures().get(lastIndex);
        }

        return "";
    }

    @Override
    public Boolean isAllowedToEditParameters(SpecificContext context) {
        CoreSession documentManager = context.getSession();

        SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
        if (ssPrincipal != null) {
            return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_PARAM_LEGIS_UPDATER);
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean isAllowedToPublishLegislature(SpecificContext context) {
        CoreSession documentManager = context.getSession();

        SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
        if (ssPrincipal != null) {
            return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_PUBLI_LEGIS_EXEC);
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean isAllowedToViewJournalTechnique(SpecificContext context) {
        CoreSession documentManager = context.getSession();

        SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
        if (ssPrincipal != null) {
            return ssPrincipal.isMemberOfAtLeastOne(ALLOWED_TO_VIEW_JOURNAL_TECHNIQUE);
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean displayExportTreeNode(SpecificContext context) {
        boolean publicationLegisPrecVisible = context.getFromContextData(
            PanContextDataKey.PUBLICATION_LEGIS_PREC_VISIBLE
        );
        return (
            isAllowedToEditParameters(context) ||
            (isAllowedToPublishLegislature(context) && publicationLegisPrecVisible) ||
            isAllowedToExportAN(context) ||
            isAllowedToViewJournalTechnique(context)
        );
    }

    @Override
    public void exportLegisPrec(SpecificContext context) {
        context.putInContextData(PanContextDataKey.LEGISLATURE_EN_COURS, false);
        context.putInContextData(PanContextDataKey.LEGISLATURE_LIST, new ArrayList<String>());
        context.putInContextData(PanContextDataKey.PAN_EXPORT_PARAMETRE_DTO, new PANExportParametreDTO());
        context.putInContextData(PanContextDataKey.FORCE_EXPORT_LEGIS_PREC, true);
        exportPanStats(context);
    }

    private ExportPANStat getLegis(SpecificContext context, String legislature) {
        String currentUser = context.getSession().getPrincipal().getName();

        DocumentModel exportPanStatDoc = SolonEpgServiceLocator
            .getActiviteNormativeStatsService()
            .getExportPanStatDoc(context.getSession(), currentUser, Arrays.asList(legislature));

        ExportPANStat exportLegislature = null;
        if (exportPanStatDoc != null) {
            exportLegislature = exportPanStatDoc.getAdapter(ExportPANStat.class);
        }
        return exportLegislature;
    }

    @Override
    public boolean isLegisPrecExported(SpecificContext context) {
        CoreSession session = context.getSession();
        ParametrageAN parameterStatsDoc = SolonEpgServiceLocator
            .getSolonEpgParametreService()
            .getDocAnParametre(session);
        String legislaturePrecedente = getLegislaturePrecedenteLabel(parameterStatsDoc);

        ExportPANStat exportLegislature = getLegis(context, legislaturePrecedente);
        return exportLegislature != null && !exportLegislature.isExporting();
    }

    @Override
    public boolean isLegisExporting(SpecificContext context) {
        for (String legis : SolonEpgServiceLocator
            .getSolonEpgParametreService()
            .getDocAnParametre(context.getSession())
            .getLegislatures()) {
            ExportPANStat exportLegislature = getLegis(context, legis);
            if (exportLegislature != null && exportLegislature.isExporting()) {
                return true;
            }
        }

        return false;
    }
}
