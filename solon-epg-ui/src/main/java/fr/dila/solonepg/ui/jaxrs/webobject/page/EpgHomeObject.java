package fr.dila.solonepg.ui.jaxrs.webobject.page;

import fr.dila.st.ui.jaxrs.webobject.pages.HomeObject;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@Produces("text/html;charset=UTF-8")
@WebObject(type = "AppliHome")
public class EpgHomeObject extends HomeObject {

    @GET
    @Override
    public Object doHome() {
        Response response = redirectToEditAuthIfNeeded();
        //Si la réponse n'est pas vide c'est qu'on doit rediriger vers la page de l'outil d'édition
        if (response != null) {
            return response;
        }

        //sinon ce n'est pas nécessaire et on redirige vers la page standard
        return redirect("/travail");
    }
}
