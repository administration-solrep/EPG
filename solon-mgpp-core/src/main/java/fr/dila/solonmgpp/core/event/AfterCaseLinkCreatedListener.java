package fr.dila.solonmgpp.core.event;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

/**
 * Listener exécuté après la création d'un CaseLink ie après le passage d'une étape à l'état "running"
 *
 * @author asatre
 */
public class AfterCaseLinkCreatedListener implements PostCommitEventListener {

    public void handleEvent(Event event) {
        final EventContext eventCtx = event.getContext();
        final CoreSession session = eventCtx.getCoreSession();

        // récupération des propriétés de l'événement
        final Map<String, Serializable> eventProperties = eventCtx.getProperties();
        final String dossierDocId = (String) eventProperties.get(
            SolonEpgEventConstant.AFTER_CASE_LINK_CREATED_DOSSIER_DOCID_PARAM
        );
        final String routeStepDocId = (String) eventProperties.get(
            SolonEpgEventConstant.AFTER_CASE_LINK_CREATED_ROUTE_STEP_DOCID_PARAM
        );

        final ActeService acteService = SolonEpgServiceLocator.getActeService();

        final String typeEtape = retrieveRouteStepType(session, routeStepDocId);
        final Dossier dossier = retrieveDossier(session, dossierDocId);
        final String typeActe = dossier.getTypeActe();

        if (VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SECTEUR_PARLEMENTAIRE.equals(typeEtape)) {
            if (StringUtils.isNotBlank(typeActe)) {
                final Set<String> listLoi = acteService.getLoiList();

                if (listLoi.contains(typeActe)) {
                    if (StringUtils.isBlank(dossier.getIdDossier())) {
                        // creation de l'evenement "1 : Dépôt d'un projet de loi"
                        SolonMgppServiceLocator
                            .getEvenementService()
                            .createEvenementEppEvt01Brouillon(dossier, eventCtx.getCoreSession());
                    }
                } else if (TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT.equals(typeActe)) {
                    // creation de l'evenement "44 : Dépôt de rapport au parlement"
                    SolonMgppServiceLocator
                        .getEvenementService()
                        .createEvenementEppEvt44Brouillon(dossier, eventCtx.getCoreSession());
                } else if (acteService.isDecretPR(typeActe)) {
                    // Décret du Président de la République
                    SolonMgppServiceLocator
                        .getDossierService()
                        .notifierDecret(dossier.getNumeroNor(), eventCtx.getCoreSession(), Boolean.TRUE);
                }
            }
        }
    }

    @Override
    public void handleEvent(EventBundle events) {
        for (Event event : events) {
            if (SolonEpgEventConstant.AFTER_CASE_LINK_CREATED_EVENT.equals(event.getName())) {
                handleEvent(event);
            }
        }
    }

    private String retrieveRouteStepType(final CoreSession session, final String routeStepDocId) {
        final DocumentModel routeStepDoc = session.getDocument(new IdRef(routeStepDocId));
        final SSRouteStep routeStep = routeStepDoc.getAdapter(SSRouteStep.class);
        return routeStep.getType();
    }

    private Dossier retrieveDossier(final CoreSession session, final String dossierDocId) {
        final DocumentModel dossierDoc = session.getDocument(new IdRef(dossierDocId));
        return dossierDoc.getAdapter(Dossier.class);
    }
}
