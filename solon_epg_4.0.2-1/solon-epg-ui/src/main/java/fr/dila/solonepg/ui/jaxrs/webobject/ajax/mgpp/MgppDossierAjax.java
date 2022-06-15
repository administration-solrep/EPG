package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import static fr.dila.solonepg.ui.services.mgpp.impl.MgppDossierParlementaireMenuServiceImpl.ACTIVE_KEY;
import static fr.dila.solonepg.ui.th.constants.MgppTemplateConstants.ID_COMMUNICATION;

import com.google.gson.Gson;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.MessageList;
import fr.dila.solonepg.ui.bean.MgppCorbeilleContentList;
import fr.dila.solonepg.ui.bean.MgppExportable;
import fr.dila.solonepg.ui.bean.RapidSearchDTO;
import fr.dila.solonepg.ui.bean.travail.DossierCreationForm;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.exceptions.RequiredPosteSelection;
import fr.dila.solonepg.ui.jaxrs.webobject.page.mgpp.MgppRecherche;
import fr.dila.solonepg.ui.services.EpgDossierCreationUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.MessageListForm;
import fr.dila.solonepg.ui.th.bean.MgppCorbeilleContentListForm;
import fr.dila.solonepg.ui.th.constants.MgppTemplateConstants;
import fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import java.io.File;
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
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossierCommunicationAjax")
public class MgppDossierAjax extends SolonWebObject {
    private static final int TITLE_PREFIX_TO_REMOVE = 9;
    private static final String MGPP_PUBLICATION = "mgpp_publication";
    private static final String DOSSIER_SIMILAIRE_RESULT_KEY = "dossier.similaire.result.label";

    @GET
    public ThTemplate doHome(@PathParam("id") String corbeilleId) {
        return doSearchCommunication(corbeilleId, null);
    }

    @POST
    @Path("export/{extension:xls|pdf}")
    @Produces("application/vnd.ms-excel")
    public Response exportExcel(
        String id,
        @FormParam("search") String paramJsonSearch,
        @PathParam("extension") String extension
    ) {
        initContext(paramJsonSearch, id);
        MgppCorbeilleName corbeille = context.getFromContextData(MgppContextDataKey.CORBEILLE_ID);
        String jsonSearch = context.getFromContextData(MgppContextDataKey.JSON_SEARCH);
        context.putInContextData(SSContextDataKey.IS_EXPORT, true);

        MgppExportable exportableList;
        if (corbeille.isDynamic()) {
            MgppCorbeilleContentListForm msgform = new MgppCorbeilleContentListForm(jsonSearch);
            context.putInContextData(MgppContextDataKey.FICHE_LIST_FORM, msgform);
            MgppCorbeilleContentList list = MgppUIServiceLocator
                .getMgppCorbeilleUIService()
                .getFicheListForCorbeille(context);
            list.buildColonnes(msgform, corbeille);
            exportableList = list;
        } else {
            MessageListForm msgform = new MessageListForm(jsonSearch);
            context.putInContextData(MgppContextDataKey.MESSAGE_LIST_FORM, msgform);
            MessageList list = MgppUIServiceLocator.getMgppCorbeilleUIService().getMessageListForCorbeille(context);
            list.buildColonnes(msgform);
            exportableList = list;
        }

        context.putInContextData(MgppContextDataKey.EXPORT_LIST, exportableList);

        fr.dila.st.api.constant.MediaType format = fr.dila.st.api.constant.MediaType.fromExtension(extension);
        boolean isPdf = format == fr.dila.st.api.constant.MediaType.APPLICATION_PDF;
        context.putInContextData(MgppContextDataKey.IS_PDF, isPdf);

        File exportFile = MgppUIServiceLocator.getMgppDossierUIService().exportDossier(context);
        if (isPdf) {
            return FileDownloadUtils.getAttachmentPdf(exportFile, "Export_MGPP." + format.extension());
        } else {
            return FileDownloadUtils.getAttachmentXls(exportFile, "Export_MGPP." + format.extension());
        }
    }

