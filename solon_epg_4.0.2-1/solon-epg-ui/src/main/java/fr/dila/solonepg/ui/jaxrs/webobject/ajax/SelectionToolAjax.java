package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import fr.dila.solonepg.ui.bean.ReattributionNorSummaryList;
import fr.dila.solonepg.ui.bean.SelectionDto;
import fr.dila.solonepg.ui.bean.TraitementPapierList;
import fr.dila.solonepg.ui.bean.TransfertSummaryList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgSelectionTypeEnum;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.ControllerUtils;
import fr.dila.st.ui.utils.FileDownloadUtils;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "OutilSelectionAjax")
public class SelectionToolAjax extends AbstractEpgMassActionAjax {
    private static final String DIRECTION = "direction";
    private static final String MINISTERE = "ministere";
    private static final String ID = "id";
    private static final String IDS = "ids[]";

    @Path("ajouter")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDocToSelection(@SwId @SwRequired @FormParam(ID) String id) throws IOException {
        context.setCurrentDocument(id);
        EpgUIServiceLocator.getEpgSelectionToolUIService().addDocToSelection(context);
        return getSelectionToolTemplate();
    }

    @Path("retirer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeDocFromSelection(@SwId @SwRequired @FormParam(ID) String id) throws IOException {
        context.setCurrentDocument(id);
        EpgUIServiceLocator.getEpgSelectionToolUIService().removeDocFromSelection(context);
        return getSelectionToolTemplate();
    }

    @Path("valider/etape")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validerEtapeDossiers() throws IOException {
        EpgUIServiceLocator.getEpgSelectionToolUIService().initSelectionActionContext(context);
        verifyAction(EpgActionEnum.VALIDATE_STEP_SELECTION_TOOL, "valider/etape");
        EpgUIServiceLocator.getEpgDossierDistributionUIService().validerEtapeMassSelection(context);
        return getSelectionToolTemplate();
    }

    @Path("vider")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response viderSelection() throws IOException {
        EpgUIServiceLocator.getEpgSelectionToolUIService().initSelectionActionContext(context);
        verifyAction(EpgActionEnum.EMPTY_SELECTION_TOOL, "vider");
        EpgUIServiceLocator.getEpgDossierDistributionUIService().viderSelectionTool(context);
        return getSelectionToolTemplate();
    }

    @Path("ajouter/fdr")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFdrToSelection(@SwRequired @FormParam(IDS) List<String> ids) throws IOException {
        context.putInContextData(STContextDataKey.IDS, ids);
        context.putInContextData(EpgContextDataKey.SELECTION_TYPE, EpgSelectionTypeEnum.MODELE);
        EpgUIServiceLocator.getEpgSelectionToolUIService().addMultipleItemsToSelection(context);
        return getSelectionToolTemplate();
    }

    @Path("ajouter/dossiers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDocsToSelection(@SwRequired @FormParam(IDS) List<String> ids) throws IOException {
        context.putInContextData(STContextDataKey.IDS, ids);
        context.putInContextData(EpgContextDataKey.SELECTION_TYPE, EpgSelectionTypeEnum.DOSSIER);
        EpgUIServiceLocator.getEpgSelectionToolUIService().addMultipleItemsToSelection(context);
        return getSelectionToolTemplate();
    }

    @Path("create/list/epreuve")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createListEpreuve() throws IOException {
        EpgUIServiceLocator.getEpgSelectionToolUIService().initSelectionActionContext(context);
        verifyAction(EpgActionEnum.CREATE_LIST_DEMANDE_EPREUVE, "create/list/epreuve");
        EpgUIServiceLocator.getEpgSelectionToolUIService().createListDemandeEpreuve(context);
        return getSelectionToolTemplate();
    }

    @Path("add/dossiers/suivis")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDossiersSuivis() throws IOException {
        EpgUIServiceLocator.getEpgSelectionToolUIService().initSelectionActionContext(context);
        verifyAction(EpgActionEnum.ADD_CORBEILLE_DOSSIER_SUIVI, "add/dossiers/suivis");
        EpgUIServiceLocator.getEpgSelectionToolUIService().addDossiersToDossiersSuivis(context);
        return getSelectionToolTemplate();
    }

    @Path("create/list/publication")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createListPublication() throws IOException {
        EpgUIServiceLocator.getEpgSelectionToolUIService().initSelectionActionContext(context);
        verifyAction(EpgActionEnum.CREATE_LIST_DEMANDE_PUBLICATION, "create/list/publication");
        EpgUIServiceLocator.getEpgSelectionToolUIService().createListDemandePublication(context);
        return getSelectionToolTemplate();
    }

