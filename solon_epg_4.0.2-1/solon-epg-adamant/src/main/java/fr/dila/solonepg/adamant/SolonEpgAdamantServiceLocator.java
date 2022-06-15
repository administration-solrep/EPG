package fr.dila.solonepg.adamant;

import static fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil.getRequiredService;

import fr.dila.solonepg.adamant.dao.DossierExtractionDao;
import fr.dila.solonepg.adamant.service.BordereauService;
import fr.dila.solonepg.adamant.service.DossierExtractionService;
import fr.dila.solonepg.adamant.service.ExtractionLotService;
import fr.dila.solonepg.adamant.service.RetourVitamService;

/**
 * Service locator pour le module Adamant.
 *
 * @author tlombard
 */
public class SolonEpgAdamantServiceLocator {

    public static DossierExtractionDao getDossierExtractionDao() {
        return getRequiredService(DossierExtractionDao.class);
    }

    public static ExtractionLotService getExtractionLotService() {
        return getRequiredService(ExtractionLotService.class);
    }

    public static DossierExtractionService getDossierExtractionService() {
        return getRequiredService(DossierExtractionService.class);
    }

    public static BordereauService getBordereauService() {
        return getRequiredService(BordereauService.class);
    }

    public static RetourVitamService getRetourVitamService() {
        return getRequiredService(RetourVitamService.class);
    }
}
