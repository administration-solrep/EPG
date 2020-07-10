package fr.dila.solonepg.elastic.persistence.service;

import java.util.Collection;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.elastic.persistence.model.DossierIndexationQuery;

public interface IIndexationPersistenceService {

	List<DossierIndexationQuery> listDossiersAIndexerEnContinu() throws ClientException;

	List<String> listDossiersAIndexerMassivement() throws ClientException;

	void markIndexationContinueModification(DocumentModel dossierDocumentModel) throws ClientException;

	void markIndexationContinueSuppression(DocumentModel dossierDocumentModel) throws ClientException;

	void markIndexationMassive(Collection<String> dossierIds) throws ClientException;

	void markIndexationSuccess(String dossierId, boolean isDeletion);

	void markIndexationSuccessWarning(String dossierId, boolean isDeletion, String message);

	void markIndexationError(String dossierId, String message);

	/**
	 * Attention, cette méthode gère une transaction distincte, à la différence de
	 * {@link IIndexationPersistenceService##markIndexationContinueSuppression(DocumentModel)}
	 * 
	 * @see IIndexationPersistenceService#markIndexationContinueSuppression(DocumentModel)
	 */
	void markTransactionalIndexationContinueSuppression(String documentId) throws ClientException;


}
