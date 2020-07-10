package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;

public class MgppDetailCommJSS02Page extends AbstractMgppDetailComm {

    public static final String OBJECT_ID = "meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_objet";
    
    
    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return MgppCreateCommJSS02Page.class;
    }

}
