package fr.dila.solonepg.adamant.service.impl;

import fr.dila.solonepg.adamant.SolonEpgAdamantConstant;
import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.adamant.model.DossierExtractionBordereau;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.adamant.service.DossierExtractionService;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.ParametrageAdamantService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.api.service.PdfGeneratorService;
import fr.dila.solonepg.core.service.NORServiceImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.service.DossierDistributionService;
import fr.dila.st.api.constant.MediaType;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.core.util.SHA512Util;
import fr.dila.st.core.util.SolonDateConverter;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.model.RouteTableElement;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import fr.sword.naiad.nuxeo.commons.core.util.SessionUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.persistence.PersistenceProvider;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunVoid;
import org.nuxeo.ecm.core.persistence.PersistenceProviderFactory;
import org.nuxeo.ecm.core.schema.FacetNames;
import org.nuxeo.ecm.core.uidgen.UIDGeneratorService;
import org.nuxeo.ecm.core.uidgen.UIDSequencer;
import org.nuxeo.ecm.platform.audit.api.LogEntry;
import org.nuxeo.runtime.api.Framework;

/**
 * Service dédié à l'extraction d'un dossier.
 *
 * @author tlombard
 */
public class DossierExtractionServiceImpl implements DossierExtractionService {
    private static final String MESSAGE_ERROR_GET_JOURNAL = " -> Impossible de récupérer le journal";

    private static final String RECORD_GRP = "RecordGrp";

    private static final String ATTR_ID_EQUALS_ID = "id=\"ID";

    private static final String TAG_ARCHIVEUNIT_END = "</ArchiveUnit>";

    private static final String TAG_ARCHIVEUNIT_BEG = "<ArchiveUnit ";

    private static final String TAG_DATAOBJECTREFERENCEID_END = "</DataObjectReferenceId>";

    private static final String TAG_DATAOBJECTREFERENCEID_BEG = "<DataObjectReferenceId>";

    private static final String TAG_DATAOBJECTREFERENCE_END = "</DataObjectReference>";

    private static final String TAG_DATAOBJECTREFERENCE_BEG = "<DataObjectReference>";

    private static final STLogger LOGGER = STLogFactory.getLog(DossierExtractionServiceImpl.class);

    private static final STLogger ARCHIVAGE_LOGGER = STLogFactory.getLog(SolonEpgAdamantConstant.ARCHIVAGE_LOG);

    private static final String SOLON_EPG_ARCHIVAGE_SEQUENCER = "SOLON_EPG_ARCHIVAGE_SEQUENCER";

    private static final String TAG_RULE_BEG = "<Rule>";
    private static final String TAG_RULE_END = "</Rule>";

    private static final String TAG_IDENTIFIER_END = "</Identifier>";
    private static final String TAG_IDENTIFIER_BEG = "<Identifier>";

    private static final String TAG_STARTDATE_END = "</StartDate>";
    private static final String TAG_STARTDATE_BEG = "<StartDate>";

    private CoreSession session;

    private static volatile PersistenceProvider persistenceProvider;

    public DossierExtractionServiceImpl() {
        super();
    }

    private String getSipName(Dossier dossier) {
        return dossier.getNumeroNor() + ".zip";
    }

