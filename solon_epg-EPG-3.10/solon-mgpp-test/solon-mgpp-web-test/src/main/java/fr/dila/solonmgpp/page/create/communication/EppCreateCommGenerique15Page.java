package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailCommGenerique15Page;

public class EppCreateCommGenerique15Page extends EppCreateCommAlerteGeneriquePage {

    public static final String TYPE_COMM = "Generique - Autres documents transmis aux assemblées";
    
    
    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailCommGenerique15Page.class;

    }
    
    public EppDetailCommGenerique15Page createCommGenerique15(final String destinataire ) {
        checkValue(COMMUNICATION, TYPE_COMM);
        selectInEPPOrganigramme(destinataire, DESTINATAIRE);
        return publier();
        //return getPage(EppDetailCommGenerique15Page.class);
    }
    
}