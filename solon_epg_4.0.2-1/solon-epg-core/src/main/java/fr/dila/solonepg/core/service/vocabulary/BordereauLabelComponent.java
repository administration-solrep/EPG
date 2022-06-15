package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.BordereauLabelService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class BordereauLabelComponent
    extends ServiceEncapsulateComponent<BordereauLabelService, BordereauLabelServiceImpl> {

    public BordereauLabelComponent() {
        super(BordereauLabelService.class, new BordereauLabelServiceImpl());
    }
}
