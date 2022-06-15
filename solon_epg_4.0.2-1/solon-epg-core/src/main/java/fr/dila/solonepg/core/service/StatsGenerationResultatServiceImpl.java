package fr.dila.solonepg.core.service;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.birt.BirtResultatFichier;
import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.service.StatsGenerationResultatService;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.event.batch.BatchLoggerModel;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.FileUtils;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.convert.api.ConversionException;

public class StatsGenerationResultatServiceImpl implements StatsGenerationResultatService {
    private static final long serialVersionUID = -3445703103388008293L;

    private static final STLogger LOGGER = STLogFactory.getLog(StatsGenerationResultatServiceImpl.class);

    private BatchLoggerModel batchLoggerModel;

    @Override
    public void generateAllReportResult(
        final CoreSession session,
        final String reportName,
        final String reportFile,
        final Map<String, Serializable> inputValues,
        final ANReportEnum report,
        final BatchLoggerModel batchLoggerModel,
        boolean curLegis
    ) {
        try {
            this.batchLoggerModel = batchLoggerModel;
            DocumentModel birtResultatFichier = getBirtResultatFichier(session, reportName);
            List<BirtOutputFormat> outPutFormats = new ArrayList<>();
            outPutFormats.add(BirtOutputFormat.HTML);
            outPutFormats.add(BirtOutputFormat.PDF);
            outPutFormats.add(BirtOutputFormat.XLS);
            // Pour les besoins débug génération rapport birt
            //     LOGGER.info(session, STLogEnumImpl.LOG_INFO_TEC, "****************NAME REPORT : " + reportName);
            Map<BirtOutputFormat, Blob> exportsContent = SSServiceLocator
                .getSSBirtService()
                .generateReportResults(reportName, reportFile, inputValues, outPutFormats);
            final Blob blobHtml = exportsContent.get(BirtOutputFormat.HTML);
            final Blob blobPdf = exportsContent.get(BirtOutputFormat.PDF);
            final Blob blobXls = exportsContent.get(BirtOutputFormat.XLS);

            if (birtResultatFichier == null) {
                LOGGER.info(
                    session,
                    SSLogEnumImpl.BIRT_BATCH_TEC,
                    "\t\tCREATING BirtResultatFichier - " + report.name() + " - " + reportName + " - " + reportFile
                );
                birtResultatFichier =
                    session.createDocumentModel(
                        ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_PATH,
                        reportName,
                        ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_TYPE
                    );

                // set document file properties
                birtResultatFichier.setProperty(
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_FILE_NAME_PROPERTY,
                    reportName
                );
                birtResultatFichier.setProperty(
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PROPERTY,
                    blobHtml
                );
                birtResultatFichier.setProperty(
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_CSV_PROPERTY,
                    blobXls
                );
                birtResultatFichier.setProperty(
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PDF_PROPERTY,
                    blobPdf
                );
                birtResultatFichier.setProperty(
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_MODIFIED,
                    Calendar.getInstance()
                );

                // set entite
                // creation du document en session
                session.createDocument(birtResultatFichier);
            } else {
                LOGGER.info(
                    session,
                    SSLogEnumImpl.BIRT_BATCH_TEC,
                    "\t\tUPDATING BirtResultatFichier #" +
                    birtResultatFichier.getId() +
                    " - " +
                    report.name() +
                    " - " +
                    reportName +
                    " - " +
                    reportFile
                );
                birtResultatFichier.setProperty(
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PROPERTY,
                    blobHtml
                );
                birtResultatFichier.setProperty(
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_CSV_PROPERTY,
                    blobXls
                );
                birtResultatFichier.setProperty(
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_CONTENT_PDF_PROPERTY,
                    blobPdf
                );
                birtResultatFichier.setProperty(
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_SCHEMA,
                    ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_MODIFIED,
                    Calendar.getInstance()
                );

                session.saveDocument(birtResultatFichier);
            }
            session.save();
            // publier les rapports
            if (report.isBatchPubliable()) {
                publierReportResulat(reportName, blobHtml, curLegis, session);
            }
        } catch (final ConversionException exc) {
            LOGGER.warn(session, SSLogEnumImpl.FAIL_CREATE_BIRT_TEC, exc);
        } catch (final Exception e) {
            throw new NuxeoException("error while generating stat report birt", e);
        }
    }

