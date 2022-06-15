package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan;

import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.ui.bean.pan.PanOnglet;
import fr.dila.solonepg.ui.enums.pan.PanActionCategory;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.th.model.EpgSuiviTemplate;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "Pan")
public class Pan extends SolonWebObject {
    private static final String PAN_WEBOBJECT = "Pan";

    public Pan() {
        super();
    }

    @Path("stats")
    public Object doStats() {
        return newObject("PanStatistiques");
    }

    @Path("journal")
    public Object doJournalTechnique() {
        return newObject("PanJournalTechnique", context, template);
    }

    @Path("{section}/{tab}")
    public Object getPanTab(@PathParam("section") String section, @PathParam("tab") String tab) {
        template.setName("pages/suivi/panConsult");
        template.setContext(context);

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
        context = PanUIServiceLocator.getPanUIService().putPanActionDTOInContext(context);

        Map<String, Object> map = new HashMap<>();
        List<Action> onglets = context.getActions(PanActionCategory.TAB);
        OngletConteneur content = PanUIServiceLocator.getPanUIService().actionsToTabs(onglets, tab);

        map.put(PanTemplateConstants.MY_PAN_TABS, content);
        map.put(PanTemplateConstants.CURRENT_SECTION, section);
        map.put(PanTemplateConstants.CURRENT_PAN_TAB, tab);
        template.setData(map);

        tab = tab.replace("-", "");
        return newObject(PAN_WEBOBJECT + StringUtils.capitalize(tab), context, template);
    }

    @Path("ordonnances/tableau-ordonnances/exporter")
    public Object exportTableauOrdo() {
        return newObject("PanTableauordonnances");
    }

    @Path("lois/tableau-lois/exporter")
    public Object exportTableauLois() {
        return newObject("PanTableaulois");
    }

    @GET
    @Path("{section}/{tab}/{subtab}")
    public Object getPanSubtab(
        @PathParam("section") String section,
        @PathParam("tab") String tab,
        @PathParam("subtab") String subtab
    )
        throws IllegalAccessException, InstantiationException {
        ThTemplate template = getMyTemplate();
        template.setName("pages/suivi/panConsult");
        template.setContext(context);
        context = PanUIServiceLocator.getPanUIService().putPanActionDTOInContext(context);

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
        map.put(PanTemplateConstants.CURRENT_PAN_SUBTAB, tab);

        List<Action> onglets = context.getActions(PanActionCategory.TAB);
        OngletConteneur content = PanUIServiceLocator.getPanUIService().actionsToTabs(onglets, tab);
        map.put(PanTemplateConstants.MY_PAN_TABS, content);

        List<Action> sousOnglets = context.getActions(PanActionCategory.SUBTAB);
        OngletConteneur subContent = PanUIServiceLocator.getPanUIService().actionsToTabs(sousOnglets, subtab);

        map.put(PanTemplateConstants.MY_INNER_SUBTABS, subContent);

        PanOnglet actif = PanUIServiceLocator.getPanUIService().getActiveTab(subContent);

        PanStatistiques.loadBirtContext(actif, context, map);

        template.setData(map);
        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgSuiviTemplate();
    }
}
