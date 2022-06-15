package fr.dila.solonepg.api.service;

import fr.dila.st.api.service.STUserManager;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface SolonEpgUserManager extends STUserManager {
    List<String> getAdministratorIds();

    boolean isExistAndNotActive(String username);

    void deleteUserConsultation(CoreSession session, DocumentModel userDoc);

    boolean canDeleteUser(CoreSession session, String userId);
}
