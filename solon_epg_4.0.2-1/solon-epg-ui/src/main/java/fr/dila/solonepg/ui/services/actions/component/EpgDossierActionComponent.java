package fr.dila.solonepg.ui.services.actions.component;

import fr.dila.solonepg.ui.services.actions.EpgDossierActionService;
import fr.dila.solonepg.ui.services.actions.impl.EpgDossierActionServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgDossierActionComponent
    extends ServiceEncapsulateComponent<EpgDossierActionService, EpgDossierActionServiceImpl> {

    /**
     * Default constructor
     */
    public EpgDossierActionComponent() {
        super(EpgDossierActionService.class, new EpgDossierActionServiceImpl());
    }
}
