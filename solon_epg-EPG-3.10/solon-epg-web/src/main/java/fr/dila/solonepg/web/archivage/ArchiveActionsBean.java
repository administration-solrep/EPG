package fr.dila.solonepg.web.archivage;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.faces.context.FacesContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.audit.api.LogEntry;
import org.nuxeo.ecm.platform.audit.web.listener.ejb.ContentHistoryActionsBean;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.platform.ui.web.model.SelectDataModelRow;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import fr.dila.ecm.platform.routing.api.DocumentRouteTableElement;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.api.service.PdfGeneratorService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.espace.CorbeilleActionsBean;
import fr.dila.solonepg.web.feuilleroute.DocumentRoutingActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.administration.utilisateur.MailSuggestionActionsBean;
import fr.dila.st.web.pdf.PdfPrintActionsBean;

@Name("archiveActions")
@Scope(CONVERSATION)
public class ArchiveActionsBean implements Serializable {
	/**
	 * Serial UID.
	 */
	private static final long							serialVersionUID		= -1699435653759401973L;

	/**
	 * Logger.
	 */
	private static final Log							log						= LogFactory
																						.getLog(ArchiveActionsBean.class);

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(required = true, create = true)
	protected SSPrincipal								ssPrincipal;

	@In(create = true, required = false)
	protected transient NavigationContext				navigationContext;

	@In(create = true)
	protected transient DocumentsListsManager			documentsListsManager;

	@In(create = true, required = false)
	protected transient ContentViewActions				contentViewActions;

	@In(create = true, required = false)
	protected transient MailSuggestionActionsBean		mailSuggestionActions;

	@In(create = true)
	protected transient CorbeilleActionsBean			corbeilleActions;

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(create = true)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	@In(create = true, required = false)
	protected transient DocumentRoutingActionsBean		routingActions;

	/**
	 * utilisé pour récupérer les informations de la feuille de route
	 */
	@In(create = true, required = false)
	protected transient PdfPrintActionsBean				pdfPrintActions;

	/**
	 * utilisé pour récupérer les informations du journal
	 */
	@In(create = true, required = false)
	protected transient ContentHistoryActionsBean		contentHistoryActions;

	public static final String							MAIL_ERROR_MSG			= "feedback.solonepg.dossier.error.mail";

	public static final String							MAIL_SUCCESS_MSG		= "feedback.solonepg.dossier.mail.ok";

	private Boolean										formCopieMail;
	private String										formObjetMail;
	private String										formTexteMail;
	private List<String>								formListMail;

	/**
	 * Nom des fichiers pdf à générer pour l'impression.
	 */
	private static final String							BORDEREAU_PDF_NAME		= "bordereau.pdf";

	private static final String							JOURNAL_PDF_NAME		= "journal.pdf";

	private static final String							FEUILLE_ROUTE_PDF_NAME	= "feuille_de_route.pdf";

	private static final String							DOSSIER_PDF_NAME		= "dossier.pdf";

	/**
	 * Nom des fichiers zip à générer.
	 */
	private static final String							PARAPHEUR_ZIP_NAME		= "parapheur.zip";

	private static final String							FDD_ZIP_NAME			= "exportFdd.zip";

	private static final String							DOSSIER_ZIP_NAME		= "dossier.zip";

	private static HttpServletResponse getHttpServletResponse() {
		ServletResponse response = null;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			response = (ServletResponse) facesContext.getExternalContext().getResponse();
		}

