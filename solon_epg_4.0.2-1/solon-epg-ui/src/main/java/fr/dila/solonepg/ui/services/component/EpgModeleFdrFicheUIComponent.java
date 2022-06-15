package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgModeleFdrFicheUIService;
import fr.dila.solonepg.ui.services.impl.EpgModeleFdrFicheUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgModeleFdrFicheUIComponent
    extends ServiceEncapsulateComponent<EpgModeleFdrFicheUIService, EpgModeleFdrFicheUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgModeleFdrFicheUIComponent() {
        super(EpgModeleFdrFicheUIService.class, new EpgModeleFdrFicheUIServiceImpl());
    }
}
