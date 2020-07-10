package fr.dila.solonepg.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.activation.DataSource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.migration.MigrationDetailModel;
import fr.dila.solonepg.core.dto.DossierArchivageStatDTO;
import fr.dila.ss.api.client.InjectionGvtDTO;
import fr.dila.ss.api.constant.SSInjectionGouvernementConstants;
import fr.dila.ss.core.client.InjectionGvtDTOImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.organigramme.EntiteNodeImpl;
import fr.dila.st.core.organigramme.ProtocolarOrderComparator;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.STExcelUtil;

/**
 * Classe utilitaire pour créér des Documents Excel.
 * 
 * @author arolin
 */
public class ExcelUtil extends STExcelUtil {

	/**
	 * Logger formalisé en surcouche du logger apache/log4j
	 */
	private static final STLogger	LOGGER				= STLogFactory.getLog(ExcelUtil.class);

	private static final String			DOSSIERS_SHEET_NAME	= "Liste dossiers";

	private static final String[]	DOSSIER_HEADER		= { "Numéro NOR", "Titre Acte", "Ministère responsable",
			"Ministère attaché", "Responsable dossier"	};
	private static final int		NB_COLONNES_DOSSIER	= DOSSIER_HEADER.length;
	
	private static final String[]	HEADER_ADMIN_DATE_DERNIERE_CONNEXION	= { "Utilisateur", "Nom", "Prénom", "Mél.", "Téléphone", "Date début", "Ministère", "Direction", "Poste", "Date dernière connexion" };
	private static final String[]	HEADER_ADMIN_SIMPLE	= { "Utilisateur", "Nom", "Prénom", "Mél.", "Téléphone", "Date début", "Ministère", "Direction", "Poste" };
	private static final String 	USERS_SHEET_NAME	= "Recherche utilisateurs";

	private static final String[]	HEADER_MIGRATION_DETAILS	= {"Message", "Date de début", "Date de fin", "Statut"};
	private static final String 	MIGRATION_DETAILS_SHEET_NAME	= "Détails de migration";
	
	private static final String[]	HEADER_STAT_DOSSIERS_ARCHIVES	= {"Nor", "Titre acte", "Statut sur la période", "Date du changement de statut", "Statut en cours", "Message erreur"};
	private static final String 	STAT_DOSSIERS_ARCHIVES_SHEET_NAME	= "Statistique dossiers archivés";

	/**
	 * Créé un fichier Excel contenant les informations souhaitées pour une liste de dossiers;
	 * 
	 * @param dossiers
	 * @return fichier Excel sous forme de Datasource (afin d'envoyer le fichier comme pièce jointe dans un mail).
	 */
	public static DataSource creationListDossierExcel(List<DocumentModel> dossiers) {
		DataSource fichierExcelResultat = null;
		try {
			// création du fichier Excel
			HSSFWorkbook workbook = initExcelFile(DOSSIERS_SHEET_NAME, DOSSIER_HEADER);
			HSSFSheet sheet = workbook.getSheet(DOSSIERS_SHEET_NAME);
			int numRow = 1;
			for (DocumentModel dossierDoc : dossiers) {
				addDocumentToSheet(sheet, numRow++, dossierDoc);
			}

			formatStyle(workbook, DOSSIERS_SHEET_NAME, NB_COLONNES_DOSSIER);

			fichierExcelResultat = convertExcelToDataSource(workbook);

		} catch (IOException exc) {
			LOGGER.error(null, STLogEnumImpl.FAIL_CREATE_EXCEL_TEC, exc);
		}
		return fichierExcelResultat;
	}
	
