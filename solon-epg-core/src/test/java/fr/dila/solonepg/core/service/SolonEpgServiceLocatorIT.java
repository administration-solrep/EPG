package fr.dila.solonepg.core.service;

import static org.assertj.core.api.Assertions.assertThat;

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
import fr.dila.solonepg.api.service.vocabulary.TypeMesureService;
import fr.dila.solonepg.api.service.vocabulary.TypePublicationService;
import fr.dila.solonepg.api.service.vocabulary.TypeSignataireTraitementPapierService;
import fr.dila.solonepg.core.test.SolonEPGFeature;
import fr.dila.st.api.service.JetonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(SolonEPGFeature.class)
public class SolonEpgServiceLocatorIT {

    @Test
    public void testServices() {
        assertThat(SolonEpgServiceLocator.getCorbeilleService()).isInstanceOf(EpgCorbeilleService.class);
        assertThat(SolonEpgServiceLocator.getEpgOrganigrammeService()).isInstanceOf(EpgOrganigrammeService.class);
        assertThat(SolonEpgServiceLocator.getDossierService()).isInstanceOf(DossierService.class);
        assertThat(SolonEpgServiceLocator.getJetonService()).isInstanceOf(JetonService.class);
        assertThat(SolonEpgServiceLocator.getDossierDistributionService())
            .isInstanceOf(EpgDossierDistributionService.class);
        assertThat(SolonEpgServiceLocator.getFondDeDossierService()).isInstanceOf(FondDeDossierService.class);
        assertThat(SolonEpgServiceLocator.getTreeService()).isInstanceOf(TreeService.class);
        assertThat(SolonEpgServiceLocator.getFondDeDossierModelService()).isInstanceOf(FondDeDossierModelService.class);
        assertThat(SolonEpgServiceLocator.getParapheurModelService()).isInstanceOf(ParapheurModelService.class);
        assertThat(SolonEpgServiceLocator.getParapheurService()).isInstanceOf(ParapheurService.class);
        assertThat(SolonEpgServiceLocator.getFeuilleRouteModelService()).isInstanceOf(FeuilleRouteModelService.class);
        assertThat(SolonEpgServiceLocator.getEPGFeuilleRouteService()).isInstanceOf(EPGFeuilleRouteService.class);
        assertThat(SolonEpgServiceLocator.getSolonEpgVocabularyService()).isInstanceOf(SolonEpgVocabularyService.class);
        assertThat(SolonEpgServiceLocator.getNORService()).isInstanceOf(NORService.class);
        assertThat(SolonEpgServiceLocator.getActeService()).isInstanceOf(ActeService.class);
        assertThat(SolonEpgServiceLocator.getFeuilleStyleService()).isInstanceOf(FeuilleStyleService.class);
        assertThat(SolonEpgServiceLocator.getTableReferenceService()).isInstanceOf(TableReferenceService.class);
        assertThat(SolonEpgServiceLocator.getDossierSignaleService()).isInstanceOf(DossierSignaleService.class);
        assertThat(SolonEpgServiceLocator.getBulletinOfficielService()).isInstanceOf(BulletinOfficielService.class);
        assertThat(SolonEpgServiceLocator.getIndexationEpgService()).isInstanceOf(IndexationEpgService.class);
        assertThat(SolonEpgServiceLocator.getProfilUtilisateurService()).isInstanceOf(ProfilUtilisateurService.class);
        assertThat(SolonEpgServiceLocator.getParametreApplicationService())
            .isInstanceOf(ParametreApplicationService.class);
        assertThat(SolonEpgServiceLocator.getParametrageAdamantService()).isInstanceOf(ParametrageAdamantService.class);
        assertThat(SolonEpgServiceLocator.getParametrageParapheurService())
            .isInstanceOf(ParametrageParapheurService.class);
        assertThat(SolonEpgServiceLocator.getEspaceRechercheService()).isInstanceOf(EspaceRechercheService.class);
        assertThat(SolonEpgServiceLocator.getArchiveService()).isInstanceOf(ArchiveService.class);
        assertThat(SolonEpgServiceLocator.getTableauDynamiqueService()).isInstanceOf(TableauDynamiqueService.class);
        assertThat(SolonEpgServiceLocator.getListeTraitementPapierService())
            .isInstanceOf(ListeTraitementPapierService.class);
        assertThat(SolonEpgServiceLocator.getAmpliationService()).isInstanceOf(AmpliationService.class);
        assertThat(SolonEpgServiceLocator.getDossierBordereauService()).isInstanceOf(DossierBordereauService.class);
        assertThat(SolonEpgServiceLocator.getTraitementPapierService()).isInstanceOf(TraitementPapierService.class);
        assertThat(SolonEpgServiceLocator.getSchedulerService()).isInstanceOf(SchedulerService.class);
        assertThat(SolonEpgServiceLocator.getTexteSignaleService()).isInstanceOf(TexteSignaleService.class);
        assertThat(SolonEpgServiceLocator.getRequeteurDossierSimpleService())
            .isInstanceOf(RequeteurDossierSimpleService.class);
        assertThat(SolonEpgServiceLocator.getActiviteNormativeService()).isInstanceOf(ActiviteNormativeService.class);
        assertThat(SolonEpgServiceLocator.getWsBdjService()).isInstanceOf(WsBdjService.class);
        assertThat(SolonEpgServiceLocator.getPdfGeneratorService()).isInstanceOf(PdfGeneratorService.class);
        assertThat(SolonEpgServiceLocator.getStatistiquesService()).isInstanceOf(StatistiquesService.class);
        assertThat(SolonEpgServiceLocator.getWsSpeService()).isInstanceOf(WsSpeService.class);
        assertThat(SolonEpgServiceLocator.getStatsGenerationResultatService())
            .isInstanceOf(StatsGenerationResultatService.class);
        assertThat(SolonEpgServiceLocator.getChangementGouvernementService())
            .isInstanceOf(ChangementGouvernementService.class);
        assertThat(SolonEpgServiceLocator.getTranspositionDirectiveService())
            .isInstanceOf(TranspositionDirectiveService.class);
        assertThat(SolonEpgServiceLocator.getMailboxService()).isInstanceOf(EPGMailboxService.class);
        assertThat(SolonEpgServiceLocator.getHistoriqueMajMinisterielleService())
            .isInstanceOf(HistoriqueMajMinisterielleService.class);
        assertThat(SolonEpgServiceLocator.getActiviteNormativeStatsService())
            .isInstanceOf(ActiviteNormativeStatsService.class);
        assertThat(SolonEpgServiceLocator.getSolonEpgParametreService()).isInstanceOf(SolonEpgParametreService.class);
        assertThat(SolonEpgServiceLocator.getEpgInjectionGouvernementService())
            .isInstanceOf(EpgInjectionGouvernementService.class);
        assertThat(SolonEpgServiceLocator.getDocumentRoutingService()).isInstanceOf(EpgDocumentRoutingService.class);
        assertThat(SolonEpgServiceLocator.getElasticParametrageService()).isInstanceOf(ElasticParametrageService.class);
        assertThat(SolonEpgServiceLocator.getInformationsParlementairesService())
            .isInstanceOf(InformationsParlementairesService.class);
        assertThat(SolonEpgServiceLocator.getSolonEpgCorbeilleTreeService())
            .isInstanceOf(SolonEpgCorbeilleTreeService.class);
        assertThat(SolonEpgServiceLocator.getTypeMesureService()).isInstanceOf(TypeMesureService.class);
        assertThat(SolonEpgServiceLocator.getTypeActeService()).isInstanceOf(TypeActeService.class);
        assertThat(SolonEpgServiceLocator.getArchivageAdamantService()).isInstanceOf(EpgArchivageAdamantService.class);
        assertThat(SolonEpgServiceLocator.getCategorieActeService()).isInstanceOf(CategorieActeService.class);
        assertThat(SolonEpgServiceLocator.getPrioriteCEService()).isInstanceOf(PrioriteCEService.class);
        assertThat(SolonEpgServiceLocator.getDelaiPublicationService()).isInstanceOf(DelaiPublicationService.class);
        assertThat(SolonEpgServiceLocator.getEpgRoutingTaskTypeService()).isInstanceOf(EpgRoutingTaskTypeService.class);
        assertThat(SolonEpgServiceLocator.getResponsableAmendementService())
            .isInstanceOf(ResponsableAmendementService.class);
        assertThat(SolonEpgServiceLocator.getPoleChargeMissionService()).isInstanceOf(PoleChargeMissionService.class);
        assertThat(SolonEpgServiceLocator.getProcedureVoteService()).isInstanceOf(ProcedureVoteService.class);
        assertThat(SolonEpgServiceLocator.getNatureTexteService()).isInstanceOf(NatureTexteService.class);
        assertThat(SolonEpgServiceLocator.getNatureTexteBaseLegaleService())
            .isInstanceOf(NatureTexteBaseLegaleService.class);
        assertThat(SolonEpgServiceLocator.getStatutArchivageFacetService())
            .isInstanceOf(StatutArchivageFacetService.class);
        assertThat(SolonEpgServiceLocator.getTypePublicationService()).isInstanceOf(TypePublicationService.class);
        assertThat(SolonEpgServiceLocator.getDirectiveEtatService()).isInstanceOf(DirectiveEtatService.class);
        assertThat(SolonEpgServiceLocator.getBordereauLabelService()).isInstanceOf(BordereauLabelService.class);
        assertThat(SolonEpgServiceLocator.getTypeSignataireTraitementPapierService())
            .isInstanceOf(TypeSignataireTraitementPapierService.class);
        assertThat(SolonEpgServiceLocator.getPeriodiciteRapportService()).isInstanceOf(PeriodiciteRapportService.class);
        assertThat(SolonEpgServiceLocator.getEpgExcelService()).isInstanceOf(EpgExcelService.class);
        assertThat(SolonEpgServiceLocator.getPdfService()).isInstanceOf(EpgPdfDossierService.class);
    }
}
