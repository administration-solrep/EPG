package fr.dila.solonepg.ui.jaxrs.webobject.page;

import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "SelectionTool")
public class SelectionTool extends SolonWebObject {

    @Path("substitution")
    public Object doSubstitution() {
        return newObject("SubstitutionMass");
    }
}
