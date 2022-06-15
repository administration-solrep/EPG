package fr.dila.solonepg.ui.jaxrs.webobject.ajax.recherche;

import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgRechercheUIService;

import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.recherchelibre.CritereRechercheForm;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.core.util.ObjectHelper;
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
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "RechercheFavorisDossierAjax")
public class EpgRechercheFavorisDossierAjax extends SolonWebObject {

    @GET
    public ThTemplate getFavorisDossiers(@SwBeanParam DossierListForm dossierlistForm) {
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        ThTemplate template = new AjaxLayoutThTemplate("fragments/table/tableDossiers", getMyContext());
        dossierlistForm = ObjectHelper.requireNonNullElseGet(dossierlistForm, DossierListForm::newForm);
        dossierlistForm.setDossier(true);
        List<String> idDossiers = EpgUIServiceLocator
            .getEpgFavorisConsultationUIService()
            .getFavorisConsultationDossiersId(context);
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idDossiers);
        EpgDossierList lstResults = EpgUIServiceLocator.getSolonEpgDossierListUIService().getDossiersFavoris(context);

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
        template.getData().put(STTemplateConstants.DATA_URL, "/recherche/favoris/dossiers");
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, "/ajax/recherche/favoris/dossiers");
        template
            .getData()
            .put(
                EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
                context.getActions(EpgActionCategory.SELECTION_TOOL_DOSSIERS_ACTIONS)
            );
        template
            .getData()
            .put(
                EpgTemplateConstants.FAVORIS_DOSSIER_ACTIONS,
                context.getActions(EpgActionCategory.FAVORIS_DOSSIERS_ACTIONS)
            );
        return template;
    }

    @POST
    @Path("supprimer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response supprimer(@SwRequired @FormParam("ids[]") List<String> ids) {
        context.putInContextData(EpgContextDataKey.LIST_FAVORIS, ids);
        EpgUIServiceLocator.getEpgFavorisConsultationUIService().removeFavorisConsultation(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("ajouter")
    public Response ajouter(@FormParam("dossiersIds[]") List<String> dossiersIds) {
        context.putInContextData(EpgContextDataKey.LIST_FAVORIS, dossiersIds);

        EpgUIServiceLocator.getEpgFavorisConsultationUIService().addDossiersToFavorisConsultation(context);
        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("enregistrer/criteres/libre")
    public Response saveCritereRecLibre(@SwBeanParam CritereRechercheForm form) {
        context.putInContextData(EpgContextDataKey.CRITERE_RECHERCHE_FORM, form);
        CritereRechercheForm updatedForm = getEpgRechercheUIService().getUpdatedCritereRechercheForm(context);
        UserSessionHelper.putUserSessionParameter(context, EpgUserSessionKey.CRITERE_RECHERCHE_KEY, updatedForm);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }
}
