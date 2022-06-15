package fr.dila.solonepg.adamant.dao;

import fr.dila.solonepg.adamant.model.BordereauPK;
import fr.dila.solonepg.adamant.model.DossierExtractionBordereau;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.adamant.model.ExtractionStatus;
import fr.dila.solonepg.adamant.model.RetourVitamBordereauLivraison;
import fr.dila.solonepg.adamant.model.RetourVitamRapportVersement;
import fr.dila.solonepg.api.cases.Dossier;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.persistence.PersistenceProvider;
import org.nuxeo.ecm.core.persistence.PersistenceProviderFactory;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.runtime.transaction.TransactionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAO associé aux objets liés à l'extraction Adamant.
 *
 * @author tlombard
 */
public class DossierExtractionDaoImpl extends DefaultComponent implements DossierExtractionDao {
    private static final String ERREUR_DE_GESTION_DES_TRANSACTIONS = "Erreur de gestion des transactions";

    private static final Logger LOGGER = LoggerFactory.getLogger(DossierExtractionDaoImpl.class);

    private PersistenceProvider persistenceProvider;

    public DossierExtractionDaoImpl() {
        super();
    }

    @Override
    public DossierExtractionLot createLot() {
        DossierExtractionLot dossierExtractionLot = null;
        try {
            Transaction transaction = null;

            try {
                // on réalise l'opération dans une transaction séparée
                transaction = TransactionHelper.lookupTransactionManager().suspend();
                TransactionHelper.lookupTransactionManager().begin();
                dossierExtractionLot = getOrCreatePersistenceProvider().run(true, this::doCreateLot);
            } catch (NuxeoException ce) {
                LOGGER.error("Erreur de persistence du lot", ce);
                TransactionHelper.setTransactionRollbackOnly();
            } finally {
                TransactionHelper.commitOrRollbackTransaction();
                if (transaction != null) {
                    TransactionHelper.lookupTransactionManager().resume(transaction);
                }
            }
        } catch (NotSupportedException | SystemException | InvalidTransactionException | NamingException nse) {
            LOGGER.error(ERREUR_DE_GESTION_DES_TRANSACTIONS, nse);
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
            PersistenceProviderFactory persistenceProviderFactory = ServiceUtil.getRequiredService(
                PersistenceProviderFactory.class
            );
            persistenceProvider = persistenceProviderFactory.newProvider("adamant-provider");
            persistenceProvider.openPersistenceUnit();
        } finally {
            thread.setContextClassLoader(last);
        }
    }

