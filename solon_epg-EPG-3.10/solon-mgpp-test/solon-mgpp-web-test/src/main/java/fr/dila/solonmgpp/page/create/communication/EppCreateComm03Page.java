package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.DetailComm03Page;

public class EppCreateComm03Page extends AbstractEppCreateComm{
  public static final String TYPE_COMM = "LEX-03 : Pjl - Enregistrement du dépôt";
  
  public static final String NUM_DEPOT_TXT_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_numeroDepotTexte";
  public static final String DATE_DEPOT_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateDepotTexteInputDate";


  public void setNumeroDepotTexte(String numero) {
    final WebElement elem = getDriver().findElement(By.id(NUM_DEPOT_TXT_INPUT));
    fillField("Numero dépôt texte", elem, numero);
  }

  public void setDateDepotTexte(String date) {
    final WebElement elem = getDriver().findElement(By.id(DATE_DEPOT_INPUT));
    fillField("Date dépôt texte", elem, date);
  }

  public DetailComm03Page createComm03(String dateDepot, String numeroDepotTexte) {
    checkValue(COMMUNICATION, TYPE_COMM);

    setDateDepotTexte(dateDepot);
    checkValue(DATE_DEPOT_INPUT, dateDepot);
    
    setNumeroDepotTexte(numeroDepotTexte);
    checkValue(NUM_DEPOT_TXT_INPUT, numeroDepotTexte);

    return publier();
}
  
  @Override
  protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
      return DetailComm03Page.class;
  }
}
