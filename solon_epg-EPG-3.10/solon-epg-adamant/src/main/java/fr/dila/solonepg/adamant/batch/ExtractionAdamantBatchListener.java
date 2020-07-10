package fr.dila.solonepg.adamant.batch;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.adamant.SolonEpgAdamantConstant;
import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.adamant.model.DossierExtractionBordereau;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.adamant.model.ExtractionStatus;
import fr.dila.solonepg.adamant.service.DossierExtractionService;
import fr.dila.solonepg.adamant.service.ExtractionLotService;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ParametrageAdamantService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.api.service.SuiviBatchService;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Batch nocturne d'extraction des dossiers pour Adamant.
 * 
 * @author tlombard
 *
 */
public class ExtractionAdamantBatchListener extends AbstractBatchEventListener {

	private static final STLogger LOGGER = STLogFactory.getLog(ExtractionAdamantBatchListener.class);
	
	private static final Log ARCHIVAGE_LOGGER = LogFactory.getLog(SolonEpgAdamantConstant.ARCHIVAGE_LOG);

	/** Nombre maximal de NORs de dossiers en erreur d'extraction à afficher par ligne du suivi du batch. */
	private Integer maxErreursParLigneSuivi;

	/** Nombre de dossiers traités sur cette occurrence de batch. */
	private int nbDossiersTraites;

	/** Lot de dossiers en cours d'extraction. */
	private DossierExtractionLot currentLot;

	/** Dossier en cours d'extraction. */
	private Dossier currentDossier;

