package fr.dila.solonepg.ui.jaxrs.webobject.page;

import static fr.dila.solonepg.ui.enums.EpgActionCategory.ATTENTE_SIGNATURE_ONGLET;
import static fr.dila.ss.ui.services.impl.SSMailboxListComponentServiceImpl.ACTIVE_KEY;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgOngletAttenteSignatureEnum;
import fr.dila.solonepg.ui.helper.EpgDossierListHelper;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.EpgTravailTemplate;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.ss.ui.bean.actions.SSNavigationActionDTO;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.actions.SSActionsServiceLocator;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.bean.PaginationForm;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.commons.cli.MissingArgumentException;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliTravail")
public class EpgTravail extends SolonWebObject {
    private static final String RESULTS_TEMPLATE_NAME = "pages/travail/listeDossiersCorbeille";
    public static final String DOSSIERS_A_TRAITER_LABEL = "menu.suivi.dossiers.atraiter.title";

    public EpgTravail() {
        super();
    }

    @GET
    public ThTemplate getHome() {
        template.setName("pages/espaceTravailHome");
        context.removeNavigationContextTitle();
        context.setContextData(new HashMap<>());
        template.setContext(context);

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.IS_FROM_TRAVAIL, true);
        template.setData(map);
        return template;
    }

    @GET
    @Path("listePoste")
    public Object getListPoste(@QueryParam("posteId") @SwNotEmpty @SwId String posteId)
        throws MissingArgumentException {
        DossierListForm dossierlistForm = newFormSortedByDateArriveeDesc();
        context.putInContextData(EpgContextDataKey.POSTE_ID, posteId);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        context.putInContextData(EpgContextDataKey.IS_FROM_CORBEILLE, true);
        setActiveKey(posteId);
        template.setName(RESULTS_TEMPLATE_NAME);
        template.setContext(context);
        EpgDossierList lstResults = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersFromPosteCorbeille(context);
        Map<String, Object> otherParameter = FiltreUtils.initOtherParamMapWithProviderInfos(context);
        otherParameter.put(EpgTemplateConstants.POSTE_ID, posteId);

        template.setData(
            buildMap(
                dossierlistForm,
                lstResults,
                "listePoste",
                otherParameter,
                EpgDossierListHelper.getProviderAdditionalColumns(context)
            )
        );

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, lstResults, template.getData());

        PosteNode poste = STServiceLocator
            .getOrganigrammeService()
            .getOrganigrammeNodeById(posteId, OrganigrammeType.POSTE);

        template
            .getContext()
            .setNavigationContextTitle(
                new Breadcrumb(
                    String.format("Corbeille du poste : %s", poste.getLabel()),
                    "/travail/listePoste",
                    Breadcrumb.TITLE_ORDER,
                    template.getContext().getWebcontext().getRequest()
                )
            );
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);
        return template;
    }

    private void setActiveKey(String key) {
        context.putInContextData(ACTIVE_KEY, key);
        UserSessionHelper.putUserSessionParameter(context, ACTIVE_KEY, key);
    }

    @GET
    @Path("listeTypeEtape")
    public Object getListeTypeEtape(@QueryParam("typeEtape") @SwNotEmpty @SwId String typeEtape)
        throws MissingArgumentException {
        DossierListForm dossierlistForm = newFormSortedByDateArriveeDesc();
        context.putInContextData(SSTemplateConstants.TYPE_ETAPE, typeEtape);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        setActiveKey(typeEtape);
        template.setName(RESULTS_TEMPLATE_NAME);
        template.setContext(context);
        EpgDossierList lstResults = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersFromTypeEtapeCorbeille(context);
        Map<String, Object> otherParameter = FiltreUtils.initOtherParamMapWithProviderInfos(context);
        otherParameter.put(SSTemplateConstants.TYPE_ETAPE, typeEtape);

        template.setData(
            buildMap(
                dossierlistForm,
                lstResults,
                "listeTypeEtape",
                otherParameter,
                EpgDossierListHelper.getProviderAdditionalColumns(context)
            )
        );

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, lstResults, template.getData());

        String etapeLabel = STServiceLocator
            .getVocabularyService()
            .getEntryLabel(STVocabularyConstants.ROUTING_TASK_TYPE_VOCABULARY, typeEtape);

        template
            .getContext()
            .setNavigationContextTitle(
                new Breadcrumb(
                    String.format("Corbeille par type d'étape : %s", etapeLabel),
                    "/travail/listeTypeEtape",
                    Breadcrumb.TITLE_ORDER,
                    template.getContext().getWebcontext().getRequest()
                )
            );
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);
        return template;
    }

    @GET
    @Path("listeTypeActe")
    public Object getListeTypeActe(@QueryParam("typeActe") @SwNotEmpty @SwId String typeActe)
        throws MissingArgumentException {
        DossierListForm dossierlistForm = newFormSortedByDateArriveeDesc();
        context.putInContextData(EpgContextDataKey.TYPE_ACTE, typeActe);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        setActiveKey(typeActe);
        template.setName(RESULTS_TEMPLATE_NAME);
        template.setContext(context);
        EpgDossierList lstResults = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersFromTypeActeCorbeille(context);
        Map<String, Object> otherParameter = FiltreUtils.initOtherParamMapWithProviderInfos(context);
        otherParameter.put(EpgTemplateConstants.TYPE_ACTE, typeActe);

        template.setData(
            buildMap(
                dossierlistForm,
                lstResults,
                "listeTypeActe",
                otherParameter,
                EpgDossierListHelper.getProviderAdditionalColumns(context)
            )
        );

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, lstResults, template.getData());

        String typeActeLabel = STServiceLocator
            .getVocabularyService()
            .getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY, typeActe);

        template
            .getContext()
            .setNavigationContextTitle(
                new Breadcrumb(
                    String.format("Corbeille par type d'acte : %s", typeActeLabel),
                    "/travail/listeTypeActe",
                    Breadcrumb.TITLE_ORDER,
                    template.getContext().getWebcontext().getRequest()
                )
            );
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);
        return template;
    }

    @GET
    @Path("dossierCreation")
    public Object getDossiersEnCreation() {
        DossierListForm form = DossierListForm.newFormSortedByCreatedDateDesc();

        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, form);
        template.setName(RESULTS_TEMPLATE_NAME);
        template.setContext(context);

        EpgDossierList lstResults = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersFromEnCoursDeCreationCorbeille(context);

        Map<String, Object> otherParameter = FiltreUtils.initOtherParamMapWithProviderInfos(context);

        template.setData(
            buildMap(
                form,
                lstResults,
                "dossierCreation",
                otherParameter,
                EpgDossierListHelper.getProviderAdditionalColumns(context)
            )
        );

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, lstResults, template.getData());

        template
            .getContext()
            .setNavigationContextTitle(
                new Breadcrumb(
                    "Dossier en cours de création",
                    "/travail/dossierCreation",
                    Breadcrumb.TITLE_ORDER,
                    template.getContext().getWebcontext().getRequest()
                )
            );

        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);

        return template;
    }

    @GET
    @Path("dossiersCrees")
    public Object getDossiersCrees(@SwBeanParam DossierListForm dossierlistForm) {
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        template.setName(RESULTS_TEMPLATE_NAME);
        template.setContext(context);

        EpgDossierList lstResults = EpgUIServiceLocator.getSolonEpgDossierListUIService().getDossiersFromCrees(context);

        Map<String, Object> otherParameter = FiltreUtils.initOtherParamMapWithProviderInfos(context);

        template.setData(
            buildMap(
                dossierlistForm,
                lstResults,
                "dossiersCrees",
                otherParameter,
                EpgDossierListHelper.getProviderAdditionalColumns(context)
            )
        );

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, lstResults, template.getData());

        template
            .getContext()
            .setNavigationContextTitle(
                new Breadcrumb(
                    "Dossiers créés",
                    "/travail/dossiersCrees",
                    Breadcrumb.TITLE_ORDER,
                    template.getContext().getWebcontext().getRequest()
                )
            );

        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);

        return template;
    }

    @GET
    @Path("dossiersSuivis")
    public Object getDossiersSuivi(@SwBeanParam DossierListForm dossierlistForm) {
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        template.setName(RESULTS_TEMPLATE_NAME);
        template.setContext(context);
        EpgDossierList lstResults = EpgUIServiceLocator.getSolonEpgDossierListUIService().getDossiersFromSuivi(context);
        context.putInContextData(EpgContextDataKey.IS_DOSSIER_SUIVI, true);

        Map<String, Object> otherParameter = FiltreUtils.initOtherParamMapWithProviderInfos(context);

        template.setData(
            buildMap(
                dossierlistForm,
                lstResults,
                "dossiersSuivis",
                otherParameter,
                EpgDossierListHelper.getProviderAdditionalColumns(context)
            )
        );

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, lstResults, template.getData());

        template
            .getContext()
            .setNavigationContextTitle(
                new Breadcrumb(
                    "Dossiers suivis",
                    "/travail/dossiersSuivis",
                    Breadcrumb.TITLE_ORDER,
                    template.getContext().getWebcontext().getRequest()
                )
            );

        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);

        return template;
    }

    @GET
    @Path("dossiersATraiter")
    public Object getDossiersATraiter(@SwBeanParam DossierListForm dossierlistForm) {
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        template.setName(RESULTS_TEMPLATE_NAME);
        template.setContext(context);

        EpgDossierList lstResults = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersFromATraiter(context);

        Map<String, Object> otherParameter = FiltreUtils.initOtherParamMapWithProviderInfos(context);

        template.setData(
            buildMap(
                dossierlistForm,
                lstResults,
                "dossiersATraiter",
                otherParameter,
                EpgDossierListHelper.getProviderAdditionalColumns(context)
            )
        );

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, lstResults, template.getData());

        template
            .getContext()
            .setNavigationContextTitle(
                new Breadcrumb(
                    ResourceHelper.getString(DOSSIERS_A_TRAITER_LABEL),
                    "/travail/dossiersATraiter",
                    Breadcrumb.TITLE_ORDER,
                    template.getContext().getWebcontext().getRequest()
                )
            );

        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);

        return template;
    }

    @GET
    @Path("historiqueValidation")
    public Object getHistoriqueValidation(@SwBeanParam DossierListForm dossierlistForm) {
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        template.setName(RESULTS_TEMPLATE_NAME);
        template.setContext(context);

        EpgDossierList lstResults = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersFromHistoriqueValidation(context);

        Map<String, Object> otherParameter = FiltreUtils.initOtherParamMapWithProviderInfos(context);

        template.setData(
            buildMap(
                dossierlistForm,
                lstResults,
                "historiqueValidation",
                otherParameter,
                EpgDossierListHelper.getProviderAdditionalColumns(context)
            )
        );

        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, lstResults, template.getData());

        template
            .getContext()
            .setNavigationContextTitle(
                new Breadcrumb(
                    "Historique de validation",
                    "/travail/historiqueValidation",
                    Breadcrumb.TITLE_ORDER,
                    template.getContext().getWebcontext().getRequest()
                )
            );

        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);

        return template;
    }

    private Map<String, Object> buildMap(
        DossierListForm dossierlistForm,
        EpgDossierList lstResults,
        String url,
        Map<String, Object> otherParameter,
        List<String> lstUserColonnes
    ) {
        dossierlistForm = ObjectHelper.requireNonNullElseGet(dossierlistForm, DossierListForm::newForm);
        EpgDossierListHelper.setUserColumns(context.getSession(), dossierlistForm, lstResults, lstUserColonnes);

        Map<String, Object> map = new HashMap<>();
        SSNavigationActionDTO navigationActionDTO = new SSNavigationActionDTO();
        navigationActionDTO.setIsFromEspaceTravail(
            SSActionsServiceLocator.getNavigationActionService().isFromEspaceTravail(context)
        );

        map.put(SSTemplateConstants.NAVIGATION_ACTIONS, navigationActionDTO);
        map.put(STTemplateConstants.IS_FROM_TRAVAIL, true);
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, lstResults.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, lstResults.getListeSortableAndVisibleColonnes());
        map.put(STTemplateConstants.RESULT_FORM, dossierlistForm);
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.NB_RESULTS, lstResults.getNbTotal());
        map.put(STTemplateConstants.TITRE, lstResults.getTitre());
        map.put(STTemplateConstants.SOUS_TITRE, lstResults.getSousTitre());
        map.put(STTemplateConstants.DISPLAY_TABLE, !lstResults.getListe().isEmpty());

        FiltreUtils.buildActions(map, context);

        map.put(STTemplateConstants.DATA_URL, "/travail/" + url);
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/travail/" + url);
        map.put(STTemplateConstants.OTHER_PARAMETER, otherParameter);

        return map;
    }

    @Path("attenteSignature/{tab}")
    public Object getAttenteSignature(@PathParam("tab") String tab, @SwBeanParam PaginationForm form) {
        template.setName("pages/travail/attenteSignature");
        template.setContext(context);

        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("epg.sidebar.textes.attente.signature.title"),
                "/travail/attenteSignature/" + tab,
                Breadcrumb.TITLE_ORDER
            )
        );

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.MY_TABS, OngletConteneur.actionsToTabs(context, ATTENTE_SIGNATURE_ONGLET, tab));
        template.setData(map);

        return newObject("AttenteSignatureAjax", context, template);
    }

    @GET
    @Path("attenteSignature/telecharger/pdf")
    @Produces("application/pdf")
    public Response telechargerAttenteSignaturePdf(@QueryParam("tab") String tab) {
        EpgOngletAttenteSignatureEnum onglet = EpgOngletAttenteSignatureEnum.valueOf(tab);
        context.putInContextData(SSContextDataKey.BIRT_OUTPUT_FORMAT, BirtOutputFormat.PDF);
        context.putInContextData(EpgContextDataKey.TYPE_ATTENTE_SIGNATURE, onglet.getId());
        File file = EpgUIServiceLocator.getEpgAttenteSignatureUIService().exportTextesAttenteSignature(context);
        return FileDownloadUtils.getInlinePdf(file);
    }

    @GET
    @Path("attenteSignature/telecharger/xls")
    @Produces("application/vnd.ms-excel")
    public Response telechargerAttenteSignatureExcel(@QueryParam("tab") String tab) {
        EpgOngletAttenteSignatureEnum onglet = EpgOngletAttenteSignatureEnum.valueOf(tab);
        context.putInContextData(EpgContextDataKey.TYPE_ATTENTE_SIGNATURE, onglet.getId());
        File file = EpgUIServiceLocator.getEpgAttenteSignatureUIService().exportTextesAttenteSignature(context);
        return FileDownloadUtils.getAttachmentXls(file, "attente_signature.xls");
    }

    @Path("dossier")
    public Object getTravailDossier() {
        return newObject("AppliTravailDossier", context, template);
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgTravailTemplate();
    }

    private static DossierListForm newFormSortedByDateArriveeDesc() {
        DossierListForm form = new DossierListForm();
        form.setDateArriveeDossierLink(SortOrder.DESC);
        form.setDateArriveeDossierLinkOrder(1);
        return form;
    }
}
