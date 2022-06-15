package fr.dila.solonepg.adamant.batch;

import com.google.common.collect.Iterables;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import fr.dila.solonepg.adamant.SolonEpgAdamantConstant;
import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.adamant.model.RetourVitamBordereauLivraison;
import fr.dila.solonepg.adamant.model.RetourVitamRapportVersement;
import fr.dila.solonepg.adamant.service.RetourVitamService;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.st.api.constant.STPathConstant;
import fr.dila.st.api.event.batch.BatchResultModel;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.SuiviBatchService;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.core.util.ZipUtil;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipFile;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.runtime.transaction.TransactionHelper;

public class RetourVitamBatchListener extends AbstractBatchEventListener {
    private static final int NB_COLONNE_RV = 13;

    private static final int NB_COLONNES_BL = 9;

    private static final STLogger LOGGER = STLogFactory.getLog(RetourVitamBatchListener.class);
    private static final String RETOUR_VITAM_LIVRAISON_OK = STServiceLocator
        .getConfigService()
        .getValue(SolonEpgAdamantConstant.RETOUR_VITAM_LIVRAISON_OK);
    private static final String RETOUR_VITAM_VERSEMENT_OK = STServiceLocator
        .getConfigService()
        .getValue(SolonEpgAdamantConstant.RETOUR_VITAM_VERSEMENT_OK);
    private static final String INVALID_NUMBER_COLUMNS_ERROR_MESSAGE =
        "Erreur à la ligne %d. Le nombre de colonnes de la ligne n'est pas valide [actuel : %d - attendu : %d]";

    // Nombre de ligne injecté depuis le fichier CSV
    private int nbLinesTraites;

    // Temps de début de traitement du fichier en cours en millisecondes
    private long startTimeCurrentFile;

    // Message d'erreur lors de la lecture du fichier csv
    private String erreurMessage;

    // Nom du fichier en cours de traitement
    private String currentFileName;

