package fr.dila.solonepg.elastic.batch;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.schema.PrefetchInfo;
import org.nuxeo.ecm.platform.scheduler.core.EventJob;
import org.nuxeo.ecm.platform.ui.web.auth.NuxeoAuthenticationFilter;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.runtime.transaction.TransactionHelper;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.elastic.models.indexing.IndexationDossierStatistics;
import fr.dila.solonepg.elastic.models.indexing.IndexationDossiersBatchStatistics;
import fr.dila.solonepg.elastic.persistence.model.DossierIndexationQuery;
import fr.dila.solonepg.elastic.persistence.service.IIndexationPersistenceService;
import fr.dila.st.api.constant.STConfigConstants;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SessionUtil;

/**
 * Ce batch réalise l'export en XML de l'ensemble des dossiers EPG.
 * 
 * @author tlombard
 */
public class IndexationDossiersBatch extends DefaultComponent implements IIndexationDossiersBatch {

	private static final long EXECUTOR_INDEXATION_TIMEOUT = TimeUnit.HOURS.toMillis(30);
	private static final long EXECUTOR_STOP_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(30);

	/**
	 * Logger formalisé en surcouche du logger apache/log4j
	 */
	private static final STLogger			LOGGER				= STLogFactory.getLog(IndexationDossiersBatch.class);

	/**
	 * Logger géré dans un fichier à part.
	 */
	private static final org.slf4j.Logger	LOGGER_BATCH			= LoggerFactory
																		.getLogger("INDEXATION-BATCH");

	/** prefetch des champs pour les dossiers */
	private final String					prefetchDossier		= Joiner.on(",").join(
		new String[] { "common",
			CaseConstants.CASE_SCHEMA, STSchemaConstant.DUBLINCORE_SCHEMA, STSchemaConstant.NOTE_SCHEMA,
			CaseConstants.DISTRIBUTION_SCHEMA, ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
			DossierSolonEpgConstants.DOSSIER_SCHEMA, RetourDilaConstants.RETOUR_DILA_SCHEMA,
			TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA
	});

	/** composants tiers (cf {@link #activate(ComponentContext)} et {@link #deactivate(ComponentContext)}) */
	private IIndexationService				indexationService;
	private ConfigService					configService;
	private IIndexationPersistenceService	indexationPersistenceService;
	private String							batchLogin;

	/** composants internes */
	private ThreadPoolExecutor				executor;

	/** encapsulation des statistiques (accès via {@link ThreadLocal}, cf {@link IndexationDossiersBatchStatistics#get()} */
	private IndexationDossiersBatchStatistics statistics = IndexationDossiersBatchStatistics.create();

	/**
	 * compteur incrémenté pendant l'indexation, permet d'attendre l'arrêt de l'indexation en cas d'arrêt prématuré,
	 * cf {@link #deactivate(ComponentContext)} et {@link #stopExecutor(ThreadPoolExecutor)}
	 */
	private CountDownLatch					running				= new CountDownLatch(1);

