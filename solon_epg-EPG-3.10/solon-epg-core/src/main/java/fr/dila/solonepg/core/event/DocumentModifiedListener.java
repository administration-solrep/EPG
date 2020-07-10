package fr.dila.solonepg.core.event;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.DossierDistributionService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.event.AbstractFilterEventListener;

/**
 * Listener permettant de traiter les changement du {@link Dossier} pour les denormaliser dans les {@link DossierLink}
 * 
 * @author asatre
 */
public class DocumentModifiedListener extends AbstractFilterEventListener<DocumentEventContext> {

	public DocumentModifiedListener(){
		super(new String[]{DocumentEventTypes.DOCUMENT_UPDATED, DocumentEventTypes.DOCUMENT_CREATED}, DocumentEventContext.class);
	}
	
    @Override
    protected void doHandleEvent(Event event, final DocumentEventContext ctx) throws ClientException {

        CoreSession session = ctx.getCoreSession();

        // Traite uniquement les modifications ou la creation de document
        
        // Traite uniquement les modifications de document ayant pour type dossier
        DocumentModel model = ctx.getSourceDocument();
        String docType = model.getType();
        if (!DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE .equals(docType)) {
            return;
        }

        // Renseigne les champs du DossierLink
        final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        dossierDistributionService.updateDossierLinksFields(session, model.getAdapter(Dossier.class));
     }

}
