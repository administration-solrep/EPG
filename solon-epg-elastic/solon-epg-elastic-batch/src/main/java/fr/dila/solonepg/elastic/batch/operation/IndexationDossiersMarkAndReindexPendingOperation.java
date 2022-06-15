package fr.dila.solonepg.elastic.batch.operation;

import fr.dila.solonepg.elastic.batch.IIndexationDossiersBatch;
import fr.dila.solonepg.elastic.persistence.service.IIndexationPersistenceService;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.StringList;
import org.nuxeo.runtime.api.Framework;

@Operation(
    id = IndexationDossiersMarkAndReindexPendingOperation.ID,
    label = "Marquage et indexation massive des dossiers en attente"
)
public class IndexationDossiersMarkAndReindexPendingOperation {
    public static final String ID = "SolonEpg.Indexation.markAndReindexPending";

    @Param(name = "dossiersIds", required = true, order = 1)
    protected StringList dossiersIds;

    @OperationMethod
    public void run() {
        // transaction handled manually by called methods
        IIndexationPersistenceService persistenceService = Framework.getService(IIndexationPersistenceService.class);
        IIndexationDossiersBatch indexationDossiersBatch = Framework.getService(IIndexationDossiersBatch.class);
        persistenceService.markIndexationMassive(dossiersIds);
        indexationDossiersBatch.indexationMassive();
    }
}
