package fr.dila.solonepg.ui.services.actions;

import static fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil.getRequiredService;

public final class SolonEpgActionServiceLocator {

    /**
     * Utility class
     */
    private SolonEpgActionServiceLocator() {
        // do nothing
    }

    public static EpgModeleFeuilleRouteActionService getEpgModeleFeuilleRouteActionService() {
        return getRequiredService(EpgModeleFeuilleRouteActionService.class);
    }

    public static EpgCorbeilleActionService getEpgCorbeilleActionService() {
        return getRequiredService(EpgCorbeilleActionService.class);
    }

    public static EpgDocumentRoutingActionService getEpgDocumentRoutingActionService() {
        return getRequiredService(EpgDocumentRoutingActionService.class);
    }

    public static EpgDossierActionService getEpgDossierActionService() {
        return getRequiredService(EpgDossierActionService.class);
    }

    public static EpgDossierDistributionActionService getEpgDossierDistributionActionService() {
        return getRequiredService(EpgDossierDistributionActionService.class);
    }

    public static EpgGestionIndexationActionService getEpgGestionIndexationActionService() {
        return getRequiredService(EpgGestionIndexationActionService.class);
    }
}
