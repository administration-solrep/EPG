package fr.dila.solonepg.ui.services;

import static fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil.getRequiredService;

import fr.dila.solonepg.elastic.services.RequeteurService;
import fr.dila.solonepg.ui.services.actions.EpgDossierDistributionActionService;
import fr.dila.solonepg.ui.services.actions.EpgRechercheModeleFeuilleRouteActionService;
import fr.dila.solonepg.ui.services.actions.suggestion.numeronor.NumeroNorSuggestionProviderService;
import fr.dila.solonepg.ui.services.pan.PanRequeteUIService;
import fr.dila.ss.ui.services.SSRoutingTaskFilterService;

public final class EpgUIServiceLocator {

    /**
     * Utility class
     */
    private EpgUIServiceLocator() {
        // do nothing
    }

    public static EpgDossierUIService getEpgDossierUIService() {
        return getRequiredService(EpgDossierUIService.class);
    }

    public static EpgFondDeDossierUIService getEpgFondDeDossierUIService() {
        return getRequiredService(EpgFondDeDossierUIService.class);
    }

    public static EpgParapheurUIService getEpgParapheurUIService() {
        return getRequiredService(EpgParapheurUIService.class);
    }

    public static EpgArchiveUIService getEpgArchiveUIService() {
        return getRequiredService(EpgArchiveUIService.class);
    }

    public static EpgDossierDistributionActionService getEpgDossierDistributionUIService() {
        return getRequiredService(EpgDossierDistributionActionService.class);
    }

    public static EpgDossierDistributionActionService getEpgDossierDistributionService() {
        return getRequiredService(EpgDossierDistributionActionService.class);
    }

    public static EpgBordereauUIService getBordereauUIService() {
        return getRequiredService(EpgBordereauUIService.class);
    }

    public static EpgRechercheUIService getEpgRechercheUIService() {
        return getRequiredService(EpgRechercheUIService.class);
    }

    public static EpgSelectionSummaryUIService getEpgSelectionSummaryUIService() {
        return getRequiredService(EpgSelectionSummaryUIService.class);
    }

