package fr.dila.solonepg.ui.services.pan.impl;

import static fr.dila.st.core.util.ObjectHelper.requireNonNullElse;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.cases.typescomplexe.LigneProgrammationHabilitation;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.core.dto.activitenormative.LigneProgrammationHabilitationDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.TableauProgrammationHabilitationDTO;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.TableauProgrammationCUIService;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Bean Seam de gestion du tableau de programmation et de suivi du suivi des
 * habilitations de l'activite normative
 *
 * @author asatre
 */
public class TableauProgrammation38CUIServiceImpl implements TableauProgrammationCUIService {
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(TableauProgrammation38CUIServiceImpl.class);

    @Override
    public List<LigneProgrammationHabilitationDTO> getCurrentListProgrammationHabilitation(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        DocumentModel currentDocument = context.getCurrentDocument();
        TableauProgrammationHabilitationDTO tableauDeProgrammationHabilitationDTO;
        ActiviteNormativeProgrammation activiteNormativeProgrammation = currentDocument.getAdapter(
            ActiviteNormativeProgrammation.class
        );
        // si le tableau est défigé, on le vide et on le recalcule
        if (StringUtils.isEmpty(activiteNormativeProgrammation.getLockHabUser())) {
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .removeCurrentProgrammationHabilitation(
                    currentDocument.getAdapter(ActiviteNormative.class),
                    documentManager
                );
            tableauDeProgrammationHabilitationDTO =
                new TableauProgrammationHabilitationDTO(
                    activiteNormativeProgrammation,
                    documentManager,
                    Boolean.TRUE,
                    Boolean.FALSE
                );

            // on save le tableau à exporter
            List<LigneProgrammationHabilitation> lignesProgrammations = tableauDeProgrammationHabilitationDTO.remapField(
                new LinkedList<LigneProgrammationHabilitation>()
            );
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .saveCurrentProgrammationHabilitation(
                    lignesProgrammations,
                    currentDocument.getAdapter(ActiviteNormative.class),
                    documentManager
                );
        } else {
            // le tableau figé
            tableauDeProgrammationHabilitationDTO =
                new TableauProgrammationHabilitationDTO(
                    activiteNormativeProgrammation,
                    documentManager,
                    Boolean.TRUE,
                    Boolean.FALSE
                );
        }
        return tableauDeProgrammationHabilitationDTO.getListe();
    }

    @Override
    public List<LigneProgrammationHabilitationDTO> getCurrentListSuiviHabilitation(SpecificContext context) {
        CoreSession documentManager = context.getSession();

        ActiviteNormativeProgrammation activiteNormativeProgrammation = context
            .getCurrentDocument()
            .getAdapter(ActiviteNormativeProgrammation.class);
        TableauProgrammationHabilitationDTO tableauDeProgrammationHabilitationDTO = new TableauProgrammationHabilitationDTO(
            activiteNormativeProgrammation,
            documentManager,
            Boolean.FALSE,
            Boolean.FALSE
        );
        return tableauDeProgrammationHabilitationDTO.getListe();
    }

    @Override
    public String lockCurrentProgrammationHabilitation(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel activiteNormativeDoc = context.getCurrentDocument();
        TableauProgrammationHabilitationDTO tableauDeProgrammationHabilitationDTO = context.getFromContextData(
            PanContextDataKey.TABLEAU_PROGRAMMATION_DTO
        );

        if (tableauDeProgrammationHabilitationDTO != null) {
            List<LigneProgrammationHabilitation> lignesProgrammations = tableauDeProgrammationHabilitationDTO.remapField(
                new LinkedList<LigneProgrammationHabilitation>()
            );
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .saveCurrentProgrammationHabilitation(
                    lignesProgrammations,
                    activiteNormativeDoc.getAdapter(ActiviteNormative.class),
                    session
                );
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .lockCurrentProgrammationHabilitation(
                    activiteNormativeDoc.getAdapter(ActiviteNormative.class),
                    session
                );
        }
        return null;
    }

    @Override
    public String unlockCurrentProgrammationHabilitation(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel activiteNormativeDoc = context.getCurrentDocument();
        TableauProgrammationHabilitationDTO tableauDeProgrammationHabilitationDTO = context.getFromContextData(
            PanContextDataKey.TABLEAU_PROGRAMMATION_DTO
        );

        if (tableauDeProgrammationHabilitationDTO != null) {
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .removeCurrentProgrammationHabilitation(
                    activiteNormativeDoc.getAdapter(ActiviteNormative.class),
                    session
                );
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .unlockCurrentProgrammationHabilitation(
                    activiteNormativeDoc.getAdapter(ActiviteNormative.class),
                    session
                );
        }

        return null;
    }

