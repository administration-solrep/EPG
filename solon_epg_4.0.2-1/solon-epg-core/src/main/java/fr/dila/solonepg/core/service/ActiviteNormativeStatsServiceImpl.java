package fr.dila.solonepg.core.service;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.activitenormative.ANReport;
import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.birt.BirtRefreshFichier;
import fr.dila.solonepg.api.birt.ExportPANStat;
import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.service.ActiviteNormativeStatsService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SolonDateConverter;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;

public class ActiviteNormativeStatsServiceImpl implements ActiviteNormativeStatsService {
    private static final long serialVersionUID = 1L;

    private static final String REFRESH_STATS_PATH = "/messtatsrafraichies";

    private static final String EXPORT_PAN_STAT_FOLDER = "export-pan-stats";

    private static final String REFRESH_FICHIER_QUERY =
        "Select b.ecm:uuid as id From BirtRefreshFichier as b where b.breff:owner = ? AND b.breff:filename = ?";

    private static final String EXPORT_PAN_STAT_QUERY =
        "Select d.ecm:uuid as id From " +
        ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_TYPE +
        " as d where d.expanstat:owner = ? and d.expanstat:legislatures = ?";

    private static final String ALLEXPORT_PAN_STAT_QUERY =
        "Select d.ecm:uuid as id From " +
        ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_TYPE +
        " as d where d.expanstat:owner = ? ";
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(ActiviteNormativeStatsServiceImpl.class);

    @Override
    public void refreshStats(
        final CoreSession session,
        final String userWorkspacePath,
        final ANReport report,
        final String user,
        final Map<String, Serializable> inputValues
    ) {
        // Création ou récupération du document de rafraichissement stats
        final DocumentModel birtRefreshDoc = getOrCreateRefreshFichier(session, user, userWorkspacePath, report);
        // Génération stats
        final Map<BirtOutputFormat, Blob> contents = generateStats(report, inputValues);
        // Maj document
        final BirtRefreshFichier refreshFichier = birtRefreshDoc.getAdapter(BirtRefreshFichier.class);

        if (
            ANReportEnum.TAB_AN_BILAN_SEM_LOI_TOUS == report.getType() ||
            ANReportEnum.TAB_AN_BILAN_SEM_ORDONNANCE_TOUS == report.getType()
        ) {
            final Map<String, Object> params = new HashMap<>();
            params.put("DEBUT_INTERVALLE1_PARAM", inputValues.get("DEBUT_INTERVALLE1_PARAM"));
            params.put("FIN_INTERVALLE1_PARAM", inputValues.get("FIN_INTERVALLE1_PARAM"));
            params.put("DEBUT_INTERVALLE2_PARAM", inputValues.get("DEBUT_INTERVALLE2_PARAM"));
            params.put("FIN_INTERVALLE2_PARAM", inputValues.get("FIN_INTERVALLE2_PARAM"));
            refreshFichier.setParamList(new JSONObject(params).toString());
        }

        refreshFichier.setHtmlContent(contents.get(BirtOutputFormat.HTML));
        refreshFichier.setXlsContent(contents.get(BirtOutputFormat.XLS));
        refreshFichier.setPdfContent(contents.get(BirtOutputFormat.PDF));
        refreshFichier.save(session);
        session.save();
    }

    protected Map<BirtOutputFormat, Blob> generateStats(
        final ANReport report,
        final Map<String, Serializable> inputValues
    ) {
        final List<BirtOutputFormat> outPutFormats = new ArrayList<>();
        outPutFormats.add(BirtOutputFormat.HTML);
        outPutFormats.add(BirtOutputFormat.PDF);
        outPutFormats.add(BirtOutputFormat.XLS);

        String reportDir = STServiceLocator
            .getConfigService()
            .getValue(SolonEpgConfigConstant.SOLONEPG_GENERATED_REPORT_DIRECTORY);
        File fileResult = new File(reportDir + "/" + report.getName());
        return SSServiceLocator
            .getSSBirtService()
            .generateReportResults(
                fileResult,
                null,
                report.getType().getRptdesignId(),
                SSServiceLocator.getBirtGenerationService().getReport(report.getType().getRptdesignId()).getFile(),
                inputValues,
                outPutFormats
            );
    }

