package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.EppCreateCommAUD02Page;

public class EppDetailCommAUD02Page extends AbstractEppDetailComm {

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return EppCreateCommAUD02Page.class;
    }

}
