package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.ActiviteNormativeStatsUIService;
import fr.dila.solonepg.ui.services.pan.impl.ActiviteNormativeStatsUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class ActiviteNormativeStatsUIComponent
    extends ServiceEncapsulateComponent<ActiviteNormativeStatsUIService, ActiviteNormativeStatsUIService> {

    public ActiviteNormativeStatsUIComponent() {
        super(ActiviteNormativeStatsUIService.class, new ActiviteNormativeStatsUIServiceImpl());
    }
}
