package fr.dila.solonepg.core.operation.livraison;

import static fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl.DEFAULT;

import fr.dila.solonepg.core.cm.work.FixDossierAclRattachementWork;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.operation.STVersion;
import fr.dila.st.core.service.AbstractPersistenceDefaultComponent;
import fr.dila.st.core.service.STServiceLocator;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections4.ListUtils;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.work.api.WorkManager;

@STVersion(version = "4.0.1")
@Operation(id = EpgFixDossierAclRattachementOperation.ID, category = "ACL")
public class EpgFixDossierAclRattachementOperation extends AbstractPersistenceDefaultComponent {
    public static final String ID = "SolonEpg.Dossiers.Fix.ACL.Rattachement";

    private static final STLogger LOG = STLogFactory.getLog(EpgFixDossierAclRattachementOperation.class);

    private static final int BUCKET_SIZE = 10;

    private static final int TIMEOUT_NB_HOURS = 10;

    /**
     * Requête pour récupérer le ids des dossiers dont le ministère et direction
     * de rattachement ne correspondent pas aux droits sur l'acl de rattachement
     */
    private static final String DOSSIERS_ACL_RATTACHEMENT_ERROR_QUERY =
        "Select dsg.ID from DOSSIER_SOLON_EPG dsg WHERE dsg.ID IN(" +
        "Select d.ID from DOSSIER_SOLON_EPG d JOIN ACLS a on d.ID = a.ID AND d.ID = dsg.ID GROUP BY d.ID HAVING count(d.ID) = " +
        "(Select count(d.ID) from DOSSIER_SOLON_EPG d JOIN ACLS a on d.ID = a.ID AND d.ID = dsg.ID " +
        "WHERE a.\"USER\" != ('dossier_rattach_min-' || d.MINISTEREATTACHE) GROUP BY d.ID))";

    @OperationMethod
    public void run() {
        LOG.info(STLogEnumImpl.DEFAULT, "Début de l'opération " + ID);
        LocalTime start = LocalTime.now();

        List<String> dosIds = getDossiersAclRattachementError();
        LOG.info(DEFAULT, dosIds.size() + " dossiers à traiter");

        WorkManager wm = STServiceLocator.getWorkManager();
        ListUtils.partition(dosIds, BUCKET_SIZE).stream().map(FixDossierAclRattachementWork::new).forEach(wm::schedule);

        try {
            wm.awaitCompletion(FixDossierAclRattachementWork.class.getSimpleName(), TIMEOUT_NB_HOURS, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NuxeoException(e);
        } catch (Exception e) {
            LOG.error(STLogEnumImpl.DEFAULT, e);
        }

        LocalTime end = LocalTime.now();
        Duration duration = Duration.between(start, end);
        LOG.info(STLogEnumImpl.DEFAULT, String.format("Fin de l'opération %s. Durée : %s", ID, duration.toString()));
    }

    @SuppressWarnings("unchecked")
    private List<String> getDossiersAclRattachementError() {
        return apply(
            true,
            entityManager -> entityManager.createNativeQuery(DOSSIERS_ACL_RATTACHEMENT_ERROR_QUERY).getResultList()
        );
    }
}
