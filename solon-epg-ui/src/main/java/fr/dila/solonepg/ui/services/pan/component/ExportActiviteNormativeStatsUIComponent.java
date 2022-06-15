package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.ExportActiviteNormativeStatsUIService;
import fr.dila.solonepg.ui.services.pan.impl.ExportActiviteNormativeStatsUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class ExportActiviteNormativeStatsUIComponent
    extends ServiceEncapsulateComponent<ExportActiviteNormativeStatsUIService, ExportActiviteNormativeStatsUIServiceImpl> {

    public ExportActiviteNormativeStatsUIComponent() {
        super(ExportActiviteNormativeStatsUIService.class, new ExportActiviteNormativeStatsUIServiceImpl());
    }
}
