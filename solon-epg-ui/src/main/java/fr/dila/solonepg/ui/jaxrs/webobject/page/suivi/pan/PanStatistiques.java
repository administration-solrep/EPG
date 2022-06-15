package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan;

import static fr.dila.solonepg.ui.services.pan.impl.PanStatistiquesUIServiceImpl.BIRT_PATTERN;

import com.google.common.collect.ImmutableList;
import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.birt.ExportPANStat;
import fr.dila.solonepg.ui.bean.pan.PanOnglet;
import fr.dila.solonepg.ui.bean.pan.PanStatistiquesParamDTO;
import fr.dila.solonepg.ui.bean.pan.birt.PanBirtDTO;
import fr.dila.solonepg.ui.bean.pan.birt.PanBirtFragmentDTO;
import fr.dila.solonepg.ui.bean.pan.birt.PanBirtStats;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanActionCategory;
import fr.dila.solonepg.ui.enums.pan.PanActionEnum;
import fr.dila.solonepg.ui.services.pan.PanStatistiquesUIService;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.th.model.EpgSuiviTemplate;
import fr.dila.ss.api.birt.BirtReport;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.jaxrs.webobject.page.AbstractSSStatistiques;
import fr.dila.ss.ui.jaxrs.weboject.model.WebObjectExportModel;
import fr.dila.ss.ui.services.SSStatistiquesUIService;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import fr.dila.st.ui.validators.annot.SwRegex;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanStatistiques")
public class PanStatistiques extends AbstractSSStatistiques implements WebObjectExportModel {
    public static final String KEY_SWITCH_LABEL = "switchLabel";
    public static final String KEY_PAN_BIRT_FORM_FIELDS = "panBirtFormFields";
    public static final String KEY_PAN_DOWNLOAD_ACTIONS = "downloadPanActions";
    public static final String KEY_ALT_ID = "altStatId";
    public static final String KEY_STAT_ID = "statId";
    public static final String KEY_STAT_DTO = "statistiques";
    public static final String KEY_PENDING_REFRESH = "pendingRefresh";
    public static final String KEY_EXPORTS_DTO = "exports";
    public static final String KEY_LIST_REPORTS = "listeReports";
    public static final String KEY_EXPORT_PENDING = "exportEnCours";
    public static final String KEY_PREC_LEGIS_EXPORTED = "legisPrecExportee";
    public static final String KEY_LEGIS_ENC = "legislatureEnCours";
    public static final String KEY_LEGIS = "legislatures";

    @GET
    @Path("exports")
    public ThTemplate getExport() {
        template = new EpgSuiviTemplate();
        template.setName("pages/suivi/panExport");
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("pan.stats.exports.publication.title.breadcrumb.label"),
                "/suivi/pan/stats/exports",
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );

        PanStatistiquesParamDTO dto = PanUIServiceLocator.getPanStatistiquesUIService().getStatParams(context);

        List<ExportPANStat> exports = PanUIServiceLocator
            .getExportActiviteNormativeStatsUIService()
            .getAllExportPanStatDoc(context.getSession(), (SSPrincipal) context.getWebcontext().getPrincipal());

        Map<String, Object> map = new HashMap<>();

        map.put(
            PanStatistiques.KEY_PREC_LEGIS_EXPORTED,
            PanUIServiceLocator.getExportActiviteNormativeStatsUIService().isLegisPrecExported(context)
        );

        if (PanUIServiceLocator.getExportActiviteNormativeStatsUIService().isLegisExporting(context)) {
            map.put(PanStatistiques.KEY_EXPORT_PENDING, true);
            context
                .getMessageQueue()
                .addInfoToQueue(ResourceHelper.getString("pan.stats.exports.publication.generation.en.cours.label"));
        }