    @Override
    public void deactivate(ComponentContext context) {
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

    @SuppressWarnings("unchecked")
    private List<DossierExtractionLot> doListLotsByStatusSortedByDateAsc(EntityManager em, ExtractionStatus status) {
        Query query = em
            .createQuery(
                "SELECT lot " +
                "FROM DOSSIER_EXTRACTION_LOT lot " +
                "WHERE lot.status = :statusValue " +
                "ORDER BY lot.creationDate"
            )
            .setParameter("statusValue", status);
        return query.getResultList();
    }

    @Override
    public List<DossierExtractionLot> listLotsByStatusSortedByDateAsc(final ExtractionStatus status) {
        return getOrCreatePersistenceProvider()
            .run(
                true,
                em -> {
                    return doListLotsByStatusSortedByDateAsc(em, status);
                }
            );
    }

    @Override
    public void updateLotStatus(final DossierExtractionLot lot, final ExtractionStatus status) {
        try {
            Transaction transaction = null;
            try {
                // on réalise l'opération dans une transaction séparée
                transaction = TransactionHelper.lookupTransactionManager().suspend();
                TransactionHelper.lookupTransactionManager().begin();
                getOrCreatePersistenceProvider()
                    .run(
                        true,
                        em -> {
                            doUpdateLotStatus(em, lot.getId(), status);
                        }
                    );
            } catch (NuxeoException ce) {
                LOGGER.error(String.format("Erreur de persistence du lot d'extraction sur %s", lot.getId()), ce);
                TransactionHelper.setTransactionRollbackOnly();
            } finally {
                TransactionHelper.commitOrRollbackTransaction();
                if (transaction != null) {
                    TransactionHelper.lookupTransactionManager().resume(transaction);
                }
            }
        } catch (NotSupportedException | SystemException | InvalidTransactionException | NamingException nse) {
            LOGGER.error(ERREUR_DE_GESTION_DES_TRANSACTIONS, nse);
        }
    }

    private void doUpdateLotStatus(EntityManager em, Integer lotId, ExtractionStatus status) {
        DossierExtractionLot lot = em.find(DossierExtractionLot.class, lotId);
        if (lot != null) {
            lot.setStatus(status);
            em.persist(lot);
        } else {
            throw new NuxeoException("Le lot " + lotId + " ne peut pas être récupéré.");
        }
    }

    private void doDeleteFromLot(EntityManager entityManager, final String dossierId, final Integer lotId) {
        Query query = entityManager.createNativeQuery(
            "UPDATE DOSSIER_SOLON_EPG SET ID_EXTRACTION_LOT=null WHERE ID=:idDossier AND ID_EXTRACTION_LOT=:lotId"
        );
        query.setParameter("idDossier", dossierId);
        query.setParameter("lotId", lotId);
        query.executeUpdate();
    }

    @Override
    public void deleteFromLot(final Dossier dossier, final DossierExtractionLot lot) {
        try {
            Transaction transaction = null;
            try {
                // on réalise l'opération dans une transaction séparée
                transaction = TransactionHelper.lookupTransactionManager().suspend();
                TransactionHelper.lookupTransactionManager().begin();
                getOrCreatePersistenceProvider()
                    .run(
                        true,
                        em -> {
                            doDeleteFromLot(em, dossier.getDocument().getId(), lot.getId());
                        }
                    );
            } catch (NuxeoException ce) {
                LOGGER.error(String.format("Erreur de persistence du lot d'extraction sur %s", lot.getId()), ce);
                TransactionHelper.setTransactionRollbackOnly();
            } finally {
                TransactionHelper.commitOrRollbackTransaction();
                if (transaction != null) {
                    TransactionHelper.lookupTransactionManager().resume(transaction);
                }
            }
        } catch (NotSupportedException | SystemException | InvalidTransactionException | NamingException nse) {
            LOGGER.error(ERREUR_DE_GESTION_DES_TRANSACTIONS, nse);
        }
    }

    private void doSaveBordereau(final DossierExtractionBordereau bordereau, EntityManager entityManager) {
        BordereauPK key = bordereau.getBordereauPK();
        DossierExtractionBordereau persisted = entityManager.find(DossierExtractionBordereau.class, key);
        if (persisted == null) {
            persisted = new DossierExtractionBordereau();
            persisted.setBordereauPK(key);
        }

        persisted.setEmpreinte(bordereau.getEmpreinte());
        persisted.setExtractionDate(bordereau.getExtractionDate());
        persisted.setIdPaquet(bordereau.getIdPaquet());
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
        saveEntity(
            bordereau,
            (entityManager, entity) -> doSaveBordereau(entity, entityManager),
            bordereau.getBordereauPK().toString(),
            "du bordereau"
        );
    }

    @SuppressWarnings("unchecked")
    private Collection<DossierExtractionBordereau> doGetBordereaux(EntityManager em, DossierExtractionLot lot) {
        Query query = em.createQuery(
            "FROM DOSSIER_EXTRACTION_BORDEREAU b " +
            "WHERE b.bordereauPK.idLot=:lotId and b.success=1 ORDER BY b.extractionDate"
        );
        query.setParameter("lotId", lot.getId());
        return query.getResultList();
    }

    @Override
    public Collection<DossierExtractionBordereau> getBordereaux(final DossierExtractionLot lot) {
        return getOrCreatePersistenceProvider()
            .run(
                true,
                em -> {
                    return doGetBordereaux(em, lot);
                }
            );
    }

    @Override
    public void saveLineBordereauLivraison(final RetourVitamBordereauLivraison bordereau) {
        saveEntity(bordereau, EntityManager::persist, bordereau.getNomSip(), "du bordereau de livraison du SIP");
    }

    @Override
    public void saveLineRapportVersement(final RetourVitamRapportVersement rapport) {
        saveEntity(rapport, EntityManager::persist, rapport.getNomSip(), "du rapport de versement du SIP");
    }

    private <T> void saveEntity(
        T entity,
        BiConsumer<EntityManager, T> persistMethod,
        String entityName,
        String entityLogName
    ) {
        try {
            Transaction transaction = null;
            try {
                // on réalise l'opération dans une transaction séparée
                transaction = TransactionHelper.lookupTransactionManager().suspend();
                TransactionHelper.lookupTransactionManager().begin();
                getOrCreatePersistenceProvider()
                    .run(
                        true,
                        entityManager -> {
                            persistMethod.accept(entityManager, entity);
                        }
                    );
            } catch (NuxeoException ce) {
                LOGGER.error(String.format("Erreur de persistence %s %s", entityLogName, entityName), ce);
                TransactionHelper.setTransactionRollbackOnly();
            } finally {
                TransactionHelper.commitOrRollbackTransaction();
                if (transaction != null) {
                    TransactionHelper.lookupTransactionManager().resume(transaction);
                }
            }
        } catch (NotSupportedException | SystemException | InvalidTransactionException | NamingException nse) {
            LOGGER.error(ERREUR_DE_GESTION_DES_TRANSACTIONS, nse);
        }
    }
}
