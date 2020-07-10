package fr.dila.solonmgpp.webtest.webdriver010.dossier040;

import java.util.Calendar;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.creation.CreationDossier100Page;
import fr.dila.solonepg.page.creation.CreationDossier101Page;
import fr.dila.solonepg.page.main.onglet.TraitementPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.ParapheurPage;
import fr.dila.solonepp.page.CorbeillePage;
import fr.dila.solonepp.page.communication.aud.create.CreateCommAUD02Page;
import fr.dila.solonepp.page.communication.aud.create.alerte.CreateCommAlerte14Page;
import fr.dila.solonepp.page.communication.aud.detail.DetailCommAUD01Page;
import fr.dila.solonepp.page.communication.aud.detail.generique.DetailCommGenerique14Page;
import fr.dila.solonepp.page.communication.cens.create.CreateComm17Page;
import fr.dila.solonepp.page.communication.doc.create.generique.CreateDOCGenerique;
import fr.dila.solonepp.page.communication.doc.detail.DetailCommDOC01;
import fr.dila.solonepp.page.communication.doc.detail.generique.DetailCommDOCGenerique;
import fr.dila.solonepp.page.communication.jss.create.CreateCommGenerique13Page;
import fr.dila.solonepp.page.communication.jss.create.CreateCommJSS02Page;
import fr.dila.solonepp.page.communication.jss.detail.DetailCommJSS01Page;
import fr.dila.solonepp.page.communication.jss.detail.DetailCommJSS02Page;
import fr.dila.solonepp.page.communication.lex.create.CreateComm02Page;
import fr.dila.solonepp.page.communication.nom.create.CreateComm34Page;
import fr.dila.solonepp.page.communication.nom.detail.DetailComm33Page;
import fr.dila.solonepp.page.communication.oep.create.CreateComm490Page;
import fr.dila.solonepp.page.communication.oep.create.CreateComm51Page;
import fr.dila.solonepp.page.communication.pg.create.CreateCommPG04Page;
import fr.dila.solonepp.page.communication.pg.detail.DetailCommPG_01;
import fr.dila.solonepp.page.communication.pg.detail.DetailCommPG_03;
import fr.dila.solonepp.page.communication.ppr.create.CreateComm39Page;
import fr.dila.solonepp.page.communication.ppr.create.CreateComm43BisPage;
import fr.dila.solonepp.page.communication.sd.create.CreateCommSD01Page;
import fr.dila.solonepp.page.communication.sd.create.CreateCommSD03Page;
import fr.dila.solonepp.page.communication.sd.create.alerte.CreateCommAlerte12Page;
import fr.dila.solonepp.page.communication.sd.detail.DetailCommSD02Page;
import fr.dila.solonepp.page.communication.sd.detail.alerte.DetailCommAlerte12Page;
import fr.dila.solonepp.page.communication.sd.detail.generique.DetailCommGenerique12Page;
import fr.dila.solonmgpp.creation.page.BordereauOnglet;
import fr.dila.solonmgpp.creation.page.FDROnglet;
import fr.dila.solonmgpp.page.commun.CommonWebPage;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm33Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommAUD01Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommAlerte13Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommAlerte15Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommDOC01Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommGenerique12Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommGenerique14Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommJSS01Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommPG01Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommPG02Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommPG03Page;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommSD02Page;
import fr.dila.solonmgpp.page.create.communication.MgppDetailCommAUD02Page;
import fr.dila.solonmgpp.page.create.communication.MgppDetailCommJSS01Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm02Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm17Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm33Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm34Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm36Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm39Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommAUD01Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommAlerte14Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommDOC01Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommGenerique12Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommGenerique13Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommGenerique15Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommJSS02Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommPG01Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommPG02Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommPG03Page;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommSD01Page;
import fr.dila.solonmgpp.page.epg.espaceEpg.CreationPage;
import fr.dila.solonmgpp.page.epg.espaceEpg.DeclarationPage;
import fr.dila.solonmgpp.page.epg.espaceEpg.MgppPage;
import fr.dila.solonmgpp.page.epg.espaceEpg.NominationPage;
import fr.dila.solonmgpp.page.epg.espaceEpg.OrganisationPage;
import fr.dila.solonmgpp.page.epg.espaceEpg.RapportPage;
import fr.dila.solonmgpp.page.epg.espaceEpg.TraitementPageForMGGP;
import fr.dila.solonmgpp.page.epg.mgpp.decretpr.DecretPrPage;
import fr.dila.solonmgpp.page.epg.mgpp.decretpr.FichePresentationDecretPrPage;
import fr.dila.solonmgpp.page.epg.mgpp.ie.AjouterDemandeIePage;
import fr.dila.solonmgpp.page.epg.mgpp.ie.FichePresentationIEPage;
import fr.dila.solonmgpp.page.epg.mgpp.ie.FondDossierOngletIE;
import fr.dila.solonmgpp.page.epg.mgpp.ie.InterventionExterieurePage;
import fr.dila.solonmgpp.page.epg.mgpp.jss.JoursSupplementairesSeancePage;
import fr.dila.solonmgpp.page.epg.mgpp.nomination.AjouterAuditionPage;
import fr.dila.solonmgpp.page.epg.mgpp.nomination.AviPage;
import fr.dila.solonmgpp.page.epg.mgpp.nomination.CreateAviPage;
import fr.dila.solonmgpp.page.epg.mgpp.nomination.DemandesAuditionPage;
import fr.dila.solonmgpp.page.epg.mgpp.nomination.FondDossierOngletNomination;
import fr.dila.solonmgpp.page.epg.mgpp.nomination.PageFichePresentationAudition;
import fr.dila.solonmgpp.page.epg.mgpp.nomination.PageFichePresentationAvi;
import fr.dila.solonmgpp.page.epg.mgpp.oep.CreateOepPage;
import fr.dila.solonmgpp.page.epg.mgpp.oep.FondDossierOngletOep;
import fr.dila.solonmgpp.page.epg.mgpp.oep.OepPage;
import fr.dila.solonmgpp.page.epg.mgpp.oep.ResolutionsPage;
import fr.dila.solonmgpp.page.epg.mgpp.pg.Politique‌Generale;
import fr.dila.solonmgpp.page.epg.mgpp.procedureLegislative.ProcedureLegislativePage;
import fr.dila.solonmgpp.page.epg.mgpp.rapport.AjouterDossierPage;
import fr.dila.solonmgpp.page.epg.mgpp.rapport.DocumentPage;
import fr.dila.solonmgpp.page.epg.mgpp.rapport.PageFichePresentationDocument;
import fr.dila.solonmgpp.page.epg.mgpp.sd.SujetDetermine;
import fr.dila.solonmgpp.utils.DateUtils;
import fr.dila.solonmgpp.webtest.common.AbstractMgppWebTest;
import fr.dila.st.annotations.TestDocumentation;
import fr.sword.naiad.commons.webtest.annotation.WebTest;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.PieceJointeType;
import fr.sword.xsd.solon.epp.TypeLoi;

public class TestSuite extends AbstractMgppWebTest {

	private static final int	TIMEOUT_SEC	= 15;

