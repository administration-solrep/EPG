package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STParametreService;
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

public class DossierAbandonListener extends AbstractBatchEventListener {
    private static final STLogger LOGGER = STLogFactory.getLog(DossierAbandonListener.class);

    public DossierAbandonListener() {
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_DOSSIER_ABANDON);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_ABANDON_TEC);
        final long startTime = Calendar.getInstance().getTimeInMillis();
        long nbDossierAbandon = 0;
        try {
            final Calendar dua = Calendar.getInstance();
            final STParametreService paramService = STServiceLocator.getSTParametreService();
            final String duaDelai = paramService.getParametreValue(session, STParametreConstant.DELAI_ABANDON_DOSSIERS);
            dua.add(Calendar.DAY_OF_YEAR, -Integer.parseInt(duaDelai) + 1);
            final String literalDate = DateLiteral.dateFormatter.print(dua.getTimeInMillis());

            final String query =
                "SELECT * FROM Dossier WHERE ecm:currentLifeCycleState = 'running' and dos:" +
                DossierSolonEpgConstants.DOSSIER_CANDIDAT_PROPERTY +
                " = '" +
                VocabularyConstants.CANDIDAT_ABANDON +
                "' and dos:" +
                DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY +
                " = '" +
                VocabularyConstants.STATUT_LANCE +
                "' and  dos:" +
                DossierSolonEpgConstants.DOSSIER_DATE_CANDIDATURE_PROPERTY +
                " < TIMESTAMP '" +
                literalDate +
                " ' and dos:" +
                DossierSolonEpgConstants.DOSSIER_ID_DOSSIER_PROPERTY +
                " IS NULL and ecm:isProxy = 0 ";

            DocumentModelList dossierList = null;
            dossierList = session.query(query);
            final DossierService dossierService = SolonEpgServiceLocator.getDossierService();

            Integer compteurDossier = 0;
            for (final DocumentModel dossierDoc : dossierList) {
                dossierService.abandonDossier(session, dossierDoc);
                nbDossierAbandon++;
                compteurDossier++;
                if (compteurDossier.equals(50)) {
                    // Remise à zéro du compteur et commit pour éviter les problèmes de rollback
                    TransactionHelper.commitOrRollbackTransaction();
                    TransactionHelper.startTransaction();
                    compteurDossier = 0;
                }
            }
            session.save();
        } catch (final Exception exc) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_ABANDON_TEC, exc);
            errorCount++;
        }
        final long endTime = Calendar.getInstance().getTimeInMillis();
        try {
            STServiceLocator
                .getSuiviBatchService()
                .createBatchResultFor(batchLoggerModel, "Abandon de dossiers", nbDossierAbandon, endTime - startTime);
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_ABANDON_TEC);
    }
}
