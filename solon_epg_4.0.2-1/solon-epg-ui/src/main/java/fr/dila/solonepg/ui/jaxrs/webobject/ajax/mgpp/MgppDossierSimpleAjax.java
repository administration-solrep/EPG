package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossierSimpleAjax")
public class MgppDossierSimpleAjax extends SolonWebObject {

    @Path("{id}/{tab}")
    public Object doTab(
        @PathParam("id") String id,
        @PathParam("tab") String tab,
        @QueryParam("version") String version
    ) {
        tab = StringUtils.capitalize(tab);

        return newObject("DossierSimple" + tab + "Ajax", context);
    }
}
