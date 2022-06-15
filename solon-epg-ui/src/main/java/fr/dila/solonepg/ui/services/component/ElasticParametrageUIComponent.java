package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.ElasticParametrageUIService;
import fr.dila.solonepg.ui.services.impl.ElasticParametrageUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class ElasticParametrageUIComponent
    extends ServiceEncapsulateComponent<ElasticParametrageUIService, ElasticParametrageUIServiceImpl> {

    /**
     * Default constructor
     */
    public ElasticParametrageUIComponent() {
        super(ElasticParametrageUIService.class, new ElasticParametrageUIServiceImpl());
    }
}
