package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.impl.MgppNotificationUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;
import fr.dila.st.ui.services.NotificationUIService;

public class MgppNotificationUIComponent
    extends ServiceEncapsulateComponent<NotificationUIService, MgppNotificationUIServiceImpl> {

    public MgppNotificationUIComponent() {
        super(NotificationUIService.class, new MgppNotificationUIServiceImpl());
    }
}
