package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.ui.bean.pan.AbstractPanSortedList;
import fr.dila.solonepg.ui.bean.pan.PanOnglet;
import fr.dila.solonepg.ui.enums.pan.PanActionCategory;
import fr.dila.solonepg.ui.enums.pan.PanActionEnum;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanFiltreEnum;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.helper.PanHelper;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanStatistiques;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanTableaumaitre;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanAjax")
public class PanAjax extends SolonWebObject {
    private static final String PAN_WEBOBJECT = "Pan";
    private static final String MASQUER = "masquer";

    public PanAjax() {
        super();
    }

    @POST
    public ThTemplate forDelegation() {
        return template;
    }

    @Path("{section}/{tab}")
    public Resource getPanSection(@PathParam("section") String section, @PathParam("tab") String tab) {
        // On indique en context l'onglet courant
        context.putInContextData(PanContextDataKey.CURRENT_SECTION, section);
        ActiviteNormativeEnum currentMenu = ActiviteNormativeEnum.getById(section);
        context.putInContextData(PanContextDataKey.CURRENT_SECTION_ENUM, currentMenu);
        context.putInContextData(PanContextDataKey.CURRENT_TAB, tab);
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("menu.suivi.pan." + section + ".title"),
                "/suivi/pan/" + section + "/" + tab,
                Breadcrumb.TITLE_ORDER
            )
        );

        if (template instanceof AjaxLayoutThTemplate) {
            template.setName(getMyTemplateName());
        }

        tab = tab.replace("-", "");
        return newObject(PAN_WEBOBJECT + StringUtils.capitalize(tab), context, template);
    }

    /**
     * Méthode appelée quand on clique sur un onglet non contextuel, il faut alors
     * charger tout le contenu de l'onglet
     */
    @GET
    @Path("{section}/{tab}/{subtab}/loadtab")
    public ThTemplate getPanTabWithSubtab(
        @PathParam("section") String section,
        @PathParam("tab") String tab,
        @PathParam("subtab") String subtab
    ) {
        template.setContext(context);

        // On indique en context l'onglet courant
        context.putInContextData(PanContextDataKey.CURRENT_SECTION, section);
        ActiviteNormativeEnum currentMenu = ActiviteNormativeEnum.getById(section);
        context.putInContextData(PanContextDataKey.CURRENT_SECTION_ENUM, currentMenu);
        context.putInContextData(PanContextDataKey.CURRENT_TAB, tab);
        context.putInContextData(PanContextDataKey.CURRENT_SUB_TAB, subtab);
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("menu.suivi.pan." + section + ".title"),
                "/suivi/pan/" + section + "/" + tab + "/" + subtab,
                Breadcrumb.TITLE_ORDER
            )
        );

        Map<String, Object> map = new HashMap<>();
        map.put(PanTemplateConstants.CURRENT_SECTION, section);
        map.put(PanTemplateConstants.CURRENT_PAN_TAB, tab);
        map.put(PanTemplateConstants.CURRENT_PAN_SUBTAB, subtab);

        List<Action> sousOnglets = context.getActions(PanActionCategory.SUBTAB);
        OngletConteneur subContent = PanUIServiceLocator.getPanUIService().actionsToTabs(sousOnglets, subtab);
        map.put(PanTemplateConstants.MY_INNER_SUBTABS, subContent);

        PanOnglet sousOngletActif = PanUIServiceLocator.getPanUIService().getActiveTab(subContent);

        // si l'onglet possède un rapport Birt, on l'affiche
        PanStatistiques.loadBirtContext(sousOngletActif, context, map);

        template.setName("fragments/pan/panTabWithSubtabs");

        template.setData(map);
        return template;
    }

    /**
     * Méthode appelée quand on clique sur un sous-onglet d'un onglet non
     * contextuel, on charge uniquement le sous-onglet
     */
    @GET
    @Path("{section}/{tab}/{subtab}")
    public ThTemplate getPanSubtab(
        @PathParam("section") String section,
        @PathParam("tab") String tab,
        @PathParam("subtab") String subtab
    )
        throws IllegalAccessException, InstantiationException {
        getPanTabWithSubtab(section, tab, subtab);

        // On ne charge que le sous-onglet
        PanOnglet sousOngletActif = PanUIServiceLocator
            .getPanUIService()
            .getActiveTab((OngletConteneur) template.getData().get(PanTemplateConstants.MY_INNER_SUBTABS));

        template.setContext(context);

        // On indique en context l'onglet courant
        context.putInContextData(PanContextDataKey.CURRENT_SECTION, section);
        context.putInContextData(PanContextDataKey.CURRENT_TAB, tab);
        context.putInContextData(PanContextDataKey.CURRENT_SUB_TAB, subtab);

        Map<String, Object> map = new HashMap<>();
        map.put(PanTemplateConstants.CURRENT_SECTION, section);
        map.put(PanTemplateConstants.CURRENT_PAN_TAB, tab);
        map.put(PanTemplateConstants.CURRENT_PAN_SUBTAB, subtab);

        List<Action> sousOnglets = context.getActions(PanActionCategory.SUBTAB);
        OngletConteneur subContent = PanUIServiceLocator.getPanUIService().actionsToTabs(sousOnglets, subtab);
        map.put("myInnerSubTabs", subContent);

        // On ne charge que le sous-onglet
        if (sousOngletActif != null) {
            template.setName(sousOngletActif.getFragmentFile());
            // si l'onglet a un rapport BIRT, on l'affiche
            PanStatistiques.loadBirtContext(sousOngletActif, context, map);
        }

        return template;
    }

    @Path("stats")
    public Object doPanStats() {
        return newObject("PanStatistiquesAjax");
    }

    @Path("{section}/historique")
    public Resource doHistorique(@PathParam("section") String section) {
        context.putInContextData(PanContextDataKey.CURRENT_SECTION, section);
        return newObject("PanHistoriqueAjax", context);
    }

    @Path("journalTechnique")
    public Resource doJournal() {
        return newObject("PanJournalTechniqueAjax", context);
    }

    @Path("{section}/mesure")
    public Resource doMesure(@PathParam("section") String section) {
        putTexteMaitreInfosInContext(section);
        return newObject("PanMesureAjax", context);
    }

    @Path("{section}/decret")
    public Resource doDecret(@PathParam("section") String section) {
        putTexteMaitreInfosInContext(section);
        return newObject("PanDecretAjax", context);
    }

    @Path("{section}/habilitation")
    public Resource doHabilitation(@PathParam("section") String section) {
        putTexteMaitreInfosInContext(section);
        return newObject("PanHabilitationAjax", context);
    }

    @Path("{section}/ordonnanceHabilitation")
    public Resource doOrdonnancesHabilitation(@PathParam("section") String section) {
        putTexteMaitreInfosInContext(section);
        return newObject("PanOrdonnanceHabilitationAjax", context);
    }

    @Path("{section}/texte")
    public Resource doTexteTransposition(@PathParam("section") String section) {
        putTexteMaitreInfosInContext(section);
        return newObject("PanTexteTranspositionAjax", context);
    }

    @GET
    @Path("ratification/afficherMasquer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response afficher(@SwNotEmpty @QueryParam("action") String action) {
        UserSessionHelper.putUserSessionParameter(
            context,
            PanTableaumaitre.MASQUER_RATIFICATION,
            MASQUER.equals(action)
        );
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("{section}/loiRatification")
    public Resource doLoiRatification(@PathParam("section") String section) {
        putTexteMaitreInfosInContext(section);
        return newObject("PanLoiRatificationAjax", context);
    }

    @Path("{section}/recherche/experte")
    public Object doPanRechercheExperte(@PathParam("section") String section) {
        putTexteMaitreInfosInContext(section);
        return newObject("AjaxRequeteExpertePan", context);
    }

    @Path("traites-tableau-maitre")
    public Resource doTraites() {
        putTexteMaitreInfosInContext("traites");
        return newObject("PanTraitesAjax", context);
    }

    @Override
    public ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("", getMyContext());
    }

    public String getMyTemplateName() {
        String section = context.getFromContextData(PanContextDataKey.CURRENT_SECTION);
        String onglet = context.getFromContextData(PanContextDataKey.CURRENT_TAB);
        return "fragments/pan/" + section + "/" + onglet;
    }

    private void putTexteMaitreInfosInContext(String section) {
        context.putInContextData(PanContextDataKey.CURRENT_SECTION, section);
        context.putInContextData(PanContextDataKey.CURRENT_SUB_TAB, ActiviteNormativeConstants.TAB_AN_TEXTE_MAITRE);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reinitialiser")
    public Response doReinitialiserFiltres(@FormParam("cle") String cle) {
        UserSessionHelper.clearUserSessionParameter(context, PanUserSessionKey.FILTERS);
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Path("filtrer")
    public ThTemplate doSearch(
        @FormParam("search") String jsonSearch,
        @FormParam("currentSection") String currentSection,
        @FormParam("currentPanTab") String currentPanTab
    ) {
        Map<String, Serializable> mapSearch = FiltreUtils.extractMapSearch(jsonSearch);

        Map<String, Serializable> filters = new HashMap<>();
        FiltreUtils.extractFilters(mapSearch, filters, PanFiltreEnum.class);

        context.putInContextData(PanContextDataKey.CURRENT_TAB, currentPanTab);
        context.putInContextData(PanContextDataKey.CURRENT_SECTION, currentSection);

        LoisSuiviesForm loisSuiviesForm = new LoisSuiviesForm(mapSearch);
        UserSessionHelper.putUserSessionParameter(
            context,
            PanHelper.getPaginateFormSessionKey(context),
            loisSuiviesForm
        );

        context.putInContextData(PanContextDataKey.CURRENT_SECTION, currentSection);
        ActiviteNormativeEnum currentMenu = ActiviteNormativeEnum.getById(currentSection);
        context.putInContextData(PanContextDataKey.CURRENT_SECTION_ENUM, currentMenu);
        context.putInContextData(PanContextDataKey.CURRENT_TAB, currentPanTab);
        context.putInContextData(PanContextDataKey.PAN_FILTERS, filters);
        context.putInContextData(PanContextDataKey.PAN_FORM, loisSuiviesForm);
        template.setContext(context);

        switch (currentPanTab) {
            case "tableau-maitre":
                template.setName("fragments/pan/tableau-maitre-resultats");
                break;
            case "tableau-lois":
                template.setName("fragments/pan/lois/tableau-lois-resultats");
                break;
            case "tableau-ordonnances":
                template.setName("fragments/pan/ordonnances/tableau-ordonnances-resultats");
                break;
            default:
                template.setName("fragments/pan/tableau-maitre-resultats");
        }

        AbstractPanSortedList<AbstractMapDTO> textesMaitres = PanUIServiceLocator
            .getPanUIService()
            .getTableauMaitre(context);

        Map<String, Object> templateMap = template.getData();

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, textesMaitres, templateMap);

        List<Action> addAction = context.getActions(PanActionCategory.TEXTE_MAITRE_ADD);

        templateMap.put(PanTemplateConstants.TEXTE_MAITRE_ADD_ACTION, addAction.isEmpty() ? null : addAction.get(0));
        templateMap.put(
            PanTemplateConstants.IS_TRAITES_ACCORDS,
            currentSection.equals(ActiviteNormativeEnum.TRAITES_ET_ACCORDS.getId())
        );

        templateMap.put(STTemplateConstants.RESULT_LIST, textesMaitres);
        templateMap.put(STTemplateConstants.RESULT_FORM, loisSuiviesForm);
        templateMap.put(PanTemplateConstants.CURRENT_SECTION, currentSection);

        templateMap.put(STTemplateConstants.RESULT_VISIBLE, MapUtils.isNotEmpty(mapSearch));
        templateMap.put(STTemplateConstants.NB_RESULTS, textesMaitres.getNbTotal());
        templateMap.put(
            PanTemplateConstants.TEXTE_MAITRE_DELETE_ACTION,
            context.getAction(PanActionEnum.TEXTE_MAITRE_DELETE)
        );
        templateMap.put(STTemplateConstants.LST_SORTED_COLONNES, textesMaitres.getListeSortedColonnes());
        templateMap.put(STTemplateConstants.LST_SORTABLE_COLONNES, textesMaitres.getListeSortableAndVisibleColonnes());

        ActiviteNormativeEnum currentSectionAN = context.getFromContextData(PanContextDataKey.CURRENT_SECTION_ENUM);
        templateMap.put(PanTemplateConstants.TYPE_NOR_AUTO_COMPLETE, currentSectionAN.getNorType());
        templateMap.put(
            PanTemplateConstants.IS_TRANSPOSITION_DIRECTIVES,
            currentSection.equals(ActiviteNormativeEnum.TRANSPOSITION.getId())
        );
        templateMap.put(
            PanTemplateConstants.ACCESS_TAB_MIN,
            context.getAction(PanActionEnum.TAB_TABLEAU_MAITRE_MINISTERES) != null
        );
        PanUIServiceLocator.getPanUIService().setTemplateUrls(currentSection, currentPanTab, templateMap);

        template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());

        return template;
    }
}
