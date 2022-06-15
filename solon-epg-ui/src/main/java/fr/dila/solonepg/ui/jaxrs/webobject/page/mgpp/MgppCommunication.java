package fr.dila.solonepg.ui.jaxrs.webobject.page.mgpp;

import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.COMMUNICATION_ID;
import static fr.dila.st.core.util.ResourceHelper.getString;
import static fr.dila.st.ui.enums.STContextDataKey.ID;
import static fr.dila.st.ui.utils.FileDownloadUtils.getAttachmentFromStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.MgppTemplateConstants;
import fr.dila.solonepg.ui.th.model.mgpp.MgppDossierParlementaireTemplate;
import fr.dila.solonmgpp.api.constant.TypeEvenementConstants;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.MgppPieceJointeDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.core.dto.EvenementDTOImpl;
import fr.dila.solonmgpp.core.dto.MgppPieceJointeDTOImpl;
import fr.dila.solonmgpp.core.dto.PieceJointeFichierDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.ui.bean.FondDTO;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.exception.STValidationException;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.DetailCommunication;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.bean.TransmettreParMelForm;
import fr.dila.st.ui.th.bean.mapper.MapPropertyDescToWidget;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.sword.xsd.solon.epp.CompressionFichier;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "Communication")
public class MgppCommunication extends SolonWebObject {
    private static final String EVENEMENT_PJ_ERROR = "evenement.pj.error";
    private static final String COMMUNICATION_CREATE_EDIT_TEMPLATE = "pages/mgpp/createEditCommunication";
    private static final String CREER_DOSSIER_PAGE = "/mgpp/dossier/creation?idCommunication=%s#main_content";
    private static final String CREER_FICHE_AVI_PAGE =
        "/mgpp/fiche/creation/FichePresentationAVI?idCommunication=%s#main_content";
    private static final String CREER_FICHE_OEP_PAGE =
        "/mgpp/fiche/creation/FichePresentationOEP?idCommunication=%s#main_content";
    private static final String CREER_FICHE_341_PAGE =
        "/mgpp/fiche/creation/FichePresentation341?idCommunication=%s#main_content";
    private static final String TITRE_LABEL = "Modification de la communication : ";
    private static final String TITRE_COMPLETER_LABEL = "command.admin.param.tableRefEPP.completer.title";
    private static final String TITRE_RECTIFIER_LABEL = "command.admin.param.tableRefEPP.rectifier.title";
    private static final String COMPLETER = "completer";
    private static final STLogger LOGGER = STLogFactory.getLog(MgppCommunication.class);

    @GET
    public ThTemplate doHome() {
        ThTemplate template = getMyTemplate();
        template.setName("pages/espaceTravailHome");
        context.removeNavigationContextTitle();
        template.setContext(context);

        return template;
    }

