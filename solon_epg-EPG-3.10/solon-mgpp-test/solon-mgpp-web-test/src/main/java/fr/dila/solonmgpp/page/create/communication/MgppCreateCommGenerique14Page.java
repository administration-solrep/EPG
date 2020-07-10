package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;

public class MgppCreateCommGenerique14Page extends AbstractMgppCreateComm {

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailCommGenerique14Page.class;
    }

    public MgppDetailCommGenerique14Page createCommGenerique14(final String destinataire) {
        setDestinataire(destinataire);
        sleep(3);
        return publier();
    }

}