	public static DataSource exportResultUserSearch(List<DocumentModel> usersDocs, boolean isAdmin, boolean isAdminMinisteriel,
			Set<String> adminMinisterielMinisteres) {
		DataSource fichierExcelResultat = null;
		try {
			// création du fichier Excel
			HSSFWorkbook workbook = null;
			int nbColonnes = 0;
			if (isAdmin || isAdminMinisteriel) {
				workbook = initExcelFile(USERS_SHEET_NAME, HEADER_ADMIN_DATE_DERNIERE_CONNEXION);
				nbColonnes = HEADER_ADMIN_DATE_DERNIERE_CONNEXION.length;
			} else {
				workbook = initExcelFile(USERS_SHEET_NAME, HEADER_ADMIN_SIMPLE);
				nbColonnes = HEADER_ADMIN_SIMPLE.length;
			}
			HSSFSheet sheet = workbook.getSheet(USERS_SHEET_NAME);
			int numRow = 1;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			for (DocumentModel userDoc : usersDocs) {
				STUser user = userDoc.getAdapter(STUser.class);
				if (user != null) {
					STUserService userService = STServiceLocator.getSTUserService();
					numRow = numRow + 1;
					HSSFRow currentRow = sheet.createRow(numRow);
					String dateDebut = "";
					if (user.getDateDebut() != null) {
						dateDebut = sdf.format(user.getDateDebut().getTime());
					}
					String ministeres = "";
					List<String> ministeresIds = Collections.emptyList();
					String directions = "";
					String postes = "";
					try {
						ministeres = userService.getUserMinisteres(userDoc.getId());
						ministeresIds = userService.getAllUserMinisteresId(userDoc.getId());
						directions = userService.getAllDirectionsRattachement(userDoc.getId());
						postes = userService.getUserPostes(userDoc.getId());
					} catch (ClientException exc) {
						
					}
					boolean exportDateLastConnection = false;
					if (isAdmin) {
						exportDateLastConnection = true;
					} else if (isAdminMinisteriel) {
						// on exporte seulement si on a un élément en commun entre les ministères de l'utilisateur
						// et les ministères de l'utilisateur réalisant l'export
						exportDateLastConnection = ! Collections.disjoint(ministeresIds, adminMinisterielMinisteres);
					}
					if (exportDateLastConnection) {
						String dateDerniereConnexion = "";
						// Utilisation autorisée de dateLastConnection
						@SuppressWarnings("deprecation")
						Calendar calendarDateLastConnection = user.getDateLastConnection();
						if (calendarDateLastConnection != null) {
							dateDerniereConnexion = sdf.format(calendarDateLastConnection.getTime());
						}
						addCellToRow(currentRow, 0, user.getUsername(), user.getLastName(), user.getFirstName(),
								user.getEmail(), user.getTelephoneNumber(), dateDebut, ministeres, directions, postes, dateDerniereConnexion);
					} else if (isAdminMinisteriel) {
						// cas d'un administrateur ministériel sans autorisation d'accès à la date de dernière connexion
						// (utilisateur dans un ministère non géré)
						addCellToRow(currentRow, 0, user.getUsername(), user.getLastName(), user.getFirstName(),
								user.getEmail(), user.getTelephoneNumber(), dateDebut, ministeres, directions, postes, "non accessible");
					} else {
						addCellToRow(currentRow, 0, user.getUsername(), user.getLastName(), user.getFirstName(),
								user.getEmail(), user.getTelephoneNumber(), dateDebut, ministeres, directions, postes);
					}
					
				}
			}

			formatStyle(workbook, USERS_SHEET_NAME, nbColonnes);

			fichierExcelResultat = convertExcelToDataSource(workbook);

		} catch (IOException exc) {
			LOGGER.error(null, STLogEnumImpl.FAIL_CREATE_EXCEL_TEC, exc);
		}
		return fichierExcelResultat;
	}

	public static DataSource exportMigrationDetails(List<MigrationDetailModel> detailDocs) {
		DataSource fichierExcelResultat = null;
		try {
			// création du fichier Excel
			HSSFWorkbook workbook = null;
			int nbColonnes = 0;
			workbook = initExcelFile(MIGRATION_DETAILS_SHEET_NAME, HEADER_MIGRATION_DETAILS);
			nbColonnes = HEADER_MIGRATION_DETAILS.length;
			HSSFSheet sheet = workbook.getSheet(MIGRATION_DETAILS_SHEET_NAME);
			int numRow = 1;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			for (MigrationDetailModel detail : detailDocs) {
				if (detail != null) {
					numRow = numRow + 1;
					HSSFRow currentRow = sheet.createRow(numRow);
					String startDate = "";
					if (detail.getStartDate() != null) {
						startDate = sdf.format(detail.getStartDate().getTime());
					}
					String endDate = "";
					if (detail.getEndDate() != null) {
						endDate = sdf.format(detail.getEndDate().getTime());
					}

					addCellToRow(currentRow, 0, detail.getDetail(), startDate, endDate, detail.getStatut());
				}
			}

			formatStyle(workbook, MIGRATION_DETAILS_SHEET_NAME, nbColonnes);

			fichierExcelResultat = convertExcelToDataSource(workbook);

		} catch (IOException exc) {
			LOGGER.error(null, STLogEnumImpl.FAIL_CREATE_EXCEL_TEC, exc);
		}
		return fichierExcelResultat;
	}
	
