package fr.dila.solonepg.core.util;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.recherche.EpgDossierListingDTO;
import fr.dila.solonepg.core.dto.DossierArchivageStatDTO;
import fr.dila.solonepg.core.export.AdminDossierConfig;
import fr.dila.solonepg.core.export.DossierConfig;
import fr.dila.solonepg.core.export.RechercheDossierConfig;
import fr.dila.solonepg.core.export.dto.ExportDossierDTO;
import fr.dila.solonepg.core.export.dto.ExportRechercheDossierDTO;
import fr.dila.ss.api.client.InjectionGvtDTO;
import fr.dila.ss.api.constant.SSInjGvtColumnsEnum;
import fr.dila.ss.core.client.InjectionGvtDTOImpl;
import fr.dila.ss.core.util.SSExcelUtil;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.organigramme.EntiteNodeImpl;
import fr.dila.st.core.organigramme.ProtocolarOrderComparator;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.STExcelUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.activation.DataSource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.schema.PrefetchInfo;

/**
 * Classe utilitaire pour créér des Documents Excel.
 *
 * @author arolin
 */
public final class EpgExcelUtil {
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(EpgExcelUtil.class);

    private static final String DOSSIERS_SHEET_NAME = ResourceHelper.getString("export.dossier.sheet.name");

    private static final String[] DOSSIER_HEADER = {
        "export.header.nor",
        "export.header.titre.acte",
        "export.header.ministere.responsable",
        "export.header.ministere.attache",
        "export.header.responsable.dossier"
    };

    private static final int NB_COLONNES_DOSSIER = DOSSIER_HEADER.length;

    private static final String[] HEADER_STAT_DOSSIERS_ARCHIVES = {
        "export.header.nor",
        "export.header.titre.acte",
        "export.header.statut.periode",
        "export.header.date.changement.statut",
        "export.header.statut.en.cours",
        "export.header.message.erreur"
    };
    private static final String STAT_DOSSIERS_ARCHIVES_SHEET_NAME = ResourceHelper.getString(
        "export.dossiers.archives.sheet.name"
    );

    private static final String[] HEADER_ATTENTE_SIGNATURE_LOIS_EXPORT = {
        "attente.signature.label.header.titre",
        "attente.signature.label.header.nor",
        "attente.signature.label.header.publication.demandee",
        "attente.signature.label.header.arrivee.solon",
        "attente.signature.label.header.accord.pm",
        "attente.signature.label.header.accord.sgg",
        "attente.signature.label.header.arrivee.originale",
        "attente.signature.label.header.mise.en.signature",
        "attente.signature.label.header.retour.signature",
        "attente.signature.label.header.decret.pr",
        "attente.signature.label.header.envoi.pr",
        "attente.signature.label.header.retour.pr",
        "attente.signature.label.header.date.jo",
        "attente.signature.label.header.delai.publication",
        "attente.signature.label.header.observation"
    };
    private static final String[] HEADER_ATTENTE_SIGNATURE_AUTRES_EXPORT = {
        "attente.signature.label.header.titre",
        "attente.signature.label.header.nor",
        "attente.signature.label.header.publication.demandee",
        "attente.signature.label.header.arrivee.solon",
        "attente.signature.label.header.accord.pm",
        "attente.signature.label.header.accord.sgg",
        "attente.signature.label.header.date.jo",
        "attente.signature.label.header.delai.publication",
        "attente.signature.label.header.observation"
    };
    private static final String ATTENTE_SIGNATURE_EXPORT_SHEET_NAME = ResourceHelper.getString(
        "export.attente.signature.sheet.name"
    );

    private static final int CHARACTER_WIDTH_MULTIPLIER = 256;
    private static final float CHARACTER_PIXEL_MULTIPLIER = 7.29f;
    private static final float CHARACTER_PIXEL_GAB_MULTIPLIER = 2f;
    private static final int ATTENTE_SIGNATURE_AUTRES_COLUMN_WIDTH = 10 * CHARACTER_WIDTH_MULTIPLIER;
    private static final int ATTENTE_SIGNATURE_LOIS_COLUMN_WIDTH = 6 * CHARACTER_WIDTH_MULTIPLIER;
    private static final int ATTENTE_SIGNATURE_HEADER_ROW_HEIGHT = 20;
    private static final float ATTENTE_SIGNATURE_ROW_HEIGHT = 15f;

