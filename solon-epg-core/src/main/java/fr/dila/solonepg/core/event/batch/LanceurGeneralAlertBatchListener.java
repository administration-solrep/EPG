package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.ss.api.constant.SSEventConstant;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

/**
 * Lanceur général des batchs
 *
 */
public class LanceurGeneralAlertBatchListener extends AbstractBatchEventListener {

    /**
     * Les batchs quotidiens
     *
     */
    private enum DailyBatch {
        BATCH_EVENT_SEND_ALERT(SSEventConstant.SEND_ALERT_BATCH_EVENT),
        BATCH_EVENT_CONFIRM_ALERT(SolonEpgEventConstant.BATCH_EVENT_CONFIRM_ALERT),
        BATCH_EVENT_REMINDER_OUTDATED_PASSWORD(STEventConstant.SEND_DAILY_REMIND_CHANGE_PASS_EVENT),
        BATCH_EVENT_DOSSIER_LINK_INCOHERENT(SSEventConstant.INCOHERENT_DOSSIER_LINK_EVENT),
        BATCH_EVENT_ALERT_BLOCKED_DOSSIERS(SSEventConstant.BLOCKED_ROUTES_ALERT_EVENT);

        private String eventName;

        DailyBatch(String eventName) {
            this.eventName = eventName;
        }
    }

    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(LanceurGeneralAlertBatchListener.class);

    public LanceurGeneralAlertBatchListener() {
        super(LOGGER, SolonEpgEventConstant.LANCEUR_GENERAL_ALERT);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) throws NuxeoException {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_LANCEUR_GENERAL_DIVERS_TEC);
        final long startTime = Calendar.getInstance().getTimeInMillis();
        try {
            EventProducer eventProducer = STServiceLocator.getEventProducer();
            Map<String, Serializable> eventProperties = new HashMap<>();
            eventProperties.put(STEventConstant.BATCH_EVENT_PROPERTY_PARENT_ID, batchLoggerId);

            for (DailyBatch dailyBatch : DailyBatch.values()) {
                InlineEventContext inlineEventContext = new InlineEventContext(
                    session,
                    session.getPrincipal(),
                    eventProperties
                );
                eventProducer.fireEvent(inlineEventContext.newEvent(dailyBatch.eventName));
            }
        } catch (Exception exc) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_LANCEUR_GENERAL_DIVERS_TEC, exc);
            ++errorCount;
        }
        final long endTime = Calendar.getInstance().getTimeInMillis();
        try {
            STServiceLocator
                .getSuiviBatchService()
                .createBatchResultFor(batchLoggerModel, "Exécution du lanceur général", endTime - startTime);
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_LANCEUR_GENERAL_DIVERS_TEC);
    }
}
