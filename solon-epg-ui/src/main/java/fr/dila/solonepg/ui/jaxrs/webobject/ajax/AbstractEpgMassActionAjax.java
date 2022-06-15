package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;

public abstract class AbstractEpgMassActionAjax extends SolonWebObject {

    public AbstractEpgMassActionAjax() {
        super();
    }

    public Response doDeleteDossiers(String username, String data) {
        context.putInContextData(EpgContextDataKey.USERNAME, username);
        context.putInContextData(EpgContextDataKey.DATA, data);

        SolonStatus status = EpgUIServiceLocator
            .getEpgDossierUIService()
            .checkUserAndDeleteDossierForMassAction(context);

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            addMessageQueueInSession();
        }

        return new JsonResponse(status, context.getMessageQueue()).build();
    }
}
