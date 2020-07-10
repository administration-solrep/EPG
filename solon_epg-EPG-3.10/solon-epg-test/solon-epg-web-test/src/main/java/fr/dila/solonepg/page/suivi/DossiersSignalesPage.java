package fr.dila.solonepg.page.suivi;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class DossiersSignalesPage extends EPGWebPage {
    
    public static final String VIDER_CORBEILLE_INPUT_PATH  = "//input[@value='Vider la corbeille']" ;
    public static final String AJOUTER_SELECTION_INPUT_PATH  = "//input[@value='Ajouter à la sélection']" ;
    public static final String RETIRER_DOSSIER_INPUT_PATH  = "//input[@value='Retirer Dossiers']" ;

    public static final String POPUP_OK_INPUT_PATH  = "//input[@id='popup_ok']";
   
    public void checkRow(final String row){
        final WebElement defaultcheckBox = getDriver().findElement(By.id("espace_suivi_dossiers_signales_content:nxl_dossier_listing_dto_"+row+":nxw_listing_ajax_checkbox_dto"));
        defaultcheckBox.click();
      }
    
    public void retirerSelection(){
       getDriver().findElement(By.xpath(RETIRER_DOSSIER_INPUT_PATH)).click();
      }
    
    public void viderCorbeille(){
        getDriver().findElement(By.xpath(VIDER_CORBEILLE_INPUT_PATH)).click();
        waitForPageSourcePart("Êtes-vous sûr de vouloir vider la corbeille des dossiers signalés ?", TIMEOUT_IN_SECONDS);
        getDriver().findElement(By.xpath(POPUP_OK_INPUT_PATH)).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated((By.xpath(DossiersSignalesPage.VIDER_CORBEILLE_INPUT_PATH))));
       }

}
