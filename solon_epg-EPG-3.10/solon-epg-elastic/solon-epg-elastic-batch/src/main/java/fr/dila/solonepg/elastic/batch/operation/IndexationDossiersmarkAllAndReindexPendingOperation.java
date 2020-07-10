package fr.dila.solonepg.elastic.batch.operation;

import java.util.List;

import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.elastic.batch.IIndexationDossiersBatch;
import fr.dila.solonepg.elastic.batch.IIndexationService;
import fr.dila.solonepg.elastic.persistence.service.IIndexationPersistenceService;

@Operation(id = IndexationDossiersmarkAllAndReindexPendingOperation.ID, label = "Indexation complète")
public class IndexationDossiersmarkAllAndReindexPendingOperation {

	public final static String ID = "SolonEpg.Indexation.markAllAndReindexPending";

	@OperationMethod
	public void run() throws Exception {
		// transaction handled manually by called methods
		TransactionHelper.commitOrRollbackTransaction();
		IIndexationService indexationService = Framework.getService(IIndexationService.class);
		IIndexationPersistenceService persistenceService = Framework.getService(IIndexationPersistenceService.class);
		IIndexationDossiersBatch indexationDossiersBatch = Framework.getService(IIndexationDossiersBatch.class);
		// marquage des indexations
		List<String> ids = indexationService.listDossiersIdsAIndexer();
		persistenceService.markIndexationMassive(ids);
		indexationDossiersBatch.indexationMassive();
	}

}
