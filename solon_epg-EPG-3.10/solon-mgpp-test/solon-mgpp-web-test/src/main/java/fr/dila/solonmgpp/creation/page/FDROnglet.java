package fr.dila.solonmgpp.creation.page;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import fr.dila.solonmgpp.page.commun.CommonWebPage;

public class FDROnglet extends CommonWebPage{

  private static final String ACTIONS_PATH = "//*[@id='dm_route_elements:nxl_feuille_route_instance_listing:nxw_dr_listing_step_actions:titleref']/img[@alt='Actions']";
  private static final String AJOUTER_APRES_ID = "//div[@id= 'jqContextMenu']/ul/li/a[@id='dm_route_elements:nxl_feuille_route_instance_listing:nxw_dr_listing_step_actions:nxw_dr_listing_step_actions_add:addStepInForActionList:0:step_0']";
  private static final String AJOUTER_DES_ETAPES_PANEL_PATH = "//*[@id='selectRouteElementsTypeForCreationForm:selectRouteElementsTypePanelHeader']";
  private static final String AJOUTER_ETAPE_ID = "selectRouteElementsTypeForCreationForm:selectRouteElementsTypeForCreationTable:0:selectRouteElementTypeForCreationCategory:0:selectRouteElementsTypeForCreationCategoryTable:0:createRouteElementLink";
  private static final String TYPE_ETAPE_SELECT_ID = "document_create:nxl_routing_task_detail:routing_task_type";
  private static final String ACTIONS_PATH_2 = "//*[@id='dm_route_elements:nxl_feuille_route_instance_listing_1:nxw_dr_listing_step_actions_1:titleref']/img[@alt='Actions']";
  private static final String AJOUTER_APRES_2_ID = "//div[@id= 'jqContextMenu']/ul/li/a[@id='dm_route_elements:nxl_feuille_route_instance_listing_1:nxw_dr_listing_step_actions_1:nxw_dr_listing_step_actions_1_add:addStepInForActionList:1:step_0']";
                         
  private static final String ETAPE1_LABEL_TO_SET = "Pour attribution au secteur parlementaire";
  private static final String ETAPE2_LABEL_TO_SET = "Pour publication à la DILA JO";
  private static final String DESTINATAIRE_ORGANIGRAMME = "document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_findButton";
  
  public void initialize(){
    ajouterEtape1();
    ajouterEtape2();
  }
  
  private void ajouterEtape1(){
    getDriver().findElement(By.xpath(ACTIONS_PATH)).click();
    
    //waitForPageSourcePartDisplayed(By.id("jqContextMenu"), TIMEOUT_IN_SECONDS);
    
    //new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.id(AJOUTER_APRES_ID)));
    getDriver().findElement(By.xpath(AJOUTER_APRES_ID)).click();
    
    //<td>waitForVisible</td>     <td>//*[@id='selectRouteElementsTypeForCreationForm:selectRouteElementsTypePanelHeader']</td>
    new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(AJOUTER_DES_ETAPES_PANEL_PATH)));
    
    //<td>clickAndWait</td> <td>id=selectRouteElementsTypeForCreationForm:selectRouteElementsTypeForCreationTable:0:selectRouteElementTypeForCreationCategory:0:selectRouteElementsTypeForCreationCategoryTable:0:createRouteElementLink</td>
    getDriver().findElement(By.id(AJOUTER_ETAPE_ID)).click();//ajouter une etape
    
    final WebElement typeDetapeSelect = getDriver().findElement(By.id(TYPE_ETAPE_SELECT_ID));
    getFlog().action("Selectionne \"" + ETAPE1_LABEL_TO_SET + "\" dans le select \"Type d'etape\"");
    final Select selectTypeDetape = new Select(typeDetapeSelect);
    selectTypeDetape.selectByVisibleText(ETAPE1_LABEL_TO_SET);
    
    
    ArrayList<String> ar = new ArrayList<String>();
    ar.add("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3::min:handle:img:collapsed");
    ar.add("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3:node:0::dir:handle:img:collapsed");
    ar.add("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3:node:0:node:1::nxw_routing_task_distribution_mailbox_addPst");
    
    selectDestinataireFromOrganigramme(ar, DESTINATAIRE_ORGANIGRAMME);
    sleep(2);
    save();
    
  }
  
  private void ajouterEtape2(){
    //<!-- Ajout etape pour Publication DILA -->
    
    getDriver().findElement(By.xpath(ACTIONS_PATH_2)).click();
    
    //new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.id(AJOUTER_APRES_2_ID)));
    getDriver().findElement(By.xpath(AJOUTER_APRES_2_ID)).click();
    
    //<td>waitForVisible</td>     <td>//*[@id='selectRouteElementsTypeForCreationForm:selectRouteElementsTypePanelHeader']</td>
    new WebDriverWait(getDriver(), TIMEOUT_IN_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(AJOUTER_DES_ETAPES_PANEL_PATH)));
    
    //<td>clickAndWait</td> <td>id=selectRouteElementsTypeForCreationForm:selectRouteElementsTypeForCreationTable:0:selectRouteElementTypeForCreationCategory:0:selectRouteElementsTypeForCreationCategoryTable:0:createRouteElementLink</td>
    getDriver().findElement(By.id(AJOUTER_ETAPE_ID)).click();
    
    final WebElement typeDetapeSelect2 = getDriver().findElement(By.id(TYPE_ETAPE_SELECT_ID));
    getFlog().action("Selectionne \"" + ETAPE2_LABEL_TO_SET + "\" dans le select \"Type d'etape\"");
    final Select selectTypeDetape2 = new Select(typeDetapeSelect2);
    selectTypeDetape2.selectByVisibleText(ETAPE2_LABEL_TO_SET);
    
    ArrayList<String> ar = new ArrayList<String>();
    ar.add("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3::min:handle:img:collapsed");
    ar.add("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3:node:0::dir:handle:img:collapsed");
    ar.add("document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_tree:node:0:node:3:node:0:node:1::nxw_routing_task_distribution_mailbox_addPst");
    selectDestinataireFromOrganigramme(ar, DESTINATAIRE_ORGANIGRAMME);
    sleep(2);
    save();
    
  }
  
  
  private void save(){
    //<td>clickAndWait</td>     <td>id=document_create:button_create</td>
    getDriver().findElement(By.id("document_create:button_create")).click();
  }
  
}
