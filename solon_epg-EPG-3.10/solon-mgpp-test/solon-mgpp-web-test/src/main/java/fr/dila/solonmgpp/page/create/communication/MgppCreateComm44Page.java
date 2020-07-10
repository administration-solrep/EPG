package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm44Page;
import fr.sword.xsd.solon.epp.PieceJointeType;

public class MgppCreateComm44Page extends AbstractMgppCreateComm{

  private static final String IDENTIFIANT_DOSSIER_ID = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_identifiant_dossier";
  private static final String RAPPORT_PARLEMENT_SELECT = "//select[@id='evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_rapportParlement']";
  private static final String DATE_INPUT_DATE_ID = "evenement_metadonnees:nxl_metadonnees_evenement:nxw_metadonnees_evenement_dateInputDate";
  private static final String LETTRE_PM_TITRE = "PJLPMPour" + getDossierId();
  private static final String LETTRE_PM_URL = "url@gmail.fr";
  private static final String[] LETTRE_PM_PJ = new String[]{"//*[@id='PAReditFileForm:parapheurTree:0:0::parapheurFichier:text']/table/tbody/tr/td/div/span/input"}; // //*[@id='PAReditFileForm:parapheurTree:1:::parapheurFolder:text']/table/tbody/tr/td/div/span/input
  private static final String RAPPORT_TITRE = "PJTPour" + getDossierId();
  private static final String RAPPORT_URL = "http://" + getDossierId() + "@solon-epg.fr";
  private static final String RAPPORT_PJ = "src/main/attachments/doc2007.doc";
  
  @Override
  protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
    return MgppDetailComm44Page.class;
  }

  
  public MgppDetailComm44Page createComm44(final String dossierId, final String objet, final String rapport_parlement, final String dateInputDate, final String destinataire){
    
    setDestinataire(destinataire);
    setDossierId(dossierId);
    setObjet(objet);
    setRapportParlement(rapport_parlement);
    setDateInputDate(dateInputDate);
    
    addPieceJointeDepuis(PieceJointeType.LETTRE_PM, ADD_PJFILE_DEPUIS_PARAPHEUR, LETTRE_PM_TITRE, LETTRE_PM_URL, LETTRE_PM_PJ);
    addPieceJointe(PieceJointeType.RAPPORT, RAPPORT_TITRE, RAPPORT_URL, RAPPORT_PJ);
    
    return publier();
  }
  
  
  public void setDossierId(final String dossierId){
      final WebElement elem = getDriver().findElement(By.id(IDENTIFIANT_DOSSIER_ID));
      fillField("Identifiant Dossier", elem, dossierId);
  }
  

  public void setRapportParlement(final String rapport_parlement) {
      final WebElement elem = getDriver().findElement(By.xpath(RAPPORT_PARLEMENT_SELECT));
      getFlog().action("Selectionne \"" + rapport_parlement + "\" dans le select \"Rapport Parlement\"");
      final Select select = new Select(elem);
      select.selectByVisibleText(rapport_parlement);
  }
  
  public void setDateInputDate(final String dateInputDate){
      final WebElement elem = getDriver().findElement(By.id(DATE_INPUT_DATE_ID));
      fillField("Date", elem, dateInputDate);
    }
  

}
