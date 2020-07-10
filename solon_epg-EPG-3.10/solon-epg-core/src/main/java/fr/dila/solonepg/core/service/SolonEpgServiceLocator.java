package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.ActiviteNormativeStatsService;
import fr.dila.solonepg.api.service.AmpliationService;
import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.ChangementGouvernementService;
import fr.dila.solonepg.api.service.CorbeilleService;
import fr.dila.solonepg.api.service.DndService;
import fr.dila.solonepg.api.service.DocumentRoutingService;
import fr.dila.solonepg.api.service.DossierBordereauService;
import fr.dila.solonepg.api.service.DossierDistributionService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.DossierSignaleService;
import fr.dila.solonepg.api.service.ElasticParametrageService;
import fr.dila.solonepg.api.service.EpgAlertService;
import fr.dila.solonepg.api.service.EpgInjectionGouvernementService;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.api.service.FeuilleRouteService;
import fr.dila.solonepg.api.service.FeuilleStyleService;
import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.HistoriqueMajMinisterielleService;
import fr.dila.solonepg.api.service.IndexationEpgService;
import fr.dila.solonepg.api.service.InformationsParlementairesService;
import fr.dila.solonepg.api.service.ListeTraitementPapierService;
import fr.dila.solonepg.api.service.MailboxService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.ParametrageAdamantService;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.api.service.ParapheurModelService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.api.service.PdfGeneratorService;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.api.service.RequeteurDossierSimpleService;
import fr.dila.solonepg.api.service.SchedulerService;
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
import fr.dila.solonepg.api.service.WsSpeService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.service.JetonService;
import fr.dila.st.core.util.ServiceUtil;

/**
 * ServiceLocator de l'application SOLON EPG : permet de retourner les services de l'application.
 * 
 * @author jtremeaux
 */
public class SolonEpgServiceLocator extends SSServiceLocator {

	/**
	 * Retourne le service Corbeille.
	 * 
	 * @return service Corbeille
	 */
	public static CorbeilleService getCorbeilleService() {
		CorbeilleService service = ServiceUtil.getService(CorbeilleService.class);

		return service;
	}

	/**
	 * Retourne le service EpgOrganigramme.
	 * 
	 * @return service EpgOrganigramme
	 */
	public static EpgOrganigrammeService getEpgOrganigrammeService() {
		EpgOrganigrammeService service = ServiceUtil.getService(EpgOrganigrammeService.class);

		return service;
	}

	/**
	 * Retourne le service Dossier.
	 * 
	 * @return service Dossier
	 */
	public static DossierService getDossierService() {
		DossierService service = ServiceUtil.getService(DossierService.class);

		return service;
	}

	/**
	 * 
	 * Retourne le service de jeton
	 * 
	 * @return service Jeton
	 */
	public static JetonService getJetonService() {
		return ServiceUtil.getService(JetonService.class);
	}

	/**
	 * Retourne le service DossierDistribution.
	 * 
	 * @return service DossierDistribution
	 */
	public static DossierDistributionService getDossierDistributionService() {
		DossierDistributionService service = ServiceUtil.getService(DossierDistributionService.class);

		return service;
	}

	/**
	 * Retourne le service FondDeDossier.
	 * 
	 * @return service FondDeDossier
	 */
	public static FondDeDossierService getFondDeDossierService() {
		FondDeDossierService service = ServiceUtil.getService(FondDeDossierService.class);

		return service;
	}

	/**
	 * Retourne le service TreeService.
	 * 
	 * @return service TreeService
	 */
	public static TreeService getTreeService() {
		TreeService service = ServiceUtil.getService(TreeService.class);

		return service;
	}

	/**
	 * Retourne le service FondDeDossierModelService.
	 * 
	 * @return service FondDeDossierModelService
	 */
	public static FondDeDossierModelService getFondDeDossierModelService() {
		FondDeDossierModelService service = ServiceUtil.getService(FondDeDossierModelService.class);

		return service;
	}

	/**
	 * Retourne le service ParapheurModelService.
	 * 
	 * @return service ParapheurModelService
	 */
	public static ParapheurModelService getParapheurModelService() {
		ParapheurModelService service = ServiceUtil.getService(ParapheurModelService.class);

		return service;
	}

	/**
	 * Retourne le service ParapheurService.
	 * 
	 * @return service ParapheurService
	 */
	public static ParapheurService getParapheurService() {
		ParapheurService service = ServiceUtil.getService(ParapheurService.class);

		return service;
	}

	/**
	 * Retourne le service FeuilleRouteModel.
	 * 
	 * @return service FeuilleRouteModel
	 */
	public static FeuilleRouteModelService getFeuilleRouteModelService() {
		FeuilleRouteModelService service = ServiceUtil.getService(FeuilleRouteModelService.class);

		return service;
	}

	/**
	 * Retourne le service FeuilleRoute.
	 * 
	 * @return service FeuilleRoute
	 */
	public static FeuilleRouteService getFeuilleRouteService() {
		FeuilleRouteService service = ServiceUtil.getService(FeuilleRouteService.class);

		return service;
	}

