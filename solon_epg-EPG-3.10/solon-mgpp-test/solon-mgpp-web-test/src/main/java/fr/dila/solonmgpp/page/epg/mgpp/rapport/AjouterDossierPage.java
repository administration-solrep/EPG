package fr.dila.solonmgpp.page.epg.mgpp.rapport;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonmgpp.page.commun.CommonWebPage;

public class AjouterDossierPage extends CommonWebPage {
    
    
    
    public static final String ID_DOSSIER_INPUT = "create_fpdoc:nxl_fiche_presentation_doc:nxw_fiche_doc_idDossier";
    public static final String OBJET_INPUT = "create_fpdoc:nxl_fiche_presentation_doc:nxw_fiche_doc_objet";
    public static final String BASE_LEGALE_INPUT = "create_fpdoc:nxl_fiche_presentation_doc:nxw_fiche_doc_baseLegale";
    public static final String IDDOSSIER_INPUT = "create_fpdoc:nxl_fiche_presentation_doc:nxw_fiche_doc_idDossier";
    public static final String DATE_LETTRE_PM_INPUT = "create_fpdoc:nxl_fiche_presentation_doc:nxw_fiche_doc_dateLettrePmInputDate";

    public static final String COMMISSION_INPUT = "create_fpdoc:nxl_fiche_presentation_doc:nxw_fiche_doc_commissions_suggest";
    public static final String COMMISSION_SUGGEST = "//table[@id='create_fpdoc:nxl_fiche_presentation_doc:nxw_fiche_doc_commissions_suggestionBox:suggest']/tbody/tr/td[2]";
    public static final String COMMISSION_DELETE = "create_fpdoc:nxl_fiche_presentation_doc:nxw_fiche_doc_commissions_list:0:nxw_fiche_doc_commissions_delete";

    
    public void setObjet(final String objet) {
        final WebElement elem = getDriver().findElement(By.id(OBJET_INPUT));
        fillField("Objet", elem, objet);
    }
    
    public void setDateLettrePM(final String dateLettrePm) {
        final WebElement elem = getDriver().findElement(By.id(DATE_LETTRE_PM_INPUT));
        fillField("Date de la lettre du Premier ministre", elem, dateLettrePm);
    }
    
    public void setBaseLegale(final String baseLegale) {
        final WebElement elem = getDriver().findElement(By.id(BASE_LEGALE_INPUT));
        fillField("Base légale", elem, baseLegale);
    }
    
    public void ajouterDossier() {
        getDriver().findElement(By.xpath("//*[@value='Ajouter un dossier']")).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.id(IDDOSSIER_INPUT)));
    }
    

    
   
    public void addCommission(final String commision) {
        final WebElement elem = getDriver().findElement(By.id(COMMISSION_INPUT));
        fillField("Auteur", elem, commision);
        waitForPageSourcePart(By.xpath(COMMISSION_SUGGEST), TIMEOUT_IN_SECONDS);
        final WebElement suggest = getDriver().findElement(By.xpath(COMMISSION_SUGGEST));
        suggest.click();
        waitForPageSourcePart(By.id(COMMISSION_DELETE), TIMEOUT_IN_SECONDS);
    }

    public void  enregistrer(){
    getDriver().findElement(By.xpath("//*[@value='Enregistrer']")).click();
    waitForPageSourcePart("Document créé.", TIMEOUT_IN_SECONDS);
    }
}
