package fr.dila.solonepg.ui.services.actions.component;

import fr.dila.solonepg.ui.services.actions.EpgRouteStepNoteActionService;
import fr.dila.solonepg.ui.services.actions.impl.EpgRouteStepNoteActionServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgRouteStepNoteActionComponent
    extends ServiceEncapsulateComponent<EpgRouteStepNoteActionService, EpgRouteStepNoteActionServiceImpl> {

    /**
     * Default constructor
     */
    public EpgRouteStepNoteActionComponent() {
        super(EpgRouteStepNoteActionService.class, new EpgRouteStepNoteActionServiceImpl());
    }
}
