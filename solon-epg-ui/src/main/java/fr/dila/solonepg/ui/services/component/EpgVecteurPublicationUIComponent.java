package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgVecteurPublicationUIService;
import fr.dila.solonepg.ui.services.impl.EpgVecteurPublicationUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgVecteurPublicationUIComponent
    extends ServiceEncapsulateComponent<EpgVecteurPublicationUIService, EpgVecteurPublicationUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgVecteurPublicationUIComponent() {
        super(EpgVecteurPublicationUIService.class, new EpgVecteurPublicationUIServiceImpl());
    }
}
