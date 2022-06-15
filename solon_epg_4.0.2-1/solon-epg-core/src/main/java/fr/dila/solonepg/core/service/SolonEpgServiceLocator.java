package fr.dila.solonepg.core.service;

import static fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil.getRequiredService;

import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.ActiviteNormativeStatsService;
import fr.dila.solonepg.api.service.AmpliationService;
import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.ChangementGouvernementService;
import fr.dila.solonepg.api.service.DossierBordereauService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.DossierSignaleService;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.api.service.EPGMailboxService;
import fr.dila.solonepg.api.service.ElasticParametrageService;
import fr.dila.solonepg.api.service.EpgArchivageAdamantService;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.api.service.EpgDocumentRoutingService;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.api.service.EpgExcelService;
import fr.dila.solonepg.api.service.EpgInjectionGouvernementService;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.api.service.EpgPdfDossierService;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.api.service.FeuilleStyleService;
import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.HistoriqueMajMinisterielleService;
import fr.dila.solonepg.api.service.IndexationEpgService;
import fr.dila.solonepg.api.service.InformationsParlementairesService;
import fr.dila.solonepg.api.service.ListeTraitementPapierService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.ParametrageAdamantService;
import fr.dila.solonepg.api.service.ParametrageParapheurService;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.api.service.ParapheurModelService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.api.service.PdfGeneratorService;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.api.service.RequeteurDossierSimpleService;
import fr.dila.solonepg.api.service.SchedulerService;
import fr.dila.solonepg.api.service.SolonEpgCorbeilleTreeService;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.api.service.StatistiquesService;
import fr.dila.solonepg.api.service.StatsGenerationResultatService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.api.service.TableauDynamiqueService;
import fr.dila.solonepg.api.service.TexteSignaleService;
import fr.dila.solonepg.api.service.TraitementPapierService;
import fr.dila.solonepg.api.service.TranspositionDirectiveService;
import fr.dila.solonepg.api.service.TreeService;
import fr.dila.solonepg.api.service.WsBdjService;
import fr.dila.solonepg.api.service.WsSpeService;
import fr.dila.solonepg.api.service.vocabulary.BordereauLabelService;
import fr.dila.solonepg.api.service.vocabulary.CategorieActeService;
import fr.dila.solonepg.api.service.vocabulary.DelaiPublicationService;
import fr.dila.solonepg.api.service.vocabulary.DirectiveEtatService;
import fr.dila.solonepg.api.service.vocabulary.DispositionHabilitationService;
import fr.dila.solonepg.api.service.vocabulary.EpgRoutingTaskTypeService;
import fr.dila.solonepg.api.service.vocabulary.NatureTexteBaseLegaleService;
import fr.dila.solonepg.api.service.vocabulary.NatureTexteService;
import fr.dila.solonepg.api.service.vocabulary.PeriodiciteRapportService;
import fr.dila.solonepg.api.service.vocabulary.PoleChargeMissionService;
import fr.dila.solonepg.api.service.vocabulary.PrioriteCEService;
import fr.dila.solonepg.api.service.vocabulary.ProcedureVoteService;
import fr.dila.solonepg.api.service.vocabulary.ResponsableAmendementService;
import fr.dila.solonepg.api.service.vocabulary.StatutArchivageFacetService;
import fr.dila.solonepg.api.service.vocabulary.TypeActeService;
import fr.dila.solonepg.api.service.vocabulary.TypeAvisTraitementPapierService;
import fr.dila.solonepg.api.service.vocabulary.TypeMesureService;
import fr.dila.solonepg.api.service.vocabulary.TypePublicationService;
import fr.dila.solonepg.api.service.vocabulary.TypeSignataireTraitementPapierService;
import fr.dila.st.api.service.JetonService;
import fr.dila.solonepg.api.service.DndService;

/**
 * ServiceLocator de l'application SOLON EPG : permet de retourner les services de l'application.
 *
 * @author jtremeaux
 */
public final class SolonEpgServiceLocator {

    public static EpgCorbeilleService getCorbeilleService() {
        return getRequiredService(EpgCorbeilleService.class);
    }

    public static EpgOrganigrammeService getEpgOrganigrammeService() {
        return getRequiredService(EpgOrganigrammeService.class);
    }

    public static DndService getDndService() {
        return getRequiredService(DndService.class);
    }

    public static DossierService getDossierService() {
        return getRequiredService(DossierService.class);
    }

    public static JetonService getJetonService() {
        return getRequiredService(JetonService.class);
    }

