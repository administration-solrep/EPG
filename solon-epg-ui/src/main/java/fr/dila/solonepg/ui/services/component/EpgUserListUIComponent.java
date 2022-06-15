package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgUserListUIService;
import fr.dila.solonepg.ui.services.impl.EpgUserListUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgUserListUIComponent
    extends ServiceEncapsulateComponent<EpgUserListUIService, EpgUserListUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgUserListUIComponent() {
        super(EpgUserListUIService.class, new EpgUserListUIServiceImpl());
    }
}
