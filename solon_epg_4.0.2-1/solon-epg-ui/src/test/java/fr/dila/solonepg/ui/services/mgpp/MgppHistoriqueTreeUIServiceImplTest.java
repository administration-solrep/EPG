package fr.dila.solonepg.ui.services.mgpp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppHistoriqueTreeUIServiceImpl;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.HistoriqueDossierDTO;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.api.service.EvenementService;
import fr.dila.solonmgpp.core.dto.EvenementDTOImpl;
import fr.dila.solonmgpp.core.dto.HistoriqueDossierDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.ui.bean.DossierHistoriqueEPP;
import fr.dila.st.ui.bean.MessageVersion;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.CoreSession;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ SolonMgppServiceLocator.class, MgppUIServiceLocator.class })
@PowerMockIgnore("javax.management.*")
public class MgppHistoriqueTreeUIServiceImplTest {

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private MgppHistoriqueTreeUIService service;

    @Mock
    private SpecificContext context;

    @Mock
    private DossierService dossierService;

    @Mock
    private EvenementService evenementService;

    @Mock
    private EvenementDTO eventDto;

    @Mock
    private CoreSession session;

    @Mock
    private MgppDossierUIService mgppDossierService;

    @Before
    public void before() throws Exception {
        service = new MgppHistoriqueTreeUIServiceImpl();

        PowerMockito.mockStatic(SolonMgppServiceLocator.class);
        PowerMockito.mockStatic(MgppUIServiceLocator.class);
        when(SolonMgppServiceLocator.getDossierService()).thenReturn(dossierService);
        when(SolonMgppServiceLocator.getEvenementService()).thenReturn(evenementService);
        when(MgppUIServiceLocator.getMgppDossierUIService()).thenReturn(mgppDossierService);

        when(context.getFromContextData(MgppContextDataKey.COMMUNICATION_ID)).thenReturn("idEvent");
        when(context.getSession()).thenReturn(session);
    }

    @Test
    public void testGetHistoriqueEpp() {
        // root event
        EvenementDTO rootEvent = new EvenementDTOImpl();
        rootEvent.put(EvenementDTOImpl.ID_EVENEMENT, "event_0001");
        Map<String, Serializable> map = new HashMap<>();
        map.put(EvenementDTOImpl.LABEL, "eventTypeLabel1");
        map.put(EvenementDTOImpl.NAME, "eventTypeName1");
        rootEvent.put(EvenementDTOImpl.TYPE_EVENEMENT, (Serializable) map);
        rootEvent.put(EvenementDTOImpl.ETAT, "PUBLIE");
        Map<String, EvenementDTO> rootEvents = new HashMap<>();
        rootEvents.put(rootEvent.getIdEvenement(), rootEvent);

        // première enfant
        Map<String, List<EvenementDTO>> mapSuivant = new HashMap<>();
        EvenementDTO event2 = new EvenementDTOImpl();
        event2.put(EvenementDTOImpl.ID_EVENEMENT, "event_0002");
        map = new HashMap<>();
        map.put(EvenementDTOImpl.LABEL, "eventTypeLabel2");
        map.put(EvenementDTOImpl.NAME, "eventTypeName2");
        event2.put(EvenementDTOImpl.TYPE_EVENEMENT, (Serializable) map);
        event2.put(EvenementDTOImpl.ETAT, "ANNULER");
        event2.put(EvenementDTOImpl.ID_EVENEMENT_PRECEDENT, "event_0001");
        mapSuivant.put(event2.getIdEvenementPrecedent(), new ArrayList<>());
        mapSuivant.get(event2.getIdEvenementPrecedent()).add(event2);
        // second enfant
        EvenementDTO event3 = new EvenementDTOImpl();
        event3.put(EvenementDTOImpl.ID_EVENEMENT, "event_0003");
        map = new HashMap<>();
        map.put(EvenementDTOImpl.LABEL, "eventTypeLabel3");
        map.put(EvenementDTOImpl.NAME, "eventTypeName3");
        event3.put(EvenementDTOImpl.TYPE_EVENEMENT, (Serializable) map);
        event3.put(EvenementDTOImpl.ETAT, "PUBLIE");
        event3.put(EvenementDTOImpl.ID_EVENEMENT_PRECEDENT, "event_0002");
        mapSuivant.put(event3.getIdEvenementPrecedent(), new ArrayList<>());
        mapSuivant.get(event3.getIdEvenementPrecedent()).add(event3);

        HistoriqueDossierDTO hitoriqueDto = new HistoriqueDossierDTOImpl(null, rootEvents, mapSuivant, null);

        when(mgppDossierService.getCurrentEvenementDTO(context)).thenReturn(eventDto);
        when(eventDto.getIdDossier()).thenReturn("idDossier");
        when(dossierService.findDossier("idDossier", session)).thenReturn(hitoriqueDto);

        DossierHistoriqueEPP historique = service.getHistoriqueEPP(context);

        assertEquals(1, historique.getLstVersions().size());
        MessageVersion version = historique.getLstVersions().get(0);
        assertEquals("event_0001", version.getCommunicationId());
        assertEquals("eventTypeLabel1", version.getCommunicationLabel());
        assertEquals("eventTypeName1", version.getCommunicationType());
        assertFalse(version.isCourant());
        assertFalse(version.isAnnule());
        assertEquals(1, version.getLstChilds().size());

        MessageVersion child1 = version.getLstChilds().get(0);
        assertEquals("event_0002", child1.getCommunicationId());
        assertEquals("eventTypeLabel2", child1.getCommunicationLabel());
        assertEquals("eventTypeName2", child1.getCommunicationType());
        assertFalse(child1.isCourant());
        assertTrue(child1.isAnnule());
        assertEquals(1, child1.getLstChilds().size());

        MessageVersion child2 = child1.getLstChilds().get(0);
        assertEquals("event_0003", child2.getCommunicationId());
        assertEquals("eventTypeLabel3", child2.getCommunicationLabel());
        assertEquals("eventTypeName3", child2.getCommunicationType());
        assertFalse(child2.isCourant());
        assertFalse(child2.isAnnule());
        assertEquals(0, child2.getLstChilds().size());
    }
}
