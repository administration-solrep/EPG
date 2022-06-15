package fr.dila.solonepg.ui.jaxrs.webobject.page.recherche;

import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.ss.ui.jaxrs.webobject.page.SSTransverseUser;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "EpgRechercheUser")
public class EpgRechercheUser extends SSTransverseUser {

    public EpgRechercheUser() {}

    @Override
    @GET
    @Path("rechercher")
    public ThTemplate getRechercheUtilisateurs() {
        return generateRechercheUtilisateursTemplate(context, template, EpgURLConstants.URL_SEARCH_USER);
    }
}
