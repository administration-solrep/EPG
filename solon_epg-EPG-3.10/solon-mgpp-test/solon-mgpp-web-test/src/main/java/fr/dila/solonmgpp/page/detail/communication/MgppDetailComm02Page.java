package fr.dila.solonmgpp.page.detail.communication;

import fr.dila.solonmgpp.page.create.communication.AbstractCreateComm;

public class MgppDetailComm02Page extends AbstractDetailComm {

    public static final String NATURE_LOI = "nxw_metadonnees_version_natureLoi_row";
    public static final String TYPE_LOI = "nxw_metadonnees_version_typeLoi_row";

    @Override
    protected Class<? extends AbstractCreateComm> getModifierResultPageClass() {
        return null;
    }

}
