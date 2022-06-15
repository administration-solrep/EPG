package fr.dila.solonmgpp.core.operation;

import static fr.dila.solonmgpp.api.constant.MgppDocTypeConstants.MODELE_COURRIER_ROOT_TYPE;
import static fr.dila.solonmgpp.api.constant.ModeleCourrierConstants.MODELE_COURRIER_ROOT_NAME;
import static fr.dila.solonmgpp.api.constant.ModeleCourrierConstants.MODELE_COURRIER_ROOT_PATH;
import static fr.dila.st.api.constant.STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME;
import static fr.dila.st.api.constant.STConstant.CASE_MANAGEMENT_PATH;
import static org.nuxeo.ecm.core.api.security.SecurityConstants.READ_WRITE;

import fr.dila.st.core.operation.STVersion;
import fr.dila.st.core.util.ObjectHelper;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.impl.ACPImpl;

@Operation(
    id = CreateModeleCourrierRootOperation.ID,
    category = Constants.CAT_DOCUMENT,
    label = "CreateModeleCourrierRootOperation",
    description = "Création du répertoire racine pour stocker les modèles de courrier"
)
@STVersion(version = "4.0.0")
public class CreateModeleCourrierRootOperation {
    public static final String ID = "Modele.Courrier.Create";

    @Context
    private CoreSession session;

    @OperationMethod
    public void run() {
        if (session.exists(new PathRef(MODELE_COURRIER_ROOT_PATH))) {
            return;
        }

        DocumentModel doc = session.createDocumentModel(
            CASE_MANAGEMENT_PATH,
            MODELE_COURRIER_ROOT_NAME,
            MODELE_COURRIER_ROOT_TYPE
        );
        doc = session.createDocument(doc);

        session.getACP(doc.getRef());
        ACP acp = ObjectHelper.requireNonNullElseGet(session.getACP(doc.getRef()), ACPImpl::new);
        ACL acl = acp.getOrCreateACL(ACL.LOCAL_ACL);
        acl.add(ACE.builder(ADMIN_FONCTIONNEL_GROUP_NAME, READ_WRITE).build());
        acp.addACL(acl);
        session.setACP(doc.getRef(), acp, true);
    }
}
