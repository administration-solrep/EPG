package fr.dila.solonepg.webtest.webdriver010;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.administration.CreationEtapePage;
import fr.dila.solonepg.page.administration.ParametrageDeLapplicationPage;
import fr.dila.solonepg.page.administration.TablesDeReferencePage;
import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.solonepg.page.creation.CreationDossier100Page;
import fr.dila.solonepg.page.main.onglet.AdministrationEpgPage;
import fr.dila.solonepg.page.main.onglet.CreationPage;
import fr.dila.solonepg.page.main.onglet.PilotageANPage;
import fr.dila.solonepg.page.main.onglet.RecherchePage;
import fr.dila.solonepg.page.main.onglet.SuiviPage;
import fr.dila.solonepg.page.main.onglet.TraitementPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.BordereauPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.DossierDetailMenu;
import fr.dila.solonepg.page.main.onglet.dossier.detail.FeuilleDeRoutePage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.JournalPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.ParapheurPage;
import fr.dila.solonepg.page.recherche.RechercheExpertePage;
import fr.dila.solonepg.webtest.AbstractSolonSuite;
import fr.dila.solonepg.webtest.helper.url.UrlEpgHelper;
import fr.dila.st.annotations.TestDocumentation;
import fr.dila.st.webdriver.model.CommonWebPage.By_enum;
import fr.dila.st.webdriver.ws.WsResultPage;
import fr.sword.naiad.commons.webtest.WebPage;
import fr.sword.naiad.commons.webtest.annotation.WebTest;

public class TestSuiteSubstitution extends AbstractSolonSuite {

	public static final String	USER_ADMIN_FON	= "adminsgg";
	public static final String	USER_ADMIN_MIN	= "bdc";
	public static final String	USER_CONTR_MIN	= "SEC-GEN";

	public static String		NOR_DOSSIER;
	public static String		CURRENT_USER;

	/*
	 * Tests de substitution
	 */

	@WebTest(description = "Substitution fdr d'un dossier / administrateur fonctionnel", order = 10)
	@TestDocumentation(categories = { "FDR", "Dossier" })
	public void TST_SUBSTITUTION_FDR_001() {

		maximizeBrowser();

		CURRENT_USER = USER_ADMIN_FON;

		substitutionFdR("Conseil de la concurrence (Economie)");

		// Log out
		logoutEpg();
	}

	@WebTest(description = "Substitution fdr d'un dossier / administrateur ministériel", order = 11)
	@TestDocumentation(categories = { "FDR", "Dossier" })
	public void TST_SUBSTITUTION_FDR_002() {

		maximizeBrowser();

		CURRENT_USER = USER_ADMIN_MIN;

		substitutionFdR("Bureau des Cabinets (Economie)");

		// Log out
		logoutEpg();
	}

	@WebTest(description = "Substitution fdr d'un dossier / contributeur ministériel", order = 12)
	@TestDocumentation(categories = { "FDR", "Dossier" })
	public void TST_SUBSTITUTION_FDR_003() {

		maximizeBrowser();

		CURRENT_USER = USER_CONTR_MIN;

		substitutionFdR("CGIET (Economie)");

		// Log out
		logoutEpg();
	}

	/*
	 * Tests sur l'onglet Document
	 */

	@WebTest(description = "Gestion de documents dans le parapheur", order = 20)
	@TestDocumentation(categories = { "Dossier", "Parapheur" })
	public void TST_GESTION_DOC_PAR() {

		maximizeBrowser();

		CURRENT_USER = USER_ADMIN_FON;

		/*
		 * Création du dossier pour les 3 tests sur l'onglet document
		 */

		// Créer dossier
		DossierDetailMenu dossierDetailMenu = createDossier("Conseil de la concurrence (Economie)");

		// Ouvrir la page "Bordereau"
		BordereauPage bordereauPage = dossierDetailMenu.goToBordereau();
		bordereauPage.verrouiller();

		// Remplir les champs
		bordereauPage.setTitreActe("Avis test");
		bordereauPage.setNomDuResponsable("Test");
		bordereauPage.setQualiteDuResponsable("Mr");
		bordereauPage.setTelDuResponsable("0123456789");
		bordereauPage.setPrenomDuResponsable("Test");
		bordereauPage.setRubriques("Domaine public");

		// Enregistrer
		bordereauPage.enregistrer();

		// Lancer le Dossier
		FeuilleDeRoutePage feuilleDeRoutePage = dossierDetailMenu.goToFeuilleDeRoute();
		feuilleDeRoutePage.lancerDossier();
		getFlog().actionClickButton("Lancer");
		feuilleDeRoutePage.assertTrue("est lancé");

		// Rechercher le Dossier
		RecherchePage rechPage = feuilleDeRoutePage.rechercheNor(NOR_DOSSIER);
		dossierDetailMenu = rechPage.getDossierDetail(NOR_DOSSIER);
		feuilleDeRoutePage = dossierDetailMenu.goToFeuilleDeRoute();

		// Verrouiller
		feuilleDeRoutePage.verrouiller();

		// Valider pour déclencher l'étape gérée par le ministère AGR
		// Dans TST_VISIBILITE_REP_FDD un contributeur du ministère AGR va pouvoir accéder au dossier
		new WebDriverWait(feuilleDeRoutePage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//input[@title='Valider']")));
		feuilleDeRoutePage.valider();

		// Rechercher le Dossier
		rechPage = feuilleDeRoutePage.rechercheNor(NOR_DOSSIER);
		dossierDetailMenu = rechPage.getDossierDetail(NOR_DOSSIER);
		ParapheurPage parapheurPage = dossierDetailMenu.goToParapheur();
		parapheurPage.assertTrue("Parapheur");

		// Verrouiller
		parapheurPage.verrouiller();

		/*
		 * Ajout de Document
		 */

		// Télécharger "word2003.doc"
		parapheurPage.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']", "word2003.doc");
		// Attendre que le document soit affiché dans l'hiérarchie
		new WebDriverWait(parapheurPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.id("document_properties_parapheur:parapheurTree:0:0::parapheurFichier")));
		// Valider les données du document
		assertFilePresent(parapheurPage, "parapheurTree:0:0", "word2003.doc", "Adminsgg", "1");
		// Cliquer sur "Acte intégral
		parapheurPage.getElementByXpath(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']").click();
		getFlog().actionClickLink("Acte intégral");
		// Vérifier que le menu contextuel n'est pas affiché
		parapheurPage.assertElementNotPresent(By_enum.ID, "document_properties_parapheur:menuParapheurDossier_menu");
		getFlog().check("assertElementNotPresent context menu");

		/*
		 * Renommage de document
		 */

		// Cliquer sur "Renommer"
		contextMenuClick(parapheurPage.getElementByXpath("//span[contains(@id,'parapheurTree:0:0')]"), "Renommer");
		// Remplir le champ "Nom" avec "word.doc"
		fillField(parapheurPage.getElementById("renameFileParapheurForm:fileParapheurName"), "Nom", "word");
		// Cliquer sur "Valider"
		parapheurPage.getElementById("renameFileParapheurForm:validFileParapheurRename").click();
		WebPage.sleep(2);
		// Valider les données du document
		assertFilePresent(parapheurPage, "parapheurTree:0:0", "word.doc", "Adminsgg", "2");

		/*
		 * Nouvelle version de document
		 */

		final File file = new File("src/main/attachments/word2003.doc");
		final File fileIncompatible = new File("src/main/attachments/pdfTest.pdf");
		// Cliquer sur "Nouvelle version"
		contextMenuClick(parapheurPage.getElementByXpath("//span[contains(@id,'parapheurTree:0:0')]"),
				"Nouvelle version");
		parapheurPage.waitForPageSourcePartDisplayed(By.id("editFileParapheurPanelHeader"), WebPage.TIMEOUT_IN_SECONDS);
		// Cliquer sur "Ajouter fichier"
		parapheurPage.getElementById("editFileParapheurForm:uploadFondDeDossierFiles:add2").click();
		getFlog().startAction("Télécharger 'pdfTest.pdf'");
		// Ecrire le path vers le fichier incompatible
		parapheurPage.findElement(By.id("editFileParapheurForm:uploadFondDeDossierFiles:file")).sendKeys(
				fileIncompatible.getAbsolutePath());
		getFlog().endAction();

		// Vérifier que le fichier ne s'affiche pas dans la fenêtre d'ajout de document
		parapheurPage.assertElementNotPresent(By_enum.XPATH,
				"//div[@class='rich-fileupload-name-padding' and contains(text(),'pdfTest.pdf')]");
		getFlog().check("assertElementNotPresent file 'pdfTest.pdf'");
		// Cliquer sur "Ajouter fichier"
		parapheurPage.getElementById("editFileParapheurForm:uploadFondDeDossierFiles:add2").click();
		getFlog().startAction("Télécharger 'word2003.doc'");
		// Ecrire le path vers le fichier
		parapheurPage.findElement(By.id("editFileParapheurForm:uploadFondDeDossierFiles:file")).sendKeys(
				file.getAbsolutePath());
		parapheurPage.waitForPageSourcePart("Téléchargement effectué", WebPage.TIMEOUT_IN_SECONDS);
		getFlog().endAction();
		// Cliquer sur "Valider"
		parapheurPage.getElementById("editFileParapheurForm:validerEditFileParapheurImp").click();
		getFlog().actionClickButton("Valider");
		WebPage.sleep(2);
		// Valider les données du document
		assertFilePresent(parapheurPage, "parapheurTree:0:0", "word2003.doc", "Adminsgg", "3");

		/*
		 * Gestion des versions du document
		 */

		// Cliquer sur "Gestionnaire de version"
		contextMenuClick(parapheurPage.getElementByXpath("//span[contains(@id,'parapheurTree:0:0')]"),
				"Gestionnaire de version");
		new WebDriverWait(parapheurPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.id("parapheurFileVersionListPanelContentTable")));
		// Verifier les données des 3 versions
		parapheurPage.getElementByXpath(String.format("//form[@id='parapheurFileVersionListForm']//tbody/tr[%d]"
				+ "[contains(td[1],'%d') and contains(td[2],'%s') and contains(td[4],'%s')]", 1, 3, "word2003.doc",
				"Adminsgg"));
		getFlog().check(String.format("assertVersionPresent '%d' '%s' by '%s'", 3, "word2003.doc", "Adminsgg"));
		parapheurPage.getElementByXpath(String.format("//form[@id='parapheurFileVersionListForm']//tbody/tr[%d]"
				+ "[contains(td[1],'%d') and contains(td[2],'%s') and contains(td[4],'%s')]", 2, 2, "word.doc",
				"Adminsgg"));
		getFlog().check(String.format("assertVersionPresent '%d' '%s' by '%s'", 2, "word.doc", "Adminsgg"));
		parapheurPage.getElementByXpath(String.format("//form[@id='parapheurFileVersionListForm']//tbody/tr[%d]"
				+ "[contains(td[1],'%d') and contains(td[2],'%s') and contains(td[4],'%s')]", 3, 1, "word2003.doc",
				"Adminsgg"));
		getFlog().check(String.format("assertVersionPresent '%d' '%s' by '%s'", 1, "word2003.doc", "Adminsgg"));
		// Cliquer sur "Fermer"
		parapheurPage.getElementById("parapheurFileVersionListForm:quitterparapheurFileVersionListText").click();
		getFlog().actionClickButton("Fermer");
		WebPage.sleep(2);

		/*
		 * Suppression de document
		 */

		// Cliquer sur "Supprimer"
		contextMenuClick(parapheurPage.getElementByXpath("//span[contains(@id,'parapheurTree:0:0')]"), "Supprimer");
		// Cliquer sur "Supprimer la version courante du document"
		parapheurPage.getElementById("deleteParapheurFileForm:radioButtonChoixSuppression:1").click();
		getFlog().actionClickLink("Supprimer la version courante du document");
		// Cliquer sur "Valider"
		getFlog().actionClickButton("Valider");
		parapheurPage.clickAndRefreshElement(By.id("deleteParapheurFileForm:validerSuppressionText"),
				By.id("document_properties_parapheur:parapheurDocumentViewPanel"), WebPage.class);
		// Vérifier que le document est revenu à la version 2
		assertFilePresent(parapheurPage, "parapheurTree:0:0", "word.doc", "Adminsgg", "2");
		// Cliquer sur "Supprimer"
		contextMenuClick(parapheurPage.getElementByXpath("//span[contains(@id,'parapheurTree:0:0')]"), "Supprimer");
		// Cliquer sur "Supprimer le document et toutes ses versions"
		parapheurPage.getElementById("deleteParapheurFileForm:radioButtonChoixSuppression:0").click();
		getFlog().actionClickLink("Supprimer le document et toutes ses versions");
		// Cliquer sur "Valider"
		getFlog().actionClickButton("Valider");
		parapheurPage.clickAndRefreshElement(By.id("deleteParapheurFileForm:validerSuppressionText"),
				By.id("document_properties_parapheur:parapheurDocumentViewPanel"), WebPage.class);
		// Vérifier que le document n'est plus affiché
		parapheurPage.assertElementNotPresent(By_enum.ID,
				"document_properties_parapheur:parapheurTree:0:0::parapheurFichier");
		getFlog().check("assertElementNotPresent document '0:0' in 'parapheurTree'");

		// Déverrouiller
		parapheurPage.deverouiller_par_alt();

		// Log out
		logoutEpg();
	}