    protected DocumentModel getOrCreateRefreshFichier(
        final CoreSession session,
        final String user,
        final String userWorkspacePath,
        final ANReport report
    ) {
        DocumentModel birtRefreshDoc = getBirtRefreshDoc(session, report, user);
        if (birtRefreshDoc == null) {
            // Création du document
            final String birtRefreshRootPath = getBirtRefreshRootPath(session, userWorkspacePath);
            if (birtRefreshRootPath == null) {
                return null;
            } else {
                birtRefreshDoc =
                    session.createDocumentModel(
                        birtRefreshRootPath,
                        report.getName(),
                        ActiviteNormativeStatsConstants.BIRT_REFRESH_FICHIER_TYPE
                    );
                final BirtRefreshFichier refreshFichier = birtRefreshDoc.getAdapter(BirtRefreshFichier.class);
                refreshFichier.setOwner(user);
                refreshFichier.setFileName(report.getName());
                birtRefreshDoc = session.createDocument(birtRefreshDoc);
            }
        }
        return birtRefreshDoc;
    }

    @Override
    public DocumentModel getBirtRefreshDocForCurrentUser(final CoreSession session, final ANReport report) {
        if (session == null) {
            LOGGER.error(null, STLogEnumImpl.NPE_PARAM_METH_TEC, "La session est null");
            return null;
        }
        return getBirtRefreshDoc(session, report, session.getPrincipal().getName());
    }

    @Override
    public DocumentModel getBirtRefreshDoc(final CoreSession session, final ANReport report, final String user) {
        if (session == null || report == null || user == null) {
            LOGGER.error(session, STLogEnumImpl.NPE_PARAM_METH_TEC, "La session, le rapport, ou le user est null");
            return null;
        }
        final Object[] params = new Object[] { user, report.getName() };
        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            "BirtRefreshFichier",
            REFRESH_FICHIER_QUERY,
            params
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        }

