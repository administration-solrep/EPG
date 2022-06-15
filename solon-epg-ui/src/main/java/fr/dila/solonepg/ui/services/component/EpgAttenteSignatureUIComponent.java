package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgAttenteSignatureUIService;
import fr.dila.solonepg.ui.services.impl.EpgAttenteSignatureUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgAttenteSignatureUIComponent
    extends ServiceEncapsulateComponent<EpgAttenteSignatureUIService, EpgAttenteSignatureUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgAttenteSignatureUIComponent() {
        super(EpgAttenteSignatureUIService.class, new EpgAttenteSignatureUIServiceImpl());
    }
}
