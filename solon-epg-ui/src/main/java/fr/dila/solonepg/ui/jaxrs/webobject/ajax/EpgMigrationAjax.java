package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import fr.dila.solonepg.ui.jaxrs.webobject.page.admin.EpgMigration;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.ss.ui.jaxrs.webobject.ajax.SSMigrationAjax;
import fr.dila.ss.ui.services.organigramme.SSMigrationGouvernementUIService;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "MigrationAjax")
public class EpgMigrationAjax extends SSMigrationAjax {

    public EpgMigrationAjax() {
        super();
    }

    @Override
    public Map<String, String> getMigrationTypes() {
        return EpgMigration.MIGRATION_TYPES;
    }

    @Override
    public Map<String, List<String>> getActions() {
        return EpgMigration.ACTIONS;
    }

    @Override
    protected SSMigrationGouvernementUIService getMigrationGouvernementUIService() {
        return EpgUIServiceLocator.getEpgMigrationGouvernementUIService();
    }
}