	@Override
	public void activate(ComponentContext context) throws Exception {
		configService = STServiceLocator.getConfigService();
		indexationService = Framework.getService(IIndexationService.class);
		indexationPersistenceService = Framework.getService(IIndexationPersistenceService.class);
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
		batchLogin = null;
		running.countDown();
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
	public void indexationMassive() throws ClientException {
		LoginContext loginContext = null;
		try {
			loginContext = NuxeoAuthenticationFilter.loginAs(batchLogin);
			running.await();
			doIndexationMassive();
		} catch (InterruptedException ie) {
			LOGGER_BATCH.error("Indexation interrompue pendant la phase de démarrage");
		} catch (LoginException le) {
			// équivalent RuntimeException
			throw new RuntimeException("Erreur de login", le);
		} finally {
			if (loginContext != null) {
				try {
					loginContext.logout();
				} catch (LoginException le) {
					// équivalent RuntimeException
					throw new RuntimeException("Erreur de logout", le);
				}
			}
			running.countDown();
		}
	}

	/**
	 * Le EventJob gère le login et une ouverture de transaction. On ferme la transaction pour
	 * la gérer localement. On garde le login car il ne consomme pas de ressources.
	 */
	@Override
	public void indexationContinue(Event event) {
		// manage transaction locally
		TransactionHelper.commitOrRollbackTransaction();
		
		doIndexationContinue();
		
		// démarrage d'une transaction qui va être fermée par le EventJob
		TransactionHelper.startTransaction();
	}

	@Override
	public boolean isIndexationContinueEnabled() {
		return configService.getBooleanValue(SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_CONTINUE_ENABLED);
	}

	private void doIndexationContinue() {
		List<DossierIndexationQuery> indexQueries;
		try {
			indexQueries = indexationPersistenceService.listDossiersAIndexerEnContinu();
		} catch (ClientException e) {
			throw new RuntimeException("Erreur de récupération des dossiers à indexer");
		}
		
		// récupération des timings de manière à faire des pauses quand il n'y a rien à traiter
		// et de manière à arrêter le traitement toutes les 2 minutes (qui est automatiquement
		// relancé par le job quartz).
		long start = System.currentTimeMillis();
		long end = start + TimeUnit.MINUTES.toMillis(2) + TimeUnit.SECONDS.toMillis(10);
		long startPreviousLoop = 0;
		long startLoop = start;
		long minimumLoopTime = 200;
		
		// boucle continue, qui fait des pauses si la boucle fait moins de 200 ms.
		// on sort au bout de 2 minutes ou en cas d'interruption.
		while (true) {
			if (startLoop > end) {
				break;
			}
			
			// si on boucle en moins de 200ms, on fait une pause
			if (startLoop - startPreviousLoop < minimumLoopTime) {
				try {
					Thread.sleep(minimumLoopTime - (startLoop - startPreviousLoop));
				} catch (InterruptedException e) {
					// si on est interrompu, on stoppe les traitements
					// si le serveur n'est pas arrêté, c'est au scheduler de relancer la tâche
					LOGGER_BATCH.warn("Interruption de l'indexation continue", e);
					Thread.currentThread().interrupt();
					return;
				}
			}
			if (Thread.currentThread().isInterrupted()) {
				LOGGER_BATCH.warn("Interruption de l'indexation continue");
				return;
			}
			
			// si liste épuisée, on recharge les éléments
			if (indexQueries.isEmpty()) {
				try {
					indexQueries = indexationPersistenceService.listDossiersAIndexerEnContinu();
				} catch (ClientException e) {
					throw new RuntimeException("Erreur de récupération des dossiers à indexer");
				}
			}
			// si rien à indexer, on boucle
			if (indexQueries.isEmpty()) {
				continue;
			}
			
			// traitement d'un élément
			DossierIndexationQuery indexQuery = indexQueries.remove(0);
			if (indexQuery.isDeleted()) {
				LOGGER_BATCH.info(String.format("Désindexation du document %s", indexQuery.getDossierId()));
				indexationService.doUnindexDossier(indexQuery.getDossierId());
			} else {
				LOGGER_BATCH.info(String.format("Indexation du document %s", indexQuery.getDossierId()));
				indexationService.doIndexDossier(indexQuery.getDossierId());
			}
			
			startPreviousLoop = startLoop;
			startLoop = System.currentTimeMillis();
		}
	}

	private void stopExecutor(ThreadPoolExecutor ex) {
		while (true) {
			ex.shutdownNow();
			try {
				if (! ex.awaitTermination(EXECUTOR_STOP_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
					LOGGER_BATCH.error("Thread principal de l'indexation en attente de l'arrêt de l'executor");
				}
				break;
			} catch (InterruptedException e) {
				LOGGER_BATCH.error("Thread principal de l'indexation interrompu lors de l'attente de l'arrêt");
				Thread.currentThread().interrupt();
			}
		}
	}

	private void doIndexationMassive() throws ClientException {
		LOGGER.info(EpgLogEnumImpl.INIT_B_EXPORT_DOSSIERS_TEC);
		// récupération des éléments à traiter
		List<String> ids = indexationPersistenceService.listDossiersAIndexerMassivement();

		// découpage en lots
		int queryLimit = getQueryLimit();
		List<List<String>> partitions = Lists.partition(ids, queryLimit);
		if (partitions.size() == 0) {
			LOGGER_BATCH.warn("Arrêt prématuré du processus d'indexation ; aucun élément à indexer");
			return;
		}

		// initialisation de l'objet de statistiques
		statistics.reset();
		statistics.start(ids.size(), queryLimit);
		BlockingQueue<Runnable> runnables = new LinkedBlockingQueue<Runnable>();

		// initialisation de l'executor et alimentation
		int poolSize = getBatchThreadPoolSize();
		executor = new ThreadPoolExecutor(poolSize, poolSize, 10000, TimeUnit.MILLISECONDS, runnables);
		executor.setThreadFactory(new IndexationThreadFactory());
		executor.prestartAllCoreThreads();
		List<Future<Boolean>> futures = Lists.newArrayList();
		for (List<String> lot : partitions) {
			Future<Boolean> future = executor.submit(new LotRunnable(lot));
			futures.add(future);
		}
		executor.shutdown();

		// attente de la fin d'indexation, d'un arrêt provoqué ou du timeout d'indexation
		while (true) {
			try {
				if (! executor.awaitTermination(EXECUTOR_INDEXATION_TIMEOUT, TimeUnit.MILLISECONDS)) {
					LOGGER_BATCH.error(String.format("Arrêt forcé de l'indexation après %d minutes",
							TimeUnit.MILLISECONDS.toMinutes(statistics.getStartTimeMillis() - System.currentTimeMillis())));
					stopExecutor(executor);
				}
				break;
			} catch (InterruptedException e) {
				LOGGER_BATCH.error("Thread principal de l'indexation interrompu lors de l'attente de l'arrêt");
			}
		}
		// logging état de l'indexation (nombre de lots traités)
		doLogFutures(futures);

	}

	/**
	 * Message de log de fin d'une indexation, indiquant en particulier le nombre de lots traités
	 */
	private void doLogFutures(List<Future<Boolean>> futures) {
		int nombreLots = futures.size();
		int nombreLotsTermines = 0;
		int nombreLotsAbandonnes = 0;
		int nombreLotsInterrompus = 0;
		int nombreLotsException = 0;
		for (Future<Boolean> future : futures) {
			if (future.isDone()) {
				try {
					if (future.get()) {
						nombreLotsTermines++;
					} else {
						nombreLotsInterrompus++;
					}
				} catch (ExecutionException ee) {
					LOGGER_BATCH.error("Exception sur un lot d'indexation", ee);
					nombreLotsException++;
				} catch (InterruptedException ie) {
					nombreLotsException++;
				} catch (CancellationException ce) {
					nombreLotsAbandonnes++;
				}
			} else {
				nombreLotsAbandonnes++;
			}
		}
		long end = System.currentTimeMillis();
		LOGGER_BATCH.info(String.format(
				"%d dossiers exportés en %d secondes",
				statistics.getDossier(),
				TimeUnit.MILLISECONDS.toSeconds(end - statistics.getStartTimeMillis())));
		LOGGER_BATCH.info(String.format(
				"lots - total: %d - terminés: %d - abandonnés: %d - interrompus: %d - exception: %d",
				nombreLots, nombreLotsTermines, nombreLotsAbandonnes, nombreLotsInterrompus, nombreLotsException
		));
	}

	/**
	 * Traitement d'un lot ; initialisation du contexte thread (session, statistiques),
	 * récupération des données ( {@link DocumentModel} ), traitement dossier par dossier,
	 * et nettoyage du contexte.
	 * 
	 * Pour chaque lot, on ouvre un contexte login, une transaction et une session qu'on ferme.
	 */
	private boolean doReindexLot(List<String> lot) {
		CoreSession lotSession = null;
		LoginContext loginContext = null;
		long startTick = System.currentTimeMillis();
		try {
			// contexte statistiques
			IndexationDossiersBatchStatistics.set(statistics);
			
			// contexte session + récupération des données dossier
			Exception initError = null;
			DocumentModelList documentModelList = null;
			try {
				// transaction et loginContext fermés dans le finally deux blocs au-dessus
				loginContext = NuxeoAuthenticationFilter.loginAs(batchLogin);
				TransactionHelper.startTransaction();
				lotSession = SessionUtil.getCoreSession();
				PrefetchInfo prefetch = new PrefetchInfo(prefetchDossier);
				documentModelList = lotSession.getDocuments(lot, prefetch);
			} catch (ClientException e) {
				initError = e;
			} catch (LoginException e) {
				initError = e;
			}
			
			// interruption et mise à jour des statistiques si erreur d'initialisation
			if (initError != null) {
				throw new IllegalStateException(String.format(
						"Erreur de préparation d'un thread d'indexation; %d dossiers non indexés",
						lot.size()), initError);
			}
			if (documentModelList == null) {
				// ne peut pas arriver
				throw new IllegalStateException("documentModelList null");
			}

			// traitement dossier par dossier
			for (DocumentModel documentModel : documentModelList) {
				// sortie rapide si thread interrompu
				if (Thread.interrupted()) {
					LOGGER_BATCH.warn("Interruption d'un lot d'indexation; arrêt du traitement du lot");
					// restore interrupt flag
					Thread.currentThread().interrupt();
					return false;
				}

				// Indexation du dossier
				IndexationDossiersBatchStatistics.dossier();
				IndexationDossierStatistics s = indexationService.doIndexDossier(lotSession, documentModel.getId(), false);
				s.updateDossiersBatchStatistics();
				if (! s.hasAnyError()) {
					IndexationDossiersBatchStatistics.dossierSuccess();
				}
			}

			// logging du lot
			doLogLot(startTick, documentModelList.size());
			return true;
		} catch (RuntimeException e) {
			LOGGER_BATCH.warn("Interruption d'un lot par une exception non attendue", e);
			return false;
		} finally {
			IndexationDossiersBatchStatistics.unset();
			if (lotSession != null) {
				SessionUtil.close(lotSession);
			}
			TransactionHelper.commitOrRollbackTransaction();
			if (loginContext != null) {
				try {
					loginContext.logout();
				} catch (LoginException e) {
					throw new IllegalStateException("Erreur de logout d'un thread d'indexation", e);
				}
			}
		}
	}

	/**
	 * Logging sur l'indexation d'un lot
	 */
	private void doLogLot(long startTick, int lotSize) {
		long stopTick = System.currentTimeMillis();
		long totalElapsedInMillis = stopTick - IndexationDossiersBatchStatistics.get().getStartTimeMillis();
		long lotElapsedInMillis = stopTick - startTick;
		int nDossier = IndexationDossiersBatchStatistics.get().getDossier();
		LOGGER_BATCH.info(String.format("Export des dossiers: %d/%d - %d/%d s. - %.2f/s. (lot) - %.2f/s. (global)",
				nDossier, IndexationDossiersBatchStatistics.get().getDossierAIndexer(), // nombre de dossiers importés / total
				TimeUnit.MILLISECONDS.toSeconds(lotElapsedInMillis), // durée du lot
				TimeUnit.MILLISECONDS.toSeconds(totalElapsedInMillis), // durée totale
				(float) lotSize / lotElapsedInMillis * 1000, // vitesse du lot
				(float) nDossier / totalElapsedInMillis * 1000)); // vitesse moyenne
		IndexationDossiersBatchStatistics.log(LOGGER_BATCH);
	}

	/**
	 * Récupération de la taille du {@link ThreadPoolExecutor}
	 */
	private int getBatchThreadPoolSize() {
		return configService.getIntegerValue(SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_THREAD_POOL_SIZE);
	}

	/**
	 * Récupération du nombre total de dossiers récupérés sur une requête Nuxeo. Si ce nombre est inférieur à
	 * getBatchLimit(), on réalise plusieurs requêtes lors d'un même batch.
	 * 
	 * @return un int compris entre 0
	 */
	private int getQueryLimit() {
		try {
			return Integer.parseInt(configService.getValue(
					SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_LIMITE_REQUETE));
		} catch (Exception e) {
			LOGGER_BATCH.error("Erreur de récupération {}",
					SolonEpgConfigConstant.SOLONEPG_BATCH_INDEXATION_DOSSIERS_LIMITE_REQUETE, e);
		}
		return Integer.MAX_VALUE; // Pas de limite
	}

	private class LotRunnable implements Callable<Boolean> {

		private final List<String>	ids;

		public LotRunnable(List<String> ids) {
			this.ids = ids;
		}

		@Override
		public Boolean call() {
			return doReindexLot(ids);
		}
	}

	/**
	 * {@link ThreadFactory} qui renomme les threads pour pouvoir les identifier
	 * (indexation-*-thread-* au lieu de pool-*-thread-*).
	 */
	private class IndexationThreadFactory implements ThreadFactory {
		private ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
		@Override
		public Thread newThread(Runnable r) {
			Thread t = defaultThreadFactory.newThread(r);
			t.setName(t.getName().replaceFirst("^pool", "indexation"));
			return t;
		}
	}
}
