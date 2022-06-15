package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.TexteMaitreTraitesUIService;
import fr.dila.solonepg.ui.services.pan.impl.TexteMaitreTraitesUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class TexteMaitreTraitesUIComponent
    extends ServiceEncapsulateComponent<TexteMaitreTraitesUIService, TexteMaitreTraitesUIServiceImpl> {

    public TexteMaitreTraitesUIComponent() {
        super(TexteMaitreTraitesUIService.class, new TexteMaitreTraitesUIServiceImpl());
    }
}
