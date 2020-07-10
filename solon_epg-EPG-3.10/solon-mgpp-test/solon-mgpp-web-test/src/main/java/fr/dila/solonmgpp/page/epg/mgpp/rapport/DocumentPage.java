package fr.dila.solonmgpp.page.epg.mgpp.rapport;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonmgpp.page.commun.CommonWebPage;
import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.sword.xsd.solon.epp.EvenementType;

public class DocumentPage  extends CommonWebPage{

    private static final String EVENEMENT_TYPE_SELECT = "create_evt_createur_form:selectEvenementTypeId";
    private static final String BOUTON_CREER_ID = "create_evt_createur_form:btn_create_evt";
    
    public <T extends AbstractCreateComm> T navigateToCreateComm(final EvenementType evenementType, final Class<T> pageClazz) {
      final WebElement evtTypeElem = getDriver().findElement(By.id(EVENEMENT_TYPE_SELECT));
      getFlog().action("Selectionne \"" + evenementType.value() + "\" dans le select \"Créer communication créatrice\"");
      final Select select = new Select(evtTypeElem);
      select.selectByValue(evenementType.value());

      final WebElement createBtn = getDriver().findElement(By.id(BOUTON_CREER_ID));
      getFlog().actionClickButton("Créer communication créatrice");
      createBtn.click();
      return getPage(pageClazz);
    }
    
    public void doRapidSearch(String corbeilleName, String valueOptionSelected) {
        openCorbeille(corbeilleName);

        openFiltre();

        getFlog().action("Ajout du type de communication " + valueOptionSelected + " dans les critères de filtre");
        WebElement typeComm = getDriver().findElement(By.id("corbeille_message_list_form_filtre:nxl_corbeille_message_listing_layout:typeEvenementId"));
        Select typesSelect = new Select(typeComm);
        typesSelect.selectByValue(valueOptionSelected);

        doRechercheRapide();
        
      }
    
  }
