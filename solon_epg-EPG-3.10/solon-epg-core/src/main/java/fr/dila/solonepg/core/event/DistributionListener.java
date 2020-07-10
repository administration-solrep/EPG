package fr.dila.solonepg.core.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;

import fr.dila.cm.cases.Case;
import fr.dila.cm.event.CaseManagementEventConstants;
import fr.dila.solonepg.api.service.DossierDistributionService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.event.AbstractEventListener;

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
	protected void doHandleEvent(final Event event) throws ClientException {
		EventContext eventCtx = event.getContext();
		Object dossier = eventCtx.getProperty(CaseManagementEventConstants.EVENT_CONTEXT_CASE);
		if (!(dossier instanceof Case)) {
			return;
		}
		DocumentModel dossierDoc = ((Case) dossier).getDocument();

		Map<String, List<String>> recipients = (Map<String, List<String>>) eventCtx
				.getProperty(CaseManagementEventConstants.EVENT_CONTEXT_INTERNAL_PARTICIPANTS);
		if (recipients == null) {
			return;
		}

		// Initialise les ACL de distribution du dossier
		List<String> mailboxIdList = new ArrayList<String>();
		for (Map.Entry<String, List<String>> recipient : recipients.entrySet()) {
			mailboxIdList.addAll(recipient.getValue());
		}
		final CoreSession session = eventCtx.getCoreSession();
		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();
		dossierDistributionService.initDossierDistributionAcl(session, dossierDoc, mailboxIdList);
	}
}