	@WebTest(description = "Gestion de répertoires et documents dans le fond de dossier", order = 21)
	@TestDocumentation(categories = { "Dossier", "FDD" })
	public void TST_GESTION_REP_DOC_FDD() {

		maximizeBrowser();

		// Log in as adminsgg
		TraitementPage traitementPage = loginEpg(USER_ADMIN_FON);

		// Rechercher le Dossier
		RecherchePage rechPage = traitementPage.rechercheNor(NOR_DOSSIER);
		DossierDetailMenu dossierDetailMenu = rechPage.getDossierDetail(NOR_DOSSIER);
		ParapheurPage parapheurPage = dossierDetailMenu.goToParapheur();
		parapheurPage.assertTrue("Fond de Dossier");

		// Verrouiller
		if (!parapheurPage.containsEltLocatedBy(By
				.xpath("//table[@class='dossierActions']//td[contains(text(),'Verrouillé')]"))) {
			// Si pas Verrouilé
			parapheurPage.assertVerrouille();
			parapheurPage.verrouiller();
		}
		parapheurPage.assertVerrouilleAbsent();

		/*
		 * Ajout de répertoire
		 */

		// Cliquer sur 'Divers' dans le fond de Dossier puis "Ajouter répertoire"
		contextMenuClick(
				parapheurPage
						.getElementByXpath("//td[contains(@id,'fondDeDossierTree:3:0:2::fondDeDossierRootFolder:text')]"),
				"Ajouter répertoire");
		// Vérifier que le nouveau répertoire a été créé dans le bon niveau de l'hiérarchie
		parapheurPage
				.getElementByXpath("//table[contains(@id,'fondDeDossierTree:3:0:2:0::')]//td[contains(text(),'Nouveau répertoire')]");
		getFlog().check("assertRepertoirePresent 'Nouveau répertoire' in 'Divers'");

		/*
		 * Renommage de répertoire
		 */

		// Cliquer sur "Renommer"
		contextMenuClick(
				parapheurPage
						.getElementByXpath("//td[contains(@id,'fondDeDossierTree:3:0:2:0::fondDeDossierDeletableFolder:text')]"),
				"Renommer");
		// Remplir le champ "Nom" par "Répertoire Test"
		fillField(parapheurPage.getElementById("renameFolderForm:fileName"), "Nom", "Répertoire Test");
		// Cliquer sur "Valider"
		parapheurPage.getElementById("renameFolderForm:validerRennomage").click();
		getFlog().actionClickButton("Valider");
		// Vérifier que le répertoire a été renommé
		new WebDriverWait(parapheurPage.getDriver(), 15)
				.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath("//table[contains(@id,'fondDeDossierTree:3:0:2:0::')]//td[contains(text(),'Répertoire Test')]")));
		parapheurPage
				.getElementByXpath("//table[contains(@id,'fondDeDossierTree:3:0:2:0::')]//td[contains(text(),'Répertoire Test')]");
		getFlog().check("assertRepertoirePresent 'Répertoire Test'");
		WebPage.sleep(1);

		/*
		 * Ajout de documents
		 */

		final File file1 = new File("src/main/attachments/word2003.doc");
		final File file2 = new File("src/main/attachments/pdfTest.pdf");
		// Cliquer sur "Ajouter document"
		contextMenuClick(
				parapheurPage
						.getElementByXpath("//td[contains(@id,'fondDeDossierTree:3:0:2:0::fondDeDossierDeletableFolder:text')]"),
				"Ajouter document");
		parapheurPage.waitForPageSourcePartDisplayed(By.id("createFileFddPanelHeader"), WebPage.TIMEOUT_IN_SECONDS);

		getFlog().startAction("Télécharger 'word2003.doc'");
		// Ecrire le path vers le fichier 1
		parapheurPage.addAttachmentFDD(file1.getAbsolutePath(), "", false);
		getFlog().endAction();

		getFlog().startAction("Télécharger 'pdfTest.pdf'");
		// Ecrire le path vers le fichier 2
		parapheurPage.addAttachmentFDD(file2.getAbsolutePath(), "0", true);
		getFlog().endAction();

		// Cliquer sur "Valider"
		getFlog().actionClickButton("Valider");

		// Valider les données des documents
		assertFilePresent(parapheurPage, "fondDeDossierTree:3:0:2:0:0::", "pdfTest.pdf", "Adminsgg", "1");
		assertFilePresent(parapheurPage, "fondDeDossierTree:3:0:2:0:1::", "word2003.doc", "Adminsgg", "1");

		/*
		 * Suppression de répertoire
		 */

		// Cliquer sur "Supprimer"
		contextMenuClick(
				parapheurPage
						.getElementByXpath("//td[contains(@id,'fondDeDossierTree:3:0:2:0::fondDeDossierDeletableFolder:text')]"),
				"Supprimer");
		parapheurPage.waitForPageSourcePartDisplayed(By.id("deleteFddNodePanelHeader"), WebPage.TIMEOUT_IN_SECONDS);
		// Cliquer sur "Valider"
		parapheurPage.getElementById("deleteFddNodeForm:validerSuppressionFddNodeImg").click();
		getFlog().actionClickButton("Valider");

		// Déverrouiller
		parapheurPage.deverouiller_par_alt();

		// Log out
		logoutEpg();
	}

	@WebTest(description = "Visibilité des répertoires dans le fond de dossier", order = 22)
	@TestDocumentation(categories = { "Dossier", "FDD" })
	public void TST_VISIBILITE_REP_FDD() {

		maximizeBrowser();

		/*
		 * Log in as Administrateur Fonctionnel
		 */

		TraitementPage traitementPage = loginEpg(USER_ADMIN_FON);
		getFlog().check("Utilisateur connecté : Administrateur fonctionnel");

		// Rechercher le Dossier
		RecherchePage rechPage = traitementPage.rechercheNor(NOR_DOSSIER);
		DossierDetailMenu dossierDetailMenu = rechPage.getDossierDetail(NOR_DOSSIER);
		ParapheurPage parapheurPage = dossierDetailMenu.goToParapheur();
		parapheurPage.assertTrue("Fond de Dossier");

		// Vérifier les visibilités des répertoires
		parapheurPage
				.getElementByXpath("//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire réservé au ministère porteur') and not(contains(text(),'SGG'))]");
		getFlog().check("assertFolderPresent 'Répertoire réservé au ministère porteur'");
		parapheurPage
				.getElementByXpath("//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire réservé au SGG')]");
		getFlog().check("assertFolderPresent 'Répertoire réservé au SGG'");
		parapheurPage
				.getElementByXpath("//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire réservé au ministère porteur et au SGG')]");
		getFlog().check("assertFolderPresent 'Répertoire réservé au ministère porteur et au SGG'");
		parapheurPage
				.getElementByXpath("//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire accessible à tous les utilisateurs')]");
		getFlog().check("assertFolderPresent 'Répertoire accessible à tous les utilisateurs'");
		// Log out
		logoutEpg();

		/*
		 * Log in as Contributeur Ministériel du ministère porteur
		 */

		traitementPage = loginEpg("SEC-BDC");
		getFlog().check("Utilisateur connecté : Contributeur ministériel du ministère porteur");

		// Rechercher le Dossier
		rechPage = traitementPage.rechercheNor(NOR_DOSSIER);
		dossierDetailMenu = rechPage.getDossierDetail(NOR_DOSSIER);
		parapheurPage = dossierDetailMenu.goToParapheur();
		parapheurPage.assertTrue("Fond de Dossier");

		// Vérifier les visibilités des répertoires
		parapheurPage
				.getElementByXpath("//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire réservé au ministère porteur') and not(contains(text(),'SGG'))]");
		getFlog().check("assertFolderPresent 'Répertoire réservé au ministère porteur'");
		parapheurPage.assertElementNotPresent(By_enum.XPATH,
				"//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire réservé au SGG')]");
		getFlog().check("assertFolderNotPresent 'Répertoire réservé au SGG'");
		parapheurPage
				.getElementByXpath("//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire réservé au ministère porteur et au SGG')]");
		getFlog().check("assertFolderPresent 'Répertoire réservé au ministère porteur et au SGG'");
		parapheurPage
				.getElementByXpath("//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire accessible à tous les utilisateurs')]");
		getFlog().check("assertFolderPresent 'Répertoire accessible à tous les utilisateurs'");

		// Log out
		logoutEpg();

		/*
		 * Log in as Contributeur du SGG
		 */

		traitementPage = loginEpg("ACONTR");
		getFlog().check("Utilisateur connecté : Contributeur du SGG");

		// Rechercher le Dossier
		rechPage = traitementPage.rechercheNor(NOR_DOSSIER);
		dossierDetailMenu = rechPage.getDossierDetail(NOR_DOSSIER);
		parapheurPage = dossierDetailMenu.goToParapheur();
		parapheurPage.assertTrue("Fond de Dossier");

		// Vérifier les visibilités des répertoires
		parapheurPage
				.assertElementNotPresent(
						By_enum.XPATH,
						"//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire réservé au ministère porteur') and not(contains(text(),'SGG'))]");
		getFlog().check("assertFolderNotPresent 'Répertoire réservé au ministère porteur'");
		parapheurPage
				.getElementByXpath("//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire réservé au SGG')]");
		getFlog().check("assertFolderPresent 'Répertoire réservé au SGG'");
		parapheurPage
				.getElementByXpath("//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire réservé au ministère porteur et au SGG')]");
		getFlog().check("assertFolderPresent 'Répertoire réservé au ministère porteur et au SGG'");
		parapheurPage
				.getElementByXpath("//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire accessible à tous les utilisateurs')]");
		getFlog().check("assertFolderPresent 'Répertoire accessible à tous les utilisateurs'");

		// Log out
		logoutEpg();

		/*
		 * Log in as Contributeur Ministériel non attaché au ministère porteur
		 */

		traitementPage = loginEpg("CKUNTZ");
		getFlog().check("Utilisateur connecté : Contributeur ministériel non attaché au ministère porteur");

		// Rechercher le Dossier
		rechPage = traitementPage.rechercheNor(NOR_DOSSIER);
		dossierDetailMenu = rechPage.getDossierDetail(NOR_DOSSIER);
		parapheurPage = dossierDetailMenu.goToParapheur();
		parapheurPage.assertTrue("Fond de Dossier");

		// Vérifier les visibilités des répertoires
		parapheurPage
				.assertElementNotPresent(
						By_enum.XPATH,
						"//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire réservé au ministère porteur') and not(contains(text(),'SGG'))]");
		getFlog().check("assertFolderNotPresent 'Répertoire réservé au ministère porteur'");
		parapheurPage.assertElementNotPresent(By_enum.XPATH,
				"//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire réservé au SGG')]");
		getFlog().check("assertFolderNotPresent 'Répertoire réservé au SGG'");
		parapheurPage
				.assertElementNotPresent(
						By_enum.XPATH,
						"//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire réservé au ministère porteur et au SGG')]");
		getFlog().check("assertFolderNotPresent 'Répertoire réservé au ministère porteur et au SGG'");
		parapheurPage
				.getElementByXpath("//td[contains(@id,'fondDeDossierRootFolder:text')]//td[contains(text(),'Répertoire accessible à tous les utilisateurs')]");
		getFlog().check("assertFolderPresent 'Répertoire accessible à tous les utilisateurs'");

		// Log out
		logoutEpg();
	}

	/*
	 * Tests FEV507 - Ergonomie générale
	 */

	@WebTest(description = "Ergonomie Générale - Liste des derniers dossiers", order = 30)
	@TestDocumentation(categories = { "Dossier" })
	public void TST_ERG_GEN_001() {

		maximizeBrowser();

		// Log in
		TraitementPage traitementPage = loginEpg(USER_ADMIN_FON);

		/*
		 * Vérifier la présence du bloc "5 derniers dossiers"
		 */

		// Traitement
		traitementPage.getElementById("derniersDossiersId");
		getFlog().check("assertElementPresent '5 derniers dossiers' in [Traitement]");
		// Création
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();
		new WebDriverWait(creationPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//h4[contains(text(),'Création')]")));
		creationPage.getElementById("derniersDossiersId");
		getFlog().check("assertElementPresent '5 derniers dossiers' in [Creation]");
		// Suivi
		SuiviPage suiviPage = creationPage.getOngletMenu().goToSuivi();
		new WebDriverWait(suiviPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//h4[contains(text(),'Espace de suivi')]")));
		suiviPage.getElementById("derniersDossiersId");
		getFlog().check("assertElementPresent '5 derniers dossiers' in [Suivi]");
		// Recherche
		RecherchePage recherchePage = suiviPage.getOngletMenu().goToRecherche();
		new WebDriverWait(recherchePage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//h4[contains(text(),'Recherche')]")));
		recherchePage.getElementById("derniersDossiersId");
		getFlog().check("assertElementPresent '5 derniers dossiers' in [Recherche]");
		// Pilotage de l'activité normative
		PilotageANPage pilotageANPage = recherchePage.getOngletMenu().goToPilotageAN();
		new WebDriverWait(pilotageANPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//h4[contains(text(),'Pilotage')]")));
		pilotageANPage.getElementById("derniersDossiersId");
		getFlog().check("assertElementPresent '5 derniers dossiers' in [Pilotage de l'activité normative]");
		// Administration
		AdministrationEpgPage administrationEpgPage = pilotageANPage.getOngletMenu().goToAdministration();
		new WebDriverWait(administrationEpgPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//h4[contains(text(),'Administration')]")));
		administrationEpgPage.getElementById("derniersDossiersId");
		getFlog().check("assertElementPresent '5 derniers dossiers' in [Administration]");

		/*
		 * Vérifier l'absence du bloc "5 derniers dossiers" dans "Espace parlementaire"
		 */

		getFlog().startAction("Clique sur un lien Espace parlementaire");
		administrationEpgPage.getElementByXpath("//div[@id='espace_parlementaire']/div/div/a").click();
		getFlog().endAction();
		new WebDriverWait(administrationEpgPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//a[contains(text(),'Procédure législative')]")));
		administrationEpgPage.assertElementNotPresent(By_enum.ID, "derniersDossiersId");
		getFlog().check("assertElementNotPresent '5 derniers dossiers' in [Espace parlementaire]");

		/*
		 * Vérifier que le bloc n'est pas vide
		 */

		traitementPage = administrationEpgPage.getOngletMenu().goToEspaceTraitement();
		new WebDriverWait(traitementPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//h4[contains(text(),'Corbeilles')]")));
		traitementPage.assertElementNotPresent(By_enum.XPATH, "//div[contains(text(),'aucun dossier')]");
		getFlog().check("isNotEmpty '5 derniers dossiers'");

		/*
		 * Verrouillage et déverrouillage de dossiers
		 */

		// 3ème dossier
		String nor1 = traitementPage.getElementByXpath("//div[@id='clipboardCopyDerniersDossier']/div[3]/a").getText()
				.split(" - ")[0];
		recherchePage = traitementPage.rechercheNor(nor1);
		DossierDetailMenu dossierDetailMenu = recherchePage.getDossierDetail(nor1);
		BordereauPage bordereauPage = dossierDetailMenu.goToBordereau();
		// Vérifier qu'il est encore en 3ème position après ouverture
		bordereauPage.getElementByXpath("//div[@id='clipboardCopyDerniersDossier']/div[3]/a[contains(text(),'" + nor1
				+ "')]");
		getFlog().check("'" + nor1 + "' est la 3ème référence dans la liste");
		// Verrouiller
		if (bordereauPage.containsEltLocatedBy(By
				.xpath("//table[@class='dossierActions']//td[contains(text(),'Verrouillé')]"))) {
			// Si déjà verrouilé
			getFlog().check("Dossier déjà verrouillé, le déverrouiller avant de continuer");
			bordereauPage.assertVerrouilleAbsent();
			bordereauPage.deverouiller_par_alt();
		}
		bordereauPage.assertVerrouille();
		bordereauPage.verrouiller();
		new WebDriverWait(bordereauPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//img[@alt='Déverrouiller']")));
		// Vérifier qu'il est en 1ère position
		bordereauPage.getElementByXpath("//div[@id='clipboardCopyDerniersDossier']/div[1]/a[contains(text(),'" + nor1
				+ "')]");
		getFlog().check("'" + nor1 + "' est la 1ère référence dans la liste");

		// 5ème dossier
		String nor2 = traitementPage.getElementByXpath("//div[@id='clipboardCopyDerniersDossier']/div[5]/a").getText()
				.split(" - ")[0];
		recherchePage = traitementPage.rechercheNor(nor2);
		dossierDetailMenu = recherchePage.getDossierDetail(nor2);
		bordereauPage = dossierDetailMenu.goToBordereau();
		// Vérifier qu'il est encore en 5ème position après ouverture
		bordereauPage.getElementByXpath("//div[@id='clipboardCopyDerniersDossier']/div[5]/a[contains(text(),'" + nor2
				+ "')]");
		getFlog().check("'" + nor2 + "' est la 5ème référence dans la liste");
		// Verrouiller
		if (bordereauPage.containsEltLocatedBy(By
				.xpath("//table[@class='dossierActions']//td[contains(text(),'Verrouillé')]"))) {
			// Si déjà verrouilé
			getFlog().check("Dossier déjà verrouillé, le déverrouiller avant de continuer");
			bordereauPage.assertVerrouilleAbsent();
			bordereauPage.deverouiller_par_alt();
		}
		bordereauPage.assertVerrouille();
		bordereauPage.verrouiller();
		new WebDriverWait(bordereauPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//img[@alt='Déverrouiller']")));
		// Vérifier qu'il est en 1ère position
		bordereauPage.getElementByXpath("//div[@id='clipboardCopyDerniersDossier']/div[1]/a[contains(text(),'" + nor2
				+ "')]");
		getFlog().check("'" + nor2 + "' est la première référence dans la liste");
		// Déverrouiller
		bordereauPage.assertVerrouilleAbsent();
		bordereauPage.deverouiller_par_alt();
		new WebDriverWait(bordereauPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//img[@alt='Verrouiller']")));
		// Vérifier qu'il est encore en 1ère position
		bordereauPage.getElementByXpath("//div[@id='clipboardCopyDerniersDossier']/div[1]/a[contains(text(),'" + nor2
				+ "')]");
		getFlog().check("'" + nor2 + "' est la première référence dans la liste");
		// Vérifier que la référence du dossier n'est pas doublonnée
		for (int i = 2; i <= 5; i++)
			bordereauPage.assertElementNotPresent(By_enum.XPATH, "//div[@id='clipboardCopyDerniersDossier']/div[" + i
					+ "]/a[contains(text(),'" + nor2 + "')]");
		getFlog().check("assertElementNotPresent doublon de 'EFIG1800006V'");

		// Dossier avec le nor1
		recherchePage = traitementPage.rechercheNor(nor1);
		dossierDetailMenu = recherchePage.getDossierDetail(nor1);
		bordereauPage = dossierDetailMenu.goToBordereau();
		// Vérifier qu'il est encore en 2ème position après ouverture
		bordereauPage.getElementByXpath("//div[@id='clipboardCopyDerniersDossier']/div[2]/a[contains(text(),'" + nor1
				+ "')]");
		getFlog().check("'" + nor1 + "' est la 2ème référence dans la liste");
		// Déverrouiller
		bordereauPage.assertVerrouilleAbsent();
		bordereauPage.deverouiller_par_alt();
		new WebDriverWait(bordereauPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//img[@alt='Verrouiller']")));
		// Vérifier qu'il est en 1ère position
		bordereauPage.getElementByXpath("//div[@id='clipboardCopyDerniersDossier']/div[1]/a[contains(text(),'" + nor1
				+ "')]");
		getFlog().check("'" + nor1 + "' est la 1ère référence dans la liste");

		/*
		 * Comparer les références de dossiers entre différents utilisateurs
		 */

		// Retenir les références des derniers dossiers de l'Administrateur Fonctionnel
		String[] refs = new String[5];
		for (int i = 0; i <= 4; i++)
			refs[i] = bordereauPage.getElementByXpath(
					"//div[@id='clipboardCopyDerniersDossier']/div[" + (i + 1) + "]/a").getText();
		// Déconnexion
		logoutEpg();
		// Connexion avec "BDC"
		traitementPage = loginEpg(USER_ADMIN_MIN);
		new WebDriverWait(bordereauPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//div[@id='clipboardCopyDerniersDossier']")));
		// Vérifier que les références des derniers dossiers sont différentes
		traitementPage.getElementByXpath("//div[@id='clipboardCopyDerniersDossier' and (not(./div[1]/a[text()='"
				+ refs[0] + "']) or not(./div[2]/a[text()='" + refs[1] + "']) or not(./div[3]/a[text()='" + refs[2]
				+ "']) or not(./div[4]/a[text()='" + refs[3] + "']) or not(./div[5]/a[text()='" + refs[4] + "']))]");
		getFlog().check("différentes références dans '5 derniers dossiers' que pour adminsgg");
		// Déconnexion
		logoutEpg();
		// Reconnexion avec "adminsgg"
		traitementPage = loginEpg(USER_ADMIN_FON);
		new WebDriverWait(bordereauPage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//div[@id='clipboardCopyDerniersDossier']")));
		traitementPage.getElementByXpath("//div[@id='clipboardCopyDerniersDossier' and ./div[1]/a[text()='" + refs[0]
				+ "'] and ./div[2]/a[text()='" + refs[1] + "'] and ./div[3]/a[text()='" + refs[2]
				+ "'] and ./div[4]/a[text()='" + refs[3] + "'] and ./div[5]/a[text()='" + refs[4] + "']]");
		getFlog().check("mêmes références dans '5 derniers dossiers' pour adminsgg");

		// Log out
		logoutEpg();
	}

	@WebTest(description = "Ergonomie Générale - Recherche dans l'organigramme", order = 31)
	@TestDocumentation(categories = { "Recherche", "Dossier", "Administration" })
	public void TST_ERG_GEN_002() {

		maximizeBrowser();

		// Log in
		TraitementPage traitementPage = loginEpg(USER_ADMIN_FON);

		/*
		 * Page de Recherche Simple
		 */

		RecherchePage recherchePage = traitementPage.getOngletMenu().goToRecherche();
		new WebDriverWait(recherchePage.getDriver(), 15).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//h4[contains(text(),'Recherche')]")));

		testFieldSuggestions(recherchePage, "Ministère responsable",
				"requete_all_form:nxl_requeteCriteresPrincipaux:nxw_ministere_responsable_suggest",
				"requete_all_form:nxl_requeteCriteresPrincipaux:nxw_ministere_responsable_findButton",
				"requete_all_form:nxl_requeteCriteresPrincipaux:nxw_ministere_responsable_suggestionBox", "tra",
				"TRAVAIL", "Min. Travail, emploi et santé");

		testFieldSuggestions(recherchePage, "Direction responsable",
				"requete_all_form:nxl_requeteCriteresPrincipaux:nxw_direction_responsable_suggest",
				"requete_all_form:nxl_requeteCriteresPrincipaux:nxw_direction_responsable_findButton",
				"requete_all_form:nxl_requeteCriteresPrincipaux:nxw_direction_responsable_suggestionBox", "agr",
				"AGRICULTURE", "Vigie Agriculture");

		/*
		 * Page d'Ajout de Dossier
		 */

		CreationPage creationPage = recherchePage.getOngletMenu().goToEspaceCreation();
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();

		testFieldSuggestions(creationDossier100Page, "Entité à l'origine du projet",
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_suggest",
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_findButton",
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_suggestionBox", "tra",
				"TRAVAIL", "Min. Travail, emploi et santé");

		testFieldSuggestions(creationDossier100Page, "Direction Concernée",
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_suggest",
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_findButton",
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_suggestionBox", "agr",
				"AGRICULTURE", "Vigie Agriculture");

		/*
		 * Page de Recherche Experte
		 */

		recherchePage = creationDossier100Page.getOngletMenu().goToRecherche();
		RechercheExpertePage rechercheExpertePage = recherchePage.goToRechercheExperte();
		rechercheExpertePage.setReqCategory("dossier");
		WebPage.sleep(1);

		rechercheExpertePage.setReqField("requeteur_dossier_d_dos_directionAttache");
		testFieldSuggestions(
				rechercheExpertePage,
				"Direction rattach.",
				"smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_directionAttache:nxw_requeteur_dossier_d_dos_directionAttache:nxw_orgaWidget_1_suggest",
				"smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_directionAttache:nxw_requeteur_dossier_d_dos_directionAttache:nxw_orgaWidget_1_findButton",
				"smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_directionAttache:nxw_requeteur_dossier_d_dos_directionAttache:nxw_orgaWidget_1_suggestionBox",
				"agr", "AGRICULTURE", "Vigie Agriculture");

		rechercheExpertePage.setReqField("requeteur_dossier_d_dos_directionResp");
		testFieldSuggestions(
				rechercheExpertePage,
				"Direction resp.",
				"smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_directionResp:nxw_requeteur_dossier_d_dos_directionResp:nxw_orgaWidget_3_suggest",
				"smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_directionResp:nxw_requeteur_dossier_d_dos_directionResp:nxw_orgaWidget_3_findButton",
				"smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_directionResp:nxw_requeteur_dossier_d_dos_directionResp:nxw_orgaWidget_3_suggestionBox",
				"agr", "AGRICULTURE", "Vigie Agriculture");

		rechercheExpertePage.setReqField("requeteur_dossier_d_dos_ministereAttache");
		testFieldSuggestions(
				rechercheExpertePage,
				"Ministère rattach.",
				"smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_ministereAttache:nxw_requeteur_dossier_d_dos_ministereAttache:nxw_orgaWidget_5_suggest",
				"smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_ministereAttache:nxw_requeteur_dossier_d_dos_ministereAttache:nxw_orgaWidget_5_findButton",
				"smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_ministereAttache:nxw_requeteur_dossier_d_dos_ministereAttache:nxw_orgaWidget_5_suggestionBox",
				"tra", "TRAVAIL", "Min. Travail, emploi et santé");

		rechercheExpertePage.setReqField("requeteur_dossier_d_dos_ministereResp");
		testFieldSuggestions(
				rechercheExpertePage,
				"Ministère resp.",
				"smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_ministereResp:nxw_requeteur_dossier_d_dos_ministereResp:nxw_orgaWidget_7_suggest",
				"smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_ministereResp:nxw_requeteur_dossier_d_dos_ministereResp:nxw_orgaWidget_7_findButton",
				"smartSearchForm:nxl_nxql_incremental_smart_query:nxw_nxql_incremental_smart_query_widget:nxl_incremental_smart_query_selection:nxl_incremental_smart_query_selection_selectionSubview_requeteur_dossier_d_dos_ministereResp:nxw_requeteur_dossier_d_dos_ministereResp:nxw_orgaWidget_7_suggestionBox",
				"tra", "TRAVAIL", "Min. Travail, emploi et santé");

		/*
		 * Page d'Administration des Tables de Référence
		 */

		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		TablesDeReferencePage tablesDeReferencePage = administrationEpgPage.goToTablesDeReference();

		testFieldSuggestions(
				tablesDeReferencePage,
				"Cabinet du PM",
				"tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_cabinet_pm_select_suggest",
				"tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_cabinet_pm_select_findButton",
				"tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_cabinet_pm_select_suggestionBox",
				"rob", "FRANCK", "Franck ROBINE");

		testFieldSuggestions(
				tablesDeReferencePage,
				"Chargé de mission",
				"tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_charge_mission_select_suggest",
				"tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_charge_mission_select_findButton",
				"tables_reference_properties:nxl_tables_reference_autorites_validation:nxw_charge_mission_select_suggestionBox",
				"tai", "MARION", "Marion TAILLEFER");

		testFieldSuggestions(tablesDeReferencePage, "Signataires",
				"tables_reference_properties:nxl_tables_reference_signataires:nxw_signataire_select_suggest",
				"tables_reference_properties:nxl_tables_reference_signataires:nxw_signataire_select_findButton",
				"tables_reference_properties:nxl_tables_reference_signataires:nxw_signataire_select_suggestionBox",
				"rob", "FRANCK", "Franck ROBINE");

		testFieldSuggestions(tablesDeReferencePage, "Ministère PM",
				"tables_reference_properties:nxl_tables_reference_ministere_pm:nxw_ministerePM_select_suggest",
				"tables_reference_properties:nxl_tables_reference_ministere_pm:nxw_ministerePM_select_findButton",
				"tables_reference_properties:nxl_tables_reference_ministere_pm:nxw_ministerePM_select_suggestionBox",
				"pre", "MINISTRE", "Premier Ministre");

		testFieldSuggestions(tablesDeReferencePage, "Directions du PM autorisées lors de la création du dossier",
				"tables_reference_properties:nxl_tables_reference_ministere_pm:nxw_directionPM_select_suggest",
				"tables_reference_properties:nxl_tables_reference_ministere_pm:nxw_directionPM_select_findButton",
				"tables_reference_properties:nxl_tables_reference_ministere_pm:nxw_directionPM_select_suggestionBox",
				"pre", "MINISTRE", "Cabinet du Premier ministre");

		testFieldSuggestions(tablesDeReferencePage, "Conseil d'État",
				"tables_reference_properties:nxl_tables_reference_ministere_ce:nxw_ministereCe_select_suggest",
				"tables_reference_properties:nxl_tables_reference_ministere_ce:nxw_ministereCe_select_findButton",
				"tables_reference_properties:nxl_tables_reference_ministere_ce:nxw_ministereCe_select_suggestionBox",
				"eil", "CONSTITUTIONNEL", "Conseil constitutionnel");

		testFieldSuggestions(
				tablesDeReferencePage,
				"Liste de diffusion",
				"tables_reference_properties:nxl_tables_reference_vecteur_publication_multiples:nxw_vecteur_publication_multiples_select_suggest",
				"tables_reference_properties:nxl_tables_reference_vecteur_publication_multiples:nxw_vecteur_publication_multiples_select_findButton",
				"tables_reference_properties:nxl_tables_reference_vecteur_publication_multiples:nxw_vecteur_publication_multiples_select_suggestionBox",
				"rob", "FRANCK", "Franck ROBINE");

		testFieldSuggestions(
				tablesDeReferencePage,
				"Cabinet du PM",
				"tables_reference_properties:nxl_tables_reference_signature_cheminCroix:nxw_signature_cheminCroix_cpm_select_suggest",
				"tables_reference_properties:nxl_tables_reference_signature_cheminCroix:nxw_signature_cheminCroix_cpm_select_findButton",
				"tables_reference_properties:nxl_tables_reference_signature_cheminCroix:nxw_signature_cheminCroix_cpm_select_suggestionBox",
				"rob", "FRANCK", "Franck ROBINE");

		testFieldSuggestions(
				tablesDeReferencePage,
				"SGG",
				"tables_reference_properties:nxl_tables_reference_signature_cheminCroix:nxw_signature_cheminCroix_sgg_select_suggest",
				"tables_reference_properties:nxl_tables_reference_signature_cheminCroix:nxw_signature_cheminCroix_sgg_select_findButton",
				"tables_reference_properties:nxl_tables_reference_signature_cheminCroix:nxw_signature_cheminCroix_sgg_select_suggestionBox",
				"tai", "MARION", "Marion TAILLEFER");

		testFieldSuggestions(
				tablesDeReferencePage,
				"Poste de publication",
				"tables_reference_properties:nxl_tables_reference_epreuve_postes:nxw_epreuve_poste_publication_suggest",
				"tables_reference_properties:nxl_tables_reference_epreuve_postes:nxw_epreuve_poste_publication_findButton",
				"tables_reference_properties:nxl_tables_reference_epreuve_postes:nxw_epreuve_poste_publication_suggestionBox",
				"doc", "SERVICE", "Agents du service de documentation");

		testFieldSuggestions(tablesDeReferencePage, "Poste du DAN",
				"tables_reference_properties:nxl_tables_reference_epreuve_postes:nxw_epreuve_poste_dan_suggest",
				"tables_reference_properties:nxl_tables_reference_epreuve_postes:nxw_epreuve_poste_dan_findButton",
				"tables_reference_properties:nxl_tables_reference_epreuve_postes:nxw_epreuve_poste_dan_suggestionBox",
				"dan", "CONTRIBUTEUR", "Contributeur DAN");

		// Log out
		logoutEpg();
	}

	@WebTest(description = "Ergonomie Générale - Alerte mail à personnes extérieures", order = 32)
	@TestDocumentation(categories = { "Recherche", "Administration" })
	public void TST_ERG_GEN_003() {

		maximizeBrowser();

		// Log in
		TraitementPage traitementPage = loginEpg(USER_ADMIN_FON);

		/*
		 * Saisie de noms de domaines externes autorisés
		 */

		AdministrationEpgPage administrationEpgPage = traitementPage.getOngletMenu().goToAdministration();
		ParametrageDeLapplicationPage parametrageDeLapplicationPage = administrationEpgPage
				.goToParametrageDeLapplicationPage();
		new WebDriverWait(parametrageDeLapplicationPage.getDriver(), 15)
				.until(ExpectedConditions.visibilityOfElementLocated(By
						.id("parametrage_application_properties:nxl_parametrage_application_mails:nxw_suffixes_mails_autorises:nxw_suffixes_mails_autorises_input")));

		// @test1.com
		fillField(
				parametrageDeLapplicationPage
						.getElementById("parametrage_application_properties:nxl_parametrage_application_mails:nxw_suffixes_mails_autorises:nxw_suffixes_mails_autorises_input"),
				"Noms de domaine autorisés", "@test1.com");
		parametrageDeLapplicationPage
				.getElementById(
						"parametrage_application_properties:nxl_parametrage_application_mails:nxw_suffixes_mails_autorises:nxw_suffixes_mails_autorises_add_icon")
				.click();
		getFlog().actionClickButton("ajouter");
		WebPage.sleep(1);
		parametrageDeLapplicationPage
				.getElementById("parametrage_application_properties:nxl_parametrage_application_mails:nxw_suffixes_mails_autorises:nxw_suffixes_mails_autorises_message");
		getFlog().check("assertElementPresent message d'erreur 'Le nom de domaine saisi est invalide'");

		// test1.com
		fillField(
				parametrageDeLapplicationPage
						.getElementById("parametrage_application_properties:nxl_parametrage_application_mails:nxw_suffixes_mails_autorises:nxw_suffixes_mails_autorises_input"),
				"Noms de domaine autorisés", "test1.com");
		parametrageDeLapplicationPage
				.getElementById(
						"parametrage_application_properties:nxl_parametrage_application_mails:nxw_suffixes_mails_autorises:nxw_suffixes_mails_autorises_add_icon")
				.click();
		getFlog().actionClickButton("ajouter");
		WebPage.sleep(1);
		parametrageDeLapplicationPage
				.getElementByXpath("//span[contains(@id,'nxw_suffixes_mails_autorises_node') and contains(text(),'test1.com')]");
		getFlog().check("assertElementPresent in noms des domaines autorisés 'test1.com'");

		// test2
		fillField(
				parametrageDeLapplicationPage
						.getElementById("parametrage_application_properties:nxl_parametrage_application_mails:nxw_suffixes_mails_autorises:nxw_suffixes_mails_autorises_input"),
				"Noms de domaine autorisés", "test2");
		parametrageDeLapplicationPage
				.getElementById(
						"parametrage_application_properties:nxl_parametrage_application_mails:nxw_suffixes_mails_autorises:nxw_suffixes_mails_autorises_add_icon")
				.click();
		getFlog().actionClickButton("ajouter");
		WebPage.sleep(1);
		parametrageDeLapplicationPage
				.getElementById("parametrage_application_properties:nxl_parametrage_application_mails:nxw_suffixes_mails_autorises:nxw_suffixes_mails_autorises_message");
		getFlog().check("assertElementPresent message d'erreur 'Le nom de domaine saisi est invalide'");

		// test2.fr (pour vérifier l'ajout de plusieurs noms de domaines)
		fillField(
				parametrageDeLapplicationPage
						.getElementById("parametrage_application_properties:nxl_parametrage_application_mails:nxw_suffixes_mails_autorises:nxw_suffixes_mails_autorises_input"),
				"Noms de domaine autorisés", "test2.fr");
		parametrageDeLapplicationPage
				.getElementById(
						"parametrage_application_properties:nxl_parametrage_application_mails:nxw_suffixes_mails_autorises:nxw_suffixes_mails_autorises_add_icon")
				.click();
		getFlog().actionClickButton("ajouter");
		WebPage.sleep(1);
		parametrageDeLapplicationPage
				.getElementByXpath("//span[contains(@id,'nxw_suffixes_mails_autorises_node') and contains(text(),'test1.com')]");
		getFlog().check("assertElementPresent in noms des domaines autorisés 'test2.fr'");

		// Valider
		parametrageDeLapplicationPage
				.getElementById("parametrage_application_properties:validerParametrageApplication").click();
		getFlog().actionClickButton("Valider");
		WebPage.sleep(3);

		/*
		 * Création d'alerte et saisie d'addresses mail externes
		 */

		RecherchePage recherchePage = parametrageDeLapplicationPage.getOngletMenu().goToRecherche();
		new WebDriverWait(recherchePage.getDriver(), 15).until(ExpectedConditions.visibilityOfElementLocated(By
				.id("requete_all_form:nxl_requeteCriteresPrincipaux:nxw_nor")));
		fillField(recherchePage.getElementById("requete_all_form:nxl_requeteCriteresPrincipaux:nxw_nor"), "NOR", "*");
		recherchePage.getElementById("requete_all_form:generer_alerte").click();
		getFlog().actionClickButton("Éditer une alerte");
		new WebDriverWait(recherchePage.getDriver(), 15).until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath("//h3[contains(text(),'Alerte')]")));
		recherchePage.getElementById("alertForm:nxl_alert:nxw_externalRecipients:nxw_externalRecipients_input");
		getFlog().check("assertElementPresent champ 'Utilisateurs extérieurs'");
		recherchePage.getElementById("alertForm:nxl_alert:nxw_externalRecipients:nxw_externalRecipients_add_icon");
		getFlog().check("assertElementPresent bouton (+) pour 'Utilisateurs extérieurs'");

		// azerty
		fillField(
				recherchePage.getElementById("alertForm:nxl_alert:nxw_externalRecipients:nxw_externalRecipients_input"),
				"Utilisateurs extérieurs", "azerty");
		recherchePage.getElementById("alertForm:nxl_alert:nxw_externalRecipients:nxw_externalRecipients_add_icon")
				.click();
		getFlog().actionClickButton("ajouter");
		WebPage.sleep(1);
		recherchePage
				.getElementByXpath("//span[@id='alertForm:nxl_alert:nxw_externalRecipients:nxw_externalRecipients_message_ext_rec' and contains(text(),'pas valide')]");
		getFlog().check("assertElementPresent message d'erreur 'L'adresse de courrier électronique n'est pas valide'");

		// user1@test1.com
		fillField(
				recherchePage.getElementById("alertForm:nxl_alert:nxw_externalRecipients:nxw_externalRecipients_input"),
				"Utilisateurs extérieurs", "user1@test1.com");
		recherchePage.getElementById("alertForm:nxl_alert:nxw_externalRecipients:nxw_externalRecipients_add_icon")
				.click();
		getFlog().actionClickButton("ajouter");
		WebPage.sleep(1);
		recherchePage
				.getElementByXpath("//span[contains(@id,'nxw_externalRecipients_node') and contains(text(),'user1@test1.com')]");
		getFlog().check("assertElementPresent in utilisateurs extérieurs 'user1@test1.com'");

		// user2@azerty.uio
		fillField(
				recherchePage.getElementById("alertForm:nxl_alert:nxw_externalRecipients:nxw_externalRecipients_input"),
				"Utilisateurs extérieurs", "user2@azerty.uio");
		recherchePage.getElementById("alertForm:nxl_alert:nxw_externalRecipients:nxw_externalRecipients_add_icon")
				.click();
		getFlog().actionClickButton("ajouter");
		WebPage.sleep(1);
		recherchePage
				.getElementByXpath("//span[@id='alertForm:nxl_alert:nxw_externalRecipients:nxw_externalRecipients_message_ext_rec' and contains(text(),'pas autorisé')]");
		getFlog()
				.check("assertElementPresent message d'erreur 'Le nom de domaine pour ce courrier électronique n'est pas autorisé'");

		// user2@test2.fr
		fillField(
				recherchePage.getElementById("alertForm:nxl_alert:nxw_externalRecipients:nxw_externalRecipients_input"),
				"Utilisateurs extérieurs", "user2@test2.fr");
		recherchePage.getElementById("alertForm:nxl_alert:nxw_externalRecipients:nxw_externalRecipients_add_icon")
				.click();
		getFlog().actionClickButton("ajouter");
		WebPage.sleep(1);
		recherchePage
				.getElementByXpath("//span[contains(@id,'nxw_externalRecipients_node') and contains(text(),'user2@test2.fr')]");
		getFlog().check("assertElementPresent in utilisateurs extérieurs 'user2@test2.fr'");

		/*
		 * Suppression des noms de domaines autorisés
		 */

		administrationEpgPage = recherchePage.getOngletMenu().goToAdministration();
		parametrageDeLapplicationPage = administrationEpgPage.goToParametrageDeLapplicationPage();
		parametrageDeLapplicationPage.getElementByXpath(
				"//span[contains(@id,'nxw_suffixes_mails_autorises_node') and contains(text(),'test1.com')]/../a/img")
				.click();
		WebPage.sleep(1);
		getFlog().actionClickButton("(X) test1.com");
		parametrageDeLapplicationPage.getElementByXpath(
				"//span[contains(@id,'nxw_suffixes_mails_autorises_node') and contains(text(),'test2.fr')]/../a/img")
				.click();
		WebPage.sleep(1);
		getFlog().actionClickButton("(X) test2.fr");
		// Valider
		parametrageDeLapplicationPage
				.getElementById("parametrage_application_properties:validerParametrageApplication").click();
		getFlog().actionClickButton("Valider");

		// Log out
		logoutEpg();
	}

	@WebTest(description = "Publication Conjointe", order = 40)
	@TestDocumentation(categories = { "Publication", "Dossier", "Bordereau" })
	public void TST_PUB_CJT() {

		maximizeBrowser();

		final String PUB_CJT_XPATH = "//form[@id='bordereauForm']/div[2]/div[1]/table[6]/tbody/tr[4]/td[3]/div";
		final String PUB_CJT_SPAN_ID = "bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_pubPanel";

		// Log in
		TraitementPage traitementPage = loginEpg(USER_ADMIN_FON);

		/*
		 * Création des dossiers
		 */

		final String NOR1 = createDossier(traitementPage, "Avis");
		final String NOR2 = createDossier(traitementPage, "Avis");
		final String NOR3 = createDossier(traitementPage, "Avis");
		final String NOR4 = createDossier(traitementPage, "Avis");

		/*
		 * Saisie multiple de NOR
		 */

		// Ouvrir le dossier 1
		RecherchePage recherchePage = traitementPage.rechercheNor(NOR1);
		DossierDetailMenu dossierDetailMenu = recherchePage.getDossierDetail(NOR1);
		BordereauPage bordereauPage = dossierDetailMenu.goToBordereau();
		// Vérifier qu'il n'y a pas de publication conjointe
		bordereauPage.getElementByXpath(PUB_CJT_XPATH + "[not(contains(text(),'" + NOR2
				+ "')) and not(contains(text(),'" + NOR3 + "')) and not(contains(text(),'" + NOR4 + "'))]");
		getFlog().check("Pas de publication conjointe");
		// Verrouiller le dossier 1
		bordereauPage.verrouiller();
		// Réaliser une saisie inacceptable dans le champ 'Publication conjointe'
		fillField(
				bordereauPage
						.getElementById("bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_inputPub"),
				"Publication conjointe", NOR2 + "/" + NOR3 + "/" + NOR4);
		bordereauPage
				.getElementById("bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_addPubImage")
				.click();
		new WebDriverWait(bordereauPage.getDriver(), 5).until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath("//span[@id='" + PUB_CJT_SPAN_ID
						+ "']/span[@class='error' and contains(text(),'Aucun dossier portant le(s) NOR(s)')]")));
		getFlog().check(
				"assertErrorDisplayed 'Aucun dossier portant le(s) NOR(s) " + NOR2 + "/" + NOR3 + "/" + NOR4 + "'");
		// Saisir plusieurs NORs corrects et incorrects dans le champ 'Publication conjointe'
		fillField(
				bordereauPage
						.getElementById("bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_inputPub"),
				"Publication conjointe", "AZER1234567T-" + NOR2 + "-QSDF8901234G");
		bordereauPage
				.getElementById("bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_addPubImage")
				.click();
		WebPage.sleep(1);
		new WebDriverWait(bordereauPage.getDriver(), 5).until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath("//span[@id='" + PUB_CJT_SPAN_ID
						+ "']/span[@class='error' and contains(text(),'Aucun dossier portant le(s) NOR(s)')]")));
		bordereauPage.getElementByXpath("//span[@id='" + PUB_CJT_SPAN_ID + "' and contains(.,'" + NOR2 + "')]");
		getFlog().check("assertErrorDisplayed 'Aucun dossier portant le(s) NOR(s) AZER1234567T, QSDF8901234G'");
		getFlog().action("Ajout du NOR");
		// Saisir plusieurs NORs dans le champ 'Publication conjointe'
		fillField(
				bordereauPage
						.getElementById("bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_inputPub"),
				"Publication conjointe", NOR3 + ";" + NOR4);
		bordereauPage
				.getElementById("bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_addPubImage")
				.click();
		WebPage.sleep(1);
		bordereauPage.getElementByXpath("//span[@id='" + PUB_CJT_SPAN_ID + "' and contains(.,'" + NOR3
				+ "') and contains(.,'" + NOR4 + "')]");
		getFlog().action("Ajout des 2 NORs");
		// Enregistrer
		bordereauPage.enregistrer();
		getFlog().action("Enregistrement");
		// Ouvrir le dossier 2
		recherchePage = traitementPage.rechercheNor(NOR2);
		dossierDetailMenu = recherchePage.getDossierDetail(NOR2);
		bordereauPage = dossierDetailMenu.goToBordereau();
		// Vérifier qu'il y a une publication conjointe avec les 3 autres dossiers
		bordereauPage.getElementByXpath(PUB_CJT_XPATH + "[contains(text(),'" + NOR1 + "') and contains(text(),'" + NOR3
				+ "') and contains(text(),'" + NOR4 + "')]");
		getFlog().check("'Publication conjointe' avec les 3 autres dossiers");
		// Ouvrir le dossier 3
		recherchePage = traitementPage.rechercheNor(NOR3);
		dossierDetailMenu = recherchePage.getDossierDetail(NOR3);
		bordereauPage = dossierDetailMenu.goToBordereau();
		// Vérifier qu'il y a une publication conjointe avec les 3 autres dossiers
		bordereauPage.getElementByXpath(PUB_CJT_XPATH + "[contains(text(),'" + NOR1 + "') and contains(text(),'" + NOR2
				+ "') and contains(text(),'" + NOR4 + "')]");
		getFlog().check("'Publication conjointe' avec les 3 autres dossiers");
		// Ouvrir le dossier 4
		recherchePage = traitementPage.rechercheNor(NOR4);
		dossierDetailMenu = recherchePage.getDossierDetail(NOR4);
		bordereauPage = dossierDetailMenu.goToBordereau();
		// Vérifier qu'il y a une publication conjointe avec les 3 autres dossiers
		bordereauPage.getElementByXpath(PUB_CJT_XPATH + "[contains(text(),'" + NOR1 + "') and contains(text(),'" + NOR2
				+ "') and contains(text(),'" + NOR3 + "')]");
		getFlog().check("'Publication conjointe' avec les 3 autres dossiers");

		/*
		 * Suppression de NOR
		 */

		// Verrouiller le dossier 4
		bordereauPage.verrouiller();
		WebPage.sleep(1);
		// Supprimer NOR1
		getFlog().startAction("Suppression du NOR1");
		String[] nors = bordereauPage.getElementById(PUB_CJT_SPAN_ID).getText().split("\n");
		for (int i = 0; i < nors.length; i++)
			if (nors[i].equals(NOR1)) {
				bordereauPage.getElementById(
						"bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_indexLabel_" + i).click();
				break;
			}
		WebPage.sleep(1);
		bordereauPage.getElementByXpath("//span[@id='" + PUB_CJT_SPAN_ID + "' and not(contains(text(),'" + NOR1
				+ "'))]");
		getFlog().endAction();
		// Supprimer NOR2
		getFlog().startAction("Suppression du NOR2");
		nors = bordereauPage.getElementById(PUB_CJT_SPAN_ID).getText().split("\n");
		for (int i = 0; i < nors.length; i++)
			if (nors[i].equals(NOR2)) {
				bordereauPage.getElementById(
						"bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_indexLabel_" + i).click();
				break;
			}
		WebPage.sleep(1);
		bordereauPage.getElementByXpath("//span[@id='" + PUB_CJT_SPAN_ID + "' and not(contains(text(),'" + NOR2
				+ "'))]");
		getFlog().endAction();
		// Enregistrer
		bordereauPage.enregistrer();
		getFlog().action("Enregistrement");
		// Ouvrir le dossier 3
		recherchePage = traitementPage.rechercheNor(NOR3);
		dossierDetailMenu = recherchePage.getDossierDetail(NOR3);
		bordereauPage = dossierDetailMenu.goToBordereau();
		// Vérifier qu'il y a une publication conjointe avec le dossier 4
		bordereauPage.getElementByXpath(PUB_CJT_XPATH + "[not(contains(text(),'" + NOR1
				+ "')) and not(contains(text(),'" + NOR2 + "')) and contains(text(),'" + NOR4 + "')]");
		getFlog().check("'Publication conjointe' avec le dossier 4");
		// Ouvrir le dossier 2
		recherchePage = traitementPage.rechercheNor(NOR2);
		dossierDetailMenu = recherchePage.getDossierDetail(NOR2);
		bordereauPage = dossierDetailMenu.goToBordereau();
		// Vérifier qu'il n'y a pas de publication conjointe
		bordereauPage.getElementByXpath(PUB_CJT_XPATH + "[not(contains(text(),'" + NOR1
				+ "')) and not(contains(text(),'" + NOR3 + "')) and not(contains(text(),'" + NOR4 + "'))]");
		getFlog().check("Pas de publication conjointe");

		/*
		 * Propagation des métadonnées de publications conjointes
		 */

		// Vérifier les valeurs des champs de publication
		bordereauPage
				.getElementByXpath("//form[@id='bordereauForm']/div[2]/div[1]/table[6]/tbody/tr[5]/td[3][contains(text(),'Electronique')]");
		getFlog().check("assertTrue 'Mode parution': Electronique");
		bordereauPage
				.getElementByXpath("//form[@id='bordereauForm']/div[2]/div[1]/table[6]/tbody/tr[6]/td[3][not(normalize-space())]");
		getFlog().check("assertTrue 'Délai publication' est vide");
		bordereauPage
				.getElementByXpath("//form[@id='bordereauForm']/div[2]/div[1]/table[6]/tbody/tr[7]/td[3][not(normalize-space())]");
		getFlog().check("assertTrue 'Publication à date précisée' est vide");
		// Ouvrir le dossier 1
		recherchePage = traitementPage.rechercheNor(NOR1);
		dossierDetailMenu = recherchePage.getDossierDetail(NOR1);
		bordereauPage = dossierDetailMenu.goToBordereau();
		// Vérifier qu'il n'y a pas de publication conjointe
		bordereauPage.getElementByXpath("//span[@id='" + PUB_CJT_SPAN_ID + "' and not(contains(.,'" + NOR2
				+ "')) and not(contains(.,'" + NOR3 + "')) and not(contains(.,'" + NOR4 + "'))]");
		getFlog().check("Pas de publication conjointe");
		// Saisie de valeurs pour les champs de publication
		bordereauPage.setModeParution("Mixte");
		bordereauPage.setDelaiPublication("A date précisée");
		fillField(
				bordereauPage
						.getElementById("bordereauForm:nxl_bordereau_parution:nxw_date_precisee_publication_fieldInputDate"),
				"Publication à date précisée", "31/12/2099");
		// Saisir le NOR2 dans le champ 'Publication conjointe'
		fillField(
				bordereauPage
						.getElementById("bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_inputPub"),
				"Publication conjointe", NOR2);
		bordereauPage
				.getElementById("bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_addPubImage")
				.click();
		WebPage.sleep(1);
		bordereauPage.getElementByXpath("//span[@id='" + PUB_CJT_SPAN_ID + "' and contains(.,'" + NOR2
				+ "') and not(contains(.,'" + NOR3 + "')) and not(contains(.,'" + NOR4 + "'))]");
		getFlog().check("Ajout du NOR");
		// Enregistrer
		bordereauPage.enregistrer();
		getFlog().check("Enregistrement");
		// Ouvrir le dossier 2
		recherchePage = traitementPage.rechercheNor(NOR2);
		dossierDetailMenu = recherchePage.getDossierDetail(NOR2);
		bordereauPage = dossierDetailMenu.goToBordereau();
		// Vérifier qu'il y a une publication conjointe avec le dossier 1
		bordereauPage.getElementByXpath(PUB_CJT_XPATH + "[contains(text(),'" + NOR1 + "') and not(contains(text(),'"
				+ NOR3 + "')) and not(contains(text(),'" + NOR4 + "'))]");
		getFlog().check("'Publication conjointe' avec le dossier 1");
		// Vérifier les valeurs des champs de publication
		bordereauPage
				.getElementByXpath("//form[@id='bordereauForm']/div[2]/div[1]/table[6]/tbody/tr[5]/td[3][contains(text(),'Mixte')]");
		getFlog().check("assertTrue 'Mode parution': Mixte");
		bordereauPage
				.getElementByXpath("//form[@id='bordereauForm']/div[2]/div[1]/table[6]/tbody/tr[6]/td[3][contains(text(),'A date précisée')]");
		getFlog().check("assertTrue 'Délai publication': A date précisée");
		bordereauPage
				.getElementByXpath("//form[@id='bordereauForm']/div[2]/div[1]/table[6]/tbody/tr[7]/td[3]/span[contains(text(),'31/12/2099')]");
		getFlog().check("assertTrue 'Publication à date précisée': 31/12/2099");

		/*
		 * Fusion de publications conjointes
		 */

		// Verrouiller le dossier 2
		bordereauPage.verrouiller();
		// Saisir le NOR3 dans le champ 'Publication conjointe'
		fillField(
				bordereauPage
						.getElementById("bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_inputPub"),
				"Publication conjointe", NOR3);
		bordereauPage
				.getElementById("bordereauForm:nxl_bordereau_parution:nxw_publication_conjointe_field_addPubImage")
				.click();
		WebPage.sleep(1);
		bordereauPage.getElementByXpath("//span[@id='" + PUB_CJT_SPAN_ID + "' and contains(.,'" + NOR1
				+ "') and contains(.,'" + NOR3 + "') and contains(.,'" + NOR4 + "')]");
		getFlog().check("Ajout du NOR");
		// Enregistrer
		bordereauPage.enregistrer();
		getFlog().check("Enregistrement");
		// Déverrouiller le dossier 2
		bordereauPage.deverouiller_par_alt();
		// Ouvrir le dossier 1
		recherchePage = traitementPage.rechercheNor(NOR1);
		dossierDetailMenu = recherchePage.getDossierDetail(NOR1);
		bordereauPage = dossierDetailMenu.goToBordereau();
		// Déverrouiller le dossier 1
		bordereauPage.deverouiller_par_alt();
		// Vérifier qu'il y a une publication conjointe avec les 3 autres dossiers
		bordereauPage.getElementByXpath(PUB_CJT_XPATH + "[contains(text(),'" + NOR2 + "') and contains(text(),'" + NOR3
				+ "') and contains(text(),'" + NOR4 + "')]");
		getFlog().check("'Publication conjointe' avec les 3 autres dossiers");
		// Ouvrir le dossier 3
		recherchePage = traitementPage.rechercheNor(NOR3);
		dossierDetailMenu = recherchePage.getDossierDetail(NOR3);
		bordereauPage = dossierDetailMenu.goToBordereau();
		// Vérifier qu'il y a une publication conjointe avec les 3 autres dossiers
		bordereauPage.getElementByXpath(PUB_CJT_XPATH + "[contains(text(),'" + NOR1 + "') and contains(text(),'" + NOR2
				+ "') and contains(text(),'" + NOR4 + "')]");
		getFlog().check("'Publication conjointe' avec les 3 autres dossiers");
		// Ouvrir le dossier 4
		recherchePage = traitementPage.rechercheNor(NOR4);
		dossierDetailMenu = recherchePage.getDossierDetail(NOR4);
		bordereauPage = dossierDetailMenu.goToBordereau();
		// Déverrouiller le dossier 4
		bordereauPage.deverouiller_par_alt();
		// Vérifier qu'il y a une publication conjointe avec les 3 autres dossiers
		bordereauPage.getElementByXpath(PUB_CJT_XPATH + "[contains(text(),'" + NOR1 + "') and contains(text(),'" + NOR2
				+ "') and contains(text(),'" + NOR3 + "')]");
		getFlog().check("'Publication conjointe' avec les 3 autres dossiers");

		// Log out
		logoutEpg();
	}

	@WebTest(description = "Conseil d'Etat", order = 50)
	@TestDocumentation(categories = { "Dossier", "Bordereau", "Recherche" })
	public void TST_CONS_ETAT() {

		maximizeBrowser();

		// Log in
		TraitementPage traitementPage = loginEpg(USER_ADMIN_FON);

		/*
		 * Préparation du Dossier
		 */

		// Création du Dossier
		final String NOR = createDossier(traitementPage, "Décret en Conseil d'Etat (individuel)");
		// Ouverture du Dossier
		RecherchePage recherchePage = traitementPage.rechercheNor(NOR);
		DossierDetailMenu dossierDetailMenu = recherchePage.getDossierDetail(NOR);
		// Remplissage du Bordereau
		BordereauPage bordereauPage = dossierDetailMenu.goToBordereau();
		bordereauPage.verrouiller();
		bordereauPage.setTitreActe("CE test");
		bordereauPage.setNomDuResponsable("Test");
		bordereauPage.setQualiteDuResponsable("Mr");
		bordereauPage.setTelDuResponsable("0123456789");
		bordereauPage.setPrenomDuResponsable("Test");
		bordereauPage.setRubriques("Domaine public");
		// les valeurs vides peuvent faire bagotter les tests, avec soit l'absence du champ dans le XML
		// soit la mise en place d'un champ vide.
		String numeroIsaDuDossier = "numeroIsaDuDossier";
		bordereauPage.setNumeroIsa(numeroIsaDuDossier);
		bordereauPage.enregistrer();
		getFlog().action("Enregistrer");
		// Téléchargement d'un extrait
		ParapheurPage parapheurPage = dossierDetailMenu.goToParapheur();
		parapheurPage.ajoutDocumentParapheur(
				"//td[@id='document_properties_parapheur:parapheurTree:1::parapheurFolder:text']", "word2003.doc");
		// Ajout de l'étape "Pour avis CE"
		FeuilleDeRoutePage feuilleDeRoutePage = dossierDetailMenu.goToFeuilleDeRoute();
		CreationEtapePage creationEtapePage = feuilleDeRoutePage.ajouterApres();
		creationEtapePage.setTypeEtape("Pour avis CE");
		creationEtapePage
				.setDestinataire(Arrays
						.asList("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:9::min:handle:img:collapsed",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:9:node:0::dir:handle",
								"document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:9:node:0:node:0::addBtnMB"));
		creationEtapePage.creer();
		getFlog().actionClickButton("Créer");
		// Lancer le Dossier
		feuilleDeRoutePage.lancerDossier();
		getFlog().actionClickButton("Lancer");

		/*
		 * Début du Test
		 */

		// Recherche du Dossier
		recherchePage = traitementPage.rechercheNor(NOR);
		dossierDetailMenu = recherchePage.getDossierDetail(NOR);
		parapheurPage = dossierDetailMenu.goToParapheur();
		// Verrouiller
		parapheurPage.verrouiller();
		// Vérifications
		parapheurPage.getElementByXpath("//input[@title='Saisine rectificative']");
		getFlog().check("assertElementPresent Bouton 'Saisine rectificative'");
		parapheurPage.getElementByXpath("//input[contains(@title,'Envoi de')]");
		getFlog().check("assertElementPresent Bouton 'Envoi de pièces complémentaires au Conseil d'Etat'");
		WebPage.sleep(5);
		parapheurPage
				.getElementByXpath("//td[contains(@id,'::fondDeDossierRootFolder:text')]//td[contains(text(),'Saisine rectificative')]");
		getFlog().check("assertElementPresent Répertoire 'Saisine rectificative' dans le Fond de Dossier");
		// Cliquer sur "Acte intégral
		parapheurPage.getElementByXpath(
				"//td[@id='document_properties_parapheur:parapheurTree:0::parapheurFolder:text']").click();
		getFlog().actionClickLink("Acte intégral");
		// Vérifier que le menu contextuel n'est pas affiché
		parapheurPage.assertElementNotPresent(By_enum.ID, "document_properties_parapheur:menuParapheurDossier_menu");
		getFlog().check("assertElementNotPresent context menu");

		/*
		 * Téléchargement des fichiers
		 */

		// Ajouter document dans 'Divers'
		final File file1 = new File("src/main/attachments/word2003.doc");
		try {
			contextMenuClick(
					parapheurPage
							.getElementByXpath("//td[contains(@id,'fondDeDossierTree:3:0:3::fondDeDossierRootFolder:text')]"),
					"Ajouter document");
			parapheurPage.waitForPageSourcePartDisplayed(By.id("createFileFddPanelHeader"), 3);
		} catch (Exception e) {
			contextMenuClick(
					parapheurPage
							.getElementByXpath("//td[contains(@id,'fondDeDossierTree:3:0:3::fondDeDossierRootFolder:text')]"),
					"Ajouter document");
			parapheurPage.waitForPageSourcePartDisplayed(By.id("createFileFddPanelHeader"), 3);
		}
		// Cliquer sur "Ajouter fichier"
		parapheurPage.getElementById("createFileFddForm:uploadFondDeDossierFiles:add2").click();
		getFlog().startAction("Télécharger 'word2003.doc'");
		// Ecrire le path vers le fichier 1
		WebPage.sleep(1);
		parapheurPage.findElement(By.id("createFileFddForm:uploadFondDeDossierFiles:file")).sendKeys(
				file1.getAbsolutePath());
		parapheurPage.waitForPageSourcePart("Téléchargement effectué", WebPage.TIMEOUT_IN_SECONDS);
		getFlog().endAction();
		// Cliquer sur "Valider"
		parapheurPage.getElementById("createFileFddForm:createFileFddButtonImage").click();
		getFlog().actionClickButton("Valider");
		WebPage.sleep(2);
		// Valider les données du document
		parapheurPage.getElementBy(By_enum.XPATH, "//table[contains(@id,'fondDeDossierTree:3:0:3:0::')]"
				+ "//td[1][contains(.,'* word2003.doc')]");
		getFlog().check("assertFilePresent 'word2003.doc' dans 'Divers' avec *");

		// Ajouter document dans 'Saisine rectificative'
		final File file2 = new File("src/main/attachments/pdfTest.pdf");
		try {
			contextMenuClick(
					parapheurPage
							.getElementByXpath("//td[contains(@id,'fondDeDossierTree:3:0:10::fondDeDossierRootFolder:text')]"),
					"Ajouter document");
			parapheurPage.waitForPageSourcePartDisplayed(By.id("createFileFddPanelHeader"), 3);
		} catch (Exception e) {
			contextMenuClick(
					parapheurPage
							.getElementByXpath("//td[contains(@id,'fondDeDossierTree:3:0:10::fondDeDossierRootFolder:text')]"),
					"Ajouter document");
			parapheurPage.waitForPageSourcePartDisplayed(By.id("createFileFddPanelHeader"), 3);
		}
		// Cliquer sur "Ajouter fichier"
		parapheurPage.getElementById("createFileFddForm:uploadFondDeDossierFiles:add2").click();
		getFlog().startAction("Télécharger 'pdfTest.pdf'");
		// Ecrire le path vers le fichier 1
		WebPage.sleep(1);
		parapheurPage.findElement(By.id("createFileFddForm:uploadFondDeDossierFiles:file")).sendKeys(
				file2.getAbsolutePath());
		parapheurPage.waitForPageSourcePart("Téléchargement effectué", WebPage.TIMEOUT_IN_SECONDS);
		getFlog().endAction();
		// Cliquer sur "Valider"
		parapheurPage.getElementById("createFileFddForm:createFileFddButtonImage").click();
		getFlog().actionClickButton("Valider");
		WebPage.sleep(2);
		// Valider les données du document
		parapheurPage.getElementBy(By_enum.XPATH, "//table[contains(@id,'fondDeDossierTree:3:0:10:0::')]"
				+ "//td[1][contains(.,'* pdfTest.pdf')]");
		getFlog().check("assertFilePresent 'pdfTest.pdf' dans 'Saisine rectificative' avec *");

		/*
		 * Saisine rectificative
		 */

		// Cliquer sur 'Saisine rectificative'
		parapheurPage.getElementByXpath("//input[@title='Saisine rectificative']").click();
		getFlog().actionClickButton("Saisine rectificative");
		// Vérifier le bon affichage
		parapheurPage
				.getElementByXpath("//div[1]/h3[contains(text(),'Liste des documents du répertoire') and contains(text(),'modifiés depuis le dernier envoi au Conseil')]");
		getFlog().check("Les fichiers de 'Saisine rectificative' sont affichés en premier");
		parapheurPage
				.getElementByXpath("//div[3]/h3[contains(text(),'Pour information : liste des documents hors du répertoire') and contains(text(),'à transmettre au Conseil')]");
		getFlog().check("Les fichiers hors de 'Saisine rectificative' sont affichés après");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[1]//td[1][contains(text(),'pdfTest.pdf')]");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[1]//td[2][contains(text(),'adminsgg')]");
		parapheurPage
				.getElementByXpath("//table[3]//td[2]/div/div[1]//td[5][contains(text(),'Saisine rectificative')]");
		getFlog()
				.check("assertFilePresent dans la 1ère liste : 'pdfTest.pdf' par 'adminsgg' du répertoire 'Saisine rectificative'");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[3]//td[1][contains(text(),'word2003.doc')]");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[3]//td[2][contains(text(),'adminsgg')]");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[3]//td[5][contains(text(),'Divers')]");
		getFlog().check("assertFilePresent dans la 2ème liste : 'word2003.doc' par 'adminsgg' du répertoire 'Divers'");
		// Cliquer sur 'Transmettre la saisine rectificative'
		parapheurPage.getElementById("saisine_rectificative:confirmerSaisineRectificative").click();
		getFlog().actionClickButton("Transmettre la saisine rectificative");
		// Vérifications dans le Fond de Dossier
		parapheurPage.getElementBy(By_enum.XPATH, "//table[contains(@id,'fondDeDossierTree:3:0:3:0::')]"
				+ "//td[1][contains(.,'* word2003.doc')]");
		getFlog().check("assertElementPresent * encore affiché pour 'word2003.doc'");
		parapheurPage.getElementBy(By_enum.XPATH, "//table[contains(@id,'fondDeDossierTree:3:0:10:0::')]"
				+ "//td[1][contains(.,'pdfTest.pdf') and not(contains(.,'*'))]");
		getFlog().check("assertElementNotPresent pas de * pour 'pdfTest.pdf'");
		parapheurPage.getElementBy(By_enum.XPATH, "//table[contains(@id,'fondDeDossierTree:3:0:10:0::')]"
				+ "//td[1]//div[contains(text(),'1') and contains(@style,'pink')]");
		getFlog().check("assertElementPresent '1' en rose à côté de 'pdfTest.pdf'");
		// Ouvrir le Journal
		JournalPage journalPage = dossierDetailMenu.goToJournalPage();
		journalPage
				.getElementByXpath("//form[@id='JOURNAL_DOSSIER']/div/table/tbody/tr[1]/td[5]/div[contains(text(),'Saisine rectificative pour les fichiers pdfTest.pdf')]");
		getFlog()
				.check("assertElementPresent 'Saisine rectificative pour les fichiers pdfTest.pdf' est l'entrée la plus récente du Journal");
		// Renommer 'pdfTest.pdf'
		parapheurPage = dossierDetailMenu.goToParapheur();
		contextMenuClick(parapheurPage.getElementByXpath("//span[contains(@id,'fondDeDossierTree:3:0:10:0')]"),
				"Renommer");
		
		// le remplissage du champ de renommage provoque un rafraichissement
		WebElement refreshedElement = parapheurPage.findElement(By.id("document_propertiesFDD:fddDocumentViewPanel"));
		parapheurPage.fillField("Nom", parapheurPage.getElementById("renameFileFDDForm:fileFDDName"), "pdfTest2");
		parapheurPage.findElement(By.id("renameFileFDDForm:fileFDDName")).sendKeys(Keys.TAB);
		new WebDriverWait(getDriver(), WebPage.TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(refreshedElement));
		
		parapheurPage.clickAndRefreshElement(By.id("renameFileFDDForm:validFileFDDRename"),
				By.id("document_propertiesFDD:fddDocumentViewPanel"), WebPage.class);
		getFlog().actionClickButton("Valider");
		assertFilePresent(parapheurPage, "fondDeDossierTree:3:0:10:0", "* pdfTest2.pdf", "Adminsgg", "2");
		// Cliquer sur 'Saisine rectificative'
		parapheurPage.getElementByXpath("//input[@title='Saisine rectificative']").click();
		getFlog().actionClickButton("Saisine rectificative");
		// Vérifier le bon affichage
		parapheurPage
				.getElementByXpath("//div[1]/h3[contains(text(),'Liste des documents du répertoire') and contains(text(),'modifiés depuis le dernier envoi au Conseil')]");
		getFlog().check("Les fichiers de 'Saisine rectificative' sont affichés en premier");
		parapheurPage
				.getElementByXpath("//div[3]/h3[contains(text(),'Pour information : liste des documents hors du répertoire') and contains(text(),'à transmettre au Conseil')]");
		getFlog().check("Les fichiers hors de 'Saisine rectificative' sont affichés après");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[1]//td[1][contains(text(),'pdfTest2.pdf')]");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[1]//td[2][contains(text(),'adminsgg')]");
		parapheurPage
				.getElementByXpath("//table[3]//td[2]/div/div[1]//td[5][contains(text(),'Saisine rectificative')]");
		getFlog()
				.check("assertFilePresent dans la 1ère liste : 'pdfTest2.pdf' par 'adminsgg' du répertoire 'Saisine rectificative'");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[3]//td[1][contains(text(),'word2003.doc')]");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[3]//td[2][contains(text(),'adminsgg')]");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[3]//td[5][contains(text(),'Divers')]");
		getFlog().check("assertFilePresent dans la 2ème liste : 'word2003.doc' par 'adminsgg' du répertoire 'Divers'");
		// Cliquer sur 'Transmettre la saisine rectificative'
		parapheurPage.getElementById("saisine_rectificative:confirmerSaisineRectificative").click();
		getFlog().actionClickButton("Transmettre la saisine rectificative");
		// Vérifications dans le Fond de Dossier
		parapheurPage.getElementBy(By_enum.XPATH, "//table[contains(@id,'fondDeDossierTree:3:0:3:0::')]"
				+ "//td[1][contains(.,'* word2003.doc')]");
		getFlog().check("assertElementPresent * encore affiché pour 'word2003.doc'");
		parapheurPage.getElementBy(By_enum.XPATH, "//table[contains(@id,'fondDeDossierTree:3:0:10:0::')]"
				+ "//td[1][contains(.,'pdfTest2.pdf') and not(contains(.,'*'))]");
		getFlog().check("assertElementNotPresent pas de * pour 'pdfTest.pdf'");
		parapheurPage.getElementBy(By_enum.XPATH, "//table[contains(@id,'fondDeDossierTree:3:0:10:0::')]"
				+ "//td[1]//div[contains(text(),'2') and contains(@style,'pink')]");
		getFlog().check("assertElementPresent '2' en rose à côté de 'pdfTest.pdf'");
		// Ouvrir le Journal
		journalPage = dossierDetailMenu.goToJournalPage();
		journalPage
				.getElementByXpath("//form[@id='JOURNAL_DOSSIER']/div/table/tbody/tr[1]/td[5]/div[contains(text(),'Saisine rectificative pour les fichiers pdfTest2.pdf')]");
		getFlog()
				.check("assertElementPresent 'Saisine rectificative pour les fichiers pdfTest2.pdf' est l'entrée la plus récente du Journal");
		// Renommer 'pdfTest2.pdf'
		parapheurPage = dossierDetailMenu.goToParapheur();
		contextMenuClick(parapheurPage.getElementByXpath("//span[contains(@id,'fondDeDossierTree:3:0:10:0')]"),
				"Renommer");
		parapheurPage.fillField("Nom", parapheurPage.getElementById("renameFileFDDForm:fileFDDName"), "pdfTest3");
		parapheurPage.clickAndRefreshElement(By.id("renameFileFDDForm:validFileFDDRename"),
				By.id("document_propertiesFDD:fddDocumentViewPanel"), WebPage.class);
		getFlog().actionClickButton("Valider");
		WebPage.sleep(1);
		assertFilePresent(parapheurPage, "fondDeDossierTree:3:0:10:0", "* pdfTest3.pdf", "Adminsgg", "3");

		/*
		 * Envoi de pièces complémentaires
		 */

		// Cliquer sur 'Envoi de pièces complémentaires au Conseil d'Etat'
		parapheurPage.getElementByXpath("//input[contains(@title,'Envoi de pièces complémentaires')]").click();
		getFlog().actionClickButton("Envoi de pièces complémentaires au Conseil d'Etat");
		// Vérifier le bon affichage
		parapheurPage
				.getElementByXpath("//div[1]/h3[contains(text(),'Liste des documents hors du répertoire') and contains(text(),'à transmettre au Conseil')]");
		getFlog().check("Les fichiers hors de 'Saisine rectificative' sont affichés en premier");
		parapheurPage
				.getElementByXpath("//div[3]/h3[contains(text(),'Pour information : liste des documents du répertoire') and contains(text(),'à transmettre au Conseil')]");
		getFlog().check("Les fichiers de 'Saisine rectificative' sont affichés après");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[1]//td[1][contains(text(),'word2003.doc')]");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[1]//td[2][contains(text(),'adminsgg')]");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[1]//td[5][contains(text(),'Divers')]");
		getFlog().check("assertFilePresent dans la 1ère liste : 'word2003.doc' par 'adminsgg' du répertoire 'Divers'");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[3]//td[1][contains(text(),'pdfTest3.pdf')]");
		parapheurPage.getElementByXpath("//table[3]//td[2]/div/div[3]//td[2][contains(text(),'adminsgg')]");
		parapheurPage
				.getElementByXpath("//table[3]//td[2]/div/div[3]//td[5][contains(text(),'Saisine rectificative')]");
		getFlog()
				.check("assertFilePresent dans la 2ème liste : 'pdfTest3.pdf' par 'adminsgg' du répertoire 'Saisine rectificative'");
		// Cliquer sur 'Envoyer les pièces complémentaires'
		parapheurPage.getElementById("pieces_complementaires:confirmerEnvoiPiecesComplementaires").click();
		getFlog().actionClickButton("Envoyer les pièces complémentaires");
		// Vérifications dans le Fond de Dossier
		parapheurPage.getElementBy(By_enum.XPATH, "//table[contains(@id,'fondDeDossierTree:3:0:3:0::')]"
				+ "//td[1][contains(.,'word2003.doc') and not(contains(.,'*'))]");
		getFlog().check("assertElementNotPresent pas de * pour 'word2003.doc'");
		parapheurPage.getElementBy(By_enum.XPATH, "//table[contains(@id,'fondDeDossierTree:3:0:10:0::')]"
				+ "//td[1][contains(.,'* pdfTest3.pdf')]");
		getFlog().check("assertElementPresent * encore affiché pour 'pdfTest3.pdf'");
		// Ouvrir le Journal
		journalPage = dossierDetailMenu.goToJournalPage();
		journalPage
				.getElementByXpath("//form[@id='JOURNAL_DOSSIER']/div/table/tbody/tr[1]/td[5]/div[contains(text(),'Mise à disposition de pièces complémentaires pour les fichiers word2003.doc')]");
		getFlog()
				.check("assertElementPresent 'Mise à disposition de pièces complémentaires pour les fichiers word2003.doc' est l'entrée la plus récente du Journal");

		/*
		 * Priorités
		 */

		// Vérifier la présence du champ Priorité et toutes ses options
		bordereauPage = dossierDetailMenu.goToBordereau();
		bordereauPage.getElementByXpath("//div[@class='foldableBox']//span[text()='Priorité']");
		getFlog().check("assertElementPresent liste déroulante 'Priorité'");
		bordereauPage
				.getElementByXpath("//select[@id='bordereauForm:nxl_bordereau_ce:nxw_priorite_ce_field']/option[text()='Très urgent ']");
		getFlog().check("assertElementPresent option 'Très urgent'");
		bordereauPage.getElementByXpath(
				"//select[@id='bordereauForm:nxl_bordereau_ce:nxw_priorite_ce_field']/option[text()='Urgent']").click();
		getFlog().check("assertElementPresent option 'Urgent'");
		getFlog().actionClickLink("Urgent");
		bordereauPage
				.getElementByXpath("//select[@id='bordereauForm:nxl_bordereau_ce:nxw_priorite_ce_field']/option[text()='Normal']");
		getFlog().check("assertElementPresent option 'Normal'");
		bordereauPage.enregistrer();
		getFlog().action("Enregistrer");
		bordereauPage.deverouiller_par_alt();
		// Vérifier la valeur de Priorité
		bordereauPage
				.getElementByXpath("//tr[contains(td[@class='labelColumn'],'Priorité')]/td[@class='fieldColumn' and contains(text(),'Urgent')]");
		// Ouvrir le Journal
		journalPage = dossierDetailMenu.goToJournalPage();
		journalPage
				.getElementByXpath("//form[@id='JOURNAL_DOSSIER']/div/table/tbody/tr[2]/td[5]/div[contains(text(),\"Priorité : '2' remplace ''\")]");
		getFlog().check("assertElementPresent \" Priorité : '2' remplace '' \"");

		// Déconnexion
		logoutEpg();

		/*
		 * Web Service 'chercherModificationDossier'
		 */

		// Préparer la requête
		StringBuilder xmlForWs = new StringBuilder();
		xmlForWs.append("<?xml version='1.0'");
		xmlForWs.append(" encoding='UTF-8'?>");
		xmlForWs.append(" <p:chercherModificationDossierRequest");
		xmlForWs.append(" xmlns:p='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg'");
		xmlForWs.append(" xmlns:p1='http://www.dila.premier-ministre.gouv.fr/solon/epg/actes' xmlns:p2='http://www.dila.premier-ministre.gouv.fr/solon/epg/solon-commons' xmlns:p3='http://www.dila.premier-ministre.gouv.fr/solon/epg/ar'");
		xmlForWs.append(" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
		xmlForWs.append(" xsi:schemaLocation='http://www.dila.premier-ministre.gouv.fr/solon/epg/WSepg ../../../../../../../../xsd/solon/epg/WSepg.xsd '>");
		xmlForWs.append(" <p:nor>" + NOR + "</p:nor>");
		xmlForWs.append(" </p:chercherModificationDossierRequest>");
		// Envoi de la requête Web Service
		WsResultPage wsResultPage = loginWSEpg("adminsgg", "adminsgg",
				((UrlEpgHelper) UrlEpgHelper.getInstance()).getEpgUrl()
						+ "/site/solonepg/WSepg/chercherModificationDossier", xmlForWs.toString());
		WebPage.sleep(2);
		// Récupération et vérification du résultat
		String result = wsResultPage.getRealContent();
		// 2018-07-18: un numéro ISA a été positionné dans le bordereau, de manière à ce que le résultat XML soit stable.
		// Si pas de numéro ISA, on peut avoir une balise absente ou vide (qui correspondent sûrement à un enregistrement null ou '').
		// match d'une date au format : 2018-07-26T07:13:29.000+02:00
		String dateModificationPattern = "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3}[+-][0-9]{2}:[0-9]{2}";
		// note: \Qblabla\E permet de matcher sans prise en compte des caractères spéciaux la chaine blabla
		assertResultContainsPattern(
				result,
				String.format(
					"\\Q<dossier_modification><ns2:type_modification>SAISINE_RECTIFICATIVE</ns2:type_modification><ns2:numero_isa>%s</ns2:numero_isa><ns2:numero_nor>%s</ns2:numero_nor><ns2:date_modification>\\E%s\\Q</ns2:date_modification><ns2:fichier><ns2:nom>pdfTest.pdf</ns2:nom>\\E",
					numeroIsaDuDossier, NOR, dateModificationPattern));
		assertResultContains(
				result,
				String.format(
					"<ns2:cheminFichier>Répertoire accessible à tous les utilisateurs/Documentation jointe/Saisine rectificative/pdfTest3.pdf</ns2:cheminFichier><ns2:version>1</ns2:version></ns2:fichier></dossier_modification>",
					numeroIsaDuDossier));
		assertResultContainsPattern(
				result,
				String.format(
					"\\Q<dossier_modification><ns2:type_modification>SAISINE_RECTIFICATIVE</ns2:type_modification><ns2:numero_isa>%s</ns2:numero_isa><ns2:numero_nor>%s</ns2:numero_nor><ns2:date_modification>\\E%s\\Q</ns2:date_modification><ns2:fichier><ns2:nom>pdfTest2.pdf</ns2:nom>\\E",
					numeroIsaDuDossier, NOR, dateModificationPattern));
		assertResultContains(
				result,
				String.format(
					"<ns2:cheminFichier>Répertoire accessible à tous les utilisateurs/Documentation jointe/Saisine rectificative/pdfTest3.pdf</ns2:cheminFichier><ns2:version>2</ns2:version></ns2:fichier></dossier_modification>",
					numeroIsaDuDossier));
		assertResultContainsPattern(
				result,
				String.format(
					"\\Q<dossier_modification><ns2:type_modification>PIECE_COMPLEMENTAIRE</ns2:type_modification><ns2:numero_isa>%s</ns2:numero_isa><ns2:numero_nor>%s</ns2:numero_nor><ns2:date_modification>\\E%s\\Q</ns2:date_modification><ns2:fichier><ns2:nom>word2003.doc</ns2:nom>\\E",
					numeroIsaDuDossier, NOR, dateModificationPattern));
		assertResultContains(
				result,
				String.format(
					"<ns2:cheminFichier>Répertoire accessible à tous les utilisateurs/Documentation jointe/Divers/word2003.doc</ns2:cheminFichier><ns2:version>1</ns2:version></ns2:fichier></dossier_modification>",
					numeroIsaDuDossier));
		assertResultContainsPattern(result,
				String.format(
					"\\Q<dossier_modification><ns2:type_modification>PRIORISATION</ns2:type_modification><ns2:numero_isa>%s</ns2:numero_isa><ns2:numero_nor>%s</ns2:numero_nor><ns2:priorite>2</ns2:priorite><ns2:date_modification>\\E%s\\Q</ns2:date_modification>\\E",
					numeroIsaDuDossier, NOR, dateModificationPattern));

		// Déconnexion
		logoutWS();
	}

	private String createDossier(EPGWebPage webPage, String typeActe) {

		// Ouvrir la page "Création"
		CreationPage creationPage = webPage.getOngletMenu().goToEspaceCreation();

		// Cliquer sur "Ajouter un dossier"
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();

		// Choisir le "Type d'acte"
		creationDossier100Page.setTypeActe(typeActe);

		// Cliquer sur "Terminer"
		DossierDetailMenu dossierDetailMenu = creationDossier100Page.appuyerBoutonTerminer(DossierDetailMenu.class);
		String nor = creationDossier100Page.waitAndGetNumeroDossierCree();
		dossierDetailMenu.assertTrue(nor + " enregistré");
		return nor;
	}

	private DossierDetailMenu createDossier(String direction) {

		// Log in
		TraitementPage traitementPage = loginEpg(CURRENT_USER);
		traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : " + CURRENT_USER);

		// Ouvrir la page "Création"
		CreationPage creationPage = traitementPage.getOngletMenu().goToEspaceCreation();

		// Cliquer sur "Ajouter un dossier"
		CreationDossier100Page creationDossier100Page = creationPage.createDossier();

		// Choisir le "Type d'acte"
		creationDossier100Page.setTypeActe("Avis");

		// Choisir le "Ministère Responsable"
		creationDossier100Page.getElementById(
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_findButton").click();
		WebPage.sleep(2);
		creationDossier100Page.getElementByXpath("//td/span[contains(text(),'CCO')]")
				.findElement(By.xpath("./../a/img")).click();
		getFlog().action("Selectionne le ministère \"CCO\"");

		// Choisir la "Direction Concernée"
		creationDossier100Page.getElementById(
				"creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_findButton").click();
		WebPage.sleep(2);
		creationDossier100Page.getElementByXpath("//td/span[contains(text(),'CCO')]")
				.findElement(By.xpath("./../..//img[contains(@id,'collapsed')]")).click();
		WebPage.sleep(2);
		creationDossier100Page.getElementByXpath("//td/span[contains(text(),'" + direction + "')]")
				.findElement(By.xpath("./../a/img")).click();
		getFlog().action("Selectionne la direction \"" + direction + "\"");

		// Vérifier que la bonne Direction est choisie
		WebPage.sleep(2);
		creationDossier100Page.assertTrue(direction);

		// Cliquer sur "Terminer"
		DossierDetailMenu dossierDetailMenu = creationDossier100Page.appuyerBoutonTerminer(DossierDetailMenu.class);
		NOR_DOSSIER = creationDossier100Page.waitAndGetNumeroDossierCree();
		dossierDetailMenu.assertTrue(NOR_DOSSIER + " enregistré");

		return dossierDetailMenu;
	}

	private void substitutionFdR(String direction) {

		// Créer un Dossier
		DossierDetailMenu dossierDetailMenu = createDossier(direction);

		// Ouvrir la page "Feuille de Route"
		FeuilleDeRoutePage feuilleDeRoutePage = dossierDetailMenu.goToFeuilleDeRoute();
		feuilleDeRoutePage.assertTrue("Agriculture");
		feuilleDeRoutePage.assertFalse("Substituer");

		// Verrouiller
		feuilleDeRoutePage.verrouiller();
		feuilleDeRoutePage.assertElementPresent(By_enum.ID, "substituerFeuilleRouteForm");

		// Cliquer sur "Substituer"
		feuilleDeRoutePage.getElementByXpath("//form[@id='substituerFeuilleRouteForm']/div/input").click();
		getFlog().actionClickButton("Substituer");
		feuilleDeRoutePage.assertTrue("Substituer la feuille de route du dossier");
		WebPage.sleep(2);

		// Vérifier que seules les modèles de FdR du Ministère actuel sont affichés
		feuilleDeRoutePage.assertTrue("CCO -");
		feuilleDeRoutePage.assertFalse("PRM -");

		// Cliquer sur "CCO - Ordonnance - Economie - Feuille de route des ordonnances"
		feuilleDeRoutePage
				.getElementByXpath(
						"//a[contains(text(),'CCO - Ordonnance - Economie - Feuille de route des ordonnances')]")
				.click();
		getFlog().actionClickLink("CCO - Ordonnance - Economie - Feuille de route des ordonnances");

		// Cliquer sur "Retour au dossier"
		feuilleDeRoutePage.getElementByXpath("//form[@id='mfdrSelectionForm']/input[@value='Retour au dossier']")
				.click();
		getFlog().actionClickButton("Retour au dossier");

		// Vérifier que la Feuille de Route n'a pas changé
		feuilleDeRoutePage.assertTrue("Agriculture");

		// Cliquer sur "Substituer"
		feuilleDeRoutePage.getElementByXpath("//form[@id='substituerFeuilleRouteForm']/div/input").click();
		getFlog().actionClickButton("Substituer");
		feuilleDeRoutePage.assertTrue("Substituer la feuille de route du dossier");

		// Cliquer sur "Economie - Feuille de route des ordonnances"
		feuilleDeRoutePage
				.getElementByXpath(
						"//a[contains(text(),'CCO - Ordonnance - Economie - Feuille de route des ordonnances')]")
				.click();
		getFlog().actionClickLink("CCO - Ordonnance - Economie - Feuille de route des ordonnances");

		// Cliquer sur "Substituer"
		WebPage.sleep(2);
		feuilleDeRoutePage.getElementByXpath("//form[@id='mfdrSelectionForm']/input[@value='Substituer']").click();
		getFlog().actionClickButton("Substituer");

		// Vérifier que la nouvelle Feuille de Route a été appliquée
		feuilleDeRoutePage.assertTrue("(CE)");

		// Si l'utilisateur est un Contributeur Ministériel
		if (CURRENT_USER.equals(USER_CONTR_MIN)) {
			feuilleDeRoutePage.deverouiller_par_alt();
			logoutEpg();
			TraitementPage traitementPage = loginEpg(USER_ADMIN_FON);
			traitementPage.assertTrue("Mon identifiant dans S.O.L.O.N. : " + USER_ADMIN_FON);
			RecherchePage rechPage = traitementPage.rechercheNor(NOR_DOSSIER);
			dossierDetailMenu = rechPage.getDossierDetail(NOR_DOSSIER);
			getFlog().actionClickLink(NOR_DOSSIER);
			dossierDetailMenu.assertTrue("Parapheur");
			dossierDetailMenu.goToBordereau().verrouiller();
		}

		// Ouvrir la page "Bordereau"
		BordereauPage bordereauPage = dossierDetailMenu.goToBordereau();

		// Remplir les champs
		bordereauPage.setTitreActe("Avis test");
		bordereauPage.setNomDuResponsable("Test");
		bordereauPage.setQualiteDuResponsable("Mr");
		bordereauPage.setTelDuResponsable("0123456789");
		bordereauPage.setPrenomDuResponsable("Test");
		bordereauPage.setRubriques("Domaine public");

		// Enregistrer
		bordereauPage.enregistrer();

		// Lancer le Dossier
		feuilleDeRoutePage = dossierDetailMenu.goToFeuilleDeRoute();
		feuilleDeRoutePage.lancerDossier();
		getFlog().actionClickButton("Lancer");
		feuilleDeRoutePage.assertTrue("est lancé");

		// Rechercher avec le NOR
		RecherchePage recherchePage = feuilleDeRoutePage.rechercheNor(NOR_DOSSIER);

		// Selectionner le Dossier
		dossierDetailMenu = recherchePage.getDossierDetail(NOR_DOSSIER);
		getFlog().actionClickLink(NOR_DOSSIER);
		dossierDetailMenu.assertTrue("Parapheur");

		// Ouvrir la page "Feuille de Route"
		feuilleDeRoutePage = dossierDetailMenu.goToFeuilleDeRoute();

		// Vérifier que le dossier est lancé
		feuilleDeRoutePage.assertTrue("Dossier lancé");

		// Si l'utilisateur est un Administrateur Fonctionnel
		if (CURRENT_USER.equals(USER_ADMIN_FON)) {
			// Verrouiller
			feuilleDeRoutePage.verrouiller();

			// Vérifier que le bouton "Substituer" n'est pas affiché
			feuilleDeRoutePage.assertElementNotPresent(By_enum.ID, "substituerFeuilleRouteForm");

			// Déverrouiller
			feuilleDeRoutePage.deverouiller_par_alt();
		}
	}

	private void contextMenuClick(WebElement elem, String action) {
		// TODO Ajouter des logs pour chaque ligne

		elem.click();
		By xpath = By.xpath("//div[contains(@id,'_menu')]//span[contains(., '" + action + "')]");

		new WebDriverWait(getDriver(), WebPage.TIMEOUT_IN_SECONDS).until(ExpectedConditions
				.visibilityOfElementLocated(xpath));

		final WebElement elem2 = getDriver().findElement(xpath);
		getFlog().actionClickButton(action);
		elem2.click();

		WebPage.sleep(2);
	}

	private void fillField(WebElement elem, String fieldName, String value) {
		elem.sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
		getFlog().action("Remplit le champ [" + fieldName + "] avec la valeur [" + value + "]");
	}

	private void assertFilePresent(EPGWebPage webPage, String xpath, String nom, String auteur, String version) {
		webPage.getElementBy(By_enum.XPATH, "//table[contains(@id,'" + xpath + "')]//table//td[1][contains(.,'" + nom
				+ "')]");
		webPage.getElementBy(By_enum.XPATH, "//table[contains(@id,'" + xpath + "')]//table//td[2][contains(text(),'"
				+ auteur + "')]");
		webPage.getElementBy(By_enum.XPATH, "//table[contains(@id,'" + xpath + "')]//table//td[5][contains(text(),'"
				+ version + "')]");
		getFlog().check(String.format("assertFilePresent '%s' by '%s' version '%s'", nom, auteur, version));
	}

	private void testFieldSuggestions(EPGWebPage webPage, String fieldName, String fieldID, String iconID,
			String suggestionID, String input1, String input2, String result) {
		getFlog().startAction("Tests sur le champ '" + fieldName + "'");
		new WebDriverWait(webPage.getDriver(), 15).until(ExpectedConditions.visibilityOfElementLocated(By.id(fieldID)));
		WebElement we = webPage.getElementById(fieldID);
		getFlog().check("assertElementPresent champ avec suggestion pour '" + fieldName + "'");
		webPage.getElementById(iconID);
		getFlog().check("assertElementPresent icône de recherche pour '" + fieldName + "'");
		fillField(we, fieldName, input1);
		int tries = 0;
		while (tries < 3) {
			try {
				new WebDriverWait(webPage.getDriver(), 2).until(ExpectedConditions.visibilityOfElementLocated(By
						.id(suggestionID)));
				break;
			} catch (Exception e) {
				tries++;
				fillField(we, fieldName, " ");
				WebPage.sleep(1);
				fillField(we, fieldName, input1);
			}
		}
		webPage.getElementByXpath("//div[@id='" + suggestionID + "']//td[contains(text(),'" + result + "')]").click();
		getFlog().action("Sélectionne l'élément [" + result + "]");
		new WebDriverWait(webPage.getDriver(), 15).until(ExpectedConditions.invisibilityOfElementLocated(By
				.id(suggestionID)));
		fillField(we, fieldName, input2);
		tries = 0;
		while (tries < 3) {
			try {
				new WebDriverWait(webPage.getDriver(), 2).until(ExpectedConditions.visibilityOfElementLocated(By
						.id(suggestionID)));
				break;
			} catch (Exception e) {
				tries++;
				fillField(we, fieldName, " ");
				WebPage.sleep(1);
				fillField(we, fieldName, input2);
			}
		}
		webPage.getElementByXpath("//div[@id='" + suggestionID + "']//td[contains(text(),'" + result + "')]").click();
		getFlog().action("Sélectionne l'élément [" + result + "]");
		getFlog().endAction();
	}

	private void assertResultContains(String result, String text) {
		if (result.contains(text))
			getFlog().check("assertResultContains '" + text + "'");
		else {
			getFlog().error("Cannot find : '" + text + "'");
			getDriver().quit();
			throw new WebDriverException("Cannot find : '" + text + "'");
		}
	}

	private void assertResultContainsPattern(String result, String pattern) {
		if (result.matches(".*" + pattern + ".*"))
			getFlog().check("assertResultContainsPattern '" + pattern + "'");
		else {
			getFlog().error("Cannot find pattern : '" + pattern + "'");
			getDriver().quit();
			throw new WebDriverException("Cannot find pattern : '" + pattern + "'");
		}
	}
}