    @POST
    @Path("recherche")
    public ThTemplate doSearchCommunication(String id, @FormParam("search") String paramJsonSearch) {
        template.setContext(context);

        initContext(paramJsonSearch, id);
        MgppCorbeilleName corbeille = context.getFromContextData(MgppContextDataKey.CORBEILLE_ID);
        String idCorbeille = corbeille.getIdCorbeille();
        HashMap<String, Object> mapSearch = context.getFromContextData(MgppContextDataKey.MAP_SEARCH);
        String jsonSearch = context.getFromContextData(MgppContextDataKey.JSON_SEARCH);
        String dossierParlementaire = context.getFromContextData(MgppContextDataKey.DOSSIERS_PARLEMENTAIRES_SELECTED);

        if (template.getData() == null) {
            Map<String, Object> map = new HashMap<>();
            template.setData(map);
        }

        String titre;
        RapidSearchDTO rapidSearchDTO = MgppUIServiceLocator.getMgppCorbeilleUIService().buildFilters(context);
        template.getData().put("dossiersParlementairesSelected", dossierParlementaire);

        if (corbeille.isDynamic()) {
            if (paramJsonSearch != null) {
                template.setName("fragments/table/tableResultatsCorbeille");
            }
            MgppCorbeilleContentListForm msgform = new MgppCorbeilleContentListForm(jsonSearch);
            context.putInContextData(MgppContextDataKey.FICHE_LIST_FORM, msgform);
            MgppCorbeilleContentList result = MgppUIServiceLocator
                .getMgppCorbeilleUIService()
                .getFicheListForCorbeille(context);

            titre = result.getTitre();
            template.getData().put(STTemplateConstants.RESULT_LIST, result);
            template.getData().put(STTemplateConstants.RESULT_FORM, msgform);
            template.getData().put(STTemplateConstants.LST_COLONNES, result.getListeColonnes(msgform, corbeille));
            template.getData().put(MgppTemplateConstants.SUBTITLE, buildSubtitle(dossierParlementaire, result));
        } else {
            if (paramJsonSearch != null) {
                template.setName("fragments/table/tableCommunications");
            }
            MessageListForm msgform = new MessageListForm(jsonSearch);
            context.putInContextData(MgppContextDataKey.MESSAGE_LIST_FORM, msgform);
            MessageList result = MgppUIServiceLocator.getMgppCorbeilleUIService().getMessageListForCorbeille(context);

            titre = result.getTitre();
            template.getData().put(STTemplateConstants.RESULT_LIST, result);
            template.getData().put(STTemplateConstants.RESULT_FORM, msgform);
            template.getData().put(STTemplateConstants.LST_COLONNES, result.getListeColonnes(msgform));
        }

        context.removeNavigationContextTitle(Breadcrumb.SUBTITLE_ORDER);
        context.setNavigationContextTitle(
            new Breadcrumb(
                titre,
                "/mgpp/dossier/" + corbeille.getIdCorbeille() + "#main_content",
                Breadcrumb.SUBTITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );

        MgppActionEnum curAction = MgppActionEnum.fromValue(dossierParlementaire, MgppActionEnum.DOSSIER_PROC_LEGIS);
        String typeParlementaire = context.getAction(curAction) == null
            ? "dossier.parlementaire.inconnu"
            : context.getAction(curAction).getLabel();

        // On renseigne la corbeille sélectionnée pour qu'elle soit surlignée
        UserSessionHelper.putUserSessionParameter(context, ACTIVE_KEY, idCorbeille);
        template.getData().put(STTemplateConstants.RESULT_VISIBLE, mapSearch != null);
        template.getData().put(STTemplateConstants.DATA_URL, "/mgpp/dossier");
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, "/ajax/mgpp/dossier/recherche");
        template.getData().put(STTemplateConstants.CORBEILLE, idCorbeille);
        template.getData().put("typeParlementaire", typeParlementaire);
        template.getData().put(STTemplateConstants.RAPID_SEARCH, rapidSearchDTO.getLstWidgets());
        template.getData().put(MgppTemplateConstants.MGPP_PRINT_ACTIONS, getListeActions(corbeille));

        return template;
    }

    private List<Action> getListeActions(MgppCorbeilleName corbeille) {
        MgppActionCategory mgppNonDynamicListPrintAction = corbeille.isDynamic()
            ? MgppActionCategory.MGPP_DYNAMIC_LIST_PRINT_ACTION
            : MgppActionCategory.MGPP_NON_DYNAMIC_LIST_PRINT_ACTION;
        return context.getActions(mgppNonDynamicListPrintAction);
    }

