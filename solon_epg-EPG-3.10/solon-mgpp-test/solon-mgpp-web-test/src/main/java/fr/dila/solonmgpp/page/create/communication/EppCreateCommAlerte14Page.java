package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailCommAlerte14Page;

public class EppCreateCommAlerte14Page extends EppCreateCommAlerteGeneriquePage {

    public static final String TYPE_COMM = " Alerte - Demandes d'audition";

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailCommAlerte14Page.class;

    }

    public EppDetailCommAlerte14Page createCommAlerte14Page(String destinataire) {
        checkValue(COMMUNICATION, TYPE_COMM);
        selectInEPPOrganigramme(destinataire, DESTINATAIRE);
        return publier();
    }

}