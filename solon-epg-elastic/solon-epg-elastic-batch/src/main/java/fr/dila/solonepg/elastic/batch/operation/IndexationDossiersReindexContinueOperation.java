package fr.dila.solonepg.elastic.batch.operation;

import fr.dila.solonepg.elastic.batch.IIndexationDossiersBatch;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.runtime.api.Framework;

@Operation(id = IndexationDossiersReindexContinueOperation.ID, label = "Indexation complète")
public class IndexationDossiersReindexContinueOperation {
    public static final String ID = "SolonEpg.Indexation.reindexContinue";

    @OperationMethod
    public void run() {
        IIndexationDossiersBatch indexationDossiersBatch = Framework.getService(IIndexationDossiersBatch.class);
        indexationDossiersBatch.indexationContinue();
    }
}
