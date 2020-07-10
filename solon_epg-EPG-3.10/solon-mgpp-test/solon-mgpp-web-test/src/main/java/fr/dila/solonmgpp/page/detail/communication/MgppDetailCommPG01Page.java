package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateCommPG01Page;

public class MgppDetailCommPG01Page extends AbstractMgppDetailComm{

    
    public static final String OBJECT_ID = "meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_objet";
    public static final String DATE_VOTE_ID = "meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_dateVote";
    public static final String HORODATAGE_ID = "meta_evt:nxl_metadonnees_evenement:nxw_metadonnees_evenement_horodatage";
    
    
    
    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
      return MgppCreateCommPG01Page.class;
    }


}
