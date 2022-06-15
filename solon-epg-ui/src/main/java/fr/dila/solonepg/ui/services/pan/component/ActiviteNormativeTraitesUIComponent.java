package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.ActiviteNormativeTraitesUIService;
import fr.dila.solonepg.ui.services.pan.impl.ActiviteNormativeTraitesUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class ActiviteNormativeTraitesUIComponent
    extends ServiceEncapsulateComponent<ActiviteNormativeTraitesUIService, ActiviteNormativeTraitesUIServiceImpl> {

    public ActiviteNormativeTraitesUIComponent() {
        super(ActiviteNormativeTraitesUIService.class, new ActiviteNormativeTraitesUIServiceImpl());
    }
}
