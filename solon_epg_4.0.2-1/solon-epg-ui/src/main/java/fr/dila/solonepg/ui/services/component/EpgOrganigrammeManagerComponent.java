package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgOrganigrammeManagerService;
import fr.dila.solonepg.ui.services.impl.EpgOrganigrammeManagerServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgOrganigrammeManagerComponent
    extends ServiceEncapsulateComponent<EpgOrganigrammeManagerService, EpgOrganigrammeManagerServiceImpl> {

    public EpgOrganigrammeManagerComponent() {
        super(EpgOrganigrammeManagerService.class, new EpgOrganigrammeManagerServiceImpl());
    }
}
