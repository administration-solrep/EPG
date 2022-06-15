package fr.dila.solonepg.core.event;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.recherche.EpgDossierListingDTO;
import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.util.EpgExcelUtil;
import fr.dila.ss.core.event.AbstractExportDossierListener;
import fr.dila.st.core.util.FileUtils;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.activation.DataSource;
import org.nuxeo.ecm.core.api.CoreSession;

public class ExportDossierArchivageDefinitifListener extends AbstractExportDossierListener {
    private static final String EXPORT_FILENAME = FileUtils.generateCompleteXlsFilename("Dossiers_archivage_définitif");

    public ExportDossierArchivageDefinitifListener() {
        super(SolonEpgEventConstant.DOSSIERS_ARCHIVAGE_DEFINITIF_EXPORT_EVENT, EXPORT_FILENAME);
    }

    @Override
    protected long countItemsToExport(CoreSession session, Map<String, Serializable> eventProperties) {
        ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
        return archiveService.countAllDossiersArchivageDefinitif(session);
    }

    @Override
    protected DataSource processExport(CoreSession session, Map<String, Serializable> eventProperties) {
        ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();

        List<EpgDossierListingDTO> itemsToExport = archiveService.getAllDossiersArchivageDefinitif(session);
        return EpgExcelUtil.exportAdminDossiers(session, itemsToExport);
    }
}
