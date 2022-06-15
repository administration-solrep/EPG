package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.st.ui.jaxrs.webobject.ajax.AbstractNotificationAjax;
import fr.dila.st.ui.services.NotificationUIService;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "NotificationAjax")
public class MgppNotificationAjax extends AbstractNotificationAjax {

    public MgppNotificationAjax() {
        super();
    }

    @Override
    protected NotificationUIService getNotificationUIService() {
        return MgppUIServiceLocator.getNotificationUIService();
    }
}
