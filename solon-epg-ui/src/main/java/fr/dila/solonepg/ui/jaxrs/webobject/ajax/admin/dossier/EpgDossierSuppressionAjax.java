package fr.dila.solonepg.ui.jaxrs.webobject.ajax.admin.dossier;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.ui.bean.SelectionSummaryDTO;
import fr.dila.solonepg.ui.bean.SelectionSummaryList;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.admin.dossier.EpgDossiersSuppression;
import fr.dila.solonepg.ui.services.EpgSelectionSummaryUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossiersSuppressionAjax")
public class EpgDossierSuppressionAjax extends SolonWebObject {
    private static final String DELETABLE_DOSSIERS_ID = "adminDeletableDossiersId";
    private static final String AUTH_TEMPLATE_URL = "fragments/components/modal-suppression-dossiers-auth-content";
    private static final String CONFIRM_AND_AUTH_TEMPLATE_URL =
        "fragments/components/modal-suppression-dossiers-summary-content";
    private static final String NO_DELETABLE_DOS_TEMPLATE_URL =
        "fragments/components/modal-suppression-dossiers-no-deletable-dos-content";
    private static final String DATA_DELETE = "/admin/dossiers/suppression";
    private static final String ONGLET_CONSULTATION = "consultation";
    private static final String ONGLET_SUIVI = "suivi";

    @Path("{tab}")
    @POST
    public ThTemplate getListDossierASupprimerTab(
        @PathParam("tab") String tab,
        @SwBeanParam DossierListForm dossierlistForm
    ) {
        if (ONGLET_CONSULTATION.equals(tab)) {
            verifyAction(
                EpgActionEnum.TAB_DOSSIERS_SUPPRIMER_CONSULTATION,
                DATA_DELETE + "&tab=" + ONGLET_CONSULTATION
            );
        } else {
            verifyAction(EpgActionEnum.TAB_DOSSIERS_SUPPRIMER_SUIVI, DATA_DELETE + "&tab=" + ONGLET_SUIVI);
        }

        template = new AjaxLayoutThTemplate("fragments/table/tableDossiers", context);
        Map<String, Object> map = EpgDossiersSuppression.createMapFromTab(tab, dossierlistForm, context);
        template.setData(map);
        return template;
    }

    @Path("retirer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFolders(@SwRequired @FormParam("idDossiers[]") List<String> idDossiers) {
        verifyAction(EpgActionEnum.RETIRER_DOSSIERS, DATA_DELETE);
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idDossiers);
        EpgUIServiceLocator.getEpgDossierUIService().removeDossiersCandidatElimination(context);

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            context.getMessageQueue().addToastSuccess(ResourceHelper.getString("admin.delete.dossier.retirer.success"));
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @GET
    @Path("selectionner")
    public ThTemplate showContentModalDeleteFolders(@SwRequired @QueryParam("idDossiers[]") List<String> idDossiers) {
        verifyAction(EpgActionEnum.SUPPRIMER_DOSSIERS, DATA_DELETE);
        ThTemplate template = new AjaxLayoutThTemplate();

        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idDossiers);
        context.putInContextData(EpgContextDataKey.IS_FROM_ADMINISTRATION, true);
        template.setContext(context);

        EpgSelectionSummaryUIService epgSelectionSummaryUIService = EpgUIServiceLocator.getEpgSelectionSummaryUIService();
        SelectionSummaryList selectionSummaryList = epgSelectionSummaryUIService.getSelectionSummaryList(context);

        template.setName(
            CollectionUtils.isNotEmpty(selectionSummaryList.getDeletableDossiers())
                ? (
                    CollectionUtils.isEmpty(selectionSummaryList.getIgnoredDossiers())
                        ? AUTH_TEMPLATE_URL
                        : CONFIRM_AND_AUTH_TEMPLATE_URL
                )
                : NO_DELETABLE_DOS_TEMPLATE_URL
        );

