package fr.dila.solonmgpp.core.event.batch;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.event.Event;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.SuiviBatchService;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Batch de mise à jour de la table mgpp_info_corbeille
 * 
 */
public class UpdateMgppInfoCorbeilleBatchListener extends AbstractBatchEventListener {

    private static final STLogger LOGGER = STLogFactory.getLog(UpdateMgppInfoCorbeilleBatchListener.class);
    
    public UpdateMgppInfoCorbeilleBatchListener(){
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_UPDATE_INFO_CORBEILLE);
    }
    
    @Override
    protected void processEvent(CoreSession session, Event event) throws ClientException {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_UPDATE_INFO_CORBEILLE_TEC);
        final long startTime = Calendar.getInstance().getTimeInMillis();
        try {
            SolonMgppServiceLocator.getNotificationService().updateMgppInfoCorbeille(session);
        } catch (Exception exc) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_UPDATE_INFO_CORBEILLE_TEC, exc);
            errorCount++;
        }
        final long endTime = Calendar.getInstance().getTimeInMillis();
        final SuiviBatchService suiviBatchService = STServiceLocator.getSuiviBatchService();
        try {
        	suiviBatchService.createBatchResultFor(batchLoggerModel, "Mise à jour table MGPP_INFO_CORBEILLE", endTime-startTime);
        } catch (Exception e) {
        	LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC,e);
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_UPDATE_INFO_CORBEILLE_TEC);
    }
}