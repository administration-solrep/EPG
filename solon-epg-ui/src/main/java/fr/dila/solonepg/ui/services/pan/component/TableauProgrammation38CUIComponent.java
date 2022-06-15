package fr.dila.solonepg.ui.services.pan.component;

import fr.dila.solonepg.ui.services.pan.TableauProgrammationCUIService;
import fr.dila.solonepg.ui.services.pan.impl.TableauProgrammation38CUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class TableauProgrammation38CUIComponent
    extends ServiceEncapsulateComponent<TableauProgrammationCUIService, TableauProgrammation38CUIServiceImpl> {

    public TableauProgrammation38CUIComponent() {
        super(TableauProgrammationCUIService.class, new TableauProgrammation38CUIServiceImpl());
    }
}