	/**
	 * Retourne le service Vocabulary de solon-epg.
	 * 
	 * @return service Vocabulary
	 */
	public static SolonEpgVocabularyService getSolonEpgVocabularyService() {
		SolonEpgVocabularyService service = ServiceUtil.getService(SolonEpgVocabularyService.class);

		return service;
	}

	/**
	 * Retourne le service de gestion des NOR
	 * 
	 * @return
	 */
	public static NORService getNORService() {
		NORService service = ServiceUtil.getService(NORService.class);

		return service;
	}

	/**
	 * Retourne le service de gestion des types d'actes
	 * 
	 * @return
	 */
	public static ActeService getActeService() {
		return ServiceUtil.getService(ActeService.class);
	}

	/**
	 * Retourne le service de gestion des feuilles de styles
	 * 
	 * @return service feuilles de styles
	 */
	public static FeuilleStyleService getFeuilleStyleService() {
		return ServiceUtil.getService(FeuilleStyleService.class);
	}

	/**
	 * Retourne le service de gestion des tables de reference
	 * 
	 * @return {@link TableReferenceService}
	 */
	public static TableReferenceService getTableReferenceService() {
		return ServiceUtil.getService(TableReferenceService.class);
	}

	/**
	 * Retourne le service de gestion des dossiers signalés de l'espace de suivi
	 * 
	 * @return {@link DossierSignaleService}
	 */
	public static DossierSignaleService getDossierSignaleService() {
		return ServiceUtil.getService(DossierSignaleService.class);
	}

	/**
	 * Retourne le service de gestion des bulletins officiels
	 * 
	 * @return {@link BulletinOfficielService}
	 */
	public static BulletinOfficielService getBulletinOfficielService() {
		return ServiceUtil.getService(BulletinOfficielService.class);
	}

	/**
	 * Retourne le service de gestion de l'indexation
	 * 
	 * @return {@link IndexationEpgService}
	 */
	public static IndexationEpgService getIndexationEpgService() {
		return ServiceUtil.getService(IndexationEpgService.class);
	}

	/**
	 * Retourne le service de gestion du profil utilisateur
	 * 
	 * @return {@link ProfilUtilisateurService}
	 */
	public static ProfilUtilisateurService getProfilUtilisateurService() {
		return ServiceUtil.getService(ProfilUtilisateurService.class);
	}

	/**
	 * Retourne le service de gestion du paramètrage de l'application
	 * 
	 * @return {@link ParametreApplicationService}
	 */
	public static ParametreApplicationService getParametreApplicationService() {
		return ServiceUtil.getService(ParametreApplicationService.class);
	}
	
	/**
	 * Retourne le service de gestion du paramètrage Adamant
	 * 
	 * @return {@link ParametrageAdamantService}
	 */
	public static ParametrageAdamantService getParametrageAdamantService() {
		return ServiceUtil.getService(ParametrageAdamantService.class);
	}

	/**
	 * Retourne le service de gestion de l'espace de recherche
	 * 
	 * @return {@link EspaceRechercheService}
	 */
	public static EspaceRechercheService getEspaceRechercheService() {
		return ServiceUtil.getService(EspaceRechercheService.class);
	}

	/**
	 * Retourne le service de gestion de l'archivage du dossier (suppression et export zip)
	 * 
	 * @return {@link ArchiveService}
	 */
	public static ArchiveService getArchiveService() {
		return ServiceUtil.getService(ArchiveService.class);
	}

	/**
	 * Retourne le service de gestion des tableaux dynamiques
	 * 
	 * @return {@link TableauDynamiqueService}
	 */
	public static TableauDynamiqueService getTableauDynamiqueService() {
		return ServiceUtil.getService(TableauDynamiqueService.class);
	}

	/**
	 * Retourne le service ListeTraitementPapierService.
	 * 
	 * @return service ListeTraitementPapierService
	 */
	public static ListeTraitementPapierService getListeTraitementPapierService() {
		return ServiceUtil.getService(ListeTraitementPapierService.class);
	}

	/**
	 * Retourne le service AmpliationService.
	 * 
	 * @return service AmpliationService
	 */
	public static AmpliationService getAmpliationService() {
		return ServiceUtil.getService(AmpliationService.class);
	}

	/**
	 * Retourne le service DossierBordereau.
	 * 
	 * @return service DossierBordereau
	 */
	public static DossierBordereauService getDossierBordereauService() {
		return ServiceUtil.getService(DossierBordereauService.class);
	}

	/**
	 * Retourne le service TraitementPapierService.
	 * 
	 * @return service TraitementPapierService
	 */
	public static TraitementPapierService getTraitementPapierService() {
		return ServiceUtil.getService(TraitementPapierService.class);
	}

	/**
	 * Retourne le service AlertConfirmationScheduler.
	 * 
	 * @return service AlertConfirmationScheduler
	 */
	public static EpgAlertService getAlertConfirmationSchedulerService() {
		return ServiceUtil.getService(EpgAlertService.class);
	}

