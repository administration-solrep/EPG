package fr.dila.solonepg.page.administration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FondDeDossierPage extends SupportTypeActePage {

	private static final String ADD_FILE_PANEL_ID							= "fond_dossier_model_properties:nxl_fond_dossier_modele_racine:addFilePanel";
	private static final String CONTEXT_MENU_CLASS_NAME						= "menuContextuelStyle";
	private static final String FOND_DOSSIER_TREE_ID						= "fond_dossier_model_properties:treeFondDossierModelSubview:fondDeDossierModelTree";
	public static final String	DELETE_FORMAT								= "//a[@id='fond_dossier_model_properties:nxl_fond_dossier_modele_racine:nxw_format_autorise_fdd_model_list:0:nxw_format_autorise_fdd_model_delete']/img";
	public static final String	LAST_DELETE_FORMAT							= "//*[@id='fond_dossier_model_properties:nxl_fond_dossier_modele_racine:nxw_format_autorise_fdd_model_list:17:nxw_format_autorise_fdd_model_delete']/img";

	public static final String	MOTS_CLE_LABEL								= "fond_dossier_model_properties:nxl_fond_dossier_modele_racine:nxw_format_autorise_fdd_model_motsCleLabel";

	public static final String	ADD_FORMAT									= "//*[@id='fond_dossier_model_properties:nxl_fond_dossier_modele_racine:nxw_format_autorise_fdd_model_add_file']/img";

	public static final String	REPERTOIRE_RESERVE_MINISTERE_PORTEUR_PATH	= "//span[text()='Répertoire réservé au ministère porteur']";

	public static final String	NOUVEAU_REPERTOIRE_PATH						= "//span[text()='Nouveau répertoire']";

	public static final String	ANNEX_NON_PUBLIEE							= "//span[text()='Annexes non publiées']";

	public static final String	NOUVEAU_REPERTOIRE_AVANT_ANNEX				= "fond_dossier_model_properties:treeFondDossierModelSubview:fondDeDossierModelTree:4:1:1::nomRepertoireDeletable";

	private static final String	FOND_DOSSIER_TITLE_ID						= "fond_dossier_model_properties:treeFondDossierModelFolderSubview:nxl_fond_dossier_modele_repertoire_formulaire:nxw_title";

	private static final String	ANNULER_BUTTON_PATH							= "//input[@id='fond_dossier_model_properties:treeFondDossierModelFolderSubview:annulerModelefonDossier']";

	private static final String	VALIDER_BUTTON_PATH							= "//input[@value='Valider']";

	public static final String	NOUVELLES_ANNEXES_PATH						= "//span[contains(text(), 'Nouvelles Annexes')]";

	public void deleteFormat() {
		getDriver().findElement(By.xpath(DELETE_FORMAT)).click();
		WebElement element = findElement(By.id(ADD_FILE_PANEL_ID));
		// wait panel add reload
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(element));
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By
				.xpath(LAST_DELETE_FORMAT)));
	}

	public void ajoutFormat(String format) {
		final WebElement elem = getDriver().findElement(By.id(MOTS_CLE_LABEL));
		fillField("Format", elem, format);
		WebElement element = findElement(By.id(ADD_FILE_PANEL_ID));
		getDriver().findElement(By.xpath(ADD_FORMAT)).click();
		// wait panel add reload
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(element));
		waitForPageSourcePart(format, TIMEOUT_IN_SECONDS);
	}

	public void ajouterRepertoireDedans(final String repertoire, boolean isDeletable, String newIndex) {
		this.contextMenuClick(By.xpath(repertoire), "Ajouter dedans", true, newIndex);
	}

	public void ajouterRepertoireAvant(final String repertoire, boolean isDeletable, String newIndex) {
		this.contextMenuClick(By.xpath(repertoire), "Ajouter avant", true, newIndex);
	}

	public void ajouterRepertoireApres(final String repertoire, boolean isDeletable, String newIndex) {
		this.contextMenuClick(By.xpath(repertoire), "Ajouter après", true, newIndex);
	}

	public void modifierRepertoire(final String repertoire, boolean isDeletable) {
		this.contextMenuClick(By.id(repertoire), "Editer", true, null);
		waitForPageLoaded(getDriver());
	}

	public void supprimerRepertoire(final String repertoire, boolean validateDeletion) {
		By repertoireBy = By.xpath(repertoire);
		WebElement repertoireElm = findElement(repertoireBy);
		contextMenuClick(repertoireBy, "Supprimer", false, null);
		// Wait the popup to Open
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By
				.id("deleteFondDeDossierModelFolderPanelCDiv")));

		// Valider ou Annuler
		String elementInPopup = validateDeletion ? "deleteFondDeDossierForm:validerSuppressionText"
				: "deleteFondDeDossierForm:annulerSuppressionText";

		findElement(By.id(elementInPopup)).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(repertoireElm));
	}

	private void contextMenuClick(By by, String action, boolean waitRefresh, String newIndex) {
		final WebElement elem = getDriver().findElement(by);
		clickContextMenu(elem, action);

		if (waitRefresh) {
			// Wait for element refresh
			new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(elem));
	
			if (newIndex != null) {
				// Wait to Refresh and display the new created folder
				String newRepertoireId = "fond_dossier_model_properties:treeFondDossierModelSubview:fondDeDossierModelTree:"
					+ newIndex + ":nomRepertoireDeletable";
				By newRepertoire = By.id(newRepertoireId);
				new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(newRepertoire));
			}
		}
	}

	private void clickContextMenu(WebElement element, String libelle) {
		getFlog().startAction("Ouvre le menu contextuel du lien " + element.getText());
		By context = By.className(CONTEXT_MENU_CLASS_NAME);
		By item = By.xpath(String.format(".//span[contains(text(),'%s')]", libelle));
		element.click();
		By by = new ByChained(context, item);
		WebElement contextMenuItem =
				new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(by));
		contextMenuItem.click();
	}

	public void setRepertoireName(String name) {
		final WebElement elem = getDriver().findElement(By.id(FOND_DOSSIER_TITLE_ID));
		fillField("Nom", elem, name);
	}

	public void annluerEditionRepertoire() {
		WebElement tree = findElement(By.id(FOND_DOSSIER_TREE_ID));
		final WebElement elem = getDriver().findElement(By.xpath(ANNULER_BUTTON_PATH));
		elem.click();
		// Wait for element refresh
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(tree));
	}

	public void validerEditionRepertoire() {
		WebElement tree = findElement(By.id(FOND_DOSSIER_TREE_ID));
		final WebElement elem = getDriver().findElement(By.xpath(VALIDER_BUTTON_PATH));
		elem.click();
		// Wait for element refresh
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(tree));
	}

	@Override
	protected String[] getTypeActeReplacement() {
		String[] replacement = new String[2];
		replacement[0] = "fond_dossier";
		replacement[1] = "fdd";
		return replacement;
	}

}