        return docs.get(0);
    }

    @Override
    public boolean existBirtRefreshForCurrentUser(final CoreSession session, final ANReport report) {
        if (session == null) {
            LOGGER.error(null, STLogEnumImpl.NPE_PARAM_METH_TEC);
            return false;
        }
        return existBirtRefresh(session, report, session.getPrincipal().getName());
    }

    @Override
    public boolean existBirtRefresh(final CoreSession session, final ANReport report, final String user) {
        if (session == null || report == null || user == null) {
            LOGGER.error(session, STLogEnumImpl.NPE_PARAM_METH_TEC, "La session, le rapport, ou le user est null");
            return false;
        }
        Object[] params = new Object[] { user, report.getName() };

        DocumentRef[] lstIds = QueryUtils.doUFNXQLQueryForIds(session, REFRESH_FICHIER_QUERY, params);
        if (lstIds == null) {
            return false;
        } else {
            return lstIds.length > 0;
        }
    }

    @Override
    public boolean flagRefreshFor(
        final CoreSession session,
        final ANReport report,
        final String user,
        final String userWorkspacePath
    ) {
        final DocumentModel birtRefreshDoc = getOrCreateRefreshFichier(session, user, userWorkspacePath, report);
        if (birtRefreshDoc == null) {
            return false;
        } else {
            final BirtRefreshFichier birtRefresh = birtRefreshDoc.getAdapter(BirtRefreshFichier.class);
            synchronized (birtRefresh) {
                if (!birtRefresh.isRefreshing()) {
                    birtRefresh.setDateRequest(Calendar.getInstance());
                    birtRefresh.setRefreshing(true);
                    birtRefresh.save(session);
                    session.save();
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public void flagEndRefreshFor(
        final CoreSession session,
        final ANReport report,
        final String user,
        final String userWorkspacePath
    ) {
        final DocumentModel birtRefreshDoc = getOrCreateRefreshFichier(session, user, userWorkspacePath, report);
        if (birtRefreshDoc != null) {
            final BirtRefreshFichier birtRefresh = birtRefreshDoc.getAdapter(BirtRefreshFichier.class);
            if (birtRefresh.isRefreshing()) {
                birtRefresh.setRefreshing(false);
                birtRefresh.save(session);
                session.save();
            }
        }
    }

    @Override
    public boolean isCurrentlyRefreshing(final CoreSession session, final ANReport report, final String user) {
        final DocumentModel birtRefreshDoc = getBirtRefreshDoc(session, report, user);
        if (birtRefreshDoc == null) {
            return false;
        } else {
            final BirtRefreshFichier birtRefresh = birtRefreshDoc.getAdapter(BirtRefreshFichier.class);
            return birtRefresh.isRefreshing();
        }
    }

    @Override
    public String getHorodatageRequest(final CoreSession session, final ANReport report, final String user) {
        final DocumentModel birtRefreshDoc = getBirtRefreshDoc(session, report, user);
        if (birtRefreshDoc == null) {
            return "Aucune donnée trouvée pour les paramètres fournis";
        } else {
            final BirtRefreshFichier birtRefresh = birtRefreshDoc.getAdapter(BirtRefreshFichier.class);
            return SolonDateConverter.DATETIME_SLASH_MINUTE_COLON.format(birtRefresh.getDateRequest());
        }
    }

    @Override
    public String getBirtRefreshRootPath(final CoreSession session, final String userWorkspacePath) {
        DocumentModel birtRefreshRoot = null;
        PathRef ref = new PathRef(userWorkspacePath + REFRESH_STATS_PATH);
        if (session.exists(ref)) {
            birtRefreshRoot = session.getDocument(ref);
        } else {
            birtRefreshRoot =
                session.createDocumentModel(
                    userWorkspacePath,
                    "messtatsrafraichies",
                    ActiviteNormativeStatsConstants.BIRT_REFRESH_ROOT_TYPE
                );
            birtRefreshRoot = session.createDocument(birtRefreshRoot);
            session.save();
        }
        if (birtRefreshRoot == null) {
            return null;
        } else {
            return birtRefreshRoot.getPathAsString();
        }
    }

    @Override
    public void exportPANStats(
        final CoreSession session,
        final String userWorkspacePath,
        final String user,
        final Map<String, Serializable> inputValues,
        List<String> exportedLegislature
    ) {
        ExportPANStat adapter = null;
        try {
            final DocumentModel exportPanStat = getOrCreateExportPanStat(
                session,
                user,
                userWorkspacePath,
                exportedLegislature
            );
            adapter = exportPanStat.getAdapter(ExportPANStat.class);
            final ActiviteNormativeReportsGenerator activiteNormativeReportsGenerator = new ActiviteNormativeReportsGenerator(
                inputValues,
                user,
                adapter,
                session,
                adapter.getName()
            );
            activiteNormativeReportsGenerator.generateReports();
            session.save();
        } catch (final Exception e) {
            if (adapter != null) {
                adapter.setExporting(false);
                session.save();
            }
        }
    }

    @Override
    public boolean isCurrentlyExporting(
        final CoreSession session,
        final String user,
        List<String> exportedLegislature
    ) {
        final DocumentModel exportPanStatDoc = getExportPanStatDoc(session, user, exportedLegislature);
        if (exportPanStatDoc == null) {
            return false;
        } else {
            final ExportPANStat exportPANStat = exportPanStatDoc.getAdapter(ExportPANStat.class);
            return exportPANStat.isExporting();
        }
    }

    @Override
    public boolean flagExportFor(
        final CoreSession session,
        final String user,
        final String userWorkspacePath,
        List<String> exportedLegislature
    ) {
        final DocumentModel exportPanStatDoc = getOrCreateExportPanStat(
            session,
            user,
            userWorkspacePath,
            exportedLegislature
        );
        if (exportPanStatDoc == null) {
            return false;
        } else {
            final ExportPANStat exportPANStat = exportPanStatDoc.getAdapter(ExportPANStat.class);
            if (!exportPANStat.isExporting()) {
                exportPANStat.setDateRequest(Calendar.getInstance());
                exportPANStat.setExporting(true);
                exportPANStat.setExportedLegislatures(exportedLegislature);
                exportPANStat.save(session);
                session.save();
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public String getExportHorodatageRequest(
        final CoreSession session,
        final String user,
        List<String> exportedLegislature
    ) {
        final DocumentModel exportPanStatDoc = getExportPanStatDoc(session, user, exportedLegislature);
        if (exportPanStatDoc == null) {
            return "Aucune donnée trouvée pour les paramètres fournis";
        } else {
            final ExportPANStat exportPANStat = exportPanStatDoc.getAdapter(ExportPANStat.class);
            return SolonDateConverter.DATETIME_SLASH_MINUTE_COLON.format(exportPANStat.getDateRequest());
        }
    }

    @Override
    public DocumentModel getExportPanStatDoc(
        final CoreSession session,
        final String user,
        List<String> exportedLegislature
    ) {
        if (session == null || user == null) {
            LOGGER.error(session, STLogEnumImpl.NPE_PARAM_METH_TEC, "La session, ou le user est null");
            return null;
        }
        String strExportedLegis = StringUtils.join(exportedLegislature, ",");
        final Object[] params = new Object[] { user, strExportedLegis };
        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_TYPE,
            EXPORT_PAN_STAT_QUERY,
            params
        );
        if (docs == null || docs.isEmpty()) {
            LOGGER.info(
                session,
                STLogEnumImpl.DEFAULT,
                "L'export de l'utilisateur " +
                user +
                " pour la/les législatures " +
                strExportedLegis +
                " n'a pas été trouvé"
            );
            return null;
        }

        return docs.get(0);
    }

    protected DocumentModel getOrCreateExportPanStat(
        final CoreSession session,
        final String user,
        final String userWorkspacePath,
        List<String> exportedLegislature
    ) {
        DocumentModel exportPanStatDoc = getExportPanStatDoc(session, user, exportedLegislature);
        if (exportPanStatDoc == null) {
            // Création du document
            final String exportPanStatRootPath = getExportPanStatRootPath(session, userWorkspacePath);
            if (exportPanStatRootPath == null) {
                return null;
            } else {
                boolean curLegis;
                ParametrageAN exportParameters = SolonEpgServiceLocator
                    .getSolonEpgParametreService()
                    .getDocAnParametre(session);
                int lastIndex =
                    exportParameters.getLegislatures().indexOf(exportParameters.getLegislatureEnCours()) - 1;
                String labelLegisPrec = exportParameters.getLegislatures().get(lastIndex);
                // On considère qu'on exporte la législature "courante" si on n'est pas
                //entrain d'exporter uniquement la précédente
                if (exportedLegislature.size() == 1 && exportedLegislature.get(0).equals(labelLegisPrec)) {
                    curLegis = false;
                } else {
                    curLegis = true;
                }

                String prefix = curLegis ? "curLegis" : "precLegis";
                String suffix = StringUtils.join(exportedLegislature, "-");
                exportPanStatDoc =
                    session.createDocumentModel(
                        exportPanStatRootPath,
                        prefix + ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_TYPE + suffix,
                        ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_TYPE
                    );
                final ExportPANStat exportPANStat = exportPanStatDoc.getAdapter(ExportPANStat.class);
                exportPANStat.setOwner(user);
                exportPANStat.setExportedLegislatures(exportedLegislature);
                exportPanStatDoc = session.createDocument(exportPanStatDoc);
            }
        }
        return exportPanStatDoc;
    }

    @Override
    public String getExportPanStatRootPath(final CoreSession session, final String userWorkspacePath) {
        DocumentModel exportPanstatsRoot;
        PathRef ref = new PathRef(userWorkspacePath + "/" + EXPORT_PAN_STAT_FOLDER);
        if (session.exists(ref)) {
            exportPanstatsRoot = session.getDocument(ref);
        } else {
            exportPanstatsRoot =
                session.createDocumentModel(
                    userWorkspacePath,
                    EXPORT_PAN_STAT_FOLDER,
                    ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_ROOT_TYPE
                );
            exportPanstatsRoot = session.createDocument(exportPanstatsRoot);
            session.save();
        }
        if (exportPanstatsRoot == null) {
            return null;
        } else {
            return exportPanstatsRoot.getPathAsString();
        }
    }

    @Override
    public void flagEndEXportingFor(
        final CoreSession session,
        final String user,
        final String userWorkspacePath,
        List<String> exportedLegislature
    ) {
        final DocumentModel exportPanStatDoc = getExportPanStatDoc(session, user, exportedLegislature);
        if (exportPanStatDoc != null) {
            final ExportPANStat exportPANStat = exportPanStatDoc.getAdapter(ExportPANStat.class);
            if (exportPANStat.isExporting()) {
                exportPANStat.setExporting(false);
                exportPANStat.save(session);
                session.save();
            } else {
                String strExportedLegis = StringUtils.join(exportedLegislature, ", ");
                LOGGER.info(
                    session,
                    STLogEnumImpl.DEFAULT,
                    "Pas d'export en cours pour l'utilisateur " + user + " pour la/les législatures " + strExportedLegis
                );
            }
        }
    }

    @Override
    public List<ExportPANStat> getAllExportPanStatDocForUser(CoreSession documentManager, String name) {
        if (documentManager == null || name == null) {
            LOGGER.error(documentManager, STLogEnumImpl.NPE_PARAM_METH_TEC, "La session, ou le user est null");
            return null;
        }

        final Object[] params = new Object[] { name };
        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            documentManager,
            ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_TYPE,
            ALLEXPORT_PAN_STAT_QUERY,
            params
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        }

        List<ExportPANStat> lstExports = new ArrayList<>();

        for (DocumentModel doc : docs) {
            lstExports.add(doc.getAdapter(ExportPANStat.class));
        }

        return lstExports;
    }
}
