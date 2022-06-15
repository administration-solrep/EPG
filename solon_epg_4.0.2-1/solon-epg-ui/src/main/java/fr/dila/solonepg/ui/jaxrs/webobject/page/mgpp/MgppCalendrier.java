package fr.dila.solonepg.ui.jaxrs.webobject.page.mgpp;

import static fr.dila.solonepg.ui.enums.mgpp.MgppActionEnum.MGPP_CALENDRIER_PROMULGATION;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionEnum;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.model.mgpp.MgppEspaceParlementaireTemplate;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.commons.io.FilenameUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "MgppCalendrier")
public class MgppCalendrier extends SolonWebObject {
    private static final String URL_MGPP_CALENDRIER = "/mgpp/calendrier";
    private static final String NAVIGATION_ECP_TITLE = "Echéancier de promulgation";
    private static final String DATA_URL_EC = "/mgpp/calendrier/promulgation#main_content";
    private static final String NAVIGATION_SUIVI_TITLE = "menu.mgpp.calendrier.suivi.echeance";
    private static final String DATA_URL_SUIVI = "/mgpp/calendrier/suivi#main_content";

    public MgppCalendrier() {
        super();
    }

    @GET
    public ThTemplate getHome() {
        verifyAction(MgppActionEnum.MGPP_CALENDRIERS, URL_MGPP_CALENDRIER);
        template.setName("pages/mgpp/calendriers");
        context.removeNavigationContextTitle();
        template.setContext(context);

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();
        template.setData(map);
        return template;
    }

    @Path("promulgation")
    public Object getEcheancierPromulgation() {
        verifyAction(MgppActionEnum.MGPP_CALENDRIER_PROMULGATION, DATA_URL_EC);
        template.setName("pages/mgpp/echeancierPromulgation");
        template.setContext(context);
        context.setNavigationContextTitle(new Breadcrumb(NAVIGATION_ECP_TITLE, DATA_URL_EC, Breadcrumb.TITLE_ORDER));
        verifyAction(MGPP_CALENDRIER_PROMULGATION, DATA_URL_EC);
        return newObject("CalendrierAjax", context, template);
    }

    @Path("suivi")
    public Object getSuiviEcheances() {
        verifyAction(MgppActionEnum.MGPP_CALENDRIER_SUIVI, DATA_URL_SUIVI);
        template.setName("pages/mgpp/suiviEcheances");
        template.setContext(context);
        context.setNavigationContextTitle(
            new Breadcrumb(NAVIGATION_SUIVI_TITLE, DATA_URL_SUIVI, Breadcrumb.TITLE_ORDER)
        );

        return newObject("CalendrierSuiviEcheancesAjax", context, template);
    }

    @GET
    @Path("export/excel")
    @Produces("application/vnd.ms-excel")
    public Response exporterExcel() {
        Blob blob = getReport(BirtOutputFormat.XLS, "echeancier_promulgation");
        String fileName = modifyFileName(blob);

        return FileDownloadUtils.getAttachmentXls(blob.getFile(), fileName);
    }

    @GET
    @Path("export/pdf")
    @Produces("application/pdf")
    public Response exporterPdf() {
        Blob blob = getReport(BirtOutputFormat.PDF, "echeancier_promulgation");
        String fileName = modifyFileName(blob);

        return FileDownloadUtils.getAttachmentPdf(blob.getFile(), fileName);
    }

    @GET
    @Path("suivi/export/excel")
    @Produces("application/vnd.ms-excel")
    public Response exporterSuiviExcel() {
        Blob blob = getReport(BirtOutputFormat.XLS, "suivi_echeances");
        String fileName = modifyFileName(blob);

        return FileDownloadUtils.getAttachmentXls(blob.getFile(), fileName);
    }

    @GET
    @Path("suivi/export/pdf")
    @Produces("application/pdf")
    public Response exporterSuiviPdf() {
        Blob blob = getReport(BirtOutputFormat.PDF, "suivi_echeances");
        String fileName = modifyFileName(blob);

        return FileDownloadUtils.getAttachmentPdf(blob.getFile(), fileName);
    }

    private Blob getReport(BirtOutputFormat reportFormat, String reportId) {
        context.putInContextData(SSContextDataKey.BIRT_OUTPUT_FORMAT, reportFormat);
        context.putInContextData(SSContextDataKey.BIRT_REPORT_ID, reportId);
        return MgppUIServiceLocator.getMgppCalendrierUIService().generateReport(context);
    }

    private String modifyFileName(Blob blob) {
        String filename = blob.getFile().getName();
        return filename.substring(0, filename.indexOf("_")) + "." + FilenameUtils.getExtension(filename);
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new MgppEspaceParlementaireTemplate();
    }
}
