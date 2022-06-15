package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.DispositionHabilitationService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class DispositionHabilitationComponent
    extends ServiceEncapsulateComponent<DispositionHabilitationService, DispositionHabilitationServiceImpl> {

    public DispositionHabilitationComponent() {
        super(DispositionHabilitationService.class, new DispositionHabilitationServiceImpl());
    }
}
