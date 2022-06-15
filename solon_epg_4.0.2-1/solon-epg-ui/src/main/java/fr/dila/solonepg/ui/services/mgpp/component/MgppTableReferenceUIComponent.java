package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppTableReferenceUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppTableReferenceUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppTableReferenceUIComponent
    extends ServiceEncapsulateComponent<MgppTableReferenceUIService, MgppTableReferenceUIServiceImpl> {

    public MgppTableReferenceUIComponent() {
        super(MgppTableReferenceUIService.class, new MgppTableReferenceUIServiceImpl());
    }
}
