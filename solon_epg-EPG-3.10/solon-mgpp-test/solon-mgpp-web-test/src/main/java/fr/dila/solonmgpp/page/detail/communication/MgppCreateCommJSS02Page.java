package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;

public class MgppCreateCommJSS02Page extends AbstractCreateComm {

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailCommJSS02Page.class;
    }

}
