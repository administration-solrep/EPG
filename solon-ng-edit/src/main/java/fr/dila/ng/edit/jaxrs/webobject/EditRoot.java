package fr.dila.ng.edit.jaxrs.webobject;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import fr.dila.ng.edit.service.EditService;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;

@Path("edit")
@Produces("text/html;charset=UTF-8")
@WebObject(type = "ng-edit")
public class EditRoot extends ModuleRoot {

    public EditRoot() {
        super();
    }

    @GET
    @Path("/version")
    public Response version() {
        EditService editService = ServiceUtil.getRequiredService(EditService.class);

        CoreSession session = WebEngine.getActiveContext().getCoreSession();
        return editService.getSolonEditVersion(session);
    }

    @GET
    @Path("/{id}/download")
    public Response download(@PathParam("id") String idFichier) {
        EditService editService = ServiceUtil.getRequiredService(EditService.class);

        CoreSession session = WebEngine.getActiveContext().getCoreSession();
        return editService.telechargerFichier(session, idFichier);
    }

    @DELETE
    @Path("/token/{token}/revoke")
    public Response revokeToken(@PathParam("token") String token) {
        STServiceLocator.getTokenService().revokeToken(token);
        return Response.ok("{\"status\": 200, \"message\":\"Token révoqué\"}").build();
    }

    @POST
    @Path("/{id}/editing")
    public Response editing(@PathParam("id") String idFichier, @FormParam("version") long version) {
        EditService editService = ServiceUtil.getRequiredService(EditService.class);

        CoreSession session = WebEngine.getActiveContext().getCoreSession();
        return editService.fichierEnCoursModification(session, idFichier, version);
    }

    @POST
    @Path("/{id}/unmodified")
    public Response unmodified(@PathParam("id") String idFichier) {
        EditService editService = ServiceUtil.getRequiredService(EditService.class);

        CoreSession session = WebEngine.getActiveContext().getCoreSession();
        return editService.abandonnerModificationFichier(session, idFichier);
    }

    @POST
    @Path("/{id}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(
        @PathParam("id") String idFichier,
        @FormDataParam("file") InputStream uploadedInputStream,
        @FormDataParam("file") FormDataContentDisposition fileDetail,
        @FormDataParam("version") long version
    ) {
        EditService editService = ServiceUtil.getRequiredService(EditService.class);

        CoreSession session = WebEngine.getActiveContext().getCoreSession();
        return editService.modifierFichier(session, idFichier, version, fileDetail.getFileName(), uploadedInputStream);
    }
}
