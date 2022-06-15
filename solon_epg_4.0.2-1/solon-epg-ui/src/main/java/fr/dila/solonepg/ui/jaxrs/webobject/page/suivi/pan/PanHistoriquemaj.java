package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan;

import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.ui.bean.pan.LigneHistoriquePanUnsortedList;
import fr.dila.solonepg.ui.enums.pan.PanActionEnum;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.helper.PanHelper;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanHistoriquemaj")
public class PanHistoriquemaj extends PanAbstractOngletContextuel {

    public PanHistoriquemaj() {
        super();
    }

    @Override
    public void loadTabContent(Map<String, Serializable> mapSearch) {
        ActiviteNormativeEnum currentMenu = context.getFromContextData(PanContextDataKey.CURRENT_SECTION_ENUM);

        context.putInContextData(
            PanContextDataKey.JSON_SEARCH_HISTORIQUE_MAJ,
            mapSearch != null ? mapSearch : new HashMap<>()
        );

        LigneHistoriquePanUnsortedList historique = PanUIServiceLocator
            .getHistoriqueMajMinUIService()
            .getHistoriquePaginated(context, currentMenu.getMajTarget());

        LoisSuiviesForm loisSuiviForm = PanHelper.getPaginateFormFromUserSession(mapSearch, context);
        template.getData().put(STTemplateConstants.RESULT_FORM, loisSuiviForm);
        template.getData().put(STTemplateConstants.RESULT_LIST, historique);
        template.getData().put(STTemplateConstants.LST_COLONNES, historique.getListeColonnes());
        template.getData().put(STTemplateConstants.NB_RESULTS, historique.getNbTotal());

        template.getData().put("deleteHistoriqueAction", context.getAction(PanActionEnum.HISTORIQUE_DELETE));
    }

    @Override
    public String getMyTemplateName() {
        return "fragments/pan/historique-maj";
    }

    @Override
    protected PanUserSessionKey getSortPaginateParameterKey() {
        return PanUserSessionKey.JSON_SEARCH_HISTORIQUE_MAJ;
    }

    @Override
    @POST
    public ThTemplate sortPaginateTable(@FormParam("search") String jsonSearch) {
        Map<String, Serializable> mapSearch = FiltreUtils.extractMapSearch(jsonSearch);
        context.putInContextData(PanContextDataKey.MAP_SEARCH, mapSearch);
        return super.sortPaginateTable(jsonSearch);
    }
}
