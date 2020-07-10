package fr.dila.solonmgpp.page.create.communication;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailComm21Page;

public class EppCreateComm21Page extends AbstractEppCreateComm{

  private static final String INPUT_DATE_ID = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateList_inputInputDate";
  private static final String PLUS_ICONE_ID = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateList_addText";
  private static final String DELETE_ICONE_PATH = "//*[@id='evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateList_delete%s']";
  private int inputDateCounter = 0;
  
  private static final String EMETTEUR_GLOBAL_REGION_ID = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_emetteur_global_region";
  private static final String DESTINATAIRE_GLOBAL_REGION_ID = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_destinataire_global_region";
  private static final String COPIE_GLOBAL_REGION_PATH = "//*[@id='evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_copie_global_region']/table/tbody/tr/td";
  
  @Override
  protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
    return EppDetailComm21Page.class;
  }

  public EppDetailComm21Page createComm21(final String inputInputDate1, final String inputInputDate2, final String emetteur_global_region_value, final String destinataire_global_region_value, final String copie_global_region_value){
    inputDateCounter = 0;
    
    setInputDate(inputInputDate1);
    
    if(StringUtils.isNotBlank(inputInputDate2))
      setInputDate(inputInputDate2);
    
    if(StringUtils.isNotBlank(emetteur_global_region_value))
      checkValue(EMETTEUR_GLOBAL_REGION_ID, emetteur_global_region_value);
    
    if(StringUtils.isNotBlank(destinataire_global_region_value))
      checkValue(DESTINATAIRE_GLOBAL_REGION_ID, destinataire_global_region_value);
    
    if(StringUtils.isNotBlank(copie_global_region_value))
      checkValue(By_enum.XPATH, COPIE_GLOBAL_REGION_PATH, copie_global_region_value, Boolean.FALSE);
    
    
    return publier();
  }
  
  private void setInputDate(final String inputInputDateValue){
    getDriver().findElement(By.id(INPUT_DATE_ID)).sendKeys(inputInputDateValue);
    
    getDriver().findElement(By.id(PLUS_ICONE_ID)).click();
    
    new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(By.xpath( String.format(DELETE_ICONE_PATH, inputDateCounter) )));
    inputDateCounter++;
    
  }
  
}
