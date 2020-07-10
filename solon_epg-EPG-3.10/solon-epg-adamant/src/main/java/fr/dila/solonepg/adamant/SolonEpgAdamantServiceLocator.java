package fr.dila.solonepg.adamant;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.adamant.dao.DossierExtractionDao;
import fr.dila.solonepg.adamant.service.BordereauService;
import fr.dila.solonepg.adamant.service.DossierExtractionService;
import fr.dila.solonepg.adamant.service.ExtractionLotService;

/**
 * Service locator pour le module Adamant.
 * 
 * @author tlombard
 */
public class SolonEpgAdamantServiceLocator {
	private SolonEpgAdamantServiceLocator() {
		
	}
	
	public static DossierExtractionDao getDossierExtractionDao() throws ClientException {
		DossierExtractionDao dossierExtractionDao = null;
		try {
			dossierExtractionDao = Framework.getService(DossierExtractionDao.class);
		} catch (Exception e) {
			throw new ClientException("Impossible de récupérer le DAO DossierExtractionDao",e);
		}
		return dossierExtractionDao;
	}

	public static ExtractionLotService getExtractionLotService() throws ClientException {
		ExtractionLotService extractionLotService = null;
		try {
			extractionLotService = Framework.getService(ExtractionLotService.class);
		} catch (Exception e) {
			throw new ClientException("Impossible de récupérer le service ExtractionLotService",e);
		}
		return extractionLotService;
	}
	
	public static DossierExtractionService getDossierExtractionService() throws ClientException {
		DossierExtractionService dossierExtractionService = null;
		try {
			dossierExtractionService = Framework.getService(DossierExtractionService.class);
		} catch (Exception e) {
			throw new ClientException("Impossible de récupérer le service DossierExtractionService",e);
		}
		return dossierExtractionService;
	}
	
	public static BordereauService getBordereauService() throws ClientException {
		BordereauService bordereauService = null;
		try {
			bordereauService = Framework.getService(BordereauService.class);
		} catch (Exception e) {
			throw new ClientException("Impossible de récupérer le service BordereauService",e);
		}
		return bordereauService;
	}	
}
