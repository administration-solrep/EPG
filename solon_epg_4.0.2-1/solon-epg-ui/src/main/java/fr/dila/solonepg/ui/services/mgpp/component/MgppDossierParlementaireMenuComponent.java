package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppDossierParlementaireMenuService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppDossierParlementaireMenuServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppDossierParlementaireMenuComponent
    extends ServiceEncapsulateComponent<MgppDossierParlementaireMenuService, MgppDossierParlementaireMenuServiceImpl> {

    /**
     * Default constructor
     */
    public MgppDossierParlementaireMenuComponent() {
        super(MgppDossierParlementaireMenuService.class, new MgppDossierParlementaireMenuServiceImpl());
    }
}
