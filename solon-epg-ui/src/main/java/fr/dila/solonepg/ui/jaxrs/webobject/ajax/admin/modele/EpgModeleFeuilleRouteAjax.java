package fr.dila.solonepg.ui.jaxrs.webobject.ajax.admin.modele;

import fr.dila.solonepg.api.service.EpgExcelService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgSqueletteList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgModeleFdrFicheUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.actions.EpgModeleFeuilleRouteActionService;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.solonepg.ui.th.bean.EpgSqueletteListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.bean.EpgModeleFdrForm;
import fr.dila.solonepg.ui.th.model.bean.SqueletteFdrForm;
import fr.dila.ss.core.enumeration.StatutModeleFDR;
import fr.dila.ss.ui.bean.fdr.FeuilleRouteDTO;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.jaxrs.webobject.ajax.modele.SSModeleFeuilleRouteAjax;
import fr.dila.ss.ui.services.actions.ModeleFeuilleRouteActionService;
import fr.dila.ss.ui.services.actions.SSActionsServiceLocator;
import fr.dila.ss.ui.th.bean.ModeleFDRListForm;
import fr.dila.ss.ui.th.bean.RechercheModeleFdrForm;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import fr.dila.st.ui.validators.annot.SwRequired;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "ModeleFeuilleRouteAjax")
public class EpgModeleFeuilleRouteAjax extends SSModeleFeuilleRouteAjax {

    @Path("modele/valider")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validerModele(@SwBeanParam EpgModeleFdrForm form, @FormParam("sauvegarder") boolean sauvegarder) {
        ModeleFeuilleRouteActionService modeleAction = SSActionsServiceLocator.getModeleFeuilleRouteActionService();
        EpgModeleFdrFicheUIService modeleFDRFicheUIService = EpgUIServiceLocator.getEpgModeleFdrFicheUIService();
        context.setCurrentDocument(form.getId());

        if (modeleAction.canValidateRoute(context)) {
            if (sauvegarder) {
                modeleFDRFicheUIService.updateModele(context, form);
            }
            // Si update OK on valide le modèle
            if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
                modeleAction.validateRouteModel(context);
                modeleAction.libererVerrou(context);
            }

            if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
                addMessageQueueInSession();
            }
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("/rechercher/resultats/export")
    @GET
    @Produces("text/csv")
    public Response exporterResultat() {
        Map<String, Object> map = UserSessionHelper.getUserSessionParameter(
            context,
            SSModeleFeuilleRouteAjax.MFDR_SEARCH_MAP
        );
        RechercheModeleFdrForm searchForm = (RechercheModeleFdrForm) map.get(STTemplateConstants.SEARCH_FORM);
        ModeleFDRListForm modeleSearchForm = (ModeleFDRListForm) map.get(STTemplateConstants.RESULT_FORM);
        // Désactivation de la pagination pour récupérer tous les résultats.
        modeleSearchForm.noPagination();
        context.putInContextData(SSContextDataKey.SEARCH_MODELEFDR_FORM, searchForm);
        context.putInContextData(SSContextDataKey.LIST_MODELE_FDR, modeleSearchForm);
        ModeleFDRList lstResults = SSActionsServiceLocator
            .getRechercheModeleFeuilleRouteActionService()
            .getModeles(context);
        EpgExcelService excelService = SolonEpgServiceLocator.getEpgExcelService();
        List<DocumentRef> modelesRefs = lstResults
            .getListe()
            .stream()
            .map(FeuilleRouteDTO::getId)
            .map(IdRef::new)
            .collect(Collectors.toList());
        Consumer<OutputStream> consumerOutputStream = excelService.creationExcelListModeleFdr(
            context.getSession(),
            modelesRefs
        );
        return FileDownloadUtils.getAttachmentXls(consumerOutputStream::accept, "export-recherche-modeles.xls");
    }

    @Override
    protected void setSpecificDataMap(Map<String, Object> map) {
        map.put(
            EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
            context.getActions(EpgActionCategory.SELECTION_TOOL_FDR_ACTIONS)
        );
    }

