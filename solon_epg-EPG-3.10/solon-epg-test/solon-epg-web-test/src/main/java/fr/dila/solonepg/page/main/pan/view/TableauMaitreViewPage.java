package fr.dila.solonepg.page.main.pan.view;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.sword.naiad.commons.webtest.WebPage;

public class TableauMaitreViewPage extends EPGWebPage {

    String DELETE_TEXT_MAITRE_ICON_ID = "tableau_maitre_application_lois:nxl_tableau_maitre_application_lois_listing_@INDEX@:nxw_activite_normative_widget_remove@AINDEX@:nxw_activite_normative_widget_remove@AINDEX@delete_img";

    @FindBy(how = How.XPATH, using = "//a[contains(text(), 'Filtres')]")
    private WebElement textMaitreFiltre;

    /**
     * @param index start by 1
     */
    public void removeTextMaitreByIndex(Integer index) {

        String aIndex = index.equals(1) ? "" : "_" + index;
        String delIconId = DELETE_TEXT_MAITRE_ICON_ID.replace("@INDEX@", index.toString()).replace("@AINDEX@", aIndex);
        WebElement elementById = this.getElementById(delIconId);
        if (elementById != null) {
            elementById.click();
            // Confirm deletion
            this.waitElementVisibilityById("popup_ok");
            this.getElementById("popup_ok").click();
            // Wait for element invisibility
            this.waitForPageSourcePartHide(By.id(delIconId), TIMEOUT_IN_SECONDS);
        }
    }

    public TableauMaitreDossierDetail getTextMaitreDetailMenu(String nor) {
        By linkText = By.linkText(nor);
        getDriver().findElement(linkText).click();
        waitForPageSourcePart(By.className("subbodyContainer"), TIMEOUT_IN_SECONDS);
        return this.getPage(TableauMaitreDossierDetail.class);
    }

    public TableauMaitreFilterPage openTextMaitreFilter() {
        textMaitreFiltre.click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.id("tableau_maitre_application_lois_form_filtre")));
        return getPage(TableauMaitreFilterPage.class);
    }

    public void sortByMinistere() {
        String id = "tableau_maitre_application_lois:nxl_tableau_maitre_application_lois_listing_1:activite_normative_widget_ministereResp_simple_2_lines_header_sort";
        clickToPage(this.getElementById(id), WebPage.class);
    }

    public void sortByNor() {
        String id = "tableau_maitre_application_lois:nxl_tableau_maitre_application_lois_listing_1:dossier_listing_link_an_numeroNor_header_sort";
        clickToPage(this.getElementById(id), WebPage.class);
    }

}
