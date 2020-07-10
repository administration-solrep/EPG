package fr.dila.solonepg.page.recherche;

import org.openqa.selenium.By;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class ViewUtilisateurDetailsPage extends EPGWebPage {
    
    public static final String MODIFIER_RECHECRCHE_BOUTTON_PATH = "//input[@value='Modifier la recherche']";
    
    public RechercheUtilisateurPage modifierRecherche() {
        getFlog().action("modifier recherche ");
        findElement(By.xpath(MODIFIER_RECHECRCHE_BOUTTON_PATH)).click();
        waitForPageSourcePart("Recherche d'utilisateur", TIMEOUT_IN_SECONDS);
        return (RechercheUtilisateurPage) getPage(RechercheUtilisateurPage.class);
    } 
    
}
