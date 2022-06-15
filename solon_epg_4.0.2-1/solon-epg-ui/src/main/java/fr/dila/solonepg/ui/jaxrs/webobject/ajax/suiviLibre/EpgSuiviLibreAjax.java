package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suiviLibre;

import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "SuiviLibreAjax")
public class EpgSuiviLibreAjax extends SolonWebObject {

    @Path("activiteParlementaire")
    public Object doActiviteParlementaire() {
        return newObject("SuiviLibreActiviteParlementaireAjax", context);
    }

    @Path("application")
    public Object doApplication() {
        return newObject("SuiviLibreApplicationAjax", context);
    }
}
