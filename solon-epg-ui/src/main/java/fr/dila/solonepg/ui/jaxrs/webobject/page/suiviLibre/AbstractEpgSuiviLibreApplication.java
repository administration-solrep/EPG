package fr.dila.solonepg.ui.jaxrs.webobject.page.suiviLibre;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.SuiviLibreReportDTO;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.jaxrs.webobject.AbstractEpgSuiviLibrePage;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.th.model.EpgHorsConnexionTemplate;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.utils.FileDownloadUtils;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.io.File;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.IdRef;

public abstract class AbstractEpgSuiviLibreApplication extends AbstractEpgSuiviLibrePage {
    private static final String LEGISLATURE_EN_COURS = "legislatureEnCours";

    private static final String LEGIS_EN_COURS = "enCours";
    private static final String LEGIS_PREC = "precedente";

    public AbstractEpgSuiviLibreApplication() {
        super();
    }

    @Path("consulter")
    public Object getHome(@QueryParam(LEGISLATURE_EN_COURS) Boolean legislatureEnCours) {
        legislatureEnCours = treatsLegis(legislatureEnCours);
        String application = getApplicationName();
        template = new EpgHorsConnexionTemplate("pages/suiviLibre/" + application, context);
        template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        template
            .getData()
            .put(
                PanTemplateConstants.OPTIONS,
                ImmutableList.of(
                    new SelectValueDTO(LEGIS_EN_COURS, "suiviLibre." + application + ".optin.enCours"),
                    new SelectValueDTO(LEGIS_PREC, "suiviLibre." + application + ".optin.precedente")
                )
            );
        template.getData().put(PanTemplateConstants.VALUE, legislatureEnCours ? LEGIS_EN_COURS : LEGIS_PREC);
        template.getData().put(PanTemplateConstants.CAN_CONSULT_MIN, canConsultMin());
        context.putInContextData(PanContextDataKey.SUIVI_CURRENT_SECTION, application);
        context.putInContextData(PanContextDataKey.SUIVI_TYPE_ACTE, getTypeActe());
        context.putInContextData(PanContextDataKey.LEGISLATURE_EN_COURS, legislatureEnCours);
        return newObject("SuiviLibreApplicationAjax", context, template);
    }

    @GET
    @Path("details/{id}")
    public Object consultDetail(
        @PathParam("id") String id,
        @QueryParam(LEGISLATURE_EN_COURS) Boolean legislatureEnCours
    ) {
        legislatureEnCours = treatsLegis(legislatureEnCours);
        Boolean finalLegislatureEnCours = legislatureEnCours;
        logAndDo(
            () -> {
                template = new EpgHorsConnexionTemplate("pages/suiviLibre/detail", context);
                TexteMaitre txt = context.getSession().getDocument(new IdRef(id)).getAdapter(TexteMaitre.class);
                context.setNavigationContextTitle(
                    new Breadcrumb(
                        txt.getTitreOfficiel(),
                        "/" + getApplicationName() + "/details/" + id,
                        Breadcrumb.SUBTITLE_ORDER + 1
                    )
                );
                template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
                template.getData().put(STTemplateConstants.TITRE, txt.getTitreOfficiel());
                template.getData().put(PanTemplateConstants.LEGISLATURE_EN_COURS, finalLegislatureEnCours);
                context.putInContextData(PanContextDataKey.SUIVI_TYPE_ACTE, getTypeActe());
                context.putInContextData(PanContextDataKey.SUIVI_CURRENT_SECTION, getApplicationName());
                context.putInContextData(PanContextDataKey.LEGISLATURE_EN_COURS, finalLegislatureEnCours);
                context.putInContextData(PanContextDataKey.NUMERO_NOR, txt.getNumeroNor());
                template.getData().put(PanTemplateConstants.TYPE_APPLICATION, getApplicationName());

                template.getData().put(EpgTemplateConstants.TYPE_ACTE, getTypeActe());
                template
                    .getData()
                    .put(
                        PanTemplateConstants.REPORTS,
                        PanUIServiceLocator.getPanStatistiquesUIService().getMainReportsSuiviLibre(context)
                    );
                template
                    .getData()
                    .put(
                        PanTemplateConstants.ACCORDION_REPORTS,
                        PanUIServiceLocator.getPanStatistiquesUIService().getSecondaryReportsSuiviLibre(context)
                    );
            }
        );
        return template;
    }

