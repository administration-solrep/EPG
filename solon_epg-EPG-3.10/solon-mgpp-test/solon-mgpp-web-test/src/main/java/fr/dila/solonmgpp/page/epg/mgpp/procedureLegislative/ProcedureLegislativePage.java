package fr.dila.solonmgpp.page.epg.mgpp.procedureLegislative;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonmgpp.page.commun.CommonWebPage;
import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;

public class ProcedureLegislativePage extends CommonWebPage {

  
    public <T extends AbstractCreateComm> T navigateToCreateComm(/*final EvenementType evenementType, */final Class<T> pageClazz) {
      getFlog().actionClickButton("Modifier une communication");
      return clickToPage(findElement(By.xpath("//*[@value='Modifier']")), pageClazz);
    }

    /**
     * Clique sur un lien nor d'une corbeille pour ouvrir les détails d'une communication
     * @param <T>
     * @param numeroNor le numero nor link à cliquer
     * @param pageClazz la page de détails qu'on souhaite obtenir
     * @return la page de détail de la communication
     */
    public <T extends AbstractDetailComm> T navigateToDetailCommByLinkNor(String numeroNor, final Class<T> pageClazz) {
        getFlog().actionClickLink("dossier nor " + numeroNor);
        By norLink = By.linkText(numeroNor);
        getDriver().findElement(norLink).click();
        
        return getPage(pageClazz);
    }
    
    public void doRapidSearch(String corbeilleName, String valueOptionSelected, final String NUM_NOR) {
    openCorbeille(corbeilleName);

    getFlog().check("Vérification de la présence du NOR " + NUM_NOR);
    Assert.assertTrue(this.hasElement(By.xpath("//*[contains(text(), '" + NUM_NOR + "')]")));

    openFiltre();

    getFlog().action("Ajout du type de communication " + valueOptionSelected + " dans les critères de filtre");
    WebElement typeComm = getDriver().findElement(By.id("corbeille_message_list_form_filtre:nxl_corbeille_message_listing_layout:typeEvenementId"));
    Select typesSelect = new Select(typeComm);
    typesSelect.selectByValue(valueOptionSelected);

    getDriver().findElement(By.xpath("//td[contains(text(), 'Identifiant dossier')]/../td[2]/input")).sendKeys(NUM_NOR);

    doRechercheRapide();
    
  }
    
    public void doRapidSearchAndClickElement(String corbeilleName, String valueOptionSelected, final String NUM_NOR) {
      doRapidSearch(corbeilleName, valueOptionSelected, NUM_NOR);
      
      getDriver().findElement(By.xpath("//a/span[contains(text(),'"+NUM_NOR+"')]")).click();
    }
    
    public void assertCorbeilles(final int procLegis, final int recu, final int emis, final int historique ){
        assertTrue("Procédure législative (" + procLegis + ")");
        assertTrue("Reçu (" + recu + ")");
        assertTrue("Emis (" + emis + ")");
        assertTrue("Historique (" + historique + ")");
    }
    
}
