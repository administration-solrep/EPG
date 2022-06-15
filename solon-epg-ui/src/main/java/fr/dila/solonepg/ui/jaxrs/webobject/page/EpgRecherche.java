package fr.dila.solonepg.ui.jaxrs.webobject.page;

import static fr.dila.st.ui.enums.STContextDataKey.BREADCRUMB_BASE_URL;

import fr.dila.solonepg.ui.th.model.EpgRechercheTemplate;
import fr.dila.ss.ui.jaxrs.webobject.page.SSRecherche;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwRegex;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliRecherche")
public class EpgRecherche extends SSRecherche {

    public EpgRecherche() {
        super();
    }

    @Path("libre")
    public Object getRechercheLibre() {
        return newObject("AppliRechercheLibre", context, template);
    }

    @Path("user")
    public Object getRechercheUser() {
        return newObject("EpgRechercheUser", context, template);
    }

    @Path("users")
    public Object getUsers() {
        return newObject("EpgRechercheUsers", context, template);
    }

    @Path("rapide")
    public Object doRechercheRapide(@QueryParam("nor") @SwRegex(NOR_KEY_REGEX) String nor) {
        ThTemplate template = buildTemplateRapideSearch(nor);

        return newObject("AppliEpgRapideResultats", context, template);
    }

    @Path("experte")
    public Object getRechercheExperte() {
        return newObject("AppliRechercheExperte", context, template);
    }

    @Path("fdr")
    public Object getModeleFeuilleRoute() {
        context.putInContextData(BREADCRUMB_BASE_URL, "/recherche/fdr");
        return newObject("ModeleFeuilleRoute", context, getMyTemplate());
    }

    @Path("favoris")
    public Object getRechercheFavoris() {
        return newObject("AppliRechercheFavoris", context, template);
    }

    @Path("derniers/resultats")
    public Object getDerniersResultatsConsultes() {
        template.setName("pages/recherche/derniers-resultats-consultes");
        template.setContext(context);
        return newObject("DerniersResultatsConsultesAjax", context, template);
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgRechercheTemplate();
    }
}
