package fr.dila.solonepg.core.export;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.core.export.dto.ExportDossierDTO;
import fr.dila.solonepg.core.export.enums.EpgExcelHeader;
import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.CoreSession;

@RunWith(MockitoJUnitRunner.class)
public class DossierConfigTest {
    @Mock
    private CoreSession session;

    @Test
    public void getDataCells() {
        String nor = "nor";
        String titreActe = "titre acte";
        String ministereResponsable = "Ministère Responsable Label";
        String ministereAttacheLabel = "Ministère attaché Label";
        String prenomRespDossier = "prenom";
        String nomRespDossier = "nom";

        ExportDossierDTO item = new ExportDossierDTO(
            nor,
            titreActe,
            ministereResponsable,
            ministereAttacheLabel,
            prenomRespDossier,
            nomRespDossier
        );

        DossierConfig dossierConfig = new DossierConfig(ImmutableList.of(item));

        assertThat(dossierConfig.getDataCells(session, item))
            .hasSize(dossierConfig.getSheetName().getHeadersSize())
            .containsExactly(
                nor,
                titreActe,
                ministereResponsable,
                ministereAttacheLabel,
                prenomRespDossier + ' ' + nomRespDossier
            );
    }

    @Test
    public void getSheetNameHeaders() {
        DossierConfig dossierConfig = new DossierConfig(Collections.emptyList());

        assertThat(dossierConfig.getSheetName().getHeaders())
            .containsExactly(
                EpgExcelHeader.NOR,
                EpgExcelHeader.TITRE_ACTE,
                EpgExcelHeader.MINISTERE_RESPONSABLE,
                EpgExcelHeader.MINISTERE_ATTACHE,
                EpgExcelHeader.RESPONSABLE_DOSSIER
            );
    }
}
