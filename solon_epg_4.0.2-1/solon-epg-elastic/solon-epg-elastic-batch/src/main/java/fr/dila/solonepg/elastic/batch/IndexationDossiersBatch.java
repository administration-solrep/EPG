package fr.dila.solonepg.elastic.batch;

import com.google.common.collect.Lists;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.elastic.batch.work.IndexationWork;
import fr.dila.solonepg.elastic.persistence.service.IIndexationPersistenceService;
import fr.dila.st.api.constant.STConfigConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.scheduler.EventJob;
import org.nuxeo.ecm.core.work.api.WorkManager;
import org.nuxeo.ecm.platform.ui.web.auth.NuxeoAuthenticationFilter;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.runtime.transaction.TransactionHelper;
import org.slf4j.LoggerFactory;

/**
 * Ce batch réalise l'export en XML de l'ensemble des dossiers EPG.
 *
 * @author tlombard
 */
public class IndexationDossiersBatch extends DefaultComponent implements IIndexationDossiersBatch {
    private static final long EXECUTOR_STOP_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(30);

    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(IndexationDossiersBatch.class);

    /**
     * Logger géré dans un fichier à part.
     */
    private static final org.slf4j.Logger LOGGER_BATCH = LoggerFactory.getLogger("INDEXATION-BATCH");

    private static final int TIMEOUT_NB_HOURS = 30;

    /** composants tiers (cf {@link #activate(ComponentContext)} et {@link #deactivate(ComponentContext)}) */
    private ConfigService configService;
    private IIndexationPersistenceService indexationPersistenceService;
    private IIndexationService indexationService;
    private String batchLogin;

    /** composants internes */
    private ThreadPoolExecutor executor;

    /**
     * compteur incrémenté pendant l'indexation, permet d'attendre l'arrêt de l'indexation en cas d'arrêt prématuré,
     * cf {@link #deactivate(ComponentContext)} et {@link #stopExecutor(ThreadPoolExecutor)}
     */
    private CountDownLatch running = new CountDownLatch(1);

    @Override
    public void activate(ComponentContext context) {
        configService = STServiceLocator.getConfigService();
        indexationPersistenceService = ServiceUtil.getRequiredService(IIndexationPersistenceService.class);
        indexationService = ServiceUtil.getRequiredService(IIndexationService.class);
        if (executor != null) {
            throw new IllegalStateException("executor doit être null à l'initialisation");
        }
        batchLogin = configService.getValue(STConfigConstants.NUXEO_BATCH_USER);
        running.countDown();
    }

    /**
     * Libération de toutes les ressources, en particulier, arrêt de l'executor et attente de la fin de l'indexation
     */
    @Override
    public void deactivate(ComponentContext context) {
        if (executor != null) {
            stopExecutor(executor);
            while (true) {
                try {
                    running.await();
                    break;
                } catch (InterruptedException ie) {
                    LOGGER_BATCH.warn("Attente de la fin du processus d'indexation après arrêt de l'executor");
                }
            }
        }
        configService = null;
        indexationPersistenceService = null;
        indexationService = null;
        batchLogin = null;
        running.countDown();
    }

    @Override
    public void indexationContinue() {
        indexation(() -> indexationPersistenceService.listDossiersAIndexerEnContinu(false), false);
        indexation(() -> indexationPersistenceService.listDossiersAIndexerEnContinu(true), true);
    }

    /**
     * Un seul accès concurrent, géré par {@link #running}
     *
     * Lance une réindexation globale. Cette indexation se termine soit en cas
     * de traitement exhaustif, soit si le composant est désactivé, soit si l'indexation dépasse le timeout
     * maximum autorisé.
     *
     * Le login et une transaction sont gérés par le {@link EventJob}. On ferme la transaction et on conserve le login.
     */
    @Override
    public void indexationMassive() {
        indexation(indexationPersistenceService::listDossiersAIndexerMassivement, false);
    }

    /**
     * Un seul accès concurrent, géré par {@link #running}
     *
     * Lance une réindexation globale. Cette indexation se termine soit en cas
     * de traitement exhaustif, soit si le composant est désactivé, soit si l'indexation dépasse le timeout
     * maximum autorisé.
     *
     * Le login et une transaction sont gérés par le {@link EventJob}. On ferme la transaction et on conserve le login.
     */
    private void indexation(Supplier<List<String>> supplierIds, boolean delete) {
        LoginContext loginContext = null;
        try {
            loginContext = NuxeoAuthenticationFilter.loginAs(batchLogin);
            running.await();
            doIndexation(supplierIds, delete);
        } catch (InterruptedException ie) {
            LOGGER_BATCH.error("Indexation interrompue pendant la phase de démarrage");
        } catch (LoginException le) {
            // équivalent RuntimeException
            throw new NuxeoException("Erreur de login", le);
        } finally {
            if (loginContext != null) {
                try {
                    loginContext.logout();
                } catch (LoginException le) {
                    // équivalent RuntimeException
                    throw new NuxeoException("Erreur de logout", le);
                }
            }
            running.countDown();
        }
    }

    private void stopExecutor(ThreadPoolExecutor ex) {
        while (true) {
            ex.shutdownNow();
            try {
                if (!ex.awaitTermination(EXECUTOR_STOP_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                    LOGGER_BATCH.error("Thread principal de l'indexation en attente de l'arrêt de l'executor");
                }
                break;
            } catch (InterruptedException e) {
                LOGGER_BATCH.error("Thread principal de l'indexation interrompu lors de l'attente de l'arrêt");
                Thread.currentThread().interrupt();
            }
        }
    }

    private void doIndexation(Supplier<List<String>> supplierIds, boolean delete) throws InterruptedException {
        LOGGER.info(EpgLogEnumImpl.INIT_B_EXPORT_DOSSIERS_TEC);
        // récupération des éléments à traiter
        List<String> ids = supplierIds.get();

        // Début d'un nouvelle transaction pour éviter que les requêtes
        // précédentes interfèrent avec les workers d'indexation
        TransactionHelper.commitOrRollbackTransaction();
        TransactionHelper.startTransaction();

        // découpage en lots
        int queryLimit = indexationService.getQueryLimit();
        List<List<String>> partitions = Lists.partition(ids, queryLimit);
        if (CollectionUtils.isEmpty(partitions)) {
            LOGGER_BATCH.warn("Arrêt prématuré du processus d'indexation ; aucun élément à indexer");
            return;
        }

        // initialisation de l'objet de statistiques
        //statistics.reset();
        //statistics.start(ids.size(), queryLimit);

        //StreamWorkManager
        WorkManager workManager = Framework.getService(WorkManager.class);

        for (List<String> lot : partitions) {
            workManager.schedule(new IndexationWork(lot, batchLogin, delete));
        }
        //        executor.shutdown();

        try {
            workManager.awaitCompletion(IndexationWork.QUEUE_ID, TIMEOUT_NB_HOURS, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            LOGGER_BATCH.error("Thread principal de l'indexation interrompu lors de l'attente de l'arrêt");
            throw e;
        } finally {
            //doLogFutures(futures);
        }
        // logging état de l'indexation (nombre de lots traités)
        //        doLogFutures(futures);
    }
}
