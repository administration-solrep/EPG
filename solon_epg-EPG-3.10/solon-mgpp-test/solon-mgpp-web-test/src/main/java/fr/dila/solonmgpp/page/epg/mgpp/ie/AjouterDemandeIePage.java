package fr.dila.solonmgpp.page.epg.mgpp.ie;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonmgpp.page.commun.CommonWebPage;

public class AjouterDemandeIePage extends CommonWebPage {
    
    
    
    public static final String OBJET = "create_fpie:nxl_fiche_presentation_intervention_exterieure:nxw_fiche_intervention_exterieure_intitule";
    public static final String NOM_ORGANISME_INPUT = "create_fpavi:nxl_fiche_presentation_AVI:nxw_fiche_AVI_nomOrganisme";
    public static final String BASE_LEGALE_INPUT = "create_fpavi:nxl_fiche_presentation_AVI:nxw_fiche_AVI_baseLegale";
    public static final String NOMINE_INPUT = "create_fpavi:nxl_nomine_AVI:nxw_nomine_avi_nomine";
    public static final String DATE_INPUT = "create_fpie:nxl_fiche_presentation_intervention_exterieure:nxw_fiche_intervention_exterieure_dateInputDate";
    
    

    
    public static final String AUTEUR_INPUT = "//*[@id='nxw_fiche_intervention_exterieure_auteursuggestDiv']/input";
    public static final String AUTEUR_SUGGEST = "//table[contains(@id,'nxw_fiche_intervention_exterieure_auteur_suggestionBox')]/tbody/tr/td[2]";
    public static final String AUTEUR_DELETE = "//img[contains(@id,'nxw_fiche_intervention_exterieure_auteur_delete')]";
    
    
    
    public InterventionExterieurePage ajouteDemandeIE(String auteur, String inutile, String currentDate) {
       addAuteur(auteur);
       setObjet(inutile);
       setDateTransmission(currentDate);
       getDriver().findElement(By.xpath("//*[@value='Enregistrer']")).click();
       return getPage(InterventionExterieurePage.class);
   }
    
    public void setObjet(final String objet) {
        final WebElement elem = getDriver().findElement(By.id(OBJET));
        fillField("Objet", elem, objet);
    }
    
    public void setDateTransmission(final String dateTransmission) {
        final WebElement elem = getDriver().findElement(By.id(DATE_INPUT));
        fillField("Date de la transmission", elem, dateTransmission);
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
    
    public void addAuteur(final String auteur) {
        final WebElement elem = getDriver().findElement(By.xpath(AUTEUR_INPUT));
        fillField("Auteur", elem, auteur);
        waitForPageSourcePart(By.xpath(AUTEUR_SUGGEST), TIMEOUT_IN_SECONDS);
        final WebElement suggest = getDriver().findElement(By.xpath(AUTEUR_SUGGEST));
        suggest.click();
        waitForPageSourcePart(By.xpath(AUTEUR_DELETE), TIMEOUT_IN_SECONDS);
    }
}
