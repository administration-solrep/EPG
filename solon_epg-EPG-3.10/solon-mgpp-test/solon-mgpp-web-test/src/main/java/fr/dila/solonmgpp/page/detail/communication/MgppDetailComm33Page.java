package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm33Page;

public class MgppDetailComm33Page extends AbstractMgppDetailComm {

    public static final String OBJET = "meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_objet";
   
    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
       
        return MgppCreateComm33Page.class;
    }

    

}
