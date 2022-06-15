package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgRechercheUIService;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getTableauDynamiqueUIService;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.ADD_DOSSIERS_TO_FAVORIS_ACTIONS;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.ID_TAB;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.TABLEAU_DYNAMIQUE;
import static org.apache.commons.lang3.StringUtils.isBlank;

import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.TableauxDynamiquesDTO;
import fr.dila.solonepg.ui.bean.recherchelibre.CritereRechercheForm;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.services.TableauDynamiqueUIService;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.bean.TableauDynamiqueForm;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "TableauxDynamiquesAjax")
public class EpgSuiviTableauxDynamiquesAjax extends SolonWebObject {

    @GET
    @Path("{idTab}")
    public ThTemplate getHomeResults(@PathParam(ID_TAB) String id, @SwBeanParam DossierListForm dossierListForm) {
        return getResultats(id, dossierListForm);
    }

    @GET
    @Path("resultats")
    public ThTemplate getResultats(
        @SwRequired @SwId @QueryParam(ID_TAB) String id,
        @SwBeanParam DossierListForm dossierListForm
    ) {
        context.setCurrentDocument(id);
        TableauDynamiqueUIService tableauDynamiqueUIService = getTableauDynamiqueUIService();
        TableauxDynamiquesDTO td = tableauDynamiqueUIService.getTableauxDynamiqueDTO(context);
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("tableaux.dynamiques.resultats.title", td.getLabel()),
                "/suivi/tableaux/executer/" + td.getId(),
                Breadcrumb.TITLE_ORDER + 1
            )
        );
        if (isBlank(template.getName())) {
            template = new AjaxLayoutThTemplate("fragments/table/tableDossiers", context);
        }
        dossierListForm = ObjectHelper.requireNonNullElseGet(dossierListForm, DossierListForm::newForm);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierListForm);
        context.putInContextData(STContextDataKey.ID, id);
        Map<String, Object> otherParameter = new HashMap<>();
        otherParameter.put(ID_TAB, id);

        EpgDossierList lstDossier = tableauDynamiqueUIService.getDossierListForTableauxDynamique(context);
        dossierListForm.setColumnVisibility(lstDossier.getListeColonnes());
        template.getData().put(STTemplateConstants.LST_COLONNES, lstDossier.getListeColonnes());
        template.getData().put(STTemplateConstants.LST_SORTED_COLONNES, lstDossier.getListeSortedColonnes());
        template
            .getData()
            .put(STTemplateConstants.LST_SORTABLE_COLONNES, lstDossier.getListeSortableAndVisibleColonnes());
        template.getData().put(STTemplateConstants.RESULT_FORM, dossierListForm);
        template.getData().put(STTemplateConstants.RESULT_LIST, lstDossier);
        template.getData().put(STTemplateConstants.NB_RESULTS, lstDossier.getNbTotal());
        template.getData().put(STTemplateConstants.DATA_URL, String.format(EpgURLConstants.URL_PAGE_TD, id));
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_TD);
        template.getData().put(STTemplateConstants.OTHER_PARAMETER, otherParameter);
        template.getData().put(TABLEAU_DYNAMIQUE, td);
        template
            .getData()
            .put(
                ADD_DOSSIERS_TO_FAVORIS_ACTIONS,
                context.getActions(EpgActionCategory.ADD_DOSSIERS_TO_FAVORIS_ACTIONS)
            );
        template
            .getData()
            .put(SELECTION_TOOL_DOSSIER_ACTIONS, context.getActions(EpgActionCategory.SELECTION_TOOL_DOSSIERS_ACTIONS));
        template
            .getData()
            .put(STTemplateConstants.EXPORT_ACTION, context.getAction(EpgActionEnum.TAB_DYN_DOSSIER_EXPORT));

        return template;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("sauvegarder")
    public Response saveTableauDynamique(@FormParam("type") String type, @SwBeanParam TableauDynamiqueForm form) {
        context.putInContextData(EpgContextDataKey.TABLEAU_DYNAMIQUE_FORM, form);
        context.putInContextData(EpgContextDataKey.TD_TYPE_RECHERCHE, type);
        EpgUIServiceLocator.getTableauDynamiqueUIService().createOrSaveTableauDynamique(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("supprimer")
    public Response supprimerTableauDynamique(@SwRequired @SwId @FormParam("id") String id) {
        context.setCurrentDocument(id);
        EpgUIServiceLocator.getTableauDynamiqueUIService().deleteTableauDynamique(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("enregistrer/criteres/libre")
    public Response saveCritereRecLibre(@SwBeanParam CritereRechercheForm form) {
        context.putInContextData(EpgContextDataKey.CRITERE_RECHERCHE_FORM, form);
        CritereRechercheForm updatedForm = getEpgRechercheUIService().getUpdatedCritereRechercheForm(context);
        UserSessionHelper.putUserSessionParameter(context, EpgUserSessionKey.TD_CRITERES_RECH_LIBRE, updatedForm);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("exporter/{idTab}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response exporter(@PathParam(ID_TAB) String id) {
        context.putInContextData(STContextDataKey.ID, id);

        SolonEpgUIServiceLocator.getSolonEpgDossierListUIService().exportDossiersTabDyn(context);
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }
}
