package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
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
public class LanceurGeneralPANBatchListener extends AbstractBatchEventListener {

    /**
     * Les batchs quotidiens
     *
     */
    private enum DailyBatch {
        BATCH_EVENT_DELETE_EXPORT_PAN_STATS(SolonEpgEventConstant.BATCH_EVENT_DELETE_EXPORT_PAN_STATS_EVENT),
        BATCH_EVENT_STAT_GENERATION_RESULTAT(SolonEpgEventConstant.BATCH_EVENT_STAT_GENERATION_RESULTAT),
        BATCH_EVENT_INJECTION_TM_BDJ(SolonEpgEventConstant.BATCH_EVENT_INJECTION_TM_BDJ);

        private String eventName;

        DailyBatch(String eventName) {
            this.eventName = eventName;
        }
    }

    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(LanceurGeneralPANBatchListener.class);

    public LanceurGeneralPANBatchListener() {
        super(LOGGER, SolonEpgEventConstant.LANCEUR_GENERAL_PAN_STATS);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) throws NuxeoException {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_LANCEUR_GENERAL_PAN_TEC);
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
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_LANCEUR_GENERAL_PAN_TEC, exc);
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
        LOGGER.info(session, EpgLogEnumImpl.END_B_LANCEUR_GENERAL_PAN_TEC);
    }
}
