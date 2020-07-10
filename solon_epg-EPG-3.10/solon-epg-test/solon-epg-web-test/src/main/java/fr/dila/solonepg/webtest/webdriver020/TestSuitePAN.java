package fr.dila.solonepg.webtest.webdriver020;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import fr.dila.solonepg.page.administration.CreationEtapePage;
import fr.dila.solonepg.page.administration.CreationModelePage;
import fr.dila.solonepg.page.administration.EditerOrganigrammePage;
import fr.dila.solonepg.page.administration.GestionModelesPage;
import fr.dila.solonepg.page.administration.GestionOrganigrammePage;
import fr.dila.solonepg.page.administration.ViewModelePage;
import fr.dila.solonepg.page.an.loi.ApplicationDesLoisPage;
import fr.dila.solonepg.page.an.loi.DetailDecretPage;
import fr.dila.solonepg.page.an.loi.ModificationMesurePage;
import fr.dila.solonepg.page.creation.AbstractCreationDossierPage;
import fr.dila.solonepg.page.creation.CreationDossier100Page;
import fr.dila.solonepg.page.creation.CreationDossier101Page;
import fr.dila.solonepg.page.creation.CreationDossier104Page;
import fr.dila.solonepg.page.creation.CreationDossier108Page;
import fr.dila.solonepg.page.creation.CreationDossierSelectPostePage;
import fr.dila.solonepg.page.main.onglet.AdministrationEpgPage;
import fr.dila.solonepg.page.main.onglet.CreationPage;
import fr.dila.solonepg.page.main.onglet.PilotageANPage;
import fr.dila.solonepg.page.main.onglet.TraitementPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.BordereauPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.DossierDetailMenu;
import fr.dila.solonepg.page.main.onglet.dossier.detail.FeuilleDeRoutePage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.ParapheurPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.TextMaitrePage;
import fr.dila.solonepg.page.main.pan.view.ParamStatField;
import fr.dila.solonepg.page.main.pan.view.TableauMaitreDossierDetail;
import fr.dila.solonepg.page.main.pan.view.TableauMaitreFilterPage;
import fr.dila.solonepg.page.main.pan.view.TableauMaitreViewPage;
import fr.dila.solonepg.page.main.pan.view.mesure.MesureViewPage;
import fr.dila.solonepg.webtest.AbstractSolonSuite;
import fr.dila.solonepg.webtest.helper.url.UrlEpgHelper;
import fr.dila.solonepg.webtest.webdriver020.report.validator.ADL_A0;
import fr.dila.solonepg.webtest.webdriver020.report.validator.PanReportValidator;
import fr.dila.st.annotations.TestDocumentation;
import fr.dila.st.webdriver.model.CommonWebPage.By_enum;
import fr.dila.st.webdriver.ws.WsResultPage;
import fr.sword.naiad.commons.webtest.WebPage;
import fr.sword.naiad.commons.webtest.annotation.WebTest;

public class TestSuitePAN extends AbstractSolonSuite {

	private final static String		LOI								= "L";
	private final static String		DECRET							= "D";

	private final static String		APPLICATION_LOI_REFERENCE		= "Reference";
	private final static String		APPLICATION_LOI_TITRE			= "Titre";
	private final static String		APPLICATION_LOI_NUMERO_ARTICLE	= "Numero-Article";
	private final static String		APPLICATION_LOI_NUMERO_ORDRE	= "Numero-Ordre";
	private final static String		APPLICATION_LOI_COMMENTAIRE		= "Commentaire";

	// Used just for test
	private final static boolean	VERIFY_FEUILLE_DE_ROUTE			= false;

	// Ministere Param
	private final static String		MINISTERE_PARAM					= "CCO - CCO";
	private final static String		AFFICHER_TOUTES_LES_MESURES		= "Afficher les mesures appliquées";
	private final static String		AFFICHER_LES_HABILITATIONS		= "Afficher les habilitations utilisées";
	private final static String		AFFICHER_LES_ORDONNANCES		= "Afficher les ordonnances dont la ratification a abouti";

	private final static int		INDEX_1							= 1;
	private final static int		INDEX_2							= 2;
	private final static int		INDEX_3							= 3;
	private final static int		INDEX_4							= 4;
	private final static int		INDEX_5							= 5;

	private static String[]			norDossierLoi					= new String[INDEX_5 + 1];
	private static String[]			norDossierDecret				= new String[INDEX_5 + 1];