    @Override
    public BirtResultatFichier saveReportResulat(
        final CoreSession session,
        final String reportName,
        final Blob blobHtml,
        final Blob blobPdf,
        final Blob blobXls
    ) {
        try {
            DocumentModel birtResultatFichierDoc = getBirtResultatFichier(session, reportName);
            if (birtResultatFichierDoc == null) {
                birtResultatFichierDoc =
                    session.createDocumentModel(
                        ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_PATH,
                        reportName,
                        ActiviteNormativeStatsConstants.BIRT_RESULTAT_FICHIER_TYPE
                    );
                session.createDocument(birtResultatFichierDoc);
            }
            BirtResultatFichier birtResultatFichier = birtResultatFichierDoc.getAdapter(BirtResultatFichier.class);
            birtResultatFichier.setReportName(reportName);
            birtResultatFichier.setHtmlContent(blobHtml);
            birtResultatFichier.setXlsContent(blobXls);
            birtResultatFichier.setPdfContent(blobPdf);
            birtResultatFichier.setDateModified(Calendar.getInstance());
            session.saveDocument(birtResultatFichierDoc);
            return birtResultatFichier;
        } catch (final ConversionException exc) {
            LOGGER.warn(session, SSLogEnumImpl.FAIL_CREATE_BIRT_TEC, exc);
            throw new NuxeoException("Error while generating stat report birt", exc);
        } catch (final Exception e) {
            throw new NuxeoException("Error while generating stat report birt", e);
        }
    }

    @Override
    public void publierReportResulat(
        String reportName,
        String idMinistere,
        String nor,
        final Blob blobHtml,
        boolean curLegis,
        CoreSession session
    ) {
        if (idMinistere != null) {
            reportName += "-" + idMinistere;
        } else if (nor != null) {
            reportName += "-" + nor;
        }

        publierReportResulat(reportName, blobHtml, curLegis, session);
        session.save();
    }

    @Override
    public void publierReportResulat(
        final String reportName,
        final Blob blobHtml,
        boolean curLegis,
        CoreSession session
    ) {
        final String generatedReportPath;
        if (curLegis) {
            generatedReportPath = SolonEpgServiceLocator.getActiviteNormativeService().getPathDirANStatistiquesPublie();
        } else {
            generatedReportPath =
                SolonEpgServiceLocator.getActiviteNormativeService().getPathDirANStatistiquesLegisPrecPublie(session);
        }

        publierReportResulat(reportName, blobHtml, generatedReportPath);
        session.save();
    }

    @Override
    public void publierReportResulat(final String reportName, final Blob blobHtml, final String path) {
        publierReportResulat(reportName, blobHtml, path, "html");
    }

    @Override
    public void publierReportResulat(
        final String reportName,
        final Blob blob,
        final String path,
        final String extension
    ) {
        final long startTime = Calendar.getInstance().getTimeInMillis();
        final String pathName = path + File.separator + FileUtils.sanitizePathTraversal(reportName + "." + extension);
        try {
            if (blob != null) {
                LOGGER.info(SSLogEnumImpl.CREATE_BIRT_TEC, pathName);
                final File report = new File(pathName);
                if (!report.exists()) {
                    report.createNewFile();
                }
                try (FileOutputStream outputStream = new FileOutputStream(report)) {
                    final byte[] bytes = blob.getByteArray();
                    outputStream.write(bytes);
                }
            }
        } catch (final Exception exc1) {
            LOGGER.error(SSLogEnumImpl.FAIL_PUBLISH_BIRT_TEC, exc1);
            final long endTime = Calendar.getInstance().getTimeInMillis();
            if (batchLoggerModel != null) {
                try {
                    STServiceLocator
                        .getSuiviBatchService()
                        .createBatchResultFor(
                            batchLoggerModel,
                            "Erreur lors de la génération d'un rapport " + pathName,
                            endTime - startTime
                        );
                } catch (Exception exc) {
                    LOGGER.error(STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, exc);
                    throw new NuxeoException("error while saving report birt to " + extension, exc);
                }
            }
            throw new NuxeoException("error while saving report birt to " + extension, exc1);
        }

        if (batchLoggerModel != null) {
            final long endTimeOk = Calendar.getInstance().getTimeInMillis();
            try {
                STServiceLocator
                    .getSuiviBatchService()
                    .createBatchResultFor(
                        batchLoggerModel,
                        "Génération d'un rapport" + pathName,
                        endTimeOk - startTime
                    );
            } catch (Exception exc) {
                LOGGER.error(STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, exc);
            }
        }
    }

