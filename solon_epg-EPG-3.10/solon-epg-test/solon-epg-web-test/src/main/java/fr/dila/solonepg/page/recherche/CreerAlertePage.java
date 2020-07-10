package fr.dila.solonepg.page.recherche;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class CreerAlertePage extends EPGWebPage {

    public static final String SAUVEGARDER_ALERTE_BOUTTON_PATH = "//input[@value='Sauvegarder']";
    public static final String NAME_INPUT_ID =  "alertForm:nxl_alert:nxw_title";
    public static final String VALIDITY_BEGIN_DATE_INPUT_ID =  "alertForm:nxl_alert:nxw_dateValidityBeginInputDate";
    public static final String VALIDITY_END_DATE_INPUT_ID = "alertForm:nxl_alert:nxw_dateValidityEndInputDate";
    public static final String PERIODICITY_ID = "alertForm:nxl_alert:nxw_periodicity";

    
    public void setintitule(final String intitule) {
        final WebElement elem = findElement(By.id(NAME_INPUT_ID));
        fillField("Name", elem, intitule);
    }
    
    public void setValidtyBeginDate(final String validtyBeginDate) {
        final WebElement elem = findElement(By.id(VALIDITY_BEGIN_DATE_INPUT_ID));
        fillField("Date de première exécution", elem, validtyBeginDate);
    }
    
    public void setValidtyEndDate(final String validtyEndDate) {
        final WebElement elem = findElement(By.id(VALIDITY_END_DATE_INPUT_ID));
        fillField("Date de fin de validité", elem, validtyEndDate);
    }

    public void setPeriodcity(final String periodcity) {
        final WebElement elem = findElement(By.id(PERIODICITY_ID));
        fillField("Name", elem, periodcity);
    } 
    
    public void sauvegarder() {
        getElementByXpath(SAUVEGARDER_ALERTE_BOUTTON_PATH).click();
    } 
}
