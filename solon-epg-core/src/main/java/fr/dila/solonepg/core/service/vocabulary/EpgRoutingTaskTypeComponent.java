package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.EpgRoutingTaskTypeService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgRoutingTaskTypeComponent
    extends ServiceEncapsulateComponent<EpgRoutingTaskTypeService, EpgRoutingTaskTypeServiceImpl> {

    public EpgRoutingTaskTypeComponent() {
        super(EpgRoutingTaskTypeService.class, new EpgRoutingTaskTypeServiceImpl());
    }
}
