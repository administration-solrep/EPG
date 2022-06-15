package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.ResponsableAmendementService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class ResponsableAmendementComponent
    extends ServiceEncapsulateComponent<ResponsableAmendementService, ResponsableAmendementServiceImpl> {

    public ResponsableAmendementComponent() {
        super(ResponsableAmendementService.class, new ResponsableAmendementServiceImpl());
    }
}
