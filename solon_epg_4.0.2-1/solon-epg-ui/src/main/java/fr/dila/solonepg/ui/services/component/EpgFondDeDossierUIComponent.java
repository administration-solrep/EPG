package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgFondDeDossierUIService;
import fr.dila.solonepg.ui.services.impl.EpgFondDeDossierUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgFondDeDossierUIComponent
    extends ServiceEncapsulateComponent<EpgFondDeDossierUIService, EpgFondDeDossierUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgFondDeDossierUIComponent() {
        super(EpgFondDeDossierUIService.class, new EpgFondDeDossierUIServiceImpl());
    }
}
