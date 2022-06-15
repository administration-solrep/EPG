package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.PanUIService;
import fr.dila.solonepg.ui.services.pan.impl.PanUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class PanUIComponent extends ServiceEncapsulateComponent<PanUIService, PanUIServiceImpl> {

    public PanUIComponent() {
        super(PanUIService.class, new PanUIServiceImpl());
    }
}
