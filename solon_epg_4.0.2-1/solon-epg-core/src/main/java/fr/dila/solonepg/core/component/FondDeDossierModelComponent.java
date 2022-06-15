package fr.dila.solonepg.core.component;

import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.solonepg.core.service.FondDeDossierModelServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class FondDeDossierModelComponent
    extends ServiceEncapsulateComponent<FondDeDossierModelService, FondDeDossierModelServiceImpl> {

    public FondDeDossierModelComponent() {
        super(FondDeDossierModelService.class, new FondDeDossierModelServiceImpl());
    }
}
