package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgOrganigrammeTreeUIService;
import fr.dila.solonepg.ui.services.impl.EpgOrganigrammeTreeUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgOrganigrammeTreeUIComponent
    extends ServiceEncapsulateComponent<EpgOrganigrammeTreeUIService, EpgOrganigrammeTreeUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgOrganigrammeTreeUIComponent() {
        super(EpgOrganigrammeTreeUIService.class, new EpgOrganigrammeTreeUIServiceImpl());
    }
}
