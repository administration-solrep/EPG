package fr.dila.solonepg.webtest.webdriver010;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import fr.dila.solonepg.page.administration.CreationEtapePage;
import fr.dila.solonepg.page.administration.CreationModelePage;
import fr.dila.solonepg.page.administration.FondDeDossierPage;
import fr.dila.solonepg.page.administration.GestionDeLindexationPage;
import fr.dila.solonepg.page.administration.GestionModelesPage;
import fr.dila.solonepg.page.administration.ParametrageDeLapplicationPage;
import fr.dila.solonepg.page.administration.ParapheurPage;
import fr.dila.solonepg.page.administration.TablesDeReferencePage;
import fr.dila.solonepg.page.administration.VecteursPublicationPage;
import fr.dila.solonepg.page.administration.ViewModelePage;
import fr.dila.solonepg.page.commun.ProfilUtilisateurPage;
import fr.dila.solonepg.page.creation.CreationDossier100Page;
import fr.dila.solonepg.page.creation.CreationDossier101Page;
import fr.dila.solonepg.page.creation.CreationDossier104Page;
import fr.dila.solonepg.page.creation.CreationDossier105Page;
import fr.dila.solonepg.page.creation.CreationDossierSelectPostePage;
import fr.dila.solonepg.page.main.onglet.AdministrationEpgPage;
import fr.dila.solonepg.page.main.onglet.CreationPage;
import fr.dila.solonepg.page.main.onglet.RecherchePage;
import fr.dila.solonepg.page.main.onglet.TraitementPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.BordereauPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.DossierDetailMenu;
import fr.dila.solonepg.page.main.onglet.dossier.detail.JournalPage;
import fr.dila.solonepg.page.parametre.ModifierParametre;
import fr.dila.solonepg.page.parametre.ParametreDetailPage;
import fr.dila.solonepg.page.recherche.CommonRechercheUtilisateurPage;
import fr.dila.solonepg.page.recherche.CreerAlertePage;
import fr.dila.solonepg.page.recherche.RechercheExpertePage;
import fr.dila.solonepg.page.recherche.RechercheUtilisateurPage;
import fr.dila.solonepg.page.recherche.ViewUtilisateurDetailsPage;
import fr.dila.solonepg.webtest.AbstractSolonSuite;
import fr.dila.solonepg.webtest.helper.url.UrlEpgHelper;
import fr.dila.st.annotations.TestDocumentation;
import fr.sword.naiad.commons.webtest.WebPage;
import fr.sword.naiad.commons.webtest.annotation.WebTest;

/**
 * Ici seuls les tests indépendants sont gardés : par indépendant, on entend qu'ils ne nécessitent pas que d'autres
 * tests soient passés en amont pour fonctionner
 * 
 */
public class TestSuite extends AbstractSolonSuite {

	private static final String VECTEUR_FAKE = "Vecteur bidon";
	final static String			BULLETIN_EFI	= "Bulletin officiel EFI";
	final static String			MINISTERE_EFI	= "EFI - Min. Economie, finances et industrie";
	private static final String	UTILISATEUR_BDC	= "bdc";

	public static String		NOR_DOSSIER_EFI;
	public static String		NOR_DOSSIER_CCO;
	public static String		NOR_DOSSIER_ACP;

	/**
	 * test Administration FDR defaut
	 * 
	 */
	@WebTest(description = "Administration FDR defaut", order = 10)
	@TestDocumentation(categories = { "FDR", "Administration" })
	public void testCase001() {

		maximizeBrowser();

		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		GestionModelesPage gestionModelesPage = administrationEpgPage.goToGestionModeles();
		CreationModelePage creationModelePage = gestionModelesPage.goToCreationModele();
		creationModelePage.setintituleLibre("Feuille de route standard");
		creationModelePage.setMinistere(MINISTERE_EFI);
		creationModelePage.setTypeActe("Avis");
		creationModelePage.checkDefault();
		// On gère le cas où le modèle a déjà été crée par ce test
		if (creationModelePage.creer() != null) {
			CreationEtapePage creationEtapePage = creationModelePage.ajouterEtape();
			creationEtapePage.setTypeEtape("Pour attribution");
			creationEtapePage
					.setDestinataire(Arrays
							.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3::min:handle:img:collapsed",
									"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3:node:0::dir:handle",
									"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3:node:0:node:0::addBtnMB"));
			creationModelePage = creationEtapePage.creer();

			creationEtapePage = creationModelePage.ajouterEtape();
			creationEtapePage.setTypeEtape("Pour attribution");
			creationEtapePage
					.setDestinataire(Arrays
							.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:14::min:handle:img:collapsed",
									"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:14:node:0::dir:handle",
									"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:14:node:0:node:0::addBtnMB"));
			creationModelePage = creationEtapePage.creer();

