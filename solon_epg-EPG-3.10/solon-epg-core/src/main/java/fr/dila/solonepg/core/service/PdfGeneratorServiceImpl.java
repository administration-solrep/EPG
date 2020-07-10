package fr.dila.solonepg.core.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.common.Environment;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.platform.audit.api.LogEntry;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import fr.dila.ecm.platform.routing.api.DocumentRouteTableElement;
import fr.dila.ecm.platform.routing.api.RouteFolderElement;
import fr.dila.solon.schemas.traitement_papier.VocType;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.cases.typescomplexe.InfoHistoriqueAmpliation;
import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.client.InjectionEpgGvtDTO;
import fr.dila.solonepg.api.constant.ActeVisibilityConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.PdfGeneratorService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.ss.api.client.InjectionGvtDTO;
import fr.dila.ss.api.domain.feuilleroute.StepFolder;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.MailboxService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.SHA512Util;

/**
 * Implémentation du service de gnération du bordereau.
 * 
 * @author admin
 */
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

	private static final String		ROUTE_SERIAL_VERTICAL_PNG	= "route_serial_vertical.png";

	private static final String		ROUTE_PARALLEL_PNG			= "route_parallel.png";

	private static final STLogger	LOGGER						= STLogFactory.getLog(PdfGeneratorServiceImpl.class);

	private static final String		ICON_PATH					= initIconPath(false);

	private static final String		NUXEO_ICON_PATH				= initIconPath(true);

	/**
	 * Font
	 */
	private static final Font		FONT_TITRE_1				= new Font(Font.TIMES_ROMAN, 18, Font.BOLD);

	private static final Font		FONT_TITRE_2				= new Font(Font.TIMES_ROMAN, 14, Font.BOLD);

	private static final Font		FONT_LIBELLE				= new Font(Font.TIMES_ROMAN, 10, Font.BOLD);

	private static final Font		FONT_VALUE					= new Font(Font.TIMES_ROMAN, 10);

	private static final Font		RED_FONT_VALUE				= new Font(Font.TIMES_ROMAN, 10, Font.BOLD, Color.RED);

	/**
	 * Color
	 */

	private static final Color		COLOR_GREEN					= new Color(241, 255, 251);

	private static final Color		COLOR_ORANGE				= new Color(255, 252, 238);

	private static final Color		COLOR_BLUE					= new Color(236, 244, 255);

	/**
	 * Nombre de colonne affiché dans la feuille de route.
	 */
	private static final int		FDR_NB_COLUMN				= 9;

	private static final String		BORDEREAU_PDF				= "bordereau.pdf";

	private static final String		JOURNAL_PDF					= "journal.pdf";

	private static final String		FEUILLE_ROUTE_PDF			= "feuilleRoute.pdf";

	private static final String		TRAITEMENT_PAPIER_PDF		= "traitementPapier.pdf";

	/**
	 * Debut du Nom du repertoire contenant un dossier dans le zip.
	 */
	private static final String		DOSSIER_FOLDER_NAME_IN_ZIP	= "Dossier_";

	@Override
	public ByteArrayOutputStream addPdfToZip(ZipOutputStream zip, Dossier dossier, String fileName,
			List<LogEntry> logEntries, List<DocumentRouteTableElement> documentRouteTableElement, CoreSession session,
			Map<String, String> messages, Boolean archiveDefinitif, String idArchivageDefinitif) throws Exception {

		// on met les fichiers pdf dans un répertoire
		String pathToFile = "";
		if (!archiveDefinitif) {
			pathToFile = DOSSIER_FOLDER_NAME_IN_ZIP + dossier.getNumeroNor() + "/";
		}
		if (archiveDefinitif) {
			pathToFile = pathToFile + "Content" + "/" + idArchivageDefinitif + ".pdf";
		} else {
			pathToFile = pathToFile + fileName;
		}

		// ajout du fichier
		ZipEntry entry = new ZipEntry(pathToFile);
		zip.putNextEntry(entry);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// initialisation d'un document PDF que l'on lie au ZIP
		Document document = new Document(PageSize.A4.rotate());
		PdfWriter writer = PdfWriter.getInstance(document, baos);
		writer.setCloseStream(false);
		document.open();

		if (fileName.equals(BORDEREAU_PDF)) {
			generateBordereau(session, document, dossier, archiveDefinitif);
		} else if (fileName.equals(JOURNAL_PDF)) {
			generateJournal(document, logEntries, dossier, messages);
		} else if (fileName.equals(FEUILLE_ROUTE_PDF)) {
			generateFeuilleDeRoute(document, documentRouteTableElement, session, dossier, messages, archiveDefinitif);
		} else if (fileName.equals(TRAITEMENT_PAPIER_PDF)) {
			generateTraitementPapier(session, document, dossier);
		}

		// fermeture du pdf et l'Entry ZIP
		if (document.getPageNumber() == 0) {
			// Ajout d'un parapheur vide pour éviter les erreurs quand le document est vide
			Paragraph parapheVide = new Paragraph("   ");
			document.add(parapheVide);
		}
		document.close();

		String sha512HashPDF = SHA512Util.getSHA512Hash(baos.toByteArray());
		zip.write(baos.toByteArray());
		zip.closeEntry();

		return baos;
	}

	@Override
	public ByteArrayOutputStream addBordereauToZip(CoreSession session, ZipOutputStream zip, Dossier dossier,
			Boolean archiveDefinitif, String IDArchivageDefinitif) throws Exception {
		return addPdfToZip(zip, dossier, BORDEREAU_PDF, null, null, session, null, archiveDefinitif,
				IDArchivageDefinitif);
	}

	@Override
	public ByteArrayOutputStream addJournalToZip(CoreSession session, ZipOutputStream zip, Dossier dossier,
			List<LogEntry> logEntries, Map<String, String> messages, Boolean archiveDefinitif,
			String IDArchivageDefinitif) throws Exception {
		return addPdfToZip(zip, dossier, JOURNAL_PDF, logEntries, null, session, messages, archiveDefinitif,
				IDArchivageDefinitif);
	}

	@Override
	public ByteArrayOutputStream addFeuilleRouteToZip(ZipOutputStream zip, Dossier dossier,
			List<DocumentRouteTableElement> documentRouteTableElement, CoreSession session,
			Map<String, String> messages, Boolean archiveDefinitif, String IDArchivageDefinitif) throws Exception {
		return addPdfToZip(zip, dossier, FEUILLE_ROUTE_PDF, null, documentRouteTableElement, session, messages,
				archiveDefinitif, IDArchivageDefinitif);
	}

	@Override
	public ByteArrayOutputStream addTraitementPapierToZip(CoreSession session, ZipOutputStream zip, Dossier dossier,
			Boolean archiveDefinitif, String IDArchivageDefinitif) throws Exception {
		return addPdfToZip(zip, dossier, TRAITEMENT_PAPIER_PDF, null, null, session, null, archiveDefinitif,
				IDArchivageDefinitif);
	}

	@Override
	public void generateBordereau(CoreSession session, Document document, Dossier dossier, Boolean archivageDefinitif)
			throws DocumentException {
		if (dossier == null) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, "Dossier non trouvé !");
			return;
		}
		final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
		final STUserService sTUserService = STServiceLocator.getSTUserService();
		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		BulletinOfficielService bulletinService = SolonEpgServiceLocator.getBulletinOfficielService();
		DocumentModel dossierDoc = dossier.getDocument();

		RetourDila retourDila = null;

		document.newPage();

		String typeActeId = dossier.getTypeActe();
		Map<String, Boolean> mapVisibility = acteService.getActeVisibility(typeActeId);

		// Données principales
		Paragraph paragraph = new Paragraph("Données principales", FONT_TITRE_1);
		document.add(paragraph);

		ajoutBordereauPartieInformationActe(document, dossier, vocabularyService, mapVisibility, false,
				archivageDefinitif);

		ajoutBordereauPartieResponsable(document, dossier, STServiceLocator.getSTMinisteresService(),
				STServiceLocator.getSTUsAndDirectionService(), sTUserService, mapVisibility, false);

		PdfPTable table = null;

		if (mapVisibility.get(ActeVisibilityConstants.CE)) {
			// partie CE
			paragraph = new Paragraph("CE", FONT_TITRE_2);
			document.add(paragraph);

			table = getInitPdfTable();

			ConseilEtat conseilEtat = dossierDoc.getAdapter(ConseilEtat.class);

			// Priorité CE
			ajoutLigneVocabulaire(table, vocabularyService, "Priorité", conseilEtat.getPriorite(),
					VocabularyConstants.VOC_PRIORITE);

			// Section du CE
			ajoutLigneTexte(table, "Section du CE", conseilEtat.getSectionCe());

			// Rapporteur
			ajoutLigneTexte(table, "Rapporteur", conseilEtat.getRapporteurCe());

			// Date transm. section CE
			ajoutLigneDate(table, "Date transm. section CE", conseilEtat.getDateTransmissionSectionCe());

			// Date section CE
			ajoutLigneDate(table, "Date prévisionnelle section CE", conseilEtat.getDateSectionCe());

			// Date AG CE
			ajoutLigneDate(table, "Date prévisionnelle AG CE", conseilEtat.getDateAgCe());

			// Numéro ISA
			ajoutLigneTexte(table, "Numéro ISA", conseilEtat.getNumeroISA());
			document.add(table);
		}

		// Signature
		paragraph = new Paragraph("Signature", FONT_TITRE_2);
		document.add(paragraph);

		table = getInitPdfTable();

		// Chargé de mission
		ajoutLigneTexteList(table, "Chargé de mission", dossier.getNomCompletChargesMissions());

		// Conseiller PM
		ajoutLigneTexteList(table, "Conseiller PM", dossier.getNomCompletConseillersPms());

		if (mapVisibility.get(ActeVisibilityConstants.DATE_SIGNATURE)) {
			// Date signature
			ajoutLigneDate(table, "Date signature", dossier.getDateSignature());
		}
		document.add(table);

		if (mapVisibility.get(ActeVisibilityConstants.PUBLICATION)) {
			paragraph = new Paragraph("Publication", FONT_TITRE_2);
			document.add(paragraph);

			// Publication
			table = getInitPdfTable();
			// Date de publication souhaitée
			ajoutLigneDate(table, "Date de publication souhaitée", dossier.getDatePublication());
			document.add(table);
		}

		if (mapVisibility.get(ActeVisibilityConstants.PARUTION) || archivageDefinitif) {
			// Parution
			paragraph = new Paragraph("Parution", FONT_TITRE_2);
			document.add(paragraph);

			// ajout table
			table = getInitPdfTable();

			// Pour fourniture d'épreuve
			ajoutLigneDate(table, "Pour fourniture d'épreuve", dossier.getDatePourFournitureEpreuve());

			if (dossier.getVecteurPublication() != null && !dossier.getVecteurPublication().isEmpty()) {

				// Vecteur de publication
				ajoutLigneTexteList(table, "Vecteur de publication",
						bulletinService.convertVecteurIdListToLabels(session, dossier.getVecteurPublication()));
			} else {

				// Vecteur de publication
				ajoutLigneTexteList(table, "Vecteur de publication", null);
			}

			// Publication intégrale ou par extrait
			ajoutLigneVocabulaire(table, vocabularyService, "Publication intégrale ou par extrait",
					dossier.getPublicationIntegraleOuExtrait(), VocabularyConstants.TYPE_PUBLICATION);

			// Decret numéro
			ajoutLigneBooleen(table, "Décret numéroté", dossier.getDecretNumerote());

			// Publication conjointe
			ajoutLigneTexteList(table, "Publication conjointe", dossier.getPublicationsConjointes());

			// Mode parution
			if (retourDila == null) {
				retourDila = dossierDoc.getAdapter(RetourDila.class);
			}
			final String docId = retourDila.getModeParution();
			final IdRef docRef = new IdRef(docId);
			try {
				if (docId != null && session.exists(docRef)) {
					DocumentModel modeParutionDoc = session.getDocument(docRef);
					if (modeParutionDoc != null) {
						ModeParution modeParution = modeParutionDoc.getAdapter(ModeParution.class);
						ajoutLigneTexte(table, "Mode parution", modeParution.getMode());
					}
				}
			} catch (ClientException ce) {
				LOGGER.error(session, EpgLogEnumImpl.FAIL_GET_MODE_PARUTION_TEC, docId, ce);
			}

			// Délai publication
			ajoutLigneVocabulaire(table, vocabularyService, "Délai publication", dossier.getDelaiPublication(),
					VocabularyConstants.DELAI_PUBLICATION);

			if (VocabularyConstants.STATUT_PUBLIE.equals(dossier.getStatut())) {
				// Publication à date précisée
				ajoutLigneDate(table, "Publication à date précisée", dossier.getDatePreciseePublication());
			}

			document.add(table);
		}

		if (mapVisibility.get(ActeVisibilityConstants.JORF)) {
			// Parution JORF
			paragraph = new Paragraph("Parution JORF", FONT_TITRE_2);
			document.add(paragraph);

			// ajoute la table
			table = getInitPdfTable();

			if (retourDila == null) {
				retourDila = dossierDoc.getAdapter(RetourDila.class);
			}

			// Date JO
			ajoutLigneDate(table, "Date JO", retourDila.getDateParutionJorf());

			// N° du texte JO
			ajoutLigneTexte(table, "N° du texte JO", retourDila.getNumeroTexteParutionJorf());

			// Page JO
			if (retourDila.getPageParutionJorf() != null) {
				ajoutLigneTexte(table, "Page JO", retourDila.getPageParutionJorf().toString());
			}

			// Titre officiel
			ajoutLigneTexte(table, "Titre officiel", retourDila.getTitreOfficiel());

			document.add(table);
		}

		if (mapVisibility.get(ActeVisibilityConstants.PARUTION_BO)) {

			if (retourDila == null) {
				retourDila = dossierDoc.getAdapter(RetourDila.class);
			}

			// Parution BO
			List<ParutionBo> parutionBoList = retourDila.getParutionBo();
			if (parutionBoList != null && parutionBoList.size() > 0) {
				paragraph = new Paragraph("Parution BO", FONT_TITRE_2);
				document.add(paragraph);
				for (ParutionBo parutionBo : parutionBoList) {
					// ajoute la table
					table = getInitPdfTable();
					// Date JO
					ajoutLigneDate(table, "Date BO", parutionBo.getDateParutionBo(), PdfPCell.TOP);
					// N° du texte BO
					ajoutLigneTexte(table, "", parutionBo.getNumeroTexteParutionBo(), PdfPCell.NO_BORDER);
					// Page BO
					if (parutionBo.getPageParutionBo() != null) {
						ajoutLigneTexte(table, "", parutionBo.getPageParutionBo().toString(), PdfPCell.BOTTOM);
					}
					document.add(table);
				}
			}
		}

		if (mapVisibility.get(ActeVisibilityConstants.SGG_DILA)) {
			// SGG-DILA
			paragraph = new Paragraph("SGG-DILA", FONT_TITRE_2);
			document.add(paragraph);

			// ajoute la table
			table = getInitPdfTable();

			// Zone commentaire SGG-DILA
			// note : visible pour tous les utilisateurs
			ajoutLigneTexte(table, "Zone commentaire SGG-DILA", dossier.getZoneComSggDila());

			document.add(table);
		}

		if (mapVisibility.get(ActeVisibilityConstants.BASE_LEGALE)) {
			// Base légale
			paragraph = new Paragraph("Base légale", FONT_TITRE_2);
			document.add(paragraph);

			// ajoute la table
			table = getInitPdfTable();
			ajoutLigneTexte(table, "Base légale", dossier.getBaseLegaleActe());

			document.add(table);
		}

		if (mapVisibility.get(ActeVisibilityConstants.INDEXATION)) {
			document.newPage();

			// Données d'indexation
			paragraph = new Paragraph("Données d'indexation", new Font(Font.TIMES_ROMAN, 20, Font.BOLD));
			paragraph.setSpacingBefore(20);
			document.add(paragraph);

			// ajoute la table
			table = getInitPdfTable();

			// Rubriques
			ajoutLigneTexteList(table, "Rubriques", dossier.getIndexationRubrique());

			// Mots-clés
			ajoutLigneTexteList(table, "Mots-clés", dossier.getIndexationMotsCles());

			// Champs libres
			ajoutLigneTexteList(table, "Champs libres", dossier.getIndexationChampLibre());

			document.add(table);

		}

		if (mapVisibility.get(ActeVisibilityConstants.TRANSPOSITION_APPLICATION)) {
			document.newPage();

			boolean isVisibleApplicationLoi = mapVisibility.get(ActeVisibilityConstants.APPLICATION_LOI);
			boolean isVisibleTranspositionOrdonnance = mapVisibility
					.get(ActeVisibilityConstants.TRANSPOSITION_ORDONNANCE);
			boolean isVisibleHabilitation = mapVisibility.get(ActeVisibilityConstants.HABILITATION);
			boolean isVisibleTranspositionDirective = mapVisibility
					.get(ActeVisibilityConstants.TRANSPOSITION_DIRECTIVE);

			if (isVisibleApplicationLoi || isVisibleTranspositionOrdonnance || isVisibleHabilitation
					|| isVisibleTranspositionDirective) {
				// Transposition et application
				paragraph = new Paragraph("Transposition et application", new Font(Font.TIMES_ROMAN, 20, Font.BOLD));
				document.add(paragraph);

				if (isVisibleApplicationLoi) {
					// Application des lois
					paragraph = new Paragraph("Application des lois", FONT_TITRE_2);
					document.add(paragraph);

					List<ComplexeType> applicationLoi = dossier.getApplicationLoi();
					ajoutTranspositionEtHabilitation(document, applicationLoi, true);
				}

				if (isVisibleTranspositionOrdonnance) {
					// Application des ordonnances
					paragraph = new Paragraph("Application des ordonnances", FONT_TITRE_2);
					document.add(paragraph);

					List<ComplexeType> transpositionOrdonnance = dossier.getTranspositionOrdonnance();
					ajoutTranspositionEtHabilitation(document, transpositionOrdonnance, false);
				}

				if (isVisibleTranspositionDirective) {
					// Transposition des directives
					paragraph = new Paragraph("Transposition des directives", FONT_TITRE_2);
					document.add(paragraph);

					List<ComplexeType> transpositionDirective = dossier.getTranspositionDirective();
					ajoutTranspositionEtHabilitation(document, transpositionDirective, false);
				}

				if (isVisibleHabilitation) {
					// Habilitations
					paragraph = new Paragraph("Habilitations", FONT_TITRE_2);
					document.add(paragraph);

					// ajoute la table
					table = getInitPdfTable();

					// Référence de la loi
					ajoutLigneTexte(table, "Référence de la loi", dossier.getHabilitationRefLoi(), PdfPCell.TOP);

					// Numéro article
					ajoutLigneTexte(table, "Numéro article", dossier.getHabilitationNumeroArticles(),
							PdfPCell.NO_BORDER);

					// Commentaire
					ajoutLigneTexte(table, "Commentaire", dossier.getHabilitationCommentaire(), PdfPCell.BOTTOM);

					document.add(table);
				}

			}
		}
	}

	/**
	 * Fonction utilitaire pour convertir un serializable en String
	 * 
	 * @param serializable
	 *            le serializable à convertir
	 * @return la chaîne vide si le Serializable est null, la valeur de son toString sinon
	 */
	public static String convertToString(Serializable serializable) {
		if (serializable == null) {
			return StringUtils.EMPTY;
		} else {
			return serializable.toString();
		}
	}

	/**
	 * 
	 * 
	 * @param document
	 * @param applicationLoi
	 * @throws BadElementException
	 * @throws DocumentException
	 */
	private void ajoutTranspositionEtHabilitation(Document document, List<ComplexeType> applicationLoi,
			boolean isApplicationLoi) throws BadElementException, DocumentException {
		PdfPTable table;
		if (applicationLoi != null && applicationLoi.size() > 0) {
			for (ComplexeType complexeType : applicationLoi) {
				// ajoute la table
				table = getInitPdfTable();
				Map<String, Serializable> serializableMap = complexeType.getSerializableMap();

				// Référence
				ajoutLigneTexte(table, "Référence",
						convertToString(serializableMap
								.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY)), PdfPCell.TOP);

				// Titre
				ajoutLigneTexte(table, "Titre",
						convertToString(serializableMap
								.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY)),
						PdfPCell.NO_BORDER);

				// Numéro d'article
				ajoutLigneTexte(table, "Numéro d'article",
						convertToString(serializableMap
								.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_NUMERO_PROPERTY)),
						PdfPCell.NO_BORDER);

				if (isApplicationLoi) {
					// Référence de la mesure
					ajoutLigneTexte(table, "Référence de la mesure",
							convertToString(serializableMap
									.get(DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY)),
							PdfPCell.NO_BORDER);
				}

				// Commentaire
				ajoutLigneTexte(table, "Commentaire",
						convertToString(serializableMap
								.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY)),
						PdfPCell.BOTTOM);

				document.add(table);
			}
		}
	}

	/**
	 * @param document
	 * @param dossier
	 * @param organigrammeService
	 * @param mapVisibility
	 * @throws BadElementException
	 * @throws DocumentException
	 */
	private void ajoutBordereauPartieResponsable(Document document, Dossier dossier,
			final STMinisteresService ministeresService, final STUsAndDirectionService usService,
			final STUserService sTUserService, Map<String, Boolean> mapVisibility, final Boolean traitementPapier)
			throws BadElementException, DocumentException {
		// Responsable sur l'acte
		Paragraph paragraph = new Paragraph("Responsable de l'acte", FONT_TITRE_2);
		document.add(paragraph);

		PdfPTable table = getInitPdfTable();
		// Ministère resp.
		ajoutLigneOrganigramme(table, ministeresService, usService, "Ministère resp.", dossier.getMinistereResp(),
				STConstant.ORGANIGRAMME_ENTITE_DIR);

		// Ministère resp.
		ajoutLigneOrganigramme(table, ministeresService, usService, "Direction resp.", dossier.getDirectionResp(),
				STConstant.ORGANIGRAMME_UNITE_STRUCTURELLE_DIR);
		if (!traitementPapier) {
			// Ministère rattach.
			ajoutLigneOrganigramme(table, ministeresService, usService, "Ministère rattach.",
					dossier.getMinistereAttache(), STConstant.ORGANIGRAMME_ENTITE_DIR);

			// Ministère rattach.
			ajoutLigneOrganigramme(table, ministeresService, usService, "Direction rattach.",
					dossier.getDirectionAttache(), STConstant.ORGANIGRAMME_UNITE_STRUCTURELLE_DIR);
		}

		// Nom du responsable du dossier
		ajoutLigneTexte(table, "Nom du responsable du dossier", dossier.getNomRespDossier());

		// Prénom resp. dossier
		ajoutLigneTexte(table, "Prénom resp. dossier", dossier.getPrenomRespDossier());

		// Qualité resp. dossier
		ajoutLigneTexte(table, "Qualité resp. dossier", dossier.getQualiteRespDossier());

		// Tél. resp. dossier
		ajoutLigneTexte(table, "Tél. resp. dossier ", dossier.getTelephoneRespDossier());
		if (!traitementPapier) {
			// Mél resp. dossier
			ajoutLigneTexte(table, "Mél resp. dossier", dossier.getMailRespDossier());

			// Créé par
			ajoutLigneUser(table, sTUserService, "Créé par", dossier.getIdCreateurDossier());
		}

		document.add(table);
	}

	/**
	 * @param document
	 * @param dossier
	 * @param vocabularyService
	 * @param mapVisibility
	 * @throws BadElementException
	 * @throws DocumentException
	 */
	private void ajoutBordereauPartieInformationActe(Document document, Dossier dossier,
			final VocabularyService vocabularyService, Map<String, Boolean> mapVisibility, Boolean traitementPapier,
			Boolean archivageDefinitif) throws BadElementException, DocumentException {
		// Information sur l'acte
		Paragraph paragraph = new Paragraph("Information sur l'acte", FONT_TITRE_2);
		document.add(paragraph);

		PdfPTable table = getInitPdfTable();

		// statut
		if (!traitementPapier) {
			ajoutLigneVocabulaire(table, vocabularyService, "Statut", dossier.getStatut(),
					VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME);

			// type acte
			ajoutLigneVocabulaire(table, vocabularyService, "Type d'acte", dossier.getTypeActe(),
					VocabularyConstants.TYPE_ACTE_VOCABULARY);
		}
		// NOR
		ajoutLigneTexte(table, "NOR", dossier.getNumeroNor());

		// titre acte
		ajoutLigneTexte(table, "Titre de l'acte", dossier.getTitreActe());
		if (!traitementPapier && !archivageDefinitif) {
			if (mapVisibility.get(ActeVisibilityConstants.CATEGORY_ACTE)) {
				// categorie acte
				ajoutLigneVocabulaire(table, vocabularyService, "Catégorie acte", dossier.getCategorieActe(),
						VocabularyConstants.NATURE);
			}

			if (mapVisibility.get(ActeVisibilityConstants.PUBLICATION_RAPPORT_PRESENTATION)) {
				// publication rapport de présentation
				ajoutLigneBooleen(table, "Publication du rapport de présentation",
						dossier.getPublicationRapportPresentation());
			}
		} else if (archivageDefinitif) {
			// categorie acte
			ajoutLigneVocabulaire(table, vocabularyService, "Catégorie acte", dossier.getCategorieActe(),
					VocabularyConstants.NATURE);
			
			if (mapVisibility.get(ActeVisibilityConstants.PUBLICATION_RAPPORT_PRESENTATION)) {
				// publication rapport de présentation
				ajoutLigneBooleen(table, "Publication du rapport de présentation",
						dossier.getPublicationRapportPresentation());
			}
		}
		// Texte relevant de la rubrique entreprise
		if (dossier.getTexteEntreprise()) {
			ajoutLigneBooleen(table, "Texte relevant de la rubrique entreprise", dossier.getTexteEntreprise());
			// dates d'effet
			ajoutLigneDateList(table, "Date(s) d'effet", dossier.getDateEffet());
		} else {
			ajoutLigneBooleen(table, "Texte relevant de la rubrique entreprise", dossier.getTexteEntreprise());
			ajoutLigneTexte(table, "Date(s) d'effet", "");
		}
		document.add(table);
	}

	/**
	 * Initialise la table qui va contenir les couple libellé -valeur du bordereau
	 * 
	 * @return PdfPTable
	 * @throws BadElementException
	 * @throws DocumentException
	 */
	private PdfPTable getInitPdfTable() throws BadElementException, DocumentException {
		// init table
		PdfPTable table = new PdfPTable(2);
		int[] largeurColonnePourcentage = { 1, 3 };
		table.setWidths(largeurColonnePourcentage);
		table.setWidthPercentage(90);
		table.setSpacingBefore(10);
		table.setSpacingAfter(10);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		return table;
	}

	/**
	 * créé un document de type Image.
	 * 
	 * @param imagePath
	 * @throws BadElementException
	 */
	private Image createIconImage(String imagePath, boolean isNuxeoIcon, String alt, float heigth, float width)
			throws BadElementException {
		// TODO voir si optimisable
		String defaultPath = ICON_PATH;
		if (isNuxeoIcon) {
			defaultPath = NUXEO_ICON_PATH;
		}
		File dir = new File(defaultPath, imagePath);
		String path = dir.getAbsolutePath();
		try {
			Image image = Image.getInstance(path);
			if (alt != null) {
				image.setAlt(alt);
			}
			if (heigth > 0 && width > 0) {
				image.scaleAbsolute(heigth, width);
			}
			return image;
		} catch (MalformedURLException e) {
			LOGGER.info(null, STLogEnumImpl.FAIL_GET_ICON_TEC, imagePath, e);
		} catch (IOException e) {
			LOGGER.info(null, STLogEnumImpl.FAIL_GET_ICON_TEC, imagePath, e);
		}
		return null;
	}

	/**
	 * récupère le chemin du répertoire contenant les images utilisés pour les icônes de l'application.
	 * 
	 * @return le chemin du répertoire contenant les images utilisés pour les icônes de l'application.
	 */
	private static String initIconPath(Boolean isCheminNuxeo) {
		Environment env = Environment.getDefault();
		File dir = env.getHome();
		dir = new File(dir, "nuxeo.war");
		if (!isCheminNuxeo) {
			dir = new File(dir, "img");
		}
		dir = new File(dir, "icons");

		return dir.getAbsolutePath();
	}

	/**
	 * Ajoute une information sur un champ du bordereau dans le pdf.
	 * 
	 * @param table
	 * @param vocabularyService
	 * @param libelle
	 * @param value
	 * @param directory
	 * @throws BadElementException
	 */
	private void ajoutLigneVocabulaire(PdfPTable table, VocabularyService vocabularyService, String libelle,
			String value, String directory) throws BadElementException {
		if (!StringUtils.isEmpty(directory) && !StringUtils.isEmpty(value)) {
			value = vocabularyService.getEntryLabel(directory, value);
		}
		PdfPCell cell = ajoutCelluleLibelle(libelle);
		table.addCell(cell);
		cell = ajoutCelluleValue(value);
		table.addCell(cell);
	}

	/**
	 * Ajoute une information de type date dans le pdf du bordereau.
	 * 
	 * @param table
	 * @param string
	 * @param datePublication
	 */
	private void ajoutLigneDate(PdfPTable table, String string, Calendar datePublication) {
		String dateString = null;
		if (datePublication != null) {
			dateString = DateUtil.formatDDMMYYYYSlash(datePublication);
		}
		PdfPCell cell = ajoutCelluleLibelle(string);
		table.addCell(cell);
		cell = ajoutCelluleValue(dateString);
		table.addCell(cell);
	}

	/**
	 * Ajoute une information de type liste de date sur un champ du bordereau dans le pdf.
	 * 
	 * @param table
	 * @param libelle
	 * @param value
	 * @throws BadElementException
	 */
	private void ajoutLigneDateList(PdfPTable table, String libelle, List<Calendar> values) throws BadElementException {
		PdfPCell cell = ajoutCelluleLibelle(libelle);
		table.addCell(cell);

		if (values == null || values.size() < 1) {
			cell = ajoutCelluleValue(null);
		} else {
			String listeDateFormatee = "";
			for (Calendar date : values) {
				if (!listeDateFormatee.isEmpty()) {
					listeDateFormatee += ", ";
				}
				listeDateFormatee += DateUtil.formatDDMMYYYYSlash(date);
			}
			cell = ajoutCelluleValue(listeDateFormatee);
		}
		table.addCell(cell);
	}

	/**
	 * Ajoute une information de type date dans le pdf du bordereau.
	 * 
	 * @param table
	 * @param string
	 * @param datePublication
	 */
	private void ajoutLigneDate(PdfPTable table, String string, Calendar datePublication, int cellBorder) {
		String dateString = null;
		if (datePublication != null) {
			dateString = DateUtil.formatDDMMYYYYSlash(datePublication);
		}
		PdfPCell cell = ajoutCelluleLibelle(string, cellBorder);
		table.addCell(cell);
		cell = ajoutCelluleValueBordereau(dateString, cellBorder);
		table.addCell(cell);
	}

	/**
	 * Ajoute une information de type texte sur un champ du bordereau dans le pdf.
	 * 
	 * @param table
	 * @param libelle
	 * @param value
	 * @throws BadElementException
	 */
	private void ajoutLigneBooleen(PdfPTable table, String libelle, Boolean value) throws BadElementException {
		String boolValue = "oui";
		if (value == null || !value) {
			boolValue = "non";
		}
		PdfPCell cell = ajoutCelluleLibelle(libelle);
		table.addCell(cell);
		cell = ajoutCelluleValue(boolValue);
		table.addCell(cell);
	}

	/**
	 * Ajoute une information de type texte sur un champ du bordereau dans le pdf.
	 * 
	 * @param table
	 * @param libelle
	 * @param value
	 * @throws BadElementException
	 */
	private void ajoutLigneTexte(PdfPTable table, String libelle, String value) throws BadElementException {
		PdfPCell cell = ajoutCelluleLibelle(libelle);
		table.addCell(cell);
		cell = ajoutCelluleValue(value);
		table.addCell(cell);
	}

	/**
	 * Ajoute une information de type texte sur un champ du bordereau dans le pdf.
	 * 
	 * @param table
	 * @param libelle
	 * @param value
	 * @throws BadElementException
	 */
	private void ajoutLigneTexte(PdfPTable table, String libelle, String value, int cellBorder)
			throws BadElementException {
		PdfPCell cell1 = ajoutCelluleLibelle(libelle, cellBorder);
		table.addCell(cell1);
		PdfPCell cell2 = ajoutCelluleValueBordereau(value, cellBorder);
		table.addCell(cell2);
	}

	/**
	 * Ajoute une information de type liste de texte sur un champ du bordereau dans le pdf.
	 * 
	 * @param table
	 * @param libelle
	 * @param value
	 * @throws BadElementException
	 */
	private void ajoutLigneTexteList(PdfPTable table, String libelle, List<String> values) throws BadElementException {
		PdfPCell cell = ajoutCelluleLibelle(libelle);
		table.addCell(cell);

		if (values == null || values.size() < 1) {
			cell = ajoutCelluleValue(null);
		} else {
			cell = ajoutCelluleValue(StringUtils.join(values, ","));
		}
		table.addCell(cell);
	}

	/**
	 * Ajoute une information de type utilisateur sur un champ du bordereau dans le pdf.
	 * 
	 * @param table
	 * @param libelle
	 * @param value
	 * @throws BadElementException
	 */
	private void ajoutLigneUser(PdfPTable table, STUserService stUserservice, String libelle, String value)
			throws BadElementException {
		if (!StringUtils.isEmpty(value)) {
			value = stUserservice.getUserFullName(value);
		}
		PdfPCell cell = ajoutCelluleLibelle(libelle);
		table.addCell(cell);
		cell = ajoutCelluleValue(value);
		table.addCell(cell);
	}

	/**
	 * Ajoute une information de type organigramme sur un champ du bordereau dans le pdf.
	 * 
	 * @param table
	 * @param organigrammeService
	 * @param string
	 * @param ministereResp
	 */
	private void ajoutLigneOrganigramme(PdfPTable table, STMinisteresService ministeresService,
			STUsAndDirectionService usService, String libelle, String value, String nodeType) {

		if (!StringUtils.isEmpty(value) && !StringUtils.isEmpty(nodeType)) {
			try {
				OrganigrammeNode node = null;
				if (STConstant.ORGANIGRAMME_ENTITE_DIR.equals(nodeType)) {
					node = ministeresService.getEntiteNode(value);
				} else if (STConstant.ORGANIGRAMME_UNITE_STRUCTURELLE_DIR.equals(nodeType)) {
					node = usService.getUniteStructurelleNode(value);
				} else {
					LOGGER.error(null, STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC, nodeType + " - " + value);
					value = "**type du noeud (poste ou ministère) inconnu**";
				}
				if (node == null) {
					LOGGER.error(null, STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC, nodeType + " - " + value);
					value = "**poste ou ministère inconnu**";
				} else {
					value = node.getLabel();
				}
			} catch (ClientException e) {
				LOGGER.error(null, STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC, nodeType + " - " + value, e);
				value = null;
			}
		}
		PdfPCell cell = ajoutCelluleLibelle(libelle);
		table.addCell(cell);
		cell = ajoutCelluleValue(value);
		table.addCell(cell);
	}

	/**
	 * Créé et initialise une cellule pour un libelle.
	 * 
	 * @param libelle
	 * @return Cell
	 */
	private PdfPCell ajoutCelluleLibelle(String libelle) {
		PdfPCell cell = new PdfPCell();
		Paragraph parapgraph = new Paragraph(libelle, FONT_LIBELLE);
		cell.addElement(parapgraph);
		cell.setBorderWidth(0);
		return cell;
	}

	/**
	 * Créé et initialise une cellule pour une valeur.
	 * 
	 * @param libelle
	 * @return Cell
	 */
	private PdfPCell ajoutCelluleValue(String value) {
		PdfPCell cell = new PdfPCell();
		Paragraph parapgraph = new Paragraph(value, FONT_VALUE);
		cell.addElement(parapgraph);
		cell.setBorderWidth(0);
		return cell;
	}

	/**
	 * Créé et initialise une cellule pour un libelle.
	 * 
	 * @param libelle
	 * @return Cell
	 */
	private PdfPCell ajoutCelluleLibelle(String libelle, int cellBorder) {
		PdfPCell cell = new PdfPCell();
		Paragraph parapgraph = new Paragraph(libelle, FONT_LIBELLE);
		cell.addElement(parapgraph);
		cell.setBorder(PdfPCell.LEFT | cellBorder);
		return cell;
	}

	/**
	 * Créé et initialise une cellule pour une valeur du bordereau.
	 * 
	 * @param libelle
	 * @return Cell
	 */
	private PdfPCell ajoutCelluleValueBordereau(String value, int cellBorder) {
		PdfPCell cell = new PdfPCell();
		Paragraph parapgraph = new Paragraph(value, FONT_VALUE);
		cell.addElement(parapgraph);
		cell.setBorder(PdfPCell.RIGHT | cellBorder);
		return cell;
	}

	/**
	 * Créé et initialise une cellule pour un tableau (jouranl , feuille de route).
	 * 
	 * @param libelle
	 * @return Cell
	 */
	private PdfPCell ajoutCelluleValue(String value, int borderWidth) {
		PdfPCell cell = new PdfPCell();
		Paragraph parapgraph = new Paragraph(value, FONT_VALUE);
		cell.addElement(parapgraph);
		cell.setBorder(borderWidth);
		return cell;
	}

	@Override
	public void generateJournal(Document document, List<LogEntry> logEntryList, Dossier dossier,
			Map<String, String> messages) throws DocumentException {
		// on redefinit la taille de la page
		document.setPageSize(PageSize.A4.rotate());
		document.newPage();

		// il y a 5 colonnes dans le journal
		PdfPTable table = new PdfPTable(5);
		int[] largeurColonnePourcentage = { 2, 2, 5, 2, 5 };
		table.setWidths(largeurColonnePourcentage);
		table.setWidthPercentage(90);
		table.setSpacingBefore(10);
		table.setSpacingAfter(10);
		table.getDefaultCell().setBorderWidth(Rectangle.NO_BORDER);
		table.setHeaderRows(1);

		// header de la table journal
		ajoutHeaderCell(table, "Date");
		ajoutHeaderCell(table, "Utilisateur");
		ajoutHeaderCell(table, "Poste");
		ajoutHeaderCell(table, "Catégorie");
		ajoutHeaderCell(table, "Commentaire");

		// contenu du journal
		if (logEntryList != null && logEntryList.size() > 0) {
			for (LogEntry logEntry : logEntryList) {
				ajoutDateCell(table, logEntry.getEventDate(), PdfPCell.BOTTOM);
				ajoutTexteCell(table, logEntry.getPrincipalName(), PdfPCell.BOTTOM);
				ajoutTexteCell(table, logEntry.getDocPath(), PdfPCell.BOTTOM);
				ajoutTexteCell(table, messages.get(logEntry.getCategory()), PdfPCell.BOTTOM);
				String comment = messages.get(logEntry.getComment());
				if (StringUtils.isEmpty(comment)) {
					comment = logEntry.getComment();
				}
				ajoutTexteCell(table, comment, PdfPCell.BOTTOM);
			}
		}
		// ajout de la table
		document.add(table);
	}

	/**
	 * ajout une cellule de type header dans un tableau
	 * 
	 * @param intitule
	 * @return
	 */
	protected void ajoutHeaderCell(PdfPTable table, String intitule) {
		PdfPCell cell = new PdfPCell();
		cell.setGrayFill(0.6f);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setPhrase(new Phrase(intitule));
		cell.setBorder(PdfPCell.BOTTOM);
		table.addCell(cell);
	}

	/**
	 * ajout une cellule de type date dans un tableau
	 * 
	 * @param intitule
	 * @return
	 */
	protected void ajoutDateCell(PdfPTable table, Date date, int cellBorder) {
		String dateString = null;
		if (date != null) {
			dateString = DateUtil.formatWithHour(date);
		}
		PdfPCell cell = ajoutCelluleValue(dateString, cellBorder);
		table.addCell(cell);
	}

	/**
	 * ajout une cellule de type texte dans un tableau
	 * 
	 * @param intitule
	 * @return
	 */
	protected void ajoutTexteCell(PdfPTable table, String value, int cellBorder) {
		PdfPCell cell = ajoutCelluleValue(value, cellBorder);
		table.addCell(cell);
	}

	@Override
	public void generateFeuilleDeRoute(Document document, List<DocumentRouteTableElement> docRouteTableElementList,
			CoreSession session, Dossier dossier, Map<String, String> messages, Boolean archivageDefinitif) throws DocumentException, ClientException {

		// on redefinit la taille de la page
		document.setPageSize(PageSize.A4.rotate());
		document.newPage();

		int maxDepth = 0;
		if (docRouteTableElementList != null && docRouteTableElementList.size() > 0) {
			// colspan pour l'affichage des étapes parallèles et en série de la feuille de route
			maxDepth = docRouteTableElementList.get(0).getRouteMaxDepth();
		}

		// nb de colonnes de la feuille de route + profondeur maximales des étapes de feuille de route
		int nbColumn = FDR_NB_COLUMN + maxDepth;
		if (archivageDefinitif) {
			nbColumn = FDR_NB_COLUMN + 1 + maxDepth;
		}
		PdfPTable table = new PdfPTable(nbColumn);
		table.setWidths(getTableWidthsInArrayInt(nbColumn, maxDepth));
		table.setWidthPercentage(90);
		table.setSpacingBefore(10);
		table.setSpacingAfter(10);
		table.getDefaultCell().setBorderWidth(Rectangle.NO_BORDER);
		table.setHeaderRows(1);

		PdfPCell cell = new PdfPCell();
		cell.setGrayFill(0.6f);

		if (maxDepth > 0) {
			cell.setColspan(maxDepth);
			cell.setBorder(PdfPCell.BOTTOM);
			table.addCell(cell);
		}

		// note : on affiche toutes les colonnes de la feuille de route : on ne tient pas compte des colonnes définies
		// dans le profil utilisateur
		// colonne Type d'étape
		ajoutHeaderCell(table, messages.get("st.feuilleRoute.etape.list.type"));
		// colonne Ministère
		ajoutHeaderCell(table, messages.get("st.feuilleRoute.etape.list.ministere"));
		// colonne Poste
		ajoutHeaderCell(table, messages.get("st.feuilleRoute.etape.list.poste"));
		// colonne N°version
		ajoutHeaderCell(table, messages.get("label.casemanagement.routing.task.numeroVersion"));
		// colonne Etat
		ajoutHeaderCell(table,
				messages.get("label.widget.feuille_route_instance_listing.routing_task_list_element_state"));
		// /Ajout d'une colonne vide pour mettre le libellé qui va avec l'état
		if (archivageDefinitif) {
			ajoutHeaderCell(table, " ");
		}
		// on n'affiche pas la colonne note
		// colonne Obligatoire
		ajoutHeaderCell(table, messages.get("st.feuilleRoute.etape.list.obligatoire"));
		// colonne Auteur
		ajoutHeaderCell(table, messages.get("label.casemanagement.routing.task.auteur"));
		// colonne Echéance
		ajoutHeaderCell(table, messages.get("label.casemanagement.routing.task.dueDate"));
		// colonne Date de traitement
		ajoutHeaderCell(table, messages.get("label.casemanagement.routing.task.dateFinEtape"));

		final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
		final MailboxService mailboxService = STServiceLocator.getMailboxService();
		final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
		// contenu du journal
		if (docRouteTableElementList != null && docRouteTableElementList.size() > 0) {
			for (DocumentRouteTableElement docRouteTableElement : docRouteTableElementList) {

				int depth = docRouteTableElement.getDepth();
				List<RouteFolderElement> firstChildFolders = docRouteTableElement.getFirstChildFolders();

				// 1 gestion de l'affichage des étapes parallèles et en série de la feuille de route

				if (depth == 0) {
					if (maxDepth != 0) {
						// fusion des cellules vide
						ajoutTexteCell(table, null, PdfPCell.BOTTOM, Color.WHITE, maxDepth);
					}
				} else if (firstChildFolders == null || firstChildFolders.size() == 0) {
					for (int i = 0; i < depth; i++) {
						// détermine la couleur
						Color color = getColor(i + 1);
						// ajout de la cellule représentant la position de l'élément
						ajoutTexteCell(table, null, PdfPCell.BOTTOM, color);
						// si on affiche la dernière cellule on s'assure d'avoir le bon nombre en rajoutant une cellule
						// ayant le bon colspa
						if (i == depth - 1 && i < maxDepth && depth != maxDepth) {
							// ajout de la cellule avec le bon colspan
							ajoutTexteCell(table, null, PdfPCell.BOTTOM, color, maxDepth - i - 1);
						}
					}
				} else if (firstChildFolders.get(0).getDepth() > 0) {
					// gestion du cas ou le premier firstchildfolder ne commence pas à depth = 0
					// gestion de l'affichage de la profondeur du noeud
					for (int i = 0; i < firstChildFolders.get(0).getDepth(); i++) {
						ajoutTexteCell(table, null, PdfPCell.BOTTOM, getColor(i + 1));
					}
				}

				// parcourt des éléments fils
				if (firstChildFolders != null) {

					for (Iterator<RouteFolderElement> iterator = firstChildFolders.iterator(); iterator.hasNext();) {
						RouteFolderElement routeFolderElement = iterator.next();
						// note : on incremente la profondeur de l'élément fils de 1
						int routeFolderElementDepth = routeFolderElement.getDepth() + 1;
						StepFolder stepFolder = routeFolderElement.getRouteElement().getDocument()
								.getAdapter(StepFolder.class);
						// ajout de la cellule contenant l'image à afficher
						PdfPCell pdfCell = new PdfPCell();
						pdfCell.setBackgroundColor(getColor(routeFolderElementDepth));
						pdfCell.addElement(new Paragraph());
						if (stepFolder.isParallel()) {
							pdfCell.addElement(createIconImage(ROUTE_PARALLEL_PNG, true, null, 16, 16));
						} else {
							pdfCell.addElement(createIconImage(ROUTE_SERIAL_VERTICAL_PNG, true, null, 16, 16));
						}
						table.addCell(pdfCell);
						if (!iterator.hasNext() && routeFolderElementDepth < maxDepth) {
							// ajout de la cellule avec le bon colspan
							ajoutTexteCell(table, null, PdfPCell.BOTTOM, getColor(routeFolderElementDepth), maxDepth
									- routeFolderElementDepth);
						}
					}

				}

				// 2 affichage du contenu de la ligne
				Color color = getColor(depth);
				DocumentModel docModel = docRouteTableElement.getDocument();
				if (STConstant.STEP_FOLDER_DOCUMENT_TYPE.equals(docModel.getType())) {
					ajoutTexteCell(table, " ", PdfPCell.BOTTOM, color, FDR_NB_COLUMN);
				} else {
					SolonEpgRouteStep routeStep = docModel.getAdapter(SolonEpgRouteStep.class);

					// ajout colonne action
					String typeAction = routeStep.getType();
					if (!StringUtils.isEmpty(typeAction)) {
						typeAction = vocabularyService.getEntryLabel(
								STVocabularyConstants.ROUTING_TASK_TYPE_VOCABULARY, typeAction);
						typeAction = messages.get(typeAction);
					}
					ajoutTexteCell(table, typeAction, PdfPCell.BOTTOM, color);

					// ajout colonne ministère
					String distributionMailBoxId = routeStep.getDistributionMailboxId();
					String minLabel = routeStep.getMinistereLabel();
					if (!StringUtils.isEmpty(distributionMailBoxId) && StringUtils.isEmpty(minLabel)) {
						minLabel = mailboxPosteService.getMinisteresEditionFromMailboxId(routeStep
								.getDistributionMailboxId());
					}
					ajoutTexteCell(table, minLabel, PdfPCell.BOTTOM, color);

					// ajout colonne poste
					String posteLabel = routeStep.getPosteLabel();
					if (!StringUtils.isEmpty(distributionMailBoxId) && StringUtils.isEmpty(posteLabel)) {
						try {
							posteLabel = mailboxService.getMailboxTitle(session, distributionMailBoxId);
						} catch (ClientException e) {
							LOGGER.error(session, STLogEnumImpl.FAIL_GET_POSTE_TEC, distributionMailBoxId, e);
						}
					}
					ajoutTexteCell(table, posteLabel, PdfPCell.BOTTOM, color);

					// ajout colonne numéro version
					if (routeStep.getNumeroVersion() == null || routeStep.getNumeroVersion() == 0) {
						ajoutTexteCell(table, null, PdfPCell.BOTTOM, color);
					} else {
						ajoutTexteCell(table, routeStep.getNumeroVersion().toString(), PdfPCell.BOTTOM, color);
					}

					// ajout colonne etat
					if (routeStep.isDraft()) {
						ajoutImageCell(table, "bullet_ball_glass_grey_16.png", false, null, 16, 16, PdfPCell.BOTTOM,
								color);
					} else if (routeStep.isValidated()) {
						ajoutImageCell(table, "bullet_ball_glass_green_16.png", false, null, 16, 16, PdfPCell.BOTTOM,
								color);
					} else if (routeStep.isReady()) {
						ajoutImageCell(table, "bullet_ball_glass_grey_16.png", false, null, 16, 16, PdfPCell.BOTTOM,
								color);
					} else if (routeStep.isRunning()) {
						ajoutImageCell(table, "bullet_ball_glass_yellow_16.png", false, null, 16, 16, PdfPCell.BOTTOM,
								color);
					} else if (routeStep.isDone()) {
						String validationStatutEtape = routeStep.getValidationStatus();
						// note : possibilité d'ajouter le type de validation en plus de l'icône.
						if (STSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE
								.equals(validationStatutEtape)) {
							ajoutImageCell(table, "check_16.png", false, null, 16, 16, PdfPCell.BOTTOM, color);
						} else if (STSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_DEFAVORABLE_VALUE
								.equals(validationStatutEtape)) {
							ajoutImageCell(table, "check_red_16.png", false, null, 16, 16, PdfPCell.BOTTOM, color);
						} else if (STSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_VALIDE_AUTOMATIQUEMENT_VALUE
								.equals(validationStatutEtape)) {
							ajoutImageCell(table, "bullet_ball_glass_green_16.png", false, null, 16, 16,
									PdfPCell.BOTTOM, color);
						} else if (STSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_NON_CONCERNE_VALUE
								.equals(validationStatutEtape)) {
							ajoutImageCell(table, "non_concerne.png", false, null, 16, 16, PdfPCell.BOTTOM, color);
						} else if (SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_CORRECTION_VALUE
								.equals(validationStatutEtape)) {
							ajoutImageCell(table, "check_yellow_16.png", false, null, 16, 16, PdfPCell.BOTTOM, color);
						} else if (SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETOUR_MODIFICATION_VALUE
								.equals(validationStatutEtape)) {
							ajoutImageCell(table, "back_update_16.png", false, null, 16, 16, PdfPCell.BOTTOM, color);
						} else if (SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_POUR_AVIS_RENDU_VALUE
								.equals(validationStatutEtape)) {
							ajoutImageCell(table, "check_16.png", false, null, 16, 16, PdfPCell.BOTTOM, color);
						} else if (SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETRAIT_VALUE
								.equals(validationStatutEtape)) {
							ajoutImageCell(table, "back_update_16.png", false, null, 16, 16, PdfPCell.BOTTOM, color);
						} else if (SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_REFUS_VALUE
								.equals(validationStatutEtape)) {
							ajoutImageCell(table, "back_update_16.png", false, null, 16, 16, PdfPCell.BOTTOM, color);
						} else if (SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RENVOI_VALUE
								.equals(validationStatutEtape)) {
							ajoutImageCell(table, "back_update_16.png", false, null, 16, 16, PdfPCell.BOTTOM, color);
						} else if (SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_DESSAISISSEMENT_VALUE
								.equals(validationStatutEtape)) {
							ajoutImageCell(table, "back_update_16.png", false, null, 16, 16, PdfPCell.BOTTOM, color);
						}
					}

					// Ajout label statut dossier
					if (archivageDefinitif) {
						String validationStatusId = routeStep.getValidationStatus();
						String validationStatusLabel = "";
						if (messages != null) {
							ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
							validationStatusLabel = archiveService.idIconeToLabelFDR(routeStep.getType(),
									validationStatusId, messages);
						}
						ajoutTexteCell(table, validationStatusLabel, PdfPCell.BOTTOM, color);
					}

					// ajout colonne obligatoire
					if (routeStep.isObligatoireMinistere() || routeStep.isObligatoireSGG()) {
						ajoutImageCell(table, "check_16.png", false, null, 16, 16, PdfPCell.BOTTOM, color);
					} else {
						ajoutTexteCell(table, null, PdfPCell.BOTTOM, color);
					}

					// ajout colonne auteur
					ajoutTexteCell(table, routeStep.getValidationUserLabel(), PdfPCell.BOTTOM, color);

					// ajout colonne échéance
					ajoutCalendarCell(table, routeStep.getDueDate(), PdfPCell.BOTTOM, color);

					// ajout colonne date de traitement
					ajoutCalendarCell(table, routeStep.getDateFinEtape(), PdfPCell.BOTTOM, color);
				}
			}
		}

		// ajout de la table
		document.add(table);
	}

	/**
	 * Définit la largeur de chacune des colonnes de la table sous forme de String
	 * 
	 * @param nbTotalColumn
	 * @param nbAditionnalColumn
	 * @return la largeur de chacune des colonnes de la table sous forme de String
	 */
	public float[] getTableWidthsInArrayInt(int nbTotalColumn, int nbAditionnalColumn) {
		if (nbTotalColumn < 1) {
			return null;
		}

		float[] tableColumnsWidth = new float[nbTotalColumn];
		int iterateur = 0;
		// largeur des colonnes pour l'affichage des branhces
		if (nbAditionnalColumn > 0) {
			for (int i = 0; i < nbAditionnalColumn; i++) {
				tableColumnsWidth[iterateur] = 0.5f;
				iterateur = iterateur + 1;
			}
		}
		// largeur des colonnes affichant des données
		if (nbTotalColumn - nbAditionnalColumn > 0) {
			for (int i = 0; i < nbTotalColumn - nbAditionnalColumn; i++) {
				tableColumnsWidth[iterateur] = 1f;
				iterateur = iterateur + 1;
			}
		}

		return tableColumnsWidth;
	}

	protected Color getColor(int depth) {
		Color color = Color.WHITE;
		// détermine la couleur
		int reste = (1 + depth) % 4;
		if (reste == 1) {
			color = Color.WHITE;
		} else if (reste == 2) {
			color = COLOR_GREEN;
		} else if (reste == 3) {
			color = COLOR_ORANGE;
		} else if (reste == 0) {
			color = COLOR_BLUE;
		}
		return color;
	}

	/**
	 * ajout une cellule de type texte dans un tableau
	 * 
	 * @param intitule
	 * @return
	 */
	protected void ajoutTexteCell(PdfPTable table, String value, int cellBorder, Color color, int colspan) {
		PdfPCell cell = ajoutCelluleValue(value, cellBorder);
		cell.setBackgroundColor(color);
		if (colspan > 0) {
			cell.setColspan(colspan);
		}
		table.addCell(cell);
	}

	/**
	 * ajout une cellule de type texte dans un tableau
	 * 
	 * @param intitule
	 * @return
	 */
	protected void ajoutTexteCell(PdfPTable table, String value, int cellBorder, Color color) {
		ajoutTexteCell(table, value, cellBorder, color, -1);
	}

	/**
	 * ajout une cellule de type texte dans un tableau
	 * 
	 * @param intitule
	 * @return
	 */
	protected void ajoutCalendarCell(PdfPTable table, Calendar calendar, int cellBorder, Color color) {
		String date = null;
		if (calendar != null) {
			date = DateUtil.formatForClient(calendar.getTime());
		}
		ajoutTexteCell(table, date, cellBorder, color, -1);
	}

	/**
	 * ajout une cellule de type image dans un tableau
	 * 
	 * @param intitule
	 * @return
	 * @throws BadElementException
	 */
	protected void ajoutImageCell(PdfPTable table, String imageName, boolean isNuxeoImage, int cellBorder, Color color)
			throws BadElementException {
		ajoutImageCell(table, imageName, isNuxeoImage, imageName, 0, 0, cellBorder, color);
	}

	/**
	 * ajout une cellule de type image dans un tableau
	 * 
	 * @param intitule
	 * @return
	 * @throws BadElementException
	 */
	protected void ajoutImageCell(PdfPTable table, String imageName, boolean isNuxeoImage, String alt, int width,
			int heigth, int cellBorder, Color color) throws BadElementException {
		PdfPCell pdfCell = new PdfPCell();
		pdfCell.setBackgroundColor(color);
		pdfCell.addElement(new Paragraph());
		pdfCell.addElement(createIconImage(imageName, isNuxeoImage, alt, heigth, width));
		pdfCell.setBorder(cellBorder);
		table.addCell(pdfCell);
	}

	@Override
	public void generateInjection(OutputStream outputStream, List<InjectionEpgGvtDTO> listCompared)
			throws DocumentException {
		// TODO Auto-generated method stub
		Document document = new Document(PageSize.A4.rotate());
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		writer.setCloseStream(false);
		document.open();
		document.newPage();
		document.addTitle("Injection de nouveau gouvernement");
		document.add(new Paragraph("Injection de nouveau gouvernement", FONT_TITRE_1));
		PdfPTable table = new PdfPTable(3);
		int[] largeurColonnePourcentage = { 3, 1, 3 };
		table.setWidths(largeurColonnePourcentage);
		table.setWidthPercentage(90);
		table.setSpacingBefore(10);
		table.setSpacingAfter(10);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.setHeaderRows(1);

		// header du récapitulatif de l'injection
		ajoutHeaderCell(table, "Organigramme actuel");
		ajoutHeaderCell(table, "Type de modification");
		ajoutHeaderCell(table, "Organigramme modifié");
		int addedLines = 1;
		for (InjectionEpgGvtDTO line : listCompared) {
			addMultiLineTextCell(table, getValueFromCompared(line.getBaseGvt()), PdfPCell.BOTTOM, (addedLines % 2) == 0);
			addRedColoredLine(table, line.getModification(), PdfPCell.BOTTOM, (addedLines % 2) == 0);
			addMultiLineTextCell(table, getValueFromCompared(line.getImportedGvt()), PdfPCell.BOTTOM,
					(addedLines % 2) == 0);
			addedLines++;
		}
		document.add(table);
		document.close();
	}

	private void addRedColoredLine(PdfPTable table, String modification, int cellBorder, boolean colored) {
		PdfPCell cell = new PdfPCell();
		Paragraph parapgraph = new Paragraph(modification, RED_FONT_VALUE);
		cell.addElement(parapgraph);
		cell.setBorder(cellBorder);
		if (colored) {
			cell.setBackgroundColor(COLOR_BLUE);
		}
		table.addCell(cell);
	}

	private void addMultiLineTextCell(PdfPTable table, final List<String> lines, int cellBorder, boolean colored) {
		PdfPCell cell = new PdfPCell();
		for (String line : lines) {
			Paragraph parapgraph = new Paragraph(line, FONT_VALUE);
			cell.addElement(parapgraph);
		}
		cell.setBorder(cellBorder);
		if (colored) {
			cell.setBackgroundColor(COLOR_BLUE);
		}
		table.addCell(cell);
	}

	private List<String> getValueFromCompared(InjectionGvtDTO injection) {
		List<String> result = new ArrayList<String>();
		if (injection == null) {
			result.add("");
		} else {
			StringBuilder sb = new StringBuilder();
			if (injection.getNor() == null || injection.getNor().isEmpty()) {
				result.add("");
			} else {
				sb.append(injection.getNor()).append(" (").append(injection.getOrdreProtocolaireSolon()).append(")");
				result.add(sb.toString());
			}

			// reuse it
			sb = reuseForBetterPerf(sb);
			if (injection.getLibelleCourt() != null && !injection.getLibelleCourt().isEmpty()) {
				sb.append(injection.getLibelleCourt()).append(" - ");
			}
			sb.append(injection.getLibelleLong());
			result.add(sb.toString());

			result.add(injection.getFormule());
			result.add(injection.getPrenomNom());

			// reuse it again
			sb = reuseForBetterPerf(sb);
			Calendar cal = Calendar.getInstance();
			if (injection.getDateDeDebut() != null) {
				cal.setTime(injection.getDateDeDebut());
				sb.append(DateUtil.formatDDMMYYYYSlash(cal)).append(" - ");
				cal = Calendar.getInstance();
			}
			if (injection.getDateDeFin() != null) {
				cal.setTime(injection.getDateDeFin());
				sb.append(DateUtil.formatDDMMYYYYSlash(cal));
			}
			sb.append("");
			result.add(sb.toString());
		}
		return result;
	}

	private static StringBuilder reuseForBetterPerf(final StringBuilder sb) {
		sb.delete(0, sb.length());
		return sb;
	}

	@Override
	public void generateTraitementPapier(CoreSession session, Document document, Dossier dossier)
			throws DocumentException {
		if (dossier == null) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, "Dossier non trouvé !");
			return;
		}
		VocabularyService vocService = STServiceLocator.getVocabularyService();
		PdfPTable table = null;
		final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
		final STUserService sTUserService = STServiceLocator.getSTUserService();
		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		final BulletinOfficielService bulletinService = SolonEpgServiceLocator.getBulletinOfficielService();
		DocumentModel dossierDoc = dossier.getDocument();
		TraitementPapier traitementPapierDoc = dossierDoc.getAdapter(TraitementPapier.class);
		String typeActeId = dossier.getTypeActe();
		Map<String, Boolean> mapVisibility = acteService.getActeVisibility(typeActeId);
		document.newPage();
		// ****Références
		Paragraph paragraph = new Paragraph("Références", FONT_TITRE_1);
		document.add(paragraph);
		// --Données du bordereau
		paragraph = new Paragraph("Données du bordereau", FONT_TITRE_1);
		document.add(paragraph);
		// Informations sur l'actes
		ajoutBordereauPartieInformationActe(document, dossier, vocabularyService, mapVisibility, true, false);
		// Responsable de l'acte
		ajoutBordereauPartieResponsable(document, dossier, STServiceLocator.getSTMinisteresService(),
				STServiceLocator.getSTUsAndDirectionService(), sTUserService, mapVisibility, true);

		// Publication
		if (mapVisibility.get(ActeVisibilityConstants.PARUTION)) {
			paragraph = new Paragraph("Publication", FONT_TITRE_2);
			document.add(paragraph);
			// Publication
			table = getInitPdfTable();
			// Publication conjointe
			ajoutLigneTexteList(table, "Publication conjointe", dossier.getPublicationsConjointes());
			document.add(table);
		}

		// --Complément
		paragraph = new Paragraph("Complément", FONT_TITRE_1);
		document.add(paragraph);
		// Références
		paragraph = new Paragraph("Références", FONT_TITRE_2);
		document.add(paragraph);
		// Date d'arrivée traitement papier
		table = getInitPdfTable();
		ajoutLigneDate(table, "Date d'arrivée papier", traitementPapierDoc.getDateArrivePapier());
		// Commentaire
		ajoutLigneTexte(table, "Commentaire", traitementPapierDoc.getCommentaireReference());
		// Texte à publier
		ajoutLigneBooleen(table, "Texte à publier", traitementPapierDoc.getTexteAPublier());
		// Texte soumis à validation
		ajoutLigneBooleen(table, "Texte soumis à validation", traitementPapierDoc.getTexteSoumisAValidation());
		// Signataire
		String signataire = "";
		if (traitementPapierDoc.getSignataire() != null && !traitementPapierDoc.getSignataire().isEmpty()) {
			signataire = vocService.getEntryLabel(VocabularyConstants.TYPE_SIGNATAIRE_TP_DIRECTORY_NAME,
					traitementPapierDoc.getSignataire());
		}
		ajoutLigneTexte(table, "Signataire", signataire);
		document.add(table);

		document.newPage();
		table = getInitPdfTable();
		// ****Communication
		paragraph = new Paragraph("Communication", FONT_TITRE_1);
		document.add(paragraph);

		// --Cabinet du PM
		paragraph = new Paragraph("Cabinet du PM", FONT_TITRE_2);
		document.add(paragraph);
		List<DestinataireCommunication> listCabinet = traitementPapierDoc.getCabinetPmCommunication();
		generateCommunication(listCabinet, document);
		// --Chargé de mission
		paragraph = new Paragraph("Chargé de mission", FONT_TITRE_2);
		document.add(paragraph);
		List<DestinataireCommunication> listChargeDeMission = traitementPapierDoc.getChargeMissionCommunication();
		generateCommunication(listChargeDeMission, document);
		// --Autres
		paragraph = new Paragraph("Autres", FONT_TITRE_2);
		document.add(paragraph);
		List<DestinataireCommunication> listAutres = traitementPapierDoc.getAutresDestinatairesCommunication();
		generateCommunication(listAutres, document);
		// --Priorité & signataires & liste de personnes nommées dans le même texte
		paragraph = new Paragraph("Priorité & signataires & liste de personnes nommées dans le même texte",
				FONT_TITRE_2);
		document.add(paragraph);
		table = getInitPdfTable();
		// priorité
		String priorite = "";
		if (traitementPapierDoc.getPriorite() != null && !traitementPapierDoc.getPriorite().isEmpty()) {
			priorite = vocService.getEntryLabel(VocabularyConstants.NIVEAU_PRIORITE_TP_DIRECTORY_NAME,
					traitementPapierDoc.getPriorite());
		}
		ajoutLigneTexte(table, "Priorité", priorite);
		// Liste des personnes nommées
		ajoutLigneTexte(table, "Liste des personnes nommées", traitementPapierDoc.getPersonnesNommees());
		// Signataire
		ajoutLigneTexte(table, "Signataire", traitementPapierDoc.getNomsSignatairesCommunication());
		document.add(table);

		document.newPage();

		// **Signature
		table = getInitPdfTable();
		paragraph = new Paragraph("Signature", FONT_TITRE_1);
		document.add(paragraph);
		// Chemin de croix
		ajoutLigneBooleen(table, "Chemin de croix", traitementPapierDoc.getCheminCroix());
		document.add(table);
		DonneesSignataire donneesSignataire;
		if (traitementPapierDoc.getSignaturePm() != null
				&& signataire
						.equals(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PM)) {
			paragraph = new Paragraph("signature du pm", FONT_TITRE_2);
			document.add(paragraph);
			table = getInitPdfTable();
			donneesSignataire = traitementPapierDoc.getSignaturePm();
			ajoutLigneDate(table, "Date d'envoi", donneesSignataire.getDateEnvoiSignature());
			ajoutLigneDate(table, "Date de retour", donneesSignataire.getDateRetourSignature());
			ajoutLigneTexte(table, "commentaire", donneesSignataire.getCommentaireSignature());
			document.add(table);
		}

		if (traitementPapierDoc.getSignaturePr() != null
				&& signataire
						.equals(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PR)) {
			paragraph = new Paragraph("signature du pr", FONT_TITRE_2);
			document.add(paragraph);
			table = getInitPdfTable();
			donneesSignataire = traitementPapierDoc.getSignaturePr();
			ajoutLigneDate(table, "Date d'envoi", donneesSignataire.getDateEnvoiSignature());
			ajoutLigneDate(table, "Date de retour", donneesSignataire.getDateRetourSignature());
			ajoutLigneTexte(table, "commentaire", donneesSignataire.getCommentaireSignature());
			document.add(table);
		}

		if (traitementPapierDoc.getSignatureSgg() != null
				&& signataire
						.equals(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_SGG)) {
			paragraph = new Paragraph("signature du sgg", FONT_TITRE_2);
			document.add(paragraph);
			table = getInitPdfTable();
			donneesSignataire = traitementPapierDoc.getSignatureSgg();
			ajoutLigneDate(table, "Date d'envoi", donneesSignataire.getDateEnvoiSignature());
			ajoutLigneDate(table, "Date de retour", donneesSignataire.getDateRetourSignature());
			ajoutLigneTexte(table, "commentaire", donneesSignataire.getCommentaireSignature());
			document.add(table);
		}
		document.newPage();

		// **Retour
		paragraph = new Paragraph("Retour", FONT_TITRE_1);
		document.add(paragraph);
		// Retour à
		table = getInitPdfTable();
		ajoutLigneTexte(table, "Retour à", traitementPapierDoc.getRetourA());
		// Date
		ajoutLigneDate(table, "Date", traitementPapierDoc.getDateRetour());
		// Motif
		ajoutLigneTexte(table, "Motif", traitementPapierDoc.getMotifRetour());
		// Signataire
		ajoutLigneTexte(table, "Signataire", traitementPapierDoc.getNomsSignatairesRetour());
		document.add(table);

		document.newPage();
		// *Epreuves
		paragraph = new Paragraph("Epreuves", FONT_TITRE_1);
		document.add(paragraph);
		// Epreuve demandée le
		table = getInitPdfTable();
		InfoEpreuve infoEpreuve = traitementPapierDoc.getEpreuve();
		ajoutLigneDate(table, "Epreuve demandée le", infoEpreuve.getEpreuveDemandeLe());
		// Pour le
		ajoutLigneDate(table, "Pour le", infoEpreuve.getEpreuvePourLe());
		// Numéro de liste
		ajoutLigneTexte(table, "Numéro de liste", infoEpreuve.getNumeroListe());
		// Envoi en relecture le
		ajoutLigneDate(table, "Envoi en relecture le", infoEpreuve.getEnvoiRelectureLe());
		// Destinataire
		ajoutLigneTexte(table, "Destinataire", infoEpreuve.getDestinataireEntete());
		// Signataire
		ajoutLigneTexte(table, "Signataire", infoEpreuve.getNomsSignataires());
		document.add(table);

		table = getInitPdfTable();
		InfoEpreuve nouvelleDemandeEpreuve = traitementPapierDoc.getNouvelleDemandeEpreuves();
		// Epreuve demandée le
		ajoutLigneDate(table, "Nouvelle demande le", nouvelleDemandeEpreuve.getEpreuveDemandeLe());
		// Pour le
		ajoutLigneDate(table, "Pour le", nouvelleDemandeEpreuve.getEpreuvePourLe());
		// Numéro de liste
		ajoutLigneTexte(table, "Numéro de la liste", nouvelleDemandeEpreuve.getNumeroListe());
		// Envoi en relecture le
		ajoutLigneDate(table, "Envoi en relecture de la nouvelle demande le",
				nouvelleDemandeEpreuve.getEnvoiRelectureLe());
		// Destinataire
		ajoutLigneTexte(table, "Destinataire", nouvelleDemandeEpreuve.getDestinataireEntete());
		// Signataire
		ajoutLigneTexte(table, "Signataire", nouvelleDemandeEpreuve.getNomsSignataires());
		document.add(table);

		// Retour du bon à tirer le
		table = getInitPdfTable();
		ajoutLigneDate(table, "Retour du bon à tirer le", traitementPapierDoc.getRetourDuBonaTitrerLe());
		document.add(table);

		table = getInitPdfTable();
		document.newPage();
		// **Publication
		paragraph = new Paragraph("Publication", FONT_TITRE_1);
		document.add(paragraph);
		// date d'envoi JO
		ajoutLigneDate(table, "Date d'envoi JO", traitementPapierDoc.getPublicationDateEnvoiJo());
		// Vecteur de publication
		if (traitementPapierDoc.getPublicationNomPublication() != null
				&& !traitementPapierDoc.getPublicationNomPublication().isEmpty()) {
			ajoutLigneTexte(table, "Vecteur de publication", bulletinService.convertVecteurIdToLabel(session,
					traitementPapierDoc.getPublicationNomPublication()));
		} else {
			ajoutLigneTexteList(table, "Vecteur de publication", null);
		}
		// Mode de parution
		ajoutLigneTexte(table, "Mode de parution", traitementPapierDoc.getPublicationModePublication());
		// Epreuves en retour
		ajoutLigneBooleen(table, "Epreuves en retour", traitementPapierDoc.getPublicationEpreuveEnRetour());
		// Délai publication
		ajoutLigneVocabulaire(table, vocabularyService, "Délai publication", traitementPapierDoc.getPublicationDelai(),
				VocabularyConstants.DELAI_PUBLICATION);

		if (VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE.equals(traitementPapierDoc.getPublicationDelai())) {
			// Publication à date précisée
			ajoutLigneDate(table, "Publication à date précisée", traitementPapierDoc.getPublicationDateDemande());
		}
		// Numéro de liste
		ajoutLigneTexte(table, "Numéro de liste", traitementPapierDoc.getPublicationNumeroListe());
		// Parution JORF
		ajoutLigneDate(table, "Parution JORF - Date JO", traitementPapierDoc.getPublicationDate());
		document.add(table);
		table = getInitPdfTable();

		document.newPage();

		// **Ampliation
		paragraph = new Paragraph("Ampliation", FONT_TITRE_1);
		document.add(paragraph);
		// Dossier papier archivé
		ajoutLigneBooleen(table, "Dossier papier archivé", traitementPapierDoc.getPapierArchive());
		// Destinataire
		ajoutLigneTexteList(table, "Destinataire", traitementPapierDoc.getAmpliationDestinataireMails());

		// historique des ampliations
		List<InfoHistoriqueAmpliation> listHistoriqueAmpliation = traitementPapierDoc.getAmpliationHistorique();
		table = new PdfPTable(2);
		int[] largeurColonnePourcentage = { 5, 5 };
		table.setWidths(largeurColonnePourcentage);
		table.setWidthPercentage(90);
		table.setSpacingBefore(10);
		table.setSpacingAfter(10);
		table.getDefaultCell().setBorderWidth(Rectangle.NO_BORDER);
		table.setHeaderRows(1);
		ajoutHeaderCell(table, "Date d'envoi");
		ajoutHeaderCell(table, "Liste des destinataires");
		for (InfoHistoriqueAmpliation historiqueAmpliation : listHistoriqueAmpliation) {
			if (historiqueAmpliation.getDateEnvoiAmpliation() != null) {
				ajoutDateCell(table, historiqueAmpliation.getDateEnvoiAmpliation().getTime(), PdfPCell.BOTTOM);
			} else {
				ajoutDateCell(table, null, PdfPCell.BOTTOM);
			}
			ajoutTexteCell(table, StringUtils.join(historiqueAmpliation.getAmpliationDestinataireMails(), " "),
					PdfPCell.BOTTOM);
		}
		// ajout de la table
		document.add(table);

	}

	/**
	 * Construit le tableau des communications pour le traitement papier
	 * 
	 * @param listDestinataire
	 * @param document
	 * @throws DocumentException
	 */
	public void generateCommunication(List<DestinataireCommunication> listDestinataire, Document document)
			throws DocumentException {
		VocabularyService vocService = STServiceLocator.getVocabularyService();
		// 6 colonnes pour la table Cabinet du PM
		PdfPTable table = new PdfPTable(6);
		int[] largeurColonnePourcentage = { 5, 2, 2, 2, 2, 5 };
		table.setWidths(largeurColonnePourcentage);
		table.setWidthPercentage(90);
		table.setSpacingBefore(10);
		table.setSpacingAfter(10);
		table.getDefaultCell().setBorderWidth(Rectangle.NO_BORDER);
		table.setHeaderRows(1);
		ajoutHeaderCell(table, "Destinataire");
		ajoutHeaderCell(table, "Date d'envoi");
		ajoutHeaderCell(table, "Date retour");
		ajoutHeaderCell(table, "Date de relance");
		ajoutHeaderCell(table, "Sens de l'avis");
		ajoutHeaderCell(table, "Objet");
		for (DestinataireCommunication destinataire : listDestinataire) {
			// On va faire une table avec les différentes valeurs ? Comme pour la feuille de route ?
			// On ajout d'abord un header - cf comme pour le journal technique en fait
			ajoutTexteCell(table, destinataire.getNomDestinataireCommunication(), PdfPCell.BOTTOM); // ?
			if (destinataire.getDateEnvoi() != null) {
				ajoutDateCell(table, destinataire.getDateEnvoi().getTime(), PdfPCell.BOTTOM);
			} else {
				ajoutDateCell(table, null, PdfPCell.BOTTOM);
			}
			if (destinataire.getDateRetour() != null) {
				ajoutDateCell(table, destinataire.getDateRetour().getTime(), PdfPCell.BOTTOM);
			} else {
				ajoutDateCell(table, null, PdfPCell.BOTTOM);
			}
			if (destinataire.getDateRelance() != null) {
				ajoutDateCell(table, destinataire.getDateRelance().getTime(), PdfPCell.BOTTOM);
			} else {
				ajoutDateCell(table, null, PdfPCell.BOTTOM);
			}
			String sensAvis = destinataire.getSensAvis();
			if (!StringUtils.isBlank(sensAvis)) {
				VocType vocType = new VocType();
				vocType.setId(sensAvis);
				String label = vocService
						.getEntryLabel(VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_DIRECTORY, sensAvis);
				ajoutTexteCell(table, label, PdfPCell.BOTTOM);
			} else {
				ajoutTexteCell(table, "", PdfPCell.BOTTOM);
			}
			ajoutTexteCell(table, destinataire.getObjet(), PdfPCell.BOTTOM);
		}
		// ajout de la table
		document.add(table);
	}

}
