package fr.dila.solonepg.core.event.batch;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.constant.SSEventConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;

/**
 * Batch de suppression des CaseLink avec deleted=1
 * 
 * @author asatre
 * 
 */
public class CleanDeletedDossierBatchListener extends AbstractBatchEventListener {

	/**
	 * Logger formalisé en surcouche du logger apache/log4j
	 */
	private static final STLogger	LOGGER	= STLogFactory.getLog(CleanDeletedDossierBatchListener.class);

	public CleanDeletedDossierBatchListener() {
		super(LOGGER, SSEventConstant.CLEAN_DELETED_DOS_EVENT);
	}

	@Override
	protected void processEvent(CoreSession session, Event event) throws ClientException {
		LOGGER.info(session, EpgLogEnumImpl.INIT_B_DEL_DOS_TEC);
		try {

			StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
			queryBuilder.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
			queryBuilder.append(" as d WHERE d.");
			queryBuilder.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
			queryBuilder.append(":");
			queryBuilder.append(DossierSolonEpgConstants.DELETED);
			queryBuilder.append(" = 1 ");

			DocumentRef[] docRefs = QueryUtils.doUFNXQLQueryForIds(session, queryBuilder.toString(), null);
			LOGGER.info(session, STLogEnumImpl.DEL_DOSSIER_TEC, docRefs);
			Integer compteurDocument = 0;
			DossierService dossierService = SolonEpgServiceLocator.getDossierService();
			JournalService journalService = SolonEpgServiceLocator.getJournalService();
			for (DocumentRef docRef : docRefs) {
				String nor = null;
				String id = null;
				// On cherche à récupérer le nor du dossier pour le stocker dans la table de correspondance id/nor pour
				// les dossiers supprimés
				DocumentModel doc = session.getDocument(docRef);
				if (doc != null) {
					id = doc.getRef().toString();
					Dossier dossier = doc.getAdapter(Dossier.class);
					if (doc != null) {
						nor = dossier.getNumeroNor();
					}
				}
				session.removeDocument(docRef);
				if (nor != null && id != null) {
					try {
						dossierService.saveNorIdDossierSupprime(nor, id);
					} catch (ClientException e) {
						LOGGER.error(STLogEnumImpl.DEL_DOSSIER_TEC, e);
					}
					// Inscription dans le journal technique
					journalService.journaliserActionAdministration(session, id,
							SolonEpgEventConstant.DOSSIER_SUPPRIME_EVENT,
							SolonEpgEventConstant.STATUT_DOSSIER_SUPPRIME_COMMENT);
				}
				compteurDocument = compteurDocument + 1;
				if (compteurDocument.equals(50)) {
					// Remise à zéro du compteur et commit pour éviter les erreurs de rollback
					compteurDocument = 0;
					TransactionHelper.commitOrRollbackTransaction();
					TransactionHelper.startTransaction();
				}
			}

		} catch (Exception exc) {
			LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_DEL_DOS_TEC, exc);
		}
		LOGGER.info(session, EpgLogEnumImpl.END_B_DEL_DOS_TEC);

	}
}
