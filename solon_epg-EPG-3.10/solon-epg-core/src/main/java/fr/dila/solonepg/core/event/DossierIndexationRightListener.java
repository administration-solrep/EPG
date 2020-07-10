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
 * Listener executé au changement de cycle de vie d'un dossier
 * 
 * @author bgamard
 */
public class DossierIndexationRightListener extends RollbackEventListener {
    @Override
    public void handleInlineEvent(Event event, InlineEventContext ctx) throws ClientException {
        //TODO : Les droits d'indexation devront aussi être mis lors de la réattribution de NOR et de la cloture d'un dossier
        final CoreSession session = ctx.getCoreSession();
        final Dossier dossier = (Dossier) ctx.getProperty(SolonEpgEventConstant.DOSSIER_EVENT_PARAM);
        
        // Initialise les droits d'indexation du dossier
        final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        dossierService.initDossierIndexationAclUnrestricted(session, Collections.singletonList(dossier));
     }
}
