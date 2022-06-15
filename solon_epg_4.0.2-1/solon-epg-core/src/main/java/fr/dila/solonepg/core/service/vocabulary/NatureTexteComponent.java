package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.NatureTexteService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class NatureTexteComponent extends ServiceEncapsulateComponent<NatureTexteService, NatureTexteServiceImpl> {

    public NatureTexteComponent() {
        super(NatureTexteService.class, new NatureTexteServiceImpl());
    }
}
