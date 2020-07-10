package fr.dila.solonepg.page.parametre;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class ParametreDetailPage extends EPGWebPage {

    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Modifier")
    private WebElement modifier;

    public ModifierParametre modifier() {
        return this.clickToPage(modifier, ModifierParametre.class);
    }
}
