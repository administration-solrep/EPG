package fr.dila.solonepg.api.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.audit.api.LogEntry;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import fr.dila.ecm.platform.routing.api.DocumentRouteTableElement;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.ss.api.service.SSArchiveService;

/**
 * Interface du service d'archive de solon epg
 * 
 * @author arolin
 * 
 */
public interface ArchiveService extends SSArchiveService, Serializable {

	/**
	 * Envoi de mails SolonEpg : ajout des fichiers pdf du journal, du bordereau et de la feuille de route.
	 * 
	 * @param documentManager
	 * @param files
	 *            fichier à ajouter en pièce jointe
	 * @param listMail
	 *            destinataires
	 * @param formCopieMail
	 *            l'utilisateur courant est en copie
	 * @param formObjetMail
	 *            objet du mail
	 * @param formTexteMail
	 *            contenu du mail
	 * @param dossierDoc
	 *            dossier auquel on va rattacher le log
	 * @param logEntryList
	 *            entrées du journal
	 * @param documentRouteTableElement
	 *            étape de feuilles de route
	 * @param messages
	 * @throws Exception
	 */
	void sendMail(CoreSession documentManager, List<DocumentModel> files, List<String> listMail, Boolean formCopieMail,
			String formObjetMail, String formTexteMail, DocumentModel dossiersDoc, List<LogEntry> logEntryList,
			List<DocumentRouteTableElement> documentRouteTableElement, Map<String, String> messages) throws Exception;

	/**
	 * Récupère une liste de documents, récupère les fichiers PDF et pouvant être convertit en PDF, les fusionne et
	 * l'ajoute dans le stream.
	 */
	void writePdfStream(CoreSession session, List<DocumentModel> files, OutputStream outputStream, Dossier dossier,
			List<LogEntry> logEntryList, List<DocumentRouteTableElement> documentRouteTableElement,
			Map<String, String> messages) throws Exception;

	/**
	 * Initialise un ZipStream et y ajoute les fichiers sélectionnés.
	 */
	ZipOutputStream initWriteZipStreamAndAddFile(List<DocumentModel> files, OutputStream outputStream,
			CoreSession session) throws Exception;

	/**
	 * Initialise la création du document PDF.
	 * 
	 * @param outputStream
	 * @return Document
	 */
	Document initDocumentPdf(OutputStream outputStream) throws Exception;

	/**
	 * Initialise la création du fichier PDF.
	 * 
	 * @param outputStream
	 * @return Document
	 */
	PdfWriter initWriterPdf(OutputStream outputStream, Document document) throws Exception;

	/**
	 * Initialise la création d'un fichier PDF.
	 * 
	 * @param pdfDocument
	 * @param outputStream
	 */
	void closeWritePdf(Document pdfDocument, OutputStream outputStream) throws Exception;

	/**
	 * Convertit les fichiers en PDF et les ajoute au PDF en cours de création
	 * 
	 * @param files
	 * @param document
	 * @param writer
	 * @param cb
	 * @throws Exception
	 * @throws IOException
	 */
	void ajoutFichiersDansPdf(List<DocumentModel> files, Document document, PdfWriter writer) throws Exception;

	/**
	 * 
	 * @param session
	 * @param outputStream
	 * @param listeEliminationId
	 * @throws Exception
	 */
	public void generateListeEliminationPdf(OutputStream outputStream, String[] docIds) throws Exception;

	/**
	 * 
	 * @param dossierDocs
	 * @param session
	 * @throws Exception
	 */
	void retirerArchivageDefinitive(final List<DocumentModel> dossierDocs, CoreSession session) throws Exception;

	/**
	 * @param routingTaskType
	 * @param labelKey
	 * @param resourcesAccessor
	 * @return
	 */
	String getActionFeuilleRouteButtonLabel(String routingTaskType, String labelKey, Map<String, String> messages);

	/**
	 * Retourne le label qui correspond au pictogramme pour une étape de feuille de route
	 * 
	 * @param routingTaskType
	 * @param validationStatusId
	 * @param resourcesAccessor
	 * @return
	 */
	String idIconeToLabelFDR(String routingTaskType, String validationStatusId, Map<String,String> messages);

	/**
	 * Retourne la date de publication au JO du dossier
	 * 
	 * @param documentModel
	 * @return
	 * @throws ClientException 
	 */	
	Calendar getDateParutionJorfFromRetourDila(Dossier dossier);

	/**
	 * Retourne true si le dossier est issue de la reprise 
	 * 
	 * @param documentModel
	 * @return
	 * @throws ClientException 
	 */	
	boolean isDossierReprise(Dossier dossier);
	
	String getValidStatusToLabel(String key);
}
