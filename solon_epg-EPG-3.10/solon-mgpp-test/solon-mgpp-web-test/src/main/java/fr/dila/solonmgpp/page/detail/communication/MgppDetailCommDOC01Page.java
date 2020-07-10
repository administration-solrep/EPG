package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommDOC01Page;

public class MgppDetailCommDOC01Page  extends AbstractMgppDetailComm{

    
    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
      return MgppCreateCommDOC01Page.class;
    }


}
