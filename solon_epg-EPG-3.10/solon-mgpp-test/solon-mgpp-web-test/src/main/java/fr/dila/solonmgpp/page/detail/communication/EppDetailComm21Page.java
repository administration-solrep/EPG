package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.EppCreateComm21Page;

public class EppDetailComm21Page extends AbstractEppDetailComm{

  @Override
  protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
    return EppCreateComm21Page.class;
  }

}
