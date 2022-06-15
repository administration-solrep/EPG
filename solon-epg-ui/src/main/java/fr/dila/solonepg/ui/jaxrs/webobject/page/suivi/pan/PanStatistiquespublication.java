package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan;

import fr.dila.solonepg.ui.bean.pan.PanOnglet;
import fr.dila.solonepg.ui.constants.pan.PanConstants;
import fr.dila.solonepg.ui.enums.pan.PanActionCategory;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.th.model.ThTemplate;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ws.rs.POST;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanStatistiquespublication")
public class PanStatistiquespublication extends PanAbstractOngletContextuel {

    public PanStatistiquespublication() {
        super();
    }

    @Override
    public void loadTabContent(Map<String, Serializable> mapSearch) {
        List<Action> onglets = context.getActions(PanActionCategory.TAB);
        OngletConteneur content = PanUIServiceLocator
            .getPanUIService()
            .actionsToTabs(onglets, PanConstants.Tab.TAB_STATISTIQUES_PUBLICATION.getKey());

        PanOnglet ongletActif = PanUIServiceLocator.getPanUIService().getActiveTab(content);

        PanStatistiques.loadBirtContext(ongletActif, context, template.getData());

        template
            .getData()
            .put(PanTemplateConstants.CURRENT_SECTION, context.getFromContextData(PanContextDataKey.CURRENT_SECTION));
    }

    @Override
    protected PanUserSessionKey getSortPaginateParameterKey() {
        return PanUserSessionKey.JSON_SEARCH_STATISTIQUES_PUBLICATION;
    }

    @Override
    @POST
    public ThTemplate sortPaginateTable(String jsonSearch) {
        return super.sortPaginateTable(jsonSearch);
    }
}
