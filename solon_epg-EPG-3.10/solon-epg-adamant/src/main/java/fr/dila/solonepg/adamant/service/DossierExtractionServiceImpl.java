package fr.dila.solonepg.adamant.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunVoid;
import org.nuxeo.ecm.core.schema.FacetNames;
import org.nuxeo.ecm.platform.audit.api.LogEntry;
import org.nuxeo.ecm.platform.ui.web.model.SelectDataModel;
import org.nuxeo.ecm.platform.ui.web.model.SelectDataModelRow;
import org.nuxeo.ecm.platform.ui.web.model.impl.SelectDataModelImpl;
import org.nuxeo.ecm.platform.uidgen.UIDSequencer;
import org.nuxeo.ecm.platform.uidgen.service.ServiceHelper;
import org.nuxeo.ecm.platform.uidgen.service.UIDGeneratorService;
import org.nuxeo.ecm.platform.uidgen.service.UIDSequencerImpl;
import org.nuxeo.runtime.api.Framework;

import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.ecm.platform.routing.api.DocumentRouteTableElement;
import fr.dila.solonepg.adamant.SolonEpgAdamantConstant;
import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.adamant.model.DossierExtractionBordereau;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.FeuilleRouteService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.ParametrageAdamantService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.api.service.PdfGeneratorService;
import fr.dila.solonepg.core.dto.DossierArchivageStatDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.service.DossierDistributionService;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.SHA512Util;
import fr.dila.st.core.util.SessionUtil;

/**
 * Service dédié à l'extraction d'un dossier.
 * 
 * @author tlombard
 */
public class DossierExtractionServiceImpl implements DossierExtractionService{

	private static final String RECORD_GRP = "RecordGrp";

	private static final String ATTR_ID_EQUALS_ID = "id=\"ID";

	private static final String TAG_ARCHIVEUNIT_END = "</ArchiveUnit>";

	private static final String TAG_ARCHIVEUNIT_BEG = "<ArchiveUnit ";

	private static final String TAG_DATAOBJECTREFERENCEID_END = "</DataObjectReferenceId>";

	private static final String TAG_DATAOBJECTREFERENCEID_BEG = "<DataObjectReferenceId>";

	private static final String TAG_DATAOBJECTREFERENCE_END = "</DataObjectReference>";

	private static final String TAG_DATAOBJECTREFERENCE_BEG = "<DataObjectReference>";

	private static final Log LOGGER = LogFactory.getLog(SolonEpgAdamantConstant.ARCHIVAGE_LOG);
	
	private static final Log ARCHIVAGE_LOGGER = LogFactory.getLog(SolonEpgAdamantConstant.ARCHIVAGE_LOG);

	private static final String SOLON_EPG_ARCHIVAGE_SEQUENCER = "SOLON_EPG_ARCHIVAGE_SEQUENCER";
	
	private static final String TAG_RULE_BEG = "<Rule>";
	private static final String TAG_RULE_END = "</Rule>";
	
	private static final String TAG_IDENTIFIER_END = "</Identifier>";
	private static final String TAG_IDENTIFIER_BEG = "<Identifier>";

	private static final String TAG_STARTDATE_END = "</StartDate>";
	private static final String TAG_STARTDATE_BEG = "<StartDate>";

	private CoreSession session;
	private static Map<String, String> lifeCycleStateToLabel;

	static {
		lifeCycleStateToLabel = new HashMap<String, String>();
		lifeCycleStateToLabel.put("draft", "label.epg.feuilleRoute.etape.draft");
		lifeCycleStateToLabel.put("validated", "label.epg.feuilleRoute.etape.validated");
		lifeCycleStateToLabel.put("ready", "label.epg.feuilleRoute.etape.ready");
		lifeCycleStateToLabel.put("running", "label.epg.feuilleRoute.etape.running");
	}
	
	public DossierExtractionServiceImpl() {
		super();
	}
	
	private String getSipName(Dossier dossier) {
		return dossier.getNumeroNor() + ".zip";
	}
	
	@Override
	public void genererDossierArchive(final CoreSession documentManager, Dossier dossier,
			Map<String, String> messages, DossierExtractionLot lot, DossierExtractionBordereau bordereau) throws ClientException {
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
		OutputStream outputStreamZip = null;
		String zipFile = null;
		List<LogEntry> listLogEntryJournal = getLogEntryList(dossier.getDocument().getId());
		
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Début de génération de l'archive définitive du dossier" + dossier.getNumeroNor());
			}
			
			File archivageDefTree = getOrCreateExtractionLotFolder(lot);

			// On crée le zip à vide car on exporte séparemment le parapheur et
			// le FDD
			zipFile = archivageDefTree.getPath() + SolonEpgAdamantConstant.FILE_SEPARATOR + sipName;
			outputStreamZip = new FileOutputStream(zipFile);
			zip = SolonEpgServiceLocator.getArchiveService()
					.initWriteZipStreamAndAddFile(new ArrayList<DocumentModel>(), outputStreamZip, session);

