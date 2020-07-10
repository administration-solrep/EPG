package fr.dila.solonepg.page.administration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;
import junit.framework.Assert;

public class ParametrageDeLapplicationPage extends EPGWebPage {

    public static final String VALIDER_BTN = "parametrage_application_properties:validerParametrageApplication";
    public static final String AJOUTER_BTN = "parametrage_application_properties:metadonneesDisponiblecopylink";
    public static final String ENLEVER_BTN = "parametrage_application_properties:metadonneesDisponibleremovelink";
    public static final String AJOUTER_TOUT_BTN_PATH = "//a[@id='parametrage_application_properties:metadonneesDisponiblecopyAlllink']";
    public static final String REMOVE_TOUT_BTN_PATH = "//a[@id='parametrage_application_properties:metadonneesDisponibleremoveAlllink']";

    public void valider(boolean waitForPageLoad) {
        final WebElement elem = getDriver().findElement(By.id(VALIDER_BTN));
        ((Locatable) elem).getCoordinates().inViewPort();
        WebElement body = findElement(By.cssSelector("body"));
        elem.click();
        if (waitForPageLoad) {
            new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(body));
            waitForPageLoaded(getDriver());
        }
    }

    public void valider() {
        valider(true);
    }

    public void setParamValue(String elementName, String value) {
        WebElement el = this.getElementByName(elementName);
        this.fillField("", el, value);
    }

    public void assertValue(String name, String value) {
        WebElement el = this.getElementByName(name);
        String elValue = el.getAttribute("value");
        Assert.assertEquals(value, elValue);
    }

    public void ajouterMetadonnee(String title) {
        this.clickElement(title);
        JavascriptExecutor jse = (JavascriptExecutor) getDriver();
        jse.executeScript("document.getElementById('" + AJOUTER_BTN + "').click();");
    }

    private void clickElement(String title) {
        String templatePath = "//td[contains(text(),'" + title + "')]";
        WebElement el = this.getElementByXpath(templatePath);
        el.click();
    }

    public void enleverMetadonnee(String title) {
        this.clickElement(title);
        JavascriptExecutor jse = (JavascriptExecutor) getDriver();
        jse.executeScript("document.getElementById('" + ENLEVER_BTN + "').click();");

    }

    public void ajouterTout() {
        this.getElementByXpath(AJOUTER_TOUT_BTN_PATH).click();
    }

    public void removeTout() {
        this.getElementByXpath(REMOVE_TOUT_BTN_PATH).click();
    }
}
