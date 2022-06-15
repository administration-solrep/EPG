package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.TypePublicationService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class TypePublicationComponent
    extends ServiceEncapsulateComponent<TypePublicationService, TypePublicationServiceImpl> {

    public TypePublicationComponent() {
        super(TypePublicationService.class, new TypePublicationServiceImpl());
    }
}
