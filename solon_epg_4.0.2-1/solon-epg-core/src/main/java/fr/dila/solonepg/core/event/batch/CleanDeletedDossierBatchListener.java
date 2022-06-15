package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.event.AbstractBatchPostCommitEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.runtime.transaction.TransactionHelper;

/**
 * Batch de suppression des dossiers avec deleted=1
 *
 * @author asatre
 *
 */
public class CleanDeletedDossierBatchListener extends AbstractBatchPostCommitEventListener {
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(CleanDeletedDossierBatchListener.class);

    @Override
    protected void handleEvent(Event event, CoreSession session) {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_DEL_DOS_TEC);
        try {
            StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
            queryBuilder.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
            queryBuilder.append(" as d WHERE d.");
            queryBuilder.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
            queryBuilder.append(":");
            queryBuilder.append(DossierSolonEpgConstants.DELETED);
            queryBuilder.append(" = 1 ");

            DocumentRef[] docRefs = QueryUtils.doUFNXQLQueryForIds(session, queryBuilder.toString(), null);
            LOGGER.info(session, STLogEnumImpl.DEL_DOSSIER_TEC, docRefs);
            Integer compteurDocument = 0;
            DossierService dossierService = SolonEpgServiceLocator.getDossierService();
            JournalService journalService = STServiceLocator.getJournalService();
            for (DocumentRef docRef : docRefs) {
                String nor = null;
                String id = null;
                // On cherche à récupérer le nor du dossier pour le stocker dans la table de correspondance id/nor pour
                // les dossiers supprimés
                DocumentModel doc = session.getDocument(docRef);
                if (doc != null) {
                    id = doc.getRef().toString();
                    Dossier dossier = doc.getAdapter(Dossier.class);
                    if (doc != null) {
                        nor = dossier.getNumeroNor();
                    }
                }
                session.removeDocument(docRef);
                if (nor != null && id != null) {
                    // On vérifie si notre dossier existe avant d'essayer de le sauvegarder
                    boolean found = false;
                    try {
                        List<String> lstIds = dossierService.getIdsDossiersSupprimes(nor);
                        found = CollectionUtils.isNotEmpty(lstIds) && lstIds.contains(id);
                    } catch (Exception e) {
                        LOGGER.error(STLogEnumImpl.DEL_DOSSIER_TEC, e);
                    }

                    if (!found) {
                        try {
                            dossierService.saveNorIdDossierSupprime(nor, id);
                        } catch (NuxeoException e) {
                            LOGGER.error(STLogEnumImpl.DEL_DOSSIER_TEC, e);
                        }
                    }
                    // Inscription dans le journal technique
                    journalService.journaliserActionAdministration(
                        session,
                        id,
                        SolonEpgEventConstant.DOSSIER_SUPPRIME_EVENT,
                        SolonEpgEventConstant.STATUT_DOSSIER_SUPPRIME_COMMENT
                    );
                }
                dossierService.removeDossierReferences(session, id);

                compteurDocument = compteurDocument + 1;
                if (compteurDocument.equals(50)) {
                    // Remise à zéro du compteur et commit pour éviter les erreurs de rollback
                    compteurDocument = 0;
                    TransactionHelper.commitOrRollbackTransaction();
                    TransactionHelper.startTransaction();
                }
            }
        } catch (Exception exc) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_DEL_DOS_TEC, exc);
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_DEL_DOS_TEC);
    }

    @Override
    protected boolean accept(Event event) {
        return true;
    }
}
