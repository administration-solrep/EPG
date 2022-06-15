package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgDossierUIService;
import fr.dila.solonepg.ui.services.impl.EpgDossierUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgDossierUIComponent extends ServiceEncapsulateComponent<EpgDossierUIService, EpgDossierUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgDossierUIComponent() {
        super(EpgDossierUIService.class, new EpgDossierUIServiceImpl());
    }
}
