package fr.dila.solonepg.core.event;

import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.service.DossierDistributionService;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.core.event.RollbackEventListener;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

/**
 * Listener permettant d'effectuer des traitements après la substitution d'une feuille de route.
 *
 * @author jtremeaux
 */
public class FeuilleRouteSubstitutionListener extends RollbackEventListener {

    @Override
    public void handleInlineEvent(Event event, InlineEventContext ctx) {
        // Traite uniquement les évènements de substitution de feuille de route
        if (!(event.getName().equals(STEventConstant.AFTER_SUBSTITUTION_FEUILLE_ROUTE))) {
            return;
        }
        final CoreSession session = ctx.getCoreSession();

        final DocumentModel oldFeuilleRouteDoc = (DocumentModel) ctx.getProperty(
            STEventConstant.DOSSIER_DISTRIBUTION_OLD_ROUTE_EVENT_PARAM
        );
        final DocumentModel newFeuilleRouteDoc = (DocumentModel) ctx.getProperty(
            STEventConstant.DOSSIER_DISTRIBUTION_NEW_ROUTE_EVENT_PARAM
        );
        String typeCreation = (String) ctx.getProperty(
            STEventConstant.DOSSIER_DISTRIBUTION_SUBSTITUTION_ROUTE_TYPE_EVENT_PARAM
        );

        // Démarre la nouvelle instance de feuille de route
        final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        dossierDistributionService.startRouteAfterSubstitution(
            session,
            oldFeuilleRouteDoc,
            newFeuilleRouteDoc,
            typeCreation
        );
    }
}
