package fr.dila.solonepg.core.export;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.core.export.dto.ExportRechercheDossierDTO;
import fr.dila.solonepg.core.export.enums.EpgExcelHeader;
import fr.dila.ss.core.export.enums.SSExcelHeader;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SolonDateConverter;
import java.util.Collections;
import java.util.Date;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.CoreSession;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest(STServiceLocator.class)
public class RechercheDossierConfigTest {

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Mock
    private STUserService userService;

    @Mock
    private CoreSession session;

    @Test
    public void getDataCells() {
        PowerMockito.mockStatic(STServiceLocator.class);
        when(STServiceLocator.getSTUserService()).thenReturn(userService);

        String nor = "nor";
        String titreActe = "titreActe";
        Date dateCreation = new Date();

        String dernierContributeurId = "contributeurId";
        String contributeur = "Philippe Martin";
        when(userService.getUserFullName(dernierContributeurId)).thenReturn(contributeur);

        String creeParId = "creeParId";
        String createur = "Jean Dupont";
        when(userService.getUserFullName(creeParId)).thenReturn(createur);

        String statut = "statut";
        String typeActe = "typeActe";

        ExportRechercheDossierDTO item = new ExportRechercheDossierDTO(
            nor,
            titreActe,
            dateCreation,
            dernierContributeurId,
            creeParId,
            statut,
            typeActe
        );

        RechercheDossierConfig rechercheDossierConfig = new RechercheDossierConfig(ImmutableList.of(item));

        assertThat(rechercheDossierConfig.getDataCells(session, item))
            .hasSize(rechercheDossierConfig.getSheetName().getHeadersSize())
            .containsExactly(
                nor,
                titreActe,
                SolonDateConverter.DATE_SLASH.format(dateCreation),
                contributeur,
                createur,
                statut,
                typeActe
            );
    }

    @Test
    public void getSheetNameHeaders() {
        RechercheDossierConfig rechercheDossierConfig = new RechercheDossierConfig(Collections.emptyList());

        assertThat(rechercheDossierConfig.getSheetName().getHeaders())
            .containsExactly(
                EpgExcelHeader.NOR,
                EpgExcelHeader.TITRE_ACTE,
                EpgExcelHeader.DATE_CREATION,
                EpgExcelHeader.DERNIER_CONTRIBUTEUR,
                EpgExcelHeader.CREE_PAR,
                SSExcelHeader.STATUT,
                EpgExcelHeader.TYPE_ACTE
            );
    }
}
