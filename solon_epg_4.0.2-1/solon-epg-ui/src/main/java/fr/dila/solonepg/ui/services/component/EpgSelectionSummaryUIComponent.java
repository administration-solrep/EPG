package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgSelectionSummaryUIService;
import fr.dila.solonepg.ui.services.impl.EpgSelectionSummaryUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgSelectionSummaryUIComponent
    extends ServiceEncapsulateComponent<EpgSelectionSummaryUIService, EpgSelectionSummaryUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgSelectionSummaryUIComponent() {
        super(EpgSelectionSummaryUIService.class, new EpgSelectionSummaryUIServiceImpl());
    }
}
