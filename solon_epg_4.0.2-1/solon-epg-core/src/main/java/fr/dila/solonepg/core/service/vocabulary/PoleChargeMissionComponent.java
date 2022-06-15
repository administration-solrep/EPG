package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.PoleChargeMissionService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class PoleChargeMissionComponent
    extends ServiceEncapsulateComponent<PoleChargeMissionService, PoleChargeMissionServiceImpl> {

    public PoleChargeMissionComponent() {
        super(PoleChargeMissionService.class, new PoleChargeMissionServiceImpl());
    }
}
