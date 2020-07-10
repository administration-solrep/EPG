package fr.dila.solonepg.page.creation;

import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.solonepg.webtest.utils.RegexUtils;
import fr.sword.naiad.commons.webtest.WebPage;

public abstract class AbstractCreationDossierPage extends EPGWebPage {

	public final static Pattern	regExpDossierCree				= Pattern.compile("[\\s|\\t]*Dossier (.*) enregistr.*");

	public static final String	BOUTON_TERMINER_ID_SELECT_POSTE	= "creation_dossier_select_poste:buttonTerminer";

	public <T extends WebPage> T appuyerBoutonSuivant(Class<T> class1) {
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.elementToBeClickable(By
				.id(getButtonSuivantId())));
		getDriver().findElement(By.id(getButtonSuivantId())).click();
		getFlog().startAction("Appuyez sur le bouton suivant");
		T page = getPage(class1);
		getFlog().endAction();
		return page;
	}

	public <T extends WebPage> T appuyerBoutonTerminer(Class<T> class1) {
		getFlog().action("appuyer bouton Terminer");
		WebElement terminer =
				new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(
						ExpectedConditions.elementToBeClickable(By.id(getButtonTerminerId())));
		return clickToPage(terminer, class1);
	}

	protected abstract String getButtonSuivantId();

	protected abstract String getButtonTerminerId();

	public String waitAndGetNumeroDossierCree() {
		WebElement element =
				new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(
						ExpectedConditions.presenceOfElementLocated(By.xpath("//li[@class='infoFeedback']")));
		String content = element.getAttribute("innerHTML");
		return RegexUtils.getGroupMatch(regExpDossierCree, content);
	}

	public <T extends WebPage> T appuyerBoutonTerminerSelectPoste(Class<T> class1) {
		getFlog().action("appuyer bouton Terminer");
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.elementToBeClickable(By
				.id(BOUTON_TERMINER_ID_SELECT_POSTE)));
		getDriver().findElement(By.id(BOUTON_TERMINER_ID_SELECT_POSTE)).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By
				.id(BOUTON_TERMINER_ID_SELECT_POSTE)));
		T page = getPage(class1);
		return page;
	}

}
