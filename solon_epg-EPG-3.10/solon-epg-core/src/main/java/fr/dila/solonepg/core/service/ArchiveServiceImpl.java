package fr.dila.solonepg.core.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
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
import org.nuxeo.ecm.platform.audit.api.LogEntry;
import org.nuxeo.ecm.platform.convert.ooomanager.OOoManagerService;
import org.nuxeo.ecm.platform.io.selectionReader.DocumentModelListReader;
import org.nuxeo.ecm.platform.reporting.engine.BirtEngine;
import org.nuxeo.ecm.platform.reporting.report.ReportHelper;
import org.nuxeo.runtime.api.Framework;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import fr.dila.ecm.platform.routing.api.DocumentRouteTableElement;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.api.service.CorbeilleService;
import fr.dila.solonepg.api.service.DossierSignaleService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.api.service.PdfGeneratorService;
import fr.dila.solonepg.core.archive.SolonEpgZipWriter;
import fr.dila.ss.core.service.SSArchiveServiceImpl;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;

/**
 * Implémentation du service d'archivage epg
 * 
 */
public class ArchiveServiceImpl extends SSArchiveServiceImpl implements ArchiveService {
	private static final long			serialVersionUID				= -7780914517900449689L;

	/**
	 * Logger.
	 */
	private static final Log			LOGGER							= LogFactory.getLog(ArchiveServiceImpl.class);

	private static Map<String, String>	validStatusToLabel;
	
