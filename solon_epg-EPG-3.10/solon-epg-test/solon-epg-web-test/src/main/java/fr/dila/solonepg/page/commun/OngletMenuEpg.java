package fr.dila.solonepg.page.commun;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import fr.dila.solonepg.page.main.onglet.AdministrationEpgPage;
import fr.dila.solonepg.page.main.onglet.CreationPage;
import fr.dila.solonepg.page.main.onglet.PilotageANPage;
import fr.dila.solonepg.page.main.onglet.RecherchePage;
import fr.dila.solonepg.page.main.onglet.SuiviPage;
import fr.dila.solonepg.page.main.onglet.TraitementPage;

public class OngletMenuEpg extends EPGWebPage {
    @FindBy(xpath="//div[@id='SHOW_REPOSITORY']/div/div/a")
    private WebElement espaceTraitement;
    
    @FindBy(xpath="//div[@id='espace_creation']/div/div/a")
    private WebElement espaceCreation;
    
    @FindBy(xpath="//div[@id='espace_recherche']/div/div/a")
    private WebElement recherche;
    
    @FindBy(xpath="//div[@id='espace_suivi']/div/div/a")
    private WebElement suivi;
    
    @FindBy(xpath="//div[@id='espace_administration']/div/div/a")
    private WebElement administration;
    
    @FindBy(xpath="//div[@id='espace_activite_normative']/div/div/a")
    private WebElement pilotageAN;

    /**
     * Va vers l'espace de traitement
     * @return
     */
    public TraitementPage goToEspaceTraitement(){
        return clickToPage(espaceTraitement, TraitementPage.class);
    }
    
    
    /**
     * Va vers l'espace de création
     * @return
     */
    public CreationPage goToEspaceCreation(){
        return clickToPage(espaceCreation, CreationPage.class);
    }
    
    /**
     * Va vers la recherche
     * @return
     */
    public RecherchePage goToRecherche(){
        return clickToPage(recherche, RecherchePage.class);
    }
    
    
    /**
     * Va vers le suivi
     * @return
     */
    public SuiviPage goToSuivi(){
        return clickToPage(suivi, SuiviPage.class);
    }
    
    /**
     * Va vers l'administration
     * @return
     */
    public AdministrationEpgPage goToAdministration(){
        return clickToPage(administration, AdministrationEpgPage.class);
    }

    /**
     * Va vers le pilotage de l'activité normative
     * @return
     */
    public PilotageANPage goToPilotageAN(){
        return clickToPage(pilotageAN, PilotageANPage.class);
    }
    
//    /**
//     * Va vers le mgpp
//     * @return
//     */
//    public WebPage goToMGPP(){
//        return click(mgpp, WebPage.class);
//    }
    
/*
    public <T extends SolonEpgPage> T rechercheRapide(String query, Class<T> clazz) {
        fillField("Recherche rapide", rechercheRapideInputElt, query);
        return click(rechercheRapideAtteindreBtn, clazz);
    }
*/
}