		if (response != null && response instanceof HttpServletResponse) {
			return (HttpServletResponse) response;
		}
		return null;
	}

	/**
	 * Export pdf du bordereau.
	 * 
	 * @throws Exception
	 */
	public void exportPdfBordereau() throws Exception {
		exportPdf(null, BORDEREAU_PDF_NAME);
	}

	/**
	 * Export pdf du journal.
	 * 
	 * @throws Exception
	 */
	public void exportPdfJournal() throws Exception {
		exportPdf(null, JOURNAL_PDF_NAME);
	}

	/**
	 * Export pdf de la feuille de route.
	 * 
	 * @throws Exception
	 */
	public void exportPdfFeuilleRoute() throws Exception {
		exportPdf(null, FEUILLE_ROUTE_PDF_NAME);
	}

	/**
	 * Export pdf du dossier.
	 * 
	 * @throws Exception
	 */
	public void exportPdfDossier() throws Exception {
		exportPdf(getAllDossierFile(), DOSSIER_PDF_NAME);
	}

	/**
	 * Export zip d'un fond de dossier.
	 * 
	 * @throws Exception
	 */
	public void exportZipFdd() throws Exception {
		exportZip(FDD_ZIP_NAME);
	}

	/**
	 * Export zip d'un parapheur.
	 * 
	 * @throws Exception
	 */
	public void exportZipParapheur() throws Exception {
		exportZip(PARAPHEUR_ZIP_NAME);
	}

	/**
	 * Export zip d'un dossier.
	 * 
	 * @throws Exception
	 */
	public void exportZipDossier() throws Exception {
		exportZip(DOSSIER_ZIP_NAME);
	}

	/**
	 * Export pdf d'une liste de fichier
	 * 
	 * @throws Exception
	 */
	protected void exportPdf(List<DocumentModel> documentToExport, String pdfName) throws Exception {
		// Récupération de la réponse Http
		HttpServletResponse response = getHttpServletResponse();
		if (response == null) {
			return;
		}
		response.reset();
		OutputStream outputStream = response.getOutputStream();
		// Chargement des services
		final PdfGeneratorService pdfGeneratorService = SolonEpgServiceLocator.getPdfGeneratorService();
		final ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
		final JournalService journalService = STServiceLocator.getJournalService();
		// export des fichiers

		if (StringUtils.isEmpty(pdfName)) {
			log.info("type d'impression pdf non définie (journal,bordereau,...)");
			return;
		}
		Document pdfDocument = archiveService.initDocumentPdf(outputStream);
		PdfWriter pdfWriter = archiveService.initWriterPdf(outputStream, pdfDocument);
		if (BORDEREAU_PDF_NAME.equals(pdfName)) {

			pdfGeneratorService.generateBordereau(documentManager, pdfDocument, getCurrentDossier(), false);
			archiveService.closeWritePdf(pdfDocument, outputStream);
		} else if (JOURNAL_PDF_NAME.equals(pdfName)) {

			pdfGeneratorService.generateJournal(pdfDocument, getLogEntryList(), getCurrentDossier(),
					resourcesAccessor.getMessages());
			archiveService.closeWritePdf(pdfDocument, outputStream);
		} else if (FEUILLE_ROUTE_PDF_NAME.equals(pdfName)) {
			pdfGeneratorService.generateFeuilleDeRoute(pdfDocument, getDocumentRouteTableElementList(),
					documentManager, getCurrentDossier(), resourcesAccessor.getMessages(), false);
			archiveService.closeWritePdf(pdfDocument, outputStream);
		} else if (DOSSIER_PDF_NAME.equals(pdfName)) {
			// cas de l'export du dossier complet
			pdfGeneratorService.generateBordereau(documentManager, pdfDocument, getCurrentDossier(), false);
			pdfGeneratorService.generateJournal(pdfDocument, getLogEntryList(), getCurrentDossier(),
					resourcesAccessor.getMessages());
			pdfGeneratorService.generateFeuilleDeRoute(pdfDocument, getDocumentRouteTableElementList(),
					documentManager, getCurrentDossier(), resourcesAccessor.getMessages(), false);
			// export des fichiers à convertir en PDF
			try {
				archiveService.ajoutFichiersDansPdf(documentToExport, pdfDocument, pdfWriter);
			} catch (IOException e) {
				facesMessages.add(StatusMessage.Severity.WARN,
						"Il y a eu une erreur dans la génération du pdf. Opération abandonnée.");
			}
			archiveService.closeWritePdf(pdfDocument, outputStream);
		}

		response.setHeader("Content-Disposition", "attachment; filename=\"" + pdfName + "\";");
		response.setHeader("Content-Type", "application/pdf");
		FacesContext.getCurrentInstance().responseComplete();

		// log de l'export
		journalService.journaliserActionBordereau(documentManager, getCurrentDossierDoc(),
				SolonEpgEventConstant.DOSSIER_IMPRESSION_PDF_EVENT,
				SolonEpgEventConstant.DOSSIER_IMPRESSION_PDF_COMMENT);
	}

	/**
	 * Export zip d'une liste de fichier
	 * 
	 * @throws Exception
	 */
	protected void exportZip(String zipName) throws Exception {
		// Récupération de la réponse Http
		HttpServletResponse response = getHttpServletResponse();
		if (response == null) {
			return;
		}

		// Chargement des services
		final ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
		final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
		final FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
		final PdfGeneratorService pdfGeneratorService = SolonEpgServiceLocator.getPdfGeneratorService();
		final JournalService journalService = STServiceLocator.getJournalService();

		response.reset();
		OutputStream outputStream = response.getOutputStream();

		if (StringUtils.isEmpty(zipName)) {
			log.info("type d'export zip non défini (parapheur,fond de dossier,...)");
			return;
		}
		List<DocumentModel> documentToExport = new ArrayList<DocumentModel>();

		String logEventName;
		String logEventComment;
		String logEventCategory;

		// récupération des fichiers à mettre dans le zip
		if (FDD_ZIP_NAME.equals(zipName)) {
			// FEV509 - On ne retourne que les répertoire qui ont des fichiers
			documentToExport.addAll(fondDeDossierService.getAllFoldersWithDocuments(documentManager,
					getCurrentDossier()));
			logEventName = SolonEpgEventConstant.FDD_IMPRESSION_ZIP_EVENT;
			logEventComment = SolonEpgEventConstant.FDD_IMPRESSION_ZIP_COMMENT;
			logEventCategory = STEventConstant.CATEGORY_FDD;
		} else if (PARAPHEUR_ZIP_NAME.equals(zipName)) {
			documentToExport.addAll(parapheurService.getParapheurDocumentsWithoutEmptyFolders(documentManager,
					getCurrentDossier()));
			logEventName = SolonEpgEventConstant.PARAPHEUR_IMPRESSION_ZIP_EVENT;
			logEventComment = SolonEpgEventConstant.PARAPHEUR_IMPRESSION_ZIP_COMMENT;
			logEventCategory = STEventConstant.CATEGORY_PARAPHEUR;
		} else if (DOSSIER_ZIP_NAME.equals(zipName)) {
			documentToExport.addAll(fondDeDossierService.getAllFoldersWithDocuments(documentManager,
					getCurrentDossier()));
			documentToExport.addAll(parapheurService.getParapheurDocumentsWithoutEmptyFolders(documentManager,
					getCurrentDossier()));
			logEventName = STEventConstant.EVENT_EXPORT_ZIP_DOSSIER;
			logEventComment = STEventConstant.COMMENT_EXPORT_ZIP_DOSSIER;
			logEventCategory = STEventConstant.CATEGORY_PARAPHEUR;
		} else {
			log.info("type d'export zip non défini (parapheur,fond de dossier,...)");
			return;
		}

		// export des fichiers
		if (DOSSIER_ZIP_NAME.equals(zipName)) {
			// on créé le zip avec les dossiers du fichiers et on rajoute les pdfs du journal, du bordereau et de la
			// feuille de route.
			ZipOutputStream zip = archiveService.initWriteZipStreamAndAddFile(documentToExport, outputStream,
					documentManager);
			pdfGeneratorService.addBordereauToZip(documentManager, zip, getCurrentDossier(), false, null);
			pdfGeneratorService.addJournalToZip(documentManager, zip, getCurrentDossier(), getLogEntryList(),
					resourcesAccessor.getMessages(), false, null);
			pdfGeneratorService.addFeuilleRouteToZip(zip, getCurrentDossier(), getDocumentRouteTableElementList(),
					documentManager, resourcesAccessor.getMessages(), false, null);
			zip.close();
		} else {
			archiveService.writeZipStream(documentToExport, outputStream, getCurrentDossierDoc(), documentManager);
		}

		response.setHeader("Content-Disposition", "attachment; filename=\"" + zipName + "\";");
		response.setHeader("Content-Type", "application/zip");
		FacesContext.getCurrentInstance().responseComplete();

		// log de l'export
		journalService.journaliserAction(documentManager, getCurrentDossierDoc(), logEventName, logEventComment,
				logEventCategory);
	}

	protected List<DocumentModel> getAllDossierFile() throws ClientException {
		// récupération des fichiers à convertir en pdf et à merger pour l'export
		Dossier dossier = getCurrentDossier();
		List<DocumentModel> listeDocsToSend = SolonEpgServiceLocator.getFondDeDossierService().getFddDocuments(
				documentManager, dossier);
		List<DocumentModel> listeParapheur = SolonEpgServiceLocator.getParapheurService().getParapheurDocuments(
				documentManager, dossier);
		listeDocsToSend.addAll(listeParapheur);
		return listeDocsToSend;
	}

	protected List<DocumentModel> getAllDossierFileFromFolderNotEmpty() throws ClientException {
		// récupération des fichiers
		Dossier dossier = getCurrentDossier();
		List<DocumentModel> listeDocsToSend = SolonEpgServiceLocator.getFondDeDossierService()
				.getAllFoldersWithDocuments(documentManager, dossier);
		List<DocumentModel> listeParapheur = SolonEpgServiceLocator.getParapheurService()
				.getParapheurDocumentsWithoutEmptyFolders(documentManager, dossier);
		listeDocsToSend.addAll(listeParapheur);
		return listeDocsToSend;
	}

	/**
	 * Récupère la liste des DocumentRouteTableElement à partir de la liste des SelectDataModelRow.
	 * 
	 * @param SelectDataModelRowList
	 * @return
	 * @throws ClientException
	 */
	protected List<DocumentRouteTableElement> getDocumentRouteTableElementList() throws ClientException {
		List<SelectDataModelRow> selectDataModelRowList = routingActions
				.computeDossierSelectDataModelRelatedRouteElements().getRows();
		List<DocumentRouteTableElement> documentRouteTableElementList = new ArrayList<DocumentRouteTableElement>();
		for (SelectDataModelRow selectDataModelRow : selectDataModelRowList) {
			documentRouteTableElementList.add((DocumentRouteTableElement) selectDataModelRow.getData());
		}
		return documentRouteTableElementList;
	}

	protected List<LogEntry> getLogEntryList() throws ClientException {
		return contentHistoryActions.computeLogEntries();
	}

	/**
	 * Envoi des mails
	 * 
	 * @throws Exception
	 */
	public String envoyerMailDossier() throws Exception {
		if (formListMail == null || formListMail.isEmpty()) {
			facesMessages.add(StatusMessage.Severity.WARN, resourcesAccessor.getMessages().get(MAIL_ERROR_MSG));
			return routingWebActions.getFeuilleRouteView();
		}

		List<String> listMail = new ArrayList<String>();
		for (String destId : formListMail) {
			String destMail = (String) mailSuggestionActions.getMailInfo(destId).get(
					MailSuggestionActionsBean.ENTRY_KEY_NAME);
			listMail.add(destMail);
		}

		try {
			// FEV509 - On ne renvoi que les répertoires qui contiennent des fichiers
			SolonEpgServiceLocator.getArchiveService().sendMail(documentManager, getAllDossierFileFromFolderNotEmpty(),
					listMail, formCopieMail, formObjetMail, formTexteMail, getCurrentDossierDoc(), getLogEntryList(),
					getDocumentRouteTableElementList(), resourcesAccessor.getMessages());
			facesMessages.add(StatusMessage.Severity.INFO, resourcesAccessor.getMessages().get(MAIL_SUCCESS_MSG));
		} catch (Exception e) {
			String message = resourcesAccessor.getMessages().get(MAIL_ERROR_MSG);
			log.error(message, e);
			facesMessages.add(StatusMessage.Severity.WARN, message);
		}

		resetMailDossier();
		return routingWebActions.getFeuilleRouteView();
	}

	/**
	 * Annuler l'envoi des mails
	 */
	public String annulerMailDossier() {
		resetMailDossier();
		return routingWebActions.getFeuilleRouteView();

	}

	private void resetMailDossier() {
		formCopieMail = true;
		formObjetMail = null;
		formTexteMail = null;
		formListMail = null;
	}

	private Dossier getCurrentDossier() {
		return navigationContext.getCurrentDocument().getAdapter(Dossier.class);
	}

	/**
	 * Retourne le Case courant attaché à la feuille de route, ou nul si on le document courant n'est pas un Case.
	 * 
	 * @return Document case
	 */
	private DocumentModel getCurrentDossierDoc() {
		DocumentModel currentDocument = navigationContext.getCurrentDocument();
		if (!currentDocument.hasFacet(STConstant.ROUTABLE_FACET)) {
			return null;
		}
		return currentDocument;
	}

	public Boolean getFormCopieMail() {
		return formCopieMail;
	}

	public void setFormCopieMail(Boolean formCopieMail) {
		this.formCopieMail = formCopieMail;
	}

	public String getFormObjetMail() {
		formObjetMail = "Informations relatives au dossier n°" + getCurrentDossier().getNumeroNor();
		return formObjetMail;
	}

	public void setFormObjetMail(String formObjetMail) {
		this.formObjetMail = formObjetMail;
	}

	public String getFormTexteMail() {
		String ministereLabel = "";

		// Par défaut le ministère responsable du dossier comme destinataire du mail
		OrganigrammeNode ministereNode;
		try {
			ministereNode = STServiceLocator.getSTMinisteresService().getEntiteNode(
					getCurrentDossier().getMinistereResp());
		} catch (ClientException e) {
			ministereNode = null;
		}
		if (ministereNode != null) {
			ministereLabel = ministereNode.getLabel();
		}

		StringBuilder formTexteMailtemp = new StringBuilder("Bonjour,\n\nVous trouverez ci-joint le dossier n°");
		formTexteMailtemp.append(getCurrentDossier().getNumeroNor());
		formTexteMailtemp.append(" \"");
		if (getCurrentDossier().getTitreActe() != null) {
			formTexteMailtemp.append(getCurrentDossier().getTitreActe());
		}
		formTexteMailtemp.append("\" et les documents associés.\nCordialement.\n\n");
		formTexteMailtemp.append(ministereLabel);
		formTexteMail = formTexteMailtemp.toString();
		return formTexteMail;
	}

	public void setFormTexteMail(String formTexteMail) {
		this.formTexteMail = formTexteMail;
	}

	public List<String> getFormListMail() {
		return formListMail;
	}

	public void setFormListMail(List<String> formListMail) {
		this.formListMail = formListMail;
	}

}
