package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailComm13BisPage;

public class EppCreateComm13BisPage extends AbstractEppCreateComm{

  private static final String NUMERO_DEPOT_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_numeroDepotTexte";
  private static final String DATE_DEPOT_TEXTE_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateDepotTexteInputDate";
  
  @Override
  protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
    return EppDetailComm13BisPage.class;
  }

  public EppDetailComm13BisPage createComm13Bis(final String numeroDepotTexte, final String dateDepotTextInputDate){
    
    setNumeroDepotTexte(numeroDepotTexte);
    
    setDateDepotTextInputDate(dateDepotTextInputDate);
    
    return publier();
  }
  
  private void setNumeroDepotTexte(String numeroDepotTexte){
    final WebElement elem = getDriver().findElement(By.id(NUMERO_DEPOT_INPUT));
    fillField("Numéro texte adopté", elem, numeroDepotTexte);
  }

  private void setDateDepotTextInputDate(String dateDepotTextInputDate){
    final WebElement elem = getDriver().findElement(By.id(DATE_DEPOT_TEXTE_INPUT));
    fillField("Numéro texte adopté", elem, dateDepotTextInputDate);
  }

}
