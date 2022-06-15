package fr.dila.solonepg.ui.jaxrs.webobject.page;

import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgStatistiquesUIService;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getIndexationUIService;
import static fr.dila.ss.ui.enums.SSContextDataKey.BIRT_OUTPUT_FORMAT;
import static fr.dila.ss.ui.enums.SSContextDataKey.HAS_RIGHT_TO_EXPORT;
import static fr.dila.ss.ui.enums.SSContextDataKey.IS_MULTI;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.birt.constant.EpgBirtReportParams;
import fr.dila.solonepg.ui.bean.EpgBirtReportList;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.services.EpgStatistiquesUIService;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.model.EpgStatistiquesTemplate;
import fr.dila.solonepg.ui.th.model.bean.EpgStatForm;
import fr.dila.ss.api.birt.BirtReport;
import fr.dila.ss.api.birt.ReportProperty;
import fr.dila.ss.ui.enums.SSActionCategory;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliStats")
public class EpgStatistiques extends BaseEpgStatistiques {
    public static final String STATTS_SGG_LIST = "statsSggList";
    public static final String STATTS_GLOBALE_LIST = "statsGlobaleList";
    public static final String EST_UTILISATEUR_SGG = "utilisateurSGG";
    public static final String PERIOD_VAUES = "periodesType";
    public static final String VECTEURS = "vecteurs";
    public static final String RUBRIQUES = "rubriques";
    public static final String KEY_DISPLAY_DATE_PERIODE = "displayDatePeriode";
    public static final String KEY_DISPLAY_DATE = "displayDate";
    public static final String KEY_DISPLAY_VECTEUR_PUBLICATION = "displayVecteurPublication";
    public static final String KEY_DISPLAY_BLOC_PERIODE = "displayBlocPeriode";
    public static final String KEY_DISPLAY_RUBRIQUES = "displayRubriques";
    public static final String KEY_DISPLAY_MOTCLES_CHAMPLIBRE = "displayMotClesChampsLibres";
    public static final String KEY_DISPLAY_STATUT_ARCHIVAGE = "displayStatutArchivage";

    public EpgStatistiques() {
        super();
    }

    @GET
    public ThTemplate getHome() {
        EpgStatistiquesUIService statsService = getEpgStatistiquesUIService();

        template.setName("pages/statistiques/stats");
        context.removeNavigationContextTitle();
        template.setContext(context);

        //relatives au traitement des dossiers transmis au SGG
        EpgBirtReportList statSggList = statsService.getStatistquesMapSggAsList(context);
        //d'activité globale dans S.O.L.O.N.
        EpgBirtReportList statGlobaleList = statsService.getStatistquesMapGlobalAsList(context);

        Map<String, Object> map = new HashMap<>();
        map.put(STATTS_SGG_LIST, statSggList);
        map.put(STATTS_GLOBALE_LIST, statGlobaleList);
        map.put(EST_UTILISATEUR_SGG, statsService.canViewSggStat(context.getSession().getPrincipal()));
        template.setData(map);
        return template;
    }

