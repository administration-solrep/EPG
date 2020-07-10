package fr.dila.solonepg.core.event.batch;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.ss.api.constant.SSEventConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;

/**
 * Batch de suppression des CaseLink avec deleted=1
 * 
 * @author asatre
 * 
 */
public class CleanDeletedDossierLinkBatchListener extends AbstractBatchEventListener {

	/**
	 * Logger formalisé en surcouche du logger apache/log4j
	 */
	private static final STLogger	LOGGER	= STLogFactory.getLog(CleanDeletedDossierLinkBatchListener.class);

	public CleanDeletedDossierLinkBatchListener() {
		super(LOGGER, SSEventConstant.CLEAN_DELETED_DL_EVENT);
	}

	@Override
	protected void processEvent(CoreSession session, Event event) throws ClientException {
		LOGGER.info(session, EpgLogEnumImpl.INIT_B_DEL_DL_TEC);
		try {

			StringBuilder queryBuilder = new StringBuilder("SELECT dl.ecm:uuid as id FROM ");
			queryBuilder.append(DossierSolonEpgConstants.DOSSIER_LINK_DOCUMENT_TYPE);
			queryBuilder.append(" as dl WHERE dl.");
			queryBuilder.append(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX);
			queryBuilder.append(":");
			queryBuilder.append(DossierSolonEpgConstants.DELETED);
			queryBuilder.append(" = 1 ");

			DocumentRef[] docRefs = QueryUtils.doUFNXQLQueryForIds(session, queryBuilder.toString(), null);
			Integer compteurDocument = 0;
			for (DocumentRef docRef : docRefs) {
				session.removeDocument(docRef);
				compteurDocument = compteurDocument + 1;
				if (compteurDocument.equals(50)) {
					// Remise à zéro du compteur et commit pour éviter les erreurs de rollback
					compteurDocument = 0;
					TransactionHelper.commitOrRollbackTransaction();
					TransactionHelper.startTransaction();
				}
			}
			LOGGER.info(session, STLogEnumImpl.DEL_DL_TEC, docRefs);

		} catch (Exception exc) {
			LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_DEL_DL_TEC, exc);
		}
		LOGGER.info(session, EpgLogEnumImpl.END_B_DEL_DL_TEC);

	}
}
