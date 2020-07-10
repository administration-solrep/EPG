package fr.dila.solonmgpp.page.epg.mgpp.nomination;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonmgpp.page.commun.CommonWebPage;

public class CreateAviPage extends CommonWebPage {
    
    
    
    public static final String ID_DOSSIER_INPUT = "create_fpavi:nxl_fiche_presentation_AVI:nxw_fiche_AVI_idDossier";
    public static final String NOM_ORGANISME_INPUT = "create_fpavi:nxl_fiche_presentation_AVI:nxw_fiche_AVI_nomOrganisme";
    public static final String BASE_LEGALE_INPUT = "create_fpavi:nxl_fiche_presentation_AVI:nxw_fiche_AVI_baseLegale";
    public static final String NOMINE_INPUT = "create_fpavi:nxl_nomine_AVI:nxw_nomine_avi_nomine";
    public static final String DATE_DEBUT_INPUT = "create_fpavi:nxl_nomine_AVI:nxw_nomine_avi_dateDebut";
    
    public void setIdDossier(final String idDossier) {
        final WebElement elem = getDriver().findElement(By.id(ID_DOSSIER_INPUT));
        fillField("Identifiant dossier", elem, idDossier);
    }
    
    public void setNomOrganisme(final String nomOrganisme) {
        final WebElement elem = getDriver().findElement(By.id(NOM_ORGANISME_INPUT));
        fillField("Nom organisme", elem, nomOrganisme);
    }
    
    
    public void setBaseLegale(final String baseLegale) {
        final WebElement elem = getDriver().findElement(By.id(BASE_LEGALE_INPUT));
        fillField("Base légale", elem, baseLegale);
    }
    
    public void ajouterNomine(String nominne) {
        getDriver().findElement(By.xpath("//*[@value='Ajouter un nominé']")).click();
        new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.id("create_fpavi:nxl_nomine_AVI:nxw_nomine_avi_nomine")));
        final WebElement elem = getDriver().findElement(By.id(NOMINE_INPUT));
        fillField("Nominé", elem, nominne);
    }
    

    public void  enregistrer(){
    getDriver().findElement(By.xpath("//*[@value='Enregistrer']")).click();
    waitForPageSourcePart("Organisme créé.", TIMEOUT_IN_SECONDS);
    }
}
