package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonmgpp.page.commun.CommonWebPage;
import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;

public abstract class AbstractCreateComm extends CommonWebPage {
    
    public static final String BROUILLON_BTN = "//span[@id='evenement_metadonnees:documentViewPanel']/div[2]/input";
    public static final String PUBLIER_BTN = "//span[@id='evenement_metadonnees:documentViewPanel']/div[2]/input[2]";
    public static final String CANCEL_BTN = "//span[@id='evenement_metadonnees:documentViewPanel']/div[2]/input[3]";



    @SuppressWarnings("unchecked")
    public <T extends AbstractDetailComm> T creerBrouillon() {
        final WebElement elem = getDriver().findElement(By.xpath(BROUILLON_BTN));
        getFlog().actionClickButton("Créer Brouillon");
        elem.click();

        waitForPageSourcePart("La communication a été enregistrée", TIMEOUT_IN_SECONDS);
        return (T) getPage(getCreateResultPageClass());

    }
    
    @SuppressWarnings("unchecked")
    public <T extends AbstractDetailComm> T publier() {
        final WebElement elem = getDriver().findElement(By.xpath(PUBLIER_BTN));
        getFlog().actionClickButton("Publier");
        elem.click();
        // XXX : à corriger ! la ligne bloque meme quand le message en question s'affiche :(
//        waitForPageSourcePart("La communication a été publiée", TIMEOUT_IN_SECONDS);
        return (T) getPage(getCreateResultPageClass());

    }
    
    protected abstract Class<? extends AbstractDetailComm> getCreateResultPageClass();
    
 
}
