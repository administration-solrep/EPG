package fr.dila.solonepg.ui.jaxrs.webobject.page;

import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.ss.ui.jaxrs.webobject.page.SSAdmin;
import fr.dila.st.ui.th.model.ThTemplate;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliAdmin")
public class EpgAdmin extends SSAdmin {

    public EpgAdmin() {
        super();
    }

    @Path("archivage")
    public Object getArchivage() {
        return newObject("AppliArchivage", context);
    }

    @Path("dossiers")
    public Object getDossier() {
        return newObject("AdminDossier", context);
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }

    @Path("mgpp")
    public Object getAdmnMGPP() {
        return newObject("AdminMGPP", context, getMyTemplate());
    }

    @Path("mgpp/courriers")
    public Object getModeleCourrier() {
        return newObject("MgppModeleCourrier", context);
    }
}
