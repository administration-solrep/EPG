package fr.dila.solonepg.ui.services.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.actions.impl.EpgModeleFeuilleRouteActionServiceImpl;
import fr.dila.solonepg.ui.th.model.bean.EpgModeleFdrForm;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.th.impl.SolonAlertManager;
import fr.dila.st.ui.th.model.SpecificContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ STServiceLocator.class, SolonEpgServiceLocator.class })
@PowerMockIgnore("javax.management.*")
public class EpgModeleFeuilleRouteActionServiceImplTest {
    private EpgModeleFeuilleRouteActionService service;

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Mock
    private SpecificContext context;

    @Mock
    private EpgModeleFdrForm modele;

    @Mock
    private OrganigrammeService organigrammeService;

    @Mock
    private UniteStructurelleNode directionNode;

    @Mock
    private SolonEpgVocabularyService vocService;

    @Before
    public void before() {
        PowerMockito.mockStatic(STServiceLocator.class);
        PowerMockito.mockStatic(SolonEpgServiceLocator.class);

        when(STServiceLocator.getOrganigrammeService()).thenReturn(organigrammeService);
        when(SolonEpgServiceLocator.getSolonEpgVocabularyService()).thenReturn(vocService);

        when(organigrammeService.getOrganigrammeNodeById("dir", OrganigrammeType.DIRECTION)).thenReturn(directionNode);
        when(directionNode.getType()).thenReturn(OrganigrammeType.DIRECTION);

        service = new EpgModeleFeuilleRouteActionServiceImpl();
    }

    @Test
    public void testcheckBeforeSaveOk() {
        when(context.getFromContextData(EpgContextDataKey.EPG_MODELE_FORM)).thenReturn(modele);
        when(modele.getIdMinistere()).thenReturn("min");
        when(modele.getIdDirection()).thenReturn("dir");
        when(directionNode.getNorDirectionForMinistereId("min")).thenReturn("nor-test");

        boolean result = service.checkBeforeSave(context);

        assertTrue(result);
    }

    @Test
    public void testcheckBeforeSaveKoMinVideDirNonVide() {
        SolonAlertManager alertManager = new SolonAlertManager();
        context.setMessageQueue(alertManager);

        when(context.getFromContextData(EpgContextDataKey.EPG_MODELE_FORM)).thenReturn(modele);
        when(modele.getIdMinistere()).thenReturn("");
        when(modele.getIdDirection()).thenReturn("dir");
        when(context.getMessageQueue()).thenReturn(alertManager);

        boolean result = service.checkBeforeSave(context);

        assertFalse(result);
        assertEquals(1, alertManager.getErrorQueue().size());
        assertEquals(
            "Le ministère est obligatoire si la direction est renseignée",
            alertManager.getErrorQueue().get(0).getAlertMessage().get(0)
        );
    }

    @Test
    public void testcheckBeforeSaveKoNorVide() {
        SolonAlertManager alertManager = new SolonAlertManager();
        context.setMessageQueue(alertManager);

        when(context.getFromContextData(EpgContextDataKey.EPG_MODELE_FORM)).thenReturn(modele);
        when(modele.getIdMinistere()).thenReturn("min");
        when(modele.getIdDirection()).thenReturn("dir");
        when(organigrammeService.getOrganigrammeNodeById("dir", OrganigrammeType.DIRECTION)).thenReturn(directionNode);
        when(directionNode.getNorDirectionForMinistereId("min")).thenReturn("");
        when(context.getMessageQueue()).thenReturn(alertManager);

        boolean result = service.checkBeforeSave(context);

        assertFalse(result);
        assertEquals(1, alertManager.getErrorQueue().size());
        assertEquals(
            "La direction doit appartenir au ministère sélectionné",
            alertManager.getErrorQueue().get(0).getAlertMessage().get(0)
        );
    }

    @Test
    public void testSplitIntituleAll() {
        when(
                vocService.getEntryLabel(
                    VocabularyConstants.TYPE_ACTE_VOCABULARY,
                    "typeActeId",
                    STVocabularyConstants.COLUMN_LABEL
                )
            )
            .thenReturn("Type acte");

        String[] result = service.splitIntitule(
            "MIN - X - Type acte - Intitule modele",
            "ministereID",
            "directionID",
            "typeActeId"
        );

        assertEquals("MIN - X - Type acte", result[0]);
        assertEquals("Intitule modele", result[1]);
    }

    @Test
    public void testSplitIntituleMinDir() {
        String[] result = service.splitIntitule("MIN - X - Intitule modele", "ministereID", "directionID", "");

        assertEquals("MIN - X", result[0]);
        assertEquals("Intitule modele", result[1]);
    }

    @Test
    public void testSplitIntituleMin() {
        String[] result = service.splitIntitule("MIN - Intitule modele", "ministereID", "", "");

        assertEquals("MIN", result[0]);
        assertEquals("Intitule modele", result[1]);
    }
}
