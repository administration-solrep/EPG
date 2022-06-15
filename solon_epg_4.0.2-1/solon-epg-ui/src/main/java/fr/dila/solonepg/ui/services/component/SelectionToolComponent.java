package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.SelectionToolComponentService;
import fr.dila.solonepg.ui.services.impl.SelectionToolComponentServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class SelectionToolComponent
    extends ServiceEncapsulateComponent<SelectionToolComponentService, SelectionToolComponentServiceImpl> {

    /**
     * Default constructor
     */
    public SelectionToolComponent() {
        super(SelectionToolComponentService.class, new SelectionToolComponentServiceImpl());
    }
}
