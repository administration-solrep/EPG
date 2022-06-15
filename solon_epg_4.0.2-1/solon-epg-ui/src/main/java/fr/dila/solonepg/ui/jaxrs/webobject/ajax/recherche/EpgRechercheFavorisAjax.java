package fr.dila.solonepg.ui.jaxrs.webobject.ajax.recherche;

import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getFavorisRechercheUIService;
import static fr.dila.st.ui.th.constants.STTemplateConstants.DATA_AJAX_URL;
import static fr.dila.st.ui.th.constants.STTemplateConstants.DATA_URL;
import static fr.dila.st.ui.th.constants.STTemplateConstants.GENERALE_ACTIONS;
import static fr.dila.st.ui.th.constants.STTemplateConstants.LST_COLONNES;
import static fr.dila.st.ui.th.constants.STTemplateConstants.NB_RESULTS;
import static fr.dila.st.ui.th.constants.STTemplateConstants.RESULT_FORM;
import static fr.dila.st.ui.th.constants.STTemplateConstants.RESULT_LIST;
import static fr.dila.st.ui.th.constants.STTemplateConstants.URL_PREVIOUS_PAGE;

import fr.dila.solonepg.ui.bean.favoris.recherche.EpgFavorisRechercheList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.FavorisRechercheUIType;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.EpgFavorisRechercheListForm;
import fr.dila.solonepg.ui.th.bean.FavorisRechercheForm;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
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
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "RechercheFavorisAjax")
public class EpgRechercheFavorisAjax extends SolonWebObject {

    @Path("dossiers")
    public Object getDossier() {
        return newObject("RechercheFavorisDossierAjax", context);
    }

    @Path("utilisateurs")
    public Object getUtilisateur() {
        return newObject("RechercheFavorisUtilisateurAjax", context);
    }

    @Path("fdr")
    public Object getFdr() {
        return newObject("RechercheFavorisFdrAjax", context);
    }

    @POST
    @Path("sauvegarder")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doSave(
        @SwBeanParam FavorisRechercheForm favorisRechercheForm,
        @FormParam("typeFavori") String typeFavori
    ) {
        context.putInContextData(EpgContextDataKey.FAVORIS_RECHERCHE_FORM, favorisRechercheForm);
        context.putInContextData(
            EpgContextDataKey.TYPE_RECHERCHE,
            FavorisRechercheUIType.getTypeFromUIValue(typeFavori)
        );
        EpgUIServiceLocator.getFavorisRechercheUIService().saveFavori(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @GET
    @Path("liste")
    public ThTemplate getListeFavoris(@SwBeanParam EpgFavorisRechercheListForm favorisListForm) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/table/tableFavorisRecherche", context);
        favorisListForm = ObjectHelper.requireNonNullElseGet(favorisListForm, EpgFavorisRechercheListForm::new);
        context.putInContextData(EpgContextDataKey.FAVORIS_RECHERCHE_LIST_FORM, favorisListForm);
        EpgFavorisRechercheList favoris = getFavorisRechercheUIService().getFavorisRechercheList(context);
        favoris.buildColonnes(favorisListForm);
        Map<String, Object> map = new HashMap<>();
        map.put(URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        map.put(LST_COLONNES, favoris.getListeColonnes());
        map.put(RESULT_FORM, favorisListForm);
        map.put(RESULT_LIST, favoris);
        map.put(NB_RESULTS, favoris.getNbTotal());
        map.put(DATA_URL, "/recherche/favoris/liste");
        map.put(DATA_AJAX_URL, "/ajax/recherche/favoris/liste");
        map.put(GENERALE_ACTIONS, context.getActions(EpgActionCategory.FAVORIS_TOOL_ACTIONS));
        template.setData(map);
        return template;
    }

    @POST
    @Path("supprimer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response supprimer(@FormParam("idFavoris[]") List<String> idFavoris) {
        if (CollectionUtils.isNotEmpty(idFavoris)) {
            context.getSession().removeDocuments(idFavoris.stream().map(IdRef::new).toArray(DocumentRef[]::new));
        }
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }
}
