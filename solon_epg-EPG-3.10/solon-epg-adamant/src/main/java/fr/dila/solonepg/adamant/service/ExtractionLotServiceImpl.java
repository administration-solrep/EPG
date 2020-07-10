package fr.dila.solonepg.adamant.service;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.runtime.model.DefaultComponent;

import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.adamant.dao.DossierExtractionDao;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.adamant.model.ExtractionStatus;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

public class ExtractionLotServiceImpl extends DefaultComponent implements ExtractionLotService {

	@Override
	public List<DossierExtractionLot> getLotsByExtractionStatus(ExtractionStatus status) throws ClientException {
		return SolonEpgAdamantServiceLocator.getDossierExtractionDao().listLotsByStatusSortedByDateAsc(status);
	}

	@Override
	public boolean deleteFromLot(CoreSession session, Dossier dossier, DossierExtractionLot lot)
			throws ClientException {
		SolonEpgAdamantServiceLocator.getDossierExtractionDao().deleteFromLot(dossier, lot);
		if (countDossiers(session, lot) < 1) {
			SolonEpgAdamantServiceLocator.getDossierExtractionDao().updateLotStatus(lot, ExtractionStatus.TERMINE);
			return true;
		}
		return false;
	}

	@Override
	public void updateLotStatus(DossierExtractionLot lot, ExtractionStatus status) throws ClientException {
		SolonEpgAdamantServiceLocator.getDossierExtractionDao().updateLotStatus(lot, status);
	}

	@Override
	public int countDossiers(CoreSession session, DossierExtractionLot lot) throws ClientException {
		DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		
		return dossierService.findDossiersFromIdExtractionLot(session, lot.getId(), 0).size();
	}

	@Override
	public Dossier getNextDossierInLot(CoreSession session, DossierExtractionLot lot) throws ClientException {
		DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		
		List<DocumentModel> list = dossierService.findDossiersFromIdExtractionLot(session, lot.getId(), 1);
		if(list != null && !list.isEmpty()) {
			return list.get(0).getAdapter(Dossier.class);
		}
		return null;
	}
	
	@Override
	public void createAndAssignLotToDocumentsByIds (List<String> ids, CoreSession documentManager) throws ClientException {
		DossierExtractionDao dossierExtractionDao = SolonEpgAdamantServiceLocator.getDossierExtractionDao();
		DossierExtractionLot lot = dossierExtractionDao.createLot();
		Dossier dossier;
		for (String id : ids) {
			dossier = documentManager.getDocument(new IdRef(id)).getAdapter(Dossier.class);
			dossier.setIdExtractionLot(String.valueOf(lot.getId()));
			dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_EXTRACTION_EN_COURS);
			dossier.save(documentManager);
		}
	}

}
