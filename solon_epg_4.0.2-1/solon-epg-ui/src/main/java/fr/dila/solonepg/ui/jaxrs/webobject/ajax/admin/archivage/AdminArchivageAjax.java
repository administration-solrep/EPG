package fr.dila.solonepg.ui.jaxrs.webobject.ajax.admin.archivage;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.core.recherche.EpgDossierListingDTOImpl;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
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
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AdminArchivageAjax")
public class AdminArchivageAjax extends SolonWebObject {

    @GET
    @Path("definitif")
    public ThTemplate getDossierArchivageDefinitif(@SwBeanParam DossierListForm dossierlistForm) {
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        ThTemplate template = new AjaxLayoutThTemplate("fragments/table/tableDossiers", context);
        EpgDossierList lstResults = SolonEpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersCandidatToArchivageDefinitive(context);
        dossierlistForm.setColumnVisibility(lstResults.getListeColonnes());

        template.getData().put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        template.getData().put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        template
            .getData()
            .put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());
        template.getData().put(STTemplateConstants.RESULT_FORM, dossierlistForm);
        template.getData().put(STTemplateConstants.RESULT_LIST, lstResults);
        template.getData().put(STTemplateConstants.NB_RESULTS, lstResults.getNbTotal());
        template.getData().put(STTemplateConstants.TITRE, lstResults.getTitre());
        template.getData().put(STTemplateConstants.SOUS_TITRE, lstResults.getSousTitre());
        template.getData().put(STTemplateConstants.DISPLAY_TABLE, !lstResults.getListe().isEmpty());
        template.getData().put(STTemplateConstants.DATA_URL, EpgURLConstants.ADMIN_ARCHIVAGE_DEFINITIF);
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.AJAX_ARCHIVAGE_DEFINITIF);
        return template;
    }

    @Path("retirer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response retirerDossiers(@SwRequired @FormParam("idDossiers[]") List<String> idDossiers) {
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idDossiers);
        EpgUIServiceLocator.getEpgDossierUIService().retirerDossiersArchivageDefinitive(context);
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

    @Path("exporter")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response export() {
        context.putInContextData(
            EpgContextDataKey.EXPORT_TYPE,
            SolonEpgEventConstant.DOSSIERS_ARCHIVAGE_DEFINITIF_EXPORT_EVENT
        );
        SolonEpgUIServiceLocator.getSolonEpgDossierListUIService().exportDossiers(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("genererArchive")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response genererArchive() {
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, DossierListForm.newForm());
        context.putInContextData(
            EpgContextDataKey.ID_DOSSIERS,
            SolonEpgUIServiceLocator
                .getSolonEpgDossierListUIService()
                .getDossiersCandidatToArchivageDefinitive(context)
                .getListe()
                .stream()
                .map(EpgDossierListingDTOImpl::getDossierId)
                .collect(Collectors.toList())
        );
        EpgUIServiceLocator.getEpgDossierUIService().createAndAssignLotToDossiersByIds(context);

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            context
                .getMessageQueue()
                .addToastSuccess(ResourceHelper.getString("message.archivage.definitive.archive.creee"));
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }
}