    public static EpgDossierDistributionService getDossierDistributionService() {
        return getRequiredService(EpgDossierDistributionService.class);
    }

    public static FondDeDossierService getFondDeDossierService() {
        return getRequiredService(FondDeDossierService.class);
    }

    public static TreeService getTreeService() {
        return getRequiredService(TreeService.class);
    }

    public static FondDeDossierModelService getFondDeDossierModelService() {
        return getRequiredService(FondDeDossierModelService.class);
    }

    public static ParapheurModelService getParapheurModelService() {
        return getRequiredService(ParapheurModelService.class);
    }

    public static ParapheurService getParapheurService() {
        return getRequiredService(ParapheurService.class);
    }

    public static FeuilleRouteModelService getFeuilleRouteModelService() {
        return getRequiredService(FeuilleRouteModelService.class);
    }

    public static EPGFeuilleRouteService getEPGFeuilleRouteService() {
        return getRequiredService(EPGFeuilleRouteService.class);
    }

    public static SolonEpgVocabularyService getSolonEpgVocabularyService() {
        return getRequiredService(SolonEpgVocabularyService.class);
    }

    public static NORService getNORService() {
        return getRequiredService(NORService.class);
    }

    public static ActeService getActeService() {
        return getRequiredService(ActeService.class);
    }

    public static FeuilleStyleService getFeuilleStyleService() {
        return getRequiredService(FeuilleStyleService.class);
    }

    public static TableReferenceService getTableReferenceService() {
        return getRequiredService(TableReferenceService.class);
    }

    public static DossierSignaleService getDossierSignaleService() {
        return getRequiredService(DossierSignaleService.class);
    }

    public static BulletinOfficielService getBulletinOfficielService() {
        return getRequiredService(BulletinOfficielService.class);
    }

    public static IndexationEpgService getIndexationEpgService() {
        return getRequiredService(IndexationEpgService.class);
    }

    public static ProfilUtilisateurService getProfilUtilisateurService() {
        return getRequiredService(ProfilUtilisateurService.class);
    }

    public static ParametreApplicationService getParametreApplicationService() {
        return getRequiredService(ParametreApplicationService.class);
    }

    public static ParametrageAdamantService getParametrageAdamantService() {
        return getRequiredService(ParametrageAdamantService.class);
    }

    public static ParametrageParapheurService getParametrageParapheurService() {
        return getRequiredService(ParametrageParapheurService.class);
    }

    public static EspaceRechercheService getEspaceRechercheService() {
        return getRequiredService(EspaceRechercheService.class);
    }

    public static ArchiveService getArchiveService() {
        return getRequiredService(ArchiveService.class);
    }

    public static TableauDynamiqueService getTableauDynamiqueService() {
        return getRequiredService(TableauDynamiqueService.class);
    }

    public static ListeTraitementPapierService getListeTraitementPapierService() {
        return getRequiredService(ListeTraitementPapierService.class);
    }

    public static AmpliationService getAmpliationService() {
        return getRequiredService(AmpliationService.class);
    }

    public static DossierBordereauService getDossierBordereauService() {
        return getRequiredService(DossierBordereauService.class);
    }

    public static TraitementPapierService getTraitementPapierService() {
        return getRequiredService(TraitementPapierService.class);
    }

    public static SchedulerService getSchedulerService() {
        return getRequiredService(SchedulerService.class);
    }

    public static TexteSignaleService getTexteSignaleService() {
        return getRequiredService(TexteSignaleService.class);
    }

    public static RequeteurDossierSimpleService getRequeteurDossierSimpleService() {
        return getRequiredService(RequeteurDossierSimpleService.class);
    }

    public static ActiviteNormativeService getActiviteNormativeService() {
        return getRequiredService(ActiviteNormativeService.class);
    }

    public static WsBdjService getWsBdjService() {
        return getRequiredService(WsBdjService.class);
    }

    public static PdfGeneratorService getPdfGeneratorService() {
        return getRequiredService(PdfGeneratorService.class);
    }

    public static StatistiquesService getStatistiquesService() {
        return getRequiredService(StatistiquesService.class);
    }

    public static WsSpeService getWsSpeService() {
        return getRequiredService(WsSpeService.class);
    }

    public static StatsGenerationResultatService getStatsGenerationResultatService() {
        return getRequiredService(StatsGenerationResultatService.class);
    }

    public static ChangementGouvernementService getChangementGouvernementService() {
        return getRequiredService(ChangementGouvernementService.class);
    }

