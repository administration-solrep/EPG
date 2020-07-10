package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.EppCreateComm24Page;

public class DetailComm24Page extends AbstractEppDetailComm{

  @Override
  protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
    return EppCreateComm24Page.class;
  }

}
