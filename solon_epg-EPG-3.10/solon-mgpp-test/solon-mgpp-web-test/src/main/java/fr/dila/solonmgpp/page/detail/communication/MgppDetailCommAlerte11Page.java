package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommAlerte11Page;

public class MgppDetailCommAlerte11Page extends AbstractMgppDetailComm{

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
      return MgppCreateCommAlerte11Page.class;
    }


}
