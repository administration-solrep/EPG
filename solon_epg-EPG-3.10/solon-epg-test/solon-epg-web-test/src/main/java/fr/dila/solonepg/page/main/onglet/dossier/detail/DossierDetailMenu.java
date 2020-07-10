package fr.dila.solonepg.page.main.onglet.dossier.detail;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonepg.page.administration.FondDeDossierPage;
import fr.dila.solonepg.page.commun.EPGWebPage;

public class DossierDetailMenu extends EPGWebPage {

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Bordereau")
	private WebElement	bordereau;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Parapheur")
	private WebElement	parapheur;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Document")
	private WebElement	document;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Feuille de route")
	private WebElement	feuilleDeRoute;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Journal")
	private WebElement	journal;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Texte-maître")
	private WebElement	textMaitre;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Fond de dossier")
	private WebElement	fondDeDossier;

	/**
	 * go to Bordereau tab
	 * 
	 * @return
	 */
	public BordereauPage goToBordereau() {
		return clickToPage(this.bordereau, BordereauPage.class);
	}

	/**
	 * go to Parapheur tab
	 * 
	 * @return
	 */
	public ParapheurPage goToParapheur() {
		return clickToPage(this.document, ParapheurPage.class);
	}

	/**
	 * go to Parapheur tab
	 * 
	 * @return
	 */
	public FondDeDossierPage goToFondDeDossier() {
		return clickToPage(this.document, FondDeDossierPage.class);
	}

	/**
	 * go to Feuille de route tab
	 * 
	 * @return
	 */
	public FeuilleDeRoutePage goToFeuilleDeRoute() {
		return clickToPage(this.feuilleDeRoute, FeuilleDeRoutePage.class);
	}

	/**
	 * go to journal tab
	 * 
	 * @return
	 */
	public JournalPage goToJournalPage() {
		return clickToPage(this.journal, JournalPage.class);
	}

	/**
	 * go to Texte-maître tab
	 * 
	 * @return TextMaitrePage
	 */
	public TextMaitrePage goToTextMaitrePage() {
		return clickToPage(this.textMaitre, TextMaitrePage.class);
	}
}
