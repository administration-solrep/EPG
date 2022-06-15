package fr.dila.solonepg.core.export;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.recherche.EpgDossierListingDTO;
import fr.dila.solonepg.core.export.enums.EpgExcelHeader;
import fr.dila.solonepg.core.recherche.EpgDossierListingDTOImpl;
import fr.dila.st.core.util.SolonDateConverter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.CoreSession;

@RunWith(MockitoJUnitRunner.class)
public class AdminDossierConfigTest {
    @Mock
    private CoreSession session;

    @Test
    public void getDataCells() {
        String nor = "nor";
        String titreActe = "titreActe";

        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.of(2021, 8, 23);
        Date dateArrivee = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        Date datePublication = Date.from(localDate.plusDays(3).atStartOfDay(defaultZoneId).toInstant());
        String typeActe = "typeActe";

        EpgDossierListingDTO item = new EpgDossierListingDTOImpl();
        item.setNumeroNor(nor);
        item.setTitreActe(titreActe);
        item.setDateArrivee(dateArrivee);
        item.setDatePublication(datePublication);
        item.setTypeActe(typeActe);

        AdminDossierConfig adminDossierConfig = new AdminDossierConfig(ImmutableList.of(item));

        assertThat(adminDossierConfig.getDataCells(session, item))
            .hasSize(adminDossierConfig.getSheetName().getHeadersSize())
            .containsExactly(
                nor,
                titreActe,
                SolonDateConverter.DATE_SLASH.format(dateArrivee),
                SolonDateConverter.DATE_SLASH.format(datePublication),
                typeActe
            );
    }

    @Test
    public void getSheetNameHeaders() {
        AdminDossierConfig adminDossierConfig = new AdminDossierConfig(Collections.emptyList());

        assertThat(adminDossierConfig.getSheetName().getHeaders())
            .containsExactly(
                EpgExcelHeader.NOR,
                EpgExcelHeader.TITRE_ACTE,
                EpgExcelHeader.DATE_ARRIVEE,
                EpgExcelHeader.DATE_PUBLICATION,
                EpgExcelHeader.TYPE_ACTE
            );
    }
}
