package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgTraitementPapierListeUIService;
import fr.dila.solonepg.ui.services.impl.EpgTraitementPapierListeUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgTraitementPapierListeUIComponent
    extends ServiceEncapsulateComponent<EpgTraitementPapierListeUIService, EpgTraitementPapierListeUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgTraitementPapierListeUIComponent() {
        super(EpgTraitementPapierListeUIService.class, new EpgTraitementPapierListeUIServiceImpl());
    }
}
