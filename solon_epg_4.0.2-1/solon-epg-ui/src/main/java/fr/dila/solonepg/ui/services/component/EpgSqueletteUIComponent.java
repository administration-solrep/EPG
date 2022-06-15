package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgSqueletteUIService;
import fr.dila.solonepg.ui.services.impl.EpgSqueletteUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgSqueletteUIComponent
    extends ServiceEncapsulateComponent<EpgSqueletteUIService, EpgSqueletteUIServiceImpl> {

    public EpgSqueletteUIComponent() {
        super(EpgSqueletteUIService.class, new EpgSqueletteUIServiceImpl());
    }
}
