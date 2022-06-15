package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgSuiviLibreFooterService;
import fr.dila.solonepg.ui.services.impl.EpgSuiviLibreFooterServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgSuiviLibreFooterServiceComponent
    extends ServiceEncapsulateComponent<EpgSuiviLibreFooterService, EpgSuiviLibreFooterServiceImpl> {

    /**
     * Default constructor
     */
    public EpgSuiviLibreFooterServiceComponent() {
        super(EpgSuiviLibreFooterService.class, new EpgSuiviLibreFooterServiceImpl());
    }
}
