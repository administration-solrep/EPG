package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan;

import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.birt.BirtResultatFichier;
import fr.dila.solonepg.ui.bean.pan.PANExportParametreDTO;
import fr.dila.solonepg.ui.bean.pan.birt.PanBirtDTO;
import fr.dila.solonepg.ui.bean.pan.birt.PanBirtParamForm;
import fr.dila.solonepg.ui.bean.pan.birt.PanBirtStats;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanStatistiques;
import fr.dila.solonepg.ui.services.pan.PanStatistiquesUIService;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.th.model.bean.pan.PanStatistiquesParamLegisEnCoursForm;
import fr.dila.ss.api.birt.BirtReport;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Calendar;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanStatistiquesAjax")
public class PanStatistiquesAjax extends SolonWebObject {

    @GET
    @Path("charger/{statId}")
    public ThTemplate chargerRapport(
        @PathParam("statId") String statId,
        @QueryParam("idContextuel") String idContextuel
    ) {
        template = new AjaxLayoutThTemplate("fragments/pan/panBirtDetail", context);
        PanStatistiquesUIService panStatistiquesUIService = PanUIServiceLocator.getPanStatistiquesUIService();
        BirtReport birtReport = SSServiceLocator.getBirtGenerationService().getReport(statId);
        ANReportEnum reportEnum = ANReportEnum.getByReportId(birtReport.getId());

        context.putInContextData(SSContextDataKey.STAT_ID, statId);
        context.putInContextData(EpgContextDataKey.BIRT_REPORT_SUFFIX, idContextuel);
        template
            .getData()
            .put(PanStatistiques.KEY_PENDING_REFRESH, panStatistiquesUIService.isCurrentReportRefreshing(context));
        template
            .getData()
            .put(
                PanStatistiques.KEY_PAN_DOWNLOAD_ACTIONS,
                context.getActions(
                    reportEnum.isGraphique()
                        ? EpgActionCategory.BIRT_PAN_DOWNLOAD_GRAPH_ACTIONS
                        : EpgActionCategory.BIRT_PAN_DOWNLOAD_ACTIONS
                )
            );
        template
            .getData()
            .put(SSTemplateConstants.BIRT_REPORT, SSServiceLocator.getBirtGenerationService().getReport(statId));
        template
            .getData()
            .put(
                SSTemplateConstants.GENERATED_REPORT_PATH,
                panStatistiquesUIService.getReportUrl(birtReport, null, idContextuel)
            );
        template
            .getData()
            .put(SSTemplateConstants.DISPLAY_REPORT, panStatistiquesUIService.getBlobCurrentReport(context));
        template.getData().put(PanStatistiques.KEY_STAT_ID, statId);
        template
            .getData()
            .put(PanTemplateConstants.CURRENT_PAN_SUBTAB, PanBirtStats.PAN_BIRT_TREE_RELATION.get(statId).getSubtab());

        return template;
    }

    @GET
    @Path("rafraichir/{statId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response displayReport(
        @PathParam("statId") String statId,
        @QueryParam("idContextuel") String idContextuel,
        @SwBeanParam PanBirtParamForm form
    ) {
        PanStatistiquesUIService panStatistiqueService = PanUIServiceLocator.getPanStatistiquesUIService();

        context.putInContextData(SSContextDataKey.STAT_ID, statId);
        context.putInContextData(EpgContextDataKey.BIRT_REPORT_SUFFIX, idContextuel);

        PanBirtDTO dto = panStatistiqueService.getBirtDTOFromContext(context, form);

        panStatistiqueService.refreshReport(statId, dto.toBirt(), context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @GET
    @Path("sauvegarder/{statId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@PathParam("statId") String statId, @QueryParam("idContextuel") String idContextuel) {
        BirtResultatFichier result = PanUIServiceLocator
            .getPanStatistiquesUIService()
            .saveBirtRefreshAsResultat(statId, idContextuel, context);
        if (result == null) {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("pan.espace.an.stats.save.error"));
        } else {
            context.getMessageQueue().addToastSuccess(ResourceHelper.getString("pan.espace.an.stats.save.msg"));
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @GET
    @Path("publier/{statId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response publish(
        @PathParam("statId") String statId,
        @QueryParam("idContextuel") String idContextuel,
        @SwBeanParam PanBirtParamForm form
    ) {
        context.putInContextData(PanContextDataKey.DOSSIER_ID, form.getDossierId());
        context.putInContextData(PanContextDataKey.MINISTERE_PILOTE_ID, form.getMinisterePilote());

        boolean published = PanUIServiceLocator
            .getPanStatistiquesUIService()
            .publishReport(statId, idContextuel, context);
        if (published) {
            context.getMessageQueue().addToastSuccess(ResourceHelper.getString("pan.espace.an.stats.publish.msg"));
        } else {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("pan.espace.an.stats.publish.error"));
        }
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Path("exporter/precedente")
    @Produces(MediaType.APPLICATION_JSON)
    public Response exportPrecedente() {
        context.putInContextData(PanContextDataKey.IS_PERIODE_PAR_MOIS, true);
        context.putInContextData(PanContextDataKey.MOIS, Calendar.getInstance().get(Calendar.MONTH));
        context.putInContextData(PanContextDataKey.ANNEE, Calendar.getInstance().get(Calendar.YEAR));

        PanUIServiceLocator.getExportActiviteNormativeStatsUIService().exportLegisPrec(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Path("publier/precedente")
    @Produces(MediaType.APPLICATION_JSON)
    public Response publierPrecedente() {
        PanUIServiceLocator.getExportActiviteNormativeStatsUIService().publierLegisPrec(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Path("exporter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response export(
        @SwBeanParam PanStatistiquesParamLegisEnCoursForm legisForm,
        @FormParam("legislatures") List<String> exported
    ) {
        PANExportParametreDTO dto = PanUIServiceLocator.getPanStatistiquesUIService().prepareExport(legisForm);

        context.putInContextData(PanContextDataKey.LEGISLATURE_LIST, exported);
        context.putInContextData(PanContextDataKey.PAN_EXPORT_PARAMETRE_DTO, dto);
        context.putInContextData(PanContextDataKey.FORCE_EXPORT_LEGIS_PREC, false);
        context.putInContextData(PanContextDataKey.IS_PERIODE_PAR_MOIS, true);
        context.putInContextData(PanContextDataKey.MOIS, Calendar.getInstance().get(Calendar.MONTH));
        context.putInContextData(PanContextDataKey.ANNEE, Calendar.getInstance().get(Calendar.YEAR));

        PanUIServiceLocator.getExportActiviteNormativeStatsUIService().exportPanStats(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("parametrage")
    public Object doParametrage() {
        return newObject("PanParametrageAjax", context, template);
    }
}
