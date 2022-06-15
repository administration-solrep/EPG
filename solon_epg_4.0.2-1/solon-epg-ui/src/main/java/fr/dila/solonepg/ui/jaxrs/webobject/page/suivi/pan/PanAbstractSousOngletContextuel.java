package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan;

import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.ui.bean.pan.ConsultTexteMaitreDTO;
import fr.dila.solonepg.ui.bean.pan.PanOnglet;
import fr.dila.solonepg.ui.enums.pan.PanActionCategory;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.enums.ActionCategory;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import java.io.File;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.nuxeo.ecm.platform.actions.Action;

public abstract class PanAbstractSousOngletContextuel extends PanAbstractTab {

    public PanAbstractSousOngletContextuel() {
        super();
    }

    public abstract void loadSubtabContent();

    @GET
    public ThTemplate getSubtabContent() {
        template.setContext(context);

        String subtab = context.getFromContextData(PanContextDataKey.CURRENT_SUB_TAB);
        String contexte = context.getFromContextData(PanContextDataKey.PAN_CONTEXT);

        context = PanUIServiceLocator.getPanUIService().putPanActionDTOInContext(context);

        ActionCategory actionCategory = PanActionCategory.TEXTEM_TAB;
        if (StringUtils.equals(contexte, ActiviteNormativeConstants.PAN_CONTEXT_MINISTERE)) {
            actionCategory = PanActionCategory.MINISTERE_TAB;
        } else if (StringUtils.equals(contexte, ActiviteNormativeConstants.PAN_CONTEXT_TEXTEM)) {
            ConsultTexteMaitreDTO texteMaitreDTO = PanUIServiceLocator
                .getPanUIService()
                .getConsultTexteMaitreDTO(context);
            template.getData().put(PanTemplateConstants.CONSULT_TEXTE_MAITRE_DTO, texteMaitreDTO);
            context.putInContextData(PanContextDataKey.CONSULT_TEXTEM_DTO, texteMaitreDTO);
            template
                .getData()
                .put(
                    PanTemplateConstants.SUBTAB_LEFT_ACTIONS,
                    context.getActions(PanActionCategory.SUBTAB_TOOLBAR_LEFT)
                );
            template
                .getData()
                .put(
                    PanTemplateConstants.SUBTAB_RIGHT_ACTIONS,
                    context.getActions(PanActionCategory.SUBTAB_TOOLBAR_RIGHT)
                );
            template.getData().put("texteMaitreLockActions", context.getActions(PanActionCategory.TEXTEM_ACTIONS_LOCK));
        }

        List<Action> sousOnglets = context.getActions(actionCategory);
        OngletConteneur subContent = PanUIServiceLocator.getPanUIService().actionsToTabs(sousOnglets, subtab);

        PanOnglet sousOngletActif = PanUIServiceLocator.getPanUIService().getActiveTab(subContent); // quand on charge le bloc contextuel entier, subtab est vide donc on récupère le nom de l'onglet actif par défaut càd le premier

        context.putInContextData(PanContextDataKey.ONGLET_ACTIF, sousOngletActif);
        template
            .getData()
            .put(PanTemplateConstants.CURRENT_SECTION, context.getFromContextData(PanContextDataKey.CURRENT_SECTION));
        template.getData().put(PanTemplateConstants.MY_CONTEXTUAL_TABS, subContent);
        template
            .getData()
            .put(PanTemplateConstants.CURRENT_PAN_TAB, context.getFromContextData(PanContextDataKey.CURRENT_TAB));
        template.getData().put(PanTemplateConstants.CURRENT_PAN_SUBTAB, sousOngletActif.getId());
        template
            .getData()
            .put(PanTemplateConstants.PAN_CONTEXT, context.getFromContextData(PanContextDataKey.PAN_CONTEXT));
        template
            .getData()
            .put(PanTemplateConstants.ID_CONTEXTUEL, context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_ID));
        loadSubtabContent();

        String url = buildUrl();
        template.getData().put(PanTemplateConstants.FIRST_TABLE_DATA_URL, url);
        template.getData().put(PanTemplateConstants.FIRST_TABLE_DATA_AJAX_URL, "/ajax" + url);
        template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        if (template instanceof AjaxLayoutThTemplate && StringUtils.isBlank(template.getName())) {
            template.setName(getMyTemplateName());
        }
        return template;
    }

    @Path("exporter")
    @GET
    @Produces("application/vnd.ms-excel")
    public Response exporterSousOngletContextuel() {
        String contexte = context.getFromContextData(PanContextDataKey.PAN_CONTEXT);

        if (StringUtils.equals(contexte, ActiviteNormativeConstants.PAN_CONTEXT_TEXTEM)) {
            TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);
            context.putInContextData(PanContextDataKey.MINISTERE_PILOTE_ID, texteMaitre.getMinisterePilote());
            context.putInContextData(PanContextDataKey.MASQUER_APPLIQUE, false);

            Pair<File, String> exportData = PanUIServiceLocator.getPanUIService().genererXls(context);
            return FileDownloadUtils.getAttachmentXls(exportData.getLeft(), exportData.getRight());
        } else {
            return Response.noContent().build();
        }
    }

    @Override
    public ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("", getMyContext());
    }

    protected String buildUrl() {
        String section = context.getFromContextData(PanContextDataKey.CURRENT_SECTION);
        String onglet = context.getFromContextData(PanContextDataKey.CURRENT_TAB);
        String sousOnglet = context.getFromContextData(PanContextDataKey.CURRENT_SUB_TAB);
        String contexte = context.getFromContextData(PanContextDataKey.PAN_CONTEXT);
        String id = context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_ID);
        return "/suivi/pan/" + section + "/" + onglet + "/" + contexte + "/" + id + "/" + sousOnglet;
    }

    public String getMyTemplateName() {
        String section = context.getFromContextData(PanContextDataKey.CURRENT_SECTION);
        String sousOnglet = context.getFromContextData(PanContextDataKey.CURRENT_SUB_TAB);
        return "fragments/pan/" + section + "/" + sousOnglet;
    }
}
