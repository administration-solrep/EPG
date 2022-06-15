package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.ModeleCourrierUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.ModeleCourrierUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class ModeleCourrierUIComponent
    extends ServiceEncapsulateComponent<ModeleCourrierUIService, ModeleCourrierUIServiceImpl> {

    public ModeleCourrierUIComponent() {
        super(ModeleCourrierUIService.class, new ModeleCourrierUIServiceImpl());
    }
}