	static {
		validStatusToLabel = new HashMap<String, String>();
		// État validée manuellement
		validStatusToLabel.put("1", "label.epg.feuilleRoute.etape.valide.manuellement");
		// État avis favorable avec correction
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_CORRECTION_VALUE,
				"label.epg.feuilleRoute.etape.valide.avisFavorableCorrection");
		// État invalidée
		validStatusToLabel.put("2", "label.epg.feuilleRoute.etape.valide.refusValidation");
		// État validée automatiquement
		validStatusToLabel.put("3", "label.epg.feuilleRoute.etape.valide.automatiquement");
		// État avec sortie 'non concerné'
		validStatusToLabel.put("4", "label.epg.feuilleRoute.etape.valide.nonConcerne");
		// État retour pour modification
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETOUR_MODIFICATION_VALUE,
				"label.epg.feuilleRoute.etape.valide.retourModification");
		// État validée manuellement : avis rendu
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_POUR_AVIS_RENDU_VALUE,
				"label.epg.feuilleRoute.etape.valide.manuellement.avisRendu");
		// État invalidée : dessaisissement
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_DESSAISISSEMENT_VALUE,
				"label.epg.feuilleRoute.etape.valide.refusValidation.dessaisissement");
		// État invalidée : retrait
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETRAIT_VALUE,
				"label.epg.feuilleRoute.etape.valide.refusValidation.retrait");
		// État invalidée : refus
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_REFUS_VALUE,
				"label.epg.feuilleRoute.etape.valide.manuellement.refus");
		// État invalidée : renvoi
		validStatusToLabel.put(SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RENVOI_VALUE,
				"label.epg.feuilleRoute.etape.valide.manuellement.renvoi");
	}

	/**
	 * Default constructor
	 */
	public ArchiveServiceImpl() {
		super();
	}

	@Override
	public void sendMail(CoreSession documentManager, List<DocumentModel> files, List<String> listMail,
			Boolean formCopieMail, String formObjetMail, String formTexteMail, DocumentModel dossiersDoc,
			List<LogEntry> formLogEntryList, List<DocumentRouteTableElement> formDocumentRouteTableElement,
			Map<String, String> formMessages) throws Exception {

		List<DocumentModel> dossierModelList = new ArrayList<DocumentModel>();
		dossierModelList.add(dossiersDoc);
		sendMail(documentManager, files, listMail, formCopieMail, formObjetMail, formTexteMail, dossierModelList,
				formLogEntryList, formDocumentRouteTableElement, formMessages);
	}

	@Override
	public void sendMail(CoreSession documentManager, List<DocumentModel> files, List<String> listMail,
			Boolean formCopieMail, String formObjetMail, String formTexteMail, List<DocumentModel> dossiers)
			throws Exception {

		sendMail(documentManager, files, listMail, formCopieMail, formObjetMail, formTexteMail, dossiers, null, null,
				null);
	}

	/**
	 * 
	 * @param documentManager
	 * @param files
	 * @param listMail
	 * @param formCopieMail
	 * @param formObjetMail
	 * @param formTexteMail
	 * @param dossiersDoc
	 * @param logEntryList
	 * @param documentRouteTableElement
	 * @param messages
	 * @throws Exception
	 */
	protected void sendMail(CoreSession documentManager, List<DocumentModel> files, List<String> listMail,
			Boolean formCopieMail, String formObjetMail, String formTexteMail, List<DocumentModel> dossiersDoc,
			List<LogEntry> formLogEntryList, List<DocumentRouteTableElement> formDocumentRouteTableElement,
			Map<String, String> formMessages) throws Exception {

		// write the ZIP content to the output stream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

		zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
		writeZipStream(documentManager, files, zipOutputStream);

		for (DocumentModel dossierModelDoc : dossiersDoc) {
			generateCurrentDocumentPdf(zipOutputStream, dossierModelDoc, documentManager, formLogEntryList,
					formDocumentRouteTableElement, formMessages);
		}

		zipOutputStream.close();

		byte[] bytes = outputStream.toByteArray();

		String nomFichier = "export_solonepg_" + DateUtil.formatDDMMYYYY(new GregorianCalendar()) + ".zip";
		DataSource dataSource = new ByteArrayDataSource(bytes, "application/zip");

		// destinataire en copie
		if (formCopieMail) {
			NuxeoPrincipal user = (NuxeoPrincipal) documentManager.getPrincipal();
			if (user != null) {
				String userMail = STServiceLocator.getOrganigrammeService().getMailFromUid(user.getName());
				if (StringUtils.isNotBlank(userMail)) {
					if (listMail == null) {
						listMail = new ArrayList<String>();
					}
					listMail.add(userMail);
				}
			}
		}

		// envoi du mail
		STServiceLocator.getSTMailService().sendMailWithAttachement(listMail, formObjetMail, formTexteMail, nomFichier,
				dataSource);

		// Chargement des services
		final JournalService journalService = STServiceLocator.getJournalService();
		// log de l'action
		for (DocumentModel dossierDoc : dossiersDoc) {
			journalService.journaliserActionParapheur(documentManager, dossierDoc,
					STEventConstant.EVENT_ENVOI_MAIL_DOSSIER, STEventConstant.COMMENT_ENVOI_MAIL_DOSSIER);
		}
	}
	
	@Override
	public void supprimerDossier(CoreSession session, DocumentModel dossierDoc, Principal principal) throws Exception {
		// suppression du dossier signalé lié au dossier
		List<DocumentModel> dossierSignaleList = new ArrayList<DocumentModel>();
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
	protected void logSuppressionDossier(final CoreSession session, final DocumentModel dossierDoc, final Principal principal) throws ClientException {
		final JournalService journalService = STServiceLocator.getJournalService();
		if (principal == null) {
			journalService.journaliserActionAdministration(session, dossierDoc, STEventConstant.EVENT_ARCHIVAGE_DOSSIER, STEventConstant.COMMENT_SUPPRESSION_DOSSIER);
		} else {
			journalService.journaliserActionAdministration(session, principal, dossierDoc, STEventConstant.EVENT_ARCHIVAGE_DOSSIER, STEventConstant.COMMENT_SUPPRESSION_DOSSIER);
		}
	}

	@Override
	public void writeZipStream(CoreSession session, List<DocumentModel> files, ZipOutputStream outputStream)
			throws Exception {
		DocumentReader reader = null;
		DocumentWriter writer = null;

		reader = new DocumentModelListReader(files);

		writer = new SolonEpgZipWriter(session, outputStream);

		DocumentPipe pipe = new DocumentPipeImpl(10);
		pipe.setReader(reader);
		pipe.setWriter(writer);

		pipe.run();

		reader.close();
	}

	@Override
	protected List<DocumentModel> findDossierLinkUnrestricted(CoreSession session, String identifier)
			throws ClientException {
		final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
		return corbeilleService.findDossierLinkUnrestricted(session, identifier);
	}

	/**
	 * ajouter les documents du dossier (bordereau,journal et feuille de route) au zip.
	 * 
	 * @param outputStream
	 *            ZipOutputStream
	 * @param dossierDoc
	 * @param session
	 * @throws Exception
	 */
	private void generateCurrentDocumentPdf(ZipOutputStream outputStream, DocumentModel dossierDoc,
			CoreSession session, List<LogEntry> formLogEntryList,
			List<DocumentRouteTableElement> formDocumentRouteTableElement, Map<String, String> formMessages)
			throws Exception {
		// ajout du pdf comme entrée dans le ZipOutputStream
		PdfGeneratorService pdfGeneratorService = SolonEpgServiceLocator.getPdfGeneratorService();
		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		// on ajoute les pdf du bordereau de la feuille de route et du journal
		ZipEntry entry = new ZipEntry("Dossier.pdf");
		outputStream.putNextEntry(entry);
		Document document = new Document(PageSize.A4.rotate());
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		writer.setCloseStream(false);
		document.open();
		pdfGeneratorService.generateBordereau(session, document, dossier, false);
		if (formMessages != null && formLogEntryList != null) {
			pdfGeneratorService.generateJournal(document, formLogEntryList, dossier, formMessages);
		}
		if (formMessages != null && formDocumentRouteTableElement != null) {
			pdfGeneratorService.generateFeuilleDeRoute(document, formDocumentRouteTableElement, session, dossier,
					formMessages, false);
		}
		document.close();
		outputStream.closeEntry();
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
	private void generateCurrentDocumentPdf(ZipOutputStream outputStream, DocumentModel dossierDoc, CoreSession session)
			throws Exception {
		generateCurrentDocumentPdf(outputStream, dossierDoc, session, null, null, null);
	}

	@Override
	public ZipOutputStream initWriteZipStreamAndAddFile(List<DocumentModel> files, OutputStream outputStream,
			CoreSession session) throws Exception {
		ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
		zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
		writeZipStream(session, files, zipOutputStream);
		return zipOutputStream;
	}

	@Override
	public void writeZipStream(List<DocumentModel> files, OutputStream outputStream, DocumentModel dossierDoc,
			CoreSession session) throws Exception {
		ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
		zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
		writeZipStream(session, files, zipOutputStream);
		generateCurrentDocumentPdf(zipOutputStream, dossierDoc, session);
		zipOutputStream.close();

	}

	@Override
	public void writeZipStream(List<DocumentModel> files, OutputStream outputStream, List<DocumentModel> dossiers,
			CoreSession session) throws Exception {
		ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
		zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
		writeZipStream(session, files, zipOutputStream);

		for (DocumentModel dossierDoc : dossiers) {
			generateCurrentDocumentPdf(zipOutputStream, dossierDoc, session);
		}

		zipOutputStream.close();
	}

	@Override
	public void writePdfStream(CoreSession session, List<DocumentModel> files, OutputStream outputStream,
			Dossier dossier, List<LogEntry> logEntryList, List<DocumentRouteTableElement> documentRouteTableElement,
			Map<String, String> messages) throws Exception {
		// see http://java-x.blogspot.com/2006/11/merge-pdf-files-with-itext.html
		Document document = new Document(PageSize.A4.rotate());

		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		document.open();

		// on ajoute une methode pour lancer l'impression du pdf dès son ouverture
		lancerDemandeImpressionPdf(writer);

		// on ajoute les pdf du bordereau de la feuille de route et du journal
		PdfGeneratorService pdfGeneratorService = SolonEpgServiceLocator.getPdfGeneratorService();
		pdfGeneratorService.generateBordereau(session, document, dossier, false);
		pdfGeneratorService.generateJournal(document, logEntryList, dossier, messages);
		pdfGeneratorService.generateFeuilleDeRoute(document, documentRouteTableElement, session, dossier, messages,
				false);

		// convertit les fichiers en PDF et les ajoute au PDF en cours de création
		ajoutFichiersDansPdf(files, document, writer);

		outputStream.flush();
		document.close();
		outputStream.close();
	}

	@Override
	public void ajoutFichiersDansPdf(List<DocumentModel> files, Document document, PdfWriter writer) throws Exception {
		// see http://java-x.blogspot.com/2006/11/merge-pdf-files-with-itext.html

		PdfContentByte contentByte = writer.getDirectContent(); // Holds the PDF

		// récupération des fichiers convertis en pdf
		List<InputStream> pdfs;
		try {
			pdfs = convertDocumentModelsBlobsToPdf(files);
		} catch (IOException e) {
			throw new IOException(e);
		}

		List<PdfReader> readers = new ArrayList<PdfReader>();

		Iterator<InputStream> iteratorPDFs = pdfs.iterator();

		// Create Readers for the pdfs.
		while (iteratorPDFs.hasNext()) {
			InputStream pdf = iteratorPDFs.next();
			PdfReader pdfReader = new PdfReader(pdf);
			readers.add(pdfReader);
		}

		// data
		PdfImportedPage page;
		int pageOfCurrentReaderPDF = 0;
		Iterator<PdfReader> iteratorPDFReader = readers.iterator();

		// Loop through the PDF files and add to the output.
		while (iteratorPDFReader.hasNext()) {
			PdfReader pdfReader = iteratorPDFReader.next();

			// Create a new page in the target for each source page.
			while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
				pageOfCurrentReaderPDF++;
				// définit si l'on affiche la page au format portrait ou paysage
				document.setPageSize(pdfReader.getPageSizeWithRotation(pageOfCurrentReaderPDF));
				document.newPage();
				page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
				contentByte.addTemplate(page, 0, 0);
			}
			pageOfCurrentReaderPDF = 0;
		}
	}

	/**
	 * Convertis les Blobs d'une liste de DocumentModel en pdf puis stocke ces pdfs dans une liste d'InputStream.
	 * 
	 * @param files
	 * @return
	 * @throws Exception
	 */
	protected List<InputStream> convertDocumentModelsBlobsToPdf(List<DocumentModel> files) throws Exception {
		List<InputStream> pdfs = new ArrayList<InputStream>();
		// on parcourt tous les document afin de récupérer tous les documents convertissable en pdf
		for (DocumentModel documentModel : files) {
			String docType = documentModel.getType();
			if (docType != null && !docType.isEmpty()) {
				if (docType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)
						|| docType.equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)) {
					fr.dila.st.api.domain.file.File file = documentModel
							.getAdapter(fr.dila.st.api.domain.file.File.class);
					Blob blob = file.getContent();
					if (blob != null) {
						// si le le fichier est vide : on ne fait rien
						// si le blob est déjà au format pdf : on ne fait rien
						if (!(blob.getMimeType() != null && blob.getMimeType().equals("application/pdf"))) {
							String converterName;
							ConversionService conversionService = null;
							try {
								OOoManagerService ooManagerService = Framework.getLocalService(OOoManagerService.class);
								if(ooManagerService == null) {
									throw new IOException("Le daemon OpenOffice ne peut pas être trouvé.");
								}
								if (!ooManagerService.isOOoManagerStarted()) {
									LOGGER.warn("Daemon OOo non actif. Démarrage.");
									ooManagerService.startOOoManager();
								}
								// on convertit le fichier en pdf si celui-ci peut être converti
								conversionService = Framework.getLocalService(ConversionService.class);
								converterName = conversionService.getConverterName(blob.getMimeType(),
										"application/pdf");

								if (!conversionService.isConverterAvailable(converterName, true).isAvailable()) {
									LOGGER.info("Convertisseur pdf non disponible : Redémarrage daemon ooo");
									ooManagerService.stopOOoManager();
									ooManagerService.startOOoManager();
									converterName = conversionService.getConverterName(blob.getMimeType(),
											"application/pdf");
								}
							} catch (IOException exc) {
								LOGGER.error("Erreur de démarrage du daemon OpenOffice", exc);
								throw new IOException(exc);
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
									throw new IOException(exc);
								} catch (ClientException e) {
									throw new IOException(e);
								}
							}
						}
						if (blob != null) {
							// ajout du blob dans la liste des pdfs à merger
							pdfs.add(blob.getStream());
						}
					}
				}
			}
		}
		return pdfs;
	}

	/**
	 * Ajoute un script pour demander l'impression du PDF dès son ouverture.
	 * 
	 * @param writer
	 */
	protected void lancerDemandeImpressionPdf(PdfWriter writer) {
		StringBuffer javascript = new StringBuffer();
		// javascript.append("this.print({bUI:false});");
		javascript.append("this.print();");
		PdfAction pdfAction = PdfAction.javaScript(javascript.toString(), writer);
		writer.addJavaScript(pdfAction);
	}

	@Override
	public Document initDocumentPdf(OutputStream outputStream) throws Exception {
		// initalise le document Pdf : par defaut le pdf est au format paysage.
		return new Document(PageSize.A4.rotate());
	}

	@Override
	public PdfWriter initWriterPdf(OutputStream outputStream, Document document) throws Exception {
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		document.open();
		// on ajoute une methode pour lancer l'impression du pdf dès son ouverture
		lancerDemandeImpressionPdf(writer);
		return writer;
	}

	@Override
	public void closeWritePdf(Document pdfDocument, OutputStream outputStream) throws Exception {
		// on ferme le stream et le document
		outputStream.flush();
		pdfDocument.close();
		outputStream.close();
	}

	@Override
	public void retirerArchivageDefinitive(final List<DocumentModel> dossierDocs, CoreSession session) throws Exception {
		// on lance les suppressions des dossiers sans vérifier les droits
		new UnrestrictedSessionRunner(session) {
			@Override
			public void run() throws ClientException {
				for (DocumentModel documentModel : dossierDocs) {
					Dossier dossier = documentModel.getAdapter(Dossier.class);
					dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE);
					dossier.save(session);
				}
				session.save();
			}
		}.runUnrestricted();
	}

	@Override
	public void generateListeEliminationPdf(OutputStream outputStream, String[] docIds) throws Exception {
		// Lecture du fichier birt .rptdesign

		InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(SolonEpgConstant.BIRT_REPORT_BORDEREAU_ARCHIVAGE);
		if (inputStream == null) {
			inputStream = Framework.getResourceLoader().getResourceAsStream(
					SolonEpgConstant.BIRT_REPORT_BORDEREAU_ARCHIVAGE);
		}
		IReportRunnable nuxeoReport = ReportHelper.getNuxeoReport(inputStream);
		IRunAndRenderTask task = BirtEngine.getBirtEngine().createRunAndRenderTask(nuxeoReport);

		HTMLRenderOption options = new HTMLRenderOption();
		options.setImageHandler(new HTMLServerImageHandler());
		options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
		options.setOutputStream(outputStream);

		StringBuilder stringBuilder = new StringBuilder();
		for (String docId : docIds) {
			if (stringBuilder.length() != 0) {
				stringBuilder.append(",");
			}
			stringBuilder.append("'").append(docId).append("'");
		}
		// Paramètre des id de la liste d'élimination
		Map<String, String> inputValues = new HashMap<String, String>();
		inputValues.put("LISTDOCS_PARAM", stringBuilder.toString());

		task.setParameterValues(inputValues);
		task.setRenderOption(options);
		task.run();
		task.close();
	}

	@Override
	public String getActionFeuilleRouteButtonLabel(String routingTaskType, String labelKey,
			Map<String, String> messages) {
		final String key = labelKey + "." + routingTaskType;
		String label = messages.get(key);
		if (key.equals(label)) {
			return messages.get(labelKey);
		} else {
			return label;
		}
	}
	
	/**
	 * Retourne true si le dossier est issue de la reprise de solon v1
	 * 
	 * @param doc
	 */
	@Override
	public boolean isDossierReprise(Dossier dossier){
		if (dossier.getIndexationRubrique().contains("REPRISE")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Retourne la date de publication au JO d'un dossier
	 * 
	 * @param doc
	 */
	@Override
	public Calendar getDateParutionJorfFromRetourDila(Dossier dossier){
		Calendar datePublication = null;
		RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
		if (retourDila != null && retourDila.getDateParutionJorf() != null) {
			datePublication = retourDila.getDateParutionJorf();
		}
		return datePublication;
	}

	@Override
	public String idIconeToLabelFDR(String routingTaskType, String validationStatusId,
			Map<String,String> messages) {
		return getActionFeuilleRouteButtonLabel(routingTaskType, validStatusToLabel.get(validationStatusId),
				messages);
	}

	@Override
	public String getValidStatusToLabel(String key) {
		return validStatusToLabel.get(key);
	}
	
}
