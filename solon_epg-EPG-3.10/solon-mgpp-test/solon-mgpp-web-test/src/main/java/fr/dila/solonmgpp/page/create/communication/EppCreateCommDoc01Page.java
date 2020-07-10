package fr.dila.solonmgpp.page.create.communication;


import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailCommDoc01Page;

public class EppCreateCommDoc01Page extends AbstractEppCreateComm {

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailCommDoc01Page.class;
    }

}
