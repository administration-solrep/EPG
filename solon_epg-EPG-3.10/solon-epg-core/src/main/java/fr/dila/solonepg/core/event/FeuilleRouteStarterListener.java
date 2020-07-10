package fr.dila.solonepg.core.event;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.service.DossierDistributionService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.event.RollbackEventListener;

/**
 * Gestionnaire d'évènements qui permet de traiter les évènements de création des documents
 * de type dossier SOLON EPG.
 * 
 * @author jtremeaux
 */
public class FeuilleRouteStarterListener extends RollbackEventListener {
    @Override
    public void handleInlineEvent(Event event, InlineEventContext ctx) throws ClientException {
        // Traite uniquement les évènements de création de document
        if (!(event.getName().equals(SolonEpgEventConstant.AFTER_DOSSIER_CREATED))) {
            return;
        }

        // Traite uniquement les documents de type Dossier
        Dossier dossier = (Dossier) ctx.getProperty(SolonEpgEventConstant.DOSSIER_EVENT_PARAM);
        String posteId = (String) ctx.getProperty(SolonEpgEventConstant.POSTE_ID_EVENT_PARAM);
        String norDossierCopieFdr = (String) ctx.getProperty(SolonEpgEventConstant.NOR_DOSSIER_COPIE_FDR_PARAM);

    	DossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
    	
        // Si il n'y a pas de NOR pour copier uen feuille de route existante
        if (norDossierCopieFdr == null || norDossierCopieFdr.isEmpty()) {
        	// Démarre la feuille de route par defaut associée au dossier
        	dossierDistributionService.startDefaultRoute(ctx.getCoreSession(), dossier, posteId);
        } else {
        	dossierDistributionService.startRouteCopieFromDossier(ctx.getCoreSession(), dossier.getDocument(), posteId, norDossierCopieFdr);
        }
     }
}