    public static TranspositionDirectiveService getTranspositionDirectiveService() {
        return getRequiredService(TranspositionDirectiveService.class);
    }

    public static EPGMailboxService getMailboxService() {
        return getRequiredService(EPGMailboxService.class);
    }

    public static HistoriqueMajMinisterielleService getHistoriqueMajMinisterielleService() {
        return getRequiredService(HistoriqueMajMinisterielleService.class);
    }

    public static ActiviteNormativeStatsService getActiviteNormativeStatsService() {
        return getRequiredService(ActiviteNormativeStatsService.class);
    }

    public static SolonEpgParametreService getSolonEpgParametreService() {
        return getRequiredService(SolonEpgParametreService.class);
    }

    public static EpgInjectionGouvernementService getEpgInjectionGouvernementService() {
        return getRequiredService(EpgInjectionGouvernementService.class);
    }

    /**
     * Retourne le service DocumentRouting.
     *
     * @return Service DocumentRouting
     */
    public static EpgDocumentRoutingService getDocumentRoutingService() {
        return getRequiredService(EpgDocumentRoutingService.class);
    }

    public static ElasticParametrageService getElasticParametrageService() {
        return getRequiredService(ElasticParametrageService.class);
    }

    public static InformationsParlementairesService getInformationsParlementairesService() {
        return getRequiredService(InformationsParlementairesService.class);
    }

    public static SolonEpgCorbeilleTreeService getSolonEpgCorbeilleTreeService() {
        return getRequiredService(SolonEpgCorbeilleTreeService.class);
    }

    public static TypeMesureService getTypeMesureService() {
        return getRequiredService(TypeMesureService.class);
    }

    public static TypeActeService getTypeActeService() {
        return getRequiredService(TypeActeService.class);
    }

    public static EpgArchivageAdamantService getArchivageAdamantService() {
        return getRequiredService(EpgArchivageAdamantService.class);
    }

    private SolonEpgServiceLocator() {
        // private default constructor
    }

    public static CategorieActeService getCategorieActeService() {
        return getRequiredService(CategorieActeService.class);
    }

    public static PrioriteCEService getPrioriteCEService() {
        return getRequiredService(PrioriteCEService.class);
    }

    public static DelaiPublicationService getDelaiPublicationService() {
        return getRequiredService(DelaiPublicationService.class);
    }

    public static EpgRoutingTaskTypeService getEpgRoutingTaskTypeService() {
        return getRequiredService(EpgRoutingTaskTypeService.class);
    }

    public static ResponsableAmendementService getResponsableAmendementService() {
        return getRequiredService(ResponsableAmendementService.class);
    }

    public static PoleChargeMissionService getPoleChargeMissionService() {
        return getRequiredService(PoleChargeMissionService.class);
    }

    public static ProcedureVoteService getProcedureVoteService() {
        return getRequiredService(ProcedureVoteService.class);
    }

    public static NatureTexteService getNatureTexteService() {
        return getRequiredService(NatureTexteService.class);
    }

    public static NatureTexteBaseLegaleService getNatureTexteBaseLegaleService() {
        return getRequiredService(NatureTexteBaseLegaleService.class);
    }

    public static DispositionHabilitationService getDispositionHabilitationService() {
        return getRequiredService(DispositionHabilitationService.class);
    }

    public static StatutArchivageFacetService getStatutArchivageFacetService() {
        return getRequiredService(StatutArchivageFacetService.class);
    }

    public static TypePublicationService getTypePublicationService() {
        return getRequiredService(TypePublicationService.class);
    }

    public static DirectiveEtatService getDirectiveEtatService() {
        return getRequiredService(DirectiveEtatService.class);
    }

    public static BordereauLabelService getBordereauLabelService() {
        return getRequiredService(BordereauLabelService.class);
    }

    public static TypeAvisTraitementPapierService getTypeAvisTraitementPapierService() {
        return getRequiredService(TypeAvisTraitementPapierService.class);
    }

    public static TypeSignataireTraitementPapierService getTypeSignataireTraitementPapierService() {
        return getRequiredService(TypeSignataireTraitementPapierService.class);
    }

    public static PeriodiciteRapportService getPeriodiciteRapportService() {
        return getRequiredService(PeriodiciteRapportService.class);
    }

    public static EpgExcelService getEpgExcelService() {
        return getRequiredService(EpgExcelService.class);
    }

    public static EpgPdfDossierService getPdfService() {
        return getRequiredService(EpgPdfDossierService.class);
    }
}
