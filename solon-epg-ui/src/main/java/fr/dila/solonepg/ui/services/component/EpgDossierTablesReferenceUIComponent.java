package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgDossierTablesReferenceUIService;
import fr.dila.solonepg.ui.services.impl.EpgDossierTablesReferenceUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgDossierTablesReferenceUIComponent
    extends ServiceEncapsulateComponent<EpgDossierTablesReferenceUIService, EpgDossierTablesReferenceUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgDossierTablesReferenceUIComponent() {
        super(EpgDossierTablesReferenceUIService.class, new EpgDossierTablesReferenceUIServiceImpl());
    }
}
