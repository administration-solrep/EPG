package fr.dila.solonepg.core.operation.distribution;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

/**
 * Opération permettant de vérifier si le parapheur est complet
 *
 */
@Operation(
    id = CheckParapheurCompletOperation.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "Check parapheur complet",
    description = CheckParapheurCompletOperation.DESCRIPTION
)
public class CheckParapheurCompletOperation {
    /**
     * Identifiant technique de l'opération.
     */
    public static final String ID = "EPG.Parapheur.checkComplet";

    public static final String DESCRIPTION = "Cette opération check si le parapheur est complet";

    private static final STLogger LOGGER = STLogFactory.getLog(CheckParapheurCompletOperation.class);

    @Context
    protected OperationContext context;

    @Context
    protected CoreSession session;

    @Param(name = "id", required = true)
    protected String id;

    @OperationMethod
    public void runStep() {
        LOGGER.info(EpgLogEnumImpl.INIT_OPERATION_CHECK_PARAPHEUR_COMPLET);

        LOGGER.info(EpgLogEnumImpl.PROCESS_OPERATION_CHECK_PARAPHEUR_COMPLET, "Running runStep : dossierUUID = " + id);

        DocumentModel doc = session.getDocument(new IdRef(id));

        if (doc == null) {
            LOGGER.error(
                EpgLogEnumImpl.FAIL_OPERATION_CHECK_PARAPHEUR_COMPLET,
                "Argument dossierUuid non valide, dossier non trouvé"
            );
        } else {
            ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
            LOGGER.info(EpgLogEnumImpl.PROCESS_OPERATION_CHECK_PARAPHEUR_COMPLET, "Vérification données parapheur");
            Dossier dossier = doc.getAdapter(Dossier.class);
            LOGGER.info(
                EpgLogEnumImpl.PROCESS_OPERATION_CHECK_PARAPHEUR_COMPLET,
                "état complétude parapheur avant : " + dossier.getIsParapheurComplet()
            );
            parapheurService.checkParapheurComplet(doc, session);
            LOGGER.info(
                EpgLogEnumImpl.PROCESS_OPERATION_CHECK_PARAPHEUR_COMPLET,
                "état complétude parapheur après : " + dossier.getIsParapheurComplet()
            );

            LOGGER.info(
                EpgLogEnumImpl.PROCESS_OPERATION_CHECK_PARAPHEUR_COMPLET,
                "Fin du script - état parapheur : " + dossier.getIsParapheurComplet()
            );
        }
        LOGGER.info(EpgLogEnumImpl.END_OPERATION_CHECK_PARAPHEUR_COMPLET);
    }
}
