package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.ss.core.service.SSLockServiceImpl;
import java.util.function.Consumer;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class EpgLockServiceImpl extends SSLockServiceImpl {
    private static final long serialVersionUID = -5322329337078435031L;

    @Override
    public boolean unlockDocUnrestricted(CoreSession session, DocumentModel document) {
        if (document.getType().equals(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE)) {
            Consumer<CoreSession> removeEditingFlagsFunction = superSession ->
                SolonEpgServiceLocator.getDossierService().removeEditingFlagOnFiles(superSession, document);
            CoreInstance.doPrivileged(session, removeEditingFlagsFunction);
        }
        return super.unlockDocUnrestricted(session, document);
    }
}