			ViewModelePage viewModelePage = creationModelePage.validerModele();
			gestionModelesPage = viewModelePage.retourListe();
		}
		gestionModelesPage.assertTrue("EFI - Avis - Feuille de route standard");
		gestionModelesPage.assertTrue("Min. Economie, finances et industrie");

		logoutEpg();
	}

	@WebTest(description = "duplication de modèle de fdr", order = 11)
	@TestDocumentation(categories = { "Administration", "FDR" })
	public void testCase001_1() {
		TraitementPage traitementPage = loginEpg(UTILISATEUR_BDC, UTILISATEUR_BDC, TraitementPage.class);
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		administrationEpgPage.assertTrue("Gestion des modèles");

		GestionModelesPage gestionModelesPage = administrationEpgPage.goToGestionModeles();
		CreationModelePage creationFDRPage = gestionModelesPage.dupliquer("Feuille de route par défaut");
		creationFDRPage.setintituleLibre("Economie - Feuille de route des ordonnances",
				"document_edit:nxl_feuille_route_detail_edit:nxw_feuille_route_intitule_libre");
		creationFDRPage.setTypeActe("Ordonnance",
				"document_edit:nxl_feuille_route_detail_edit:nxw_feuille_route_type_acte");
		creationFDRPage.enregistrer();
		CreationEtapePage etapePage = creationFDRPage.ajouterEtape();
		etapePage.setTypeEtape("Pour avis");
		etapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3:node:0::dir:handle",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3:node:0:node:0::addBtnMB"));
		creationFDRPage = etapePage.creer();
		creationFDRPage = creationFDRPage.ajouterBranche();
		etapePage = creationFDRPage
				.definirBranche(
						"dm_route_elements:nxl_document_routing_route_content_2:nxw_dr_listing_step_actions_2:titleref",
						"dm_route_elements:nxl_document_routing_route_content_2:nxw_dr_listing_step_actions_2:nxw_dr_listing_step_actions_2_add:addStepInForkActionList:0:step_0");

		etapePage.setTypeEtape("Pour impression");
		etapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3:node:0::dir:handle",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3:node:0:node:0::addBtnMB"));
		creationFDRPage = etapePage.creer();
		creationFDRPage
				.definirBranche(
						"dm_route_elements:nxl_document_routing_route_content_3:nxw_dr_listing_step_actions_3:titleref",
						"dm_route_elements:nxl_document_routing_route_content_3:nxw_dr_listing_step_actions_3:nxw_dr_listing_step_actions_3_add:addStepInForkActionList:0:step_0");
		etapePage.setTypeEtape("Pour avis");
		etapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:9::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:9:node:0::dir:handle",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:9:node:0:node:0::addBtnMB"));
		creationFDRPage = etapePage.creer();

		ViewModelePage vueModelFDR = creationFDRPage.demandeValidationSGG();
		vueModelFDR.assertTrue(" CCO - Ordonnance - Economie - Feuille de route des ordonnances");
		vueModelFDR.assertTrue("Ordonnance");
		vueModelFDR.assertTrue("Non");
		logoutEpg();

		traitementPage = loginAsAdminEpg();
		administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		administrationEpgPage.assertTrue("Gestion des modèles");
		gestionModelesPage = administrationEpgPage.goToGestionModeles();
		vueModelFDR = gestionModelesPage.ouvrir("CCO - Ordonnance - Economie - Feuille de route des ordonnances");
		vueModelFDR = vueModelFDR.libererVerrou();

		vueModelFDR.validerFDR();

		logoutEpg();

	}


	@WebTest(description = "ajout des modes de parution", order = 15)
	@TestDocumentation(categories = { "Administration", "TDR" })
	public void testCase001_2() {
		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		administrationEpgPage.assertTrue("Tables de référence");

		TablesDeReferencePage tablesDeReference = administrationEpgPage.goToTablesDeReference();
		tablesDeReference.ajouteModeParution("Electronique");
		tablesDeReference.ajouteModeParution("Mixte");
		tablesDeReference.ajouteModeParution("Papier");
		logoutEpg();
	}
	
	@WebTest(description = "ajout des vecteurs de publication", order = 16)
	@TestDocumentation(categories = { "Administration", "Publication" })
	public void testCase001_3() {
		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		administrationEpgPage.assertTrue("vecteurs de publication");
		VecteursPublicationPage vecteurPage = administrationEpgPage.goToVecteursPublication();
		vecteurPage.ajouterVecteurPublication(VECTEUR_FAKE);

		// On ajoute un autre vecteur de publication portant le même intitulé
		// cette opération n'est pas permise et va donner un message d'erreur
		vecteurPage.getElementById("vecteur_publication_properties:button_add_vecteur").click();
		vecteurPage.waitForPageSourcePart("Ajout d'un nouveau vecteur de publication", WebPage.TIMEOUT_IN_SECONDS);
		String inputId = "vecteur_publication_properties:vecteur_publication_content:nxl_vecteur_publication_listing";
		String inputIdEnd = ":nxw_vp_intitule";
		vecteurPage.waitForPageSourcePartDisplayed(By.id(inputId + inputIdEnd), WebPage.TIMEOUT_IN_SECONDS);
		List<WebElement> inputs = getDriver().findElements(
				By.xpath("//input[contains(@id, '" + inputId + "')][contains(@id, '" + inputIdEnd + "')]"));
		WebElement input = inputs.get(inputs.size() - 1);
		input.sendKeys(Keys.chord(Keys.CONTROL, "a"), VECTEUR_FAKE);
		vecteurPage.getElementById("vecteur_publication_properties:button_save_vecteur").click();
		vecteurPage.waitForPageSourcePart("Sauvegarde annulée", WebPage.TIMEOUT_IN_SECONDS);

		// On crée également un bulletin officiel, car depuis la FEV 391, il est obligatoire sur les dossiers pour
		// valider une étape de type "publication BO"
		vecteurPage.setMinistere(MINISTERE_EFI);
		vecteurPage.setIntitule(BULLETIN_EFI);
		vecteurPage.ajouterBulletin();
		
		vecteurPage.waitForPageSourcePart("EFI", WebPage.TIMEOUT_IN_SECONDS);
		
		// Tests suite à la création
		vecteurPage.assertTrue("EFI");
		vecteurPage.assertTrue(BULLETIN_EFI);

		logoutEpg();
	}

	/**
	 * test Initialisation dossiers
	 * 
	 */
	@WebTest(description = "Initialisation dossiers", order = 20)
	@TestDocumentation(categories = { "Dossier" })
	public void testCase002() {

		TraitementPage traitementPage = loginEpg("bdc", "bdc", TraitementPage.class);
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();
		creationDossier100Page.setTypeActe("Décret en Conseil d'Etat de l'article 37 second alinéa de la Constitution");
		creationDossier100Page.setMinistereResponable(MINISTERE_EFI);

		ArrayList<String> ar = new ArrayList<String>();
		ar.add("creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0::min:handle:img:collapsed");
		ar.add("creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0:node:5::nxw_direction_resp_field_addDir");

		creationDossier100Page.setDirectionConcerne(ar);

		// creationDossier100Page.assertTrue("DAJ (Economie)");
		CreationDossier101Page creationDossier101Page = creationDossier100Page
				.appuyerBoutonSuivant(CreationDossier101Page.class);
		creationDossier101Page.checkapplicationLoi();
		creationDossier101Page.checkTranspositionOrdonnance();
		CreationDossier104Page creationDossier104Page = creationDossier101Page
				.appuyerBoutonSuivant(CreationDossier104Page.class);

		creationDossier104Page.setReference("4444-1");
		creationDossier104Page.setTitre("Titre 1");
		creationDossier104Page.setNumeroArticle("1, 15, 87");
		creationDossier104Page.setNumeroOrdre("123");
		creationDossier104Page.setCommentaire("Commentaire 1");
		creationDossier104Page = creationDossier104Page.ajouterTransposition("4444-1");
		creationDossier104Page.setReference("1234-4321");
		creationDossier104Page.setTitre("Titre 2");
		creationDossier104Page.setNumeroArticle("5, 54, 87");
		creationDossier104Page.setNumeroOrdre("1");
		creationDossier104Page.setCommentaire("Commentaire 2");
		creationDossier104Page = creationDossier104Page.ajouterTransposition("1234-4321");
		CreationDossier105Page creationDossier105Page = creationDossier104Page
				.appuyerBoutonSuivant(CreationDossier105Page.class);
		creationDossier105Page.setReference("4567-2");
		creationDossier105Page.setTitre("Titre 1");
		creationDossier105Page.setNumeroArticle("1, 15, 87");
		creationDossier105Page.setCommentaire("Commentaire 1");
		creationDossier105Page = creationDossier105Page.ajouterTransposition("4567-2");
		creationDossier105Page.setReference("4568-3");
		creationDossier105Page.setTitre("Titre 2");
		creationDossier105Page.setNumeroArticle("9, 45, 86");
		creationDossier105Page.setCommentaire("Commentaire 2");
		creationDossier105Page = creationDossier105Page.ajouterTransposition("4568-3");
		creationPage = creationDossier105Page.appuyerBoutonTerminer(CreationPage.class);
		// On stocke le numéro de dossier créé
		NOR_DOSSIER_EFI = creationDossier105Page.waitAndGetNumeroDossierCree();

		logoutEpg();
	}

	/**
	 * test Initialisation dossiers
	 * 
	 * @throws UnexpectedException
	 * 
	 */
	@WebTest(description = "Initialisation dossiers affectation du poste", order = 30)
	@TestDocumentation(categories = { "Dossier" })
	public void testCase003() throws UnexpectedException {

		TraitementPage traitementPage = loginAsAdminEpg();
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();
		creationDossier100Page.setTypeActe("Amnistie");
		creationDossier100Page.setMinistereResponable("CCO - CCO");

		ArrayList<String> ar = new ArrayList<String>();
		ar.add("creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:3::min:handle:img:collapsed");
		ar.add("creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:3:node:0::nxw_direction_resp_field_addDir");

		creationDossier100Page.setDirectionConcerne(ar);

		creationPage = creationDossier100Page.appuyerBoutonTerminer(CreationPage.class);

		NOR_DOSSIER_CCO = creationDossier100Page.waitAndGetNumeroDossierCree();
		creationPage.assertTrue("CCOZ" + anneeCourante);

		creationPage.assertTrue("Parapheur");
		creationDossier100Page = creationPage.createDossier();
		creationDossier100Page.setTypeActe("Amnistie");
		creationDossier100Page.setMinistereResponable("ACP - Autorité contrôle prudentiel");

		ar = new ArrayList<String>();
		ar.add("creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:1::min:handle:img:collapsed");
		ar.add("creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:1:node:0::nxw_direction_resp_field_addDir");

		creationDossier100Page.setDirectionConcerne(ar);

		CreationDossierSelectPostePage creationDossierSelectPostePage = creationDossier100Page
				.appuyerBoutonTerminer(CreationDossierSelectPostePage.class);

		ar = new ArrayList<String>();
		ar.add("creation_dossier_select_poste:nxl_creation_dossier_layout_select_poste:nxw_dossier_creation_choix_poste_poste_tree:node:0:node:22::min:handle:img:collapsed");
		ar.add("creation_dossier_select_poste:nxl_creation_dossier_layout_select_poste:nxw_dossier_creation_choix_poste_poste_tree:node:0:node:22:node:0::dir:handle:img:collapsed");
		ar.add("creation_dossier_select_poste:nxl_creation_dossier_layout_select_poste:nxw_dossier_creation_choix_poste_poste_tree:node:0:node:22:node:0:node:0::addBtnPST");
		creationDossierSelectPostePage.setPoste(ar);
		creationPage = creationDossierSelectPostePage.appuyerBoutonTerminer(CreationPage.class);

		NOR_DOSSIER_ACP = creationDossierSelectPostePage.waitAndGetNumeroDossierCree();

		WebPage.sleep(5);

		creationPage.assertTrue("ACPP" + anneeCourante);

		logoutEpg();
	}

	/**
	 * test Initialisation dossiers
	 * 
	 * @throws UnexpectedException
	 * 
	 */
	@WebTest(description = "Administration Fond de Dossier", order = 40)
	@TestDocumentation(categories = { "FDD", "Administration" })
	public void testCase004() throws UnexpectedException {

		maximizeBrowser();

		TraitementPage traitementPage = loginEpg("bdc");
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : bdc");

		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		administrationEpgPage.assertFalse("Fond de dossier");
		logoutEpg();
		traitementPage = loginAsAdminEpg();

		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");

		administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		FondDeDossierPage fondDeDossierPage = administrationEpgPage.goToFondDeDossier();

		fondDeDossierPage.waitForPageSourcePart("doc", TIMEOUT_SEC);
		fondDeDossierPage.assertTrue("odi");
		fondDeDossierPage.assertTrue("jpg");
		fondDeDossierPage.assertTrue("png");
		fondDeDossierPage.assertTrue("pdf");
		fondDeDossierPage.assertTrue("rtf");
		fondDeDossierPage.assertTrue("odt");
		fondDeDossierPage.assertTrue("doc");
		fondDeDossierPage.assertTrue("docx");
		fondDeDossierPage.assertTrue("xls");
		fondDeDossierPage.assertTrue("xlsx");
		fondDeDossierPage.assertTrue("odp");
		fondDeDossierPage.assertTrue("ppt");
		fondDeDossierPage.assertTrue("pptx");
		fondDeDossierPage.assertTrue("odc");
		fondDeDossierPage.assertTrue("vsd");
		fondDeDossierPage.assertTrue("zip");

		// Pour que le script soit réentrant, on check si le nouveau format est présent
		final String nouveauFormat = "format";
		boolean dejaPasse = fondDeDossierPage.hasElement(By.xpath("//*[contains(text(), \"" + nouveauFormat + "\")]"));
		if (!dejaPasse) {
			fondDeDossierPage.assertTrue("odi");
			fondDeDossierPage.deleteFormat();
			fondDeDossierPage.ajoutFormat(nouveauFormat);
		}
		fondDeDossierPage.addtypeActe("amn", "Amnistie");
		fondDeDossierPage.editTypeActe("Amnistie");
		fondDeDossierPage.assertTrue("Répertoire réservé au ministère porteur");
		fondDeDossierPage.assertTrue("Répertoire réservé au SGG");
		fondDeDossierPage.assertTrue("Répertoire réservé au ministère porteur et au SGG");
		fondDeDossierPage.assertTrue("Répertoire accessible à tous les utilisateurs");
		fondDeDossierPage.assertTrue("Documentation jointe");
		fondDeDossierPage.assertTrue("Annexes non publiées");
		fondDeDossierPage.assertTrue("Note pour le ministère");
		fondDeDossierPage.assertTrue("Rapport de présentation");
		fondDeDossierPage.assertTrue("Lettres d'accord");
		fondDeDossierPage.assertTrue("Lettre de saisine du Conseil d'Etat");
		fondDeDossierPage.assertTrue("Avis divers");
		fondDeDossierPage.assertTrue("Textes de références");
		fondDeDossierPage.assertTrue("Etude d'impact");
		fondDeDossierPage.assertTrue("Divers");
		fondDeDossierPage.assertTrue("Etude d'impact");
		fondDeDossierPage.assertTrue("Textes consolidés");

		fondDeDossierPage.addtypeActe("ave", "Avenant");
		fondDeDossierPage.editTypeActe("Avenant");

		fondDeDossierPage.assertTrue("Répertoire réservé au ministère porteur");
		fondDeDossierPage.assertTrue("Répertoire réservé au SGG");
		fondDeDossierPage.assertTrue("Répertoire réservé au ministère porteur et au SGG");
		fondDeDossierPage.assertTrue("Répertoire accessible à tous les utilisateurs");
		fondDeDossierPage.assertTrue("Documentation jointe");
		fondDeDossierPage.assertTrue("Note pour le ministère");
		fondDeDossierPage.assertTrue("Rapport de présentation");
		fondDeDossierPage.assertTrue("Avis divers");
		fondDeDossierPage.assertTrue("Textes de références");
		fondDeDossierPage.assertTrue("Etude d'impact");
		fondDeDossierPage.assertTrue("Divers");
		fondDeDossierPage.assertTrue("Etude d'impact");
		fondDeDossierPage.assertTrue("Textes consolidés");

		if (!dejaPasse) {
			fondDeDossierPage.assertTrue("Annexes non publiées");

			fondDeDossierPage.ajouterRepertoireDedans(FondDeDossierPage.REPERTOIRE_RESERVE_MINISTERE_PORTEUR_PATH,
					false, "1:1:");

			// création d'un répertoire dans le répertoire venant d'être créé
			fondDeDossierPage.ajouterRepertoireDedans(FondDeDossierPage.NOUVEAU_REPERTOIRE_PATH, true, "1:1:1:");

			// création d'un répertoire avant le répertoire sélectionné
			fondDeDossierPage.ajouterRepertoireAvant(FondDeDossierPage.ANNEX_NON_PUBLIEE, true, "4:1:1:");

			// création d'un répertoire après le répertoire sélectionné
			fondDeDossierPage.ajouterRepertoireApres(FondDeDossierPage.ANNEX_NON_PUBLIEE, true, "4:1:3:");

			/*
			 * 5.1 édition document puis annulation clic action éditer
			 */

			fondDeDossierPage.modifierRepertoire(FondDeDossierPage.NOUVEAU_REPERTOIRE_AVANT_ANNEX, true);

			// Ajouter un nom
			fondDeDossierPage.setRepertoireName("Nouvelles Annexes");

			// clic bouton Annuler
			fondDeDossierPage.annluerEditionRepertoire();

			fondDeDossierPage.assertTrueExactMatch("Annexes non publiées");

			/*
			 * 5.2 édition document puis Validation clic action valider
			 */

			fondDeDossierPage.modifierRepertoire(FondDeDossierPage.NOUVEAU_REPERTOIRE_AVANT_ANNEX, true);
			// Ajouter un nom
			fondDeDossierPage.setRepertoireName("Nouvelles Annexes");

			// clic bouton Valider
			fondDeDossierPage.validerEditionRepertoire();

			fondDeDossierPage.assertTrueExactMatch("Nouvelles Annexes");

			/*
			 * 6.1 popup suppression puis annulation clic action Annuler
			 */

			// Supprimer Nouvelles Annexes reppertoire
			fondDeDossierPage.supprimerRepertoire(FondDeDossierPage.NOUVELLES_ANNEXES_PATH, false);

			fondDeDossierPage.assertTrueExactMatch("Nouvelles Annexes");

			/*
			 * 6.1 popup suppression puis confirmation de la suppresssion
			 */

			fondDeDossierPage.supprimerRepertoire(FondDeDossierPage.NOUVELLES_ANNEXES_PATH, true);

			fondDeDossierPage.assertFalseExactMatch("Nouvelles Annexes");
		}

		logoutEpg();
	}

	/**
	 * Administration Parapheur
	 */
	@WebTest(description = "Administration Parapheur", order = 50)
	@TestDocumentation(categories = { "Administration", "Parapheur" })
	public void testCase005() {
		TraitementPage traitementPage = loginEpg("bdc");
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : bdc");

		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		administrationEpgPage.assertFalse("Parapheur");
		logoutEpg();
		traitementPage = loginAsAdminEpg();

		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");

		administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		administrationEpgPage.assertTrue("Parapheur");

		ParapheurPage parapheurPage = administrationEpgPage.goToParapheur();
		parapheurPage.addtypeActe("Avi", "Avis");

		parapheurPage.editTypeActe("Avis");

		// Clic sur le lein Acte intégral
		parapheurPage.clickRepertoire(ParapheurPage.ACTE_INTEGRALE_PATH);

		parapheurPage.waitNonVideCheckBoxVisibility();

		parapheurPage.assertTrueExactMatch("Non vide");

		// Check Non Vide checkbox
		parapheurPage.clickNonVideCheckBox();

		// Click Valider Boutton
		parapheurPage.clickValiderBoutton();

		// Clic sur le lein Acte Extrait
		parapheurPage.clickRepertoire(ParapheurPage.EXTRAIT_PATH);

		// Make sure Non vide checkbox is not selected
		parapheurPage.isNonVideUnSelected();

		logoutEpg();
	}

	@WebTest(description = "Administration Parapheur/Tests et vérifications sur les arborescences pour les modèles de parapheur dans l'administration des parapheurs", order = 60)
	@TestDocumentation(categories = { "Administration", "Parapheur" })
	public void testCase005_1() {

		maximizeBrowser();

		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		ParapheurPage parapheurPage = administrationEpgPage.goToParapheur();

		parapheurPage.addtypeActe("amn", "Amnistie");

		parapheurPage.editTypeActe("Amnistie");

		// Clic sur le lein Acte intégral
		parapheurPage.clickRepertoire(ParapheurPage.ACTE_INTEGRALE_PATH);

		parapheurPage.assertTrueExactMatch("Acte intégral");

		parapheurPage.assertTrueExactMatch("Extrait");

		parapheurPage.assertTrueExactMatch("Pièces complémentaires à publier");

		// Clic sur le lein Acte Extrait
		parapheurPage.clickRepertoire(ParapheurPage.EXTRAIT_PATH);
		ParapheurPage.sleep(1);

		parapheurPage.addFormat("doc", 0);

		parapheurPage.addFormat("odt", 1);

		parapheurPage.clickValiderBoutton();
		ParapheurPage.sleep(1);

		// Clic sur le lein Acte Extrait Apres validation
		parapheurPage.clickRepertoire(ParapheurPage.EXTRAIT_PATH);

		parapheurPage
				.waitElementVisibilityByXpath("//*[contains(@id, 'nxw_format_autorise_parapheur_model_listItem')]");

		parapheurPage.assertTrueExactMatch("doc");

		parapheurPage.assertTrueExactMatch("odt");

		// Clic sur Annuler Bouton
		parapheurPage.clickAnnulerBoutton();

		// Clic sur le lien repertoire Acte Integrale
		parapheurPage.clickRepertoire(ParapheurPage.ACTE_INTEGRALE_PATH);

		//
		parapheurPage.assertTrueInputValue(ParapheurPage.PARAPHEUR_TITLE_ID, "Acte intégral");

		// Check Box Non Vide must be selected
		parapheurPage.isNonVideSelected();

		// On vérifie que par défaut aucun format n'est défini dans la liste des formats de fichiers autorisés par
		// défaut dans l'administration des parapheurs
		parapheurPage.assertFalseExactMatch("odg");
		parapheurPage.assertFalseExactMatch("odi");
		parapheurPage.assertFalseExactMatch("jpg");
		parapheurPage.assertFalseExactMatch("png");
		parapheurPage.assertFalseExactMatch("pdf");
		parapheurPage.assertFalseExactMatch("rtf");
		parapheurPage.assertFalseExactMatch("odt");
		parapheurPage.assertFalseExactMatch("docs");
		parapheurPage.assertFalseExactMatch("ods");
		parapheurPage.assertFalseExactMatch("xls");
		parapheurPage.assertFalseExactMatch("xlsx");
		parapheurPage.assertFalseExactMatch("odp");
		parapheurPage.assertFalseExactMatch("ppt");
		parapheurPage.assertFalseExactMatch("odc");
		parapheurPage.assertFalseExactMatch("vsd");
		parapheurPage.assertFalseExactMatch("zip");

		// edition des données du répertoires
		// edition de la propriété estVide

		parapheurPage.clickNonVideCheckBox();

		// ajouts de fichier feuille de style non valide
		parapheurPage.ajouterFeuilleDeStyle("word2003.doc");

		parapheurPage.assertTrueExactMatch(ParapheurPage.FEUILLE_DE_STYLE_INVALIDE);

		parapheurPage.ajouterFeuilleDeStyle("pdfTest.pdf");
		parapheurPage.assertTrueExactMatch(ParapheurPage.FEUILLE_DE_STYLE_INVALIDE);

		// Supprimer toutes les feuilles de style
		parapheurPage.supprimerFeuillesDeStyles();

		parapheurPage.assertFalseExactMatch(ParapheurPage.FEUILLE_DE_STYLE_INVALIDE);

		// 2eme test sur les feuilles de styles

		// ajouts de fichier feuille de style non valide
		parapheurPage.ajouterFeuilleDeStyle("FeuilleStyleValide1.odt");
		parapheurPage.assertFalseExactMatch(ParapheurPage.FEUILLE_DE_STYLE_INVALIDE);
		ParapheurPage.sleep(1);

		// ajout/définition de format de fichier autorisé
		// ajout fichier doc
		parapheurPage.addFormat("doc", 0);

		parapheurPage.addFormat("pdf", 1);

		parapheurPage.addFormat("odt", 2);

		// enregistrement des modifications
		parapheurPage.clickValiderBoutton();

		// Clic sur le lien repertoire Acte Integrale
		parapheurPage.clickRepertoire(ParapheurPage.ACTE_INTEGRALE_PATH);

		// vérification des propriétés
		parapheurPage.assertTrueExactMatch("Acte intégral");

		parapheurPage.isNonVideUnSelected();

		// on vérifie que les fichiers non valide n'ont pas été ajouté
		parapheurPage.assertFalse("word2003.doc");
		parapheurPage.assertFalse("pdfTest.pdf");
		parapheurPage.assertTrue("doc");

		logoutEpg();

	}

	@WebTest(description = "Administration Table de references", order = 70)
	@TestDocumentation(categories = { "TDR", "Administration" })
	public void testCase0055() {

		maximizeBrowser();

		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		administrationEpgPage.assertTrue("Tables de référence");

		TablesDeReferencePage tablesDeReference = administrationEpgPage.goToTablesDeReference();

		tablesDeReference.assertTrue("Autorités de validation");
		tablesDeReference.assertTrue("Signataires de la rubrique Communication");

		tablesDeReference.setCabinetDuPm(TablesDeReferencePage.CABINET_DU_PM_ARCEPUSER1_ID);
		tablesDeReference.assertTrue("ARCEPUSER1");

		tablesDeReference.setCabinetDuPm(TablesDeReferencePage.CABINET_DU_PM_ARCEPVIG1_ID);
		tablesDeReference.assertTrue("ARCEPVIG1");

		tablesDeReference.setChargeDuMission(TablesDeReferencePage.CHARGE_DU_MISSION_ARCEPUSER1_ID);
		tablesDeReference.assertTrue("ARCEPUSER1");

		tablesDeReference.setChargeDuMission(TablesDeReferencePage.CHARGE_DU_MISSION_ARCEPVIG1_ID);
		tablesDeReference.assertTrue("ARCEPVIG1");

		tablesDeReference.setSignataire(TablesDeReferencePage.SIGNATAIRE_ARCEPVIG1_ID);
		tablesDeReference.assertTrue("ARCEPVIG1");

		tablesDeReference.setSignataire(TablesDeReferencePage.SIGNATAIRE_ARCEPUSER1_ID);
		tablesDeReference.assertTrue("ARCEPUSER1");

		tablesDeReference.assertFalse("Premier Ministre");
		tablesDeReference.setMinistere(TablesDeReferencePage.PRM_PREMIER_MINISTERE_ID);
		tablesDeReference.assertTrue("Premier Ministre");

		tablesDeReference.setDirectionDuPm();
		tablesDeReference.assertTrue("Sec. gal défense et sécurité nationale");

		tablesDeReference.setConseilleDetat();
		tablesDeReference.assertTrue("Conseil d'État");

		tablesDeReference.setListDeDiffusion(TablesDeReferencePage.LISTE_DE_DIFFUSION_ARCEPUSER1_ID);
		tablesDeReference.assertTrue("ARCEPUSER1");

		// Enfin Enregistrer les modifications
		tablesDeReference.enregistrer();

		// Logout
		logoutEpg();
	}

	@WebTest(description = "Gestion des alertes", order = 180)
	@TestDocumentation(categories = { "Alerte" })
	public void testCase0071() {

		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");

		RecherchePage recherchePage = traitementPage.getOngletMenu().goToRecherche();

		RechercheExpertePage rechercheExpertePage = recherchePage.goToRechercheExperte();
		CreerAlertePage creerAlertePage = rechercheExpertePage.createNumeroDeNorQuery("C*");
		creerAlertePage.setintitule("à malibu");
		creerAlertePage.setValidtyBeginDate("27/06/11 12:00<");
		creerAlertePage.setValidtyEndDate("28/06/11 12:00");
		creerAlertePage.setPeriodcity("7");
		creerAlertePage.sauvegarder();

		logoutEpg();

	}

	@WebTest(description = "Liste des bulletins officiels", order = 200)
	@TestDocumentation(categories = { "Publication", "Administration" })
	public void testCase0090() {

		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		VecteursPublicationPage bulletinsOfficielsPage = administrationEpgPage.goToVecteursPublication();
		bulletinsOfficielsPage.setMinistere("CCO - CCO");
		bulletinsOfficielsPage.setIntitule("Bulletin officiel CCO N");
		bulletinsOfficielsPage.ajouterBulletin();

		WebElement reqSugg = bulletinsOfficielsPage
				.getElementById("bulletins_officiels_properties:nxl_bulletin_officiel_layout_nor:nxw_bo_nor_select_suggest");
		reqSugg.sendKeys("CCO");
		WebPage.sleep(5);
		bulletinsOfficielsPage
				.getElementByXpath(
						"//table[@id='bulletins_officiels_properties:nxl_bulletin_officiel_layout_nor:nxw_bo_nor_select_suggestionBox:suggest']//td[contains(., 'CCO')]")
				.click();
		WebPage.sleep(2);
		bulletinsOfficielsPage.setIntitule("Bulletin officiel CCO SL");
		bulletinsOfficielsPage.ajouterBulletin();

		logoutEpg();
		traitementPage = loginAsAdminEpg();
		administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		bulletinsOfficielsPage = administrationEpgPage.goToVecteursPublication();

		bulletinsOfficielsPage.assertTrue("Bulletin officiel CCO SL");
		bulletinsOfficielsPage.assertTrue("Bulletin officiel CCO N");

		logoutEpg();
	}

	@WebTest(description = "Gestion de l'indexation des rubriques", order = 210)
	@TestDocumentation(categories = { "Indexation", "Administration" })
	public void testCase0091() {

		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		GestionDeLindexationPage gestionDeLindexationPage = administrationEpgPage.goToGestionDeLindexation();

		gestionDeLindexationPage.addRubriques("Rubrique 1");
		gestionDeLindexationPage.waitForPageSourcePart("Rubrique 1", TIMEOUT_SEC);

		gestionDeLindexationPage.addRubriques("Rubrique de la pêche");
		gestionDeLindexationPage.waitForPageSourcePart("Rubrique de la pêche", TIMEOUT_SEC);

		gestionDeLindexationPage.addRubriques("Rubrique 3;Rubrique 4");
		gestionDeLindexationPage.assertTrue("Rubrique 3");
		gestionDeLindexationPage.assertTrue("Rubrique 4");

		logoutEpg();
	}

	@WebTest(description = "Ajout du mot-clé 'Tous - Liste 1'", order = 211)
	@TestDocumentation(categories = { "Indexation", "Administration" })
	public void testCase0092() {

		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		GestionDeLindexationPage gestionDeLindexationPage = administrationEpgPage.goToGestionDeLindexation();

		// Ajouter un mot cle
		getFlog().startAction("Ajoute le mot clé tous liste 1");
		gestionDeLindexationPage.addMotCle("Liste 1",
				Arrays.asList("tree:node:0::Tous:addBtnMin", "tree:node:0::Tous:addBtnMin"));
		gestionDeLindexationPage.waitForPageSourcePart("Tous - Liste 1", TIMEOUT_SEC);
		getFlog().endAction();
		logoutEpg();
	}

	@WebTest(description = "Ajout du mot-clé 'ACP - Liste 2'", order = 212)
	@TestDocumentation(categories = { "Indexation", "Administration" })
	public void testCase0093() {

		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		GestionDeLindexationPage gestionDeLindexationPage = administrationEpgPage.goToGestionDeLindexation();

		// Ajouter un mot cle
		getFlog().startAction("Ajoute le mot clé ACP - liste 2");

		gestionDeLindexationPage.addMotCle("Liste 2",
				Arrays.asList("tree:node:0::gvt:handle:img:collapsed", "tree:node:0:node:1::_min:addBtnMinN"));

		gestionDeLindexationPage.waitForPageSourcePart("ACP - Liste 2", TIMEOUT_SEC);
		getFlog().endAction();
		logoutEpg();
	}

	@WebTest(description = "Modification du mot-clé 'Tous - Liste 1' en 'CCO - Liste 1'", order = 213)
	@TestDocumentation(categories = { "Indexation", "Administration" })
	public void testCase0094() {

		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		GestionDeLindexationPage gestionDeLindexationPage = administrationEpgPage.goToGestionDeLindexation();
		// Clic sur Tous - Liste 1
		getFlog().startAction("Modifie le mot clé Tous - Liste 1 en CCO - Liste 1");
		getFlog().actionClickLink("Tous - Liste 1");
		gestionDeLindexationPage.waitForPageSourcePartDisplayed(By.id("tableMotsCles:1:_mc:setcommandLink"),
				TIMEOUT_SEC);
		gestionDeLindexationPage.getElementById("tableMotsCles:1:_mc:setcommandLink").click();
		gestionDeLindexationPage.waitForPageLoaded(getDriver());

		gestionDeLindexationPage.waitElementVisibilityById("tableMotsCles:1:_mcc:change_img");

		gestionDeLindexationPage.selectItemInOrganigramme("tableMotsCles:1:_mcc:change_img", Arrays.asList(
				"treeChange:node:0::gvt:handle:img:collapsed", "treeChange:node:0:node:3::_minC:_addMinN"));

		gestionDeLindexationPage.waitForPageSourcePart("CCO - Liste 1", TIMEOUT_SEC);
		getFlog().endAction();

		gestionDeLindexationPage.addMotCle_C("Mot cle1;Mot cle2;Mot cle3;Mot cle4");

		logoutEpg();
	}

	@WebTest(description = "Recherche Utilisateurs", order = 270)
	@TestDocumentation(categories = { "Recherche utilisateurs" })
	public void testCase0110() {

		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");

		RecherchePage recherchePage = traitementPage.getOngletMenu().goToRecherche();
		RechercheUtilisateurPage rechercheUtilisateurPage = recherchePage.goToRechercheUtilisateur();
		rechercheUtilisateurPage.setIdentifiant("finance*");
		CommonRechercheUtilisateurPage resultatRechercheUtilisateurPage = rechercheUtilisateurPage.rechercher();
		resultatRechercheUtilisateurPage.assertTrue("finance_bdc");
		resultatRechercheUtilisateurPage.assertTrue("Résultats de la recherche");
		ViewUtilisateurDetailsPage viewUtilisateurDetails = resultatRechercheUtilisateurPage
				.goToUserDetails("finance_bdc");
		rechercheUtilisateurPage = viewUtilisateurDetails.modifierRecherche();
		rechercheUtilisateurPage = rechercheUtilisateurPage.initialiserRecherche();

		ArrayList<String> ar = new ArrayList<String>();
		ar.add("searchUserForm:nxl_recherche_utilisateur:nxw_poste_tree:node:0:node:3::min:handle");
		ar.add("searchUserForm:nxl_recherche_utilisateur:nxw_poste_tree:node:0:node:3:node:0::dir:handle");
		ar.add("searchUserForm:nxl_recherche_utilisateur:nxw_poste_tree:node:0:node:3:node:0:node:0::addBtnPST");
		rechercheUtilisateurPage.setDirection(ar);
		rechercheUtilisateurPage.waitForPageSourcePart("Agents BDC (Economie)", TIMEOUT_SEC);
		resultatRechercheUtilisateurPage = rechercheUtilisateurPage.rechercher();
		resultatRechercheUtilisateurPage.waitForPageSourcePart("Résultats de la recherche", TIMEOUT_SEC);
		resultatRechercheUtilisateurPage.assertTrue("JCHIFFRE");
		resultatRechercheUtilisateurPage.assertTrue("MDALLARD");
		viewUtilisateurDetails = resultatRechercheUtilisateurPage.goToUserDetails("bdc");
		rechercheUtilisateurPage = viewUtilisateurDetails.modifierRecherche();
		logoutEpg();

	}

	@WebTest(description = "Parametrage de l'application", order = 320)
	@TestDocumentation(categories = { "Administration" })
	public void testCase0130() {
		maximizeBrowser();
		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		ParametrageDeLapplicationPage parametragePage = administrationEpgPage.goToParametrageDeLapplicationPage();

		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_nb_dossier_page:nxw_nb_dossier_page_from",
						"10");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_delai_rafraichissement_corbeille:nxw_delai_rafraichissement_corbeille_from",
						"5");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_derniers_resultats_consultes:nxw_nb_derniers_resultats_consultes_from",
						"30");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_consultation:nxw_nb_favoris_consultation_from",
						"30");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_recherche:nxw_nb_favoris_recherche_from",
						"30");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_dossiers_signales:nxw_nb_dossiers_signales_from",
						"100");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_tableaux_dynamiques:nxw_nb_tableaux_dynamiques_from",
						"10");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_delai_envoi_mail_alerte:nxw_delai_envoi_mail_alerte_from",
						"15");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_messages:nxw_delai_affichage_message:nxw_delai_affichage_message_from",
						"5");

		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_nb_dossier_page:nxw_nb_dossier_page_from",
						"-1");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_delai_rafraichissement_corbeille:nxw_delai_rafraichissement_corbeille_from",
						"-1");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_derniers_resultats_consultes:nxw_nb_derniers_resultats_consultes_from",
						"-1");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_consultation:nxw_nb_favoris_consultation_from",
						"-1");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_recherche:nxw_nb_favoris_recherche_from",
						"-1");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_dossiers_signales:nxw_nb_dossiers_signales_from",
						"-1");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_tableaux_dynamiques:nxw_nb_tableaux_dynamiques_from",
						"-1");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_delai_envoi_mail_alerte:nxw_delai_envoi_mail_alerte_from",
						"-1");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_messages:nxw_delai_affichage_message:nxw_delai_affichage_message_from",
						"-1");

		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_infosparl:nxw_parametrage_infosParl_urlEpp",
						"aaa");

		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_infosparl:nxw_parametrage_infosParl_loginEpp",
						"aaa");

		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_infosparl:nxw_parametrage_infosParl_passEpp",
						"aaa");

		// Valider
		parametragePage.valider();
		parametragePage = administrationEpgPage.goToParametrageDeLapplicationPage();

		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_nb_dossier_page:nxw_nb_dossier_page_from",
						"5");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_delai_rafraichissement_corbeille:nxw_delai_rafraichissement_corbeille_from",
						"1");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_derniers_resultats_consultes:nxw_nb_derniers_resultats_consultes_from",
						"1");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_consultation:nxw_nb_favoris_consultation_from",
						"1");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_recherche:nxw_nb_favoris_recherche_from",
						"1");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_dossiers_signales:nxw_nb_dossiers_signales_from",
						"1");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_tableaux_dynamiques:nxw_nb_tableaux_dynamiques_from",
						"1");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_delai_envoi_mail_alerte:nxw_delai_envoi_mail_alerte_from",
						"1");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_messages:nxw_delai_affichage_message:nxw_delai_affichage_message_from",
						"1");

		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_infosparl:nxw_parametrage_infosParl_urlEpp",
						"aaa");

		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_infosparl:nxw_parametrage_infosParl_loginEpp",
						"aaa");

		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_nb_dossier_page:nxw_nb_dossier_page_from",
						"1000");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_delai_rafraichissement_corbeille:nxw_delai_rafraichissement_corbeille_from",
						"1000");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_derniers_resultats_consultes:nxw_nb_derniers_resultats_consultes_from",
						"1000");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_consultation:nxw_nb_favoris_consultation_from",
						"1000");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_recherche:nxw_nb_favoris_recherche_from",
						"1000");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_dossiers_signales:nxw_nb_dossiers_signales_from",
						"1000");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_tableaux_dynamiques:nxw_nb_tableaux_dynamiques_from",
						"1000");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_delai_envoi_mail_alerte:nxw_delai_envoi_mail_alerte_from",
						"1000");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_messages:nxw_delai_affichage_message:nxw_delai_affichage_message_from",
						"1000");

		// Valider
		parametragePage.valider();
		parametragePage = administrationEpgPage.goToParametrageDeLapplicationPage();

		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_nb_dossier_page:nxw_nb_dossier_page_from",
						"100");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_delai_rafraichissement_corbeille:nxw_delai_rafraichissement_corbeille_from",
						"10");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_derniers_resultats_consultes:nxw_nb_derniers_resultats_consultes_from",
						"100");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_consultation:nxw_nb_favoris_consultation_from",
						"100");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_recherche:nxw_nb_favoris_recherche_from",
						"100");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_dossiers_signales:nxw_nb_dossiers_signales_from",
						"150");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_tableaux_dynamiques:nxw_nb_tableaux_dynamiques_from",
						"100");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_delai_envoi_mail_alerte:nxw_delai_envoi_mail_alerte_from",
						"100");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_messages:nxw_delai_affichage_message:nxw_delai_affichage_message_from",
						"100");

		// Test des valeurs nulles
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_consultation:nxw_nb_favoris_consultation_from",
						"");
		parametragePage.valider(false);
		parametragePage.waitForPageSourcePart("Un paramètre obligatoire est vide !", TIMEOUT_SEC);

		//
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_consultation:nxw_nb_favoris_consultation_from",
						"-1");

		// Ajouter Aux metadonnees

		parametragePage.ajouterMetadonnee("Créé par");
		parametragePage.ajouterMetadonnee("Statut");
		// parametragePage.ajouterMetadonnee("Type d'acte");

		// Valider
		parametragePage.valider();

		// Retour à l'écran de paramètrage
		parametragePage = administrationEpgPage.goToParametrageDeLapplicationPage();

		// on remet les valeurs par défaut
		parametragePage.enleverMetadonnee("Créé par");
		parametragePage.enleverMetadonnee("Statut");
		// parametragePage.enleverMetadonnee("Type d'acte");

		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_nb_dossier_page:nxw_nb_dossier_page_from",
						"50");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_delai_rafraichissement_corbeille:nxw_delai_rafraichissement_corbeille_from",
						"5");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_derniers_resultats_consultes:nxw_nb_derniers_resultats_consultes_from",
						"30");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_consultation:nxw_nb_favoris_consultation_from",
						"30");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_recherche:nxw_nb_favoris_recherche_from",
						"30");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_dossiers_signales:nxw_nb_dossiers_signales_from",
						"100");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_tableaux_dynamiques:nxw_nb_tableaux_dynamiques_from",
						"10");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_delai_envoi_mail_alerte:nxw_delai_envoi_mail_alerte_from",
						"15");
		parametragePage
				.setParamValue(
						"parametrage_application_properties:nxl_parametrage_application_messages:nxw_delai_affichage_message:nxw_delai_affichage_message_from",
						"5");

		// Valider
		parametragePage.valider();

		// Retour à l'écran de paramètrage
		parametragePage = administrationEpgPage.goToParametrageDeLapplicationPage();

		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_nb_dossier_page:nxw_nb_dossier_page_from",
						"50");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_corbeille:nxw_delai_rafraichissement_corbeille:nxw_delai_rafraichissement_corbeille_from",
						"5");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_derniers_resultats_consultes:nxw_nb_derniers_resultats_consultes_from",
						"30");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_consultation:nxw_nb_favoris_consultation_from",
						"30");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_recherche:nxw_nb_favoris_recherche:nxw_nb_favoris_recherche_from",
						"30");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_dossiers_signales:nxw_nb_dossiers_signales_from",
						"100");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_nb_tableaux_dynamiques:nxw_nb_tableaux_dynamiques_from",
						"10");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_suivi:nxw_delai_envoi_mail_alerte:nxw_delai_envoi_mail_alerte_from",
						"15");
		parametragePage
				.assertValue(
						"parametrage_application_properties:nxl_parametrage_application_messages:nxw_delai_affichage_message:nxw_delai_affichage_message_from",
						"5");

	}

	@WebTest(description = "Paramétrage technique de l'url envoyée par mèl", order = 330)
	@TestDocumentation(categories = { "Administration" })
	public void testCase0131() {
		loginAsAdminEpg();
		getFlog().startAction("Mise à jour de l'url envoyée par mail");
		String url = "solon-epg/nxpath/default/case-management/workspaces/admin/parametre/url-application-transmise-par-mel@view_parametre";
		url = UrlEpgHelper.getInstance().getAppUrl() + url;
		ParametreDetailPage parametreTechnique = WebPage.goToPage(getDriver(), getFlog(), url,
				ParametreDetailPage.class);
		ModifierParametre parametre = parametreTechnique.modifier();
		parametre.setValeur(UrlEpgHelper.getInstance().getAppUrl() + "solon-epg");
		parametre.enregistrer();
		getFlog().endAction();
		logoutEpg();
	}

	@WebTest(description = "Profil utilisateur", order = 340)
	@TestDocumentation(categories = { "Utilisateurs", "Droits et profils" })
	public void testCase0140() {
		maximizeBrowser();
		TraitementPage traitementPage = loginEpg("bdc");
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : bdc");

		ProfilUtilisateurPage profilUtilisateurPage = traitementPage.openProfilUtilisateurByButton();
		profilUtilisateurPage.annuler();
		profilUtilisateurPage = traitementPage.openProfilUtilisateurByLink();
		profilUtilisateurPage.annuler();

		profilUtilisateurPage = traitementPage.openProfilUtilisateurByButton();

		// vérification des propriétés par défaut : case à cocher notification
		profilUtilisateurPage.assertNotChecked(ProfilUtilisateurPage.DOSSIER_URGENT_ID);
		profilUtilisateurPage.assertNotChecked(ProfilUtilisateurPage.RETOUR_SGG_ID);
		profilUtilisateurPage.assertNotChecked(ProfilUtilisateurPage.NOTIFIER_PAR_MEL);

		// on configure que 50 dossiers doivent s'afficher dans les corbeilles
		profilUtilisateurPage.setList("Nombre de dossiers par page", ProfilUtilisateurPage.NB_DOSSIER_PAGE_ID, "50");
		profilUtilisateurPage.waitForFieldValue(ProfilUtilisateurPage.NB_DOSSIER_PAGE_ID, "50");

		// on coche toutes les options de sélection
		profilUtilisateurPage.selectCheckBox(ProfilUtilisateurPage.DOSSIER_URGENT_ID);
		profilUtilisateurPage.waitElementSelectedById(ProfilUtilisateurPage.DOSSIER_URGENT_ID);
		profilUtilisateurPage.selectCheckBox(ProfilUtilisateurPage.RETOUR_SGG_ID);
		profilUtilisateurPage.waitElementSelectedById(ProfilUtilisateurPage.RETOUR_SGG_ID);
		profilUtilisateurPage.selectCheckBox(ProfilUtilisateurPage.NOTIFIER_PAR_MEL);
		profilUtilisateurPage.waitElementSelectedById(ProfilUtilisateurPage.NOTIFIER_PAR_MEL);
		profilUtilisateurPage.addTypeActe("amn", "Amnistie");
		// Valider les modifications
		profilUtilisateurPage.valider();

		// Ouvrir le popup de nouveau
		profilUtilisateurPage.openProfilUtilisateurByButton();
		profilUtilisateurPage.checkValue(ProfilUtilisateurPage.NB_DOSSIER_PAGE_ID, "50");
		profilUtilisateurPage.assertChecked(ProfilUtilisateurPage.DOSSIER_URGENT_ID);
		profilUtilisateurPage.assertChecked(ProfilUtilisateurPage.RETOUR_SGG_ID);
		profilUtilisateurPage.assertChecked(ProfilUtilisateurPage.NOTIFIER_PAR_MEL);

		// on décoche toutes les options de sélection
		profilUtilisateurPage.selectCheckBox(ProfilUtilisateurPage.DOSSIER_URGENT_ID);
		profilUtilisateurPage.waitElementNotSelectedById(ProfilUtilisateurPage.DOSSIER_URGENT_ID);
		profilUtilisateurPage.selectCheckBox(ProfilUtilisateurPage.RETOUR_SGG_ID);
		profilUtilisateurPage.waitElementNotSelectedById(ProfilUtilisateurPage.RETOUR_SGG_ID);
		profilUtilisateurPage.selectCheckBox(ProfilUtilisateurPage.NOTIFIER_PAR_MEL);
		profilUtilisateurPage.waitElementNotSelectedById(ProfilUtilisateurPage.NOTIFIER_PAR_MEL);
		profilUtilisateurPage.removeTypeActe();
		// Valider les modifications
		profilUtilisateurPage.valider();

		logoutEpg();

	}

	@WebTest(description = "Profil utilisateur/Administration > connection adminsgg", order = 350)
	@TestDocumentation(categories = { "Utilisateurs", "Administration" })
	public void testCase0140_1() {
		maximizeBrowser();
		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");

		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		ParametrageDeLapplicationPage parametragePage = administrationEpgPage.goToParametrageDeLapplicationPage();

		parametragePage.assertTrue("NOR");
		parametragePage.assertTrue("Date d'arrivée");
		parametragePage.assertTrue("Titre de l'acte");
		parametragePage.assertTrue("Dernier contributeur");
		parametragePage.assertTrue("Créé par");
		parametragePage.assertTrue("Statut");
		parametragePage.assertTrue("Type d'acte");
		parametragePage.assertTrue("Entité à l'origine du projet");
		parametragePage.assertTrue("Direction concernée");
		parametragePage.assertTrue("Ministère resp.");
		parametragePage.assertTrue("Direction resp.");
		parametragePage.assertTrue("Ministère rattach.");
		parametragePage.assertTrue("Direction rattach.");
		parametragePage.assertTrue("Nom du responsable du dossier");
		parametragePage.assertTrue("Prénom resp. dossier");
		parametragePage.assertTrue("Qualité resp. dossier");
		parametragePage.assertTrue("Tél. resp. dossier");
		parametragePage.assertTrue("Mél resp. dossier");
		parametragePage.assertTrue("Catégorie acte");
		parametragePage.assertTrue("Base légale");
		parametragePage.assertTrue("Date de publication souhaitée");
		parametragePage.assertTrue("Publication du rapport de présentation");
		parametragePage.assertTrue("Section du CE");
		parametragePage.assertTrue("Rapporteur");
		parametragePage.assertTrue("Date transm. section CE");
		parametragePage.assertTrue("Date prévisionnelle section CE");
		parametragePage.assertTrue("Date prévisionnelle AG CE");
		parametragePage.assertTrue("Numéro ISA");
		parametragePage.assertTrue("Chargé de mission");
		parametragePage.assertTrue("Conseiller PM");
		parametragePage.assertTrue("Date signature");
		parametragePage.assertTrue("Pour fourniture d'épreuve");
		parametragePage.assertTrue("Vecteur de publication");
		parametragePage.assertTrue("Publication conjointe");
		parametragePage.assertTrue("Publication intégrale ou par extrait");
		parametragePage.assertTrue("Décret numéroté");
		parametragePage.assertTrue("Mode parution");
		parametragePage.assertTrue("Délai publication");
		parametragePage.assertTrue("Publication à date précisée");
		parametragePage.assertTrue("Date JO");
		parametragePage.assertTrue("N° du texte JO");
		parametragePage.assertTrue("Page JO");
		parametragePage.assertTrue("Parutions BO");
		parametragePage.assertTrue("Titre publié");
		parametragePage.assertTrue("Zone commentaire SGG-DILA");
		parametragePage.assertTrue("Rubrique");
		parametragePage.assertTrue("Mots Cles");
		parametragePage.assertTrue("Champ Libre");
		parametragePage.assertTrue("Réf. Lois Appliquées");
		parametragePage.assertTrue("Réf. Ordonnances");
		parametragePage.assertTrue("Réf. Directives Europeennes");
		parametragePage.assertTrue("Réf. Habilitation");
		parametragePage.assertTrue("N° Article Habilitation");
		parametragePage.assertTrue("Commentaire Habilitation");

		// on rajoute toutes les colonnes existantes
		parametragePage.ajouterTout();
		// sauvegarde du document
		parametragePage.valider();

		logoutEpg();
	}

	@WebTest(description = "Profil utilisateur/Connection bdc", order = 360)
	@TestDocumentation(categories = { "Utilisateurs", "Droits et profils" })
	public void testCase0140_2() {
		maximizeBrowser();
		TraitementPage traitementPage = loginEpg("bdc");
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : bdc");

		ProfilUtilisateurPage profilUtilisateurPage = traitementPage.openProfilUtilisateurByButton();
		profilUtilisateurPage.assertTrue("NOR");
		profilUtilisateurPage.assertTrue("Date d'arrivée");
		profilUtilisateurPage.assertTrue("Titre de l'acte");
		profilUtilisateurPage.assertTrue("Dernier contributeur");
		profilUtilisateurPage.assertTrue("Créé par");
		profilUtilisateurPage.assertTrue("Statut");
		profilUtilisateurPage.assertTrue("Type d'acte");
		profilUtilisateurPage.assertTrue("Entité à l'origine du projet");
		profilUtilisateurPage.assertTrue("Direction concernée");
		profilUtilisateurPage.assertTrue("Ministère resp.");
		profilUtilisateurPage.assertTrue("Direction resp.");
		profilUtilisateurPage.assertTrue("Ministère rattach.");
		profilUtilisateurPage.assertTrue("Direction rattach.");
		profilUtilisateurPage.assertTrue("Nom du responsable du dossier");
		profilUtilisateurPage.assertTrue("Prénom resp. dossier");
		profilUtilisateurPage.assertTrue("Qualité resp. dossier");
		profilUtilisateurPage.assertTrue("Tél. resp. dossier");
		profilUtilisateurPage.assertTrue("Mél resp. dossier");
		profilUtilisateurPage.assertTrue("Catégorie acte");
		profilUtilisateurPage.assertTrue("Base légale");
		profilUtilisateurPage.assertTrue("Date de publication souhaitée");
		profilUtilisateurPage.assertTrue("Publication du rapport de présentation");
		profilUtilisateurPage.assertTrue("Section du CE");
		profilUtilisateurPage.assertTrue("Rapporteur");
		profilUtilisateurPage.assertTrue("Date transm. section CE");
		profilUtilisateurPage.assertTrue("Date prévisionnelle section CE");
		profilUtilisateurPage.assertTrue("Date prévisionnelle AG CE");
		profilUtilisateurPage.assertTrue("Numéro ISA");
		profilUtilisateurPage.assertTrue("Chargé de mission");
		profilUtilisateurPage.assertTrue("Conseiller PM");
		profilUtilisateurPage.assertTrue("Date signature");
		profilUtilisateurPage.assertTrue("Pour fourniture d'épreuve");
		profilUtilisateurPage.assertTrue("Vecteur de publication");
		profilUtilisateurPage.assertTrue("Publication conjointe");
		profilUtilisateurPage.assertTrue("Publication intégrale ou par extrait");
		profilUtilisateurPage.assertTrue("Décret numéroté");
		profilUtilisateurPage.assertTrue("Mode parution");
		profilUtilisateurPage.assertTrue("Délai publication");
		profilUtilisateurPage.assertTrue("Publication à date précisée");
		profilUtilisateurPage.assertTrue("Date JO");
		profilUtilisateurPage.assertTrue("N° du texte JO");
		profilUtilisateurPage.assertTrue("Page JO");
		profilUtilisateurPage.assertTrue("Parutions BO");
		profilUtilisateurPage.assertTrue("Titre publié");
		profilUtilisateurPage.assertTrue("Zone commentaire SGG-DILA");
		profilUtilisateurPage.assertTrue("Rubrique");
		profilUtilisateurPage.assertTrue("Mots Cles");
		profilUtilisateurPage.assertTrue("Champ Libre");
		profilUtilisateurPage.assertTrue("Réf. Lois Appliquées");
		profilUtilisateurPage.assertTrue("Réf. Ordonnances");
		profilUtilisateurPage.assertTrue("Réf. Directives Europeennes");
		profilUtilisateurPage.assertTrue("Réf. Habilitation");
		profilUtilisateurPage.assertTrue("N° Article Habilitation");
		profilUtilisateurPage.assertTrue("Commentaire Habilitation");

		// Ajouter Tout
		profilUtilisateurPage.ajouterTout();
		// Valider
		profilUtilisateurPage.valider();

		// on recharge l'espace de traitement : déconnexion puis reconnexion
		logoutEpg();

		traitementPage = loginEpg("bdc");
		traitementPage.openProfilUtilisateurByButton();
		profilUtilisateurPage = traitementPage.openProfilUtilisateurByButton();

		profilUtilisateurPage.assertTrue("NOR");
		profilUtilisateurPage.assertTrue("Date d'arrivée");
		profilUtilisateurPage.assertTrue("Titre de l'acte");
		profilUtilisateurPage.assertTrue("Dernier contributeur");
		profilUtilisateurPage.assertTrue("Créé par");
		profilUtilisateurPage.assertTrue("Statut");
		profilUtilisateurPage.assertTrue("Type d'acte");
		profilUtilisateurPage.assertTrue("Entité à l'origine du projet");
		profilUtilisateurPage.assertTrue("Direction concernée");
		profilUtilisateurPage.assertTrue("Ministère resp.");
		profilUtilisateurPage.assertTrue("Direction resp.");
		profilUtilisateurPage.assertTrue("Ministère rattach.");
		profilUtilisateurPage.assertTrue("Direction rattach.");
		profilUtilisateurPage.assertTrue("Nom du responsable du dossier");
		profilUtilisateurPage.assertTrue("Prénom resp. dossier");
		profilUtilisateurPage.assertTrue("Qualité resp. dossier");
		profilUtilisateurPage.assertTrue("Tél. resp. dossier");
		profilUtilisateurPage.assertTrue("Mél resp. dossier");
		profilUtilisateurPage.assertTrue("Catégorie acte");
		profilUtilisateurPage.assertTrue("Base légale");
		profilUtilisateurPage.assertTrue("Date de publication souhaitée");
		profilUtilisateurPage.assertTrue("Publication du rapport de présentation");
		profilUtilisateurPage.assertTrue("Section du CE");
		profilUtilisateurPage.assertTrue("Rapporteur");
		profilUtilisateurPage.assertTrue("Date transm. section CE");
		profilUtilisateurPage.assertTrue("Date prévisionnelle section CE");
		profilUtilisateurPage.assertTrue("Date prévisionnelle AG CE");
		profilUtilisateurPage.assertTrue("Numéro ISA");
		profilUtilisateurPage.assertTrue("Chargé de mission");
		profilUtilisateurPage.assertTrue("Conseiller PM");
		profilUtilisateurPage.assertTrue("Date signature");
		profilUtilisateurPage.assertTrue("Pour fourniture d'épreuve");
		profilUtilisateurPage.assertTrue("Vecteur de publication");
		profilUtilisateurPage.assertTrue("Publication conjointe");
		profilUtilisateurPage.assertTrue("Publication intégrale ou par extrait");
		profilUtilisateurPage.assertTrue("Décret numéroté");
		profilUtilisateurPage.assertTrue("Mode parution");
		profilUtilisateurPage.assertTrue("Délai publication");
		profilUtilisateurPage.assertTrue("Publication à date précisée");
		profilUtilisateurPage.assertTrue("Date JO");
		profilUtilisateurPage.assertTrue("N° du texte JO");
		profilUtilisateurPage.assertTrue("Page JO");
		profilUtilisateurPage.assertTrue("Parutions BO");
		profilUtilisateurPage.assertTrue("Titre publié");
		profilUtilisateurPage.assertTrue("Zone commentaire SGG-DILA");
		profilUtilisateurPage.assertTrue("Rubrique");
		profilUtilisateurPage.assertTrue("Mots Cles");
		profilUtilisateurPage.assertTrue("Champ Libre");
		profilUtilisateurPage.assertTrue("Réf. Lois Appliquées");
		profilUtilisateurPage.assertTrue("Réf. Ordonnances");
		profilUtilisateurPage.assertTrue("Réf. Directives Europeennes");
		profilUtilisateurPage.assertTrue("Réf. Habilitation");
		profilUtilisateurPage.assertTrue("N° Article Habilitation");
		profilUtilisateurPage.assertTrue("Commentaire Habilitation");
		logoutEpg();

		// on remet les colonnes par défaut dans l'espace d'administration
		// Connexion adminsgg (admin. fonctionnel)

		traitementPage = loginAsAdminEpg();

		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		ParametrageDeLapplicationPage parametragePage = administrationEpgPage.goToParametrageDeLapplicationPage();
		parametragePage.removeTout();
		parametragePage.ajouterMetadonnee("NOR");
		parametragePage.ajouterMetadonnee("arrivée");
		parametragePage.ajouterMetadonnee("Titre de l");
		parametragePage.ajouterMetadonnee("Dernier contributeur");
		// Valider
		parametragePage.valider();
		logoutEpg();

		traitementPage = loginEpg("bdc");
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : bdc");

		profilUtilisateurPage = traitementPage.openProfilUtilisateurByButton();
		// Enlever Tout
		profilUtilisateurPage.enleverTout();
		// Valider
		profilUtilisateurPage.valider();
		profilUtilisateurPage.assertTrue("NOR");
		profilUtilisateurPage.assertTrue("Date d'arrivée");
		profilUtilisateurPage.assertTrue("Titre de l'acte");

		// Logout
		logoutEpg();

	}

	@WebTest(description = "Modifications des Données du Conseil d'Etat", order = 520)
	@TestDocumentation(categories = { "Conseil d'état", "Bordereau", "Journal" })
	public void testCase0200() {
		TraitementPage traitementPage = loginAsAdminEpg();
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();
		creationDossier100Page.setTypeActe("Décret en Conseil d'Etat de l'article 37 second alinéa de la Constitution");
		CreationDossier101Page creationDossier101Page = creationDossier100Page
				.appuyerBoutonSuivant(CreationDossier101Page.class);
		DossierDetailMenu dossierDetailMenu = creationDossier101Page.appuyerBoutonTerminer(DossierDetailMenu.class);

		BordereauPage bordereauPage = dossierDetailMenu.goToBordereau();
		bordereauPage.verrouiller();

		bordereauPage.setSectionCE("sectionCe");
		bordereauPage.setRapporteur("rapporteur");
		bordereauPage.setDateTransmissionCE("11/11/2012");
		bordereauPage.setDateSectionCe("12/11/2012");
		bordereauPage.setDateAgce("13/11/2012");
		bordereauPage.setNumeroIsa("numeroISA");
		bordereauPage.enregistrer();

		bordereauPage.deverouiller_par_alt();

		bordereauPage.assertTrue("sectionCe");
		bordereauPage.assertTrue("rapporteur");
		bordereauPage.assertTrue("11/11/2012");
		bordereauPage.assertTrue("12/11/2012");
		bordereauPage.assertTrue("13/11/2012");
		bordereauPage.assertTrue("numeroISA");

		JournalPage journalPage = dossierDetailMenu.goToJournalPage();
		journalPage.assertTrue("Section du CE : 'sectionCe' remplace ''");
		journalPage.assertTrue("Rapporteur : 'rapporteur' remplace ''");
		journalPage.assertTrue("Date transm. section CE : '11/11/2012' remplace ''");
		journalPage.assertTrue("Date prévisionnelle section CE : '12/11/2012' remplace ''");
		journalPage.assertTrue("Date prévisionnelle AG CE : '13/11/2012' remplace ''");
		journalPage.assertTrue("Numéro ISA : 'numeroISA' remplace ''");

		logoutEpg();
	}
}
