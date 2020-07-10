package fr.dila.solonepg.page.recherche.dernierresultat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;

public abstract class AbstractDernierResultat extends EPGWebPage {

    /**
     * First Row => Index = 1
     * 
     * @param index start by 1
     */
    public void selectRow(int index) {
    	// les boutons de sélection sont rafraichis par la sélection
    	WebElement selectionButtons = findElement(By.id("selection_buttons"));
        String replaceBy = index == 1 ? "" : new StringBuilder("_").append(Integer.toString(index - 1)).toString();
        String checkBoxId = index == 1 ? this.getCheckBoxId().replace("@INDEX@", replaceBy) : this.getCheckBoxId().replace("@INDEX@", replaceBy);
        getElementById(checkBoxId).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.stalenessOf(selectionButtons));
    }

    public void selectRow(String checkBoxId) {
        this.getElementById(checkBoxId).click();
    }

    /**
     * Clic sur le boutton Ajouter aux favoris de consultation
     */
    public void ajouterAuxFavorisDeConsultaion() {
        this.getElementById(this.getAjouterAuxFavrisDeConsultation()).click();
    }

    protected abstract String getAjouterAuxFavrisDeConsultation();

    protected abstract String getCheckBoxId();
}
