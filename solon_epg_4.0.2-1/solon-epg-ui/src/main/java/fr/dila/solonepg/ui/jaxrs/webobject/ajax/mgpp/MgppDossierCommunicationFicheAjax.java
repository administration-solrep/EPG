package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.COMMUNICATION_ID;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.DOSSIER_COMMUNICATION_CONSULTATION_FICHE;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.EVENEMENT_DTO;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.GERER_FICHE_PRESENTATION;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.VERSION_ID;
import static fr.dila.st.ui.enums.STContextDataKey.DOSSIER_ID;
import static fr.dila.st.ui.enums.STContextDataKey.FILE_CONTENT;
import static fr.dila.st.ui.enums.STContextDataKey.FILE_DETAILS;
import static fr.dila.st.ui.enums.STContextDataKey.ID;
import static java.util.Optional.ofNullable;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import fr.dila.solonepg.ui.bean.MgppDossierCommunicationConsultationFiche;
import fr.dila.solonepg.ui.bean.MgppMessageDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppUserSessionKey;
import fr.dila.solonepg.ui.services.mgpp.MgppEvenementDetailsUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppFicheUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.MgppRepresentantForm;
import fr.dila.solonepg.ui.th.bean.MgppRepresentantTableForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.constants.MgppTemplateConstants;
import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.RepresentantAUD;
import fr.dila.solonmgpp.api.domain.RepresentantAVI;
import fr.dila.solonmgpp.api.domain.RepresentantOEP;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.solonmgpp.core.domain.RepresentantAUDImpl;
import fr.dila.solonmgpp.core.domain.RepresentantOEPImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.bean.SelectValueGroupDTO;
import fr.dila.st.ui.bean.VersionSelectDTO;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.enums.AlertType;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossierCommunicationFicheAjax")
public class MgppDossierCommunicationFicheAjax extends SolonWebObject {
    private static final STLogger LOGGER = STLogFactory.getLog(MgppDossierCommunicationFicheAjax.class);

    private static final List<String> ALWAYS_AVAILABLE_ACTION = ImmutableList.of(
        SolonMgppActionConstant.VERSION_ACTION_METTRE_EN_ATTENTE,
        SolonMgppActionConstant.VERSION_ACTION_RELANCER_MESSAGE,
        SolonMgppActionConstant.VERSION_LIAISON_OEP
    );

    private static final String FICHE_ID = "ficheId";
    private static final String MESSAGE = "message";
    private static final String ACCEPTER_ANNULATION_MESSAGE =
        "communication.action.accepter.annulation.confirm.message";
    private static final String REJETER_ANNULATION_MESSAGE = "communication.action.rejeter.annulation.confirm.message";
    private static final String ABANDONNER_ANNULATION_MESSAGE =
        "communication.action.abandonner.annulation.confirm.message";
    private static final String DOCUMENT_FILE = "documentFile";
    private static final String MSG_ERREUR_EMPTY_FILE = "dossier.add.empty.file.error";

    public MgppDossierCommunicationFicheAjax() {
        super();
    }

