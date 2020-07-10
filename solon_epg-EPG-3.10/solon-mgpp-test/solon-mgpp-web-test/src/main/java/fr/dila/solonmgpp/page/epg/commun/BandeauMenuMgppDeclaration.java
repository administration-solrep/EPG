package fr.dila.solonmgpp.page.epg.commun;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonmgpp.page.epg.mgpp.ie.InterventionExterieurePage;
import fr.dila.solonmgpp.page.epg.mgpp.pg.Politique‌Generale;
import fr.dila.solonmgpp.page.epg.mgpp.sd.SujetDetermine;

public class BandeauMenuMgppDeclaration  extends SolonEpgPage {

    
    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Intervention extérieure")
    private WebElement interventionExt;
    
    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Politique générale")
    private WebElement politique‌Generale;
    
    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Sujet déterminé (50-1C)")
    private WebElement sujetDetermine;

    
    /**
     * Va vers l'espace interventions exterieures
     * @return
     */
    public InterventionExterieurePage goToIE(){
        return click(interventionExt, InterventionExterieurePage.class);
    }
    
    /**
     * Va vers l'espace interventions exterieures
     * @return
     */
    public Politique‌Generale goToPolitique‌Generale(){
        return click(politique‌Generale, Politique‌Generale.class);
    }
    
    /**
     * Va vers l'espace interventions exterieures
     * @return
     */
    public SujetDetermine goToSujetDetermine(){
        return click(sujetDetermine, SujetDetermine.class);
    }
}
