package fr.dila.solonepg.core.operation.livraison;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.operation.STVersion;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.work.SolonWork;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections4.ListUtils;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.work.api.WorkManager;

@STVersion(version = "4.0.0")
@Operation(
    id = EpgEditRepEpreuveDossierOperation.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "Edite un paramètre du répertoire Epreuve",
    description = "Edite le nombre max de doc accepté dans le répertoire Epreuve des dossiers du stock"
)
public class EpgEditRepEpreuveDossierOperation {
    public static final String ID = "SolonEpg.Edit.Rep.Epreuve";

    private static final STLogger LOG = STLogFactory.getLog(EpgEditRepEpreuveDossierOperation.class);

    private static final int LIMITE_NB_DOC_TRAITEMENT = 50;

    @Context
    private CoreSession session;

    private static final String QUERY =
        "Select pf.ecm:uuid as id " +
        "from ParapheurFolder as pf, Parapheur as p, Dossier as d " +
        "where pf.ecm:parentId = p.ecm:uuid and p.ecm:parentId = d.ecm:uuid " +
        "and d.dos:statut in ('1', '2') and pf.dc:title = 'Epreuve' and pf.pf:nbDocAccepteMax = 1";

    @SuppressWarnings("squid:S00112")
    @OperationMethod
    public void run() {
        LOG.info(STLogEnumImpl.DEFAULT, "Début de l'opération d'édition du rep Epreuve des dossier du stock");

        Instant begin = Instant.now();

        List<String> idList = QueryUtils.doUFNXQLQueryForIdsList(session, QUERY, new Object[] {});
        LOG.info(STLogEnumImpl.DEFAULT, idList.size() + " ParapheurFolder à traiter");

        WorkManager wm = ServiceUtil.getRequiredService(WorkManager.class);
        // Split de la list d'id pour traiter par lot de 50 doc
        ListUtils
            .partition(idList, LIMITE_NB_DOC_TRAITEMENT)
            .stream()
            .map(ParapheurFolderMaxDocWork::new)
            .forEach(wm::schedule);

        try {
            wm.awaitCompletion("ParapheurFolderMaxDocWorkQueue", 5, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            // reset interrupted status
            Thread.currentThread().interrupt();
            // continue interrupt
            throw new RuntimeException(e);
        }

        LOG.info(STLogEnumImpl.DEFAULT, "Durée de l'opération '" + ID + "' : " + DateUtil.getDuration(begin));

        LOG.info(STLogEnumImpl.DEFAULT, "Fin de l'opération d'édition du rep Epreuve des dossier du stock");
    }

    private static class ParapheurFolderMaxDocWork extends SolonWork {
        private static final long serialVersionUID = -2870468174480496316L;

        private static final STLogger WORK_LOG = STLogFactory.getLog(ParapheurFolderMaxDocWork.class);

        private Collection<String> parapheurFolderIds;

        public ParapheurFolderMaxDocWork(Collection<String> parapheurFolderIds) {
            this.parapheurFolderIds = new ArrayList<>(parapheurFolderIds);
        }

        @Override
        protected void doWork() {
            openSystemSession();

            WORK_LOG.info(STLogEnumImpl.DEFAULT, "ParapheurFolder à traiter : " + parapheurFolderIds);

            session
                .getDocuments(parapheurFolderIds, null)
                .stream()
                .map(doc -> doc.getAdapter(ParapheurFolder.class))
                .forEach(this::editNbMaxDocRep);
        }

        private void editNbMaxDocRep(ParapheurFolder folder) {
            folder.setNbDocAccepteMax(2L);
            session.saveDocument(folder.getDocument());
        }
    }
}