    private void initContext(String paramJsonSearch, String id) {
        Gson gson = new Gson();

        Map<String, Object> mapSearch = paramJsonSearch != null
            ? gson.fromJson(paramJsonSearch, Map.class)
            : new HashMap<>();

        MgppCorbeilleName corbeilleId = MgppCorbeilleName.fromValue(
            MapUtils.isNotEmpty(mapSearch) ? (String) mapSearch.get("idCorbeille") : id
        );
        String jsonSearch;
        if (paramJsonSearch != null) {
            jsonSearch = paramJsonSearch;
            UserSessionHelper.putUserSessionParameter(
                context,
                MgppRecherche.JSON_SEARCH + corbeilleId.getIdCorbeille(),
                paramJsonSearch
            );
        } else {
            jsonSearch =
                UserSessionHelper.getUserSessionParameter(
                    context,
                    MgppRecherche.JSON_SEARCH + corbeilleId.getIdCorbeille(),
                    String.class
                );
            if (jsonSearch != null) {
                mapSearch = gson.fromJson(jsonSearch, Map.class);
            }
        }

        String dossierParlementaire = SolonMgppServiceLocator
            .getCorbeilleService()
            .findDossierParlementaireForCorbeille(corbeilleId.getIdCorbeille(), context.getSession());

        context.putInContextData(MgppContextDataKey.CORBEILLE_ID, corbeilleId);
        context.putInContextData(MgppContextDataKey.MAP_SEARCH, mapSearch != null ? mapSearch : new HashMap<>());
        context.putInContextData(MgppContextDataKey.JSON_SEARCH, jsonSearch);
        context.putInContextData(MgppContextDataKey.DOSSIERS_PARLEMENTAIRES_SELECTED, dossierParlementaire);
    }

    /**
     * Construit le sous titre à partir du messages.properties si le dossier est une publication
     * ou sinon à partir du titre qui est de la forme <b>liste des XXX</b> et où il faut garder uniquement les XXX.
     *
     * @param dossierParlementaire
     * @param result
     * @return le sous titre
     */
    private String buildSubtitle(String dossierParlementaire, MgppCorbeilleContentList result) {
        String subtitle;
        if (MGPP_PUBLICATION.equals(dossierParlementaire)) {
            subtitle = ResourceHelper.getString(DOSSIER_SIMILAIRE_RESULT_KEY, result.getNbTotal());
        } else {
            subtitle =
                result.getNbTotal() +
                StringUtils.capitalize(result.getTitre().substring(TITLE_PREFIX_TO_REMOVE, result.getTitre().length()));
        }
        return subtitle;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reinitialiser")
    public Response doReinitialiser(@FormParam(value = "idCorbeille") String idCorbeille) {
        UserSessionHelper.putUserSessionParameter(context, MgppRecherche.JSON_SEARCH + idCorbeille, null);
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("{id}/{tab}")
    public Object doTab(
        @PathParam("id") String id,
        @PathParam("tab") String tab,
        @QueryParam("version") String version
    ) {
        return newObject("DossierCommunication" + StringUtils.capitalize(tab) + "Ajax", context);
    }

    @POST
    @Path("creer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response creerDossierPuisTraiter(
        @SwBeanParam DossierCreationForm dto,
        @FormParam(ID_COMMUNICATION) String idCommunication
    ) {
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

        String json = String.format("{\"isValidPoste\":%s}", isValidPoste);

        if (StringUtils.isNotBlank(idDossier)) {
            context.putInContextData(MgppContextDataKey.COMMUNICATION_ID, idCommunication);
            context.setCurrentDocument(idDossier);
            MgppUIServiceLocator.getMgppDossierUIService().rattacherDossierPuisTraiter(context);
        } else {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("mgpp.dossier.non.cree.error"));
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue(), json).build();
    }

    @POST
    @Path("rattacher")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rattacherDossierPuisTraiter(
        @FormParam("nor") String nor,
        @FormParam(ID_COMMUNICATION) String idCommunication
    ) {
        Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNORWithACL(context.getSession(), nor);
        if (dossier == null) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("mgpp.dossier.nor.non.trouve.error", nor));
        } else if (StringUtils.isNotBlank(dossier.getIdDossier())) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("mgpp.dossier.deja.rattache.error", nor));
        } else {
            context.putInContextData(MgppContextDataKey.COMMUNICATION_ID, idCommunication);
            context.setCurrentDocument(dossier.getDocument());
            MgppUIServiceLocator.getMgppDossierUIService().rattacherDossierPuisTraiter(context);
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Path("traiterSansDossier")
    @Produces(MediaType.APPLICATION_JSON)
    public Response traiterSansDossier(@FormParam(ID_COMMUNICATION) String idCommunication) {
        context.putInContextData(MgppContextDataKey.COMMUNICATION_ID, idCommunication);
        MgppUIServiceLocator.getEvenementActionService().traiterEvenement(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        ThTemplate myTemplate = new AjaxLayoutThTemplate(
            "fragments/leftblocks/mgpp-dossierParlementaireMenu",
            getMyContext()
        );
        myTemplate.setData(new HashMap<>());
        return myTemplate;
    }
}
