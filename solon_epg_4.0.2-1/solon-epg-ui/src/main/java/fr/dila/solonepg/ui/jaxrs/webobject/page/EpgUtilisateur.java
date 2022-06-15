package fr.dila.solonepg.ui.jaxrs.webobject.page;

import fr.dila.solonepg.ui.th.model.EpgUtilisateurTemplate;
import fr.dila.st.ui.jaxrs.webobject.pages.user.STUtilisateur;
import fr.dila.st.ui.th.model.ThTemplate;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliUtilisateurs")
public class EpgUtilisateur extends STUtilisateur {

    public EpgUtilisateur() {
        super();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgUtilisateurTemplate();
    }
}
