package fr.dila.solonepg.core.cm.work;

import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_DIRECTION_ATTACHE_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_MINISTERE_ATTACHE_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgAclConstant.RATTACHEMENT_ACL;
import static fr.dila.solonepg.api.util.EpgAclUtil.getDossierRattachementDirAceUsername;
import static fr.dila.solonepg.api.util.EpgAclUtil.getDossierRattachementMinAceUsername;
import static fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl.DEFAULT;
import static org.nuxeo.ecm.core.api.security.SecurityConstants.READ_WRITE;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.work.SolonWork;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.impl.ACLImpl;

public class FixDossierAclRattachementWork extends SolonWork {
    private static final long serialVersionUID = 8964157155762684423L;

    private static final STLogger LOG = STLogFactory.getLog(FixDossierAclRattachementWork.class);

    private final Collection<String> dossierIds;

    public FixDossierAclRattachementWork(List<String> dossierIds) {
        super();
        this.dossierIds = new ArrayList<>(dossierIds);
    }

    @Override
    protected void doWork() {
        openSystemSession();
        LOG.info(DEFAULT, dossierIds + " dossiers à traiter");
        LOG.debug(DEFAULT, "Dossiers à traiter : " + dossierIds);
        QueryHelper
            .getDocuments(session, dossierIds, DOSSIER_MINISTERE_ATTACHE_XPATH, DOSSIER_DIRECTION_ATTACHE_XPATH)
            .forEach(this::setAclRattachement);
    }

    private void setAclRattachement(DocumentModel doc) {
        Dossier dos = Dossier.adapt(doc);
        ACP acp = doc.getACP();
        acp.removeACL(RATTACHEMENT_ACL);
        ACL acl = new ACLImpl(RATTACHEMENT_ACL);
        acl.add(new ACE(getDossierRattachementMinAceUsername(dos.getMinistereAttache()), READ_WRITE));
        acl.add(new ACE(getDossierRattachementDirAceUsername(dos.getDirectionAttache()), READ_WRITE));
        acp.addACL(acl);
        doc.setACP(acp, true);
    }
}
