package fr.dila.solonmgpp.api.service;

import java.util.List;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface de service pour le fond de dossier d'une fiche de presentation
 *
 * @author asatre
 *
 */
public interface FondDossierService {
    List<DocumentModel> findFileFor(CoreSession session, DocumentModel doc);

    void addFileFor(CoreSession session, DocumentModel doc, Blob blob);

    void removeFileFor(CoreSession session, DocumentModel file);
}
