package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.ActiviteNormativeParamStatsUIService;
import fr.dila.solonepg.ui.services.pan.impl.ActiviteNormativeParamStatsUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class ActiviteNormativeParamStatsUIComponent
    extends ServiceEncapsulateComponent<ActiviteNormativeParamStatsUIService, ActiviteNormativeParamStatsUIServiceImpl> {

    public ActiviteNormativeParamStatsUIComponent() {
        super(ActiviteNormativeParamStatsUIService.class, new ActiviteNormativeParamStatsUIServiceImpl());
    }
}
