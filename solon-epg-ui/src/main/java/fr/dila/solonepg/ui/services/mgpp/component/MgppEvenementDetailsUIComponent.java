package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppEvenementDetailsUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppEvenementDetailsUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppEvenementDetailsUIComponent
    extends ServiceEncapsulateComponent<MgppEvenementDetailsUIService, MgppEvenementDetailsUIServiceImpl> {

    public MgppEvenementDetailsUIComponent() {
        super(MgppEvenementDetailsUIService.class, new MgppEvenementDetailsUIServiceImpl());
    }
}
