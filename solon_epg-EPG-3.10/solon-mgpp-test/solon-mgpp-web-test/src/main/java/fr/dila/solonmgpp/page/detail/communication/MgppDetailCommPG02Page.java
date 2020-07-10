package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommPG02Page;

public class MgppDetailCommPG02Page extends AbstractMgppDetailComm{

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
      return MgppCreateCommPG02Page.class;
    }


}