    @POST
    @Path("squelettes")
    public ThTemplate getSquelettes(@SwBeanParam EpgSqueletteListForm epgSqueletteListForm) {
        verifyAction(EpgActionEnum.ADMIN_MENU_MODELE_SQUELETTE, "squelettes");
        ThTemplate template = new AjaxLayoutThTemplate("fragments/table/table-squelettes", getMyContext());

        Map<String, Object> map = new HashMap<>();

        context.putInContextData(EpgContextDataKey.SQUELETTE_LIST_FORM, epgSqueletteListForm);

        EpgSqueletteList lstResults = EpgUIServiceLocator.getEpgSqueletteUIService().getSquelettes(context);

        map.put(EpgTemplateConstants.STATUT_SQUELETTE, StatutModeleFDR.values());
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.NB_RESULTS, lstResults.getNbTotal());
        map.put(STTemplateConstants.DATA_URL, EpgURLConstants.URL_SQUELETTES);
        map.put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_SQUELETTES);
        map.put(STTemplateConstants.RESULT_FORM, epgSqueletteListForm);
        map.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());

        map.put(STTemplateConstants.ACTION, context.getAction(EpgActionEnum.TAB_SQUELETTE_CREATION));
        template.setData(map);
        return template;
    }

    @Path("squelette/supprimer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response supprimerSquelette(@SwRequired @FormParam("id") String id) {
        context.setCurrentDocument(id);
        verifyAction(EpgActionEnum.TAB_SQUELETTE_SUPPRIMER, "squelette/supprimer");
        EpgUIServiceLocator.getEpgSqueletteUIService().deleteSquelette(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("squelette/retourList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response retourListeSquelettes(@SwRequired @FormParam("id") String id) {
        verifyAction(EpgActionEnum.SQUELETTE_RETOUR, "squelette/retourList");
        CoreSession session = context.getSession();
        EpgModeleFeuilleRouteActionService modeleAction = SolonEpgActionServiceLocator.getEpgModeleFeuilleRouteActionService();
        DocumentModel squeletteDoc = session.getDocument(new IdRef(id));
        context.setCurrentDocument(squeletteDoc);
        if (LockUtils.isLockedByCurrentUser(session, squeletteDoc.getRef())) {
            modeleAction.libererVerrou(context);
        }
        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("squelette/unlock")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response unlockSquelette(@SwRequired @FormParam("id") String id) {
        context.setCurrentDocument(id);
        EpgUIServiceLocator.getEpgSqueletteUIService().initSqueletteAction(context);
        verifyAction(EpgActionEnum.SQUELETTE_UNLOCK, "squelette/unlock");
        SolonEpgActionServiceLocator.getEpgModeleFeuilleRouteActionService().libererVerrou(context);
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("squelette/modifier")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifierSquelette(@SwRequired @FormParam("id") String id) {
        context.setCurrentDocument(id);
        EpgUIServiceLocator.getEpgSqueletteUIService().initSqueletteAction(context);
        verifyAction(EpgActionEnum.SQUELETTE_MODIFIER, "squelette/modifier");
        EpgUIServiceLocator.getEpgSqueletteUIService().invalidateRouteSquelette(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("squelette/valider")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validerSquelette(@SwBeanParam SqueletteFdrForm form) {
        context.setCurrentDocument(form.getId());
        EpgUIServiceLocator.getEpgSqueletteUIService().initSqueletteAction(context);
        verifyAction(EpgActionEnum.SQUELETTE_VALIDER, "squelette/valider");
        EpgModeleFeuilleRouteActionService modeleAction = SolonEpgActionServiceLocator.getEpgModeleFeuilleRouteActionService();
        context.putInContextData(EpgContextDataKey.SQUELETTE_FDR_FORM, form);
        EpgUIServiceLocator.getEpgSqueletteUIService().updateSquelette(context);
        modeleAction.validateSquelette(context);
        modeleAction.libererVerrou(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("/modeles/massAction/suppression")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifierModele(@FormParam("idModeles[]") List<String> idModeles) {
        EpgModeleFeuilleRouteActionService modeleAction = SolonEpgActionServiceLocator.getEpgModeleFeuilleRouteActionService();
        context.putInContextData(SSContextDataKey.ID_MODELES, idModeles);

        modeleAction.deleteMassModele(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }
}