    @GET
    public ThTemplate getFiche(
        @PathParam("id") String id,
        @PathParam("tab") String tab,
        @QueryParam("version") String version
    ) {
        if (template.getData() == null) {
            Map<String, Object> map = new HashMap<>();
            template.setData(map);
        }
        context.putInContextData(COMMUNICATION_ID, id);

        context.putInContextData(VERSION_ID, version);
        // Récupération de l'évènement mis à jour depuis EPP.
        context.putInContextData(GERER_FICHE_PRESENTATION, true);
        EvenementDTO evenementDTO = MgppUIServiceLocator.getMgppDossierUIService().getFreshCurrentEvenementDTO(context);
        context.putInContextData(
            DOSSIER_ID,
            Optional.ofNullable(evenementDTO).map(EvenementDTO::getIdDossier).orElse(id)
        );

        boolean isFicheVerrouillee = false;
        if (context.getCurrentDocument() != null) {
            isFicheVerrouillee =
                LockUtils.isLockedByCurrentUser(context.getSession(), context.getCurrentDocument().getRef());
        }

        context.putInContextData(MgppContextDataKey.IS_FICHE_VERROUILLEE, isFicheVerrouillee);

        context.putInContextData(EVENEMENT_DTO, evenementDTO);

        MgppEvenementDetailsUIService evenementDetailsUIService = MgppUIServiceLocator.getMgppEvenementDetailsUIservice();
        MgppDossierCommunicationConsultationFiche fiche = evenementDetailsUIService.getDetails(context);

        context.putInContextData(DOSSIER_COMMUNICATION_CONSULTATION_FICHE, fiche);
        context.putInContextData(
            DOSSIER_ID,
            Optional.ofNullable(evenementDTO).map(EvenementDTO::getIdDossier).orElse(id)
        );
        MgppFicheUIService ficheUIService = MgppUIServiceLocator.getMgppFicheUIService();
        ficheUIService.remplirFicheDossier(context);

        MgppMessageDTO curMessage = MgppUIServiceLocator.getMgppDossierUIService().getCurrentMessageDTO(context);

        context.putInContextData(MgppContextDataKey.IS_EN_ATTENTE, curMessage.isEnAttente());
        context.putInContextData(
            MgppContextDataKey.PEUT_METTRE_EN_ATTENTE,
            evenementDetailsUIService.peutMettreEnAttente(context)
        );
        context.putInContextData(MgppContextDataKey.PEUT_LIER_UN_OEP, evenementDetailsUIService.peutLierUnOEP(context));

        if (isFicheVerrouillee) {
            context.putInContextData(MgppContextDataKey.EVENT_TYPE, evenementDTO.getTypeEvenementName());
            List<SelectValueDTO> courrierSelect = MgppUIServiceLocator
                .getModeleCourrierUIService()
                .getAvailableModels(context);
            template.getData().put("courrierSelect", courrierSelect);
            context.putInContextData(
                MgppContextDataKey.IS_FICHE_SUPPRIMABLE,
                ficheUIService.isFicheSupprimable(context)
            );
            template
                .getData()
                .put(
                    MgppTemplateConstants.MGPP_GENERALE_ACTIONS,
                    context.getActions(MgppActionCategory.COMMUNICATION_FICHE_EDIT_ACTION)
                );
        } else {
            template
                .getData()
                .put(
                    MgppTemplateConstants.MGPP_GENERALE_ACTIONS,
                    context.getActions(MgppActionCategory.COMMUNICATION_FICHE_ACTION)
                );
        }
        template
            .getData()
            .put(
                MgppTemplateConstants.MGPP_PRINT_ACTIONS,
                context.getActions(MgppActionCategory.COMMUNICATION_FICHE_PRINT_ACTION)
            );

        fiche.setCurVersion(evenementDTO.getVersionCourante());
        fiche.setLstVersions(evenementDetailsUIService.getListVersions(context));
        fiche.setNatureVersion(evenementDetailsUIService.getNatureVersion(context));

        List<String> actionsSuivantes = evenementDetailsUIService.getActionsSuivantes(context);
        List<Action> baseActions = filterActionsSuivantes(
            context,
            MgppActionCategory.BASE_COMMUNICATION_DISPLAY,
            actionsSuivantes
        );
        List<Action> mainActions = filterActionsSuivantes(
            context,
            MgppActionCategory.MAIN_COMMUNICATION_DISPLAY,
            actionsSuivantes
        );
        boolean displayComSuccessive = evenementDetailsUIService.displayCreerCommunicationSuccessive(context);
        List<SelectValueGroupDTO> lstComSuccessives = evenementDetailsUIService.getCommunicationsSuccessivesList(
            context
        );

        String dossiersParlementairesSelected = UserSessionHelper.getUserSessionParameter(
            context,
            MgppUserSessionKey.DOSSIER_PARLEMENTAIRE
        );

        if (fiche.getNatureVersion().contains("annulation")) {
            for (Action action : mainActions) {
                if (action.getId().equals("ACCEPTER")) {
                    action.getProperties().put(MESSAGE, ACCEPTER_ANNULATION_MESSAGE);
                }
                if (action.getId().equals("REJETER")) {
                    action.getProperties().put(MESSAGE, REJETER_ANNULATION_MESSAGE);
                }
                if (action.getId().equals("ABANDONNER")) {
                    action.getProperties().put(MESSAGE, ABANDONNER_ANNULATION_MESSAGE);
                }
            }
        }

        template.getData().put(STTemplateConstants.ID, id);
        template.getData().put(MgppTemplateConstants.LST_WIDGETS_PRESENTATION, fiche.getLstWidgetsPresentation());
        template.getData().put(MgppTemplateConstants.LST_WIDGETS_FICHE, fiche.getLstWidgetsFiche());
        template.getData().put(MgppTemplateConstants.LST_WIDGETS_DEPOT, fiche.getLstWidgetsDepot());
        template.getData().put(MgppTemplateConstants.LST_NAVETTES, fiche.getLstNavettes());
        template.getData().put(MgppTemplateConstants.LST_WIDGETS_LOI_VOTEE, fiche.getLstWidgetsLoiVotee());
        template.getData().put(MgppTemplateConstants.LST_TABLES_REPRESENTANTS, fiche.getLstTablesRepresentants());
        template.getData().put(MgppTemplateConstants.LST_WIDGETS_DETAILS, fiche.getLstWidgetsDetails());
        template.getData().put(MgppTemplateConstants.LST_VERSIONS, fiche.getLstVersions());
        template.getData().put(MgppTemplateConstants.CUR_VERSION, fiche.getCurVersion());
        template
            .getData()
            .put(
                MgppTemplateConstants.CUR_DESCRIPTION,
                fiche
                    .getLstVersions()
                    .stream()
                    .filter(versionSelector -> versionSelector.getText().equals(fiche.getCurVersion()))
                    .map(VersionSelectDTO::getDescription)
                    .findFirst()
                    .orElse("")
            );
        template.getData().put(MgppTemplateConstants.NATURE_VERSION, fiche.getNatureVersion());
        template.getData().put(STTemplateConstants.BASE_ACTIONS, baseActions);
        template.getData().put(STTemplateConstants.MAIN_ACTIONS, mainActions);
        template.getData().put(MgppTemplateConstants.DISPLAY_COM_SUCCESSIVE, displayComSuccessive);
        template.getData().put(MgppTemplateConstants.COM_SUCCESSIVE_SELECT, lstComSuccessives);
        template
            .getData()
            .put(
                MgppTemplateConstants.IS_DIFFUSION_BUTTON_VISIBLE,
                StringUtils.equals(dossiersParlementairesSelected, "mgpp_designationOEP")
            );
        template.getData().put(MgppTemplateConstants.IS_FICHE_DIFFUSEE, fiche.isFicheDiffusee());
        template.getData().put(MgppTemplateConstants.IDENTIFIANT_DOSSIER, fiche.getIdDossier());
        template
            .getData()
            .put(
                MgppTemplateConstants.CAN_GENERER_COURRIER,
                !StringUtils.equals(FichePresentationAVI.DOC_TYPE, fiche.getTypeFiche())
            );

        return template;
    }

