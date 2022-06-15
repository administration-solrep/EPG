package fr.dila.solonepg.ui.jaxrs.webobject.page.suiviLibre;

import fr.dila.solonepg.ui.th.model.EpgHorsConnexionTemplate;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.GET;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DepotAmendements")
public class EpgSuiviLibreDepotAmendements extends SolonWebObject {

    public EpgSuiviLibreDepotAmendements() {
        super();
    }

    @GET
    public ThTemplate getHome() {
        template.setContext(context);

        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgHorsConnexionTemplate("pages/suiviLibre/depotAmendements", context);
    }
}
