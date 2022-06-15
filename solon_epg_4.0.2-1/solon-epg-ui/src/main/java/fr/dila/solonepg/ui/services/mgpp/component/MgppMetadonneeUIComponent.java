package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppMetadonneeUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppMetadonneeUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppMetadonneeUIComponent
    extends ServiceEncapsulateComponent<MgppMetadonneeUIService, MgppMetadonneeUIServiceImpl> {

    public MgppMetadonneeUIComponent() {
        super(MgppMetadonneeUIService.class, new MgppMetadonneeUIServiceImpl());
    }
}
