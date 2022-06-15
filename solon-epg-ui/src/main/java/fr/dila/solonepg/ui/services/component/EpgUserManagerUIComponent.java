package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.impl.EpgUserManagerUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;
import fr.dila.st.ui.services.STUserManagerUIService;

public class EpgUserManagerUIComponent
    extends ServiceEncapsulateComponent<STUserManagerUIService, EpgUserManagerUIServiceImpl> {

    public EpgUserManagerUIComponent() {
        super(STUserManagerUIService.class, new EpgUserManagerUIServiceImpl());
    }
}