    public static DataSource exportAdminDossiers(CoreSession session, List<EpgDossierListingDTO> dossiers) {
        AdminDossierConfig adminDossierConfig = new AdminDossierConfig(dossiers);
        return adminDossierConfig.getDataSource(session);
    }

    /**
     * Créé un fichier Excel contenant les informations souhaitées pour une liste de dossiers;
     */
    public static DataSource exportDossiers(List<ExportDossierDTO> dossiers) {
        DossierConfig dossierConfig = new DossierConfig(dossiers);
        return dossierConfig.getDataSource(null);
    }

    /**
     * Créé un fichier Excel contenant les informations souhaitées pour une liste de dossiers;
     *
     * @param dossiers
     * @return fichier Excel sous forme de Datasource (afin d'envoyer le fichier comme pièce jointe dans un mail).
     */
    public static DataSource creationListDossierExcel(List<DocumentModel> dossiers) {
        return exportDossiers(
            dossiers.stream().filter(Objects::nonNull).map(ExportDossierDTO::new).collect(Collectors.toList())
        );
    }

    public static DataSource exportRechercheDossiers(CoreSession session, List<ExportRechercheDossierDTO> dossiers) {
        RechercheDossierConfig rechercheDossierConfig = new RechercheDossierConfig(dossiers);
        return rechercheDossierConfig.getDataSource(session);
    }

