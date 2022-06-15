package fr.dila.solonepg.adamant.batch;

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
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.event.batch.BatchResultModel;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.api.service.SuiviBatchService;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.runtime.transaction.TransactionHelper;

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
    private List<Dossier> dossiersATraiterList;

    /** Nombre de dossiers dont l'extraction est réussie dans le lot en cours d'extraction. */
    private int nbReussisCurrentLot;

    /** Liste des numéros NOR des dossiers dont l'extraction a échoué dans le lot en cours d'extraction. */
    private ArrayList<String> erreursCurrentLotNorList;

    /** Temps de début de traitement du lot en cours d'extraction en millisecondes. */
    private long startTimeCurrentLot;

    public ExtractionAdamantBatchListener() {
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_EXTRACTION_ADAMANT);
    }

    /**
     * Workaround : Si on effectue 2 runs successifs du batch sans redémarrer Nuxeo, on a une exception "The
     * DocumentModel is not associated to an open CoreSession". On réinitialise donc les variables pour garantir qu'elles
     * soient recalculées à chaque run du batch.
     */

    private void workaroundReset() {
        maxErreursParLigneSuivi = null;
        currentLot = null;
        currentDossier = null;
        dossiersATraiterList = null;
    }

    /**
     * On cloture les éventuelles transactions ouvertes. Ensuite, pour chaque
     * dossier à traiter, on démarre une transaction qu'on
     * committera/rollbackera à la fin du traitement. A la toute fin de la
     * boucle, une transaction aura été démarrée.
     */
    @Override
    protected void processEvent(CoreSession session, Event event) {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_EXTRACTION_ADAMANT_TEC);

        workaroundReset();
        Instant start = Instant.now();
        BatchResultModel batchResult = null;

        TransactionHelper.commitOrRollbackTransaction();
        TransactionHelper.startTransaction();

        try {
            batchResult =
                STServiceLocator
                    .getSuiviBatchService()
                    .createBatchResultFor(batchLoggerModel, "Exécution du batch d'extraction Adamant");
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
        }

        nbDossiersTraites = 0;
        nbReussisCurrentLot = 0;
        erreursCurrentLotNorList = new ArrayList<>();
        startTimeCurrentLot = Calendar.getInstance().getTimeInMillis();

        selectLotAndDossierATraiter(session);

        while (continueBatchExecution(session)) {
            currentDossier = dossiersATraiterList.get(0);
            extractCurrentDossier(session);

            selectLotAndDossierATraiter(session);

            ARCHIVAGE_LOGGER.debug("Début du commit de la transaction pour le dossier en cours");
            TransactionHelper.commitOrRollbackTransaction();
            TransactionHelper.startTransaction();
            ARCHIVAGE_LOGGER.debug("Fin du commit de la transaction pour le dossier en cours");
        }

        workaroundSetLotEnAttente();
        Instant finish = Instant.now();
        if (batchResult != null) {
            try {
                STServiceLocator
                    .getSuiviBatchService()
                    .updateBatchResultFor(batchResult, nbDossiersTraites, Duration.between(start, finish).toMillis());
            } catch (Exception e) {
                LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
            }
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_EXTRACTION_ADAMANT_TEC);
    }

    /**
     * Workaround : si on effectue 2 runs successifs (avec redémarrage du serveur Nuxeo) et qu'un lot est repris, il est
     * mal initialisé. On le remet donc EN_ATTENTE pour obliger le batch à recharger les données. Note : le problème
     * vient de selectLotAndDossierATraiter. Si currentLot est null en entrée, getCurrentLot() retourne le lot en cours
     * et alors on ne passe pas dans selectNextLotATraiter et dossiersATraiterList reste null.
     */

    private void workaroundSetLotEnAttente() {
    // Workaround : si le lot courant n'est pas terminé à la fin du run (EN_COURS) on le repasse à EN_ATTENTE//
        if (currentLot != null && currentLot.getStatus() == ExtractionStatus.EN_COURS) {
            SolonEpgAdamantServiceLocator.getExtractionLotService().updateLotStatus(currentLot, ExtractionStatus.EN_ATTENTE);
        }
    }

    /**
     * Extraction du dossier courant.
     */
    private void extractCurrentDossier(CoreSession session) {
        LOGGER.info(
            EpgLogEnumImpl.PROCESS_B_EXTRACTION_ADAMANT_TEC,
            "Traitement en cours: lot " + currentLot.getName() + ", dossier " + currentDossier.getNumeroNor()
        );

        String statutArchivage = null;

        DossierExtractionBordereau bordereau = SolonEpgAdamantServiceLocator
            .getBordereauService()
            .initBordereau(currentDossier, currentLot);
             ARCHIVAGE_LOGGER.debug("Bordereau initialisé");

        final DossierExtractionService dossierExtractionService = SolonEpgAdamantServiceLocator.getDossierExtractionService();

        try {
            dossierExtractionService.checkFile(currentDossier.getDocument(), session);
            ARCHIVAGE_LOGGER.debug("Check terminé");
            dossierExtractionService.genererDossierArchive(session, currentDossier, currentLot, bordereau);
            statutArchivage = VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_GENERE;
            nbReussisCurrentLot++;
            bordereau.setSuccess(true);
            bordereau.setStatutArchivageAfter(statutArchivage);
        } catch (Exception e) {
            ARCHIVAGE_LOGGER.warn(EpgLogEnumImpl.FAIL_PROCESS_B_EXTRACTION_ADAMANT_TEC, e);
            statutArchivage = VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_ERREUR_GEN_SIP;
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
            dossiersATraiterList.remove(currentDossier);
        }

        nbDossiersTraites++;
        ARCHIVAGE_LOGGER.debug("Dossier traité");
    }

    private void updateDossierStatutArchivage(CoreSession session, Dossier dossier, String statut) {
        dossier.setStatutArchivage(statut);
        dossier.save(session);

        // Archivage des dossiers links
        SolonEpgServiceLocator.getDossierService().archiveDossiersLinks(session, dossier.getDocument());
    }

    /**
     * Met à jour les variables currentLot et currentDossier.
     */
    private void selectLotAndDossierATraiter(CoreSession session) {
        ARCHIVAGE_LOGGER.debug("Début de la sélection du lot et dossier en cours");
        // Y a-t-il un lot en cours ?
        if (currentLot == null) {
            currentLot = getCurrentLot();
        }
        if (currentLot == null) {
            // Il n'y a pas de lot en cours, on en sélectionne un nouveau
            selectNextLotATraiter(session);
        }

        // S'il y a un lot en cours, on peut sélectionner un prochain dossier à
        // traiter
        if (currentLot != null) {
            while (CollectionUtils.isEmpty(dossiersATraiterList) && currentLot != null) {
                // Il n'y a pas/plus de dossier dans le lot, on le termine et on
                // passe au suivant
                SolonEpgAdamantServiceLocator
                    .getExtractionLotService()
                    .updateLotStatus(currentLot, ExtractionStatus.TERMINE);
                createBatchResultForCurrentLot();
                selectNextLotATraiter(session);
            }
        }
        ARCHIVAGE_LOGGER.debug("Fin de la sélection du lot et dossier en cours");
    }

    private boolean continueBatchExecution(CoreSession session) {
        Integer maxDossiersATraiter = SolonEpgAdamantServiceLocator
            .getDossierExtractionService()
            .getMaxDossiersATraiter(session);
        return (
            nbDossiersTraites < maxDossiersATraiter &&
            currentLot != null &&
            CollectionUtils.isNotEmpty(dossiersATraiterList)
        );
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
            maxErreursParLigneSuivi =
                configService.getIntegerValue(
                    SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_BATCH_SUIVI_MAX_ERREURS
                );
        }
        return maxErreursParLigneSuivi.intValue();
    }

    /**
     * Récupère, s'il existe, le lot actuellement en cours d'extraction.
     *
     * @return null ou un objet DossierExtractionLot.
     */
    private DossierExtractionLot getCurrentLot() {
        return getNextLotByStatus(ExtractionStatus.EN_COURS);
    }

    /**
     * Sélectionne le prochain lot à traiter, le marque EN_COURS. S'il y en a
     * plusieurs, le plus ancien (date de création) est sélectionné.
     *
     * @return un objet DossierExtractionLot, null si aucun n'est à traiter.
     */
    private void selectNextLotATraiter(CoreSession session) {
        currentLot = getNextLotByStatus(ExtractionStatus.EN_ATTENTE);

        if (currentLot != null) {
            SolonEpgAdamantServiceLocator
                .getExtractionLotService()
                .updateLotStatus(currentLot, ExtractionStatus.EN_COURS);
            nbReussisCurrentLot = 0;
            erreursCurrentLotNorList = new ArrayList<>();
            startTimeCurrentLot = Calendar.getInstance().getTimeInMillis();
            dossiersATraiterList = getDossiersATraiterList(session);
        }
    }

    /**
     * @return les dossiers à traiter du lot courant.
     */
    private List<Dossier> getDossiersATraiterList(CoreSession session) {
        if (currentLot != null) {
            List<DocumentModel> docList = SolonEpgAdamantServiceLocator
                .getExtractionLotService()
                .getDossiersInLot(session, currentLot);
            return docList.stream().map(doc -> doc.getAdapter(Dossier.class)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private DossierExtractionLot getNextLotByStatus(ExtractionStatus status) {
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
     */
    private void removeCurrentDossierFromCurrentLot(CoreSession session) {
        ExtractionLotService extractionLotService = SolonEpgAdamantServiceLocator.getExtractionLotService();

        if (extractionLotService.deleteFromLot(session, currentDossier, currentLot)) {
            // Le lot est terminé
            SolonEpgAdamantServiceLocator
                .getExtractionLotService()
                .updateLotStatus(currentLot, ExtractionStatus.TERMINE);
            createBatchResultForCurrentLot();
            try {
                SolonEpgAdamantServiceLocator
                    .getBordereauService()
                    .generateBordereauFile(
                        session,
                        currentLot,
                        SolonEpgAdamantServiceLocator
                            .getDossierExtractionService()
                            .getOrCreateExtractionLotFolder(currentLot)
                    );
            } catch (IOException e) {
                LOGGER.error(
                    session,
                    EpgLogEnumImpl.FAIL_PROCESS_B_EXTRACTION_ADAMANT_TEC,
                    "Erreur d'écriture du fichier bordereau",
                    e
                );
            }

            currentLot = null; // Un nouveau lot courant sera à déterminer
        }
    }

    /**
     * Crée la ou les lignes correspondantes au lot courant dans les détails du
     * suivi du batch
     */
    private void createBatchResultForCurrentLot() {
        if (currentLot != null) {
            SuiviBatchService suiviBatchService = STServiceLocator.getSuiviBatchService();
            long endTimeCurrentLot = Calendar.getInstance().getTimeInMillis();
            StringBuilder message;
            do {
                message = new StringBuilder(String.format("Lot %s.", currentLot.getName()));
                if (!erreursCurrentLotNorList.isEmpty()) {
                    List<String> norsToDisplay = erreursCurrentLotNorList.subList(
                        0,
                        getMaxErreursParLigneSuivi() < erreursCurrentLotNorList.size()
                            ? getMaxErreursParLigneSuivi()
                            : erreursCurrentLotNorList.size()
                    );
                    message.append(" Dossiers en erreur : ");
                    message.append(StringUtils.join(norsToDisplay, ", "));
                    norsToDisplay.clear();
                }
                suiviBatchService.createBatchResultFor(
                    batchLoggerModel,
                    message.toString(),
                    nbReussisCurrentLot,
                    endTimeCurrentLot - startTimeCurrentLot
                );
            } while (!erreursCurrentLotNorList.isEmpty());
        }
    }
}