    @GET
    @Path("ministere/{id}")
    public Object consultDetailMin(
        @PathParam("id") String id,
        @QueryParam("action") String action,
        @QueryParam(LEGISLATURE_EN_COURS) Boolean legislatureEnCours
    ) {
        legislatureEnCours = treatsLegis(legislatureEnCours);
        Boolean finalLegislatureEnCours = legislatureEnCours;
        logAndDo(
            () -> {
                template = new EpgHorsConnexionTemplate("pages/suiviLibre/birtPage", context);
                context.putInContextData(PanContextDataKey.SUIVI_CURRENT_SECTION, getApplicationName());
                context.putInContextData(PanContextDataKey.LEGISLATURE_EN_COURS, finalLegislatureEnCours);
                context.putInContextData(PanContextDataKey.MINISTERE_PILOTE_ID, id);
                context.putInContextData(
                    PanContextDataKey.SUIVI_CURRENT_TAB,
                    ANReportEnum.SuiviConstants.SUIVI_MINISTERES + action + StringUtils.capitalize(getTypeActe())
                );
                template.getData().put(PanTemplateConstants.LEGISLATURE_EN_COURS, finalLegislatureEnCours);
                template.getData().put(EpgTemplateConstants.TYPE_ACTE, getTypeActe());
                template.getData().put(PanTemplateConstants.TYPE_APPLICATION, getApplicationName());
                String minLabel = STServiceLocator
                    .getOrganigrammeService()
                    .getOrganigrammeNodeById(id, OrganigrammeType.MINISTERE)
                    .getLabel();
                List<SuiviLibreReportDTO> list = PanUIServiceLocator
                    .getPanStatistiquesUIService()
                    .getReportsSuiviLibre(context);
                String title = list.get(0).getTitre() + " - " + minLabel;
                context.setNavigationContextTitle(
                    new Breadcrumb(
                        title,
                        "/" +
                        getApplicationName() +
                        "/ministere/" +
                        id +
                        "?legislatureEnCours=" +
                        finalLegislatureEnCours,
                        Breadcrumb.SUBTITLE_ORDER + 1
                    )
                );
                template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());

                template.getData().put(STTemplateConstants.TITRE, title);
                template.getData().put(PanTemplateConstants.REPORTS, list);
            }
        );
        return template;
    }

    @GET
    @Path("{tab}")
    public Object consultAdditionalReport(
        @PathParam("tab") String tab,
        @QueryParam(LEGISLATURE_EN_COURS) Boolean legislatureEnCours
    ) {
        legislatureEnCours = treatsLegis(legislatureEnCours);
        Boolean finalLegisEnCours = legislatureEnCours;
        logAndDo(
            () -> {
                template = new EpgHorsConnexionTemplate("pages/suiviLibre/birtPage", context);
                context.putInContextData(PanContextDataKey.SUIVI_CURRENT_SECTION, getApplicationName());
                context.putInContextData(PanContextDataKey.SUIVI_CURRENT_TAB, tab);
                context.putInContextData(PanContextDataKey.LEGISLATURE_EN_COURS, finalLegisEnCours);

                List<SuiviLibreReportDTO> liste = PanUIServiceLocator
                    .getPanStatistiquesUIService()
                    .getReportsSuiviLibre(context);

                String titre = liste.get(0).getTitre();
                context.setNavigationContextTitle(
                    new Breadcrumb(titre, "/" + getApplicationName() + "/" + tab, Breadcrumb.SUBTITLE_ORDER + 1)
                );
                template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());

                template.getData().put(EpgTemplateConstants.TYPE_ACTE, getTypeActe());
                template.getData().put(PanTemplateConstants.TYPE_APPLICATION, getApplicationName());
                template.getData().put(STTemplateConstants.TITRE, titre);
                template.getData().put(PanTemplateConstants.REPORTS, liste);
            }
        );

        return template;
    }

    @GET
    @Path("report")
    public Object getBirtReport(
        @QueryParam(LEGISLATURE_EN_COURS) Boolean legislatureEnCours,
        @SwRequired @QueryParam("filename") String filename
    ) {
        legislatureEnCours = treatsLegis(legislatureEnCours);

        Boolean finalLegislatureEnCours = legislatureEnCours;
        final Object[] res = new Object[1];
        logAndDo(
            () -> {
                String dir = finalLegislatureEnCours
                    ? SolonEpgServiceLocator.getActiviteNormativeService().getPathDirANStatistiquesPublie()
                    : SolonEpgServiceLocator
                        .getActiviteNormativeService()
                        .getPathDirANStatistiquesLegisPrecPublie(context.getSession());

                File file = new File(dir, fr.dila.st.core.util.FileUtils.sanitizePathTraversal(filename));

                res[0] = FileDownloadUtils.getAttachmentHtml(file, filename).getEntity();
            }
        );
        return res[0];
    }

    boolean treatsLegis(Boolean legis) {
        if (legis == null) {
            return true;
        }
        return legis;
    }

    abstract String getApplicationName();

    abstract String getTypeActe();

    boolean canConsultMin() {
        return false;
    }
}