    public static DataSource exportStatDossiersArchives(List<DossierArchivageStatDTO> listDossiersArchives) {
        DataSource fichierExcelResultat = null;
        try {
            // création du fichier Excel
            HSSFWorkbook workbook = STExcelUtil.initExcelFile(
                STAT_DOSSIERS_ARCHIVES_SHEET_NAME,
                HEADER_STAT_DOSSIERS_ARCHIVES
            );
            int nbColonnes = HEADER_STAT_DOSSIERS_ARCHIVES.length;
            HSSFSheet sheet = workbook.getSheet(STAT_DOSSIERS_ARCHIVES_SHEET_NAME);
            int numRow = 1;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (DossierArchivageStatDTO dossier : listDossiersArchives) {
                HSSFRow currentRow = sheet.createRow(numRow);
                String sdfDateModif = "";
                if (dossier.getDateModif() != null) {
                    sdfDateModif = sdf.format(dossier.getDateModif().getTime());
                }
                STExcelUtil.addCellToRow(
                    currentRow,
                    0,
                    dossier.getNor(),
                    dossier.getTitreActe(),
                    dossier.getStatutPeriode(),
                    sdfDateModif,
                    dossier.getStatut(),
                    dossier.getErreur()
                );
                numRow++;
            }

            STExcelUtil.formatStyle(workbook, STAT_DOSSIERS_ARCHIVES_SHEET_NAME, nbColonnes);

            fichierExcelResultat = STExcelUtil.convertExcelToDataSource(workbook);
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
     */
    public static DataSource creationListDossierExcelFromIds(CoreSession session, List<String> dossiersIds) {
        List<DocumentModel> dossiersDoc = session.getDocuments(
            dossiersIds,
            new PrefetchInfo(DossierSolonEpgConstants.DOSSIER_SCHEMA)
        );
        return creationListDossierExcel(dossiersDoc);
    }

    /**
     * Initialise un workbook pour un fichier excel pour les dossiers EPG
     */
    public static HSSFWorkbook createDossierExcelFile() {
        return STExcelUtil.initExcelFile(DOSSIERS_SHEET_NAME, DOSSIER_HEADER);
    }

    public static void formatStyleDossier(HSSFWorkbook workbook) {
        STExcelUtil.formatStyle(workbook, DOSSIERS_SHEET_NAME, NB_COLONNES_DOSSIER);
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
        addRowForExportDossier(
            ImmutableTriple.of(
                workbook.getSheet(DOSSIERS_SHEET_NAME),
                new ExportDossierDTO(dossierDoc),
                workbook.getSheet(DOSSIERS_SHEET_NAME).getLastRowNum() + 1
            )
        );
    }

    private static void addRowForExportDossier(ImmutableTriple<HSSFSheet, ExportDossierDTO, Integer> exportParams) {
        HSSFSheet sheet = exportParams.getLeft();
        ExportDossierDTO dossier = exportParams.getMiddle();
        Integer numRow = exportParams.getRight();

        if (dossier != null) {
            HSSFRow currentRow = sheet.createRow(numRow);

            String ministereResponsable = dossier.isHasMinistereLabel()
                ? dossier.getMinistereResp()
                : getMinistereLabel(dossier, dossier.getMinistereResp(), "Ministère responsable non reconnu");
            String ministereAttache = dossier.isHasMinistereLabel()
                ? dossier.getMinistereAttache()
                : getMinistereLabel(dossier, dossier.getMinistereAttache(), "Ministère attaché non reconnu");
            STExcelUtil.addCellToRow(
                currentRow,
                0,
                dossier.getNumeroNor(),
                dossier.getTitreActe(),
                ministereResponsable,
                ministereAttache,
                dossier.getNomCompletRespDossier()
            );
        }
    }

    public static String getMinistereLabel(ExportDossierDTO dossier, String ministereId, String ministereInconnu) {
        String ministere;
        try {
            ministere = STServiceLocator.getSTMinisteresService().getEntiteNode(ministereId).getLabel();
        } catch (NuxeoException ce) {
            LOGGER.warn(null, STLogEnumImpl.FAIL_GET_INFORMATION_TEC, dossier.toString(), ce);
            ministere = ministereInconnu;
        }
        return ministere;
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
            font.setBold(true);

            HSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setFont(font);

            // Ajout de la ligne correspondant au gouvernement actuel
            OrganigrammeNode currentGvt = STServiceLocator.getSTGouvernementService().getCurrentGouvernement();
            HSSFRow gvtRow = sheet.createRow(1);
            SSExcelUtil.createCell(gvtRow, SSInjGvtColumnsEnum.INJ_COL_LIB_LONG).setCellValue(currentGvt.getLabel());
            HSSFCell gvtCurrentDateCell = SSExcelUtil.createCell(gvtRow, SSInjGvtColumnsEnum.INJ_COL_DATE_DEB);
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
                    SSExcelUtil
                        .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_NOR)
                        .setCellValue(epgNode.getNorMinistere());
                    SSExcelUtil
                        .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_OPS)
                        .setCellValue(epgNode.getOrdre());
                    SSExcelUtil.createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_A_CREER_REP).setCellValue("0");
                    SSExcelUtil.createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_A_CREER_SOLON).setCellValue("0");
                    SSExcelUtil
                        .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_A_MODIFIER_SOLON)
                        .setCellValue("0");
                    SSExcelUtil
                        .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_NOUV_ENTITE_EPP)
                        .setCellValue("0");
                    SSExcelUtil
                        .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_LIB_COURT)
                        .setCellValue(epgNode.getEdition());
                    SSExcelUtil
                        .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_LIB_LONG)
                        .setCellValue(epgNode.getLabel());
                    SSExcelUtil
                        .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_ENTETE)
                        .setCellValue(epgNode.getFormule());
                    SSExcelUtil
                        .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_CIVILITE)
                        .setCellValue(epgNode.getMembreGouvernementCivilite());
                    SSExcelUtil
                        .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_PRENOM)
                        .setCellValue(epgNode.getMembreGouvernementPrenom());
                    SSExcelUtil
                        .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_NOM)
                        .setCellValue(epgNode.getMembreGouvernementNom());
                    SSExcelUtil
                        .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_PRENOM_NOM)
                        .setCellValue(epgNode.getMembreGouvernement());
                    SSExcelUtil
                        .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_DATE_DEB)
                        .setCellValue(epgNode.getDateDebut());

                    // format date
                    SSExcelUtil
                        .getCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_DATE_DEB)
                        .setCellStyle(dateCellStyle);
                    if (minNode.getDateFin() != null) {
                        SSExcelUtil
                            .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_DATE_FIN)
                            .setCellValue(epgNode.getDateFin());
                        SSExcelUtil
                            .getCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_DATE_FIN)
                            .setCellStyle(dateCellStyle);
                    }
                } else {
                    SSExcelUtil
                        .createCell(currentMinRow, SSInjGvtColumnsEnum.INJ_COL_LIB_LONG)
                        .setCellValue("Export impossible de l'entité : " + currentMin.getId());
                }
            }

            // size des colonnes
            for (int i = 0; i < nbCol; i++) {
                sheet.getRow(0).getCell(i).setCellStyle(cellStyle);
                sheet.autoSizeColumn(i);
            }

            // récupération du fichier Excel en tant que DataSource
            fichierExcelResultat = STExcelUtil.convertExcelToDataSource(wb);
        } catch (Exception exc) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_EXCEL_TEC, exc);
        }
        return fichierExcelResultat;
    }

    public static List<InjectionGvtDTO> prepareImportGvt(final CoreSession session, final File file) {
        List<InjectionGvtDTO> listImportGvt = new ArrayList<>();
        try (HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file))) {
            // récupération de la première feuille
            HSSFSheet sheet = wb.getSheetAt(0);
            // récupération de l'itérateur
            Iterator<Row> rowIterator = sheet.iterator();
            Row currentRow;
            int nbRow = 0;
            Date dateDeb, dateFin;
            while (rowIterator.hasNext()) {
                currentRow = rowIterator.next();
                if (isRowEmpty(currentRow)) {
                    break;
                }
                if (nbRow == 0) { // entêtes
                    if (currentRow.getCell(currentRow.getFirstCellNum()).getStringCellValue().compareTo("NOR") != 0) {
                        LOGGER.error(
                            session,
                            STLogEnumImpl.FAIL_PROCESS_EXCEL_TEC,
                            "Le fichier n'est pas formaté correctement"
                        );
                        return null;
                    }
                } else if (nbRow == 1) { // gouvernement
                    dateDeb =
                        getDateValueFromCell(SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_DATE_DEB));
                    dateFin =
                        getDateValueFromCell(SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_DATE_FIN));
                    listImportGvt.add(
                        new InjectionGvtDTOImpl(
                            null,
                            false,
                            null,
                            getStringValueFromCell(
                                SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_LIB_LONG)
                            ),
                            null,
                            null,
                            null,
                            null,
                            null,
                            dateDeb,
                            dateFin,
                            true,
                            false,
                            null
                        )
                    );
                } else { // lignes d'entités
                    dateDeb =
                        getDateValueFromCell(SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_DATE_DEB));
                    dateFin =
                        getDateValueFromCell(SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_DATE_FIN));

                    listImportGvt.add(
                        new InjectionGvtDTOImpl(
                            getStringValueFromCell(SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_NOR)),
                            getStringValueFromCell(SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_OPS)),
                            getStringValueFromCell(SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_OPR)),
                            "1".equals(
                                    getStringValueFromCell(
                                        SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_A_CREER_SOLON)
                                    )
                                ),
                            "1".equals(
                                    getStringValueFromCell(
                                        SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_A_MODIFIER_SOLON)
                                    )
                                ),
                            "1".equals(
                                    getStringValueFromCell(
                                        SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_A_CREER_REP)
                                    )
                                ),
                            getStringValueFromCell(
                                SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_LIB_COURT)
                            ),
                            getStringValueFromCell(
                                SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_LIB_LONG)
                            ),
                            getStringValueFromCell(SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_ENTETE)),
                            getStringValueFromCell(
                                SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_CIVILITE)
                            ),
                            getStringValueFromCell(SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_PRENOM)),
                            getStringValueFromCell(SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_NOM)),
                            getStringValueFromCell(
                                SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_PRENOM_NOM)
                            ),
                            dateDeb,
                            dateFin,
                            getStringValueFromCell(
                                SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_NOR_EPP)
                            ),
                            "1".equals(
                                    getStringValueFromCell(
                                        SSExcelUtil.getCell(currentRow, SSInjGvtColumnsEnum.INJ_COL_NOUV_ENTITE_EPP)
                                    )
                                ),
                            false
                        )
                    );
                }
                nbRow++;
            }
        } catch (Exception exc) {
            return null;
        }
        return listImportGvt;
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        DataFormatter dataFormatter = new DataFormatter();
        return StreamSupport
            .stream(row.spliterator(), false)
            .noneMatch(c -> dataFormatter.formatCellValue(c).trim().length() > 0);
    }

    private static String getStringValueFromCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        String value = null;
        switch (cell.getCellTypeEnum()) {
            case STRING:
                {
                    value = cell.getStringCellValue();
                    break;
                }
            case NUMERIC:
                {
                    value = String.valueOf((int) cell.getNumericCellValue());
                    break;
                }
            default:
                {
                    break;
                }
        }
        return value;
    }

    private static Date getDateValueFromCell(Cell cell) {
        return cell == null ? null : DateUtil.getJavaDate(cell.getNumericCellValue());
    }

    public static DataSource exportAttenteSignature(
        List<List<String>> textesAttenteSignature,
        boolean isAutres,
        boolean isPDF
    ) {
        DataSource fichierExcelResultat = null;
        try {
            HSSFWorkbook workbook = STExcelUtil.createExcelFile(
                ATTENTE_SIGNATURE_EXPORT_SHEET_NAME,
                isAutres ? HEADER_ATTENTE_SIGNATURE_AUTRES_EXPORT : HEADER_ATTENTE_SIGNATURE_LOIS_EXPORT,
                textesAttenteSignature,
                EpgExcelUtil::addRowsForAttenteSignature
            );

            workbook
                .getSheet(ATTENTE_SIGNATURE_EXPORT_SHEET_NAME)
                .getRow(0)
                .forEach(cell -> SSExcelUtil.setBorder(cell, BorderStyle.THIN, IndexedColors.BLACK.getIndex()));

            if (isPDF) {
                formatAttenteSignaturePDF(
                    workbook,
                    ATTENTE_SIGNATURE_EXPORT_SHEET_NAME,
                    isAutres
                        ? HEADER_ATTENTE_SIGNATURE_AUTRES_EXPORT.length
                        : HEADER_ATTENTE_SIGNATURE_LOIS_EXPORT.length,
                    textesAttenteSignature.size(),
                    isAutres
                );
            }

            fichierExcelResultat = STExcelUtil.convertExcelToDataSource(workbook);
        } catch (IOException exc) {
            LOGGER.error(null, STLogEnumImpl.FAIL_CREATE_EXCEL_TEC, exc);
        }
        return fichierExcelResultat;
    }

    private static void addRowsForAttenteSignature(ImmutableTriple<HSSFSheet, List<String>, Integer> exportParams) {
        HSSFSheet sheet = exportParams.getLeft();
        List<String> texteAttenteSignature = exportParams.getMiddle();
        Integer numRow = exportParams.getRight();

        if (CollectionUtils.isNotEmpty(texteAttenteSignature)) {
            HSSFRow currentRow = sheet.createRow(numRow);
            int numColonne = 0;
            for (String value : texteAttenteSignature) {
                currentRow.createCell(numColonne++).setCellValue(value);
            }
            currentRow.forEach(cell -> SSExcelUtil.setBorder(cell, BorderStyle.THIN, IndexedColors.BLACK.getIndex()));
        }
    }

    private static void formatAttenteSignaturePDF(
        HSSFWorkbook workbook,
        String sheetName,
        int nbColonnes,
        int nbLignes,
        boolean isAutres
    ) {
        // Font et style de la ligne de titre
        short fontHeight = (short) (isAutres ? 6 : 4);
        int columnWidth = isAutres ? ATTENTE_SIGNATURE_AUTRES_COLUMN_WIDTH : ATTENTE_SIGNATURE_LOIS_COLUMN_WIDTH;
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(fontHeight);
        HSSFFont boldFont = workbook.createFont();
        boldFont.setFontHeightInPoints(fontHeight);
        boldFont.setBold(true);

        HSSFSheet sheet = workbook.getSheet(sheetName);
        for (int i = 0; i < nbColonnes; i++) {
            sheet.setColumnWidth(i, columnWidth);
            for (int j = 0; j <= nbLignes; j++) {
                HSSFRow row = sheet.getRow(j);
                HSSFCell cell = row.getCell(i);
                HSSFCellStyle cellStyle = cell.getCellStyle();
                cellStyle.setFont(j == 0 ? boldFont : font);
                cellStyle.setWrapText(true);
                cell.setCellStyle(cellStyle);
            }
        }
        for (int j = 0; j <= nbLignes; j++) {
            HSSFRow row = sheet.getRow(j);
            HSSFCell cell = row.getCell(0);
            float heightRow;
            if (j == 0) {
                heightRow = ATTENTE_SIGNATURE_HEADER_ROW_HEIGHT;
            } else {
                String texte = cell.getStringCellValue();
                heightRow = (texte.length() / ATTENTE_SIGNATURE_ROW_HEIGHT) * CHARACTER_PIXEL_MULTIPLIER;
            }
            row.setHeightInPoints(heightRow + CHARACTER_PIXEL_GAB_MULTIPLIER);
        }
    }

    private EpgExcelUtil() {
        // Private default constructor
    }
}
