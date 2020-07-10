package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;

public class MgppCreateCommAUD02Page extends AbstractMgppCreateComm {


    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailCommAUD02Page.class;
    }
}