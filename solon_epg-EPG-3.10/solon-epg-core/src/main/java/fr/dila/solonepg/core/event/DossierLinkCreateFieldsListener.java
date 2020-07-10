package fr.dila.solonepg.core.event;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.DossierDistributionService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.event.RollbackEventListener;

/**
 * Gestionnaire d'évènements qui permet de renseigner les métadonnées du DossierLink lors de distribution d'un
 * dossier.
 * 
 * @author arolin
 */
public class DossierLinkCreateFieldsListener extends RollbackEventListener {
    @Override
    public void handleDocumentEvent(Event event, DocumentEventContext ctx) throws ClientException {
        // Traite uniquement les documents de type DossierLink
        DocumentModel doc = ctx.getSourceDocument();
        String docType = doc.getType();
        if (!DossierSolonEpgConstants.DOSSIER_LINK_DOCUMENT_TYPE.equals(docType)) {
            return;
        }

        // Renseigne les champs du DossierLink
        final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        dossierDistributionService.setDossierLinksFields(ctx.getCoreSession(), doc);
    }
}
