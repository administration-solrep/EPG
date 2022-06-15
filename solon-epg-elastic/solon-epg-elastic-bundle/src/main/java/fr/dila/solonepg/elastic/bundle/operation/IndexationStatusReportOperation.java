package fr.dila.solonepg.elastic.bundle.operation;

import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.runtime.transaction.TransactionHelper;

@Operation(id = IndexationStatusReportOperation.ID, label = "Statut des indexations")
public class IndexationStatusReportOperation implements IIndexationOperation {
    public static final String ID = "SolonEpg.Indexation.statusReport";

    protected static final Log LOG = LogFactory.getLog(IndexationStatusReportOperation.class);

    @Context
    private CoreSession session;

    @OperationMethod
    public void run() throws Exception {
        // transaction handled manually by called methods
        TransactionHelper.commitOrRollbackTransaction();
        HashSet<String> idsManquants = new HashSet<>();
        HashSet<String> idsSupplementaires = new HashSet<>();
        statusReport(idsManquants, idsSupplementaires, session);

        if (!idsManquants.isEmpty()) {
            StringBuilder ids = new StringBuilder();
            boolean first = true;
            for (String id : idsManquants) {
                if (first) {
                    first = false;
                } else {
                    ids.append(",");
                }
                ids.append(id);
            }
            LOG.warn(String.format("%d dossiers manquants dans l'index: %s", idsManquants.size(), ids.toString()));
        }
        if (!idsSupplementaires.isEmpty()) {
            StringBuilder ids = new StringBuilder();
            boolean first = true;
            for (String id : idsSupplementaires) {
                if (first) {
                    first = false;
                } else {
                    ids.append(",");
                }
                ids.append(id);
            }
            LOG.warn(
                String.format("%d dossiers surnuméraires dans l'index: %s", idsSupplementaires.size(), ids.toString())
            );
        }
    }
}
