package fr.dila.solonmgpp.core.service.component;

import fr.dila.solonmgpp.api.service.ModeleCourrierService;
import fr.dila.solonmgpp.core.service.ModeleCourrierServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class ModeleCourrierComponent
    extends ServiceEncapsulateComponent<ModeleCourrierService, ModeleCourrierServiceImpl> {

    public ModeleCourrierComponent() {
        super(ModeleCourrierService.class, new ModeleCourrierServiceImpl());
    }
}
