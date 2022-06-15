package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.TexteMaitreOrdonnanceUIService;
import fr.dila.solonepg.ui.services.pan.impl.TexteMaitreOrdonnanceUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class TexteMaitreOrdonnanceUIComponent
    extends ServiceEncapsulateComponent<TexteMaitreOrdonnanceUIService, TexteMaitreOrdonnanceUIServiceImpl> {

    public TexteMaitreOrdonnanceUIComponent() {
        super(TexteMaitreOrdonnanceUIService.class, new TexteMaitreOrdonnanceUIServiceImpl());
    }
}
