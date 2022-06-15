package fr.dila.solonepg.core.service;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.ss.api.service.MailboxPosteService;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.security.ACP;

public class TestMailboxPosteService extends AbstractEPGTest {
    @Inject
    private MailboxPosteService serviceMailboxPosteATester;

    @Test
    public void testCreatePosteMailbox() {
        try {
            Mailbox mailBox = serviceMailboxPosteATester.createPosteMailbox(session, "poste-12345", "testAHT");
            Assert.assertNotNull(mailBox);
            ACP listOfPerms = mailBox.getDocument().getACP();
            Assert.assertNotNull(listOfPerms);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
