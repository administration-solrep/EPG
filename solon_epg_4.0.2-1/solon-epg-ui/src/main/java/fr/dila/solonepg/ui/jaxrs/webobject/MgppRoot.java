package fr.dila.solonepg.ui.jaxrs.webobject;

import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@Produces("text/html;charset=UTF-8")
@WebObject(type = "MgppUI")
public class MgppRoot extends SolonWebObject {

    public MgppRoot() {
        super();
    }

    @Path("dossierSimple")
    public Object dossierSimple() {
        return newObject("MgppDossierSimple", context);
    }

    @Path("{pageTask}")
    public Object doHome(@PathParam("pageTask") String pageTask) {
        String name = pageTask.toLowerCase();
        return newObject("Mgpp" + StringUtils.capitalize(name), context);
    }

    @Path("communication")
    public Object dossier() {
        return newObject("Communication", context);
    }

    @Path("fiche")
    public Object fiche() {
        return newObject("MgppFiche", context);
    }
}
