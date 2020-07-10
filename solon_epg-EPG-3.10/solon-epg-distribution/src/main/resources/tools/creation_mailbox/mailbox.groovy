import java.util.Collections;
import fr.dila.cm.mailbox.Mailbox;
import org.nuxeo.common.utils.IdUtils;

new File("/tmp/mailbox.csv").splitEachLine("#") { fields ->

        println "SELECT * FROM SolonEpgMailbox WHERE  mlbx:mailbox_id='"+"poste-"+fields[0]+"'";

        def mailboxQuery = Session.query("SELECT * FROM SolonEpgMailbox WHERE  mlbx:mailbox_id='"+"poste-"+fields[0]+"'");

        println mailboxQuery;

        if(mailboxQuery == null || mailboxQuery.isEmpty()) {

                def mailboxModel = Session.createDocumentModel("SolonEpgMailbox");
                def mailbox = mailboxModel.getAdapter(Mailbox.class);
                mailbox.setId("poste-"+fields[0]);
                mailbox.setTitle(fields[1]);
                mailbox.setOwner("system");
                mailbox.setType("generic");

                mailbox.setGroups(Collections.singletonList("poste-"+fields[0]));

                mailboxModel.setPathInfo("/case-management/mailbox-root", IdUtils.generateId(fields[1], "-", true, 24));

                mailboxModel = Session.createDocument(mailboxModel);

                println "created";
                println IdUtils.generateId(fields[1], "-", true, 24);
        } else {
                println "existing";
                println IdUtils.generateId(fields[1], "-", true, 24);
        }
}
