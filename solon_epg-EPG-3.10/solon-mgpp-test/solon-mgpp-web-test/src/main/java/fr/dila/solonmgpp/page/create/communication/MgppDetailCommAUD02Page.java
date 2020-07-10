package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractMgppDetailComm;

public class MgppDetailCommAUD02Page  extends AbstractMgppDetailComm {

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return MgppCreateCommAUD02Page.class;
    }

}