    @Override
    public DocumentModel getBirtResultatFichier(final CoreSession session, final String reportName) {
        DocumentModel birtResultatFichier = null;
        final String query =
            "SELECT * FROM BirtResultatFichier WHERE ecm:name ='" + reportName + "' and ecm:isProxy = 0";
        final DocumentModelList list = session.query(query);
        if (CollectionUtils.isNotEmpty(list)) {
            birtResultatFichier = list.get(0);
        }
        return birtResultatFichier;
    }

    @Override
    public Blob generateReportResult(
        final String reportName,
        final String reportFile,
        final Map<String, Serializable> inputValues,
        final BirtOutputFormat outPutFormat
    ) {
        try {
            return SSServiceLocator
                .getSSBirtService()
                .generateReportResults(reportName, reportFile, inputValues, outPutFormat);
        } catch (final Exception e) {
            throw new NuxeoException("error while generating stat report birt", e);
        }
    }

    @Override
    public String getMinisteresListBirtReportParam(CoreSession session) {
        StringBuilder ministereParm = new StringBuilder();
        String query = "SELECT distinct MINISTEREPILOTE from texte_maitre where MINISTEREPILOTE is not null";
        String returnType = TexteMaitreConstants.TEXTE_MAITRE_PREFIX + ":" + TexteMaitreConstants.MINISTERE_PILOTE;

        try (
            IterableQueryResult res = QueryUtils.doSqlQuery(
                session,
                new String[] { returnType },
                query,
                new String[] {}
            )
        ) {
            Iterator<Map<String, Serializable>> iterator = res.iterator();
            // Collections.sort(ministereList, new ProtocolarOrderComparator());
            // Récupération de la liste des ministères

            while (iterator.hasNext()) {
                Map<String, Serializable> row = iterator.next();
                String minId = (String) row.get(returnType);

                final EntiteNode entiteNode = STServiceLocator.getSTMinisteresService().getEntiteNode(minId);
                if (entiteNode != null) {
                    ministereParm
                        .append("$id$=")
                        .append(entiteNode.getId())
                        .append(";;$label$=")
                        .append(entiteNode.getLabel())
                        .append(";;$ordre$=")
                        .append(entiteNode.getOrdre())
                        .append(";;$formule$=")
                        .append(entiteNode.getFormule())
                        .append(";;&");
                }
            }
        } catch (NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_EXEC_SQL, e);
        }

        return ministereParm.toString();
    }

    @Override
    public String getDirectionsListBirtReportParam() {
        StringBuilder directionParm = new StringBuilder();
        final List<OrganigrammeNode> directionsList = SolonEpgServiceLocator
            .getEpgOrganigrammeService()
            .getAllDirectionList();
        // Récupération de la liste des directions
        for (final OrganigrammeNode node : directionsList) {
            final UniteStructurelleNode uniteStructurelleNode = (UniteStructurelleNode) node;
            directionParm
                .append("$id$=")
                .append(uniteStructurelleNode.getId())
                .append(";;$label$=")
                .append(uniteStructurelleNode.getLabel())
                .append(";;&");
        }
        return directionParm.toString();
    }

    @Override
    public String getMinisteresCourantListBirtReportParam() {
        StringBuilder ministereParm = new StringBuilder();
        // Récupération de la liste des ministères
        final List<EntiteNode> ministereList = STServiceLocator.getSTMinisteresService().getCurrentMinisteres();

        for (final OrganigrammeNode node : ministereList) {
            final EntiteNode entiteNode = (EntiteNode) node;
            ministereParm
                .append("$id$=")
                .append(entiteNode.getId())
                .append(";;$label$=")
                .append(entiteNode.getLabel())
                .append(";;$ordre$=")
                .append(entiteNode.getOrdre())
                .append(";;$formule$=")
                .append(entiteNode.getFormule())
                .append(";;&");
        }
        return ministereParm.toString();
    }
}
