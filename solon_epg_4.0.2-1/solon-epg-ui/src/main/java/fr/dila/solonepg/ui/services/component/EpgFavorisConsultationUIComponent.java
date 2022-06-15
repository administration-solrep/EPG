package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgFavorisConsultationUIService;
import fr.dila.solonepg.ui.services.impl.EpgFavorisConsultationUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgFavorisConsultationUIComponent
    extends ServiceEncapsulateComponent<EpgFavorisConsultationUIService, EpgFavorisConsultationUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgFavorisConsultationUIComponent() {
        super(EpgFavorisConsultationUIService.class, new EpgFavorisConsultationUIServiceImpl());
    }
}
