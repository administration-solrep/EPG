package fr.dila.solonepg.api.service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.audit.api.LogEntry;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

import fr.dila.ecm.platform.routing.api.DocumentRouteTableElement;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.client.InjectionEpgGvtDTO;

/**
 * Interface pour le service permettant de générer des documents PDF à partir de Document Model.
 * 
 * @author arolin
 */
public interface PdfGeneratorService {

	/**
	 * Ajoute le pdf du bordereau dans le Document.
	 * 
	 * @param document
	 * @param dossier
	 */
	void generateBordereau(CoreSession session, Document document, Dossier dossier, Boolean archivageDefinitif)
			throws DocumentException;

	/**
	 * Ajoute le pdf du journal dans le Document.
	 * 
	 * @param document
	 * @param logEntry
	 * @param dossier
	 * @param messages
	 * @throws DocumentException
	 */
	void generateJournal(Document document, List<LogEntry> logEntry, Dossier dossier, Map<String, String> messages)
			throws DocumentException;

	/**
	 * Ajoute le pdf de la feuille de route dans le Document.
	 * 
	 * @param document
	 * @param logSelectDataModelRow
	 * @param session
	 * @param dossier
	 * @param messages
	 * @throws DocumentException
	 * @throws ClientException
	 */
	void generateFeuilleDeRoute(Document document, List<DocumentRouteTableElement> logDocumentRouteTableElement,
			CoreSession session, Dossier dossier, Map<String, String> messages, Boolean archivageDefinitif) throws DocumentException, ClientException;

	/**
	 * Ajoute un document Pdf dans un ZipOutputStream.
	 * 
	 * @param zip
	 * @param pdfGeneratorService
	 * @param dossier
	 * @param fileName
	 * @param logEntries
	 * @throws Exception
	 */
	ByteArrayOutputStream addPdfToZip(ZipOutputStream zip, Dossier dossier, String fileName, List<LogEntry> logEntries,
			List<DocumentRouteTableElement> logDocumentRouteTableElement, CoreSession session,
			Map<String, String> messages, Boolean archiveDefinitif, String idArchivageDefinitif) throws Exception;

	/**
	 * Ajoute le bordereau pdf dans le ZipOutputStream.
	 * 
	 * @param zip
	 * @param dossier
	 * @throws Exception
	 */
	ByteArrayOutputStream addBordereauToZip(CoreSession session, ZipOutputStream zip, Dossier dossier,
			Boolean archivageDefinitif, String IDArchivageDefinitif) throws Exception;

	/**
	 * Ajoute le journal pdf dans le ZipOutputStream.
	 * 
	 * @param zip
	 * @param dossier
	 * @param logEntries
	 * @param messages
	 * @throws Exception
	 */
	ByteArrayOutputStream addJournalToZip(CoreSession session, ZipOutputStream zip, Dossier dossier,
			List<LogEntry> logEntries, Map<String, String> messages, Boolean archivageDefinitif,
			String IDArchivageDefinitif) throws Exception;

	/**
	 * Ajoute la feuille de route pdf dans le ZipOutputStream.
	 * 
	 * @param zip
	 * @param dossier
	 * @param logDocRouteTableElement
	 * @param session
	 * @param messages
	 * @throws Exception
	 */
	ByteArrayOutputStream addFeuilleRouteToZip(ZipOutputStream zip, Dossier dossier,
			List<DocumentRouteTableElement> logDocumentRouteTableElement, CoreSession session,
			Map<String, String> messages, Boolean archivageDefinitif, String IDArchivageDefinitif) throws Exception;

	/**
	 * Génére le pdf reprenant les informations de l'injection
	 * 
	 * @param outputStream
	 * @param listCompared
	 * @throws DocumentException
	 */
	void generateInjection(OutputStream outputStream, List<InjectionEpgGvtDTO> listCompared) throws DocumentException;

	/**
	 * Ajoute le traitement papier pdf au zip
	 * 
	 * @param session
	 * @param zip
	 * @param dossier
	 * @throws Exception
	 */
	ByteArrayOutputStream addTraitementPapierToZip(CoreSession session, ZipOutputStream zip, Dossier dossier,
			Boolean archivageDefinitif, String IDArchivageDefinitif) throws Exception;

	/**
	 * Génère le pdf reprenant les informations du traitement papier
	 * 
	 * @param session
	 * @param document
	 * @param dossier
	 * @throws DocumentException
	 */
	void generateTraitementPapier(CoreSession session, Document document, Dossier dossier) throws DocumentException;
}
