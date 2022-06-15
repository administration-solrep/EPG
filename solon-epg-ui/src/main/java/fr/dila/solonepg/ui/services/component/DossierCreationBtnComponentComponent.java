package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.DossierCreationBtnComponentService;
import fr.dila.solonepg.ui.services.impl.DossierCreationBtnComponentServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class DossierCreationBtnComponentComponent
    extends ServiceEncapsulateComponent<DossierCreationBtnComponentService, DossierCreationBtnComponentServiceImpl> {

    /**
     * Default constructor
     */
    public DossierCreationBtnComponentComponent() {
        super(DossierCreationBtnComponentService.class, new DossierCreationBtnComponentServiceImpl());
    }
}
