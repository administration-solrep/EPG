package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppCorbeilleUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppCorbeilleUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppCorbeilleUIComponent
    extends ServiceEncapsulateComponent<MgppCorbeilleUIService, MgppCorbeilleUIServiceImpl> {

    /**
     * Default constructor
     */
    public MgppCorbeilleUIComponent() {
        super(MgppCorbeilleUIService.class, new MgppCorbeilleUIServiceImpl());
    }
}
