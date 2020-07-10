package fr.dila.solonepg.webtest.webdriver010;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.administration.CreationEtapePage;
import fr.dila.solonepg.page.administration.GestionDeLindexationPage;
import fr.dila.solonepg.page.administration.GestionDesMigrationsPage;
import fr.dila.solonepg.page.administration.GestionModelesPage;
import fr.dila.solonepg.page.administration.GestionRestrictionAccesPage;
import fr.dila.solonepg.page.administration.GestionUtilisateurs;
import fr.dila.solonepg.page.administration.TablesDeReferencePage;
import fr.dila.solonepg.page.an.loi.ApplicationDesLoisPage;
import fr.dila.solonepg.page.an.loi.DetailDecretPage;
import fr.dila.solonepg.page.an.loi.ModificationMesurePage;
import fr.dila.solonepg.page.commun.LoginPage;
import fr.dila.solonepg.page.commun.ProfilUtilisateurPage;
import fr.dila.solonepg.page.creation.CreationDossier100Page;
import fr.dila.solonepg.page.creation.CreationDossier101Page;
import fr.dila.solonepg.page.creation.CreationDossier104Page;
import fr.dila.solonepg.page.creation.CreationDossier108Page;
import fr.dila.solonepg.page.main.onglet.AdministrationEpgPage;
import fr.dila.solonepg.page.main.onglet.CreationPage;
import fr.dila.solonepg.page.main.onglet.PilotageANPage;
import fr.dila.solonepg.page.main.onglet.RecherchePage;
import fr.dila.solonepg.page.main.onglet.SuiviPage;
import fr.dila.solonepg.page.main.onglet.TraitementPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.BordereauPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.DossierDetailMenu;
import fr.dila.solonepg.page.main.onglet.dossier.detail.FeuilleDeRoutePage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.ParapheurPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.TextMaitrePage;
import fr.dila.solonepg.page.main.pan.view.TableauMaitreDossierDetail;
import fr.dila.solonepg.page.main.pan.view.TableauMaitreViewPage;
import fr.dila.solonepg.page.main.pan.view.mesure.MesureViewPage;
import fr.dila.solonepg.page.recherche.AjouterAuxFavorisPage;
import fr.dila.solonepg.page.recherche.AjouterAuxTableauxDynamiquesPage;
import fr.dila.solonepg.page.recherche.CommonRechercheUtilisateurPage;
import fr.dila.solonepg.page.recherche.DernierResultatConsulteUtilisateurPage;
import fr.dila.solonepg.page.recherche.RechercheExpertePage;
import fr.dila.solonepg.page.recherche.RechercheModeleFeuilleDeRoutePage;
import fr.dila.solonepg.page.recherche.RechercheUtilisateurPage;
import fr.dila.solonepg.page.recherche.ViewUtilisateurDetailsPage;
import fr.dila.solonepg.page.recherche.dernierresultat.DernierDossierResultat;
import fr.dila.solonepg.page.recherche.dernierresultat.DernierFeuilleDeRouteResultat;
import fr.dila.solonepg.page.recherche.favoris.consultation.FavorisConsultationDossier;
import fr.dila.solonepg.page.recherche.favoris.consultation.FavorisConsultationFeulleDeRoute;
import fr.dila.solonepg.page.recherche.favoris.consultation.FavorisConsultationUtilisateur;
import fr.dila.solonepg.page.recherche.user.ModifierUser;
import fr.dila.solonepg.page.recherche.user.UserDetailPage;
import fr.dila.solonepg.page.suivi.DossiersSignalesPage;
import fr.dila.solonepg.webtest.AbstractSolonSuite;
import fr.dila.solonepg.webtest.helper.SolonEpgImapHelper;
import fr.dila.solonepg.webtest.helper.url.UrlEpgHelper;
import fr.dila.st.annotations.TestDocumentation;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.webdriver.model.CommonWebPage;
import fr.dila.st.webdriver.model.CommonWebPage.By_enum;
import fr.dila.st.webdriver.ws.WsResultPage;
import fr.sword.naiad.commons.webtest.WebPage;
import fr.sword.naiad.commons.webtest.annotation.WebTest;
import fr.sword.naiad.commons.webtest.mail.ImapConsult;
import junit.framework.Assert;

/**
 * Attention, les tests ne sont pas réentrants, ni indépendants.
 * 
 * Notamment, le test 190 modifie le poste de l'utilisateur 'bdc'. Donc tous ceux qui le concernent, précédemment, ne
 * fonctionneront plus une fois que le 190 a été passé.
 * 
 */
public class TestSuiteDependant extends AbstractSolonSuite {

	private static final String	UTILISATEUR_ADMIN			= "adminsgg";
	private static final String	MESSAGE_ACCES_NON_AUTORISE	= "Utilisateur non autorisé";
	private static final String	UTILISATEUR_BDC				= "bdc";
	private static final String	DESCRIPTION_ACCES_RESTREINT	= "Accès restreint";

	final static String			BULLETIN_EFI				= "Bulletin officiel EFI";
	final static String			MINISTERE_ACO				= "ACO - Autorité de la concurrence";
	final static String			MINISTERE_EFI				= "EFI - Min. Economie, finances et industrie";

	public static String		NOR_DOSSIER_EFI;
	public static String		NOR_DOSSIER_CCO;
	public static String		NOR_DOSSIER_ACP;

	final static String			MINISTERE_ECONOMIE			= "creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_tree:node:0:node:22::nxw_ministere_resp_field_addMin";
	final static String			MINISTERE_COLLECTIVITE		= "creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_tree:node:0:node:4::nxw_ministere_resp_field_addMin";
	final static String			MINISTERE_CCO				= "creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_tree:node:0:node:3::nxw_ministere_resp_field_addMin";

	@WebTest(description = "Tableaux Dynamiques", order = 61)
	@TestDocumentation(categories = { "Recherche experte" })
	public void testCase0010() {

		TraitementPage traitementPage = loginAsAdminEpg();

		RecherchePage recherchePage = traitementPage.getOngletMenu().goToRecherche();

		RechercheExpertePage rechercheExpertePage = recherchePage.goToRechercheExperte();
		rechercheExpertePage.setReqCategory("dossier");
		RechercheExpertePage.sleep(1);
		rechercheExpertePage.setReqField("requeteur_dossier_d_dos_idCreateurDossier");
		RechercheExpertePage.sleep(1);
		rechercheExpertePage.setAdminsggASuggest();
		RechercheExpertePage.sleep(1);
		rechercheExpertePage.ajouter();
		RechercheExpertePage.sleep(1);
		// Faire la recherche
		rechercheExpertePage.rechercher();

		WebPage.sleep(10);

		// Assert after search
		rechercheExpertePage.assertTrue(NOR_DOSSIER_ACP);
		rechercheExpertePage.assertTrue(NOR_DOSSIER_CCO);
		rechercheExpertePage.assertTrue("Résultats de la recherche");
		rechercheExpertePage.assertTrue("Responsable de l'acte : créé par");
		rechercheExpertePage.assertTrue("EGAL");

		// Retour a la page precedente
		rechercheExpertePage.retourAuDossier();
		// Click Ajouter Aux tableaux dynamiques page
		AjouterAuxTableauxDynamiquesPage tableauxDynamiquesPage = rechercheExpertePage.ajouterAuxTableauxDynamiques();
		tableauxDynamiquesPage.setIntitule("Selenium TDCAdminsgg");

		tableauxDynamiquesPage.setDestinataire("CCO - CCO");
		tableauxDynamiquesPage.assertTrue("CCO");

		tableauxDynamiquesPage
				.setDestinataire(Arrays
						.asList("view_tab_dyn:nxl_tableau_dynamique_destinataires:nxw_tab_dynamique_destinataires_tree:node:0:node:16::min:handle",
								"view_tab_dyn:nxl_tableau_dynamique_destinataires:nxw_tab_dynamique_destinataires_tree:node:0:node:16:node:17::addBtnPST"));
		tableauxDynamiquesPage.assertTrue("CCO");
		tableauxDynamiquesPage.assertTrue("Bureau des cabinets (BCR)");

		tableauxDynamiquesPage.ajouter();

		AjouterAuxTableauxDynamiquesPage.sleep(1);

		SuiviPage suiviPage = traitementPage.getOngletMenu().goToSuivi();
		suiviPage.clicTableauxDynamiques("adminsgg");
		suiviPage.waitForPageSourcePart("Selenium TDCAdminsgg", TIMEOUT_SEC);

		logoutEpg();
	}

	@WebTest(description = "Tableaux Dynamiques/connexion bdc", order = 62)
	@TestDocumentation(categories = { "Suivi" })
	public void testCase0010_1() {
		TraitementPage traitementPage = loginEpg(UTILISATEUR_BDC);

		SuiviPage suiviPage = traitementPage.getOngletMenu().goToSuivi();
		suiviPage.clicTableauxDynamiques(UTILISATEUR_BDC);
		suiviPage.waitForPageSourcePart("Selenium TDCAdminsgg", TIMEOUT_SEC);

		logoutEpg();
	}

	@WebTest(description = "2)Tableaux Dynamiques/connexion finance_bdc", order = 63)
	@TestDocumentation(categories = { "Suivi" })
	public void testCase0010_2() {
		TraitementPage traitementPage = loginEpg("finance_bdc");
		SuiviPage suiviPage = traitementPage.getOngletMenu().goToSuivi();
		suiviPage.clicTableauxDynamiques("finance_bdc");
		suiviPage.waitForPageSourcePart("Selenium TDCAdminsgg", TIMEOUT_SEC);

		logoutEpg();
	}

	@WebTest(description = "Initialisation dossiers multiples", order = 80)
	@TestDocumentation(categories = { "Dossier" })
	public void testCase0057() {
		createAndLaunchDossier("Titre acte testCase0057-1");
		createAndLaunchDossier("Titre acte testCase0057-2");
		createAndLaunchDossier("Titre acte testCase0057-3");
		createAndLaunchDossier("Titre acte testCase0057-4");
		createAndLaunchDossier("Titre acte testCase0057-5");
	}

	@WebTest(description = "Lancement dossier", order = 90)
	@TestDocumentation(categories = { "Dossier", "FDR", "Bordereau" })
	public void testCase0060() {

		TraitementPage traitementPage = loginEpg(UTILISATEUR_BDC, UTILISATEUR_BDC, TraitementPage.class);
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : bdc");
		// Go to Creation Page
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();
		creationDossier100Page.setTypeActe("Amnistie");

		// Set Ministere Reponsable
		String[] mrItems = { "creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_tree:node:0:node:2::nxw_ministere_resp_field_addMin" };
		creationDossier100Page.setMinistereResponable(Arrays.asList(mrItems));

		// Set Direction concernee
		String[] dcItems = {
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0::min:handle:img:collapsed",
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0:node:2::nxw_direction_resp_field_addDir" };
		creationDossier100Page.setDirectionConcerne(Arrays.asList(dcItems));
		creationDossier100Page.assertTrue("Rapporteur Autorité de la concurrence");

		DossierDetailMenu dossierDetailMenu = creationDossier100Page.appuyerBoutonTerminer(DossierDetailMenu.class);

		ParapheurPage parapheurPage = dossierDetailMenu.getPage(ParapheurPage.class);
		parapheurPage.verrouiller();

		// Ajouter un document
		parapheurPage.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']", "word2003.doc");

		// Go to Bordereau tab
		BordereauPage bordereauPage = dossierDetailMenu.goToBordereau();
		bordereauPage.setTitreActe("Titre acte testCase0060");
		bordereauPage.setNomDuResponsable("DUPONT");

		bordereauPage.setQualiteDuResponsable("Mr");

		bordereauPage.setTelDuResponsable("0238587469");

		bordereauPage.setPrenomDuResponsable("Jean");

		bordereauPage.setModeParution("Electronique");

		bordereauPage.setDelaiPublication("Aucun");

		bordereauPage.setRubriques("Domaine public");

		// Save Data
		bordereauPage.enregistrer();
		// On déverouille le bordereau
		bordereauPage.deverouiller_par_alt();

		bordereauPage.assertTrue("Titre acte testCase0060");
		bordereauPage.assertTrue("DUPONT");
		bordereauPage.assertTrue("Mr");
		bordereauPage.assertTrue("0238587469");
		bordereauPage.assertTrue("Jean");
		bordereauPage.assertTrue("Electronique");
		bordereauPage.assertTrue("Aucun");
		bordereauPage.assertTrue("Domaine public");

		// Lancement du dossier
		// Open feuille de route tab
		FeuilleDeRoutePage feuilleDeRoute = dossierDetailMenu.goToFeuilleDeRoute();

		// Verrouiller FDR
		feuilleDeRoute.verrouiller();

		// Lancer Dossier
		feuilleDeRoute.lancerDossier();

		// le dossier n'est pas lancé car il n'y a qu'une étape à la FdR
		feuilleDeRoute.assertFalse("est lancé");

		// ajout tâche "pour avis"
		CreationEtapePage creationEtapePage = feuilleDeRoute.ajouterApres();
		creationEtapePage.setTypeEtape("Pour avis");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:13::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:13:node:0::dir:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:13:node:0:node:0::nxw_routing_task_distribution_mailbox_addPst"));
		// Create
		creationEtapePage.creer();
		CreationEtapePage.sleep(2);

		// Lancer Dossier
		feuilleDeRoute.lancerDossier();
		feuilleDeRoute.assertTrue("est lancé");

		logoutEpg();
	}

	@WebTest(description = "Visibilité des dossiers", order = 100)
	@TestDocumentation(categories = { "Dossier", "Recherche", "Parapheur" })
	public void testCase0065() {

		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");

		RecherchePage recherchePage = traitementPage.rechercheNor("EFI*");

		DossierDetailMenu dossierDetail = recherchePage.getDossierDetail(getDossierTest65());

		fr.dila.solonepg.page.main.onglet.dossier.detail.ParapheurPage parapheurPage = dossierDetail
				.getPage(fr.dila.solonepg.page.main.onglet.dossier.detail.ParapheurPage.class);
		parapheurPage.assertVerrouille();

		recherchePage.assertTrue("Parapheur");

		logoutEpg();
	}

	@WebTest(description = "Visibilité des dossiers/agriculture_bdc", order = 110)
	@TestDocumentation(categories = { "Dossier", "Recherche" })
	public void testCase0065_1() {
		// Connexion agriculture
		TraitementPage traitementPage = loginEpg("agriculture_bdc");
		RecherchePage recherchePage = traitementPage.rechercheNor("EFI*");

		recherchePage.assertFalse(getDossierTest65());
		logoutEpg();
	}

	@WebTest(description = "Visibilité des dossiers/bdc", order = 120)
	@TestDocumentation(categories = { "Dossier", "Recherche", "FDR" })
	public void testCase0065_2() {
		// Connexion bdc
		TraitementPage traitementPage = loginEpg(UTILISATEUR_BDC);
		RecherchePage recherchePage = traitementPage.rechercheNor("EFI*");

		DossierDetailMenu dossierDetail = recherchePage.getDossierDetail(getDossierTest65());
		recherchePage.assertTrue("Parapheur");

		// Go to Feulle de Route Tab
		FeuilleDeRoutePage feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		feuilleDeRoute.verrouiller();
		logoutEpg();
	}

	@WebTest(description = "Action étapes FDR", order = 130)
	@TestDocumentation(categories = { "FDR", "Recherche" })
	public void testCase0068() {

		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");

		// Recherche sur EFI*
		RecherchePage recherchePage = traitementPage.rechercheNor("EFI*");

		DossierDetailMenu dossierDetail = recherchePage.getDossierDetail(getEtapeFdrDossier());
		// Go to Feulle de Route Tab
		FeuilleDeRoutePage feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoute.verrouiller();

		ajouterUneEtapeApres(feuilleDeRoute, "Pour avis");

		ajouterUneEtapeApres(feuilleDeRoute, "Pour attribution au SGG");

		ajouterUneEtapeApres(feuilleDeRoute, "Pour contreseing");

		ajouterUneEtapeApres(feuilleDeRoute, "Pour information");

		ajouterUneEtapeApres(feuilleDeRoute, "Pour impression");

		ajouterUneEtapeApres(feuilleDeRoute, "Pour bon à tirer");

		ajouterUneEtapeApres(feuilleDeRoute, "Pour fourniture d'épreuves");

		ajouterUneEtapeApres(feuilleDeRoute, "Pour publication à la DILA JO");

		ajouterUneEtapeApres(feuilleDeRoute, "Pour publication à la DILA BO");

		ajouterUneEtapeApres(feuilleDeRoute, "Retour pour modification");

		// Valider
		Assert.assertTrue(feuilleDeRoute.valider());

		dossierDetail = recherchePage.getDossierDetail(getEtapeFdrDossier());
		// Go to Feulle de Route Tab
		feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoute.verrouiller();
		// Valider
		Assert.assertTrue(feuilleDeRoute.valider());

		logoutEpg();

	}

	@WebTest(description = "Action étapes FDR/Connexion contributeurmin", order = 140)
	@TestDocumentation(categories = { "FDR", "Recherche" })
	public void testCase0068_1() {
		// Connexion bdc
		TraitementPage traitementPage = loginEpg("contributeurmin");
		RecherchePage recherchePage = traitementPage.rechercheNor("EFI*");

		DossierDetailMenu dossierDetail = recherchePage.getDossierDetail(getEtapeFdrDossier());
		FeuilleDeRoutePage feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoute.verrouiller();

		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//input[@title='Avis favorable']");

		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//input[@title='Avis favorable avec corrections']");

		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//input[@title='Avis défavorable']");

		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//img[@title='Non concerné']");

		feuilleDeRoute.avisFavorableAvecCorrections();
		logoutEpg();
	}

	@WebTest(description = "Action étapes FDR/Connexion contributeurmin 2", order = 150)
	@TestDocumentation(categories = { "FDR", "Recherche" })
	public void testCase0068_2() {
		// Connexion bdc
		TraitementPage traitementPage = loginEpg("contributeurmin");
		RecherchePage recherchePage = traitementPage.rechercheNor("EFI*");

		DossierDetailMenu dossierDetail = recherchePage.getDossierDetail(getEtapeFdrDossier());
		FeuilleDeRoutePage feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoute.verrouiller();

		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Avis favorable']");

		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Avis favorable avec corrections']");

		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Refus de validation']");

		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//img[@title='Non concerné']");

		// Déverrouiller
		feuilleDeRoute.deverouiller_par_alt();

		logoutEpg();
	}

	@WebTest(description = "Action étapes FDR/adminsgg", order = 160)
	@TestDocumentation(categories = { "FDR", "Recherche" })
	public void testCase0068_3() {

		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");

		// Recherche sur EFI*
		RecherchePage recherchePage = traitementPage.rechercheNor("EFI*");

		DossierDetailMenu dossierDetail = recherchePage.getDossierDetail(getEtapeFdrDossier());
		// Go to Feulle de Route Tab
		FeuilleDeRoutePage feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoute.verrouiller();

		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//input[@title='Valider']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Avis favorable avec corrections']");
		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//input[@title='Refus de validation']");
		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//img[@title='Non concerné']");
		// Valider
		Assert.assertTrue(feuilleDeRoute.valider());

		// Recherche sur EFI*
		recherchePage = traitementPage.rechercheNor("EFI*");
		// Go to Feulle de Route Tab
		dossierDetail = recherchePage.getDossierDetail(getEtapeFdrDossier());
		// Go to Feulle de Route Tab
		feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoute.verrouiller();
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Avis favorable']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Avis favorable avec corrections']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//img[@title='Non concerné']");
		feuilleDeRoute.deverouiller_par_alt();

		logoutEpg();

	}