    @GET
    @Path("{id}")
    public Object viewStatistique(@PathParam("id") String id) {
        template.setName("pages/statistiques/viewStatistique");

        Objects.requireNonNull(id);
        EpgStatistiquesUIService statsService = getEpgStatistiquesUIService();
        context.putInContextData(EpgContextDataKey.STATS_ID, id);
        setDownloadExcelStatAction(context, id);
        BirtReport birtReport = statsService.getBirtReport(context);
        checkReportAccess(birtReport, "id/" + id);
        Collection<ReportProperty> scalarProperties = statsService.getScalarProperties(birtReport);
        EpgStatForm form = UserSessionHelper.getUserSessionParameter(context, EpgUserSessionKey.STATS_FORM_PARAMS);
        form = ObjectHelper.requireNonNullElseGet(form, EpgStatForm::new);
        context.putInContextData(EpgContextDataKey.EPG_STAT_FORM, form);
        context.setNavigationContextTitle(
            new Breadcrumb(
                birtReport.getTitle(),
                "/stats/" + id,
                Breadcrumb.SUBTITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        template.setContext(context);
        Map<String, Object> map = new HashMap<>();
        context.putInContextData(IS_MULTI, isNotEmpty(scalarProperties));
        context.putInContextData(HAS_RIGHT_TO_EXPORT, true);
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        map.put(SSTemplateConstants.DOWNLOAD_ACTIONS, context.getActions(SSActionCategory.STAT_ACTIONS_DOWNLOAD));
        map.put(HAS_PARAMS, isNotEmpty(scalarProperties));
        map.put(SSTemplateConstants.BIRT_REPORT, birtReport);

        // Gérer les accès directs avec l'ensemble des paramètres:
        List<String> scalarParams = statsService.getScalarParamsForBirtReport(context);
        if (scalarProperties.isEmpty()) {
            String report = statsService.getHtmlReportURL(context);
            map.put(SSTemplateConstants.DISPLAY_REPORT, true);
            map.put(SSTemplateConstants.GENERATED_REPORT_PATH, report);
            map.put(KEY_REPORT_URL, report);
        } else {
            boolean displaySelectMin = collectionContainsAny(
                scalarParams,
                EpgBirtReportParams.MINISTEREATTACHE,
                EpgBirtReportParams.MINISTERERESP_PARAM,
                EpgBirtReportParams.MINISTEREATTACHE_PARAM,
                EpgBirtReportParams.MINISTEREATTACHELABEL_PARAM,
                EpgBirtReportParams.MINISTERERESPLABEL_PARAM
            );
            map.put(KEY_DISPLAY_ORG_SELECT_MIN, displaySelectMin);
            map.put(
                KEY_DISPLAY_ORG_SELECT_DIR,
                collectionContainsAny(
                    scalarParams,
                    EpgBirtReportParams.DIRECTIONRESP_PARAM,
                    EpgBirtReportParams.DIRECTIONRESPLABEL_PARAM
                )
            );
            map.put(
                KEY_DISPLAY_ORG_SELECT_POSTE,
                collectionContainsAny(
                    scalarParams,
                    EpgBirtReportParams.POSTECREATOR_PARAM,
                    EpgBirtReportParams.POSTECREATORLABEL_PARAM,
                    EpgBirtReportParams.DISTRIBUTIONMAILBOXID_PARAM,
                    EpgBirtReportParams.DISTRIBUTIONMAILBOXLABEL_PARAM
                )
            );
            map.put(
                KEY_DISPLAY_DATE_PERIODE,
                collectionContainsAny(
                    scalarParams,
                    EpgBirtReportParams.DATEARRIVEPAPIER_BI_PARAM,
                    EpgBirtReportParams.DATEARRIVEPAPIER_BS_PARAM,
                    EpgBirtReportParams.DATERETOUR_BI_PARAM,
                    EpgBirtReportParams.DATERETOUR_BS_PARAM,
                    EpgBirtReportParams.DATEDOSSIERCREE_BI_PARAM,
                    EpgBirtReportParams.DATEDOSSIERCREE_BS_PARAM
                )
            );
            map.put(KEY_DISPLAY_DATE, collectionContainsAny(scalarParams, EpgBirtReportParams.DATEDOSSIER_PARAM));
            boolean displayVP = collectionContainsAny(scalarParams, EpgBirtReportParams.VECTEURPUBLICATION_PARAM);
            map.put(KEY_DISPLAY_VECTEUR_PUBLICATION, displayVP);
            if (displayVP) {
                map.put(VECTEURS, statsService.getVecteurPublicationSelectValues(context));
            }

            map.put(
                KEY_DISPLAY_BLOC_PERIODE,
                collectionContainsAny(
                    scalarParams,
                    EpgBirtReportParams.TYPEPERIODE_PARAM,
                    EpgBirtReportParams.VALUEPERIODE_PARAM
                )
            );

            boolean displayRubriques = collectionContainsAny(scalarParams, EpgBirtReportParams.RUBRIQUES_PARAM);
            map.put(KEY_DISPLAY_RUBRIQUES, displayRubriques);
            if (displayRubriques) {
                map.put(RUBRIQUES, getIndexationUIService().getAllIndexationRubriqueSelectValues(context.getSession()));
            }
            map.put(
                KEY_DISPLAY_MOTCLES_CHAMPLIBRE,
                collectionContainsAny(scalarParams, EpgBirtReportParams.LIBRE_PARAM)
            );
            map.put(PERIOD_VAUES, getPeriodes());
        }
        map.put(STTemplateConstants.RESULT_FORM, form);
        map.put(EpgTemplateConstants.ID_STAT, id);
        template.setData(map);
        return template;
    }

    @Path("archivage/dossiers")
    public Object viewArchivageDossier() {
        template.setName("pages/statistiques/archivageDossier");
        template.setContext(context);
        return newObject("StatistiquesAjax", context, template);
    }

    private boolean collectionContainsAny(Collection<String> coll, String... elems) {
        return CollectionUtils.containsAny(coll, Arrays.asList(elems));
    }

    @GET
    @Path("telecharger/pdf")
    @Produces("application/pdf")
    public Response telechargerPdf(@QueryParam("stat") @SwNotEmpty @SwId String stat) {
        context.putInContextData(EpgContextDataKey.STATS_ID, stat);
        context.putInContextData(BIRT_OUTPUT_FORMAT, BirtOutputFormat.PDF);
        BirtReport birtReport = getEpgStatistiquesUIService().getBirtReport(context);
        checkReportAccess(birtReport, "stat/" + stat);
        File report = getEpgStatistiquesUIService().genererStatistiques(context);
        return telechargerFichier(context, report);
    }

    @GET
    @Path("telecharger/excel")
    @Produces("application/vnd.ms-excel")
    public Response telechargerExcel(@QueryParam("stat") @SwNotEmpty @SwId String stat) {
        context.putInContextData(EpgContextDataKey.STATS_ID, stat);
        context.putInContextData(BIRT_OUTPUT_FORMAT, BirtOutputFormat.XLS);
        BirtReport birtReport = getEpgStatistiquesUIService().getBirtReport(context);
        checkReportAccess(birtReport, "stat/" + stat);
        File report = getEpgStatistiquesUIService().genererStatistiques(context);
        return telechargerFichier(context, report);
    }

    private List<SelectValueDTO> getPeriodes() {
        return Arrays.asList(
            new SelectValueDTO("1", "stats.label.periode.mois"),
            new SelectValueDTO("2", "stats.label.periode.heure"),
            new SelectValueDTO("3", "stats.label.periode.jour")
        );
    }

    @Override
    protected String modifyFileName(File file, BirtOutputFormat format) {
        return file.getName();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgStatistiquesTemplate();
    }
}
