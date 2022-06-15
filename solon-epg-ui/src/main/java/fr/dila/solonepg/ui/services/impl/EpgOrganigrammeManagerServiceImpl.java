package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.ui.services.EpgOrganigrammeManagerService;
import fr.dila.ss.ui.services.impl.SSOrganigrammeManagerServiceImpl;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.ui.bean.OrganigrammeElementDTO;

public class EpgOrganigrammeManagerServiceImpl
    extends SSOrganigrammeManagerServiceImpl
    implements EpgOrganigrammeManagerService {

    @Override
    protected boolean isPosteWebservice(OrganigrammeElementDTO node) {
        return false;
    }

    @Override
    protected boolean isPoste(OrganigrammeElementDTO node) {
        return (node != null && OrganigrammeType.isPoste(node.getType()));
    }
}