        Map<String, Object> map = new HashMap<>();
        map.put(
            EpgTemplateConstants.SHOW_SUMMARY,
            CollectionUtils.isNotEmpty(selectionSummaryList.getIgnoredDossiers())
        );
        map.put(EpgTemplateConstants.IGNORED_DOSSIERS, selectionSummaryList.getIgnoredDossiers());
        map.put(EpgTemplateConstants.DELETABLE_DOSSIERS, selectionSummaryList.getDeletableDossiers());
        UserSessionHelper.putUserSessionParameter(
            context,
            DELETABLE_DOSSIERS_ID,
            selectionSummaryList
                .getDeletableDossiers()
                .stream()
                .map(SelectionSummaryDTO::getId)
                .collect(Collectors.toList())
        );
        map.put(EpgTemplateConstants.URL_DELETE_AJAX, "/admin/dossiers/suppression/supprimer");
        template.setData(map);

        return template;
    }

    @POST
    @Path("supprimer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDossiers(
        @SwRequired @FormParam("username") String username,
        @SwRequired @FormParam("data") String data
    ) {
        verifyAction(EpgActionEnum.SUPPRIMER_DOSSIERS, DATA_DELETE);
        SolonStatus status = SolonStatus.OK;
        context.putInContextData(
            EpgContextDataKey.ID_DOSSIERS,
            UserSessionHelper.getUserSessionParameter(context, DELETABLE_DOSSIERS_ID, List.class)
        );
        UserManager manager = STServiceLocator.getUserManager();

        if (manager.checkUsernamePassword(username, data)) {
            EpgUIServiceLocator.getEpgDossierUIService().deleteDossiers(context);
            context
                .getMessageQueue()
                .addSuccessToQueue(ResourceHelper.getString("admin.delete.dossier.delete.success"));
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        } else {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("admin.delete.dossier.authentification.error"));
            status = SolonStatus.NOT_AUTORIZED;
        }

        return new JsonResponse(status, context.getMessageQueue()).build();
    }

    @Path("valider")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validate(
        @SwRequired @FormParam("username") String username,
        @SwRequired @FormParam("data") String data,
        @SwRequired @FormParam("idDossiers[]") List<String> idDossiers
    ) {
        verifyAction(EpgActionEnum.VALIDER_DOSSIERS, DATA_DELETE);
        SolonStatus status = SolonStatus.OK;
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idDossiers);
        UserManager manager = STServiceLocator.getUserManager();
        if (manager.checkUsernamePassword(username, data)) {
            EpgUIServiceLocator.getEpgDossierUIService().validateDossiersCandidatElimination(context);
            if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
                context
                    .getMessageQueue()
                    .addSuccessToQueue(ResourceHelper.getString("admin.delete.dossier.valider.success"));
                UserSessionHelper.putUserSessionParameter(
                    context,
                    SpecificContext.MESSAGE_QUEUE,
                    context.getMessageQueue()
                );
            }
        } else {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("admin.delete.dossier.authentification.error"));
            status = SolonStatus.NOT_AUTORIZED;
        }

        return new JsonResponse(status, context.getMessageQueue()).build();
    }

    @Path("exporter")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response export(@SwNotEmpty @QueryParam("tab") String tab) {
        String exportType = SolonEpgEventConstant.DOSSIERS_SUPPRESSION_SUIVI_EXPORT_EVENT;
        if (ONGLET_CONSULTATION.equals(tab)) {
            exportType = SolonEpgEventConstant.DOSSIERS_SUPPRESSION_CONSULTATION_EXPORT_EVENT;
        }

        context.putInContextData(EpgContextDataKey.EXPORT_TYPE, exportType);
        SolonEpgUIServiceLocator.getSolonEpgDossierListUIService().exportDossiers(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @GET
    @Path("onglet")
    public ThTemplate doGetOnglet(@SwNotEmpty @QueryParam("tab") String tab) {
        template = new AjaxLayoutThTemplate("fragments/dossier/dossiers-suppression/onglet", context);

        Map<String, Object> map = EpgDossiersSuppression.createMapFromTab(tab, null, context);
        template.setData(map);
        return template;
    }
}
