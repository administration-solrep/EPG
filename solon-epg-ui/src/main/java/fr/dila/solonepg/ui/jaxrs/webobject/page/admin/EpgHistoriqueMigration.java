package fr.dila.solonepg.ui.jaxrs.webobject.page.admin;

import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.solonepg.ui.th.model.EpgUtilisateurTemplate;
import fr.dila.ss.ui.jaxrs.webobject.page.admin.SSHistoriqueMigration;
import fr.dila.ss.ui.services.organigramme.SSMigrationGouvernementUIService;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "HistoriqueMigration")
public class EpgHistoriqueMigration extends SSHistoriqueMigration {

    @Override
    protected ThTemplate getMyTemplate(SpecificContext context) {
        if (context.getWebcontext().getPrincipal().isMemberOf(STBaseFunctionConstant.ESPACE_ADMINISTRATION_READER)) {
            return new EpgAdminTemplate();
        } else {
            return new EpgUtilisateurTemplate();
        }
    }

    @Override
    protected SSMigrationGouvernementUIService getMigrationGouvernementUIService() {
        return EpgUIServiceLocator.getEpgMigrationGouvernementUIService();
    }
}
