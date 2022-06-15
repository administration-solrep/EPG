package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.PanStatistiquesUIService;
import fr.dila.solonepg.ui.services.pan.impl.PanStatistiquesUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class PanStatistiquesUIComponent
    extends ServiceEncapsulateComponent<PanStatistiquesUIService, PanStatistiquesUIServiceImpl> {

    /**
     * Default constructor
     */
    public PanStatistiquesUIComponent() {
        super(PanStatistiquesUIService.class, new PanStatistiquesUIServiceImpl());
    }
}
