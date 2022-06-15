package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;

public class SuppressionNotificationListener extends AbstractBatchEventListener {
    private static final String QUERY =
        "SELECT nt.ecm:uuid as id FROM  NotificationDoc as nt WHERE nt.nt:dateArrive <= TIMESTAMP '%s' ";
    private static final STLogger LOGGER = STLogFactory.getLog(SuppressionNotificationListener.class);

    public SuppressionNotificationListener() {
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_SUPPRESSION_NOTIFICATION);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_DEL_NOTIF_TEC);
        final long startTime = Calendar.getInstance().getTimeInMillis();
        long nbSuppressionNotification = 0;
        try {
            final Calendar currentDate = Calendar.getInstance();
            currentDate.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH) - 1);

            final Long timeInMillis = currentDate.getTimeInMillis();
            final String dateLiteral = DateLiteral.dateTimeFormatter.print(timeInMillis);
            final String query = String.format(QUERY, dateLiteral);

            DocumentRef[] docRefs = QueryUtils.doUFNXQLQueryForIds(session, query, null);
            LOGGER.info(session, EpgLogEnumImpl.DEL_NOTIF_TEC, docRefs);
            if (docRefs != null) {
                nbSuppressionNotification = docRefs.length;
            }
            session.removeDocuments(docRefs);
            session.save();
        } catch (Exception exc) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_DEL_NOTIF_TEC, exc);
            errorCount++;
        }
        final long endTime = Calendar.getInstance().getTimeInMillis();
        try {
            STServiceLocator
                .getSuiviBatchService()
                .createBatchResultFor(
                    batchLoggerModel,
                    "Suppression des notifications (maj corbeille)",
                    nbSuppressionNotification,
                    endTime - startTime
                );
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_DEL_NOTIF_TEC);
    }
}
