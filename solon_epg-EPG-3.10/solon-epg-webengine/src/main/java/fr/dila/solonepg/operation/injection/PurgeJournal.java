package fr.dila.solonepg.operation.injection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.service.STServiceLocator;

@Operation(id = PurgeJournal.ID, category = Constants.CAT_DOCUMENT, label = "purge Log", description = "purge Log")
public class PurgeJournal {
    
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.PurgeJournal";
    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @OperationMethod
    public void run(DocumentModel doc) throws Exception {
        repriseLog.debug("Purger Logs");
        try {
            JournalService journalService = STServiceLocator.getJournalService();
            journalService.purger(doc.getId());
            repriseLog.debug("Purger Logs -> OK");
        } catch (Exception e) {
            repriseLog.error("Purger Logs -> KO", e);
            throw new Exception("Erreur lors de la purge de logs", e);
        }
    }
}
