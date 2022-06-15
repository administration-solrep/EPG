package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.TexteMaitreUIService;
import fr.dila.solonepg.ui.services.pan.impl.TexteMaitreUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class TexteMaitreUIComponent
    extends ServiceEncapsulateComponent<TexteMaitreUIService, TexteMaitreUIServiceImpl> {

    public TexteMaitreUIComponent() {
        super(TexteMaitreUIService.class, new TexteMaitreUIServiceImpl());
    }
}
