package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import avro.shaded.com.google.common.collect.ImmutableList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import fr.dila.solonepg.api.service.vocabulary.TypeActeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.travail.DossierCreationForm;
import fr.dila.solonepg.ui.bean.travail.MesureApplicationForm;
import fr.dila.solonepg.ui.bean.travail.MesureTranspositionForm;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgColumnEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgTypeActeFragmentEnum;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.exceptions.RequiredPosteSelection;
import fr.dila.solonepg.ui.helper.EpgDossierListHelper;
import fr.dila.solonepg.ui.helper.SolonEpgProviderHelper;
import fr.dila.solonepg.ui.services.EpgDossierCreationUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.ss.ui.bean.actions.SSNavigationActionDTO;
import fr.dila.ss.ui.services.actions.SSActionsServiceLocator;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliTravailAjax")
public class EpgTravailAjax extends SolonWebObject {
    public static final String JSON_SEARCH = "jsonSearch";
    private static final String DIRECTIVE = "directive";
    private static final String LOI = "loi";
    private static final String MESURE_TRANSPOSITION = "mesureTranspositionDTO";
    private static final String URI_EUR_LEX = "https://eur-lex.europa.eu/eli/dir/%d/%d/oj";
    private static final String URI_TRANSPOSITION_DIRECTIVE = "uriTransDirective";
    private static final String RESPONSE_ID_DOSSIER = "idDossier";
    private static final String DOSSIERS_SUIVIS = "dossiersSuivis";
    private static final String ERROR_MSG_NO_FOLDER = "epg.dossiers.creation.dossier.aucun.dossier.trouve.nor";
    private static final String MAIN_TRAVAIL_TEMPLATE = "fragments/travail/mainTravail";
    private static final ImmutableMap<String, String> STATUT_MESURES_TRANSPOSITION = ImmutableMap.of(
        "true",
        "epg.dossier.creation.transposition.directive.europeenne.oui",
        "false",
        "epg.dossier.creation.transposition.directive.europeenne.non"
    );
    private static final ImmutableMap<String, String> STATUT_MESURES_APPLICATION = ImmutableMap.of(
        "loi",
        "epg.dossier.creation.application.loi.ordonnance.application.loi",
        "ordonnance",
        "epg.dossier.creation.application.loi.ordonnance.application.ordonnance",
        "false",
        "epg.dossier.creation.application.loi.ordonnance.non"
    );
    private static final ImmutableMap<String, String> STATUT_ARTICLE = ImmutableMap.of(
        "true",
        "epg.dossier.creation.ordonnance.choix.article.38",
        "false",
        "epg.dossier.creation.ordonnance.choix.article.74.1"
    );

    public EpgTravailAjax() {
        super();
    }

