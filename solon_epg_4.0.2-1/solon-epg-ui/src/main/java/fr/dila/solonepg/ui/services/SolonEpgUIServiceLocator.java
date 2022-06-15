package fr.dila.solonepg.ui.services;

import static fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil.getRequiredService;

import fr.dila.solonepg.elastic.services.RequeteurService;
import fr.dila.solonepg.ui.services.actions.EpgDossierDistributionActionService;
import fr.dila.ss.ui.services.SSRoutingTaskFilterService;

public final class SolonEpgUIServiceLocator {

    /**
     * Utility class
     */
    private SolonEpgUIServiceLocator() {
        // do nothing
    }

    public static EpgMailboxListComponentService getEpgMailboxListComponentService() {
        return getRequiredService(EpgMailboxListComponentService.class);
    }

    public static SolonEpgDossierListUIService getSolonEpgDossierListUIService() {
        return getRequiredService(SolonEpgDossierListUIService.class);
    }

    public static EpgModeleFdrFicheUIService getEpgModeleFdrFicheUIService() {
        return getRequiredService(EpgModeleFdrFicheUIService.class);
    }

    public static EpgSelectValueUIService getEpgSelectValueUIService() {
        return getRequiredService(EpgSelectValueUIService.class);
    }

    public static EpgDossierDistributionActionService getEpgDossierDistributionUIService() {
        return getRequiredService(EpgDossierDistributionActionService.class);
    }

    public static SSRoutingTaskFilterService getEpgRoutingTaskFilterService() {
        return getRequiredService(SSRoutingTaskFilterService.class);
    }

    public static EpgParametrageAdamantUIService getParametrageAdamantUIService() {
        return getRequiredService(EpgParametrageAdamantUIService.class);
    }

    public static EpgArchivageAdamantUIService getEpgArchivageAdamantUIService() {
        return getRequiredService(EpgArchivageAdamantUIService.class);
    }

    public static RequeteurService getRequeteurService() {
        return getRequiredService(RequeteurService.class);
    }

    public static EpgTraitementPapierUIService getEpgTraitementPapierUIService() {
        return getRequiredService(EpgTraitementPapierUIService.class);
    }
}
