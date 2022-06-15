package fr.dila.solonepg.ui.jaxrs.webobject.page;

import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgEspaceSuiviUIService;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getSolonEpgDossierListUIService;
import static fr.dila.solonepg.ui.services.impl.EpgSuiviMenuServiceImpl.SUIVI_DOSSIER_KEY;
import static fr.dila.solonepg.ui.services.impl.EpgSuiviMenuServiceImpl.SUIVI_DOSSIER_LABEL;
import static fr.dila.solonepg.ui.services.impl.EpgSuiviMenuServiceImpl.SUIVI_DOSSIER_LIST_FORM_KEY;
import static fr.dila.solonepg.ui.services.impl.EpgSuiviMenuServiceImpl.SUIVI_DOSSIER_NODE_KEY;
import static fr.dila.solonepg.ui.services.impl.EpgSuiviMenuServiceImpl.SUIVI_DOSSIER_STATE;

import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.TraitementPapierList;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgColumnEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.helper.EpgDossierListHelper;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.EpgSuiviTemplate;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.ss.ui.jaxrs.webobject.page.SSSuivi;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.StringEscapeHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliSuivi")
public class EpgSuivi extends SSSuivi {
    public static final String CLE = "cle";
    public static final String STATE = "state";
    public static final String LABEL = "label";
    public static final String TITLE = "title";

    public EpgSuivi() {
        super();
    }

    @GET
    public ThTemplate getHome() {
        template.setName("pages/suivi");
        template.setContext(context);
        context.removeNavigationContextTitle();

        EpgUIServiceLocator.getEpgSuiviMenuService().getData(context);

        Map<String, Object> map = new HashMap<>();
        template.setData(map);
        return template;
    }

    @Path("pan")
    public Object getPan() {
        return newObject("Pan", context, template);
    }

    @Path("tableaux")
    public Object getTableauxDynamiques() {
        return newObject("TableauxDynamiques", context, template);
    }

    @GET
    @Path("liste")
    public ThTemplate getListe(
        @QueryParam(CLE) String key,
        @QueryParam(STATE) String state,
        @QueryParam(LABEL) String label
    ) {
        context.clearNavigationContext();

        ThTemplate template = getMyTemplate();

        template.setName("pages/suivi/listeDossiersSuivi");
        template.setContext(context);

        Map<String, Object> sessionSuivi = UserSessionHelper.getUserSessionParameter(context, SUIVI_DOSSIER_KEY);

        DossierListForm resultform = DossierListForm.newForm();

        if (sessionSuivi == null) {
            sessionSuivi = new HashMap<>();
            sessionSuivi.put(SUIVI_DOSSIER_NODE_KEY, key);
            sessionSuivi.put(SUIVI_DOSSIER_STATE, state);
            sessionSuivi.put(SUIVI_DOSSIER_LABEL, StringEscapeHelper.decodeSpecialCharactersHtmlParams(label));
        } else if (key == null) {
            key = (String) sessionSuivi.get(SUIVI_DOSSIER_NODE_KEY);
            state = (String) sessionSuivi.get(SUIVI_DOSSIER_STATE);
            label = (String) sessionSuivi.get(SUIVI_DOSSIER_LABEL);
            resultform = (DossierListForm) sessionSuivi.get(SUIVI_DOSSIER_LIST_FORM_KEY);
        }

        context.setNavigationContextTitle(
            new Breadcrumb(label, "/suivi/liste", Breadcrumb.TITLE_ORDER, context.getWebcontext().getRequest())
        );

        STDossier.DossierState dossierState = STDossier.DossierState.valueOf(state);

        context.putInContextData(STContextDataKey.DOSSIER_STATE, dossierState);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, resultform);
        context.putInContextData(STContextDataKey.ID, key);
        Map<String, Set<String>> mailboxMap = getEpgEspaceSuiviUIService().fillMailbox(context);