	public static DataSource exportStatDossiersArchives(List<DossierArchivageStatDTO> listDossiersArchives) {
		DataSource fichierExcelResultat = null;
		try {
			// création du fichier Excel
			HSSFWorkbook workbook = null;
			int nbColonnes = 0;
			workbook = initExcelFile(STAT_DOSSIERS_ARCHIVES_SHEET_NAME, HEADER_STAT_DOSSIERS_ARCHIVES);
			nbColonnes = HEADER_STAT_DOSSIERS_ARCHIVES.length;
			HSSFSheet sheet = workbook.getSheet(STAT_DOSSIERS_ARCHIVES_SHEET_NAME);
			int numRow = 1;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			for (DossierArchivageStatDTO dossier : listDossiersArchives) {
					HSSFRow currentRow = sheet.createRow(numRow);
					String sdfDateModif = "";
					if (dossier.getDateModif() != null) {
						sdfDateModif = sdf.format(dossier.getDateModif().getTime());
					}
					addCellToRow(currentRow, 0, dossier.getNor(), dossier.getTitreActe(), dossier.getStatutPeriode(), sdfDateModif,
							dossier.getStatut(), dossier.getErreur());
					numRow ++;
			}

			formatStyle(workbook, STAT_DOSSIERS_ARCHIVES_SHEET_NAME, nbColonnes);

			fichierExcelResultat = convertExcelToDataSource(workbook);

		} catch (IOException exc) {
			LOGGER.error(null, STLogEnumImpl.FAIL_CREATE_EXCEL_TEC, exc);
		}
		return fichierExcelResultat;
	}

	/**
	 * Créé un fichier Excel contenant les informations souhaitées pour une liste de dossiers;
	 * 
	 * @param dossiersIds
	 *            la liste des ids des dossiers
	 * @return fichier Excel sous forme de Datasource (afin d'envoyer le fichier comme pièce jointe dans un mail).
	 * @throws ClientException
	 */
	public static DataSource creationListDossierExcelFromIds(CoreSession session, List<String> dossiersIds) {
		DataSource fichierExcelResultat = null;
		try {
			// création du fichier Excel
			HSSFWorkbook workbook = initExcelFile(DOSSIERS_SHEET_NAME, DOSSIER_HEADER);
			HSSFSheet sheet = workbook.getSheet(DOSSIERS_SHEET_NAME);
			int numRow = 1;
			for (String dossierId : dossiersIds) {
				IdRef docRef = new IdRef(dossierId);
				if (session.exists(docRef)) {
					addDocumentToSheet(sheet, numRow++, session.getDocument(docRef));
				}
			}

			formatStyle(workbook, DOSSIERS_SHEET_NAME, NB_COLONNES_DOSSIER);

			fichierExcelResultat = convertExcelToDataSource(workbook);

		} catch (IOException exc) {
			LOGGER.error(null, STLogEnumImpl.FAIL_CREATE_EXCEL_TEC, exc);
		} catch (ClientException exc) {
			LOGGER.error(null, STLogEnumImpl.FAIL_CREATE_EXCEL_TEC, exc);
		}
		return fichierExcelResultat;
	}

