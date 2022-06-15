package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwRequired;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "EpgSaisineRectificativeAjax")
public class EpgSaisineRectificativeAjax extends SolonWebObject {

    public EpgSaisineRectificativeAjax() {
        super();
    }

    @Path("envoyer/saisine")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response envoyerSaisineRectificative(@FormParam("dossierId") @SwId @SwRequired String dossierId) {
        context.setCurrentDocument(dossierId);
        SolonEpgUIServiceLocator.getEpgDossierDistributionUIService().doSaisineRectificative(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("envoyer/pieces/complementaires")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response envoyerPiecesComplementaires(@FormParam("dossierId") @SwId @SwRequired String dossierId) {
        context.setCurrentDocument(dossierId);
        SolonEpgUIServiceLocator.getEpgDossierDistributionUIService().doEnvoiPieceComplementaire(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }
}
