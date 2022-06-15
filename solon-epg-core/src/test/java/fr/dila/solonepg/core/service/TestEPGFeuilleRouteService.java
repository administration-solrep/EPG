package fr.dila.solonepg.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.feuilleroute.AbstractEPGFeuilleRouteTest;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteStep;
import java.util.Calendar;
import javax.inject.Inject;
import org.junit.Test;

public class TestEPGFeuilleRouteService extends AbstractEPGFeuilleRouteTest {
    @Inject
    private EPGFeuilleRouteServiceImpl epgFeuilleRouteService;

    private static final String TYPE_ACTE_ID = "1";

    private static final String MINISTERE_ID = "1";

    private static final String DIRECTION_ID = "1";

    private static final String USER_1 = "M. Un";

    private static final String USER_2 = "M. Deux";

    private static final String USER_3 = "M. Trois";

    private static final String USER_4 = "M. Quatre";

    @Test
    public void testGetLastChargeMission() throws Exception {
        Dossier dossier = createDossier();
        SSFeuilleRoute feuilleRoute = feuilleRouteModelService.getDefaultRoute(
            session,
            TYPE_ACTE_ID,
            MINISTERE_ID,
            DIRECTION_ID
        );
        dossier.setLastDocumentRoute(feuilleRoute.getDocument().getId());
        documentRoutingService.lockDocumentRoute(feuilleRoute, session);
        session.saveDocument(dossier.getDocument());
        session.save();

        String chargeMission1 = epgFeuilleRouteService.getLastChargeMission(session, dossier.getDocument());

        assertNull(chargeMission1);

        addEtapeToFeuilleRouteWithValidationUserLabel(
            feuilleRoute,
            USER_1,
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS,
            SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE,
            1
        );

        String chargeMission2 = epgFeuilleRouteService.getLastChargeMission(session, dossier.getDocument());

        assertNotNull(chargeMission2);
        assertEquals(USER_1, chargeMission2);

        addEtapeToFeuilleRouteWithValidationUserLabel(
            feuilleRoute,
            USER_2,
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS,
            SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_NON_CONCERNE_VALUE,
            2
        );

        String chargeMission3 = epgFeuilleRouteService.getLastChargeMission(session, dossier.getDocument());

        assertNotNull(chargeMission3);
        assertEquals(USER_1, chargeMission3);

        addEtapeToFeuilleRouteWithValidationUserLabel(
            feuilleRoute,
            USER_3,
            VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION,
            SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE,
            3
        );

        String chargeMission4 = epgFeuilleRouteService.getLastChargeMission(session, dossier.getDocument());

        assertNotNull(chargeMission4);
        assertEquals(USER_1, chargeMission4);

        addEtapeToFeuilleRouteWithValidationUserLabel(
            feuilleRoute,
            USER_4,
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS,
            SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE,
            3
        );

        String chargeMission5 = epgFeuilleRouteService.getLastChargeMission(session, dossier.getDocument());

        assertNotNull(chargeMission5);
        assertEquals(USER_4, chargeMission5);
    }

    private void addEtapeToFeuilleRouteWithValidationUserLabel(
        FeuilleRoute feuilleRoute,
        String validationUserLabel,
        String routingTaskType,
        String validationStatus,
        int index
    ) {
        FeuilleRouteStep newRouteStepDoc = documentRoutingService.createNewRouteStep(session, POSTE1, routingTaskType);
        SSRouteStep routeStep = newRouteStepDoc.getDocument().getAdapter(SSRouteStep.class);
        routeStep.setValidationUserLabel(validationUserLabel);
        routeStep.setDateFinEtape(Calendar.getInstance());
        routeStep.setValidationStatus(validationStatus);
        documentRoutingService.addRouteElementToRoute(
            feuilleRoute.getDocument().getRef(),
            index,
            newRouteStepDoc,
            session
        );
    }
}
