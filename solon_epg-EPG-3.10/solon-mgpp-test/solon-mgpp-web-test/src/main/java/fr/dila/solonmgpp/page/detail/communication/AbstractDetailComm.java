package fr.dila.solonmgpp.page.detail.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonmgpp.page.commun.CommonWebPage;
import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.sword.naiad.commons.webtest.WebPage;

public abstract class AbstractDetailComm extends CommonWebPage {

	public static final String	MODIFIER_BTN	= "//*[@value='Modifier']";
	public static final String	TRAITE_BTN		= "//*[@value='Traité']";
	public static final String	TRAITER_BTN		= "//*[@value='Traiter']";
	public static final String	Completer_BTN	= "//*[@value='Compléter']";
	public static final String	CONFIRMER_BTN	= "popup_ok";

	@SuppressWarnings("unchecked")
	public <T extends AbstractCreateComm> T modifier() {
		final WebElement elem = getDriver().findElement(By.xpath(MODIFIER_BTN));
		getFlog().actionClickButton("Modifier");
		elem.click();

		waitForPageSourcePart("Modification communication :", TIMEOUT_IN_SECONDS);
		return (T) getPage(getModifierResultPageClass());
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractCreateComm> T traite() {
		By byPath = By.xpath(TRAITE_BTN);
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(byPath));
		final WebElement elem = getDriver().findElement(byPath);
		getFlog().actionClickButton("Traité");
		elem.click();

		waitForPageSourcePart("Transition traité effectuée", TIMEOUT_IN_SECONDS);
		return (T) getPage(getModifierResultPageClass());
	}

	public <T extends WebPage> T traiter(final Class<T> pageClazz) {
		final WebElement elem = getDriver().findElement(By.xpath(TRAITER_BTN));
		getFlog().actionClickButton("Traiter");
		elem.click();
		waitForPageSourcePartDisplayed(By.id("popup_message"), 15);
		final WebElement elemConfirm = getDriver().findElement(By.id(CONFIRMER_BTN));
		elemConfirm.click();
		return getPage(pageClazz);

	}

	public <T extends CommonWebPage> T completer(final Class<T> pageClazz) {
		final WebElement elem = getDriver().findElement(By.xpath(Completer_BTN));
		getFlog().actionClickButton("Compléter");
		sleep(1);
		elem.click();
		// elem.click();

		waitForPageSourcePart("Complétion communication :", TIMEOUT_IN_SECONDS);
		return getPage(pageClazz);

	}

	protected abstract Class<? extends AbstractCreateComm> getModifierResultPageClass();

}
