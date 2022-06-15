package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgDerniersElementsComponentService;
import fr.dila.solonepg.ui.services.impl.EpgDerniersElementsComponentServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgDerniersElementsComponentComponent
    extends ServiceEncapsulateComponent<EpgDerniersElementsComponentService, EpgDerniersElementsComponentServiceImpl> {

    /**
     * Default constructor
     */
    public EpgDerniersElementsComponentComponent() {
        super(EpgDerniersElementsComponentService.class, new EpgDerniersElementsComponentServiceImpl());
    }
}
