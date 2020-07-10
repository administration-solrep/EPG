package fr.dila.solonepg.webtest.webdriver010;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

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
import fr.dila.solonepg.page.creation.CreationDossier103Page;
import fr.dila.solonepg.page.creation.CreationDossier104Page;
import fr.dila.solonepg.page.creation.CreationDossier105Page;
import fr.dila.solonepg.page.creation.CreationDossierSelectPostePage;
import fr.dila.solonepg.page.main.onglet.AdministrationEpgPage;
import fr.dila.solonepg.page.main.onglet.CreationPage;
import fr.dila.solonepg.page.main.onglet.RecherchePage;
import fr.dila.solonepg.page.main.onglet.TraitementPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.BordereauPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.DossierDetailMenu;
import fr.dila.solonepg.page.main.onglet.dossier.detail.FeuilleDeRoutePage;
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
import fr.dila.st.core.util.DateUtil;
import fr.sword.naiad.commons.webtest.WebPage;
import fr.sword.naiad.commons.webtest.annotation.WebTest;

/**
 * Ici seuls les tests indépendants sont gardés : par indépendant, on entend qu'ils ne nécessitent pas que d'autres
 * tests soient passés en amont pour fonctionner
 * 
 */
public class TestSuiteManualTests extends AbstractSolonSuite {

	final static String			BULLETIN_EFI	= "Bulletin officiel EFI";
	final static String			MINISTERE_EFI	= "EFI - Min. Economie, finances et industrie";
	private static final String	UTILISATEUR_BDC	= "bdc";

	public static String		NOR_DOSSIER_EFI;
	public static String		NOR_DOSSIER_CCO;
	public static String		NOR_DOSSIER_ACP;

	public final String[]		concatTitresActes	= {
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus",
			"Ut velit mauris, egestas sed, gravida nec", "Ornare ut, mi. Aenean ut orci vel massa suscipit pulvinar. ",
			"Nulla sollicitudin. Fusce varius, ligula non tempus aliquam",
			"Nunc turpis ullamcorper nibh, in tempus sapien eros vitae ligula.",
			"Pellentesque rhoncus nunc et augue. Integer id felis. Curabitur",
			"Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium",
			"Nam libero tempore, cum soluta nobis est eligendi optio, cumque nihil impedit",
			"Fusce vulputate sem at sapien. Vivamus leo.", "Praesent aliquam, enim at fermentum mollis",
			"Aliquam convallis sollicitudin purus." };

	public final String[]		nomResponsables		= { "DUPONT", "DUPOND", "HADOCK", "TOURNESOL", "CASTAFIORE" };


	@WebTest(description = "ajout des modes de parution", order = 10)
	@TestDocumentation(categories = { "Administration", "TDR" })
	public void testCase001() {
		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		administrationEpgPage.assertTrue("Tables de référence");

		TablesDeReferencePage tablesDeReference = administrationEpgPage.goToTablesDeReference();
		tablesDeReference.ajouteModeParution("Electronique");
		tablesDeReference.ajouteModeParution("Mixte");
		tablesDeReference.ajouteModeParution("Papier");
		logoutEpg();
	}

	@WebTest(description = "ajout des vecteurs de publication", order = 11)
	@TestDocumentation(categories = { "Administration", "Publication" })
	public void testCase001_3() {
		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		administrationEpgPage.assertTrue("vecteurs de publication");
		VecteursPublicationPage vecteurPage = administrationEpgPage.goToVecteursPublication();
		vecteurPage.ajouterVecteurPublication("Vecteur bidon");

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
		input.sendKeys(Keys.chord(Keys.CONTROL, "a"), "Vecteur bidon");
		vecteurPage.getElementById("vecteur_publication_properties:button_save_vecteur").click();
		vecteurPage.waitForPageSourcePart("Sauvegarde annulée", WebPage.TIMEOUT_IN_SECONDS);

		// On crée également un bulletin officiel, car depuis la FEV 391, il est obligatoire sur les dossiers pour
		// valider une étape de type "publication BO"
		vecteurPage.setMinistere(MINISTERE_EFI);
		vecteurPage.setIntitule(BULLETIN_EFI);
		vecteurPage.ajouterBulletin();

		logoutEpg();
	}

	/**
	 * test Administration FDR defaut
	 * 
	 */
	@WebTest(description = "Administration FDR defaut", order = 20)
	@TestDocumentation(categories = { "FDR", "Administration" })
	public void testCase002() {

		maximizeBrowser();

		TraitementPage traitementPageBDC = loginEpg("bdc", "bdc", TraitementPage.class);
		traitementPageBDC.assertTrue("Mon identifiant dans S.O.L.O.N. : bdc");

		for (int i = 1; i <= 11; i++) {
			createDossier("Acte " + i + " : " + randomFewString(concatTitresActes), traitementPageBDC);
		}

		for (int j = 1; j <= 31; j++) {
			createAndLaunchDossier("Dossier " + j + " : " + randomFewString(concatTitresActes), traitementPageBDC);
		}

		logoutEpg();
	}



