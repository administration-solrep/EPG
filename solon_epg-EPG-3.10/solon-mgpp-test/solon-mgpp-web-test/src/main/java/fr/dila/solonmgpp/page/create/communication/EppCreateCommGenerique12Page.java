package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailCommGenerique12Page;

public class EppCreateCommGenerique12Page extends AbstractEppCreateComm {

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailCommGenerique12Page.class;

    }
}