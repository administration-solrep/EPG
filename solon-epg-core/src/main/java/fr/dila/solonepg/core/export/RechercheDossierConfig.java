package fr.dila.solonepg.core.export;

import fr.dila.solonepg.core.export.dto.ExportRechercheDossierDTO;
import fr.dila.solonepg.core.export.enums.EpgExcelSheetName;
import fr.dila.st.core.export.AbstractEnumExportConfig;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public class RechercheDossierConfig extends AbstractEnumExportConfig<ExportRechercheDossierDTO> {

    public RechercheDossierConfig(List<ExportRechercheDossierDTO> dossiers) {
        super(dossiers, EpgExcelSheetName.RECHERCHE_DOSSIER);
    }

    @Override
    protected String[] getDataCells(CoreSession session, ExportRechercheDossierDTO item) {
        return new String[] {
            item.getNumeroNor(),
            item.getTitreActe(),
            item.getDateCreation(),
            item.getDernierContributeur(),
            item.getCreePar(),
            item.getStatut(),
            item.getTypeActe()
        };
    }
}
