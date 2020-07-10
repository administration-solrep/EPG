package fr.dila.solonepg.page.administration;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.AjouterAlaFeuilleDeRoute;
import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.solonepg.page.commun.MultiElementFinder;
import fr.sword.naiad.commons.webtest.WebPage;

public class CreationModelePage extends EPGWebPage {

	private static final String	FDR_INTITULE_LIBRE			= "document_create:nxl_feuille_route_detail_edit:nxw_feuille_route_intitule_libre";

	private static final String	MINISTERE					= "document_create:nxl_feuille_route_detail_edit:nxw_feuille_route_ministere_findButton";

	private static final String	DIRECTION					= "document_create:nxl_feuille_route_detail_edit:nxw_feuille_route_direction_findButton";

	private static final String	TYPE_ACTE_LIST_ID			= "document_create:nxl_feuille_route_detail_edit:nxw_feuille_route_type_acte";

	private static final String	FDR_DEFAULT					= "document_create:nxl_feuille_route_detail_edit:nxw_feuille_route_defaut";

	private static final String	CREER_BUTTON				= "document_create:button_create";

	private static final String	LAST_ADD_STEP_BUTTON_AFTER	= "//table[@class='dataOutput']/tbody/tr[last()]//a[@class='ADD_STEP_AFTER']";

	public void setintituleLibre(final String intituleLibre) {
		setintituleLibre(intituleLibre, FDR_INTITULE_LIBRE);
	}

	public void setintituleLibre(final String intituleLibre, String id) {
		final WebElement elem = getDriver().findElement(By.id(id));
		fillField("Intitulé libre ", elem, intituleLibre);
	}

	public void setMinistere(final String ministere) {
		selectInOrganigramme(ministere, MINISTERE);
	}

	public void setDirection(final String direction) {
		selectInOrganigramme(direction, DIRECTION);
	}

	public void setTypeActe(String textValue, String id) {
		final WebElement typeDacteSelect = getDriver().findElement(By.id(id));
		getFlog().action("Selectionne \"" + textValue + "\" dans le select \"Type d'acte\"");
		final Select select = new Select(typeDacteSelect);
		select.selectByVisibleText(textValue);
	}

	public void setTypeActe(String textValue) {
		setTypeActe(textValue, TYPE_ACTE_LIST_ID);
	}

	public void checkDefault() {
		final WebElement defaultcheckBox = getDriver().findElement(By.id(FDR_DEFAULT));
		defaultcheckBox.click();
	}

	public CreationModelePage creer() {
		final WebElement elem = getDriver().findElement(By.id(CREER_BUTTON));
		getFlog().actionClickButton("creer modele feuille de route");
		WebElement body = findElement(By.cssSelector("body"));
		elem.click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(body));
		waitForPageLoaded(getDriver());
		int result = new MultiElementFinder(getDriver()).bySourcePart("Feuille de route enregistré(e)", "existe déjà");
		Assert.assertNotSame(0, result);
		if (result == 2) { // Le modèle existe déjà, pas grave, on clique sur 'annuler'
			clickToPage(findDisplayedElements(By.xpath("//input[@value='Annuler']")).get(0), WebPage.class);
			return null;
		}
		return getPage(CreationModelePage.class);
	}

	public CreationEtapePage ajouterEtape() {

		WebElement elementByXpath = this.getElementByXpath(LAST_ADD_STEP_BUTTON_AFTER);

		String id = elementByXpath.getAttribute("id");

		JavascriptExecutor jse = (JavascriptExecutor) getDriver();
		jse.executeScript("document.getElementById('" + id + "').click();");

		AjouterAlaFeuilleDeRoute ajouterAlaFeuilleDeRoute = this.getPage(AjouterAlaFeuilleDeRoute.class);

		return ajouterAlaFeuilleDeRoute.ajouterUneEtape();
	}

	public ViewModelePage validerModele() {
		WebElement body = findElement(By.cssSelector("body"));
		getDriver().findElement(By.xpath("//*[@value='Valider le modèle']")).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(body));
		waitForPageLoaded(getDriver());
		waitForPageSourcePart("Modèle validé avec succès", TIMEOUT_IN_SECONDS);
		return getPage(ViewModelePage.class);
	}

	public ViewModelePage demandeValidationSGG() {
		getFlog().startAction("Demander validation au SGG de la FDR");
		getDriver().findElement(By.xpath("//*[@value='Demande de validation SGG']")).click();
		waitForPageSourcePart("La demande de validation a été effectuée avec succès", TIMEOUT_IN_SECONDS);
		ViewModelePage page = getPage(ViewModelePage.class);
		getFlog().endAction();

		return page;

	}

	public CreationModelePage enregistrer() {
		getFlog().startAction("Enregistrement des métadonnées de la FDR");
		getDriver().findElement(By.xpath("//*[@value='Enregistrer']")).click();
		waitForPageSourcePart("Feuille de route modifié(e)", TIMEOUT_IN_SECONDS);
		CreationModelePage page = getPage(CreationModelePage.class);
		getFlog().endAction();

		return page;

	}

	public CreationModelePage ajouterBranche() {
		WebElement elementByXpath = this.getElementByXpath(LAST_ADD_STEP_BUTTON_AFTER);

		String id = elementByXpath.getAttribute("id");

		JavascriptExecutor jse = (JavascriptExecutor) getDriver();
		jse.executeScript("document.getElementById('" + id + "').click();");

		AjouterAlaFeuilleDeRoute ajouterAlaFeuilleDeRoute = this.getPage(AjouterAlaFeuilleDeRoute.class);

		CreationBranchePage branchePage = ajouterAlaFeuilleDeRoute.ajouterBranche();
		return branchePage.definirNombreBranche();

	}

	public CreationEtapePage definirBranche(String idLienAjouterEtape, String idLienDefinirEnfant) {
		WebElement elementLienEtape = this.getElementById(idLienAjouterEtape);

		clickLien(elementLienEtape);

		JavascriptExecutor jse = (JavascriptExecutor) getDriver();
		jse.executeScript("document.getElementById('" + idLienDefinirEnfant + "').click();");

		AjouterAlaFeuilleDeRoute ajouterAlaFeuilleDeRoute = this.getPage(AjouterAlaFeuilleDeRoute.class);

		return ajouterAlaFeuilleDeRoute.ajouterUneEtape();
	}
}
