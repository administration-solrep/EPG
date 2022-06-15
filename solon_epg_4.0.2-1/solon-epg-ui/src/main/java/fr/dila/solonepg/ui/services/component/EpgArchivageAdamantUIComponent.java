package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgArchivageAdamantUIService;
import fr.dila.solonepg.ui.services.impl.EpgArchivageAdamantUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgArchivageAdamantUIComponent
    extends ServiceEncapsulateComponent<EpgArchivageAdamantUIService, EpgArchivageAdamantUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgArchivageAdamantUIComponent() {
        super(EpgArchivageAdamantUIService.class, new EpgArchivageAdamantUIServiceImpl());
    }
}