    @GET
    @Path("listePoste")
    public ThTemplate getListPoste(
        @QueryParam("posteId") @SwNotEmpty @SwId String posteId,
        @SwBeanParam DossierListForm dossierlistForm
    )
        throws MissingArgumentException {
        context.putInContextData(EpgContextDataKey.POSTE_ID, posteId);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        context.putInContextData(EpgContextDataKey.IS_FROM_CORBEILLE, true);
        EpgDossierList lstResults = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersFromPosteCorbeille(context);
        Map<String, Object> otherParameter = FiltreUtils.initOtherParamMapWithProviderInfos(context);
        otherParameter.put(EpgTemplateConstants.POSTE_ID, posteId);

        ThTemplate template = buildMap(
            dossierlistForm,
            lstResults,
            "listePoste",
            otherParameter,
            Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName())
        );
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);
        return template;
    }

    @GET
    @Path("listeTypeEtape")
    public ThTemplate getListeTypeEtape(
        @QueryParam("typeEtape") @SwNotEmpty @SwId String typeEtape,
        @SwBeanParam DossierListForm dossierlistForm
    )
        throws MissingArgumentException {
        context.putInContextData(SSTemplateConstants.TYPE_ETAPE, typeEtape);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        EpgDossierList lstResults = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersFromTypeEtapeCorbeille(context);
        Map<String, Object> otherParameter = new HashMap<>();
        otherParameter.put(SSTemplateConstants.TYPE_ETAPE, typeEtape);
        ThTemplate template = buildMap(
            dossierlistForm,
            lstResults,
            "listeTypeEtape",
            otherParameter,
            Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName())
        );
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);

        return template;
    }

    @GET
    @Path("listeTypeActe")
    public ThTemplate getListeTypeActe(
        @QueryParam("typeActe") @SwNotEmpty @SwId String typeActe,
        @SwBeanParam DossierListForm dossierlistForm
    )
        throws MissingArgumentException {
        context.putInContextData(EpgContextDataKey.TYPE_ACTE, typeActe);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        EpgDossierList lstResults = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersFromTypeActeCorbeille(context);
        Map<String, Object> otherParameter = new HashMap<>();
        otherParameter.put(EpgTemplateConstants.TYPE_ACTE, typeActe);
        ThTemplate template = buildMap(
            dossierlistForm,
            lstResults,
            "listeTypeActe",
            otherParameter,
            Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName())
        );
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);
        return template;
    }

    @GET
    @Path("dossierCreation")
    public ThTemplate getDossiersEnCreation(@SwBeanParam DossierListForm dossierlistForm) {
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        EpgDossierList lstResults = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersFromEnCoursDeCreationCorbeille(context);

        return buildMap(
            dossierlistForm,
            lstResults,
            "dossierCreation",
            null,
            ImmutableList.of(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName(), EpgColumnEnum.COMPLET.getNxPropName())
        );
    }

    @GET
    @Path("dossiersCrees")
    public ThTemplate getListDossiersCrees(@SwBeanParam DossierListForm dossierlistForm) {
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        EpgDossierList lstResults = EpgUIServiceLocator.getSolonEpgDossierListUIService().getDossiersFromCrees(context);
        context.putInContextData(EpgContextDataKey.IS_DOSSIER_SUIVI, true);

        return buildMap(
            dossierlistForm,
            lstResults,
            "dossiersCrees",
            null,
            Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName())
        );
    }

    @GET
    @Path("dossiersATraiter")
    public ThTemplate getListDossiersATraiter(@SwBeanParam DossierListForm dossierListForm) {
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierListForm);
        EpgDossierList lstResults = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersFromATraiter(context);

        return buildMap(
            dossierListForm,
            lstResults,
            "dossiersATraiter",
            null,
            Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName())
        );
    }

    @GET
    @Path("dossiersSuivis")
    public ThTemplate getListDossiersSuivis(@SwBeanParam DossierListForm dossierlistForm) {
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);
        EpgDossierList lstResults = EpgUIServiceLocator.getSolonEpgDossierListUIService().getDossiersFromSuivi(context);
        context.putInContextData(EpgContextDataKey.IS_DOSSIER_SUIVI, true);

        return buildMap(
            dossierlistForm,
            lstResults,
            DOSSIERS_SUIVIS,
            null,
            Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName())
        );
    }

    @POST
    @Path("dossiersSuivis/remove")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFromDossiersSuivis(@SwRequired @FormParam("idDossiers[]") List<String> idDossiers) {
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idDossiers);
        EpgUIServiceLocator.getSolonEpgDossierListUIService().removeDossiersFromDossiersSuivis(context);
        addMessageQueueInSession();
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Path("dossiersSuivis/empty")
    @Produces(MediaType.APPLICATION_JSON)
    public Response emptyDossiersSuivis() {
        EpgUIServiceLocator.getSolonEpgDossierListUIService().emptyDossiersSuivis(context);
        addMessageQueueInSession();
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @GET
    @Path("historiqueValidation")
    public ThTemplate getListHistoriqueValidation(@SwBeanParam DossierListForm dossierListForm) {
        return getHistoriqueValidationTemplate(dossierListForm);
    }

    private ThTemplate getHistoriqueValidationTemplate(DossierListForm dossierListForm) {
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierListForm);
        EpgDossierList lstResults = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .getDossiersFromHistoriqueValidation(context);

        return buildMap(
            dossierListForm,
            lstResults,
            "historiqueValidation",
            null,
            Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName())
        );
    }

    private ThTemplate buildMap(
        DossierListForm dossierlistForm,
        EpgDossierList lstResults,
        String url,
        Map<String, Object> otherParameter,
        List<String> lstUserColonnes
    ) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/table/tableDossiers", context);
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

        template.setData(map);
        return template;
    }

    @GET
    @Path("suggestions")
    public String getSuggestions(
        @QueryParam("input") String input,
        @QueryParam("isCreation") Boolean isCreation,
        @QueryParam("isLoi") Boolean isLoi
    )
        throws JsonProcessingException {
        TypeActeService typeActeService = SolonEpgServiceLocator.getTypeActeService();
        List<String> suggestions = typeActeService.getTypeActesSuggestion(
            context.getSession(),
            input,
            isCreation,
            isLoi
        );

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(suggestions);
    }

    @POST
    @Path("appliquer")
    public ThTemplate updateFormFromTypeActe(@SwRequired @FormParam("typeActe") String typeActe) {
        String val = StringUtils.lowerCase(StringUtils.deleteWhitespace(StringUtils.stripAccents(typeActe)));

        EpgTypeActeFragmentEnum typeActeFragment = EpgTypeActeFragmentEnum.startsWithFamilleTypeActe(val);

        ThTemplate template = new AjaxLayoutThTemplate(typeActeFragment.getFragment(), context);
        Map<String, Object> map = new HashMap<>();
        MesureTranspositionForm mesureTransposition = new MesureTranspositionForm();
        map.put(MESURE_TRANSPOSITION, mesureTransposition);
        map.put("mesureTranspositionOption", STATUT_MESURES_TRANSPOSITION);
        map.put("mesureApplicationOption", STATUT_MESURES_APPLICATION);
        map.put("articleOption", STATUT_ARTICLE);
        map.put(EpgTemplateConstants.FAMILLE_ACTE, typeActeFragment.getFamilleTypeActe());

        template.setData(map);

        return template;
    }

    @POST
    @Path("transposition")
    public ThTemplate addDecretMesureTransposition(@SwBeanParam MesureTranspositionForm dto) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/travail/directive", context);

        Map<String, Object> map = new HashMap<>();
        map.put(DIRECTIVE, dto);
        map.put(URI_TRANSPOSITION_DIRECTIVE, String.format(URI_EUR_LEX, dto.getAnnee(), dto.getReference()));
        template.setData(map);

        return template;
    }

    @POST
    @Path("application")
    public ThTemplate addDecretMesureApplication(@SwBeanParam MesureApplicationForm dto) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/travail/loi", context);

        Map<String, Object> map = new HashMap<>();
        map.put(LOI, dto);
        template.setData(map);

        return template;
    }

    @POST
    @Path("creer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFolder(@SwBeanParam DossierCreationForm dto) {
        String idDossier = "";
        boolean isValidPoste = true;

        try {
            context.putInContextData(EpgContextDataKey.DOSSIER_CREATION_FORM, dto);
            EpgDossierCreationUIService epgDossierCreationUIService = EpgUIServiceLocator.getEpgDossierCreationUIService();
            idDossier = epgDossierCreationUIService.creerDossier(context);
            UserSessionHelper.putUserSessionParameter(context, "DOSSIER_CREATION", dto);
        } catch (RequiredPosteSelection e) {
            isValidPoste = false;
        }

        Map<String, Object> result = new HashMap<>();
        result.put(RESPONSE_ID_DOSSIER, idDossier);
        result.put("isValidPoste", isValidPoste);
        result.put("isRectificatif", StringUtils.isNotBlank(dto.getNorRectificatif()));

        Gson gson = new Gson();
        String json = gson.toJson(result);
        return getJsonDataResponseWithMessagesInSessionIfSuccess(json);
    }

    @POST
    @Path("rectificatif")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rectificatif(@SwRequired @FormParam("nor") String nor) {
        String idDossier = SolonEpgServiceLocator.getNORService().getDossierIdFromNOR(context.getSession(), nor);
        if (idDossier == null) {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString(ERROR_MSG_NO_FOLDER));
        }

        Map<String, Object> result = new HashMap<>();
        result.put(RESPONSE_ID_DOSSIER, idDossier);

        Gson gson = new Gson();
        String json = gson.toJson(result);
        // on met la valeur en session pr afficher le bouton "Créer le dossier" lors de l'affichage du dossier
        UserSessionHelper.putUserSessionParameter(context, EpgUserSessionKey.RECTIFICATIF, true);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue(), json).build();
    }

    @POST
    @Path("creer/liste/signature")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createListSignature(@SwRequired @FormParam("idDossiers[]") List<String> idDossiers) {
        context.putInContextData(EpgContextDataKey.IS_FROM_CORBEILLE, true);
        verifyAction(EpgActionEnum.CREATE_LIST_MISE_EN_SIGNATURE_MAILBOX, "/travail/creer/liste/signature");
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idDossiers);
        SolonEpgActionServiceLocator.getEpgDossierActionService().createListeSignatureDossiers(context);

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put(EpgTemplateConstants.ID_LISTE, context.getFromContextData(EpgContextDataKey.ID_LISTE));

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue(), (Serializable) jsonData).build();
    }

    @POST
    @Path("listePoste")
    public ThTemplate refreshListePoste(@FormParam("posteId") @SwNotEmpty @SwId String posteId) {
        context.putInContextData(EpgContextDataKey.POSTE_ID, posteId);
        context.putInContextData(EpgContextDataKey.IS_FROM_CORBEILLE, true);

        return refreshTravail(JSON_SEARCH + posteId, "listePoste", posteId, null, null);
    }

    @POST
    @Path("listeTypeEtape")
    public ThTemplate getListeTypeEtape(@FormParam("typeEtape") @SwNotEmpty @SwId String typeEtape) {
        context.putInContextData(SSTemplateConstants.TYPE_ETAPE, typeEtape);

        return refreshTravail(JSON_SEARCH + typeEtape, "listeTypeEtape", null, typeEtape, null);
    }

    @POST
    @Path("listeTypeActe")
    public ThTemplate getListeTypeActe(@FormParam("typeActe") @SwNotEmpty @SwId String typeActe) {
        context.putInContextData(EpgContextDataKey.TYPE_ACTE, typeActe);

        return refreshTravail(JSON_SEARCH + typeActe, "listeTypeActe", null, null, typeActe);
    }

    @POST
    @Path("dossierCreation")
    public ThTemplate getListPost() {
        return refreshTravail(
            EpgTemplateConstants.JSON_SEARCH_DOSSIER_EN_COURS_DE_CREATION,
            "dossierCreation",
            null,
            null,
            null
        );
    }

    @POST
    @Path("dossiersCrees")
    public ThTemplate getListDossiersCreesPost() {
        return refreshTravail(EpgTemplateConstants.JSON_SEARCH_DOSSIERS_CREES, "dossiersCrees", null, null, null);
    }

    @POST
    @Path("dossiersATraiter")
    public ThTemplate getListDossiersATraiterPost() {
        return refreshTravail(
            EpgTemplateConstants.JSON_SEARCH_DOSSIERS_A_TRAITER,
            "dossiersATraiter",
            null,
            null,
            null
        );
    }

    @POST
    @Path("dossiersSuivis")
    public ThTemplate getListDossiersSuivisPost() {
        return refreshTravail(EpgTemplateConstants.JSON_SEARCH_DOSSIERS_SUIVIS, DOSSIERS_SUIVIS, null, null, null);
    }

    @POST
    @Path("historiqueValidation")
    public ThTemplate getListHistoriqueValidationRefresh() {
        return refreshTravail(
            EpgTemplateConstants.JSON_SEARCH_HISTORIQUE_VALIDATION,
            "historiqueValidation",
            null,
            null,
            null
        );
    }

    private ThTemplate refreshTravail(
        String templateConstant,
        String url,
        String posteId,
        String typeEtape,
        String typeActe
    ) {
        String jsonSearch = null;
        Gson gson = new Gson();
        if (UserSessionHelper.getUserSessionParameter(context, templateConstant, String.class) != null) {
            jsonSearch = UserSessionHelper.getUserSessionParameter(context, templateConstant, String.class);
        }

        @SuppressWarnings("unchecked")
        Map<String, Serializable> mapSearch = jsonSearch != null
            ? gson.fromJson(jsonSearch, Map.class)
            : new HashMap<>();
        context.putInContextData(MgppContextDataKey.MAP_SEARCH, mapSearch);
        DossierListForm dossierlistForm = new DossierListForm(jsonSearch);
        context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierlistForm);

        if (DOSSIERS_SUIVIS.equals(url)) {
            context.putInContextData(EpgContextDataKey.IS_DOSSIER_SUIVI, true);
        }

        EpgDossierList lstResults = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .applyFilters(jsonSearch, dossierlistForm, mapSearch, context);

        Map<String, Object> otherParameter = FiltreUtils.initOtherParamMapWithProviderInfos(context);
        if (posteId != null) {
            otherParameter.put(EpgTemplateConstants.POSTE_ID, posteId);
        } else if (typeEtape != null) {
            otherParameter.put(SSTemplateConstants.TYPE_ETAPE, typeEtape);
        } else if (typeActe != null) {
            otherParameter.put(EpgTemplateConstants.TYPE_ACTE, typeActe);
        }
        String listTitle = (String) otherParameter.get(EpgContextDataKey.PROVIDER_TITLE.getName());
        lstResults.setTitre(listTitle);

        template = buildMap(dossierlistForm, lstResults, url, otherParameter, getAdditionalColumns());

        template.setName(MAIN_TRAVAIL_TEMPLATE);
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);
        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, lstResults, template.getData());

        return template;
    }

    private List<String> getAdditionalColumns() {
        String providerName = context.getFromContextData(EpgContextDataKey.PROVIDER_NAME);
        return SolonEpgProviderHelper.getAdditionalColumns(providerName);
    }
}
