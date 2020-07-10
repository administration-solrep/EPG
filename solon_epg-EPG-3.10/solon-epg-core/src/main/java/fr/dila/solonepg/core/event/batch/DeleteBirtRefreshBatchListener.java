package fr.dila.solonepg.core.event.batch;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.event.Event;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;

public class DeleteBirtRefreshBatchListener extends AbstractBatchEventListener {

    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOGGER = STLogFactory.getLog(DeleteBirtRefreshBatchListener.class);
    
    private static final String QUERY = "Select b.ecm:uuid as id from BirtRefreshFichier as b";
    
    public DeleteBirtRefreshBatchListener() {
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_DELETE_BIRT_REFRESH);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) throws ClientException {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_DEL_BIRT_REFRESH_TEC);
        
        final long startTime = Calendar.getInstance().getTimeInMillis();
        long nbSuppressionBirt = 0;
        try {
        	DocumentRef[] refsToDelete = QueryUtils.doUFNXQLQueryForIds(session, QUERY, new Object[]{});
	        LOGGER.info(session, EpgLogEnumImpl.DEL_BIRT_REFRESH_TEC, refsToDelete);
	        if (refsToDelete != null) {
	        	nbSuppressionBirt = refsToDelete.length;
	        }
            session.removeDocuments(refsToDelete);
            session.save();
        } catch (Exception exc) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_DEL_BIRT_REFRESH_TEC, exc);
            errorCount++;
        }
        
        final long endTime = Calendar.getInstance().getTimeInMillis();
        try {
        	STServiceLocator.getSuiviBatchService().createBatchResultFor(batchLoggerModel, "Suppression BirtRefreshFichier", nbSuppressionBirt, endTime-startTime);
        } catch (Exception e) {
        	LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC,e);
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_DEL_BIRT_REFRESH_TEC);
    }

}
