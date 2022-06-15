package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgRechercheUIService;
import fr.dila.solonepg.ui.services.impl.EpgRechercheUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgRechercheUIComponent
    extends ServiceEncapsulateComponent<EpgRechercheUIService, EpgRechercheUIServiceImpl> {

    public EpgRechercheUIComponent() {
        super(EpgRechercheUIService.class, new EpgRechercheUIServiceImpl());
    }
}
