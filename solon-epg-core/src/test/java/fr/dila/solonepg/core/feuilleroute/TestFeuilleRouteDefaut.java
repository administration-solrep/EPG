package fr.dila.solonepg.core.feuilleroute;

import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Test de la recherche de feuilles de routes par défaut à la création d'un dossier.
 *
 * @author jtremeaux
 */
public class TestFeuilleRouteDefaut extends AbstractEPGFeuilleRouteTest {
    private static final String TYPE_ACTE_1 = "1";

    private static final String TYPE_ACTE_2 = "2";

    private static final String MINISTERE_1 = "1000";

    private static final String MINISTERE_2 = "1001";

    private static final String DIRECTION_1 = "2000";

    private static final String DIRECTION_2 = "2001";

    @Test
    public void testDefaultRoute() throws Exception {
        // Crée les feuilles de route par défaut
        // La feuille de route par défaut globalement est créée par le content repository service.
        createDefaultRoute(session, "FDR_1", TYPE_ACTE_1, MINISTERE_1, DIRECTION_1);
        createDefaultRoute(session, "FDR_2", TYPE_ACTE_1, MINISTERE_1, null);
        createDefaultRoute(session, "FDR_3", null, MINISTERE_1, null);

        // Vérifie la présence d'une feuille de route par défaut
        FeuilleRoute defaultRoute = feuilleRouteModelService.getDefaultRoute(
            session,
            TYPE_ACTE_1,
            MINISTERE_1,
            DIRECTION_1
        );
        Assert.assertNotNull(defaultRoute);
        Assert.assertEquals("FDR_1", defaultRoute.getName());

        defaultRoute = feuilleRouteModelService.getDefaultRoute(session, TYPE_ACTE_1, MINISTERE_1, DIRECTION_2);
        Assert.assertNotNull(defaultRoute);
        Assert.assertEquals("FDR_2", defaultRoute.getName());

        defaultRoute = feuilleRouteModelService.getDefaultRoute(session, TYPE_ACTE_2, MINISTERE_1, DIRECTION_1);
        Assert.assertNotNull(defaultRoute);
        Assert.assertEquals("FDR_3", defaultRoute.getName());

        defaultRoute = feuilleRouteModelService.getDefaultRoute(session, TYPE_ACTE_1, MINISTERE_2, DIRECTION_1);
        Assert.assertNotNull(defaultRoute);
        Assert.assertEquals("Feuille de route par défaut", defaultRoute.getName());
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
    private FeuilleRoute createDefaultRoute(
        CoreSession session,
        String routeName,
        String typeActeId,
        String ministereId,
        String directionId
    )
        throws Exception {
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

        solonEpgFeuilleRoute.setFeuilleRouteDefaut(true);
        solonEpgFeuilleRoute.setTypeActe(typeActeId);
        solonEpgFeuilleRoute.setMinistere(ministereId);
        solonEpgFeuilleRoute.setDirection(directionId);
        session.saveDocument(solonEpgFeuilleRoute.getDocument());
        session.save();

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
