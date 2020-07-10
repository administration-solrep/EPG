package fr.dila.solonmgpp.page.create.communication;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailCommAlerte12Page;

public class EppCreateCommAlerte12Page extends EppCreateCommAlerteGeneriquePage {

    public static final String TYPE_COMM = "Alerte - Déclaration sur un sujet déterminé";
    

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailCommAlerte12Page.class;

    }

    public EppDetailCommAlerte12Page createCommAlerte12Page(String destinataire ) {
        checkValue(COMMUNICATION, TYPE_COMM);
        selectInEPPOrganigramme(destinataire, DESTINATAIRE);
        return publier();
    }
    
    

}