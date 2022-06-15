package fr.dila.solonepg.core.event;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

/**
 * Listener de l'evenement de rattachement des dossier à l'espace d'activité normative
 *
 * @author asatre
 */
public class ActiviteNormativeAttachDossierListener implements PostCommitEventListener {
    private static final STLogger LOGGER = STLogFactory.getLog(ActiviteNormativeAttachDossierListener.class);

    @Override
    public void handleEvent(EventBundle events) {
        if (!events.containsEventName(SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_EVENT)) {
            // do not contains interesting event for this listener
            return;
        }
        for (Event event : events) {
            if (event.getName().equals(SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_EVENT)) {
                handleEvent(event);
            }
        }
    }

    protected void handleEvent(Event event) {
        final EventContext ctx = event.getContext();
        final CoreSession session = ctx.getCoreSession();

        final String dossierDocId = (String) ctx.getProperty(
            SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_DOSSIER_DOCID_PARAM
        );
        final Dossier dossier = retrieveDossier(session, dossierDocId);
        if (dossier == null) {
            return;
        }
        LOGGER.debug(STLogEnumImpl.START_EVENT_TEC, "Traitement du rattachement des dossiers");

        // creation des decrets et des mesures de la loi de réference
        SolonEpgServiceLocator.getActiviteNormativeService().attachDecretToLoi(dossier, session);
        session.save();
        // creation des decrets et des loi de ratification de l'ordonnance
        SolonEpgServiceLocator.getActiviteNormativeService().attachDecretToOrdonnance(dossier, session);
        session.save();
        // creation des ordonnances et habilitations
        SolonEpgServiceLocator.getActiviteNormativeService().attachOrdonnanceToLoiHabilitation(dossier, session);
        session.save();
        // creation des directive Européenne
        SolonEpgServiceLocator.getTranspositionDirectiveService().attachDirectiveEuropenneToDossier(dossier, session);
        session.save();
        LOGGER.debug(STLogEnumImpl.END_EVENT_TEC, "Traitement du rattachement des dossiers");
    }

    private Dossier retrieveDossier(final CoreSession session, final String dossierDocId) {
        if (StringUtils.isBlank(dossierDocId)) {
            return null;
        }
        final DocumentModel dossierDoc = session.getDocument(new IdRef(dossierDocId));
        if (dossierDoc == null) {
            return null;
        } else {
            return dossierDoc.getAdapter(Dossier.class);
        }
    }
}
