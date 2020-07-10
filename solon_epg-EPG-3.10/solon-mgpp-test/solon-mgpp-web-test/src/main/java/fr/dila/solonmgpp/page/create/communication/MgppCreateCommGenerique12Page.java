package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailCommGenerique12Page;

public class MgppCreateCommGenerique12Page extends AbstractMgppCreateComm {

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailCommGenerique12Page.class;
    }

    public MgppDetailCommGenerique12Page createCommGenerique12(final String objet, final String destinataire) {

        setObjet(objet);
        setDestinataire(destinataire);
        return publier();
    }

}
