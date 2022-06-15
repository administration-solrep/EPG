package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgDossierCreationUIService;
import fr.dila.solonepg.ui.services.impl.EpgDossierCreationUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgDossierCreationUIComponent
    extends ServiceEncapsulateComponent<EpgDossierCreationUIService, EpgDossierCreationUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgDossierCreationUIComponent() {
        super(EpgDossierCreationUIService.class, new EpgDossierCreationUIServiceImpl());
    }
}
