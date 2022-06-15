package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppHistoriqueTreeUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppHistoriqueTreeUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppHistoriqueTreeUIComponent
    extends ServiceEncapsulateComponent<MgppHistoriqueTreeUIService, MgppHistoriqueTreeUIServiceImpl> {

    public MgppHistoriqueTreeUIComponent() {
        super(MgppHistoriqueTreeUIService.class, new MgppHistoriqueTreeUIServiceImpl());
    }
}