	private String getNumeroLoi() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		return year + "-0001";
	}

	private String getLoiDossierId(int index) {
		return norDossierLoi[index];
	}

	private String getDecretDossierId(int index) {
		return norDossierDecret[index];
	}

	private String getCurrentDate(String format) {
		SimpleDateFormat formatDate = new SimpleDateFormat(format);
		return formatDate.format(Calendar.getInstance().getTime());
	}

	private void assignMesureData(ModificationMesurePage modificationMesurePage, String typeMesure, String numeroOrdre) {
		modificationMesurePage.setNumeroOrdre(numeroOrdre);
		modificationMesurePage.setArticleDeLaLoi("Article 1er");
		modificationMesurePage
				.setBaseLegale("loi n° 2010-729 du 30 juin 2010 tendant à suspendre la commercialisation de biberons produits à base de bisphénol A");
		modificationMesurePage
				.setObjet("Conditions dans lesquelles tout conditionnement comportant du bisphénol A et destiné à entrer en contact direct avec des denrées alimentaires doit comporter un avertissement sanitaire déconseillant son usage, du fait de la présence de bisphénol A, aux femmes enceintes, aux femmes allaitantes et aux nourrissons et enfants en bas âge");
		modificationMesurePage.setMinisterePilote("EFI");
		modificationMesurePage.setDirectionResponsable("DGCCRF");
		modificationMesurePage.setConsulttionObligatoireHorsCE("Commission européenne");
		modificationMesurePage.setCalendrierConsultation("CL");
		modificationMesurePage.setDateSaisieCE("06/2014");
		modificationMesurePage.setObjectivePublication("06/2014");
		modificationMesurePage
				.setObservations("DS, soumis à la commission européenne Le projet de décret d'application de la loi \"Bisphénol A\" a bien été notifié à la commission européenne au titre de la directive 98/34. Le SGAE a saisi le cabinet du Premier ministre pour choisir entre l'abandon du projet de décret ou bien sa modification, compte tenu des avis circonstanciés reçus. Dans cette dernière hypothèse, la publication pourrait être envisagée au 1er semestre 2014");
		if (typeMesure != null) {
			modificationMesurePage.setTypeMesure(typeMesure);
		}
		modificationMesurePage.setPoleChargeDeMission("AGR");
		modificationMesurePage.setAmendement();
		modificationMesurePage.setDateDePassageEnCM("30/06/2014");
		modificationMesurePage.setResponsableAmendement("Parlement");
		modificationMesurePage.setNumeroQuestion("114515");
		modificationMesurePage.setChampLibre("CL");
	}

	private void createDecret(int index, Map<String, String> applicationLoiInfo) {
		createDossier(index, DECRET, applicationLoiInfo);
	}

	private void createLoi(int index) {
		createDossier(index, LOI, null);
	}

	private void createDossier(int index, String type, Map<String, String> applicationLoiInfo,
			String ministereResponsable) {
		boolean isLoi = type.equals(LOI);

		TraitementPage traitementPage = loginAsAdminEpg();
		CreationPage espaceCreation = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page createDossierPage = espaceCreation.createDossier();

		String typeActe = isLoi ? "Loi" : "Décret en Conseil d'Etat";
		createDossierPage.setTypeActe(typeActe);

		createDossierPage.setMinistereResponable(ministereResponsable);
		createDossierPage
				.setDirectionConcerne(Arrays
						.asList("creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:62::min:handle:img:collapsed",
								"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:62:node:0::nxw_direction_resp_field_addDir"));
		AbstractCreationDossierPage creationDossierPage = createDossierPage
				.appuyerBoutonSuivant(isLoi ? CreationDossier108Page.class : CreationDossier101Page.class);
		if (creationDossierPage instanceof CreationDossier101Page && applicationLoiInfo != null) {

			CreationDossier101Page creationDossier101Page = (CreationDossier101Page) creationDossierPage;
			creationDossier101Page.checkapplicationLoi();

			CreationDossier104Page creationDossier104Page = creationDossier101Page
					.appuyerBoutonSuivant(CreationDossier104Page.class);
			creationDossier104Page.setReference(applicationLoiInfo.get(APPLICATION_LOI_REFERENCE));
			creationDossier104Page.setTitre(applicationLoiInfo.get(APPLICATION_LOI_TITRE));
			creationDossier104Page.setNumeroArticle(applicationLoiInfo.get(APPLICATION_LOI_NUMERO_ARTICLE));
			creationDossier104Page.setNumeroOrdre(applicationLoiInfo.get(APPLICATION_LOI_NUMERO_ORDRE));
			creationDossier104Page.setCommentaire(applicationLoiInfo.get(APPLICATION_LOI_COMMENTAIRE));
			creationDossier104Page.ajouterTransposition(applicationLoiInfo.get(APPLICATION_LOI_REFERENCE));

			creationDossierPage = creationDossier104Page;
		}

		CreationDossierSelectPostePage creationDossierSelectPostePage = creationDossierPage
				.appuyerBoutonTerminer(CreationDossierSelectPostePage.class);
		creationDossierSelectPostePage
				.setPoste(Arrays
						.asList("creation_dossier_select_poste:nxl_creation_dossier_layout_select_poste:nxw_dossier_creation_choix_poste_poste_tree:node:0:node:62::min:handle:img:collapsed",
								"creation_dossier_select_poste:nxl_creation_dossier_layout_select_poste:nxw_dossier_creation_choix_poste_poste_tree:node:0:node:62:node:5::nxw_dossier_creation_choix_poste_poste_addPst"));

		DossierDetailMenu dossierDetailMenu = creationDossierSelectPostePage
				.appuyerBoutonTerminer(DossierDetailMenu.class);
		DossierDetailMenu.sleep(1);

		String norDossierCree = creationDossierSelectPostePage.waitAndGetNumeroDossierCree();
		if (isLoi) {
			norDossierLoi[index] = norDossierCree;
		} else {
			norDossierDecret[index] = norDossierCree;
		}

		getFlog().action("On a crée le dossier " + norDossierCree + " qui utilise l'index interne " + index);
		// Verifier que Le dossier est ajouté à l'espace de création.
		String dossierId = norDossierCree; // isLoi ? this.getLoiDossierId(index) : this.getDecretDossierId(index);
		creationDossierSelectPostePage.assertTrue(dossierId);

		ParapheurPage parapheurPage = dossierDetailMenu.getPage(ParapheurPage.class);
		parapheurPage.verrouiller();

		// Ajouter un document
		parapheurPage.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']", "word2003.doc");

		FeuilleDeRoutePage feuilleDeRoutePage = dossierDetailMenu.goToFeuilleDeRoute();

		if (VERIFY_FEUILLE_DE_ROUTE) {
			feuilleDeRoutePage.assertTrue("Pour publication à la DILA JO");
		}

		BordereauPage bordereau = dossierDetailMenu.goToBordereau();

		bordereau.setTitreActe("Titre acte 6");
		bordereau.setNomDuResponsable("DUPONT");

		bordereau.setQualiteDuResponsable("Mr");

		bordereau.setTelDuResponsable("0238587469");

		bordereau.setPrenomDuResponsable("Jean");

		bordereau.setModeParution("Electronique");

		bordereau.setDelaiPublication("Aucun");

		bordereau.setRubriques("Domaine public");

		// Save Data
		bordereau.enregistrer();
		bordereau.deverouiller_par_alt();

		// Lancement du dossier
		// Open feuille de route tab
		feuilleDeRoutePage = dossierDetailMenu.goToFeuilleDeRoute();

		// Verouiller FDR
		feuilleDeRoutePage.verrouiller();

		// Lancer Dossier
		feuilleDeRoutePage.lancerDossier();

		logoutEpg();
	}

	private void createDossier(int index, String type, Map<String, String> applicationLoiInfo) {
		createDossier(index, type, applicationLoiInfo, "PRM - Premier Ministre");
	}

	@WebTest(description = "Test pop paramétrage des statistiques", order = 5)
	@TestDocumentation(categories = { "Administration", "Statistiques", "Paramétrage" })
	public void TST_PARAMETRAGE_STATISTIQUES() {

		maximizeBrowser();

		// Connexion avec : Contributeur Activité Normative du SGG
		TraitementPage traitementPage = loginEpg("contributeuransgg");

		PilotageANPage pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();
		pilotageAN.openParamStatsPopUp();
		ParamStatField fields = new ParamStatField();
		fields.setBs_ec_prom_deb("01/01/1991");
		fields.setBs_ec_prom_fin("02/02/1992");
		fields.setBs_ec_pub_deb("03/03/1993");
		fields.setBs_ec_pub_fin("04/04/1994");

		fields.setEc_debutLegislature("05/05/1995");

		fields.setTp_ec_prom_deb("06/06/1996");
		fields.setTp_ec_prom_fin("07/07/1997");
		fields.setTp_ec_pub_deb("08/08/1998");
		fields.setTp_ec_pub_fin("09/09/1999");

		fields.setTl_ec_prom_deb("10/10/2000");
		fields.setTl_ec_prom_fin("11/11/2001");
		fields.setTl_ec_pub_deb("12/12/2002");
		fields.setTl_ec_pub_fin("01/01/2003");

		pilotageAN
				.waitElementVisibilityById("displayedStatsParametersPopup:configureStatsParametersForm:nxl_debut_legis_encours:nxw_ec_debut_legislatureInputDate");

		pilotageAN.addLegislature("13");
		pilotageAN.addLegislature("14");

		pilotageAN.fillParamStatsPopUp(fields);

		pilotageAN.validerParamStatsPopUp();
		pilotageAN.openParamStatsPopUp();

		pilotageAN.setLegislatureEnCours("14");
		pilotageAN.validerParamStatsPopUp();

		pilotageAN.openExportPopUp();
		pilotageAN.checkExportPopupValues(fields);
		// Logout
		logoutEpg();

	}

	@WebTest(description = "Gestion de l'organigramme", order = 10)
	@TestDocumentation(categories = { "Administration", "Organigramme" })
	public void TST_GESTION_ORGANIGRAMME() {

		maximizeBrowser();
		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		GestionOrganigrammePage gestionOrganigrammePage = administrationEpgPage.goToGestionOrganigrammePage();
		EditerOrganigrammePage editerOrganigrammePage = gestionOrganigrammePage.modifierElement(MINISTERE_PARAM);
		editerOrganigrammePage.setSuiviAn();
		editerOrganigrammePage.setNomMembreGouvernement("Test");
		editerOrganigrammePage.setPrenomMembreGouvernement("Test");
		// Enregistrer
		editerOrganigrammePage.enregistrer();
		logoutEpg();

	}

	@WebTest(description = "Administration FDR defaut", order = 40)
	@TestDocumentation(categories = { "Administration", "FDR" })
	public void testCaseFeuilleDeRouteDefaut() {

		maximizeBrowser();

		TraitementPage traitementPage = loginAsAdminEpg();
		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		GestionModelesPage gestionModelesPage = administrationEpgPage.goToGestionModeles();
		CreationModelePage creationModelePage = gestionModelesPage.goToCreationModele();
		creationModelePage.setintituleLibre("PRM-LOI");
		creationModelePage.setMinistere("PRM - Premier Ministre");
		creationModelePage.setTypeActe("Loi");
		creationModelePage.checkDefault();
		creationModelePage.creer();

		CreationEtapePage creationEtapePage = creationModelePage.ajouterEtape();
		creationEtapePage.setTypeEtape("Pour publication à la DILA JO");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62:node:5::nxw_routing_task_distribution_mailbox_addPst"));
		creationModelePage = creationEtapePage.creer();

		ViewModelePage viewModelePage = creationModelePage.validerModele();
		gestionModelesPage = viewModelePage.retourListe();

		logoutEpg();

		traitementPage = loginAsAdminEpg();
		administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		gestionModelesPage = administrationEpgPage.goToGestionModeles();
		creationModelePage = gestionModelesPage.goToCreationModele();
		creationModelePage.setintituleLibre("PRM-DECRET");
		creationModelePage.setMinistere("PRM - Premier Ministre");
		creationModelePage.setTypeActe("Décret en Conseil d'Etat");
		creationModelePage.checkDefault();
		creationModelePage.creer();

		creationEtapePage = creationModelePage.ajouterEtape();
		creationEtapePage.setTypeEtape("Pour attribution");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62:node:5::nxw_routing_task_distribution_mailbox_addPst"));
		creationModelePage = creationEtapePage.creer();

		creationEtapePage = creationModelePage.ajouterEtape();
		creationEtapePage.setTypeEtape("Pour avis CE");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:9::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:9:node:6::nxw_routing_task_distribution_mailbox_addPst"));
		creationModelePage = creationEtapePage.creer();

		creationEtapePage = creationModelePage.ajouterEtape();
		creationEtapePage.setTypeEtape("Pour attribution");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62:node:5::nxw_routing_task_distribution_mailbox_addPst"));
		creationModelePage = creationEtapePage.creer();

		creationEtapePage = creationModelePage.ajouterEtape();
		creationEtapePage.setTypeEtape("Pour publication à la DILA JO");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62:node:5::nxw_routing_task_distribution_mailbox_addPst"));
		creationModelePage = creationEtapePage.creer();

		creationEtapePage = creationModelePage.ajouterEtape();
		creationEtapePage.setTypeEtape("Pour attribution");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:62:node:5::nxw_routing_task_distribution_mailbox_addPst"));
		creationModelePage = creationEtapePage.creer();

		viewModelePage = creationModelePage.validerModele();
		gestionModelesPage = viewModelePage.retourListe();
		// Logout
		logoutEpg();
	}

	@WebTest(description = "Test création loi", order = 50)
	@TestDocumentation(categories = { "Dossier" })
	public void TST_CREATION_LOI() {
		createLoi(INDEX_1);
		createDossier(INDEX_2, LOI, null, "CCO - CCO");
	}

	@WebTest(description = "Ajouter un texte maître", order = 60)
	@TestDocumentation(categories = { "Texte maitre" })
	public void TST_APP_LOI_001() {

		maximizeBrowser();

		// Connextion avec : Contributeur Activité Normative du SGG
		TraitementPage traitementPage = loginEpg("contributeuransgg");

		// Tabs pour contributeuransgg
		traitementPage.assertElementValue(By_enum.ID,
				"userServicesForm:userServicesActionsTable:0:userServicesActionCommandLink", "Suivi");
		traitementPage.assertElementValue(By_enum.ID,
				"userServicesForm:userServicesActionsTable:1:userServicesActionCommandLink", "Recherche");
		traitementPage.assertElementValue(By_enum.ID,
				"userServicesForm:userServicesActionsTable:2:userServicesActionCommandLink",
				"Pilotage de l'activité normative");
		traitementPage.assertElementValue(By_enum.ID,
				"userServicesForm:userServicesActionsTable:3:userServicesActionCommandLink", "Espace parlementaire");

		traitementPage.assertFalse("Création");
		traitementPage.assertFalse("Administration");

		PilotageANPage pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();
		ApplicationDesLoisPage applicationDesLoisPage = pilotageAN.goToApplicationDesLoisPage();

		// On fait un 'wait' sur le dernier onglet => comme ça on est sûr que tous auront eu le temps de s'afficher
		applicationDesLoisPage.waitForPageSourcePart("Recherche de l'activité normative", TIMEOUT_SEC);
		applicationDesLoisPage.assertTrue("Tableau-maître");
		applicationDesLoisPage.assertTrue("Tableau-maître (Ministères)");
		applicationDesLoisPage.assertTrue("Tableaux de bord");
		applicationDesLoisPage.assertTrue("Indicateurs LOLF");
		applicationDesLoisPage.assertTrue("Bilan Semestriel");
		applicationDesLoisPage.assertTrue("Taux d'application au fil de l'eau");
		applicationDesLoisPage.assertTrue("Délais moyens");
		applicationDesLoisPage.assertTrue("Statistiques sur la publication");
		applicationDesLoisPage.assertTrue("Tableau des lois");
		applicationDesLoisPage.assertTrue("Historique des mises à jour ministérielles");

		// Renseigner un NOR ne correspondant pas à un dossier EPG existant
		TableauMaitreViewPage textMaitreViewPage = applicationDesLoisPage.ajouterNor("PRMX1400000D");
		String aucunNorNeCorrespondAlaRecherche = "Aucun NOR ne correspond à la recherche.";
		textMaitreViewPage.waitForPageSourcePart(aucunNorNeCorrespondAlaRecherche, TIMEOUT_SEC);
		textMaitreViewPage.assertTrue(aucunNorNeCorrespondAlaRecherche);

		// Renseigner un NOR ne correspondant pas à un dossier EPG de loi
		textMaitreViewPage = applicationDesLoisPage.ajouterNor("EFIM" + anneeCourante + "00000D");
		String dossierNePeutPasEtreAjoute = "Le dossier ne peut être ajouté dans cette liste";
		textMaitreViewPage.waitForPageSourcePart(dossierNePeutPasEtreAjoute, TIMEOUT_SEC);
		textMaitreViewPage.assertTrue(dossierNePeutPasEtreAjoute);

		// Renseigner un NOR valide
		textMaitreViewPage = applicationDesLoisPage.ajouterNor(getLoiDossierId(INDEX_1));

		textMaitreViewPage
				.assertElementValue(
						By_enum.XPATH,
						"//form[@id='tableau_maitre_application_lois']/div[@name='containerTabDiv']/table/tbody/tr[2]/td[2]/span/a",
						"PRM");
		textMaitreViewPage
				.assertElementValue(
						By_enum.XPATH,
						"//form[@id='tableau_maitre_application_lois']/div[@name='containerTabDiv']/table/tbody/tr[2]/td[3]/table/tbody/tr/td[1]/a",
						getLoiDossierId(INDEX_1));
		boolean checkBoxSelected = applicationDesLoisPage
				.isCheckBoxSelected("tableau_maitre_application_lois:nxl_tableau_maitre_application_lois_listing_1:nxw_listing_ajax_selection_box");
		Assert.assertFalse(checkBoxSelected);
		// Je ne sais pas ce que l'on veut tester
		textMaitreViewPage.assertElementValue(By_enum.XPATH,
				"//form[@id='tableau_maitre_application_lois']/div[@name='containerTabDiv']/table/tbody/tr[2]/td[4]",
				"");
		textMaitreViewPage
				.assertElementValue(
						By_enum.XPATH,
						"//form[@id='tableau_maitre_application_lois']/div[@name='containerTabDiv']/table/tbody/tr[2]/td[5]/span",
						"");
		textMaitreViewPage
				.assertElementValue(
						By_enum.XPATH,
						"//form[@id='tableau_maitre_application_lois']/div[@name='containerTabDiv']/table/tbody/tr[2]/td[7]/span",
						"");
		textMaitreViewPage.assertElementValue(By_enum.XPATH,
				"//form[@id='tableau_maitre_application_lois']/div[@name='containerTabDiv']/table/tbody/tr[2]/td[8]",
				"");

		// Make a mouseOver action on help icon
		String mouseOverMsg = "Cliquez sur le titre de la colonne pour trier sur un seul critère, et cliquez sur les flèches pour ajouter de nouveaux critères de tri. Cliquez à nouveau pour changer le sens du tri.";
		textMaitreViewPage.makeMouseOverAction(By_enum.XPATH, "//img[@src='/solon-epg/icons/lightbulb.png']",
				mouseOverMsg);
		textMaitreViewPage.assertTrue(mouseOverMsg);

		// ajout d'un second texte maitre
		textMaitreViewPage = applicationDesLoisPage.ajouterNor(getLoiDossierId(INDEX_2));

		// Test du tri sur les colonnes
		// Triage par ministere
		textMaitreViewPage.sortByMinistere();
		textMaitreViewPage
				.assertElementValue(
						By_enum.XPATH,
						"//form[@id='tableau_maitre_application_lois']/div[@name='containerTabDiv']/table/tbody/tr[1]/td[2]/span/a",
						"PRM");
		textMaitreViewPage
				.assertElementValue(
						By_enum.XPATH,
						"//form[@id='tableau_maitre_application_lois']/div[@name='containerTabDiv']/table/tbody/tr[2]/td[2]/span/a",
						"CCO");

		textMaitreViewPage.sortByMinistere();
		textMaitreViewPage
				.assertElementValue(
						By_enum.XPATH,
						"//form[@id='tableau_maitre_application_lois']/div[@name='containerTabDiv']/table/tbody/tr[1]/td[2]/span/a",
						"CCO");
		textMaitreViewPage
				.assertElementValue(
						By_enum.XPATH,
						"//form[@id='tableau_maitre_application_lois']/div[@name='containerTabDiv']/table/tbody/tr[2]/td[2]/span/a",
						"CCO");
		textMaitreViewPage
				.assertElementValue(
						By_enum.XPATH,
						"//form[@id='tableau_maitre_application_lois']/div[@name='containerTabDiv']/table/tbody/tr[3]/td[2]/span/a",
						"PRM");

		DossierDetailMenu textMaitreDetailMenu = textMaitreViewPage.getTextMaitreDetailMenu(getLoiDossierId(INDEX_1));

		// Validate tabs details
		textMaitreDetailMenu.assertTrue("Texte-maître");
		textMaitreDetailMenu.assertTrue("Fiche signalétique");
		textMaitreDetailMenu.assertTrue("Tableau de programmation");
		textMaitreDetailMenu.assertTrue("Tableau de suivi");
		textMaitreDetailMenu.assertTrue("Mesures d'application");
		textMaitreDetailMenu.assertTrue("Bilan semestriel");
		textMaitreDetailMenu.assertTrue("Taux d'application au fil de l'eau par loi");

		// Text Maitre detail page
		TextMaitrePage textMaitrePage = textMaitreDetailMenu.goToTextMaitrePage();
		textMaitrePage.assertElementValue(By_enum.XPATH, "//form[@id='texte_maitre_Form']/table/tbody/tr[4]/td[3]/a",
				getLoiDossierId(INDEX_1));
		textMaitrePage.verrouiller();
		// textMaitrePage.setNumeroLock(this.getNumeroLoi());
		// textMaitrePage.setNumeroInterne(this.getNumeroLoi());
		textMaitrePage.setNatureDuText("Projet de loi");
		textMaitrePage.setNatureDuVote("Procédure accélérée");
		textMaitrePage.sauvegarder();
		// Deverouiller
		textMaitrePage.deverouiller_par_alt();
		logoutEpg();

	}

	@WebTest(description = "Filtrer / Rechercher un texte maître", order = 70)
	@TestDocumentation(categories = { "Texte maitre", "Filtre" })
	public void TST_APP_LOI_002() {

		maximizeBrowser();

		// Connextion avec : Contributeur Activité Normative du SGG
		TraitementPage traitementPage = loginEpg("contributeuransgg");

		PilotageANPage pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();
		TableauMaitreViewPage tableauMaitreViewPage = pilotageAN.goToApplicationDesLoisTextMaitreViewPage();

		// Open the Filter
		TableauMaitreFilterPage tableauMaitreFilter = tableauMaitreViewPage.openTextMaitreFilter();
		// Filtrer par ministere
		tableauMaitreFilter.filtreParMinistere("PRM - Premier Ministre");
		// Reiinitialiser les criteres de recherche
		tableauMaitreFilter.reinitialiser();

		tableauMaitreFilter = tableauMaitreViewPage.openTextMaitreFilter();
		// Filter par NOR
		tableauMaitreFilter.filtrerParNor(getLoiDossierId(INDEX_1));
		// Logout
		logoutEpg();

	}

	@WebTest(description = "Modifier un texte maître", order = 80)
	@TestDocumentation(categories = { "Texte maitre" })
	public void TST_APP_LOI_003() {
		maximizeBrowser();

		// Connextion avec : Contributeur Activité Normative du SGG
		TraitementPage traitementPage = loginEpg("contributeuransgg");

		PilotageANPage pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();
		TableauMaitreViewPage tableauMaitreViewPage = pilotageAN.goToApplicationDesLoisTextMaitreViewPage();

		DossierDetailMenu textMaitreDetailMenu = tableauMaitreViewPage
				.getTextMaitreDetailMenu(getLoiDossierId(INDEX_1));

		TextMaitrePage textMaitrePage = textMaitreDetailMenu.goToTextMaitrePage();
		textMaitrePage.verrouiller();

		List<String> tmLabels = Arrays.asList("Mot clé", "N° interne", "N°", "NOR", "Intitulé", "Ministère pilote",
				"Nature du texte", "Procédure de vote", "Date d'entrée en vigueur", "Commission Sénat",
				"Commission Assemblée nationale", "Titre", "Date de promulgation", "Date de publication",
				"Législature de publication", "Loi d'application immédiate", "Observation", "Champ libre",
				"Loi d'habilitation", "Date de la réunion de programmation",
				"Date de mise en circulation du CR de la réunion");
		for (String label : tmLabels) {
			textMaitrePage.assertTrue(label);
		}

		textMaitrePage.setMotCle("Mot clé NEW");
		textMaitrePage.setIntitule("Initulé " + getLoiDossierId(INDEX_1));
		textMaitrePage.setNatureDuText("Proposition de loi");
		textMaitrePage.setNatureDuVote("Procédure accélérée");
		textMaitrePage.setMinisterePilote("CCO - CCO");
		textMaitrePage.selectApplicationDeLoiImmediate();
		textMaitrePage.setObservation("Obsrv.");
		textMaitrePage.setChampLibre("cl");
		textMaitrePage.selectLoiAbilitation();
		textMaitrePage.setDateDeReunion("27/06/2014");
		textMaitrePage.setDateMiseEnCirculation("27/06/2014");

		// Sauvegarder les donnees
		textMaitrePage.sauvegarder();

		// Deverouiller
		textMaitrePage.deverouiller_par_alt();

		TextMaitrePage.sleep(1);

		// Assert Elements after save
		textMaitrePage.assertTrue("Mot clé NEW");
		textMaitrePage.assertTrue("Initulé " + getLoiDossierId(INDEX_1));
		textMaitrePage.assertTrue("Proposition de loi");
		textMaitrePage.assertTrue("Procédure accélérée");
		textMaitrePage.assertTrue("Obsrv.");
		textMaitrePage.assertTrue("cl");

		textMaitrePage.assertTrue("27/06/2014");
		textMaitrePage.assertTrue("27/06/2014");

		// Verfier que l'image (selected CheckBox) de Loi d'application immediate existe
		WebElement el = textMaitrePage
				.getElementByXpath("//table[@class='dataInput']/tbody/tr[16]/td[3]//img[@src=\"/solon-epg/icons/task.png\"]");
		Assert.assertNotNull(el);

		// Verfier que l'image (selected CheckBox) de Loi d'habilitation existe
		el = textMaitrePage
				.getElementByXpath("//table[@class='dataInput']/tbody/tr[19]/td[3]//img[@src=\"/solon-epg/icons/task.png\"]");
		Assert.assertNotNull(el);
		// Logout
		logoutEpg();

	}

	@WebTest(description = "Mises à jour automatiques du texte maître", order = 90)
	@TestDocumentation(categories = { "Texte maitre", "Parapheur" })
	public void TST_APP_LOI_004() {

		maximizeBrowser();

		TraitementPage traitementPage = loginEpg("contributeuransgg");

		PilotageANPage pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();
		TableauMaitreViewPage tableauMaitreViewPage = pilotageAN.goToApplicationDesLoisTextMaitreViewPage();

		TableauMaitreDossierDetail textMaitreDetailMenu = tableauMaitreViewPage
				.getTextMaitreDetailMenu(getLoiDossierId(INDEX_1));

		// Text Maitre detail page
		TextMaitrePage textMaitrePage = textMaitreDetailMenu.goToTextMaitrePage();
		textMaitrePage.verrouiller();

		// Assert data before refresh
		textMaitrePage
				.assertElementValue(By_enum.ID, TextMaitrePage.INTITULE_ID, "Initulé " + getLoiDossierId(INDEX_1));
		textMaitrePage.assertMinisterePiloteValue("50002618");
		// Refresh
		textMaitrePage.recharger();
		// Apres Refresh =>L'intitulé du texte-maître et le ministère pilote sont modifiés avec les valeurs du dossier
		// EPG.
		textMaitrePage.assertElementValue(By_enum.ID, TextMaitrePage.INTITULE_ID, "Titre acte 6");
		textMaitrePage.assertMinisterePiloteValue("50000607");

		textMaitrePage.setIntitule("new title");
		textMaitrePage.lockIntitule();
		textMaitrePage.sauvegarder();
		// La modification du champ est conservée.
		textMaitrePage.assertElementValue(By_enum.ID, TextMaitrePage.INTITULE_ID, "new title");

		textMaitrePage.setMinisterePilote("ART - ARCEP");
		textMaitrePage
				.waitForFieldValue(
						"texte_maitre_Form:nxl_texte_maitre:nxw_activite_normative_widget_ministereResp_dos_nodeId",
						"50002368");
		textMaitrePage.lockMinisterePilote();
		textMaitrePage.sauvegarder();
		textMaitrePage.assertMinisterePiloteValue("50002368");

		logoutEpg();
	}

	@WebTest(description = "Webservices/publication", order = 100)
	@TestDocumentation(categories = { "WS", "Publication" })
	public void PUBLCATION_0() {

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
		xmlForWs.append(" <p1:date_parution>" + getCurrentDate("yyyy-MM-dd") + "</p1:date_parution>");
		xmlForWs.append(" </p1:gestion> <p1:acte> <p1:nor>" + getLoiDossierId(INDEX_1) + "</p1:nor>");
		xmlForWs.append(" <p1:numero_texte>" + getNumeroLoi() + "</p1:numero_texte>");
		xmlForWs.append(" <p1:titre_officiel>titre officiel 100</p1:titre_officiel>");
		xmlForWs.append(" <p1:page>100</p1:page>");
		xmlForWs.append(" </p1:acte>");
		xmlForWs.append(" </p:retourPublicationJo>");
		xmlForWs.append(" </p:envoyerRetourPERequest>");

		WsResultPage wsResultPage = loginWSEpg("ws_ministere_prm", "ws_ministere_prm", getEpgUrl()
				+ "/site/solonepg/WSspe/envoyerRetourPE", xmlForWs.toString());
		wsResultPage
				.assertTextResult("&lt;envoyerRetourPEResponse xmlns=&quot;http://www.dila.premier-ministre.gouv.fr/solon/epg/WSSpe&quot;&gt;&lt;status&gt;OK&lt;/status&gt;&lt;/envoyerRetourPEResponse&gt;");

	}

	@WebTest(description = "Ajouter une mesure d'application", order = 110)
	@TestDocumentation(categories = { "Texte maitre", "Mesure" })
	public void TST_APP_LOI_005() {

		maximizeBrowser();

		// Connextion avec : Contributeur Activité Normative du SGG
		TraitementPage traitementPage = loginEpg("contributeuransgg");

		PilotageANPage pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();
		TableauMaitreViewPage tableauMaitreViewPage = pilotageAN.goToApplicationDesLoisTextMaitreViewPage();

		TableauMaitreDossierDetail textMaitreDetailMenu = tableauMaitreViewPage
				.getTextMaitreDetailMenu(getLoiDossierId(INDEX_1));

		ParapheurPage parapheurPage = textMaitreDetailMenu.getPage(ParapheurPage.class);

		MesureViewPage mesureViewPage = textMaitreDetailMenu.getMesureViewPage();

		List<String> mesureLabels = Arrays.asList("N° ordre", "Article de la loi", "Code modifié", "Base légale",
				"Objet", "Ministère pilote", "Direction responsable", "Consultation obligatoire hors CE",
				"Calendrier consultations hors CE", "Date prévisionnelle de saisine CE", "Objectif de publication",
				"Observations", "Type de mesure", "Mesure ayant reçu application", "Date de mise en application",
				"Date certaine d'entrée en vigueur", "Date limite du délai de six mois", "Pôle chargé de mission",
				"Amendement", "Date de passage en CM", "Responsable amendement", "Numéro question parlementaire",
				"Champ libre");
		for (String mesureLabel : mesureLabels) {
			mesureViewPage.assertTrue(mesureLabel);
		}

		ModificationMesurePage modificationMesurePage = mesureViewPage.edit();
		assignMesureData(modificationMesurePage, "Active", "0");
		// Valider les modifications
		modificationMesurePage.valider();

		List<String> allCases = Arrays
				.asList("Autre", "Différée", "En cours", "Facultative", "Hors LOLF", "Éventuelle");

		int index = 1;
		for (String typeMesure : allCases) {
			mesureViewPage.ajouterUneMesure();
			modificationMesurePage = mesureViewPage.edit(index);
			assignMesureData(modificationMesurePage, typeMesure, String.valueOf(index));
			modificationMesurePage.valider();
			index++;
		}

		// Vérifier que les mesures sont correctement ajoutées.
		mesureViewPage.assertNombreDesMesures(7);

		// Sauvegarder le detail
		mesureViewPage.sauvegarderDetail();

		// Deverouiller
		parapheurPage.deverouiller_par_alt();

		// Logout
		logoutEpg();

	}

	@WebTest(description = "Test création Decret", order = 120)
	@TestDocumentation(categories = { "Décret" })
	public void TST_CREA_DECCE() {
		createDecret(INDEX_2, null);
	}

	@WebTest(description = "Ajouter un décret d'application", order = 130)
	@TestDocumentation(categories = { "Décret", "Texte maitre" })
	public void TST_APP_LOI_006() {

		maximizeBrowser();

		// Connextion avec : Contributeur Activité Normative du SGG
		TraitementPage traitementPage = loginEpg("contributeuransgg");

		PilotageANPage pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();
		TableauMaitreViewPage tableauMaitreViewPage = pilotageAN.goToApplicationDesLoisTextMaitreViewPage();

		TableauMaitreDossierDetail textMaitreDetailMenu = tableauMaitreViewPage
				.getTextMaitreDetailMenu(getLoiDossierId(INDEX_1));

		ParapheurPage parapheurPage = textMaitreDetailMenu.getPage(ParapheurPage.class);
		parapheurPage.verrouiller();

		TextMaitrePage textMaitrePage = textMaitreDetailMenu.goToTextMaitrePage();
		DetailDecretPage detailDecretPage = textMaitrePage.detailDecretPage(0);

		detailDecretPage.ajouterNor(getDecretDossierId(INDEX_2));

		List<String> decLabels = Arrays.asList("Nor", "Titre", "Catégorie", "Section du CE", "Date de saisine CE",
				"Date d'examen CE", "Rapporteur CE", "Réference Avis CE", "N° texte", "Date signature",
				"Date publication", "Délai de publication", "N° du JO", "N° de page");
		for (String decLabel : decLabels) {
			detailDecretPage.assertTrue(decLabel);
		}

		// Logout
		logoutEpg();

		// STEP 8
		createDecret(INDEX_3, null);

		Map<String, String> applicationLoiInfo = new HashMap<String, String>();
		applicationLoiInfo.put(APPLICATION_LOI_REFERENCE, getNumeroLoi());
		applicationLoiInfo.put(APPLICATION_LOI_TITRE, "Titre créé à partir du Décret");
		applicationLoiInfo.put(APPLICATION_LOI_NUMERO_ARTICLE, "Art1");
		applicationLoiInfo.put(APPLICATION_LOI_NUMERO_ORDRE, "20");
		applicationLoiInfo.put(APPLICATION_LOI_COMMENTAIRE, "libre");
		createDecret(INDEX_4, applicationLoiInfo);

		// Pas besoin de faire de logout : il est fait via createDecret
	}

	@WebTest(description = "Ajouter un décret d'application/1", order = 140)
	@TestDocumentation(categories = { "Décret", "Texte maitre" })
	public void TST_APP_LOI_006_1() {
		maximizeBrowser();
		TraitementPage traitementPage = loginEpg("contributeuransgg");
		PilotageANPage pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();
		TableauMaitreViewPage tableauMaitreViewPage = pilotageAN.goToApplicationDesLoisTextMaitreViewPage();
		DossierDetailMenu textMaitreDetailMenu = tableauMaitreViewPage
				.getTextMaitreDetailMenu(getLoiDossierId(INDEX_1));
		TextMaitrePage textMaitrePage = textMaitreDetailMenu.goToTextMaitrePage();
		// Get The Detail

		DetailDecretPage detailDecretPage = textMaitrePage.detailDecretPage(0);
		detailDecretPage.ajouterNor(getDecretDossierId(INDEX_3));
		detailDecretPage.sauvegarder();
		detailDecretPage.assertTrue("Décret en Conseil d'Etat");
		detailDecretPage.assertTrue(getDecretDossierId(INDEX_3));
		detailDecretPage.verifylegifranceLink(getDecretDossierId(INDEX_3));

		logoutEpg();
	}

	@WebTest(description = "Modification des mesures et décrets", order = 150)
	@TestDocumentation(categories = { "Décret", "Texte maitre", "Surbrillance PAN" })
	public void TST_APP_LOI_007() {

		Map<String, String> applicationLoiInfo = new HashMap<String, String>();
		applicationLoiInfo.put(APPLICATION_LOI_REFERENCE, getNumeroLoi());
		applicationLoiInfo.put(APPLICATION_LOI_TITRE, "Titre créé à partir du Décret");
		applicationLoiInfo.put(APPLICATION_LOI_NUMERO_ARTICLE, "Art1");
		applicationLoiInfo.put(APPLICATION_LOI_NUMERO_ORDRE, "6");
		applicationLoiInfo.put(APPLICATION_LOI_COMMENTAIRE, "libre");
		createDecret(INDEX_5, applicationLoiInfo);

		maximizeBrowser();
		TraitementPage traitementPage = loginEpg("contributeuransgg");
		PilotageANPage pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();
		TableauMaitreViewPage tableauMaitreViewPage = pilotageAN.goToApplicationDesLoisTextMaitreViewPage();
		DossierDetailMenu textMaitreDetailMenu = tableauMaitreViewPage
				.getTextMaitreDetailMenu(getLoiDossierId(INDEX_1));
		TextMaitrePage textMaitrePage = textMaitreDetailMenu.goToTextMaitrePage();
		// Get The Detail
		String path = "//tr[@class=\"dataRowRetourPourModification\"]";
		// On dénombre les lignes surlignées.
		// Attention, pour chaque ligne surlignées, le tableau des mesures est construit en deux parties, donc 1 ligne
		// visuelle surlignée = 2 row de class dataRowRetourPourModification
		List<WebElement> elementsBy = textMaitrePage.getElementsBy(By.xpath(path));
		Assert.assertEquals(2, elementsBy.size());

		DetailDecretPage detailDecretPage = textMaitrePage.detailDecretPage(6);

		elementsBy = textMaitrePage.getElementsBy(By.xpath(path));
		Assert.assertEquals(3, elementsBy.size());

		detailDecretPage.sauvegarder();

		elementsBy = textMaitrePage.getElementsBy(By.xpath(path));
		Assert.assertEquals(0, elementsBy.size());

		// Changement des donnees du decret
		detailDecretPage = textMaitrePage.detailDecretPage(6);
		String titreMesure = "Titre Mesure";
		String sectionDuCe = "SCC";
		String dateSaisine = "07/06/2014";
		String dateExamenCe = "08/06/2014";
		String rapporteurCe = "RCE";
		String referenceAvisCe = "RACE";
		String numeroText = "4545";
		String dateSignature = "09/06/2014";
		String datePublication = "10/06/2014";
		String numeroJo = "852";
		String numeroPage = "741";

		boolean lock = false;
		detailDecretPage.assignDecretNor(getDecretDossierId(INDEX_4));
		detailDecretPage.setTitre(titreMesure);
		detailDecretPage.setSectionDuCe(sectionDuCe, lock);
		detailDecretPage.setDateSaisieCe(dateSaisine, lock);
		detailDecretPage.setDateExamenCe(dateExamenCe, lock);
		detailDecretPage.setRaporteurCe(rapporteurCe, lock);
		detailDecretPage.setReferenceAvisCe(referenceAvisCe);
		detailDecretPage.setNumeroDuText(numeroText);
		detailDecretPage.setDateSignature(dateSignature);
		detailDecretPage.setDatePublication(datePublication, lock);
		detailDecretPage.setNumeroJo(numeroJo);
		detailDecretPage.setNumeroPage(numeroPage, lock);

		// Save Data
		detailDecretPage.sauvegarder();

		// Validate data after save
		detailDecretPage.assertElementValue(DetailDecretPage.DECRET_NOR_INPUT_ID, getDecretDossierId(INDEX_4));
		detailDecretPage.assertElementText(DetailDecretPage.TITLE_ID, titreMesure);
		detailDecretPage.assertElementText(DetailDecretPage.SECTIONDUCE_ID, sectionDuCe);
		detailDecretPage.assertElementValue(DetailDecretPage.DATE_SAISIE_CE_ID, dateSaisine);
		detailDecretPage.assertElementValue(DetailDecretPage.DATE_EXAMEN_CE_ID, dateExamenCe);
		detailDecretPage.assertElementText(DetailDecretPage.RAPPORTEUR_CE_ID, rapporteurCe);
		detailDecretPage.assertElementText(DetailDecretPage.REFERENCE_AVIS_CE_ID, referenceAvisCe);
		detailDecretPage.assertElementText(DetailDecretPage.NUMERO_DU_TEXT_ID, numeroText);
		detailDecretPage.assertElementValue(DetailDecretPage.DATE_SIGNATURE_ID, dateSignature);
		detailDecretPage.assertElementValue(DetailDecretPage.DATE_PUBLICATION_ID, datePublication);
		detailDecretPage.assertElementText(DetailDecretPage.NUMERO_JO_ID, numeroJo);
		detailDecretPage.assertElementValue(DetailDecretPage.NUMERO_PAGE_ID, numeroPage);

		// Refresh =>Clear data
		detailDecretPage.refresh();
		ParapheurPage parapheurPage = textMaitreDetailMenu.getPage(ParapheurPage.class);
		parapheurPage.deverouiller_par_alt();
		// Logout
		logoutEpg();

		// Validate Decret
		validateDecret(INDEX_4);

		traitementPage = loginEpg("contributeuransgg");
		pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();
		tableauMaitreViewPage = pilotageAN.goToApplicationDesLoisTextMaitreViewPage();
		textMaitreDetailMenu = tableauMaitreViewPage.getTextMaitreDetailMenu(getLoiDossierId(INDEX_1));
		textMaitrePage = textMaitreDetailMenu.goToTextMaitrePage();

		parapheurPage = textMaitreDetailMenu.getPage(ParapheurPage.class);
		parapheurPage.verrouiller();

		detailDecretPage = textMaitrePage.detailDecretPage(6);
		//
		detailDecretPage.assertElementValue(DetailDecretPage.DATE_SAISIE_CE_ID, getCurrentDate("dd/MM/yyyy"));
		logoutEpg();
	}

	@WebTest(description = "rafraîchir tous les rapports", order = 160)
	@TestDocumentation(categories = { "Statistiques" })
	public void TST_STATS_REFRESH_REPORTS() {

		maximizeBrowser();
		TraitementPage traitementPage = loginEpg("contributeuransgg");
		PilotageANPage pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();

		for (ReportInfo reportInfo : ReportInfo.values()) {
			getFlog().check("On teste les rapports " + reportInfo.name());
			for (String title : reportInfo.getPath()) {

				pilotageAN.waitElementVisibilityByLinkText(title);
				int tentative = 0;
				boolean succes = false;
				while (!succes && tentative < 2) {
					WebElement el = pilotageAN.getElementByLinkText(title);
					try {
						el.click();
						succes = true;
					} catch (StaleElementReferenceException e) {
						// Parfois, Selenium n'arrive pas à cliquer sur le bouton, alors qu'il était déclaré visible.
						// Suivons ses conseils:
						// "Element not found in the cache - perhaps the page has changed since it was looked up"
						tentative++;
						WebPage.sleep(5);
					}
				}
				pilotageAN.waitForPageLoaded(getDriver());

			}

			// Finally Refresh the report => Only if Refresh is supported
			if (reportInfo.isSupportRefresh()) {

				String refreshImage = "//img[@src='/solon-epg/img/icons/refresh.png']";
				pilotageAN.waitElementVisibilityByXpath(refreshImage);
				pilotageAN.getElementByXpath(refreshImage).click();
				pilotageAN.waitForPageLoaded(getDriver());
			}

		}

		logoutEpg();
	}

	@WebTest(description = "Valider les rapports", order = 170)
	@TestDocumentation(categories = { "Statistiques" })
	public void TST_STATS_VALIDATE_REPORTS() {
		maximizeBrowser();
		TraitementPage traitementPage = loginEpg("contributeuransgg");
		PilotageANPage pilotageAN = traitementPage.getOngletMenu().goToPilotageAN();

		String lastReportName = null;
		for (ReportInfo reportInfo : ReportInfo.values()) {

			List<String> path = reportInfo.getPath();
			String reportName = path.get(path.size() - 1);

			if (reportName.equals(AFFICHER_TOUTES_LES_MESURES) && lastReportName != null) {
				reportName = lastReportName + "/" + reportName;
			}
			lastReportName = reportName;

			getFlog().startAction("vérification du rapport " + reportName);

			PanReportValidator selectedPage = null;
			for (String title : path) {
				@SuppressWarnings("unchecked")
				Class<PanReportValidator> reportValidator = (Class<PanReportValidator>) reportInfo.getReportValidator();
				selectedPage = pilotageAN.getSelectedPage(title, reportValidator);
			}
			if (selectedPage != null) {
				selectedPage.validate(reportName);
			}
			getFlog().endAction();

		}

		logoutEpg();
	}

	public void validateDecret(int index) {

		TraitementPage traitementPage = loginAsAdminEpg();
		traitementPage.openCorbeille("Administrateur S.O.L.O.N. (SGG)");
		DossierDetailMenu dossierDetail = traitementPage.getDossierDetail(getDecretDossierId(index));

		ParapheurPage parapheurPage = dossierDetail.getPage(ParapheurPage.class);
		parapheurPage.verrouiller();
		parapheurPage.valider();
		// Logout
		logoutEpg();

	}

	public enum ReportInfo {

		// Application des lois
		ADL_A0(Arrays.asList("Application des lois", "Tableau-maître (Ministères)", MINISTERE_PARAM,
				"Mesures d'application par ministère"), true, ADL_A0.class), ADL_A1(Arrays
				.asList(AFFICHER_TOUTES_LES_MESURES), true, PanReportValidator.class), ADL_A2(Arrays
				.asList("Taux d'exécution des lois promulguées"), true, PanReportValidator.class), ADL_A3(Arrays
				.asList("Bilan semestriel par ministère"), true, PanReportValidator.class), ADL_A4(Arrays
				.asList("Bilan semestriel par ministère"), true, PanReportValidator.class), ADL_A5(Arrays
				.asList("Taux d'application au fil de l'eau par ministère"), true, PanReportValidator.class),

		ADL_B1(Arrays.asList("Tableaux de bord", "Mesures législatives appelant un décret"), true,
				PanReportValidator.class), ADL_B2(Arrays.asList(AFFICHER_TOUTES_LES_MESURES), true,
				PanReportValidator.class), ADL_B3(Arrays
				.asList("Mesures d'application pour lesquelles la date prévisionnelle de publication est dépassée"),
				true, PanReportValidator.class), ADL_B4(Arrays.asList(AFFICHER_TOUTES_LES_MESURES), true,
				PanReportValidator.class), ADL_B5(Arrays.asList("Mesures avec entrée en vigueur différée"), true,
				PanReportValidator.class),

		ADL_C1(Arrays.asList("Indicateurs LOLF", "Taux d'exécution des lois"), true, PanReportValidator.class), ADL_C2(
				Arrays.asList("Délai de mise en application des lois"), true, PanReportValidator.class), ADL_C3(Arrays
				.asList("Taux d'exécution des lois promulguées au cours de la dernière session parlementaire"), true,
				PanReportValidator.class),

		ADL_D1(Arrays.asList("Bilan Semestriel", "Par loi"), true, PanReportValidator.class), ADL_D2(Arrays
				.asList("Par ministère"), true, PanReportValidator.class), ADL_D3(Arrays.asList("Par nature de texte"),
				true, PanReportValidator.class), ADL_D4(Arrays.asList("Par procédure de vote"), true,
				PanReportValidator.class),

		ADL_E1(Arrays.asList("Taux d'application au fil de l'eau", "Global"), true, PanReportValidator.class), ADL_E2(
				Arrays.asList("Par loi"), true, PanReportValidator.class), ADL_E3(Arrays.asList("Par ministère"), true,
				PanReportValidator.class), ADL_E4(Arrays.asList("Par nature de texte"), true, PanReportValidator.class), ADL_E5(
				Arrays.asList("Par procédure de vote"), true, PanReportValidator.class),

		ADL_F1(Arrays.asList("Délais moyens", "Par loi"), true, PanReportValidator.class), ADL_F2(Arrays
				.asList("Par ministère"), true, PanReportValidator.class), ADL_F3(Arrays.asList("Par nature de texte"),
				true, PanReportValidator.class),

		// Transposition des directives
		TDD_A1(Arrays.asList("Transposition des directives", "Tableau-maître (Ministères)", MINISTERE_PARAM,
				"Suivi des travaux de transposition à la charge des ministères"), true, PanReportValidator.class), TDD_A2(
				Arrays.asList("Directives dont la transposition est achevée par ministère"), true,
				PanReportValidator.class),

		TDD_B1(Arrays.asList("Tableaux de bord", "Directives dont la transposition est achevée"), true,
				PanReportValidator.class), TDD_B2(Arrays.asList("Directives dont la transposition reste en cours"),
				true, PanReportValidator.class),

		TDD_C1(Arrays.asList("Statistiques", "Indicateurs de transposition"), true, PanReportValidator.class), TDD_C2(
				Arrays.asList("Retards moyens de transposition"), true, PanReportValidator.class), TDD_C3(Arrays
				.asList("Répartition des textes restant à prendre"), true, PanReportValidator.class),

		// Suivi des habilitations
		SDH_A1(Arrays.asList("Suivi des habilitations", "Tableau-maître (Ministères)", MINISTERE_PARAM,
				"Suivi par ministère des habilitations de la législature en cours"), true, PanReportValidator.class), SDH_A2(
				Arrays.asList(AFFICHER_LES_HABILITATIONS), true, PanReportValidator.class), SDH_A3(Arrays
				.asList("Suivi par ministère des habilitations"), true, PanReportValidator.class), SDH_A4(Arrays
				.asList(AFFICHER_LES_HABILITATIONS), true, PanReportValidator.class),

		SDH_B1(Arrays.asList("Tableaux de bord", "Suivi global des habilitations"), true, PanReportValidator.class), SDH_B2(
				Arrays.asList(AFFICHER_LES_HABILITATIONS), true, PanReportValidator.class),

		// Ratification des ordonnances
		RDO_A1(Arrays.asList("Ratification des ordonnances", "Tableau-maître (Ministères)", MINISTERE_PARAM,
				"Suivi par ministère de la ratification des ordonnances de l'article 38 C"), true,
				PanReportValidator.class), RDO_A2(Arrays.asList(AFFICHER_LES_ORDONNANCES), true,
				PanReportValidator.class), RDO_A3(Arrays
				.asList("Suivi par ministère de la ratification des ordonnances de l'article 74-1"), true,
				PanReportValidator.class), RDO_A4(Arrays.asList(AFFICHER_LES_ORDONNANCES), true,
				PanReportValidator.class),

		RDO_B1(Arrays.asList("Tableaux de bord",
				"Ordonnances de l'article 38 C dont le projet de loi de ratification n'a pas été déposé"), true,
				PanReportValidator.class), RDO_B2(Arrays
				.asList("Suivi global de la ratification des ordonnances de l'article 38 C"), true,
				PanReportValidator.class), RDO_B3(Arrays.asList(AFFICHER_LES_ORDONNANCES), true,
				PanReportValidator.class), RDO_B4(Arrays
				.asList("Suivi global de la ratification des ordonnances de l'article 74-1"), true,
				PanReportValidator.class), RDO_B5(Arrays.asList(AFFICHER_LES_ORDONNANCES), true,
				PanReportValidator.class), RDO_B6(Arrays
				.asList("Suivi des décrets pris en application des ordonnances"), true, PanReportValidator.class),

		// Traités et accords

		A1(Arrays.asList("Traités et accords", "Tableau-maître (Ministères)", MINISTERE_PARAM,
				"Tableaux ministériels des traités et accords dont la publication reste à intervenir"), true,
				PanReportValidator.class), A2(Arrays.asList("Tableaux de bord",
				"Tableau général des traités et accords dont la publication est intervenue"), true,
				PanReportValidator.class), A3(Arrays.asList("Tableaux de bord",
				"Tableau général des traités et accords dont la publication reste à intervenir"), true,
				PanReportValidator.class);

		private List<String>	path;

		private boolean			supportRefresh	= true;

		private Class<?>		reportValidator;

		private ReportInfo(List<String> path, boolean supportRefresh, Class<?> reportValidator) {
			this.path = path;
			this.supportRefresh = supportRefresh;
			this.reportValidator = reportValidator;
		}

		public List<String> getPath() {
			return path;
		}

		public boolean isSupportRefresh() {
			return supportRefresh;
		}

		public Class<?> getReportValidator() {
			return reportValidator;
		}
	}

	private String getEpgUrl() {
		return ((UrlEpgHelper) UrlEpgHelper.getInstance()).getEpgUrl();
	}
}
