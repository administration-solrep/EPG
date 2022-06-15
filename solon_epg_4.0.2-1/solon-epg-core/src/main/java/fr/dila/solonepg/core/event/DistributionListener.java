package fr.dila.solonepg.core.event;

import fr.dila.cm.event.CaseManagementEventConstants;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.core.event.AbstractEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;

/**
 * Renseigne les droits du dossier lors de la distribution.
 *
 * @author jtremeaux
 */
public class DistributionListener extends AbstractEventListener {

    public DistributionListener() {
        super();
    }

    @SuppressWarnings("unchecked")
    protected void doHandleEvent(final Event event) {
        EventContext eventCtx = event.getContext();
        Object dossier = eventCtx.getProperty(CaseManagementEventConstants.EVENT_CONTEXT_CASE);
        if (!(dossier instanceof STDossier)) {
            return;
        }
        DocumentModel dossierDoc = ((STDossier) dossier).getDocument();

        Map<String, List<String>> recipients = (Map<String, List<String>>) eventCtx.getProperty(
            CaseManagementEventConstants.EVENT_CONTEXT_INTERNAL_PARTICIPANTS
        );
        if (recipients == null) {
            return;
        }

        // Initialise les ACL de distribution du dossier
        List<String> mailboxIdList = new ArrayList<String>();
        for (Map.Entry<String, List<String>> recipient : recipients.entrySet()) {
            mailboxIdList.addAll(recipient.getValue());
        }
        final CoreSession session = eventCtx.getCoreSession();
        final EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        dossierDistributionService.initDossierDistributionAcl(session, dossierDoc, mailboxIdList);
    }
}
