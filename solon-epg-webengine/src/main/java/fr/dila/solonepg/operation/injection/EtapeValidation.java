package fr.dila.solonepg.operation.injection;

import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

@Operation(
    id = EtapeValidation.ID,
    category = Constants.CAT_DOCUMENT,
    label = "validate step",
    description = "validate step ."
)
public class EtapeValidation {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.EtapeValidation";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "stepId", required = true, order = 1)
    protected String stepId;

    @OperationMethod
    public void run() throws Exception {
        repriseLog.debug("Validation l'etape" + stepId);
        try {
            final EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
            final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
            DocumentModel dossierLink = corbeilleService.getDossierLink(session, stepId);
            dossierDistributionService.validerEtapePourReprise(session, dossierLink);
            // Framework.getLocalService(EventService.class).waitForAsyncCompletion();
            repriseLog.debug("validation l'etape" + stepId + "-> OK ");
        } catch (Exception e) {
            repriseLog.debug("Validation l'etape" + stepId + "-> KO ", e);
            throw new Exception("Erreur lors de la validation l'etape" + stepId, e);
        }
    }
}
