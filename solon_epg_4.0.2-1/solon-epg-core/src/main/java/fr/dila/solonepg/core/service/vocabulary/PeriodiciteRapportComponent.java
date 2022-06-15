package fr.dila.solonepg.core.service.vocabulary;

import fr.dila.solonepg.api.service.vocabulary.PeriodiciteRapportService;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class PeriodiciteRapportComponent
    extends ServiceEncapsulateComponent<PeriodiciteRapportService, PeriodiciteRapportServiceImpl> {

    public PeriodiciteRapportComponent() {
        super(PeriodiciteRapportService.class, new PeriodiciteRapportServiceImpl());
    }
}