	@WebTest(description = "Action étapes FDR/Signataire", order = 170)
	@TestDocumentation(categories = { "FDR", "Recherche", "Parapheur" })
	public void testCase0068_4() {

		TraitementPage traitementPage = loginEpg("signataire");
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : signataire");

		// Recherche sur EFI*
		RecherchePage recherchePage = traitementPage.rechercheNor("EFI*");

		DossierDetailMenu dossierDetail = recherchePage.getDossierDetail(getEtapeFdrDossier());
		// Go to Feulle de Route Tab
		FeuilleDeRoutePage feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoute.verrouiller();

		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//input[@title='Avis favorable']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Avis favorable avec corrections']");
		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//input[@title='Refus de signature']");
		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//img[@title='Non concerné']");

		// Clic avis favorable
		feuilleDeRoute.avisFavorable();
		dossierDetail = recherchePage.getDossierDetail(getEtapeFdrDossier());

		// Go to Feulle de Route Tab
		feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoute.verrouiller();

		feuilleDeRoute.assertValider();
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Avis favorable avec corrections']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Refus de validation']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//img[@title='Non concerné']");

		// Valider
		Assert.assertTrue(feuilleDeRoute.valider());

		dossierDetail = recherchePage.getDossierDetail(getEtapeFdrDossier());
		// Go to Feulle de Route Tab
		feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoute.verrouiller();
		feuilleDeRoute.assertValider();

		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//input[@title='Valider avec correction']");
		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//input[@title='Refus de validation']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//img[@title='Non concerné']");

		// Valider
		Assert.assertTrue(feuilleDeRoute.valider());

		dossierDetail = recherchePage.getDossierDetail(getEtapeFdrDossier());
		// Go to Feulle de Route Tab
		feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoute.verrouiller();

