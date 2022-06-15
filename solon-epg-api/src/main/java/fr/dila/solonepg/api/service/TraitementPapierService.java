package fr.dila.solonepg.api.service;

import fr.dila.st.api.service.LogDocumentUpdateService;
import java.io.Serializable;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface TraitementPapierService extends LogDocumentUpdateService, Serializable {
    void saveTraitementPapier(CoreSession session, DocumentModel dossierDoc);
}
