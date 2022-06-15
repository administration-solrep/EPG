package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.SolonEpgDossierListUIService;
import fr.dila.solonepg.ui.services.impl.SolonEpgDossierListUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class SolonEpgDossierListUIComponent
    extends ServiceEncapsulateComponent<SolonEpgDossierListUIService, SolonEpgDossierListUIServiceImpl> {

    /**
     * Default constructor
     */
    public SolonEpgDossierListUIComponent() {
        super(SolonEpgDossierListUIService.class, new SolonEpgDossierListUIServiceImpl());
    }
}
