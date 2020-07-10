package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm23Page;

public class MgppDetailComm23Page extends AbstractMgppDetailComm{

  @Override
  protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
    return MgppCreateComm23Page.class;
  }

}
