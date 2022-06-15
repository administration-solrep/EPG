package fr.dila.solonepg.core.feuilleroute;

import fr.dila.cm.cases.CaseDistribConstants;
import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Test de distribution d'un dossier : - Crée une route par défaut, avec 4 étapes de distribution (dont 1ère étape
 * obligatoire créée automatiquement) - Crée un un dossier (case) - Instancie la feuille de route sur le dossier -
 * Vérifie le déroulement des 3 étapes
 *
 * @author jtremeaux
 */
public class TestFeuilleRouteDistribution extends AbstractEPGFeuilleRouteTest {

    @Test
    public void testDefaultRoute() throws Exception {
        // Crée la feuille de route par défaut
        createDefaultRoute(session, DEFAULT_ROUTE_NAME);

        // Vérifie la présence d'une feuille de route par défaut
        FeuilleRoute defaultRoute = feuilleRouteModelService.getDefaultRoute(
            session,
            DEFAULT_TYPE_ACTE_ID,
            DEFAULT_MINISTERE_ID,
            DEFAULT_DIRECTION_ID
        );
        Assert.assertNotNull(defaultRoute);
        Assert.assertEquals(DEFAULT_ROUTE_NAME, defaultRoute.getName());
        List<DocumentModel> stepList = session.getChildren(defaultRoute.getDocument().getRef());
        Assert.assertEquals(4, stepList.size());

        // Crée le dossier
        createDossier();

        // Valide les tâches successivement
        validateUserTask(POSTE1);
        validateUserTask(POSTE1);
        validateUserTask(POSTE2);
        validateUserTask(POSTE3);
    }

    /**
     * Crée une feuille de route par défaut.
     *
     * @param routeName
     *            Nom de la feuille de route
     * @param session
     *            Session
     * @return Route par défaut
     * @throws Exception
     */
    private FeuilleRoute createDefaultRoute(CoreSession session, String routeName) throws Exception {
        final DocumentModel feuilleRouteModelFolder = feuilleRouteModelService.getFeuilleRouteModelFolder(session);

        // Crée la feuille de route,dc=dila,dc=f
        DocumentModel route = createDocumentModel(
            session,
            routeName,
            SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE,
            feuilleRouteModelFolder.getPathAsString()
        );
        SolonEpgFeuilleRoute solonEpgFeuilleRoute = route.getAdapter(SolonEpgFeuilleRoute.class);
        Assert.assertNotNull(solonEpgFeuilleRoute);

        solonEpgFeuilleRoute.setTypeActe(DEFAULT_TYPE_ACTE_ID);
        solonEpgFeuilleRoute.setMinistere(DEFAULT_MINISTERE_ID);
        solonEpgFeuilleRoute.setDirection(DEFAULT_DIRECTION_ID);
        solonEpgFeuilleRoute.setFeuilleRouteDefaut(true);
        session.saveDocument(solonEpgFeuilleRoute.getDocument());
        session.save();

        // Etape 1
        DocumentModel step1 = createDocumentModel(
            session,
            "Tâche de validation user1",
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE,
            route.getPathAsString()
        );
        Mailbox poste1Mailbox = getPosteMailbox(POSTE1);
        step1.setPropertyValue(CaseDistribConstants.STEP_DISTRIBUTION_MAILBOX_ID_PROPERTY_NAME, poste1Mailbox.getId());
        session.saveDocument(step1);

        // Etape 2
        DocumentModel step2 = createDocumentModel(
            session,
            "Tâche de validation user2",
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE,
            route.getPathAsString()
        );
        Mailbox poste2Mailbox = getPosteMailbox(POSTE2);
        step2.setPropertyValue(CaseDistribConstants.STEP_DISTRIBUTION_MAILBOX_ID_PROPERTY_NAME, poste2Mailbox.getId());
        session.saveDocument(step2);

        // Etape 3
        DocumentModel step3 = createDocumentModel(
            session,
            "Tâche de validation user3",
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE,
            route.getPathAsString()
        );
        Mailbox poste3Mailbox = getPosteMailbox(POSTE3);
        step3.setPropertyValue(CaseDistribConstants.STEP_DISTRIBUTION_MAILBOX_ID_PROPERTY_NAME, poste3Mailbox.getId());
        session.saveDocument(step3);

        // Valide la feuille de route
        documentRoutingService.lockDocumentRoute(solonEpgFeuilleRoute, session);
        SSFeuilleRoute feuilleRoute = documentRoutingService.validateRouteModel(solonEpgFeuilleRoute, session);
        session.saveDocument(feuilleRoute.getDocument());
        session.save();
        documentRoutingService.unlockDocumentRoute(feuilleRoute, session);

        eventService.waitForAsyncCompletion();
        Assert.assertEquals("validated", feuilleRoute.getDocument().getCurrentLifeCycleState());

        return route.getAdapter(FeuilleRoute.class);
    }
}
