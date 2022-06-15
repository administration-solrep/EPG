package fr.dila.solonepg.core.component;

import fr.dila.solonepg.api.service.EpgPdfDossierService;
import fr.dila.solonepg.core.service.EpgPdfDossierServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgPdfDossierComponent
    extends ServiceEncapsulateComponent<EpgPdfDossierService, EpgPdfDossierServiceImpl> {

    public EpgPdfDossierComponent() {
        super(EpgPdfDossierService.class, new EpgPdfDossierServiceImpl());
    }
}
