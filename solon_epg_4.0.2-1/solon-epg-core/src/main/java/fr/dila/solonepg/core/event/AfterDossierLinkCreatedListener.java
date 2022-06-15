package fr.dila.solonepg.core.event;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STDossierLinkConstant;
import fr.dila.st.core.event.RollbackEventListener;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

/**
 * Listener exécuté après la création d'un DossierLink.
 *
 * @author jtremeaux
 */
public class AfterDossierLinkCreatedListener extends RollbackEventListener {

    @Override
    public void handleDocumentEvent(Event event, DocumentEventContext ctx) {
        // Traite uniquement les évènements de document sur le point d'être créé
        if (!(event.getName().equals(DocumentEventTypes.DOCUMENT_CREATED))) {
            return;
        }

        // Traite uniquement les documents de type DossierLink
        final DocumentModel dossierLinkDoc = ctx.getSourceDocument();
        CoreSession session = ctx.getCoreSession();
        String docType = dossierLinkDoc.getType();
        if (!STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE.equals(docType)) {
            return;
        }

        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                // Charge le dossier
                DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
                DocumentModel dossierDoc = session.getDocument(new IdRef(dossierLink.getDossierId()));

                // Initialise les droits du DossierLink
                final EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
                dossierDistributionService.initDossierLinkAcl(session, dossierLinkDoc, dossierDoc);
            }
        }
        .runUnrestricted();
    }
}
