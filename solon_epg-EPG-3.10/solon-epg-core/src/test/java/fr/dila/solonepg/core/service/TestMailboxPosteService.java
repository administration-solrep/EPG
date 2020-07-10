package fr.dila.solonepg.core.service;

import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.Access;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.MailboxPosteServiceImpl;

public class TestMailboxPosteService extends SolonEpgRepositoryTestCase {

	private MailboxPosteService	serviceMailboxPosteATester;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		openSession();
		serviceMailboxPosteATester = new MailboxPosteServiceImpl();
	}

	@Override
	public void tearDown() throws Exception {
		closeSession();
		super.tearDown();
	}

	public void testCreatePosteMailbox() {
		try {
			Mailbox mailBox = serviceMailboxPosteATester.createPosteMailbox(session, "poste-12345", "testAHT");
			assertNotNull(mailBox);
			ACP listOfPerms = mailBox.getDocument().getACP();
			assertNotNull(listOfPerms);
		} catch (Exception e) {

			e.printStackTrace();
			fail();
		}
	}

}
