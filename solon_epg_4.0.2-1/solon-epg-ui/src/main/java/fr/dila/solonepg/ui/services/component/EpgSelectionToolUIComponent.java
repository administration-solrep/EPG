package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgSelectionToolUIService;
import fr.dila.solonepg.ui.services.impl.EpgSelectionToolUIServicerImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgSelectionToolUIComponent
    extends ServiceEncapsulateComponent<EpgSelectionToolUIService, EpgSelectionToolUIServicerImpl> {

    /**
     * Default constructor
     */
    public EpgSelectionToolUIComponent() {
        super(EpgSelectionToolUIService.class, new EpgSelectionToolUIServicerImpl());
    }
}