	/**
	 * Retourne le service de gestion du scheduler
	 * 
	 * @return {@link SchedulerService}
	 */
	public static SchedulerService getSchedulerService() {
		return ServiceUtil.getService(SchedulerService.class);
	}

	/**
	 * Retourne le service de gestion des dossiers signalés de l'espace de suivi
	 * 
	 * @return {@link TexteSignaleService}
	 */
	public static TexteSignaleService getTexteSignaleService() {
		return ServiceUtil.getService(TexteSignaleService.class);
	}

	/**
	 * Retourne le service la recherche de dossier simple
	 * 
	 * @return
	 */
	public static RequeteurDossierSimpleService getRequeteurDossierSimpleService() {
		return ServiceUtil.getService(RequeteurDossierSimpleService.class);
	}

	/**
	 * Retourne le service de gestion de l'activite normative
	 * 
	 * @return {@link ActiviteNormativeService}
	 */
	public static ActiviteNormativeService getActiviteNormativeService() {
		return ServiceUtil.getService(ActiviteNormativeService.class);
	}

	/**
	 * Retourne le service permettant de créer et générer des documents pdf.
	 * 
	 * @return {@link PdfGeneratorService}
	 */
	public static PdfGeneratorService getPdfGeneratorService() {
		return ServiceUtil.getService(PdfGeneratorService.class);
	}

	/**
	 * Retourne le service de gestion de statistiques
	 * 
	 * @return {@link StatistiquesService}
	 */
	public static StatistiquesService getStatistiquesService() {
		return ServiceUtil.getService(StatistiquesService.class);
	}

	/**
	 * Retourne le service de gestion de la publication ( envoi des informations d'un dossier à publier à la dila via
	 * les webservices )
	 * 
	 * @return {@link WsSpeService}
	 */
	public static WsSpeService getWsSpeService() {
		return ServiceUtil.getService(WsSpeService.class);
	}

	/**
	 * Retourne le service de generation des statisque et de tableau de bord pour l'activite normative
	 * 
	 * @return {@link StatsGenerationResultatService}
	 */
	public static StatsGenerationResultatService getStatsGenerationResultatService() {
		return ServiceUtil.getService(StatsGenerationResultatService.class);
	}

	/**
	 * Retourne le service de gestion du changement de gouvernement.
	 * 
	 * @return {@link ChangementGouvernementService}
	 */
	public static ChangementGouvernementService getChangementGouvernementService() {
		return ServiceUtil.getService(ChangementGouvernementService.class);
	}

	/**
	 * Retourne le service de gestion des transpositions de directive européeene.
	 * 
	 * @return {@link TranspositionDirectiveService}
	 */
	public static TranspositionDirectiveService getTranspositionDirectiveService() {
		return ServiceUtil.getService(TranspositionDirectiveService.class);
	}

	/**
	 * Retourne le service de gestion des mailbox.
	 * 
	 * @return {@link MailboxService}
	 */
	public static MailboxService getMailboxServiceService() {
		return ServiceUtil.getService(MailboxService.class);
	}

	/**
	 * Retourne le service d'historique des mises à jour ministérielles
	 * 
	 * @return {@link HistoriqueMajMinisterielleService}
	 */
	public static HistoriqueMajMinisterielleService getHistoriqueMajMinisterielleService() {
		return ServiceUtil.getService(HistoriqueMajMinisterielleService.class);
	}

	/**
	 * Retourne le service de gestion de l'activite normative dédié au stats
	 * 
	 * @return {@link ActiviteNormativeStatsService}
	 */
	public static ActiviteNormativeStatsService getActiviteNormativeStatsService() {
		return ServiceUtil.getService(ActiviteNormativeStatsService.class);
	}

	/**
	 * Retourne le service des parametres
	 * 
	 * @return {@link SolonEpgParametreService}
	 */
	public static SolonEpgParametreService getSolonEpgParametreService() {
		return ServiceUtil.getService(SolonEpgParametreService.class);
	}

	/**
	 * Retourne le service d'injection de gouvernement
	 * 
	 * @return {@link EpgInjectionGouvernementService}
	 */
	public static EpgInjectionGouvernementService getEpgInjectionGouvernementService() {
		return ServiceUtil.getService(EpgInjectionGouvernementService.class);
	}

	/**
	 * Retourne le service DocumentRouting.
	 * 
	 * @return Service DocumentRouting
	 */
	public static DocumentRoutingService getDocumentRoutingService() {
		return ServiceUtil.getService(DocumentRoutingService.class);
	}

	/**
	 * Retourne le service dndService
	 * 
	 * @return service dndService
	 */
	public static DndService getDndService() {
		return ServiceUtil.getService(DndService.class);
	}

	public static ElasticParametrageService getElasticParametrageService() {
		return ServiceUtil.getService(ElasticParametrageService.class);
	}
	
	/**
	 * Retourne le service InformationsParlementaires
	 * 
	 * @return service InformationsParlementaires
	 */
	public static InformationsParlementairesService getInformationsParlementairesService() {
		return ServiceUtil.getService(InformationsParlementairesService.class);
	}

	protected SolonEpgServiceLocator() {
		// private default constructor
	}
}
