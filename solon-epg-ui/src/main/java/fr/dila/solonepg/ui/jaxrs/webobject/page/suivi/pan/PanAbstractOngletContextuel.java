package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.ui.enums.pan.PanActionCategory;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.bean.Onglet;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.enums.ActionCategory;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.platform.actions.Action;

public abstract class PanAbstractOngletContextuel extends PanAbstractTab {
    private static final String PAN_WEBOBJECT = "Pan";
    private static final String PAN_BIRT_TAB_WEBOBJECT = "PanBirtContextualtab";

    public PanAbstractOngletContextuel() {
        super();
    }

    public abstract void loadTabContent(Map<String, Serializable> mapSearch);

    protected abstract PanUserSessionKey getSortPaginateParameterKey();

    @GET
    public ThTemplate getTabContent() {
        ActiviteNormativeEnum currentSection = context.getFromContextData(PanContextDataKey.CURRENT_SECTION_ENUM);
        String onglet = context.getFromContextData(PanContextDataKey.CURRENT_TAB);
        if (!"historique-maj".equals(onglet)) {
            verifyAnyAccess(context.getSession(), currentSection);
        } else {
            verifyMinisterOrUpdaterAccess(context.getSession(), currentSection);
        }
        template.setContext(context);
        context = PanUIServiceLocator.getPanUIService().putPanActionDTOInContext(context);

        Map<String, Serializable> mapSearch = ObjectHelper.requireNonNullElseGet(
            context.getFromContextData(PanContextDataKey.MAP_SEARCH),
            HashMap::new
        );
        Map<String, Object> templateData = template.getData();

        templateData.put(PanTemplateConstants.CURRENT_SECTION, currentSection.getId());
        templateData.put(PanTemplateConstants.CURRENT_PAN_TAB, onglet);

        PanUIServiceLocator.getPanUIService().setTemplateUrls(currentSection.getId(), onglet, templateData);

        template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());

        template.getData().put(PanTemplateConstants.CURRENT_SECTION, currentSection.getId());
        template.getData().put(PanTemplateConstants.CURRENT_PAN_TAB, onglet);
        String url = "/suivi/pan/" + currentSection.getId() + "/" + onglet;
        template.getData().put(PanTemplateConstants.AJAX_URL_CONTEXTUAL_TAB, "/ajax" + url);
        template.getData().put(PanTemplateConstants.URL_CONTEXTUAL_TAB, url);
        template.getData().put(STTemplateConstants.DATA_URL, url);
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, "/ajax" + url);
        template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());

        loadTabContent(mapSearch);

        templateData.put(
            PanTemplateConstants.SUBTAB_RIGHT_ACTIONS,
            context.getActions(PanActionCategory.TAB_TOOLBAR_RIGHT)
        );

        if (template instanceof AjaxLayoutThTemplate) {
            template.setName(getMyTemplateName());
        }
        return template;
    }

    /**
     * Quand on clique sur le lien du texte maitre on doit renvoyer le bloc entier
     * et on ne connait pas a priori le nom du sous-onglet à charger
     */
    @Path("textemaitre/{idTexteMaitre}")
    public Object getTexteMaitreBlock(@PathParam("idTexteMaitre") String id) {
        return getBlocContextuel(id, "textemaitre");
    }

    @Path("textemaitre/{idTexteMaitre}/{subtab}")
    public Object getTexteMaitreSubtab(@PathParam("idTexteMaitre") String id, @PathParam("subtab") String subtab) {
        return getSousOngletContextuel(id, subtab, "textemaitre");
    }

    /**
     * Quand on clique sur le lien du ministere on doit renvoyer le bloc entier
     * et on ne connait pas a priori le nom du sous-onglet à charger
     */
    @Path("ministere/{idMinistere}")
    public Object getMinistereBlock(@PathParam("idMinistere") String id) {
        context.putInContextData(PanContextDataKey.MINISTERE_PILOTE_ID, id);
        return getBlocContextuel(id, "ministere");
    }

    @Path("ministere/{idMinistere}/{subtab}")
    public Object getMinistereSubtab(@PathParam("idMinistere") String id, @PathParam("subtab") String subtab) {
        context.putInContextData(PanContextDataKey.MINISTERE_PILOTE_ID, id);
        return getSousOngletContextuel(id, subtab, "ministere");
    }

    @POST
    public ThTemplate sortPaginateTable(String jsonSearch) {
        if (jsonSearch != null) {
            UserSessionHelper.putUserSessionParameter(context, getSortPaginateParameterKey(), jsonSearch);
        }
        return getTabContent();
    }

    /**
     * On charge le bloc contextuel sans connaitre a priori le nom du sous-onglet à charger
     */
    private Object getBlocContextuel(String id, String contexte) {
        String delegationWebObject = getDelegationWebObject(id, "", contexte);

        if (template instanceof AjaxLayoutThTemplate) {
            template.setName(CONTEXTUAL_BLOCK_TEMPLATE); // Fragment pour recharger tout le bloc qu'on transmet lors de la délégation au controlleur du sous-onglet
        } else {
            template = getTabContent();
        }
        return newObject(delegationWebObject, context, template);
    }

    private Object getSousOngletContextuel(String id, String subtab, String contexte) {
        String delegationWebObject = getDelegationWebObject(id, subtab, contexte);

        if (template instanceof AjaxLayoutThTemplate) {
            return newObject(delegationWebObject, context);
        } else {
            template = getTabContent();
            return newObject(delegationWebObject, context, template);
        }
    }

    private String getDelegationWebObject(String id, String subtab, String contexte) {
        context.putInContextData(PanContextDataKey.TEXTE_MAITRE_ID, id);
        context.putInContextData(PanContextDataKey.PAN_CONTEXT, contexte);
        context.putInContextData(PanContextDataKey.MASQUER_APPLIQUE, true);

        context = PanUIServiceLocator.getPanUIService().putPanActionDTOInContext(context);

        ActionCategory actionCategory = PanActionCategory.TEXTEM_TAB;
        if (StringUtils.equals(contexte, ActiviteNormativeConstants.PAN_CONTEXT_MINISTERE)) {
            actionCategory = PanActionCategory.MINISTERE_TAB;
        } else {
            context.setCurrentDocument(id);
        }
        String delegationWebObject = "";
        List<Action> sousOnglets = context.getActions(actionCategory);
        OngletConteneur subContent = PanUIServiceLocator.getPanUIService().actionsToTabs(sousOnglets, subtab);
        Onglet sousOngletActif = PanUIServiceLocator.getPanUIService().getActiveTab(subContent);
        if (sousOngletActif != null) {
            subtab = sousOngletActif.getId();
        }
        context.putInContextData(PanContextDataKey.CURRENT_SUB_TAB, subtab);

        if (sousOngletActif != null && StringUtils.equals(sousOngletActif.getFragmentName(), "panBirtSubtab")) {
            context.putInContextData(PanContextDataKey.FRAGMENT_FILE, sousOngletActif.getFragmentFile());
            delegationWebObject = PAN_BIRT_TAB_WEBOBJECT;
        } else {
            delegationWebObject = PAN_WEBOBJECT + pathSectionAndSubtab(subtab);
        }
        return delegationWebObject;
    }

    private String pathSectionAndSubtab(String subtab) {
        String section = context.getFromContextData(PanContextDataKey.CURRENT_SECTION);
        section = section.replace("-", "");
        subtab = subtab.replace("-", "");
        return StringUtils.capitalize(section) + StringUtils.capitalize(subtab);
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
}
