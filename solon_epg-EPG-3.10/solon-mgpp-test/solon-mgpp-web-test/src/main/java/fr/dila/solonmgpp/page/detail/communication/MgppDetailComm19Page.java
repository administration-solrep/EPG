package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm19Page;

public class MgppDetailComm19Page extends AbstractMgppDetailComm{

  @Override
  protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
    return MgppCreateComm19Page.class;
  }

}
