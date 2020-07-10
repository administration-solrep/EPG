package fr.dila.solonmgpp.page.epg.espaceEpg;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import fr.dila.solonepg.page.main.onglet.TraitementPage;

public class TraitementPageForMGGP extends TraitementPage {
	
    @FindBy(xpath="//div[@id='espace_parlementaire']/div/div/a")
    private WebElement mgpp; 
    
    @FindBy(xpath="//div[@id='espace_administration']/div/div/a")
    private WebElement administration;
	
    public MgppPage goToMGPP(){
        return clickToPage(mgpp, MgppPage.class);
    }

	public AdministrationEpgPageForMGPP goToAdministration() {
		return clickToPage(administration, AdministrationEpgPageForMGPP.class);
	}
}
