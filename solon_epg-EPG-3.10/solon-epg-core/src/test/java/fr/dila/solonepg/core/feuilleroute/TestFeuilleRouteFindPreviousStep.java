package fr.dila.solonepg.core.feuilleroute;

import java.util.List;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.runtime.api.Framework;

import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.solonepg.core.filter.RouteStepValideFilter;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STSchemaConstant;

/**
 * Test de l'ajout d'étape dans un conteneur de feuille de route.
 * 
 * @author jtremeaux
 */
public class TestFeuilleRouteFindPreviousStep extends SolonEpgRepositoryTestCase {
    private static final String TYPE_ACTE_ID = "1";

    private static final String MINISTERE_ID = "1";

    private static final String DIRECTION_ID = "1";

    private static final String POSTE1 = "poste1";

    private static final String DEFAULT_ROUTE_NAME = "FeuilleRouteTest";


    public void testDefaultRoute() throws Exception {
    	openSession();
    	
    	// Crée la feuille de route
        DocumentRoute documentRoute = createDefaultRoute(session, DEFAULT_ROUTE_NAME);
        DocumentModel documentRouteDoc = documentRoute.getDocument();
        
        // Vérifie la présence d'une feuille de route
        DocumentRoute defaultRoute = feuilleRouteModelService.getDefaultRoute(session, TYPE_ACTE_ID, MINISTERE_ID, DIRECTION_ID);
        assertNotNull(defaultRoute);
        assertEquals(DEFAULT_ROUTE_NAME, defaultRoute.getName());
        List<DocumentModel> stepList = session.getChildren(documentRouteDoc.getRef());
        assertEquals(4, stepList.size());
        
        // Crée le dossier
        Dossier dossier = createDossier();
        
        // Démarre la feuille de route associée au dossier
        dossierDistributionService.startDefaultRoute(session, dossier, POSTE1);

        // Recherche l'étape validée avant la première étape : impossible
        Filter routeStepFilter = new RouteStepValideFilter();
        DocumentModel etapePrecedenteDoc = feuilleRouteService.findPreviousStepInFolder(session, stepList.get(0), routeStepFilter, false);
        assertNull(etapePrecedenteDoc);
        etapePrecedenteDoc = feuilleRouteService.findPreviousStepInFolder(session, stepList.get(1), routeStepFilter, false);
        assertNull(etapePrecedenteDoc);
        
        // Valide l'étape 1
        validateUserTask(POSTE1);
        updateRouteStep(stepList.get(0), STSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE);

        // Recherche l'étape validée avant l'étape 2 : impossible
        etapePrecedenteDoc = feuilleRouteService.findPreviousStepInFolder(session, stepList.get(0), routeStepFilter, false);
        assertNull(etapePrecedenteDoc);
        etapePrecedenteDoc = feuilleRouteService.findPreviousStepInFolder(session, stepList.get(1), routeStepFilter, false);
        assertNull(etapePrecedenteDoc);
        
        // Valide l'étape 2 (avis favorable)
        validateUserTask(POSTE1);
        updateRouteStep(stepList.get(1), STSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE);

        // Recherche l'étape validée avant l'étape 3 : retourne l'étape 2
        etapePrecedenteDoc = feuilleRouteService.findPreviousStepInFolder(session, stepList.get(0), routeStepFilter, false);
        assertNull(etapePrecedenteDoc);
        etapePrecedenteDoc = feuilleRouteService.findPreviousStepInFolder(session, stepList.get(1), routeStepFilter, false);
        assertNull(etapePrecedenteDoc);
        etapePrecedenteDoc = feuilleRouteService.findPreviousStepInFolder(session, stepList.get(2), routeStepFilter, false);
        assertEquals(etapePrecedenteDoc, stepList.get(1));

        // Valide l'étape 3 (pour information), pas de validation automatique dans ce test
        validateUserTask(POSTE1);
        updateRouteStep(stepList.get(2), STSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE);

        // Recherche l'étape validée avant l'étape 4 : retourne l'étape 2 (l'étape pour info. est ignorée)
        etapePrecedenteDoc = feuilleRouteService.findPreviousStepInFolder(session, stepList.get(0), routeStepFilter, false);
        assertNull(etapePrecedenteDoc);
        etapePrecedenteDoc = feuilleRouteService.findPreviousStepInFolder(session, stepList.get(1), routeStepFilter, false);
        assertNull(etapePrecedenteDoc);
        etapePrecedenteDoc = feuilleRouteService.findPreviousStepInFolder(session, stepList.get(2), routeStepFilter, false);
        assertEquals(etapePrecedenteDoc, stepList.get(1));
        etapePrecedenteDoc = feuilleRouteService.findPreviousStepInFolder(session, stepList.get(3), routeStepFilter, false);
        assertEquals(etapePrecedenteDoc, stepList.get(1));
        
        closeSession();
    }

    /**
     * Crée une feuille de route par défaut.
     * 
     * @param routeName Nom de la feuille de route
     * @param session Session
     * @return Route par défaut
     * @throws Exception
     */
    private DocumentRoute createDefaultRoute(CoreSession session, String routeName) throws Exception {
        final DocumentModel feuilleRouteModelFolder = feuilleRouteModelService.getFeuilleRouteModelFolder(session);

        // Crée la feuille de route
        DocumentModel route = createDocumentModel(session, routeName, STConstant.FEUILLE_ROUTE_DOCUMENT_TYPE, feuilleRouteModelFolder.getPathAsString());
        SolonEpgFeuilleRoute solonEpgFeuilleRoute = route.getAdapter(SolonEpgFeuilleRoute.class);
        assertNotNull(solonEpgFeuilleRoute);
        
        solonEpgFeuilleRoute.setTypeActe(TYPE_ACTE_ID);
        solonEpgFeuilleRoute.setMinistere(MINISTERE_ID);
        solonEpgFeuilleRoute.setDirection(DIRECTION_ID);
        solonEpgFeuilleRoute.setFeuilleRouteDefaut(true);
        session.saveDocument(solonEpgFeuilleRoute.getDocument());
        session.save();
        
        // Etape 2
        createSerialStep(session, route, POSTE1, "Pour attribution",
                VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
                STConstant.ROUTE_STEP_DOCUMENT_TYPE);

        // Etape 3
        createSerialStep(session, route, POSTE1, "Pour information",
                VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION,
                STConstant.ROUTE_STEP_DOCUMENT_TYPE);

        // Etape 4
        createSerialStep(session, route, POSTE1, "Pour attribution",
                VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
                STConstant.ROUTE_STEP_DOCUMENT_TYPE);

        // Valide la feuille de route
        documentRoutingService.lockDocumentRoute(solonEpgFeuilleRoute, session);
        DocumentRoute feuilleRoute = documentRoutingService.validateRouteModel(solonEpgFeuilleRoute, session);
        session.saveDocument(feuilleRoute.getDocument());
        session.save();
        documentRoutingService.unlockDocumentRoute(feuilleRoute, session);
        
        Framework.getLocalService(EventService.class).waitForAsyncCompletion();
        assertEquals("validated", feuilleRoute.getDocument().getCurrentLifeCycleState());
        
        return route.getAdapter(DocumentRoute.class);
    }
}
