package fr.dila.solonepg.ui.jaxrs.webobject.ajax.recherche;

import static fr.dila.solonepg.ui.enums.EpgUserSessionKey.EPG_FAVORIS_RECHERCHE_MODELE_FDR_FORM;

import fr.dila.solonepg.ui.bean.EpgFavorisRechercheModeleFdrForm;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.th.bean.ModeleFDRListForm;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
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
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "RechercheFavorisFdrAjax")
public class EpgRechercheFavorisFdrAjax extends SolonWebObject {

    @GET
    public ThTemplate getFavorisDossiers(@SwBeanParam ModeleFDRListForm modeleFDRform) {
        context.putInContextData(SSContextDataKey.LIST_MODELE_FDR, modeleFDRform);

        ThTemplate template = new AjaxLayoutThTemplate("fragments/table/tableModelesFDR", getMyContext());
        List<String> idFdr = EpgUIServiceLocator
            .getEpgFavorisConsultationUIService()
            .getFavorisConsultationFdrsId(context);
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idFdr);
        ModeleFDRList lstResults = EpgUIServiceLocator.getEpgModeleFdrListUIService().getModelesFDRByIds(context);
        lstResults.setHasSelect(true);
        lstResults.setHasPagination(true);
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());
        map.put(STTemplateConstants.NB_RESULTS, lstResults.getNbTotal());
        map.put(STTemplateConstants.RESULT_FORM, modeleFDRform);
        map.put(STTemplateConstants.DATA_URL, "/recherche/favoris/fdr");
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/recherche/favoris/fdr");
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        map.put(
            EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
            context.getActions(EpgActionCategory.SELECTION_TOOL_FDR_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.FAVORIS_DOSSIER_ACTIONS,
            context.getActions(EpgActionCategory.FAVORIS_FDR_ACTIONS)
        );
        map.put(STTemplateConstants.BASE_URL, "/recherche/fdr");
        template.setData(map);
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
    @Path("ajouter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response ajouter(@SwRequired @FormParam("ids[]") List<String> ids) {
        context.putInContextData(EpgContextDataKey.LIST_FAVORIS, ids);

        EpgUIServiceLocator.getEpgFavorisConsultationUIService().addFDRToFavorisConsultation(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("enregistrer/criteres")
    public Response saveCritereRecherche(@SwBeanParam EpgFavorisRechercheModeleFdrForm searchForm) {
        searchForm = ObjectHelper.requireNonNullElseGet(searchForm, EpgFavorisRechercheModeleFdrForm::new);
        UserSessionHelper.putUserSessionParameter(context, EPG_FAVORIS_RECHERCHE_MODELE_FDR_FORM, searchForm);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }
}
