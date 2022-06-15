package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppAdminUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppAdminUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppAdminUIComponent extends ServiceEncapsulateComponent<MgppAdminUIService, MgppAdminUIServiceImpl> {

    public MgppAdminUIComponent() {
        super(MgppAdminUIService.class, new MgppAdminUIServiceImpl());
    }
}
