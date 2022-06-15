package fr.dila.solonepg.core.service;

import com.google.common.net.MediaType;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.recherche.EpgDossierListingDTO;
import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.api.service.DossierSignaleService;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.api.service.EpgPdfDossierService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.archive.SolonEpgZipWriter;
import fr.dila.solonepg.core.util.DossierPageProviderHelper;
import fr.dila.ss.core.service.SSAbstractArchiveService;
import fr.dila.ss.core.util.SSPdfUtil;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.io.STDocumentReader;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.convert.api.ConversionException;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.ecm.core.io.DocumentPipe;
import org.nuxeo.ecm.core.io.DocumentReader;
import org.nuxeo.ecm.core.io.DocumentWriter;
import org.nuxeo.ecm.core.io.impl.DocumentPipeImpl;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.runtime.api.Framework;

/**
 * Implémentation du service d'archivage epg
 *
 */
public class ArchiveServiceImpl extends SSAbstractArchiveService implements ArchiveService {
    private static final long serialVersionUID = -7780914517900449689L;

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog(ArchiveServiceImpl.class);

    private static Map<String, String> validStatusToLabel;

    static {
        validStatusToLabel = new HashMap<>();
        // État validée manuellement
        validStatusToLabel.put("1", "label.epg.feuilleRoute.etape.valide.manuellement");
        // État avis favorable avec correction
        validStatusToLabel.put(
            SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_CORRECTION_VALUE,
            "label.epg.feuilleRoute.etape.valide.avisFavorableCorrection"
        );
        // État invalidée
        validStatusToLabel.put("2", "label.epg.feuilleRoute.etape.valide.refusValidation");
        // État validée automatiquement
        validStatusToLabel.put("3", "label.epg.feuilleRoute.etape.valide.automatiquement");
        // État avec sortie 'non concerné'
        validStatusToLabel.put("4", "label.epg.feuilleRoute.etape.valide.nonConcerne");
        // État retour pour modification
        validStatusToLabel.put(
            SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETOUR_MODIFICATION_VALUE,
            "label.epg.feuilleRoute.etape.valide.retourModification"
        );
        // État validée manuellement : avis rendu
        validStatusToLabel.put(
            SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_POUR_AVIS_RENDU_VALUE,
            "label.epg.feuilleRoute.etape.valide.manuellement.avisRendu"
        );
        // État invalidée : dessaisissement
        validStatusToLabel.put(
            SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_DESSAISISSEMENT_VALUE,
            "label.epg.feuilleRoute.etape.valide.refusValidation.dessaisissement"
        );
        // État invalidée : retrait
        validStatusToLabel.put(
            SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETRAIT_VALUE,
            "label.epg.feuilleRoute.etape.valide.refusValidation.retrait"
        );
        // État invalidée : refus
        validStatusToLabel.put(
            SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_REFUS_VALUE,
            "label.epg.feuilleRoute.etape.valide.manuellement.refus"
        );
        // État invalidée : renvoi
        validStatusToLabel.put(
            SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RENVOI_VALUE,
            "label.epg.feuilleRoute.etape.valide.manuellement.renvoi"
        );
    }

    private static final String ARCHIVAGE_INTERMEDIAIRE_PROVIDER_NAME = "dossierCandidatToArchivageIntermediairePage";
    private static final String ARCHIVAGE_DEFINITIF_PROVIDER_NAME = "dossierCandidatToArchivageDefinitivePage";

    /**
     * Default constructor
     */
    public ArchiveServiceImpl() {
        super();
    }

