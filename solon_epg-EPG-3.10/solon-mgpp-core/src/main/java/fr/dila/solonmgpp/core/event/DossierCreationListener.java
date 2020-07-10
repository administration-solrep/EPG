package fr.dila.solonmgpp.core.event;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.core.event.RollbackEventListener;

/**
 * Listener exécuté au moment de la création des documents Dossier.
 * 
 * @author jtremeaux
 */
public class DossierCreationListener extends RollbackEventListener {
    @Override
    public void handleInlineEvent(Event event, InlineEventContext ctx) throws ClientException {
        // Traite uniquement les évènements de création de document
        if (!event.getName().equals(SolonEpgEventConstant.AFTER_DOSSIER_CREATED) && !event.getName().equals(SolonEpgEventConstant.INJECTION_AFTER_DOSSIER_CREATED)) {
            return;
        }

        final CoreSession session = ctx.getCoreSession();
        Dossier dossier = (Dossier) ctx.getProperty(SolonEpgEventConstant.DOSSIER_EVENT_PARAM);

        ActeService acteService = SolonEpgServiceLocator.getActeService();
        if (acteService.isRapportAuParlement(dossier.getTypeActe())) {

            final DossierService dossierService = SolonMgppServiceLocator.getDossierService();
            FichePresentationDR fichePresentationDR = dossierService.findOrCreateFicheDR(session, dossier.getNumeroNor());
            fichePresentationDR.setMinisteres(dossier.getMinistereResp());
            fichePresentationDR.setPole(dossier.getNomCompletChargesMissions()) ;
            dossierService.saveFicheDR(session, fichePresentationDR.getDocument());
        }
    }
}
