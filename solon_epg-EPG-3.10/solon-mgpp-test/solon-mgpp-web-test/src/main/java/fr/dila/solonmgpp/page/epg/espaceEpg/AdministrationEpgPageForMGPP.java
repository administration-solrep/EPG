package fr.dila.solonmgpp.page.epg.espaceEpg;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.main.onglet.AdministrationEpgPage;
import fr.dila.solonmgpp.webtest.helper.url.UrlEppHelper;
import fr.sword.naiad.commons.webtest.WebPage;

public class AdministrationEpgPageForMGPP extends AdministrationEpgPage {

	private static final String	WS_EPP_URL			= "param_mgpp_connexion:nxl_parametrage_mgpp:nxw_parametrage_mgpp_urlEpp";
	private static final String	WS_EPP_USERNAME		= "param_mgpp_connexion:nxl_parametrage_mgpp:nxw_parametrage_mgpp_loginEpp";
	private static final String	WS_EPP_PASSWORD		= "param_mgpp_connexion:nxl_parametrage_mgpp:nxw_parametrage_mgpp_passEpp";
	private static final String	WS_EPP_SAVE_BOUTON	= "param_mgpp_connexion:save";

	@FindBy(how = How.ID, id = WS_EPP_URL)
	private WebElement urlEpp;
	@FindBy(how = How.ID, id = WS_EPP_USERNAME)
	private WebElement userEpp;
	@FindBy(how = How.ID, id = WS_EPP_PASSWORD)
	private WebElement passEpp;
	@FindBy(how = How.ID, id = WS_EPP_SAVE_BOUTON)
	private WebElement saveButton;

	public void createMgppParametres() {
		WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("param_mgpp_connexion")));

		fillField("url", urlEpp, "http://localhost:8080/solon-epp/site/solonepp/");
		fillField("user", userEpp, "ws-gouvernement");
		fillField("password", passEpp, "ws-gouvernement-error");
		// pas de changement de page
		saveButton.click();

		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.textToBePresentInElementLocated(
				By.xpath("//li[@class='warningFeedback']"), "Connexion impossible avec ces paramètres"));
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By
				.xpath("//li[@class='warningFeedback']")));

		fillField("url", urlEpp, UrlEppHelper.DEFAULT_APP_URL + "solon-epp/site/solonepp/");
		fillField("user", userEpp, "ws-gouvernement");
		fillField("password", passEpp, "ws-gouvernement");
		// changement de page
		clickToPage(saveButton, WebPage.class);

		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.textToBePresentInElementLocated(
				By.xpath("//li[@class='infoFeedback']"), "Paramétrage enregistré"));
	}
}
