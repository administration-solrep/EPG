package fr.dila.solonepg.core.feuilleroute;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.runtime.api.Framework;

import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.st.api.constant.STConstant;

/**
 * Test de la recherche de feuilles de routes par défaut à la création d'un dossier.
 * 
 * @author jtremeaux
 */
public class TestFeuilleRouteDefaut extends SolonEpgRepositoryTestCase  { 
    private static final String TYPE_ACTE_1 = "1";

    private static final String TYPE_ACTE_2 = "2";

    private static final String MINISTERE_1 = "1000";

    private static final String MINISTERE_2 = "1001";

    private static final String DIRECTION_1 = "2000"; 

    private static final String DIRECTION_2 = "2001";

    
    public void testDefaultRoute() throws Exception {
    	openSession();
    	
    	// Crée les feuilles de route par défaut
        // La feuille de route par défaut globalement est créée par le content repository service. 
        createDefaultRoute(session, "FDR_1", TYPE_ACTE_1, MINISTERE_1, DIRECTION_1);
        createDefaultRoute(session, "FDR_2", TYPE_ACTE_1, MINISTERE_1, null);
        createDefaultRoute(session, "FDR_3", null, MINISTERE_1, null);
        
        // Vérifie la présence d'une feuille de route par défaut
    	DocumentRoute defaultRoute = feuilleRouteModelService.getDefaultRoute(session, TYPE_ACTE_1, MINISTERE_1, DIRECTION_1);
        assertNotNull(defaultRoute);
        assertEquals("FDR_1", defaultRoute.getName());

        defaultRoute = feuilleRouteModelService.getDefaultRoute(session, TYPE_ACTE_1, MINISTERE_1, DIRECTION_2);
        assertNotNull(defaultRoute);
        assertEquals("FDR_2", defaultRoute.getName());

        defaultRoute = feuilleRouteModelService.getDefaultRoute(session, TYPE_ACTE_2, MINISTERE_1, DIRECTION_1);
        assertNotNull(defaultRoute);
        assertEquals("FDR_3", defaultRoute.getName());

        defaultRoute = feuilleRouteModelService.getDefaultRoute(session, TYPE_ACTE_1, MINISTERE_2, DIRECTION_1);
        assertNotNull(defaultRoute);
        assertEquals("Feuille de route par défaut", defaultRoute.getName());
        
        closeSession();
    }

    /**
     * Crée une feuille de route par défaut.
     * 
     * @param routeName Nom de la feuille de route
     * @param typeActeId Type d'acte
     * @param ministereId Ministère
     * @param directionId Direction
     * @param session Session
     * @return Route par défaut
     * @throws Exception
     */
    private DocumentRoute createDefaultRoute(CoreSession session, String routeName, String typeActeId, String ministereId, String directionId) throws Exception {
        final DocumentModel feuilleRouteModelFolder = feuilleRouteModelService.getFeuilleRouteModelFolder(session);

        // Crée la feuille de route
        DocumentModel route = createDocumentModel(session, routeName, STConstant.FEUILLE_ROUTE_DOCUMENT_TYPE, feuilleRouteModelFolder.getPathAsString());
        SolonEpgFeuilleRoute solonEpgFeuilleRoute = route.getAdapter(SolonEpgFeuilleRoute.class);
        assertNotNull(solonEpgFeuilleRoute);
        
        solonEpgFeuilleRoute.setFeuilleRouteDefaut(true);
        solonEpgFeuilleRoute.setTypeActe(typeActeId);
        solonEpgFeuilleRoute.setMinistere(ministereId);
        solonEpgFeuilleRoute.setDirection(directionId);
        session.saveDocument(solonEpgFeuilleRoute.getDocument());
        session.save();
        
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
