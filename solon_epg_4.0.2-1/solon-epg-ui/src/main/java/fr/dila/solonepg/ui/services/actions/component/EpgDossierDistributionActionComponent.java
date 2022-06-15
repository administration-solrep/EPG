package fr.dila.solonepg.ui.services.actions.component;

import fr.dila.solonepg.ui.services.actions.EpgDossierDistributionActionService;
import fr.dila.solonepg.ui.services.actions.impl.EpgDossierDistributionActionServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgDossierDistributionActionComponent
    extends ServiceEncapsulateComponent<EpgDossierDistributionActionService, EpgDossierDistributionActionServiceImpl> {

    /**
     * Default constructor
     */
    public EpgDossierDistributionActionComponent() {
        super(EpgDossierDistributionActionService.class, new EpgDossierDistributionActionServiceImpl());
    }
}
