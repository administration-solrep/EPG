package fr.dila.solonmgpp.core.event;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;

import fr.dila.solonmgpp.api.domain.ActiviteParlementaire;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.domain.SemaineParlementaire;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;

/**
 * 
 * @author admin
 */
public class PurgeCalendrierBatchListener extends AbstractBatchEventListener {

	/**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(PurgeCalendrierBatchListener.class);
    
    public PurgeCalendrierBatchListener() {
    	super(LOGGER,STEventConstant.BATCH_EVENT_PURGE_CALENDRIER_BATCH);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) throws ClientException {
    	final long startTime = Calendar.getInstance().getTimeInMillis();   
        try {
            final Calendar purgeCalendar = Calendar.getInstance();
            
            LOGGER.info(session, MgppLogEnumImpl.START_BATCH_PURGE_CALENDAR_TEC) ;
            ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator.getParametreMgppService().findParametrageMgpp(session);
            long delai = (parametrageMgpp.getDelaiPurgeCalendrier() == null ? 1 : parametrageMgpp.getDelaiPurgeCalendrier());
           
            purgeCalendar.add(Calendar.MONTH, - (int)delai);
            
            final String literalDate = DateLiteral.dateFormatter.print(purgeCalendar.getTimeInMillis());
            
            StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
            queryBuilder.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_DOC_TYPE + " as d");
            queryBuilder.append(" WHERE d.");
            queryBuilder.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX);
            queryBuilder.append(":");
            queryBuilder.append(ActiviteParlementaire.DATE);
            queryBuilder.append(" <= DATE '" + literalDate + "'");
 
            DocumentRef[] docsAPRefs = QueryUtils.doUFNXQLQueryForIds(session, queryBuilder.toString(), null);
            session.removeDocuments(docsAPRefs);
            
            
            queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
            queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_DOC_TYPE + " as d");
            queryBuilder.append(" WHERE d.");
            queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
            queryBuilder.append(":");
            queryBuilder.append(SemaineParlementaire.DATE_FIN);
            queryBuilder.append(" <= DATE '" + literalDate + "'");

            DocumentRef[] docsSPRefs = QueryUtils.doUFNXQLQueryForIds(session, queryBuilder.toString(), null);
            session.removeDocuments(docsSPRefs);

        } catch (Exception e) {
             LOGGER.error(session, MgppLogEnumImpl.FAIL_BATCH_PURGE_CALENDAR_TEC,e);
             errorCount++;
        }
        final long endTime = Calendar.getInstance().getTimeInMillis();
        try {
        	STServiceLocator.getSuiviBatchService().createBatchResultFor(batchLoggerModel, "Exécution du Batch de purge du calendrier", endTime-startTime);
        } catch (Exception e) {
        	LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC,e);
        }
        LOGGER.info(session, MgppLogEnumImpl.END_BATCH_PURGE_CALENDAR_TEC) ;
    }

}
