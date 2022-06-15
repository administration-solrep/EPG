package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.TexteSignaleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;
import org.nuxeo.runtime.transaction.TransactionHelper;

/**
 * Batch de suppression des textes signalés
 *
 * @author asatre
 */
public class TextesSignalesBatchListener extends AbstractBatchEventListener {
    private static final String QUERY =
        "SELECT * FROM Dossier WHERE retdila:dateParutionJorf <= TIMESTAMP '%s' AND dos:dateVersementTS IS NOT NULL and ecm:isProxy = 0 ";

    private static final STLogger LOGGER = STLogFactory.getLog(TextesSignalesBatchListener.class);

    public TextesSignalesBatchListener() {
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_TEXTE_SIGNALE);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_DEL_TXT_SIGNALES_TEC);
        final long startTime = Calendar.getInstance().getTimeInMillis();
        long nbTextesRetires = 0;
        try {
            final Calendar currentDate = Calendar.getInstance();
            currentDate.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH) - 1);
            final Long timeInMillis = currentDate.getTimeInMillis();
            final String dateLiteral = DateLiteral.dateTimeFormatter.print(timeInMillis);

            final String query = String.format(QUERY, dateLiteral);

            final DocumentModelList documentList = session.query(query);

            final TexteSignaleService texteSignaleService = SolonEpgServiceLocator.getTexteSignaleService();
            Integer compteurTexte = 0;
            if (documentList != null) {
                for (final DocumentModel documentModel : documentList) {
                    final Dossier dossier = documentModel.getAdapter(Dossier.class);
                    texteSignaleService.retirer(session, dossier);
                    nbTextesRetires++;
                    compteurTexte++;
                    if (compteurTexte.equals(50)) {
                        compteurTexte = 0;
                        // remise à zéro du compteur et commit des modifications pour éviter les problèmes de rollback
                        TransactionHelper.commitOrRollbackTransaction();
                        TransactionHelper.startTransaction();
                    }
                }
                session.save();
            }
        } catch (final Exception exc) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_DEL_TXT_SIGNALES_TEC, exc);
            errorCount++;
        }
        final long endTime = Calendar.getInstance().getTimeInMillis();
        try {
            STServiceLocator
                .getSuiviBatchService()
                .createBatchResultFor(
                    batchLoggerModel,
                    "Suppression des textes signalés",
                    nbTextesRetires,
                    endTime - startTime
                );
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_DEL_TXT_SIGNALES_TEC);
    }
}
