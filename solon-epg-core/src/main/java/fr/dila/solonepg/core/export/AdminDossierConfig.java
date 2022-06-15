package fr.dila.solonepg.core.export;

import fr.dila.solonepg.api.recherche.EpgDossierListingDTO;
import fr.dila.solonepg.core.export.enums.EpgExcelSheetName;
import fr.dila.st.core.export.AbstractEnumExportConfig;
import fr.dila.st.core.util.SolonDateConverter;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public class AdminDossierConfig extends AbstractEnumExportConfig<EpgDossierListingDTO> {

    public AdminDossierConfig(List<EpgDossierListingDTO> dossiers) {
        super(dossiers, EpgExcelSheetName.ADMIN_DOSSIER);
    }

    @Override
    protected String[] getDataCells(CoreSession session, EpgDossierListingDTO item) {
        String dateArrivee = SolonDateConverter.DATE_SLASH.format(item.getDateArrivee());
        String datePublication = SolonDateConverter.DATE_SLASH.format(item.getDatePublication());

        return new String[] {
            item.getNumeroNor(),
            item.getTitreActe(),
            dateArrivee,
            datePublication,
            item.getTypeActe()
        };
    }
}
