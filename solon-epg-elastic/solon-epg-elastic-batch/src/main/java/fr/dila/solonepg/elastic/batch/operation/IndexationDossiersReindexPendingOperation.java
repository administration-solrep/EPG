package fr.dila.solonepg.elastic.batch.operation;

import fr.dila.solonepg.elastic.batch.IIndexationDossiersBatch;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.runtime.api.Framework;

@Operation(id = IndexationDossiersReindexPendingOperation.ID, label = "Indexation des dossiers en attente")
public class IndexationDossiersReindexPendingOperation {
    public static final String ID = "SolonEpg.Indexation.reindexPending";

    @OperationMethod
    public void run() {
        IIndexationDossiersBatch indexationDossiersBatch = Framework.getService(IIndexationDossiersBatch.class);
        indexationDossiersBatch.indexationMassive();
    }
}
