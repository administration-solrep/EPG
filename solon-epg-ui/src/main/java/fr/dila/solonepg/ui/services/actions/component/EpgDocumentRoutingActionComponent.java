package fr.dila.solonepg.ui.services.actions.component;

import fr.dila.solonepg.ui.services.actions.EpgDocumentRoutingActionService;
import fr.dila.solonepg.ui.services.actions.impl.EpgDocumentRoutingActionServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgDocumentRoutingActionComponent
    extends ServiceEncapsulateComponent<EpgDocumentRoutingActionService, EpgDocumentRoutingActionServiceImpl> {

    /**
     * Default constructor
     */
    public EpgDocumentRoutingActionComponent() {
        super(EpgDocumentRoutingActionService.class, new EpgDocumentRoutingActionServiceImpl());
    }
}
