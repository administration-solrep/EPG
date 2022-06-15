package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppGenerationFicheUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppGenerationFicheUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppGenerationFicheUIComponent
    extends ServiceEncapsulateComponent<MgppGenerationFicheUIService, MgppGenerationFicheUIServiceImpl> {

    /**
     * Default constructor
     */
    public MgppGenerationFicheUIComponent() {
        super(MgppGenerationFicheUIService.class, new MgppGenerationFicheUIServiceImpl());
    }
}
