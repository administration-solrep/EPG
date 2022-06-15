package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan;

import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.th.model.bean.pan.TraiteForm;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanTraitesAjax")
public class PanTraitesAjax extends SolonWebObject {

    @POST
    @Path("ajouter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response ajouterElement(@SwBeanParam TraiteForm traiteForm) {
        SolonEpgServiceLocator
            .getActiviteNormativeService()
            .addTraiteToTableauMaitre(
                traiteForm.getIntitule(),
                traiteForm.getDateSignature().getTime(),
                traiteForm.getPublication(),
                context.getSession()
            );
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }
}
