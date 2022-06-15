package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.StatutArchivageFacetService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class StatutArchivageFacetComponent
    extends ServiceEncapsulateComponent<StatutArchivageFacetService, StatutArchivageFacetServiceImpl> {

    public StatutArchivageFacetComponent() {
        super(StatutArchivageFacetService.class, new StatutArchivageFacetServiceImpl());
    }
}
