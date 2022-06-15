package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.birt.ExportPANStat;
import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.core.service.ActiviteNormativeReportsGenerator;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;

public class DeleteExportPanStatsBatchListener extends AbstractBatchEventListener {
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(DeleteExportPanStatsBatchListener.class);

    private static final String QUERY =
        "Select e.ecm:uuid as id from ExportPANStat as e WHERE e.expanstat:dateRequest <= TIMESTAMP '%s' ";

    public DeleteExportPanStatsBatchListener() {
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_DELETE_EXPORT_PAN_STATS_EVENT);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_DEL_EXPORT_PAN_STATS_TEC);

        final long startTime = Calendar.getInstance().getTimeInMillis();
        long nbSuppression = 0;
        try {
            final Calendar currentDate = Calendar.getInstance();
            currentDate.add(Calendar.HOUR, -24);
            final Long timeInMillis = currentDate.getTimeInMillis();
            final String dateLiteral = DateLiteral.dateTimeFormatter.print(timeInMillis);

            final String query = String.format(QUERY, dateLiteral);

            List<DocumentModel> exportsTargets = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
                session,
                ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_TYPE,
                query,
                new Object[] {}
            );
            List<DocumentRef> refsToDelete = new ArrayList<DocumentRef>();
            // Remove Main directory contains the zip file
            if (exportsTargets != null && !exportsTargets.isEmpty()) {
                ParametrageAN param = SolonEpgServiceLocator.getSolonEpgParametreService().getDocAnParametre(session);
                int dernierElem = param.getLegislatures().indexOf(param.getLegislatureEnCours()) - 1;
                String labelLegisPrec = param.getLegislatures().get(dernierElem);

                for (DocumentModel exportDoc : exportsTargets) {
                    ExportPANStat export = exportDoc.getAdapter(ExportPANStat.class);
                    String legislatures = PropertyUtil.getStringProperty(
                        exportDoc,
                        ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_SCHEMA,
                        ActiviteNormativeStatsConstants.EXPORT_PAN_STAT_LEGISLATURE_PROPERTY
                    );
                    boolean currentLegislature = true;
                    if (labelLegisPrec != null && labelLegisPrec.equals(legislatures)) {
                        currentLegislature = false;
                    }
                    String folderPath = ActiviteNormativeReportsGenerator.getFilePath(
                        currentLegislature,
                        export.getOwner(),
                        export.getName()
                    );
                    ActiviteNormativeReportsGenerator.removeFolderAndZip(folderPath);
                    refsToDelete.add(exportDoc.getRef());
                }
            }

            LOGGER.info(session, EpgLogEnumImpl.DEL_EXPORT_PAN_STATS_TEC, refsToDelete);

            session.removeDocuments(refsToDelete.toArray(new DocumentRef[refsToDelete.size()]));
            session.save();
        } catch (Exception exc) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_PAN_STATS_TEC, exc);
            errorCount++;
        }

        final long endTime = Calendar.getInstance().getTimeInMillis();
        try {
            STServiceLocator
                .getSuiviBatchService()
                .createBatchResultFor(
                    batchLoggerModel,
                    "Suppression des Statistiques PAN exportées",
                    nbSuppression,
                    endTime - startTime
                );
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_DEL_EXPORT_PAN_STATS_TEC);
    }
}
