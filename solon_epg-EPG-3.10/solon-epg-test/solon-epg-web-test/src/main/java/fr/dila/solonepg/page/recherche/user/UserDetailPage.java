package fr.dila.solonepg.page.recherche.user;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class UserDetailPage extends EPGWebPage {

    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Modifier")
    private WebElement modifier;

    public ModifierUser modifier() {
        return this.clickToPage(modifier, ModifierUser.class);
    }
}
