package fr.dila.solonmgpp.page.epg.commun;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonmgpp.page.epg.espaceEpg.DeclarationPage;
import fr.dila.solonmgpp.page.epg.espaceEpg.NominationPage;
import fr.dila.solonmgpp.page.epg.espaceEpg.OrganisationPage;
import fr.dila.solonmgpp.page.epg.espaceEpg.RapportPage;
import fr.dila.solonmgpp.page.epg.mgpp.oep.ResolutionsPage;
import fr.dila.solonmgpp.page.epg.mgpp.procedureLegislative.ProcedureLegislativePage;

public class BandeauMenuMgpp  extends SolonEpgPage {

    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Procédure législative")
    private WebElement procedureLegislative;
/*    
    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Publication")
    private WebElement publication;
*/    
    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Rapport")
    private WebElement rapport;
    
    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Nomination")
    private WebElement nomination;
    
    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Déclaration")
    private WebElement declaration;
    
    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Organisation")
    private WebElement organisation;
    
    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Résolutions")
    private WebElement resolutions;
/*    
    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Calendriers")
    private WebElement calendriers;
*/
    /**
     * Va vers l'espace de procédure législative
     * @return
     */
    public ProcedureLegislativePage goToProcedureLegislative(){
        return click(procedureLegislative, ProcedureLegislativePage.class);
    }
    
    /**
    * Va vers l'espace de Eapport
    * @return
    */
   public RapportPage goToRapport(){
       return click(rapport, RapportPage.class);
   }
   
   /**
    * Va vers l'espace de Nomination
    * @return
    */
   public NominationPage goToNomination(){
       return click(nomination, NominationPage.class);
   }

   
   /**
    * Va vers l'espace résolutions
    * @return
    */
   public ResolutionsPage goToResolutions(){
       return click(resolutions, ResolutionsPage.class);
   }
   
   public DeclarationPage goToDeclaration(){
       return click(declaration, DeclarationPage.class);
   }
   
   /**
    * Va vers l'espace Organisation
    * @return
    */
   public OrganisationPage goToOrganisationPage(){
       return click(organisation, OrganisationPage.class);
   }

 
}
