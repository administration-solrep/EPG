package fr.dila.solonepg.page.administration;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class GestionOrganigrammePage extends EPGWebPage {

	public EditerOrganigrammePage modifierElement(String onElementTitle) {
		String xPath = "//span[text()='" + onElementTitle + "']";
		WebElement onElement = this.getElementByXpath(xPath);

		Actions oAction = new Actions(this.getDriver());
		oAction.moveToElement(onElement);
		// right click
		oAction.contextClick(onElement).build().perform();

		this.waitElementVisibilityByXpath("//span[text()='Modifier élément']");

		this.getElementByXpath("//span[text()='Modifier élément']").click();

		this.waitElementVisibilityById("editEntiteForm");

		return this.getPage(EditerOrganigrammePage.class);
	}

	public EditerOrganigrammePage modifierElement(List<String> pathToElement, String title) {
		if (pathToElement != null && !pathToElement.isEmpty()) {
			selectMultipleInOrganigramme(pathToElement, pathToElement.get(pathToElement.size() - 1));
		}

		return modifierElement(title);
	}
}
