package fr.dila.solonepg.ui.services.actions.component;

import fr.dila.solonepg.ui.services.actions.EpgGestionIndexationActionService;
import fr.dila.solonepg.ui.services.actions.impl.EpgGestionIndexationActionServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgGestionIndexationActionComponent
    extends ServiceEncapsulateComponent<EpgGestionIndexationActionService, EpgGestionIndexationActionServiceImpl> {

    /**
     * Default constructor
     */
    public EpgGestionIndexationActionComponent() {
        super(EpgGestionIndexationActionService.class, new EpgGestionIndexationActionServiceImpl());
    }
}
