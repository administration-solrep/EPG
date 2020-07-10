package fr.dila.solonepg.page.an.loi;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.solonepg.page.main.onglet.dossier.detail.DossierDetailMenu;
import fr.dila.solonepg.page.main.pan.view.TableauMaitreViewPage;

public class ApplicationDesLoisPage extends EPGWebPage {

    private static final String NOR_INPUT_ID = "loi_form:loi_input";
    private static final String ADD_BTN_ID = "loi_form:loiAddBtn";

    public TableauMaitreViewPage ajouterNor(String nor) {
        WebElement el = this.getElementById(NOR_INPUT_ID);
        this.fillField("Nor", el, nor);
        this.getElementById(ADD_BTN_ID).click();
        sleep(2);
        return this.getPage(TableauMaitreViewPage.class);
    }

    public DossierDetailMenu getDossierDetailMenu(String nor) {

        By linkText = By.linkText(nor);
        getDriver().findElement(linkText).click();
        waitForPageSourcePart(By.className("reponseToolBar"), TIMEOUT_IN_SECONDS);
        return this.getPage(DossierDetailMenu.class);
    }
}