    @Override
    public void supprimerDossier(CoreSession session, DocumentModel dossierDoc, NuxeoPrincipal principal) {
        // suppression du dossier signalé lié au dossier
        List<DocumentModel> dossierSignaleList = new ArrayList<>();
        dossierSignaleList.add(dossierDoc);
        DossierSignaleService dossierSignaleService = SolonEpgServiceLocator.getDossierSignaleService();
        dossierSignaleService.retirer(session, dossierSignaleList);

        // suppression du dossier
        super.supprimerDossier(session, dossierDoc, principal);

        if ("true".equals(Framework.getProperty("solonepg.dossier.soft.delete", "true"))) {
            Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            dossier.setIsDossierDeleted(true);
            session.saveDocument(dossier.getDocument());
            session.save();
        }
    }

    @Override
    protected void logSuppressionDossier(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final NuxeoPrincipal principal
    ) {
        final JournalService journalService = STServiceLocator.getJournalService();
        if (principal == null) {
            journalService.journaliserActionAdministration(
                session,
                dossierDoc,
                STEventConstant.EVENT_ARCHIVAGE_DOSSIER,
                STEventConstant.COMMENT_SUPPRESSION_DOSSIER
            );
        } else {
            journalService.journaliserActionAdministration(
                session,
                principal,
                dossierDoc,
                STEventConstant.EVENT_ARCHIVAGE_DOSSIER,
                STEventConstant.COMMENT_SUPPRESSION_DOSSIER
            );
        }
    }

    @Override
    public void writeZipStream(CoreSession session, List<DocumentModel> files, ZipOutputStream outputStream) {
        DocumentReader reader = null;
        DocumentWriter writer = null;

        reader = new STDocumentReader(files);

        writer = new SolonEpgZipWriter(session, outputStream);

        DocumentPipe pipe = new DocumentPipeImpl(10);
        pipe.setReader(reader);
        pipe.setWriter(writer);

        try {
            pipe.run();
        } catch (IOException e) {
            throw new NuxeoException(e);
        }

        reader.close();
    }

    @Override
    protected List<DocumentModel> findDossierLinkUnrestricted(CoreSession session, String identifier) {
        final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
        return corbeilleService.findDossierLinkUnrestricted(session, identifier);
    }

    /**
     * ajouter les documents du dossier (bordereau,journal et feuille de route) au zip.
     *
     * @param outputStream
     *            ZipOutputStream
     * @param dossierDoc
     * @param session
     */
    private void generateCurrentDocumentPdf(
        ZipOutputStream outputStream,
        DocumentModel dossierDoc,
        CoreSession session,
        boolean inclureJournal,
        boolean inclureFeuilleRoute
    ) {
        // ajout du pdf comme entrée dans le ZipOutputStream
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        // on ajoute les pdf du bordereau de la feuille de route et du journal
        ZipEntry entry = new ZipEntry("Dossier.pdf");
        try {
            outputStream.putNextEntry(entry);
            EpgPdfDossierService pdfService = SolonEpgServiceLocator.getPdfService();
            List<ByteArrayOutputStream> pdfs = new ArrayList<>();
            pdfs.add(pdfService.genererPdfBordereau(session, dossier, false));

            if (inclureJournal) {
                pdfs.add(pdfService.genererPdfJournal(dossier));
            }

            if (inclureFeuilleRoute) {
                pdfs.add(pdfService.genererPdfFeuilleRoute(session, dossier, false));
            }

            ByteArrayOutputStream pdfMerge = new ByteArrayOutputStream();
            SSPdfUtil.getPdfsMerger(pdfs).accept(pdfMerge);
            outputStream.write(pdfMerge.toByteArray());
            outputStream.closeEntry();
        } catch (IOException e) {
            throw new NuxeoException("IOException occured while generating pdf document", e);
        }
    }

    /**
     * ajouter le bordereau du dossier au zip.
     *
     * @param outputStream
     *            ZipOutputStream
     * @param dossierDoc
     * @param session
     * @throws Exception
     */
    @Override
    protected void generateCurrentDocumentPdf(
        ZipOutputStream outputStream,
        DocumentModel dossierDoc,
        CoreSession session
    ) {
        generateCurrentDocumentPdf(outputStream, dossierDoc, session, false, false);
    }