    @Path("create/list/signature")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createListSignature() throws IOException {
        EpgUIServiceLocator.getEpgSelectionToolUIService().initSelectionActionContext(context);
        verifyAction(EpgActionEnum.CREATE_LIST_MISE_EN_SIGNATURE, "create/list/signature");
        EpgUIServiceLocator.getEpgSelectionToolUIService().createListMiseEnSignature(context);
        return getSelectionToolTemplate();
    }

    @Path("create/list/signature/load/content")
    @POST
    public ThTemplate loadContentModalListSignature(
        @SwId @SwRequired @FormParam(EpgTemplateConstants.ID_LISTE) String id,
        @FormParam(EpgTemplateConstants.MESSAGE_ERROR) String message
    )
        throws IOException {
        ThTemplate template = new AjaxLayoutThTemplate(
            "fragments/components/modal-success-create-list-signature-content",
            context
        );
        TraitementPapierList listeDto = null;
        if (StringUtils.isNotEmpty(id)) {
            context.setCurrentDocument(id);
            listeDto = EpgUIServiceLocator.getEpgTraitementPapierListeUIService().consultTraitementPapierListe(context);
        }

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.RESULT_LIST, listeDto);
        map.put(EpgTemplateConstants.MESSAGE_ERROR, message);
        template.setData(map);

