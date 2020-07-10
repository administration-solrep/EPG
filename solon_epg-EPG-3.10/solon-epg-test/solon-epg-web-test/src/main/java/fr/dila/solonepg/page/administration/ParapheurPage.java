package fr.dila.solonepg.page.administration;

import java.io.File;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ParapheurPage extends SupportTypeActePage {

	public static final String	ACTE_INTEGRALE_PATH					= "//a[text()='Acte intégral']";

	public static final String	EXTRAIT_PATH						= "//a[text()='Extrait']";

	public static final String	NON_VIDE_CHCKBOX_ID					= "parapheur_model_properties:treeParapheurFolderSubview:nxl_parapheur_modele_repertoire_formulaire:nxw_non_vide_parapheur_model";

	public static final String	VALIDER_BOUTTON_PATH				= "//input[@value='Valider']";

	public static final String	ANNULER_BOUTTON_PATH				= "//input[@id='parapheur_model_properties:treeParapheurFolderSubview:annulerModeleParapheur']";

	public static final String	FORMAT_INPUT_ID						= "parapheur_model_properties:treeParapheurFolderSubview:nxl_parapheur_modele_repertoire_formulaire:nxw_format_autorise_parapheur_model_motsCleLabel";

	public static final String	ADD_FORMAT_IMG_PATH					= "//a[@id='parapheur_model_properties:treeParapheurFolderSubview:nxl_parapheur_modele_repertoire_formulaire:nxw_format_autorise_parapheur_model_add_file']/img";

	public static final String	PARAPHEUR_TITLE_ID					= "parapheur_model_properties:treeParapheurFolderSubview:nxl_parapheur_modele_repertoire_formulaire:nxw_title_parapheur";

	public static final String	FEUILLE_DE_STYLE_PATH				= "//*[contains(@id, 'uploadFeuilleStyle') and contains(@id, 'file') and not(contains(@id,'Items'))]";

	public static final String	FEUILLE_DE_STYLE_INVALIDE			= "La feuille de style proposée n'est pas valide et ne sera pas enregistrée en l'état!";

	public static final String	SUPPRIMER_PATH						= "//div[text()='Supprimer']";

	public static final String	SUPPRIMER_DOC_PATH					= "//*[contains(@id, 'nxw_format_autorise_parapheur_model_list:@INDEX@') and contains(@id,'nxw_format_autorise_parapheur_model_listItem')]";

	public static final String	BOUTTON_SUPPRIMER_ID				= "parapheur_model_properties:treeParapheurFolderSubview:nxl_parapheur_modele_repertoire_formulaire:nxw_feuille_style_parapheur_model:uploadFeuilleStyle:clean2";

	public static final String	FEUILLE_DE_STYLE_FORMAT_ERROR_ID	= "parapheur_model_properties:treeParapheurFolderSubview:nxl_parapheur_modele_repertoire_formulaire:nxw_feuille_style_parapheur_model:errorName";

	public static final String	MINISTERE_PORTEUR_PATH				= "//td[contains(text(),'Répertoire réservé au ministère porteur') and not (contains(text(),'SGG'))]";
	public static final String	TOUS_UTILISATEURS_PATH				= "//td[contains(text(),'Répertoire accessible à tous les utilisateurs')]";
	public static final String	AJOUTER_DOCUMENT_LINK				= "//span[text()='Ajouter document']";
	public static final String	DOSSIER_ATTACH_MINISTERE_PORTEUR	= "src/main/attachments/doc2005.doc";
	public static final String	DOSSIER_ATTACH_TOUS_UTILISATEURS	= "src/main/attachments/doc2006.doc";

	public static final String	DOSSIER_PJ							= "document_propertiesFDD:fondDeDossierTree:2:1::zoneFichierParapheur";

	@Override
	protected String[] getTypeActeReplacement() {
		String[] replacement = new String[2];
		replacement[0] = "parapheur";
		replacement[1] = "parapheur";
		return replacement;
	}

	public void clickRepertoire(String repertoire) {
		this.waitElementVisibilityByXpath(repertoire);
		this.getElementByXpath(repertoire).click();
		this.waitElementVisibilityByXpath("//span[text()='Format']");
	}

	public void waitNonVideCheckBoxVisibility() {
		this.waitElementVisibilityById(NON_VIDE_CHCKBOX_ID);
	}

	public void clickNonVideCheckBox() {
		this.getElementById(NON_VIDE_CHCKBOX_ID).click();
	}

	public void clickValiderBoutton() {
		this.getElementByXpath(VALIDER_BOUTTON_PATH).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By
				.xpath(VALIDER_BOUTTON_PATH)));

	}

	public void clickAnnulerBoutton() {
		this.getElementByXpath(ANNULER_BOUTTON_PATH).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By
				.xpath(ANNULER_BOUTTON_PATH)));
	}

	public void isNonVideUnSelected() {
		Assert.assertFalse(this.isCheckBoxSelected(NON_VIDE_CHCKBOX_ID));
	}

	public void isNonVideSelected() {
		Assert.assertTrue(this.isCheckBoxSelected(NON_VIDE_CHCKBOX_ID));
	}

	public void addFormat(String format, int index) {
		this.waitElementVisibilityById(FORMAT_INPUT_ID);
		this.waitElementVisibilityByXpath(ADD_FORMAT_IMG_PATH);

		WebElement elementById = findElement(By.id(FORMAT_INPUT_ID));
		this.fillField("Format", elementById, format);

		waitForFieldValue(FORMAT_INPUT_ID, format);

		this.getElementByXpath(ADD_FORMAT_IMG_PATH).click();

		this.waitElementVisibilityByXpath(SUPPRIMER_DOC_PATH.replace("@INDEX@", new Integer(index).toString()));
	}

	public void assertTrueInputValue(String inputId, String value) {
		this.waitElementVisibilityById(inputId);
		String valueFromInput = this.getInputValueById(inputId);
		Assert.assertTrue(value.equals(valueFromInput));
	}

	public void ajouterFeuilleDeStyle(String fileName) {
		final File file = new File("src/main/attachments/" + fileName);
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By
				.xpath(FEUILLE_DE_STYLE_PATH)));
		WebElement element = getDriver().findElement(By.xpath(FEUILLE_DE_STYLE_PATH));
		fillField("Feuille de style (upload)", element, file.getAbsolutePath());
		this.waitElementVisibilityById(BOUTTON_SUPPRIMER_ID);
	}

	public void supprimerFeuillesDeStyles() {
		this.waitElementVisibilityByXpath(SUPPRIMER_PATH);
		this.getElementByXpath(SUPPRIMER_PATH).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By
				.id(FEUILLE_DE_STYLE_FORMAT_ERROR_ID)));

	}

}
