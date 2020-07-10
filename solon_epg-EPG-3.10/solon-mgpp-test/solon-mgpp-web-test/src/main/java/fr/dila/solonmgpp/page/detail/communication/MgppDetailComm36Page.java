package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.EppCreateComm34Page;

public class MgppDetailComm36Page extends AbstractMgppDetailComm {

    public static final String OBJET = "meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_objet";
    public static final String AUTEUR = "nxw_metadonnees_evenement_auteur";

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
       
        return EppCreateComm34Page.class;
    }

    

}
