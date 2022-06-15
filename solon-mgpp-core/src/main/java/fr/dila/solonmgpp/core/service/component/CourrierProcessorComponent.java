package fr.dila.solonmgpp.core.service.component;

import fr.dila.solonmgpp.api.service.CourrierProcessorService;
import fr.dila.solonmgpp.core.service.CourrierProcessorServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class CourrierProcessorComponent
    extends ServiceEncapsulateComponent<CourrierProcessorService, CourrierProcessorServiceImpl> {

    public CourrierProcessorComponent() {
        super(CourrierProcessorService.class, new CourrierProcessorServiceImpl());
    }
}
