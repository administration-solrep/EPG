package fr.dila.solonepg.page.main.pan.view;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.sword.naiad.commons.webtest.WebPage;

public class TableauMaitreFilterPage extends EPGWebPage {

    private final static String MINISTERE = "tableau_maitre_application_lois_form_filtre:nxl_tableau_maitre_application_lois_listing:nxw_activite_normative_widget_ministereResp_simple_2_lines_findButton";

    private final static String FILTRER_BTN_ID = "tableau_maitre_application_lois_form_filtre:_filter_filtrer";

    private final static String REINITIALISER_BTN_ID = "tableau_maitre_application_lois_form_filtre:_filter_raz";

    private final static String NOR_INPUT_PATH = "//div[@id='boxBody_div']/form/table/tbody/tr[2]/td[2]/input";

    private final static String TABLE_NOR_PATH = "//table[@class='dataOutput']/tbody/tr/td[3]";

    private final static String TABLE_MINISTERE_PATH = "//table[@class='dataOutput']/tbody/tr/td[2]";

    private void setMinistere(String ministere) {
        String xpath = String.format("//td[./*[contains(text(), '%s')]]/a", ministere);
        selectMultipleByInOrganigramme(Collections.singletonList(By.xpath(xpath)), MINISTERE);
    }

    public void filtreParMinistere(String ministere) {
        String ministereNor = ministere.substring(0, 3);
        this.setMinistere(ministere);
        this.filtrer();
        this.validateSearchByMinistere(ministereNor);

    }

    public void filtrerParNor(String nor) {
    	fillField("Champ NOR", getElementByXpath(NOR_INPUT_PATH), nor);
        this.filtrer();
        this.validateSearchByNor(nor);
    }

    private void filtrer() {
        WebElement filtreBtnEl = this.getElementById(FILTRER_BTN_ID);
        clickToPage(filtreBtnEl, WebPage.class);
    }

    public void reinitialiser() {
        this.openFiltre();
        fillField("Champ NOR", getElementByXpath(NOR_INPUT_PATH), "SomeTextForTest");
        WebElement reinitialiserBtnEl = this.getElementById(REINITIALISER_BTN_ID);
        reinitialiserBtnEl.click();
        this.waitForPageLoaded(getDriver());

        // Make sure filter inputs are empty
        Assert.assertTrue(this.getElementByXpath(NOR_INPUT_PATH).getText().isEmpty());
    }

    private void validateSearchBy(String value, String byPath) {
        List<WebElement> allNorElements = this.getElementsBy(By.xpath(byPath));
        for (WebElement webElement : allNorElements) {
            String norValue = webElement.getText();
            Assert.assertEquals(value, norValue);
        }

    }

    private void validateSearchByMinistere(String ministere) {
        this.validateSearchBy(ministere, TABLE_MINISTERE_PATH);
    }

    private void validateSearchByNor(String nor) {
        this.validateSearchBy(nor, TABLE_NOR_PATH);
    }

}
