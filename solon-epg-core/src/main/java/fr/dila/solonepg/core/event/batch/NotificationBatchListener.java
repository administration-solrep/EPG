package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.service.WsSpeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.event.batch.AbstractNotificationBatchListener;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Batch de ré-essai d'envoi des notifications ( demande de publication ) aux webservices
 *
 * @author bgamard
 */
public class NotificationBatchListener extends AbstractNotificationBatchListener {

    public NotificationBatchListener() {
        super();
    }

    @Override
    protected void doNotifications(final CoreSession session) throws Exception {
        final WsSpeService wsNotificationService = SolonEpgServiceLocator.getWsSpeService();
        long nbError = wsNotificationService.retryPremiereDemandePublication(session, batchLoggerModel, 0L);
        errorCount += nbError;
    }
}
