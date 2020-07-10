package fr.dila.solonepg.page.administration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class CreationBranchePage extends EPGWebPage {

	@FindBy(how = How.ID, using = "document_create:button_create")
	private WebElement	boutonCreer;
	@FindBy(how = How.ID, using = "document_create:j_id265")
	private WebElement	boutonAnnuler;

	public CreationModelePage definirNombreBranche() {

		return clickToPage(boutonCreer, CreationModelePage.class);

	}

	public CreationModelePage annuler() {

		return clickToPage(boutonAnnuler, CreationModelePage.class);
	}
}
