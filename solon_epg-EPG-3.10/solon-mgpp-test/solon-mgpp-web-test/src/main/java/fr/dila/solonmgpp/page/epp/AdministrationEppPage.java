package fr.dila.solonmgpp.page.epp;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonmgpp.page.commun.CommonWebPage;

public class AdministrationEppPage extends CommonWebPage {

	private static final String	GOUVERNEMENT_TD_ID				= "viewOrganigrammeForm:organigrammeTree:node:2::ins:text";
	private static final String	CREER_POSTE_WEBSERVICE_LINK_ID	= "ctxMenu:ust_createPstWs:anchor";
	private static final String	LIBELLE_ID						= "createPosteForm:nxl_postews:nxw_label";
	private static final String	URL_WS__ID						= "createPosteForm:nxl_postews:nxw_wsUrl";
	private static final String	USERNAME_WS_ID					= "createPosteForm:nxl_postews:nxw_wsUser";
	private static final String	PASSWORD_WS_ID					= "createPosteForm:nxl_postews:nxw_wsPassword";
	private static final String	ENREGISTRER_BOUTON_ID			= "createPosteForm:button_create";

	public void createEppParametres() {

		WebElement linkOfContextMenu = getDriver().findElement(By.id(GOUVERNEMENT_TD_ID));

		Actions action = new Actions(getDriver());

		action.contextClick(linkOfContextMenu)
		// .sendKeys(Keys.ARROW_DOWN)
				/*
				 * .sendKeys(Keys.ARROW_DOWN) .sendKeys(Keys.RETURN)
				 */
				// .click(mnEle)
				// .sendKeys(Keys.ARROW_DOWN,Keys.ARROW_DOWN,Keys.RETURN)
				.build().perform();

		WebDriverWait wait = new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CREER_POSTE_WEBSERVICE_LINK_ID)));

		getDriver().findElement(By.id(CREER_POSTE_WEBSERVICE_LINK_ID)).click();

		// Action action2 = action.contextClick(linkOfContextMenu).build();
		// action2.perform();

		// WebElement mnEle = getDriver().findElement(By.xpath("//span[@value='Créer poste webservice']"));
		// action.moveToElement(mnEle).Perform();
		// // Giving 5 Secs for submenu to be displayed
		// Thread.sleep(5000L);

		WebDriverWait wait2 = new WebDriverWait(getDriver(), 15);
		wait2.until(ExpectedConditions.presenceOfElementLocated(By.id("createPosteForm")));

		getDriver().findElement(By.id(LIBELLE_ID)).sendKeys("mgpp");
		getDriver().findElement(By.id(URL_WS__ID)).sendKeys("http://localhost:8180/solon-epg/site/solonmgpp/");
		getDriver().findElement(By.id(USERNAME_WS_ID)).sendKeys("adminsgg");
		getDriver().findElement(By.id(PASSWORD_WS_ID)).sendKeys("adminsgg");

		getDriver().findElement(By.id(ENREGISTRER_BOUTON_ID)).click();

		WebDriverWait wait3 = new WebDriverWait(getDriver(), 18);
		// wait3.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(), 'mgpp']")));
		wait3.until(ExpectedConditions.invisibilityOfElementLocated(By.id("createPosteForm")));

		Assert.assertTrue(this.hasElement(By.xpath("//*[contains(text(), 'mgpp')]")));
	}
}
