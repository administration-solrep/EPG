package fr.dila.solonepg.adamant.service.impl;

import fr.dila.solonepg.adamant.SolonEpgAdamantConstant;
import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.adamant.dao.DossierExtractionDao;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.adamant.model.ExtractionStatus;
import fr.dila.solonepg.adamant.service.ExtractionLotService;
import fr.dila.solonepg.adamant.work.CreationLotArchivageWork;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.util.List;
import org.apache.commons.collections4.ListUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.work.api.WorkManager;
import org.nuxeo.runtime.model.DefaultComponent;

public class ExtractionLotServiceImpl extends DefaultComponent implements ExtractionLotService {
    private static final STLogger LOG = STLogFactory.getLog(SolonEpgAdamantConstant.ARCHIVAGE_LOG);

    private static final int LOT_ARCHIVAGE_BUCKET_SIZE = 50;

    @Override
    public List<DossierExtractionLot> getLotsByExtractionStatus(ExtractionStatus status) {
        return SolonEpgAdamantServiceLocator.getDossierExtractionDao().listLotsByStatusSortedByDateAsc(status);
    }

    @Override
    public boolean deleteFromLot(CoreSession session, Dossier dossier, DossierExtractionLot lot) {
        SolonEpgAdamantServiceLocator.getDossierExtractionDao().deleteFromLot(dossier, lot);
        if (countDossiers(session, lot) < 1) {
            SolonEpgAdamantServiceLocator.getDossierExtractionDao().updateLotStatus(lot, ExtractionStatus.TERMINE);
            return true;
        }
        return false;
    }

    @Override
    public void updateLotStatus(DossierExtractionLot lot, ExtractionStatus status) {
        SolonEpgAdamantServiceLocator.getDossierExtractionDao().updateLotStatus(lot, status);
    }

    @Override
    public int countDossiers(CoreSession session, DossierExtractionLot lot) {
        DossierService dossierService = SolonEpgServiceLocator.getDossierService();

        return dossierService.findDossiersFromIdExtractionLot(session, lot.getId(), 0).size();
    }

    @Override
    public List<DocumentModel> getDossiersInLot(CoreSession session, DossierExtractionLot lot) {
        DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        return dossierService.findDossiersFromIdExtractionLot(session, lot.getId(), 1);
    }

    @Override
    public void createAndAssignLotToDocumentsByIds(List<String> idsDossiers) {
        LOG.info(
            STLogEnumImpl.DEFAULT,
            String.format("Création d'un lot d'archivage de %d dossiers", idsDossiers.size())
        );

        DossierExtractionDao dossierExtractionDao = SolonEpgAdamantServiceLocator.getDossierExtractionDao();
        DossierExtractionLot lot = dossierExtractionDao.createLot();
        String idLot = String.valueOf(lot.getId());

        WorkManager workManager = ServiceUtil.getRequiredService(WorkManager.class);

        ListUtils
            .partition(idsDossiers, LOT_ARCHIVAGE_BUCKET_SIZE)
            .stream()
            .map(ids -> new CreationLotArchivageWork(idLot, ids))
            .forEach(workManager::schedule);
    }
}
