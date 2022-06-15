package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgBordereauUIService;
import fr.dila.solonepg.ui.services.impl.EpgBordereauUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgBordereauUIComponent
    extends ServiceEncapsulateComponent<EpgBordereauUIService, EpgBordereauUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgBordereauUIComponent() {
        super(EpgBordereauUIService.class, new EpgBordereauUIServiceImpl());
    }
}
