package fr.dila.solonepg.core.feuilleroute;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.cm.service.CaseDistributionService;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class TestFeuilleRoutePourInformation extends AbstractEPGFeuilleRouteTest {
    private static final String TYPE_ACTE_ID = "1";

    private static final String MINISTERE_ID = "1";

    private static final String DIRECTION_ID = "1";

    private static final String POSTE1 = "poste1";

    private static final String POSTE2 = "poste2";

    private static final String POSTE3 = "poste3";

    private static final String NOR = "ECOX9800017A";

    private static final String DEFAULT_ROUTE_NAME = "DefaultRouteModel";

    // Une feuille de route qui ressemble un plus à celle utilisée par réponse
    private SSFeuilleRoute createFeuilleRoute(CoreSession session) {
        final DocumentModel feuilleRouteModelFolder = feuilleRouteModelService.getFeuilleRouteModelFolder(session);
        // Crée la feuille de route
        DocumentModel route = createDocumentModel(
            session,
            DEFAULT_ROUTE_NAME,
            SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE,
            feuilleRouteModelFolder.getPathAsString()
        );
        SolonEpgFeuilleRoute feuilleRoute = route.getAdapter(SolonEpgFeuilleRoute.class);
        Assert.assertNotNull(feuilleRoute);

        feuilleRoute.setTypeActe(TYPE_ACTE_ID);
        feuilleRoute.setMinistere(MINISTERE_ID);
        feuilleRoute.setDirection(DIRECTION_ID);
        feuilleRoute.setFeuilleRouteDefaut(true);
        session.saveDocument(feuilleRoute.getDocument());
        session.save();

        createSerialStep(
            session,
            route,
            POSTE1,
            "Pour attribution agents BDC",
            VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE
        );
        createSerialStep(
            session,
            route,
            POSTE2,
            "Pour Information DLF",
            VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION,
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE
        );
        createSerialStep(
            session,
            route,
            POSTE3,
            "Pour attribution",
            VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE
        );
        session.save();
        return route.getAdapter(SSFeuilleRoute.class);
    }

    private Dossier createDossier(CoreSession session) {
        // Crée le dossier
        DocumentModel dossierDoc = session.createDocumentModel(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
        DublincoreSchemaUtils.setTitle(dossierDoc, NOR);
        final CaseDistributionService caseDistribService = SSServiceLocator.getCaseDistributionService();
        Mailbox userMailbox = getPersonalMailbox(user1);
        Dossier dossier = caseDistribService.createEmptyCase(session, dossierDoc, userMailbox, Dossier.class);

        // Ajout des informations de création
        dossier.setTypeActe(TYPE_ACTE_ID);
        dossier.setMinistereResp(MINISTERE_ID);
        dossier.setDirectionResp(DIRECTION_ID);
        dossier.setNumeroNor(NOR);

        // Crée le dossier
        dossierService.createDossier(session, dossier, POSTE1, null);
        return dossier;
    }

    private void createAndValidateFdr() {
        // Crée la feuille de route par défaut
        SSFeuilleRoute documentRoute = createFeuilleRoute(session);

        // Valide la feuille de route
        documentRoutingService.lockDocumentRoute(documentRoute, session);
        documentRoute = documentRoutingService.validateRouteModel(documentRoute, session);
        session.saveDocument(documentRoute.getDocument());
        session.save();
        documentRoutingService.unlockDocumentRoute(documentRoute, session);
        eventService.waitForAsyncCompletion();

        Assert.assertEquals("validated", documentRoute.getDocument().getCurrentLifeCycleState());
        Assert.assertEquals(
            "validated",
            session.getChildren(documentRoute.getDocument().getRef()).get(0).getCurrentLifeCycleState()
        );
    }

    @Test
    public void testValidationPourInformationStep() {
        createAndValidateFdr();

        createDossier(session);

        // validation de l'étape d'initialisation
        validateUserTask(POSTE1);

        // validation de l'étape 1
        validateUserTask(POSTE1);

        // la fdr doit maintenant être à l'étape 3
        validateUserTask(POSTE3);
    }
}
