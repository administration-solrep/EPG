package fr.dila.solonepg.ui.jaxrs.webobject.ajax.admin.dossier;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
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
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AdminDossierAjax")
public class AdminDossierAjax extends SolonWebObject {

    @Path("suppression")
    public Object doSuppressionAjax() {
        return newObject("DossiersSuppressionAjax");
    }

    @Path("parapheur")
    public Object doParapheurAjax() {
        return newObject("DossiersParapheurAjax");
    }

    @Path("fdd")
    public Object doFddAjax() {
        return newObject("DossiersFddAjax");
    }

    @Path("vecteurs")
    public Object doVecteursAjax() {
        return newObject("DossiersVecteursAjax");
    }

    @Path("table/reference")
    public Object doTableReferenceAjax() {
        return newObject("DossiersTableReferenceAjax");
    }

    @POST
    @Path("abandon")
    public ThTemplate getListDossierAAbandonner(@SwBeanParam DossierListForm dossierlistForm) {
        verifyAction(EpgActionEnum.ADMIN_MENU_DOSSIER_ABANDON, EpgURLConstants.ADMIN_DOSSIERS_ABANDON);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        EpgDossierList lstResults = SolonEpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersAbandon(context);
        ThTemplate template = new AjaxLayoutThTemplate("fragments/table/tableDossiers", context);
        dossierlistForm = ObjectHelper.requireNonNullElseGet(dossierlistForm, DossierListForm::newForm);

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
        template.getData().put(STTemplateConstants.DATA_URL, EpgURLConstants.ADMIN_DOSSIERS_ABANDON);
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.AJAX_ADMIN_DOSSIERS_ABANDON);
        return template;
    }

    @Path("abandon/retirer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response retirerDossierAbandon(@SwRequired @FormParam("idDossiers[]") List<String> idDossiers) {
        verifyAction(EpgActionEnum.RETIRER_DOSSIERS, EpgURLConstants.ADMIN_DOSSIERS_ABANDON);
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idDossiers);
        EpgUIServiceLocator.getEpgDossierUIService().retirerAbandonDossiers(context);

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            context
                .getMessageQueue()
                .addToastSuccess(ResourceHelper.getString("admin.abandon.dossier.retirer.success"));
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("abandon/exporter")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response export() {
        context.putInContextData(EpgContextDataKey.EXPORT_TYPE, SolonEpgEventConstant.DOSSIERS_ABANDON_EXPORT_EVENT);
        SolonEpgUIServiceLocator.getSolonEpgDossierListUIService().exportDossiers(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }
}
