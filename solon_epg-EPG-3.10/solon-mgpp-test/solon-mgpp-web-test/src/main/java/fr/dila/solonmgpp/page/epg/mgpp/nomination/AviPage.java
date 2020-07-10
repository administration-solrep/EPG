package fr.dila.solonmgpp.page.epg.mgpp.nomination;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonmgpp.page.commun.CommonWebPage;

public class AviPage extends CommonWebPage {

    public static final String MESSAGE_LISTING_ROW_1 =  "fiche_presentation_avi_list:nxl_fiche_presentation_AVI_listing_1:nxw_fiche_AVI_idDossier_listing_1:msgName";
    
    
    public PageFichePresentationAvi goToAviPageDetail(String link, String id) {
        openCorbeille(link);
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
        openRecordByIdFromList(id, PageFichePresentationAvi.class);
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[contains(@src,'lock_add_24.png')]")));
        return getPage(PageFichePresentationAvi.class);
    }

}
