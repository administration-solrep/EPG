package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgTraitementPapierUIService;
import fr.dila.solonepg.ui.services.impl.EpgTraitementPapierUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgTraitementPapierUIComponent
    extends ServiceEncapsulateComponent<EpgTraitementPapierUIService, EpgTraitementPapierUIServiceImpl> {

    public EpgTraitementPapierUIComponent() {
        super(EpgTraitementPapierUIService.class, new EpgTraitementPapierUIServiceImpl());
    }
}
