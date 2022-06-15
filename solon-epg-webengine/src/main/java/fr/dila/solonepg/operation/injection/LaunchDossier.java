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
    id = LaunchDossier.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Launch Dossier",
    description = "Launch Dossier ."
)
public class LaunchDossier {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.LaunchDossier";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "stepId", required = true, order = 1)
    protected String stepId;

    @OperationMethod
    public DocumentModel run(DocumentModel doc) throws Exception {
        repriseLog.debug("Lancement du dossier " + doc.getTitle());
        try {
            final EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
            final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
            DocumentModel dossierLinkDoc = corbeilleService.getDossierLink(session, stepId);
            // Validation de la première étape de FDR

            // Déverrouille le dossier si il est verrouillé
            // if (getCanUnlockCurrentDossier()) {
            // unlockCurrentDossier();
            // }

            // Valide l'étape en cours
            dossierDistributionService.lancerDossier(session, doc, dossierLinkDoc);

            // Notifie le changement de l'ancienne Mailbox
            // Events.instance().raiseEvent(EventNames.DOCUMENT_CHILDREN_CHANGED, session.getDocument(mailboxRef));

            // Passage du dossier à l'état lancé
            //doc.followTransition(Dossier.DossierTransition.toRunning.toString());
            session.saveDocument(doc);
            // log.info("before  waitForAsyncCompletion");
            // Framework.getLocalService(EventService.class).waitForAsyncCompletion();
            repriseLog.debug("Lancement du dossier " + doc.getTitle() + " -> OK");
        } catch (Exception e) {
            repriseLog.debug("Lancement du dossier " + doc.getTitle() + " -> KO", e);
            throw new Exception("Erreur lors de Lancement du dossier", e);
        }
        return doc;
    }
}
