package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.DossierCreationComponentService;
import fr.dila.solonepg.ui.services.impl.DossierCreationComponentServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class DossierCreationComponentComponent
    extends ServiceEncapsulateComponent<DossierCreationComponentService, DossierCreationComponentServiceImpl> {

    /**
     * Default constructor
     */
    public DossierCreationComponentComponent() {
        super(DossierCreationComponentService.class, new DossierCreationComponentServiceImpl());
    }
}
