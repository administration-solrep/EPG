package fr.dila.solonepg.ui.jaxrs.webobject.ajax.archivage;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "ArchivageIntermediaireAjax")
public class EpgArchivageIntermediaireAjax extends SolonWebObject {

    @GET
    public ThTemplate getArchivageIntermediaire(@SwBeanParam DossierListForm dossierlistForm) {
        verifyAction(EpgActionEnum.ADMIN_MENU_DOSSIER_SUPPRESSION, EpgURLConstants.ADMIN_ARCHIVAGE_INTERMEDIAIRE);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        EpgDossierList lstResults = SolonEpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersCandidatToArchivageIntermediaire(context);
        ThTemplate template = new AjaxLayoutThTemplate("fragments/table/tableDossiers", context);
        dossierlistForm = ObjectHelper.requireNonNullElseGet(dossierlistForm, DossierListForm::newForm);

        Map<String, Object> map = new HashMap<>();
        dossierlistForm.setColumnVisibility(lstResults.getListeColonnes());
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());
        map.put(STTemplateConstants.RESULT_FORM, dossierlistForm);
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.DATA_URL, EpgURLConstants.ADMIN_ARCHIVAGE_INTERMEDIAIRE);
        map.put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.AJAX_ARCHIVAGE_INTERMEDIAIRE);
        template.setData(map);
        return template;
    }

    @Path("retirer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFolders(
        @SwRequired @FormParam("idDossiers[]") List<String> idDossiers,
        @SwRequired @FormParam("nombreMois") Integer nombreMois
    ) {
        verifyAction(EpgActionEnum.RETIRER_DOSSIERS, EpgURLConstants.ADMIN_ARCHIVAGE_INTERMEDIAIRE);
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idDossiers);
        context.putInContextData(EpgContextDataKey.DUREE_MAINTIEN_EN_PRODUCTION, nombreMois);
        EpgUIServiceLocator.getEpgDossierUIService().retirerDossiersCandidatsArchivageIntermediaire(context);

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            context
                .getMessageQueue()
                .addSuccessToQueue(ResourceHelper.getString("admin.archivage.dossier.retirer.success"));
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("valider")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validate(
        @SwRequired @FormParam("username") String username,
        @SwRequired @FormParam("data") String data,
        @SwRequired @FormParam("idDossiers[]") List<String> idDossiers
    ) {
        verifyAction(EpgActionEnum.VALIDER_DOSSIERS, EpgURLConstants.ADMIN_ARCHIVAGE_INTERMEDIAIRE);
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idDossiers);

        SolonStatus status = SolonStatus.OK;
        UserManager manager = STServiceLocator.getUserManager();
        if (manager.checkUsernamePassword(username, data)) {
            EpgUIServiceLocator.getEpgDossierUIService().verserDossierBaseArchivageIntermediaire(context);

            if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
                context
                    .getMessageQueue()
                    .addSuccessToQueue(ResourceHelper.getString("admin.archivage.dossier.valider.success"));
                UserSessionHelper.putUserSessionParameter(
                    context,
                    SpecificContext.MESSAGE_QUEUE,
                    context.getMessageQueue()
                );
            }
        } else {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("admin.archivage.dossier.authentification.error"));
            status = SolonStatus.NOT_AUTORIZED;
        }

        return new JsonResponse(status, context.getMessageQueue()).build();
    }

    @Path("exporter")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response export() {
        context.putInContextData(
            EpgContextDataKey.EXPORT_TYPE,
            SolonEpgEventConstant.DOSSIERS_ARCHIVAGE_INTERMEDIAIRE_EXPORT_EVENT
        );
        SolonEpgUIServiceLocator.getSolonEpgDossierListUIService().exportDossiers(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }
}
