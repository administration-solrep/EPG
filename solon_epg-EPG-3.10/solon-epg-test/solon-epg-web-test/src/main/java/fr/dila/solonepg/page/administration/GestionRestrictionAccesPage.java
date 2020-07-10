package fr.dila.solonepg.page.administration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonepg.page.commun.EPGWebPage;

/**
 * Gestion de l'état de l'application
 * 
 * @author jbrunet
 * 
 */
public class GestionRestrictionAccesPage extends EPGWebPage {

	@FindBy(how = How.XPATH, using = "//input[@value='TRUE']")
	private WebElement	activerRestrictionAccesBtn;

	@FindBy(how = How.XPATH, using = "//input[@value='FALSE']")
	private WebElement	desactiverRestrictionAccesBtn;

	@FindBy(how = How.XPATH, using = "//input[@value='Enregistrer']")
	private WebElement	enregistrerBtn;

	@FindBy(how = How.XPATH, using = "//textarea[contains(@id, 'nxw_restrictionDescription')]")
	private WebElement	descriptionRestriction;

	public void setDescriptionRestrictionAcces(String description) {
		fillField("Description de la restriction", descriptionRestriction, description);
	}

	public void activerRestrictionAcces() {
		activerRestrictionAccesBtn.click();
	}

	public void desactiverRestrictionAcces() {
		desactiverRestrictionAccesBtn.click();
	}

	public GestionRestrictionAccesPage enregistrerChangements() {
		return clickToPage(enregistrerBtn, GestionRestrictionAccesPage.class);
	}

}
