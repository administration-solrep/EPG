package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.PrioriteCEService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class PrioriteCEServiceComponent extends ServiceEncapsulateComponent<PrioriteCEService, PrioriteCEServiceImpl> {

    public PrioriteCEServiceComponent() {
        super(PrioriteCEService.class, new PrioriteCEServiceImpl());
    }
}
