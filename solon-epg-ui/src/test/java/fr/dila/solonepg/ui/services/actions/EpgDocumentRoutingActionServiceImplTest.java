package fr.dila.solonepg.ui.services.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.services.actions.impl.EpgDocumentRoutingActionServiceImpl;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.st.ui.th.impl.SolonAlertManager;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ SolonEpgServiceLocator.class })
@PowerMockIgnore("javax.management.*")
public class EpgDocumentRoutingActionServiceImplTest {
    private EpgDocumentRoutingActionService service;

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Mock
    private SpecificContext context;

    @Mock
    private SolonEpgFeuilleRoute route;

    @Mock
    private DocumentModel doc;

    @Mock
    private SSRouteStep step;

    @Mock
    private ActeService acteService;

    @Before
    public void before() throws Exception {
        PowerMockito.mockStatic(SolonEpgServiceLocator.class);

        when(SolonEpgServiceLocator.getActeService()).thenReturn(acteService);

        service = new EpgDocumentRoutingActionServiceImpl();

        when(doc.getAdapter(SSRouteStep.class)).thenReturn(step);
    }

    @Test
    public void testCheckValiditySteps() {
        List<DocumentModel> stepsDoc = new ArrayList<>();
        stepsDoc.add(doc);
        String typeActe = "typeActe";

        when(step.getType()).thenReturn(VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION);
        when(route.getTypeActe()).thenReturn(typeActe);
        when(acteService.isActeTexteNonPublie(typeActe)).thenReturn(false);

        boolean result = service.checkValiditySteps(context, route, stepsDoc);

        assertTrue(result);
    }

    @Test
    public void testCheckValidityStepsIncompatible() {
        List<DocumentModel> stepsDoc = new ArrayList<>();
        stepsDoc.add(doc);
        String typeActe = VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE;

        SolonAlertManager alertManager = new SolonAlertManager();
        context.setMessageQueue(alertManager);

        when(step.getType()).thenReturn(VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE);
        when(route.getTypeActe()).thenReturn(typeActe);
        when(acteService.isActeTexteNonPublie(typeActe)).thenReturn(true);
        when(context.getMessageQueue()).thenReturn(alertManager);

        boolean result = service.checkValiditySteps(context, route, stepsDoc);

        assertFalse(result);
        assertEquals(
            "Impossible de sauvegarder l'étape. Elle est incompatible avec le type d'acte.",
            alertManager.getErrorQueue().get(0).getAlertMessage().get(0)
        );
    }
}
