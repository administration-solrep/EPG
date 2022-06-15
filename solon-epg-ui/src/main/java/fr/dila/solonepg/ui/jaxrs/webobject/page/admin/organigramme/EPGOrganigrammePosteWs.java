package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.organigramme;

import fr.dila.st.ui.jaxrs.webobject.pages.admin.organigramme.STOrganigrammePosteWs;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "OrganigrammePosteWs")
public class EPGOrganigrammePosteWs extends STOrganigrammePosteWs implements EPGSharedBetweenAdminAndUser {

    public EPGOrganigrammePosteWs() {
        super();
    }
}
