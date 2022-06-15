package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.DOSSIER_COMMUNICATION_CONSULTATION_FICHE;

import com.google.gson.Gson;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.ui.bean.MgppDossierCommunicationConsultationFiche;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppUserSessionKey;
import fr.dila.solonepg.ui.services.mgpp.MgppFicheUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.MgppTemplateConstants;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossierSimpleFicheAjax")
public class MgppDossierSimpleFicheAjax extends SolonWebObject {

    public MgppDossierSimpleFicheAjax() {
        super();
    }

    @GET
    public ThTemplate getFiche(@PathParam("id") String id, @PathParam("tab") String tab) {
        if (template.getData() == null) {
            Map<String, Object> map = new HashMap<>();
            template.setData(map);
        }

        // Le current document peut être une fiche ou un dossier
        boolean isFicheVerrouillee = false;
        if (context.getCurrentDocument() != null) {
            isFicheVerrouillee =
                LockUtils.isLockedByCurrentUser(context.getSession(), context.getCurrentDocument().getRef());
        }

        context.putInContextData(MgppContextDataKey.IS_FICHE_VERROUILLEE, isFicheVerrouillee);
        // Setter la fiche comme current document
        DocumentModel doc = context.getSession().getDocument(new IdRef(id));
        if (doc.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
            Dossier dossier = doc.getAdapter(Dossier.class);
            FicheLoi ficheLoi = SolonMgppServiceLocator
                .getDossierService()
                .findOrCreateFicheLoi(context.getSession(), dossier.getNumeroNor());
            context.setCurrentDocument(ficheLoi.getDocument());
        } else {
            context.setCurrentDocument(id);
        }
        MgppFicheUIService ficheUIService = MgppUIServiceLocator.getMgppFicheUIService();
        MgppDossierCommunicationConsultationFiche fiche = ficheUIService.getFicheRemplie(context);

        if (isFicheVerrouillee) {
            List<SelectValueDTO> courrierSelect = MgppUIServiceLocator
                .getModeleCourrierUIService()
                .getAvailableModels(context);
            template.getData().put("courrierSelect", courrierSelect);
            context.putInContextData(DOSSIER_COMMUNICATION_CONSULTATION_FICHE, fiche);
            context.putInContextData(
                MgppContextDataKey.IS_FICHE_SUPPRIMABLE,
                ficheUIService.isFicheSupprimable(context)
            );
            template
                .getData()
                .put(
                    MgppTemplateConstants.MGPP_GENERALE_ACTIONS,
                    context.getActions(MgppActionCategory.COMMUNICATION_FICHE_EDIT_ACTION)
                );
        } else {
            template
                .getData()
                .put(
                    MgppTemplateConstants.MGPP_GENERALE_ACTIONS,
                    context.getActions(MgppActionCategory.COMMUNICATION_FICHE_ACTION)
                );
        }
        template
            .getData()
            .put(
                MgppTemplateConstants.MGPP_PRINT_ACTIONS,
                context.getActions(MgppActionCategory.COMMUNICATION_FICHE_PRINT_ACTION)
            );

        String dossiersParlementairesSelected = UserSessionHelper.getUserSessionParameter(
            context,
            MgppUserSessionKey.DOSSIER_PARLEMENTAIRE
        );

        template.getData().put(STTemplateConstants.ID, context.getCurrentDocument().getId());
        template.getData().put(MgppTemplateConstants.LST_WIDGETS_PRESENTATION, fiche.getLstWidgetsPresentation());
        template.getData().put(MgppTemplateConstants.LST_WIDGETS_FICHE, fiche.getLstWidgetsFiche());
        template.getData().put(MgppTemplateConstants.LST_WIDGETS_DEPOT, fiche.getLstWidgetsDepot());
        template.getData().put(MgppTemplateConstants.LST_NAVETTES, fiche.getLstNavettes());
        template.getData().put(MgppTemplateConstants.LST_WIDGETS_LOI_VOTEE, fiche.getLstWidgetsLoiVotee());
        template.getData().put(MgppTemplateConstants.LST_TABLES_REPRESENTANTS, fiche.getLstTablesRepresentants());
        template
            .getData()
            .put(
                MgppTemplateConstants.IS_DIFFUSION_BUTTON_VISIBLE,
                StringUtils.equals(dossiersParlementairesSelected, "mgpp_designationOEP")
            );
        template.getData().put(MgppTemplateConstants.IS_FICHE_DIFFUSEE, fiche.isFicheDiffusee());
        template.getData().put(MgppTemplateConstants.IDENTIFIANT_DOSSIER, fiche.getIdDossier());
        template
            .getData()
            .put(
                MgppTemplateConstants.CAN_GENERER_COURRIER,
                !StringUtils.equals(FichePresentationAVI.DOC_TYPE, fiche.getTypeFiche())
            );

        return template;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("sauvegarde")
    public Response enregistrerFiche(
        @FormParam("fiche") String json,
        @FormParam("idFiche") String idFiche,
        @FormParam("diffuser") Boolean diffuser
    ) {
        Gson gson = new Gson();
        context.putInContextData(MgppContextDataKey.FICHE_METADONNEES_MAP, gson.fromJson(json, Map.class));
        context.setCurrentDocument(idFiche);

        MgppFicheUIService ficheUIService = MgppUIServiceLocator.getMgppFicheUIService();
        ficheUIService.saveFiche(context);
        if (
            BooleanUtils.isTrue(diffuser) &&
            FichePresentationOEPImpl.DOC_TYPE.equals(context.getCurrentDocument().getType())
        ) {
            ficheUIService.diffuserFiche(context);
        }

        if (CollectionUtils.isNotEmpty(context.getMessageQueue().getSuccessQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("verrouiller")
    public Response verrouillerFiche(@FormParam("ficheId") @SwNotEmpty @SwId String idFiche) {
        CoreSession session = context.getSession();

        DocumentModel ficheDoc = session.getDocument(new IdRef(idFiche));
        STActionsServiceLocator
            .getSTLockActionService()
            .lockDocuments(context, session, Collections.singletonList(ficheDoc), ficheDoc.getType());

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deverrouiller")
    public Response deverrouillerFiche(@FormParam("ficheId") @SwNotEmpty @SwId String idFiche) {
        CoreSession session = context.getSession();

        DocumentModel ficheDoc = session.getDocument(new IdRef(idFiche));
        STActionsServiceLocator
            .getSTLockActionService()
            .unlockDocumentsUnrestricted(context, Collections.singletonList(ficheDoc), ficheDoc.getType());

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        String templateName = MgppUIServiceLocator.getMgppFicheUIService().isFicheLoiVisible(getMyContext())
            ? "fragments/mgpp/dossier/onglets/ficheLoi"
            : "fragments/mgpp/dossier/onglets/fichePresentation";

        return new AjaxLayoutThTemplate(templateName, getMyContext());
    }
}
