package fr.dila.solonepg.core.operation.distribution;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;

/**
 * Opération permettant de run une étape de feuille de route
 *
 */
@Operation(
    id = RunStepOperation.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "Run routeStep",
    description = RunStepOperation.DESCRIPTION
)
public class RunStepOperation {
    /**
     * Identifiant technique de l'opération.
     */
    public static final String ID = "EPG.FeuilleRoute.RunStep";

    public static final String DESCRIPTION = "Cette opération permet de redémarrer une étape de feuile de route";

    private static final STLogger LOGGER = STLogFactory.getLog(RunStepOperation.class);

    private static final String MESSAGE_ERROR_END =
        "Fin du script, étape non redémarrée, étape distribuée dans des corbeilles. Voir la log pour plus d'information";

    @Context
    protected OperationContext context;

    @Context
    protected CoreSession session;

    @Param(name = "stepId", required = true)
    protected String stepId;

    @OperationMethod
    public void runStep() {
        LOGGER.info(SSLogEnumImpl.INIT_OPERATION_RUN_STEP);

        LOGGER.info(SSLogEnumImpl.PROCESS_OPERATION_RUN_STEP, "Running runStep : stepUUID = " + stepId);

        DocumentModel stepDoc = session.getDocument(new IdRef(stepId));

        if (stepDoc == null) {
            LOGGER.error(SSLogEnumImpl.FAIL_OPERATION_RUN_STEP, "Argument stepUUID non valide, étape non trouvée");
        } else {
            LOGGER.info(SSLogEnumImpl.PROCESS_OPERATION_RUN_STEP, "retrieve DossierLink from Step");

            String queryDL =
                "SELECT * FROM DossierLink WHERE acslk:stepDocumentId = '" + stepId + "' AND acslk:deleted = 0 ";
            DocumentModelList list = session.query(queryDL);

            if (CollectionUtils.isNotEmpty(list)) {
                StringBuilder message = new StringBuilder();
                message.append("Le dossier est distribué dans des corbeilles ! : ");
                message.append(list.stream().map(doc -> doc.getId()).collect(Collectors.joining(", ")));
                message.append(MESSAGE_ERROR_END);
                LOGGER.info(SSLogEnumImpl.PROCESS_OPERATION_RUN_STEP, message.toString());
            } else {
                getFromDossier(stepDoc);
            }
        }

        LOGGER.info(SSLogEnumImpl.END_OPERATION_RUN_STEP);
    }

    private void getFromDossier(DocumentModel stepDoc) {
        LOGGER.info(SSLogEnumImpl.PROCESS_OPERATION_RUN_STEP, "Retrieve DossierLink from Dossier");
        SSRouteStep step = stepDoc.getAdapter(SSRouteStep.class);

        String queryDossier =
            "SELECT * FROM Dossier WHERE dos:lastDocumentRoute = '" + step.getDocumentRouteId() + "' ";
        DocumentModelList listDossier = session.query(queryDossier);
        if (CollectionUtils.isEmpty(listDossier)) {
            LOGGER.error(SSLogEnumImpl.FAIL_OPERATION_RUN_STEP, "Fin du script, dossier introuvable");
        } else {
            DocumentModel dossierDoc = listDossier.get(0);
            String queryDossierLink =
                "SELECT * FROM DossierLink WHERE acslk:caseDocumentId = '" +
                dossierDoc.getId() +
                "' AND acslk:stepDocumentId='" +
                stepId +
                "' AND acslk:deleted = 0 ";
            DocumentModelList listDL = session.query(queryDossierLink);

            if (CollectionUtils.isNotEmpty(listDL)) {
                StringBuilder message = new StringBuilder();
                message.append(
                    "Le dossier " + dossierDoc.getId() + " est distribué dans des corbeilles ! DossierLinkId liste : "
                );
                message.append(listDL.stream().map(doc -> doc.getId()).collect(Collectors.joining(", ")));
                message.append(MESSAGE_ERROR_END);
                LOGGER.info(SSLogEnumImpl.PROCESS_OPERATION_RUN_STEP, message.toString());
            } else {
                LOGGER.info(SSLogEnumImpl.PROCESS_OPERATION_RUN_STEP, "ok running step");
                step = stepDoc.getAdapter(SSRouteStep.class);
                step.run(session);
                LOGGER.info(SSLogEnumImpl.PROCESS_OPERATION_RUN_STEP, "Fin du script, etape redemarree");
            }
        }
    }
}
