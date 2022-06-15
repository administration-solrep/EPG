package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.organigramme;

import fr.dila.solonepg.ui.th.model.EpgLayoutThTemplate;
import fr.dila.st.ui.jaxrs.webobject.pages.admin.organigramme.STOrganigrammeUniteStructurelle;
import fr.dila.st.ui.th.model.ThTemplate;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "OrganigrammeUniteStructurelle")
public class EPGOrganigrammeUniteStructurelle
    extends STOrganigrammeUniteStructurelle
    implements EPGSharedBetweenAdminAndUser {

    public EPGOrganigrammeUniteStructurelle() {
        super();
    }

    @Override
    public ThTemplate getUniteStructurelleCreation(String idParent, String ministereId) {
        ThTemplate template = super.getUniteStructurelleCreation(idParent, ministereId);
        template.getData().put(EpgLayoutThTemplate.HAS_NOR, true);
        return template;
    }

    @Override
    public ThTemplate getUniteStructurelleModification(String idUniteStructurelle, String ministereId) {
        ThTemplate template = super.getUniteStructurelleModification(idUniteStructurelle, ministereId);
        template.getData().put(EpgLayoutThTemplate.HAS_NOR, true);
        return template;
    }
}
