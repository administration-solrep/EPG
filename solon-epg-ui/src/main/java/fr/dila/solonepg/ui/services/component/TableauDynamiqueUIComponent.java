package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.TableauDynamiqueUIService;
import fr.dila.solonepg.ui.services.impl.TableauDynamiqueUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class TableauDynamiqueUIComponent
    extends ServiceEncapsulateComponent<TableauDynamiqueUIService, TableauDynamiqueUIServiceImpl> {

    public TableauDynamiqueUIComponent() {
        super(TableauDynamiqueUIService.class, new TableauDynamiqueUIServiceImpl());
    }
}
