package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailCommGenerique14Page;

public class EppCreateCommGenerique14Page extends EppCreateCommAlerteGeneriquePage {

    public static final String TYPE_COMM = "Generique - Autres documents transmis aux assemblées";
    
    
    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailCommGenerique14Page.class;

    }
    
    public EppDetailCommGenerique14Page createCommGenerique14(final String destinataire ) {
        checkValue(COMMUNICATION, TYPE_COMM);
        selectInEPPOrganigramme(destinataire, DESTINATAIRE);
        return publier();
        //return getPage(EppDetailCommGenerique14Page.class);
    }
    
}
