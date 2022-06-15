package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.TexteMaitreHabilitationUIService;
import fr.dila.solonepg.ui.services.pan.impl.TexteMaitreHabilitationUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class TexteMaitreHabilitationUIComponent
    extends ServiceEncapsulateComponent<TexteMaitreHabilitationUIService, TexteMaitreHabilitationUIServiceImpl> {

    public TexteMaitreHabilitationUIComponent() {
        super(TexteMaitreHabilitationUIService.class, new TexteMaitreHabilitationUIServiceImpl());
    }
}