    @GET
    @Path("creation")
    public ThTemplate creerCommunication(
        @QueryParam("typeEvenement") String typeEvenement,
        @QueryParam("idMessagePrecedent") String idMessagePrecedent
    ) {
        template.setName(COMMUNICATION_CREATE_EDIT_TEMPLATE);
        template.setContext(context);
        context.putInContextData(ID, idMessagePrecedent);

        if (StringUtils.isBlank(idMessagePrecedent)) {
            context.clearNavigationContext();
        }
        context.setNavigationContextTitle(
            new Breadcrumb(
                "Création communication",
                StringUtils.join(
                    "/mgpp/communication/creation?typeEvenement=",
                    typeEvenement,
                    StringUtils.isNotBlank(idMessagePrecedent) ? "&idMessagePrecedent=" + idMessagePrecedent : "",
                    "#main_content"
                ),
                Breadcrumb.SUBTITLE_ORDER + 2,
                context.getWebcontext().getRequest()
            )
        );

        context.putInContextData(MgppContextDataKey.EVENT_TYPE, typeEvenement);
        context.putInContextData(MgppContextDataKey.COMMUNICATION_ID, idMessagePrecedent);
        context.putInContextData(MapPropertyDescToWidget.IS_EDIT_MODE, true);
        EvenementDTO dto = MgppUIServiceLocator
            .getMgppEvenementDetailsUIservice()
            .initFicheDetailCommunication(context);
        context.putInContextData(MgppContextDataKey.CURRENT_EVENT, dto);
        DetailCommunication com = MgppUIServiceLocator.getMgppMetadonneeUIService().getDetailCommunication(context);

        List<Action> baseActions = context.getActions(
            idMessagePrecedent != null
                ? MgppActionCategory.BASE_COMMUNICATION_EDIT
                : MgppActionCategory.BASE_COMMUNICATION_ADD
        );
        List<Action> mainActions = context.getActions(
            typeEvenement.startsWith("ALERTE")
                ? MgppActionCategory.MAIN_COMMUNICATION_CREER_ALERTE
                : MgppActionCategory.MAIN_COMMUNICATION_CREER
        );

        FondDTO parapheurDto = new FondDTO(Boolean.TRUE);
        template.getData().put(EpgTemplateConstants.PARAPHEUR_DTO, parapheurDto);
        FondDTO fondDto = new FondDTO(Boolean.TRUE);
        template.getData().put(EpgTemplateConstants.FOND_DTO, fondDto);
        template.getData().put(STTemplateConstants.ID, idMessagePrecedent);
        template.getData().put(STTemplateConstants.TYPE_EVENEMENT, typeEvenement);
        template.getData().put(EvenementDTOImpl.VERSION_COURANTE, getVersionCouranteAsString(dto));
        template.getData().put(STTemplateConstants.TYPE_ACTION, "creer");
        template.getData().put(STTemplateConstants.LST_WIDGETS, com.getLstWidgets());
        template.getData().put(STTemplateConstants.BASE_ACTIONS, baseActions);
        template.getData().put(STTemplateConstants.MAIN_ACTIONS, mainActions);
        if (dto.getIdDossier() != null) {
            template.getData().put(STTemplateConstants.ID_DOSSIER, dto.getIdDossier());
        }
        if (context.getNavigationContext().size() > 1) {
            template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        } else {
            context.removeNavigationContextTitle();
            template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, "");
        }

        template
            .getData()
            .put(
                STTemplateConstants.TITRE,
                ResourceHelper.getString(
                    idMessagePrecedent == null
                        ? "creation.communication.title"
                        : "creation.communication.successive.title"
                )
            );

