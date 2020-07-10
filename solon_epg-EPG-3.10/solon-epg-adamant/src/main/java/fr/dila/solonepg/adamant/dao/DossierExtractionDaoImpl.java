package fr.dila.solonepg.adamant.dao;

import java.math.BigDecimal;
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

import fr.dila.solonepg.adamant.model.BordereauPK;
import fr.dila.solonepg.adamant.model.DossierExtractionBordereau;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.adamant.model.ExtractionStatus;
import fr.dila.solonepg.api.cases.Dossier;

/**
 * DAO associé aux objets liés à l'extraction Adamant.
 * 
 * @author tlombard
 */
public class DossierExtractionDaoImpl extends DefaultComponent implements DossierExtractionDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(DossierExtractionDaoImpl.class);

	private PersistenceProvider persistenceProvider;
	
	public DossierExtractionDaoImpl() {
		super();
	}

	@Override
	public DossierExtractionLot createLot() throws ClientException {
		DossierExtractionLot dossierExtractionLot = null;
		try {
			Transaction transaction = null;

			try {
				// on réalise l'opération dans une transaction séparée
				transaction = TransactionHelper.lookupTransactionManager().suspend();
				TransactionHelper.lookupTransactionManager().begin();
				dossierExtractionLot = getOrCreatePersistenceProvider().run(true,
						new RunCallback<DossierExtractionLot>() {
							@Override
							public DossierExtractionLot runWith(EntityManager em) throws ClientException {
								return doCreateLot(em);
							}
						});
			} catch (ClientException ce) {
				LOGGER.error("Erreur de persistence du lot", ce);
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
		return dossierExtractionLot;
	}

	private DossierExtractionLot doCreateLot(EntityManager em) {
		DossierExtractionLot lot = new DossierExtractionLot();
		lot.setCreationDate(new Date());
		lot.setName(assigneNameIncrement(em));
		lot.setStatus(ExtractionStatus.EN_ATTENTE);
		em.persist(lot);
		return lot;
	}

	protected String assigneNameIncrement(EntityManager em) {
		Query query = em.createQuery("SELECT count(*) " + "FROM DOSSIER_EXTRACTION_LOT ");
		int result = Integer.parseInt(query.getSingleResult().toString());
		int nextName = result + 1;
		return String.valueOf(nextName);
	}

	protected PersistenceProvider getOrCreatePersistenceProvider() {
		if (persistenceProvider == null) {
			synchronized (DossierExtractionDaoImpl.class) {
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
			persistenceProvider = persistenceProviderFactory.newProvider("adamant-provider");
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
			synchronized (DossierExtractionDaoImpl.class) {
				if (persistenceProvider != null) {
					persistenceProvider.closePersistenceUnit();
					persistenceProvider = null;
				}
			}
		}
	}

	private List<DossierExtractionLot> doListLotsByStatusSortedByDateAsc(EntityManager em, ExtractionStatus status) {
		Query query = em.createQuery("SELECT lot " + "FROM DOSSIER_EXTRACTION_LOT lot "
				+ "WHERE lot.status = :statusValue " + "ORDER BY lot.creationDate").setParameter("statusValue", status);
		@SuppressWarnings("unchecked")
		List<DossierExtractionLot> result = (List<DossierExtractionLot>) query.getResultList();
		return result;
	}

	@Override
	public List<DossierExtractionLot> listLotsByStatusSortedByDateAsc(final ExtractionStatus status)
			throws ClientException {
		return getOrCreatePersistenceProvider().run(true, new RunCallback<List<DossierExtractionLot>>() {
			@Override
			public List<DossierExtractionLot> runWith(EntityManager em) {
				return doListLotsByStatusSortedByDateAsc(em, status);
			}
		});
	}

	@Override
	public void updateLotStatus(final DossierExtractionLot lot, final ExtractionStatus status) throws ClientException {
		try {
			Transaction transaction = null;
			try {
				// on réalise l'opération dans une transaction séparée
				transaction = TransactionHelper.lookupTransactionManager().suspend();
				TransactionHelper.lookupTransactionManager().begin();
				getOrCreatePersistenceProvider().run(true, new RunVoid() {
					@Override
					public void runWith(EntityManager em) throws ClientException {
						doUpdateLotStatus(em, lot.getId(), status);
					}
				});
			} catch (ClientException ce) {
				LOGGER.error(String.format("Erreur de persistence du lot d'extraction sur %s", lot.getId()), ce);
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

	private void doUpdateLotStatus(EntityManager em, Integer lotId, ExtractionStatus status) throws ClientException {
		DossierExtractionLot lot = em.find(DossierExtractionLot.class, lotId);
		if (lot != null) {
			lot.setStatus(status);
			em.persist(lot);
		} else {
			throw new ClientException("Le lot " + lotId + " ne peut pas être récupéré.");
		}
	}

	private void doDeleteFromLot(EntityManager entityManager, final String dossierId, final Integer lotId) {
		Query query = entityManager.createNativeQuery(
				"UPDATE DOSSIER_SOLON_EPG SET ID_EXTRACTION_LOT=null WHERE ID=:idDossier AND ID_EXTRACTION_LOT=:lotId");
		query.setParameter("idDossier", dossierId);
		query.setParameter("lotId", lotId);
		query.executeUpdate();
	}

	@Override
	public void deleteFromLot(final Dossier dossier, final DossierExtractionLot lot) throws ClientException {
		try {
			Transaction transaction = null;
			try {
				// on réalise l'opération dans une transaction séparée
				transaction = TransactionHelper.lookupTransactionManager().suspend();
				TransactionHelper.lookupTransactionManager().begin();
				getOrCreatePersistenceProvider().run(true, new RunVoid() {
					@Override
					public void runWith(EntityManager entityManager) throws ClientException {
						doDeleteFromLot(entityManager, dossier.getDocument().getId(), lot.getId());
					}
				});
			} catch (ClientException ce) {
				LOGGER.error(String.format("Erreur de persistence du lot d'extraction sur %s", lot.getId()), ce);
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

	private void doSaveBordereau(final DossierExtractionBordereau bordereau,
			EntityManager entityManager) {
		BordereauPK key = bordereau.getBordereauPK();
		DossierExtractionBordereau persisted = entityManager.find(DossierExtractionBordereau.class,
				key);
		if (persisted == null) {
			persisted = new DossierExtractionBordereau();
			persisted.setBordereauPK(key);
		}

		persisted.setEmpreinte(bordereau.getEmpreinte());
		persisted.setExtractionDate(bordereau.getExtractionDate());
		persisted.setMessage(bordereau.getMessage());
		persisted.setNomSip(bordereau.getNomSip());
		persisted.setPoids(bordereau.getPoids());
		persisted.setStatut(bordereau.getStatut());
		persisted.setSuccess(bordereau.getSuccess());
		persisted.setTypeActe(bordereau.getTypeActe());
		persisted.setVersion(bordereau.getVersion());
		persisted.setStatutArchivageAfter(bordereau.getStatutArchivageAfter());

		entityManager.persist(persisted);
	}
	
	@Override
	public void saveBordereau(final DossierExtractionBordereau bordereau) {
		try {
			Transaction transaction = null;
			try {
				// on réalise l'opération dans une transaction séparée
				transaction = TransactionHelper.lookupTransactionManager().suspend();
				TransactionHelper.lookupTransactionManager().begin();
				getOrCreatePersistenceProvider().run(true, new RunVoid() {
					@Override
					public void runWith(EntityManager entityManager) throws ClientException {
						doSaveBordereau(bordereau, entityManager);
					}				
				});
			} catch (ClientException ce) {
				LOGGER.error(
						String.format("Erreur de persistence du bordereau %s", bordereau.getBordereauPK().toString()),
						ce);
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

	@SuppressWarnings("unchecked")
	private Collection<DossierExtractionBordereau> doGetBordereaux(EntityManager em, DossierExtractionLot lot) {
		ArrayList<DossierExtractionBordereau> result = new ArrayList<DossierExtractionBordereau>();
		
		Query query = em.createNativeQuery(
				"SELECT b.ID_DOSSIER, b.ID_EXTRACTION_LOT "
				+ "FROM DOSSIER_EXTRACTION_BORDEREAU b "
				+ "WHERE b.ID_EXTRACTION_LOT=:lotId and b.SUCCESS=1 ORDER BY b.EXTRACTION_DATE")
				.setParameter("lotId", lot.getId());
		for (Object[] pkObj : (List<Object[]>) query.getResultList()) {
			BordereauPK bordereauPk = new BordereauPK((String) pkObj[0], ((BigDecimal) pkObj[1]).intValue());
			result.add(em.find(DossierExtractionBordereau.class,
					bordereauPk));
		}
		
		return result;
	}

	@Override
	public Collection<DossierExtractionBordereau> getBordereaux(final DossierExtractionLot lot) throws ClientException {
		return getOrCreatePersistenceProvider().run(true, new RunCallback<Collection<DossierExtractionBordereau>>() {
			@Override
			public Collection<DossierExtractionBordereau> runWith(EntityManager em) {
				return doGetBordereaux(em, lot);
			}
		});
	}
}
