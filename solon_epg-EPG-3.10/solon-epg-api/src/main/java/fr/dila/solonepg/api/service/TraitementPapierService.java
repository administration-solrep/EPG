package fr.dila.solonepg.api.service;

import java.io.Serializable;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.st.api.service.LogDocumentUpdateService;

public interface TraitementPapierService extends LogDocumentUpdateService, Serializable {

    void saveTraitementPapier(CoreSession session, DocumentModel dossierDoc) throws ClientException;

}