    @Override
    public String getTableauProgrammationLockInfo(SpecificContext context) {
        ActiviteNormativeProgrammation activiteNormativeProgrammation = context
            .getCurrentDocument()
            .getAdapter(ActiviteNormativeProgrammation.class);
        if (StringUtils.isNotEmpty(activiteNormativeProgrammation.getLockHabUser())) {
            return (
                "Figé le " +
                SolonDateConverter.getClientConverter().format(activiteNormativeProgrammation.getLockHabDate()) +
                ", par " +
                activiteNormativeProgrammation.getLockHabUser()
            );
        }
        return "";
    }

    @Override
    public Boolean isCurrentProgrammationHabilitationLocked(SpecificContext context) {
        ActiviteNormativeProgrammation activiteNormativeProgrammation = context
            .getCurrentDocument()
            .getAdapter(ActiviteNormativeProgrammation.class);
        return StringUtils.isEmpty(activiteNormativeProgrammation.getLockHabUser());
    }

    @Override
    public String publierTableauSuiviHabilitation(SpecificContext context) {
        TableauProgrammationHabilitationDTO tableauDeProgrammationHabilitationDTO = context.getFromContextData(
            PanContextDataKey.TABLEAU_PROGRAMMATION_DTO
        );
        CoreSession documentManager = context.getSession();
        DocumentModel currentDocument = context.getCurrentDocument();

        if (tableauDeProgrammationHabilitationDTO != null) {
            List<LigneProgrammationHabilitation> lignesProgrammations = tableauDeProgrammationHabilitationDTO.remapField(
                new LinkedList<LigneProgrammationHabilitation>()
            );
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .publierTableauSuiviHab(
                    lignesProgrammations,
                    currentDocument.getAdapter(ActiviteNormative.class),
                    documentManager
                );

            String loiNb = currentDocument.getAdapter(TexteMaitre.class).getNumeroNor();
            try {
                String generatedReportPath = SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .getPathDirANStatistiquesPublie();
                SimpleDateFormat sdf = SolonDateConverter.DATE_SLASH.simpleDateFormat();
                SimpleDateFormat mySdf = SolonDateConverter.MONTH_YEAR.simpleDateFormat();

                File htmlReport = new File(generatedReportPath + "/" + "tableauDeSuiviHab-" + loiNb + ".html");
                if (!htmlReport.exists()) {
                    htmlReport.createNewFile();
                }
                FileOutputStream outputStream = new FileOutputStream(htmlReport);
                writeTableauHeader(outputStream);

                writeTableauBody(tableauDeProgrammationHabilitationDTO, sdf, mySdf, outputStream);
                outputStream.close();
            } catch (IOException ioe) {
                LOGGER.error(documentManager, EpgLogEnumImpl.FAIL_PUBLIER_TABLEAU_SUIVI_HABILITATION_TEC, ioe);
            }
        }

        return null;
    }

    private void writeTableauBody(
        TableauProgrammationHabilitationDTO tableauDeProgrammationHabilitationDTO,
        SimpleDateFormat sdf,
        SimpleDateFormat mySdf,
        FileOutputStream outputStream
    )
        throws IOException {
        outputStream.write("<tbody>".getBytes());

        for (LigneProgrammationHabilitationDTO lp : (List<LigneProgrammationHabilitationDTO>) tableauDeProgrammationHabilitationDTO.getListe()) {
            writeTableauRow(sdf, mySdf, outputStream, lp);
        }
        outputStream.write(("</tbody></table>").getBytes());
    }

    private void writeTableauCell(FileOutputStream outputStream, String tagStart, String content, String tagEnd)
        throws IOException {
        outputStream.write((tagStart + content + tagEnd).getBytes());
    }

    private void writeTableauCell(FileOutputStream outputStream, String content) throws IOException {
        writeTableauCell(outputStream, "<td>", content, "</td>");
    }

    private void writeTableauHeaderCell(FileOutputStream outputStream, String key) throws IOException {
        writeTableauCell(outputStream, "<th>", ResourceHelper.getString(key), "</th>");
    }

