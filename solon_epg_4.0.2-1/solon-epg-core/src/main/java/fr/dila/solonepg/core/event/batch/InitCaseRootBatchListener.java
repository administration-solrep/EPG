package fr.dila.solonepg.core.event.batch;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.cm.cases.CaseTreeHelper;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.st.api.constant.STSuiviBatchsConstants.TypeBatch;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.service.SuiviBatchService;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.event.Event;

/**
 * Batch de mis à jour du plan de classement
 *
 */
public class InitCaseRootBatchListener extends AbstractBatchEventListener {
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(InitCaseRootBatchListener.class);

    private static final int NEXT_DAYS_CHECK = 5;

    public InitCaseRootBatchListener() {
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_INIT_CASE_ROOT);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_INIT_CASE_ROOT_TEC);
        batchType = TypeBatch.TECHNIQUE;
        final long startTime = Calendar.getInstance().getTimeInMillis();
        final Calendar calendarToCheck = Calendar.getInstance();
        // Retrieve the MailRoot folder
        final DocumentModel mailRootdoc = session.getDocument(new PathRef(CaseConstants.CASE_ROOT_DOCUMENT_PATH));
        // On vérifie que les NEXT_DAYS_CHECK prochains jours ont déjà le
        // répertoire existant sinon on le créé
        for (int i = 0; i <= NEXT_DAYS_CHECK; i++) {
            // Create (or retrieve) the current MailRoot folder
            // (/case-root/YYYY/MM/DD)
            CaseTreeHelper.getOrCreateDateTreeFolder(
                session,
                mailRootdoc,
                calendarToCheck.getTime(),
                CaseConstants.CASE_TREE_TYPE
            );
            // On ajoute 1 jour à la date pour l'itération suivante
            calendarToCheck.add(Calendar.DATE, 1);
        }

        session.save();

        final long endTime = Calendar.getInstance().getTimeInMillis();
        final SuiviBatchService suiviBatchService = STServiceLocator.getSuiviBatchService();
        suiviBatchService.createBatchResultFor(
            batchLoggerModel,
            "Mise à jour du plan de classement",
            endTime - startTime
        );
        LOGGER.info(session, EpgLogEnumImpl.END_B_INIT_CASE_ROOT_TEC);
    }
}
