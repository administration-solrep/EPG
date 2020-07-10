package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailComm22Page;

public class EppCreateComm22Page extends AbstractEppCreateComm{

  private static final String ID_DOSSIER_COMM_PRECEDENTE = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_identifiant_precedent";
  private static final String DATE_DEPOT_TEXTE_INPUT_DATE_ID = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateDepotTexteInputDate";
  private static final String RESULTAT_CMP_SELECT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_resultatCMP";
  
  @Override
  protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
    return EppDetailComm22Page.class;
  }
  
  public EppDetailComm22Page createComm22Epp(final String dossier_id,final String dateDepotTexteInputDate,final String resultat_cmp_label){
    
    checkValue(ID_DOSSIER_COMM_PRECEDENTE, dossier_id);
    
    fillDateDepot(dateDepotTexteInputDate);
    fillResultatCmp(resultat_cmp_label);
    
    return publier();
  }
  
  private void fillDateDepot(final String dateDepot){
    fillField("Date de dépôt", getDriver().findElement(By.id(DATE_DEPOT_TEXTE_INPUT_DATE_ID)), dateDepot);
  }

  private void fillResultatCmp(final String resultat_cmp_label){
    
    final WebElement resultatCmpSelect = getDriver().findElement(By.id(RESULTAT_CMP_SELECT));
    getFlog().action("Selectionne \"" + resultat_cmp_label + "\" dans le select \"Résultat CMP\"");
    final Select selectResultatCmp = new Select(resultatCmpSelect);
    selectResultatCmp.selectByVisibleText(resultat_cmp_label);
    
  }
  
}
