package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.DossiersSimilairesUIService;
import fr.dila.solonepg.ui.services.impl.DossiersSimilairesUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class DossiersSimilairesUIComponent
    extends ServiceEncapsulateComponent<DossiersSimilairesUIService, DossiersSimilairesUIServiceImpl> {

    public DossiersSimilairesUIComponent() {
        super(DossiersSimilairesUIService.class, new DossiersSimilairesUIServiceImpl());
    }
}
