package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractMgppDetailComm;

public class MgppDetailCommGenerique14Page extends AbstractMgppDetailComm {

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return MgppCreateCommGenerique14Page.class;
    }

}
