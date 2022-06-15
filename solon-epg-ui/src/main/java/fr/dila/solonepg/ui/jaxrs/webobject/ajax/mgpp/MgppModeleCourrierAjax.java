package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import static fr.dila.st.core.util.ResourceHelper.getString;

import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.validators.annot.SwId;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;

// webobject qui sert à tester le service de génération de courrier
@WebObject(type = "ModeleCourrierAjax")
public class MgppModeleCourrierAjax extends SolonWebObject {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("creer")
    public Response doCreer(
        @FormDataParam("modeleName") String modeleName,
        @FormDataParam("modeleFile") InputStream uploadedInputStream,
        @FormDataParam("fileName") String fileName,
        @FormDataParam("typesCommunication") List<FormDataBodyPart> typesCommunication
    ) {
        context.putInContextData(MgppContextDataKey.MODELE_NAME, modeleName);
        putInContext(uploadedInputStream, fileName, getTypesCommunication(typesCommunication));
        MgppUIServiceLocator.getModeleCourrierUIService().createModeleCourrier(context);
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("modifier")
    public Response doModifier(
        @FormDataParam("idModele") @SwId String idModele,
        @FormDataParam("modeleName") String modeleName,
        @FormDataParam("modeleFile") InputStream uploadedInputStream,
        @FormDataParam("fileName") String fileName,
        @FormDataParam("typesCommunication") List<FormDataBodyPart> typesCommunication
    ) {
        context.setCurrentDocument(idModele);
        context.putInContextData(STContextDataKey.ID, modeleName);
        context.putInContextData(MgppContextDataKey.MODELE_TEMPLATE_NAME, modeleName);
        putInContext(uploadedInputStream, fileName, getTypesCommunication(typesCommunication));
        MgppUIServiceLocator.getModeleCourrierUIService().updateModeleCourrier(context);
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    private List<String> getTypesCommunication(List<FormDataBodyPart> typesCommunication) {
        List<FormDataBodyPart> parts = ObjectHelper.requireNonNullElseGet(typesCommunication, Collections::emptyList);
        return Arrays.stream(parts.get(0).getValue().split(",")).collect(Collectors.toList());
    }

    private void putInContext(InputStream uploadedInputStream, String fileName, List<String> typesCommunication) {
        context.putInContextData(STContextDataKey.FILE_CONTENT, uploadedInputStream);
        context.putInContextData(STContextDataKey.FILE_NAME, fileName);
        context.putInContextData(MgppContextDataKey.TYPES_COMMUNICATION, typesCommunication);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("supprimer")
    public Response doRemove(@FormDataParam("idModele") @SwId String idModele) {
        context.getSession().removeDocument(new IdRef(idModele));
        context.getMessageQueue().addToastSuccess(getString("modele.courrier.delete.success"));
        addMessageQueueInSession(); // on ajoute le message en session pour l'afficher sur la page de redirection

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }
}
