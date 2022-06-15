package fr.dila.solonepg.core.operation.livraison;

import static fr.dila.solonepg.core.cm.work.TraitementDossiersFinScheduleWork.TRAITEMENT_DOSSIERS_FIN_CATEGORY;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonepg.core.cm.work.TraitementDossiersFinScheduleWork;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.util.concurrent.TimeUnit;
import org.nuxeo.common.utils.ExceptionUtils;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.work.api.WorkManager;

@Operation(
    id = TraitementDossiersFinOperation.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "Traitement des étapes d'un dossier au statut fin",
    description = "Gestion des dossiers au statut fin par la validation ou la suppression des étapes en cours et à venir"
)
public class TraitementDossiersFinOperation {
    public static final String ID = "SolonEpg.Traitement.Dossier.Fin";

    private static final STLogger LOG = STLogFactory.getLog(TraitementDossiersFinOperation.class);

    private static final int TIMEOUT_NB_HOURS = 10;

    @Context
    private CoreSession session;

    @Context
    private NuxeoPrincipal principal;

    @OperationMethod
    public void run() {
        if (!principal.isAdministrator()) {
            return;
        }

        LOG.info(STLogEnumImpl.DEFAULT, "Début de l'opération de traitement des dossiers de fin");

        WorkManager workManager = ServiceUtil.getRequiredService(WorkManager.class);
        try {
            workManager.schedule(new TraitementDossiersFinScheduleWork());
            workManager.awaitCompletion(TRAITEMENT_DOSSIERS_FIN_CATEGORY, TIMEOUT_NB_HOURS, TimeUnit.HOURS);
        } catch (InterruptedException e) { // NOSONAR exception traitée dans ExceptionUtils.checkInterrupt(e)
            ExceptionUtils.checkInterrupt(e);
        }

        LOG.info(STLogEnumImpl.DEFAULT, "Fin de l'opération de traitement des dossiers de fin");
    }
}
