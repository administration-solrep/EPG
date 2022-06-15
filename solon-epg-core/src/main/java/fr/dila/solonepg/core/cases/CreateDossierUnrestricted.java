package fr.dila.solonepg.core.cases;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.cm.security.CaseManagementSecurityConstants;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.SecurityConstants;

/**
 * Création d'un dossier et initialisation des ACP.
 *
 * @author arolin
 */
public class CreateDossierUnrestricted extends UnrestrictedSessionRunner {
    protected final String parentPath;

    protected DocumentModel caseDoc;

    protected Mailbox mailBox;

    public CreateDossierUnrestricted(CoreSession session, DocumentModel caseDoc, String parentPath, Mailbox mailBox) {
        super(session);
        this.caseDoc = caseDoc;
        this.mailBox = mailBox;
        this.parentPath = parentPath;
    }

    @Override
    public void run() {
        String caseTitle = DublincoreSchemaUtils.getTitle(caseDoc);
        caseDoc.setPathInfo(parentPath, caseTitle);
        caseDoc = session.createDocument(caseDoc);

        ACP acp = caseDoc.getACP();

        // Donne le droit de lecture / écriture sur la mailbox utilisateur du propriétaire des dossiers
        ACL acl = acp.getOrCreateACL(CaseManagementSecurityConstants.ACL_MAILBOX_PREFIX);
        acl.add(
            new ACE(
                CaseManagementSecurityConstants.MAILBOX_PREFIX + mailBox.getId(),
                SecurityConstants.READ_WRITE,
                true
            )
        );

        // Donne le droit de lecture / écriture au créateur des dossiers
        acl.add(new ACE(SolonEpgBaseFunctionConstant.DOSSIER_CREATOR, SecurityConstants.READ_WRITE, true));

        acp.addACL(acl);
        session.save();
        session.setACP(caseDoc.getRef(), acp, true);
        caseDoc.detach(true);
    }

    public Dossier getDossier() {
        return caseDoc.getAdapter(Dossier.class);
    }
}