    private static List<Action> filterActionsSuivantes(
        SpecificContext context,
        MgppActionCategory actionCategory,
        List<String> actionsSuivantes
    ) {
        return context
            .getActions(actionCategory)
            .stream()
            .filter(a -> actionsSuivantes.contains(a.getId()) || ALWAYS_AVAILABLE_ACTION.contains(a.getId()))
            .collect(Collectors.toList());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("sauvegarde")
    public Response enregistrerFiche(
        @FormParam("communication") String json,
        @FormParam("idFiche") String idFiche,
        @FormParam("diffuser") Boolean diffuser
    ) {
        Gson gson = new Gson();
        context.putInContextData(MgppContextDataKey.FICHE_METADONNEES_MAP, gson.fromJson(json, Map.class));
        context.setCurrentDocument(idFiche);

        MgppFicheUIService ficheUIService = MgppUIServiceLocator.getMgppFicheUIService();
        context.setCurrentDocument(ficheUIService.saveFiche(context));
        if (
            BooleanUtils.isTrue(diffuser) &&
            FichePresentationOEPImpl.DOC_TYPE.equals(context.getCurrentDocument().getType())
        ) {
            ficheUIService.diffuserFiche(context);
        }

        if (CollectionUtils.isNotEmpty(context.getMessageQueue().getSuccessQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }
        idFiche = ofNullable(context.getCurrentDocument()).map(DocumentModel::getId).orElse(null);
        return new JsonResponse(
            SolonStatus.OK,
            context.getMessageQueue(),
            StringUtils.isNotBlank(idFiche)
                ? String.format(EpgURLConstants.URL_MGPP_FICHE_CONSULT, idFiche)
                : context.getUrlPreviousPage()
        )
        .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("verrouiller")
    public Response verrouillerFiche(@FormParam(FICHE_ID) @SwNotEmpty @SwId String idFiche) {
        CoreSession session = context.getSession();

        DocumentModel ficheDoc = session.getDocument(new IdRef(idFiche));
        STActionsServiceLocator
            .getSTLockActionService()
            .lockDocuments(context, session, Collections.singletonList(ficheDoc), ficheDoc.getType());

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deverrouiller")
    public Response deverrouillerFiche(@FormParam(FICHE_ID) @SwNotEmpty @SwId String idFiche) {
        CoreSession session = context.getSession();

        DocumentModel ficheDoc = session.getDocument(new IdRef(idFiche));
        STActionsServiceLocator
            .getSTLockActionService()
            .unlockDocumentsUnrestricted(context, Collections.singletonList(ficheDoc), ficheDoc.getType());

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        String templateName = MgppUIServiceLocator.getMgppFicheUIService().isFicheLoiVisible(getMyContext())
            ? "fragments/mgpp/dossier/onglets/ficheLoi"
            : "fragments/mgpp/dossier/onglets/fichePresentation";

        return new AjaxLayoutThTemplate(templateName, getMyContext());
    }

    @GET
    @Produces("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    @Path("genererCourrier")
    public Response genererCourrier(
        @SwRequired @SwNotEmpty @QueryParam("idCommunication") String idCommunication,
        @SwRequired @SwNotEmpty @QueryParam("version") String version,
        @SwRequired @SwNotEmpty @QueryParam("typeComCourrier") String typeComCourrier
    ) {
        context.putInContextData(MgppContextDataKey.MODELE_TEMPLATE_NAME, typeComCourrier);
        context.putInContextData(COMMUNICATION_ID, idCommunication);
        context.putInContextData(VERSION_ID, version);
        EvenementDTO evenementDTO = MgppUIServiceLocator.getMgppDossierUIService().getCurrentEvenementDTO(context);

        context.putInContextData(
            EpgContextDataKey.DOSSIER_NOR,
            Optional.ofNullable(evenementDTO).map(EvenementDTO::getIdDossier).orElse(idCommunication)
        );

        File fichierCourrier = MgppUIServiceLocator
            .getModeleCourrierUIService()
            .buildModeleCourrierForCommunication(context);
        return FileDownloadUtils.getAttachmentDocx(fichierCourrier, "courrier.docx");
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("supprimer")
    public Response supprimerFiche(@FormParam("ficheId") @SwNotEmpty @SwId String idFiche) {
        CoreSession session = context.getSession();

        try {
            session.removeDocument(new IdRef(idFiche));
            context
                .getMessageQueue()
                .addMessageToQueue(ResourceHelper.getString("supprimer.fiche.ok"), AlertType.TOAST_SUCCESS);
        } catch (Exception e) {
            LOGGER.error(MgppLogEnumImpl.FAIL_DEL_FICHE_LOI_TEC, e);
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("supprimer.fiche.ko"));
        }

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @GET
    @Path("representant/{idRepresentant}/charger")
    public ThTemplate chargerRepresentant(@PathParam("idRepresentant") String idRepresentant) {
        String templateName;
        Class representantClass;
        DocumentModel representantDoc = context.getSession().getDocument(new IdRef(idRepresentant));
        String suffix = "";
        if (RepresentantAVI.DOC_TYPE.equals(representantDoc.getType())) {
            templateName = "fragments/mgpp/components/mgppEditNomine";
            representantClass = RepresentantAVI.class;
        } else if (RepresentantOEPImpl.DOC_TYPE.equals(representantDoc.getType())) {
            templateName = "fragments/mgpp/components/mgppEditRepresentantOEP";
            representantClass = RepresentantOEP.class;
            RepresentantOEP representantOEP = representantDoc.getAdapter(RepresentantOEP.class);
            suffix = representantOEP.getType();
            TableReferenceDTO tableRefDto = SolonMgppServiceLocator
                .getTableReferenceService()
                .findTableReferenceByIdAndType(representantOEP.getRepresentant(), "Identite", null);
            if (tableRefDto != null) {
                template
                    .getData()
                    .put(
                        MgppTemplateConstants.SELECTED_REPRESENTANTS,
                        Collections.singletonList(
                            new SelectValueDTO(representantOEP.getRepresentant(), tableRefDto.getTitle())
                        )
                    );
            }
            template
                .getData()
                .put(
                    MgppTemplateConstants.FONCTION_LIST,
                    MgppUIServiceLocator.getMgppSelectValueUIService().getAllFonctions()
                );
        } else if (RepresentantAUDImpl.DOC_TYPE.equals(representantDoc.getType())) {
            templateName = "fragments/mgpp/components/mgppEditPersonneAud";
            representantClass = RepresentantAUD.class;
        } else {
            return template;
        }
        template.setName(templateName);
        template.getData().put(MgppTemplateConstants.TYPE_REPRESENTANT, representantDoc.getType() + suffix);
        template.getData().put(EpgTemplateConstants.ITEM, representantDoc.getAdapter(representantClass));

        return template;
    }

    @GET
    @Path("representant/{typeRepresentant}/creer")
    public Object ajouterRepresentant(@PathParam("typeRepresentant") String typeRepresentant) {
        CoreSession session = context.getSession();
        if (RepresentantAVI.DOC_TYPE.equals(typeRepresentant)) {
            template.setName("fragments/mgpp/components/mgppEditNomine");
            DocumentModel modelDesired = session.createDocumentModel(RepresentantAVI.DOC_TYPE);
            RepresentantAVI representantAVI = modelDesired.getAdapter(RepresentantAVI.class);
            representantAVI.setDateDebut(Calendar.getInstance());
            template.getData().put(EpgTemplateConstants.ITEM, representantAVI);
        } else if (typeRepresentant.startsWith(RepresentantOEPImpl.DOC_TYPE)) {
            template.setName("fragments/mgpp/components/mgppEditRepresentantOEP");
            DocumentModel modelDesired = session.createDocumentModel(RepresentantOEPImpl.DOC_TYPE);
            RepresentantOEP representantOEP = modelDesired.getAdapter(RepresentantOEP.class);
            representantOEP.setDateDebut(Calendar.getInstance());
            template.getData().put(EpgTemplateConstants.ITEM, representantOEP);
            template
                .getData()
                .put(
                    MgppTemplateConstants.FONCTION_LIST,
                    MgppUIServiceLocator.getMgppSelectValueUIService().getAllFonctions()
                );
        } else if (RepresentantAUDImpl.DOC_TYPE.equals(typeRepresentant)) {
            template.setName("fragments/mgpp/components/mgppEditPersonneAud");
            DocumentModel modelDesired = session.createDocumentModel(RepresentantAUDImpl.DOC_TYPE);
            RepresentantAUD representantAUD = modelDesired.getAdapter(RepresentantAUD.class);
            representantAUD.setDateDebut(Calendar.getInstance());
            template.getData().put(EpgTemplateConstants.ITEM, representantAUD);
        }
        template.getData().put(MgppTemplateConstants.TYPE_REPRESENTANT, typeRepresentant);
        return template;
    }

    @POST
    @Path("representant/editer")
    public ThTemplate editerRepresentant(
        @FormParam("idFiche") String idFiche,
        @SwBeanParam MgppRepresentantForm representantForm
    ) {
        template.setName("fragments/mgpp/dossier/onglets/bloc-representants.html");
        context.setCurrentDocument(idFiche);
        context.putInContextData(MgppContextDataKey.REPRESENTANT_FORM, representantForm);
        String typeRepresentant = MgppUIServiceLocator.getMgppFicheUIService().saveRepresentant(context);
        context.putInContextData(MgppContextDataKey.TYPE_REPRESENTANT, typeRepresentant);
        context.putInContextData(MgppContextDataKey.IS_FICHE_VERROUILLEE, true);
        MgppRepresentantTableForm representantTable = MgppUIServiceLocator
            .getMgppFicheUIService()
            .getRepresentantTable(context);

        template.getData().put(MgppTemplateConstants.REPRESENTANTS_TABLE, representantTable);

        return template;
    }

    @POST
    @Path("representant/{idRepresentant}/supprimer")
    public ThTemplate supprimerRepresentant(
        @PathParam("idRepresentant") String idRepresentant,
        @SwRequired @FormParam("idFiche") String idFiche
    ) {
        CoreSession session = context.getSession();
        template.setName("fragments/mgpp/dossier/onglets/bloc-representants.html");
        context.setCurrentDocument(idFiche);
        DocumentModel representantDoc = session.getDocument(new IdRef(idRepresentant));
        String representantType = representantDoc.getType();
        if (RepresentantOEPImpl.DOC_TYPE.equals(representantDoc.getType())) {
            representantType += representantDoc.getAdapter(RepresentantOEP.class).getType();
        }
        context.putInContextData(MgppContextDataKey.TYPE_REPRESENTANT, representantType);
        context.putInContextData(MgppContextDataKey.IS_FICHE_VERROUILLEE, true);
        session.removeDocument(representantDoc.getRef());
        session.save();
        MgppRepresentantTableForm representantTable = MgppUIServiceLocator
            .getMgppFicheUIService()
            .getRepresentantTable(context);

        template.getData().put(MgppTemplateConstants.REPRESENTANTS_TABLE, representantTable);

        return template;
    }

    @Path("ajout/fichier/fdd")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDocumentFondDossier(
        @FormDataParam(FICHE_ID) @SwNotEmpty @SwId String ficheId,
        FormDataMultiPart multipart
    ) {
        context.setCurrentDocument(ficheId);
        if (CollectionUtils.isEmpty(multipart.getFields(DOCUMENT_FILE))) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(MSG_ERREUR_EMPTY_FILE, "fond de dossier"));
        } else {
            for (FormDataBodyPart field : multipart.getFields(DOCUMENT_FILE)) {
                context.putInContextData(FILE_CONTENT, ((BodyPartEntity) field.getEntity()).getInputStream());
                context.putInContextData(FILE_DETAILS, field.getFormDataContentDisposition());
                MgppUIServiceLocator.getMgppFondDossierUIService().addFile(context);
            }
        }
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("suppression/fichier/fdd")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDocumentFondDossier(
        @FormParam(FICHE_ID) @SwNotEmpty @SwId String ficheId,
        @FormParam("documentIdToDelete") @SwNotEmpty @SwId String documentIdToDelete
    ) {
        context.setCurrentDocument(ficheId);
        context.putInContextData(ID, documentIdToDelete);

        MgppUIServiceLocator.getMgppFondDossierUIService().deleteFile(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("annuler/diffusion")
    public Response annulerDiffusionFiche(@FormParam(FICHE_ID) String idFiche) {
        context.setCurrentDocument(idFiche);

        MgppUIServiceLocator.getMgppFicheUIService().annulerDiffusionFiche(context);

        if (CollectionUtils.isNotEmpty(context.getMessageQueue().getSuccessQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }
}