	private void createDossier(String titreActe, TraitementPage traitementPage) {


		// Go to Creation Page
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();

		creationDossier100Page.setTypeActe("Avis");

		// Set Ministere Reponsable
		String[] mrItems = {
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_tree:node:0:node:22::nxw_ministere_resp_field_addMin" };
		creationDossier100Page.setMinistereResponable(Arrays.asList(mrItems));

		// Set Direction concernee
		String[] dcItems = {
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0::min:handle:img:collapsed",
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0:node:5::nxw_direction_resp_field_addDir" };
		creationDossier100Page.setDirectionConcerne(Arrays.asList(dcItems));
		creationDossier100Page.assertTrue("DAJ (Economie)");

		DossierDetailMenu dossierDetailMenu = creationDossier100Page.appuyerBoutonTerminer(DossierDetailMenu.class);
		dossierDetailMenu.assertTrue("Parapheur");

		BordereauPage bordereauPage = dossierDetailMenu.goToBordereau();
		// Verrouiller
		bordereauPage.verrouiller();

		bordereauPage.setTitreActe(titreActe /* + UUID.randomUUID().toString() */);

		bordereauPage.setNomDuResponsable(randomFewString(nomResponsables));

		String[] mrMs = { "Mr" };
		bordereauPage.setQualiteDuResponsable(randomFewString(mrMs));

		bordereauPage.setTelDuResponsable("0238587469");

		bordereauPage.setPrenomDuResponsable("Jean");

		bordereauPage.setModeParution("Electronique");

		bordereauPage.setVecteurPublication(BULLETIN_EFI);

		bordereauPage.setDelaiPublication("Aucun");

		bordereauPage.setRubriques("Sécurité");

		Calendar dateFourniture = Calendar.getInstance();
		dateFourniture.add(Calendar.MONTH, 1);
		bordereauPage.setDatePourFournitureEpreuve(DateUtil.formatDDMMYYYYSlash(dateFourniture));

		// Save Data
		bordereauPage.enregistrer();

	}

	private void createAndLaunchDossier(String titreActe, TraitementPage traitementPage) {

		// Go to Creation Page
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();

		creationDossier100Page.setTypeActe("Avis");

		// Set Ministere Reponsable
		String[] mrItems = {
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_tree:node:0:node:22::nxw_ministere_resp_field_addMin" };
		creationDossier100Page.setMinistereResponable(Arrays.asList(mrItems));

		// Set Direction concernee
		String[] dcItems = {
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0::min:handle:img:collapsed",
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0:node:5::nxw_direction_resp_field_addDir" };
		creationDossier100Page.setDirectionConcerne(Arrays.asList(dcItems));
		creationDossier100Page.assertTrue("DAJ (Economie)");

		DossierDetailMenu dossierDetailMenu = creationDossier100Page.appuyerBoutonTerminer(DossierDetailMenu.class);
		dossierDetailMenu.assertTrue("Parapheur");

		BordereauPage bordereauPage = dossierDetailMenu.goToBordereau();
		// Verrouiller
		bordereauPage.verrouiller();

		bordereauPage.setTitreActe(titreActe /* + UUID.randomUUID().toString() */);

		bordereauPage.setNomDuResponsable(randomFewString(nomResponsables));

		String[] mrMs = { "Mr" };
		bordereauPage.setQualiteDuResponsable(randomFewString(mrMs));

		bordereauPage.setTelDuResponsable("0238587469");

		bordereauPage.setPrenomDuResponsable("Jean");

		bordereauPage.setModeParution("Electronique");

		bordereauPage.setVecteurPublication(BULLETIN_EFI);

		bordereauPage.setDelaiPublication("Aucun");

		bordereauPage.setRubriques("Sécurité");

		Calendar dateFourniture = Calendar.getInstance();
		dateFourniture.add(Calendar.MONTH, 1);
		bordereauPage.setDatePourFournitureEpreuve(DateUtil.formatDDMMYYYYSlash(dateFourniture));

		// Save Data
		bordereauPage.enregistrer();

		// Lancement du dossier
		// Open feuille de route tab
		FeuilleDeRoutePage feuilleDeRoute = dossierDetailMenu.goToFeuilleDeRoute();

		// Lancer Dossier
		feuilleDeRoute.lancerDossier();

		// feuilleDeRoute.assertTrue("est lancé");

	}

	/**
	 * test Administration FDR defaut
	 * 
	 */
	@WebTest(description = "Reloging pour tests manuels", order = 99)
	@TestDocumentation(categories = { "FDR", "Administration" })
	public void testCaseRelog() {

		maximizeBrowser();

		TraitementPage traitementPageBDC = loginEpg("bdc", "bdc", TraitementPage.class);
		traitementPageBDC.assertTrue("Mon identifiant dans S.O.L.O.N. : bdc");


	}

	private String randomFewString(String[] toRandomize) {
		return toRandomize[(int) Math.floor((Math.random() * toRandomize.length))];
	}

}
