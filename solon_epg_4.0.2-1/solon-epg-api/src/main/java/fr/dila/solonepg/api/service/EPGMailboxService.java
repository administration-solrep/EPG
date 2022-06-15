package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.mailbox.SolonEpgMailbox;
import fr.dila.st.api.service.MailboxService;
import org.nuxeo.ecm.core.api.CoreSession;

public interface EPGMailboxService extends MailboxService {
    Long getNumberOfDossierLinkIds(CoreSession session, SolonEpgMailbox mailBox);

    String getMailboxListQuery(CoreSession session);
}
