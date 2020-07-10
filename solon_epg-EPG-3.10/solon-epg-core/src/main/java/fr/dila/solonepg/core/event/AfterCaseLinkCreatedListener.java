package fr.dila.solonepg.core.event;

import java.io.Serializable;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.feuilleroute.STRouteStep;

/**
 * Listener exécuté après la création d'un CaseLink après le passage d'une étape à l'état "running" -->.
 * 
 * @author arolin
 */
public class AfterCaseLinkCreatedListener implements PostCommitEventListener {

    public void handleEvent(Event event) throws ClientException {
        EventContext eventCtx = event.getContext();
        final CoreSession session = eventCtx.getCoreSession();
        // récupération des propriétés de l'événement
        Map<String, Serializable> eventProperties = eventCtx.getProperties();
        final String dossierDocId = (String) eventProperties.get(SolonEpgEventConstant.AFTER_CASE_LINK_CREATED_DOSSIER_DOCID_PARAM);
        final String routeStepDocId = (String) eventProperties.get(SolonEpgEventConstant.AFTER_CASE_LINK_CREATED_ROUTE_STEP_DOCID_PARAM);
        final STRouteStep routeStep = retrieveRouteStep(session, routeStepDocId);

        // on n'envoie pas de notifications lorsque l'on créé le dossier cad lorsque on l'étape de feuille de route est à "Pour initialisation"
        if (!VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION.equals(routeStep.getType())) {
            final DocumentModel dossierDoc = session.getDocument(new IdRef(dossierDocId));
            // traitement pour envoyer des notifications aux utilisateurs
            ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
            profilUtilisateurService.checkAndSendMailNotificationValidationEtape(routeStep, dossierDoc, eventCtx.getCoreSession());
        }
    }

    @Override
    public void handleEvent(EventBundle events) throws ClientException {
    	if(!events.containsEventName(SolonEpgEventConstant.AFTER_CASE_LINK_CREATED_EVENT)){
    		return;
    	}
        for (Event event : events) {
            if (SolonEpgEventConstant.AFTER_CASE_LINK_CREATED_EVENT.equals(event.getName())) {
                handleEvent(event);
            }
        }
    }
    
    private STRouteStep retrieveRouteStep(final CoreSession session, final String routeStepDocId) throws ClientException {
    	 final DocumentModel routeStepDoc = session.getDocument(new IdRef(routeStepDocId));
         return routeStepDoc.getAdapter(STRouteStep.class);
    }
}