	/**
	 * Ajoute une ligne dossier (le documentModel) au fichier excel à la ligne row
	 * 
	 * @param sheet
	 * @param row
	 *            le numéro de ligne où les données seront ajoutées
	 * @param dossierDoc
	 *            le dossier epg à ajouter
	 */
	public static void addDocumentToSheet(final HSSFSheet sheet, int row, DocumentModel dossierDoc) {
		if (dossierDoc != null) {
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			if (dossier != null) {
				HSSFRow currentRow = sheet.createRow(row);
				String ministereResponsable = null;
				try {
					// ajout ministère responsable
					ministereResponsable = STServiceLocator.getSTMinisteresService()
							.getEntiteNode(dossier.getMinistereResp()).getLabel();
				} catch (ClientException ce) {
					LOGGER.warn(null, STLogEnumImpl.FAIL_GET_INFORMATION_TEC, dossier, ce);
					ministereResponsable = "Minstère responsable non reconnu";
				}
				String ministereAttache = null;
				try {
					// ajout ministère attaché
					ministereAttache = STServiceLocator.getSTMinisteresService()
							.getEntiteNode(dossier.getMinistereAttache()).getLabel();
				} catch (ClientException ce) {
					LOGGER.warn(null, STLogEnumImpl.FAIL_GET_INFORMATION_TEC, dossier, ce);
					ministereAttache = "Ministère attaché non reconnu";
				}

				STExcelUtil.addCellToRow(currentRow, 0, dossier.getNumeroNor(), dossier.getTitreActe(),
						ministereResponsable, ministereAttache, dossier.getNomCompletRespDossier());
			}
		}
	}

	/**
	 * Initialise un workbook pour un fichier excel pour les dossiers EPG
	 * 
	 * @return
	 */
	public static HSSFWorkbook createDossierExcelFile() {
		return initExcelFile(DOSSIERS_SHEET_NAME, DOSSIER_HEADER);
	}

	public static void formatStyleDossier(HSSFWorkbook workbook) {
		formatStyle(workbook, DOSSIERS_SHEET_NAME, NB_COLONNES_DOSSIER);
	}

	/**
	 * ajoute une ligne dossier à la fin du fichier excel
	 * 
	 * @param dossierDoc
	 *            le documentModel du dossier à ajouter au fichier excel
	 * @param workbook
	 *            le fichier excel où ajouter l'information
	 */
	public static void addDossierAtEndToFile(final DocumentModel dossierDoc, final HSSFWorkbook workbook) {
		addDocumentToSheet(workbook.getSheet(DOSSIERS_SHEET_NAME),
				getLastLine(workbook.getSheet(DOSSIERS_SHEET_NAME)) + 1, dossierDoc);
	}

	public static String getDossierSheetName() {
		return DOSSIERS_SHEET_NAME;
	}

