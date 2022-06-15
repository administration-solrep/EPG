package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.dossier;

import static fr.dila.solonepg.ui.enums.EpgActionCategory.DOSSIERS_SUPPRIMER_TAB_ACTION_LIST;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossiersSuppression")
public class EpgDossiersSuppression extends SolonWebObject {
    private static final String PARAM_TABS = "tabs";
    private static final String DATA_DELETE = "/admin/dossiers/suppression";
    private static final String ONGLET_CONSULTATION = "consultation";
    private static final String CAN_BE_SELECTED = "canBeSelected";
    private static final String ID = "id";

    @GET
    public ThTemplate getDossierASupprimer(
        @QueryParam("tab") String tab,
        @SwBeanParam DossierListForm dossierlistForm
    ) {
        if (StringUtils.isBlank(tab)) {
            tab = ONGLET_CONSULTATION;
        }

        String link = DATA_DELETE + "?tab=" + tab;
        if (ONGLET_CONSULTATION.equals(tab)) {
            verifyAction(EpgActionEnum.TAB_DOSSIERS_SUPPRIMER_CONSULTATION, link);
        } else {
            verifyAction(EpgActionEnum.TAB_DOSSIERS_SUPPRIMER_SUIVI, link);
        }

        template.setContext(context);
        template.setName("pages/admin/dossier/suppression-dossier");

        context.setNavigationContextTitle(
            new Breadcrumb("Dossier à supprimer", link, Breadcrumb.TITLE_ORDER, context.getWebcontext().getRequest())
        );

        Map<String, Object> map = createMapFromTab(tab, dossierlistForm, context);
        map.put(
            STTemplateConstants.MY_TABS,
            OngletConteneur.actionsToTabs(context, DOSSIERS_SUPPRIMER_TAB_ACTION_LIST, tab)
        );
        template.setData(map);
        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }

    public static Map<String, Object> createMapFromTab(
        String tab,
        DossierListForm dossierlistForm,
        SpecificContext context
    ) {
        EpgDossierList lstResults;
        Map<String, Object> map = new HashMap<>();

        dossierlistForm = ObjectHelper.requireNonNullElseGet(dossierlistForm, DossierListForm::newForm);

        if (ONGLET_CONSULTATION.equals(tab)) {
            context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
            lstResults =
                SolonEpgUIServiceLocator.getSolonEpgDossierListUIService().getDossiersCandidatElimination(context);
            map.put(
                STTemplateConstants.EDIT_ACTIONS,
                context.getActions(EpgActionCategory.ADMIN_DELETE_DOSSIER_ACTIONS)
            );
            UserSessionHelper.putUserSessionParameter(
                context,
                EpgContextDataKey.DOSSIER_RECHERCHE_FORM.name(),
                dossierlistForm
            );
        } else {
            context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM_SUIVI, dossierlistForm);
            lstResults =
                SolonEpgUIServiceLocator.getSolonEpgDossierListUIService().getDossiersValidesElimination(context);
            map.put(STTemplateConstants.EDIT_ACTIONS, ImmutableList.of());
            map.put(CAN_BE_SELECTED, false);
            UserSessionHelper.putUserSessionParameter(
                context,
                EpgContextDataKey.DOSSIER_RECHERCHE_FORM_SUIVI.name(),
                dossierlistForm
            );
        }

        dossierlistForm.setColumnVisibility(lstResults.getListeColonnes());

        Map<String, Object> otherParams = FiltreUtils.initOtherParamMapWithProviderInfos(context);
        otherParams.put(PARAM_TABS, ImmutableList.of(new SelectValueDTO("tab", tab)));
        map.put(STTemplateConstants.OTHER_PARAMETER, otherParams);

        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());
        map.put(STTemplateConstants.RESULT_FORM, dossierlistForm);
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.NB_RESULTS, lstResults.getNbTotal());
        map.put(STTemplateConstants.TITRE, lstResults.getTitre());
        map.put(STTemplateConstants.SOUS_TITRE, lstResults.getSousTitre());
        map.put(STTemplateConstants.DISPLAY_TABLE, !lstResults.getListe().isEmpty());
        map.put(STTemplateConstants.DATA_URL, DATA_DELETE);
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/admin/dossiers/suppression/" + tab);
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        map.put(ID, "listeDossierSuppression" + tab);

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, lstResults, map);

        return map;
    }
}
