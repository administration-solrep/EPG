package fr.dila.solonepg.operation.injection;

import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

@Operation(
    id = AbandonDossier.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Abandon dossier",
    description = "Abandon dossier ."
)
public class AbandonDossier {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.AbandonDossier";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @OperationMethod
    public void run(DocumentModel doc) throws Exception {
        repriseLog.debug("Abandon Dossier" + doc.getId());
        final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        dossierService.abandonDossierPourReprise(session, doc);
        repriseLog.debug("Abandon Dossier" + doc.getId() + " OK");
    }
}
