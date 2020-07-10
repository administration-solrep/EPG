package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommGenerique11Page;

public class MgppDetailCommGenerique11Page extends AbstractMgppDetailComm{

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
      return MgppCreateCommGenerique11Page.class;
    }


}