    public RetourVitamBatchListener() {
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_RETOUR_VITAM);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_RETOUR_VITAM_TEC);
        Instant start = Instant.now();
        BatchResultModel batchResult = null;

        try {
            batchResult =
                STServiceLocator
                    .getSuiviBatchService()
                    .createBatchResultFor(batchLoggerModel, "Exécution du batch de retour VITAM");
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
        }

        RetourVitamService vitamService = SolonEpgAdamantServiceLocator.getRetourVitamService();

        // On récupère la liste des fichier csv à injecter en base
        File directory = new File(vitamService.getPathDirRetourVitamTodo());
        String[] filePathnames = directory.list();
        if (ArrayUtils.isNotEmpty(filePathnames)) {
            for (String pathname : filePathnames) {
                startTimeCurrentFile = System.currentTimeMillis();
                currentFileName = pathname;
                nbLinesTraites = 0;

                if (FileUtils.isCsvFile(pathname)) {
                    injectDataFromCSV(session);
                } else if (FileUtils.isZipFile(pathname)) {
                    injectDataFromZip(session);
                }

                TransactionHelper.commitOrRollbackTransaction();
                TransactionHelper.startTransaction();

                createBatchResultForCurrentFile(currentFileName);
            }
        } else {
            LOGGER.info(STLogEnumImpl.DEFAULT, String.format("Répertoire %s vide.", directory));
        }

        Instant finish = Instant.now();
        if (batchResult != null) {
            try {
                STServiceLocator
                    .getSuiviBatchService()
                    .updateBatchResultFor(
                        batchResult,
                        filePathnames.length,
                        Duration.between(start, finish).toMillis()
                    );
            } catch (Exception e) {
                LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
            }
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_RETOUR_VITAM_TEC);
    }

    private void injectDataFromCSV(CoreSession session) {
        erreurMessage = null;
        LOGGER.info(EpgLogEnumImpl.PROCESS_B_RETOUR_VITAM_TEC, "Traitement en cours: fichier " + currentFileName);

        try {
            List<String[]> csvContent = readCsvFile(currentFileName);
            // Traitement des Bordereaux de livraison ou Rapport de versement
            if (currentFileName.startsWith("BL_")) {
                processBordereauLivraison(session, csvContent);
            } else if (currentFileName.startsWith("RV_")) {
                processRapportVersement(session, csvContent);
            } else {
                throw new IllegalArgumentException("Préfix du fichier non identifiable.");
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(STLogEnumImpl.DEFAULT, e);
            erreurMessage = e.getMessage();
            moveFileToError(session);
        } catch (IOException | CsvException e) {
            LOGGER.error(STLogEnumImpl.FAIL_READ_FILE, e);
            erreurMessage = "Erreur lors de la lecture du fichier CSV.";
            moveFileToError(session);
        }
    }

    private void injectDataFromZip(CoreSession session) {
        erreurMessage = null;
        LOGGER.info(EpgLogEnumImpl.PROCESS_B_RETOUR_VITAM_TEC, "Traitement en cours: fichier " + currentFileName);
        RetourVitamService vitamService = SolonEpgAdamantServiceLocator.getRetourVitamService();

        try (
            ZipFile zipFile = new ZipFile(
                vitamService.getPathDirRetourVitamTodo() + STPathConstant.PATH_SEP + currentFileName
            )
        ) {
            zipFile
                .stream()
                .filter(zipEntry -> !zipEntry.isDirectory())
                .forEach(
                    zipEntry -> {
                        String zipEntryName = new File(zipEntry.getName()).getName();
                        if (FileUtils.isPdfFile(zipEntryName)) {
                            vitamService.saveBordereauVersementDossier(
                                session,
                                zipEntryName,
                                ZipUtil.getZipEntryContent(zipFile, zipEntry)
                            );
                        } else {
                            throw new IllegalArgumentException(
                                String.format("Le fichier %s n'est pas un pdf.", zipEntryName)
                            );
                        }
                    }
                );
            moveFileToDone(session);
        } catch (IllegalArgumentException | IOException | NuxeoException e) {
            LOGGER.error(STLogEnumImpl.FAIL_READ_FILE, e);
            erreurMessage = e.getMessage();
            moveFileToError(session);
        }
    }

    private void processBordereauLivraison(CoreSession session, List<String[]> csvContent) {
        processCsvLines(
            session,
            csvContent,
            params -> processBordereauLivraisonLine(params.getLeft(), params.getMiddle(), params.getRight())
        );
    }

    private void processBordereauLivraisonLine(CoreSession session, RetourVitamService vitamService, String[] line) {
        int lineIndex = nbLinesTraites + 2;
        RetourVitamBordereauLivraison bordereau = mappingBordereauLivraison(line, lineIndex);
        vitamService.updateStatutDossierFromBordereauLivraison(session, bordereau, lineIndex);
        vitamService.saveLineBorderauLivraison(bordereau);
    }

    private void processRapportVersement(CoreSession session, List<String[]> csvContent) {
        processCsvLines(
            session,
            csvContent,
            params -> processRapportVersementLine(params.getLeft(), params.getMiddle(), params.getRight())
        );
    }

    private void processRapportVersementLine(CoreSession session, RetourVitamService vitamService, String[] line) {
        int lineIndex = nbLinesTraites + 2;
        RetourVitamRapportVersement rapport = mappingRapportVersement(line, lineIndex);
        vitamService.updateStatutDossierFromRapportVersement(session, rapport, lineIndex);
        vitamService.saveLineRapportVersement(rapport);
    }

    private void processCsvLines(
        CoreSession session,
        List<String[]> csvContent,
        Consumer<ImmutableTriple<CoreSession, RetourVitamService, String[]>> processLine
    ) {
        RetourVitamService vitamService = SolonEpgAdamantServiceLocator.getRetourVitamService();
        for (String[] line : Iterables.skip(csvContent, 1)) {
            processLine.accept(ImmutableTriple.of(session, vitamService, line));
            nbLinesTraites++;
        }
        moveFileToDone(session);
    }

    private List<String[]> readCsvFile(String fileName) throws IOException, CsvException {
        RetourVitamService vitamService = SolonEpgAdamantServiceLocator.getRetourVitamService();
        List<String[]> csvContent = null;
        try (
            CSVReader reader = new CSVReader(
                new FileReader(vitamService.getPathDirRetourVitamTodo() + STPathConstant.PATH_SEP + fileName)
            )
        ) {
            csvContent = reader.readAll();
        }
        // Si vide ou contient une seul ligne => pas d'info à saisir
        if (CollectionUtils.isEmpty(csvContent) || csvContent.size() == 1) {
            throw new IllegalArgumentException("Fichier CSV vide.");
        }
        return csvContent;
    }

    private RetourVitamBordereauLivraison mappingBordereauLivraison(String[] line, int lineIndex) {
        checkNumberColumns(line, lineIndex, NB_COLONNES_BL);

        RetourVitamBordereauLivraison bdl = new RetourVitamBordereauLivraison();
        bdl.setNor(FilenameUtils.removeExtension(line[0]));
        bdl.setNomSip(line[0]);
        bdl.setIdPaquet(line[1]);
        bdl.setEmpreinte(line[2]);
        bdl.setPoids(Long.parseLong(line[3]));
        bdl.setTypeActe(line[4]);
        bdl.setStatut(line[5]);
        bdl.setVersion(line[6]);
        bdl.setStatutLivraison(line[7]);
        bdl.setDateLivraison(parseDateFromLine(line[8], lineIndex));
        bdl.setStatutArchivageAfter(
            RETOUR_VITAM_LIVRAISON_OK.equals(line[7])
                ? VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_LIVRE
                : VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_ERREUR_LIVRAISON
        );
        return bdl;
    }

    private RetourVitamRapportVersement mappingRapportVersement(String[] line, int lineIndex) {
        checkNumberColumns(line, lineIndex, NB_COLONNE_RV);

        RetourVitamRapportVersement rapport = new RetourVitamRapportVersement();
        rapport.setNor(FilenameUtils.removeExtension(line[0]));
        rapport.setNomSip(line[0]);
        rapport.setIdPaquet(line[1]);
        rapport.setEmpreinte(line[2]);
        rapport.setPoids(Long.parseLong(line[3]));
        rapport.setTypeActe(line[4]);
        rapport.setStatut(line[5]);
        rapport.setVersion(line[6]);
        rapport.setStatutLivraison(line[7]);
        rapport.setDateLivraison(parseDateFromLine(line[8], lineIndex));
        rapport.setStatutVersement(line[9]);
        rapport.setDateVersement(parseDateFromLine(line[10], lineIndex));
        rapport.setIdVitam(line[11]);
        rapport.setMessageRejet(line[12]);
        rapport.setStatutArchivageAfter(
            RETOUR_VITAM_VERSEMENT_OK.equals(line[9])
                ? VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_ARCHIVE
                : VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_ERREUR_ARCHIVAGE
        );
        return rapport;
    }

    private static void checkNumberColumns(String[] line, int lineIndex, int expectedNumberColumns) {
        if (line.length != expectedNumberColumns) {
            throw new IllegalArgumentException(
                String.format(INVALID_NUMBER_COLUMNS_ERROR_MESSAGE, lineIndex, line.length, expectedNumberColumns)
            );
        }
    }

    private static Date parseDateFromLine(String dateToParse, int lineIndex) {
        try {
            return SolonDateConverter.DATETIME_DASH_REVERSE_T_SECOND_COLON.parseToDate(dateToParse);
        } catch (NuxeoException e) {
            throw new IllegalArgumentException(
                String.format("Erreur à la ligne %d. Le format de date n'est pas valide", lineIndex),
                e
            );
        }
    }

    /**
     * Crée la ou les lignes correspondantes au fichier injecté dans les détails du
     * suivi du batch
     */
    private void createBatchResultForCurrentFile(String fileName) {
        SuiviBatchService suiviBatchService = STServiceLocator.getSuiviBatchService();
        long endTimeCurrentFile = System.currentTimeMillis();
        String message = "Fichier " + fileName;
        if (StringUtils.isNotBlank(erreurMessage)) {
            message += ". " + erreurMessage;
        }
        try {
            suiviBatchService.createBatchResultFor(
                batchLoggerModel,
                message,
                nbLinesTraites,
                endTimeCurrentFile - startTimeCurrentFile
            );
        } catch (Exception e) {
            LOGGER.error(STLogEnumImpl.DEFAULT, e, "Erreur lors de la création du batch result");
        }
    }

    private void moveFileToDone(CoreSession session) {
        RetourVitamService vitamService = SolonEpgAdamantServiceLocator.getRetourVitamService();
        try {
            vitamService.moveFileToDone(session, currentFileName);
        } catch (IOException e) {
            erreurMessage = String.format("Erreur lors du déplacement du fichier %s.", currentFileName);
            LOGGER.error(STLogEnumImpl.DEFAULT, e, erreurMessage);
            moveFileToError(session);
        }
    }

    private void moveFileToError(CoreSession session) {
        RetourVitamService vitamService = SolonEpgAdamantServiceLocator.getRetourVitamService();
        try {
            vitamService.moveFileToError(session, currentFileName);
        } catch (IOException e) {
            erreurMessage = String.format("Erreur lors du déplacement du fichier %s.", currentFileName);
            LOGGER.error(STLogEnumImpl.DEFAULT, e, erreurMessage);
        }
    }
}
