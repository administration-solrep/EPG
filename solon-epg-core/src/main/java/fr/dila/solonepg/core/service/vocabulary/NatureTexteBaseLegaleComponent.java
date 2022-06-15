package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.NatureTexteBaseLegaleService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class NatureTexteBaseLegaleComponent
    extends ServiceEncapsulateComponent<NatureTexteBaseLegaleService, NatureTexteBaseLegaleServiceImpl> {

    public NatureTexteBaseLegaleComponent() {
        super(NatureTexteBaseLegaleService.class, new NatureTexteBaseLegaleServiceImpl());
    }
}