	public ExtractionAdamantBatchListener() {
		super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_EXTRACTION_ADAMANT);
	}

	/** Nombre de dossiers dont l'extraction est réussie dans le lot en cours d'extraction. */
	private int nbReussisCurrentLot;

	/** Liste des numéros NOR des dossiers dont l'extraction a échoué dans le lot en cours d'extraction. */
	private ArrayList<String> erreursCurrentLotNorList;

	/** Temps de début de traitement du lot en cours d'extraction en millisecondes. */
	private long startTimeCurrentLot;

	/**
	 * On cloture les éventuelles transactions ouvertes. Ensuite, pour chaque
	 * dossier à traiter, on démarre une transaction qu'on
	 * committera/rollbackera à la fin du traitement. A la toute fin de la
	 * boucle, une transaction aura été démarrée.
	 */
	@Override
	protected void processEvent(CoreSession session, Event event) throws ClientException {
		LOGGER.info(session, EpgLogEnumImpl.INIT_B_EXTRACTION_ADAMANT_TEC);

		TransactionHelper.commitOrRollbackTransaction();
		TransactionHelper.startTransaction();
		
		nbDossiersTraites = 0;
		nbReussisCurrentLot = 0;
		erreursCurrentLotNorList = new ArrayList<String>();
		startTimeCurrentLot = Calendar.getInstance().getTimeInMillis();

		selectLotAndDossierATraiter(session);

		while (continueBatchExecution(session)) {
			extractCurrentDossier(session);
			
			selectLotAndDossierATraiter(session);
			
			TransactionHelper.commitOrRollbackTransaction();
			TransactionHelper.startTransaction();
		}
		
		createBatchResultForCurrentLot();
		LOGGER.info(session, EpgLogEnumImpl.END_B_EXTRACTION_ADAMANT_TEC);
	}

	private Map<String, String> resourceBundleToMap(ResourceBundle bundle) {
		Map<String, String> map = new HashMap<String, String>();
		
		for(String key : bundle.keySet()) {
			map.put(key, bundle.getString(key));
		}
		
		return map;
	}
	
	/**
	 * Extraction du dossier courant.
	 * 
	 * @throws ClientException
	 */
	private void extractCurrentDossier(CoreSession session) throws ClientException {
		LOGGER.info(EpgLogEnumImpl.PROCESS_B_EXTRACTION_ADAMANT_TEC,
				"Traitement en cours: lot " + currentLot.getName() + ", dossier " + currentDossier.getNumeroNor());

		ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.FRENCH);
		Map<String, String> resourceMap = resourceBundleToMap(bundle);
		
		String statutArchivage = null;
		
		DossierExtractionBordereau bordereau = SolonEpgAdamantServiceLocator.getBordereauService()
				.initBordereau(currentDossier, currentLot);
		
		final DossierExtractionService dossierExtractionService = SolonEpgAdamantServiceLocator.getDossierExtractionService();
		
		try {
			dossierExtractionService.checkFile(currentDossier.getDocument(), session);
			dossierExtractionService.genererDossierArchive(session, currentDossier, resourceMap, currentLot, bordereau);
			statutArchivage = VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_GENERE;
			nbReussisCurrentLot++;
			bordereau.setSuccess(true);
			bordereau.setStatutArchivageAfter(statutArchivage);
		} catch (Exception e) {
			ARCHIVAGE_LOGGER.warn(EpgLogEnumImpl.FAIL_PROCESS_B_EXTRACTION_ADAMANT_TEC, e);
			statutArchivage =VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_ERREUR_GEN_SIP;
			bordereau.setMessage(e.getMessage());
			bordereau.setSuccess(false);
			bordereau.setStatutArchivageAfter(statutArchivage);
			erreursCurrentLotNorList.add(currentDossier.getNumeroNor());
			errorCount++;
		} finally {
			if (bordereau != null) {
				SolonEpgAdamantServiceLocator.getBordereauService().saveBordereau(bordereau);
			}
			
			// Sortir le dossier du lot courant
			removeCurrentDossierFromCurrentLot(session);
			updateDossierStatutArchivage(session, currentDossier, statutArchivage);
		}
		
		nbDossiersTraites++;
	}
	
	private void updateDossierStatutArchivage(CoreSession session, Dossier dossier, String statut) throws ClientException {
		dossier.setStatutArchivage(statut);
		dossier.save(session);
		
		// Archivage des dossiers links
		SolonEpgServiceLocator.getDossierService().archiveDossiersLinks(session, dossier.getDocument());
	}

	/**
	 * Met à jour les variables currentLot et currentDossier.
	 * 
	 * @throws ClientException
	 */
	private void selectLotAndDossierATraiter(CoreSession session) throws ClientException {
		// Y a-t-il un lot en cours ?
		currentLot = getCurrentLot();
		if (currentLot == null) {
			// Il n'y a pas de lot en cours, on en sélectionne un nouveau
			selectNextLotATraiter();
		}

		// S'il y a un lot en cours, on peut sélectionner un prochain dossier à
		// traiter
		if (currentLot != null) {
			currentDossier = selectNextDossierATraiter(session);
			while (currentDossier == null && currentLot != null) {
				// Il n'y a pas/plus de dossier dans le lot, on le termine et on
				// passe au suivant
				SolonEpgAdamantServiceLocator.getExtractionLotService().updateLotStatus(currentLot, ExtractionStatus.TERMINE);
				selectNextLotATraiter();
				currentDossier = selectNextDossierATraiter(session);
			}
		}
	}

	private boolean continueBatchExecution(CoreSession session) throws ClientException {
		Integer maxDossiersATraiter = SolonEpgAdamantServiceLocator.getDossierExtractionService().getMaxDossiersATraiter(session);
		return (nbDossiersTraites < maxDossiersATraiter) && (currentLot != null) && (currentDossier != null);
	}

	/**
	 * Récupère dans la configuration le nombre maximal de NORs de dossiers en
	 * erreur d'extraction à afficher par ligne du suivi du batch.
	 * 
	 * @return valeur int
	 */
	private int getMaxErreursParLigneSuivi() {
		if (maxErreursParLigneSuivi == null) {
			// Valeur non définie, on va la récupérer
			ConfigService configService = STServiceLocator.getConfigService();
			maxErreursParLigneSuivi = configService
					.getIntegerValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_BATCH_SUIVI_MAX_ERREURS);
		}
		return maxErreursParLigneSuivi.intValue();
	}

	/**
	 * Récupère, s'il existe, le lot actuellement en cours d'extraction.
	 * 
	 * @return null ou un objet DossierExtractionLot.
	 * @throws ClientException
	 */
	private DossierExtractionLot getCurrentLot() throws ClientException {
		return getNextLotByStatus(ExtractionStatus.EN_COURS);
	}

	/**
	 * Sélectionne le prochain lot à traiter, le marque EN_COURS. S'il y en a
	 * plusieurs, le plus ancien (date de création) est sélectionné.
	 * 
	 * @return un objet DossierExtractionLot, null si aucun n'est à traiter.
	 */
	private void selectNextLotATraiter() throws ClientException {
		currentLot = getNextLotByStatus(ExtractionStatus.EN_ATTENTE);

		if (currentLot != null) {
			SolonEpgAdamantServiceLocator.getExtractionLotService().updateLotStatus(currentLot, ExtractionStatus.EN_COURS);
			nbReussisCurrentLot = 0;
			erreursCurrentLotNorList = new ArrayList<String>();
			startTimeCurrentLot = Calendar.getInstance().getTimeInMillis();
		}
	}

	/**
	 * Sélectionne et retourne le prochain dossier à traiter dans le lot
	 * courant.
	 * 
	 * @return un objet Dossier.
	 * @throws ClientException
	 */
	private Dossier selectNextDossierATraiter(CoreSession session) throws ClientException {
		if (currentLot != null) {
			return SolonEpgAdamantServiceLocator.getExtractionLotService().getNextDossierInLot(session, currentLot);
		}
		return null;
	}

	private DossierExtractionLot getNextLotByStatus(ExtractionStatus status) throws ClientException {
		ExtractionLotService extractionLotService = SolonEpgAdamantServiceLocator.getExtractionLotService();

		List<DossierExtractionLot> lots = extractionLotService.getLotsByExtractionStatus(status);
		if (lots.isEmpty()) {
			return null;
		}
		return lots.get(0);
	}

	/**
	 * Supprime l'occurrence du dossier courant dans le lot courant. Si, à la
	 * suite de cette suppression, le lot devient vide, passer le statut du lot
	 * à {@link ExtractionStatus}.TERMINE et forcer la sélection d'un nouveau
	 * lot par la mise à null de <code>currentLot</code>.
	 * 
	 * @throws ClientException
	 */
	private void removeCurrentDossierFromCurrentLot(CoreSession session) throws ClientException {
		ExtractionLotService extractionLotService = SolonEpgAdamantServiceLocator.getExtractionLotService();

		if (extractionLotService.deleteFromLot(session, currentDossier, currentLot)) {
			
			// Le lot est terminé
			SolonEpgAdamantServiceLocator.getExtractionLotService().updateLotStatus(currentLot,
					ExtractionStatus.TERMINE);
			createBatchResultForCurrentLot();
			try {
				SolonEpgAdamantServiceLocator.getBordereauService().generateBordereauFile(session, currentLot,
						SolonEpgAdamantServiceLocator.getDossierExtractionService()
								.getOrCreateExtractionLotFolder(currentLot));
			} catch (IOException e) {
				LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_EXTRACTION_ADAMANT_TEC,
						"Erreur d'écriture du fichier bordereau", e);
			}
			

			currentLot = null; // Un nouveau lot courant sera à déterminer
		}
	}

	/**
	 * Crée la ou les lignes correspondantes au lot courant dans les détails du
	 * suivi du batch
	 * 
	 * @throws ClientException
	 */
	private void createBatchResultForCurrentLot() throws ClientException {
		if (currentLot != null) {
			SuiviBatchService suiviBatchService = STServiceLocator.getSuiviBatchService();
			long endTimeCurrentLot = Calendar.getInstance().getTimeInMillis();
			StringBuilder message;
			do {
				message = new StringBuilder(String.format("Lot %s.", currentLot.getName()));
				if (!erreursCurrentLotNorList.isEmpty()) {
					List<String> norsToDisplay = erreursCurrentLotNorList.subList(0,
							getMaxErreursParLigneSuivi() < erreursCurrentLotNorList.size()
									? getMaxErreursParLigneSuivi()
									: erreursCurrentLotNorList.size());
					message.append(" Dossiers en erreur : ");
					message.append(StringUtils.join(norsToDisplay, ", "));
					norsToDisplay.clear();
				}
				suiviBatchService.createBatchResultFor(batchLoggerModel, message.toString(), nbReussisCurrentLot,
						endTimeCurrentLot - startTimeCurrentLot);
			} while (!erreursCurrentLotNorList.isEmpty());
		}
	}

}
