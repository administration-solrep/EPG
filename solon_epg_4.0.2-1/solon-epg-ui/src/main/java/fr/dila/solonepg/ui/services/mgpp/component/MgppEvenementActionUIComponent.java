package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppEvenementActionUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppEvenementActionUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppEvenementActionUIComponent
    extends ServiceEncapsulateComponent<MgppEvenementActionUIService, MgppEvenementActionUIServiceImpl> {

    /**
     * Default constructor
     */
    public MgppEvenementActionUIComponent() {
        super(MgppEvenementActionUIService.class, new MgppEvenementActionUIServiceImpl());
    }
}
