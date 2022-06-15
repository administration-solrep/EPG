package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgParametrageAdamantUIService;
import fr.dila.solonepg.ui.services.impl.EpgParametrageAdamantUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgParametrageAdamantUIComponent
    extends ServiceEncapsulateComponent<EpgParametrageAdamantUIService, EpgParametrageAdamantUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgParametrageAdamantUIComponent() {
        super(EpgParametrageAdamantUIService.class, new EpgParametrageAdamantUIServiceImpl());
    }
}