    @Override
    public ZipOutputStream initWriteZipStreamAndAddFile(
        List<DocumentModel> files,
        OutputStream outputStream,
        CoreSession session
    ) {
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
        writeZipStream(session, files, zipOutputStream);
        return zipOutputStream;
    }

    @Override
    public void writeZipStream(
        List<DocumentModel> files,
        OutputStream outputStream,
        DocumentModel dossierDoc,
        CoreSession session
    ) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
            writeZipStream(session, files, zipOutputStream);
            generateCurrentDocumentPdf(zipOutputStream, dossierDoc, session);
        } catch (IOException e) {
            throw new NuxeoException(e);
        }
    }

    @Override
    public void writeZipStream(
        List<DocumentModel> files,
        OutputStream outputStream,
        List<DocumentModel> dossiers,
        CoreSession session
    ) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
            writeZipStream(session, files, zipOutputStream);

            for (DocumentModel dossierDoc : dossiers) {
                generateCurrentDocumentPdf(zipOutputStream, dossierDoc, session);
            }
        } catch (IOException e) {
            throw new NuxeoException(e);
        }
    }

    /**
     * Convertis les Blobs d'une liste de DocumentModel en pdf puis stocke ces pdfs dans une liste d'InputStream.
     *
     * @param files
     * @return
     * @throws Exception
     */
    protected List<InputStream> convertDocumentModelsBlobsToPdf(List<DocumentModel> files) {
        List<InputStream> pdfs = new ArrayList<>();
        // on parcourt tous les document afin de récupérer tous les documents convertissable en pdf
        for (DocumentModel documentModel : files) {
            String docType = documentModel.getType();
            if (
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE.equals(docType) ||
                SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE.equals(docType)
            ) {
                fr.dila.st.api.domain.file.File file = documentModel.getAdapter(fr.dila.st.api.domain.file.File.class);
                Blob blob = file.getContent();
                if (blob != null) {
                    // si le le fichier est vide : on ne fait rien
                    // si le blob est déjà au format pdf : on ne fait rien
                    if (fr.dila.st.api.constant.MediaType.APPLICATION_PDF.mime().equals(blob.getMimeType())) {
                        // on convertit le fichier en pdf si celui-ci peut être converti
                        ConversionService conversionService = ServiceUtil.getRequiredService(ConversionService.class);
                        String converterName = conversionService.getConverterName(
                            blob.getMimeType(),
                            MediaType.PDF.toString()
                        );

                        if (!conversionService.isConverterAvailable(converterName, true).isAvailable()) {
                            LOGGER.info("Convertisseur pdf non disponible : Redémarrage daemon ooo");
                            converterName =
                                conversionService.getConverterName(blob.getMimeType(), MediaType.PDF.toString());
                        }
                        if (converterName == null) {
                            // le fichier ne peut pas être converti au format pdf
                            blob = null;
                        } else {
                            try {
                                BlobHolder blobHolder = new SimpleBlobHolder(blob);
                                BlobHolder result = conversionService.convert(converterName, blobHolder, null);
                                blob = result.getBlob();
                            } catch (ConversionException exc) {
                                LOGGER.error("Erreur de conversion du fichier", exc);
                                throw new NuxeoException(exc);
                            }
                        }
                    }
                    if (blob != null) {
                        // ajout du blob dans la liste des pdfs à merger
                        try {
                            pdfs.add(blob.getStream());
                        } catch (IOException e) {
                            throw new NuxeoException(e);
                        }
                    }
                }
            }
        }
        return pdfs;
    }

    @Override
    public void retirerArchivageDefinitive(final List<DocumentModel> dossierDocs, CoreSession session) {
        // on lance les suppressions des dossiers sans vérifier les droits
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                for (DocumentModel documentModel : dossierDocs) {
                    Dossier dossier = documentModel.getAdapter(Dossier.class);
                    dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE);
                    dossier.save(session);
                }
                session.save();
            }
        }
        .runUnrestricted();
    }

    @Override
    public String getActionFeuilleRouteButtonLabel(String routingTaskType, String labelKey) {
        final String key = labelKey + "." + routingTaskType;
        String label = ResourceHelper.getString(key);
        if (key.equals(label)) {
            return ResourceHelper.getString(labelKey);
        } else {
            return label;
        }
    }

    /**
     * Retourne true si le dossier est issue de la reprise de solon v1
     */
    @Override
    public boolean isDossierReprise(Dossier dossier) {
        return dossier.getIndexationRubrique().contains("REPRISE");
    }

    /**
     * Retourne la date de publication au JO d'un dossier
     */
    @Override
    public Calendar getDateParutionJorfFromRetourDila(Dossier dossier) {
        Calendar datePublication = null;
        RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
        if (retourDila != null && retourDila.getDateParutionJorf() != null) {
            datePublication = retourDila.getDateParutionJorf();
        }
        return datePublication;
    }

    @Override
    public String idIconeToLabelFDR(String routingTaskType, String validationStatusId) {
        return getActionFeuilleRouteButtonLabel(routingTaskType, validStatusToLabel.get(validationStatusId));
    }

    @Override
    public String getValidStatusToLabel(String key) {
        return validStatusToLabel.get(key);
    }

    @Override
    public List<DocumentModel> getListDocsToSend(CoreSession session, STDossier sTdossier) {
        Dossier dossier = sTdossier.getDocument().getAdapter(Dossier.class);
        final FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
        List<DocumentModel> fddDocs = fondDeDossierService.getFddDocuments(session, dossier);
        final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
        List<DocumentModel> paraphDocs = parapheurService.getAllFilesFromParapheurAllFolder(session, dossier);

        return ListUtils.union(fddDocs, paraphDocs);
    }

    @Override
    protected Consumer<ZipOutputStream> getDossierArchiveZosConsumer(
        final CoreSession session,
        final List<DocumentModel> files,
        final List<DocumentModel> dossiers
    ) {
        return (ZipOutputStream zos) -> {
            writeZipStream(session, files, zos);
            for (final DocumentModel dossierDoc : dossiers) {
                generateCurrentDocumentPdf(zos, dossierDoc, session, true, true);
            }
        };
    }

    @Override
    public long countAllDossiersArchivageIntermediaire(CoreSession session) {
        PageProvider<EpgDossierListingDTO> pageProvider = DossierPageProviderHelper.getPageProvider(
            ARCHIVAGE_INTERMEDIAIRE_PROVIDER_NAME,
            null,
            1,
            0,
            session
        );
        pageProvider.getCurrentPage();
        return pageProvider.getResultsCount();
    }

    @Override
    public List<EpgDossierListingDTO> getAllDossiersArchivageIntermediaire(CoreSession session) {
        PageProvider<EpgDossierListingDTO> pageProvider = DossierPageProviderHelper.getPageProvider(
            ARCHIVAGE_INTERMEDIAIRE_PROVIDER_NAME,
            null,
            0,
            0,
            session
        );
        return pageProvider.getCurrentPage();
    }

    @Override
    public long countAllDossiersArchivageDefinitif(CoreSession session) {
        PageProvider<EpgDossierListingDTO> pageProvider = DossierPageProviderHelper.getPageProvider(
            ARCHIVAGE_DEFINITIF_PROVIDER_NAME,
            null,
            1,
            0,
            session
        );
        pageProvider.getCurrentPage();
        return pageProvider.getResultsCount();
    }

    @Override
    public List<EpgDossierListingDTO> getAllDossiersArchivageDefinitif(CoreSession session) {
        PageProvider<EpgDossierListingDTO> pageProvider = DossierPageProviderHelper.getPageProvider(
            ARCHIVAGE_DEFINITIF_PROVIDER_NAME,
            null,
            0,
            0,
            session
        );
        return pageProvider.getCurrentPage();
    }
}
