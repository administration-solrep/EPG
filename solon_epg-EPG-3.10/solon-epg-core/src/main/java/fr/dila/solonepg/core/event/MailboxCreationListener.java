package fr.dila.solonepg.core.event;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import fr.dila.cm.mailbox.MailboxConstants;
import fr.dila.st.core.event.AbstractFilterEventListener;

/**
 * Gestionnaire d'évènements qui permet de traiter les évènements de création des documents de type Mailbox.
 * 
 * @author jtremeaux
 */
public class MailboxCreationListener extends AbstractFilterEventListener<DocumentEventContext> {

	public MailboxCreationListener() {
		super(DocumentEventContext.class);
	}

	@Override
	protected void doHandleEvent(final Event event, final DocumentEventContext context) throws ClientException {
		// Traite uniquement les évènements de création de document
		// Traite uniquement les documents de type Mailbox
		DocumentModel doc = context.getSourceDocument();
		String docType = doc.getType();
		if (!MailboxConstants.MAILBOX_DOCUMENT_TYPE.equals(docType)) {
			return;
		}

		// NOP
	}
}
