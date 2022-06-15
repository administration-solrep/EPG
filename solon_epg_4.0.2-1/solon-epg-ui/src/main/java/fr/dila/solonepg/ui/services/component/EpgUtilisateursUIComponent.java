package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.impl.EpgUtilisateursUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;
import fr.dila.st.ui.services.STUtilisateursUIService;

public class EpgUtilisateursUIComponent
    extends ServiceEncapsulateComponent<STUtilisateursUIService, EpgUtilisateursUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgUtilisateursUIComponent() {
        super(STUtilisateursUIService.class, new EpgUtilisateursUIServiceImpl());
    }
}
