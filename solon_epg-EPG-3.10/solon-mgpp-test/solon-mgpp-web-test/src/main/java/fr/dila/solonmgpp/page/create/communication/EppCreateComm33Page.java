package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailComm33Page;


public class EppCreateComm33Page extends AbstractEppCreateComm {

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailComm33Page.class;

    }
}