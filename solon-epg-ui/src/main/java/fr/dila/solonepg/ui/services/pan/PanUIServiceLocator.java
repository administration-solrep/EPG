package fr.dila.solonepg.ui.services.pan;

import static fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil.getRequiredService;

public final class PanUIServiceLocator {

    private PanUIServiceLocator() {
        // do nothing
    }

    public static ActiviteNormativeParamStatsUIService getActiviteNormativeParamStatsUIService() {
        return getRequiredService(ActiviteNormativeParamStatsUIService.class);
    }

    public static ActiviteNormativeStatsUIService getActiviteNormativeStatsUIService() {
        return getRequiredService(ActiviteNormativeStatsUIService.class);
    }

    public static ActiviteNormativeTraitesUIService getActiviteNormativeTraitesUIService() {
        return getRequiredService(ActiviteNormativeTraitesUIService.class);
    }

    public static PanUIService getPanUIService() {
        return getRequiredService(PanUIService.class);
    }

    public static ExportActiviteNormativeStatsUIService getExportActiviteNormativeStatsUIService() {
        return getRequiredService(ExportActiviteNormativeStatsUIService.class);
    }

    public static HistoriqueMajMinUIService getHistoriqueMajMinUIService() {
        return getRequiredService(HistoriqueMajMinUIService.class);
    }

    public static PanTreeUIService getPanTreeUIService() {
        return getRequiredService(PanTreeUIService.class);
    }

    public static TableauProgrammationUIService getTableauProgrammationUIService() {
        return getRequiredService(TableauProgrammationUIService.class);
    }

    public static TableauProgrammationCUIService getTableauProgrammation38CUIService() {
        return getRequiredService(TableauProgrammationCUIService.class);
    }

    public static TexteMaitreUIService getTexteMaitreUIService() {
        return getRequiredService(TexteMaitreUIService.class);
    }

    public static TexteMaitreHabilitationUIService getTexteMaitreHabilitationUIService() {
        return getRequiredService(TexteMaitreHabilitationUIService.class);
    }

    public static TexteMaitreOrdonnanceUIService getTexteMaitreOrdonnanceUIService() {
        return getRequiredService(TexteMaitreOrdonnanceUIService.class);
    }

    public static TexteMaitreTraitesUIService getTexteMaitreTraitesUIService() {
        return getRequiredService(TexteMaitreTraitesUIService.class);
    }

    public static TranspositionDirectiveUIService getTranspositionDirectiveUIService() {
        return getRequiredService(TranspositionDirectiveUIService.class);
    }

    public static PanStatistiquesUIService getPanStatistiquesUIService() {
        return getRequiredService(PanStatistiquesUIService.class);
    }

    public static PanJournalUIService getJournalUIService() {
        return getRequiredService(PanJournalUIService.class);
    }

    public static PanJournalAdminUIService getJournalAdminUIService() {
        return getRequiredService(PanJournalAdminUIService.class);
    }
}