        map.put(PanStatistiques.KEY_LIST_REPORTS, PanBirtStats.getAllBySource());
        map.put(PanStatistiques.KEY_EXPORTS_DTO, exports);
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());

        map.put(KEY_LEGIS_ENC, ImmutableList.of(dto.getLegislatureEnCours()));
        map.put(SSTemplateConstants.EXPORT_ACTIONS, context.getActions(PanActionCategory.EXPORT_ACTIONS));
        map.put(KEY_LEGIS, dto.getLegislatures());
        map.put(PanStatistiques.KEY_STAT_DTO, dto.getLegisEnCours());
        template.setContext(context);
        template.setData(map);

        return template;
    }

    @GET
    @Path("telecharger/export")
    @Produces("application/zip")
    public Response telechargerExport(@QueryParam("id") String id) {
        context.setCurrentDocument(id);
        StreamingOutput outputStream = getOutputStream(
            context,
            PanUIServiceLocator.getExportActiviteNormativeStatsUIService()::createZipFile
        );

        DocumentModel doc = context.getCurrentDocument();
        final String zipFilename = doc.getName() + ".zip";

        return FileDownloadUtils.getZipResponse(outputStream, zipFilename);
    }

    @GET
    @Path("telecharger/pdf")
    @Produces("application/pdf")
    public Response telechargerPdf(
        @QueryParam("statId") @SwRegex(BIRT_PATTERN) String statId,
        @QueryParam("idContextuel") String idContextuel
    ) {
        context.putInContextData(SSContextDataKey.STAT_ID, statId);
        context.putInContextData(SSContextDataKey.BIRT_OUTPUT_FORMAT, BirtOutputFormat.PDF);
        context.putInContextData(EpgContextDataKey.BIRT_REPORT_SUFFIX, idContextuel);
        Blob blob = PanUIServiceLocator.getPanStatistiquesUIService().getBlobCurrentReport(context);

        return telechargerFichier(context, blob);
    }

    @GET
    @Path("telecharger/excel")
    @Produces("application/vnd.ms-excel")
    public Response telechargerExcel(
        @QueryParam("statId") @SwRegex(BIRT_PATTERN) String statId,
        @QueryParam("idContextuel") String idContextuel
    ) {
        context.putInContextData(SSContextDataKey.STAT_ID, statId);
        context.putInContextData(SSContextDataKey.BIRT_OUTPUT_FORMAT, BirtOutputFormat.XLS);
        context.putInContextData(EpgContextDataKey.BIRT_REPORT_SUFFIX, idContextuel);
        Blob blob = PanUIServiceLocator.getPanStatistiquesUIService().getBlobCurrentReport(context);

        return telechargerFichier(context, blob);
    }

    @Override
    @GET
    @Path("report")
    public Object getBirtReport(
        @QueryParam("statId") @SwRegex(BIRT_PATTERN) String statId,
        @QueryParam("idContextuel") String idContextuel
    ) {
        context.putInContextData(SSContextDataKey.STAT_ID, statId);
        context.putInContextData(EpgContextDataKey.BIRT_REPORT_SUFFIX, idContextuel);
        BirtReport report = SSServiceLocator.getBirtGenerationService().getReport(statId);
        Blob blob = PanUIServiceLocator.getPanStatistiquesUIService().getBlobCurrentReport(context);
        return blob != null ? FileDownloadUtils.getAttachmentHtml(blob.getFile(), report.getFile()).getEntity() : null;
    }

    public static void loadBirtContext(PanOnglet actif, SpecificContext context, Map<String, Object> map) {
        // si l'onglet possède un rapport Birt, on l'affiche
        if (actif != null && actif.hasBirt()) {
            context.putInContextData(SSContextDataKey.STAT_ID, actif.getIdBirtReport());
            map.put(PanStatistiques.KEY_STAT_ID, actif.getIdBirtReport());
            if (!StringUtils.isEmpty(actif.getAltIdBirtReport())) {
                // si on est sur un onglet qui contient 2 rapports, on ajoute le
                // link pour switcher entre les 2
                map.put(PanStatistiques.KEY_SWITCH_LABEL, actif.getSwitchLabel());
                map.put(PanStatistiques.KEY_ALT_ID, actif.getAltIdBirtReport());
            }

            updateBirtInfo(context, map);
        }
    }

    private static void updateBirtInfo(SpecificContext context, Map<String, Object> map) {
        PanStatistiquesUIService panStatistiquesUIService = PanUIServiceLocator.getPanStatistiquesUIService();

        String statId = context.getFromContextData(SSContextDataKey.STAT_ID);
        ANReportEnum reportEnum = ANReportEnum.getByReportId(statId);

        BirtReport birtReport = SSServiceLocator.getBirtGenerationService().getReport(statId);

        // construction du dto à partir du contexte
        PanBirtDTO birtDto = panStatistiquesUIService.getBirtDTOFromContext(context, null);

        // construction du paramétrage du fragment Thymeleaf
        List<PanBirtFragmentDTO> fragmentDto = birtDto.toFragment();

        String idContextuel = (String) map.get(PanTemplateConstants.ID_CONTEXTUEL);
        context.putInContextData(EpgContextDataKey.BIRT_REPORT_SUFFIX, idContextuel);
        boolean refreshExisting = panStatistiquesUIService.isRefreshExisting(context);
        boolean resultatExisting = panStatistiquesUIService.isResultatExisting(context);
        map.put(SSTemplateConstants.DISPLAY_REPORT, refreshExisting || resultatExisting);
        map.put(PanTemplateConstants.REFRESH_ACTION, context.getAction(PanActionEnum.PAN_BIRT_REFRESH));
        map.put(PanTemplateConstants.PUBLISH_ACTION, context.getAction(PanActionEnum.PAN_BIRT_PUBLISH));
        map.put(PanTemplateConstants.REFRESH_EXISTING, refreshExisting);
        map.put(
            SSTemplateConstants.GENERATED_REPORT_PATH,
            panStatistiquesUIService.getReportUrl(birtReport, null, idContextuel)
        );
        map.put(PanStatistiques.KEY_PENDING_REFRESH, panStatistiquesUIService.isCurrentReportRefreshing(context));

        map.put(SSTemplateConstants.BIRT_REPORT, birtReport);
        map.put(PanStatistiques.KEY_PAN_BIRT_FORM_FIELDS, fragmentDto);

        map.put(
            PanStatistiques.KEY_PAN_DOWNLOAD_ACTIONS,
            context.getActions(
                reportEnum.isGraphique()
                    ? EpgActionCategory.BIRT_PAN_DOWNLOAD_GRAPH_ACTIONS
                    : EpgActionCategory.BIRT_PAN_DOWNLOAD_ACTIONS
            )
        );
    }

    @Override
    protected SSStatistiquesUIService getStatService() {
        return PanUIServiceLocator.getPanStatistiquesUIService();
    }

    @Override
    protected String modifyFileName(File file, BirtOutputFormat format) {
        String statId = context.getFromContextData(SSContextDataKey.STAT_ID);
        return (
            SSServiceLocator.getBirtGenerationService().getReport(statId).getTitle() +
            format.getExtensionWithSeparator()
        );
    }
}