			// Initialisation des StringBuffer qui vont contenir les données à
			// mettre dans le fichier XML
			StringBuffer enteteFichierXML = new StringBuffer();
			enteteFichierXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			enteteFichierXML.append("<?xml-model href=\"" 
					+ configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_BALISE_HREF)
					+ "\" type=\"application/xml\" schematypens=\"http://relaxng.org/ns/structure/1.0\"?>");
			enteteFichierXML.append("<ArchiveTransfer xmlns=\"fr:gouv:culture:archivesdefrance:seda:v2.1\">");
			enteteFichierXML.append("");

			StringBuffer metadonneesFichierXML = new StringBuffer();
			metadonneesFichierXML.append("<Comment>Versement du dossier NOR " + dossier.getNumeroNor() + "</Comment>");
			Date now = new Date();
			metadonneesFichierXML.append("<Date>" + formatArchiveDateTime(now) + "</Date>");
			// Identifiant séquentiel (séquence oracle)
			metadonneesFichierXML.append("<MessageIdentifier>" + messageIdentifierTarget + "</MessageIdentifier>");
			// Conf
			metadonneesFichierXML.append("<ArchivalAgreement>"
					+ configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ARCHIVAL_AGREEMENT)
					+ "</ArchivalAgreement>");
			metadonneesFichierXML.append("<CodeListVersions>");
			// ReplyCodeListVersion
			metadonneesFichierXML.append("<ReplyCodeListVersion>"
					+ configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_REPLY_CODE_LIST_VERSION)
					+ "</ReplyCodeListVersion>");
			// Conf
			metadonneesFichierXML.append("<MessageDigestAlgorithmCodeListVersion>"
					+ configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_MESSAGE_DIGEST_ALGORITHM)
					+ "</MessageDigestAlgorithmCodeListVersion>");
			// Conf
			metadonneesFichierXML.append("<MimeTypeCodeListVersion>"
					+ configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_MIME_TYPE)
					+ "</MimeTypeCodeListVersion>");
			// EncodingCodeListVersion
			metadonneesFichierXML.append("<EncodingCodeListVersion>"
					+ configService
							.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ENCODING_CODE_LIST_VERSION)
					+ "</EncodingCodeListVersion>");
			// Conf
			metadonneesFichierXML.append("<FileFormatCodeListVersion>"
					+ configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_FILE_FORMAT)
					+ "</FileFormatCodeListVersion>");
			// CompressionAlgorithmCodeListVersion
			metadonneesFichierXML.append("<CompressionAlgorithmCodeListVersion>"
					+ configService.getValue(
							SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_COMPRESSION_ALGORITHM_CODE_LIST_VERSION)
					+ "</CompressionAlgorithmCodeListVersion>");
			// DataObjectversionCodeListVersion
			metadonneesFichierXML.append("<DataObjectVersionCodeListVersion>"
					+ configService.getValue(
							SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_DATA_OBJECT_VERSION_CODE_LIST_VERSION)
					+ "</DataObjectVersionCodeListVersion>");
			// StorageRulecodeListVersion
			metadonneesFichierXML.append("<StorageRuleCodeListVersion>"
					+ configService
							.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_STORAGE_RULE_CODE_LIST_VERSION)
					+ "</StorageRuleCodeListVersion>");
			// Conf
			metadonneesFichierXML.append("<AppraisalRuleCodeListVersion>"
					+ configService
							.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_APPRAISAL_RULE_CODE_LIST_VERSION)
					+ "</AppraisalRuleCodeListVersion>");
			// Conf
			metadonneesFichierXML.append("<AccessRuleCodeListVersion>"
					+ configService
							.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_CODE_LIST_VERSION)
					+ "</AccessRuleCodeListVersion>");
			// Conf
			metadonneesFichierXML.append("<DisseminationRuleCodeListVersion>"
					+ configService.getValue(
							SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_DISSEMINATION_RULE_CODE_LIST_VERSION)
					+ "</DisseminationRuleCodeListVersion>");
			// Conf
			metadonneesFichierXML.append("<ReuseRuleCodeListVersion>"
					+ configService
							.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_REUSE_RULE_CODE_LIST_VERSION)
					+ "</ReuseRuleCodeListVersion>");
			// ClassificationRuleCodeListVersion
			metadonneesFichierXML.append("<ClassificationRuleCodeListVersion>"
					+ configService.getValue(
							SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_CLASSIFICATION_RULE_CODE_LIST_VERSION)
					+ "</ClassificationRuleCodeListVersion>");
			// Conf
			metadonneesFichierXML.append("<AcquisitionInformationCodeListVersion>"
					+ configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ACQUISITION_INFORMATION)
					+ "</AcquisitionInformationCodeListVersion>");
			// AuthorizationCodeListVersion
			metadonneesFichierXML.append("<AuthorizationReasonCodeListVersion>"
					+ configService
							.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_AUTHORIZATION_CODE_LIST_VERSION)
					+ "</AuthorizationReasonCodeListVersion>");
			// RelationshipCodeListVersion
			metadonneesFichierXML.append("<RelationshipCodeListVersion>"
					+ configService
							.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_RELATIONSHIP_CODE_LIST_VERSION)
					+ "</RelationshipCodeListVersion>");
			metadonneesFichierXML.append("</CodeListVersions>");
			metadonneesFichierXML.append("<DataObjectPackage>");
			StringBuffer finFichierXML = new StringBuffer();
			finFichierXML.append("<ManagementMetadata>");
			// Conf
			finFichierXML.append("<ArchivalProfile>"
					+ parametrageAdamantService.getParametrageAdamantDocument(documentManager).getArchivalProfile()
					+ "</ArchivalProfile>");
			// Conf
			finFichierXML.append("<ServiceLevel>"
					+ configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_SERVICE_LEVEL)
					+ "</ServiceLevel>");
			finFichierXML.append("<AcquisitionInformation>" + "Versement" + "</AcquisitionInformation>");
			// Conf
			finFichierXML.append("<OriginatingAgencyIdentifier>"
					+ parametrageAdamantService.getParametrageAdamantDocument(documentManager).getOriginatingAgencyIdentifier()
					+ "</OriginatingAgencyIdentifier>");
			// Conf
			finFichierXML.append("<SubmissionAgencyIdentifier>"
					+ parametrageAdamantService.getParametrageAdamantDocument(documentManager).getSubmissionAgencyIdentifier()
					+ "</SubmissionAgencyIdentifier>");
			finFichierXML.append("</ManagementMetadata>");
			finFichierXML.append("</DataObjectPackage>");
			finFichierXML.append("<ArchivalAgency>");
			// Conf
			finFichierXML.append(TAG_IDENTIFIER_BEG
					+ configService
							.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ARCHIVAL_AGENCY_IDENTIFIER)
					+ TAG_IDENTIFIER_END);
			finFichierXML.append("</ArchivalAgency>");
			finFichierXML.append("<TransferringAgency>");
			// Conf
			finFichierXML.append(TAG_IDENTIFIER_BEG
					+ configService
							.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_TRANSFERING_AGENCY_IDENTIFIER)
					+ TAG_IDENTIFIER_END);
			finFichierXML.append("</TransferringAgency>");
			finFichierXML.append("</ArchiveTransfer>");

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
					TAG_RULE_BEG + configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_APPRAISAL_RULE)
							+ TAG_RULE_END);
			// Date de JO si Publié, date de dernière étape de FdR sinon - au
			// format AAAA-MM-JJ
			String datePublicationOuFinDossier = "";
			ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
			Boolean dossierReprise = archiveService.isDossierReprise(dossier);

			SimpleDateFormat isoDate = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
			if (dossier.getStatut().equals(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION)) {
				// Date du journal pour le passage de la dernière étape de la fdr
				Date latest = null;
				if (listLogEntryJournal != null) {
					for (LogEntry logEntryJournal : listLogEntryJournal) {
						if ("label.journal.comment.terminerDossierSansPublication".equals(logEntryJournal.getComment()) &&
								(latest == null || latest.before(logEntryJournal.getEventDate()))) {
							latest = logEntryJournal.getEventDate();
						}
					}
				} else {
					// impossible de récupérer le journal
					ARCHIVAGE_LOGGER.info(dossier.getNumeroNor() + " -> Impossible de récupérer le journal");
				}
				if (Boolean.TRUE.equals(dossierReprise) && !(latest != null && latest.after(dossier.getDateCreation().getTime()))) {
					if (dossier.getDateCreation() != null) {
						datePublicationOuFinDossier = isoDate.format(new Date(dossier.getDateCreation().getTimeInMillis()));
					} else {
						ARCHIVAGE_LOGGER.info(dossier.getNumeroNor() + " -> date de création null");
					}
				} else {
					datePublicationOuFinDossier=isoDate.format(latest);
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
						datePublicationOuFinDossier = isoDate.format(latest);
					}
				} else {
					// impossible de récupérer le journal
					ARCHIVAGE_LOGGER.info(dossier.getNumeroNor() + " -> Impossible de récupérer le journal");
				}
			}else if (dossier.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
				// Date de JO si Publié
				Calendar datePublication = getDatePublicationFromRetourDila(dossier);
				if(datePublication != null) {
					datePublicationOuFinDossier = isoDate.format(datePublication.getTimeInMillis());
				}
			}
			descriptiveMetadataXML.append(TAG_STARTDATE_BEG + datePublicationOuFinDossier + TAG_STARTDATE_END);
			descriptiveMetadataXML.append("<FinalAction>" 
			+ configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_APPRAISAL_RULE_FINAL_ACTION) 
			+ "</FinalAction>");
			descriptiveMetadataXML.append("</AppraisalRule>");

			descriptiveMetadataXML.append("<AccessRule>");
			// conf
			descriptiveMetadataXML.append(TAG_RULE_BEG + getAccessRuleFromTypeActe(dossier, configService) + TAG_RULE_END);
			descriptiveMetadataXML.append(TAG_STARTDATE_BEG + datePublicationOuFinDossier + TAG_STARTDATE_END);
			descriptiveMetadataXML.append("</AccessRule>");

			descriptiveMetadataXML.append("<DisseminationRule>");
			// conf
			descriptiveMetadataXML.append(TAG_RULE_BEG
					+ configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_DISSEMINATION_RULE)
					+ TAG_RULE_END);
			descriptiveMetadataXML.append(TAG_STARTDATE_BEG + datePublicationOuFinDossier + TAG_STARTDATE_END);
			descriptiveMetadataXML.append("</DisseminationRule>");

			descriptiveMetadataXML.append("<ReuseRule>");
			// conf
			descriptiveMetadataXML.append(TAG_RULE_BEG
					+ configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_REUSE_RULE) + TAG_RULE_END);
			descriptiveMetadataXML.append(TAG_STARTDATE_BEG + datePublicationOuFinDossier + TAG_STARTDATE_END);
			descriptiveMetadataXML.append("</ReuseRule>");

			descriptiveMetadataXML.append("</Management>");

			writeContentArchiveUnit(documentManager, descriptiveMetadataXML, dossier, datePublicationOuFinDossier,
					"NOR N°" + dossier.getNumeroNor(), true, null, true, RECORD_GRP, null, null, null,
					messageIdentifierTarget, null, null, aAAUICount);

			// Export des documents du parapheur + versions précédentes
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Export des fichiers du parapheur ainsi que leurs versions précédentes");
			}
			countIds = exportParapheurForArchivage(dossier, countIds, zip, binaryDataObjectsXML, descriptiveMetadataXML,
					datePublicationOuFinDossier, session, messageIdentifierTarget, aAAUICount, listLogEntryJournal);
			// Export des documents du fond de dossier + versions précédentes
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Export des fichiers du fond de dossier ainsi que leurs versions précédentes");
			}
			countIds = exportFDDForArchivage(dossier, countIds, zip, binaryDataObjectsXML, descriptiveMetadataXML,
					datePublicationOuFinDossier, session, messageIdentifierTarget, aAAUICount, listLogEntryJournal);
			// Bordereau
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Export du PDF du bordereau");
			}
			descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">");
			countIds = countIds + 1;
			ByteArrayOutputStream pdfBaosBordereau = pdfGeneratorService.addBordereauToZip(session, zip, dossier, true,
					"ID" + String.format("%02d", countIds));
			// Génération du binaryDataObject
			docGenereToBinaryDataOBject("bordereau.pdf", countIds, binaryDataObjectsXML, pdfBaosBordereau, dossier);
			// Génération de l'archiveUnit
			writeContentArchiveUnit(documentManager, descriptiveMetadataXML, dossier, datePublicationOuFinDossier, 
					SolonEpgAdamantConstant.BORDEREAU, false, null, false, "Item", null, null, null, 
					messageIdentifierTarget, null, null, aAAUICount);

			descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_BEG);
			descriptiveMetadataXML
					.append(TAG_DATAOBJECTREFERENCEID_BEG+"ID" + String.format("%02d", countIds) + TAG_DATAOBJECTREFERENCEID_END);
			descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_END);
			countIds = countIds + 1;
			descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_END);
			// Feuille de route
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Export du PDF de la feuille de route");
			}
			descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">");
			countIds = countIds + 1;
			ByteArrayOutputStream pdfBaosFDR = pdfGeneratorService.addFeuilleRouteToZip(zip, dossier,
					getDocumentRouteTableElementList(dossier.getDocument()), session, messages, true,
					"ID" + String.format("%02d", countIds));
			// Génération du binaryDataObject
			docGenereToBinaryDataOBject("feuilleRoute.pdf", countIds, binaryDataObjectsXML, pdfBaosFDR, dossier);
			// Génération de l'archiveUnit
			writeContentArchiveUnit(documentManager, descriptiveMetadataXML, dossier, datePublicationOuFinDossier, 
					SolonEpgAdamantConstant.FEUILLE_ROUTE, false, null, false, "Item", null, null, null, 
					messageIdentifierTarget, null, null, aAAUICount);

			descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_BEG);
			descriptiveMetadataXML
					.append(TAG_DATAOBJECTREFERENCEID_BEG+"ID" + String.format("%02d", countIds) + TAG_DATAOBJECTREFERENCEID_END);
			descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_END);
			countIds = countIds + 1;
			descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_END);
			// Journal
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Export du Journal");
			}
			descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">");
			countIds = countIds + 1;
			ByteArrayOutputStream pdfBaosJournal = pdfGeneratorService.addJournalToZip(session, zip, dossier,
					listLogEntryJournal, messages, true,
					"ID" + String.format("%02d", countIds));
			// Génération du binaryDataObject
			docGenereToBinaryDataOBject("journal.pdf", countIds, binaryDataObjectsXML, pdfBaosJournal, dossier);
			// Génération de l'archiveUnit
			writeContentArchiveUnit(documentManager, descriptiveMetadataXML, dossier, datePublicationOuFinDossier, 
					SolonEpgAdamantConstant.JOURNAL, false, null, false, "Item", null, null, null, 
					messageIdentifierTarget, null, null, aAAUICount);

			descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_BEG);
			descriptiveMetadataXML
					.append(TAG_DATAOBJECTREFERENCEID_BEG+"ID" + String.format("%02d", countIds) + TAG_DATAOBJECTREFERENCEID_END);
			descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_END);
			countIds = countIds + 1;
			descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_END);
			// Traitement Papier
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Export du PDF du traitement papier");
			}
			descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">");
			countIds = countIds + 1;
			ByteArrayOutputStream pdfBaosTP = pdfGeneratorService.addTraitementPapierToZip(session, zip, dossier, true,
					"ID" + String.format("%02d", countIds));
			// Génération du binaryDataObject
			docGenereToBinaryDataOBject("traitementPapier.pdf", countIds, binaryDataObjectsXML, pdfBaosTP, dossier);
			// Génération de l'archiveUnit
			writeContentArchiveUnit(documentManager, descriptiveMetadataXML, dossier, datePublicationOuFinDossier, 
					SolonEpgAdamantConstant.TRAITEMENT_PAPIER, false, null, false, "Item", null, null, null, 
					messageIdentifierTarget, null, null, aAAUICount);

			descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_BEG);
			descriptiveMetadataXML
					.append(TAG_DATAOBJECTREFERENCEID_BEG+"ID" + String.format("%02d", countIds) + TAG_DATAOBJECTREFERENCEID_END);
			descriptiveMetadataXML.append(TAG_DATAOBJECTREFERENCE_END);
			countIds = countIds + 1;
			descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_END);
			descriptiveMetadataXML.append(TAG_ARCHIVEUNIT_END);
			descriptiveMetadataXML.append("</DescriptiveMetadata>");

			// TLD : Le contenu du fichier a bien été généré au compteur près,
			// on remplace la messageIdentifierTarget par une vraie valeur
			String messageIdentifierArchive = parametrageAdamantService.getParametrageAdamantDocument(documentManager).getNumeroSolon() 
					+ "_" + getIdentifiantSequentielArchivage();
			String enteteFichierStr = enteteFichierXML.toString().replace(messageIdentifierTarget,
					messageIdentifierArchive);
			String metadonneesFichierStr = metadonneesFichierXML.toString().replace(messageIdentifierTarget,
					messageIdentifierArchive);
			String binaryDataObjectsStr = binaryDataObjectsXML.toString().replace(messageIdentifierTarget,
					messageIdentifierArchive);
			String descriptiveMetadataStr = descriptiveMetadataXML.toString().replace(messageIdentifierTarget,
					messageIdentifierArchive);
			String finFichierStr = finFichierXML.toString().replace(messageIdentifierTarget, messageIdentifierArchive);

			// Génération fichier XML
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Génération du fichier XML");
			}
			ZipEntry entry = new ZipEntry("manifest.xml");
			zip.putNextEntry(entry);
			zip.write(enteteFichierStr.getBytes());
			zip.write(metadonneesFichierStr.getBytes());
			zip.write(binaryDataObjectsStr.getBytes());
			zip.write(descriptiveMetadataStr.getBytes());
			zip.write(finFichierStr.getBytes());
			zip.closeEntry();
			zip.close();
			
			SolonEpgAdamantServiceLocator.getBordereauService().completeBordereau(bordereau, new File(zipFile));

			LOGGER.info(
					"Dossier " + dossier.getNumeroNor() + " exporté pour archivage définitif");
		} catch (Exception e) {
			LOGGER.error(e);

			if (outputStreamZip != null) {
				IOUtils.closeQuietly(outputStreamZip);
				new File(zipFile).delete();
			}
			throw new ClientException(e);

		} finally {
			IOUtils.closeQuietly(outputStreamZip);
		}
	}

	@Override
	public File getOrCreateExtractionLotFolder(DossierExtractionLot lot) throws ClientException {
		StringBuilder extractionDir = new StringBuilder(getPathDirArchivageDefinitive(session));
		extractionDir.append(SolonEpgAdamantConstant.FILE_SEPARATOR)
				.append(DateUtil.simpleDateFormat(SolonEpgAdamantConstant.DATE_FORMAT_FOR_FILES_AND_FOLDERS)
						.format(lot.getCreationDate()));

		File extractionFolder = new File(extractionDir.toString());
		if (!extractionFolder.exists()) {
			extractionFolder.mkdirs();
		}
		
		return extractionFolder;
	}

	private List<LogEntry> getLogEntryList(String idDossier) throws ClientException {
		JournalService journalService = STServiceLocator.getJournalService();
		return journalService.getLogEntries(idDossier);
	}

	/**
	 * Récupère la liste des DocumentRouteTableElement à partir de la liste des
	 * SelectDataModelRow.
	 * 
	 * @param SelectDataModelRowList
	 * @return
	 * @throws ClientException
	 */
	private List<DocumentRouteTableElement> getDocumentRouteTableElementList(DocumentModel currentDocument)
			throws ClientException {
		List<SelectDataModelRow> selectDataModelRowList = computeDossierSelectDataModelRelatedRouteElements(
				currentDocument).getRows();
		List<DocumentRouteTableElement> documentRouteTableElementList = new ArrayList<DocumentRouteTableElement>();
		for (SelectDataModelRow selectDataModelRow : selectDataModelRowList) {
			documentRouteTableElementList.add((DocumentRouteTableElement) selectDataModelRow.getData());
		}
		return documentRouteTableElementList;
	}

	private SelectDataModel computeDossierSelectDataModelRelatedRouteElements(DocumentModel currentDocument)
			throws ClientException {
		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();
		DocumentModel dossierRoute = dossierDistributionService.getLastDocumentRouteForDossier(session,
				currentDocument);
		if (dossierRoute == null) {
			return new SelectDataModelImpl("related_route_elements", new ArrayList<DocumentRouteTableElement>(), null);
		} else {
			DocumentRoute currentRoute = dossierRoute.getAdapter(DocumentRoute.class);
			return new SelectDataModelImpl("related_route_elements", getElements(currentRoute), null);
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

		String strTmp = configService
				.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_LIST_ACC_00002);
		List<String> listAcc00002 = new ArrayList<String>(Arrays.asList(strTmp.split(",")));
		strTmp = configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_LIST_ACC_00003);
		List<String> listAcc00003 = new ArrayList<String>(Arrays.asList(strTmp.split(",")));
		strTmp = configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_LIST_ACC_00020);
		List<String> listAcc00020 = new ArrayList<String>(Arrays.asList(strTmp.split(",")));
		strTmp = configService.getValue(SolonEpgAdamantConstant.SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_LIST_ACC_00025);
		List<String> listAcc00025 = new ArrayList<String>(Arrays.asList(strTmp.split(",")));

		if (listAcc00002.contains(dossier.getTypeActe())) {
			return VocabularyConstants.ACCESS_RULE_ACC_00002;
		} else if (listAcc00003.contains(dossier.getTypeActe())) {
			return VocabularyConstants.ACCESS_RULE_ACC_00003;
		} else if (listAcc00020.contains(dossier.getTypeActe())) {
			return VocabularyConstants.ACCESS_RULE_ACC_00020;
		} else if (listAcc00025.contains(dossier.getTypeActe())) {
			return VocabularyConstants.ACCESS_RULE_ACC_00025;
		}
		return "";
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
	 * @throws ClientException 
	 */
	private void writeContentArchiveUnit(CoreSession documentManager, StringBuffer descriptiveMetadata, Dossier dossier,
			String datePublicationOuFinDossier, String title, Boolean isMainParent, Integer version, Boolean hasDate,
			String descriptionLevel, String dateDerniereVersion, FileSolonEpg documentToExport,
			Integer compteurDocumentDansDossier, String messageIdentifierArchive,
			String parentImmediatSiHorsPlanDeClassement, Integer nombreDeVersion, AtomicInteger countAAAUI) throws ClientException {
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
		if (compteurDocumentDansDossier != null && !title.equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME)
				&& !SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_NON_CLASIFIED.equals(title) 
				&& !title.contains("Version antérieure")) {
			if ("Documents".equals(title)) {
				// M157338
				title = SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_NON_CLASIFIED + ", " + parentImmediatSiHorsPlanDeClassement;
			}
			titleArchiveUnit = title + ": Document " + compteurDocumentDansDossier.toString();
		}
		descriptiveMetadata.append("<Title>").append(escapeXml(titleArchiveUnit)).append("</Title>");
		// TODO : voir ce que c'est que la côté AN. C'est du paramétrage ? ->
		// Comment on génère ce truc ?
		descriptiveMetadata.append("<ArchivalAgencyArchiveUnitIdentifier>").append(messageIdentifierArchive).append("_")
				.append(countAAAUI.toString()).append("</ArchivalAgencyArchiveUnitIdentifier>");
		countAAAUI.incrementAndGet();

		String datePublicationJOString = "";
		RetourDila retourDila = null;
		if (isMainParent) {
			descriptiveMetadata.append("<OriginatingAgencyArchiveUnitIdentifier>").append(dossier.getNumeroNor())
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
			} else if (dossier.getStatut().equals(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION)
					|| dossier.getStatut().equals(VocabularyConstants.STATUT_CLOS)) {
				titreOfficiel = dossier.getTitreActe();
			}
			if (!titreOfficiel.isEmpty()) {
				descriptiveMetadata.append("<Description>").append(escapeXml(titreOfficiel)).append("</Description>");
			} else {
				descriptiveMetadata.append("<Description></Description>");
			}
			String typeActe = "";
			final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
			if (!StringUtils.isEmpty(VocabularyConstants.TYPE_ACTE_VOCABULARY)
					&& !StringUtils.isEmpty(dossier.getTypeActe())) {
				typeActe = vocabularyService.getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY,
						dossier.getTypeActe());
			}
			descriptiveMetadata.append("<DocumentType>").append(typeActe).append("</DocumentType>");
			String statutDossier = "";
			if (!StringUtils.isEmpty(VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME)
					&& !StringUtils.isEmpty(dossier.getStatut())) {
				statutDossier = vocabularyService.getEntryLabel(VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME,
						dossier.getStatut());
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
			descriptiveMetadata.append(TAG_IDENTIFIER_BEG
					+ parametrageAdamantService.getParametrageAdamantDocument(documentManager).getOriginatingAgencyBlocIdentifier()
					+ TAG_IDENTIFIER_END);
			descriptiveMetadata.append("</OriginatingAgency>");

			descriptiveMetadata.append("<SubmissionAgency>");
			descriptiveMetadata.append(TAG_IDENTIFIER_BEG
					+ parametrageAdamantService.getParametrageAdamantDocument(documentManager).getSubmissionAgencyBlocIdentifier()
					+ TAG_IDENTIFIER_END);
			descriptiveMetadata.append("</SubmissionAgency>");
			
		} else {			
			if (version != null) {
				descriptiveMetadata.append("<Version>V").append(version.toString()).append("</Version>");
			}
			descriptiveMetadata.append("<OriginatingAgency>");
			descriptiveMetadata.append(TAG_IDENTIFIER_BEG
					+ parametrageAdamantService.getParametrageAdamantDocument(documentManager).getOriginatingAgencyBlocIdentifier()
					+ TAG_IDENTIFIER_END);
			descriptiveMetadata.append("</OriginatingAgency>");

			descriptiveMetadata.append("<SubmissionAgency>");
			descriptiveMetadata.append(TAG_IDENTIFIER_BEG
					+ parametrageAdamantService.getParametrageAdamantDocument(documentManager).getSubmissionAgencyBlocIdentifier()
					+ TAG_IDENTIFIER_END);
			descriptiveMetadata.append("</SubmissionAgency>");
		}
		
		DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		if (hasDate) {
			// Comparaison entre la date de fin et la date de début.
			// Si date de début après date de fin on prend le 1er janvier de
			// l'année de création du dossier
			// car dossier issu de la reprise
			Calendar datePublicationOufinDossierCal = null;
			SimpleDateFormat isoDateForDatePublicationOuFinDossier = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
			try {
				Date datePublicationOufinDossierParsed = isoDateForDatePublicationOuFinDossier
						.parse(datePublicationOuFinDossier);
				datePublicationOufinDossierCal = Calendar.getInstance();
				datePublicationOufinDossierCal.setTime(datePublicationOufinDossierParsed);
			} catch (Exception e) {
				LOGGER.warn(
						"Erreur de conversion de la date de fin du dossier. Impossible de vérifier la cohérence entre la date de début et la date de fin. "
								+ e.getMessage(),
						e);
			}

			// pour les dossiers de la reprise ou ceux incohérent, la date de
			// création est au 01-01 de l'année du NOR
			// sinon la date de création peut être reprise depuis l'attribut
			// correspondant
			String dateDebutDossier = "";

			if (dossierService.isDossierReprise(dossier) || 
					// incohérent car date de création est apres la date de fin
					(datePublicationOufinDossierCal != null 
							&& !dossier.getDateCreation().before(datePublicationOufinDossierCal))) {
				String anneeDossier = "20" + dossier.getNumeroNor().substring(4, 6) + "-01-01";
				dateDebutDossier = anneeDossier;
			} else {
				dateDebutDossier = isoDateForDatePublicationOuFinDossier
						.format(dossier.getDateCreation().getTimeInMillis());
			}

			descriptiveMetadata.append(TAG_STARTDATE_BEG).append(dateDebutDossier).append(TAG_STARTDATE_END);
			descriptiveMetadata.append("<EndDate>").append(datePublicationOuFinDossier).append("</EndDate>");
		}
		
		if (!isMainParent && hasWriterBloc(title)) {
			descriptiveMetadata.append("<Writer>");
			descriptiveMetadata.append("<Corpname>").append(getEntiteDerniereVersion(dossier, documentToExport, version, nombreDeVersion)).append("</Corpname>");
			descriptiveMetadata.append("</Writer>");
		}
		if (dateDerniereVersion != null && !dateDerniereVersion.isEmpty()) {
			descriptiveMetadata.append("<RegisteredDate>").append(dateDerniereVersion).append("</RegisteredDate>");
		}

		if (dossier.getStatut().equals(VocabularyConstants.STATUT_PUBLIE) && isMainParent) {
			// Si on est au statut publié on renseigné les balises Event
			if (retourDila != null) {
				java.util.Calendar datePublicationJournalOfficielDate = retourDila.getDateParutionJorf();
				if (datePublicationJournalOfficielDate != null) {
					datePublicationJOString = formatArchiveDateTime(datePublicationJournalOfficielDate.getTime());
					descriptiveMetadata.append("<Event>");
					descriptiveMetadata.append("<EventDateTime>").append(datePublicationJOString).append("</EventDateTime>");
					descriptiveMetadata.append("<EventDetail>").append("Date de publication au journal officiel")
					.append("</EventDetail>");
					descriptiveMetadata.append("</Event>");
				}
			}

			// On renseigne publication BO s'il y a eu une publication BO
			if (retourDila != null && !retourDila.getParutionBo().isEmpty()) {
				List<ParutionBo> listParutionBO = retourDila.getParutionBo();
				for (ParutionBo parutionBO : listParutionBO) {
					java.util.Calendar dateParutionBO = parutionBO.getDateParutionBo();
					String dateParutionBOString = formatArchiveDateTime(dateParutionBO.getTime());
					descriptiveMetadata.append("<Event>");
					descriptiveMetadata.append("<EventDateTime>").append(dateParutionBOString)
							.append("</EventDateTime>");
					descriptiveMetadata.append("<EventDetail>").append("Date de publication au Bulletin officiel")
							.append("</EventDetail>");
					descriptiveMetadata.append("</Event>");
				}
			}
		}
		descriptiveMetadata.append("</Content>");
	}
	
	/**
	 * get entité dernière version pour corpname
	 */
	private String getEntiteDerniereVersion(Dossier dossier, FileSolonEpg documentToExport, Integer version, Integer nombreDeVersion) {
		
		String entiteDerniereVersion = null;
		DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		
		if (dossierService.isDossierReprise(dossier)) {
			try {
				entiteDerniereVersion = STServiceLocator.getSTMinisteresService()
						.getEntiteNode(dossier.getMinistereResp()).getLabel();
			} catch (NullPointerException e) {
				LOGGER.warn("Impossible de récupérer le ministère responsaable du dossier " + dossier.getNumeroNor(), e);
			} catch (ClientException e) {
				LOGGER.warn("Impossible de récupérer le ministère responsaable du dossier " + dossier.getNumeroNor(), e);
			}
		} else {
			if (documentToExport != null) {
				entiteDerniereVersion = documentToExport.getEntite();
			}
			if (entiteDerniereVersion == null) {
				entiteDerniereVersion = "";
			}
			STMinisteresService stMinistereService = STServiceLocator.getSTMinisteresService();
			if ((entiteDerniereVersion == null || entiteDerniereVersion.trim().isEmpty())
					&& ((version != null && nombreDeVersion != null && nombreDeVersion.equals(version))
							|| (version == null && nombreDeVersion != null && nombreDeVersion.equals(1)))) {
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
				} catch (ClientException e) {
					LOGGER.error("Impossible de récupérer le ministère de rattachement " + dossier.getMinistereAttache()
							+ " du dossier " + dossier.getNumeroNor(), e);
				}
			}

			if ((entiteDerniereVersion == null || entiteDerniereVersion.trim().isEmpty()) && documentToExport != null
					&& "ADMINISTRATEUR".equalsIgnoreCase(documentToExport.getCreator())) {
				entiteDerniereVersion = dossier.getMinistereResp();
			} else if (entiteDerniereVersion == null
					|| entiteDerniereVersion.trim().isEmpty() && documentToExport != null) {
				// chercher le ministère de rattachement de l'utilisateur
				// On récupère bien l'id du user
				STUserService stUserService = STServiceLocator.getSTUserService();
				if (documentToExport.getCreator() != null) {
					try {
						entiteDerniereVersion = stUserService.getUserMinisteres(documentToExport.getCreator());
					} catch (ClientException e) {
						LOGGER.error("Erreur lors de la récupération de l'utilisateur " + documentToExport.getCreator()
								+ " " + e.getMessage(),e);
					}
				}
			}
		}
		if (entiteDerniereVersion == null || entiteDerniereVersion.isEmpty()) {
			// Si pas trouvé on met non renseigné
			entiteDerniereVersion = "Non renseigné";
		}
		return entiteDerniereVersion;
	}
	
	private boolean hasWriterBloc(String title) {
		
		if (title.equals(SolonEpgAdamantConstant.BORDEREAU) || 
				title.equals(SolonEpgAdamantConstant.FEUILLE_ROUTE) ||
				title.equals(SolonEpgAdamantConstant.JOURNAL) ||
				title.equals(SolonEpgAdamantConstant.TRAITEMENT_PAPIER) || 
				title.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER) ||
				title.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE) ||
				title.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_NON_CLASIFIED)) {
			return false;
		} 
		return true;
	}

	/**
	 * get le path du répertoire qui va contenir l'archivage definitive
	 * 
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	private String getPathDirArchivageDefinitive(CoreSession session) throws ClientException {
		String generatedArchivageDefinitivePath = STServiceLocator.getSTParametreService().getParametreValue(session,
				STParametreConstant.ARHCHIVAGE_REPERTOIRE);

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
	private Integer exportFDDForArchivage(Dossier dossier, Integer countIds, ZipOutputStream zip,
			StringBuffer binaryDataObjects, StringBuffer descriptiveMetadata, String datePublicationOuFinDossier,
			CoreSession documentManager, String messageIdentifierArchive, AtomicInteger countAAAUI, 
			List<LogEntry> listLogEntryJournal) throws Exception {
		final FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
		Integer compteurDocDansDossier = 1;

		// Liste des documents exportés
		List<DocumentModel> exportedDoc = new ArrayList<DocumentModel>();

		// Export des documents du fond de dossier
		// Pour chaque répertoire du fond de dossier on va parcourir la même
		// chose
		// Nécéssité de gérer les folders parents
		// Répertoire réservé au SGG
		String folderName = SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG;
		List<DocumentModel> documentsToExportFDD = new ArrayList<DocumentModel>();
		try {
			documentsToExportFDD = fondDeDossierService.getAllFilesFromFondDeDossierForArchivage(documentManager,
					dossier, folderName);

			// Export des documents du fond de dossier
			for (DocumentModel docToExportFDD : documentsToExportFDD) {
				countIds = exportDocumentAndWriteXML(countIds, docToExportFDD, dossier, zip, binaryDataObjects,
						descriptiveMetadata, datePublicationOuFinDossier, documentManager, folderName,
						compteurDocDansDossier, messageIdentifierArchive, null, countAAAUI, listLogEntryJournal);
				compteurDocDansDossier = compteurDocDansDossier + 1;
				exportedDoc.add(docToExportFDD);
			}
		} catch (EPGException e) {
			// DO nothing. C'est au cas où le répertoire n'est pas trouvé
		}
		compteurDocDansDossier = 1;
		// Répertoire réservé au ministère porteur et au SGG
		try {
			folderName = SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG;
			documentsToExportFDD = fondDeDossierService.getAllFilesFromFondDeDossierForArchivage(documentManager,
					dossier, folderName);
			for (DocumentModel docToExportFDD : documentsToExportFDD) {
				countIds = exportDocumentAndWriteXML(countIds, docToExportFDD, dossier, zip, binaryDataObjects,
						descriptiveMetadata, datePublicationOuFinDossier, documentManager, folderName,
						compteurDocDansDossier, messageIdentifierArchive, null, countAAAUI, listLogEntryJournal);
				compteurDocDansDossier = compteurDocDansDossier + 1;
				exportedDoc.add(docToExportFDD);
			}
		} catch (EPGException e) {
			// DO nothing. C'est au cas où le répertoire n'est pas trouvé
		}
		compteurDocDansDossier = 1;

		// On liste les répertoires contenus dans Documentation Jointe
		List<String> listRepertoiresPlanClassementDocumentationJointe = new ArrayList<String>();
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ANNEXES_NON_PUBLIEES);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_DIVERS);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DIVERS);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ETUDE_IMPACT);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EXPOSE_MOTIF);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_LETTRE_SAISINE_CE);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_lETTRES_ACCORD);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_LISTE_COM_GOUV);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_NOTE_MIN);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RAPPORT_PRESENTATION);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_TEXTE_CONSOLIDES);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_TEXTE_REF);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_SAISINE_RECTIFICATIVE);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_PDF_ORIGINAL_SIGNE);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES);

		// On vérifie que le répertoire 'Répertoire accessible à tous les
		// utilisateurs' et 'Documentation jointe' ont
		// bien des documents dedans
		// Content
		if (fondDeDossierService.hasFichiersInFoldersInFDD(documentManager, dossier,
				listRepertoiresPlanClassementDocumentationJointe)) {
			// Répertoire accessible à tous les utilisateurs
			// Ecriture de l'archive unit
			descriptiveMetadata.append(TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">");
			countIds = countIds + 1;
			writeContentArchiveUnit(documentManager, descriptiveMetadata, dossier, datePublicationOuFinDossier,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER, false, null, false,
					RECORD_GRP, null, null, null, messageIdentifierArchive, null, null, countAAAUI);
			// Documentation jointe
			// Ecriture de l'archive unit
			descriptiveMetadata.append(TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">");
			countIds = countIds + 1;
			// Content
			writeContentArchiveUnit(documentManager, descriptiveMetadata, dossier, datePublicationOuFinDossier,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE, false, null, false,
					RECORD_GRP, null, null, null, messageIdentifierArchive, null, null, countAAAUI);

			// On parcours la liste des répertoires de documentation jointe
			for (String nomRepertoire : listRepertoiresPlanClassementDocumentationJointe) {
				try {
					// on récupère tous les fichiers de ce répertoire
					documentsToExportFDD = fondDeDossierService
							.getAllFilesFromFondDeDossierForArchivage(documentManager, dossier, nomRepertoire);
					
					for (DocumentModel docToExportFDD : documentsToExportFDD) {
						countIds = exportDocumentAndWriteXML(countIds, docToExportFDD, dossier, zip, binaryDataObjects,
								descriptiveMetadata, datePublicationOuFinDossier, documentManager, nomRepertoire,
								compteurDocDansDossier, messageIdentifierArchive, null, countAAAUI, listLogEntryJournal);
						compteurDocDansDossier = compteurDocDansDossier + 1;
						exportedDoc.add(docToExportFDD);
					}
				} catch (EPGException e) {
					// DO nothing. C'est au cas où le répertoire n'est pas
					// trouvé
					continue;
				}
				compteurDocDansDossier = 1;
			}
			descriptiveMetadata.append(TAG_ARCHIVEUNIT_END);
			descriptiveMetadata.append(TAG_ARCHIVEUNIT_END);
		}
		// Documents divers hors plan de classement
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE);
		listRepertoiresPlanClassementDocumentationJointe
				.add(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR);
		try {
			documentsToExportFDD = fondDeDossierService.getAllFilesFromFondDeDossierForArchivageOutOfFolder(
					documentManager, dossier, listRepertoiresPlanClassementDocumentationJointe);
			documentsToExportFDD.removeAll(exportedDoc); // on retire les
															// documents déjà
															// exportés
			if (documentsToExportFDD != null && !documentsToExportFDD.isEmpty()) {
				// Ecriture de l'archive unit
				descriptiveMetadata.append(TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID + String.format("%02d", countIds) + "\">");
				countIds = countIds + 1;
				// Content
				writeContentArchiveUnit(documentManager, descriptiveMetadata, dossier, datePublicationOuFinDossier,
						"Documents hors plan de classement", false, null, false, RECORD_GRP, null, null,
						compteurDocDansDossier, messageIdentifierArchive, null, null, countAAAUI);
				for (DocumentModel docToExportFDD : documentsToExportFDD) {
					String titreDossierParent = "";
					DocumentModel parentRefDoc = documentManager.getDocument(docToExportFDD.getParentRef());
					if (parentRefDoc != null) {
						FondDeDossierFolder fondDedossierParent = parentRefDoc.getAdapter(FondDeDossierFolder.class);
						titreDossierParent = fondDedossierParent.getTitle();
					}
					countIds = exportDocumentAndWriteXML(countIds, docToExportFDD, dossier, zip, binaryDataObjects,
							descriptiveMetadata, datePublicationOuFinDossier, documentManager, "Documents",
							compteurDocDansDossier, messageIdentifierArchive, titreDossierParent, countAAAUI, listLogEntryJournal);
					compteurDocDansDossier = compteurDocDansDossier + 1;
				}
				descriptiveMetadata.append(TAG_ARCHIVEUNIT_END);
			}
		} catch (EPGException e) {
			// DO nothing. C'est au cas où le répertoire n'est pas trouvé
		}
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
	private Integer exportParapheurForArchivage(Dossier dossier, Integer countIds, ZipOutputStream zip,
			StringBuffer binaryDataObjects, StringBuffer descriptiveMetadata, String datePublicationOuFinDossier,
			CoreSession documentManager, String messageIdentifierArchive, AtomicInteger countAAAUI, List<LogEntry> listLogEntryJournal) throws Exception {
		final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
		String nomRepertoire = SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME;
		List<DocumentModel> documentsToExportParapheur = new ArrayList<DocumentModel>();
		Integer compteurDocDansDossier = 1;
		// Acte intégral
		try {
			documentsToExportParapheur = parapheurService.getAllFilesFromParapheurForArchivage(documentManager, dossier,
					nomRepertoire);
			for (DocumentModel docToExportParapheur : documentsToExportParapheur) {
				countIds = exportDocumentAndWriteXML(countIds, docToExportParapheur, dossier, zip, binaryDataObjects,
						descriptiveMetadata, datePublicationOuFinDossier, documentManager, nomRepertoire,
						compteurDocDansDossier, messageIdentifierArchive, null, countAAAUI, listLogEntryJournal);
				compteurDocDansDossier = compteurDocDansDossier + 1;
			}
		} catch (EPGException e) {
			// DO nothing. C'est au cas où le répertoire n'est pas trouvé
		}
		compteurDocDansDossier = 1;
		// extrait
		try {
			nomRepertoire = SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME;
			documentsToExportParapheur = parapheurService.getAllFilesFromParapheurForArchivage(documentManager, dossier,
					nomRepertoire);
			for (DocumentModel docToExportParapheur : documentsToExportParapheur) {
				countIds = exportDocumentAndWriteXML(countIds, docToExportParapheur, dossier, zip, binaryDataObjects,
						descriptiveMetadata, datePublicationOuFinDossier, documentManager, nomRepertoire,
						compteurDocDansDossier, messageIdentifierArchive, null, countAAAUI, listLogEntryJournal);
				compteurDocDansDossier = compteurDocDansDossier + 1;
			}
		} catch (EPGException e) {
			// DO nothing. C'est au cas où le répertoire n'est pas trouvé
		}
		compteurDocDansDossier = 1;
		// Pièce complémentaires à publier
		try {
			nomRepertoire = SolonEpgParapheurConstants.PARAPHEUR_FOLDER_PIECE_COMPLEMENTAIRE_NAME;
			documentsToExportParapheur = parapheurService.getAllFilesFromParapheurForArchivage(documentManager, dossier,
					nomRepertoire);
			for (DocumentModel docToExportParapheur : documentsToExportParapheur) {
				countIds = exportDocumentAndWriteXML(countIds, docToExportParapheur, dossier, zip, binaryDataObjects,
						descriptiveMetadata, datePublicationOuFinDossier, documentManager, nomRepertoire,
						compteurDocDansDossier, messageIdentifierArchive, null, countAAAUI, listLogEntryJournal);
				compteurDocDansDossier = compteurDocDansDossier + 1;
			}
		} catch (EPGException e) {
			// DO nothing. C'est au cas où le répertoire n'est pas trouvé
		}
		compteurDocDansDossier = 1;
		// Epreuves
		// Ecriture de l'archive unit
		try {
			nomRepertoire = SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME;
			documentsToExportParapheur = parapheurService.getAllFilesFromParapheurForArchivage(documentManager, dossier,
					nomRepertoire);
			for (DocumentModel docToExportParapheur : documentsToExportParapheur) {
				countIds = exportDocumentAndWriteXML(countIds, docToExportParapheur, dossier, zip, binaryDataObjects,
						descriptiveMetadata, datePublicationOuFinDossier, documentManager, nomRepertoire,
						compteurDocDansDossier, messageIdentifierArchive, null, countAAAUI, listLogEntryJournal);
				compteurDocDansDossier = compteurDocDansDossier + 1;
			}
		} catch (EPGException e) {
			// DO nothing. C'est au cas où le répertoire n'est pas trouvé
		}
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
	private void docGenereToBinaryDataOBject(String nomDoc, Integer countDocument, StringBuffer binaryDataObjects,
			ByteArrayOutputStream pdfbaos, Dossier dossier) {
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
		binaryDataObjects.append("<MimeType>" + "application/pdf" + "</MimeType>");
		binaryDataObjects.append("</FormatIdentification>");
		binaryDataObjects.append("<FileInfo>");
		binaryDataObjects.append("<Filename>" + nomDoc + "</Filename>");
		binaryDataObjects.append("<LastModified>" + formatArchiveDateTime(new Date()) + "</LastModified>");
		binaryDataObjects.append("</FileInfo>");
		binaryDataObjects.append("</BinaryDataObject>");
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
	protected Integer exportDocumentAndWriteXML(Integer countIds, DocumentModel docToExport, Dossier dossier,
			ZipOutputStream zip, StringBuffer binaryDataObjects, StringBuffer descriptiveMetadata,
			String datePublicationOuFinDossier, CoreSession documentManager, String folderName,
			Integer compteurDocumentDansDossier, String messageIdentifierArchive,
			String parentImmediatSiHorsPlanDeClassement, AtomicInteger aAAUICount, List<LogEntry> listLogEntryJournal) throws Exception {
		FileSolonEpg docToExportFile = docToExport.getAdapter(FileSolonEpg.class);

		// Content
		descriptiveMetadata.append(TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID).append(String.format("%02d", countIds)).append("\">");
		countIds = countIds + 1;
		String dateDernierTraitement = "";
		if (SolonEpgServiceLocator.getDossierService().isDossierReprise(dossier)) {
			if (listLogEntryJournal != null) {
				// On récupère l'entrée la plus récente
				Date latest = null;
				for (LogEntry logEntryJournal : listLogEntryJournal) {
					if ("FeuilleRoute".equals(logEntryJournal.getCategory()) && 
							(latest == null || latest.before(logEntryJournal.getEventDate()))) {
						latest = logEntryJournal.getEventDate();
					}
				}
				if (latest != null && latest.after(dossier.getDateCreation().getTime()) && docToExportFile.getDateDernierTraitement() != null) {
					dateDernierTraitement = formatArchiveDateTime(docToExportFile.getDateDernierTraitement().getTime());
				} else if (VocabularyConstants.STATUT_PUBLIE.equals(dossier.getStatut())) {
					Calendar datePublication = getDatePublicationFromRetourDila(dossier);
					if(datePublication != null) {
						dateDernierTraitement = formatArchiveDateTime(datePublication.getTime());
					}
				} else if (VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION.equals(dossier.getStatut())) {
					dateDernierTraitement = formatArchiveDateTime(dossier.getDateCreation().getTime());
				}
			}
		}else if (docToExportFile.getDateDernierTraitement() != null) {
			dateDernierTraitement = formatArchiveDateTime(docToExportFile.getDateDernierTraitement().getTime());
		}
		Integer listVersionsDocument = getFileVersionList(docToExport.getId(), documentManager).size();
		writeContentArchiveUnit(documentManager, descriptiveMetadata, dossier, datePublicationOuFinDossier, folderName, false, null,
				false, RECORD_GRP, dateDernierTraitement, docToExportFile, compteurDocumentDansDossier,
				messageIdentifierArchive, parentImmediatSiHorsPlanDeClassement, listVersionsDocument, aAAUICount);
		// Gestion des versions précédentes
		countIds = exportListVersions(docToExport, countIds, dossier, zip, binaryDataObjects, descriptiveMetadata,
				datePublicationOuFinDossier, documentManager, folderName, compteurDocumentDansDossier,
				messageIdentifierArchive, parentImmediatSiHorsPlanDeClassement, aAAUICount, listLogEntryJournal);

		// Export de la version courante
		exportDocumentToZip(zip, docToExport, countIds);
		// Inscription BinaryDataObject dans le XML
		fichierToBinaryDataOBject(docToExport, countIds, binaryDataObjects, dossier, listLogEntryJournal);
		// On renseigne la version courante du document dans une archiveUnit
		descriptiveMetadata.append(TAG_DATAOBJECTREFERENCE_BEG);
		descriptiveMetadata.append(TAG_DATAOBJECTREFERENCEID_BEG+"ID").append(String.format("%02d", countIds))
				.append(TAG_DATAOBJECTREFERENCEID_END);
		descriptiveMetadata.append(TAG_DATAOBJECTREFERENCE_END);
		descriptiveMetadata.append(TAG_ARCHIVEUNIT_END);

		countIds = countIds + 1;

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
	 * @throws ClientException
	 * @throws Exception
	 */
	protected Integer exportListVersions(DocumentModel docToExport, Integer countIds, Dossier dossier,
			ZipOutputStream zip, StringBuffer binaryDataObjects, StringBuffer descriptiveMetadata,
			String datePublicationOuFinDossier, CoreSession documentManager, String folderName,
			Integer compteurDocumentDansDossier, String messageIdentifierArchive,
			String parentImmediatSiHorsPlanDeClassement, AtomicInteger aAAUICount, List<LogEntry> listLogEntryJournal) throws ClientException, Exception {
		try {
			FileSolonEpg docToExportFile = docToExport.getAdapter(FileSolonEpg.class);
			List<DocumentModel> listVersionsDocument = getFileVersionList(docToExport.getId(), documentManager);
			// On retire le premier élément de la liste car il s'agit de la
			// version la plus récente
			listVersionsDocument.remove(0);
			// On a besoin d'avoir de la version la plus ancienne à la plus
			// récente
			Collections.reverse(listVersionsDocument);
			Integer compteurVersion = 1;
			for (DocumentModel versionDocument : listVersionsDocument) {
				if (!versionDocument.getId().equals(docToExport.getId())) {
					FileSolonEpg versionToExportFile = versionDocument.getAdapter(FileSolonEpg.class);

					exportDocumentToZip(zip, versionDocument, countIds);
					fichierToBinaryDataOBject(versionDocument, countIds, binaryDataObjects, dossier, listLogEntryJournal);
					Integer idVersionDocument = countIds;
					countIds = countIds + 1;
					// Ecriture de l'archive unit
					descriptiveMetadata.append(TAG_ARCHIVEUNIT_BEG + ATTR_ID_EQUALS_ID).append(String.format("%02d", countIds))
							.append("\">");
					countIds = countIds + 1;
					// Content
					String dateDernierTraitement = "";
					if (SolonEpgServiceLocator.getDossierService().isDossierReprise(dossier)) {
						if (listLogEntryJournal != null) {
							// On récupère l'entrée la plus récente
							Date latest = null;
							for (LogEntry logEntryJournal : listLogEntryJournal) {
								if ("FeuilleRoute".equals(logEntryJournal.getCategory()) && 
										(latest == null || latest.before(logEntryJournal.getEventDate()))) {
									latest = logEntryJournal.getEventDate();
								}
							}
							if (latest != null && latest.after(dossier.getDateCreation().getTime()) && versionToExportFile.getDateDernierTraitement() != null) {
								dateDernierTraitement = formatArchiveDateTime(versionToExportFile.getDateDernierTraitement().getTime());
							} else if (VocabularyConstants.STATUT_PUBLIE.equals(dossier.getStatut())) {
								Calendar datePublication = getDatePublicationFromRetourDila(dossier);
								if(datePublication != null) {
									dateDernierTraitement = formatArchiveDateTime(datePublication.getTime());
								}
							} else if (VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION.equals(dossier.getStatut())) {
								dateDernierTraitement = formatArchiveDateTime(dossier.getDateCreation().getTime());
							}
						}
					}else if (versionToExportFile.getDateDernierTraitement() != null) {
						dateDernierTraitement = formatArchiveDateTime(versionToExportFile.getDateDernierTraitement().getTime());
					}
					String titreVersionAnterieure = "";
					if ("Documents".equals(folderName.trim())) {
						folderName = "Documents hors plan de classement, " + parentImmediatSiHorsPlanDeClassement;
					}
					if (folderName.trim().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME)) {
						titreVersionAnterieure = folderName + ". Version antérieure " + compteurVersion.toString();
					} else {
						titreVersionAnterieure = folderName + ": Document " + compteurDocumentDansDossier.toString()
								+ ". Version antérieure " + compteurVersion.toString();
					}
					writeContentArchiveUnit(documentManager, descriptiveMetadata, dossier, datePublicationOuFinDossier,
							titreVersionAnterieure, false, compteurVersion, false, "Item", dateDernierTraitement,
							docToExportFile, compteurDocumentDansDossier, messageIdentifierArchive, null,
							listVersionsDocument.size(), aAAUICount);

					descriptiveMetadata.append(TAG_DATAOBJECTREFERENCE_BEG);
					descriptiveMetadata.append(TAG_DATAOBJECTREFERENCEID_BEG+"ID")
							.append(String.format("%02d", idVersionDocument)).append(TAG_DATAOBJECTREFERENCEID_END);
					descriptiveMetadata.append(TAG_DATAOBJECTREFERENCE_END);
					descriptiveMetadata.append(TAG_ARCHIVEUNIT_END);
					countIds = countIds + 1;
					compteurVersion = compteurVersion + 1;
				}
			}
		} catch (FileNotFoundException e) {
			return countIds;
		}
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
	 * @throws ClientException 
	 */
	private void fichierToBinaryDataOBject(DocumentModel docToExport, Integer countDocument,
			StringBuffer binaryDataObjects, Dossier dossier, List<LogEntry> listLogEntryJournal) throws ClientException {
		try {
			FileSolonEpg fichier = docToExport.getAdapter(FileSolonEpg.class);
			// On est obligé de mettre Pattern.quote car sinon le split(".") ne
			// marche pas
			String[] splitDocName = fichier.getFilename().split(Pattern.quote("."));
			String docExtension = "";
			if (splitDocName.length > 0) {
				docExtension = "." + splitDocName[splitDocName.length - 1];
			}
			// Affichage du numéro sur deux chiffres
			binaryDataObjects.append("<BinaryDataObject id=\"ID" + String.format("%02d", countDocument) + "\">");
			binaryDataObjects
					.append("<Uri>Content/ID" + String.format("%02d", countDocument) + docExtension + "</Uri>");
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
				LOGGER.warn(e);
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
			binaryDataObjects.append("<Filename>" + escapeXml(fichier.getFilename()) + "</Filename>");
			String dateDernierTraitement = "";
			if (SolonEpgServiceLocator.getDossierService().isDossierReprise(dossier)) {
				if (listLogEntryJournal != null) {
					// On récupère l'entrée la plus récente
					Date latest = null;
					for (LogEntry logEntryJournal : listLogEntryJournal) {
						if ("FeuilleRoute".equals(logEntryJournal.getCategory()) &&
								(latest == null || latest.before(logEntryJournal.getEventDate()))) {
							latest = logEntryJournal.getEventDate();
						}
					}
					if (latest != null && latest.after(dossier.getDateCreation().getTime()) && fichier.getDateDernierTraitement() != null) {
						dateDernierTraitement = formatArchiveDateTime(fichier.getDateDernierTraitement().getTime());
					} else if (VocabularyConstants.STATUT_PUBLIE.equals(dossier.getStatut())) {
						Calendar datePublication = getDatePublicationFromRetourDila(dossier);
						if(datePublication != null) {
							dateDernierTraitement = formatArchiveDateTime(datePublication.getTime());
						}
					} else if (VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION.equals(dossier.getStatut())) {
						dateDernierTraitement = formatArchiveDateTime(dossier.getDateCreation().getTime());
					}
				}
			}else if (fichier.getDateDernierTraitement() != null) {
				dateDernierTraitement = formatArchiveDateTime(fichier.getDateDernierTraitement().getTime());
			}
			binaryDataObjects.append("<LastModified>" + dateDernierTraitement + "</LastModified>");
			binaryDataObjects.append("</FileInfo>");
			binaryDataObjects.append("</BinaryDataObject>");
		} catch (IOException ex) {
			// Do nothing ?
		}
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
			throws ClientException, FileNotFoundException {
		List<DocumentModel> fileVersionList = null;
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
				if (!fileVersionList.isEmpty()
						&& currentFile.getVersionLabel().equals(fileVersionList.get(0).getVersionLabel())) {
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
			} catch (final ClientException e) {
				throw new RuntimeException("impossible de récupérer le répertoire sélectionné", e);
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.FRENCH);

		// TLD : En Java 1.6, la time zone n'est pas implémentée comme voulu sur
		// le format ISO 8601 donc il faut bricoler :-/
		StringBuilder buffer = new StringBuilder(sdf.format(date));
		return buffer.insert(buffer.length() - 2, ':').toString();
	}

	/**
	 * Retourne l'identifiant séquentiel pour l'archivage - Séquence Oracle
	 * 
	 * @return
	 */
	private String getIdentifiantSequentielArchivage() throws ClientException {
		// Compteur
		String compteur;
		String nuxeoTemplates = Framework.getProperty("nuxeo.templates", "");
		// Si le template oracle n'est pas activé, on utilise le sequenceur
		// nuxeo (utile pour H2)
		if (nuxeoTemplates.contains("oracle")) {
			compteur = Integer.toString(getNumeroChronoArchivage());
		} else {
			final UIDGeneratorService uidGeneratorService = ServiceHelper.getUIDGeneratorService();
			final UIDSequencer sequencer = uidGeneratorService.getSequencer();
			compteur = Integer.toString(sequencer.getNext(SOLON_EPG_ARCHIVAGE_SEQUENCER));
		}

		return compteur;
	}

	private int getNumeroChronoArchivage() throws ClientException {
		final int[] nextVal = new int[1];
		nextVal[0] = -1;
		UIDSequencerImpl.getOrCreatePersistenceProvider().run(true, new RunVoid() {
			@Override
			public void runWith(EntityManager entityManager) throws ClientException {
				// génération du nom de séquence à aller chercher
				String sequenceName = SOLON_EPG_ARCHIVAGE_SEQUENCER;

				// On vérifie que la séquence existe
				final StringBuilder checkSequence = new StringBuilder(
						"SELECT count(1) FROM user_sequences WHERE sequence_name = '").append(sequenceName).append("'");
				final Query checkSequenceQuery = entityManager.createNativeQuery(checkSequence.toString());
				int count = ((Number) checkSequenceQuery.getSingleResult()).intValue();
				// Si elle n'existe pas, on la créée
				if (count == 0) {
					// Si la sequence n'existe pas, on génère une nouvelle
					// séquence
					CoreSession session = null;
					try {
						session = SessionUtil.getCoreSession();
						// Appel de la procédure stockée CREATE_NEW_SEQUENCE qui
						// prend en paramètre le nom de la
						// nouvelle séquence
						StringBuilder createSequence = new StringBuilder("CREATE_NEW_SEQUENCE('").append(sequenceName)
								.append("')");
						QueryUtils.execSqlFunction(session, createSequence.toString(), null);
					} finally {
						if (session != null) {
							SessionUtil.close(session);
						}
					}
				}
				final StringBuilder selectNextVal = new StringBuilder("SELECT ").append(sequenceName)
						.append(".nextval from dual");
				// récupération de la prochaine valeur
				final Query nextValQuery = entityManager.createNativeQuery(selectNextVal.toString());
				@SuppressWarnings("unchecked")
				final List<Number> resultList = nextValQuery.getResultList();
				if (resultList != null && !resultList.isEmpty()) {
					nextVal[0] = (resultList.get(0)).intValue();
				}
			}
		});
		if (nextVal[0] == -1) {
			throw new EPGException("Impossible de générer le numéro chrono pour l'archivage");
		}
		return nextVal[0];
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
	private void exportDocumentToZip(ZipOutputStream zip, DocumentModel docToExport, Integer countDocument) throws Exception {
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
						"Content" + "/" + "ID" + String.format("%02d", countDocument) + docExtension);
				entry.setSize(blobFile.getByteArray().length);
				zip.putNextEntry(entry);
				zip.write(blobFile.getByteArray());
				zip.closeEntry();
			}
		}
	}

	private List<DocumentRouteTableElement> getElements(DocumentRoute currentRoute) {
		return SolonEpgServiceLocator.getDocumentRoutingService().getRouteElements(currentRoute, session);
	}
	
	@Override
	public int getDelaisEligibilite(CoreSession session) throws ClientException {
		ParametrageAdamantService parametrageAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();
		return parametrageAdamantService.getParametrageAdamantDocument(session).getDelaiEligibilite().intValue();
	}
	
	@Override
	public int getNumeroSolon(CoreSession session) throws ClientException {
		ParametrageAdamantService parametrageAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();
		return parametrageAdamantService.getParametrageAdamantDocument(session).getNumeroSolon().intValue();
	}
	
	@Override
	public boolean isDocumentEligible(final Dossier dossier, CoreSession session) throws ClientException {
		ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
		int delaisEligibilite = getDelaisEligibilite(session);
		Date dateNow = new Date();
		Date dateEligibiliteDos=null;
		Date datePublicationOuFinDossier=null;
		
		List<LogEntry> listLogEntryJournal = getLogEntryList(dossier.getDocument().getId());
		
		if(dossier.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
			Calendar dateParution = archiveService.getDateParutionJorfFromRetourDila(dossier);
			if (dateParution == null) {
				ARCHIVAGE_LOGGER.info(dossier.getNumeroNor() + " -> date de fin dernière étape fdr null");
				return false;
			}
			datePublicationOuFinDossier = new Date(dateParution.getTimeInMillis());
			dateEligibiliteDos = addMonthToDate(datePublicationOuFinDossier, delaisEligibilite);
			if (dateEligibiliteDos.before(dateNow)) {
				return true;
			}
		} else if (dossier.getStatut().equals(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION)) {
			Date latest = null;
			if (listLogEntryJournal != null) {
				for (LogEntry logEntryJournal : listLogEntryJournal) {
					if ("label.journal.comment.terminerDossierSansPublication".equals(logEntryJournal.getComment()) &&
							(latest == null || latest.before(logEntryJournal.getEventDate()))) {
						latest = logEntryJournal.getEventDate();
					}
					if (latest != null) {
						datePublicationOuFinDossier = new Date (latest.getTime());
						dateEligibiliteDos = addMonthToDate(datePublicationOuFinDossier, delaisEligibilite);
					} 
				}
			} else {
				// impossible de récupérer le journal
				ARCHIVAGE_LOGGER.info(dossier.getNumeroNor() + " -> Impossible de récupérer le journal");
			}
			if (archiveService.isDossierReprise(dossier) && !(datePublicationOuFinDossier != null && 
					datePublicationOuFinDossier.after(dossier.getDateCreation().getTime()))){
				if (dossier.getDateCreation() != null) {
					dateEligibiliteDos = new Date (dossier.getDateCreation().getTimeInMillis());
					dateEligibiliteDos = addMonthToDate(dateEligibiliteDos, delaisEligibilite);
					if (dateEligibiliteDos.before(dateNow)) {
						return true;
					}
				} else {
					ARCHIVAGE_LOGGER.info(dossier.getNumeroNor() + " -> date de création null");
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
					dateEligibiliteDos = new Date (latest.getTime());
					dateEligibiliteDos = addMonthToDate(dateEligibiliteDos, delaisEligibilite);
					if (dateEligibiliteDos.before(dateNow)) {
						return true;
					}
				}
			} else {
				// impossible de récupérer le journal
				ARCHIVAGE_LOGGER.info(dossier.getNumeroNor() + " -> Impossible de récupérer le journal");
			}
		}
		return false;
	}
	
	/**
	 * Récupère dans la configuration le nombre maximal de dossiers à traiter
	 * lors d'une occurrence du batch.
	 */
	@Override
	public int getMaxDossiersATraiter(CoreSession session) throws ClientException {
		ParametrageAdamantService parametrageAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();
		return parametrageAdamantService.getParametrageAdamantDocument(session).getNbDossierExtraction().intValue();
	}
	
	/**
	 * Ajouter un nombre de mois a une date
	 * @param date
	 * @param month
	 * @return
	 */
	@Override
	public Date addMonthToDate(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}
	
	@Override
	public boolean checkFile(DocumentModel documentModel, CoreSession documentManager) throws ClientException {
		final FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
		final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
		Dossier dossier = documentModel.getAdapter(Dossier.class);
		List<DocumentModel> documentsToExportFDD = new ArrayList<DocumentModel>();
		List<DocumentModel> documentsToExportParapheur = new ArrayList<DocumentModel>();
		try {
			documentsToExportFDD = fondDeDossierService.getAllFilesFromFondDeDossierAllFolder(documentManager, dossier);
			documentsToExportParapheur = parapheurService.getAllFilesFromParapheurAllFolder(documentManager, dossier);
		} catch (ClientException e) {
			throw new ClientException("erreur lors de la récupération des documents du dossier", e);
		}

		if (!documentsToExportFDD.isEmpty() || !documentsToExportParapheur.isEmpty()) {
			for (DocumentModel docToExportFDD : documentsToExportFDD) {
				if (docToExportFDD.getName().contains(".zip")) {
					throw new ClientException("Le dossier contient des fichiers zip.");
				}
			}
			for (DocumentModel docToExportParapheur : documentsToExportParapheur) {
				if (docToExportParapheur.getName().contains(".zip")) {
					throw new ClientException("Le dossier contient des fichiers zip.");
				}
			}
		} else {
			throw new ClientException("Le dossier ne possède pas de fichier dans son bordereau/fond de dossier.");
		}
		return true;
	}
}