		feuilleDeRoute.assertValider();
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Valider avec correction']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Refus de validation']");
		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//img[@title='Retour pour modification']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//img[@title='Non concerné']");

		// Valider => on attend une erreur car il manque un fichier dans le parapheur
		Assert.assertFalse(feuilleDeRoute.valider());

		fr.dila.solonepg.page.main.onglet.dossier.detail.ParapheurPage parapheur = dossierDetail.goToParapheur();

		parapheur.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:1::parapheurFolder:text']", "word2003.doc");

		// Go to feuille de route
		feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		Assert.assertTrue(feuilleDeRoute.valider());

		dossierDetail = recherchePage.getDossierDetail(getEtapeFdrDossier());
		// Go to Feulle de Route Tab
		feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoute.verrouiller();

		feuilleDeRoute.assertValider();

		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Valider avec correction']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Refus de validation']");
		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//img[@title='Retour pour modification']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//img[@title='Non concerné']");

		Assert.assertTrue(feuilleDeRoute.valider());

		dossierDetail = recherchePage.getDossierDetail(getEtapeFdrDossier());
		// Go to Feulle de Route Tab
		feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoute.verrouiller();
		feuilleDeRoute.assertValider();
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Valider avec correction']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Refus de validation']");
		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//img[@title='Retour pour modification']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//img[@title='Non concerné']");

		Assert.assertTrue(feuilleDeRoute.valider());

		dossierDetail = recherchePage.getDossierDetail(getEtapeFdrDossier());
		// Go to Feulle de Route Tab
		feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoute.verrouiller();
		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//input[@title='Correction effectuée']");
		feuilleDeRoute.assertElementNotPresent(By_enum.XPATH, "//input[@title='Valider avec correction']");
		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//input[@title='Refus de modification']");
		feuilleDeRoute.assertElementPresent(By_enum.XPATH, "//img[@title='Non concerné']");

		logoutEpg();

	}

	@WebTest(description = "Gestion de l'indexation/Connexion bdc", order = 220)
	@TestDocumentation(categories = { "Indexation", "Administration" })
	public void testCase0091_1() {

		TraitementPage traitementPage = loginEpg(UTILISATEUR_BDC);
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		GestionDeLindexationPage gestionDeLindexationPage = administrationEpgPage.goToGestionDeLindexation();

		gestionDeLindexationPage.assertFalse("Rubriques");
		gestionDeLindexationPage.assertTrue("Mots-clés");
		gestionDeLindexationPage.assertTrue("CCO - Liste 1");
		gestionDeLindexationPage.assertFalse("ACP - Liste 2");

		logoutEpg();

	}

	@WebTest(description = "Dossiers Signales", order = 190)
	@TestDocumentation(categories = { "Dossiers signalés" })
	public void testCase0072() {

		TraitementPage traitementPage = loginEpg(UTILISATEUR_BDC);

		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();
		creationDossier100Page.setTypeActe("Amnistie");

		DossierDetailMenu dossierDetailMenu = creationDossier100Page.appuyerBoutonTerminer(DossierDetailMenu.class);
		fr.dila.solonepg.page.main.onglet.dossier.detail.ParapheurPage parapheurPage = dossierDetailMenu
				.getPage(fr.dila.solonepg.page.main.onglet.dossier.detail.ParapheurPage.class);

		parapheurPage.verserDossier();
		parapheurPage.waitForPageSourcePart("Le dossier a été versé avec succès dans les dossiers signalés",
				TIMEOUT_SEC);
		parapheurPage.verserDossier();
		parapheurPage.waitForPageSourcePart("Le dossier n'a pas pu être versé dans les dossiers signalés", TIMEOUT_SEC);

		CreationPage creationPage2 = parapheurPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page2 = creationPage2.createDossier();
		creationDossier100Page2.setTypeActe("Amnistie");

		dossierDetailMenu = creationDossier100Page2.appuyerBoutonTerminer(DossierDetailMenu.class);
		parapheurPage = dossierDetailMenu.getPage(fr.dila.solonepg.page.main.onglet.dossier.detail.ParapheurPage.class);

		parapheurPage.verserDossier();
		parapheurPage.waitForPageSourcePart("Le dossier a été versé avec succès dans les dossiers signalés",
				TIMEOUT_SEC);

		SuiviPage suiviPage = creationPage2.getOngletMenu().goToSuivi();
		DossiersSignalesPage dossiersSignales = suiviPage.goToDossiersSignalesPage();
		new WebDriverWait(getDriver(), TIMEOUT_SEC).until(ExpectedConditions.visibilityOfElementLocated((By
				.xpath(DossiersSignalesPage.VIDER_CORBEILLE_INPUT_PATH))));
		dossiersSignales.checkRow("1");
		new WebDriverWait(getDriver(), TIMEOUT_SEC).until(ExpectedConditions.visibilityOfElementLocated((By
				.xpath(DossiersSignalesPage.AJOUTER_SELECTION_INPUT_PATH))));
		new WebDriverWait(getDriver(), TIMEOUT_SEC).until(ExpectedConditions.visibilityOfElementLocated((By
				.xpath(DossiersSignalesPage.RETIRER_DOSSIER_INPUT_PATH))));
		dossiersSignales.retirerSelection();
		new WebDriverWait(getDriver(), TIMEOUT_SEC).until(ExpectedConditions.visibilityOfElementLocated((By
				.xpath(DossiersSignalesPage.VIDER_CORBEILLE_INPUT_PATH))));
		dossiersSignales.viderCorbeille();

		logoutEpg();

	}

	@WebTest(description = " Favoris de Recherche", order = 230)
	@TestDocumentation(categories = { "Favoris", "Recherche experte" })
	public void testCase0101() {

		TraitementPage traitementPage = loginAsAdminEpg();

		RecherchePage recherchePage = traitementPage.getOngletMenu().goToRecherche();

		RechercheExpertePage rechercheExpertePage = recherchePage.goToRechercheExperte();
		rechercheExpertePage.waitElementVisibilityById("smartSearchForm");
		rechercheExpertePage.setReqCategory("dossier");
		RechercheExpertePage.sleep(2);
		rechercheExpertePage.setReqField("requeteur_dossier_d_dos_idCreateurDossier");
		RechercheExpertePage.sleep(2);
		rechercheExpertePage.setAdminsggASuggest();
		RechercheExpertePage.sleep(1);
		rechercheExpertePage.ajouter();
		RechercheExpertePage.sleep(1);
		// Faire la recherche
		rechercheExpertePage.rechercher();

		// Assert after search
		rechercheExpertePage.waitForPageSourcePart("ACPP" + anneeCourante + "00002Y", TIMEOUT_SEC);
		rechercheExpertePage.assertTrue("CCOZ" + anneeCourante + "00001Y");
		rechercheExpertePage.assertTrue("Résultats de la recherche");
		rechercheExpertePage.assertTrue("Responsable de l'acte : créé par");
		rechercheExpertePage.assertTrue("EGAL");

		// Retour a la page precedente
		rechercheExpertePage.retourAuDossier();
		// Click Ajouter Aux tableaux dynamiques page
		AjouterAuxFavorisPage ajouterAuxFavorisPage = rechercheExpertePage.ajouterAuxFavoris();
		ajouterAuxFavorisPage.setIntitule("Selenium FavRecAdminsgg");

		ajouterAuxFavorisPage
				.setDestinataire(Arrays
						.asList("fav_rech_form:nxl_favoris_recherche_layout:nxw_fr_poste_select_tree:node:0:node:3::min:handle",
								"fav_rech_form:nxl_favoris_recherche_layout:nxw_fr_poste_select_tree:node:0:node:3:node:0::dir:handle",
								"fav_rech_form:nxl_favoris_recherche_layout:nxw_fr_poste_select_tree:node:0:node:3:node:0:node:0::addBtnPST"));
		ajouterAuxFavorisPage.assertTrue("Agents BDC (Economie)");

		ajouterAuxFavorisPage.ajouter();

		AjouterAuxFavorisPage.sleep(1);

		recherchePage.goToFavorisDeRecherche();
		recherchePage.assertTrue("Selenium FavRecAdminsgg");

		logoutEpg();
	}

	@WebTest(description = "Favoris de Recherche/connexion bdc", order = 240)
	@TestDocumentation(categories = { "Favoris" })
	public void testCase0101_1() {
		TraitementPage traitementPage = loginEpg(UTILISATEUR_BDC);
		RecherchePage recherchePage = traitementPage.getOngletMenu().goToRecherche();
		recherchePage.goToFavorisDeRecherche();
		recherchePage.assertTrue("Selenium FavRecAdminsgg");

		logoutEpg();
	}

	@WebTest(description = "Favoris de Recherche/connexion finance_bdc", order = 250)
	@TestDocumentation(categories = { "Favoris" })
	public void testCase0101_2() {
		TraitementPage traitementPage = loginEpg("finance_bdc");
		RecherchePage recherchePage = traitementPage.getOngletMenu().goToRecherche();
		recherchePage.goToFavorisDeRecherche();
		recherchePage.assertFalse("Selenium FavRecAdminsgg");

		logoutEpg();

	}

	@WebTest(description = "Derniers Dossiers Consultés", order = 260)
	@TestDocumentation(categories = { "Recherche" })
	public void testCase0102() {

		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");

		RecherchePage recherchePage = traitementPage.getOngletMenu().goToRecherche();
		recherchePage.goToDernierDossier();

		// Verify Element Text
		recherchePage.assertElementValue(By_enum.XPATH,
				"//form[@id='recherche_resultats_consultes_dossier']/div/table/tbody/tr[1]/td[2]", "EFIM"
						+ anneeCourante + "00004V");
		recherchePage.assertElementValue(By_enum.XPATH,
				"//form[@id='recherche_resultats_consultes_dossier']/div/table/tbody/tr[2]/td[2]", "EFIM"
						+ anneeCourante + "00005V");

	}

	@WebTest(description = "Derniers Utilisateurs Consultés", order = 280)
	@TestDocumentation(categories = { "Recherche utilisateurs" })
	public void testCase0111() {
		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");
		RecherchePage recherchePage = traitementPage.getOngletMenu().goToRecherche();
		DernierResultatConsulteUtilisateurPage dernierResultatConsulteUtilisateurPage = recherchePage
				.goToDernierResultatConsulteUtilisateur();
		WebElement element = dernierResultatConsulteUtilisateurPage.findElement(By
				.xpath(DernierResultatConsulteUtilisateurPage.RECHERCHE_RESULTATS_CONSULTES_USER_1));
		dernierResultatConsulteUtilisateurPage.checkValue(element, UTILISATEUR_BDC, true);
		element = dernierResultatConsulteUtilisateurPage.findElement(By
				.xpath(DernierResultatConsulteUtilisateurPage.RECHERCHE_RESULTATS_CONSULTES_USER_2));
		dernierResultatConsulteUtilisateurPage.checkValue(element, "finance_bdc", true);
		ViewUtilisateurDetailsPage viewUtilisateurDetails = dernierResultatConsulteUtilisateurPage
				.goToUserDetails("finance_bdc");
		WebElement element2 = viewUtilisateurDetails.findElement(By
				.xpath(DernierResultatConsulteUtilisateurPage.RECHERCHE_RESULTATS_CONSULTES_USER_1));
		viewUtilisateurDetails.checkValue(element2, "finance_bdc", true);
		element2 = viewUtilisateurDetails.findElement(By
				.xpath(DernierResultatConsulteUtilisateurPage.RECHERCHE_RESULTATS_CONSULTES_USER_2));
		viewUtilisateurDetails.checkValue(element2, UTILISATEUR_BDC, true);
		logoutEpg();
	}

	@WebTest(description = "Recherche > modèles de feuilles de route", order = 290)
	@TestDocumentation(categories = { "Recherche", "FDR" })
	public void testCase0115() {
		TraitementPage traitementPage = loginAsAdminEpg();
		RecherchePage recherchePage = traitementPage.getOngletMenu().goToRecherche();
		RechercheModeleFeuilleDeRoutePage rechercheModeleFeuilleDeRoutePage = recherchePage
				.goToRechercheModeleFeuilleDeRoute();

		rechercheModeleFeuilleDeRoutePage.setIntitule("Feuille de route standard");
		rechercheModeleFeuilleDeRoutePage.setNumero("2");
		rechercheModeleFeuilleDeRoutePage.setTypeActe("Avis");
		DernierFeuilleDeRouteResultat resultat = rechercheModeleFeuilleDeRoutePage.rechercher();

		resultat.assertTrue("Résultats de la recherche");
		resultat.assertTrue("intitulé COMME Feuille de route standard");

		resultat.getDetail("EFI - Avis - Feuille de route standard"); // créé par testCase001

		resultat.assertTrue("Résultats de la recherche");
		resultat.assertTrue("intitulé COMME Feuille de route standard");
		resultat.assertElementPresent(By_enum.XPATH, "//input[@value='Modifier le modèle']");

		// Modifier la recherche
		resultat.modifierLaRecherche();
		AjouterAuxFavorisPage ajouterAuxFavoris = rechercheModeleFeuilleDeRoutePage.ajouterAuxFavoris();
		ajouterAuxFavoris.setIntitule("Favori FDR 1");
		ajouterAuxFavoris
				.setDestinataire(Arrays
						.asList("fav_rech_form:nxl_favoris_recherche_layout:nxw_fr_poste_select_tree:node:0:node:3::min:handle",
								"fav_rech_form:nxl_favoris_recherche_layout:nxw_fr_poste_select_tree:node:0:node:3:node:0::dir:handle",
								"fav_rech_form:nxl_favoris_recherche_layout:nxw_fr_poste_select_tree:node:0:node:3:node:0:node:0::addBtnPST"));
		ajouterAuxFavoris.ajouter();

		// Clic sur favoris de recherche qui se trouve a gauche
		recherchePage.goToFavorisDeRecherche();
		resultat.getDetailPage("Favori FDR 1");
		resultat.assertTrue("Résultats de la recherche");
		resultat.assertTrue("EFI - Avis - Feuille de route standard");

		// Modifier la recherche
		DernierFeuilleDeRouteResultat.sleep(1);
		resultat.modifierLaRecherche();

		rechercheModeleFeuilleDeRoutePage.reinitialiser();
		RechercheModeleFeuilleDeRoutePage.sleep(1);

		resultat = rechercheModeleFeuilleDeRoutePage.rechercher();
		resultat.getDetail("Feuille de route par défaut");
		resultat.assertTrue("Pour initialisation");

		// Slect The checkBox In first Row
		resultat.selectRow("recherche_modele_feuille_route_resultat:nxl_recherche_feuille_route_model_listing:nxw_listing_ajax_selection_box");
		// Slect The checkBox In Second Row
		resultat.selectRow("recherche_modele_feuille_route_resultat:nxl_recherche_feuille_route_model_listing_1:nxw_listing_ajax_selection_box_1");

		new WebDriverWait(getDriver(), TIMEOUT_SEC).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='Ajouter à la sélection']")));
		new WebDriverWait(getDriver(), TIMEOUT_SEC).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='Exporter la liste des résultats']")));

		logoutEpg();
	}

	@WebTest(description = "Recherche > Derniers modèles FDR consultés", order = 300)
	@TestDocumentation(categories = { "Recherche", "FDR" })
	public void testCase0116() {
		TraitementPage traitementPage = loginAsAdminEpg();
		RecherchePage recherchePage = traitementPage.getOngletMenu().goToRecherche();

		DernierFeuilleDeRouteResultat resultatRecherchePage = recherchePage.goToDernierModeleFeuilleDeRoute();
		resultatRecherchePage.assertElementValue(By_enum.XPATH,
				"//form[@id='recherche_resultats_consultes_modele_feuille_route']/div/table/tbody/tr[1]/td[3]",
				"Feuille de route par défaut");
		resultatRecherchePage.assertElementValue(By_enum.XPATH,
				"//form[@id='recherche_resultats_consultes_modele_feuille_route']/div/table/tbody/tr[2]/td[3]",
				"EFI - Avis - Feuille de route standard");

		resultatRecherchePage.getDetail("EFI - Avis - Feuille de route standard");

		resultatRecherchePage.assertElementValue(By_enum.XPATH,
				"//form[@id='recherche_resultats_consultes_modele_feuille_route']/div/table/tbody/tr[1]/td[3]",
				"EFI - Avis - Feuille de route standard");
		resultatRecherchePage.assertElementValue(By_enum.XPATH,
				"//form[@id='recherche_resultats_consultes_modele_feuille_route']/div/table/tbody/tr[2]/td[3]",
				"Feuille de route par défaut");

		resultatRecherchePage.assertTrue("Pour initialisation");
		logoutEpg();
	}

	@WebTest(description = "Favoris de consultation", order = 310)
	@TestDocumentation(categories = { "Favoris" })
	public void testCase0120() {
		TraitementPage traitementPage = loginAsAdminEpg();
		RecherchePage recherchePage = traitementPage.getOngletMenu().goToRecherche();
		DernierDossierResultat dernierDossier = recherchePage.goToDernierDossier();
		dernierDossier.selectRow(1);
		dernierDossier.waitForPageSourcePart(By.id(DernierDossierResultat.AJOUTER_AUX_FAVORIS_DE_CONSULTATIONS_BTN_ID),
				TIMEOUT_SEC);
		dernierDossier.ajouterAuxFavorisDeConsultaion();

		RechercheUtilisateurPage rechercheUtilisateurPage = recherchePage.goToRechercheUtilisateur();
		rechercheUtilisateurPage.setIdentifiant(UTILISATEUR_BDC);
		CommonRechercheUtilisateurPage resultatRechercheUtilisateurPage = rechercheUtilisateurPage.rechercher();
		resultatRechercheUtilisateurPage.assertTrue(UTILISATEUR_BDC);
		resultatRechercheUtilisateurPage
				.getElementById(
						"recherche_user_resultat:nxl_recherche_user_listing:nxw_user_listing_ajax_selection_box:nxw_user_listing_ajax_selection_box")
				.click();
		resultatRechercheUtilisateurPage.waitForPageSourcePart(
				By.id("recherche_user_resultat:clipboardActionsTable__0:1:clipboardActionsButton"), TIMEOUT_SEC);
		resultatRechercheUtilisateurPage.getElementById(
				"recherche_user_resultat:clipboardActionsTable__0:1:clipboardActionsButton").click();

		DernierFeuilleDeRouteResultat dernierFeuilleDeRouteResultat = recherchePage.goToDernierModeleFeuilleDeRoute();

		dernierFeuilleDeRouteResultat.selectRow(1);
		dernierFeuilleDeRouteResultat.selectRow(2);
		dernierFeuilleDeRouteResultat.waitForPageSourcePart(
				By.xpath(DernierFeuilleDeRouteResultat.AJOUTER_AUX_FAVORIS_DE_CONSULTATION_PATH), TIMEOUT_SEC);
		dernierFeuilleDeRouteResultat.ajouterAuxFavorisDeConsultation();

		// Favoris FDR
		FavorisConsultationFeulleDeRoute favorsiDeConsultationFDR = recherchePage.goToFavorsiDeConsultationFDR();

		favorsiDeConsultationFDR.assertTrue("Feuille de route par défaut");
		favorsiDeConsultationFDR.assertTrue("EFI - Avis - Feuille de route standard");
		// Favoris dossier
		FavorisConsultationDossier favorsiDeConsultationDossier = recherchePage.goToFavorsiDeConsultationDossier();
		FavorisConsultationDossier.sleep(1);
		favorsiDeConsultationDossier.assertTrue("EFIM" + anneeCourante + "00004V");

		favorsiDeConsultationDossier.selectRow(1);
		favorsiDeConsultationDossier.retirerDesFavorisDeConsultation();
		FavorisConsultationDossier.sleep(1);
		favorsiDeConsultationDossier.assertTrue("1 document retiré");

		// Favoris Utilisateurs
		FavorisConsultationUtilisateur favoriDeConsultationUtilisateurs = recherchePage
				.goToFavoriDeConsultationUtilisateurs();

		favoriDeConsultationUtilisateurs.assertTrue(UTILISATEUR_BDC);
		favoriDeConsultationUtilisateurs.selectRow(1);
		favoriDeConsultationUtilisateurs.retirerDesFavorisDeConsultation();
		FavorisConsultationDossier.sleep(1);
		favoriDeConsultationUtilisateurs.assertFalse(UTILISATEUR_BDC);
		logoutEpg();
	}

	@WebTest(description = "Webservices", order = 370)
	@TestDocumentation(categories = { "WS", "FDR", "Parapheur", "Bordereau" })
	public void testCase0150() {

		StringBuilder xmlForWs = new StringBuilder();
		xmlForWs.append("<?xml version='1.0'");
		xmlForWs.append(" encoding='UTF-8'?>");
		xmlForWs.append(" <p:attribuerNorRequest");
		xmlForWs.append(" xmlns:p='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg'");
		xmlForWs.append(" xmlns:p1='http://www.dila.premier-ministre.gouv.fr/solon/epg/actes' xmlns:p2='http://www.dila.premier-ministre.gouv.fr/solon/epg/solon-commons' xmlns:p3='http://www.dila.premier-ministre.gouv.fr/solon/epg/ar'");
		xmlForWs.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
		xmlForWs.append(" xsi:schemaLocation='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg ../../../../../../../../xsd/solon/epg/WSepg.xsd '>");
		xmlForWs.append(" <p:type_acte>AVIS</p:type_acte>");
		xmlForWs.append(" <p:code_entite>DRD</p:code_entite>");
		xmlForWs.append(" <p:code_direction>A</p:code_direction>");
		xmlForWs.append(" </p:attribuerNorRequest>");

		WsResultPage wsResultPage = loginWSEpg("ws_ministere_cco", "ws_ministere_cco", getEpgUrl()
				+ "/site/solonepg/WSepg/attribuerNor", xmlForWs.toString());
		WsResultPage.sleep(2);
		wsResultPage
				.assertTextResult("&lt;attribuerNorResponse xmlns=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg&quot;&gt;&lt;statut&gt;KO&lt;/statut&gt;&lt;code_erreur&gt;ENTITE_INCONNUE&lt;/code_erreur&gt;&lt;/attribuerNorResponse&gt;");

		xmlForWs.delete(0, xmlForWs.length());
		// Prepate a new request
		xmlForWs.append("<?xml version='1.0' ");
		xmlForWs.append(" encoding='UTF-8'?>");
		xmlForWs.append(" <p:attribuerNorRequest");
		xmlForWs.append(" xmlns:p='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg'");
		xmlForWs.append(" xmlns:p1='http://www.dila.premier-ministre.gouv.fr/solon/epg/actes' xmlns:p2='http://www.dila.premier-ministre.gouv.fr/solon/epg/solon-commons' xmlns:p3='http://www.dila.premier-ministre.gouv.fr/solon/epg/ar'");
		xmlForWs.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'   xsi:schemaLocation='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg ../../../../../../../../xsd/solon/epg/WSepg.xsd '> <p:type_acte>AVIS</p:type_acte>");
		xmlForWs.append(" <p:code_entite>CCO</p:code_entite>");
		xmlForWs.append(" <p:code_direction>W</p:code_direction>");
		xmlForWs.append(" </p:attribuerNorRequest>");

		wsResultPage.setRequest(xmlForWs.toString());
		wsResultPage.doSubmit();
		WsResultPage.sleep(2);
		wsResultPage
				.assertTextResult("&lt;attribuerNorResponse xmlns=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg&quot;&gt;&lt;statut&gt;KO&lt;/statut&gt;&lt;code_erreur&gt;DIRECTION_INCONNUE&lt;/code_erreur&gt;&lt;/attribuerNorResponse&gt;");

		xmlForWs.delete(0, xmlForWs.length());
		// Prepate a new request
		xmlForWs.append("<?xml version='1.0'");
		xmlForWs.append(" encoding='UTF-8'?>");
		xmlForWs.append(" <p:attribuerNorRequest");
		xmlForWs.append(" xmlns:p='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg'");
		xmlForWs.append(" xmlns:p1='http://www.dila.premier-ministre.gouv.fr/solon/epg/actes'");
		xmlForWs.append(" xmlns:p2='http://www.dila.premier-ministre.gouv.fr/solon/epg/solon-commons'");
		xmlForWs.append(" xmlns:p3='http://www.dila.premier-ministre.gouv.fr/solon/epg/ar'");
		xmlForWs.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
		xmlForWs.append(" xsi:schemaLocation='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg ../../../../../../../../xsd/solon/epg/WSepg.xsd '>");
		xmlForWs.append(" <p:type_acte>AVIS</p:type_acte>");
		xmlForWs.append(" <p:code_entite>CCO</p:code_entite>");
		xmlForWs.append(" <p:code_direction>Z</p:code_direction>");
		xmlForWs.append(" </p:attribuerNorRequest>");

		logoutWS();

		// Login bdc

		TraitementPage traitementPage = loginEpg(UTILISATEUR_BDC, UTILISATEUR_BDC, TraitementPage.class);
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		DossierDetailMenu dossierDetail = creationPage.getDossierDetail("EFIM" + anneeCourante + "00000D");
		FeuilleDeRoutePage feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();

		// Verouiler
		feuilleDeRoute.verrouiller();
		// ajout tâche "pour avis Ce"
		CreationEtapePage creationEtapePage = feuilleDeRoute.ajouterApres();
		creationEtapePage.setTypeEtape("Pour avis CE");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:9::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:9:node:6::poste:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:9:node:6::nxw_routing_task_distribution_mailbox_addPst"));
		// Create
		creationEtapePage.creer();
		CreationEtapePage.sleep(2);

		// ajout tâche "pour publication Dila Bo"
		creationEtapePage = feuilleDeRoute.ajouterApres();
		creationEtapePage.setTypeEtape("Pour publication à la DILA BO");
		CreationEtapePage.sleep(2);
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62:node:5::poste:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62:node:5::nxw_routing_task_distribution_mailbox_addPst"));
		// Create
		creationEtapePage.creer();
		CreationEtapePage.sleep(2);

		// Go to Parapheur
		fr.dila.solonepg.page.main.onglet.dossier.detail.ParapheurPage parapheur = dossierDetail.goToParapheur();
		parapheur.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']", "word2003.doc");

		CreationEtapePage.sleep(5);

		// Go to bordereau
		BordereauPage bordereauPage = dossierDetail.goToBordereau();
		bordereauPage.setTitreActe("Titre acte testCase0150");

		bordereauPage.setNomDuResponsable("DUPONT");

		bordereauPage.setQualiteDuResponsable("Mr");

		bordereauPage.setTelDuResponsable("0238587469");

		bordereauPage.setPrenomDuResponsable("Jean");

		bordereauPage.setModeParution("Electronique");

		bordereauPage.setDelaiPublication("Aucun");

		bordereauPage.setRubriques("Rubrique de la pêche");

		// Sauvegarder le bordereau
		bordereauPage.enregistrer();
		bordereauPage.deverouiller_par_alt();
		bordereauPage.assertTrue("DUPONT");
		bordereauPage.assertTrue("Jean");
		bordereauPage.assertTrue("0238587469");
		bordereauPage.assertTrue("Electronique");
		bordereauPage.assertTrue("Aucun");
		bordereauPage.assertTrue("Rubrique de la pêche");

		// Vrouiller
		bordereauPage.verrouiller();

		// Lancer le dossier
		feuilleDeRoute.lancerDossier();
		feuilleDeRoute.assertTrue("est lancé");
	}

	@WebTest(description = "Webservices/ajoute les étapes publications JO et pour Epreuve (etape pouvant ne pouvant pas être créé avec un profil administrateur ministériel)", order = 380)
	@TestDocumentation(categories = { "WS", "Recherche", "FDR", "Publication" })
	public void testCase0150_1() {

		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");

		RecherchePage recherchePage = traitementPage.rechercheNor("EFI*");

		DossierDetailMenu dossierDetail = recherchePage.getDossierDetail("EFIM" + anneeCourante + "00000D");

		FeuilleDeRoutePage feuilleDeRoute = dossierDetail.goToFeuilleDeRoute();
		// Verrouiller
		feuilleDeRoute.verrouiller();

		CreationEtapePage creationEtapePage = feuilleDeRoute.ajouterApres();
		creationEtapePage.setTypeEtape("Pour publication à la DILA JO");
		CreationEtapePage.sleep(1);
		List<String> buttonAdmSgg = Arrays
				.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62::min:handle:img:collapsed",
						"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62:node:5::poste:handle:img:collapsed",
						"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62:node:5::nxw_routing_task_distribution_mailbox_addPst");
		creationEtapePage.setDestinataire(buttonAdmSgg);
		// Creer
		creationEtapePage.waitForPageSourcePart("Administrateur S.O.L.O.N. (SGG)", TIMEOUT_SEC);
		creationEtapePage.creer();

		// ajout tâche "pour Epreuve"
		creationEtapePage = feuilleDeRoute.ajouterApres();
		creationEtapePage.setTypeEtape("Pour fourniture d'épreuves");
		CreationEtapePage.sleep(1);
		creationEtapePage.setDestinataire(buttonAdmSgg);
		creationEtapePage.waitForPageSourcePart("Administrateur S.O.L.O.N. (SGG)", TIMEOUT_SEC);
		// Creer
		creationEtapePage.creer();

		// Deverouiller
		feuilleDeRoute.deverouiller_par_alt();

		logoutEpg();
	}

	@WebTest(description = "Webservices/mise à jour de l'utilisateur MCAPPERON comme administrateur webservice  du ministere CE,Mise à jour du ministère CE", order = 390)
	@TestDocumentation(categories = { "Administration", "TDR", "Utilisateurs" })
	public void testCase0150_2() {

		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();

		administrationEpgPage.assertTrue("Tables de référence");

		TablesDeReferencePage tablesDeReference = administrationEpgPage.goToTablesDeReference();
		tablesDeReference.setConseilleDetat();

		tablesDeReference.assertTrue("Conseil d'État");
		// enregistrement ministere CE
		tablesDeReference.enregistrer();

		// Clic sur Gestion des utilisateurs
		GestionUtilisateurs gestionUtilisateurs = administrationEpgPage.goToGestionUtilisateurs();
		gestionUtilisateurs.chercherUtilisateurs("MCA");

		UserDetailPage detail = gestionUtilisateurs.getDetail("MCAPPERON");
		ModifierUser modifier = detail.modifier();
		modifier.selectMonsieur();
		modifier.setTelePhone("0611223344");
		modifier.setMail("ab.rs@ze-tb.com");
		modifier.setProfile("Webservices Ministériels");
		UserDetailPage.sleep(2);
		// Save Data
		modifier.enregistrer();
		// Ajout d'un sleep car sinon c'est des fois trop rapide
		UserDetailPage.sleep(2);
		detail.assertTrue("0611223344");
		detail.assertTrue("ab.rs@ze-tb.com");
		detail.assertTrue("Webservices Ministériels");
	}

	@WebTest(description = "Webservices/webservices WS EPG : ChercherDossier : recherche par Nor", order = 400)
	@TestDocumentation(categories = { "WS", "Conseil d'état" })
	public void testCase0150_3() {

		StringBuilder xmlForWs = new StringBuilder();

		xmlForWs.append("<?xml version='1.0' encoding='UTF-8'?>");
		xmlForWs.append(" <p:chercherDossierRequest xmlns:p='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg'");
		xmlForWs.append(" xmlns:p1='http://www.dila.premier-ministre.gouv.fr/solon/epg/actes'");
		xmlForWs.append(" xmlns:p2='http://www.dila.premier-ministre.gouv.fr/solon/pg/solon-commons'");
		xmlForWs.append(" xmlns:p3='http://www.dila.premier-ministre.gouv.fr/solon/epg/ar'");
		xmlForWs.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
		xmlForWs.append(" xsi:schemaLocation='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg ../../../../../../../../xsd/solon/epg/WSepg.xsd '>");
		// xmlForWs.append(" <p:nor>CCOZ1400011V</p:nor>");
		xmlForWs.append(" <p:nor>EFIM" + anneeCourante + "00000D</p:nor>");
		xmlForWs.append(" </p:chercherDossierRequest>");

		WsResultPage wsResultPage = loginWSEpg("MCAPPERON", "MCAPPERON", getEpgUrl()
				+ "/site/solonepg/WSepg/chercherDossier", xmlForWs.toString());
		WsResultPage.sleep(2);
		// Dans la valeur attendue, on a enlevé les références à la date du jour, et le contenu du fichier WORD
		wsResultPage
				.assertTextResult("<chercherDossierResponse xmlns:ns4=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/ar&quot; xmlns:ns3=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/solon-commons&quot; xmlns:ns2=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/actes&quot; xmlns=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg&quot;><statut>OK</statut><jeton>-1</jeton><dernier_envoi>true</dernier_envoi><dossier><ns3:DossierEpg><ns3:decret_ce_art_37><ns2:statut_acte>INITIE</ns2:statut_acte><ns2:type_acte>DECRET_CE_ART_37</ns2:type_acte><ns2:nor>EFIM1500000D</ns2:nor><ns2:titre_acte>Titre acte 7</ns2:titre_acte><ns2:entite_responsable>Min. Economie, finances et industrie</ns2:entite_responsable><ns2:direction_responsable>DAJ (Economie)</ns2:direction_responsable><ns2:ministere_rattachement>Min. Economie, finances et industrie</ns2:ministere_rattachement><ns2:direction_rattachement>DAJ (Economie)</ns2:direction_rattachement><ns2:createur>system</ns2:createur><ns2:responsable><ns2:nom_reponsable>DUPONT</ns2:nom_reponsable><ns2:prenom_responsable>Jean</ns2:prenom_responsable><ns2:qualite_responsable>Mr</ns2:qualite_responsable><ns2:telephone_responsable>0238587469</ns2:telephone_responsable></ns2:responsable><ns2:categorie_acte>REGLEMENTAIRE</ns2:categorie_acte><ns2:vecteur_de_publication>Journal Officiel</ns2:vecteur_de_publication><ns2:mode_parution>ELECTRONIQUE</ns2:mode_parution><ns2:delai_publication>AUCUN</ns2:delai_publication>");

		// webservices WS EPG : donnerAvisCE
		xmlForWs.delete(0, xmlForWs.length());
		// Prepare a new request
		xmlForWs.append("<?xml version='1.0' encoding='UTF-8'?>");
		xmlForWs.append("<p:chercherDossierRequest");
		xmlForWs.append(" xmlns:p='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg' ");
		xmlForWs.append(" xmlns:p1='http://www.dila.premier-ministre.gouv.fr/solon/epg/actes'");
		xmlForWs.append(" xmlns:p2='http://www.dila.premier-ministre.gouv.fr/solon/pg/solon-commons'");
		xmlForWs.append(" xmlns:p3='http://www.dila.premier-ministre.gouv.fr/solon/epg/ar'");
		xmlForWs.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
		xmlForWs.append(" xsi:schemaLocation='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg ../../../../../../../../xsd/solon/epg/WSepg.xsd' >");
		xmlForWs.append("<p:jeton>0</p:jeton>");
		xmlForWs.append("</p:chercherDossierRequest>");
		wsResultPage.setRequest(xmlForWs.toString());
		wsResultPage.doSubmit();
		WsResultPage.sleep(2);
		// wsResultPage.assertTextRersult("&lt;chercherDossierResponse xmlns=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg&quot; xmlns:ns2=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/actes&quot; xmlns:ns3=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/solon-commons&quot; xmlns:ns4=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/ar&quot;&gt;&lt;statut&gt;OK&lt;/statut&gt;&lt;jeton&gt;1&lt;/jeton&gt;&lt;dernier_envoi&gt;true&lt;/dernier_envoi&gt;&lt;dossier&gt;&lt;ns3:DossierEpg&gt;&lt;ns3:decret_ce_art_37&gt;&lt;ns2:statut_acte&gt;INITIE&lt;/ns2:statut_acte&gt;&lt;ns2:type_acte&gt;DECRET_CE_ART_37&lt;/ns2:type_acte&gt;&lt;ns2:nor&gt;EFIM1400000D&lt;/ns2:nor&gt;&lt;ns2:titre_acte&gt;Titre acte 7&lt;/ns2:titre_acte&gt;&lt;ns2:entite_responsable&gt;Min. Economie, finances et industrie&lt;/ns2:entite_responsable&gt;&lt;ns2:direction_responsable&gt;DAJ (Economie)&lt;/ns2:direction_responsable&gt;&lt;ns2:ministere_rattachement&gt;Min. Economie, finances et industrie&lt;/ns2:ministere_rattachement&gt;&lt;ns2:direction_rattachement&gt;DAJ (Economie)&lt;/ns2:direction_rattachement&gt;&lt;ns2:createur&gt;Bdc&lt;/ns2:createur&gt;&lt;ns2:responsable&gt;&lt;ns2:nom_reponsable&gt;DUPONT&lt;/ns2:nom_reponsable&gt;&lt;ns2:prenom_responsable&gt;Jean&lt;/ns2:prenom_responsable&gt;&lt;ns2:qualite_responsable&gt;Mr&lt;/ns2:qualite_responsable&gt;&lt;ns2:telephone_responsable&gt;0238587469&lt;/ns2:telephone_responsable&gt;&lt;/ns2:responsable&gt;&lt;ns2:categorie_acte&gt;REGLEMENTAIRE&lt;/ns2:categorie_acte&gt;&lt;ns2:vecteur_de_publication&gt;Journal Officiel&lt;/ns2:vecteur_de_publication&gt;&lt;ns2:mode_parution&gt;ELECTRONIQUE&lt;/ns2:mode_parution&gt;&lt;ns2:delai_publication&gt;AUCUN&lt;/ns2:delai_publication&gt;&lt;ns2:indexation&gt;");

		// webservices WS EPG : donnerAvisCE
		xmlForWs.delete(0, xmlForWs.length());
		// Prepate a new request
		xmlForWs.append("<?xml version='1.0' encoding='UTF-8'?>");
		xmlForWs.append(" <p:donnerAvisCERequest");
		xmlForWs.append(" xmlns:p='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg'");
		xmlForWs.append(" xmlns:p1='http://www.dila.premier-ministre.gouv.fr/solon/epg/actes'");
		xmlForWs.append(" xmlns:p2='http://www.dila.premier-ministre.gouv.fr/solon/epg/solon-commons'");
		xmlForWs.append(" xmlns:p3='http://www.dila.premier-ministre.gouv.fr/solon/epg/ar'");
		xmlForWs.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
		xmlForWs.append(" xsi:schemaLocation='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg ../../../../../../../../xsd/solon/epg/WSepg.xsd '>");
		xmlForWs.append(" <p:nor>EFIM" + anneeCourante + "00000D</p:nor>");
		xmlForWs.append(" <p:type_validation_ce>AVIS_RENDU</p:type_validation_ce>");
		xmlForWs.append(" <p:section_ce>");
		xmlForWs.append(" <p1:rapporteur>rapporteur</p1:rapporteur>");
		xmlForWs.append(" <p1:date_transmission_section_ce>2011-01-01T09:00:00</p1:date_transmission_section_ce>");
		xmlForWs.append(" <p1:date_section_ce>2011-01-01T09:00:00</p1:date_section_ce>");
		xmlForWs.append(" <p1:date_ag_ce>2011-01-01T09:00:00</p1:date_ag_ce>");
		xmlForWs.append(" <p1:numero_isa>numeroISA</p1:numero_isa>");
		xmlForWs.append(" </p:section_ce>");
		xmlForWs.append(" <p:projet_decret>");
		xmlForWs.append(" <p2:nom>projet decret</p2:nom>");
		xmlForWs.append(" <p2:mime_type>text/plain</p2:mime_type>");
		xmlForWs.append(" <p2:taille_fichier>123</p2:taille_fichier>");
		xmlForWs.append(" <p2:contenu>SGVsbG8gV29ybGQ=</p2:contenu>");
		xmlForWs.append(" </p:projet_decret>");
		xmlForWs.append(" <p:note_gouvernement>");
		xmlForWs.append(" <p2:nom>note au gouvernement</p2:nom>");
		xmlForWs.append(" <p2:mime_type>text/plain</p2:mime_type>");
		xmlForWs.append(" <p2:taille_fichier>345</p2:taille_fichier>");
		xmlForWs.append(" <p2:contenu>SGVsbG8gV29ybGQ=</p2:contenu>");
		xmlForWs.append(" </p:note_gouvernement>");
		xmlForWs.append(" </p:donnerAvisCERequest>");

		wsResultPage.setUrl(getEpgUrl() + "/site/solonepg/WSepg/donnerAvisCE");
		wsResultPage.setRequest(xmlForWs.toString());
		wsResultPage.doSubmit();
		WsResultPage.sleep(2);
		wsResultPage
				.assertTextResult("&lt;donnerAvisCEResponse xmlns=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg&quot;&gt;&lt;statut&gt;OK&lt;/statut&gt;&lt;nor&gt;EFIM"
						+ anneeCourante + "00000D&lt;/nor&gt;&lt;/donnerAvisCEResponse&gt;");

		// webservices WS EPG : donnerAvisCE : verifie que l'on ne peut pas donner plusieurs fois un avis
		wsResultPage.doSubmit();
		WsResultPage.sleep(2);
		wsResultPage
				.assertTextResult("<donnerAvisCEResponse xmlns=\"http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg\"><statut>KO</statut><nor>EFIM"
						+ anneeCourante
						+ "00000D</nor><message_erreur>Le dossier n'est pas à l'étape 'Pour avis CE' ! Il n'est pas possible de donner un avis du conseil d'Etat sur ce dossier !</message_erreur></donnerAvisCEResponse>");
	}

	@WebTest(description = "Webservices/connexion avec utilisateur publication", order = 410)
	@TestDocumentation(categories = { "WS", "Publication" })
	public void testCase0150_4() {
		StringBuilder xmlForWs = new StringBuilder();

		xmlForWs.append("<?xml version='1.0' encoding='UTF-8'?> ");
		xmlForWs.append("<p:envoyerRetourPERequest");
		xmlForWs.append(" xmlns:p='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSSpe'");
		xmlForWs.append(" xmlns:p1='http://www.dila.premier-ministre.gouv.fr/solon/epg/spe-commons' ");
		xmlForWs.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ");
		xmlForWs.append(" xsi:schemaLocation='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSSpe ../../../../../../../xsd/solon/epg/WSspe.xsd '> ");
		xmlForWs.append(" <p:type>PUBLICATION_BO</p:type>");
		xmlForWs.append(" <p:retourPublicationBo>");
		xmlForWs.append(" <p1:gestion>");
		xmlForWs.append(" <p1:date_parution>2011-01-01</p1:date_parution>");
		xmlForWs.append(" </p1:gestion>");
		xmlForWs.append(" <p1:acte>");
		xmlForWs.append(" <p1:nor>EFIM" + anneeCourante + "00000D</p1:nor>");
		xmlForWs.append(" <p1:numero_texte>100</p1:numero_texte>");
		xmlForWs.append(" <p1:page>20</p1:page>");
		xmlForWs.append(" </p1:acte>");
		xmlForWs.append(" <p1:acte>");
		xmlForWs.append(" <p1:nor>EFIM" + anneeCourante + "00000D</p1:nor>");
		xmlForWs.append(" <p1:numero_texte>101</p1:numero_texte>");
		xmlForWs.append(" <p1:page>21</p1:page>");
		xmlForWs.append(" </p1:acte>");
		xmlForWs.append(" </p:retourPublicationBo>");
		xmlForWs.append(" </p:envoyerRetourPERequest>");

		WsResultPage wsResultPage = loginWSEpg("ws_ministere_prm", "ws_ministere_prm", getEpgUrl()
				+ "/site/solonepg/WSspe/envoyerRetourPE", xmlForWs.toString());
		wsResultPage
				.assertTextResult("&lt;envoyerRetourPEResponse xmlns=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/WSSpe&quot;&gt;&lt;status&gt;OK&lt;/status&gt;&lt;/envoyerRetourPEResponse&gt;");
		logoutWS();
	}

	@WebTest(description = "Webservices/webservices WS SPE : envoyerRetourPE : publicationBO : vérification et validation étape", order = 420)
	@TestDocumentation(categories = { "WS", "Publication" })
	public void testCase0150_5() {

		StringBuilder xmlForWs = new StringBuilder();
		xmlForWs.append("<?xml version='1.0' encoding='UTF-8'?>");
		xmlForWs.append(" <p:envoyerRetourPERequest");
		xmlForWs.append(" xmlns:p='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSSpe'");
		xmlForWs.append(" xmlns:p1='http://www.dila.premier-ministre.gouv.fr/solon/epg/spe-commons'");
		xmlForWs.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
		xmlForWs.append(" xsi:schemaLocation='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSSpe ../../../../../../../xsd/solon/epg/WSspe.xsd '>");
		xmlForWs.append(" <p:type>PUBLICATION_JO</p:type>");
		xmlForWs.append(" <p:retourPublicationJo>");
		xmlForWs.append(" <p1:gestion>");
		xmlForWs.append(" <p1:date_parution>2011-01-01</p1:date_parution>");
		xmlForWs.append(" </p1:gestion> <p1:acte> <p1:nor>EFIM" + anneeCourante + "00000D</p1:nor>");
		xmlForWs.append(" <p1:numero_texte>num_text_100</p1:numero_texte>");
		xmlForWs.append(" <p1:titre_officiel>titre officiel 100</p1:titre_officiel>");
		xmlForWs.append(" <p1:page>100</p1:page>");
		xmlForWs.append(" </p1:acte>");
		xmlForWs.append(" </p:retourPublicationJo>");
		xmlForWs.append(" </p:envoyerRetourPERequest>");

		WsResultPage wsResultPage = loginWSEpg("ws_ministere_prm", "ws_ministere_prm", getEpgUrl()
				+ "/site/solonepg/WSspe/envoyerRetourPE", xmlForWs.toString());
		WsResultPage.sleep(2);
		wsResultPage
				.assertTextResult("&lt;envoyerRetourPEResponse xmlns=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/WSSpe&quot;&gt;&lt;status&gt;OK&lt;/status&gt;&lt;/envoyerRetourPEResponse&gt;");

	}

	@WebTest(description = "Webservices/webservices WS SPE : envoyerRetourPE : Epreuvage (ministere PRM)", order = 430)
	@TestDocumentation(categories = { "WS", "Publication" })
	public void testCase0150_6() {

		StringBuilder xmlForWs = new StringBuilder();
		xmlForWs.append("<?xml version='1.0' encoding='UTF-8'?>");
		xmlForWs.append(" <p:envoyerRetourPERequest");
		xmlForWs.append(" xmlns:p='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSSpe'");
		xmlForWs.append(" xmlns:p1='http://www.dila.premier-ministre.gouv.fr/solon/epg/spe-commons'");
		xmlForWs.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
		xmlForWs.append(" xsi:schemaLocation='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSSpe ../../../../../../../xsd/solon/epg/WSspe.xsd '>");
		xmlForWs.append(" <p:type>EPREUVAGE</p:type>");
		xmlForWs.append(" <p:retourEpreuvage>");
		xmlForWs.append(" <p1:nor>EFIM" + anneeCourante + "00000D</p1:nor>");
		xmlForWs.append(" <p1:epreuve>");
		xmlForWs.append(" <p1:nom>nom du fichier.txt</p1:nom>");
		xmlForWs.append(" <p1:content>MA==</p1:content>");
		xmlForWs.append(" </p1:epreuve>");
		xmlForWs.append(" </p:retourEpreuvage> ");
		xmlForWs.append(" </p:envoyerRetourPERequest>");

		WsResultPage wsResultPage = loginWSEpg("ws_ministere_prm", "ws_ministere_prm", getEpgUrl()
				+ "/site/solonepg/WSspe/envoyerRetourPE", xmlForWs.toString());
		WsResultPage.sleep(2);
		wsResultPage
				.assertTextResult("&lt;envoyerRetourPEResponse xmlns=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/WSSpe&quot;&gt;&lt;status&gt;OK&lt;/status&gt;&lt;/envoyerRetourPEResponse&gt;");

		logoutWS();
	}

	@WebTest(description = " 0160_ActiviteNormativeLoi.html", order = 440)
	@TestDocumentation(categories = { "Texte maitre", "Mesure PAN", "Décret PAN" })
	public void testCase0160() {
		TraitementPage traitementPage = loginAsAdminEpg();
		CreationPage espaceCreation = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page createDossierPage = espaceCreation.createDossier();
		createDossierPage.assertTrue("CCO");
		createDossierPage.assertTrue("Conseil de la concurrence (Economie)");
		createDossierPage.setTypeActe("Loi");

		CreationDossier108Page creationDossier108Page = createDossierPage
				.appuyerBoutonSuivant(CreationDossier108Page.class);
		espaceCreation = creationDossier108Page.appuyerBoutonTerminer(CreationPage.class);
		CreationDossier108Page.sleep(2);
		espaceCreation.assertTrue(getDossierIdPourCreationDuLoi());
		espaceCreation.assertFalse("Choix du poste");

		// Clic Pilotage de l'activite normative
		PilotageANPage pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();
		ApplicationDesLoisPage applicationDesLoisPage = pilotageAN.goToApplicationDesLoisPage();
		applicationDesLoisPage.waitElementVisibilityById("loi_form:loi_input");
		applicationDesLoisPage.ajouterNor(getDossierIdPourCreationDuLoi());
		applicationDesLoisPage.assertTrue(getDossierIdPourCreationDuLoi());
		applicationDesLoisPage.assertTrue("Ministère");
		// Go to the Detail
		DossierDetailMenu dossierDetailMenu = applicationDesLoisPage
				.getDossierDetailMenu(getDossierIdPourCreationDuLoi());
		TextMaitrePage textMaitrePage = dossierDetailMenu.goToTextMaitrePage();
		// Vrouiller
		textMaitrePage.verrouiller();
		textMaitrePage.assertTrue("Verrouillé le");
		// Assign data
		textMaitrePage.setMotCle("Mot clé");
		textMaitrePage.setNumeroLock("2011-0001");
		textMaitrePage.setNumeroInterne("2011-0001-27-4");
		textMaitrePage.setIntitule("Initulé " + getDossierIdPourCreationDuLoi());
		textMaitrePage.setNatureDuText("Projet de loi");
		textMaitrePage.setNatureDuVote("Procédure accélérée");
		textMaitrePage.selectNumeroLock();
		// Sauvegarder les donnees
		textMaitrePage.sauvegarder();

		// Assert Data
		textMaitrePage.assertTrue("Mot clé");
		textMaitrePage.assertTrue("2011-0001-27-4");
		textMaitrePage.assertTrue("2011-0001");

		// Recharger
		textMaitrePage.recharger();
		textMaitrePage.sauvegarder();
		textMaitrePage.assertTrue("Mot clé");
		textMaitrePage.assertTrue("2011-0001-27-4");
		textMaitrePage.assertTrue("2011-0001");
		textMaitrePage.assertFalse("Initulé " + getDossierIdPourCreationDuLoi());

		// Edit
		ModificationMesurePage modificationMesurePage = textMaitrePage.edit();
		modificationMesurePage.setNumeroOrdre("123");
		modificationMesurePage.valider();
		textMaitrePage.assertTrue("123");
		textMaitrePage.sauvegarderDetail();

		// Detail Decret
		DetailDecretPage detailDecretPage = textMaitrePage.detailDecretPage(0);
		detailDecretPage.ajouterNor("EFIM" + anneeCourante + "00000D");

		detailDecretPage
				.verifyValue(
						"texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_numeroNor_lock:nxw_activite_normative_widget_numeroNor_lock_text",
						"EFIM" + anneeCourante + "00000D");
		detailDecretPage.remove();
		detailDecretPage.sauvegarder();
		logoutEpg();

	}

	@WebTest(description = " 0160_ActiviteNormativeLoi.html/Création d'une Loi", order = 450)
	@TestDocumentation(categories = { "Dossier", "Texte maitre", "Décret PAN" })
	public void testCase0160_1() {

		TraitementPage traitementPage = loginAsAdminEpg();

		// Création d'un premier dossier qui ne sera pas ajouté au PAN
		CreationPage espaceCreation = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page createDossierPage = espaceCreation.createDossier();
		createDossierPage.assertTrue("CCO");
		createDossierPage.assertTrue("Conseil de la concurrence (Economie)");
		createDossierPage.setTypeActe("Décret en Conseil d'Etat");

		CreationDossier101Page creationDossier101Page = createDossierPage
				.appuyerBoutonSuivant(CreationDossier101Page.class);
		creationDossier101Page.assertTrue("Création d'un Décret CE");
		creationDossier101Page.checkapplicationLoi();
		CreationDossier104Page creationDossier104Page = creationDossier101Page
				.appuyerBoutonSuivant(CreationDossier104Page.class);
		creationDossier104Page.setReference("2011-0001");
		creationDossier104Page.setNumeroOrdre("124");
		creationDossier104Page.ajouterTransposition("2011-0001");
		espaceCreation = creationDossier104Page.appuyerBoutonTerminer(CreationPage.class);

		// Création du deuxième dossier qui sera bien ajouté au PAN
		espaceCreation = traitementPage.getOngletMenu().goToEspaceCreation();
		createDossierPage = espaceCreation.createDossier();
		createDossierPage.assertTrue("CCO");
		createDossierPage.assertTrue("Conseil de la concurrence (Economie)");
		createDossierPage.setTypeActe("Décret en Conseil d'Etat");

		creationDossier101Page = createDossierPage.appuyerBoutonSuivant(CreationDossier101Page.class);
		creationDossier101Page.assertTrue("Création d'un Décret CE");
		creationDossier101Page.checkapplicationLoi();
		creationDossier104Page = creationDossier101Page.appuyerBoutonSuivant(CreationDossier104Page.class);
		creationDossier104Page.setReference("2011-0001");
		creationDossier104Page.setNumeroOrdre("123");
		creationDossier104Page.ajouterTransposition("2011-0001");
		espaceCreation = creationDossier104Page.appuyerBoutonTerminer(CreationPage.class);

		// Clic Pilotage de l'activite normative
		PilotageANPage pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();
		TableauMaitreViewPage applicationDesLoisPage = pilotageAN.goToApplicationDesLoisTextMaitreViewPage();
		applicationDesLoisPage.waitElementVisibilityById("loi_form:loi_input");
		ApplicationDesLoisPage.sleep(2);
		applicationDesLoisPage.assertTrue(getDossierIdPourCreationDuLoi());
		applicationDesLoisPage.assertTrue("Ministère");
		// Go to the Detail
		TableauMaitreDossierDetail dossierDetailMenu = applicationDesLoisPage
				.getTextMaitreDetailMenu(getDossierIdPourCreationDuLoi());
		MesureViewPage mesureView = dossierDetailMenu.getMesureViewPage();
		mesureView.assertNombreDesMesures(1);
		mesureView
				.assertElementValue(
						By_enum.ID,
						"texte_maitre_mesures:nxl_texte_maitre_mesures_listing:nxw_activite_normative_widget_mesure_numeroOrdre",
						"123");

		TextMaitrePage textMaitrePage = dossierDetailMenu.goToTextMaitrePage();
		DetailDecretPage detailDecretPage = textMaitrePage.detailDecretPage(0);
		detailDecretPage
				.verifyValue(
						"texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_numeroNor_lock:nxw_activite_normative_widget_numeroNor_lock_text",
						"CCOZ" + anneeCourante + "00014D");

	}

	@WebTest(description = "Suivi", order = 460)
	@TestDocumentation(categories = { "Suivi" })
	public void testCase0170() {
		TraitementPage traitementPage = loginEpg(UTILISATEUR_BDC);
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : bdc");
		SuiviPage suiviPage = traitementPage.getOngletMenu().goToSuivi();
		suiviPage.assertTrue("Mon infocentre");
		suiviPage.assertTrue("Infocentre général");
		suiviPage.clickToMonInfocentre();
		suiviPage.waitForPageSourcePart("Dossiers créés", TIMEOUT_SEC);
		suiviPage.assertTrue("Dossiers à traiter");
		suiviPage.assertTrue("Historique des validations");
		suiviPage = suiviPage.openCorbeille("Dossiers créés (7)", SuiviPage.class);
		SuiviPage.sleep(2);
		suiviPage.assertFalseExactMatch("EFIM" + anneeCourante + "00000D");
		suiviPage.assertTrue("ACOR" + anneeCourante + "00009Y");
		suiviPage.assertTrue("CCOZ" + anneeCourante + "00011Y");
		suiviPage.assertTrue("CCOZ" + anneeCourante + "00010Y");
		// suiviPage.assertTrue("EFIM" + anneeCourante + "00004V");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00005V");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00006V");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00007V");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00008V");
		suiviPage.assertTrue("7 dossiers");
		suiviPage = suiviPage.openCorbeille("Dossiers à traiter (4)", SuiviPage.class);
		SuiviPage.sleep(2);
		suiviPage.assertTrue("EFIM" + anneeCourante + "00005V");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00006V");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00007V");

		suiviPage = suiviPage.openCorbeille("Historique des validations (1)", SuiviPage.class);
		SuiviPage.sleep(2);
		suiviPage.assertTrue("ACOR" + anneeCourante + "00009Y");
		// suiviPage.assertTrue("EFIM1500003V");
		// suiviPage.assertTrue("EFIM" + anneeCourante + "00004V");

		suiviPage.clickToInfocentreGenerale();

		suiviPage.clickSuiviNode("form_espace_suivi:espaceSuiviTree:node:1:node:0::gouvernement:handle");
		suiviPage.assertTrue("CCO");

		suiviPage.clickSuiviNode("form_espace_suivi:espaceSuiviTree:node:1:node:0:node:0::elementNodeLabelMIN");

		suiviPage.assertTrue("12 dossiers");
		suiviPage.assertTrue("ACOR" + anneeCourante + "00009Y");
		suiviPage.assertTrue("CCOZ" + anneeCourante + "00011Y");
		suiviPage.assertTrue("CCOZ" + anneeCourante + "00010Y");
		// suiviPage.assertTrue("EFIM" + anneeCourante + "00004V");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00005V");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00006V");
		suiviPage.assertTrue("ACOR" + anneeCourante + "00009Y");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00007V");

		suiviPage.clickSuiviNode("form_espace_suivi:espaceSuiviTree:node:1:node:1::gouvernement:handle");
		suiviPage.assertTrue("CCO");
		suiviPage.clickSuiviNode("form_espace_suivi:espaceSuiviTree:node:1:node:1:node:0::etapeNodeCmdMIN");
		suiviPage.assertTrue("4 dossiers");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00005V");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00006V");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00007V");

		suiviPage.clickSuiviNode("form_espace_suivi:espaceSuiviTree:node:1:node:2::gouvernement:handle");

		suiviPage.assertTrue("CCO (4)");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00005V");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00006V");
		suiviPage.assertTrue("EFIM" + anneeCourante + "00007V");

		suiviPage.clickSuiviNode("form_espace_suivi:espaceSuiviTree:node:1:node:2:node:0::etapeNodeCmdMIN");

		suiviPage.assertTrue("1 dossier");
		suiviPage.assertTrue("ACOR" + anneeCourante + "00009Y");
		// suiviPage.assertTrue("EFIM" + anneeCourante + "00004V");

		logoutEpg();
	}

	@WebTest(description = "Corbeille pour indexation", order = 470)
	@TestDocumentation(categories = { "Indexation", "Corbeille" })
	public void testCase0180() {
		TraitementPage traitementPage = loginEpg("documentalistesgg", "documentalistesgg", TraitementPage.class);
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : documentalistesgg");
		traitementPage.pourIndexation();
		traitementPage.assertTrue("ACOR" + anneeCourante + "00009Y");
		traitementPage.assertTrue("EFIM" + anneeCourante + "00008V");
		traitementPage.assertTrue("EFIM" + anneeCourante + "00007V");
		traitementPage.assertTrue("EFIM" + anneeCourante + "00006V");
		traitementPage.assertTrue("EFIM" + anneeCourante + "00005V");
		// traitementPage.assertTrue("EFIM" + anneeCourante + "00004V");
		traitementPage.assertTrue("5 dossiers");
		logoutEpg();
	}

	@WebTest(description = "Corbeille pour indexation/mise à jour des documentalistes dans l'organigramme", order = 480)
	@TestDocumentation(categories = { "Recherche utilisateurs", "Utilisateurs", "Administration" })
	public void testCase0180_1() {
		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		// Clic sur Gestion des utilisateurs
		GestionUtilisateurs gestionUtilisateurs = administrationEpgPage.goToGestionUtilisateurs();
		gestionUtilisateurs.chercherUtilisateurs("documentalistemin");

		UserDetailPage userDetailPage = gestionUtilisateurs.getDetail("documentalistemin");
		ModifierUser modifier = userDetailPage.modifier();
		modifier.setPrenom("prenom");
		modifier.setTelePhone("065548754");
		modifier.setMail("bdc.bdc@solon-epg.com");
		modifier.selectMonsieur();
		modifier.setPostUtilisateur(Arrays.asList(
				"editUser:nxl_userPostes:nxw_postes_tree:node:0:node:22::min:handle:img:collapsed",
				"editUser:nxl_userPostes:nxw_postes_tree:node:0:node:22:node:14::dir:handle:img:collapsed",
				"editUser:nxl_userPostes:nxw_postes_tree:node:0:node:22:node:14:node:0::nxw_postes_addPoste"));
		modifier.assertTrue("Agents IGF (Economie)");
		modifier.enregistrer();

		modifier.waitElementVisibilityByLinkText(modifier.retourAlaListe.getText());
		gestionUtilisateurs = modifier.retourAlaListe();
		gestionUtilisateurs.chercherUtilisateurs("documentalistedir");
		userDetailPage = gestionUtilisateurs.getDetail("documentalistedir");
		modifier = userDetailPage.modifier();

		modifier.setPrenom("prenom");
		modifier.setTelePhone("065548754");
		modifier.setMail("bdc.bdc@solon-epg.com");
		modifier.selectMonsieur();
		modifier.setPostUtilisateur(Arrays.asList(
				"editUser:nxl_userPostes:nxw_postes_tree:node:0:node:22::min:handle:img:collapsed",
				"editUser:nxl_userPostes:nxw_postes_tree:node:0:node:22:node:5::dir:handle:img:collapsed",
				"editUser:nxl_userPostes:nxw_postes_tree:node:0:node:22:node:5:node:0::nxw_postes_addPoste"));
		modifier.assertTrue("Agents DAJ (Economie)");
		UserDetailPage.sleep(2);
		modifier.enregistrer();

		logoutEpg();
	}

	@WebTest(description = "Corbeille pour indexation/Connexion documentalistemin", order = 490)
	@TestDocumentation(categories = { "Indexation", "Corbeille" })
	public void testCase0180_2() {
		TraitementPage traitementPage = loginEpg("documentalistemin", "documentalistemin", TraitementPage.class);
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : documentalistemin");
		traitementPage.pourIndexation();
		traitementPage.assertTrue("EFIM" + anneeCourante + "00008V");
		traitementPage.assertTrue("EFIM" + anneeCourante + "00007V");
		traitementPage.assertTrue("EFIM" + anneeCourante + "00006V");
		traitementPage.assertTrue("EFIM" + anneeCourante + "00005V");
		// traitementPage.assertTrue("EFIM" + anneeCourante + "00004V");
		traitementPage.assertTrue("4 dossiers");
		logoutEpg();
	}

	@WebTest(description = "Corbeille pour indexation/Connexion documentalistedir", order = 500)
	@TestDocumentation(categories = { "Indexation", "Corbeille" })
	public void testCase0180_3() {
		TraitementPage traitementPage = loginEpg("documentalistedir", "documentalistedir", TraitementPage.class);
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : documentalistedir");
		traitementPage.pourIndexation();
		traitementPage.assertTrue("EFIM" + anneeCourante + "00008V");
		traitementPage.assertTrue("EFIM" + anneeCourante + "00007V");
		traitementPage.assertTrue("EFIM" + anneeCourante + "00006V");
		traitementPage.assertTrue("EFIM" + anneeCourante + "00005V");
		// traitementPage.assertTrue("EFIM" + anneeCourante + "00004V");
		traitementPage.assertTrue("4 dossiers");
		logoutEpg();
	}

	@WebTest(description = "Lancement deux dossiers publications conjointes avec un même ministère", order = 501)
	@TestDocumentation(categories = { "Dossier", "Publication conjointe" })
	public void testCase0181_1() {
		initPublicationsConjointesDeuxMinisteres(MINISTERE_ECONOMIE, MINISTERE_ECONOMIE, "testCase0181_1");
		logoutEpg();
	}

	@WebTest(description = "Lancement deux dossiers publications conjointes avec deux ministères différents", order = 502)
	@TestDocumentation(categories = { "Dossier", "Publication conjointe" })
	public void testCase0181_2() {
		initPublicationsConjointesDeuxMinisteres(MINISTERE_ECONOMIE, MINISTERE_COLLECTIVITE, "testCase0181_2");
		logoutEpg();
	}

	@WebTest(description = "Réattribution d'un dossier sans publication conjointe", order = 503)
	@TestDocumentation(categories = { "Dossier", "Réattribution" })
	public void testCase0182_1() {

		// Création du dossier qui fera l'objet d'une réattribution
		TraitementPage traitementPage = loginEpg(UTILISATEUR_BDC, UTILISATEUR_BDC, TraitementPage.class);
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : bdc");
		// Go to Creation Page
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();
		creationDossier100Page.setTypeActe("Exequatur");

		// Set Ministere Reponsable
		String[] mrItems = { "creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_tree:node:0:node:2::nxw_ministere_resp_field_addMin" };
		creationDossier100Page.setMinistereResponable(Arrays.asList(mrItems));

		// Set Direction concernee
		String[] dcItems = {
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0::min:handle:img:collapsed",
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0:node:2::nxw_direction_resp_field_addDir" };
		creationDossier100Page.setDirectionConcerne(Arrays.asList(dcItems));
		creationDossier100Page.assertTrue("Rapporteur Autorité de la concurrence");

		DossierDetailMenu dossierDetailMenu = creationDossier100Page.appuyerBoutonTerminer(DossierDetailMenu.class);
		ParapheurPage parapheurPage = dossierDetailMenu.getPage(ParapheurPage.class);
		parapheurPage.verrouiller();

		// Ajouter un document
		parapheurPage.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']", "word2003.doc");

		// Go to Bordereau tab
		BordereauPage bordereauPage = dossierDetailMenu.goToBordereau();
		bordereauPage.setTitreActe("Titre acte testCase0182_1");
		bordereauPage.setNomDuResponsable("DUPONT");

		bordereauPage.setQualiteDuResponsable("Mr");

		bordereauPage.setTelDuResponsable("0238587469");

		bordereauPage.setPrenomDuResponsable("Jean");

		bordereauPage.setModeParution("Electronique");

		bordereauPage.setDelaiPublication("Aucun");

		bordereauPage.setRubriques("Domaine public");

		Calendar dateFourniture = Calendar.getInstance();
		dateFourniture.add(Calendar.MONTH, 1);
		bordereauPage.setDatePourFournitureEpreuve(DateUtil.formatDDMMYYYYSlash(dateFourniture));

		// Save Data
		bordereauPage.enregistrer();
		bordereauPage.deverouiller_par_alt();

		bordereauPage.assertTrue("Titre acte testCase0182_1");
		bordereauPage.assertTrue("DUPONT");
		bordereauPage.assertTrue("Mr");
		bordereauPage.assertTrue("0238587469");
		bordereauPage.assertTrue("Jean");
		bordereauPage.assertTrue("Electronique");
		bordereauPage.assertTrue("Aucun");
		bordereauPage.assertTrue("Domaine public");

		String NORDossier = bordereauPage.getNOR();
		// Lancement du dossier
		// Open feuille de route tab
		FeuilleDeRoutePage feuilleDeRoute = dossierDetailMenu.goToFeuilleDeRoute();

		// Verrouiller FDR
		feuilleDeRoute.verrouiller();

		// Lancer Dossier
		feuilleDeRoute.lancerDossier();

		// le dossier n'est pas lancé car il n'y a qu'une étape à la FdR
		feuilleDeRoute.assertFalse("est lancé");

		// ajout tâche "pour avis"
		CreationEtapePage creationEtapePage = feuilleDeRoute.ajouterApres();
		creationEtapePage.setTypeEtape("Pour avis");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62:node:5::nxw_routing_task_distribution_mailbox_addPst"));
		// Create
		creationEtapePage.creer();
		CreationEtapePage.sleep(2);

		// Lancer Dossier
		feuilleDeRoute.lancerDossier();
		feuilleDeRoute.assertTrue("est lancé");
		// On se logout, pour se login ensuite avec adminsgg
		logoutEpg();
		traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");
		// Réattribution du dossier
		reattributionDossier(NORDossier, traitementPage);

		logoutEpg();

	}

	@WebTest(description = "Réattribution d'un dossier avec une publication conjointe avec un même ministère", order = 504)
	@TestDocumentation(categories = { "Dossier", "Réattribution", "Publication conjointe" })
	public void testCase0182_2() {
		// Initialisation des publications conjointes
		List<String> publications = initPublicationsConjointesDeuxMinisteres(MINISTERE_ECONOMIE, MINISTERE_ECONOMIE,"testCase0182_2");
		String NORDossierUn = publications.get(0);
		String NORDossierDeux = publications.get(1);

		logoutEpg();

		// Connexion à EPG
		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");

		// Réattribution du dossier 1
		String nouveauNORDossierUn = reattributionDossier(NORDossierUn, traitementPage);

		// On va sur le bordereau du dossier 1 pour vérifier que la publication conjointe est toujours renseignée
		RecherchePage recherchePage = traitementPage.rechercheNor(nouveauNORDossierUn);
		DossierDetailMenu dossierDetail = recherchePage.getDossierDetail(nouveauNORDossierUn);
		// Go to Bordereau tab
		BordereauPage bordereauPage = dossierDetail.goToBordereau();
		bordereauPage.assertTrue(NORDossierDeux);

		// On va sur le bordereau du dossier 1 pour vérifier que la publication conjointe est renseignée avec le nouveau
		// NOR du dossier 1
		recherchePage = traitementPage.rechercheNor(NORDossierDeux);
		dossierDetail = recherchePage.getDossierDetail(NORDossierDeux);
		// Go to Bordereau tab
		bordereauPage = dossierDetail.goToBordereau();
		bordereauPage.assertTrue(nouveauNORDossierUn);

		logoutEpg();
	}

	@WebTest(description = "Réattribution d'un dossier avec une publication conjointe avec deux ministères différents", order = 505)
	@TestDocumentation(categories = { "Dossier", "Réattribution", "Publication conjointe" })
	public void testCase0182_3() {
		// Initialisation des publications conjointes
		List<String> publications = initPublicationsConjointesDeuxMinisteres(MINISTERE_ECONOMIE, MINISTERE_COLLECTIVITE,"testCase0182_3");
		String NORDossierUn = publications.get(0);
		String NORDossierDeux = publications.get(1);

		logoutEpg();

		// Connexion à EPG
		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");

		// Réattribution du dossier 1
		String nouveauNORDossierUn = reattributionDossier(NORDossierUn, traitementPage);

		// On va sur le bordereau du dossier 1 pour vérifier que la publication conjointe est toujours renseignée
		RecherchePage recherchePage = traitementPage.rechercheNor(nouveauNORDossierUn);
		DossierDetailMenu dossierDetail = recherchePage.getDossierDetail(nouveauNORDossierUn);
		// Go to Bordereau tab
		BordereauPage bordereauPage = dossierDetail.goToBordereau();
		bordereauPage.assertTrue(NORDossierDeux);

		// On va sur le bordereau du dossier 1 pour vérifier que la publication conjointe est renseignée avec le nouveau
		// NOR du dossier 1
		recherchePage = traitementPage.rechercheNor(NORDossierDeux);
		dossierDetail = recherchePage.getDossierDetail(NORDossierDeux);
		// Go to Bordereau tab
		bordereauPage = dossierDetail.goToBordereau();
		bordereauPage.assertTrue(nouveauNORDossierUn);

		logoutEpg();
	}

	@WebTest(description = "Préparation des tests de la restriction d'accès", order = 530)
	@TestDocumentation(categories = { "Administration", "Mail", "Droits et profils", "Utilisateurs", "Dossier",
			"Parapheur", "Bordereau", "Recherche", "FDR" })
	public void testCase0210() throws MessagingException, IOException, InterruptedException {

		final String[] POSTE_AGENT_BDC_ECONOMIE = {
				"editUser:nxl_userPostes:nxw_postes_tree:node:0:node:22::min:handle",
				"editUser:nxl_userPostes:nxw_postes_tree:node:0:node:22:node:0::dir:handle",
				"editUser:nxl_userPostes:nxw_postes_tree:node:0:node:22:node:0:node:0::addBtnPST" };
		final String[] POSTE_AGENT_DAJ_ECONOMIE = {
				"editUser:nxl_userPostes:nxw_postes_tree:node:0:node:22::min:handle",
				"editUser:nxl_userPostes:nxw_postes_tree:node:0:node:22:node:5::dir:handle",
				"editUser:nxl_userPostes:nxw_postes_tree:node:0:node:22:node:5:node:0::addBtnPST" };

		// Nettoyage boite mèl

		SolonEpgImapHelper mailHelper = SolonEpgImapHelper.getInstance();
		ImapConsult imap = mailHelper.initNewImapConsult();
		imap.connect();
		mailHelper.clearInbox(imap);

		TraitementPage traitementPage = loginEpg(UTILISATEUR_BDC, UTILISATEUR_BDC, TraitementPage.class);

		// Paramétrage du profil utilisateur pour recevoir les mails

		ProfilUtilisateurPage profilUtilisateurPage = traitementPage.openProfilUtilisateurByLink();
		profilUtilisateurPage.addTypeActe("avi", "Avis");
		profilUtilisateurPage.valider();
		profilUtilisateurPage = traitementPage.openProfilUtilisateurByLink();
		profilUtilisateurPage
				.assertElementPresent(
						CommonWebPage.By_enum.ID,
						"displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_notification_layout_dossier:nxw_type_acte_notification_list:0:nxw_type_acte_notification_delete");
		profilUtilisateurPage.annuler();
		logoutEpg();
		// *IMPORTANT*: on doit ajouter le poste "Agents BDC Economie" à bdc, qui l'a perdu depuis la migration
		// Il faut être admin: Clic sur Gestion des utilisateurs
		loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		GestionUtilisateurs gestionUtilisateurs = administrationEpgPage.goToGestionUtilisateurs();
		gestionUtilisateurs.chercherUtilisateurs(UTILISATEUR_BDC);
		UserDetailPage detail = gestionUtilisateurs.getDetail(UTILISATEUR_BDC);
		ModifierUser modifier = detail.modifier();
		getFlog().startAction("Réassignation du poste DAJ Economie");
		modifier.setPostUtilisateur(Arrays.asList(POSTE_AGENT_DAJ_ECONOMIE));
		modifier.assertTrue("Agents DAJ (Economie)");
		getFlog().endAction();
		getFlog().startAction("Réassignation du poste BDC Economie");
		modifier.setPostUtilisateur(Arrays.asList(POSTE_AGENT_BDC_ECONOMIE));
		modifier.assertTrue("Agents BDC (Economie)");
		getFlog().endAction();
		// Champs obligatoires
		modifier.setPrenom(UTILISATEUR_BDC);
		modifier.setTelePhone("123456");
		modifier.selectMonsieur();
		// Save Data
		modifier.enregistrer();

		// Et on se remet en bdc

		logoutEpg();
		loginEpg(UTILISATEUR_BDC);

		// Création d'un nouveau dossier
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();
		creationDossier100Page.setTypeActe("Avis");

		// Set Ministere Reponsable : CCO (car l'économie a été fusionnée)
		String[] mrItems = { "creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_tree:node:0:node:22::addBtnMin" };
		creationDossier100Page.setMinistereResponable(Arrays.asList(mrItems));

		String[] directionDajEco = {
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0::min:handle:img:collapsed",
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0:node:5::addBtnDir" };
		// Set Direction concernee
		creationDossier100Page.setDirectionConcerne(Arrays.asList(directionDajEco));
		CreationDossier100Page.sleep(1);
		creationDossier100Page.assertTrue("DAJ (Economie)");

		creationPage = creationDossier100Page.appuyerBoutonTerminer(CreationPage.class);

		String nor = creationDossier100Page.waitAndGetNumeroDossierCree();

		RecherchePage recherchePage = creationPage.rechercheNor(nor);
		DossierDetailMenu dossierDetailMenu = recherchePage.getDossierDetail(nor);

		ParapheurPage parapheurPage = dossierDetailMenu.goToParapheur();
		parapheurPage.verrouiller();

		// Ajouter un document
		parapheurPage.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']", "word2003.doc");

		// Go to Bordereau tab
		BordereauPage bordereauPage = dossierDetailMenu.goToBordereau();
		bordereauPage.setTitreActe("Titre acte test accès");
		bordereauPage.setNomDuResponsable("DUPONT");

		bordereauPage.setQualiteDuResponsable("Mr");

		bordereauPage.setTelDuResponsable("0238587469");

		bordereauPage.setPrenomDuResponsable("Jean");

		bordereauPage.setModeParution("Electronique");

		bordereauPage.setDelaiPublication("Aucun");

		bordereauPage.setRubriques("Domaine public");

		// Save Data
		bordereauPage.enregistrer();
		bordereauPage.deverouiller_par_alt();

		bordereauPage.assertTrue("Titre acte test accès");
		bordereauPage.assertTrue("DUPONT");
		bordereauPage.assertTrue("Mr");
		bordereauPage.assertTrue("0238587469");
		bordereauPage.assertTrue("Jean");
		bordereauPage.assertTrue("Electronique");
		bordereauPage.assertTrue("Aucun");
		bordereauPage.assertTrue("Domaine public");

		// Lancement du dossier
		// Open feuille de route tab
		FeuilleDeRoutePage feuilleDeRoute = dossierDetailMenu.goToFeuilleDeRoute();

		// Verrouiller FDR
		feuilleDeRoute.verrouiller();

		// Ajout d'une étape
		// ajout tâche "pour avis"
		CreationEtapePage creationEtapePage = feuilleDeRoute.ajouterApres();
		creationEtapePage.setTypeEtape("Pour avis");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:22::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:22:node:0::dir:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:22:node:0:node:0::nxw_routing_task_distribution_mailbox_addPst"));
		// Create
		creationEtapePage.creer();
		CreationEtapePage.sleep(2);

		// On vérifie les envois de mail
		profilUtilisateurPage = traitementPage.openProfilUtilisateurByLink();
		profilUtilisateurPage
				.assertElementPresent(
						CommonWebPage.By_enum.ID,
						"displayedProfilUtilisateurPopup:profilUtilisateurForm:nxl_profil_utilisateur_notification_layout_dossier:nxw_type_acte_notification_list:0:nxw_type_acte_notification_delete");
		profilUtilisateurPage.annuler();

		// Lancer Dossier
		feuilleDeRoute.lancerDossier();

		feuilleDeRoute.assertTrue("est lancé");

		logoutEpg();
	}

	/**
	 * Mise en place de la restriction d'accès
	 * 
	 * @throws MessagingException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@WebTest(description = "Test de la restriction d'accès", order = 550)
	@TestDocumentation(categories = { "Administration", "Mail" })
	public void testCase0211() throws MessagingException, IOException, InterruptedException {
		maximizeBrowser();
		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");

		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		GestionRestrictionAccesPage gestionRestrictionAccesPage = administrationEpgPage.goToGestionAcces();
		gestionRestrictionAccesPage.activerRestrictionAcces();
		gestionRestrictionAccesPage.setDescriptionRestrictionAcces("Accès restreint");
		gestionRestrictionAccesPage.enregistrerChangements();

		GestionRestrictionAccesPage.sleep(1);

		logoutEpg();

		// Vérification de l'accès
		LoginPage loginPage = goToLoginEpgPage();
		loginPage.assertTrue(DESCRIPTION_ACCES_RESTREINT);
		traitementPage = loginPage.submitLogin(UTILISATEUR_ADMIN, UTILISATEUR_ADMIN, TraitementPage.class);
		userLogged = true; // cas de login sans passer par les méthodes prévues
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");
		logoutEpg();

		loginPage = goToLoginEpgPage();
		loginPage.assertTrue(DESCRIPTION_ACCES_RESTREINT);
		loginPage = loginPage.submitLogin(UTILISATEUR_BDC, UTILISATEUR_BDC, LoginPage.class);
		userLogged = true; // cas de login sans passer par les méthodes prévues
		loginPage.assertTrue(DESCRIPTION_ACCES_RESTREINT);
		loginPage.assertTrue(MESSAGE_ACCES_NON_AUTORISE);
		logoutEpg();

		getFlog().startAction("Désactivation de la restriction d'accès");
		traitementPage = loginAsAdminEpg();
		administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		gestionRestrictionAccesPage = administrationEpgPage.goToGestionAcces();
		gestionRestrictionAccesPage.desactiverRestrictionAcces();
		gestionRestrictionAccesPage.setDescriptionRestrictionAcces("");
		gestionRestrictionAccesPage.enregistrerChangements();
		getFlog().endAction();
		logoutEpg();

	}

	@WebTest(description = "Vérification de la désactivation de la restriction d'accès", order = 560)
	@TestDocumentation(categories = { "Droits et profils", "Administration" })
	public void testCase0212() {
		TraitementPage traitementPage = loginEpg(UTILISATEUR_BDC, UTILISATEUR_BDC, TraitementPage.class);
		// Paramétrage du profil utilisateur pour revenir à l'état initial
		ProfilUtilisateurPage profilUtilisateurPage = traitementPage.openProfilUtilisateurByLink();
		profilUtilisateurPage.removeTypeActe();
		profilUtilisateurPage.valider();

		getFlog().startAction("Vérification de la désactivation de la restriction d'accès");
		LoginPage loginPage = goToLoginEpgPage();
		loginPage.waitForPageSourcePart("Mot de passe", TIMEOUT_SEC); // On attend que la page de login soit bien
																		// chargée
		loginPage.assertFalse(DESCRIPTION_ACCES_RESTREINT);
		
		// On vérifie que le lien vers la page de renseignements est présent
		loginPage.checkLinkRenseignement();
		
		getFlog().endAction();
		logoutEpg();
	}

	private void createAndLaunchDossier(String titreActe) {
		TraitementPage traitementPage = loginEpg("bdc", "bdc", TraitementPage.class);
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : bdc");
		// Go to Creation Page
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();

		creationDossier100Page.setTypeActe("Avis");

		// Set Ministere Reponsable
		String[] mrItems = { "creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_tree:node:0:node:22::nxw_ministere_resp_field_addMin" };
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

		bordereauPage.setTitreActe(titreActe);

		bordereauPage.setNomDuResponsable("DUPONT");

		bordereauPage.setQualiteDuResponsable("Mr");

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

		feuilleDeRoute.assertTrue("est lancé");

		logoutEpg();
	}

	private void ajouterUneEtapeApres(FeuilleDeRoutePage feuilleDeRoute, String typeEtape) {
		CreationEtapePage creationEtape = feuilleDeRoute.ajouterApres();
		creationEtape.assertFalse("Pour initialisation");
		creationEtape.assertFalse("Pour signature");
		creationEtape.setTypeEtape(typeEtape);
		creationEtape
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:14::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:14:node:3::dir:handle",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:14:node:3:node:0::addBtnMB"));
		creationEtape.assertTrue("Serv. enseign. sup et recherche (AGR)");
		creationEtape.creer();

	}

	private String getEtapeFdrDossier() {
		return "EFIM" + anneeCourante + "00004V";
	}

	private String getDossierTest65() {
		return "EFIM" + anneeCourante + "00005V";
	}

	private String getEpgUrl() {
		return ((UrlEpgHelper) UrlEpgHelper.getInstance()).getEpgUrl();

	}

	private String getDossierIdPourCreationDuLoi() {
		return "CCOZ" + anneeCourante + "00012L";
	}

	@SuppressWarnings("static-access")
	private String reattributionDossier(String NOR, TraitementPage traitementPage) {
		// On va sur le dossier
		RecherchePage recherchePage = traitementPage.rechercheNor(NOR);

		// sélection du dossier qui nous intéresse (checkbox)
		recherchePage.getElementById("recherche_nor:nxl_dossier_listing_dto_1:nxw_listing_ajax_checkbox_dto").click();
		recherchePage.waitForPageSourcePart(
				"recherche_nor:clipboardActionsTable_recherche_nor_0:10:clipboardActionsButton", TIMEOUT_SEC);
		// Ajouter à la sélection
		recherchePage.getElementById("recherche_nor:clipboardActionsTable_recherche_nor_0:10:clipboardActionsButton")
				.click();
		// On vérifie que l'élément a bien été ajouté à la sélection
		recherchePage.assertTrue("Vider la sélection");
		// On va vers l'onglet administration
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		// Click sur transfert et réattribution de NOR
		By transferNOR = By.partialLinkText("Transfert et réattribution de NOR");
		administrationEpgPage.findElement(transferNOR).click();

		// Sélection du ministère cible
		administrationEpgPage.getElementById(
				"admin_transfert_form:nxl_transfert_dossier_layout:nxw_ministere_attTransfert_field_findButton")
				.click();
		administrationEpgPage.sleep(10);
		administrationEpgPage
				.getElementById(
						"admin_transfert_form:nxl_transfert_dossier_layout:nxw_ministere_attTransfert_field_tree:node:0:node:0::addBtnMin")
				.click();

		// Sélection de la direction cible
		administrationEpgPage.getElementById(
				"admin_transfert_form:nxl_transfert_dossier_layout:nxw_direction_attTransfert_field_findButton")
				.click();
		administrationEpgPage.sleep(2);
		administrationEpgPage
				.getElementById(
						"admin_transfert_form:nxl_transfert_dossier_layout:nxw_direction_attTransfert_field_tree:node:0:node:0::min:handle")
				.click();
		administrationEpgPage.sleep(2);
		administrationEpgPage
				.getElementById(
						"admin_transfert_form:nxl_transfert_dossier_layout:nxw_direction_attTransfert_field_tree:node:0:node:0:node:0::addBtnDir")
				.click();
		administrationEpgPage.sleep(2);
		// Cliquer sur le bouton 'réattribuer'
		administrationEpgPage.getElementById("admin_transfert_form:button_saveReattribution").click();
		
		// Cliquer sur le buton valider de la popup
		administrationEpgPage.getElementById("popup_ok").click();

		// Recherche du dossier pour vérifier que la réattribution a bien été effectuée
		administrationEpgPage.sleep(2);
		recherchePage = traitementPage.rechercheNor("ARTG" + NOR.substring(4));
		DossierDetailMenu dossierDetail = recherchePage.getDossierDetail("ARTG" + NOR.substring(4));
		// Go to Bordereau tab
		@SuppressWarnings("unused")
		BordereauPage bordereauPage = dossierDetail.goToBordereau();

		return "ARTG" + NOR.substring(4);
	}

	// Initialisation de deux publications conjointes en leur passant pour chacune un ministère
	private List<String> initPublicationsConjointesDeuxMinisteres(String ministereUn, String ministereDeux, String suffixeTitreActe) {
		List<String> publications = new ArrayList<String>();

		// Création du premier dossier des publications conjointes
		TraitementPage traitementPage = loginEpg(UTILISATEUR_BDC, UTILISATEUR_BDC, TraitementPage.class);
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : bdc");
		// Go to Creation Page
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();
		creationDossier100Page.setTypeActe("Exequatur");

		// Set Ministere Reponsable
		String[] mrItems = { ministereUn };
		creationDossier100Page.setMinistereResponable(Arrays.asList(mrItems));

		// Set Direction concernee
		String[] dcItems = {
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0::min:handle:img:collapsed",
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0:node:0::nxw_direction_resp_field_addDir" };
		creationDossier100Page.setDirectionConcerne(Arrays.asList(dcItems));

		DossierDetailMenu dossierDetailMenu = creationDossier100Page.appuyerBoutonTerminer(DossierDetailMenu.class);
		DossierDetailMenu.sleep(3);

		ParapheurPage parapheurPage = dossierDetailMenu.getPage(ParapheurPage.class);
		parapheurPage.verrouiller();

		// Ajouter un document
		parapheurPage.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']", "word2003.doc");

		// Go to Bordereau tab
		BordereauPage bordereauPageDossierUn = dossierDetailMenu.goToBordereau();
		bordereauPageDossierUn.setTitreActe("Titre acte " + suffixeTitreActe + "_conj1");
		bordereauPageDossierUn.setNomDuResponsable("DUPONT");

		bordereauPageDossierUn.setQualiteDuResponsable("Mr");

		bordereauPageDossierUn.setTelDuResponsable("0238587469");

		bordereauPageDossierUn.setPrenomDuResponsable("Jean");

		bordereauPageDossierUn.setModeParution("Electronique");

		bordereauPageDossierUn.setDelaiPublication("Aucun");

		bordereauPageDossierUn.setRubriques("Domaine public");

		Calendar dateFourniture = Calendar.getInstance();
		dateFourniture.add(Calendar.MONTH, 1);
		bordereauPageDossierUn.setDatePourFournitureEpreuve(DateUtil.formatDDMMYYYYSlash(dateFourniture));

		String numDossierUN = bordereauPageDossierUn.getNOR();
		publications.add(numDossierUN);

		// Save Data
		bordereauPageDossierUn.enregistrer();
		bordereauPageDossierUn.deverouiller_par_alt();

		bordereauPageDossierUn.assertTrue("Titre acte " + suffixeTitreActe + "_conj1");
		bordereauPageDossierUn.assertTrue("DUPONT");
		bordereauPageDossierUn.assertTrue("Mr");
		bordereauPageDossierUn.assertTrue("0238587469");
		bordereauPageDossierUn.assertTrue("Jean");
		bordereauPageDossierUn.assertTrue("Electronique");
		bordereauPageDossierUn.assertTrue("Aucun");
		bordereauPageDossierUn.assertTrue("Domaine public");

		// Lancement du dossier
		// Open feuille de route tab
		FeuilleDeRoutePage feuilleDeRouteDossierUn = dossierDetailMenu.goToFeuilleDeRoute();

		// Verrouiller FDR
		feuilleDeRouteDossierUn.verrouiller();

		// Lancer Dossier
		feuilleDeRouteDossierUn.lancerDossier();

		// le dossier n'est pas lancé car il n'y a qu'une étape à la FdR
		feuilleDeRouteDossierUn.assertFalse("est lancé");

		// ajout tâche "pour avis"
		CreationEtapePage creationEtapePage = feuilleDeRouteDossierUn.ajouterApres();
		creationEtapePage.setTypeEtape("Pour avis");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62:node:5::nxw_routing_task_distribution_mailbox_addPst"));
		// Create
		CreationEtapePage.sleep(2);
		creationEtapePage.creer();
		CreationEtapePage.sleep(2);

		// Lancer Dossier
		feuilleDeRouteDossierUn.lancerDossier();
		feuilleDeRouteDossierUn.assertTrue("est lancé");

		// Création du dossier deux pour les publications conjointes

		// Go to Creation Page
		creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		creationDossier100Page = creationPage.createDossier();
		creationDossier100Page.setTypeActe("Exequatur");

		// Set Ministere Reponsable
		String[] mrItemsDeux = { ministereDeux };
		creationDossier100Page.setMinistereResponable(Arrays.asList(mrItemsDeux));

		// Set Direction concernee

		creationDossier100Page.setDirectionConcerne(Arrays.asList(dcItems));

		dossierDetailMenu = creationDossier100Page.appuyerBoutonTerminer(DossierDetailMenu.class);
		DossierDetailMenu.sleep(3);

		parapheurPage = dossierDetailMenu.getPage(ParapheurPage.class);
		parapheurPage.verrouiller();

		// Ajouter un document
		parapheurPage.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']", "word2003.doc");

		// Go to Bordereau tab
		BordereauPage bordereauPageDossierDeux = dossierDetailMenu.goToBordereau();
		bordereauPageDossierDeux.setTitreActe("Titre acte " + suffixeTitreActe + "_conj2");
		bordereauPageDossierDeux.setNomDuResponsable("DUPONT");

		bordereauPageDossierDeux.setQualiteDuResponsable("Mr");

		bordereauPageDossierDeux.setTelDuResponsable("0238587469");

		bordereauPageDossierDeux.setPrenomDuResponsable("Jean");

		bordereauPageDossierDeux.setModeParution("Electronique");

		bordereauPageDossierDeux.setDelaiPublication("Aucun");

		bordereauPageDossierDeux.setRubriques("Domaine public");

		dateFourniture = Calendar.getInstance();
		dateFourniture.add(Calendar.MONTH, 1);
		bordereauPageDossierDeux.setDatePourFournitureEpreuve(DateUtil.formatDDMMYYYYSlash(dateFourniture));

		String numDossierDeux = bordereauPageDossierDeux.getNOR();
		publications.add(numDossierDeux);

		// On set la publication conjointe avec le numéro du dossier un
		bordereauPageDossierDeux.setPublicationConjointe(numDossierUN);

		// Save Data
		bordereauPageDossierDeux.enregistrer();
		bordereauPageDossierDeux.deverouiller_par_alt();

		bordereauPageDossierDeux.assertTrue("Titre acte " + suffixeTitreActe + "_conj2");
		bordereauPageDossierDeux.assertTrue("DUPONT");
		bordereauPageDossierDeux.assertTrue("Mr");
		bordereauPageDossierDeux.assertTrue("0238587469");
		bordereauPageDossierDeux.assertTrue("Jean");
		bordereauPageDossierDeux.assertTrue("Electronique");
		bordereauPageDossierDeux.assertTrue("Aucun");
		bordereauPageDossierDeux.assertTrue("Domaine public");

		// Vérifier la publication conjointe du dossier deux
		bordereauPageDossierDeux.assertTrue(numDossierUN);

		// Lancement du dossier
		// Open feuille de route tab
		FeuilleDeRoutePage feuilleDeRouteDossierDeux = dossierDetailMenu.goToFeuilleDeRoute();

		// Verrouiller FDR
		feuilleDeRouteDossierDeux.verrouiller();

		// Lancer Dossier
		feuilleDeRouteDossierDeux.lancerDossier();

		// le dossier n'est pas lancé car il n'y a qu'une étape à la FdR
		feuilleDeRouteDossierDeux.assertFalse("est lancé");

		// ajout tâche "pour avis"
		creationEtapePage = feuilleDeRouteDossierDeux.ajouterApres();
		creationEtapePage.setTypeEtape("Pour avis");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62:node:5::nxw_routing_task_distribution_mailbox_addPst"));
		// Create
		CreationEtapePage.sleep(2);
		creationEtapePage.creer();
		CreationEtapePage.sleep(2);

		// Lancer Dossier
		feuilleDeRouteDossierDeux.lancerDossier();
		feuilleDeRouteDossierDeux.assertTrue("est lancé");

		// Vérifier la publication conjointe du dossier un
		RecherchePage recherchePage = traitementPage.rechercheNor(numDossierUN);
		DossierDetailMenu dossierDetail = recherchePage.getDossierDetail(numDossierUN);
		// Go to Bordereau tab
		bordereauPageDossierUn = dossierDetail.goToBordereau();

		bordereauPageDossierUn.assertTrue(numDossierDeux);

		return publications;
	}

	@WebTest(description = "Traitement - changement de corbeille", order = 570)
	@TestDocumentation(categories = { "Corbeille", "Dossier" })
	public void testCase0213() {
		TraitementPage traitementPage = loginAsAdminEpg();

		// Création d'un premier dossier pour la première corbeille
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();
		creationDossier100Page.setTypeActe("Amnistie");
		creationDossier100Page.setMinistereResponable("CCO - CCO");

		ArrayList<String> ar = new ArrayList<String>();
		ar.add("creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:3::min:handle:img:collapsed");
		ar.add("creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:3:node:0::nxw_direction_resp_field_addDir");

		creationDossier100Page.setDirectionConcerne(ar);

		DossierDetailMenu dossierDetailMenu = creationDossier100Page.appuyerBoutonTerminer(DossierDetailMenu.class);
		String numPremierDossier = creationDossier100Page.waitAndGetNumeroDossierCree();
		// On vérifie qu'on a bien le dossier présent dans notre corbeille de création
		creationDossier100Page.assertTrue(numPremierDossier);
		// Renseignement des valeurs obligatoires pour lancer le dossier
		ParapheurPage parapheurPage = dossierDetailMenu.getPage(ParapheurPage.class);
		parapheurPage.verrouiller();

		// Ajouter un document
		parapheurPage.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']", "word2003.doc");

		// Go to Bordereau tab
		BordereauPage bordereauPageDossierUn = dossierDetailMenu.goToBordereau();
		bordereauPageDossierUn.setTitreActe("Titre acte testCase0213");
		bordereauPageDossierUn.setNomDuResponsable("DUPONT");

		bordereauPageDossierUn.setQualiteDuResponsable("Mr");

		bordereauPageDossierUn.setTelDuResponsable("0238587469");

		bordereauPageDossierUn.setPrenomDuResponsable("Jean");

		bordereauPageDossierUn.setRubriques("Domaine public");

		Calendar dateFourniture = Calendar.getInstance();
		dateFourniture.add(Calendar.MONTH, 1);
		bordereauPageDossierUn.setDatePourFournitureEpreuve(DateUtil.formatDDMMYYYYSlash(dateFourniture));

		// Save Data
		bordereauPageDossierUn.enregistrer();
		bordereauPageDossierUn.deverouiller_par_alt();

		bordereauPageDossierUn.assertTrue("Titre acte testCase0213");
		bordereauPageDossierUn.assertTrue("DUPONT");
		bordereauPageDossierUn.assertTrue("Mr");
		bordereauPageDossierUn.assertTrue("0238587469");
		bordereauPageDossierUn.assertTrue("Jean");
		bordereauPageDossierUn.assertTrue("Domaine public");

		// Lancement du dossier
		// Open feuille de route tab
		FeuilleDeRoutePage feuilleDeRouteDossierUn = dossierDetailMenu.goToFeuilleDeRoute();

		// Verrouiller FDR
		feuilleDeRouteDossierUn.verrouiller();

		// Lancer Dossier
		feuilleDeRouteDossierUn.lancerDossier();

		// le dossier n'est pas lancé car il n'y a qu'une étape à la FdR
		feuilleDeRouteDossierUn.assertFalse("est lancé");

		// ajout tâche "pour avis"
		CreationEtapePage creationEtapePage = feuilleDeRouteDossierUn.ajouterApres();
		creationEtapePage.setTypeEtape("Pour avis");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62:node:5::nxw_routing_task_distribution_mailbox_addPst"));
		// Create
		CreationEtapePage.sleep(2);
		creationEtapePage.creer();
		CreationEtapePage.sleep(2);

		// Lancer Dossier
		feuilleDeRouteDossierUn.lancerDossier();
		feuilleDeRouteDossierUn.assertTrue("est lancé");

		// Création d'un deuxième dossier pour l'autre corbeille
		creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		creationDossier100Page = creationPage.createDossier();
		creationDossier100Page.setTypeActe("Amnistie");
		creationDossier100Page.setMinistereResponable("CCO - CCO");

		ar = new ArrayList<String>();
		ar.add("creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:3::min:handle:img:collapsed");
		ar.add("creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:3:node:0::nxw_direction_resp_field_addDir");

		creationDossier100Page.setDirectionConcerne(ar);

		dossierDetailMenu = creationDossier100Page.appuyerBoutonTerminer(DossierDetailMenu.class);
		WebPage.sleep(5);

		String numDeuxiemeDossier = creationDossier100Page.waitAndGetNumeroDossierCree();
		WebPage.sleep(5);
		// On vérifie qu'on a bien le dossier présent dans notre corbeille de création
		creationDossier100Page.assertTrue(numDeuxiemeDossier);
		// Renseignement des valeurs obligatoires pour lancer le dossier

		parapheurPage = dossierDetailMenu.getPage(ParapheurPage.class);
		parapheurPage.verrouiller();

		// Ajouter un document
		parapheurPage.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']", "word2003.doc");

		// Go to Bordereau tab
		BordereauPage bordereauPageDossierDeux = dossierDetailMenu.goToBordereau();
		bordereauPageDossierDeux.setTitreActe("Titre acte testCase0213_2");
		bordereauPageDossierDeux.setNomDuResponsable("DUPONT");

		bordereauPageDossierDeux.setQualiteDuResponsable("Mr");

		bordereauPageDossierDeux.setTelDuResponsable("0238587469");

		bordereauPageDossierDeux.setPrenomDuResponsable("Jean");

		bordereauPageDossierDeux.setRubriques("Domaine public");

		dateFourniture = Calendar.getInstance();
		dateFourniture.add(Calendar.MONTH, 1);
		bordereauPageDossierDeux.setDatePourFournitureEpreuve(DateUtil.formatDDMMYYYYSlash(dateFourniture));

		// Save Data
		bordereauPageDossierDeux.enregistrer();
		bordereauPageDossierDeux.deverouiller_par_alt();

		bordereauPageDossierDeux.assertTrue("Titre acte testCase0213_2");
		bordereauPageDossierDeux.assertTrue("DUPONT");
		bordereauPageDossierDeux.assertTrue("Mr");
		bordereauPageDossierDeux.assertTrue("0238587469");
		bordereauPageDossierDeux.assertTrue("Jean");
		bordereauPageDossierDeux.assertTrue("Domaine public");

		// Lancement du dossier
		// Open feuille de route tab
		FeuilleDeRoutePage feuilleDeRouteDossierDeux = dossierDetailMenu.goToFeuilleDeRoute();

		// Verrouiller FDR
		feuilleDeRouteDossierDeux.verrouiller();

		// Lancer Dossier
		feuilleDeRouteDossierDeux.lancerDossier();

		// le dossier n'est pas lancé car il n'y a qu'une étape à la FdR
		feuilleDeRouteDossierDeux.assertFalse("est lancé");

		// ajout tâche "pour avis"
		creationEtapePage = feuilleDeRouteDossierDeux.ajouterApres();
		creationEtapePage.setTypeEtape("Pour avis");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3:node:0::dir:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3:node:0:node:1::nxw_routing_task_distribution_mailbox_addPst"));
		// Create
		CreationEtapePage.sleep(2);
		creationEtapePage.creer();
		CreationEtapePage.sleep(2);

		// Lancer Dossier
		feuilleDeRouteDossierDeux.lancerDossier();
		feuilleDeRouteDossierDeux.assertTrue("est lancé");

		// Retour à l'onglet traitement
		traitementPage.getOngletMenu().goToEspaceTraitement();
		traitementPage.openCorbeille("Administrateur S.O.L.O.N. (SGG)");
		// Vérification que le premier dossier est présent dans la première corbeille
		traitementPage.assertTrue(numPremierDossier);
		// Vérification que le deuxième dossier est présent dans la deuxième corbeille
		traitementPage.openCorbeille("Administrateurs SOLON");
		// traitementPage.getElementByLinkText("Administrateurs SOLON (1)").click();
		traitementPage.assertTrue(numDeuxiemeDossier);

	}

	@WebTest(description = "Changement Gouvernement avec publications conjointes", order = 580)
	@TestDocumentation(categories = { "Migration", "Administration", "Bordereau", "Recherche" })
	public void testCase0220() {

		// Initialisation des publications conjointes
		// Dossiers qui ont le même ministère
		List<String> publicationsMemeMinistere = initPublicationsConjointesDeuxMinisteres(MINISTERE_ECONOMIE,
				MINISTERE_ECONOMIE, "testCase02201a");
		String NORDossierUnMemeMinistere = publicationsMemeMinistere.get(0);
		String NORDossierDeuxMemeMinistere = publicationsMemeMinistere.get(1);

		logoutEpg();

		// Dossiers qui ont un ministère différent
		List<String> publicationsMinistereDifferent = initPublicationsConjointesDeuxMinisteres(MINISTERE_ECONOMIE,
				MINISTERE_CCO, "testCase0220b");
		String NORDossierUnMinistereDifferent = publicationsMinistereDifferent.get(0);
		String NORDossierDeuxMinistereDifferent = publicationsMinistereDifferent.get(1);

		logoutEpg();

		TraitementPage traitementPage = loginAsAdminEpg();

		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		GestionDesMigrationsPage gestionDesMigrationsPage = administrationEpgPage.goToGestionDesMigrations();

		ArrayList<String> ar = new ArrayList<String>();
		String prefixOld = "a4jChangementGouvernement:nxl_changement_gouvernement_poste_old_element:nxw_old_poste_field_tree:node:0:node:3:";
		ar.add(prefixOld + ":min:handle:img:collapsed");
		ar.add(prefixOld + "node:0::dir:handle:img:collapsed");
		ar.add(prefixOld + "node:0:node:0::nxw_old_poste_field_addPst");
		gestionDesMigrationsPage.setOldPoste(ar);

		ar = new ArrayList<String>();
		String prefixNew = "a4jChangementGouvernement:nxl_changement_gouvernement_poste_new_element:nxw_new_poste_field_tree:node:0:node:2:";

		ar.add(prefixNew + ":min:handle:img:collapsed");
		ar.add(prefixNew + "node:2::dir:handle:img:collapsed");
		ar.add(prefixNew + "node:2:node:0::poste:handle:img:collapsed");
		ar.add(prefixNew + "node:2:node:0::nxw_new_poste_field_addPst");
		gestionDesMigrationsPage.setNewPoste(ar);

		gestionDesMigrationsPage.validerChangementGouvernement();
		
		logoutEpg();
		traitementPage = loginEpg(UTILISATEUR_BDC);
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : bdc");
		traitementPage.assertTrue("Agents Autorité de la concurrence (5)");
		traitementPage.findElement(By.partialLinkText("Agents Autorité de la concurrence (5)")).click();
		TraitementPage.sleep(2);
		traitementPage.assertTrue("EFIM" + anneeCourante + "00005V");
		traitementPage.assertTrue("EFIM" + anneeCourante + "00006V");
		traitementPage.assertTrue("EFIM" + anneeCourante + "00007V");
		traitementPage.assertTrue("EFIM" + anneeCourante + "00008V");

		DossierDetailMenu dossierDetail = traitementPage.getDossierDetail("EFIM" + anneeCourante + "00007V");

		dossierDetail.goToBordereau().verrouiller();
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		creationPage.assertTrue("CCOZ" + anneeCourante + "00011Y");
		creationPage.assertTrue("CCOZ" + anneeCourante + "00010Y");
		logoutEpg();

		traitementPage = loginAsAdminEpg();
		administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		gestionDesMigrationsPage = administrationEpgPage.goToGestionDesMigrations();
		gestionDesMigrationsPage.checkMinistere();
		gestionDesMigrationsPage.setOldMinistere(MINISTERE_EFI);
		gestionDesMigrationsPage.setNewMinistere("CCO - CCO");
		
		gestionDesMigrationsPage.validerChangementGouvernement();
		
		administrationEpgPage = gestionDesMigrationsPage.getPage(AdministrationEpgPage.class);
		GestionModelesPage gestionModelesPage = administrationEpgPage.goToGestionModeles();
		gestionModelesPage.assertTrue("CCO");

		RecherchePage recherchePage = gestionModelesPage.rechercheNor("EFI*");
		recherchePage.assertTrue("dossiers");

		dossierDetail = traitementPage.getDossierDetail("EFIM" + anneeCourante + "00000D");
		BordereauPage bordereauPage = dossierDetail.goToBordereau();
		bordereauPage.waitForPageSourcePart("CCO", TIMEOUT_SEC);
		recherchePage = gestionModelesPage.rechercheNor("CCO*");
		recherchePage.assertTrue("CCOZ" + anneeCourante + "00010Y");
		recherchePage.assertTrue("CCOZ" + anneeCourante + "00011Y");
		recherchePage.assertTrue("CCOZ" + anneeCourante + "00012L");

		// Vérification des publications conjointes
		// publications conjointes ayant un même ministère
		// Recherche du nouveau numéro NOR du dossier un
		recherchePage = traitementPage.rechercheNor("CCOZ" + NORDossierUnMemeMinistere.substring(4));
		dossierDetail = recherchePage.getDossierDetail("CCOZ" + NORDossierUnMemeMinistere.substring(4));
		// Go to Bordereau tab
		bordereauPage = dossierDetail.goToBordereau();
		bordereauPage.assertTrue("CCOZ" + NORDossierUnMemeMinistere.substring(4));

		// Vérification du bordereau du dossier deux
		recherchePage = traitementPage.rechercheNor("CCOZ" + NORDossierDeuxMemeMinistere.substring(4));
		dossierDetail = recherchePage.getDossierDetail("CCOZ" + NORDossierDeuxMemeMinistere.substring(4));
		bordereauPage = dossierDetail.goToBordereau();
		bordereauPage.assertTrue("CCOZ" + NORDossierUnMemeMinistere.substring(4));

		// publications conjointes ayant un ministères différents

		recherchePage = traitementPage.rechercheNor("CCOZ" + NORDossierUnMinistereDifferent.substring(4));
		dossierDetail = recherchePage.getDossierDetail("CCOZ" + NORDossierUnMinistereDifferent.substring(4));
		// Go to Bordereau tab
		bordereauPage = dossierDetail.goToBordereau();
		bordereauPage.assertTrue(NORDossierDeuxMinistereDifferent);

		// Vérification du bordereau du dossier deux
		recherchePage = traitementPage.rechercheNor(NORDossierDeuxMinistereDifferent);
		dossierDetail = recherchePage.getDossierDetail(NORDossierDeuxMinistereDifferent);
		bordereauPage = dossierDetail.goToBordereau();
		bordereauPage.assertTrue("CCOZ" + NORDossierUnMinistereDifferent.substring(4));

		logoutEpg();
	}

	@WebTest(description = "Validation d'étape et changement des verrous", order = 580)
	@TestDocumentation(categories = { "Dossier", "Validation Etape", "Verrou" })
	public void testCase0230() {
		// Création du dossier
		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : adminsgg");
		// Go to Creation Page
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();
		creationDossier100Page.setTypeActe("Amnistie");
		// Set Ministere Reponsable
		String[] minResp = { "creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_tree:node:0:node:30::addBtnMin" };
		creationDossier100Page.setMinistereResponable(Arrays.asList(minResp));
		// Set Direction concernee
		String[] dirConcerne = {
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:30::min:handle:img:collapsed",
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:30:node:0::nxw_direction_resp_field_addDir" };
		creationDossier100Page.setDirectionConcerne(Arrays.asList(dirConcerne));
		creationDossier100Page.assertTrue("BDC sports");
		DossierDetailMenu dossierDetailMenu = creationDossier100Page.appuyerBoutonTerminer(DossierDetailMenu.class);
		ParapheurPage parapheurPage = dossierDetailMenu.getPage(ParapheurPage.class);
		parapheurPage.verrouiller();
		parapheurPage.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']", "word2003.doc");
		// Go to Bordereau tab
		BordereauPage bordereauPage = dossierDetailMenu.goToBordereau();
		bordereauPage.setTitreActe("Test Verrou EPG");
		bordereauPage.setNomDuResponsable("SHAX");
		bordereauPage.setQualiteDuResponsable("Mr");
		bordereauPage.setTelDuResponsable("0238587469");
		bordereauPage.setPrenomDuResponsable("TAYLOR");
		bordereauPage.setModeParution("Electronique");
		bordereauPage.setDelaiPublication("Aucun");
		bordereauPage.setRubriques("Domaine public");
		Calendar dateFourniture = Calendar.getInstance();
		dateFourniture.add(Calendar.MONTH, 1);
		bordereauPage.setDatePourFournitureEpreuve(DateUtil.formatDDMMYYYYSlash(dateFourniture));

		// Save Data
		bordereauPage.enregistrer();
		bordereauPage.deverouiller_par_alt();
		// Lancement du dossier
		// Open feuille de route tab
		FeuilleDeRoutePage feuilleDeRoute = dossierDetailMenu.goToFeuilleDeRoute();

		// Verrouiller FDR
		feuilleDeRoute.verrouiller();

		// Lancer Dossier
		feuilleDeRoute.lancerDossier();

		// le dossier n'est pas lancé car il n'y a qu'une étape à la FdR
		feuilleDeRoute.assertFalse("est lancé");
		// ajout tâche "pour avis"
		CreationEtapePage creationEtapePage = feuilleDeRoute.ajouterApres();
		creationEtapePage.setTypeEtape("Pour avis");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:28::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:28:node:0::dir:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:28:node:0:node:0::nxw_routing_task_distribution_mailbox_addPst"));

		// Create
		creationEtapePage.creer();
		CreationEtapePage.sleep(2);
		// On ne lance pas le dossier car pour l'instant on a besoin de rester à la première étape
		feuilleDeRoute.deverouiller_par_alt();
		logoutEpg();

		String NORDossierCree = "SPOK" + anneeCourante + "00031Y";
		// connexion avec Admin SPO pour vérifier qu'il a les droits
		traitementPage = loginMinSPO_Epg();
		RecherchePage recherchePage = traitementPage.rechercheNor(NORDossierCree);
		dossierDetailMenu = recherchePage.getDossierDetail(NORDossierCree);
		parapheurPage = dossierDetailMenu.goToParapheur();
		// Vérification de la présence du verrou puis test pour verrouiller/déverouiller
		parapheurPage.assertVerrouille();
		parapheurPage.verrouiller();
		parapheurPage.deverouiller_par_alt();
		logoutEpg();

		// Connexion avec ADMIN OME pour vérifier que ce dernier n'a pas le verrou. Il ne peut même pas accéder au
		// dossier
		traitementPage = loginMinOME_Epg();
		recherchePage = traitementPage.rechercheNor("SPOK*");
		recherchePage.assertFalse(NORDossierCree);
		logoutEpg();

		// Connexion avec Contributeur ministériel SPO qui doit voir le verrou
		traitementPage = loginContribMinSPO();
		recherchePage = traitementPage.rechercheNor(NORDossierCree);
		dossierDetailMenu = recherchePage.getDossierDetail(NORDossierCree);
		parapheurPage = dossierDetailMenu.goToParapheur();
		// Vérification de la présence du verrou puis test pour verrouiller/déverouiller
		parapheurPage.assertVerrouille();
		parapheurPage.verrouiller();
		parapheurPage.deverouiller_par_alt();
		logoutEpg();

		// Connexion avec contributeur ministériel OME qui ne doit pas voir le verrou
		traitementPage = loginContribMinOME();
		recherchePage = traitementPage.rechercheNor(NORDossierCree);
		recherchePage = traitementPage.rechercheNor("SPOK*");
		recherchePage.assertFalse(NORDossierCree);
		logoutEpg();

		// Connexion avec contributeurMinSPO pour valider la première étape du dossier
		traitementPage = loginContribMinSPO();
		recherchePage = traitementPage.rechercheNor(NORDossierCree);
		dossierDetailMenu = recherchePage.getDossierDetail(NORDossierCree);
		feuilleDeRoute = dossierDetailMenu.goToFeuilleDeRoute();
		feuilleDeRoute.verrouiller();
		feuilleDeRoute.lancerDossier();
		feuilleDeRoute.assertTrue("est lancé");
		logoutEpg();

		// Vérification des droits après la validation d'étape
		// connexion avec Admin SPO pour vérifier qu'il n'a plus les droits
		traitementPage = loginMinSPO_Epg();
		recherchePage = traitementPage.rechercheNor(NORDossierCree);
		dossierDetailMenu = recherchePage.getDossierDetail(NORDossierCree);
		parapheurPage = dossierDetailMenu.goToParapheur();
		// Vérification de l'absence du verrou
		parapheurPage.assertVerrouilleAbsent();
		logoutEpg();

		// Connexion avec ADMIN OME pour vérifier que ce dernier a bien le verrou
		traitementPage = loginMinOME_Epg();
		recherchePage = traitementPage.rechercheNor(NORDossierCree);
		dossierDetailMenu = recherchePage.getDossierDetail(NORDossierCree);
		parapheurPage = dossierDetailMenu.goToParapheur();
		// Vérification d'abscence du verrou
		parapheurPage.assertVerrouille();
		parapheurPage.verrouiller();
		parapheurPage.deverouiller_par_alt();
		logoutEpg();

		// Connexion avec Contributeur ministériel SPO qui ne doit pas avoir le verrou
		traitementPage = loginContribMinSPO();
		recherchePage = traitementPage.rechercheNor(NORDossierCree);
		dossierDetailMenu = recherchePage.getDossierDetail(NORDossierCree);
		parapheurPage = dossierDetailMenu.goToParapheur();
		// Vérification de l'absence du verrou
		parapheurPage.assertVerrouilleAbsent();
		logoutEpg();

		// Connexion avec contributeur ministériel OME qui doit voir le verrou
		traitementPage = loginContribMinOME();
		recherchePage = traitementPage.rechercheNor(NORDossierCree);
		dossierDetailMenu = recherchePage.getDossierDetail(NORDossierCree);
		parapheurPage = dossierDetailMenu.goToParapheur();
		// Vérification d'abscence du verrou
		parapheurPage.assertVerrouille();
		parapheurPage.verrouiller();
		parapheurPage.deverouiller_par_alt();
		logoutEpg();
	}
}
