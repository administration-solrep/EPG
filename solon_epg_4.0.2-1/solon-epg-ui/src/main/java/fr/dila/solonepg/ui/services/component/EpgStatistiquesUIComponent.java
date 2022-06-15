package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgStatistiquesUIService;
import fr.dila.solonepg.ui.services.impl.EpgStatistiquesUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgStatistiquesUIComponent
    extends ServiceEncapsulateComponent<EpgStatistiquesUIService, EpgStatistiquesUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgStatistiquesUIComponent() {
        super(EpgStatistiquesUIService.class, new EpgStatistiquesUIServiceImpl());
    }
}
