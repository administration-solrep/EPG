package fr.dila.solonepg.core.component;

import fr.dila.solonepg.core.service.EpgMailServiceImpl;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgMailComponent extends ServiceEncapsulateComponent<STMailService, EpgMailServiceImpl> {

    public EpgMailComponent() {
        super(STMailService.class, new EpgMailServiceImpl());
    }
}
