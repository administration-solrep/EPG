package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.EppCreateCommAlerte14Page;

public class EppDetailCommAlerte14Page  extends AbstractEppDetailComm {

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return EppCreateCommAlerte14Page.class;
    }

}
