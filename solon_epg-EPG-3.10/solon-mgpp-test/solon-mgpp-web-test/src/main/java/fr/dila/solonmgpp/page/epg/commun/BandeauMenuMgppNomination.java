package fr.dila.solonmgpp.page.epg.commun;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonmgpp.page.epg.mgpp.nomination.AviPage;
import fr.dila.solonmgpp.page.epg.mgpp.nomination.DemandesAuditionPage;
import fr.dila.solonmgpp.page.epg.mgpp.oep.OepPage;

public class BandeauMenuMgppNomination extends SolonEpgPage {

    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "OEP")
    private WebElement oep;
    
    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Avis de nomination")
    private WebElement avisNomination;
    
    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Demandes d'audition")
    private WebElement DemandesAudition;

    /**
     * Va vers l'espace d'organisme extra parlementaire
     * 
     * @return
     */
    public OepPage goToOEP() {
        return click(oep, OepPage.class);
    }
    
    /**
     * Va vers l'espace avis de nomination
     * @return
     */
    public AviPage goToAvisNomination(){
        return click(avisNomination, AviPage.class);
    }
    
    /**
     * Va vers l'espace avis de nomination
     * @return
     */
    public DemandesAuditionPage goToDemandesaudition(){
        return click(DemandesAudition, DemandesAuditionPage.class);
    }
}
