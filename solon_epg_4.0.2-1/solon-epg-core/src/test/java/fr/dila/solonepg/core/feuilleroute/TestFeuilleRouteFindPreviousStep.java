package fr.dila.solonepg.core.feuilleroute;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.core.filter.RouteStepValideFilter;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;

/**
 * Test de l'ajout d'étape dans un conteneur de feuille de route.
 *
 * @author jtremeaux
 */
public class TestFeuilleRouteFindPreviousStep extends AbstractEPGFeuilleRouteTest {
    private static final String TYPE_ACTE_ID = "1";

    private static final String MINISTERE_ID = "1";

    private static final String DIRECTION_ID = "1";

    private static final String POSTE1 = "poste1";

    private static final String DEFAULT_ROUTE_NAME = "FeuilleRouteTest";

    @Test
    public void testDefaultRoute() throws Exception {
        // Crée la feuille de route
        FeuilleRoute documentRoute = createDefaultRoute(session, DEFAULT_ROUTE_NAME);
        DocumentModel documentRouteDoc = documentRoute.getDocument();

        // Vérifie la présence d'une feuille de route
        FeuilleRoute defaultRoute = feuilleRouteModelService.getDefaultRoute(
            session,
            TYPE_ACTE_ID,
            MINISTERE_ID,
            DIRECTION_ID
        );
        Assert.assertNotNull(defaultRoute);
        Assert.assertEquals(DEFAULT_ROUTE_NAME, defaultRoute.getName());
        List<DocumentModel> stepList = session.getChildren(documentRouteDoc.getRef());
        Assert.assertEquals(4, stepList.size());

        // Crée le dossier
        createDossier();

        // Recherche l'étape validée avant la première étape : impossible
        Filter routeStepFilter = new RouteStepValideFilter();
        DocumentModel etapePrecedenteDoc = epgFeuilleRouteService.findPreviousStepInFolder(
            session,
            stepList.get(0),
            routeStepFilter,
            false
        );
        Assert.assertNull(etapePrecedenteDoc);
        etapePrecedenteDoc =
            epgFeuilleRouteService.findPreviousStepInFolder(session, stepList.get(1), routeStepFilter, false);
        Assert.assertNull(etapePrecedenteDoc);

        // Valide l'étape 1
        validateUserTask(POSTE1);
        updateRouteStep(stepList.get(0), SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE);

        // Recherche l'étape validée avant l'étape 2 : impossible
        etapePrecedenteDoc =
            epgFeuilleRouteService.findPreviousStepInFolder(session, stepList.get(0), routeStepFilter, false);
        Assert.assertNull(etapePrecedenteDoc);
        etapePrecedenteDoc =
            epgFeuilleRouteService.findPreviousStepInFolder(session, stepList.get(1), routeStepFilter, false);
        Assert.assertNull(etapePrecedenteDoc);

        // Valide l'étape 2 (avis favorable)
        validateUserTask(POSTE1);
        updateRouteStep(stepList.get(1), SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE);

        // Recherche l'étape validée avant l'étape 3 : retourne l'étape 2
        etapePrecedenteDoc =
            epgFeuilleRouteService.findPreviousStepInFolder(session, stepList.get(0), routeStepFilter, false);
        Assert.assertNull(etapePrecedenteDoc);
        etapePrecedenteDoc =
            epgFeuilleRouteService.findPreviousStepInFolder(session, stepList.get(1), routeStepFilter, false);
        Assert.assertNull(etapePrecedenteDoc);
        etapePrecedenteDoc =
            epgFeuilleRouteService.findPreviousStepInFolder(session, stepList.get(2), routeStepFilter, false);
        Assert.assertEquals(etapePrecedenteDoc, stepList.get(1));

        // Valide l'étape 3 (pour information), pas de validation automatique dans ce test
        validateUserTask(POSTE1);
        updateRouteStep(stepList.get(2), SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE);

        // Recherche l'étape validée avant l'étape 4 : retourne l'étape 2 (l'étape pour info. est ignorée)
        etapePrecedenteDoc =
            epgFeuilleRouteService.findPreviousStepInFolder(session, stepList.get(0), routeStepFilter, false);
        Assert.assertNull(etapePrecedenteDoc);
        etapePrecedenteDoc =
            epgFeuilleRouteService.findPreviousStepInFolder(session, stepList.get(1), routeStepFilter, false);
        Assert.assertNull(etapePrecedenteDoc);
        etapePrecedenteDoc =
            epgFeuilleRouteService.findPreviousStepInFolder(session, stepList.get(2), routeStepFilter, false);
        Assert.assertEquals(etapePrecedenteDoc, stepList.get(1));
        etapePrecedenteDoc =
            epgFeuilleRouteService.findPreviousStepInFolder(session, stepList.get(3), routeStepFilter, false);
        Assert.assertEquals(etapePrecedenteDoc, stepList.get(1));
    }

    /**
     * Crée une feuille de route par défaut.
     *
     * @param routeName Nom de la feuille de route
     * @param session Session
     * @return Route par défaut
     * @throws Exception
     */
    private FeuilleRoute createDefaultRoute(CoreSession session, String routeName) throws Exception {
        final DocumentModel feuilleRouteModelFolder = feuilleRouteModelService.getFeuilleRouteModelFolder(session);

        // Crée la feuille de route
        DocumentModel route = createDocumentModel(
            session,
            routeName,
            SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE,
            feuilleRouteModelFolder.getPathAsString()
        );
        SolonEpgFeuilleRoute solonEpgFeuilleRoute = route.getAdapter(SolonEpgFeuilleRoute.class);
        Assert.assertNotNull(solonEpgFeuilleRoute);

        solonEpgFeuilleRoute.setTypeActe(TYPE_ACTE_ID);
        solonEpgFeuilleRoute.setMinistere(MINISTERE_ID);
        solonEpgFeuilleRoute.setDirection(DIRECTION_ID);
        solonEpgFeuilleRoute.setFeuilleRouteDefaut(true);
        session.saveDocument(solonEpgFeuilleRoute.getDocument());
        session.save();

        // Etape 2
        createSerialStep(
            session,
            route,
            POSTE1,
            "Pour attribution",
            VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE
        );

        // Etape 3
        createSerialStep(
            session,
            route,
            POSTE1,
            "Pour information",
            VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION,
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE
        );

        // Etape 4
        createSerialStep(
            session,
            route,
            POSTE1,
            "Pour attribution",
            VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE
        );

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