    private void writeTableauRow(
        SimpleDateFormat sdf,
        SimpleDateFormat mySdf,
        FileOutputStream outputStream,
        LigneProgrammationHabilitationDTO lp
    )
        throws IOException {
        outputStream.write(("<tr>").getBytes());

        writeTableauCell(outputStream, requireNonNullElse(lp.getNumeroOrdre(), ""));
        writeTableauCell(outputStream, requireNonNullElse(lp.getArticle(), ""));
        writeTableauCell(outputStream, requireNonNullElse(lp.getObjetRIM(), ""));
        writeTableauCell(outputStream, requireNonNullElse(lp.getMinisterePilote(), ""));
        writeTableauCell(outputStream, requireNonNullElse(lp.getConvention(), ""));
        writeTableauCell(outputStream, lp.getDateTerme() != null ? sdf.format(lp.getDateTerme()) : "");
        writeTableauCell(outputStream, requireNonNullElse(lp.getConventionDepot(), ""));
        writeTableauCell(
            outputStream,
            lp.getDatePrevisionnelleSaisineCE() != null ? mySdf.format(lp.getDatePrevisionnelleSaisineCE()) : ""
        );
        writeTableauCell(
            outputStream,
            lp.getDatePrevisionnelleCM() != null ? mySdf.format(lp.getDatePrevisionnelleCM()) : ""
        );
        writeTableauCell(outputStream, requireNonNullElse(lp.getObservation(), ""));
        writeTableauCell(outputStream, requireNonNullElse(lp.getLegislature(), ""));
        writeTableauCell(outputStream, requireNonNullElse(lp.getNumeroNorOrdo(), ""));
        writeTableauCell(outputStream, requireNonNullElse(lp.getObjetOrdo(), ""));
        writeTableauCell(outputStream, lp.getDateSaisineCEOrdo() != null ? sdf.format(lp.getDateSaisineCEOrdo()) : "");
        writeTableauCell(outputStream, lp.getDatePassageCMOrdo() != null ? sdf.format(lp.getDatePassageCMOrdo()) : "");
        writeTableauCell(
            outputStream,
            lp.getDatePublicationOrdo() != null ? sdf.format(lp.getDatePublicationOrdo()) : ""
        );
        writeTableauCell(outputStream, requireNonNullElse(lp.getTitreOfficielOrdo(), ""));
        writeTableauCell(outputStream, requireNonNullElse(lp.getNumeroOrdo(), ""));
        writeTableauCell(outputStream, requireNonNullElse(lp.getConventionDepotOrdo(), ""));
        writeTableauCell(
            outputStream,
            lp.getDateLimiteDepotOrdo() != null ? sdf.format(lp.getDateLimiteDepotOrdo()) : ""
        );
        outputStream.write(("</tr>").getBytes());
    }

    private void writeTableauHeader(FileOutputStream outputStream) throws IOException {
        outputStream.write(
            "<style>.suiviTbl{font: normal 12px \"Lucida Grande\", sans-serif;border-collapse:collapse;width:2000px}\n.suiviTbl td,.suiviTbl th{border:thin solid black;}</style>".getBytes()
        );
        outputStream.write(
            (
                "<table class='suiviTbl' cellpadding='2' cellspacing='0'>" +
                "<colgroup><col style='width:20px'/><col style='width:100px'/><col style='width:120px'/><col style='width:250px'/><col style='width:100px'/>" +
                "<col style='width:100px'/><col style='width:100px'/><col style='width:100px'/><col style='width:80px'/><col style='width:80px'/>" +
                "<col style='width:80px'/><col style='width:80px'/><col style='width:80px'/><col style='width:300px'/><col style='width:100px'/>" +
                "<col style='width:100px'/><col style='width:120px'/><col style='width:80px'/></colgroup>" +
                "<thead><tr>"
            ).getBytes()
        );
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.numeroOrdre");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.article");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.objetRIM");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.ministerePilote");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.convention");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.dateTerme");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.conventionDepot");
        writeTableauHeaderCell(
            outputStream,
            "activite.normative.tableau.programmation.hab.datePrevisionnelleSaisineCE"
        );
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.datePrevisionnelleCM");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.observation");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.legislature");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.numeroNorOrdo");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.objetOrdo");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.dateSaisineCEOrdo");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.datePassageCMOrdo");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.datePublicationOrdo");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.titreOfficielOrdo");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.numeroOrdo");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.conventionDepotOrdo");
        writeTableauHeaderCell(outputStream, "activite.normative.tableau.programmation.hab.dateLimiteDepotOrdo");
        outputStream.write("</tr></thead>".getBytes());
    }

    @Override
    public String getTableauSuiviPublicationInfo(SpecificContext context) {
        ActiviteNormativeProgrammation activiteNormativeProgrammation = context
            .getCurrentDocument()
            .getAdapter(ActiviteNormativeProgrammation.class);
        if (StringUtils.isNotEmpty(activiteNormativeProgrammation.getTableauSuiviHabPublicationUser())) {
            return ResourceHelper.getString(
                "pan.programmation.38c.publicationInfo",
                SolonDateConverter
                    .getClientConverter()
                    .format(activiteNormativeProgrammation.getTableauSuiviHabPublicationDate()),
                activiteNormativeProgrammation.getTableauSuiviHabPublicationUser()
            );
        }
        return "";
    }
}
