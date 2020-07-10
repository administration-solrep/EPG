package fr.dila.solonmgpp.page.epg.commun;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import fr.dila.solonepg.page.main.onglet.CreationPage;
import fr.dila.solonepg.page.main.onglet.PilotageANPage;
import fr.dila.solonepg.page.main.onglet.RecherchePage;
import fr.dila.solonepg.page.main.onglet.SuiviPage;
import fr.dila.solonepg.page.main.onglet.TraitementPage;
import fr.dila.solonmgpp.page.epg.espaceEpg.AdministrationEpgPageForMGPP;
import fr.dila.solonmgpp.page.epg.espaceEpg.MgppPage;

public class BandeauMenuEpg extends SolonEpgPage {
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
    
    @FindBy(xpath="//div[@id='espace_parlementaire']/div/div/a")
    private WebElement mgpp;   
    /**
     * Va vers l'espace de traitement
     * @return
     */
    public TraitementPage goToEspaceTraitement(){
        return click(espaceTraitement, TraitementPage.class);
    }
    
    
    /**
     * Va vers l'espace de création
     * @return
     */
    public CreationPage goToEspaceCreation(){
        return click(espaceCreation, CreationPage.class);
    }
    
    /**
     * Va vers la recherche
     * @return
     */
    public RecherchePage goToRecherche(){
        return click(recherche, RecherchePage.class);
    }
    
    
    /**
     * Va vers le suivi
     * @return
     */
    public SuiviPage goToSuivi(){
        return click(suivi, SuiviPage.class);
    }
    
    /**
     * Va vers l'administration
     * @return
     */
    public AdministrationEpgPageForMGPP goToAdministration(){
        return click(administration, AdministrationEpgPageForMGPP.class);
    }

    /**
     * Va vers le pilotage de l'activité normative
     * @return
     */
    public PilotageANPage goToPilotageAN(){
        return click(pilotageAN, PilotageANPage.class);
    }
    
    /**
     * Va vers le mgpp
     * @return
     */
    public MgppPage goToMGPP(){
        return click(mgpp, MgppPage.class);
    }
}