        context.putInContextData(STContextDataKey.MAILBOX_MAP, mailboxMap);
        EpgDossierList lstResults = getSolonEpgDossierListUIService().getDossiersFromByMailBoxAndState(context);
        EpgDossierListHelper.setUserColumns(
            context.getSession(),
            resultform,
            lstResults,
            Arrays.asList(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName())
        );

        List<String> additionalColumns = EpgDossierListHelper.getProviderAdditionalColumns(context);
        EpgDossierListHelper.setUserColumns(context.getSession(), resultform, lstResults, additionalColumns);

        resultform.setColumnVisibility(lstResults.getListeColonnes());

        sessionSuivi.put(SUIVI_DOSSIER_LIST_FORM_KEY, resultform);
        UserSessionHelper.putUserSessionParameter(context, SUIVI_DOSSIER_KEY, sessionSuivi);

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.TITLE, ResourceHelper.getString(dossierState.getTitle()) + " - " + label);
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());
        map.put(STTemplateConstants.NB_RESULTS, lstResults.getNbTotal());
        map.put(STTemplateConstants.RESULT_FORM, resultform);

        Map<String, Object> otherParams = FiltreUtils.initOtherParamMapWithProviderInfos(context);

        otherParams.put(CLE, key);
        otherParams.put(STATE, state);
        otherParams.put(LABEL, label);

        map.put(STTemplateConstants.OTHER_PARAMETER, otherParams);

        map.put(
            EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
            context.getActions(EpgActionCategory.SELECTION_TOOL_DOSSIERS_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.ADD_DOSSIERS_TO_FAVORIS_ACTIONS,
            context.getActions(EpgActionCategory.ADD_DOSSIERS_TO_FAVORIS_ACTIONS)
        );
        map.put(STTemplateConstants.DATA_URL, EpgURLConstants.URL_SUIVI_LISTE);
        map.put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);

        template.setData(map);

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, lstResults, template.getData());

        template.getData().put(EpgTemplateConstants.SUIVI_DOSSIER_NODE_KEY, key);

        return template;
    }

    @GET
    @Path("listes/gestion")
    public ThTemplate getGestionListes() {
        verifyAction(EpgActionEnum.SUIVI_GESTION_LISTES, "/suivi/gestion/listes");
        context.setNavigationContextTitle(
            new Breadcrumb(
                "suivi.gestionListes",
                EpgURLConstants.URL_SUIVI_LISTES_GESTION,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );

        template.setName("pages/suivi/gestion-listes");
        template.setContext(context);

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();
        map.put(
            EpgTemplateConstants.AUTRES_LISTES,
            EpgUIServiceLocator.getEpgTraitementPapierListeUIService().getListeTraitementPapierAutre(context)
        );
        map.put(
            EpgTemplateConstants.LISTES_SIGNATURE,
            EpgUIServiceLocator.getEpgTraitementPapierListeUIService().getListeTraitementPapierSignature(context)
        );

        template.setData(map);

        return template;
    }

    @GET
    @Path("liste/{id}")
    public ThTemplate getTraitementListe(@PathParam("id") String id) {
        ThTemplate template = getMyTemplate();

        context.setCurrentDocument(id);
        TraitementPapierList resultList = EpgUIServiceLocator
            .getEpgTraitementPapierListeUIService()
            .consultTraitementPapierListe(context);

        context.setNavigationContextTitle(
            new Breadcrumb(
                resultList.getTitre(),
                EpgURLConstants.URL_SUIVI_LISTE + "/" + id,
                Breadcrumb.TITLE_ORDER + 1,
                context.getWebcontext().getRequest()
            )
        );

        template.setName("pages/suivi/detail-liste");
        template.setContext(context);

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        map.put(STTemplateConstants.RESULT_LIST, resultList);
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        EpgUIServiceLocator.getEpgTraitementPapierListeUIService().initGestionListeActionContext(context);
        map.put(STTemplateConstants.GENERALE_ACTIONS, context.getActions(EpgActionCategory.SUIVI_DETAIL_LIST));
        map.put(EpgTemplateConstants.DATES_LIST, resultList.getDatesList());

        template.setData(map);

        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgSuiviTemplate();
    }
}
