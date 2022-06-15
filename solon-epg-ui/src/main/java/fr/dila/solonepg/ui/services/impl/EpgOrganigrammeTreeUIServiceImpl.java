package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.ui.bean.EpgOrganigrammeElementDTO;
import fr.dila.solonepg.ui.services.EpgOrganigrammeTreeUIService;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.ui.bean.OrganigrammeElementDTO;
import fr.dila.st.ui.services.impl.OrganigrammeTreeUIServiceImpl;
import org.nuxeo.ecm.core.api.CoreSession;

public class EpgOrganigrammeTreeUIServiceImpl
    extends OrganigrammeTreeUIServiceImpl
    implements EpgOrganigrammeTreeUIService {

    @Override
    protected OrganigrammeElementDTO createOrganigrammeElementDTO(
        CoreSession session,
        OrganigrammeNode childGroup,
        String ministereId,
        OrganigrammeElementDTO treeNode
    ) {
        return new EpgOrganigrammeElementDTO(session, childGroup, ministereId, treeNode);
    }
}
