package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.DirectiveEtatService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class DirectiveEtatComponent
    extends ServiceEncapsulateComponent<DirectiveEtatService, DirectiveEtatServiceImpl> {

    public DirectiveEtatComponent() {
        super(DirectiveEtatService.class, new DirectiveEtatServiceImpl());
    }
}
