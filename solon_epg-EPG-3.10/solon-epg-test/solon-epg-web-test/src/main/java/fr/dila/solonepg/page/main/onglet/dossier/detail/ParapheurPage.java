package fr.dila.solonepg.page.main.onglet.dossier.detail;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ParapheurPage extends MenuItemPage {

	public static final String	DOCUMENTS_STYLE_PATH				= "//*[contains(@id, 'uploadParapheurFiles') and contains(@id, 'file') and not(contains(@id,'Items'))]";

	public static final String	MINISTERE_PORTEUR_PATH				= "//td[contains(text(),'Répertoire réservé au ministère porteur') and not (contains(text(),'SGG'))]";
	public static final String	TOUS_UTILISATEURS_PATH				= "//td[contains(text(),'Répertoire accessible à tous les utilisateurs')]";
	public static final String	AJOUTER_DOCUMENT_LINK				= "//span[text()='Ajouter document']";
	public static final String	DOSSIER_ATTACH_MINISTERE_PORTEUR	= "src/main/attachments/doc2005.doc";
	public static final String	DOSSIER_ATTACH_TOUS_UTILISATEURS	= "src/main/attachments/doc2006.doc";

	public static final String	DOSSIER_PJ							= "document_propertiesFDD:fondDeDossierTree:2:1::zoneFichierParapheur";
	private static final String	ADD_FILE_INPUT_FDD					= "createFileFddForm:uploadFondDeDossierFiles:file";
	private static final String	ADD_FILE_INPUT_FDD2					= "createFileFddForm:uploadFondDeDossierFiles:add2";
	private static final String	ADD_FILE_FDD						= "createFileFddForm:createFileFddButtonImage";

	/**
	 * Ajout d'un document
	 */
	public void ajoutDocumentParapheur(String linkXpath, String doc) {
		boolean succes = false;
		int tentative = 0;
		while (!succes && tentative < 2) {
			WebElement elementByXpath = this.getElementByXpath(linkXpath);
			elementByXpath.click();

			String ajouterDocPath = "//span[text()='Ajouter document']";

			new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By
					.xpath(ajouterDocPath)));

			final WebElement elem2 = getDriver().findElement(By.xpath(ajouterDocPath));

			elem2.click();

			waitForPageSourcePartDisplayed(By.id("createFileParapheurPanelHeader"), TIMEOUT_IN_SECONDS);

			addAttachmentParapheur("src/main/attachments/" + doc);

			try {
				// waitForPageSourcePart(doc, TIMEOUT_IN_SECONDS);
				succes = true;
			} catch (Exception e) {
				getFlog().action("Le document ne s'est pas ajouté, on recommence");
				tentative++;
			}
		}
	}

	@Override
	public String addAttachment(String attachment) {
		// ClientException ce = new
		// ClientException("Merci d'utiliser plutot les methodes addAttachmentParapheur et addAttachmentFDD");
		throw new RuntimeException("Merci d'utiliser plutot les methodes addAttachmentParapheur et addAttachmentFDD");
	}

	public String addAttachmentParapheur(String attachment) {
		final File file = new File(attachment);
		getFlog().startAction("ajout de la pièce jointe : " + file.getName());
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By
				.id("createFileParapheurForm:uploadFondDeDossierFiles:file")));

		WebElement element = getDriver().findElement(By.id("createFileParapheurForm:uploadFondDeDossierFiles:file"));
		getFlog().action("Entre " + file.getAbsolutePath() + " comme chemin");

		// Il faut d'abord cliquer sur le bouton pour que le champ 'element'
		// soit visible
		WebElement elementToClick = getDriver().findElement(
				By.id("createFileParapheurForm:uploadFondDeDossierFiles:add2"));
		elementToClick.click();

		element.sendKeys(file.getAbsolutePath());

		waitForPageSourcePart("Téléchargement effectué", TIMEOUT_IN_SECONDS);

		WebElement addFile = null;

		final List<WebElement> elements = getDriver().findElements(
				By.id("createFileParapheurForm:createFileParapheurButtonImage"));
		for (WebElement webElement : elements) {
			if (webElement.isDisplayed()) {
				addFile = webElement;
				break;
			}
		}

		if (addFile != null) {
			addFile.click();
		} else {
			Assert.fail("On n'a pas trouvé l'image d'ajout de fichier");
		}

		getFlog().endAction();

		return file.getName();
	}

	protected String getAddFileInput() {
		return "//*[contains(@id, 'createFile') and contains(@id, 'Files:file') and not(contains(@id, 'Items'))]";
	}

	protected String getAddFile() {
		return "//*[contains(@id, 'createFile') and contains(@id, 'ButtonImage')]/img";
	}

	// Fond de dossier

	public void ajouterDocumentFDD(String xpath, String fichier) {
		getDriver().findElement(By.xpath(xpath)).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(AJOUTER_DOCUMENT_LINK)));
		getDriver().findElement(By.xpath(AJOUTER_DOCUMENT_LINK)).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By
				.id("createFileFddPanelCDiv")));
		addAttachmentFDD(fichier);
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By
				.id("createFileFddPanelCDiv")));

	}

	public void ajouterDocumentsFDD() {
		getDriver().findElement(By.xpath(MINISTERE_PORTEUR_PATH)).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(AJOUTER_DOCUMENT_LINK)));
		getDriver().findElement(By.xpath(AJOUTER_DOCUMENT_LINK)).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By
				.id("createFileFddPanelCDiv")));
		addAttachmentFDD(DOSSIER_ATTACH_MINISTERE_PORTEUR);

		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By
				.id("createFileFddPanelCDiv")));

		getDriver().findElement(By.xpath(TOUS_UTILISATEURS_PATH)).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(AJOUTER_DOCUMENT_LINK)));
		getDriver().findElement(By.xpath(AJOUTER_DOCUMENT_LINK)).click();
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By
				.id("createFileFddPanelCDiv")));
		addAttachmentFDD(DOSSIER_ATTACH_TOUS_UTILISATEURS);

	}

	public String addAttachmentFDD(final String attachment) {
		return addAttachmentFDD(attachment, "", true);
	}

	public String addAttachmentFDD(final String attachment, String fileFieldSuffix, boolean validatePopup) {
		final File file = new File(attachment);
		getFlog().startAction("ajout de la pièce jointe : " + file.getName());
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By
				.id(ADD_FILE_INPUT_FDD + fileFieldSuffix)));

		WebElement element = getDriver().findElement(By.id(ADD_FILE_INPUT_FDD + fileFieldSuffix));
		getFlog().action("Entre " + file.getAbsolutePath() + " comme chemin");

		// Il faut d'abord cliquer sur le bouton pour que le champ 'element'
		// soit visible
		WebElement elementToClick = getDriver().findElement(By.id(ADD_FILE_INPUT_FDD2));
		elementToClick.click();

		fillField("Fichier fond de dossier (upload)", element, file.getAbsolutePath());
		waitForPageSourcePart("Téléchargement effectué", TIMEOUT_IN_SECONDS);

		if (validatePopup) {
			WebElement treePanel = findElement(By.id("document_propertiesFDD:fddDocumentViewPanel"));
			WebElement addFile = null;
			final List<WebElement> elements = getDriver().findElements(By.id(ADD_FILE_FDD));
			for (WebElement webElement : elements) {
				if (webElement.isDisplayed()) {
					addFile = webElement;
					break;
				}
			}
	
			if (addFile != null) {
				addFile.click();
				// attente du rechargement
				new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(treePanel));
				new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.id("document_propertiesFDD:fddDocumentViewPanel")));
			} else {
				Assert.fail("On n'a pas trouvé l'image d'ajout de fichier");
			}
		}

		getFlog().endAction();

		return file.getName();
	}

	public ParapheurPage recharger() {
		getDriver().findElement(By.xpath("//*[@alt='Recharger']")).click();
		return getPage(ParapheurPage.class);
	}

	public void fillField(String fieldTitle, WebElement fieldElt, String value) {
		super.fillField(fieldTitle, fieldElt, value);
	}

}
