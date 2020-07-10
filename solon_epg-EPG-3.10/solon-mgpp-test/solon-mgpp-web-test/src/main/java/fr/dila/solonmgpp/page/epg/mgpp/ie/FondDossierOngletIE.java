package fr.dila.solonmgpp.page.epg.mgpp.ie;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonmgpp.page.epg.mgpp.FondDossierOnglet;

public class FondDossierOngletIE extends FondDossierOnglet {

    // //*[@id='document_properties:documentViewPanel']/input
    public void ajouterUnfichier(String fichier) {
        getDriver().findElement(By.xpath("//*[@value='Ajouter un fichier']")).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='editFileFormFP:annulerParapheurFileCreationTextFP']")));
        addAttachment(fichier);
        // assertTrue("piece-jointe.doc");
    }
    
    public String addAttachment(final String attachment) {
        final File file = new File(attachment);
        getFlog().startAction("ajout de la pièce jointe : " + file.getName());
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.xpath(getAddFileInput())));

        WebElement element = getDriver().findElement(By.xpath(getAddFileInput()));
        
		// Il faut d'abord cliquer sur le bouton pour que le champ 'element'
		// soit visible"//
		WebElement elementToClick = getDriver().findElement(By.id("editFileFormFP:uploadParapheurFilesFP:add1"));
		elementToClick.click();
		
        getFlog().action("Entre " + file.getAbsolutePath() + " comme chemin");

        element.sendKeys(file.getAbsolutePath());

        waitForPageSourcePart("Téléchargement effectué", TIMEOUT_IN_SECONDS);

        WebElement addFile = getDriver().findElement(By.xpath(getAddFile()));

        final List<WebElement> elements = getDriver().findElements(By.xpath(getAddFile()));
        for (WebElement webElement : elements) {
            if (webElement.isDisplayed()) {
                addFile = webElement;
                break;
            }
        }
        addFile.click();

        getFlog().endAction();

        return file.getName();
    }

  
}
