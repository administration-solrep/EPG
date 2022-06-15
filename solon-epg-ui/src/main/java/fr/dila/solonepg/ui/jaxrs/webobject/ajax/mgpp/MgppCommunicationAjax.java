package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.COMMUNICATION_ID;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.METTRE_EN_ATTENTE;
import static fr.dila.st.ui.enums.STContextDataKey.TRANSMETTRE_PAR_MEL_FORM;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.bean.FondDTO;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.bean.TransmettreParMelForm;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "CommunicationAjax")
public class MgppCommunicationAjax extends SolonWebObject {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("publier")
    public Response doPublier(@FormParam("idMessage") String idMessage) {
        context.putInContextData(COMMUNICATION_ID, idMessage);
        MgppUIServiceLocator.getEvenementActionService().publierEvenement(context);

        putQueueInUserSession();
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("mettreEnAttente")
    public Response doMettreEnAttente(
        @SwRequired @FormParam("idMessage") String idMessage,
        @SwRequired @FormParam("attente") Boolean mettreEnAttente
    ) {
        context.putInContextData(COMMUNICATION_ID, idMessage);
        context.putInContextData(METTRE_EN_ATTENTE, mettreEnAttente);
        MgppUIServiceLocator.getEvenementActionService().mettreEnAttente(context);

        putQueueInUserSession();
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("supprimer")
    public Response doSupprimer(@FormParam("idMessage") String idMessage) {
        context.putInContextData(COMMUNICATION_ID, idMessage);
        MgppUIServiceLocator.getEvenementActionService().supprimerEvenement(context);

        putQueueInUserSession();
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("annuler")
    public Response doAnnulerCommunication(@FormParam("idMessage") String idMessage) {
        context.putInContextData(COMMUNICATION_ID, idMessage);
        MgppUIServiceLocator.getEvenementActionService().annulerEvenement(context);

        putQueueInUserSession();
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("enCoursDeTraitement")
    public Response doEnCoursDeTraitement(@FormParam("idMessage") String idMessage) {
        context.putInContextData(COMMUNICATION_ID, idMessage);
        MgppUIServiceLocator.getEvenementActionService().enCoursTraitementEvenement(context);

        putQueueInUserSession();
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("accepter")
    public Response doAccepter(@FormParam("idMessage") String idMessage) {
        context.putInContextData(COMMUNICATION_ID, idMessage);
        MgppUIServiceLocator.getEvenementActionService().accepterVersion(context);

        putQueueInUserSession();
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("rejeter")
    public Response doRejeter(@FormParam("idMessage") String idMessage) {
        context.putInContextData(COMMUNICATION_ID, idMessage);
        MgppUIServiceLocator.getEvenementActionService().rejeterVersion(context);

        putQueueInUserSession();
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("accuserReception")
    public Response doAccuserReception(@FormParam("idMessage") String idMessage) {
        context.putInContextData(COMMUNICATION_ID, idMessage);
        MgppUIServiceLocator.getEvenementActionService().accuserReceptionVersion(context);

        putQueueInUserSession();
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("abandonner")
    public Response doAbandonner(@FormParam("idMessage") String idMessage) {
        context.putInContextData(COMMUNICATION_ID, idMessage);
        MgppUIServiceLocator.getEvenementActionService().abandonnerVersion(context);

        putQueueInUserSession();
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Path("envoyerMel")
    @Produces(MediaType.APPLICATION_JSON)
    public Response envoyerMel(@SwBeanParam TransmettreParMelForm transmettreParMelForm) {
        context.putInContextData(TRANSMETTRE_PAR_MEL_FORM, transmettreParMelForm);
        MgppUIServiceLocator.getEvenementActionService().transmettreParMelEnvoyer(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    private void putQueueInUserSession() {
        if (CollectionUtils.isNotEmpty(context.getMessageQueue().getSuccessQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }
    }

    @POST
    @Path("ajoutDocuments")
    public ThTemplate ajoutDocuments(
        @FormParam("id") @SwRequired @SwId String id,
        @FormParam("pieceId") @SwRequired String pieceId
    ) {
        context.putInContextData(STContextDataKey.ID, id);
        Dossier currentDossier = MgppUIServiceLocator.getMgppDossierUIService().findDossierFromId(context);

        if (currentDossier != null) {
            context.setCurrentDocument(currentDossier.getDocument());

            template = new AjaxLayoutThTemplate("pages/mgpp/listeDocuments", context);

            FondDTO parapheurDto = EpgUIServiceLocator.getEpgParapheurUIService().getParapheurDTO(context);
            template.getData().put(EpgTemplateConstants.PARAPHEUR_DTO, parapheurDto);
            FondDTO fondDto = EpgUIServiceLocator.getEpgFondDeDossierUIService().getFondDTO(context);
            template.getData().put(EpgTemplateConstants.FOND_DTO, fondDto);
            template.getData().put(STTemplateConstants.LST_COLONNES, fondDto.getLstColonnes());
            template.getData().put(EpgTemplateConstants.PIECE_ID, pieceId);
            template.getData().put(EpgTemplateConstants.DOSSIER_ID, id);
            template
                .getData()
                .put(STTemplateConstants.EDIT_ACTIONS, context.getActions(MgppActionCategory.MGPP_DOCUMENTS_LIST_ADD));
        } else {
            template = new AjaxLayoutThTemplate("error/modale-error", context);
            template.getData().put(STTemplateConstants.MESSAGE, ResourceHelper.getString("dossier.notfound.error", id));
        }

        return template;
    }

    @Path("lier/oep")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response lierOEP(
        @FormParam("idMessage") @SwNotEmpty String idMessage,
        @FormParam("organismeOEP") @SwNotEmpty String organismeOEP
    ) {
        context.putInContextData(COMMUNICATION_ID, idMessage);
        context.putInContextData(MgppContextDataKey.ORGANISME_OEP, organismeOEP);

        MgppUIServiceLocator.getMgppEvenementDetailsUIservice().lierOEP(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }
}
