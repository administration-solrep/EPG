package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgRequeteUIService;
import fr.dila.solonepg.ui.services.impl.EpgRequeteUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgRequeteUIComponent extends ServiceEncapsulateComponent<EpgRequeteUIService, EpgRequeteUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgRequeteUIComponent() {
        super(EpgRequeteUIService.class, new EpgRequeteUIServiceImpl());
    }
}
