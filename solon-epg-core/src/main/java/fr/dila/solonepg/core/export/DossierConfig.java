package fr.dila.solonepg.core.export;

import fr.dila.solonepg.core.export.dto.ExportDossierDTO;
import fr.dila.solonepg.core.export.enums.EpgExcelSheetName;
import fr.dila.solonepg.core.util.EpgExcelUtil;
import fr.dila.st.core.export.AbstractEnumExportConfig;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public class DossierConfig extends AbstractEnumExportConfig<ExportDossierDTO> {

    public DossierConfig(List<ExportDossierDTO> dossiers) {
        super(dossiers, EpgExcelSheetName.DOSSIER);
    }

    @Override
    protected String[] getDataCells(CoreSession session, ExportDossierDTO item) {
        String ministereResponsable = item.isHasMinistereLabel()
            ? item.getMinistereResp()
            : EpgExcelUtil.getMinistereLabel(item, item.getMinistereResp(), "Ministère responsable non reconnu");
        String ministereAttache = item.isHasMinistereLabel()
            ? item.getMinistereAttache()
            : EpgExcelUtil.getMinistereLabel(item, item.getMinistereAttache(), "Ministère attaché non reconnu");

        return new String[] {
            item.getNumeroNor(),
            item.getTitreActe(),
            ministereResponsable,
            ministereAttache,
            item.getNomCompletRespDossier()
        };
    }
}
