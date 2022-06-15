package fr.dila.solonepg.elastic.batch.operation;

import fr.dila.solonepg.elastic.batch.IIndexationDossiersBatch;
import fr.dila.solonepg.elastic.batch.IIndexationService;
import fr.dila.solonepg.elastic.persistence.service.IIndexationPersistenceService;
import java.util.List;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.runtime.api.Framework;

@Operation(id = IndexationDossiersmarkAllAndReindexPendingOperation.ID, label = "Indexation complète")
public class IndexationDossiersmarkAllAndReindexPendingOperation {
    public static final String ID = "SolonEpg.Indexation.markAllAndReindexPending";

    @Context
    private CoreSession session;

    @OperationMethod
    public void run() {
        IIndexationService indexationService = Framework.getService(IIndexationService.class);
        IIndexationPersistenceService persistenceService = Framework.getService(IIndexationPersistenceService.class);
        IIndexationDossiersBatch indexationDossiersBatch = Framework.getService(IIndexationDossiersBatch.class);
        // marquage des indexations
        List<String> ids = indexationService.listDossiersIdsAIndexer(session);
        persistenceService.markIndexationMassive(ids);
        indexationDossiersBatch.indexationMassive();
    }
}
