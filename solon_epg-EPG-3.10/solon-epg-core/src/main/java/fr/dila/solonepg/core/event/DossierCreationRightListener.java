package fr.dila.solonepg.core.event;

import java.util.Collections;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.event.RollbackEventListener;

/**
 * Listener exécuté au moment de la création des documents Dossier.
 * 
 * @author jtremeaux
 */
public class DossierCreationRightListener extends RollbackEventListener {
    @Override
    public void handleInlineEvent(Event event, InlineEventContext ctx) throws ClientException {
        // Traite uniquement les évènements de création de document
        if (!event.getName().equals(SolonEpgEventConstant.AFTER_DOSSIER_CREATED) && !event.getName().equals(SolonEpgEventConstant.INJECTION_AFTER_DOSSIER_CREATED)) {
            return;
        }
        final CoreSession session = ctx.getCoreSession();
        Dossier dossier = (Dossier) ctx.getProperty(SolonEpgEventConstant.DOSSIER_EVENT_PARAM);
        
        // Initialise les droits à la création du dossier
        final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        dossierService.initDossierCreationAclUnrestricted(session, Collections.singletonList(dossier));
     }
}
