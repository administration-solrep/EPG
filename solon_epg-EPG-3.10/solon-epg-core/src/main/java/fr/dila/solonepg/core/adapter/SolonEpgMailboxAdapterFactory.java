package fr.dila.solonepg.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.cm.mailbox.MailboxConstants;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.core.mailbox.SolonEpgMailboxImpl;

/**
 * Adapteur de document vers SolonEpgMailbox.
 * 
 * @author jtremeaux
 */
public class SolonEpgMailboxAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> arg1) {
		if (!checkDocument(doc)) {
			return null;
		}

		return new SolonEpgMailboxImpl(doc);
	}

	protected Boolean checkDocument(DocumentModel doc) {
		// Vérifie si le document est une Mailbox
		if (!doc.hasFacet(MailboxConstants.MAILBOX_FACET)) {
			return false;
		}

		// Si c'est une mailbox, il doit contenir le schéma reponsesMailbox (sinon c'est une erreur)
		if (!doc.hasSchema(SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA)) {
			throw new CaseManagementRuntimeException("Document should contain schema "
					+ SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA);
		}
		return true;
	}

}
