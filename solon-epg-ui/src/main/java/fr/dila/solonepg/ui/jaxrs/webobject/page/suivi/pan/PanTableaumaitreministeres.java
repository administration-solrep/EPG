package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan;

import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.st.ui.bean.OrganigrammeElementDTO;
import fr.dila.st.ui.services.STUIServiceLocator;
import fr.dila.st.ui.services.impl.OrganigrammeTreeUIServiceImpl;
import fr.dila.st.ui.th.model.ThTemplate;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.POST;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanTableaumaitreministeres")
public class PanTableaumaitreministeres extends PanAbstractOngletContextuel {

    public PanTableaumaitreministeres() {
        super();
    }

    @Override
    public void loadTabContent(Map<String, Serializable> mapSearch) {
        List<OrganigrammeElementDTO> organigramme = STUIServiceLocator
            .getOrganigrammeTreeService()
            .getOrganigramme(context);
        context.putInContextData(
            PanContextDataKey.JSON_SEARCH_TABLEAU_MAITRE_MINISTERES,
            mapSearch != null ? mapSearch : new HashMap<>()
        );

        List<String> ministeresList = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .getAllAplicationMinisteresList(
                context.getSession(),
                context.getFromContextData(PanContextDataKey.CURRENT_SECTION),
                false
            );
        if (!organigramme.isEmpty()) {
            organigramme
                .get(0)
                .setChilds(
                    organigramme
                        .get(0)
                        .getChilds()
                        .stream()
                        .map(OrganigrammeElementDTO.class::cast)
                        .filter(node -> ministeresList.contains(node.getMinistereId()))
                        .collect(Collectors.toList())
                );
        }
        template.getData().put(OrganigrammeTreeUIServiceImpl.ORGANIGRAMME_TREE_KEY, organigramme);
        template
            .getData()
            .put(PanTemplateConstants.PAN_CONTEXT, context.getFromContextData(PanContextDataKey.PAN_CONTEXT));
    }

    @Override
    public String getMyTemplateName() {
        return "fragments/pan/tableau-maitre-ministeres";
    }

    @Override
    protected PanUserSessionKey getSortPaginateParameterKey() {
        return PanUserSessionKey.JSON_SEARCH_TABLEAU_MAITRE_MINISTERES;
    }

    @Override
    @POST
    public ThTemplate sortPaginateTable(String jsonSearch) {
        return super.sortPaginateTable(jsonSearch);
    }
}
