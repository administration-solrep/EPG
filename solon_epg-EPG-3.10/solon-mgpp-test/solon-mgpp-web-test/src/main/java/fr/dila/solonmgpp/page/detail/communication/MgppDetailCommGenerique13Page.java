package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommGenerique12Page;

public class MgppDetailCommGenerique13Page extends AbstractMgppDetailComm {

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return MgppCreateCommGenerique12Page.class;
    }

}
