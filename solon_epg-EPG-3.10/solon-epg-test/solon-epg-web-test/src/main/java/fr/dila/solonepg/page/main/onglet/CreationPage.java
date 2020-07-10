package fr.dila.solonepg.page.main.onglet;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.solonepg.page.creation.CreationDossier100Page;

public class CreationPage extends EPGWebPage {

    public static final String CREATE_DOSSIER = "formCreationDossier:buttonCreationDossier";
    public static final String DOSSIER_ATTACH_ACTE_INTEGRAL = "src/main/attachments/doc2003.doc";
    public static final String DOSSIER_ATTACH_EXTRAIT = "src/main/attachments/doc2004.doc";
    public static final String TELECHARGEMENT_EFFECTUE_MESSAGE = "Téléchargement effectué";
    private static final String DERNIER_DOSSIER_CREER_PATH = "//table[@class='dataOutput']/tbody/tr[1]/td[2]/div/a";

    public CreationDossier100Page createDossier() {
        CreationDossier100Page page = clickToPage(findElement(By.id(CREATE_DOSSIER)), CreationDossier100Page.class);
        waitForPageSourcePart("Création d'un dossier", TIMEOUT_IN_SECONDS);
        return page;
    }

    public void ajouterDocuments() {
        getFlog().startAction("attachment d'un dossier sur l'Acte intégral");
        getDriver().findElement(By.xpath("//td[text()='Acte intégral']")).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Ajouter document']")));
        getDriver().findElement(By.xpath("//span[text()='Ajouter document']")).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.id("editFilePanelCDiv")));
        addAttachment(DOSSIER_ATTACH_ACTE_INTEGRAL);
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.id("editFilePanelCDiv")));
        getFlog().endAction();

        getFlog().startAction("attachment d'un dossier sur l'Extrait");
        getDriver().findElement(By.xpath("//td[text()='Extrait']")).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Ajouter document']")));
        getDriver().findElement(By.xpath("//span[text()='Ajouter document']")).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.id("editFilePanelCDiv")));
        addAttachment(DOSSIER_ATTACH_EXTRAIT);
        getFlog().endAction();

    }

    public void ajouterDocument(String xpath, String fichier) {
        getFlog().startAction("attachment d'un fichier");
        getDriver().findElement(By.xpath(xpath)).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Ajouter document']")));
        getDriver().findElement(By.xpath("//span[text()='Ajouter document']")).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.id("editFilePanelCDiv")));
        addAttachment(fichier);
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.id("editFilePanelCDiv")));
        getFlog().endAction();
    }

    public void lancerDossier() {
    	getFlog().startAction("Lancement d'un dossier");
    	// <td>clickAndWait</td> <td>//*[@title='Lancer le dossier']</td>
        getDriver().findElement(By.xpath("//*[@title='Lancer le dossier']")).click();
        waitForPageLoaded(getDriver());
        getFlog().endAction();
    }

    public int calculerDernierCompteur() {
        String text = this.getElementByXpath(DERNIER_DOSSIER_CREER_PATH).getText();
        String compteur = text.substring(6, 11);
        return new Integer(compteur).intValue();
    }

    @Override
	public String addAttachment(String attachment) {
		final File file = new File(attachment);
        getFlog().startAction("ajout de la pièce jointe : " + file.getName());
		new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By
				.id("createFileParapheurForm:uploadFondDeDossierFiles:file")));

		WebElement element = getDriver().findElement(By.id("createFileParapheurForm:uploadFondDeDossierFiles:file"));
		getFlog().action("Entre " + file.getAbsolutePath() + " comme chemin");

		// Il faut d'abord cliquer sur le bouton pour que le champ 'element'
		// soit visible
		WebElement elementToClick = getDriver().findElement(By.id("createFileParapheurForm:uploadFondDeDossierFiles:add2"));
		elementToClick.click();

		element.sendKeys(file.getAbsolutePath());

		waitForPageSourcePart("Téléchargement effectué", TIMEOUT_IN_SECONDS);

		WebElement addFile = null;

		final List<WebElement> elements = getDriver().findElements(By.id("createFileParapheurForm:createFileParapheurButtonImage"));
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
    
}
