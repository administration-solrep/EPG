package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.organigramme;

import fr.dila.solonepg.ui.th.model.EpgLayoutThTemplate;
import fr.dila.st.ui.jaxrs.webobject.pages.admin.organigramme.STOrganigrammeMinistere;
import fr.dila.st.ui.th.model.ThTemplate;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "OrganigrammeMinistere")
public class EPGOrganigrammeMinistere extends STOrganigrammeMinistere implements EPGSharedBetweenAdminAndUser {

    public EPGOrganigrammeMinistere() {
        super();
    }

    @Override
    public ThTemplate getMinistereCreation(String idParent) {
        ThTemplate template = super.getMinistereCreation(idParent);
        template.getData().put(EpgLayoutThTemplate.HAS_NOR, true);
        template.getData().put(EpgLayoutThTemplate.SUIVI_AN, true);
        return template;
    }

    @Override
    public ThTemplate getMinistereModification(String idMinistere) {
        ThTemplate template = super.getMinistereModification(idMinistere);
        template.getData().put(EpgLayoutThTemplate.HAS_NOR, true);
        template.getData().put(EpgLayoutThTemplate.SUIVI_AN, true);
        return template;
    }
}
