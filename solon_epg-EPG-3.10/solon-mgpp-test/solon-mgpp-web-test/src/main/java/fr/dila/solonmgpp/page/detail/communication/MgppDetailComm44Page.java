package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm44Page;

public class MgppDetailComm44Page extends AbstractMgppDetailComm{

  @Override
  protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
    return MgppCreateComm44Page.class;
  }

  public void checkPresenceOfElements(){
      isDiffuserPresent();
      isEnregistrerPresent();
      isGenererCourrierPresent();
      isCreerAlertePresent();
  }
  
  public void isDiffuserPresent(){
      assertElementPresent(By_enum.XPATH, "//*[@value='Diffuser']");
  }
  public void isEnregistrerPresent(){
      assertElementPresent(By_enum.XPATH, "//*[@value='Enregistrer']");
  }
  public void isGenererCourrierPresent(){
      assertElementPresent(By_enum.XPATH, "//*[@value='Générer courrier']");
  }
  public void isCreerAlertePresent(){
      assertElementPresent(By_enum.XPATH, "//*[@value='Créer alerte']");
  }
  
}
