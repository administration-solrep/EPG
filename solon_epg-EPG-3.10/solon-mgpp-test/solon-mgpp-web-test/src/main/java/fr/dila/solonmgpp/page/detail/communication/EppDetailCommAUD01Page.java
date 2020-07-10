package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.EppCreateCommAUD01Page;

public class EppDetailCommAUD01Page extends AbstractEppDetailComm {

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return EppCreateCommAUD01Page.class;
    }

}
