package fr.dila.solonmgpp.creation.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonmgpp.page.commun.CommonWebPage;

public class BordereauOnglet extends CommonWebPage{

  private static final String TITRE_ACTE_ID = "bordereauForm:nxl_bordereau_donnees_principales:nxw_titre_acte_field";
  private static final String NOM_RESP_DOSSIER_ID = "bordereauForm:nxl_bordereau_responsable_acte:nxw_nom_resp_dossier_field";
  private static final String QUALITE_RESP_DOSSIER_ID = "bordereauForm:nxl_bordereau_responsable_acte:nxw_qualite_resp_dossier_field";
  private static final String TEL_RESP_DOSSIER_ID = "bordereauForm:nxl_bordereau_responsable_acte:nxw_tel_resp_dossier_field";
  private static final String RUBRIQUES_SELECT_ID = "bordereauForm:nxl_bordereau_indexation:nxw_indexation_rubriques_select_index_menu";
  
  
  public void initialize(final String titre_acte, final String nom_resp_dossier, final String qualite_resp_dossier, final String tel_resp_dossier, final String rubriques){
    setTitreActe(titre_acte);
    setNomRespDossier(nom_resp_dossier);
    setQualiteRespDossier(qualite_resp_dossier);
    setTelRespDossier(tel_resp_dossier);
    setRubrique(rubriques);
    save();
  }
  
  private void setTitreActe(final String titre_acte){
    getDriver().findElement(By.id(TITRE_ACTE_ID)).sendKeys(titre_acte);
  }
  
  private void setNomRespDossier(final String nom_resp_dossier){
    getDriver().findElement(By.id(NOM_RESP_DOSSIER_ID)).sendKeys(nom_resp_dossier);
  }
  
  private void setQualiteRespDossier(final String qualite_resp_dossier){
    getDriver().findElement(By.id(QUALITE_RESP_DOSSIER_ID)).sendKeys(qualite_resp_dossier);
  }
  
  private void setTelRespDossier(final String tel_resp_dossier){
    getDriver().findElement(By.id(TEL_RESP_DOSSIER_ID)).sendKeys(tel_resp_dossier);
  }
  
  private void setRubrique(final String rubriques){
    final WebElement rubriquesSelect = getDriver().findElement(By.id(RUBRIQUES_SELECT_ID));
    getFlog().action("Selectionne \"" + rubriques + "\" dans le select de \"Rubriques\"");
    final Select selectrubriques = new Select(rubriquesSelect);
    selectrubriques.selectByVisibleText(rubriques);
    
    getDriver().findElement(By.id("bordereauForm:nxl_bordereau_indexation:nxw_indexation_rubriques_addIndexImage")).click();//le sign "+"
  }
  
  private void save(){
    getDriver().findElement(By.xpath("//img[@title='Sauvegarder le bordereau']")).click();
    waitForPageLoaded(getDriver());
  }
  
}
