package fr.dila.solonepg.page.commun;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonepg.page.administration.CreationBranchePage;
import fr.dila.solonepg.page.administration.CreationEtapePage;

/*
 * Popup page
 * contient les choix :
 * Ajouter une étape
 * Créer des branches
 */
public class AjouterAlaFeuilleDeRoute extends EPGWebPage {

	@FindBy(how = How.LINK_TEXT, using = "Ajouter une étape")
	private WebElement			ajouterUneEtape;
	@FindBy(how = How.LINK_TEXT, using = "Créer des branches")
	private WebElement			ajouterBranche;

	public static final String	POPUP_VALIDATION_ID	= "selectRouteElementsTypeForCreationForm:selectRouteElementsTypePanelCDiv";

	/**
	 * Click sur le lien : Ajouter une étape
	 */
	public CreationEtapePage ajouterUneEtape() {
		// Wait the popup to open
		this.waitElementVisibilityById(POPUP_VALIDATION_ID);
		return this.clickToPage(ajouterUneEtape, CreationEtapePage.class);
	}

	public CreationBranchePage ajouterBranche() {
		// Wait the popup to open
		this.waitElementVisibilityById(POPUP_VALIDATION_ID);
		return this.clickToPage(ajouterBranche, CreationBranchePage.class);
	}

}
