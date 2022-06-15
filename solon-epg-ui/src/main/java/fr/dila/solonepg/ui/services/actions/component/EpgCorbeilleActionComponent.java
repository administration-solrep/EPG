package fr.dila.solonepg.ui.services.actions.component;

import fr.dila.solonepg.ui.services.actions.EpgCorbeilleActionService;
import fr.dila.solonepg.ui.services.actions.impl.EpgCorbeilleActionServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgCorbeilleActionComponent
    extends ServiceEncapsulateComponent<EpgCorbeilleActionService, EpgCorbeilleActionServiceImpl> {

    /**
     * Default constructor
     */
    public EpgCorbeilleActionComponent() {
        super(EpgCorbeilleActionService.class, new EpgCorbeilleActionServiceImpl());
    }
}
