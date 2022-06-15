package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgStatistiquesUIService;

import fr.dila.solonepg.ui.bean.StatutArchivageDossierList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.BaseEpgStatistiques;
import fr.dila.solonepg.ui.services.EpgStatistiquesUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.StatutArchivageDossierForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.model.bean.EpgStatForm;
import fr.dila.ss.api.birt.BirtReport;
import fr.dila.ss.ui.enums.SSActionCategory;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "StatistiquesAjax")
public class EpgStatistiquesAjax extends BaseEpgStatistiques {
    private static final String STATUT_ARCHIVAGE_DOSSIER_FORM = "statutArchivageDossierForm";

    @GET
    public ThTemplate getArchivageDossiersHome(@SwBeanParam StatutArchivageDossierForm dossierForm) {
        return getDossiersArchives(dossierForm);
    }

    @POST
    @Path("displayStat")
    public ThTemplate displayStat(@FormParam("idStat") String idStat, @SwBeanParam EpgStatForm form) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/statistiques/statDetail", context);
        UserSessionHelper.putUserSessionParameter(context, EpgUserSessionKey.STATS_FORM_PARAMS, form);
        context.putInContextData(EpgContextDataKey.EPG_STAT_FORM, form);
        Map<String, Object> map = new HashMap<>();
        setDownloadExcelStatAction(context, idStat);
        map.put(SSTemplateConstants.DOWNLOAD_ACTIONS, context.getActions(SSActionCategory.STAT_ACTIONS_DOWNLOAD));
        map.put(SSTemplateConstants.DISPLAY_REPORT, true);
        map.put(EpgTemplateConstants.ID_STAT, idStat);

        context.putInContextData(EpgContextDataKey.STATS_ID, idStat);
        EpgStatistiquesUIService statsService = getEpgStatistiquesUIService();

        BirtReport birtReport = statsService.getBirtReport(context);
        checkReportAccess(birtReport, "displayStat/" + idStat);
        map.put(SSTemplateConstants.BIRT_REPORT, birtReport);

        String report = statsService.getHtmlReportURL(context);
        map.put(SSTemplateConstants.GENERATED_REPORT_PATH, report);

        template.setData(map);
        return template;
    }

    @POST
    @Path("archivage/dossiers/rechercher")
    public ThTemplate getDossiersArchives(@SwBeanParam StatutArchivageDossierForm dossierForm) {
        if (dossierForm.isEmpty()) {
            dossierForm =
                ObjectHelper.requireNonNullElseGet(
                    UserSessionHelper.getUserSessionParameter(context, STATUT_ARCHIVAGE_DOSSIER_FORM),
                    StatutArchivageDossierForm::new
                );
        }
        if (StringUtils.isBlank(template.getName())) {
            template = new AjaxLayoutThTemplate("fragments/table/tableStatutArchivageDossier", context);
        }
        context.putInContextData(EpgContextDataKey.STATUT_ARCHIVAGE_DOSSIER_FORM, dossierForm);
        context.putInContextData(EpgContextDataKey.RECOMPUTE_LIST, true);
        context.setNavigationContextTitle(
            new Breadcrumb(
                "espace.suivi.statistique.dossier_archivage",
                "/stats/archivage/dossiers",
                Breadcrumb.SUBTITLE_ORDER - 1,
                context.getWebcontext().getRequest()
            )
        );
        StatutArchivageDossierList archiveDossierList = getEpgStatistiquesUIService()
            .getStatutArchivageDossierList(context);
        archiveDossierList.buildColonnes(dossierForm);

        UserSessionHelper.putUserSessionParameter(context, STATUT_ARCHIVAGE_DOSSIER_FORM, dossierForm);
        template.getData().put(STTemplateConstants.RESULT_FORM, dossierForm);
        template.getData().put(STTemplateConstants.LST_COLONNES, archiveDossierList.getListeColonnes());
        template.getData().put(STTemplateConstants.RESULT_LIST, archiveDossierList);
        template.getData().put(STTemplateConstants.NB_RESULTS, archiveDossierList.getNbTotal());
        template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        template
            .getData()
            .put(STTemplateConstants.EDIT_ACTIONS, context.getActions(EpgActionCategory.STATUT_ARCHIVAGE_DOSSIER));
        template
            .getData()
            .put(STTemplateConstants.EXPORT_ACTION, context.getAction(EpgActionEnum.EXPORT_DOSSIERS_ARCHIVES));
        template.getData().put(STTemplateConstants.DISPLAY_TABLE, !archiveDossierList.getListe().isEmpty());
        template.getData().put(STTemplateConstants.DATA_URL, "/stats/archivage/dossiers");
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, "/ajax/stats/archivage/dossiers/rechercher");
        template.getData().put(EpgTemplateConstants.STATUTS, getStatutArchivage());
        return template;
    }

    @POST
    @Path("archivage/dossiers/exporter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response exportExcel(@FormParam("idDossiers[]") List<String> idDossiers) {
        EpgStatistiquesUIService statsService = getEpgStatistiquesUIService();
        context.putInContextData(STContextDataKey.IDS, idDossiers);
        statsService.exportListResultats(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    private List<SelectValueDTO> getStatutArchivage() {
        return EpgUIServiceLocator.getEpgSelectValueUIService().getStatutArchivageForStatistiques();
    }
}
