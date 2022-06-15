package fr.dila.solonepg.ui.jaxrs.webobject.page.suiviLibre;

import fr.dila.solonepg.ui.jaxrs.webobject.ajax.suiviLibre.EpgSuiviLibreActiviteParlementaireAjax;
import fr.dila.solonepg.ui.th.bean.MgppFichePresentationOEPForm;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.EpgHorsConnexionTemplate;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.io.File;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "ActiviteParlementaire")
public class EpgSuiviLibreActiviteParlementaire extends SolonWebObject {
    private static final String ELEMENT_URL = "elementUrl";

    public EpgSuiviLibreActiviteParlementaire() {
        super();
    }

    @GET
    public ThTemplate getHome(@SwBeanParam MgppFichePresentationOEPForm form) {
        template = new EpgHorsConnexionTemplate("pages/suiviLibre/activiteParlementaire", context);
        EpgSuiviLibreActiviteParlementaireAjax.init(template, form, context);
        return template;
    }

    @GET
    @Path("{id}")
    public ThTemplate consultOEP(@PathParam("id") String id) {
        template = new EpgHorsConnexionTemplate("pages/suiviLibre/activiteParlementaireElement", context);

        context.setNavigationContextTitle(
            new Breadcrumb(
                id,
                EpgURLConstants.URL_SUIVI_LIBRE_ACTIVITE_PARLEMENTAIRE + "/" + id,
                Breadcrumb.SUBTITLE_ORDER + 1
            )
        );

        template.getData().put(STTemplateConstants.TITRE, id);
        template
            .getData()
            .put(ELEMENT_URL, EpgURLConstants.URL_SUIVI_LIBRE_ACTIVITE_PARLEMENTAIRE + "/link?element=" + id);
        return template;
    }

    @GET
    @Path("link")
    public Object getFrameContent(@SwNotEmpty @QueryParam("element") String id) {
        File f = grabFirstExistingFile(SolonMgppViewConstant.OEP_FICHES_PUBLIEES_PREFIX + id + ".html");
        return f != null ? FileDownloadUtils.getAttachmentHtml(f, id).getEntity() : null;
    }

    @GET
    @Path("telecharger")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response telecharger(@SwNotEmpty @QueryParam("element") String id) {
        File f = grabFirstExistingFile(SolonMgppViewConstant.OEP_FICHES_PUBLIEES_PREFIX + id + ".pdf");
        return f != null
            ? FileDownloadUtils.getAttachmentPdf(f, SolonMgppViewConstant.OEP_FICHES_PUBLIEES_PREFIX + id + ".pdf")
            : Response.noContent().build();
    }

    private File grabFirstExistingFile(String name) {
        DossierService mgppDossierService = SolonMgppServiceLocator.getDossierService();
        String generatedReportPath = mgppDossierService.getPathDirAPPublie();
        String generatedReportPathRepertoire = mgppDossierService.getPathAPPublieRepertoire();
        String sanitizedName = FileUtils.sanitizePathTraversal(name);

        File f = new File(generatedReportPath + "/" + sanitizedName);
        if (!f.exists()) {
            f = new File(generatedReportPathRepertoire + "/" + sanitizedName);
        }
        return (f.exists()) ? f : null;
    }
}
