package fr.dila.solonepg.ui.jaxrs.webobject.page.recherche.favoris;

import static fr.dila.st.ui.enums.STContextDataKey.BREADCRUMB_BASE_LEVEL;
import static fr.dila.st.ui.enums.STContextDataKey.BREADCRUMB_BASE_URL;

import fr.dila.solonepg.ui.th.bean.EpgUserListForm;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.EpgRechercheTemplate;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliRechercheFavorisUtilisateur")
public class EpgRechercheFavorisUtilisateur extends AbstractEpgRechercheFavorisUtilisateur {

    @GET
    public ThTemplate getFavorisUtilisateur(@SwBeanParam EpgUserListForm userListForm) {
        context.setNavigationContextTitle(
            new Breadcrumb(
                "Favoris de consultation - Utilisateurs",
                EpgURLConstants.RECHERCHE_FAVORIS_UTILISATEURS,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        template.setContext(context);
        template.setName("pages/recherche/favoris-utilisateur.html");
        template.setData(getTemplateData(userListForm));

        return template;
    }

    @Path("utilisateur")
    public Object getUtilisateur() {
        context.putInContextData(BREADCRUMB_BASE_URL, EpgURLConstants.RECHERCHE_FAVORIS_UTILISATEUR);
        context.putInContextData(BREADCRUMB_BASE_LEVEL, Breadcrumb.TITLE_ORDER);
        return newObject("TransverseUser", context, getMyTemplate());
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgRechercheTemplate();
    }
}
