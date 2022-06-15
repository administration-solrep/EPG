package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.OrganigrammeInjectionUIService;
import fr.dila.solonepg.ui.services.impl.OrganigrammeInjectionUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class OrganigrammeInjectionUIComponent
    extends ServiceEncapsulateComponent<OrganigrammeInjectionUIService, OrganigrammeInjectionUIServiceImpl> {

    /**
     * Default constructor
     */
    public OrganigrammeInjectionUIComponent() {
        super(OrganigrammeInjectionUIService.class, new OrganigrammeInjectionUIServiceImpl());
    }
}
