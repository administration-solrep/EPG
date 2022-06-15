package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.TypeMesureService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class TypeMesureComponent extends ServiceEncapsulateComponent<TypeMesureService, TypeMesureServiceImpl> {

    public TypeMesureComponent() {
        super(TypeMesureService.class, new TypeMesureServiceImpl());
    }
}
