package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm17Page;

public class EppCreateComm17Page extends AbstractEppCreateComm {

    public static final String TYPE_COMM = "CENS-01 - Motion de censure article 49 alinéa 2";

    public MgppDetailComm17Page createComm17(String idDossier, String objet) {
        checkValue(COMMUNICATION, TYPE_COMM);
        setIdentifiantDossier(idDossier);
        setObjet(objet);
        return publier();
    }

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailComm17Page.class;
    }

}