    @Override
    public void genererDossierArchive(
        final CoreSession documentManager,
        Dossier dossier,
        DossierExtractionLot lot,
        DossierExtractionBordereau bordereau
    ) {
        String sipName = getSipName(dossier);

        bordereau.setNomSip(sipName);

        this.session = documentManager;

        final PdfGeneratorService pdfGeneratorService = SolonEpgServiceLocator.getPdfGeneratorService();
        final ConfigService configService = STServiceLocator.getConfigService();
        final ParametrageAdamantService parametrageAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();

        // TLD : L'identifiant unique du dossier ne sera généré qu'à la fin,
        // donc on commence par mettre une chaine et on fera le remplacement dans le
        // String à la fin.
        String messageIdentifierTarget = "##MESSAGE_IDENTIFIER_TARGET##";

        ZipOutputStream zip = null;
        String zipFile = null;

        ARCHIVAGE_LOGGER.debug(STLogEnumImpl.DEFAULT, "Début de la récupération des logs de " + dossier.getDocument().getId());
        List<LogEntry> listLogEntryJournal = getLogEntryList(dossier.getDocument().getId());
        ARCHIVAGE_LOGGER.debug(STLogEnumImpl.DEFAULT, "Fin de la récupération des logs de " + dossier.getDocument().getId());

        LOGGER.debug(
            STLogEnumImpl.DEFAULT,
            "Début de génération de l'archive définitive du dossier" + dossier.getNumeroNor()
        );

        File archivageDefTree = getOrCreateExtractionLotFolder(lot);

        // On crée le zip à vide car on exporte séparemment le parapheur et
        // le FDD
        zipFile = archivageDefTree.getPath() + SolonEpgAdamantConstant.FILE_SEPARATOR + sipName;

        try (OutputStream outputStreamZip = new FileOutputStream(zipFile)) {
            zip =
                SolonEpgServiceLocator
                    .getArchiveService()
                    .initWriteZipStreamAndAddFile(new ArrayList<DocumentModel>(), outputStreamZip, session);
            ARCHIVAGE_LOGGER.debug(STLogEnumImpl.DEFAULT, "Initialisation du zip faite");

            // Initialisation des StringBuffer qui vont contenir les données à
            // mettre dans le fichier XML
            StringBuffer enteteFichierXML = new StringBuffer();
            enteteFichierXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            enteteFichierXML.append(
                "<?xml-model href=\"" +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_BALISE_HREF) +
                "\" type=\"application/xml\" schematypens=\"http://relaxng.org/ns/structure/1.0\"?>"
            );
            enteteFichierXML.append("<ArchiveTransfer xmlns=\"fr:gouv:culture:archivesdefrance:seda:v2.1\">");
            enteteFichierXML.append("");

            StringBuffer metadonneesFichierXML = new StringBuffer();
            metadonneesFichierXML.append("<Comment>Versement du dossier NOR " + dossier.getNumeroNor() + "</Comment>");
            Date now = new Date();
            metadonneesFichierXML.append("<Date>" + formatArchiveDateTime(now) + "</Date>");
            // Identifiant séquentiel (séquence oracle)
            metadonneesFichierXML.append("<MessageIdentifier>" + messageIdentifierTarget + "</MessageIdentifier>");
            // Conf
            metadonneesFichierXML.append(
                "<ArchivalAgreement>" +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ARCHIVAL_AGREEMENT) +
                "</ArchivalAgreement>"
            );
            metadonneesFichierXML.append("<CodeListVersions>");
            // ReplyCodeListVersion
            metadonneesFichierXML.append(
                "<ReplyCodeListVersion>" +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_REPLY_CODE_LIST_VERSION) +
                "</ReplyCodeListVersion>"
            );
            // Conf
            metadonneesFichierXML.append(
                "<MessageDigestAlgorithmCodeListVersion>" +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_MESSAGE_DIGEST_ALGORITHM) +
                "</MessageDigestAlgorithmCodeListVersion>"
            );
            // Conf
            metadonneesFichierXML.append(
                "<MimeTypeCodeListVersion>" +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_MIME_TYPE) +
                "</MimeTypeCodeListVersion>"
            );
            // EncodingCodeListVersion
            metadonneesFichierXML.append(
                "<EncodingCodeListVersion>" +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ENCODING_CODE_LIST_VERSION) +
                "</EncodingCodeListVersion>"
            );
            // Conf
            metadonneesFichierXML.append(
                "<FileFormatCodeListVersion>" +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_FILE_FORMAT) +
                "</FileFormatCodeListVersion>"
            );
            // CompressionAlgorithmCodeListVersion
            metadonneesFichierXML.append(
                "<CompressionAlgorithmCodeListVersion>" +
                configService.getValue(
                    SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_COMPRESSION_ALGORITHM_CODE_LIST_VERSION
                ) +
                "</CompressionAlgorithmCodeListVersion>"
            );
            // DataObjectversionCodeListVersion
            metadonneesFichierXML.append(
                "<DataObjectVersionCodeListVersion>" +
                configService.getValue(
                    SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_DATA_OBJECT_VERSION_CODE_LIST_VERSION
                ) +
                "</DataObjectVersionCodeListVersion>"
            );
            // StorageRulecodeListVersion
            metadonneesFichierXML.append(
                "<StorageRuleCodeListVersion>" +
                configService.getValue(
                    SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_STORAGE_RULE_CODE_LIST_VERSION
                ) +
                "</StorageRuleCodeListVersion>"
            );
            // Conf
            metadonneesFichierXML.append(
                "<AppraisalRuleCodeListVersion>" +
                configService.getValue(
                    SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_APPRAISAL_RULE_CODE_LIST_VERSION
                ) +
                "</AppraisalRuleCodeListVersion>"
            );
            // Conf
            metadonneesFichierXML.append(
                "<AccessRuleCodeListVersion>" +
                configService.getValue(
                    SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_CODE_LIST_VERSION
                ) +
                "</AccessRuleCodeListVersion>"
            );
            // Conf
            metadonneesFichierXML.append(
                "<DisseminationRuleCodeListVersion>" +
                configService.getValue(
                    SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_DISSEMINATION_RULE_CODE_LIST_VERSION
                ) +
                "</DisseminationRuleCodeListVersion>"
            );
            // Conf
            metadonneesFichierXML.append(
                "<ReuseRuleCodeListVersion>" +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_REUSE_RULE_CODE_LIST_VERSION) +
                "</ReuseRuleCodeListVersion>"
            );
            // ClassificationRuleCodeListVersion
            metadonneesFichierXML.append(
                "<ClassificationRuleCodeListVersion>" +
                configService.getValue(
                    SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_CLASSIFICATION_RULE_CODE_LIST_VERSION
                ) +
                "</ClassificationRuleCodeListVersion>"
            );
            // Conf
            metadonneesFichierXML.append(
                "<AcquisitionInformationCodeListVersion>" +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ACQUISITION_INFORMATION) +
                "</AcquisitionInformationCodeListVersion>"
            );
            // AuthorizationCodeListVersion
            metadonneesFichierXML.append(
                "<AuthorizationReasonCodeListVersion>" +
                configService.getValue(
                    SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_AUTHORIZATION_CODE_LIST_VERSION
                ) +
                "</AuthorizationReasonCodeListVersion>"
            );
            // RelationshipCodeListVersion
            metadonneesFichierXML.append(
                "<RelationshipCodeListVersion>" +
                configService.getValue(
                    SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_RELATIONSHIP_CODE_LIST_VERSION
                ) +
                "</RelationshipCodeListVersion>"
            );
            metadonneesFichierXML.append("</CodeListVersions>");
            metadonneesFichierXML.append("<DataObjectPackage>");
            StringBuffer finFichierXML = new StringBuffer();
            finFichierXML.append("<ManagementMetadata>");
            // Conf
            finFichierXML.append(
                "<ArchivalProfile>" + getValideArchivalProfile(documentManager, dossier) + "</ArchivalProfile>"
            );
            // Conf
            finFichierXML.append(
                "<ServiceLevel>" +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_SERVICE_LEVEL) +
                "</ServiceLevel>"
            );
            finFichierXML.append("<AcquisitionInformation>" + "Versement" + "</AcquisitionInformation>");
            // Conf
            finFichierXML.append(
                "<OriginatingAgencyIdentifier>" +
                parametrageAdamantService
                    .getParametrageAdamantDocument(documentManager)
                    .getOriginatingAgencyIdentifier() +
                "</OriginatingAgencyIdentifier>"
            );
            // Conf
            finFichierXML.append(
                "<SubmissionAgencyIdentifier>" +
                parametrageAdamantService
                    .getParametrageAdamantDocument(documentManager)
                    .getSubmissionAgencyIdentifier() +
                "</SubmissionAgencyIdentifier>"
            );
            finFichierXML.append("</ManagementMetadata>");
            finFichierXML.append("</DataObjectPackage>");
            finFichierXML.append("<ArchivalAgency>");
            // Conf
            finFichierXML.append(
                TAG_IDENTIFIER_BEG +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ARCHIVAL_AGENCY_IDENTIFIER) +
                TAG_IDENTIFIER_END
            );
            finFichierXML.append("</ArchivalAgency>");
            finFichierXML.append("<TransferringAgency>");
            // Conf
            finFichierXML.append(
                TAG_IDENTIFIER_BEG +
                configService.getValue(
                    SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_TRANSFERING_AGENCY_IDENTIFIER
                ) +
                TAG_IDENTIFIER_END
            );
            finFichierXML.append("</TransferringAgency>");
            finFichierXML.append("</ArchiveTransfer>");
            ARCHIVAGE_LOGGER.debug(STLogEnumImpl.DEFAULT, "Initialisation des buffers faite");

            Integer countIds = 1;
            AtomicInteger aAAUICount = new AtomicInteger(1);
            StringBuffer binaryDataObjectsXML = new StringBuffer();

            StringBuffer descriptiveMetadataXML = new StringBuffer();
            descriptiveMetadataXML.append("<DescriptiveMetadata>");
            // Archive unit pour le document de manière globale
            descriptiveMetadataXML.append("<ArchiveUnit id=\"ID01\">");
            countIds = countIds + 1;
            descriptiveMetadataXML.append("<Management>");
            descriptiveMetadataXML.append("<AppraisalRule>");
            // conf
            descriptiveMetadataXML.append(
                TAG_RULE_BEG +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_APPRAISAL_RULE) +
                TAG_RULE_END
            );

            // Date de JO si Publié, date de dernière étape de FdR sinon - au
            // format AAAA-MM-JJ
            String datePublicationOuFinDossier = "";
            ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
            Boolean dossierReprise = archiveService.isDossierReprise(dossier);

            if (dossier.getStatut().equals(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION)) {
                // Date du journal pour le passage de la dernière étape de la fdr
                Date latest = null;
                if (listLogEntryJournal != null) {
                    for (LogEntry logEntryJournal : listLogEntryJournal) {
                        if (
                            "label.journal.comment.terminerDossierSansPublication".equals(
                                    logEntryJournal.getComment()
                                ) &&
                            (latest == null || latest.before(logEntryJournal.getEventDate()))
                        ) {
                            latest = logEntryJournal.getEventDate();
                        }
                    }
                } else {
                    // impossible de récupérer le journal
                    ARCHIVAGE_LOGGER.info(STLogEnumImpl.DEFAULT, dossier.getNumeroNor() + MESSAGE_ERROR_GET_JOURNAL);
                }
                if (
                    Boolean.TRUE.equals(dossierReprise) &&
                    !(latest != null && latest.after(dossier.getDateCreation().getTime()))
                ) {
                    if (dossier.getDateCreation() != null) {
                        datePublicationOuFinDossier =
                            SolonDateConverter.DATE_DASH_REVERSE.format(
                                new Date(dossier.getDateCreation().getTimeInMillis())
                            );
                    } else {
                        ARCHIVAGE_LOGGER.info(
                            STLogEnumImpl.DEFAULT,
                            dossier.getNumeroNor() + " -> date de création null"
                        );
                    }
                } else {
                    datePublicationOuFinDossier = SolonDateConverter.DATE_DASH_REVERSE.format(latest);
                }
            } else if (dossier.getStatut().equals(VocabularyConstants.STATUT_CLOS)) {
                if (listLogEntryJournal != null) {
                    // On récupère l'entrée relative à la FdR la plus récente
                    Date latest = null;
                    for (LogEntry logEntryJournal : listLogEntryJournal) {
                        if (latest == null || latest.before(logEntryJournal.getEventDate())) {
                            latest = logEntryJournal.getEventDate();
                        }
                    }
                    if (latest != null) {
                        datePublicationOuFinDossier = SolonDateConverter.DATE_DASH_REVERSE.format(latest);
                    }
                } else {
                    // impossible de récupérer le journal
                    ARCHIVAGE_LOGGER.info(STLogEnumImpl.DEFAULT, dossier.getNumeroNor() + MESSAGE_ERROR_GET_JOURNAL);
                }
            } else if (dossier.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
                // Date de JO si Publié
                Calendar datePublication = getDatePublicationFromRetourDila(dossier);
                if (datePublication != null) {
                    datePublicationOuFinDossier = SolonDateConverter.DATE_DASH_REVERSE.format(datePublication);
                }
            }
            descriptiveMetadataXML.append(TAG_STARTDATE_BEG + datePublicationOuFinDossier + TAG_STARTDATE_END);
            descriptiveMetadataXML.append(
                "<FinalAction>" +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_APPRAISAL_RULE_FINAL_ACTION) +
                "</FinalAction>"
            );
            descriptiveMetadataXML.append("</AppraisalRule>");

            descriptiveMetadataXML.append("<AccessRule>");
            // conf
            descriptiveMetadataXML.append(
                TAG_RULE_BEG + getAccessRuleFromTypeActe(dossier, configService) + TAG_RULE_END
            );
            descriptiveMetadataXML.append(TAG_STARTDATE_BEG + datePublicationOuFinDossier + TAG_STARTDATE_END);
            descriptiveMetadataXML.append("</AccessRule>");

            descriptiveMetadataXML.append("<DisseminationRule>");
            // conf
            descriptiveMetadataXML.append(
                TAG_RULE_BEG +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_DISSEMINATION_RULE) +
                TAG_RULE_END
            );
            descriptiveMetadataXML.append(TAG_STARTDATE_BEG + datePublicationOuFinDossier + TAG_STARTDATE_END);
            descriptiveMetadataXML.append("</DisseminationRule>");

            descriptiveMetadataXML.append("<ReuseRule>");
            // conf
            descriptiveMetadataXML.append(
                TAG_RULE_BEG +
                configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_REUSE_RULE) +
                TAG_RULE_END
            );
            descriptiveMetadataXML.append(TAG_STARTDATE_BEG + datePublicationOuFinDossier + TAG_STARTDATE_END);
            descriptiveMetadataXML.append("</ReuseRule>");

            descriptiveMetadataXML.append("</Management>");

            writeContentArchiveUnit(
                documentManager,
                descriptiveMetadataXML,
                dossier,
                datePublicationOuFinDossier,
                "NOR N°" + dossier.getNumeroNor(),
                true,
                null,
                true,
                RECORD_GRP,
                null,
                null,
                null,
                messageIdentifierTarget,
                null,
                null,
                aAAUICount
            );

            // Export des documents du parapheur + versions précédentes
            LOGGER.debug(
                STLogEnumImpl.DEFAULT,
                "Export des fichiers du parapheur ainsi que leurs versions précédentes"
            );
            countIds =
                exportParapheurForArchivage(
                    dossier,
                    countIds,
                    zip,
                    binaryDataObjectsXML,
                    descriptiveMetadataXML,
                    datePublicationOuFinDossier,
                    session,
                    messageIdentifierTarget,
                    aAAUICount,
                    listLogEntryJournal
                );
            // Export des documents du fond de dossier + versions précédentes
            LOGGER.debug(
                STLogEnumImpl.DEFAULT,
                "Export des fichiers du fond de dossier ainsi que leurs versions précédentes"
            );
            countIds =
                exportFDDForArchivage(
                    dossier,
                    countIds,
                    zip,
                    binaryDataObjectsXML,
                    descriptiveMetadataXML,
                    datePublicationOuFinDossier,
                    session,
                    messageIdentifierTarget,
                    aAAUICount,
                    listLogEntryJournal
                );
            // Bordereau
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Export du PDF du bordereau");
            descriptiveMetadataXML.append(
                TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">"
            );
            countIds = countIds + 1;
            ByteArrayOutputStream pdfBaosBordereau = pdfGeneratorService.addBordereauToZip(
                session,
                zip,
                dossier,
                "ID" + String.format("%02d", countIds)
            );
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Génération du PDF du bordereau faite");
            // Génération du binaryDataObject
            docGenereToBinaryDataOBject("bordereau.pdf", countIds, binaryDataObjectsXML, pdfBaosBordereau);
            // Génération de l'archiveUnit
            writeContentArchiveUnit(
                documentManager,
                descriptiveMetadataXML,
                dossier,
                datePublicationOuFinDossier,
                SolonEpgAdamantConstant.BORDEREAU,
                false,
                null,
                false,
                "Item",
                null,
                null,
                null,
                messageIdentifierTarget,
                null,
                null,
                aAAUICount
            );

            descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_BEG);
            descriptiveMetadataXML.append(
                TAG_DATAOBJECTREFERENCEID_BEG + "ID" + String.format("%02d", countIds) + TAG_DATAOBJECTREFERENCEID_END
            );
            descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_END);
            countIds = countIds + 1;
            descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_END);
            // Feuille de route
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Export du PDF de la feuille de route");
            descriptiveMetadataXML.append(
                TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">"
            );
            countIds = countIds + 1;
            ByteArrayOutputStream pdfBaosFDR = pdfGeneratorService.addFeuilleRouteToZip(
                zip,
                dossier,
                getDocumentRouteTableElementList(dossier.getDocument()),
                session,
                "ID" + String.format("%02d", countIds)
            );
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Génération du PDF de la feuille de route faite");
            // Génération du binaryDataObject
            docGenereToBinaryDataOBject("feuilleRoute.pdf", countIds, binaryDataObjectsXML, pdfBaosFDR);
            // Génération de l'archiveUnit
            writeContentArchiveUnit(
                documentManager,
                descriptiveMetadataXML,
                dossier,
                datePublicationOuFinDossier,
                SolonEpgAdamantConstant.FEUILLE_ROUTE,
                false,
                null,
                false,
                "Item",
                null,
                null,
                null,
                messageIdentifierTarget,
                null,
                null,
                aAAUICount
            );

            descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_BEG);
            descriptiveMetadataXML.append(
                TAG_DATAOBJECTREFERENCEID_BEG + "ID" + String.format("%02d", countIds) + TAG_DATAOBJECTREFERENCEID_END
            );
            descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_END);
            countIds = countIds + 1;
            descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_END);
            // Journal
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Export du Journal");
            descriptiveMetadataXML.append(
                TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">"
            );
            countIds = countIds + 1;
            ByteArrayOutputStream pdfBaosJournal = pdfGeneratorService.addJournalToZip(
                session,
                zip,
                dossier,
                listLogEntryJournal,
                "ID" + String.format("%02d", countIds)
            );
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Export du Journal fait");
            // Génération du binaryDataObject
            docGenereToBinaryDataOBject("journal.pdf", countIds, binaryDataObjectsXML, pdfBaosJournal);
            // Génération de l'archiveUnit
            writeContentArchiveUnit(
                documentManager,
                descriptiveMetadataXML,
                dossier,
                datePublicationOuFinDossier,
                SolonEpgAdamantConstant.JOURNAL,
                false,
                null,
                false,
                "Item",
                null,
                null,
                null,
                messageIdentifierTarget,
                null,
                null,
                aAAUICount
            );

            descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_BEG);
            descriptiveMetadataXML.append(
                TAG_DATAOBJECTREFERENCEID_BEG + "ID" + String.format("%02d", countIds) + TAG_DATAOBJECTREFERENCEID_END
            );
            descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_END);
            countIds = countIds + 1;
            descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_END);
            // Traitement Papier
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Export du PDF du traitement papier");
            descriptiveMetadataXML.append(
                TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">"
            );
            countIds = countIds + 1;
            ByteArrayOutputStream pdfBaosTP = pdfGeneratorService.addTraitementPapierToZip(
                session,
                zip,
                dossier,
                "ID" + String.format("%02d", countIds)
            );
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Export du PDF du traitement papier fait");
            // Génération du binaryDataObject
            docGenereToBinaryDataOBject("traitementPapier.pdf", countIds, binaryDataObjectsXML, pdfBaosTP);
            // Génération de l'archiveUnit
            writeContentArchiveUnit(
                documentManager,
                descriptiveMetadataXML,
                dossier,
                datePublicationOuFinDossier,
                SolonEpgAdamantConstant.TRAITEMENT_PAPIER,
                false,
                null,
                false,
                "Item",
                null,
                null,
                null,
                messageIdentifierTarget,
                null,
                null,
                aAAUICount
            );

            descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_BEG);
            descriptiveMetadataXML.append(
                TAG_DATAOBJECTREFERENCEID_BEG + "ID" + String.format("%02d", countIds) + TAG_DATAOBJECTREFERENCEID_END
            );
            descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_END);
            countIds = countIds + 1;
            descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_END);
            descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_END);
            descriptiveMetadataXML.append("</DescriptiveMetadata>");

            // TLD : Le contenu du fichier a bien été généré au compteur près,
            // on remplace la messageIdentifierTarget par une vraie valeur
            String messageIdentifierArchive =
                parametrageAdamantService.getParametrageAdamantDocument(documentManager).getNumeroSolon() +
                "_" +
                getIdentifiantSequentielArchivage();
            String enteteFichierStr = enteteFichierXML
                .toString()
                .replace(messageIdentifierTarget, messageIdentifierArchive);
            String metadonneesFichierStr = metadonneesFichierXML
                .toString()
                .replace(messageIdentifierTarget, messageIdentifierArchive);
            String binaryDataObjectsStr = binaryDataObjectsXML
                .toString()
                .replace(messageIdentifierTarget, messageIdentifierArchive);
            String descriptiveMetadataStr = descriptiveMetadataXML
                .toString()
                .replace(messageIdentifierTarget, messageIdentifierArchive);
            String finFichierStr = finFichierXML.toString().replace(messageIdentifierTarget, messageIdentifierArchive);

            // Génération fichier XML
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Génération du fichier XML");
            ZipEntry entry = new ZipEntry("manifest.xml");
            zip.putNextEntry(entry);
            zip.write(enteteFichierStr.getBytes());
            zip.write(metadonneesFichierStr.getBytes());
            zip.write(binaryDataObjectsStr.getBytes());
            zip.write(descriptiveMetadataStr.getBytes());
            zip.write(finFichierStr.getBytes());
            zip.closeEntry();
            zip.close();
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Génération du fichier XML fait");

            SolonEpgAdamantServiceLocator
                .getBordereauService()
                .completeBordereau(bordereau, new File(zipFile), messageIdentifierArchive);

            LOGGER.info(
                STLogEnumImpl.DEFAULT,
                "Dossier " + dossier.getNumeroNor() + " exporté pour archivage définitif"
            );
        } catch (Exception e) {
            LOGGER.error(STLogEnumImpl.DEFAULT, e);
            new File(zipFile).delete();
            throw new NuxeoException(e);
        }
    }

    @Override
    public File getOrCreateExtractionLotFolder(DossierExtractionLot lot) {
        StringBuilder extractionDir = new StringBuilder(getPathDirArchivageDefinitive());
        extractionDir
            .append(SolonEpgAdamantConstant.FILE_SEPARATOR)
            .append(SolonDateConverter.DATETIME_DASH_REVERSE_SECOND_DASH.format(lot.getCreationDate(), false));

        File extractionFolder = new File(extractionDir.toString());
        if (!extractionFolder.exists()) {
            extractionFolder.mkdirs();
        }

        return extractionFolder;
    }

    @Override
    public List<LogEntry> getLogEntryList(String idDossier) {
        JournalService journalService = STServiceLocator.getJournalService();
        return journalService.getLogEntries(idDossier);
    }

    /**
     * Récupère la liste des DocumentRouteTableElement à partir de la liste des
     * SelectDataModelRow.
     *
     * @param SelectDataModelRowList
     * @return
     */
    private List<RouteTableElement> getDocumentRouteTableElementList(DocumentModel currentDocument) {
        final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        DocumentModel dossierRoute = dossierDistributionService.getLastDocumentRouteForDossier(
            session,
            currentDocument
        );
        if (dossierRoute == null) {
            return new ArrayList<>();
        } else {
            SSFeuilleRoute currentRoute = dossierRoute.getAdapter(SSFeuilleRoute.class);
            return getElements(currentRoute);
        }
    }

    /**
     * Retourne le label qui correspond au pictogramme pour une étape de feuille
     * de route
     *
     * @param dossier
     * @param configService
     * @return
     */
    private String getAccessRuleFromTypeActe(Dossier dossier, ConfigService configService) {
        String strTmp = configService.getValue(
            SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_LIST_ACC_00002
        );
        List<String> listAcc00002 = new ArrayList<>(Arrays.asList(strTmp.split(",")));
        strTmp = configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_LIST_ACC_00003);
        List<String> listAcc00003 = new ArrayList<>(Arrays.asList(strTmp.split(",")));
        strTmp = configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_LIST_ACC_00020);
        List<String> listAcc00020 = new ArrayList<>(Arrays.asList(strTmp.split(",")));
        strTmp = configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_LIST_ACC_00025);
        List<String> listAcc00025 = new ArrayList<>(Arrays.asList(strTmp.split(",")));

        String rule = "";
        if (listAcc00002.contains(dossier.getTypeActe())) {
            rule = VocabularyConstants.ACCESS_RULE_ACC_00002;
        } else if (listAcc00003.contains(dossier.getTypeActe())) {
            rule = VocabularyConstants.ACCESS_RULE_ACC_00003;
        } else if (listAcc00020.contains(dossier.getTypeActe())) {
            rule = VocabularyConstants.ACCESS_RULE_ACC_00020;
        } else if (listAcc00025.contains(dossier.getTypeActe())) {
            rule = VocabularyConstants.ACCESS_RULE_ACC_00025;
        }
        return rule;
    }

    /**
     * Ecrit la partie Content des ArchiveUnit pour un répertoire donné pour
     * l'archivage définitif
     *
     * @param descriptiveMetadata
     *            : StringBuffer pour écriture des descriptiveMetadata
     * @param dossier
     *            : dossier courant
     * @param datePublicationOuFinDossier
     *            : date de publication ou de fin du dossier pour export
     * @param title
     *            : titre du répertoire ou peut aussi indiquer les versions
     *            antérieures
     * @param isMainParent
     *            : Boolean. Info sur si le répertoire est le répertoire
     *            principal
     * @param version
     *            : Version courante pour les versions antérieures
     * @param hasDate
     *            : Boolean. Info sur si on renseigne ou non les dates de début
     *            et de fin
     */
    private void writeContentArchiveUnit(
        CoreSession documentManager,
        StringBuffer descriptiveMetadata,
        Dossier dossier,
        String datePublicationOuFinDossier,
        String title,
        Boolean isMainParent,
        Integer version,
        Boolean hasDate,
        String descriptionLevel,
        String dateDerniereVersion,
        FileSolonEpg documentToExport,
        Integer compteurDocumentDansDossier,
        String messageIdentifierArchive,
        String parentImmediatSiHorsPlanDeClassement,
        Integer nombreDeVersion,
        AtomicInteger countAAAUI
    ) {
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Génération de l'archiveUnit, début");
        ParametrageAdamantService parametrageAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();
        descriptiveMetadata.append("<Content>");
        descriptiveMetadata.append("<DescriptionLevel>").append(descriptionLevel).append("</DescriptionLevel>");
        if (title.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_lETTRES_ACCORD)) {
            title = "Lettres d’accord";
        }
        String titleArchiveUnit = title;
        // M157345 - Remplacement de Etat par État
        if (title.contains("Etat")) {
            title = title.replace("Etat", "État");
        }
        if (
            compteurDocumentDansDossier != null &&
            !title.equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME) &&
            !SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_NON_CLASIFIED.equals(title) &&
            !title.contains("Version antérieure")
        ) {
            if ("Documents".equals(title)) {
                // M157338
                title =
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_NON_CLASIFIED +
                    ", " +
                    parentImmediatSiHorsPlanDeClassement;
            }
            titleArchiveUnit = title + ": Document " + compteurDocumentDansDossier.toString();
        }
        descriptiveMetadata.append("<Title>").append(escapeXml(titleArchiveUnit)).append("</Title>");
        descriptiveMetadata
            .append("<ArchivalAgencyArchiveUnitIdentifier>")
            .append(messageIdentifierArchive)
            .append("_")
            .append(countAAAUI.toString())
            .append("</ArchivalAgencyArchiveUnitIdentifier>");
        countAAAUI.incrementAndGet();

        String datePublicationJOString = "";
        RetourDila retourDila = null;
        if (isMainParent) {
            descriptiveMetadata
                .append("<OriginatingAgencyArchiveUnitIdentifier>")
                .append(dossier.getNumeroNor())
                .append("</OriginatingAgencyArchiveUnitIdentifier>");

            // Description - Si le texte a été publié -> Titre officiel sous
            // lequel le texte a été publié
            // Si le text est terminé sans publication ou clos
            // -> Titre de l'acte dans le bordereau
            String titreOfficiel = "";
            if (dossier.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
                retourDila = dossier.getDocument().getAdapter(RetourDila.class);
                if (retourDila != null && retourDila.getTitreOfficiel() != null) {
                    titreOfficiel = retourDila.getTitreOfficiel();
                }
            } else if (
                dossier.getStatut().equals(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION) ||
                dossier.getStatut().equals(VocabularyConstants.STATUT_CLOS)
            ) {
                titreOfficiel = dossier.getTitreActe();
            }
            if (!titreOfficiel.isEmpty()) {
                descriptiveMetadata.append("<Description>").append(escapeXml(titreOfficiel)).append("</Description>");
            } else {
                descriptiveMetadata.append("<Description></Description>");
            }
            String typeActe = "";
            final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
            if (
                !StringUtils.isEmpty(VocabularyConstants.TYPE_ACTE_VOCABULARY) &&
                !StringUtils.isEmpty(dossier.getTypeActe())
            ) {
                typeActe =
                    vocabularyService.getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY, dossier.getTypeActe());
            }
            descriptiveMetadata.append("<DocumentType>").append(typeActe).append("</DocumentType>");
            String statutDossier = "";
            if (
                !StringUtils.isEmpty(VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME) &&
                !StringUtils.isEmpty(dossier.getStatut())
            ) {
                statutDossier =
                    vocabularyService.getEntryLabel(
                        VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME,
                        dossier.getStatut()
                    );
            }
            descriptiveMetadata.append("<Status>").append(statutDossier).append("</Status>");
            if (dossier.getIndexationRubrique() != null && !dossier.getIndexationRubrique().isEmpty()) {
                for (String indexation : dossier.getIndexationRubrique()) {
                    if (indexation.equals(DossierSolonEpgConstants.RUBRIQUE_REPRISE)) {
                        descriptiveMetadata.append("<Tag>").append("REPRISE V1").append("</Tag>");
                    } else {
                        descriptiveMetadata.append("<Tag>").append(indexation).append("</Tag>");
                    }
                }
            }
            descriptiveMetadata.append("<OriginatingAgency>");
            descriptiveMetadata.append(
                TAG_IDENTIFIER_BEG +
                parametrageAdamantService
                    .getParametrageAdamantDocument(documentManager)
                    .getOriginatingAgencyBlocIdentifier() +
                TAG_IDENTIFIER_END
            );
            descriptiveMetadata.append("</OriginatingAgency>");

            descriptiveMetadata.append("<SubmissionAgency>");
            descriptiveMetadata.append(
                TAG_IDENTIFIER_BEG +
                parametrageAdamantService
                    .getParametrageAdamantDocument(documentManager)
                    .getSubmissionAgencyBlocIdentifier() +
                TAG_IDENTIFIER_END
            );
            descriptiveMetadata.append("</SubmissionAgency>");
        } else {
            if (version != null) {
                descriptiveMetadata.append("<Version>V").append(version.toString()).append("</Version>");
            }
            descriptiveMetadata.append("<OriginatingAgency>");
            descriptiveMetadata.append(
                TAG_IDENTIFIER_BEG +
                parametrageAdamantService
                    .getParametrageAdamantDocument(documentManager)
                    .getOriginatingAgencyBlocIdentifier() +
                TAG_IDENTIFIER_END
            );
            descriptiveMetadata.append("</OriginatingAgency>");

            descriptiveMetadata.append("<SubmissionAgency>");
            descriptiveMetadata.append(
                TAG_IDENTIFIER_BEG +
                parametrageAdamantService
                    .getParametrageAdamantDocument(documentManager)
                    .getSubmissionAgencyBlocIdentifier() +
                TAG_IDENTIFIER_END
            );
            descriptiveMetadata.append("</SubmissionAgency>");
        }

        DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        if (hasDate) {
            // Comparaison entre la date de fin et la date de début.
            // Si date de début après date de fin on prend le 1er janvier de
            // l'année de création du dossier
            // car dossier issu de la reprise
            Calendar datePublicationOufinDossierCal = DateUtil.toCalendarFromNotNullDate(
                SolonDateConverter.DATE_DASH_REVERSE.parseToDate(datePublicationOuFinDossier)
            );

            // pour les dossiers de la reprise ou ceux incohérent, la date de
            // création est au 01-01 de l'année du NOR
            // sinon la date de création peut être reprise depuis l'attribut
            // correspondant
            String dateDebutDossier;

            if (
                dossierService.isDossierReprise(dossier) ||
                // incohérent car date de création est apres la date de fin
                (
                    datePublicationOufinDossierCal != null &&
                    !dossier.getDateCreation().before(datePublicationOufinDossierCal)
                )
            ) {
                String anneeDossier = "20" + dossier.getNumeroNor().substring(4, 6) + "-01-01";
                dateDebutDossier = anneeDossier;
            } else {
                dateDebutDossier = SolonDateConverter.DATE_DASH_REVERSE.format(dossier.getDateCreation());
            }

            descriptiveMetadata.append(TAG_STARTDATE_BEG).append(dateDebutDossier).append(TAG_STARTDATE_END);
            descriptiveMetadata.append("<EndDate>").append(datePublicationOuFinDossier).append("</EndDate>");
        }

        if (!isMainParent && hasWriterBloc(title)) {
            descriptiveMetadata.append("<Writer>");
            descriptiveMetadata
                .append("<Corpname>")
                .append(getEntiteDerniereVersion(dossier, documentToExport, version, nombreDeVersion))
                .append("</Corpname>");
            descriptiveMetadata.append("</Writer>");
        }
        if (dateDerniereVersion != null && !dateDerniereVersion.isEmpty()) {
            descriptiveMetadata.append("<RegisteredDate>").append(dateDerniereVersion).append("</RegisteredDate>");
        }

        if (dossier.getStatut().equals(VocabularyConstants.STATUT_PUBLIE) && isMainParent) {
            // Si on est au statut publié on renseigné les balises Event
            if (retourDila != null) {
                Calendar datePublicationJournalOfficielDate = retourDila.getDateParutionJorf();
                if (datePublicationJournalOfficielDate != null) {
                    datePublicationJOString = formatArchiveDateTime(datePublicationJournalOfficielDate.getTime());
                    descriptiveMetadata.append("<Event>");
                    descriptiveMetadata
                        .append("<EventDateTime>")
                        .append(datePublicationJOString)
                        .append("</EventDateTime>");
                    descriptiveMetadata
                        .append("<EventDetail>")
                        .append("Date de publication au journal officiel")
                        .append("</EventDetail>");
                    descriptiveMetadata.append("</Event>");
                }
            }

            // On renseigne publication BO s'il y a eu une publication BO
            if (retourDila != null && !retourDila.getParutionBo().isEmpty()) {
                List<ParutionBo> listParutionBO = retourDila.getParutionBo();
                for (ParutionBo parutionBO : listParutionBO) {
                    Calendar dateParutionBO = parutionBO.getDateParutionBo();
                    String dateParutionBOString = formatArchiveDateTime(dateParutionBO.getTime());
                    descriptiveMetadata.append("<Event>");
                    descriptiveMetadata
                        .append("<EventDateTime>")
                        .append(dateParutionBOString)
                        .append("</EventDateTime>");
                    descriptiveMetadata
                        .append("<EventDetail>")
                        .append("Date de publication au Bulletin officiel")
                        .append("</EventDetail>");
                    descriptiveMetadata.append("</Event>");
                }
            }
        }
        descriptiveMetadata.append("</Content>");
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Génération de l'archiveUnit, fin");
    }

    /**
     * get entité dernière version pour corpname
     */
    private String getEntiteDerniereVersion(
        Dossier dossier,
        FileSolonEpg documentToExport,
        Integer version,
        Integer nombreDeVersion
    ) {
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Get entité dernière version, début");
        String entiteDerniereVersion = null;
        DossierService dossierService = SolonEpgServiceLocator.getDossierService();

        if (dossierService.isDossierReprise(dossier)) {
            try {
                entiteDerniereVersion =
                    STServiceLocator.getSTMinisteresService().getEntiteNode(dossier.getMinistereResp()).getLabel();
            } catch (NullPointerException | NuxeoException e) {
                LOGGER.warn(
                    STLogEnumImpl.DEFAULT,
                    e,
                    "Impossible de récupérer le ministère responsaable du dossier " + dossier.getNumeroNor()
                );
            }
        } else {
            if (documentToExport != null) {
                entiteDerniereVersion = documentToExport.getEntite();
            }
            if (entiteDerniereVersion == null) {
                entiteDerniereVersion = "";
            }
            STMinisteresService stMinistereService = STServiceLocator.getSTMinisteresService();
            if (
                StringUtils.isBlank(entiteDerniereVersion) &&
                (isFirstVersion(version, nombreDeVersion) || hasOnlyOneVersion(version, nombreDeVersion))
            ) {
                // Si on a la première version du fichier et si entité vide
                // alors on prend le ministère de rattachement du
                // dossier
                // Si on a seulement une version on prend aussi le ministère de
                // rattachement du dossier
                try {
                    EntiteNode ministereNode = stMinistereService.getEntiteNode(dossier.getMinistereAttache());
                    if (ministereNode != null) {
                        entiteDerniereVersion = ministereNode.getLabel();
                    }
                } catch (NuxeoException e) {
                    LOGGER.error(
                        STLogEnumImpl.FAIL_GET_MINISTERE_TEC,
                        e,
                        "Impossible de récupérer le ministère de rattachement " +
                        dossier.getMinistereAttache() +
                        " du dossier " +
                        dossier.getNumeroNor()
                    );
                }
            }

            if (
                StringUtils.isBlank(entiteDerniereVersion) &&
                documentToExport != null &&
                "ADMINISTRATEUR".equalsIgnoreCase(documentToExport.getCreator())
            ) {
                entiteDerniereVersion = dossier.getMinistereResp();
            } else if (
                entiteDerniereVersion == null || entiteDerniereVersion.trim().isEmpty() && documentToExport != null
            ) {
                // chercher le ministère de rattachement de l'utilisateur
                // On récupère bien l'id du user
                STUserService stUserService = STServiceLocator.getSTUserService();
                if (documentToExport != null && documentToExport.getCreator() != null) {
                    try {
                        entiteDerniereVersion = stUserService.getUserMinisteres(documentToExport.getCreator());
                    } catch (NuxeoException e) {
                        LOGGER.error(
                            STLogEnumImpl.FAIL_GET_USER_TEC,
                            e,
                            "Erreur lors de la récupération de l'utilisateur " +
                            documentToExport.getCreator() +
                            " " +
                            e.getMessage()
                        );
                    }
                }
            }
        }
        if (entiteDerniereVersion == null || entiteDerniereVersion.isEmpty()) {
            // Si pas trouvé on met non renseigné
            entiteDerniereVersion = "Non renseigné";
        }
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Get entité dernière version, fin");
        return entiteDerniereVersion;
    }

    private boolean hasOnlyOneVersion(Integer version, Integer nombreDeVersion) {
        return version == null && nombreDeVersion != null && nombreDeVersion.equals(1);
    }

    private boolean isFirstVersion(Integer version, Integer nombreDeVersion) {
        return version != null && nombreDeVersion != null && nombreDeVersion.equals(version);
    }

    private boolean hasWriterBloc(String title) {
        return !SolonEpgAdamantConstant.DOC_WITHOUT_WRITE_BLOC_LIST.contains(title);
    }

    /**
     * get le path du répertoire qui va contenir l'archivage definitive
     *
     * @param session
     * @return
     */
    private String getPathDirArchivageDefinitive() {
        ConfigService configService = STServiceLocator.getConfigService();
        String generatedArchivageDefinitivePath = configService.getValue(
            SolonEpgAdamantConstant.SOLON_ARCHIVAGE_REPERTOIRE
        );

        File generatedArchivageDefinitiveDir = new File(generatedArchivageDefinitivePath);
        if (!generatedArchivageDefinitiveDir.exists()) {
            generatedArchivageDefinitiveDir.mkdirs();
        }
        return generatedArchivageDefinitivePath;
    }

    /**
     * Export des documents du Fond de dossier + versions précédentes On suit
     * l'ordre de l'arborescence SIP
     *
     * @param dossier
     *            : dossier courant
     * @param countDocument
     *            : compteur pour générer les id des fichiers du FDD
     * @param zip
     *            : zip vers lequel on exporte
     * @param binaryDataObjects
     *            : stringBuffer pour inscription des binaryDataObjects dans le
     *            XML
     * @param countArchiveUnit
     *            : compteur pour générer les id des archiveUnit
     * @param descriptiveMetadata
     *            : StringBuffer pour inscription des descriptiveMetadata dans
     *            le XML
     * @param datePublicationOuFinDossier
     *            : Date publication ou fin du dossier pour inscription dans le
     *            XML
     * @param countAAAUI
     *            : Compteur pour génerer les AchivalAgencyArchiveUnitIdentifier
     * @throws Exception
     */
    private Integer exportFDDForArchivage(
        Dossier dossier,
        Integer countIds,
        ZipOutputStream zip,
        StringBuffer binaryDataObjects,
        StringBuffer descriptiveMetadata,
        String datePublicationOuFinDossier,
        CoreSession documentManager,
        String messageIdentifierArchive,
        AtomicInteger countAAAUI,
        List<LogEntry> listLogEntryJournal
    )
        throws Exception {
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Export des documents du Fond de dossier, début");
        final FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
        Integer compteurDocDansDossier = 1;

        // Liste des documents exportés
        List<DocumentModel> exportedDoc = new ArrayList<>();

        // Export des documents du fond de dossier
        // Pour chaque répertoire du fond de dossier on va parcourir la même
        // chose
        // Nécéssité de gérer les folders parents
        // Répertoire réservé au SGG
        String folderName = SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG;
        List<DocumentModel> documentsToExportFDD = new ArrayList<>();
        try {
            documentsToExportFDD =
                fondDeDossierService.getAllFilesFromFondDeDossierForArchivage(documentManager, dossier, folderName);
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Récupération de tous les %s fichiers du répertoire %s faite", documentsToExportFDD.size(), folderName));
            // Export des documents du fond de dossier
            for (DocumentModel docToExportFDD : documentsToExportFDD) {
                countIds =
                    exportDocumentAndWriteXML(
                        countIds,
                        docToExportFDD,
                        dossier,
                        zip,
                        binaryDataObjects,
                        descriptiveMetadata,
                        datePublicationOuFinDossier,
                        documentManager,
                        folderName,
                        compteurDocDansDossier,
                        messageIdentifierArchive,
                        null,
                        countAAAUI,
                        listLogEntryJournal
                    );
                compteurDocDansDossier = compteurDocDansDossier + 1;
                exportedDoc.add(docToExportFDD);
            }
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Export de tous les %s fichiers du répertoire %s fait", documentsToExportFDD.size(), folderName));
        } catch (EPGException e) {
            // DO nothing. C'est au cas où le répertoire n'est pas trouvé
            ARCHIVAGE_LOGGER.warn(STLogEnumImpl.DEFAULT, e, String.format("Répertoire %s non trouvé.", folderName));
        }
        compteurDocDansDossier = 1;
        // Répertoire réservé au ministère porteur et au SGG
        try {
            folderName = SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG;
            documentsToExportFDD =
                fondDeDossierService.getAllFilesFromFondDeDossierForArchivage(documentManager, dossier, folderName);
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Récupération de tous les %s fichiers du répertoire %s faite", documentsToExportFDD.size(), folderName));
            for (DocumentModel docToExportFDD : documentsToExportFDD) {
                countIds =
                    exportDocumentAndWriteXML(
                        countIds,
                        docToExportFDD,
                        dossier,
                        zip,
                        binaryDataObjects,
                        descriptiveMetadata,
                        datePublicationOuFinDossier,
                        documentManager,
                        folderName,
                        compteurDocDansDossier,
                        messageIdentifierArchive,
                        null,
                        countAAAUI,
                        listLogEntryJournal
                    );
                compteurDocDansDossier = compteurDocDansDossier + 1;
                exportedDoc.add(docToExportFDD);
            }
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Export de tous les %s fichiers du répertoire %s fait", documentsToExportFDD.size(), folderName));
        } catch (EPGException e) {
            // DO nothing. C'est au cas où le répertoire n'est pas trouvé
            ARCHIVAGE_LOGGER.warn(STLogEnumImpl.DEFAULT, e, String.format("Répertoire %s non trouvé.", folderName));
        }
        compteurDocDansDossier = 1;

        // On liste les répertoires contenus dans Documentation Jointe
        List<String> listRepertoiresPlanClassementDocumentationJointe = new ArrayList<>();
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ANNEXES_NON_PUBLIEES
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_DIVERS
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DIVERS
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ETUDE_IMPACT
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EXPOSE_MOTIF
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_LETTRE_SAISINE_CE
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_lETTRES_ACCORD
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_LISTE_COM_GOUV
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_NOTE_MIN
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RAPPORT_PRESENTATION
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_TEXTE_CONSOLIDES
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_TEXTE_REF
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_SAISINE_RECTIFICATIVE
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_PDF_ORIGINAL_SIGNE
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES
        );

        // On vérifie que le répertoire 'Répertoire accessible à tous les
        // utilisateurs' et 'Documentation jointe' ont
        // bien des documents dedans
        // Content
        if (
            fondDeDossierService.hasFichiersInFoldersInFDD(
                documentManager,
                dossier,
                listRepertoiresPlanClassementDocumentationJointe
            )
        ) {
            // Répertoire accessible à tous les utilisateurs
            // Ecriture de l'archive unit
            descriptiveMetadata.append(
                TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">"
            );
            countIds = countIds + 1;
            writeContentArchiveUnit(
                documentManager,
                descriptiveMetadata,
                dossier,
                datePublicationOuFinDossier,
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER,
                false,
                null,
                false,
                RECORD_GRP,
                null,
                null,
                null,
                messageIdentifierArchive,
                null,
                null,
                countAAAUI
            );
            // Documentation jointe
            // Ecriture de l'archive unit
            descriptiveMetadata.append(
                TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">"
            );
            countIds = countIds + 1;
            // Content
            writeContentArchiveUnit(
                documentManager,
                descriptiveMetadata,
                dossier,
                datePublicationOuFinDossier,
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE,
                false,
                null,
                false,
                RECORD_GRP,
                null,
                null,
                null,
                messageIdentifierArchive,
                null,
                null,
                countAAAUI
            );

            // On parcours la liste des répertoires de documentation jointe
            for (String nomRepertoire : listRepertoiresPlanClassementDocumentationJointe) {
                try {
                    // on récupère tous les fichiers de ce répertoire
                    documentsToExportFDD =
                        fondDeDossierService.getAllFilesFromFondDeDossierForArchivage(
                            documentManager,
                            dossier,
                            nomRepertoire
                        );

                    for (DocumentModel docToExportFDD : documentsToExportFDD) {
                        countIds =
                            exportDocumentAndWriteXML(
                                countIds,
                                docToExportFDD,
                                dossier,
                                zip,
                                binaryDataObjects,
                                descriptiveMetadata,
                                datePublicationOuFinDossier,
                                documentManager,
                                nomRepertoire,
                                compteurDocDansDossier,
                                messageIdentifierArchive,
                                null,
                                countAAAUI,
                                listLogEntryJournal
                            );
                        compteurDocDansDossier = compteurDocDansDossier + 1;
                        exportedDoc.add(docToExportFDD);
                    }
                } catch (EPGException e) {
                    // DO nothing. C'est au cas où le répertoire n'est pas
                    // trouvé
                    ARCHIVAGE_LOGGER.warn(
                        STLogEnumImpl.DEFAULT,
                        e,
                        String.format("Répertoire %s non trouvé.", folderName)
                    );
                    continue;
                }
                compteurDocDansDossier = 1;
            }
            descriptiveMetadata.append(TAG_ARCHIVEUNIT_END);
            descriptiveMetadata.append(TAG_ARCHIVEUNIT_END);
        }
        // Documents divers hors plan de classement
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE
        );
        listRepertoiresPlanClassementDocumentationJointe.add(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR
        );
        try {
            documentsToExportFDD =
                fondDeDossierService.getAllFilesFromFondDeDossierForArchivageOutOfFolder(
                    documentManager,
                    dossier,
                    listRepertoiresPlanClassementDocumentationJointe
                );
            documentsToExportFDD.removeAll(exportedDoc); // on retire les
            // documents déjà
            // exportés
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Récupération de tous les %s fichiers des répertoires %s faite", documentsToExportFDD.size(), listRepertoiresPlanClassementDocumentationJointe));
            if (documentsToExportFDD != null && !documentsToExportFDD.isEmpty()) {
                // Ecriture de l'archive unit
                descriptiveMetadata.append(
                    TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">"
                );
                countIds = countIds + 1;
                // Content
                writeContentArchiveUnit(
                    documentManager,
                    descriptiveMetadata,
                    dossier,
                    datePublicationOuFinDossier,
                    "Documents hors plan de classement",
                    false,
                    null,
                    false,
                    RECORD_GRP,
                    null,
                    null,
                    compteurDocDansDossier,
                    messageIdentifierArchive,
                    null,
                    null,
                    countAAAUI
                );
                for (DocumentModel docToExportFDD : documentsToExportFDD) {
                    String titreDossierParent = "";
                    DocumentModel parentRefDoc = documentManager.getDocument(docToExportFDD.getParentRef());
                    if (parentRefDoc != null) {
                        FondDeDossierFolder fondDedossierParent = parentRefDoc.getAdapter(FondDeDossierFolder.class);
                        titreDossierParent = fondDedossierParent.getTitle();
                    }
                    countIds =
                        exportDocumentAndWriteXML(
                            countIds,
                            docToExportFDD,
                            dossier,
                            zip,
                            binaryDataObjects,
                            descriptiveMetadata,
                            datePublicationOuFinDossier,
                            documentManager,
                            "Documents",
                            compteurDocDansDossier,
                            messageIdentifierArchive,
                            titreDossierParent,
                            countAAAUI,
                            listLogEntryJournal
                        );
                    compteurDocDansDossier = compteurDocDansDossier + 1;
                }
                descriptiveMetadata.append(TAG_ARCHIVEUNIT_END);
            }
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Export de tous les %s fichiers des répertoires %s fait", documentsToExportFDD.size(), listRepertoiresPlanClassementDocumentationJointe));
        } catch (EPGException e) {
            // DO nothing. C'est au cas où le répertoire n'est pas trouvé
            ARCHIVAGE_LOGGER.warn(STLogEnumImpl.DEFAULT, e, String.format("Répertoire %s non trouvé.", folderName));
        }
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Export des documents du Fond de dossier, fin");
        return countIds;
    }

    /**
     * Export des documents du Parapheur + versions précédentes On suit l'ordre
     * de l'arborescence SIP
     *
     * @param dossier
     *            : dossier courant
     * @param countDocument
     *            : compteur pour générer les id des fichiers qu'on exporte
     * @param zip
     *            : zip vers lequel est réalisé l'export
     * @param binaryDataObjects
     *            : StringBuffer pour inscription des binaryDataObjects dans le
     *            XML
     * @param descriptiveMetadata
     *            : StringBuffer pour inscription des descriptiveMetadata dans
     *            le XML
     * @param datePublicationOuFinDossier
     *            : date de publication ou de fin du dossier pour inscription
     *            dans le XML
     * @param countArchiveUnit
     *            : Compteur pour générer les id des archiveUnit
     * @param countAAAUI
     *            : Compteur pour génerer les AchivalAgencyArchiveUnitIdentifier
     * @return
     * @throws Exception
     */
    private Integer exportParapheurForArchivage(
        Dossier dossier,
        Integer countIds,
        ZipOutputStream zip,
        StringBuffer binaryDataObjects,
        StringBuffer descriptiveMetadata,
        String datePublicationOuFinDossier,
        CoreSession documentManager,
        String messageIdentifierArchive,
        AtomicInteger countAAAUI,
        List<LogEntry> listLogEntryJournal
    )
        throws Exception {
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Export des documents du Parapheur, début");
        final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
        String nomRepertoire = SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME;
        List<DocumentModel> documentsToExportParapheur = new ArrayList<>();
        Integer compteurDocDansDossier = 1;
        // Acte intégral
        try {
            documentsToExportParapheur =
                parapheurService.getAllFilesFromParapheurForArchivage(documentManager, dossier, nomRepertoire);
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Récupération de tous les %s fichiers du répertoire %s faite", documentsToExportParapheur.size(), nomRepertoire));
            for (DocumentModel docToExportParapheur : documentsToExportParapheur) {
                countIds =
                    exportDocumentAndWriteXML(
                        countIds,
                        docToExportParapheur,
                        dossier,
                        zip,
                        binaryDataObjects,
                        descriptiveMetadata,
                        datePublicationOuFinDossier,
                        documentManager,
                        nomRepertoire,
                        compteurDocDansDossier,
                        messageIdentifierArchive,
                        null,
                        countAAAUI,
                        listLogEntryJournal
                    );
                compteurDocDansDossier = compteurDocDansDossier + 1;
            }
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Export de tous les %s fichiers du répertoire %s fait", documentsToExportParapheur.size(), nomRepertoire));
        } catch (EPGException e) {
            // DO nothing. C'est au cas où le répertoire n'est pas trouvé
            ARCHIVAGE_LOGGER.warn(STLogEnumImpl.DEFAULT, e, String.format("Répertoire %s non trouvé.", nomRepertoire));
        }
        compteurDocDansDossier = 1;
        // extrait
        try {
            nomRepertoire = SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME;
            documentsToExportParapheur =
                parapheurService.getAllFilesFromParapheurForArchivage(documentManager, dossier, nomRepertoire);
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Récupération de tous les %s fichiers du répertoire %s faite", documentsToExportParapheur.size(), nomRepertoire));
            for (DocumentModel docToExportParapheur : documentsToExportParapheur) {
                countIds =
                    exportDocumentAndWriteXML(
                        countIds,
                        docToExportParapheur,
                        dossier,
                        zip,
                        binaryDataObjects,
                        descriptiveMetadata,
                        datePublicationOuFinDossier,
                        documentManager,
                        nomRepertoire,
                        compteurDocDansDossier,
                        messageIdentifierArchive,
                        null,
                        countAAAUI,
                        listLogEntryJournal
                    );
                compteurDocDansDossier = compteurDocDansDossier + 1;
            }
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Export de tous les %s fichiers du répertoire %s fait", documentsToExportParapheur.size(), nomRepertoire));
        } catch (EPGException e) {
            // DO nothing. C'est au cas où le répertoire n'est pas trouvé
            ARCHIVAGE_LOGGER.warn(STLogEnumImpl.DEFAULT, e, String.format("Répertoire %s non trouvé.", nomRepertoire));
        }
        compteurDocDansDossier = 1;
        // Pièce complémentaires à publier
        try {
            nomRepertoire = SolonEpgParapheurConstants.PARAPHEUR_FOLDER_PIECE_COMPLEMENTAIRE_NAME;
            documentsToExportParapheur =
                parapheurService.getAllFilesFromParapheurForArchivage(documentManager, dossier, nomRepertoire);
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Récupération de tous les %s fichiers du répertoire %s faite", documentsToExportParapheur.size(), nomRepertoire));
            for (DocumentModel docToExportParapheur : documentsToExportParapheur) {
                countIds =
                    exportDocumentAndWriteXML(
                        countIds,
                        docToExportParapheur,
                        dossier,
                        zip,
                        binaryDataObjects,
                        descriptiveMetadata,
                        datePublicationOuFinDossier,
                        documentManager,
                        nomRepertoire,
                        compteurDocDansDossier,
                        messageIdentifierArchive,
                        null,
                        countAAAUI,
                        listLogEntryJournal
                    );
                compteurDocDansDossier = compteurDocDansDossier + 1;
            }
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Export de tous les %s fichiers du répertoire %s fait", documentsToExportParapheur.size(), nomRepertoire));
        } catch (EPGException e) {
            // DO nothing. C'est au cas où le répertoire n'est pas trouvé
            ARCHIVAGE_LOGGER.warn(STLogEnumImpl.DEFAULT, e, String.format("Répertoire %s non trouvé.", nomRepertoire));
        }
        compteurDocDansDossier = 1;
        // Epreuves
        // Ecriture de l'archive unit
        try {
            nomRepertoire = SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME;
            documentsToExportParapheur =
                parapheurService.getAllFilesFromParapheurForArchivage(documentManager, dossier, nomRepertoire);
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Récupération de tous les %s fichiers du répertoire %s faite", documentsToExportParapheur.size(), nomRepertoire));
            for (DocumentModel docToExportParapheur : documentsToExportParapheur) {
                countIds =
                    exportDocumentAndWriteXML(
                        countIds,
                        docToExportParapheur,
                        dossier,
                        zip,
                        binaryDataObjects,
                        descriptiveMetadata,
                        datePublicationOuFinDossier,
                        documentManager,
                        nomRepertoire,
                        compteurDocDansDossier,
                        messageIdentifierArchive,
                        null,
                        countAAAUI,
                        listLogEntryJournal
                    );
                compteurDocDansDossier = compteurDocDansDossier + 1;
            }
            LOGGER.debug(STLogEnumImpl.DEFAULT, String.format("Export de tous les %s fichiers du répertoire %s fait", documentsToExportParapheur.size(), nomRepertoire));
        } catch (EPGException e) {
            // DO nothing. C'est au cas où le répertoire n'est pas trouvé
            ARCHIVAGE_LOGGER.warn(STLogEnumImpl.DEFAULT, String.format("Répertoire %s non trouvé.", nomRepertoire));
        }
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Export des documents du Parapheur, fin");
        return countIds;
    }

    /**
     * Ecrit le binaryDataObject pour un document généré (Bordereau, FDR,
     * Journal, TraitementPapier) pour l'archivage définitif
     *
     * @param nomDoc
     *            : nom du document
     * @param countDocument
     *            : Compteur pour générer un id au fichier
     * @param binaryDataObjects
     *            : StringBuffer pour écrire dans le fichier XML
     * @param dossier
     *            : Le dossier
     */
    private void docGenereToBinaryDataOBject(
        String nomDoc,
        Integer countDocument,
        StringBuffer binaryDataObjects,
        ByteArrayOutputStream pdfbaos
    ) {
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Génération du binaryDataObject, début");
        // On est obligé de mettre Pattern.quote car sinon le split(".") ne
        // marche pas
        String docExtension = ".pdf";
        // Affichage du numéro sur deux chiffres
        binaryDataObjects.append("<BinaryDataObject id=\"ID" + String.format("%02d", countDocument) + "\">");
        binaryDataObjects.append("<Uri>Content/ID" + String.format("%02d", countDocument) + docExtension + "</Uri>");
        if (pdfbaos != null) {
            // Digest du fichier
            binaryDataObjects.append("<MessageDigest algorithm=\"SHA-512\">");
            String sha512HashPDF = SHA512Util.getSHA512Hash(pdfbaos.toByteArray());
            binaryDataObjects.append(sha512HashPDF);

            // Size du fichier
            binaryDataObjects.append("</MessageDigest>");
            binaryDataObjects.append("<Size>");
            binaryDataObjects.append(Integer.toString(pdfbaos.toByteArray().length));
            binaryDataObjects.append("</Size>");
        }

        binaryDataObjects.append("<FormatIdentification>");
        binaryDataObjects.append("<MimeType>" + MediaType.APPLICATION_PDF.mime() + "</MimeType>");
        binaryDataObjects.append("</FormatIdentification>");
        binaryDataObjects.append("<FileInfo>");
        binaryDataObjects.append("<Filename>" + nomDoc + "</Filename>");
        binaryDataObjects.append("<LastModified>" + formatArchiveDateTime(new Date()) + "</LastModified>");
        binaryDataObjects.append("</FileInfo>");
        binaryDataObjects.append("</BinaryDataObject>");
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Génération du binaryDataObject, fin");
    }

    /**
     * Pour un document donné, export du document et génération des métadonnées
     * pour export dans fichier XML pour archivage définitif
     *
     * @param countIds
     * @param docToExport
     * @param dossier
     * @param zip
     * @param binaryDataObjects
     * @param countArchiveUnit
     * @param descriptiveMetadata
     * @param datePublicationOuFinDossier
     * @throws Exception
     */
    protected Integer exportDocumentAndWriteXML(
        Integer countIds,
        DocumentModel docToExport,
        Dossier dossier,
        ZipOutputStream zip,
        StringBuffer binaryDataObjects,
        StringBuffer descriptiveMetadata,
        String datePublicationOuFinDossier,
        CoreSession documentManager,
        String folderName,
        Integer compteurDocumentDansDossier,
        String messageIdentifierArchive,
        String parentImmediatSiHorsPlanDeClassement,
        AtomicInteger aAAUICount,
        List<LogEntry> listLogEntryJournal
    )
        throws Exception {
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Export du document et génération des métadonnées, début");
        FileSolonEpg docToExportFile = docToExport.getAdapter(FileSolonEpg.class);

        // Content
        descriptiveMetadata
            .append(TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID)
            .append(String.format("%02d", countIds))
            .append("\">");
        countIds = countIds + 1;
        String dateDernierTraitement = "";
        if (SolonEpgServiceLocator.getDossierService().isDossierReprise(dossier)) {
            if (listLogEntryJournal != null) {
                // On récupère l'entrée la plus récente
                Date latest = null;
                for (LogEntry logEntryJournal : listLogEntryJournal) {
                    if (
                        "FeuilleRoute".equals(logEntryJournal.getCategory()) &&
                        (latest == null || latest.before(logEntryJournal.getEventDate()))
                    ) {
                        latest = logEntryJournal.getEventDate();
                    }
                }
                if (
                    latest != null &&
                    latest.after(dossier.getDateCreation().getTime()) &&
                    docToExportFile.getDateDernierTraitement() != null
                ) {
                    dateDernierTraitement = formatArchiveDateTime(docToExportFile.getDateDernierTraitement().getTime());
                } else if (VocabularyConstants.STATUT_PUBLIE.equals(dossier.getStatut())) {
                    Calendar datePublication = getDatePublicationFromRetourDila(dossier);
                    if (datePublication != null) {
                        dateDernierTraitement = formatArchiveDateTime(datePublication.getTime());
                    }
                } else if (VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION.equals(dossier.getStatut())) {
                    dateDernierTraitement = formatArchiveDateTime(dossier.getDateCreation().getTime());
                }
            }
        } else if (docToExportFile.getDateDernierTraitement() != null) {
            dateDernierTraitement = formatArchiveDateTime(docToExportFile.getDateDernierTraitement().getTime());
        }
        List<DocumentModel> fileVersionList = getFileVersionList(docToExport.getId(), documentManager);
        Integer listVersionsDocument = fileVersionList != null ? fileVersionList.size() : 0;
        writeContentArchiveUnit(
            documentManager,
            descriptiveMetadata,
            dossier,
            datePublicationOuFinDossier,
            folderName,
            false,
            null,
            false,
            RECORD_GRP,
            dateDernierTraitement,
            docToExportFile,
            compteurDocumentDansDossier,
            messageIdentifierArchive,
            parentImmediatSiHorsPlanDeClassement,
            listVersionsDocument,
            aAAUICount
        );
        // Gestion des versions précédentes
        countIds =
            exportListVersions(
                docToExport,
                countIds,
                dossier,
                zip,
                binaryDataObjects,
                descriptiveMetadata,
                datePublicationOuFinDossier,
                documentManager,
                folderName,
                compteurDocumentDansDossier,
                messageIdentifierArchive,
                parentImmediatSiHorsPlanDeClassement,
                aAAUICount,
                listLogEntryJournal
            );

        // Export de la version courante
        exportDocumentToZip(zip, docToExport, countIds);
        // Inscription BinaryDataObject dans le XML
        fichierToBinaryDataOBject(docToExport, countIds, binaryDataObjects, dossier, listLogEntryJournal);
        // On renseigne la version courante du document dans une archiveUnit
        descriptiveMetadata.append(TAG_DATAOBJECTREFERENCE_BEG);
        descriptiveMetadata
            .append(TAG_DATAOBJECTREFERENCEID_BEG + "ID")
            .append(String.format("%02d", countIds))
            .append(TAG_DATAOBJECTREFERENCEID_END);
        descriptiveMetadata.append(TAG_DATAOBJECTREFERENCE_END);
        descriptiveMetadata.append(TAG_ARCHIVEUNIT_END);

        countIds = countIds + 1;

        LOGGER.debug(STLogEnumImpl.DEFAULT, "Export du document et génération des métadonnées, fin");
        return countIds;
    }

    /**
     * Export versions antérieures d'un fichier donné + Ecriture dans fichier
     * XML
     *
     * @param docToExport
     *            :fichier à exporter
     * @param countIds
     *            : compteur pour générer les id des fichiers
     * @param dossier
     *            : le dossier courant
     * @param zip
     *            : le zip dans lequel on exporte les fichiers et leurs versions
     *            antérieures
     * @param binaryDataObjects
     *            : StringBuffer pour écriture du fichier XML pour les
     *            binaryDataObjects
     * @param countArchiveUnit
     *            : Compteur pour générer les id des archives units
     * @param descriptiveMetadata
     *            : StringBuffer pour écriture du fichier XML pour les
     *            descriptiveMetadata
     * @param datePublicationOuFinDossier
     *            : Date à renseigner dans le XML
     * @return
     * @throws NuxeoException
     * @throws Exception
     */
    protected Integer exportListVersions(
        DocumentModel docToExport,
        Integer countIds,
        Dossier dossier,
        ZipOutputStream zip,
        StringBuffer binaryDataObjects,
        StringBuffer descriptiveMetadata,
        String datePublicationOuFinDossier,
        CoreSession documentManager,
        String folderName,
        Integer compteurDocumentDansDossier,
        String messageIdentifierArchive,
        String parentImmediatSiHorsPlanDeClassement,
        AtomicInteger aAAUICount,
        List<LogEntry> listLogEntryJournal
    )
        throws Exception {
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Export versions antérieures, début");
        try {
            FileSolonEpg docToExportFile = docToExport.getAdapter(FileSolonEpg.class);
            List<DocumentModel> listVersionsDocument = getFileVersionList(docToExport.getId(), documentManager);
            // On retire le premier élément de la liste car il s'agit de la
            // version la plus récente
            if (CollectionUtils.isNotEmpty(listVersionsDocument)) {
                listVersionsDocument.remove(0);
                // On a besoin d'avoir de la version la plus ancienne à la plus
                // récente
                Collections.reverse(listVersionsDocument);
            }
            Integer compteurVersion = 1;
            for (DocumentModel versionDocument : listVersionsDocument) {
                if (!versionDocument.getId().equals(docToExport.getId())) {
                    FileSolonEpg versionToExportFile = versionDocument.getAdapter(FileSolonEpg.class);

                    exportDocumentToZip(zip, versionDocument, countIds);
                    fichierToBinaryDataOBject(
                        versionDocument,
                        countIds,
                        binaryDataObjects,
                        dossier,
                        listLogEntryJournal
                    );
                    Integer idVersionDocument = countIds;
                    countIds = countIds + 1;
                    // Ecriture de l'archive unit
                    descriptiveMetadata
                        .append(TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID)
                        .append(String.format("%02d", countIds))
                        .append("\">");
                    countIds = countIds + 1;
                    // Content
                    String dateDernierTraitement = "";
                    if (SolonEpgServiceLocator.getDossierService().isDossierReprise(dossier)) {
                        if (listLogEntryJournal != null) {
                            // On récupère l'entrée la plus récente
                            Date latest = null;
                            for (LogEntry logEntryJournal : listLogEntryJournal) {
                                if (
                                    "FeuilleRoute".equals(logEntryJournal.getCategory()) &&
                                    (latest == null || latest.before(logEntryJournal.getEventDate()))
                                ) {
                                    latest = logEntryJournal.getEventDate();
                                }
                            }
                            if (
                                latest != null &&
                                latest.after(dossier.getDateCreation().getTime()) &&
                                versionToExportFile.getDateDernierTraitement() != null
                            ) {
                                dateDernierTraitement =
                                    formatArchiveDateTime(versionToExportFile.getDateDernierTraitement().getTime());
                            } else if (VocabularyConstants.STATUT_PUBLIE.equals(dossier.getStatut())) {
                                Calendar datePublication = getDatePublicationFromRetourDila(dossier);
                                if (datePublication != null) {
                                    dateDernierTraitement = formatArchiveDateTime(datePublication.getTime());
                                }
                            } else if (
                                VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION.equals(dossier.getStatut())
                            ) {
                                dateDernierTraitement = formatArchiveDateTime(dossier.getDateCreation().getTime());
                            }
                        }
                    } else if (versionToExportFile.getDateDernierTraitement() != null) {
                        dateDernierTraitement =
                            formatArchiveDateTime(versionToExportFile.getDateDernierTraitement().getTime());
                    }
                    String titreVersionAnterieure = "";
                    if ("Documents".equals(folderName.trim())) {
                        folderName = "Documents hors plan de classement, " + parentImmediatSiHorsPlanDeClassement;
                    }
                    if (folderName.trim().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME)) {
                        titreVersionAnterieure = folderName + ". Version antérieure " + compteurVersion.toString();
                    } else {
                        titreVersionAnterieure =
                            folderName +
                            ": Document " +
                            compteurDocumentDansDossier.toString() +
                            ". Version antérieure " +
                            compteurVersion.toString();
                    }
                    writeContentArchiveUnit(
                        documentManager,
                        descriptiveMetadata,
                        dossier,
                        datePublicationOuFinDossier,
                        titreVersionAnterieure,
                        false,
                        compteurVersion,
                        false,
                        "Item",
                        dateDernierTraitement,
                        docToExportFile,
                        compteurDocumentDansDossier,
                        messageIdentifierArchive,
                        null,
                        listVersionsDocument.size(),
                        aAAUICount
                    );

                    descriptiveMetadata.append(TAG_DATAOBJECTREFERENCE_BEG);
                    descriptiveMetadata
                        .append(TAG_DATAOBJECTREFERENCEID_BEG + "ID")
                        .append(String.format("%02d", idVersionDocument))
                        .append(TAG_DATAOBJECTREFERENCEID_END);
                    descriptiveMetadata.append(TAG_DATAOBJECTREFERENCE_END);
                    descriptiveMetadata.append(TAG_ARCHIVEUNIT_END);
                    countIds = countIds + 1;
                    compteurVersion = compteurVersion + 1;
                }
            }
        } catch (FileNotFoundException e) {
            ARCHIVAGE_LOGGER.error(STLogEnumImpl.DEFAULT, e);
        }
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Export versions antérieures, fin");
        return countIds;
    }

    /**
     * Ecrit le binaryDataObject pour un fichier donné pour l'archivage
     * définitif
     *
     * @param docToExport
     *            : Le fichier
     * @param countDocument
     *            : Compteur pour générer un id au fichier
     * @param binaryDataObjects
     *            : StringBuffer pour écrire dans le fichier XML
     * @param dossier
     *            : Le dossier
     */
    private void fichierToBinaryDataOBject(
        DocumentModel docToExport,
        Integer countDocument,
        StringBuffer binaryDataObjects,
        Dossier dossier,
        List<LogEntry> listLogEntryJournal
    ) {
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Ecrit le binaryDataObject pour un fichier, début");
        try {
            FileSolonEpg fichier = docToExport.getAdapter(FileSolonEpg.class);
            String docExtension = FileUtils.getExtensionWithSeparator(fichier.getTitle());
            // Affichage du numéro sur deux chiffres
            binaryDataObjects.append("<BinaryDataObject id=\"ID" + String.format("%02d", countDocument) + "\">");
            binaryDataObjects.append(
                "<Uri>Content/ID" + String.format("%02d", countDocument) + docExtension + "</Uri>"
            );
            // Digest du fichier
            binaryDataObjects.append("<MessageDigest algorithm=\"SHA-512\">");
            Blob blob = fichier.getContent();
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                if (blob != null) {
                    md.update(blob.getByteArray());
                    byte[] sha512hash = md.digest();
                    StringBuilder builder = new StringBuilder();
                    for (byte b : sha512hash) {
                        builder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
                    }
                    binaryDataObjects.append(builder.toString());
                }
                binaryDataObjects.append("</MessageDigest>");
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(STLogEnumImpl.DEFAULT, e);
                binaryDataObjects.append("</MessageDigest>");
            }
            binaryDataObjects.append("<Size>");
            if (blob != null) {
                binaryDataObjects.append(Integer.toString(blob.getByteArray().length));
            }
            binaryDataObjects.append("</Size>");

            binaryDataObjects.append("<FormatIdentification>");
            binaryDataObjects.append("<MimeType>" + fichier.getFileMimeType() + "</MimeType>");
            binaryDataObjects.append("</FormatIdentification>");
            binaryDataObjects.append("<FileInfo>");
            binaryDataObjects.append("<Filename>" + escapeXml(fichier.getTitle()) + "</Filename>");
            String dateDernierTraitement = "";
            if (SolonEpgServiceLocator.getDossierService().isDossierReprise(dossier)) {
                if (listLogEntryJournal != null) {
                    // On récupère l'entrée la plus récente
                    Date latest = null;
                    for (LogEntry logEntryJournal : listLogEntryJournal) {
                        if (
                            "FeuilleRoute".equals(logEntryJournal.getCategory()) &&
                            (latest == null || latest.before(logEntryJournal.getEventDate()))
                        ) {
                            latest = logEntryJournal.getEventDate();
                        }
                    }
                    if (
                        latest != null &&
                        latest.after(dossier.getDateCreation().getTime()) &&
                        fichier.getDateDernierTraitement() != null
                    ) {
                        dateDernierTraitement = formatArchiveDateTime(fichier.getDateDernierTraitement().getTime());
                    } else if (VocabularyConstants.STATUT_PUBLIE.equals(dossier.getStatut())) {
                        Calendar datePublication = getDatePublicationFromRetourDila(dossier);
                        if (datePublication != null) {
                            dateDernierTraitement = formatArchiveDateTime(datePublication.getTime());
                        }
                    } else if (VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION.equals(dossier.getStatut())) {
                        dateDernierTraitement = formatArchiveDateTime(dossier.getDateCreation().getTime());
                    }
                }
            } else if (fichier.getDateDernierTraitement() != null) {
                dateDernierTraitement = formatArchiveDateTime(fichier.getDateDernierTraitement().getTime());
            }
            binaryDataObjects.append("<LastModified>" + dateDernierTraitement + "</LastModified>");
            binaryDataObjects.append("</FileInfo>");
            binaryDataObjects.append("</BinaryDataObject>");
        } catch (IOException ex) {
            ARCHIVAGE_LOGGER.error(STLogEnumImpl.DEFAULT, ex);
        }
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Ecrit le binaryDataObject pour un fichier, fin");
    }

    /**
     * Retourne la date de publication au JO d'un dossier
     *
     * @param doc
     */
    private Calendar getDatePublicationFromRetourDila(Dossier dossier) {
        Calendar datePublication = null;
        RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
        if (retourDila != null && retourDila.getDateParutionJorf() != null) {
            datePublication = retourDila.getDateParutionJorf();
        }
        return datePublication;
    }

    private String escapeXml(String chaine) {
        chaine = chaine.replace("&", "&amp;");
        chaine = chaine.replace("<", "&lt;");
        chaine = chaine.replace(">", "&gt;");

        return chaine;
    }

    /**
     * Récupération des versions d'un document.
     *
     * @throws FileNotFoundException
     *
     */
    private List<DocumentModel> getFileVersionList(String currentNodeId, CoreSession documentManager)
        throws FileNotFoundException {
        List<DocumentModel> fileVersionList = new ArrayList<>();
        if (currentNodeId != null) {
            final DocumentModel currentFile = getSelectedDocument(currentNodeId, documentManager);
            if (currentFile != null && currentFile.hasFacet(FacetNames.VERSIONABLE)) {
                // récupération de toute les versions du documents
                fileVersionList = documentManager.getVersions(currentFile.getRef());
                // on renvoie les élément du plus récent auplus anciens
                Collections.reverse(fileVersionList);

                // On place toujours le document courant en premier dans la
                // liste (en remplaçant la première version si elle est
                // identique)
                if (
                    !fileVersionList.isEmpty() &&
                    currentFile.getVersionLabel().equals(fileVersionList.get(0).getVersionLabel())
                ) {
                    fileVersionList.remove(0);
                }
                fileVersionList.add(0, currentFile);
            }
        }
        return fileVersionList;
    }

    /**
     * récupère le document sélectionné par l'utilisateur à partir d"un
     * identifiant.
     *
     * @return DocumentModel
     */
    private DocumentModel getSelectedDocument(final String idDocument, final CoreSession documentManager) {
        DocumentModel selectedFolder = null;
        // on récupère le document à l'aide de son identifiant
        final String selectedNodeId = idDocument;
        if (selectedNodeId != null) {
            try {
                selectedFolder = documentManager.getDocument(new IdRef(selectedNodeId));
            } catch (final NuxeoException e) {
                throw new NuxeoException("impossible de récupérer le répertoire sélectionné", e);
            }
        }
        return selectedFolder;
    }

    /**
     * Retourne la Date formaté pour les archives AAA-MM-DDThh:mm:ss,nnn+hh:mm
     *
     * @param date
     * @return
     */
    private String formatArchiveDateTime(Date date) {
        //StringBuilder buffer = new StringBuilder(SolonDateConverter.DATETIME_DASH_REVERSE_T_MILLI_Z.format(date));
        // return buffer.insert(buffer.length() - 2, ':').toString();
        return new StringBuilder(SolonDateConverter.DATETIME_DASH_REVERSE_T_MILLI_Z.format(date)).toString();
    }

    /**
     * Retourne l'identifiant séquentiel pour l'archivage - Séquence Oracle
     *
     * @return
     */
    private String getIdentifiantSequentielArchivage() {
        // Compteur
        String compteur;
        String nuxeoTemplates = Framework.getProperty("nuxeo.templates", "");
        // Si le template oracle n'est pas activé, on utilise le sequenceur
        // nuxeo (utile pour H2)
        if (nuxeoTemplates.contains("oracle")) {
            compteur = Integer.toString(getNumeroChronoArchivage());
        } else {
            final UIDGeneratorService uidGeneratorService = ServiceUtil.getRequiredService(UIDGeneratorService.class);
            final UIDSequencer sequencer = uidGeneratorService.getSequencer();
            compteur = Long.toString(sequencer.getNextLong(SOLON_EPG_ARCHIVAGE_SEQUENCER));
        }

        return compteur;
    }

    private int getNumeroChronoArchivage() {
        final int[] nextVal = new int[1];
        nextVal[0] = -1;
        getOrCreatePersistenceProvider()
            .run(
                true,
                new RunVoid() {

                    @Override
                    public void runWith(EntityManager entityManager) {
                        // génération du nom de séquence à aller chercher
                        String sequenceName = SOLON_EPG_ARCHIVAGE_SEQUENCER;

                        // On vérifie que la séquence existe
                        final StringBuilder checkSequence = new StringBuilder(
                            "SELECT count(1) FROM user_sequences WHERE sequence_name = '"
                        )
                            .append(sequenceName)
                            .append("'");
                        final Query checkSequenceQuery = entityManager.createNativeQuery(checkSequence.toString());
                        int count = ((Number) checkSequenceQuery.getSingleResult()).intValue();
                        // Si elle n'existe pas, on la créée
                        if (count == 0) {
                            // Si la sequence n'existe pas, on génère une nouvelle
                            // séquence
                            try (CloseableCoreSession session = SessionUtil.openSession()) {
                                // Appel de la procédure stockée CREATE_NEW_SEQUENCE qui
                                // prend en paramètre le nom de la
                                // nouvelle séquence
                                StringBuilder createSequence = new StringBuilder("CREATE_NEW_SEQUENCE('")
                                    .append(sequenceName)
                                    .append("')");
                                QueryUtils.execSqlFunction(session, createSequence.toString(), null);
                            }
                        }
                        final StringBuilder selectNextVal = new StringBuilder("SELECT ")
                            .append(sequenceName)
                            .append(".nextval from dual");
                        // récupération de la prochaine valeur
                        final Query nextValQuery = entityManager.createNativeQuery(selectNextVal.toString());
                        @SuppressWarnings("unchecked")
                        final List<Number> resultList = nextValQuery.getResultList();
                        if (resultList != null && !resultList.isEmpty()) {
                            nextVal[0] = (resultList.get(0)).intValue();
                        }
                    }
                }
            );
        if (nextVal[0] == -1) {
            throw new EPGException("Impossible de générer le numéro chrono pour l'archivage");
        }
        return nextVal[0];
    }

    protected PersistenceProvider getOrCreatePersistenceProvider() {
        if (persistenceProvider == null) {
            synchronized (NORServiceImpl.class) {
                if (persistenceProvider == null) {
                    activatePersistenceProvider();
                }
            }
        }
        return persistenceProvider;
    }

    protected static void activatePersistenceProvider() {
        Thread thread = Thread.currentThread();
        ClassLoader last = thread.getContextClassLoader();
        try {
            thread.setContextClassLoader(PersistenceProvider.class.getClassLoader());
            PersistenceProviderFactory persistenceProviderFactory = ServiceUtil.getRequiredService(
                PersistenceProviderFactory.class
            );
            persistenceProvider = persistenceProviderFactory.newProvider("NXUIDSequencer");
            persistenceProvider.openPersistenceUnit();
        } finally {
            thread.setContextClassLoader(last);
        }
    }

    /**
     * Export d'un fichier vers un zip
     *
     * @param zip
     *            : Zip dans lequel on exporte le fichier
     * @param docToExport
     *            : le fichier à exporter
     * @param countDocument
     *            : compteur pour générer l'id du fichier
     * @param dossier
     *            : le dossier courant
     * @throws Exception
     */
    private void exportDocumentToZip(ZipOutputStream zip, DocumentModel docToExport, Integer countDocument)
        throws Exception {
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Export d'un fichier vers un zip, début");
        FileSolonEpg docToExportFile = docToExport.getAdapter(FileSolonEpg.class);
        Blob blobFile = docToExportFile.getContent();
        if (blobFile != null) {
            // On est obligé de mettre Pattern.quote car sinon le split(".") ne
            // marche pas
            String fileName = blobFile.getFilename();
            if (fileName != null) {
                String[] splitDocName = fileName.split(Pattern.quote("."));
                String docExtension = "";
                if (splitDocName.length > 0) {
                    docExtension = "." + splitDocName[splitDocName.length - 1];
                }
                ZipEntry entry = new ZipEntry(
                    "Content" + "/" + "ID" + String.format("%02d", countDocument) + docExtension
                );
                entry.setSize(blobFile.getByteArray().length);
                zip.putNextEntry(entry);
                zip.write(blobFile.getByteArray());
                zip.closeEntry();
            }
        }
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Export d'un fichier vers un zip, fin");
    }

    private List<RouteTableElement> getElements(SSFeuilleRoute currentRoute) {
        return SolonEpgServiceLocator.getDocumentRoutingService().getFeuilleRouteElements(currentRoute, session);
    }

    @Override
    public int getDelaisEligibilite(CoreSession session) {
        ParametrageAdamantService parametrageAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();
        return parametrageAdamantService.getParametrageAdamantDocument(session).getDelaiEligibilite().intValue();
    }

    @Override
    public List<String> getVecteurPublicationEligibilite(CoreSession session) {
        ParametrageAdamantService parametrageAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();
        List<String> params = parametrageAdamantService.getParametrageAdamantDocument(session).getVecteurPublication();
        BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
        Map<String, String> vecteurs = bulletinOfficielService.getAllVecteurPublicationIdIntitulePair(session);
        return vecteurs
            .entrySet()
            .stream()
            .filter(e -> params.contains(e.getValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getTypeActeEligibilite(CoreSession session) {
        ParametrageAdamantService parametrageAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();
        return parametrageAdamantService.getParametrageAdamantDocument(session).getTypeActe();
    }

    @Override
    public int getNumeroSolon(CoreSession session) {
        ParametrageAdamantService parametrageAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();
        return parametrageAdamantService.getParametrageAdamantDocument(session).getNumeroSolon().intValue();
    }

    @Override
    public boolean isDocumentEligible(final Dossier dossier, CoreSession session) {
        ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
        int delaisEligibilite = getDelaisEligibilite(session);
        List<String> vecteurPublicationEligible = getVecteurPublicationEligibilite(session);
        List<String> typeActeEligible = getTypeActeEligibilite(session);
        Date dateNow = new Date();
        Date dateEligibiliteDos = null;
        Date datePublicationOuFinDossier = null;

        List<LogEntry> listLogEntryJournal = getLogEntryList(dossier.getDocument().getId());

        if (
            dossier.getVecteurPublication().stream().anyMatch(str -> vecteurPublicationEligible.contains(str)) &&
            typeActeEligible.contains(dossier.getTypeActe())
        ) {
            if (dossier.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
                Calendar dateParution = archiveService.getDateParutionJorfFromRetourDila(dossier);
                if (dateParution != null) {
                    datePublicationOuFinDossier = new Date(dateParution.getTimeInMillis());
                    dateEligibiliteDos = DateUtils.addMonths(datePublicationOuFinDossier, delaisEligibilite);
                    return dateEligibiliteDos.before(dateNow);
                } else {
                    ARCHIVAGE_LOGGER.info(
                        STLogEnumImpl.DEFAULT,
                        dossier.getNumeroNor() + " -> date de fin dernière étape fdr null"
                    );
                }
            } else if (dossier.getStatut().equals(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION)) {
                Date latest = null;
                if (listLogEntryJournal != null) {
                    for (LogEntry logEntryJournal : listLogEntryJournal) {
                        if (
                            "label.journal.comment.terminerDossierSansPublication".equals(
                                    logEntryJournal.getComment()
                                ) &&
                            (latest == null || latest.before(logEntryJournal.getEventDate()))
                        ) {
                            latest = logEntryJournal.getEventDate();
                        }
                        if (latest != null) {
                            datePublicationOuFinDossier = new Date(latest.getTime());
                            dateEligibiliteDos = DateUtils.addMonths(datePublicationOuFinDossier, delaisEligibilite);
                        }
                    }
                } else {
                    // impossible de récupérer le journal
                    ARCHIVAGE_LOGGER.info(STLogEnumImpl.DEFAULT, dossier.getNumeroNor() + MESSAGE_ERROR_GET_JOURNAL);
                }
                if (
                    archiveService.isDossierReprise(dossier) &&
                    !(
                        datePublicationOuFinDossier != null &&
                        datePublicationOuFinDossier.after(dossier.getDateCreation().getTime())
                    )
                ) {
                    if (dossier.getDateCreation() != null) {
                        dateEligibiliteDos = new Date(dossier.getDateCreation().getTimeInMillis());
                        dateEligibiliteDos = DateUtils.addMonths(dateEligibiliteDos, delaisEligibilite);
                        return dateEligibiliteDos.before(dateNow);
                    } else {
                        ARCHIVAGE_LOGGER.info(
                            STLogEnumImpl.DEFAULT,
                            dossier.getNumeroNor() + " -> date de création null"
                        );
                    }
                } else {
                    return dateEligibiliteDos != null && dateEligibiliteDos.before(dateNow);
                }
            } else if (dossier.getStatut().equals(VocabularyConstants.STATUT_CLOS)) {
                if (listLogEntryJournal != null) {
                    // On récupère la dernière entrée du journal
                    Date latest = null;
                    for (LogEntry logEntryJournal : listLogEntryJournal) {
                        if (latest == null || latest.before(logEntryJournal.getEventDate())) {
                            latest = logEntryJournal.getEventDate();
                        }
                    }
                    if (latest != null) {
                        dateEligibiliteDos = new Date(latest.getTime());
                        dateEligibiliteDos = DateUtils.addMonths(dateEligibiliteDos, delaisEligibilite);
                        return dateEligibiliteDos.before(dateNow);
                    }
                } else {
                    // impossible de récupérer le journal
                    ARCHIVAGE_LOGGER.info(STLogEnumImpl.DEFAULT, dossier.getNumeroNor() + MESSAGE_ERROR_GET_JOURNAL);
                }
            }
        }
        return false;
    }

    /**
     * Récupère dans la configuration le nombre maximal de dossiers à traiter
     * lors d'une occurrence du batch.
     */
    @Override
    public int getMaxDossiersATraiter(CoreSession session) {
        ParametrageAdamantService parametrageAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();
        return parametrageAdamantService.getParametrageAdamantDocument(session).getNbDossierExtraction().intValue();
    }

    @Override
    public boolean checkFile(DocumentModel documentModel, CoreSession documentManager) {
        final FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
        final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
        Dossier dossier = documentModel.getAdapter(Dossier.class);
        List<DocumentModel> documentsToExportFDD = new ArrayList<>();
        List<DocumentModel> documentsToExportParapheur = new ArrayList<>();
        try {
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Récupération de tous les fichiers FDD, début");
            documentsToExportFDD = fondDeDossierService.getAllFilesFromFondDeDossierAllFolder(documentManager, dossier);
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Récupération de tous les fichiers FDD, fin");
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Récupération de tous les fichiers Parapheur, début");
            documentsToExportParapheur = parapheurService.getAllFilesFromParapheurAllFolder(documentManager, dossier);
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Récupération de tous les fichiers Parapheur, fin");
        } catch (NuxeoException e) {
            throw new NuxeoException("erreur lors de la récupération des documents du dossier", e);
        }

        LOGGER.debug(STLogEnumImpl.DEFAULT, "Vérification des fichiers, début");
        try {
            if (!documentsToExportFDD.isEmpty() || !documentsToExportParapheur.isEmpty()) {
                for (DocumentModel docToExportFDD : documentsToExportFDD) {
                    if (docToExportFDD.getName().contains(".zip")) {
                        throw new NuxeoException("Le dossier contient des fichiers zip.");
                    }
                }
                for (DocumentModel docToExportParapheur : documentsToExportParapheur) {
                    if (docToExportParapheur.getName().contains(".zip")) {
                        throw new NuxeoException("Le dossier contient des fichiers zip.");
                    }
                }
            } else {
                throw new NuxeoException("Le dossier ne possède pas de fichier dans son bordereau/fond de dossier.");
            }
        } finally {
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Vérification des fichiers, fin");
        }
        return true;
    }

    private String getValideArchivalProfile(CoreSession session, Dossier dossier) {
        final ParametrageAdamantService paramService = SolonEpgServiceLocator.getParametrageAdamantService();
        if (SolonEpgServiceLocator.getParapheurService().hasActeIntegral(session, dossier)) {
            return paramService.getParametrageAdamantDocument(session).getArchivalProfile();
        } else {
            return paramService.getParametrageAdamantDocument(session).getArchivalProfileSpecific();
        }
    }
}
