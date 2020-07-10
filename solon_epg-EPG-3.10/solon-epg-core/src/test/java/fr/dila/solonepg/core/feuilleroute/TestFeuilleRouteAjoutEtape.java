package fr.dila.solonepg.core.feuilleroute;

import java.util.List;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.runtime.api.Framework;

import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.ecm.platform.routing.api.DocumentRouteStep;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.feuilleroute.STRouteStep;

/**
 * Test de l'ajout d'étape dans un conteneur de feuille de route.
 * 
 * @author jtremeaux
 */
public class TestFeuilleRouteAjoutEtape extends SolonEpgRepositoryTestCase {
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
        assertEquals(2, stepList.size());
        assertEquals(VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION, stepList.get(0).getAdapter(STRouteStep.class).getType());
        assertEquals(VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION, stepList.get(1).getAdapter(STRouteStep.class).getType());
        
        // Ajoute une étape après la 1ère étape (entre 2 étapes)
        documentRoutingService.lockDocumentRoute(documentRoute, session);
        DocumentRouteStep newRouteStepDoc = documentRoutingService.createNewRouteStep(session, POSTE1, VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION);
        documentRoutingService.addRouteElementToRoute(documentRouteDoc.getRef(), 1, newRouteStepDoc, session);

        // Vérifie l'ordre des étapes
        stepList = session.getChildren(defaultRoute.getDocument().getRef());
        assertEquals(3, stepList.size());
        assertEquals(VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION, stepList.get(0).getAdapter(STRouteStep.class).getType());
        assertEquals(VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION, stepList.get(1).getAdapter(STRouteStep.class).getType());
        assertEquals(VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION, stepList.get(2).getAdapter(STRouteStep.class).getType());

        // Ajoute une étape à la fin
        documentRoutingService.lockDocumentRoute(documentRoute, session);
        newRouteStepDoc = documentRoutingService.createNewRouteStep(session, POSTE1, VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION);
        documentRoutingService.addRouteElementToRoute(documentRouteDoc.getRef(), 3, newRouteStepDoc, session);

        // Vérifie l'ordre des étapes
        stepList = session.getChildren(defaultRoute.getDocument().getRef());
        assertEquals(4, stepList.size());
        assertEquals(VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION, stepList.get(0).getAdapter(STRouteStep.class).getType());
        assertEquals(VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION, stepList.get(1).getAdapter(STRouteStep.class).getType());
        assertEquals(VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION, stepList.get(2).getAdapter(STRouteStep.class).getType());
        assertEquals(VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION, stepList.get(3).getAdapter(STRouteStep.class).getType());
        
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
        createSerialStep(session, route, POSTE1, "Pour information",
                VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION,
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
