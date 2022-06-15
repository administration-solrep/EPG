package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgRequeteUIService;
import fr.dila.solonepg.ui.services.impl.EpgRequeteUIServiceImpl;
import fr.dila.solonepg.ui.services.pan.PanRequeteUIService;
import fr.dila.solonepg.ui.services.pan.impl.PanRequeteUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class PanRequeteUIComponent extends ServiceEncapsulateComponent<PanRequeteUIService, PanRequeteUIServiceImpl> {

    /**
     * Default constructor
     */
    public PanRequeteUIComponent() {
        super(PanRequeteUIService.class, new PanRequeteUIServiceImpl());
    }
}
