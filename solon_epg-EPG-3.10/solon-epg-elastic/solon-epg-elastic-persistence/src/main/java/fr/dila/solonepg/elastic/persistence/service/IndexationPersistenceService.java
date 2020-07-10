package fr.dila.solonepg.elastic.persistence.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.persistence.PersistenceProvider;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunCallback;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunVoid;
import org.nuxeo.ecm.core.persistence.PersistenceProviderFactory;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.runtime.transaction.TransactionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.dila.solonepg.elastic.persistence.model.DossierIndexationQuery;
import fr.dila.solonepg.elastic.persistence.model.DossierIndexationStatus;
import fr.dila.solonepg.elastic.persistence.model.IndexationStatus;

public class IndexationPersistenceService extends DefaultComponent implements IIndexationPersistenceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexationPersistenceService.class);

	private PersistenceProvider persistenceProvider;

	@Override
	public List<DossierIndexationQuery> listDossiersAIndexerEnContinu() throws ClientException {
		return getOrCreatePersistenceProvider().run(true, new RunCallback<List<DossierIndexationQuery>>() {
			@Override
			public List<DossierIndexationQuery> runWith(EntityManager em) throws ClientException {
				return doListDossiersAIndexerEnContinu(em);
			}
		});
	}

	@Override
	public List<String> listDossiersAIndexerMassivement() throws ClientException {
		return getOrCreatePersistenceProvider().run(true, new RunCallback<List<String>>() {
			@Override
			public List<String> runWith(EntityManager em) throws ClientException {
				return doListDossiersAIndexerMassivement(em);
			}
		});
	}

	@Override
	public void markIndexationMassive(final Collection<String> dossierIds) throws ClientException {
		// Il est nécessaire de gérer la transaction car le job d'indexation massive ne la gère pas
		try {
			TransactionHelper.startTransaction();
			getOrCreatePersistenceProvider().run(true, new RunVoid() {
				@Override
				public void runWith(EntityManager em) throws ClientException {
					doMarkIndexationMassive(em, dossierIds);
				}
			});
		} finally {
			TransactionHelper.commitOrRollbackTransaction();
		}
	}

	@Override
	public void markIndexationContinueModification(final DocumentModel dossierDocumentModel) throws ClientException {
		// L'ouverture de la transaction est gérée par l'appelant

		getOrCreatePersistenceProvider().run(true, new RunVoid() {
			@Override
			public void runWith(EntityManager em) throws ClientException {
				doMarkIndexationContinue(em, dossierDocumentModel, false);
			}
		});
	}

	@Override
	public void markIndexationContinueSuppression(final DocumentModel dossierDocumentModel) throws ClientException {
		// L'ouverture de la transaction est gérée par l'appelant

		getOrCreatePersistenceProvider().run(true, new RunVoid() {
			@Override
			public void runWith(EntityManager em) throws ClientException {
				doMarkIndexationContinue(em, dossierDocumentModel, true);
			}
		});
	}

	@Override
	public void markTransactionalIndexationContinueSuppression(final String documentId) throws ClientException {
		// Il est nécessaire de gérer la transaction car l'opération ne la gère pas
		try {
			TransactionHelper.startTransaction();
			getOrCreatePersistenceProvider().run(true, new RunVoid() {
				@Override
				public void runWith(EntityManager em) throws ClientException {
					doMarkIndexationContinue(em, documentId, true);
				}
			});
		} finally {
			TransactionHelper.commitOrRollbackTransaction();
		}
	}

	@Override
	public void markIndexationSuccess(String dossierId, boolean isDeletion) {
		markIndexation(dossierId, isDeletion ? IndexationStatus.SUCCESS_DELETE : IndexationStatus.SUCCESS, null);
	}

	@Override
	public void markIndexationSuccessWarning(String dossierId, boolean isDeletion, String message) {
		markIndexation(dossierId, isDeletion ? IndexationStatus.SUCCESS_DELETE : IndexationStatus.SUCCESS_WARNING, message);
	}

	@Override
	public void markIndexationError(String dossierId, String message) {
		markIndexation(dossierId, IndexationStatus.ERROR, message);
	}

	private void markIndexation(final String dossierId, final IndexationStatus status, final String message) {
		try {
			Transaction transaction = null;
			try {
				// on réalise l'opération dans une transaction séparée
				transaction = TransactionHelper.lookupTransactionManager().suspend();
				TransactionHelper.lookupTransactionManager().begin();
				getOrCreatePersistenceProvider().run(true, new RunVoid() {
					@Override
					public void runWith(EntityManager em) throws ClientException {
						markIndexation(em, dossierId, status, message);
					}
				});
			} catch (ClientException ce) {
				LOGGER.error(String.format("Erreur de persistence du résultat d'indexation sur %s", dossierId), ce);
				TransactionHelper.setTransactionRollbackOnly();
			} finally {
				TransactionHelper.commitOrRollbackTransaction();
				if (transaction != null) {
					TransactionHelper.lookupTransactionManager().resume(transaction);
				}
			}
		} catch (NotSupportedException nse) {
			LOGGER.error("Erreur de gestion des transactions", nse);
		} catch (SystemException se) {
			LOGGER.error("Erreur de gestion des transactions", se);
		} catch (NamingException se) {
			LOGGER.error("Erreur de gestion des transactions", se);
		} catch (InvalidTransactionException se) {
			LOGGER.error("Erreur de gestion des transactions", se);
		}
	}

	protected void doMarkIndexationMassive(EntityManager em, Collection<String> dossierIds) {
		Date now = new Date();
		for (String dossierId : dossierIds) {
			DossierIndexationQuery query = em.find(DossierIndexationQuery.class, dossierId);
			if (query == null) {
				query = new DossierIndexationQuery();
				query.setDossierId(dossierId);
			}
			query.setEventDate(now);
			query.setIndexationMassiveDate(now);
			em.persist(query);
		}
		// flush explicite ; si on ne flushe pas et qu'il n'y a pas de transaction
		// on n'a aucun message d'erreur. Appeler explicitement le flush permet de
		// bien provoquer une erreur en l'absence de transaction
		em.flush();
	}

	protected void doMarkIndexationContinue(EntityManager em, DocumentModel dossierDocumentModel, boolean deleted) {
		doMarkIndexationContinue(em, dossierDocumentModel.getId(), deleted);
	}

	protected void doMarkIndexationContinue(EntityManager em, String documentId, boolean deleted) {
		DossierIndexationQuery query = em.find(DossierIndexationQuery.class, documentId);
		if (query == null) {
			query = new DossierIndexationQuery();
			query.setDossierId(documentId);
		}
		Date now = new Date();
		query.setEventDate(now);
		query.setIndexationDate(now);
		query.setDeleted(deleted);
		em.persist(query);
		// flush explicite ; si on ne flushe pas et qu'il n'y a pas de transaction
		// on n'a aucun message d'erreur. Appeler explicitement le flush permet de
		// bien provoquer une erreur en l'absence de transaction
		em.flush();
	}

	protected List<DossierIndexationQuery> doListDossiersAIndexerEnContinu(EntityManager em) {
		return doListDossiersAIndexerByField(em, "indexationDate");
	}

	protected List<String> doListDossiersAIndexerMassivement(EntityManager em) {
		List<String> dossiers = new ArrayList<String>();
		List<DossierIndexationQuery> queries = doListDossiersAIndexerByField(em, "indexationMassiveDate");
		for (DossierIndexationQuery query : queries) {
			dossiers.add(query.getDossierId());
		}
		return dossiers;
	}

	private List<DossierIndexationQuery> doListDossiersAIndexerByField(EntityManager em, String field) {
		// On cherche les demandes d'indexation (query) pour lesquelles il n'y a pas
		// d'indexation réussie postérieure (comparaison lastIndexationDate et field).
		Query query = em.createQuery(String.format("SELECT query "
				+ "FROM %s query LEFT OUTER JOIN query.status as status "
				+ "WHERE (status.lastIndexationDate IS NULL OR status.lastIndexationDate < query.%s) "
				+ "AND query.%s < :indexationDate "
				+ "ORDER BY query.%s ",
				DossierIndexationQuery.ENTITY, field, field, field)
		).setParameter("indexationDate", new Date());
		@SuppressWarnings("unchecked")
		List<DossierIndexationQuery> result = (List<DossierIndexationQuery>) query.getResultList();
		return result;
	}

	protected void markIndexation(EntityManager em, String dossierId, IndexationStatus targetStatus, String message) {
		DossierIndexationStatus status = getOrCreateStatus(em, dossierId);
		if (targetStatus.isSuccess()) {
			status.setLastErrorDate(null);
			status.setLastIndexationDate(new Date());
		} else if (IndexationStatus.ERROR.equals(targetStatus)) {
			status.setLastErrorDate(null);
			// keep lastIndexationDate
		}
		status.setMessage(message);
		status.setStatus(targetStatus);
		em.persist(status);
	}

	private DossierIndexationStatus getOrCreateStatus(EntityManager em, String dossierId) {
		DossierIndexationStatus status = em.find(DossierIndexationStatus.class, dossierId);
		if (status == null) {
			status = new DossierIndexationStatus();
			status.setDossierId(dossierId);
			status.setStatus(IndexationStatus.UNKNOWN);
		}
		return status;
	}

	protected PersistenceProvider getOrCreatePersistenceProvider() {
		if (persistenceProvider == null) {
			synchronized (IndexationPersistenceService.class) {
				if (persistenceProvider == null) {
					activatePersistenceProvider();
				}
			}
		}
		return persistenceProvider;
	}

	protected void activatePersistenceProvider() {
		Thread thread = Thread.currentThread();
		ClassLoader last = thread.getContextClassLoader();
		try {
			thread.setContextClassLoader(PersistenceProvider.class.getClassLoader());
			PersistenceProviderFactory persistenceProviderFactory = Framework
					.getLocalService(PersistenceProviderFactory.class);
			persistenceProvider = persistenceProviderFactory.newProvider("elastic-provider");
			persistenceProvider.openPersistenceUnit();
		} finally {
			thread.setContextClassLoader(last);
		}
	}

	@Override
	public void deactivate(ComponentContext context) throws Exception {
		deactivatePersistenceProvider();
	}

	private void deactivatePersistenceProvider() {
		if (persistenceProvider != null) {
			synchronized (IndexationPersistenceService.class) {
				if (persistenceProvider != null) {
					persistenceProvider.closePersistenceUnit();
					persistenceProvider = null;
				}
			}
		}
	}

}
