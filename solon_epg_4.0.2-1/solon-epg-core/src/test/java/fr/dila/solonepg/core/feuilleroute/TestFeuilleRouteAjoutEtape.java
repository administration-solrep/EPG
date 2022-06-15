package fr.dila.solonepg.core.feuilleroute;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteStep;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Test de l'ajout d'étape dans un conteneur de feuille de route.
 *
 * @author jtremeaux
 */
public class TestFeuilleRouteAjoutEtape extends AbstractEPGFeuilleRouteTest {
    private static final String TYPE_ACTE_ID = "1";

    private static final String MINISTERE_ID = "1";

    private static final String DIRECTION_ID = "1";

    private static final String POSTE1 = "poste1";

    private static final String DEFAULT_ROUTE_NAME = "FeuilleRouteTest";

    @Test
    public void testDefaultRoute() throws Exception {
        // Crée la feuille de route
        SSFeuilleRoute feuilleRoute = createDefaultRoute(session, DEFAULT_ROUTE_NAME);
        DocumentModel documentRouteDoc = feuilleRoute.getDocument();

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
        Assert.assertEquals(2, stepList.size());
        Assert.assertEquals(
            VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION,
            stepList.get(0).getAdapter(SSRouteStep.class).getType()
        );
        Assert.assertEquals(
            VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION,
            stepList.get(1).getAdapter(SSRouteStep.class).getType()
        );

        // Ajoute une étape après la 1ère étape (entre 2 étapes)
        documentRoutingService.lockDocumentRoute(feuilleRoute, session);
        FeuilleRouteStep newRouteStepDoc = documentRoutingService.createNewRouteStep(
            session,
            POSTE1,
            VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION
        );
        documentRoutingService.addRouteElementToRoute(documentRouteDoc.getRef(), 1, newRouteStepDoc, session);

        // Vérifie l'ordre des étapes
        stepList = session.getChildren(defaultRoute.getDocument().getRef());
        Assert.assertEquals(3, stepList.size());
        Assert.assertEquals(
            VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION,
            stepList.get(0).getAdapter(SSRouteStep.class).getType()
        );
        Assert.assertEquals(
            VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION,
            stepList.get(1).getAdapter(SSRouteStep.class).getType()
        );
        Assert.assertEquals(
            VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION,
            stepList.get(2).getAdapter(SSRouteStep.class).getType()
        );

        // Ajoute une étape à la fin
        documentRoutingService.lockDocumentRoute(feuilleRoute, session);
        newRouteStepDoc =
            documentRoutingService.createNewRouteStep(
                session,
                POSTE1,
                VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION
            );
        documentRoutingService.addRouteElementToRoute(documentRouteDoc.getRef(), 3, newRouteStepDoc, session);

        // Vérifie l'ordre des étapes
        stepList = session.getChildren(defaultRoute.getDocument().getRef());
        Assert.assertEquals(4, stepList.size());
        Assert.assertEquals(
            VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION,
            stepList.get(0).getAdapter(SSRouteStep.class).getType()
        );
        Assert.assertEquals(
            VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION,
            stepList.get(1).getAdapter(SSRouteStep.class).getType()
        );
        Assert.assertEquals(
            VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION,
            stepList.get(2).getAdapter(SSRouteStep.class).getType()
        );
        Assert.assertEquals(
            VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION,
            stepList.get(3).getAdapter(SSRouteStep.class).getType()
        );
    }

    /**
     * Crée une feuille de route par défaut.
     *
     * @param routeName Nom de la feuille de route
     * @param session Session
     * @return Route par défaut
     * @throws Exception
     */
    private SSFeuilleRoute createDefaultRoute(CoreSession session, String routeName) throws Exception {
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
            "Pour information",
            VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION,
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

        return route.getAdapter(SSFeuilleRoute.class);
    }
}