	/**
	 * Crée le fichier de base pour les exports de gouvernement
	 * 
	 */
	public static DataSource createExportGvt(CoreSession session) {

		DataSource fichierExcelResultat = null;
		try {
			// création du fichier Excel
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();
			wb.setSheetName(0, "gvt");
			// création des colonnes header
			HSSFRow currentRow = sheet.createRow(0);
			int numCol = 0;

			currentRow.createCell(numCol++).setCellValue("NOR");
			currentRow.createCell(numCol++).setCellValue("OP S");
			currentRow.createCell(numCol++).setCellValue("SOLON EPG à créer");
			currentRow.createCell(numCol++).setCellValue("OP R");
			currentRow.createCell(numCol++).setCellValue("REPONSES à créer");
			currentRow.createCell(numCol++).setCellValue("SOLON à modifier");
			currentRow.createCell(numCol++).setCellValue("Libellé Court");
			currentRow.createCell(numCol++).setCellValue("Libellé Long");
			currentRow.createCell(numCol++).setCellValue("Formule Entêtes");
			currentRow.createCell(numCol++).setCellValue("Civilité");
			currentRow.createCell(numCol++).setCellValue("Prénom");
			currentRow.createCell(numCol++).setCellValue("Nom");
			currentRow.createCell(numCol++).setCellValue("Prénom et Nom");
			currentRow.createCell(numCol++).setCellValue("Date de début");
			currentRow.createCell(numCol++).setCellValue("Date de fin");
			currentRow.createCell(numCol++).setCellValue("NOR EPP (ministère de rattachement)");
			currentRow.createCell(numCol++).setCellValue("Nouvelle identité EPP");

			int nbCol = numCol;

			// Font et style de la ligne de titre
			HSSFFont font = wb.createFont();
			font.setFontHeightInPoints((short) 11);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setFont(font);

			// Ajout de la ligne correspondant au gouvernement actuel
			OrganigrammeNode currentGvt = STServiceLocator.getSTGouvernementService().getCurrentGouvernement();
			HSSFRow gvtRow = sheet.createRow(1);
			gvtRow.createCell(SSInjectionGouvernementConstants.INJ_COL_LIB_LONG).setCellValue(currentGvt.getLabel());
			HSSFCell gvtCurrentDateCell = gvtRow.createCell(SSInjectionGouvernementConstants.INJ_COL_DATE_DEB);
			gvtCurrentDateCell.setCellValue(currentGvt.getDateDebut());
			HSSFCellStyle dateCellStyle = wb.createCellStyle();
			HSSFDataFormat hssfDataFormat = wb.createDataFormat();
			dateCellStyle.setDataFormat(hssfDataFormat.getFormat("dd/mm/yyyy"));
			gvtCurrentDateCell.setCellStyle(dateCellStyle);

			// Ajout des lignes du gouvernement
			HSSFRow currentMinRow;
			EntiteNode minNode;
			int rowNum = 1;
			List<EntiteNode> currentMinisteres = STServiceLocator.getSTMinisteresService().getCurrentMinisteres();
			Collections.sort(currentMinisteres, new ProtocolarOrderComparator());
			for (OrganigrammeNode currentMin : currentMinisteres) {
				rowNum++;
				currentMinRow = sheet.createRow(rowNum);
				if (currentMin instanceof EntiteNode) {
					minNode = (EntiteNode) currentMin;
					EntiteNodeImpl epgNode = ((EntiteNodeImpl) currentMin);
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_NOR).setCellValue(
							epgNode.getNorMinistere());
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_OPS).setCellValue(
							epgNode.getOrdre());
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_A_CREER_REP).setCellValue("0");
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_A_CREER_SOLON).setCellValue("0");
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_A_MODIFIER_SOLON).setCellValue(
							"0");
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_NOUV_ENTITE_EPP)
							.setCellValue("0");
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_LIB_COURT).setCellValue(
							epgNode.getEdition());
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_LIB_LONG).setCellValue(
							epgNode.getLabel());
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_ENTETE).setCellValue(
							epgNode.getFormule());
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_CIVILITE).setCellValue(
							epgNode.getMembreGouvernementCivilite());
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_PRENOM).setCellValue(
							epgNode.getMembreGouvernementPrenom());
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_NOM).setCellValue(
							epgNode.getMembreGouvernementNom());
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_PRENOM_NOM).setCellValue(
							epgNode.getMembreGouvernement());
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_DATE_DEB).setCellValue(
							epgNode.getDateDebut());

					// format date
					currentMinRow.getCell(SSInjectionGouvernementConstants.INJ_COL_DATE_DEB)
							.setCellStyle(dateCellStyle);
					if (minNode.getDateFin() != null) {
						currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_DATE_FIN).setCellValue(
								epgNode.getDateFin());
						currentMinRow.getCell(SSInjectionGouvernementConstants.INJ_COL_DATE_FIN).setCellStyle(
								dateCellStyle);
					}
				} else {
					currentMinRow.createCell(SSInjectionGouvernementConstants.INJ_COL_LIB_LONG).setCellValue(
							"Export impossible de l'entité : " + currentMin.getId());
				}
			}

			// size des colonnes
			for (int i = 0; i < nbCol; i++) {
				sheet.getRow(0).getCell(i).setCellStyle(cellStyle);
				sheet.autoSizeColumn(i);
			}

			// récupération du fichier Excel en tant que DataSource
			fichierExcelResultat = convertExcelToDataSource(wb);
		} catch (Exception exc) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_EXCEL_TEC, exc);
		}
		return fichierExcelResultat;
	}

	public static List<InjectionGvtDTO> prepareImportGvt(final CoreSession session, final File file) {
		List<InjectionGvtDTO> listImportGvt = new ArrayList<InjectionGvtDTO>();
		HSSFWorkbook wb = null;
		try {
			// lecture du fichier Excel
			wb = new HSSFWorkbook(new FileInputStream(file));
			// récupération de la première feuille
			HSSFSheet sheet = wb.getSheetAt(0);
			// récupération de l'itérateur
			Iterator<Row> rowIterator = sheet.iterator();
			Row currentRow;
			int nbRow = 0;
			Date dateDeb, dateFin;
			while (rowIterator.hasNext()) {
				currentRow = rowIterator.next();
				if (nbRow == 0) { // entêtes
					if (currentRow.getCell(currentRow.getFirstCellNum()).getStringCellValue().compareTo("NOR") != 0) {
						LOGGER.error(session, STLogEnumImpl.FAIL_PROCESS_EXCEL_TEC,
								"Le fichier n'est pas formaté correctement");
						return null;
					}
				} else if (nbRow == 1) { // gouvernement
					dateDeb = getDateValueFromCell(currentRow
							.getCell(SSInjectionGouvernementConstants.INJ_COL_DATE_DEB));
					dateFin = getDateValueFromCell(currentRow
							.getCell(SSInjectionGouvernementConstants.INJ_COL_DATE_FIN));
					listImportGvt.add(new InjectionGvtDTOImpl(null, false, null, getStringValueFromCell(currentRow
							.getCell(SSInjectionGouvernementConstants.INJ_COL_LIB_LONG)), null, null, null, null, null,
							dateDeb, dateFin, true));
				} else { // lignes d'entités
					dateDeb = getDateValueFromCell(currentRow
							.getCell(SSInjectionGouvernementConstants.INJ_COL_DATE_DEB));
					dateFin = getDateValueFromCell(currentRow
							.getCell(SSInjectionGouvernementConstants.INJ_COL_DATE_FIN));

					listImportGvt
							.add(new InjectionGvtDTOImpl(getStringValueFromCell(currentRow
									.getCell(SSInjectionGouvernementConstants.INJ_COL_NOR)),
									getStringValueFromCell(currentRow
											.getCell(SSInjectionGouvernementConstants.INJ_COL_OPS)),
									getStringValueFromCell(currentRow
											.getCell(SSInjectionGouvernementConstants.INJ_COL_OPR)), "1"
											.equals(getStringValueFromCell(currentRow
													.getCell(SSInjectionGouvernementConstants.INJ_COL_A_CREER_SOLON))),
									"1".equals(getStringValueFromCell(currentRow
											.getCell(SSInjectionGouvernementConstants.INJ_COL_A_MODIFIER_SOLON))), "1"
											.equals(getStringValueFromCell(currentRow
													.getCell(SSInjectionGouvernementConstants.INJ_COL_A_CREER_REP))),
									getStringValueFromCell(currentRow
											.getCell(SSInjectionGouvernementConstants.INJ_COL_LIB_COURT)),
									getStringValueFromCell(currentRow
											.getCell(SSInjectionGouvernementConstants.INJ_COL_LIB_LONG)),
									getStringValueFromCell(currentRow
											.getCell(SSInjectionGouvernementConstants.INJ_COL_ENTETE)),
									getStringValueFromCell(currentRow
											.getCell(SSInjectionGouvernementConstants.INJ_COL_CIVILITE)),
									getStringValueFromCell(currentRow
											.getCell(SSInjectionGouvernementConstants.INJ_COL_PRENOM)),
									getStringValueFromCell(currentRow
											.getCell(SSInjectionGouvernementConstants.INJ_COL_NOM)),
									getStringValueFromCell(currentRow
											.getCell(SSInjectionGouvernementConstants.INJ_COL_PRENOM_NOM)), dateDeb,
									dateFin, getStringValueFromCell(currentRow
											.getCell(SSInjectionGouvernementConstants.INJ_COL_NOR_EPP)),
									"1".equals(getStringValueFromCell(currentRow
											.getCell(SSInjectionGouvernementConstants.INJ_COL_NOUV_ENTITE_EPP))), false));
				}
				nbRow++;
			}
		} catch (Exception exc) {

			return null;
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					// On ne fait rien
				}
			}
		}
		return listImportGvt;
	}

	private static String getStringValueFromCell(Cell cell) {
		String value = null;
		if (cell != null) {
			if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				value = cell.getStringCellValue().trim();
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				value = String.valueOf((int) cell.getNumericCellValue()).trim();
			}

		}
		return value;
	}

	private static Date getDateValueFromCell(Cell cell) {
		return cell == null ? null : DateUtil.getJavaDate(cell.getNumericCellValue());
	}

	private ExcelUtil() {
		// Private default constructor
	}
}
