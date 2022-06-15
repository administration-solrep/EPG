package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppCalendrierMenuService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppCalendrierMenuServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppCalendrierMenuComponent
    extends ServiceEncapsulateComponent<MgppCalendrierMenuService, MgppCalendrierMenuServiceImpl> {

    public MgppCalendrierMenuComponent() {
        super(MgppCalendrierMenuService.class, new MgppCalendrierMenuServiceImpl());
    }
}
