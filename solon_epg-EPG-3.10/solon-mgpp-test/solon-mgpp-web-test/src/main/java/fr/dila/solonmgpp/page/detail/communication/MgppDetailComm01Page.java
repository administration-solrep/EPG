package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;
import fr.dila.solonmgpp.page.create.communication.MgppCreateComm01Page;

public class MgppDetailComm01Page extends AbstractMgppDetailComm {

    public static final String NATURE_LOI = "nxw_metadonnees_version_natureLoi_row";
    public static final String TYPE_LOI = "nxw_metadonnees_version_typeLoi_row";
    
    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return MgppCreateComm01Page.class;
    }

}
