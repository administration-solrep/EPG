package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.DelaiPublicationService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class DelaiPublicationServiceComponent
    extends ServiceEncapsulateComponent<DelaiPublicationService, DelaiPublicationServiceImpl> {

    public DelaiPublicationServiceComponent() {
        super(DelaiPublicationService.class, new DelaiPublicationServiceImpl());
    }
}
