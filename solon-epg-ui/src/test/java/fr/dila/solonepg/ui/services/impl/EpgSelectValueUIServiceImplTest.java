package fr.dila.solonepg.ui.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.service.vocabulary.DelaiPublicationService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.services.EpgSelectValueUIService;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(SolonEpgServiceLocator.class)
@PowerMockIgnore("javax.management.*")
public class EpgSelectValueUIServiceImplTest {
    private static final ImmutablePair<String, String> A_DATE_PRECISEE = ImmutablePair.of("1", "A date précisée");
    private static final ImmutablePair<String, String> AUCUN = ImmutablePair.of("2", "Aucun");
    private static final ImmutablePair<String, String> DE_RIGUEUR = ImmutablePair.of("3", "De rigueur");
    private static final ImmutablePair<String, String> URGENT = ImmutablePair.of("4", "Urgent");
    private static final ImmutablePair<String, String> SANS_TARDER = ImmutablePair.of("5", "Sans tarder");
    private static final ImmutablePair<String, String> SOUS_EMBARGO = ImmutablePair.of("6", "Sous embargo");

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private EpgSelectValueUIService service = new EpgSelectValueUIServiceImpl();

    @Mock
    private DelaiPublicationService delaiPublicationService;

    @Before
    public void setUp() {
        mockStatic(SolonEpgServiceLocator.class);
        when(SolonEpgServiceLocator.getDelaiPublicationService()).thenReturn(delaiPublicationService);

        when(delaiPublicationService.getEntries())
            .thenReturn(ImmutableList.of(A_DATE_PRECISEE, AUCUN, DE_RIGUEUR, URGENT, SANS_TARDER, SOUS_EMBARGO));
    }

    @Test
    public void testGetDelaiPublication() {
        List<SelectValueDTO> results = service.getDelaiPublication();

        assertThat(results)
            .containsExactly(
                new SelectValueDTO(AUCUN.getKey(), AUCUN.getValue()),
                new SelectValueDTO(A_DATE_PRECISEE.getKey(), A_DATE_PRECISEE.getValue()),
                new SelectValueDTO(DE_RIGUEUR.getKey(), DE_RIGUEUR.getValue()),
                new SelectValueDTO(URGENT.getKey(), URGENT.getValue()),
                new SelectValueDTO(SANS_TARDER.getKey(), SANS_TARDER.getValue()),
                new SelectValueDTO(SOUS_EMBARGO.getKey(), SOUS_EMBARGO.getValue())
            );
    }

    @Test
    public void testGetDelaiPublicationFiltered() {
        List<SelectValueDTO> results = service.getDelaiPublicationFiltered();

        assertThat(results)
            .containsExactly(
                new SelectValueDTO(AUCUN.getKey(), AUCUN.getValue()),
                new SelectValueDTO(A_DATE_PRECISEE.getKey(), A_DATE_PRECISEE.getValue()),
                new SelectValueDTO(DE_RIGUEUR.getKey(), DE_RIGUEUR.getValue()),
                new SelectValueDTO(URGENT.getKey(), URGENT.getValue()),
                new SelectValueDTO(SANS_TARDER.getKey(), SANS_TARDER.getValue())
            );
    }
}
