package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.TranspositionDirectiveUIService;
import fr.dila.solonepg.ui.services.pan.impl.TranspositionDirectiveUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class TranspositionDirectiveUIComponent
    extends ServiceEncapsulateComponent<TranspositionDirectiveUIService, TranspositionDirectiveUIServiceImpl> {

    public TranspositionDirectiveUIComponent() {
        super(TranspositionDirectiveUIService.class, new TranspositionDirectiveUIServiceImpl());
    }
}
