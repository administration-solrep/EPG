package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.lois;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.ui.bean.pan.AbstractPanSortedList;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanFiltreEnum;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.helper.PanHelper;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanAbstractOngletContextuel;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.tuple.Pair;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanTableaulois")
public class PanTableaulois extends PanAbstractOngletContextuel {

    public PanTableaulois() {
        super();
    }

    @Override
    public void loadTabContent(Map<String, Serializable> mapSearch) {
        context.putInContextData(
            PanContextDataKey.JSON_SEARCH_TABLEAU_LOIS,
            mapSearch != null ? mapSearch : new HashMap<>()
        );
        LoisSuiviesForm loisSuiviForm = PanHelper.getPaginateFormFromUserSession(mapSearch, context);
        context.putInContextData(PanContextDataKey.PAN_FORM, loisSuiviForm);

        // Reconstitution du provider et des informations associées
        String currentSection = context.getFromContextData(PanContextDataKey.CURRENT_SECTION);
        String currentTab = context.getFromContextData(PanContextDataKey.CURRENT_TAB);

        Map<String, Serializable> filters = new LinkedHashMap<>();
        if (
            currentSection.equals(UserSessionHelper.getUserSessionParameter(context, PanUserSessionKey.SECTION)) &&
            currentTab.equals(UserSessionHelper.getUserSessionParameter(context, PanUserSessionKey.ONGLET))
        ) {
            // On tente de récupérer les filtres
            filters =
                ObjectHelper.requireNonNullElse(
                    UserSessionHelper.getUserSessionParameter(context, PanUserSessionKey.FILTERS),
                    new LinkedHashMap<>()
                );
        } else {
            UserSessionHelper.clearUserSessionParameter(context, PanUserSessionKey.FILTERS);
            UserSessionHelper.clearUserSessionParameter(context, PanUserSessionKey.SECTION);
            UserSessionHelper.clearUserSessionParameter(context, PanUserSessionKey.ONGLET);
        }

        FiltreUtils.extractFilters(mapSearch, filters, PanFiltreEnum.class);
        context.putInContextData(PanContextDataKey.PAN_FILTERS, filters);

        AbstractPanSortedList<AbstractMapDTO> textesMaitres = PanUIServiceLocator
            .getPanUIService()
            .getTableauMaitre(context);

        Map<String, Object> templateData = template.getData();
        template.getData().put(STTemplateConstants.RESULT_FORM, loisSuiviForm);
        template.getData().put(STTemplateConstants.RESULT_LIST, textesMaitres);
        template.getData().put(STTemplateConstants.LST_COLONNES, textesMaitres.getListeColonnes());
        template.getData().put(STTemplateConstants.NB_RESULTS, textesMaitres.getNbTotal());

        Map<String, Object> otherParams = FiltreUtils.initOtherParamMapWithProviderInfos(context);
        templateData.put(STTemplateConstants.OTHER_PARAMETER, otherParams);

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, textesMaitres, templateData);
    }

    @POST
    @Override
    public ThTemplate sortPaginateTable(@FormParam("search") String jsonSearch) {
        Map<String, Serializable> mapSearch = FiltreUtils.extractMapSearch(jsonSearch);
        context.putInContextData(PanContextDataKey.MAP_SEARCH, mapSearch);

        return super.sortPaginateTable(jsonSearch);
    }

    @Path("exporter")
    @GET
    @Produces("application/vnd.ms-excel")
    public Response exporterTableau() {
        return exporterTableauForSection(ActiviteNormativeEnum.APPLICATION_DES_LOIS);
    }

    protected Response exporterTableauForSection(ActiviteNormativeEnum section) {
        String subTab = section == ActiviteNormativeEnum.APPLICATION_DES_LOIS
            ? ActiviteNormativeConstants.TAB_AN_TABLEAU_LOIS
            : ActiviteNormativeConstants.TAB_AN_TABLEAU_ORDONNANCES;

        context.putInContextData(PanContextDataKey.CURRENT_SECTION, section.getId());
        context.putInContextData(PanContextDataKey.CURRENT_SUB_TAB, subTab);
        Pair<File, String> exportData = PanUIServiceLocator.getPanUIService().genererXls(context);
        return FileDownloadUtils.getAttachmentXls(exportData.getLeft(), exportData.getRight());
    }

    @Override
    protected PanUserSessionKey getSortPaginateParameterKey() {
        return PanUserSessionKey.JSON_SEARCH_TABLEAU_LOIS;
    }
}
