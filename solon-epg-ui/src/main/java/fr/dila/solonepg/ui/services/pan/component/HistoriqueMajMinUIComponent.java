package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.HistoriqueMajMinUIService;
import fr.dila.solonepg.ui.services.pan.impl.HistoriqueMajMinUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class HistoriqueMajMinUIComponent
    extends ServiceEncapsulateComponent<HistoriqueMajMinUIService, HistoriqueMajMinUIServiceImpl> {

    public HistoriqueMajMinUIComponent() {
        super(HistoriqueMajMinUIService.class, new HistoriqueMajMinUIServiceImpl());
    }
}
