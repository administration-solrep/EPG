package fr.dila.solonmgpp.webtest.webdriver010.dossier030;

import java.util.Calendar;

import fr.dila.solonepg.page.creation.CreationDossier100Page;
import fr.dila.solonepg.page.creation.CreationDossier108Page;
import fr.dila.solonepg.page.main.onglet.CreationPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.ParapheurPage;
import fr.dila.solonepp.page.CorbeillePage;
import fr.dila.solonepp.page.communication.lex.create.CreateComm03Page;
import fr.dila.solonepp.page.communication.lex.create.CreateComm12Page;
import fr.dila.solonepp.page.communication.lex.create.CreateComm13BisPage;
import fr.dila.solonepp.page.communication.lex.create.CreateComm21Page;
import fr.dila.solonepp.page.communication.lex.create.CreateComm22Page;
import fr.dila.solonepp.page.communication.lex.create.CreateComm24Page;
import fr.dila.solonepp.page.communication.lex.detail.DetailComm01Page;
import fr.dila.solonepp.page.communication.lex.detail.DetailComm10Page;
import fr.dila.solonepp.page.communication.lex.detail.DetailComm13BisPage;
import fr.dila.solonepp.page.communication.lex.detail.DetailComm13Page;
import fr.dila.solonepp.page.communication.lex.detail.DetailComm19Page;
import fr.dila.solonepp.page.communication.lex.detail.DetailComm21Page;
import fr.dila.solonepp.page.communication.lex.detail.DetailComm23Page;
import fr.dila.solonmgpp.creation.page.BordereauOnglet;
import fr.dila.solonmgpp.creation.page.FDROnglet;
import fr.dila.solonmgpp.page.PublicationPage;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm01Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm10Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm13Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm19Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm23Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm28Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm44Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm01Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm03Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm10Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm12Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm13BisPage;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm13Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm19Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm21Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm22Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm23Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm24Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm28Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm44Page;
import fr.dila.solonmgpp.page.epg.espaceEpg.MgppPage;
import fr.dila.solonmgpp.page.epg.espaceEpg.RapportPage;
import fr.dila.solonmgpp.page.epg.espaceEpg.TraitementPageForMGGP;
import fr.dila.solonmgpp.page.epg.mgpp.procedureLegislative.FicheLoiOnglet;
import fr.dila.solonmgpp.page.epg.mgpp.procedureLegislative.HistoriqueEppOnglet;
import fr.dila.solonmgpp.page.epg.mgpp.procedureLegislative.ProcedureLegislativePage;
import fr.dila.solonmgpp.page.epg.mgpp.rapport.DepotDeRapportPage;
import fr.dila.solonmgpp.utils.DateUtils;
import fr.dila.solonmgpp.webtest.common.AbstractMgppWebTest;
import fr.dila.solonmgpp.webtest.helper.url.UrlEpgHelper;
import fr.dila.st.annotations.TestDocumentation;
import fr.dila.st.webdriver.model.CommonWebPage.By_enum;
import fr.dila.st.webdriver.model.CommonWebPage.Mode;
import fr.dila.st.webdriver.ws.WsResultPage;
import fr.sword.naiad.commons.webtest.annotation.WebTest;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.NiveauLectureCode;

public class TestSuite extends AbstractMgppWebTest {

	public static final String	DOSSIER_ATTACH_ACTE_INTEGRAL	= "doc2003.doc";
	private static final String	DOSSIER_ID						= getDossierId();
	private static final String	NUMERO_NOR						= getDossierId();
	final String[]				POSTE_AGENT_BDC_ECONOMIE		= {
			"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0::min:handle:img:collapsed",
			"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_tree:node:0:node:0:node:6::nxw_direction_resp_field_addDir" };

	private static String getDossierId() {
		String dossierId_prefix = "CCOZ";
		String dossierId_suffix = "00000L";
		String dossierId_year = "15";

		try {
			dossierId_year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2);
		} catch (Exception e) {
		}

