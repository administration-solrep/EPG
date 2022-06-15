package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.TypeAvisTraitementPapierService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class TypeAvisTraitementPapierComponent
    extends ServiceEncapsulateComponent<TypeAvisTraitementPapierService, TypeAvisTraitementPapierServiceImpl> {

    public TypeAvisTraitementPapierComponent() {
        super(TypeAvisTraitementPapierService.class, new TypeAvisTraitementPapierServiceImpl());
    }
}
