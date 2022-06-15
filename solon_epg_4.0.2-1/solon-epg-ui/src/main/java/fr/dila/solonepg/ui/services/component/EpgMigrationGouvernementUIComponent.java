package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgMigrationGouvernementUIService;
import fr.dila.solonepg.ui.services.impl.EpgMigrationGouvernementUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgMigrationGouvernementUIComponent
    extends ServiceEncapsulateComponent<EpgMigrationGouvernementUIService, EpgMigrationGouvernementUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgMigrationGouvernementUIComponent() {
        super(EpgMigrationGouvernementUIService.class, new EpgMigrationGouvernementUIServiceImpl());
    }
}
