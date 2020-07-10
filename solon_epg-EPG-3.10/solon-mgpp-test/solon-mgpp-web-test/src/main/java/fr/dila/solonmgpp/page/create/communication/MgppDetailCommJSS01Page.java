package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractMgppDetailComm;

public class MgppDetailCommJSS01Page extends AbstractMgppDetailComm{

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
      return MgppCreateCommPG01Page.class;
    }


}
