package fr.dila.solonepg.page.main.onglet;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.solonepg.page.suivi.DossiersSignalesPage;

public class SuiviPage extends EPGWebPage {

    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Dossiers signalés")
    private WebElement dossiersSignales;

    /**
     * Va vers l'administration
     * 
     * @return
     */
    public DossiersSignalesPage goToDossiersSignalesPage() {
        return clickToPage(dossiersSignales, DossiersSignalesPage.class);
    }

    /**
     * Go to Tableaux Dynamic
     */
    public void clicTableauxDynamiques(String userName) {
        String expand = userName.equals("adminsgg") ? "form_espace_suivi:espaceSuiviTree:node:3::base:handle" : "form_espace_suivi:espaceSuiviTree:node:2::base:handle";
        this.getElementById(expand).click();
    }

    /**
     * Go to Mon infocentre
     */
    public void clickToMonInfocentre() {
        WebElement element = getElementById("form_espace_suivi:espaceSuiviTree:node:0::base:handle");
        element.click();
        // tree is reloaded when clicked
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(element));
    }

    /**
     * Go to infocentre genereale
     */
    public void clickToInfocentreGenerale() {
        WebElement element = getElementById("form_espace_suivi:espaceSuiviTree:node:1::base:handle");
        element.click();
        // tree is reloaded when clicked
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(element));
    }

    /**
     * Go to node
     */
    public void clickSuiviNode(String nodeId) {
        WebElement element = getElementById(nodeId);
        element.click();
        // tree is reloaded when clicked
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(element));
        if (!nodeId.endsWith(":handle")) {
            // si on ne clique pas sur un (+), alors on doit attendre le chargement de la page
            waitForPageLoaded(getDriver());
        }
    }

}
