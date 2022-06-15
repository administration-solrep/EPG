package fr.dila.solonepg.core.event;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.recherche.EpgDossierListingDTO;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.util.EpgExcelUtil;
import fr.dila.ss.core.event.AbstractExportDossierListener;
import fr.dila.st.core.util.FileUtils;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.activation.DataSource;
import org.nuxeo.ecm.core.api.CoreSession;

public class ExportDossierAbandonListener extends AbstractExportDossierListener {
    private static final String EXPORT_FILENAME = FileUtils.generateCompleteXlsFilename("Dossiers_à_abandonner");

    public ExportDossierAbandonListener() {
        super(SolonEpgEventConstant.DOSSIERS_ABANDON_EXPORT_EVENT, EXPORT_FILENAME);
    }

    @Override
    protected long countItemsToExport(CoreSession session, Map<String, Serializable> eventProperties) {
        DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        return dossierService.countAllDossiersAbandon(session);
    }

    @Override
    protected DataSource processExport(CoreSession session, Map<String, Serializable> eventProperties) {
        DossierService dossierService = SolonEpgServiceLocator.getDossierService();

        List<EpgDossierListingDTO> itemsToExport = dossierService.getAllDossiersAbandon(session);
        return EpgExcelUtil.exportAdminDossiers(session, itemsToExport);
    }
}
