package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.CategorieActeService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class CategorieActeServiceComponent
    extends ServiceEncapsulateComponent<CategorieActeService, CategorieActeServiceImpl> {

    public CategorieActeServiceComponent() {
        super(CategorieActeService.class, new CategorieActeServiceImpl());
    }
}