        return template;
    }

    @Path("list/signature/supprimer")
    @POST
    public ThTemplate removeElementFromListeModal(
        @SwRequired @FormParam(IDS) List<String> ids,
        @SwId @SwRequired @FormParam(EpgTemplateConstants.ID_LISTE) String idListe
    )
        throws IOException {
        context.setCurrentDocument(idListe);
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, ids);
        boolean isListeVide = EpgUIServiceLocator
            .getEpgTraitementPapierListeUIService()
            .removeElementFromListe(context);
        if (isListeVide) {
            return new AjaxLayoutThTemplate(
                "fragments/components/modal-success-create-list-signature-content",
                context
            );
        } else {
            return loadContentModalListSignature(idListe, null);
        }
    }

    @Path("list/signature/export/{idListe}")
    @GET
    @Produces("application/pdf")
    public Response exportListeModal(@PathParam(EpgTemplateConstants.ID_LISTE) String idListe) throws IOException {
        context.setCurrentDocument(idListe);
        File file = EpgUIServiceLocator.getEpgTraitementPapierListeUIService().exportListeModal(context);
        return FileDownloadUtils.getAttachmentPdf(file, file.getName());
    }

    @Path("abandon")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response abandonSelection() throws IOException {
        EpgUIServiceLocator.getEpgSelectionToolUIService().initSelectionActionContext(context);
        verifyAction(EpgActionEnum.ABANDON_SELECTION_TOOL, "abandon");
        EpgUIServiceLocator.getEpgSelectionToolUIService().abandonnerDossiers(context);
        return getSelectionToolTemplate();
    }

    @Path("supprimerMasse")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response supprimerDossiers(
        @SwRequired @FormParam("username") String username,
        @SwRequired @FormParam("data") String data
    ) {
        EpgUIServiceLocator.getEpgSelectionToolUIService().initSelectionActionContext(context);
        verifyAction(EpgActionEnum.SUPPRIMER_MASSE_SELECTION_TOOL, "supprimerMasse");
        return doDeleteDossiers(username, data);
    }

    private Response getSelectionToolTemplate() throws IOException {
        ThTemplate template = new AjaxLayoutThTemplate();
        template.setName("fragments/components/selection-tool-detail");

        Map<String, Object> map = new HashMap<>();
        EpgUIServiceLocator.getEpgSelectionToolUIService().initSelectionActionContext(context);
        List<Action> actions = context.getActions(EpgActionCategory.SELECTION_TOOL_ACTIONS);

        map.put(
            EpgTemplateConstants.SELECTION_TOOL_TYPE,
            EpgUIServiceLocator.getEpgSelectionToolUIService().getSelectionType(context)
        );
        map.put(
            EpgTemplateConstants.SELECTION_TOOL_ITEMS,
            EpgUIServiceLocator.getEpgSelectionToolUIService().getSelectionList(context)
        );
        map.put(EpgTemplateConstants.SELECTION_TOOL_ACTIONS, actions);
        template.setData(map);

        List<String> updatedIds = context.getFromContextData(EpgContextDataKey.UPDATED_IDS);

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("template", ControllerUtils.renderHtmlFromTemplate(template));
        jsonData.put("updatedIds", CollectionUtils.isNotEmpty(updatedIds) ? updatedIds : new ArrayList<>());
        jsonData.put(EpgTemplateConstants.ID_LISTE, context.getFromContextData(EpgContextDataKey.ID_LISTE));
        jsonData.put(EpgTemplateConstants.MESSAGE_ERROR, context.getFromContextData(EpgContextDataKey.MESSAGE_ERROR));

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue(), (Serializable) jsonData).build();
    }

    @Path("verifier/reattribution")
    @POST
    public ThTemplate checkReattributionDossiers(
        @SwId @SwRequired @FormParam(MINISTERE) String ministere,
        @SwId @SwRequired @FormParam(DIRECTION) String direction
    ) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/components/reattribution-nor-summary-modal", context);

        List<String> ids = EpgUIServiceLocator
            .getEpgSelectionToolUIService()
            .getSelectionList(context)
            .stream()
            .map(SelectionDto::getId)
            .collect(Collectors.toList());

        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, ids);

        ReattributionNorSummaryList list = EpgUIServiceLocator
            .getEpgSelectionSummaryUIService()
            .getReattributionSummaryList(context);

        Map<String, Object> map = new HashMap<>();

        map.put(EpgTemplateConstants.MINISTERE, ministere);
        map.put(EpgTemplateConstants.DIRECTION, direction);
        map.put(EpgTemplateConstants.REATTRIBUTION_LIST, list);

        template.setData(map);
        return template;
    }

    @Path("reattribuer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response doReattributionDossiers(
        @SwId @SwRequired @FormParam(MINISTERE) String ministere,
        @SwId @SwRequired @FormParam(DIRECTION) String direction
    ) {
        EpgUIServiceLocator.getEpgSelectionToolUIService().initSelectionActionContext(context);
        verifyAction(EpgActionEnum.REATTRIBUTION_NOR_SELECTION, "reattribuer");
        context.putInContextData(STContextDataKey.MINISTERE_ID, ministere);
        context.putInContextData(STContextDataKey.DIRECTION_ID, direction);
        EpgUIServiceLocator.getEpgSelectionToolUIService().reattribuerDossiers(context);

        getJsonResponseWithMessagesInSessionIfSuccess();

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("substituer/postes")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response doSubstitutionPostes(
        @SwId @SwRequired @FormParam("ancienPoste") String ancienPoste,
        @SwId @SwRequired @FormParam("nouveauPoste") String nouveauPoste
    )
        throws IOException {
        EpgUIServiceLocator.getEpgSelectionToolUIService().initSelectionActionContext(context);
        verifyAction(EpgActionEnum.SUBSTITUTION_POSTE_SELECTION, "substituer/postes");
        context.putInContextData(EpgContextDataKey.ANCIEN_POSTE, ancienPoste);
        context.putInContextData(EpgContextDataKey.NOUVEAU_POSTE, nouveauPoste);
        EpgUIServiceLocator.getEpgSelectionToolUIService().substituerPostes(context);

        return getSelectionToolTemplate();
    }

    @Path("verifier/transfert")
    @POST
    public ThTemplate checkTransfertDossiers(
        @SwId @SwRequired @FormParam(MINISTERE) String ministere,
        @SwId @SwRequired @FormParam(DIRECTION) String direction
    ) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/components/transfert-summary-modal", context);

        List<String> ids = EpgUIServiceLocator
            .getEpgSelectionToolUIService()
            .getSelectionList(context)
            .stream()
            .map(SelectionDto::getId)
            .collect(Collectors.toList());

        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, ids);

        TransfertSummaryList list = EpgUIServiceLocator
            .getEpgSelectionSummaryUIService()
            .getTransfertSummaryList(context);

        Map<String, Object> map = new HashMap<>();

        map.put(EpgTemplateConstants.MINISTERE, ministere);
        map.put(EpgTemplateConstants.DIRECTION, direction);
        map.put(EpgTemplateConstants.TRANSFERT_LIST, list);

        template.setData(map);
        return template;
    }

    @Path("transferer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response doTransfertDossiers(
        @SwId @SwRequired @FormParam(MINISTERE) String ministere,
        @SwId @SwRequired @FormParam(DIRECTION) String direction
    ) {
        EpgUIServiceLocator.getEpgSelectionToolUIService().initSelectionActionContext(context);
        verifyAction(EpgActionEnum.TRANSFERT_SELECTION, "transferer");
        context.putInContextData(STContextDataKey.MINISTERE_ID, ministere);
        context.putInContextData(STContextDataKey.DIRECTION_ID, direction);
        EpgUIServiceLocator.getEpgSelectionToolUIService().transfererDossiers(context);

        getJsonResponseWithMessagesInSessionIfSuccess();

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("substitution")
    public Object doSubstitution() {
        return newObject("SubstitutionMassAjax");
    }
}