		return dossierId_prefix + dossierId_year + dossierId_suffix;
	}

	/**
	 * Initialisation loi
	 * 
	 */
	@WebTest(description = "Initialisation loi")
	@TestDocumentation(categories = { "Dossier", "Parapheur", "FDD", "Bordereau", "FDR", "Comm. LEX-01" })
	public void initialisationLoi() {

		TraitementPageForMGGP traitementPage = loginAsAdminEpg();
		traitementPage.waitForPageLoaded(getDriver());
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();

		CreationDossier100Page createDossierPage = creationPage.createDossier();
		createDossierPage.setTypeActe("Loi");
		CreationDossier108Page creationDossier108Page = createDossierPage
				.appuyerBoutonSuivant(CreationDossier108Page.class);
		ParapheurPage parapheurPage = creationDossier108Page.appuyerBoutonTerminer(ParapheurPage.class);

		creationPage.waitForPageLoaded(getDriver());

		parapheurPage.verrouiller();
		parapheurPage.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']",
				DOSSIER_ATTACH_ACTE_INTEGRAL);
		// creationPage.ajouterDocuments();

		// Remplissage du FDD
		ParapheurPage.sleep(2); // on a parfois une clipboardAction exception
		parapheurPage.waitForPageSourcePart("Répertoire réservé au ministère porteur", TIMEOUT_SEC);
		parapheurPage.ajouterDocumentsFDD();

		// Remplissage du Bordereau
		BordereauOnglet bordereauOnglet = creationPage.navigateToAnotherTab("Bordereau", BordereauOnglet.class);
		final String titre_acte_label_to_set = "Projet de loi Test MGPP";
		final String nom_resp_dossier_label_to_set = "Nom responsable mgpp";
		final String qualite_resp_dossier_label_to_set = "Qualité responsable mgpp";
		final String tel_resp_dossier_label_to_set = "0123456789";
		final String rubriques_label_to_set = "Action sociale, santé, sécurité sociale";
		bordereauOnglet.initialize(titre_acte_label_to_set, nom_resp_dossier_label_to_set,
				qualite_resp_dossier_label_to_set, tel_resp_dossier_label_to_set, rubriques_label_to_set);

		FDROnglet fdrOnglet = creationPage.navigateToAnotherTab("Feuille de route", FDROnglet.class);
		fdrOnglet.initialize();

		creationPage.lancerDossier();

		traitementPage = creationPage.navigateToAnotherTab("Traitement", TraitementPageForMGGP.class);

		traitementPage.openCorbeille("Administrateurs SOLON (1)");

		// on a validé l'étape pour transmission aux assemblées, elle est en cours.
		// Un évènement asynchrone de création de communication en brouillon est lancé par la suite.
		// On laisse un peu de temps pour que cet évènement puisse être lancé pour que le test suivant puisse trouver la
		// communication
		TraitementPageForMGGP.sleep(120);

		logoutEpg();
	}

	/**
	 * Publication Comm EVT01
	 * 
	 */
	@WebTest(description = "Publication Comm EVT01")
	@TestDocumentation(categories = { "Comm. LEX-01", "Corbeille MGPP" })
	public void publicationEvt01() {

		final String OBJET = "Test MGPP";
		final String intitule = "Projet de loi Test MGPP";
		final String destinataire = "Sénat";
		final Integer niveauLecture = 1;
		final NiveauLectureCode organisme = NiveauLectureCode.SENAT;
		final String auteur = "fill";
		final String coauteur = "fill";

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertCorbeilles(1, 0, 1, 0);

		procLegisPage.openCorbeille("Emis (1)");
		procLegisPage.openRecordFromList(DOSSIER_ID);

		final MgppCreateComm01Page createComm01Page = procLegisPage.navigateToCreateComm(MgppCreateComm01Page.class);
		// Remplissage des champs de la page de création et publication de la
		// communication
		MgppDetailComm01Page detailComm01Page = createComm01Page.createComm01(destinataire, OBJET, NUMERO_NOR, auteur,
				coauteur, niveauLecture, organisme, intitule);
		detailComm01Page.assertTrue("Emis (1)");
		detailComm01Page.openRecordFromList(DOSSIER_ID);
		detailComm01Page.assertTrue("Transmis le");

		logoutEpg();
	}

	/**
	 * Navigation Communication CCOZ1600000L, creee a partir de initialisationLoi
	 * 
	 */
	@WebTest(description = "Navigation Comm CCOZ1600000L.")
	@TestDocumentation(categories = { "Comm. LEX-01", "Fiche loi" })
	public void testCase004() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertCorbeilles(1, 0, 1, 0);
		procLegisPage.openCorbeille("Emis");

		procLegisPage.assertTrue("1 - Sénat");
		procLegisPage.assertTrue("Gouvernement");
		procLegisPage.assertTrue(DOSSIER_ID + "_00000");
		procLegisPage.assertTrue("LEX-01 : Pjl - Dépôt");

		MgppDetailComm01Page detailComm01Page = procLegisPage
				.openRecordFromList(DOSSIER_ID, MgppDetailComm01Page.class);

		detailComm01Page.assertTrue("Fiche Loi");
		detailComm01Page.assertTrue(DOSSIER_ID + "_00000");

		detailComm01Page.checkValueContain("metadonnee_dossier:nxl_fiche_loi:nxw_fiche_loi_intitule",
				"Projet de loi Test MGPP");
		// "Projet de loi Test MGPP");
		detailComm01Page.checkValueContain("metadonnee_dossier:nxl_fiche_loi:nxw_fiche_loi_idDossier", DOSSIER_ID);

		detailComm01Page.assertTrue("Date limite de promulgation");
		detailComm01Page.assertTrue("Transmis le");
		detailComm01Page.assertTrue("1 - Sénat");

		detailComm01Page.verouiller_par_alt();
		detailComm01Page.deverouiller_par_alt();
		detailComm01Page.verouiller_fiche_par_alt();

		detailComm01Page.checkElementPresent(By_enum.XPATH, "//*[@value='Diffuser']", Mode.EDIT);
		detailComm01Page.checkElementPresent(By_enum.XPATH, "//*[@value='Enregistrer']", Mode.EDIT);
		detailComm01Page.checkElementPresent(By_enum.XPATH, "//*[@value='Générer courrier']", Mode.EDIT);
		detailComm01Page.checkElementPresent(By_enum.XPATH, "//*[@value='Créer alerte']", Mode.EDIT);

		FicheLoiOnglet ficheLoi = procLegisPage.navigateToAnotherTab("Fiche Loi", FicheLoiOnglet.class);
		String observation = "Observation " + DOSSIER_ID;
		ficheLoi.setObservationAndSave(observation);

		ficheLoi.deverouiller_fiche_par_alt();

	}

	/**
	 * Traitement Communication Evt01 senat
	 * 
	 */
	@WebTest(description = "Traitement Communication Evt01 senat")
	@TestDocumentation(categories = { "Comm. LEX-01", "Comm. LEX-03", "Corbeille EPP", "Action 'Traiter' EPP" })
	public void testCase005() {

		CorbeillePage corbeillePage = loginEpp("senat", "senat");

		corbeillePage.openCorbeille("Destinataire");
		corbeillePage.waitForPageLoaded(getDriver());

		corbeillePage.assertTrue(DOSSIER_ID);

		DetailComm01Page eppDetailComm01Page = corbeillePage.openRecordFromList(DOSSIER_ID, DetailComm01Page.class);
		eppDetailComm01Page.waitForPageLoaded(getDriver());
		eppDetailComm01Page.traite(DetailComm01Page.class);

		final CreateComm03Page eppCreationCommEVT03Page = eppDetailComm01Page.navigateToCreateCommSucc(
				EvenementType.EVT_03, CreateComm03Page.class);
		eppCreationCommEVT03Page.createComm03(DateUtils.format(Calendar.getInstance()), "2012-04");
		logoutEpp();
	}

	/**
	 * Traitement Communication Evt03 Gouv
	 * 
	 */
	@WebTest(description = "Traitement Communication Evt03 Gouv")
	@TestDocumentation(categories = { "Comm. LEX-03", "Action 'Traiter' MGPP", "Corbeille MGPP" })
	public void testCase006_1() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();

		procLegisPage.assertTrue("Procédure législative (1)");
		procLegisPage.assertTrue("Reçu (1)");
		procLegisPage.openCorbeille("Reçu (1)");

		procLegisPage.assertTrue(DOSSIER_ID + "_00001");
		MgppDetailComm03Page detailCommMgpp03Page = procLegisPage.openRecordFromList(DOSSIER_ID,
				MgppDetailComm03Page.class);
		detailCommMgpp03Page.traiter(ProcedureLegislativePage.class);

	}

	@WebTest(description = "Traitement Communication Evt03 Gouv")
	@TestDocumentation(categories = { "Comm. LEX-03", "Corbeille MGPP", "Comm. LEX-14" })
	public void testCase006_2() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();

		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.openCorbeille("Historique (2)");

		// click and wait on "Identifiant dossier"
		MgppDetailComm03Page mgppDetailComm03Page = procLegisPage.openRecordFromList(DOSSIER_ID,
				MgppDetailComm03Page.class);
		mgppDetailComm03Page.assertTrue(DOSSIER_ID + "_00001");

		// Vérification Dépôt
		mgppDetailComm03Page.checkValue(
				"metadonnee_dossier:nxl_fiche_loi_depot:nxw_fiche_loi_assembleeDepot_global_region", "Sénat");
		mgppDetailComm03Page.checkValue("metadonnee_dossier:nxl_fiche_loi_depot:nxw_fiche_loi_numeroDepot", "2012-04");
		mgppDetailComm03Page.checkValue("metadonnee_dossier:nxl_fiche_loi_depot:nxw_fiche_loi_dateDepot",
				DateUtils.format(Calendar.getInstance()));

		// Creation comm EVT10 successive
		MgppCreateComm10Page mgppCreateComm10Page = mgppDetailComm03Page.navigateToCreateCommSucc(EvenementType.EVT_10,
				MgppCreateComm10Page.class);
		// Vérification destinataire = Sénat + copie = Assemblée nationnale

		final Integer niveauLecture = 1;
		final NiveauLectureCode organisme = NiveauLectureCode.SENAT;
		final String titre = "PJPMPour" + DOSSIER_ID;
		final String url = "http://PJPMPour" + DOSSIER_ID + "@solon-epg.fr";
		final String pj = "src/main/attachments/doc2009.doc";

		MgppDetailComm10Page mgppDetailComm10Page = mgppCreateComm10Page.createComm10(niveauLecture, organisme, titre,
				url, pj);

		mgppDetailComm10Page.assertTrue("Emis (1)");
		mgppDetailComm10Page.openCorbeille("Emis (1)");
		mgppDetailComm10Page.openRecordFromList(DOSSIER_ID);

		logoutEpg();

	}

	/**
	 * Traitement Communication Evt10 Gouv
	 * 
	 */
	@WebTest(description = "Traitement Communication Evt10 Gouv")
	@TestDocumentation(categories = { "Comm. LEX-14", "Corbeille EPP", "Comm. LEX-16", "Action 'Traiter' EPP" })
	public void testCase007() {

		CorbeillePage corbeillePage = loginEpp("senat", "senat");

		// Traitemennt comm EVT10 du Gouvernement
		corbeillePage.waitForPageLoaded(getDriver());
		corbeillePage.openCorbeille("Destinataire");
		corbeillePage.assertTrue(DOSSIER_ID);

		DetailComm10Page eppDetailComm10Page = corbeillePage.openRecordFromList(DOSSIER_ID, DetailComm10Page.class);
		eppDetailComm10Page.traite(DetailComm10Page.class);

		// Creation comm EVT12 successive
		CreateComm12Page createComm12Page = eppDetailComm10Page.navigateToCreateCommSucc(EvenementType.EVT_12,
				CreateComm12Page.class);

		final String numeroTextAdopte = "2012-04";
		final String dateAdoptionInputDate = DateUtils.format(Calendar.getInstance());
		final String sortAdoption_label = "Adopté";
		createComm12Page.createComm12(numeroTextAdopte, dateAdoptionInputDate, sortAdoption_label);

		logoutEpp();
	}

	/**
	 * Traitement Communication Evt12 Gouv
	 * 
	 */
	@WebTest(description = "Traitement Communication Evt12 Gouv")
	@TestDocumentation(categories = { "Comm. LEX-16", "Action 'Traiter' MGPP", "Corbeille MGPP" })
	public void testCase008_1() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();

		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertTrue("Procédure législative (1)");
		procLegisPage.assertTrue("Reçu (1)");

		procLegisPage.openCorbeille("Reçu (1)");
		procLegisPage.assertTrue(DOSSIER_ID + "_00003");

		MgppDetailComm12Page detailCommMgpp12Page = procLegisPage.openRecordFromList(DOSSIER_ID,
				MgppDetailComm12Page.class);
		detailCommMgpp12Page.traiter(ProcedureLegislativePage.class);

		logoutEpg();

	}

	@WebTest(description = "Traitement Communication Evt12 Gouv")
	@TestDocumentation(categories = { "Comm. LEX-16", "Comm. LEX-17", "Corbeille MGPP" })
	public void testCase008_2() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();

		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.openCorbeille("Historique (4)");

		MgppDetailComm12Page detailComm12Page = procLegisPage
				.openRecordFromList(DOSSIER_ID, MgppDetailComm12Page.class);
		detailComm12Page.assertTrue(DOSSIER_ID + "_00003");

		// Creation comm EVT13 successive pour AN
		MgppCreateComm13Page createComm13Page = detailComm12Page.navigateToCreateCommSucc(EvenementType.EVT_13,
				MgppCreateComm13Page.class);

		final String destinataire = "Assemblée nationale";
		final Integer niveauLecture = 1;
		final NiveauLectureCode organisme = NiveauLectureCode.AN;
		final String dateAdoptionInputDate = DateUtils.format(Calendar.getInstance());
		final String numeroTexteAdopte = "2012-04";
		MgppDetailComm13Page detailComm13Page = createComm13Page.createComm13(destinataire, niveauLecture, organisme,
				dateAdoptionInputDate, numeroTexteAdopte);

		detailComm13Page.assertTrue("Emis (1)");
		detailComm13Page.openCorbeille("Emis (1)");
		detailComm13Page.openRecordFromList(DOSSIER_ID);

		// Verification Piece Jointe
		detailComm13Page.assertTrue("PJLPour" + DOSSIER_ID);
		detailComm13Page.assertTrue("PJTTPour" + DOSSIER_ID);

		logoutEpg();

	}

	/**
	 * Traitement Communication Evt13 AN
	 * 
	 */
	@WebTest(description = "Traitement Communication Evt13 AN")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. LEX-17", "Comm. LEX-18", "Action 'Traiter' EPP" })
	public void testCase009() {

		CorbeillePage corbeillePage = loginEpp("an", "an");

		// Traitement comm EVT13 du Gouvernement
		corbeillePage.openCorbeille("Toutes les communications");

		corbeillePage.assertTrue(DOSSIER_ID);

		DetailComm13Page detailComm13Page = corbeillePage.openRecordFromList(DOSSIER_ID, DetailComm13Page.class);
		detailComm13Page.traite(DetailComm13Page.class);

		// Creation comm EVT13Bis successive
		CreateComm13BisPage createComm13BisPage = detailComm13Page.navigateToCreateCommSucc(EvenementType.EVT_13_BIS,
				CreateComm13BisPage.class);

		final String numeroDepotTexte = "2012-04";
		final String dateDepotTextInputDate = DateUtils.format(Calendar.getInstance());
		createComm13BisPage.createComm13Bis(numeroDepotTexte, dateDepotTextInputDate);

		logoutEpp();
	}

	/**
	 * Traitement Evt13 Bis Gouvernement
	 * 
	 */
	@WebTest(description = "Traitement Evt13 Bis Gouvernement")
	@TestDocumentation(categories = { "Corbeille MGPP", "Action 'Traiter' MGPP", "Comm. LEX-18" })
	public void testCase010() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertCorbeilles(1, 1, 0, 5);

		procLegisPage.openCorbeille("Reçu (1)");
		MgppDetailComm13BisPage detailComm13BisPage = procLegisPage.openRecordFromList(DOSSIER_ID,
				MgppDetailComm13BisPage.class);
		detailComm13BisPage.traiter(ProcedureLegislativePage.class);

		logoutEpg();
	}

	/**
	 * Creation Evt12 AN
	 * 
	 */
	@WebTest(description = "Creation Evt12 AN")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. LEX-18", "Comm. LEX-16" })
	public void testCase011() {

		CorbeillePage corbeillePage = loginEpp("an", "an");

		// Creation Comm 12 successive de 13bis
		corbeillePage.waitForPageLoaded(getDriver());
		corbeillePage.openCorbeille("Toutes les communications");
		corbeillePage.assertTrue(DOSSIER_ID);

		DetailComm13BisPage detailComm13BisPage = corbeillePage.openRecordFromList(DOSSIER_ID,
				DetailComm13BisPage.class);
		CreateComm12Page createComm12Page = detailComm13BisPage.navigateToCreateCommSucc(EvenementType.EVT_12,
				CreateComm12Page.class);

		final String numeroTextAdopte = "2012-04";
		final String dateAdoptionInputDate = DateUtils.format(Calendar.getInstance());
		final String sortAdoption_label = "Adopté";
		createComm12Page.checkIdentifiantPrecedent(DOSSIER_ID + "_00005");
		createComm12Page.createComm12(numeroTextAdopte, dateAdoptionInputDate, sortAdoption_label);

		logoutEpp();

	}

	/**
	 * Traitement EVT12 Gouvernement
	 * 
	 */
	@WebTest(description = "Traitement EVT12 Gouvernement")
	@TestDocumentation(categories = { "Corbeille MGPP", "Action 'Traiter' MGPP", "Comm. LEX-16" })
	public void testCase012_1() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertCorbeilles(1, 1, 0, 6);

		procLegisPage.openCorbeille("Reçu (1)");

		MgppDetailComm12Page detailCommMgpp12Page = procLegisPage.openRecordFromList(DOSSIER_ID,
				MgppDetailComm12Page.class);
		detailCommMgpp12Page.traiter(ProcedureLegislativePage.class);

		logoutEpg();

	}

	@WebTest(description = "Traitement EVT12 Gouvernement")
	@TestDocumentation(categories = { "Corbeille MGPP", "Comm. LEX-16", "Comm. LEX-17" })
	public void testCase012_2() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();

		// Creation comm EVT13 successive pour Senat
		procLegisPage.assertCorbeilles(0, 0, 0, 7);

		procLegisPage.openCorbeille("Historique (7)");

		MgppDetailComm12Page detailComm12Page = procLegisPage
				.openRecordFromList(DOSSIER_ID, MgppDetailComm12Page.class);
		detailComm12Page.assertTrue(DOSSIER_ID + "_00006");
		MgppCreateComm13Page createComm13Page = detailComm12Page.navigateToCreateCommSucc(EvenementType.EVT_13,
				MgppCreateComm13Page.class);

		final String destinataire = "Sénat";
		final Integer niveauLecture = 2;
		final NiveauLectureCode organisme = NiveauLectureCode.SENAT;
		MgppDetailComm13Page detailComm13Page = createComm13Page.createComm13(destinataire, niveauLecture, organisme,
				null, null);

		detailComm13Page.assertTrue("Emis (1)");
		detailComm13Page.openCorbeille("Emis (1)");
		detailComm13Page.openRecordFromList(DOSSIER_ID);

		// Verification Piece Jointe
		detailComm13Page.assertTrue("PJLPour" + DOSSIER_ID);
		detailComm13Page.assertTrue("PJTTPour" + DOSSIER_ID);

		logoutEpg();

	}

	/**
	 * Traitement Comm EVT13 Senat
	 * 
	 */
	@WebTest(description = "Traitement Comm EVT13 Senat")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. LEX-17", "Action 'Traiter' EPP" })
	public void testCase013() {

		CorbeillePage corbeillePage = loginEpp("senat", "senat");

		// Traitemennt comm EVT13 du Gouvernement
		corbeillePage.waitForPageLoaded(getDriver());
		corbeillePage.openCorbeille("Destinataire");
		corbeillePage.assertTrue(DOSSIER_ID);
		DetailComm13Page detailComm13Page = corbeillePage.openRecordFromList(DOSSIER_ID, DetailComm13Page.class);
		detailComm13Page.traite(DetailComm13Page.class);

		logoutEpp();

	}

	/**
	 * verification CCOZ1400000L
	 * 
	 */
	@WebTest(description = "verification CCOZ1400000L")
	@TestDocumentation(categories = { "Corbeille MGPP", "Comm. LEX-17", "Navettes", "Fiche loi", "Historique EPP" })
	public void testCase014() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertCorbeilles(0, 0, 0, 8);

		procLegisPage.openCorbeille("Historique (8)");
		MgppDetailComm13Page detailComm13Page = procLegisPage
				.openRecordFromList(DOSSIER_ID, MgppDetailComm13Page.class);

		// Verification Navettes
		// 2 - SENAT
		// detailComm13Page.assertNavetteSenat2();
		// // 1 - AN
		// detailComm13Page.assertNavetteAn1();
		// // 1 - SENAT
		// detailComm13Page.assertNavetteSenat1();

		// Verification Historique EPP
		HistoriqueEppOnglet historiqueEpp = detailComm13Page.navigateToAnotherTab("Historique EPP",
				HistoriqueEppOnglet.class);
		historiqueEpp.checkAllValues(DOSSIER_ID);

		logoutEpg();
	}

	/**
	 * Creation EVT19
	 * 
	 */
	@WebTest(description = "Creation EVT19")
	@TestDocumentation(categories = { "Corbeille MGPP", "Comm. LEX-17", "Comm. LEX-22" })
	public void testCase015_1() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertCorbeilles(0, 0, 0, 8);
		procLegisPage.openCorbeille("Historique (8)");

		MgppDetailComm13Page detailComm13Page = procLegisPage
				.openRecordFromList(DOSSIER_ID, MgppDetailComm13Page.class);
		detailComm13Page.assertTrue(DOSSIER_ID + "_00007");

		// Creation comm EVT19 successive a CCOZ1400000L_00007 AN
		MgppCreateComm19Page createComm19Page = detailComm13Page.navigateToCreateCommSucc(EvenementType.EVT_19,
				MgppCreateComm19Page.class);

		String destinataire = "Assemblée nationale";
		MgppDetailComm19Page detailComm19Page = createComm19Page.createComm19(destinataire);
		detailComm19Page.assertTrue("Emis (1)");

		logoutEpg();
	}

	/**
	 * Creation EVT19 Suite
	 * 
	 */
	@WebTest(description = "Creation EVT19 part 2")
	@TestDocumentation(categories = { "Corbeille MGPP", "Comm. LEX-17", "Comm. LEX-22" })
	public void testCase015_2() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.openCorbeille("Emis (1)");

		procLegisPage.openRecordFromList(DOSSIER_ID);
		procLegisPage.waitForPageLoaded(getDriver());

		procLegisPage.openCorbeille("Historique (8)");
		MgppDetailComm13Page detailComm13Page = procLegisPage
				.openRecordFromList(DOSSIER_ID, MgppDetailComm13Page.class);
		detailComm13Page.waitForPageLoaded(getDriver());

		// Creation comm EVT19 successive a CCOZ1400000L_00007 SENAT
		detailComm13Page.assertTrue(DOSSIER_ID + "_00007");

		MgppCreateComm19Page createComm19Page = detailComm13Page.navigateToCreateCommSucc(EvenementType.EVT_19,
				MgppCreateComm19Page.class);

		String destinataire = "Sénat";
		MgppDetailComm19Page detailComm19Page = createComm19Page.createComm19(destinataire);

		detailComm19Page.assertTrue("Emis (2)");

		logoutEpg();
	}

	/**
	 * Traitement EVT19 Senat
	 * 
	 */
	@WebTest(description = "Traitement EVT19 Senat")
	@TestDocumentation(categories = { "Corbeille EPP", "Action 'Traiter' EPP", "Comm. LEX-22" })
	public void testCase016() {

		CorbeillePage corbeillePage = loginEpp("senat", "senat");

		// Traitemennt comm EVT19 du Gouvernement
		corbeillePage.waitForPageLoaded(getDriver());
		corbeillePage.openCorbeille("Destinataire");

		corbeillePage.assertTrue(DOSSIER_ID);
		DetailComm19Page detailComm19Page = corbeillePage.openRecordFromList(DOSSIER_ID, DetailComm19Page.class);
		detailComm19Page.traite(DetailComm19Page.class);

		logoutEpp();
	}

	/**
	 * Traitement EVT19 AN
	 * 
	 */
	@WebTest(description = "Traitement EVT19 AN")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. LEX-22", "Comm. LEX-24", "Action 'Traiter' EPP" })
	public void testCase017() {

		CorbeillePage corbeillePage = loginEpp("an", "an");

		// Traitemennt comm EVT19 du Gouvernement
		corbeillePage.waitForPageLoaded(getDriver());
		corbeillePage.deployFirstElementCorbeille();
		corbeillePage.openCorbeille("Administration LEGIS");

		corbeillePage.assertTrue(DOSSIER_ID);

		DetailComm19Page detailComm19Page = corbeillePage.openRecordFromList(DOSSIER_ID, DetailComm19Page.class);
		detailComm19Page.traite(DetailComm19Page.class);

		// Creation comm EVT21
		CreateComm21Page createComm21Page = detailComm19Page.navigateToCreateCommSucc(EvenementType.EVT_21,
				CreateComm21Page.class);

		String inputInputDate1 = DateUtils.format(Calendar.getInstance());
		// String inputInputDate2 = "01/01/2012";
		createComm21Page.createComm21(inputInputDate1, null, null, null, null);

		logoutEpp();

	}

	/**
	 * Traitement EVT21 Gouvernement
	 * 
	 */
	@WebTest(description = "Traitement EVT21 Gouvernement")
	@TestDocumentation(categories = { "Comm. LEX-24", "Corbeille MGPP", "Action 'Traiter' MGPP" })
	public void testCase018() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		// procLegisPage.assertCorbeilles(1, 1, 0, 10);
		procLegisPage.assertCorbeilles(2, 1, 1, 9);
		procLegisPage.openCorbeille("Reçu (1)");

		MgppDetailComm21Page detailComm21Page = procLegisPage
				.openRecordFromList(DOSSIER_ID, MgppDetailComm21Page.class);

		// Vérification navette
		// detailComm21Page.verifierNavette();
		// Traitement
		detailComm21Page.traiter(ProcedureLegislativePage.class);

		logoutEpg();
	}

	/**
	 * Creation Comm EVT22 AN
	 * 
	 */
	@WebTest(description = "Creation Comm EVT22 AN")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. LEX-24", "Comm. LEX-25" })
	public void testCase019() {

		CorbeillePage corbeillePage = loginEpp("an", "an");

		corbeillePage.waitForPageLoaded(getDriver());
		// Traitemennt comm EVT19 du Gouvernement
		corbeillePage.deployFirstElementCorbeille();
		CorbeillePage.sleep(2);
		corbeillePage.openCorbeille("Séance envoi");

		corbeillePage.assertTrue(DOSSIER_ID);

		// getDriver().findElement(By.xpath("//*[@id=&quot;corbeille_message_list&quot;]/div/table/tbody/tr/td[5]/div/a")).click();
		DetailComm21Page detailComm21Page = corbeillePage.openRecordFromList(DOSSIER_ID, DetailComm21Page.class);

		CreateComm22Page createComm22Page = detailComm21Page.navigateToCreateCommSucc(EvenementType.EVT_22,
				CreateComm22Page.class);
		final String dossier_id = DOSSIER_ID + "_00010";
		final String dateDepotTexteInputDate = DateUtils.format(Calendar.getInstance());
		final String resultat_cmp_label = "Accord";

		createComm22Page.createComm22(dossier_id, dateDepotTexteInputDate, resultat_cmp_label);

		logoutEpp();
	}

	/**
	 * Traitement EVT22 Gouv
	 * 
	 */
	@WebTest(description = "Traitement EVT22 Gouv")
	@TestDocumentation(categories = { "Corbeille MGPP", "Comm. LEX-25", "Navettes", "Action 'Traiter' MGPP" })
	public void testCase020_1() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertCorbeilles(2, 1, 1, 10);
		procLegisPage.openCorbeille("Reçu (1)");

		MgppDetailComm22Page detailComm22Page = procLegisPage
				.openRecordFromList(DOSSIER_ID, MgppDetailComm22Page.class);

		// detailComm22Page.verifierNavette_cmp();

		detailComm22Page.traiter(ProcedureLegislativePage.class);
		logoutEpg();

	}

	@WebTest(description = "Traitement EVT22 Gouv")
	@TestDocumentation(categories = { "Corbeille MGPP", "Comm. LEX-25", "Comm. LEX-26" })
	public void testCase020_2() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		// Création EVT 23
		procLegisPage.openCorbeille("Historique (11)");
		MgppDetailComm01Page detailComm01Page = procLegisPage
				.openRecordFromList(DOSSIER_ID, MgppDetailComm01Page.class);
		detailComm01Page.assertTrue(DOSSIER_ID + "_00011");
		// Resultat CMP AN
		detailComm01Page.assertTrue("Accord");
		MgppCreateComm23Page createComm23Page = detailComm01Page.navigateToCreateCommSucc(EvenementType.EVT_23,
				MgppCreateComm23Page.class);

		final String destinataire = "Assemblée nationale";

		MgppDetailComm23Page detailComm23Page = createComm23Page.createComm23(destinataire);

		procLegisPage = detailComm23Page.getPage(ProcedureLegislativePage.class);
		procLegisPage.assertCorbeilles(2, 0, 2, 11);

		logoutEpg();
	}

	/**
	 * Traitement Comm EVT23 AN
	 * 
	 */
	@WebTest(description = "Traitement Comm EVT23 AN")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. LEX-26", "Action 'Traiter' EPP", "Comm. LEX-16" })
	public void testCase021() {

		CorbeillePage corbeillePage = loginEpp("an", "an");
		corbeillePage.waitForPageLoaded(getDriver());
		// Traitemennt comm EVT23 du Gouvernement
		corbeillePage.deployFirstElementCorbeille();
		// CorbeillePage.sleep(2);
		corbeillePage.waitForPageSourcePart("Séance réception", 5);
		corbeillePage.openCorbeille("Séance réception");

		corbeillePage.assertTrue(DOSSIER_ID);
		corbeillePage.assertTrue("LEX-26 : CMP - Demande de lecture des conclusions");

		DetailComm23Page eppDetailComm23Page = corbeillePage.openRecordFromList(DOSSIER_ID, DetailComm23Page.class);
		eppDetailComm23Page.traite(DetailComm23Page.class);

		// Creation comm EVT12
		CreateComm12Page createComm12Page = eppDetailComm23Page.navigateToCreateCommSucc(EvenementType.EVT_12,
				CreateComm12Page.class);

		final String numeroTextAdopte = "2012-04";
		final String dateAdoptionInputDate = DateUtils.format(Calendar.getInstance());
		final String sortAdoption_label = "Adopté";
		createComm12Page.createComm12(numeroTextAdopte, dateAdoptionInputDate, sortAdoption_label);

		logoutEpp();
	}

	/**
	 * Traitement EVT12 GVT
	 * 
	 */
	@WebTest(description = "Traitement EVT12 GVT")
	@TestDocumentation(categories = { "Comm. LEX-16", "Corbeille MGPP", "Navettes", "Action 'Traiter' MGPP" })
	public void testCase022() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertCorbeilles(2, 1, 1, 12);
		procLegisPage.openCorbeille("Reçu (1)");
		MgppDetailComm12Page mgppDetailComm12Page = procLegisPage.openRecordFromList(DOSSIER_ID,
				MgppDetailComm12Page.class);

		// Vérification navette
		// mgppDetailComm12Page.verifierNavette();
		// Traitement
		mgppDetailComm12Page.traiter(ProcedureLegislativePage.class);

		logoutEpg();
	}

	/**
	 * Traitement Comm EVT21 SE
	 * 
	 */
	@WebTest(description = "Traitement Comm EVT21 SE")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. LEX-22", "Comm. LEX-24" })
	public void testCase023() {

		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		corbeillePage.waitForPageLoaded(getDriver());
		// Traitement comm EVT19 du Gouvernement
		corbeillePage.openCorbeille("Destinataire");
		corbeillePage.assertTrue(DOSSIER_ID);
		DetailComm19Page eppDetailComm19Page = corbeillePage.openRecordFromList(DOSSIER_ID, DetailComm19Page.class);

		// Creation comm EVT21
		CreateComm21Page createComm21Page = eppDetailComm19Page.navigateToCreateCommSucc(EvenementType.EVT_21,
				CreateComm21Page.class);

		String inputInputDate1 = DateUtils.format(Calendar.getInstance());
		String emetteur_global_region_value = "Sénat";
		String destinataire_global_region_value = "Gouvernement";
		String copie_global_region_value = "Assemblée nationale";
		createComm21Page.createComm21(inputInputDate1, null, emetteur_global_region_value,
				destinataire_global_region_value, copie_global_region_value);

		logoutEpp();
	}

	/**
	 * Traitement EVT21SE GVT
	 * 
	 */
	@WebTest(description = "Traitement EVT21SE GVT")
	@TestDocumentation(categories = { "Corbeille MGPP", "Comm. LEX-24", "Action 'Traiter' MGPP" })
	public void testCase024() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertCorbeilles(2, 1, 1, 13);
		procLegisPage.openCorbeille("Reçu (1)");
		MgppDetailComm21Page mgppDetailComm21Page = procLegisPage.openRecordFromList(DOSSIER_ID,
				MgppDetailComm21Page.class);

		// mgppDetailComm21Page.verifierNavette_cmp();
		// Traitement
		mgppDetailComm21Page.traiter(ProcedureLegislativePage.class);

		logoutEpg();
	}

	/**
	 * Creation Comm EVT22 SE
	 * 
	 */
	@WebTest(description = "Creation Comm EVT22 SE")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. LEX-24", "Comm. LEX-25" })
	public void testCase025() {

		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		corbeillePage.waitForPageLoaded(getDriver());
		corbeillePage.openCorbeille("Émetteur");
		corbeillePage.assertTrue(DOSSIER_ID);
		corbeillePage.assertTrue("LEX-24 : CMP - Convocation");
		DetailComm21Page eppDetailComm21Page = corbeillePage.openRecordFromList(DOSSIER_ID, DetailComm21Page.class);

		CreateComm22Page createComm22Page = eppDetailComm21Page.navigateToCreateCommSucc(EvenementType.EVT_22,
				CreateComm22Page.class);
		final String dossier_id = DOSSIER_ID + "_00014";
		final String dateDepotTexteInputDate = DateUtils.format(Calendar.getInstance());
		final String resultat_cmp_label = "Accord";

		createComm22Page.createComm22(dossier_id, dateDepotTexteInputDate, resultat_cmp_label);

		logoutEpp();
	}

	/**
	 * Traitement EVT22 GVT
	 * 
	 */
	@WebTest(description = "Traitement EVT22 GVT")
	@TestDocumentation(categories = { "Corbeille MGPP", "Comm. LEX-25", "Action 'Traiter' MGPP", "Navettes" })
	public void testCase026_1() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertCorbeilles(2, 1, 1, 14);
		procLegisPage.openCorbeille("Reçu (1)");

		MgppDetailComm22Page detailCommMgpp22Page = procLegisPage.openRecordFromList(DOSSIER_ID,
				MgppDetailComm22Page.class);

		// Vérification navette CMP
		// detailCommMgpp22Page.verifierNavette();
		detailCommMgpp22Page.traiter(ProcedureLegislativePage.class);

		logoutEpg();
	}

	@WebTest(description = "Traitement EVT22 GVT")
	@TestDocumentation(categories = { "Corbeille MGPP", "Comm. LEX-25", "Comm. LEX-26" })
	public void testCase026_2() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		// Création EVT 23
		procLegisPage.openCorbeille("Historique (15)");
		MgppDetailComm22Page mgppDetailComm22Page = procLegisPage.openRecordFromList(DOSSIER_ID,
				MgppDetailComm22Page.class);
		mgppDetailComm22Page.assertTrue(DOSSIER_ID + "_00015");
		MgppCreateComm23Page createComm23Page = mgppDetailComm22Page.navigateToCreateCommSucc(EvenementType.EVT_23,
				MgppCreateComm23Page.class);

		final String destinataire = "Sénat";
		MgppDetailComm23Page detailComm23Page = createComm23Page.createComm23(destinataire);

		detailComm23Page.getPage(ProcedureLegislativePage.class);
		procLegisPage.assertCorbeilles(2, 0, 2, 15);

		logoutEpg();
	}

	/**
	 * Traitement Comm EVT23 SE
	 * 
	 */
	@WebTest(description = "Traitement Comm EVT23 SE")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. LEX-26", "Action 'Traiter' EPP", "Comm. LEX-31" })
	public void testCase027() {

		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		corbeillePage.waitForPageLoaded(getDriver());
		// Traitemennt comm EVT23 du Gouvernement
		corbeillePage.deployFirstElementCorbeille();

		CorbeillePage.sleep(2);
		corbeillePage.openCorbeille("Destinataire");
		corbeillePage.assertTrue(DOSSIER_ID);
		corbeillePage.assertTrue("LEX-26 : CMP - Demande de lecture des conclusions");
		DetailComm23Page eppDetailComm23Page = corbeillePage.openRecordFromList(DOSSIER_ID, DetailComm23Page.class);
		eppDetailComm23Page.traite(DetailComm23Page.class);

		// Creation comm EVT24
		CreateComm24Page createComm24Page = eppDetailComm23Page.navigateToCreateCommSucc(EvenementType.EVT_24,
				CreateComm24Page.class);

		final String numeroTexteAdopte = "2012-04";
		final String dateAdoptionInputDate = DateUtils.format(Calendar.getInstance());
		final String sortAdoption_label = "Adopté";
		createComm24Page.createComm24(numeroTexteAdopte, dateAdoptionInputDate, sortAdoption_label);

		logoutEpp();
	}

	/**
	 * Traitement EVT24 GVT
	 * 
	 */
	@WebTest(description = "Traitement EVT24 GVT")
	@TestDocumentation(categories = { "Corbeille MGPP", "Comm. LEX-31", "Action 'Traiter' MGPP" })
	public void testCase028_1() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertCorbeilles(2, 1, 1, 16);
		procLegisPage.openCorbeille("Reçu (1)");
		MgppDetailComm24Page mgppDetailComm24Page = procLegisPage.openRecordFromList(DOSSIER_ID,
				MgppDetailComm24Page.class);

		// Vérification Loi votée (reçue le)
		mgppDetailComm24Page.checkValue(By_enum.XPATH,
				".//*[@id='metadonnee_dossier:nxl_fiche_loi_vote:nxw_fiche_loi_dateReception']",
				DateUtils.format(Calendar.getInstance()), Boolean.FALSE);
		mgppDetailComm24Page.traiter(ProcedureLegislativePage.class);

		logoutEpg();
	}

	@WebTest(description = "Traitement EVT24 GVT")
	@TestDocumentation(categories = { "Corbeille MGPP", "Onglet Publication MGPP", "Publication", "WS", "Comm. LEX-35",
			"Dossier", "Comm. RAP-01" })
	public void testCase028_2() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();

		// Verification non validation étape
		// "Pour attribution secteur parlementaire" (#43298)
		PublicationPage publicationPage = procLegisPage.navigateToAnotherTab("Publication", PublicationPage.class);
		publicationPage.assertTrue("Pour attribution secteur parlementaire (1)");
		publicationPage.assertTrue("Pour publication à la DILA JO (0)");

		// Validation étape 'Pour attrivution secteur parlementaire'
		publicationPage.openCorbeille("Pour attribution secteur parlementaire (1)");
		publicationPage.openRecordFromList(DOSSIER_ID);
		// // Renseignement des info de publication
		publicationPage.assertTrue("Pour attribution secteur parlementaire (1)");
		publicationPage.assertTrue("Pour publication à la DILA JO (0)");

		// Validation étape 'Pour attribution secteur parlementaire'
		publicationPage.openRecordFromList(DOSSIER_ID);
		publicationPage.assertTrue("Fiche Loi");

		procLegisPage = publicationPage.navigateToAnotherTab("Procédure législative", ProcedureLegislativePage.class);
		procLegisPage.assertCorbeilles(1, 0, 1, 17);
		procLegisPage.openCorbeille("Historique (17)");
		// Publication du dossier dans EPG
		logoutEpg();

		StringBuilder xmlForWs = new StringBuilder();
		xmlForWs.append("<?xml version='1.0' encoding='UTF-8'?>");
		xmlForWs.append("<p:envoyerRetourPERequest xmlns:p='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSSpe' ");
		xmlForWs.append("xmlns:p1='http://www.dila.premier-ministre.gouv.fr/solon/epg/spe-commons' ");
		xmlForWs.append("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ");
		xmlForWs.append("xsi:schemcreationPageaLocation='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSSpe ../../../../../../../xsd/solon/epg/WSspe.xsd '>");
		xmlForWs.append("<p:type>PUBLICATION_JO</p:type>");
		xmlForWs.append("<p:retourPublicationJo>");
		xmlForWs.append("<p1:gestion>");
		xmlForWs.append("<p1:date_parution>" + DateUtils.format_WS(Calendar.getInstance()) + "</p1:date_parution>");
		xmlForWs.append("</p1:gestion>");
		xmlForWs.append("<p1:acte>");
		xmlForWs.append("<p1:nor>" + NUMERO_NOR + "</p1:nor>");
		xmlForWs.append("<p1:numero_texte>2012-01</p1:numero_texte>");
		xmlForWs.append("<p1:titre_officiel>titre officiel apres publication " + DOSSIER_ID + "</p1:titre_officiel>");
		xmlForWs.append("<p1:page>100</p1:page>");
		xmlForWs.append("</p1:acte>");
		xmlForWs.append("</p:retourPublicationJo>");
		xmlForWs.append("</p:envoyerRetourPERequest>");
		String epgUrl = ((UrlEpgHelper) UrlEpgHelper.getInstance()).getEpgUrl();
		WsResultPage wsResultPage = loginWSEpg("ws_ministere_prm", "ws_ministere_prm", epgUrl
				+ "/site/solonepg/WSspe/envoyerRetourPE", xmlForWs.toString());

		wsResultPage.doSubmit();
		wsResultPage.checkValue("result", "OK");

		logoutWS();

		// Publication EVT28 pour AN et SE
		TraitementPageForMGGP.sleep(3);
		traitementpage = loginAsAdminEpg();
		mgppPage = traitementpage.goToMGPP();
		procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertCorbeilles(3, 0, 3, 17);
		// Publication EVT28 pour SE
		procLegisPage.openCorbeille("Emis (3)");
		MgppDetailComm28Page mgppDetailComm28Page = procLegisPage.openRecordFromList(DOSSIER_ID,
				MgppDetailComm28Page.class);
		String identifiant = DOSSIER_ID + "_00019";
		final String titre = "titre officiel apres publication " + DOSSIER_ID;
		final String numeroLoi = "2012-01";
		final String dateReception = DateUtils.format(Calendar.getInstance());
		mgppDetailComm28Page.checkValues(identifiant, titre, numeroLoi, dateReception);
		mgppDetailComm28Page.modifier();

		MgppCreateComm28Page mgppCreateComm28Page = mgppDetailComm28Page.getPage(MgppCreateComm28Page.class);
		mgppCreateComm28Page.assertTrue(DOSSIER_ID + "_00019");
		final String datePublication = DateUtils.format(Calendar.getInstance());
		mgppCreateComm28Page.checkValues(NUMERO_NOR, titre, datePublication);

		String objet = "Publication " + DOSSIER_ID + " SE";
		String datePromulgation = DateUtils.format(Calendar.getInstance());
		mgppCreateComm28Page.fillObjetEtDatePromulgation(objet, datePromulgation, CreationPage.class);

		CreationPage creationPage = traitementpage.getOngletMenu().goToEspaceCreation();

		CreationDossier100Page createDossierPage = creationPage.createDossier();
		createDossierPage.setTypeActe("Rapport au Parlement");
		createDossierPage.appuyerBoutonTerminer(CreationDossier100Page.class);
		createDossierPage.waitForPageLoaded(getDriver());

		traitementpage.goToMGPP();
		procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.assertTrue("Historique (18)");

		// Publication EVT28 pour AN
		procLegisPage.openCorbeille("Emis (2)");
		mgppDetailComm28Page = procLegisPage.openRecordFromList(DOSSIER_ID, MgppDetailComm28Page.class);
		identifiant = DOSSIER_ID + "_00018";
		mgppDetailComm28Page.checkValues(identifiant, titre, numeroLoi, dateReception);
		mgppDetailComm28Page.modifier();

		mgppCreateComm28Page = mgppDetailComm28Page.getPage(MgppCreateComm28Page.class);
		mgppCreateComm28Page.assertTrue(DOSSIER_ID + "_00018");
		mgppCreateComm28Page.checkValues(NUMERO_NOR, titre, datePublication);

		objet = "Publication " + DOSSIER_ID + " AN";
		mgppCreateComm28Page.fillObjetEtDatePromulgation(objet, datePromulgation, CreationPage.class);

		logoutEpg();
	}

	@WebTest(description = "Création rapport au parlement GVT")
	@TestDocumentation(categories = { "Comm. RAP-01", "Corbeille MGPP", "Dossier" })
	public void testCase028_3() {

		TraitementPageForMGGP traitementPage = loginAsAdminEpg();
		traitementPage.waitForPageLoaded(getDriver());
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page createDossierPage = creationPage.createDossier();
		createDossierPage.setTypeActe("Rapport au Parlement");
		createDossierPage.appuyerBoutonTerminer(CreationDossier100Page.class);
		creationPage.waitForPageLoaded(getDriver());

		MgppPage mgppPage = traitementPage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		ProcedureLegislativePage.sleep(1);
		procLegisPage.assertCorbeilles(1, 0, 1, 19);

	}

	/**
	 * Traitement EVT44 GVT
	 * 
	 */
	@WebTest(description = "Traitement RAP-01 GVT")
	@TestDocumentation(categories = { "Comm. RAP-01", "Corbeille MGPP" })
	public void testCase029() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		RapportPage rapportPage = mgppPage.getBandeauMenuMgpp().goToRapport();
		DepotDeRapportPage depotRapportPage = rapportPage.getBandeauMenuMgppRapport().goToDeportRapport();

		MgppCreateComm44Page createComm44Page = depotRapportPage.navigateToCreateComm(EvenementType.EVT_44,
				MgppCreateComm44Page.class);

		final String objet = "Objet DR " + DOSSIER_ID;
		final String rapport_parlement = "Rapport unique";
		final String dateInputDate = DateUtils.format(Calendar.getInstance());
		final String destinataire = "Assemblée nationale";
		MgppDetailComm44Page detailComm44Page = createComm44Page.createComm44(DOSSIER_ID, objet, rapport_parlement,
				dateInputDate, destinataire);

		// Verification fiche DR
		detailComm44Page.openCorbeille("Dépôt de rapport (1)");
		detailComm44Page.openRecordFromList(DOSSIER_ID);

		detailComm44Page.assertTrue("Projet de loi Test MGPP");
		detailComm44Page.assertTrue(DOSSIER_ID);
		detailComm44Page.assertTrue("RAP-01 : Transmission de rapports au Parlement - " + DOSSIER_ID + " - Objet DR "
				+ DOSSIER_ID);

		detailComm44Page.verouiller_fiche_par_alt();
		detailComm44Page.checkPresenceOfElements();

		logoutEpg();
	}

}