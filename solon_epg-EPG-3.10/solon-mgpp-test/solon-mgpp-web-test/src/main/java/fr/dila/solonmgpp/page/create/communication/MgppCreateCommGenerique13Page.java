package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommGenerique13Page;

public class MgppCreateCommGenerique13Page extends AbstractMgppCreateComm {

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailCommGenerique13Page.class;
    }

    public MgppDetailCommGenerique13Page createCommGenerique13(final String objet, final String destinataire) {

        setObjet(objet);
        setDestinataire(destinataire);
        return publier();
    }

}
