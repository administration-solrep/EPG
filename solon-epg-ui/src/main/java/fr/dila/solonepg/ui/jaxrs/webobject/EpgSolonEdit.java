package fr.dila.solonepg.ui.jaxrs.webobject;

import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.ss.ui.jaxrs.webobject.page.SSSuivi;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.ui.utils.FileDownloadUtils;
import java.io.File;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliEdit")
public class EpgSolonEdit extends SSSuivi {

    public EpgSolonEdit() {
        super();
    }

    @GET
    public Response getEdit() {
        final ConfigService configService = STServiceLocator.getConfigService();

        String folder = configService.getValue(SolonEpgConfigConstant.SOLON_EDIT_FOLDER);
        String filename = configService.getValue(SolonEpgConfigConstant.SOLON_EDIT_FILE);
        if (StringUtils.isBlank(folder) || StringUtils.isBlank(filename)) {
            throw new NuxeoException(
                "Configuration manquante pour l'archive solon edit",
                Response.Status.NOT_FOUND.getStatusCode()
            );
        }
        return FileDownloadUtils.getResponse(new File(folder), FileUtils.sanitizePathTraversal(filename));
    }

    @Path("key")
    @GET
    public Response getControlKey() {
        final ConfigService configService = STServiceLocator.getConfigService();

        String folder = configService.getValue(SolonEpgConfigConstant.SOLON_EDIT_CONTROL_KEY_FOLDER);
        String filename = configService.getValue(SolonEpgConfigConstant.SOLON_EDIT_CONTROL_KEY_FILE);
        if (StringUtils.isBlank(folder) || StringUtils.isBlank(filename)) {
            throw new NuxeoException(
                "Configuration manquante pour la clé de contrôle solon edit",
                Response.Status.NOT_FOUND.getStatusCode()
            );
        }
        return FileDownloadUtils.getResponse(new File(folder), FileUtils.sanitizePathTraversal(filename));
    }
}
