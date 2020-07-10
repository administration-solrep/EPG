package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailComm12Page;

public class EppCreateComm12Page extends AbstractEppCreateComm{

  private static final String NUMERO_TEXT_ADOPTE_FIELD = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_numeroTexteAdopte"; 
  private static final String DATE_ADOPTION_INPUT_DATE_FIELD = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateAdoptionInputDate"; 
  private static final String SORT_ADOPTION_SELECT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_sortAdoption"; 
  private static final String IDENTIFIANT_PRECEDENT = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_identifiant_precedent";
  
  @Override
  protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
    return EppDetailComm12Page.class;
  }

  public EppDetailComm12Page createComm12(final String numeroTextAdopte, final String dateAdoptionInputDate, final String sortAdoption_label){
      setNumeroTextAdopte(numeroTextAdopte);
      setDateAdoptionInputDate(dateAdoptionInputDate);
      setSortAdoption_label(sortAdoption_label);

      return publier();
  }
  
  public void setNumeroTextAdopte(String numeroTextAdopte){
      final WebElement elem = getDriver().findElement(By.id(NUMERO_TEXT_ADOPTE_FIELD));
      fillField("Numero dépôt texte", elem, numeroTextAdopte);
  }
  
  public void setDateAdoptionInputDate(String dateAdoptionInputDate){
    final WebElement elem = getDriver().findElement(By.id(DATE_ADOPTION_INPUT_DATE_FIELD));
    fillField("date adoption", elem, dateAdoptionInputDate);
}
  
  public void setSortAdoption_label(String sortAdoption_label){
    final WebElement sortAdoptionElem = getDriver().findElement(By.id(SORT_ADOPTION_SELECT));
    getFlog().action("Selectionne \"" + sortAdoption_label + "\" dans le select \"sort adoption\"");
    final Select select = new Select(sortAdoptionElem);
    select.selectByVisibleText(sortAdoption_label);
}
  
  public void checkIdentifiantPrecedent(final String dossier_precedent_id){
      checkValue(IDENTIFIANT_PRECEDENT, dossier_precedent_id);
  }
  
}
