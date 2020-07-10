package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailCommAUD01Page;

public class EppCreateCommAUD01Page extends AbstractEppCreateComm {

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailCommAUD01Page.class;
    }

}
