package fr.dila.solonepg.elastic.persistence.service;

import java.util.Collection;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface IIndexationPersistenceService {
    List<String> listDossiersAIndexerEnContinu(boolean deleted);

    List<String> listDossiersAIndexerMassivement();

    void markIndexationContinueModification(String documentId);

    void markIndexationContinueSuppression(String documentId);

    void markIndexationMassive(Collection<String> dossierIds);

    void markIndexationSuccess(String dossierId, boolean isDeletion);

    void markIndexationSuccessWarning(String dossierId, boolean isDeletion, String message);

    void markIndexationError(String dossierId, String message);

    /**
     * Attention, cette méthode gère une transaction distincte, à la différence de
     * {@link IIndexationPersistenceService##markIndexationContinueSuppression(DocumentModel)}
     *
     * @see IIndexationPersistenceService#markIndexationContinueSuppression(DocumentModel)
     */
    void markTransactionalIndexationContinueSuppression(String documentId);
}
