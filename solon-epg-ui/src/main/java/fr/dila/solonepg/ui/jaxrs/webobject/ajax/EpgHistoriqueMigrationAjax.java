package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import fr.dila.solonepg.ui.bean.EpgHistoriqueMigrationDTO;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.ss.ui.jaxrs.webobject.ajax.SSHistoriqueMigrationAjax;
import fr.dila.ss.ui.services.organigramme.SSMigrationGouvernementUIService;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.List;
import javax.ws.rs.GET;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "HistoriqueMigrationAjax")
public class EpgHistoriqueMigrationAjax extends SSHistoriqueMigrationAjax {
    private static final String NB_MIGRATION_KEY = "historique.nb.migrations";

    @GET
    @Override
    public ThTemplate getHistorique() {
        template.getData().put(SSTemplateConstants.MIGRATIONS, getMigrations());
        return template;
    }

    @Override
    protected SSMigrationGouvernementUIService getMigrationGouvernementUIService() {
        return EpgUIServiceLocator.getEpgMigrationGouvernementUIService();
    }

    @Override
    public String getTableCaption() {
        StringBuilder sbTableCaption = new StringBuilder(ResourceHelper.getString("historique.migration.title"));
        sbTableCaption.append(StringUtils.SPACE);
        sbTableCaption.append(ResourceHelper.getString(NB_MIGRATION_KEY, getMigrations().size()));

        return sbTableCaption.toString();
    }

    /**
     * Gets migrations
     *
     * @return the list of migrations
     */
    private List<EpgHistoriqueMigrationDTO> getMigrations() {
        return EpgUIServiceLocator.getEpgMigrationGouvernementUIService().getListHistoriqueMigrationDTO();
    }
}
