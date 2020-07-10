package fr.dila.solonepg.elastic.batch.operation;

import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.elastic.batch.IIndexationDossiersBatch;

@Operation(id = IndexationDossiersReindexPendingOperation.ID, label = "Indexation des dossiers en attente")
public class IndexationDossiersReindexPendingOperation {

	public final static String ID = "SolonEpg.Indexation.reindexPending";

	@OperationMethod
	public void run() throws Exception {
		// transaction handled manually by called methods
		TransactionHelper.commitOrRollbackTransaction();
		IIndexationDossiersBatch indexationDossiersBatch = Framework.getService(IIndexationDossiersBatch.class);
		indexationDossiersBatch.indexationMassive();
	}

}
