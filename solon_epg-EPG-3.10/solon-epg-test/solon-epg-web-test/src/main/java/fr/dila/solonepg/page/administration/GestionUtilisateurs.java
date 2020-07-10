package fr.dila.solonepg.page.administration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.solonepg.page.recherche.user.UserDetailPage;

public class GestionUtilisateurs extends EPGWebPage {

    public static final String RECHERCHE_INPUT_ID = "searchForm:searchText";
    public static final String RECHERCHE_BTN_ID = "searchForm:searchButton";

    /**
     * @param expression
     */
    public void chercherUtilisateurs(String expression) {

        WebElement el = this.getElementById(RECHERCHE_INPUT_ID);
        this.fillField("", el, expression);
        // clic sur le boutton de recherche
        this.getElementById(RECHERCHE_BTN_ID).click();
        this.waitForPageLoaded(getDriver());
    }

    public UserDetailPage getDetail(String utilisateur) {
        By linkText = By.linkText(utilisateur);
        getDriver().findElement(linkText).click();
        return this.getPage(UserDetailPage.class);
    }

}
