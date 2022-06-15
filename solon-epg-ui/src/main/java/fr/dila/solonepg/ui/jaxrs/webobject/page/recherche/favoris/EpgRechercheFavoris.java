package fr.dila.solonepg.ui.jaxrs.webobject.page.recherche.favoris;

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
import fr.dila.solonepg.ui.th.bean.EpgFavorisRechercheListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.model.EpgLayoutThTemplate;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliRechercheFavoris")
public class EpgRechercheFavoris extends SolonWebObject {

    @Path("dossiers")
    public Object getDossier() {
        return newObject("AppliRechercheFavorisDossier", context);
    }

    @Path("utilisateurs")
    public Object getUtilisateur() {
        return newObject("AppliRechercheFavorisUtilisateur", context);
    }

    @Path("fdr")
    public Object getFdr() {
        return newObject("AppliRechercheFavorisFdr", context);
    }

    @GET
    @Path("ajouter")
    public ThTemplate getAjouterFavori(@QueryParam("type") String type)
        throws IllegalAccessException, InstantiationException {
        context.setNavigationContextTitle(
            new Breadcrumb("Favoris de recherche", "/recherche/favoris/ajouter", Breadcrumb.TITLE_ORDER + 1)
        );

        ThTemplate template = getMyTemplate(context);
        template.setName("pages/recherche/formAjout");
        template.setContext(context);

        template.getData().put(URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        template.getData().put(EpgTemplateConstants.TYPE, type);

        return template;
    }

    @GET
    @Path("liste")
    public ThTemplate getListeFavoris(@SwBeanParam EpgFavorisRechercheListForm favorisListForm) {
        context.setNavigationContextTitle(
            new Breadcrumb("Liste des favoris de recherche", "/recherche/favoris/liste", Breadcrumb.TITLE_ORDER + 1)
        );
        template.setName("pages/recherche/favoris-list");
        template.setContext(context);
        favorisListForm = ObjectHelper.requireNonNullElseGet(favorisListForm, EpgFavorisRechercheListForm::new);
        context.putInContextData(EpgContextDataKey.FAVORIS_RECHERCHE_LIST_FORM, favorisListForm);
        EpgFavorisRechercheList favoris = getFavorisRechercheUIService().getFavorisRechercheList(context);
        favoris.buildColonnes(favorisListForm);
        template.getData().put(URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        template.getData().put(LST_COLONNES, favoris.getListeColonnes());
        template.getData().put(RESULT_FORM, favorisListForm);
        template.getData().put(RESULT_LIST, favoris);
        template.getData().put(NB_RESULTS, favoris.getNbTotal());
        template.getData().put(DATA_URL, "/recherche/favoris/liste");
        template.getData().put(DATA_AJAX_URL, "/ajax/recherche/favoris/liste");
        template.getData().put(GENERALE_ACTIONS, context.getActions(EpgActionCategory.FAVORIS_TOOL_ACTIONS));

        return template;
    }

    @Path("executer")
    public Object getResultatFavori() {
        template.setName("pages/recherche/favoris-result");
        template.setContext(context);
        return newObject("RechercheFavorisResultatsAjax", context, template);
    }

    private ThTemplate getMyTemplate(SpecificContext context) throws IllegalAccessException, InstantiationException {
        @SuppressWarnings("unchecked")
        Class<ThTemplate> oldTemplate = (Class<ThTemplate>) context
            .getWebcontext()
            .getUserSession()
            .get(SpecificContext.LAST_TEMPLATE);
        if (oldTemplate == ThTemplate.class) {
            return new EpgLayoutThTemplate();
        } else {
            return oldTemplate.newInstance();
        }
    }
}