        return template;
    }

    private String getVersionCouranteAsString(EvenementDTO dto) {
        String versionCourante = "";
        try {
            versionCourante = new ObjectMapper().writeValueAsString(dto.get(EvenementDTOImpl.VERSION_COURANTE));
        } catch (JsonProcessingException e) {
            LOGGER.error(
                STLogEnumImpl.FAIL_PARSE_FONC,
                e,
                "Impossible de parser la chaîne : " + dto.get(EvenementDTOImpl.VERSION_COURANTE)
            );
        }
        return versionCourante;
    }

    @GET
    @Path("modification")
    public ThTemplate modifierCommunication(@QueryParam("id") String id, @QueryParam("dossierId") String dossierId) {
        template.setName(COMMUNICATION_CREATE_EDIT_TEMPLATE);
        template.setContext(context);

        context.putInContextData(ID, id);
        context.putInContextData(MgppContextDataKey.COMMUNICATION_ID, id);
        EvenementDTO dto = MgppUIServiceLocator.getMgppDossierUIService().getCurrentEvenementDTO(context);

        context.setNavigationContextTitle(
            new Breadcrumb(
                TITRE_LABEL + dto.getTypeEvenementLabel(),
                "/mgpp/communication/modification",
                Breadcrumb.SUBTITLE_ORDER + 2,
                context.getWebcontext().getRequest()
            )
        );

        context.putInContextData(MapPropertyDescToWidget.IS_EDIT_MODE, true);
        context.putInContextData(MgppContextDataKey.CURRENT_EVENT, dto);
        context.putInContextData(MgppContextDataKey.EVENT_TYPE, dto.getTypeEvenementName());
        DetailCommunication com = MgppUIServiceLocator.getMgppMetadonneeUIService().getDetailCommunication(context);

        List<Action> baseActions = context.getActions(MgppActionCategory.BASE_COMMUNICATION_EDIT);
        List<Action> mainActions = context.getActions(MgppActionCategory.MAIN_COMMUNICATION_EDIT);

        FondDTO parapheurDto = new FondDTO(Boolean.TRUE);
        template.getData().put(EpgTemplateConstants.PARAPHEUR_DTO, parapheurDto);
        FondDTO fondDto = new FondDTO(Boolean.TRUE);
        template.getData().put(EpgTemplateConstants.FOND_DTO, fondDto);
        template.getData().put(MgppTemplateConstants.ID_CUR, id);

        template.getData().put(STTemplateConstants.ID, dto.getIdEvenement());
        template.getData().put(STTemplateConstants.TITRE, TITRE_LABEL + dto.getTypeEvenementLabel());
        template.getData().put(STTemplateConstants.TYPE_EVENEMENT, dto.getTypeEvenementName());
        template.getData().put(EvenementDTOImpl.VERSION_COURANTE, getVersionCouranteAsString(dto));
        template.getData().put(STTemplateConstants.TYPE_ACTION, "modifier");
        template.getData().put(STTemplateConstants.LST_WIDGETS, com.getLstWidgets());
        template.getData().put(STTemplateConstants.BASE_ACTIONS, baseActions);
        template.getData().put(STTemplateConstants.MAIN_ACTIONS, mainActions);
        if (dossierId != null) {
            template.getData().put(STTemplateConstants.ID_DOSSIER, dossierId);
        }
        if (context.getNavigationContext().size() > 1) {
            template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        } else {
            context.removeNavigationContextTitle();
            template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, "");
        }

        return template;
    }

    @GET
    @Path("creerAlerte")
    public ThTemplate creerAlerte(@QueryParam("idMessagePrecedent") String idMessagePrecedent) {
        context.putInContextData(COMMUNICATION_ID, idMessagePrecedent);
        String typeAlerteSuccessive = MgppUIServiceLocator.getEvenementActionService().getTypeAlerteSuccessive(context);
        return creerCommunication(typeAlerteSuccessive, idMessagePrecedent);
    }

    @GET
    @Path("transmettreParMel")
    public ThTemplate transmettreParMel(@QueryParam("id") String id) {
        template.setName("fragments/components/transmettreParMel");
        template.setContext(context);

        context.putInContextData(COMMUNICATION_ID, id);
        TransmettreParMelForm tpmForm = MgppUIServiceLocator
            .getEvenementActionService()
            .getTransmettreParMelForm(context);

        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("action.button.label.transmettreMail"),
                "/mgpp/communication/transmettreParMel",
                Breadcrumb.SUBTITLE_ORDER + 3,
                context.getWebcontext().getRequest()
            )
        );

        List<Action> baseActions = context.getActions(MgppActionCategory.BASE_COMMUNICATION_EDIT);
        List<Action> mainActions = context.getActions(MgppActionCategory.MAIN_TRANSMETTRE_PAR_MEL);

        template.getData().put(STTemplateConstants.BASE_ACTIONS, baseActions);
        template.getData().put(STTemplateConstants.MAIN_ACTIONS, mainActions);
        template.getData().put("transmettreParMelForm", tpmForm);

        if (context.getNavigationContext().size() > 1) {
            template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        } else {
            context.removeNavigationContextTitle();
            template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, "");
        }

        return template;
    }

    @GET
    @Path("completer")
    public ThTemplate completerCommunication(@QueryParam("id") String id, @QueryParam("dossierId") String dossierId) {
        return completerRectifier(id, dossierId, COMPLETER);
    }

    @GET
    @Path("rectifier")
    public ThTemplate rectifierCommunication(@QueryParam("id") String id, @QueryParam("dossierId") String dossierId) {
        return completerRectifier(id, dossierId, "rectifier");
    }

    private ThTemplate completerRectifier(String id, String dossierId, String typeAction) {
        template.setName(COMMUNICATION_CREATE_EDIT_TEMPLATE);
        template.setContext(context);

        context.putInContextData(ID, id);

        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("action.button.label." + typeAction),
                "/mgpp/communication/" + typeAction + "?id=" + id,
                Breadcrumb.SUBTITLE_ORDER + 2,
                context.getWebcontext().getRequest()
            )
        );

        context.putInContextData(MgppContextDataKey.COMMUNICATION_ID, id);
        EvenementDTO dto = MgppUIServiceLocator.getMgppDossierUIService().getCurrentEvenementDTO(context);
        context.putInContextData(MapPropertyDescToWidget.IS_EDIT_MODE, true);
        context.putInContextData(MgppContextDataKey.CURRENT_EVENT, dto);
        context.putInContextData(MgppContextDataKey.EVENT_TYPE, dto.getTypeEvenementName());
        context.putInContextData(STContextDataKey.MODE_CREATION, typeAction);
        DetailCommunication com = MgppUIServiceLocator.getMgppMetadonneeUIService().getDetailCommunication(context);

        List<Action> baseActions = context.getActions(MgppActionCategory.BASE_COMMUNICATION_EDIT);
        List<Action> mainActions = context.getActions(MgppActionCategory.MAIN_COMMUNICATION_EDIT);
        String titreLabel = StringUtils.equals(typeAction, COMPLETER)
            ? getString(TITRE_COMPLETER_LABEL, dto.getTypeEvenementLabel())
            : getString(TITRE_RECTIFIER_LABEL, dto.getTypeEvenementLabel());

        FondDTO parapheurDto = new FondDTO(Boolean.TRUE);
        template.getData().put(EpgTemplateConstants.PARAPHEUR_DTO, parapheurDto);
        FondDTO fondDto = new FondDTO(Boolean.TRUE);
        template.getData().put(EpgTemplateConstants.FOND_DTO, fondDto);
        template.getData().put(STTemplateConstants.ID, id);

        template.getData().put(MgppTemplateConstants.ID_CUR, id);
        template.getData().put(STTemplateConstants.TYPE_EVENEMENT, dto.getTypeEvenementName());
        template.getData().put(STTemplateConstants.TITRE, titreLabel);
        template.getData().put(EvenementDTOImpl.VERSION_COURANTE, getVersionCouranteAsString(dto));
        template.getData().put(STTemplateConstants.TYPE_ACTION, typeAction);
        template.getData().put(STTemplateConstants.LST_WIDGETS, com.getLstWidgets());
        template.getData().put(STTemplateConstants.BASE_ACTIONS, baseActions);
        template.getData().put(STTemplateConstants.MAIN_ACTIONS, mainActions);
        if (dossierId != null) {
            template.getData().put(STTemplateConstants.ID_DOSSIER, dossierId);
        }
        if (context.getNavigationContext().size() > 1) {
            template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        } else {
            context.removeNavigationContextTitle();
            template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, "");
        }

        return template;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("enregistrement")
    public Response enregistrerCommunication(
        @FormParam("communication") String json,
        @FormParam("idMessage") String idMessage,
        @FormParam("publier") Boolean publier,
        @FormParam("typeAction") String typeAction,
        @FormParam("typeEvenement") String typeEvenement
    ) {
        context.putInContextData(STContextDataKey.ID, idMessage);
        context.putInContextData(MgppContextDataKey.COMMUNICATION_ID, idMessage);
        Gson gson = new Gson();
        context.putInContextData(MgppContextDataKey.MAP_EVENT, gson.fromJson(json, Map.class));
        context.putInContextData(MgppContextDataKey.PUBLIER, publier);

        context.putInContextData(MgppContextDataKey.EVENT_TYPE, typeEvenement);

        String idMessageForRedirect = idMessage;
        if ("creer".equals(typeAction)) {
            idMessageForRedirect = MgppUIServiceLocator.getEvenementActionService().creerEvenement(context);
        } else if ("rectifier".equals(typeAction)) {
            MgppUIServiceLocator.getEvenementActionService().rectifierEvenement(context);
        } else if ("completer".equals(typeAction)) {
            MgppUIServiceLocator.getEvenementActionService().completerEvenement(context);
        } else if ("modifier".equals(typeAction)) {
            MgppUIServiceLocator.getEvenementActionService().saveModifierEvenement(context);
        } else {
            throw new STValidationException("mgpp.param.invalid.action");
        }

        if (CollectionUtils.isNotEmpty(context.getMessageQueue().getSuccessQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        Map<String, String> retourSave = new HashMap<>();
        retourSave.put("idMessage", idMessageForRedirect);
        retourSave.put("app", "mgpp");
        String jsonRetour = gson.toJson(retourSave);
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue(), jsonRetour).build();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new MgppDossierParlementaireTemplate();
    }

    @POST
    @Path("buildBlocPJ")
    public ThTemplate buildBlocPJ(
        @FormParam("widget") String widget,
        @FormParam("widgetName") String widgetName,
        @FormParam("widgetLabel") String widgetLabel,
        @FormParam("multiValue") String multiValue,
        @FormParam("idDossier") String idDossier
    ) {
        template = new AjaxLayoutThTemplate("fragments/components/bloc-ajout-piece-jointe", context);
        template.setData(new HashMap<>());

        FondDTO parapheurDto = new FondDTO(Boolean.TRUE);
        template.getData().put(EpgTemplateConstants.PARAPHEUR_DTO, parapheurDto);
        FondDTO fondDto = new FondDTO(Boolean.TRUE);
        template.getData().put(EpgTemplateConstants.FOND_DTO, fondDto);

        template.getData().put("widget", widget);
        template.getData().put("widgetName", widgetName);
        template.getData().put("widgetLabel", StringUtils.removeEnd(widgetLabel, "(s)"));
        template.getData().put("multiValue", multiValue);
        if (StringUtils.isNoneBlank(idDossier)) {
            template.getData().put(STTemplateConstants.ID_DOSSIER, idDossier);
        }
        return template;
    }

    @GET
    @Path("downloadPJ")
    @Produces("application/octet-stream")
    public Response downloadFileFromEpp(
        @SwNotEmpty @QueryParam("evenementId") String evenementId,
        @SwNotEmpty @QueryParam("typePJ") String typePj,
        @SwNotEmpty @QueryParam("nomPJ") String nomPj,
        @QueryParam("compresse") Boolean compresse
    ) {
        context.putInContextData(MgppContextDataKey.COMMUNICATION_ID, evenementId);
        EvenementDTO dto = MgppUIServiceLocator.getMgppDossierUIService().getCurrentEvenementDTO(context);
        PieceJointeFichierDTO fichier = new PieceJointeFichierDTOImpl();
        fichier.setCompression((BooleanUtils.isTrue(compresse) ? CompressionFichier.ZIP : CompressionFichier.AUCUNE));
        fichier.setNomFichier(nomPj);

        MgppPieceJointeDTO conteneurFichier = new MgppPieceJointeDTOImpl();
        conteneurFichier.setType(typePj);

        SolonMgppServiceLocator
            .getPieceJointeService()
            .setContentFromEpp(fichier, dto, conteneurFichier, context.getSession());

        if (fichier.getContenu() == null) {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString(EVENEMENT_PJ_ERROR));
            return null;
        }

        return getAttachmentFromStream(getPieceJointeOS(fichier)::accept, fichier.getNomFichier());
    }

    private Consumer<OutputStream> getPieceJointeOS(PieceJointeFichierDTO fichier) {
        return outputStream -> {
            try {
                outputStream.write(fichier.getContenu());
            } catch (IOException e) {
                throw new NuxeoException(e);
            }
        };
    }

    @GET
    @Path("traiter")
    public Response doTraiter(@QueryParam("idMessage") String idMessage) {
        context.putInContextData(COMMUNICATION_ID, idMessage);
        EvenementDTO evenementDTO = MgppUIServiceLocator.getMgppDossierUIService().getCurrentEvenementDTO(context);
        String nextPage = "/mgpp/dossier/" + idMessage + "/fiche#main_content";
        boolean traiter = true;

        DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        CoreSession session = context.getSession();
        String idDossier = evenementDTO.getIdDossier();
        switch (evenementDTO.getTypeEvenementName()) {
            case TypeEvenementConstants.TYPE_EVENEMENT_EVT02:
                if (dossierService.findDossierFromIdDossier(session, idDossier) == null) {
                    nextPage = String.format(CREER_DOSSIER_PAGE, idMessage);
                    traiter = false;
                }
                break;
            case TypeEvenementConstants.TYPE_EVENEMENT_EVT34:
                if (dossierService.findFicheAVI(session, idDossier) == null) {
                    nextPage = String.format(CREER_FICHE_AVI_PAGE, idMessage);
                    traiter = false;
                }
                break;
            case TypeEvenementConstants.TYPE_EVENEMENT_EVT49:
            case TypeEvenementConstants.TYPE_EVENEMENT_EVT49_0:
            case TypeEvenementConstants.TYPE_EVENEMENT_EVT51:
                if (dossierService.findFicheOEP(session, idDossier) == null) {
                    nextPage = String.format(CREER_FICHE_OEP_PAGE, idMessage);
                    traiter = false;
                }
                break;
            case TypeEvenementConstants.TYPE_EVENEMENT_EVT39:
            case TypeEvenementConstants.TYPE_EVENEMENT_EVT40:
            case TypeEvenementConstants.TYPE_EVENEMENT_EVT43:
            case TypeEvenementConstants.TYPE_EVENEMENT_EVT43BIS:
                if (dossierService.find341(session, idDossier) == null) {
                    nextPage = String.format(CREER_FICHE_341_PAGE, idMessage);
                    traiter = false;
                }
                break;
            default:
                break;
        }
        if (traiter) {
            MgppUIServiceLocator.getEvenementActionService().traiterEvenement(context);
        }

        if (CollectionUtils.isNotEmpty(context.getMessageQueue().getSuccessQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }
        return redirect(nextPage);
    }
}
