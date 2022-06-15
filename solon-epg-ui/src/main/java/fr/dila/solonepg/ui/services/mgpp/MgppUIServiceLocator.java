package fr.dila.solonepg.ui.services.mgpp;

import static fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil.getRequiredService;

import fr.dila.st.ui.services.NotificationUIService;

public final class MgppUIServiceLocator {

    /**
     * Utility class
     */
    private MgppUIServiceLocator() {
        // do nothing
    }

    public static MgppCorbeilleUIService getMgppCorbeilleUIService() {
        return getRequiredService(MgppCorbeilleUIService.class);
    }

    public static MgppEvenementDetailsUIService getMgppEvenementDetailsUIservice() {
        return getRequiredService(MgppEvenementDetailsUIService.class);
    }

    public static MgppAdminUIService getMgppAdminUIService() {
        return getRequiredService(MgppAdminUIService.class);
    }

    public static MgppHistoriqueTreeUIService getMgppHistoriqueTreeUIService() {
        return getRequiredService(MgppHistoriqueTreeUIService.class);
    }

    public static MgppSelectValueUIService getMgppSelectValueUIService() {
        return getRequiredService(MgppSelectValueUIService.class);
    }

    public static MgppSuggestionUIService getMgppSuggestionUIService() {
        return getRequiredService(MgppSuggestionUIService.class);
    }

    public static MgppMetadonneeUIService getMgppMetadonneeUIService() {
        return getRequiredService(MgppMetadonneeUIService.class);
    }

    public static ModeleCourrierUIService getModeleCourrierUIService() {
        return getRequiredService(ModeleCourrierUIService.class);
    }

    public static MgppDossierUIService getMgppDossierUIService() {
        return getRequiredService(MgppDossierUIService.class);
    }

    public static MgppFicheUIService getMgppFicheUIService() {
        return getRequiredService(MgppFicheUIService.class);
    }

    public static MgppEvenementActionUIService getEvenementActionService() {
        return getRequiredService(MgppEvenementActionUIService.class);
    }

    public static NotificationUIService getNotificationUIService() {
        return getRequiredService(NotificationUIService.class);
    }

    public static MgppRechercheUIService getRechercheUIService() {
        return getRequiredService(MgppRechercheUIService.class);
    }

    public static MgppTableReferenceUIService getMgppTableReferenceUIService() {
        return getRequiredService(MgppTableReferenceUIService.class);
    }

    public static MgppCalendrierUIService getMgppCalendrierUIService() {
        return getRequiredService(MgppCalendrierUIService.class);
    }

    public static MgppGenerationFicheUIService getMgppGenerationFicheUIService() {
        return getRequiredService(MgppGenerationFicheUIService.class);
    }

    public static MgppFondDossierUIService getMgppFondDossierUIService() {
        return getRequiredService(MgppFondDossierUIService.class);
    }
}