	private int getYear() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		year = year % 2000;
		return year;
	}

	/**
	 * test verification oep
	 * 
	 */
	@WebTest(description = "Verification OEP")
	@TestDocumentation(categories = { "Corbeille OEP MGPP", "Comm. OEP-02", "FDD OEP", "Recherche rapide MGPP" })
	public void testCase001() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		NominationPage nominationPage = mgppPage.getBandeauMenuMgpp().goToNomination();
		OepPage oepPage = nominationPage.getBandeauMenuMgppNomination().goToOEP();

		oepPage.assertTrue("Liste des OEP");
		oepPage.assertTrue("Désignation OEP (348)");

		String dossierId = "OEP_0000016P";

		oepPage.doRapidSearchAndClickElement("Liste des OEP (174)", dossierId);

		new WebDriverWait(getDriver(), TIMEOUT_SEC).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//img[contains(@src,'lock_add_24.png')]")));

		getDriver().findElement(By.xpath("//img[contains(@src,'lock_add_24.png')]")).click();

		// oepPAge.assertTrue("Enregistrer");
		// oepPAge.assertTrue("Générer courrier");

		oepPage.checkValue("view_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_nomOrganisme",
				"Conseil supérieur de la coopération");
		oepPage.checkValue("view_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_ministereRattachement",
				"Ministère des solidarités et de la cohésion sociale");
		oepPage.checkValue("view_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_texteDuree",
				"article 3 du décret n° 76-356 du 20 avril 1976");

		oepPage.checkValue("view_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_texteRef",
				"article 2 du décret n° 76-356 du 20 avril 1976");
		oepPage.checkValue("view_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_nbDeputes", "2");
		oepPage.checkValue("view_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_nbSenateurs", "2");

		oepPage.checkValue("view_fpoep:nxl_fiche_presentation_OEP_mandatAN:nxw_fiche_OEP_dureeMandatAN",
				"mandat parlementaire");
		oepPage.checkValue("view_fpoep:nxl_fiche_presentation_OEP_mandatSE:nxw_fiche_OEP_dureeMandatSE",
				"mandat parlementaire");

		oepPage.addRepresentantAn("ass");
		oepPage.setRepresentantAnFonction("TITULAIRE");

		oepPage.addRepresentantSenat("sén");
		oepPage.setRepresentantSenatFonction("SUPPLEANT");

		getDriver().findElement(By.xpath("//*[@value='Enregistrer']")).click();
		new WebDriverWait(getDriver(), TIMEOUT_SEC).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//img[contains(@src,'lock_error_24.png')]")));

		getDriver().findElement(By.xpath("//img[contains(@src,'lock_error_24.png')]")).click();

		new WebDriverWait(getDriver(), TIMEOUT_SEC).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//img[contains(@src,'lock_add_24.png')]")));

		FondDossierOngletOep fondDossierOnglet = oepPage.navigateToAnotherTab("Fond de dossier",
				FondDossierOngletOep.class);

		fondDossierOnglet.adresserDemandeDesignationSGG();
		fondDossierOnglet.addAttachment("src/main/attachments/piece-jointe.doc");
		OepPage.sleep(3);

		oepPage.assertTrue("Désignation OEP (348)");

		oepPage = oepPage.doRapidSearch("Désignation OEP (348)", dossierId, OepPage.class);
		OepPage.sleep(3);
		oepPage.assertTrue("Sénat");
		oepPage.assertTrue("Assemblée nationale");
		oepPage.assertTrue("OEP-02 : Demande de désignation au sein d'un organisme extraparlementaire");

		CreateOepPage createOepPage = oepPage.navigateToPageFromButton("Ajouter un oep", CreateOepPage.class);

		createOepPage.setIdCommun("1");
		createOepPage.setNomOrganisme("Nom OEP");
		createOepPage.setMinistereRattachement1("ministereRattachement");
		createOepPage.setMinistereRattachement2("ministereRattachement2");
		createOepPage.setMinistereRattachement3("ministereRattachement3");
		createOepPage.setTexteRef("texteRef");
		createOepPage.setTexteDuree("texteDuree");
		createOepPage.setCommentaire("commentaire");
		createOepPage.setObservation("observation");

		createOepPage.setNbDeputes("5");
		createOepPage.setNbSenateurs("12");
		createOepPage.setTelephone("0123456789");
		createOepPage.setAdresse("9 Avenue Charles de Gaulle 69771 St Didier au Mont D'Or");
		createOepPage.setFax("9876543210");
		createOepPage.setMail("oep@solon-mgpp.com");
		createOepPage.setDureeMandatAN("dureeMandatAN");
		createOepPage.setDureeMandatSE("dureeMandatSE");

		getDriver().findElement(By.xpath("//*[@value='Enregistrer']")).click();
		CreateOepPage.sleep(1);

		createOepPage.assertTrue("Liste des OEP (175)");
		oepPage.doRapidSearch("Liste des OEP (175)", "OEP_0000001P");

		logoutEpg();
	}

	@WebTest(description = "Creation EVT_49-0 AN")
	@TestDocumentation(categories = { "Comm. OEP-01" })
	public void testCase002() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		final CreateComm490Page createComm49Page = corbeillePage.navigateToCreateComm(EvenementType.EVT_49_0,
				CreateComm490Page.class);
		createComm49Page.createComm490("3367L", "objet", "baseLegale");
		logoutEpp();
	}

	@WebTest(description = "Traitement EVT_49-0 Gouvernement")
	@TestDocumentation(categories = { "Corbeille OEP MGPP", "Recherche rapide MGPP", "Action 'Traiter' MGPP" })
	public void testCase003() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		NominationPage nominationPage = mgppPage.getBandeauMenuMgpp().goToNomination();
		OepPage oepPage = nominationPage.getBandeauMenuMgppNomination().goToOEP();
		oepPage.assertTrue("Liste des OEP (175)");
		oepPage.assertTrue("Désignation OEP (349)");
		oepPage.assertTrue("Historique (0)");
		oepPage.doRapidSearch("Désignation OEP (349)", "3367L");

		getDriver().findElement(By.id(CommonWebPage.MESSAGE_LISTING_ROW_1)).click();
		oepPage.assertTrue("Fiche organisme extraparlementaire");
		CreateOepPage createOepPage = oepPage.traiter();

		createOepPage.checkValue("view_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_idCommun", "3367L");
		createOepPage.checkValue("view_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_nomOrganisme", "objet");

		createOepPage.assertTrue("Désignation OEP (348)");
		createOepPage.assertTrue("Liste des OEP (176)");

		logoutEpg();

	}

	@WebTest(description = "Creation EVT_51 AN")
	@TestDocumentation(categories = { "Comm. OEP-03" })
	public void testCase004() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		final CreateComm51Page createComm51Page = corbeillePage.navigateToCreateComm(EvenementType.EVT_51,
				CreateComm51Page.class);
		String currentDate = DateUtils.format(Calendar.getInstance());
		createComm51Page.createComm51("300", "Objet 51", "baselegale", currentDate, currentDate);
		logoutEpp();
	}

	@WebTest(description = "Traitement EVT_51 Gouvernement")
	@TestDocumentation(categories = { "Comm. OEP-03", "Corbeille OEP MGPP", "Action 'Traiter' MGPP" })
	public void testCase005() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		NominationPage nominationPage = mgppPage.getBandeauMenuMgpp().goToNomination();
		OepPage oepPage = nominationPage.getBandeauMenuMgppNomination().goToOEP();
		oepPage.assertTrue("Liste des OEP (176)");
		oepPage.assertTrue("Désignation OEP (349)");
		oepPage.doRapidSearch("Désignation OEP (349)", "300");

		getDriver().findElement(By.id(CommonWebPage.MESSAGE_LISTING_ROW_1)).click();
		oepPage.assertTrue("Aucune fiche trouvée");
		CreateOepPage createOepPage = oepPage.traiter();

		createOepPage.checkValue(CreateOepPage.ID_COMMUN_INPUT, "300");
		createOepPage.checkValue(CreateOepPage.NOM_ORGANISME_INPUT, "Objet 51");

		createOepPage.setMinistereRattachement1("ministereRattachement OEP3");
		getDriver().findElement(By.xpath("//*[@value='Enregistrer']")).click();

		CreateOepPage.sleep(1);

		createOepPage.assertTrue("Désignation OEP (348)");
		createOepPage.assertTrue("Liste des OEP (177)");

		logoutEpg();

	}

	@WebTest(description = "Creation AVI")
	@TestDocumentation(categories = { "Corbeille Nomination MGPP", "Organisme" })
	public void testCase006() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		NominationPage nominationPage = mgppPage.getBandeauMenuMgpp().goToNomination();
		AviPage aviPage = nominationPage.getBandeauMenuMgppNomination().goToAvisNomination();

		aviPage.assertTrue("Avis nomination (0)");
		aviPage.assertTrue("Liste des organismes (0)");

		CreateAviPage creatAviPage = (CreateAviPage) aviPage.navigateToPageFromButton("Ajouter un organisme",
				CreateAviPage.class);
		creatAviPage.setIdDossier("1");
		creatAviPage.setNomOrganisme("Nom AVI 1");
		creatAviPage.setBaseLegale("baseLegale AVI 1");
		creatAviPage.ajouterNomine("Fillon François");
		creatAviPage.enregistrer();
		aviPage.assertTrue("Liste des organismes (1)");

		PageFichePresentationAvi aviPageDetail = aviPage
				.goToAviPageDetail("Liste des organismes (1)",
						"fiche_presentation_avi_list:nxl_fiche_presentation_AVI_listing_1:nxw_fiche_AVI_idDossier_listing_1:msgName");

		aviPageDetail.checkValueContain(PageFichePresentationAvi.ID_DOSSIER_INPUT, "1");
		aviPageDetail.checkValueContain(PageFichePresentationAvi.NOM_ORGANISME_INPUT, "Nom AVI 1");
		aviPageDetail.checkValueContain(PageFichePresentationAvi.BASE_LEGALE_INPUT, "baseLegale AVI 1");
		aviPageDetail.checkValueContain(PageFichePresentationAvi.NOMINE_INPUT, "Fillon François");
		String currentDate = DateUtils.format(Calendar.getInstance());
		aviPageDetail.checkValue(PageFichePresentationAvi.DATE_DEBUT_INPUT, currentDate);

		logoutEpg();
	}

	@WebTest(description = "FDD AVI")
	@TestDocumentation(categories = { "Corbeille Nomination MGPP", "Organisme", "FDD Organisme", "Comm. NOM-01" })
	public void testCase007() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		NominationPage nominationPage = mgppPage.getBandeauMenuMgpp().goToNomination();
		AviPage aviPage = nominationPage.getBandeauMenuMgppNomination().goToAvisNomination();

		aviPage.assertTrue("Avis nomination (0)");

		aviPage.assertTrue("Liste des organismes (1)");

		PageFichePresentationAvi aviPageDetail = aviPage.goToAviPageDetail("Liste des organismes (1)",
				AviPage.MESSAGE_LISTING_ROW_1);

		String currentDate = DateUtils.format(Calendar.getInstance());
		aviPageDetail.checkValue(PageFichePresentationAvi.DATE_DEBUT_INPUT, currentDate);

		FondDossierOngletNomination fondDossierOnglet = aviPageDetail.navigateToAnotherTab("Fond de dossier",
				FondDossierOngletNomination.class);
		fondDossierOnglet.ajouterUnfichier("src/main/attachments/piece-jointe.doc");

		aviPage.waitForPageSourcePart("Avis nomination (2)", TIMEOUT_SEC);

		aviPage.openCorbeille("Avis nomination (2)");

		aviPage.assertTrue("Sénat");
		aviPage.assertTrue("Assemblée nationale");

		// aviPage.assertTrue("NOM-01 : Communication d'une proposition de nomination");

		MgppDetailComm33Page detailCommPage33Page = aviPage.openRecordByIdFromList(CommonWebPage.MESSAGE_LISTING_ROW_1,
				MgppDetailComm33Page.class);

		// detailCommPage33Page.assertTrue("NOM-01 : Communication d'une proposition de nomination - 1 - Nom AVI 1");
		detailCommPage33Page.checkValueContain(MgppDetailComm33Page.OBJET, "Nom AVI 1");
		detailCommPage33Page.checkValueContain(MgppDetailComm33Page.ID_COMMUNICATION, "1");

		// 2
		aviPage.openCorbeille("Avis nomination (2)");
		detailCommPage33Page = aviPage.openRecordByIdFromList(CommonWebPage.MESSAGE_LISTING_ROW_2,
				MgppDetailComm33Page.class);
		detailCommPage33Page.assertTrue("Assemblée nationale");

		MgppCreateComm33Page creationCommEVT33Page = detailCommPage33Page.modifier();
		creationCommEVT33Page.setIntitule("intitule AN");
		currentDate = DateUtils.format(Calendar.getInstance());
		creationCommEVT33Page.setDateActe(currentDate);
		creationCommEVT33Page.setEcheance("now");

		creationCommEVT33Page.addPieceJointe(PieceJointeType.LETTRE_PM, "PJPMPourAVIAN",
				"http://PJPMVANPourAVIAN.solon-epg.fr", "src/main/attachments/piece-jointe.doc");
		creationCommEVT33Page.publier();
		logoutEpg();

	}

	@WebTest(description = "Creation EVT 34 AN")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. NOM-02", "Action 'Traiter' EPP" })
	public void testCase008() {
		CorbeillePage corbeillePage = loginEpp("an", "an");

		corbeillePage.openCorbeille("DIQS Divers (5)");
		DetailComm33Page detailComm33Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailComm33Page.class);

		// DetailComm33Page detailComm33Page =
		// corbeillePage.openRecordByPathFromList("//*[@id='corbeille_message_list']/div/table/tbody/tr/td[5]/div/a",
		// DetailComm33Page.class);

		detailComm33Page.traite(DetailComm33Page.class);

		final CreateComm34Page createComm34Page = detailComm33Page.navigateToCreateCommSucc(EvenementType.EVT_34,
				CreateComm34Page.class);
		createComm34Page.createComm34("FAVORABLE", DateUtils.format(Calendar.getInstance()));
		logoutEpp();

	}

	@WebTest(description = "Traitement EVT_34 Gouvernement")
	@TestDocumentation(categories = { "Comm. NOM-02", "Corbeille Nomination MGPP", "Action 'Traiter' MGPP" })
	public void testCase009() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		NominationPage nominationPage = mgppPage.getBandeauMenuMgpp().goToNomination();
		AviPage aviPage = nominationPage.getBandeauMenuMgppNomination().goToAvisNomination();

		aviPage.assertTrue("Avis nomination (2)");

		PageFichePresentationAvi aviPageFichePresentation = aviPage.goToAviPageDetail("Avis nomination (2)",
				CommonWebPage.MESSAGE_LISTING_ROW_1);
		ProcedureLegislativePage.sleep(1);
		aviPageFichePresentation.assertTrue("Fiche organisme avis nomination");
		MgppDetailComm34Page detailComm34Page = aviPageFichePresentation.getPage(MgppDetailComm34Page.class);
		aviPage = detailComm34Page.traiter(AviPage.class);
		aviPage.assertTrue("Avis nomination (1)");
	}

	@WebTest(description = "Creation EVT_02 AN")
	@TestDocumentation(categories = { "Comm. LEX-02" })
	public void testCase010() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		CreateComm02Page createCommEVT02Page = corbeillePage.navigateToCreateComm(EvenementType.EVT_02,
				CreateComm02Page.class);
		final String currentDate = DateUtils.format(Calendar.getInstance());
		createCommEVT02Page.createCommEVT02("MGPP0001", "Objet EVT02", TypeLoi.LOI, currentDate, "2012", "ass");
		logoutEpp();
	}

	@WebTest(description = "Traitement EVT_02 Gouvernement")
	@TestDocumentation(categories = { "Comm. LEX-02", "Corbeille MGPP", "Recherche rapide MGPP",
			"Action 'Traiter' MGPP" })
	public void testCase011() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();

		procLegisPage.doRapidSearchAndClickElement("Reçu (1)", "MGPP0001");

		ProcedureLegislativePage.sleep(1);
		MgppDetailComm02Page detailCommEVT02Page = procLegisPage.getPage(MgppDetailComm02Page.class);

		CreationPage creationDossierPage = detailCommEVT02Page.traiter(CreationPage.class);

		creationDossierPage.setTypeActe("Loi");
		MgppDetailComm02Page.sleep(1);

		new WebDriverWait(getDriver(), TIMEOUT_SEC).until(ExpectedConditions.elementToBeClickable(By
				.id(CreationPage.BOUTON_SUIVANT_ID)));
		MgppDetailComm02Page.sleep(1);
		getDriver().findElement(By.id(CreationPage.BOUTON_SUIVANT_ID)).click();
		// go to next form
		new WebDriverWait(getDriver(), TIMEOUT_SEC).until(ExpectedConditions.elementToBeClickable(By
				.xpath(CreationPage.BOUTON_TERMINER_PATH)));
		// DetailCommEVT02Page.sleep(1);
		// getDriver().findElement(By.xpath(BOUTON_TERMINER_PATH)).click();
		// new WebDriverWait(getDriver(),
		// TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(BOUTON_TERMINER_PATH)));
		//
		// creationDossierPage.appuyerBoutonSuivantEtTerminer();

		// procLegisPage.assertTrue("Reçu (0)");
		//
		// procLegisPage.openCorbeille("Historique (1)");
		// procLegisPage.openRecordByIdFromList(CommonWebPage.MESSAGE_LISTING_ROW_1, AviPageFichePresentation.class);
		// new WebDriverWait(getDriver(),
		// TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@src,'lock_add_24.png')]")));
		// procLegisPage.checkValue("metadonnee_dossier:nxl_fiche_loi:nxw_fiche_loi_numeroNor", "*CCOZ1400001L*");
		//
		// procLegisPage.navigateToAnotherTab("Bordereau", BordereauOnglet.class);
		// FDDOnglet fDDOnglet = procLegisPage.navigateToAnotherTab("Fond de dossier", FDDOnglet.class);
		// fDDOnglet = fDDOnglet.recharger();
		// fDDOnglet.checkValue(FDDOnglet.DOSSIER_PJ, "piece-jointe.doc");
		// CreationPage creationPage = traitementPage.navigateToAnotherTab("Création", CreationPage.class);
		//
		// creationPage.doRapidSearchByAttribute("NOR", "CCOZ1400001L");
		logoutEpg();

	}

	@WebTest(description = "Creation Intervention Exterieure")
	@TestDocumentation(categories = { "Corbeille Déclaration MGPP", "Comm. OPEX-01", "Fiche Présentation IE", "FDD" })
	public void testCase012() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		DeclarationPage declarationPage = mgppPage.getBandeauMenuMgpp().goToDeclaration();
		InterventionExterieurePage interventionExterieurePage = declarationPage.getBandeauMenuMgppDeclaration()
				.goToIE();

		interventionExterieurePage.assertTrue("Intervention extérieure (0)");

		String currentDate = DateUtils.format(Calendar.getInstance());
		AjouterDemandeIePage ajouteDemandeIePage = interventionExterieurePage.navigateToPageFromButton(
				"Ajouter une demande", AjouterDemandeIePage.class);
		interventionExterieurePage = ajouteDemandeIePage.ajouteDemandeIE("fill", "Intervention exterieure 1",
				currentDate);

		interventionExterieurePage.assertTrue("Intervention extérieure (2)");

		interventionExterieurePage.openCorbeille("Intervention extérieure (2)");
		FichePresentationIEPage fichePresentationIEPage = interventionExterieurePage.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, FichePresentationIEPage.class);

		fichePresentationIEPage.checkValueContain(FichePresentationIEPage.ID_PROPOSITION, "35C" + getYear() + "00000");
		fichePresentationIEPage.checkValueContain(FichePresentationIEPage.AUTEUR, "M. Fillon François");
		fichePresentationIEPage.checkValueContain(FichePresentationIEPage.INTITULE, "Intervention exterieure 1");
		fichePresentationIEPage.checkValueContain(FichePresentationIEPage.DATE,
				DateUtils.format(Calendar.getInstance()));

		MgppDetailComm36Page detailComm36Page = fichePresentationIEPage.getPage(MgppDetailComm36Page.class);
		detailComm36Page.checkValueContain(MgppDetailComm36Page.ID_COMMUNICATION, "35C" + getYear() + "00000");
		detailComm36Page.checkValueContain(MgppDetailComm36Page.DESTINATAIRE, "Sénat");
		detailComm36Page.checkValueContain(MgppDetailComm36Page.EMETTEUR, "Gouvernement");
		detailComm36Page.checkValueContain(MgppDetailComm36Page.OBJET, "Intervention exterieure 1");
		detailComm36Page.checkValueContain(MgppDetailComm36Page.AUTEUR, "M. Fillon François");

		interventionExterieurePage.openCorbeille("Intervention extérieure (2)");
		fichePresentationIEPage = interventionExterieurePage.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_2, FichePresentationIEPage.class);

		fichePresentationIEPage.checkValueContain(FichePresentationIEPage.ID_PROPOSITION, "35C" + getYear() + "00000");
		fichePresentationIEPage.checkValueContain(FichePresentationIEPage.AUTEUR, "M. Fillon François");
		fichePresentationIEPage.checkValueContain(FichePresentationIEPage.INTITULE, "Intervention exterieure 1");
		fichePresentationIEPage.checkValueContain(FichePresentationIEPage.DATE,
				DateUtils.format(Calendar.getInstance()));

		detailComm36Page = fichePresentationIEPage.getPage(MgppDetailComm36Page.class);
		detailComm36Page.checkValueContain(MgppDetailComm36Page.ID_COMMUNICATION, "35C" + getYear() + "00000_00000");
		detailComm36Page.checkValueContain(MgppDetailComm36Page.DESTINATAIRE, "Assemblée nationale");
		detailComm36Page.checkValueContain(MgppDetailComm36Page.EMETTEUR, "Gouvernement");
		detailComm36Page.checkValueContain(MgppDetailComm36Page.OBJET, "Intervention exterieure 1");
		detailComm36Page.checkValueContain(MgppDetailComm36Page.AUTEUR, "M. Fillon François");

		FondDossierOngletIE fDDOnglet = fichePresentationIEPage.navigateToAnotherTab("Fond de dossier",
				FondDossierOngletIE.class);
		fDDOnglet.ajouterUnfichier("src/main/attachments/piece-jointe.doc");

		logoutEpg();

	}

	@WebTest(description = "Creation EVT_39 AN")
	@TestDocumentation(categories = { "Comm. PPR-01" })
	public void testCase013() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		final CreateComm39Page creationCommEVT39Page = corbeillePage.navigateToCreateComm(EvenementType.EVT_39,
				CreateComm39Page.class);
		final String currentDate = DateUtils.format(Calendar.getInstance());
		creationCommEVT39Page.createComm39("3411", "Objet 3411", "ass",
				"Proposition de résolution, déposée en application de l'article 34-1 de la constitution, Objet 3411",
				currentDate, "2012-3411");
		logoutEpp();

	}

	@WebTest(description = "Traitement EVT_39 Gouvernement")
	@TestDocumentation(categories = { "Comm. PPR-01", "Corbeille Résolutions MGPP", "Action 'Traiter' MGPP" })
	public void testCase014() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ResolutionsPage resolutionsPage = mgppPage.getBandeauMenuMgpp().goToResolutions();
		resolutionsPage.assertTrue("Résolutions (1)");
		resolutionsPage.doRapidSearchAndClickElement("Résolutions (1)", "3411");
		MgppDetailComm39Page detailComm39Page = resolutionsPage.getPage(MgppDetailComm39Page.class);
		detailComm39Page.checkValueContain(MgppDetailComm39Page.AUTEUR, "M. Assemblée Nationale");
		detailComm39Page.checkValueContain(MgppDetailComm39Page.ID_DOSSIER, "3411");
		detailComm39Page.checkValueContain(MgppDetailComm39Page.OBJET, "Objet 3411");
		detailComm39Page.checkValueContain(MgppDetailComm39Page.NUMERO_DEPOT_TEXTE, "2012-3411");
		final String currentDate = DateUtils.format(Calendar.getInstance());
		detailComm39Page.checkValue(MgppDetailComm39Page.DATE_DEPOT_TEXTE, currentDate);
		detailComm39Page.checkValueContain(MgppDetailComm39Page.INTITULE,
				"Proposition de résolution, déposée en application de l'article 34-1 de la constitution, Objet 3411");
		detailComm39Page.checkValueContain(MgppDetailComm39Page.ID_COMMUNICATION, "3411_00000");
		resolutionsPage = detailComm39Page.traiter(ResolutionsPage.class);
		/*
		 * By byXpath = By.xpath("//*[@value='Enregistrer']") ; new WebDriverWait(getDriver(),
		 * TIMEOUT_SEC).until(ExpectedConditions.visibilityOfElementLocated(byXpath));
		 * getDriver().findElement(byXpath).click();
		 */
		resolutionsPage.waitForPageSourcePart("Résolutions (0)", TIMEOUT_SEC);
		logoutEpg();
	}

	@WebTest(description = "Creation EVT_43BIS AN")
	@TestDocumentation(categories = { "Comm. PPRE" })
	public void testCase015() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		final CreateComm43BisPage creationComm43BisPage = corbeillePage.navigateToCreateComm(EvenementType.EVT_43_BIS,
				CreateComm43BisPage.class);
		final String currentDate = DateUtils.format(Calendar.getInstance());
		creationComm43BisPage.createComm43Bis("3412", "Objet 3412", "Résolution européenne Objet 3412", currentDate);
		logoutEpp();
	}

	@WebTest(description = "Traitement EVT_43BIS Gouvernement")
	@TestDocumentation(categories = { "Comm. PPRE", "Recherche rapide MGPP", "Action 'Traiter' MGPP" })
	public void testCase016() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ResolutionsPage resolutionsPage = mgppPage.getBandeauMenuMgpp().goToResolutions();
		resolutionsPage.assertTrue("Résolutions (1)");
		resolutionsPage.doRapidSearchAndClickElement("Résolutions (1)", "3412");
		MgppDetailComm39Page detailComm39Page = resolutionsPage.getPage(MgppDetailComm39Page.class);
		detailComm39Page.checkValueContain(MgppDetailComm39Page.ID_DOSSIER, "3412");
		detailComm39Page.checkValueContain(MgppDetailComm39Page.OBJET, "Objet 3412");
		detailComm39Page.checkValueContain(MgppDetailComm39Page.INTITULE, "Résolution européenne Objet 3412");
		detailComm39Page.checkValueContain(MgppDetailComm39Page.ID_COMMUNICATION, "3412_00000");
		detailComm39Page.assertTrue("Adopté");

		resolutionsPage = detailComm39Page.traiter(ResolutionsPage.class);
		/*
		 * By byXpath = By.xpath("//*[@value='Enregistrer']") ; new WebDriverWait(getDriver(),
		 * TIMEOUT_SEC).until(ExpectedConditions.visibilityOfElementLocated(byXpath));
		 * getDriver().findElement(byXpath).click();
		 */
		resolutionsPage.waitForPageSourcePart("Résolutions (0)", TIMEOUT_SEC);
		logoutEpg();
	}

	@WebTest(description = "Creation Décret du président de la République Gouv")
	@TestDocumentation(categories = { "FDD", "Dossier", "Bordereau", "Parapheur", "Comm. 18C" })
	public void testCase017() {
		TraitementPage traitementPage = loginAsAdminEpg();

		fr.dila.solonepg.page.main.onglet.CreationPage creationPage = traitementPage.navigateToAnotherTab("Création",
				fr.dila.solonepg.page.main.onglet.CreationPage.class);
		CreationDossier100Page createDossierPage = creationPage.createDossier();
		createDossierPage.setTypeActe("Décret du Président de la République");
		CreationDossier101Page creationDossier104Page = createDossierPage
				.appuyerBoutonSuivant(CreationDossier101Page.class);
		ParapheurPage parapheurPage = creationDossier104Page.appuyerBoutonTerminer(ParapheurPage.class);
		creationPage.waitForPageSourcePart("", TIMEOUT_SEC);

		// creationPage.openRecordFromList("CCOZ1400001D");

		parapheurPage.verrouiller();

		parapheurPage.ajoutDocumentParapheur("//td[contains(text(),'Acte intégral')]",
				"src/main/attachments/piece-jointe.doc");

		// parapheurPage.ajoutDocumentParapheur n'est pas safe pour 2 appels successifs
		ParapheurPage.sleep(2);

		parapheurPage.ajoutDocumentParapheur("//td[contains(text(),'Extrait')]",
				"src/main/attachments/piece-jointe.doc");

		// parapheurPage.ajoutDocumentParapheur n'est pas safe pour 2 appels successifs
		ParapheurPage.sleep(2);

		// Remplissage du FDD
		parapheurPage.ajouterDocumentFDD(ParapheurPage.MINISTERE_PORTEUR_PATH, "src/main/attachments/piece-jointe.doc");
		
		// parapheurPage.ajoutDocumentParapheur n'est pas safe pour 2 appels successifs
		ParapheurPage.sleep(2);
		
		parapheurPage
				.ajouterDocumentFDD(ParapheurPage.TOUS_UTILISATEURS_PATH, "src/main/attachments/piece-jointe2.doc");
		// // Remplissage du Bordereau
		BordereauOnglet bordereauOnglet = creationPage.navigateToAnotherTab("Bordereau", BordereauOnglet.class);
		bordereauOnglet.initialize("Test mgpp decret PR", "Nom responsable mgpp", "Qualité responsable mgpp",
				"0123456789", "Action sociale, santé, sécurité sociale");

		FDROnglet fdrOnglet = creationPage.navigateToAnotherTab("Feuille de route", FDROnglet.class);
		fdrOnglet.initialize();

		creationPage.lancerDossier();

		traitementPage = creationPage.navigateToAnotherTab("Traitement", TraitementPage.class);

		logoutEpg();

	}

	@WebTest(description = "Verification Fiche Decret PR")
	@TestDocumentation(categories = { "Comm. 18C", "Corbeille Organisation MGPP", "Fiche présentation décret PR" })
	public void testCase018() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		OrganisationPage organisationPage = mgppPage.getBandeauMenuMgpp().goToOrganisationPage();
		DecretPrPage decretPrPage = organisationPage.getBandeauMenuMgppOrganisation().goToDecretPR();

		// CCOZ1600003D
		decretPrPage.openCorbeille("Liste des sessions extraordinaires (1)");

		String dossierNorAttendu = "CCOZ" + getYear() + "00003D";
		TraitementPage.sleep(2);
		FichePresentationDecretPrPage fichePresentationDecretPrPage = decretPrPage.openRecordFromList(
				dossierNorAttendu, FichePresentationDecretPrPage.class);
		new WebDriverWait(getDriver(), TIMEOUT_SEC).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//img[contains(@src,'lock_add_24.png')]")));
		fichePresentationDecretPrPage.checkValue(FichePresentationDecretPrPage.NOR, dossierNorAttendu);
		logoutEpg();
	}

	@WebTest(description = "Creation EVT 17 AN")
	@TestDocumentation(categories = { "Comm. CENS-01" })
	public void testCase019() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		final CreateComm17Page creationComm17Page = corbeillePage.navigateToCreateComm(EvenementType.EVT_17,
				CreateComm17Page.class);
		creationComm17Page.createComm17("MC1", "Objet");
		logoutEpp();
	}

	@WebTest(description = "Traitement EVT_17 Gouvernement")
	@TestDocumentation(categories = { "Comm. CENS-01", "Action 'Traiter' MGPP", "Corbeille MGPP" })
	public void testCase020() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		ProcedureLegislativePage procLegisPage = mgppPage.getBandeauMenuMgpp().goToProcedureLegislative();
		procLegisPage.doRapidSearchAndClickElement("Motion de censure (1)", "MC1");

		getDriver().findElement(By.id(CommonWebPage.MESSAGE_LISTING_ROW_1)).click();
		MgppDetailComm17Page detailCommEVT17Page = procLegisPage.getPage(MgppDetailComm17Page.class);
		detailCommEVT17Page.checkValueContain(MgppDetailComm17Page.ID_COMMUNICATION, "MC1_00000");
		procLegisPage = detailCommEVT17Page.traiter(ProcedureLegislativePage.class);
		procLegisPage.assertTrue("Motion de censure (0)");

	}

	@WebTest(description = "Creation PG-01 GVT")
	@TestDocumentation(categories = { "Corbeille Déclaration MGPP", "Comm. PG-01", "Comm. PG-02" })
	public void testCase021() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		DeclarationPage declarationPage = mgppPage.getBandeauMenuMgpp().goToDeclaration();
		Politique‌Generale politique‌Generale = declarationPage.getBandeauMenuMgppDeclaration()
				.goToPolitique‌Generale();

		politique‌Generale.assertTrue("Déclaration de politique générale (0)");

		MgppCreateCommPG01Page createCommPG01Page = politique‌Generale.navigateToCreateComm(EvenementType.PG_01,
				MgppCreateCommPG01Page.class);

		final String objet = "Objet PG";
		final String datePresentation = DateUtils.format(Calendar.getInstance());
		final String dateLettrePM = DateUtils.format(Calendar.getInstance());

		// final String destinataire = "Assemblée nationale";

		MgppDetailCommPG01Page detailCommPG01Page = createCommPG01Page.createCommPG01(objet, datePresentation,
				dateLettrePM);

		detailCommPG01Page.assertTrue("Déclaration de politique générale (1)");
		detailCommPG01Page.assertTrue(objet);

		politique‌Generale.openCorbeille("Déclaration de politique générale (1)");

		detailCommPG01Page = politique‌Generale.openRecordByIdFromList(CommonWebPage.MESSAGE_LISTING_ROW_1,
				MgppDetailCommPG01Page.class);

		detailCommPG01Page.checkValue(MgppDetailCommPG01Page.OBJECT_ID, objet);

		// detailCommPG01Page.checkValue(DetailCommPG_01.DATE_VOTE_ID, DateUtils.format(Calendar.getInstance()));
		// detailCommPG01Page.checkValueContain(DetailCommPG_01.HORODATAGE_ID,
		// DateUtils.format(Calendar.getInstance()));

		MgppCreateCommPG02Page createCommPG02Page = detailCommPG01Page.navigateToCreateCommSucc(EvenementType.PG_02,
				MgppCreateCommPG02Page.class);
		createCommPG02Page.createCommPG02(objet, datePresentation, dateLettrePM);

		MgppCreateCommPG02Page.sleep(1);

		detailCommPG01Page.assertTrue("Déclaration de politique générale (2)");
		politique‌Generale.openCorbeille("Déclaration de politique générale (2)");

		MgppDetailCommPG02Page detailCommPG02Page = politique‌Generale.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, MgppDetailCommPG02Page.class);
		detailCommPG02Page.checkValue(MgppDetailCommPG01Page.OBJECT_ID, objet);

		MgppCreateCommPG03Page createCommPG03Page = politique‌Generale.navigateToCreateComm(EvenementType.PG_03,
				MgppCreateCommPG03Page.class);

		MgppDetailCommPG03Page detailCommPG03Page = createCommPG03Page.createCommPG03(objet, datePresentation,
				dateLettrePM);

		detailCommPG03Page.checkValue(MgppDetailCommPG03Page.OBJECT_ID, objet);

		// final String destinataire = "Assemblée nationale";
		// MgppCreateCommGenerique11Page createCommGenerique11Page =
		// detailCommPG01Page.navigateToCreateCommSucc(EvenementType.GENERIQUE_11, MgppCreateCommGenerique11Page.class);
		// MgppDetailCommGenerique11Page detailCommGenerique11Page =
		// createCommGenerique11Page.createCommGenerique11("GEN01", destinataire);
		//
		// MgppDetailCommGenerique11Page.sleep(1);
		//
		// createCommGenerique11Page = detailCommGenerique11Page.completer(MgppCreateCommGenerique11Page.class);

		// politique‌Generale.checkValueContain("metadonnee_dossier:nxl_fiche_presentation_dpg:nxw_fiche_dpg_datePresentation",
		// datePresentation);
		// politique‌Generale.checkValueContain("metadonnee_dossier:nxl_fiche_presentation_dpg:nxw_fiche_dpg_dateLettrePm",
		// dateLettrePM);

		logoutEpg();
	}

	@WebTest(description = "Creation PG-04 AN")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. PG-04", "Comm. PG-01", "Comm. PG-03" })
	public void testCase022() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		corbeillePage.openCorbeille("Déclarations du Gouvernement (3)");

		DetailCommPG_01 detailCommPg01Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[3]/td[5]/div/a", DetailCommPG_01.class);

		CreateCommPG04Page createPG04Page = detailCommPg01Page.navigateToCreateCommSucc(EvenementType.PG_04,
				CreateCommPG04Page.class);
		createPG04Page.createCommPG04("Favorable");
		CreateCommPG04Page.sleep(0);

		corbeillePage.openCorbeille("Déclarations du Gouvernement (3)");
		DetailCommPG_03 detailCommPg03Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[2]/td[5]/div/a", DetailCommPG_03.class);
		createPG04Page = detailCommPg03Page.navigateToCreateCommSucc(EvenementType.PG_04, CreateCommPG04Page.class);
		createPG04Page.createCommPG04("Favorable");

		// final CreateComm490Page createComm49Page = corbeillePage.navigateToCreateComm(EvenementType.PG,
		// CreateComm490Page.class);
		// createComm49Page.createComm490("3", "objet", "baseLegale");
		logoutEpp();
	}

	@WebTest(description = "Creation PG-04 SENAT 1")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. PG-04", "Recherche", "Comm. PG-01" })
	public void testCase023_1() {
		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		corbeillePage.openCorbeille("Pour info");
		getDriver().findElement(By.xpath(".//*[@id='toggleRechercheRapideForm:openRechercheRapideButtonId']")).click();
		corbeillePage.waitForPageSourcePartDisplayed(By.id("toggleRechercheRapideForm:closeRechercheRapideButtonId"),
				TIMEOUT_SEC);
		getDriver().findElement(By.id("quickSearchForm:nxl_recherche_rapide_layout:nxw_recherche_objet_dossier"))
				.sendKeys("Objet PG");
		final WebElement elem = getDriver().findElement(By.xpath(".//*[@id='quickSearchForm:submitQuickSearch']"));
		getFlog().actionClickButton("Rechercher");
		elem.click();
		corbeillePage.waitForPageSourcePart("PG" + getYear(), TIMEOUT_SEC);
		DetailCommPG_01 detailCommPg01Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[3]/td[5]/div/a", DetailCommPG_01.class);
		CreateCommPG04Page createPG04Page = detailCommPg01Page.navigateToCreateCommSucc(EvenementType.PG_04,
				CreateCommPG04Page.class);
		createPG04Page.createCommPG04("Favorable");
		CreateCommPG04Page.sleep(1);
		logoutEpp();
	}

	@WebTest(description = "Creation PG-04 SENat 2")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. PG-01", "Comm. PG-04" })
	public void testCase023_2() {
		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		corbeillePage.openCorbeille("Pour info");
		getDriver().findElement(By.xpath(".//*[@id='toggleRechercheRapideForm:openRechercheRapideButtonId']")).click();
		corbeillePage.waitForPageSourcePartDisplayed(By.id("toggleRechercheRapideForm:closeRechercheRapideButtonId"),
				TIMEOUT_SEC);
		getDriver().findElement(By.id("quickSearchForm:nxl_recherche_rapide_layout:nxw_recherche_objet_dossier"))
				.sendKeys("Objet PG");
		final WebElement elem = getDriver().findElement(By.xpath(".//*[@id='quickSearchForm:submitQuickSearch']"));
		getFlog().actionClickButton("Rechercher");
		elem.click();
		corbeillePage.waitForPageSourcePart("PG" + getYear(), TIMEOUT_SEC);
		DetailCommPG_01 detailCommPg01Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommPG_01.class);
		CreateCommPG04Page createPG04Page = detailCommPg01Page.navigateToCreateCommSucc(EvenementType.PG_04,
				CreateCommPG04Page.class);
		createPG04Page.createCommPG04("Favorable");
		CreateCommPG04Page.sleep(1);

		logoutEpp();
	}

	@WebTest(description = "Creation SD-01 GVT")
	@TestDocumentation(categories = { "Comm. SD-01" })
	public void testCase024_1() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		CorbeillePage.sleep(3);
		CreateCommSD01Page createCommSD01Page = corbeillePage.navigateToCreateComm(EvenementType.SD_01,
				CreateCommSD01Page.class);

		final String currentDate = DateUtils.format(Calendar.getInstance());
		createCommSD01Page.createCommSD01("ID1_SD-01", "objet SD-01", currentDate, "groupeAN");

		logoutEpp();

	}

	@WebTest(description = "Creation SD-02 GVT")
	@TestDocumentation(categories = { "Comm. SD-02", "Corbeille Déclaration MGPP", "Comm. SD-01" })
	public void testCase024_2() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		DeclarationPage declarationPage = mgppPage.getBandeauMenuMgpp().goToDeclaration();
		SujetDetermine sujetDetermine = declarationPage.getBandeauMenuMgppDeclaration().goToSujetDetermine();

		sujetDetermine.assertTrue("Déclaration sur un sujet déterminé (1)");

		sujetDetermine.openCorbeille("Déclaration sur un sujet déterminé (1)");

		MgppDetailCommSD01Page detailCommSD01Page = sujetDetermine.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, MgppDetailCommSD01Page.class);
		detailCommSD01Page.checkValue(MgppDetailCommSD01Page.OBJECT_ID, "objet SD-01");

		final String dateDeclaration = DateUtils.format(Calendar.getInstance());
		final String dateLettrePM = DateUtils.format(Calendar.getInstance());

		MgppCreateCommSD02Page createCommSD02Page = detailCommSD01Page.navigateToCreateCommSucc(EvenementType.SD_02,
				MgppCreateCommSD02Page.class);
		createCommSD02Page.createCommSD02("Assemblée nationale", "objet SD-01", dateDeclaration, dateLettrePM);

		MgppCreateCommPG02Page.sleep(1);

		logoutEpg();
	}

	@WebTest(description = "Creation SD-03 AN")
	@TestDocumentation(categories = { "Comm. SD-02", "Comm. SD-03", "Corbeille EPP" })
	public void testCase024_3() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		CorbeillePage.sleep(1);
		corbeillePage.openCorbeille("Déclarations du Gouvernement");

		DetailCommSD02Page detailCommSD02Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommSD02Page.class);

		CreateCommSD03Page createSD03Page = detailCommSD02Page.navigateToCreateCommSucc(EvenementType.SD_03,
				CreateCommSD03Page.class);
		createSD03Page.createCommSD03("FAVORABLE");

		logoutEpp();

	}

	@WebTest(description = "Creation GENERIQUE 12 GVT")
	@TestDocumentation(categories = { "Corbeille Déclaration MGPP", "Comm. SD-01", "Comm. Générique12" })
	public void testCase024_4() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		DeclarationPage declarationPage = mgppPage.getBandeauMenuMgpp().goToDeclaration();
		SujetDetermine sujetDetermine = declarationPage.getBandeauMenuMgppDeclaration().goToSujetDetermine();

		sujetDetermine.assertTrue("Déclaration sur un sujet déterminé (1)");

		sujetDetermine.openCorbeille("Déclaration sur un sujet déterminé (1)");
		MgppDetailCommSD01Page detailCommSD01Page = sujetDetermine.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, MgppDetailCommSD01Page.class);
		detailCommSD01Page.checkValue(MgppDetailCommSD01Page.OBJECT_ID, "objet SD-01");

		final String destinataire = "Assemblée nationale";
		MgppCreateCommGenerique12Page createCommGenerique12Page = detailCommSD01Page.navigateToCreateCommSucc(
				EvenementType.GENERIQUE_12, MgppCreateCommGenerique12Page.class);
		createCommGenerique12Page.createCommGenerique12("GEN12", destinataire);

		MgppDetailCommGenerique12Page.sleep(1);

		logoutEpg();

	}

	@WebTest(description = "Creation Alerte 12 AN")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. Générique12", "Comm. Alerte12" })
	public void testCase024_5() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		CorbeillePage.sleep(1);
		corbeillePage.openCorbeille("Toutes les communications");

		DetailCommGenerique12Page detailGenerique12Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommGenerique12Page.class);

		CreateCommAlerte12Page createCommAlerte12Page = detailGenerique12Page.navigateToCreateCommSucc(
				EvenementType.ALERTE_12, CreateCommAlerte12Page.class);
		String destinataire = "Gouvernement";
		createCommAlerte12Page.createCommAlerte12Page(destinataire);

		// CreateCommSD03Page createSD03Page = detailCommSD02Page.navigateToCreateCommSucc(EvenementType.SD_03,
		// CreateCommSD03Page.class);
		// createSD03Page.createCommSD03("FAVORABLE");

		logoutEpp();

	}

	@WebTest(description = "Creation Fusion 12 GVT")
	@TestDocumentation(categories = { "Corbeille Déclaration MGPP", "Comm. SD-01", "Comm. Fusion12" })
	public void testCase024_6() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		DeclarationPage declarationPage = mgppPage.getBandeauMenuMgpp().goToDeclaration();
		SujetDetermine sujetDetermine = declarationPage.getBandeauMenuMgppDeclaration().goToSujetDetermine();

		sujetDetermine.openCorbeille("Déclaration sur un sujet déterminé");
		DetailCommAlerte12Page detailCommAlerte12Page = sujetDetermine.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, DetailCommAlerte12Page.class);
		detailCommAlerte12Page.checkValue(DetailCommAlerte12Page.OBJET_ID, "GEN12");

		detailCommAlerte12Page.assertTrue("Alerte - Déclaration sur un sujet déterminé");

		// final String destinataire = "Assemblée nationale";
		// MgppCreateCommFusion12Page createCommFusion12Page =
		// detailCommSD01Page.navigateToCreateCommSucc(EvenementType.FUSION_12, MgppCreateCommFusion12Page.class);
		// createCommFusion12Page.createCommFusion12("123", destinataire);

		MgppDetailCommGenerique12Page.sleep(1);

		logoutEpg();

	}

	@WebTest(description = "Creation SD-01 GVT")
	@TestDocumentation(categories = { "Comm. SD-01" })
	public void testCase025_1() {
		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		CorbeillePage.sleep(3);
		CreateCommSD01Page createCommSD01Page = corbeillePage.navigateToCreateComm(EvenementType.SD_01,
				CreateCommSD01Page.class);

		final String currentDate = DateUtils.format(Calendar.getInstance());
		createCommSD01Page.createCommSD01("ID2_SD-01", "objet senat SD-01", currentDate, "groupeSen");

		logoutEpp();

	}

	@WebTest(description = "Creation SD-02 GVT")
	@TestDocumentation(categories = { "Comm. SD-02", "Corbeille Déclaration MGPP", "Comm. SD-01" })
	public void testCase025_2() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		DeclarationPage declarationPage = mgppPage.getBandeauMenuMgpp().goToDeclaration();
		SujetDetermine sujetDetermine = declarationPage.getBandeauMenuMgppDeclaration().goToSujetDetermine();

		// sujetDetermine.assertTrue("Déclaration sur un sujet déterminé (2)");

		sujetDetermine.openCorbeille("Déclaration sur un sujet déterminé");

		MgppDetailCommSD01Page detailCommSD01Page = sujetDetermine.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, MgppDetailCommSD01Page.class);
		detailCommSD01Page.checkValue(MgppDetailCommSD01Page.OBJECT_ID, "objet senat SD-01");

		final String dateDeclaration = DateUtils.format(Calendar.getInstance());
		final String dateLettrePM = DateUtils.format(Calendar.getInstance());

		MgppCreateCommSD02Page createCommSD02Page = detailCommSD01Page.navigateToCreateCommSucc(EvenementType.SD_02,
				MgppCreateCommSD02Page.class);
		createCommSD02Page.createCommSD02("Sénat", "objet senat SD-01", dateDeclaration, dateLettrePM);

		MgppCreateCommPG02Page.sleep(1);

		logoutEpg();
	}

	@WebTest(description = "Creation SD-03 senat")
	@TestDocumentation(categories = { "Comm. SD-02", "Comm. SD-03" })
	public void testCase025_3() {
		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		CorbeillePage.sleep(1);
		// corbeillePage.openCorbeille("Déclarations du Gouvernement (1)");
		corbeillePage.openCorbeille("Destinataire");

		DetailCommSD02Page detailCommSD02Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommSD02Page.class);

		CreateCommSD03Page createSD03Page = detailCommSD02Page.navigateToCreateCommSucc(EvenementType.SD_03,
				CreateCommSD03Page.class);
		createSD03Page.createCommSD03("FAVORABLE");

		logoutEpp();

	}

	@WebTest(description = "Creation GENERIQUE 12 GVT")
	@TestDocumentation(categories = { "Corbeille Déclaration MGPP", "Comm. SD-01", "Comm. Générique12" })
	public void testCase025_4() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		DeclarationPage declarationPage = mgppPage.getBandeauMenuMgpp().goToDeclaration();
		SujetDetermine sujetDetermine = declarationPage.getBandeauMenuMgppDeclaration().goToSujetDetermine();

		// sujetDetermine.assertTrue("Déclaration sur un sujet déterminé (2)");

		sujetDetermine.openCorbeille("Déclaration sur un sujet déterminé");
		MgppDetailCommSD01Page detailCommSD01Page = sujetDetermine.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, MgppDetailCommSD01Page.class);
		detailCommSD01Page.checkValue(MgppDetailCommSD01Page.OBJECT_ID, "objet senat SD-01");

		final String destinataire = "Sénat";
		MgppCreateCommGenerique12Page createCommGenerique12Page = detailCommSD01Page.navigateToCreateCommSucc(
				EvenementType.GENERIQUE_12, MgppCreateCommGenerique12Page.class);
		createCommGenerique12Page.createCommGenerique12("senat GEN12", destinataire);

		MgppDetailCommGenerique12Page.sleep(1);

		logoutEpg();

	}

	@WebTest(description = "Creation Alerte 12 Senat")
	@TestDocumentation(categories = { "Comm. Générique12", "Comm. Alerte12", "Corbeille EPP" })
	public void testCase025_5() {
		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		CorbeillePage.sleep(1);
		corbeillePage.openCorbeille("Destinataire");

		DetailCommGenerique12Page detailGenerique12Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommGenerique12Page.class);

		CreateCommAlerte12Page crateCommAlerte12Page = detailGenerique12Page.navigateToCreateCommSucc(
				EvenementType.ALERTE_12, CreateCommAlerte12Page.class);
		String destinataire = "Gouvernement";
		crateCommAlerte12Page.createCommAlerte12Page(destinataire);

		logoutEpp();

	}

	@WebTest(description = "Creation Fusion 12 GVT")
	@TestDocumentation(categories = { "Corbeille Déclaration MGPP", "Comm. SD-01", "Comm. Fusion12" })
	public void testCase025_6() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		DeclarationPage declarationPage = mgppPage.getBandeauMenuMgpp().goToDeclaration();
		SujetDetermine sujetDetermine = declarationPage.getBandeauMenuMgppDeclaration().goToSujetDetermine();

		// sujetDetermine.assertTrue("Déclaration sur un sujet déterminé (2)");

		sujetDetermine.openCorbeille("Déclaration sur un sujet déterminé");
		MgppDetailCommSD01Page detailCommSD01Page = sujetDetermine.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, MgppDetailCommSD01Page.class);
		detailCommSD01Page.checkValue(MgppDetailCommSD01Page.OBJECT_ID, "senat GEN12");

		detailCommSD01Page.assertTrue("Alerte - Déclaration sur un sujet déterminé");

		// final String destinataire = "Sénat";
		// MgppCreateCommFusion12Page createCommFusion12Page =
		// detailCommSD01Page.navigateToCreateCommSucc(EvenementType.FUSION_12, MgppCreateCommFusion12Page.class);
		// createCommFusion12Page.createCommFusion12("123", destinataire);

		MgppDetailCommGenerique12Page.sleep(1);

		logoutEpg();

	}

	@WebTest(description = "Creation JSS01 GVT")
	@TestDocumentation(categories = { "Corbeille Organisation MGPP", "Comm. JSS-01" })
	public void testCase026_1() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		OrganisationPage organisationPage = mgppPage.getBandeauMenuMgpp().goToOrganisationPage();
		JoursSupplementairesSeancePage joursSupplementairesSeance = organisationPage.getBandeauMenuMgppOrganisation()
				.goToJoursSupplementairesSeance();

		MgppCreateCommJSS01Page createCommJSS01Page = joursSupplementairesSeance.navigateToCreateComm(
				EvenementType.JSS_01, MgppCreateCommJSS01Page.class);

		final String dateLettrePM = DateUtils.format(Calendar.getInstance());
		MgppDetailCommJSS01Page mgppDetailCommJSS01Page = createCommJSS01Page.createCommJSS01("objet JSS01",
				"Assemblée nationale", dateLettrePM);
		mgppDetailCommJSS01Page.checkValue(MgppDetailCommPG01Page.OBJECT_ID, "objet JSS01");

		logoutEpg();

	}

	@WebTest(description = "Creation GENERIQUE 13 AN")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. JSS-01", "Comm. Générique13" })
	public void testCase026_2() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		CorbeillePage.sleep(1);
		corbeillePage.openCorbeille("Organisation session (1)");

		DetailCommJSS01Page detailCommJSS01Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommJSS01Page.class);

		CreateCommGenerique13Page createCommGenerique13Page = detailCommJSS01Page.navigateToCreateCommSucc(
				EvenementType.GENERIQUE_13, CreateCommGenerique13Page.class);
		String destinataire = "Gouvernement";
		createCommGenerique13Page.createCommGenerique13(destinataire);

		logoutEpp();

	}

	@WebTest(description = "Creation Alerte13 GVT")
	@TestDocumentation(categories = { "Corbeille Organisation MGPP", "Comm. Générique13", "Comm. Alerte13" })
	public void testCase026_3() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		OrganisationPage organisationPage = mgppPage.getBandeauMenuMgpp().goToOrganisationPage();
		JoursSupplementairesSeancePage joursSupplementairesSeance = organisationPage.getBandeauMenuMgppOrganisation()
				.goToJoursSupplementairesSeance();

		joursSupplementairesSeance.openCorbeille("Article 28-3C (1)");

		MgppDetailCommGenerique13Page detailCommGenerique13Page = joursSupplementairesSeance.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, MgppDetailCommGenerique13Page.class);
		detailCommGenerique13Page.checkValue(MgppDetailCommSD01Page.OBJECT_ID, "objet JSS01");

		detailCommGenerique13Page.assertTrue("Generique - Demande de jours supplémentaires de séance");

		final String destinataire = "Assemblée nationale";
		MgppCreateCommAlerte13Page createCommAlerte13Page = detailCommGenerique13Page.navigateToCreateCommSucc(
				EvenementType.ALERTE_13, MgppCreateCommAlerte13Page.class);
		createCommAlerte13Page.createCommAlerte13(destinataire);

		MgppDetailCommGenerique12Page.sleep(1);

		logoutEpg();
	}

	@WebTest(description = "Creation JSS02 AN")
	@TestDocumentation(categories = { "Comm. JSS-02" })
	public void testCase026_4() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		CorbeillePage.sleep(3);
		CreateCommJSS02Page createCommJSS02Page = corbeillePage.navigateToCreateComm(EvenementType.JSS_02,
				CreateCommJSS02Page.class);
		createCommJSS02Page.createCommJSS02("ID1_JSS-02", "objet JSS-02");

		logoutEpp();

	}

	@WebTest(description = "Creation JSS02 GVT")
	@TestDocumentation(categories = { "Corbeille Organisation MGPP", "Comm. JSS-02" })
	public void testCase026_5() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		OrganisationPage organisationPage = mgppPage.getBandeauMenuMgpp().goToOrganisationPage();
		JoursSupplementairesSeancePage joursSupplementairesSeance = organisationPage.getBandeauMenuMgppOrganisation()
				.goToJoursSupplementairesSeance();

		joursSupplementairesSeance.openCorbeille("Article 28-3C (1)");

		MgppDetailCommJSS02Page detailCommJSS02Page = joursSupplementairesSeance.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, MgppDetailCommJSS02Page.class);
		detailCommJSS02Page.checkValue(MgppDetailCommJSS02Page.OBJECT_ID, "objet JSS-02");
		detailCommJSS02Page.checkValue(MgppDetailCommJSS02Page.ID_DOSSIER, "ID1_JSS-02");

		logoutEpg();

	}

	@WebTest(description = "Creation JSS01 GVT")
	@TestDocumentation(categories = { "Comm. JSS-01", "Corbeille Organisation MGPP" })
	public void testCase027_1() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		OrganisationPage organisationPage = mgppPage.getBandeauMenuMgpp().goToOrganisationPage();
		JoursSupplementairesSeancePage joursSupplementairesSeance = organisationPage.getBandeauMenuMgppOrganisation()
				.goToJoursSupplementairesSeance();

		MgppCreateCommJSS01Page createCommJSS01Page = joursSupplementairesSeance.navigateToCreateComm(
				EvenementType.JSS_01, MgppCreateCommJSS01Page.class);

		final String date = DateUtils.format(Calendar.getInstance());
		MgppDetailCommJSS01Page mgppDetailCommJSS01Page = createCommJSS01Page.createCommJSS01("objet JSS01", "Sénat",
				date);
		mgppDetailCommJSS01Page.checkValue(MgppDetailCommPG01Page.OBJECT_ID, "objet JSS01");

		logoutEpg();

	}

	@WebTest(description = "Creation GENERIQUE 13 Sénat")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. JSS-02", "Comm. Générique13" })
	public void testCase027_2() {
		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		CorbeillePage.sleep(1);
		corbeillePage.openCorbeille("Destinataire");

		DetailCommJSS02Page detailCommJSS02Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommJSS02Page.class);

		CreateCommGenerique13Page createCommGenerique13Page = detailCommJSS02Page.navigateToCreateCommSucc(
				EvenementType.GENERIQUE_13, CreateCommGenerique13Page.class);
		String destinataire = "Gouvernement";
		createCommGenerique13Page.createCommGenerique13(destinataire);

		logoutEpp();

	}

	@WebTest(description = "Creation Alerte13 GVT")
	@TestDocumentation(categories = { "Corbeille Organisation MGPP", "Comm. Générique13", "Comm. Alerte13" })
	public void testCase027_3() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		OrganisationPage organisationPage = mgppPage.getBandeauMenuMgpp().goToOrganisationPage();
		JoursSupplementairesSeancePage joursSupplementairesSeance = organisationPage.getBandeauMenuMgppOrganisation()
				.goToJoursSupplementairesSeance();

		joursSupplementairesSeance.openCorbeille("Article 28-3C");

		MgppDetailCommGenerique13Page detailCommGenerique13Page = joursSupplementairesSeance.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, MgppDetailCommGenerique13Page.class);
		detailCommGenerique13Page.checkValue(MgppDetailCommSD01Page.OBJECT_ID, "objet JSS01");

		detailCommGenerique13Page.assertTrue("Generique - Demande de jours supplémentaires de séance");

		final String destinataire = "Sénat";
		MgppCreateCommAlerte13Page createCommAlerte13Page = detailCommGenerique13Page.navigateToCreateCommSucc(
				EvenementType.ALERTE_13, MgppCreateCommAlerte13Page.class);
		createCommAlerte13Page.createCommAlerte13(destinataire);

		MgppDetailCommGenerique12Page.sleep(1);

		logoutEpg();
	}

	@WebTest(description = "Creation JSS02 Senat")
	@TestDocumentation(categories = { "Comm. JSS-02" })
	public void testCase027_4() {
		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		CorbeillePage.sleep(3);
		CreateCommJSS02Page createCommJSS02Page = corbeillePage.navigateToCreateComm(EvenementType.JSS_02,
				CreateCommJSS02Page.class);
		createCommJSS02Page.createCommJSS02("ID1_JSS-02", "objet JSS-02");

		logoutEpp();

	}

	@WebTest(description = "Creation JSS02 GVT")
	@TestDocumentation(categories = { "Corbeille Organisation MGPP", "Comm. JSS-02" })
	public void testCase027_5() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		OrganisationPage organisationPage = mgppPage.getBandeauMenuMgpp().goToOrganisationPage();
		JoursSupplementairesSeancePage joursSupplementairesSeance = organisationPage.getBandeauMenuMgppOrganisation()
				.goToJoursSupplementairesSeance();

		joursSupplementairesSeance.openCorbeille("Article 28-3C");

		MgppDetailCommJSS02Page detailCommJSS02Page = joursSupplementairesSeance.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, MgppDetailCommJSS02Page.class);
		detailCommJSS02Page.checkValue(MgppDetailCommJSS02Page.OBJECT_ID, "objet JSS-02");
		detailCommJSS02Page.checkValue(MgppDetailCommJSS02Page.ID_DOSSIER, "ID1_JSS-02");

		logoutEpg();
	}

	@WebTest(description = "Creation AUD-01  GVT")
	@TestDocumentation(categories = { "Corbeille Nomination MGPP", "Comm. AUD-01", "FDD" })
	public void testCase028_1() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		NominationPage nominationPage = mgppPage.getBandeauMenuMgpp().goToNomination();
		DemandesAuditionPage demandesAuditionPage = nominationPage.getBandeauMenuMgppNomination()
				.goToDemandesaudition();

		AjouterAuditionPage ajouterAuditionPage = (AjouterAuditionPage) demandesAuditionPage.navigateToPageFromButton(
				"Ajouter Audition", AjouterAuditionPage.class);
		ajouterAuditionPage.setNomOrganisme("Nom Audition 1");
		ajouterAuditionPage.setBaseLegale("baseLegale Audition 1");
		ajouterAuditionPage.enregistrer();

		demandesAuditionPage.openCorbeille("Liste des demandes d'audition");
		PageFichePresentationAudition PageFichePresentationAudition = demandesAuditionPage
				.openRecordByIdFromList(
						"fiche_presentation_aud_list:nxl_fiche_presentation_AUD_listing_1:nxw_fiche_AUD_idDossier_listing_1:msgName",
						PageFichePresentationAudition.class);

		FondDossierOngletNomination fondDossierOnglet = PageFichePresentationAudition.navigateToAnotherTab(
				"Fond de dossier", FondDossierOngletNomination.class);
		fondDossierOnglet.ajouterUnfichier("src/main/attachments/piece-jointe.doc");
		logoutEpg();
	}

	@WebTest(description = "Creation AUD-01  GVT")
	@TestDocumentation(categories = { "Comm. AUD-01", "Corbeille Nomination MGPP", "FDD" })
	public void testCase028_1_1() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		NominationPage nominationPage = mgppPage.getBandeauMenuMgpp().goToNomination();
		DemandesAuditionPage demandesAuditionPage = nominationPage.getBandeauMenuMgppNomination()
				.goToDemandesaudition();

		demandesAuditionPage.openCorbeille("Demandes d’audition");
		demandesAuditionPage.assertTrue("Sénat");
		demandesAuditionPage.assertTrue("Assemblée nationale");

		MgppDetailCommAUD01Page detailCommAUD01Page = demandesAuditionPage.openRecordByIdFromList(
				"corbeille_message_list:nxl_corbeille_message_listing_layout_2:nxw_idDossier_widget_2:msgName",
				MgppDetailCommAUD01Page.class);

		MgppCreateCommAUD01Page createCommAUD01Page = detailCommAUD01Page.modifier();
		createCommAUD01Page.addPieceJointe(PieceJointeType.LETTRE_PM, "PJPMPourAVIAN",
				"http://PJPMVANPourAVIAN.solon-epg.fr", "src/main/attachments/piece-jointe.doc");
		createCommAUD01Page.setPersonneAuditionne("personneAuditionne");
		createCommAUD01Page.setDateLettrePM(DateUtils.format(Calendar.getInstance()));
		createCommAUD01Page.setFonction("fonction");
		createCommAUD01Page.publier();

		logoutEpg();
	}

	@WebTest(description = "Creation AUD02 AN")
	@TestDocumentation(categories = { "Comm. AUD-01", "Comm. AUD-02", "Corbeille EPP" })
	public void testCase028_2() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		CorbeillePage.sleep(3);
		corbeillePage.openCorbeille("Nominations et auditions");
		DetailCommAUD01Page detailCommDoc01Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommAUD01Page.class);
		CreateCommAUD02Page createCommAUD02Page = detailCommDoc01Page.navigateToCreateCommSucc(EvenementType.AUD_02,
				CreateCommAUD02Page.class);
		createCommAUD02Page.createCommAUD02("NOM");

		logoutEpp();
	}

	@WebTest(description = "Creation GENERIQUE14 GOUV")
	@TestDocumentation(categories = { "Corbeille Nomination MGPP", "Comm. AUD-02", "Comm. Générique14" })
	public void testCase028_3() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		NominationPage nominationPage = mgppPage.getBandeauMenuMgpp().goToNomination();
		DemandesAuditionPage demandesAuditionPage = nominationPage.getBandeauMenuMgppNomination()
				.goToDemandesaudition();

		demandesAuditionPage.openCorbeille("Demandes d’audition");

		MgppDetailCommAUD02Page detailCommAUD02Page = demandesAuditionPage.openRecordByIdFromList(
				"corbeille_message_list:nxl_corbeille_message_listing_layout_1:nxw_idDossier_widget_1:msgName",
				MgppDetailCommAUD02Page.class);
		MgppCreateCommGenerique14Page createCommGenerique14Page = detailCommAUD02Page.navigateToCreateCommSucc(
				EvenementType.GENERIQUE_14, MgppCreateCommGenerique14Page.class);
		final String destinataire = "Assemblée nationale";
		createCommGenerique14Page.createCommGenerique14(destinataire);
		logoutEpg();
	}

	@WebTest(description = "Creation ALERTE14 AN")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. Générique14", "Comm. Alerte14" })
	public void testCase028_4() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		CorbeillePage.sleep(3);

		corbeillePage.openCorbeille("Nominations et auditions");
		DetailCommGenerique14Page detailCommGenerique14Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommGenerique14Page.class);
		CreateCommAlerte14Page createCommGenerique14Page = detailCommGenerique14Page.navigateToCreateCommSucc(
				EvenementType.ALERTE_14, CreateCommAlerte14Page.class);
		String destinataire = "Gouvernement";
		createCommGenerique14Page.createCommAlerte14Page(destinataire);
		logoutEpg();
	}

	@WebTest(description = "verification alerte14 GVT")
	@TestDocumentation(categories = { "Corbeille Nomination MGPP", "Comm. Alerte14" })
	public void testCase028_5() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		NominationPage nominationPage = mgppPage.getBandeauMenuMgpp().goToNomination();
		DemandesAuditionPage demandesAuditionPage = nominationPage.getBandeauMenuMgppNomination()
				.goToDemandesaudition();

		demandesAuditionPage.openCorbeille("Demandes d’audition");

		MgppDetailCommAlerte14Page detailCommAlerte14Page = demandesAuditionPage.openRecordByIdFromList(
				"corbeille_message_list:nxl_corbeille_message_listing_layout_1:nxw_idDossier_widget_1:msgName",
				MgppDetailCommAlerte14Page.class);
		detailCommAlerte14Page.checkValue(MgppDetailCommAlerte14Page.OBJECT_ID, "Nom Audition 1");
		MgppDetailCommAlerte14Page.sleep(1);
		logoutEpg();
	}

	@WebTest(description = "publication AUD01 GVT-> Senat")
	@TestDocumentation(categories = { "Corbeille Nomination MGPP", "Comm. AUD-01", "FDD" })
	public void testCase028_6() {

		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		NominationPage nominationPage = mgppPage.getBandeauMenuMgpp().goToNomination();
		DemandesAuditionPage demandesAuditionPage = nominationPage.getBandeauMenuMgppNomination()
				.goToDemandesaudition();
		demandesAuditionPage.doRapidSearch("Demandes d’audition", "AUD0000");
		TraitementPage.sleep(2);
		MgppDetailCommAUD01Page detailCommAUD01Page = demandesAuditionPage.openRecordByIdFromList(
				"corbeille_message_list:nxl_corbeille_message_listing_layout_2:nxw_idDossier_widget_2:msgName",
				MgppDetailCommAUD01Page.class);

		MgppCreateCommAUD01Page createCommAUD01Page = detailCommAUD01Page.modifier();
		createCommAUD01Page.addPieceJointe(PieceJointeType.LETTRE_PM, "PJPMPourAVIAN",
				"http://PJPMVANPourAVIAN.solon-epg.fr", "src/main/attachments/piece-jointe.doc");
		createCommAUD01Page.setPersonneAuditionne("personneAuditionne");
		createCommAUD01Page.setDateLettrePM(DateUtils.format(Calendar.getInstance()));
		createCommAUD01Page.setFonction("fonction");
		createCommAUD01Page.publier();

		logoutEpg();
	}

	@WebTest(description = "Creation AUD02 SENAT")
	@TestDocumentation(categories = { "Comm. AUD-01", "Corbeille EPP", "Comm. AUD-02", "Recherche rapide EPP" })
	public void testCase028_7() {

		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		CorbeillePage.sleep(3);

		corbeillePage.openCorbeille("Destinataire");

		corbeillePage.openRechercheRapide();

		getFlog().action("Ajout du type de communication DOC01 dans les critères de recherche");
		WebElement typeComm = getDriver().findElement(
				By.id("quickSearchForm:nxl_recherche_rapide_layout:nxw_recherche_type_evenement_participant_list"));
		Select typesSelect = new Select(typeComm);
		typesSelect.selectByValue("AUD01");

		corbeillePage.doRechercheRapide();

		DocumentPage.sleep(3);
		DetailCommAUD01Page detailCommDoc01Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommAUD01Page.class);
		CreateCommAUD02Page createCommAUD02Page = detailCommDoc01Page.navigateToCreateCommSucc(EvenementType.AUD_02,
				CreateCommAUD02Page.class);
		createCommAUD02Page.createCommAUD02("Organisme Name");

		logoutEpp();
	}

	@WebTest(description = "Creation GENERIQUE14 GOUV->SENAT")
	@TestDocumentation(categories = { "Corbeille Nomination MGPP", "Comm. AUD-02", "Comm. Générique14" })
	public void testCase028_8() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		NominationPage nominationPage = mgppPage.getBandeauMenuMgpp().goToNomination();
		DemandesAuditionPage demandesAuditionPage = nominationPage.getBandeauMenuMgppNomination()
				.goToDemandesaudition();

		demandesAuditionPage.openCorbeille("Demandes d’audition");

		MgppDetailCommAUD02Page detailCommAUD02Page = demandesAuditionPage.openRecordByIdFromList(
				"corbeille_message_list:nxl_corbeille_message_listing_layout_1:nxw_idDossier_widget_1:msgName",
				MgppDetailCommAUD02Page.class);
		MgppCreateCommGenerique14Page createCommGenerique14Page = detailCommAUD02Page.navigateToCreateCommSucc(
				EvenementType.GENERIQUE_14, MgppCreateCommGenerique14Page.class);
		final String destinataire = "Sénat";
		createCommGenerique14Page.createCommGenerique14(destinataire);
		logoutEpg();
	}

	@WebTest(description = "Creation ALERTE14 AN")
	@TestDocumentation(categories = { "Corbeille EPP", "Recherche rapide EPP", "Comm. Générique14", "Comm. Alerte14" })
	public void testCase028_9() {
		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		CorbeillePage.sleep(3);

		corbeillePage.openCorbeille("Destinataire");

		corbeillePage.openRechercheRapide();

		getFlog().action("Ajout du type de communication GENERIQUE14 dans les critères de recherche");
		WebElement typeComm = getDriver().findElement(
				By.id("quickSearchForm:nxl_recherche_rapide_layout:nxw_recherche_type_evenement_participant_list"));
		Select typesSelect = new Select(typeComm);
		typesSelect.selectByValue("GENERIQUE14");

		corbeillePage.doRechercheRapide();

		DocumentPage.sleep(3);

		DetailCommGenerique14Page detailCommGenerique14Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommGenerique14Page.class);
		CreateCommAlerte14Page createCommGenerique14Page = detailCommGenerique14Page.navigateToCreateCommSucc(
				EvenementType.ALERTE_14, CreateCommAlerte14Page.class);
		String destinataire = "Gouvernement";
		createCommGenerique14Page.createCommAlerte14Page(destinataire);
		logoutEpg();
	}

	@WebTest(description = "Creation DOC-01  GVT")
	@TestDocumentation(categories = { "Corbeille Rapport MGPP", "Dossier Rapport", "Fiche présentation Document", "FDD" })
	public void testCase029_1() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		RapportPage rapportPage = mgppPage.getBandeauMenuMgpp().goToRapport();
		DocumentPage documentPage = rapportPage.getBandeauMenuMgppRapport().goToDocumment();

		final String dateLettrePM = DateUtils.format(Calendar.getInstance());

		AjouterDossierPage ajouterDossierPage = (AjouterDossierPage) documentPage.navigateToPageFromButton(
				"Ajouter un dossier", AjouterDossierPage.class);
		ajouterDossierPage.setObjet("objet 1");
		ajouterDossierPage.setBaseLegale("baseLegale doc 1");
		ajouterDossierPage.setDateLettrePM(dateLettrePM);
		ajouterDossierPage.addCommission("organisme doc 0004");
		ajouterDossierPage.enregistrer();

		documentPage.openCorbeille("Liste des documents transmis aux assemblées");
		PageFichePresentationDocument pageFichePresentationAudition = documentPage
				.openRecordByIdFromList(
						"fiche_presentation_doc_list:nxl_fiche_presentation_DOC_listing_1:nxw_fiche_DOC_idDossier_listing_1:msgName",
						PageFichePresentationDocument.class);

		FondDossierOngletNomination fondDossierOnglet = pageFichePresentationAudition.navigateToAnotherTab(
				"Fond de dossier", FondDossierOngletNomination.class);
		fondDossierOnglet.ajouterUnfichier("src/main/attachments/piece-jointe.doc");

		logoutEpg();
	}

	@WebTest(description = "Creation DOC-01  GVT")
	@TestDocumentation(categories = { "Corbeille Rapport MGPP", "Comm. DOC-01", "FDD" })
	public void testCase029_1_1() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		RapportPage rapportPage = mgppPage.getBandeauMenuMgpp().goToRapport();
		DocumentPage documentPage = rapportPage.getBandeauMenuMgppRapport().goToDocumment();

		documentPage.openCorbeille("Autres documents transmis aux assemblées");
		documentPage.assertTrue("Sénat");
		documentPage.assertTrue("Assemblée nationale");

		MgppDetailCommDOC01Page detailCommDOC01Page = documentPage.openRecordByIdFromList(
				"corbeille_message_list:nxl_corbeille_message_listing_layout_2:nxw_idDossier_widget_2:msgName",
				MgppDetailCommDOC01Page.class);

		MgppCreateCommDOC01Page createCommDOC01Page = detailCommDOC01Page.modifier();
		createCommDOC01Page.addPieceJointe(PieceJointeType.LETTRE_PM, "PJPMPourAVIAN",
				"http://PJPMVANPourAVIAN.solon-epg.fr", "src/main/attachments/piece-jointe.doc");

		createCommDOC01Page.publier();

		logoutEpg();
	}

	@WebTest(description = "Creation generique 15 AN")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. DOC-01", "Comm. Générique15" })
	public void testCase029_2() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		CorbeillePage.sleep(3);
		corbeillePage.openCorbeille("Rapports au Parlement");
		DetailCommDOC01 detailCommDoc01Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommDOC01.class);

		CreateDOCGenerique createCommGenerique15Page = detailCommDoc01Page.navigateToCreateCommSucc(
				EvenementType.GENERIQUE_15, CreateDOCGenerique.class);
		String destinataire = "Gouvernement";
		createCommGenerique15Page.createCommGenerique15(destinataire);

		logoutEpp();
	}

	@WebTest(description = "Creation Alerte15 GVT")
	@TestDocumentation(categories = { "Corbeille Rapport MGPP", "Comm. Générique15", "Comm. Alerte15" })
	public void testCase029_3() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		RapportPage rapportPage = mgppPage.getBandeauMenuMgpp().goToRapport();
		DocumentPage documentPage = rapportPage.getBandeauMenuMgppRapport().goToDocumment();

		documentPage.openCorbeille("Autres documents transmis aux assemblées");

		MgppDetailCommGenerique15Page detailCommGenerique15Page = documentPage.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, MgppDetailCommGenerique15Page.class);
		detailCommGenerique15Page.checkValue(MgppDetailCommSD01Page.OBJECT_ID, "objet 1");

		detailCommGenerique15Page.assertTrue("Generique - Autres documents transmis aux assemblées - DOC" + getYear()
				+ "0000 - objet 1");

		final String destinataire = "Assemblée nationale";
		MgppCreateCommAlerte15Page createCommAlerte15Page = detailCommGenerique15Page.navigateToCreateCommSucc(
				EvenementType.ALERTE_15, MgppCreateCommAlerte15Page.class);
		createCommAlerte15Page.createCommAlerte15(destinataire);

		MgppDetailCommGenerique15Page.sleep(1);

		logoutEpg();

	}

	@WebTest(description = "creation fusion AN")
	@TestDocumentation(categories = { "Corbeille EPP", "Comm. Alerte15" })
	public void testCase029_4() {
		CorbeillePage corbeillePage = loginEpp("an", "an");
		CorbeillePage.sleep(3);
		corbeillePage.openCorbeille("Rapports au Parlement");
		DetailCommDOCGenerique detailCommGenerique15Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommDOCGenerique.class);
		detailCommGenerique15Page.checkValue("nxw_metadonnees_evenement_libelle",
				"Alerte - Autres documents transmis aux assemblées");
		DetailCommDOCGenerique.sleep(3);
		logoutEpp();
	}

	@WebTest(description = "publicaion DOC-01  GVT - > senat")
	@TestDocumentation(categories = { "Corbeille Rapport MGPP", "Comm. DOC-01", "Recherche rapide MGPP", "Pièce jointe" })
	public void testCase029_5() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		RapportPage rapportPage = mgppPage.getBandeauMenuMgpp().goToRapport();
		DocumentPage documentPage = rapportPage.getBandeauMenuMgppRapport().goToDocumment();

		documentPage.doRapidSearch("Autres documents transmis aux assemblées", "DOC01");

		DocumentPage.sleep(3);

		MgppDetailCommDOC01Page detailCommDOC01Page = documentPage.openRecordByIdFromList(
				"corbeille_message_list:nxl_corbeille_message_listing_layout_1:nxw_idDossier_widget_1:msgName",
				MgppDetailCommDOC01Page.class);
		MgppCreateCommDOC01Page createCommDOC01Page = detailCommDOC01Page.modifier();
		createCommDOC01Page.addPieceJointe(PieceJointeType.LETTRE_PM, "PJPMPourAVIAN",
				"http://PJPMVANPourAVIAN.solon-epg.fr", "src/main/attachments/piece-jointe.doc");

		createCommDOC01Page.publier();

		logoutEpg();
	}

	@WebTest(description = "Creation generique 15 Senat")
	@TestDocumentation(categories = { "Recherche rapide EPP", "Comm. DOC-01", "Comm. Générique15" })
	public void testCase029_6() {
		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		CorbeillePage.sleep(3);

		corbeillePage.openCorbeille("Destinataire");

		corbeillePage.openRechercheRapide();

		getFlog().action("Ajout du type de communication DOC01 dans les critères de recherche");
		WebElement typeComm = getDriver().findElement(
				By.id("quickSearchForm:nxl_recherche_rapide_layout:nxw_recherche_type_evenement_participant_list"));
		Select typesSelect = new Select(typeComm);
		typesSelect.selectByValue("DOC01");

		corbeillePage.doRechercheRapide();

		DocumentPage.sleep(3);
		DetailCommDOC01 detailCommDoc01Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr/td[5]/div/a", DetailCommDOC01.class);

		CreateDOCGenerique createCommGenerique15Page = detailCommDoc01Page.navigateToCreateCommSucc(
				EvenementType.GENERIQUE_15, CreateDOCGenerique.class);
		String destinataire = "Gouvernement";
		createCommGenerique15Page.createCommGenerique15(destinataire);

		logoutEpp();
	}

	@WebTest(description = "Creation Alerte15 GVT -> senat ")
	@TestDocumentation(categories = { "Corbeille Rapport MGPP", "Comm. Générique15", "Comm. Alerte15" })
	public void testCase029_7() {
		TraitementPageForMGGP traitementpage = loginAsAdminEpg();
		traitementpage.waitForPageLoaded(getDriver());
		MgppPage mgppPage = traitementpage.goToMGPP();
		RapportPage rapportPage = mgppPage.getBandeauMenuMgpp().goToRapport();
		DocumentPage documentPage = rapportPage.getBandeauMenuMgppRapport().goToDocumment();

		documentPage.openCorbeille("Autres documents transmis aux assemblées");

		MgppDetailCommGenerique15Page detailCommGenerique15Page = documentPage.openRecordByIdFromList(
				CommonWebPage.MESSAGE_LISTING_ROW_1, MgppDetailCommGenerique15Page.class);
		detailCommGenerique15Page.checkValue(MgppDetailCommSD01Page.OBJECT_ID, "objet 1");

		detailCommGenerique15Page.assertTrue("Generique - Autres documents transmis aux assemblées - DOC" + getYear()
				+ "0000 - objet 1");

		final String destinataire = "Sénat";
		MgppCreateCommAlerte15Page createCommAlerte15Page = detailCommGenerique15Page.navigateToCreateCommSucc(
				EvenementType.ALERTE_15, MgppCreateCommAlerte15Page.class);
		createCommAlerte15Page.createCommAlerte15(destinataire);

		MgppDetailCommGenerique15Page.sleep(1);

		logoutEpg();

	}

	@WebTest(description = "creation fusion Senat")
	@TestDocumentation(categories = { "Corbeille EPP", "Recherche rapide EPP", "Comm. Générique15" })
	public void testCase029_8() {
		CorbeillePage corbeillePage = loginEpp("senat", "senat");
		CorbeillePage.sleep(3);
		corbeillePage.openCorbeille("Destinataire");

		corbeillePage.openRechercheRapide();

		getFlog().action("Ajout du type de communication GENERIQUE15 dans les critères de recherche");
		WebElement typeComm = getDriver().findElement(
				By.id("quickSearchForm:nxl_recherche_rapide_layout:nxw_recherche_type_evenement_participant_list"));
		Select typesSelect = new Select(typeComm);
		typesSelect.selectByValue("GENERIQUE15");
		DetailCommDOCGenerique detailCommGenerique15Page = corbeillePage.openRecordByPathFromList(
				"//*[@id='corbeille_message_list']/div/table/tbody/tr[1]/td[5]/div/a", DetailCommDOCGenerique.class);
		detailCommGenerique15Page.checkValue("nxw_metadonnees_evenement_libelle",
				"Alerte - Autres documents transmis aux assemblées");
		DetailCommDOCGenerique.sleep(3);
		logoutEpp();
	}
}
