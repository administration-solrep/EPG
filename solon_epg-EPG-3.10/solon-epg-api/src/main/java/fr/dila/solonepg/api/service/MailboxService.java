package fr.dila.solonepg.api.service;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.mailbox.SolonEpgMailbox;

public interface MailboxService extends fr.dila.st.api.service.MailboxService {

    Long getNumberOfDossierLinkIds(CoreSession session, SolonEpgMailbox mailBox) throws ClientException;

}
