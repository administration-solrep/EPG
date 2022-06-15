package fr.dila.solonepg.operation.injection;

import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.service.STServiceLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;

@Operation(
    id = DeleteDossier.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Delete Dossier",
    description = "Delete Dossier ."
)
public class DeleteDossier {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.DeleteDossier";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @OperationMethod
    public void run(DocumentModel doc) throws Exception {
        JournalService journalService = STServiceLocator.getJournalService();
        journalService.purger(doc.getId());

        final DocumentModel dossierDoc = doc;
        repriseLog.debug("suppression du dossier " + doc.getTitle());

        String title = doc.getTitle();
        try {
            final ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();

            //on lance les suppressions des dossiers sans vérifier les droits
            new UnrestrictedSessionRunner(session) {

                @Override
                public void run() {
                    try {
                        archiveService.supprimerDossier(session, dossierDoc);
                    } catch (Exception e) {
                        throw new NuxeoException(
                            "erreur lors de la suppression du dossier " + dossierDoc.getTitle(),
                            e
                        );
                    }
                }
            }
            .runUnrestricted();

            repriseLog.debug("suppression du dossier " + title + " -> OK");
        } catch (Exception e) {
            repriseLog.debug("suppression du dossier" + title + " -> KO", e);
            throw new Exception("Erreur lors de la suppression du dossier", e);
        }
    }
}
