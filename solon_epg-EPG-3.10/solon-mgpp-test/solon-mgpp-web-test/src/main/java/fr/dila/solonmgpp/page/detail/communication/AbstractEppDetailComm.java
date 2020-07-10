package fr.dila.solonmgpp.page.detail.communication;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.sword.xsd.solon.epp.EvenementType;

public abstract class AbstractEppDetailComm extends AbstractDetailComm{
  
  /**
   * Utilise le select de la page de détail pour créer une communication successive
   * @param <T>
   * @param evenementType
   * @param pageClazz
   * @return null si la communication donnée ne peut pas être créée, la page de création sinon
   */
  public <T extends AbstractCreateComm> T navigateToCreateCommSucc(final EvenementType evenementType, final Class<T> pageClazz) {        
      getFlog().action("Selectionne \"" + evenementType.value() + "\" dans le select de création de communication successive");
      final WebElement evtTypeElem = getDriver().findElement(By.id("evenement_metadonnees:subviewCreateEvenementSucc:selectEvenementSuccessifTypeId"));
      final Select select = new Select(evtTypeElem);
      List<WebElement> options = select.getOptions();
      boolean hasEvenementType = false;
      // On parcourt les options pour vérifier que la création de l'évènement souhaité est possible
      for (WebElement option : options) {
          if (option.getAttribute("value").equals(evenementType.value())) {
              hasEvenementType = true;
              break;
          }
      }
      if (!hasEvenementType) {
          return null;
      }
      select.selectByValue(evenementType.value());

      final WebElement createBtn = getDriver().findElement(By.id("evenement_metadonnees:subviewCreateEvenementSucc:createEvenementSuccSubmitButton"));
      getFlog().actionClickButton("Créer communication successive");
      createBtn.click();
      return getPage(pageClazz);
  }
  
  
  
}
