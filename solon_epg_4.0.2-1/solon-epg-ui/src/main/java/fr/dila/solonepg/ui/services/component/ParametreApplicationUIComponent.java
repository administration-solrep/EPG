package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.ParametreApplicationUIService;
import fr.dila.solonepg.ui.services.impl.ParametreApplicationUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class ParametreApplicationUIComponent
    extends ServiceEncapsulateComponent<ParametreApplicationUIService, ParametreApplicationUIServiceImpl> {

    /**
     * Default constructor
     */
    public ParametreApplicationUIComponent() {
        super(ParametreApplicationUIService.class, new ParametreApplicationUIServiceImpl());
    }
}
