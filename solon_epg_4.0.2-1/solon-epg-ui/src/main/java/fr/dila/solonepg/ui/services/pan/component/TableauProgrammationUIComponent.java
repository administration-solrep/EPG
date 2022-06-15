package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.TableauProgrammationUIService;
import fr.dila.solonepg.ui.services.pan.impl.TableauProgrammationUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class TableauProgrammationUIComponent
    extends ServiceEncapsulateComponent<TableauProgrammationUIService, TableauProgrammationUIServiceImpl> {

    public TableauProgrammationUIComponent() {
        super(TableauProgrammationUIService.class, new TableauProgrammationUIServiceImpl());
    }
}
