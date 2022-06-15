package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.TypeActeService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class TypeActeServiceComponent extends ServiceEncapsulateComponent<TypeActeService, TypeActeServiceImpl> {

    public TypeActeServiceComponent() {
        super(TypeActeService.class, new TypeActeServiceImpl());
    }
}
