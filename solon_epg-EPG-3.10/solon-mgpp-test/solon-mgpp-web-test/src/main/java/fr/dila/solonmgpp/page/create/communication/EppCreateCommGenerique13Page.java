package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailCommGenerique13Page;

public class EppCreateCommGenerique13Page extends EppCreateCommAlerteGeneriquePage {

    public static final String TYPE_COMM = "Generique - Demande de jours supplémentaires de séance";
    
    
    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailCommGenerique13Page.class;

    }
    
    public EppDetailCommGenerique13Page createCommGenerique13(final String destinataire ) {
        checkValue(COMMUNICATION, TYPE_COMM);
        selectInEPPOrganigramme(destinataire, DESTINATAIRE);
        return publier();
        //return getPage(EppDetailCommGenerique13Page.class);
    }
    
}