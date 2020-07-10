package fr.dila.solonmgpp.page.epg.mgpp.oep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonmgpp.page.commun.CommonWebPage;

public class OepPage extends CommonWebPage {

	public static final String	TRAITER_BTN					= "//*[@value='Traiter']";
	public static final String	CONFIRMER_BTN				= "popup_ok";

	public static final String	REPRESENTANT_AN_FUNCTION	= "view_fpoep:nxl_representant_AN_oep_mgpp_restricted:nxw_representant_oep_fonction";
	public static final String	REPRESENTANT_SENAT_FUNCTION	= "view_fpoep:nxl_representant_SE_oep_mgpp_restricted:nxw_representant_oep_fonction_1";

	public void addRepresentantAn(final String representant) {
		waitForPageSourcePartDisplayed(By.id("view_fpoep:addRepresentantAN"), TIMEOUT_IN_SECONDS);
		getDriver().findElement(By.xpath("//*[contains(@id, 'view_fpoep:addRepresentantAN')]")).click();

		waitForPageSourcePartDisplayed(By.id(REPRESENTANT_AN_FUNCTION), TIMEOUT_IN_SECONDS);

		final WebElement elem = getDriver().findElement(
				By.xpath("//span[@id='nxw_representant_oep_representant_ansuggestDiv']/input"));
		fillField("Représentant", elem, representant);

		waitForPageSourcePart(
				By.xpath("//*[contains(@id,'nxw_representant_oep_representant_an_suggestionBox:suggest')]/tbody/tr[1]/td[2]"),
				TIMEOUT_IN_SECONDS);
		final WebElement suggest = getDriver()
				.findElement(
						By.xpath("//*[contains(@id,'nxw_representant_oep_representant_an_suggestionBox:suggest')]/tbody/tr[1]/td[2]"));
		suggest.click();
		waitForPageSourcePart(By.xpath("//*[contains(@id, 'nxw_representant_oep_representant_an_delete')]"),
				TIMEOUT_IN_SECONDS);
	}

	public void setRepresentantAnFonction(final String fonction) {
		final WebElement elem = getDriver().findElement(By.id(REPRESENTANT_AN_FUNCTION));
		getFlog().action("Selectionne \"" + fonction + "\" dans le select \"Fonction\"");
		final Select select = new Select(elem);
		select.selectByValue(fonction);
	}

	public void addRepresentantSenat(final String representant) {

		getDriver().findElement(By.xpath("//*[contains(@id, 'view_fpoep:addRepresentantSE')]")).click();

		waitForPageSourcePartDisplayed(By.id(REPRESENTANT_SENAT_FUNCTION), 18);

		final WebElement elem = getDriver().findElement(
				By.xpath("//span[@id='nxw_representant_oep_representant_sesuggestDiv']/input"));
		fillField("Représentant", elem, representant);

		waitForPageSourcePart(
				By.xpath("//*[contains(@id,'nxw_representant_oep_representant_se_suggestionBox:suggest')]/tbody/tr[1]/td[2]"),
				TIMEOUT_IN_SECONDS);
		final WebElement suggest = getDriver()
				.findElement(
						By.xpath("//*[contains(@id,'nxw_representant_oep_representant_se_suggestionBox:suggest')]/tbody/tr[1]/td[2]"));
		suggest.click();
		waitForPageSourcePart(By.xpath("//*[contains(@id, 'nxw_representant_oep_representant_se_delete')]"),
				TIMEOUT_IN_SECONDS);
	}

	public void setRepresentantSenatFonction(final String fonction) {
		final WebElement elem = getDriver().findElement(By.id(REPRESENTANT_SENAT_FUNCTION));
		getFlog().action("Selectionne \"" + fonction + "\" dans le select \"Fonction\"");
		final Select select = new Select(elem);
		select.selectByValue(fonction);
	}

	@SuppressWarnings("unchecked")
	public <T extends CommonWebPage> T traiter() {
		final WebElement elem = getDriver().findElement(By.xpath(TRAITER_BTN));
		getFlog().actionClickButton("Traiter");
		elem.click();
		waitForPageSourcePartDisplayed(By.id("popup_message"), 15);
		final WebElement elemConfirm = getDriver().findElement(By.id(CONFIRMER_BTN));
		elemConfirm.click();
		return (T) getPage(CreateOepPage.class);

	}

}
