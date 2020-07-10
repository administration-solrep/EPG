package fr.dila.solonepg.page.main.onglet;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class TraitementPage extends EPGWebPage {

    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Pour indexation")
    private WebElement pourIndexation;

    public EPGWebPage pourIndexation() {
        return clickToPage(pourIndexation, EPGWebPage.class);
    }
}