    public static EpgMigrationGouvernementUIService getEpgMigrationGouvernementUIService() {
        return getRequiredService(EpgMigrationGouvernementUIService.class);
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

    public static SSRoutingTaskFilterService getEpgRoutingTaskFilterService() {
        return getRequiredService(SSRoutingTaskFilterService.class);
    }

    public static EpgParametrageAdamantUIService getParametrageAdamantUIService() {
        return getRequiredService(EpgParametrageAdamantUIService.class);
    }

    public static ParametreApplicationUIService getParametreApplicationUIService() {
        return getRequiredService(ParametreApplicationUIService.class);
    }

    public static EpgArchivageAdamantUIService getEpgArchivageAdamantUIService() {
        return getRequiredService(EpgArchivageAdamantUIService.class);
    }

    public static EpgParametrageParapheurUIService getEpgParametrageParapheurUIService() {
        return getRequiredService(EpgParametrageParapheurUIService.class);
    }

    public static RequeteurService getRequeteurService() {
        return getRequiredService(RequeteurService.class);
    }

    public static EpgFeuilleRouteUIService getEpgFeuilleRouteUIService() {
        return getRequiredService(EpgFeuilleRouteUIService.class);
    }

    public static OrganigrammeInjectionUIService getOrganigrammeInjectionUIService() {
        return getRequiredService(OrganigrammeInjectionUIService.class);
    }

    public static DossiersSimilairesUIService getDossiersSimilairesUIService() {
        return getRequiredService(DossiersSimilairesUIService.class);
    }

    public static ProfilUtilisateurAdministrationUIService getProfilUtilisateurAdministrationUIService() {
        return getRequiredService(ProfilUtilisateurAdministrationUIService.class);
    }

    public static EpgEspaceSuiviUIService getEpgEspaceSuiviUIService() {
        return getRequiredService(EpgEspaceSuiviUIService.class);
    }

    public static EpgSuiviMenuService getEpgSuiviMenuService() {
        return getRequiredService(EpgSuiviMenuService.class);
    }

    public static ElasticParametrageUIService getElasticParametrageUIService() {
        return getRequiredService(ElasticParametrageUIService.class);
    }

    public static EpgDossierCreationUIService getEpgDossierCreationUIService() {
        return getRequiredService(EpgDossierCreationUIService.class);
    }

    public static NumeroNorSuggestionProviderService getNumeroNorSuggestionProviderService() {
        return getRequiredService(NumeroNorSuggestionProviderService.class);
    }

    public static EpgSelectionToolUIService getEpgSelectionToolUIService() {
        return getRequiredService(EpgSelectionToolUIService.class);
    }

    public static EpgTraitementPapierListeUIService getEpgTraitementPapierListeUIService() {
        return getRequiredService(EpgTraitementPapierListeUIService.class);
    }

    public static EpgTraitementPapierUIService getEpgTraitementPapierUIService() {
        return getRequiredService(EpgTraitementPapierUIService.class);
    }

    public static EpgFavorisConsultationUIService getEpgFavorisConsultationUIService() {
        return getRequiredService(EpgFavorisConsultationUIService.class);
    }

    public static EpgModeleFdrListUIService getEpgModeleFdrListUIService() {
        return getRequiredService(EpgModeleFdrListUIService.class);
    }

    public static EpgUserListUIService getEpgUserListUIService() {
        return getRequiredService(EpgUserListUIService.class);
    }

    public static EpgSqueletteUIService getEpgSqueletteUIService() {
        return getRequiredService(EpgSqueletteUIService.class);
    }

    public static EpgAttenteSignatureUIService getEpgAttenteSignatureUIService() {
        return getRequiredService(EpgAttenteSignatureUIService.class);
    }

    public static EpgStatistiquesUIService getEpgStatistiquesUIService() {
        return getRequiredService(EpgStatistiquesUIService.class);
    }

    public static IndexationUIService getIndexationUIService() {
        return getRequiredService(IndexationUIService.class);
    }

    public static EpgAlerteUIService getEpgAlerteUIService() {
        return getRequiredService(EpgAlerteUIService.class);
    }

    public static EpgRequeteUIService getEpgRequeteUIService() {
        return getRequiredService(EpgRequeteUIService.class);
    }

    public static FavorisRechercheUIService getFavorisRechercheUIService() {
        return getRequiredService(FavorisRechercheUIService.class);
    }

    public static EPGSmartNXQLQueryUIService getEPGSmartNXQLQueryUIService() {
        return getRequiredService(EPGSmartNXQLQueryUIService.class);
    }

    public static FavorisRechercheModeleFeuilleRouteUIService getRechercheModeleFeuilleRouteUIService() {
        return getRequiredService(FavorisRechercheModeleFeuilleRouteUIService.class);
    }

    public static EpgRechercheModeleFeuilleRouteActionService getEpgRechercheModeleFeuilleRouteActionService() {
        return getRequiredService(EpgRechercheModeleFeuilleRouteActionService.class);
    }

    public static TableauDynamiqueUIService getTableauDynamiqueUIService() {
        return getRequiredService(TableauDynamiqueUIService.class);
    }

    public static EpgDossierTablesReferenceUIService getEpgDossierTablesReferenceUIService() {
        return getRequiredService(EpgDossierTablesReferenceUIService.class);
    }

    public static EpgVecteurPublicationUIService getEpgVecteurPublicationUIService() {
        return getRequiredService(EpgVecteurPublicationUIService.class);
    }

    public static PanRequeteUIService getPanRequeteUIService() {
        return getRequiredService(PanRequeteUIService.class);
    }

    public static EpgOrganigrammeTreeUIService getEpgOrganigrammeTreeUIService() {
        return getRequiredService(EpgOrganigrammeTreeUIService.class);
    }
}
