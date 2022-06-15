package fr.dila.solonepg.core.adapter;

import fr.dila.cm.mailbox.MailboxConstants;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.core.mailbox.SolonEpgMailboxImpl;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Adapteur de document vers SolonEpgMailbox.
 *
 * @author jtremeaux
 */
public class SolonEpgMailboxAdapterFactory implements STDocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentFacet(doc, MailboxConstants.MAILBOX_FACET);
        checkDocumentSchemas(doc, SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA);

        return new SolonEpgMailboxImpl(doc);
    }
}
