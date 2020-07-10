package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommFusion12Page;

public class MgppDetailCommFusion12Page extends AbstractMgppDetailComm {

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return MgppCreateCommFusion12Page.class;
    }

}
