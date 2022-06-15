package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.TypeSignataireTraitementPapierService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class TypeSignataireTraitementPapierComponent
    extends ServiceEncapsulateComponent<TypeSignataireTraitementPapierService, TypeSignataireTraitementPapierServiceImpl> {

    public TypeSignataireTraitementPapierComponent() {
        super(TypeSignataireTraitementPapierService.class, new TypeSignataireTraitementPapierServiceImpl());
    }
}
