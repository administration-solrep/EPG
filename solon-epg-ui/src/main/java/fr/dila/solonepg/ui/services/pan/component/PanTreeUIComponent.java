package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.PanTreeUIService;
import fr.dila.solonepg.ui.services.pan.impl.PanTreeUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class PanTreeUIComponent extends ServiceEncapsulateComponent<PanTreeUIService, PanTreeUIServiceImpl> {

    public PanTreeUIComponent() {
        super(PanTreeUIService.class, new PanTreeUIServiceImpl());
    }
}
