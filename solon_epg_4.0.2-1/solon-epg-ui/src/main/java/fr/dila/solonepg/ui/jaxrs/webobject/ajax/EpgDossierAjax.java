package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import static fr.dila.solonepg.ui.enums.EpgActionCategory.TRAITEMENT_PAPIER_SOUS_ONGLET;
import static fr.dila.ss.ui.jaxrs.webobject.ajax.AbstractSSDossierAjax.DOSSIER_AJAX_WEBOBJECT;
import static fr.dila.st.core.util.ObjectHelper.requireNonNullElseGet;
import static fr.dila.st.ui.enums.STContextDataKey.FILE_CONTENT;
import static fr.dila.st.ui.enums.STContextDataKey.FILE_DETAILS;
import static fr.dila.st.ui.enums.STContextDataKey.ID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.dossier.bordereau.TranspositionApplicationDetailDTO;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.SourceFichierEnum;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.jaxrs.webobject.ajax.AbstractSSDossierAjax;
import fr.dila.ss.ui.jaxrs.weboject.model.WebObjectExportModel;
import fr.dila.ss.ui.services.SSUIServiceLocator;
import fr.dila.ss.ui.th.bean.DossierMailForm;
import fr.dila.st.api.user.STUser;
import fr.dila.st.api.vocabulary.VocabularyEntry;
import fr.dila.st.core.exception.STAuthorizationException;
import fr.dila.st.core.requeteur.RequeteurRechercheUtilisateurs;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.ControllerUtils;
import fr.dila.st.ui.utils.FileDownloadUtils;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.ui.bean.CopyFileDTO;
import java.util.Optional;

@WebObject(type = DOSSIER_AJAX_WEBOBJECT)
public class EpgDossierAjax extends AbstractSSDossierAjax implements WebObjectExportModel {
    public static final String DOSSIER_ID = "dossierId";
    public static final String DOSSIER_NOM = "dossierNom";
    public static final String ADD_STEP = "addStep";
    public static final String DO_REATTRIBUTION = "doReattribution";
    public static final String DO_AFFECTATION_INIT = "doAffectationStepInit";
    public static final String TITLE_MODAL = "titleModal";
    public static final String LABEL_BTN_MODAL = "labelBtnModal";
    public static final String ID_POSTE_RETOUR_DAN = "idPosteDan";
    public static final String DOSSIER_LINK_ID = "dossierLinkId";
    public static final String GROUP_ID = "groupId";
    public static final String DOCUMENT_ID = "documentId";
    public static final String DOCUMENT_NAME = "documentName";
    public static final String DOCUMENT_FILE = "documentFile";
    public static final String ORIGINE_SOURCE = "origineSource";
    public static final String MSG_ERREUR_EMPTY_FILE = "dossier.add.empty.file.error";
    private static final String REDEMARRER_PATH = "redemarrer";
    private static final String JSON_SEARCH = "jsonSearch";
    private static final String POSTE_ID = "posteId";
    private static final String TYPE_ETAPE = "typeEtape";
    private static final String TYPE_ACTE = "typeActe";
    private static final String PROVIDER_NAME = "providerName";
    private static final String TRAITEMENT_PAPIER_WEBOBJECT = "AppliDossierTraitementPapier";
    private static final String DELETE_ALL_VERSIONS = "deleteAllVersions";
    private static final String DOCUMENT_ID_TO_DELETE = "documentIdToDelete";

    private void before(String dossierId, EpgActionEnum action, String path) {
        context.setCurrentDocument(dossierId);
        // Vérifier que l'utilisateur a bien le droit de réaliser l'action
        if (action != null) {
            EpgUIServiceLocator.getEpgDossierUIService().setActionsInContext(context);
            if (context.getAction(action) == null) {
                throw new STAuthorizationException(path);
            }
        }
    }

    @Path("massAction")
    public Object doMassAction() {
        return newObject("DossierMassActionAjax");
    }

    @Path("ajout/fichier/fdd")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDocumentFondDossier(
        @FormDataParam(DOSSIER_ID) String dossierId,
        @FormDataParam(GROUP_ID) String groupId,
        FormDataMultiPart multipart
    ) {
        before(dossierId, EpgActionEnum.ADD_DOCUMENT_FDD, "ajout/fichier/fdd");
        context.putInContextData(ID, groupId);
        if (CollectionUtils.isEmpty(multipart.getFields(DOCUMENT_FILE))) {
            DocumentModel parapheurDoc = context.getSession().getDocument(new IdRef(groupId));
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(MSG_ERREUR_EMPTY_FILE, parapheurDoc.getName()));
        } else {
            for (FormDataBodyPart field : multipart.getFields(DOCUMENT_FILE)) {
                context.putInContextData(FILE_CONTENT, ((BodyPartEntity) field.getEntity()).getInputStream());
                context.putInContextData(FILE_DETAILS, field.getFormDataContentDisposition());
                EpgUIServiceLocator.getEpgFondDeDossierUIService().addFile(context);
            }
        }
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }


    @Path("couper")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response couperFichier(
            @FormParam("fileId") @SwRequired @SwNotEmpty @SwId String fileId,
            @FormParam(DOSSIER_ID) @SwRequired @SwNotEmpty @SwId String dossierId
    ) {
        CopyFileDTO dto = new CopyFileDTO(fileId, dossierId);
        UserSessionHelper.putUserSessionParameter(context, SolonEpgConstant.CUT_FILE_SESSION_KEY, dto);
        context.getMessageQueue().addToastSuccess("Coupé avec succès");
        addMessageQueueInSession();
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("coller")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response collerFichier(
            @FormParam("folderId") @SwRequired @SwNotEmpty @SwId String folderId,
            @FormParam(DOSSIER_ID) @SwRequired @SwNotEmpty @SwId String dossierId
    ) {
        String fileId = Optional
                .ofNullable(
                        UserSessionHelper.getUserSessionParameter(
                                context,
                                SolonEpgConstant.CUT_FILE_SESSION_KEY,
                                CopyFileDTO.class
                        )
                )
                .map(CopyFileDTO::getFileId)
                .orElse(StringUtils.EMPTY);
        context.setCurrentDocument(dossierId);
        context.putInContextData(EpgContextDataKey.ID_FILE, fileId);
        context.putInContextData(EpgContextDataKey.ID_FOLDER, folderId);
        EpgUIServiceLocator.getEpgParapheurUIService().moveFileToAnotherFolder(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("modification/fichier")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editDocumentFondDossier(
        @FormDataParam(GROUP_ID) String groupId,
        @FormDataParam(DOCUMENT_ID) String documentId,
        @FormDataParam(DOCUMENT_FILE) InputStream uploadedInputStream,
        @FormDataParam(ORIGINE_SOURCE) SourceFichierEnum origineSource,
        @FormDataParam(DOCUMENT_FILE) FormDataContentDisposition fileDetail
    ) {
        context.setCurrentDocument(documentId);
        context.putInContextData(ID, groupId);
        context.putInContextData(FILE_CONTENT, uploadedInputStream);
        context.putInContextData(FILE_DETAILS, fileDetail);
        context.putInContextData(EpgContextDataKey.ORIGINE_EDIT_DOSSIER, origineSource.getValue());

        EpgUIServiceLocator.getEpgFondDeDossierUIService().editFile(context);

        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("suppression/fichier/fdd")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDocumentFondDossier(
        @FormParam(DOSSIER_ID) String dossierId,
        @FormParam(DOCUMENT_ID_TO_DELETE) String documentIdToDelete,
        @FormParam(DELETE_ALL_VERSIONS) Boolean deleteAllVersions
    ) {
        before(dossierId, EpgActionEnum.DELETE_DOCUMENT_FDD, "suppression/fichier/fdd");
        context.putInContextData(ID, documentIdToDelete);
        context.putInContextData(EpgContextDataKey.DELETE_ALL_VERSIONS, deleteAllVersions);

        EpgUIServiceLocator.getEpgFondDeDossierUIService().deleteFile(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("renommer/fichier")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response renameDocumentFondDossier(
        @FormDataParam(DOCUMENT_ID) String documentId,
        @FormDataParam(DOCUMENT_NAME) String documentName
    ) {
        context.setCurrentDocument(documentId);
        context.putInContextData(DOCUMENT_NAME, documentName);

        EpgUIServiceLocator.getEpgFondDeDossierUIService().renameFile(context);
        addMessageQueueInSession();

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("ajout/repertoire/fdd")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDossierFondDossier(
        @FormParam(DOSSIER_ID) String dossierId,
        @FormParam(GROUP_ID) String groupId
    ) {
        before(dossierId, EpgActionEnum.ADD_REPERTOIRE_FDD, "ajout/repertoire/fdd");
        context.putInContextData(ID, groupId);
        EpgUIServiceLocator.getEpgFondDeDossierUIService().createFolder(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("edition/repertoire/fdd")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response editDossierFondDossier(
        @FormParam(DOSSIER_ID) String dossierId,
        @FormParam(GROUP_ID) String groupId,
        @FormParam(DOSSIER_NOM) String dossierNom
    ) {
        context.putInContextData(STContextDataKey.ID, groupId);
        context.putInContextData(EpgContextDataKey.DOSSIER_NOM, dossierNom);
        context.putInContextData(
            EpgContextDataKey.IS_FDD_DELETABLE,
            EpgUIServiceLocator.getEpgFondDeDossierUIService().isDeletableFdd(context)
        );
        before(dossierId, EpgActionEnum.EDIT_REPERTOIRE_FDD, "edition/repertoire/fdd");

        EpgUIServiceLocator.getEpgFondDeDossierUIService().renameFolder(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("suppression/repertoire/fdd")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDossierFondDossier(
        @FormParam(DOSSIER_ID) String dossierId,
        @FormParam(GROUP_ID) String groupId
    ) {
        context.putInContextData(STContextDataKey.ID, groupId);
        context.putInContextData(
            EpgContextDataKey.IS_FDD_DELETABLE,
            EpgUIServiceLocator.getEpgFondDeDossierUIService().isDeletableFdd(context)
        );
        before(dossierId, EpgActionEnum.DELETE_REPERTOIRE_FDD, "suppression/repertoire/fdd");

        EpgUIServiceLocator.getEpgFondDeDossierUIService().deleteFolder(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("ajout/fichier/parapheur")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDocumentParapheur(
        @FormDataParam(DOSSIER_ID) String dossierId,
        @FormDataParam(GROUP_ID) String groupId,
        FormDataMultiPart multipart
    ) {
        before(dossierId, EpgActionEnum.ADD_DOCUMENT_PARAPHEUR, "ajout/fichier/parapheur");
        context.putInContextData(ID, groupId);

        // On vérifie que l'utilisateur ne tente pas d'ajouter plus de fichiers
        // que permis dans le dossier
        DocumentModel parapheurDoc = context.getSession().getDocument(new IdRef(groupId));
        ParapheurFolder parapheurFolder = parapheurDoc.getAdapter(ParapheurFolder.class);
        int nbCurrentDoc = SolonEpgServiceLocator
            .getParapheurService()
            .getChildrenFile(context.getSession(), parapheurDoc)
            .size();
        Long nbDocMax = parapheurDoc.getAdapter(ParapheurFolder.class).getNbDocAccepteMax();

        if (CollectionUtils.isEmpty(multipart.getFields(DOCUMENT_FILE))) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(MSG_ERREUR_EMPTY_FILE, parapheurDoc.getName()));
        } else {
            if (nbDocMax == null || multipart.getFields(DOCUMENT_FILE).size() <= nbDocMax - nbCurrentDoc) {
                for (FormDataBodyPart field : multipart.getFields(DOCUMENT_FILE)) {
                    context.putInContextData(FILE_CONTENT, ((BodyPartEntity) field.getEntity()).getInputStream());
                    context.putInContextData(FILE_DETAILS, field.getFormDataContentDisposition());
                    EpgUIServiceLocator.getEpgParapheurUIService().addFile(context);
                }
            } else {
                context
                    .getMessageQueue()
                    .addErrorToQueue(
                        ResourceHelper.getString("dossier.nombre.max.error", parapheurFolder.getName(), nbDocMax)
                    );
            }
        }
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("suppression/fichier/parapheur")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDocumentParapheur(
        @FormParam(DOSSIER_ID) String dossierId,
        @FormParam(DOCUMENT_ID_TO_DELETE) String documentIdToDelete,
        @FormParam(DELETE_ALL_VERSIONS) Boolean deleteAllVersions
    ) {
        before(dossierId, EpgActionEnum.DELETE_DOCUMENT_PARAPHEUR, "suppression/fichier/parapheur");
        context.putInContextData(ID, documentIdToDelete);
        context.putInContextData(EpgContextDataKey.DELETE_ALL_VERSIONS, deleteAllVersions);

        EpgUIServiceLocator.getEpgParapheurUIService().deleteFile(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("deverrouille")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response deverouilleDossier(@FormParam(DOSSIER_ID) @SwNotEmpty @SwId String id) {
        context.setCurrentDocument(id);
        STActionsServiceLocator.getDossierLockActionService().unlockCurrentDossier(context);

        // Ajout du dossier dans la liste des dernier dossier
        SSServiceLocator
            .getSsProfilUtilisateurService()
            .addDossierToListDerniersDossierIntervention(context.getSession(), id);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("verrouille")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response verouilleDossier(@FormParam(DOSSIER_ID) @SwNotEmpty @SwId String id) {
        context.setCurrentDocument(id);
        STActionsServiceLocator.getDossierLockActionService().lockCurrentDossier(context);

        // Ajout du dossier dans la liste des dernier dossier
        SSServiceLocator
            .getSsProfilUtilisateurService()
            .addDossierToListDerniersDossierIntervention(context.getSession(), id);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("save/traitement/papier/{tab}")
    public Object saveDossierTraitementPapier(
        @FormParam(DOSSIER_ID) @SwNotEmpty @SwId String id,
        @FormParam(DOSSIER_LINK_ID) @SwId String dossierLinkId,
        @PathParam("tab") @SwNotEmpty String tab
    ) {
        context.setCurrentDocument(id);
        context.putInContextData(STContextDataKey.CURRENT_DOSSIER_LINK, dossierLinkId);

        Map<String, Object> map = template.getData();
        map.put(
            EpgTemplateConstants.SUBTABS,
            OngletConteneur.actionsToTabs(context, TRAITEMENT_PAPIER_SOUS_ONGLET, tab)
        );
        tab = StringUtils.capitalize(tab);

        if (template instanceof AjaxLayoutThTemplate) {
            // si provient d'un call ajax on n'envoie que le context
            return newObject(TRAITEMENT_PAPIER_WEBOBJECT + tab, context);
        }
        // on envoie le context avec le template (lorsqu'on rafraichit la page par exemple)
        return newObject(TRAITEMENT_PAPIER_WEBOBJECT + tab, context, template);
    }

    @Path("valider/etape/check/publication")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validerEtapeCheckPublication(
        @FormParam(DOSSIER_ID) @SwRequired @SwNotEmpty @SwId String dossierId,
        @FormParam(DOSSIER_LINK_ID) @SwNotEmpty @SwId String dossierLinkId
    )
        throws IOException {
        context.setCurrentDocument(dossierId);
        context.putInContextData(STContextDataKey.CURRENT_DOSSIER_LINK, dossierLinkId);

        template = new AjaxLayoutThTemplate("fragments/components/message-validation-etape-charge-mission-ko", context);

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("template", ControllerUtils.renderHtmlFromTemplate(template));
        jsonData.put(
            "isChargeMissionConseillePMValide",
            SolonEpgActionServiceLocator
                .getEpgDossierDistributionActionService()
                .checkChargesDeMissionAvantDILAJO(context)
        );

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue(), (Serializable) jsonData).build();
    }

    @Path("valider/etape")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validerEtape(
        @FormParam(DOSSIER_ID) @SwRequired @SwNotEmpty @SwId String dossierId,
        @FormParam(DOSSIER_LINK_ID) @SwNotEmpty @SwId String dossierLinkId
    ) {
        context.setCurrentDocument(dossierId);
        context.putInContextData(STContextDataKey.CURRENT_DOSSIER_LINK, dossierLinkId);
        EpgUIServiceLocator.getEpgDossierDistributionUIService().validerEtapeAndAddMessage(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("valider/etape/initialisation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validerInitialisation(@FormParam(DOSSIER_ID) String dossierId) {
        context.setCurrentDocument(dossierId);
        EpgUIServiceLocator.getEpgDossierDistributionUIService().lancerDossierAndAddMessage(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("add/transposition/directive")
    public Response addTranspositionDirective(
        @FormParam("index") @SwNotEmpty String index,
        @SwBeanParam TranspositionApplicationDetailDTO directive
    )
        throws IOException {
        ThTemplate template = new AjaxLayoutThTemplate();
        template.setName("fragments/dossier/onglets/bordereau/application-directive-element");
        template.setContext(context);
        context.putInContextData(EpgContextDataKey.TRANSPOSITION_APPLICATION_DETAIL, directive);

        Map<String, Object> map = new HashMap<>();

        directive.setTitre(EpgUIServiceLocator.getBordereauUIService().getTitreDirectiveFromEurlex(context));

        map.put("index", index);
        map.put("directive", directive);
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/dossier/add/transposition/directive");

        template.setData(map);

        return new JsonResponse(
            SolonStatus.OK,
            context.getMessageQueue(),
            ControllerUtils.renderHtmlFromTemplate(template)
        )
        .build();
    }

    @POST
    @Path("add/transposition/loi-ordonnance")
    public ThTemplate addApplicationLoiOrdonnance(
        @FormParam("index") @SwNotEmpty String index,
        @SwBeanParam TranspositionApplicationDetailDTO loiOrdonnance
    ) {
        ThTemplate template = new AjaxLayoutThTemplate();
        template.setName("fragments/dossier/onglets/bordereau/application-loi-ordonnance-element");
        template.setContext(context);

        Map<String, Object> map = new HashMap<>();

        map.put("index", index);
        map.put("loiOrdonnance", loiOrdonnance);
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/dossier/add/transposition/loi-ordonnance");

        template.setData(map);

        return template;
    }

    @Path("terminer/sans/publication")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response terminerSansPublication(@FormParam(DOSSIER_ID) @SwNotEmpty @SwId String dossierId) {
        context.setCurrentDocument(dossierId);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        verifyAction(EpgActionEnum.DOSSIER_TERMINE_SANS_PUBLICATION, "terminer/sans/publication");

        SolonEpgActionServiceLocator.getEpgDossierActionService().terminerDossierSansPublication(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("ajouter/urgence")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response ajouterUrgence(@FormParam(DOSSIER_ID) @SwNotEmpty @SwId String dossierId) {
        context.setCurrentDocument(dossierId);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        before(dossierId, EpgActionEnum.AJOUTER_URGENCE_DOSSIER, "ajouter/urgence");
        SolonEpgActionServiceLocator.getEpgDossierActionService().ajouterUrgenceDossier(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("supprimer/urgence")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response supprimerUrgence(@FormParam(DOSSIER_ID) @SwNotEmpty @SwId String dossierId) {
        context.setCurrentDocument(dossierId);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        before(dossierId, EpgActionEnum.SUPPRIMER_URGENCE_DOSSIER, "supprimer/urgence");
        SolonEpgActionServiceLocator.getEpgDossierActionService().supprimerUrgenceDossier(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("add/mesure/nominative")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMesureNominative(@FormParam(DOSSIER_ID) @SwNotEmpty @SwId String dossierId) {
        context.setCurrentDocument(dossierId);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        before(dossierId, EpgActionEnum.DOSSIER_REDONNER_MESURE_NOMINATIVE, "add/mesure/nominative");
        SolonEpgActionServiceLocator.getEpgDossierActionService().redonnerMesureNominative(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("remove/mesure/nominative")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeMesureNominative(@FormParam(DOSSIER_ID) @SwNotEmpty @SwId String dossierId) {
        context.setCurrentDocument(dossierId);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        before(dossierId, EpgActionEnum.DOSSIER_ANNULER_MESURE_NOMINATIVE, "add/mesure/nominative");
        SolonEpgActionServiceLocator.getEpgDossierActionService().annulerMesureNominative(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("valider/etape/correction")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validerAvecCorrection(
        @FormParam(DOSSIER_ID) @SwNotEmpty @SwId String dossierId,
        @FormParam(DOSSIER_LINK_ID) @SwNotEmpty @SwId String dossierLinkId
    ) {
        context.setCurrentDocument(dossierId);
        context.putInContextData(STContextDataKey.CURRENT_DOSSIER_LINK, dossierLinkId);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        if (
            context.getAction(EpgActionEnum.DOSSIER_VALIDER_CORRECTION_AVIS) != null ||
            context.getAction(EpgActionEnum.DOSSIER_VALIDER_CORRECTION_BON_A_TIRER) != null
        ) {
            EpgUIServiceLocator.getEpgDossierDistributionUIService().validerEtapeCorrection(context);
        } else {
            throw new STAuthorizationException("valider/etape/correction");
        }
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("retour/modification")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validerRetourModification(
        @FormParam(DOSSIER_ID) @SwNotEmpty @SwId String dossierId,
        @FormParam(DOSSIER_LINK_ID) @SwNotEmpty @SwId String dossierLinkId,
        @FormParam(ID_POSTE_RETOUR_DAN) String idPosteDan,
        @FormParam(ADD_STEP) boolean addStep
    ) {
        context.setCurrentDocument(dossierId);
        context.putInContextData(STContextDataKey.CURRENT_DOSSIER_LINK, dossierLinkId);
        context.putInContextData(EpgContextDataKey.ID_POSTE_RETOUR_DAN, idPosteDan);
        context.putInContextData(EpgContextDataKey.ADD_STEP, addStep);

        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        verifyAction(EpgActionEnum.DOSSIER_RETOUR_POUR_MODIFICATION, "retour/modification");

        EpgUIServiceLocator.getEpgDossierDistributionUIService().validerEtapeRetourModification(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("valider/nor/attribuer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validerNorAttribuer(
        @FormParam(DOSSIER_ID) @SwNotEmpty @SwId String dossierId,
        @FormParam(DOSSIER_LINK_ID) @SwId String dossierLinkId
    ) {
        context.setCurrentDocument(dossierId);
        context.putInContextData(STContextDataKey.CURRENT_DOSSIER_LINK, dossierLinkId);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        verifyAction(EpgActionEnum.DOSSIER_VALIDER_NOR_ATTRIBUER, "valider/nor/attribuer");
        EpgUIServiceLocator.getEpgDossierDistributionUIService().norAttribue(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("valider/non/concerne")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validerNonConcerne(
        @FormParam(DOSSIER_ID) @SwNotEmpty @SwId String dossierId,
        @FormParam(ADD_STEP) Boolean addStep,
        @FormParam(DOSSIER_LINK_ID) @SwId String dossierLinkId
    ) {
        context.setCurrentDocument(dossierId);
        context.putInContextData(EpgContextDataKey.ADD_STEP, addStep);
        context.putInContextData(STContextDataKey.CURRENT_DOSSIER_LINK, dossierLinkId);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        verifyAction(EpgActionEnum.DOSSIER_VALIDER_NON_CONCERNE, "valider/non/concerne");
        EpgUIServiceLocator.getEpgDossierDistributionUIService().validerEtapeNonConcerne(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path(EpgURLConstants.URL_VALIDER_REFUS)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response validerRefus(
        @FormParam(DOSSIER_ID) @SwNotEmpty @SwId String dossierId,
        @FormParam(DOSSIER_LINK_ID) @SwNotEmpty @SwId String dossierLinkId
    ) {
        context.setCurrentDocument(dossierId);
        context.putInContextData(STContextDataKey.CURRENT_DOSSIER_LINK, dossierLinkId);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        verifyActions(
            Arrays.asList(
                EpgActionEnum.DOSSIER_VALIDER_REFUS,
                EpgActionEnum.ACTION_DOSSIER_VALIDER_REFUS_AVIS,
                EpgActionEnum.ACTION_DOSSIER_VALIDER_REFUS_RETOUR_MODIF,
                EpgActionEnum.DOSSIER_VALIDER_REFUS_CONTRESEIGN
            ),
            EpgURLConstants.URL_VALIDER_REFUS
        );
        EpgUIServiceLocator.getEpgDossierDistributionUIService().validerEtapeRefus(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @GET
    @Path("init/retour/dan")
    public ThTemplate initRetourDan() {
        ThTemplate template = new AjaxLayoutThTemplate(
            "fragments/components/modal-retour-modification-content",
            context
        );
        List<VocabularyEntry> posteDanList = EpgUIServiceLocator
            .getEpgDossierDistributionUIService()
            .getRetourDANList(context);
        Map<String, Object> map = new HashMap<>();
        map.put("posteDan", posteDanList);
        template.setData(map);

        return template;
    }

    @GET
    @Path("historique/fichier/contenu")
    public ThTemplate loadDossierContent(@SwId @QueryParam("idDocument") String idDoc) {
        ThTemplate template = new AjaxLayoutThTemplate(
            "fragments/components/modal-history-version-document-content",
            context
        );
        context.setCurrentDocument(idDoc);
        Map<String, Object> map = new HashMap<>();
        map.put(
            EpgTemplateConstants.LST_DOCUMENTS,
            EpgUIServiceLocator.getEpgFondDeDossierUIService().getFileVersionListDto(context)
        );
        template.setData(map);

        return template;
    }

    @Path("ajouter/suivis")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDossierSuivi(@FormParam(DOSSIER_ID) @SwNotEmpty @SwId String dossierId) {
        context.setCurrentDocument(dossierId);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        before(dossierId, EpgActionEnum.ADD_DOSSIER_SUIVI, "ajouter/suivis");
        SolonEpgActionServiceLocator.getEpgDossierActionService().addToDossiersSuivis(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Path("filtrer")
    public ThTemplate doSearch(@FormParam("search") String jsonSearch) {
        DossierListForm dossierlistForm = new DossierListForm(jsonSearch, true);

        template = new AjaxLayoutThTemplate("fragments/table/tableDossiers", context);

        Gson gson = new Gson();
        @SuppressWarnings("unchecked")
        Map<String, Serializable> mapSearch = jsonSearch != null
            ? gson.fromJson(jsonSearch, Map.class)
            : new HashMap<>();

        if (mapSearch.get(POSTE_ID) != null) {
            UserSessionHelper.putUserSessionParameter(context, JSON_SEARCH + mapSearch.get(POSTE_ID), jsonSearch);
        } else if (mapSearch.get(TYPE_ETAPE) != null) {
            UserSessionHelper.putUserSessionParameter(context, JSON_SEARCH + mapSearch.get(TYPE_ETAPE), jsonSearch);
        } else if (mapSearch.get(TYPE_ACTE) != null) {
            UserSessionHelper.putUserSessionParameter(context, JSON_SEARCH + mapSearch.get(TYPE_ACTE), jsonSearch);
        } else if ("corbeilleDossierEnCreationPageProvider".equals(mapSearch.get(PROVIDER_NAME))) {
            UserSessionHelper.putUserSessionParameter(
                context,
                EpgTemplateConstants.JSON_SEARCH_DOSSIER_EN_COURS_DE_CREATION,
                jsonSearch
            );
        } else if ("corbeilleHistoriqueValidationPageProvider".equals(mapSearch.get(PROVIDER_NAME))) {
            UserSessionHelper.putUserSessionParameter(
                context,
                EpgTemplateConstants.JSON_SEARCH_HISTORIQUE_VALIDATION,
                jsonSearch
            );
        } else if ("corbeilleDossiersSuivisPageProvider".equals(mapSearch.get(PROVIDER_NAME))) {
            UserSessionHelper.putUserSessionParameter(
                context,
                EpgTemplateConstants.JSON_SEARCH_DOSSIERS_SUIVIS,
                jsonSearch
            );
        } else if ("dossierTraitePage".equals(mapSearch.get(PROVIDER_NAME))) {
            UserSessionHelper.putUserSessionParameter(
                context,
                EpgTemplateConstants.JSON_SEARCH_DOSSIERS_A_TRAITER,
                jsonSearch
            );
        } else if ("dossierCreePage".equals(mapSearch.get(PROVIDER_NAME))) {
            UserSessionHelper.putUserSessionParameter(
                context,
                EpgTemplateConstants.JSON_SEARCH_DOSSIERS_CREES,
                jsonSearch
            );
        }

        EpgDossierList result = EpgUIServiceLocator
            .getSolonEpgDossierListUIService()
            .applyFilters(jsonSearch, dossierlistForm, mapSearch, context);

        dossierlistForm.setColumnVisibility(result.getListeColonnes());

        Map<String, Object> map = requireNonNullElseGet(template.getData(), HashMap::new);
        FiltreUtils.putRapidSearchDtoIntoTemplateData(context, result, map);

        context.removeNavigationContextTitle(Breadcrumb.SUBTITLE_ORDER);

        map.put(STTemplateConstants.RESULT_LIST, result);
        map.put(STTemplateConstants.RESULT_FORM, dossierlistForm);
        map.put(STTemplateConstants.RESULT_VISIBLE, MapUtils.isNotEmpty(mapSearch));
        map.put(STTemplateConstants.DATA_URL, "/dossier");
        map.put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_DOSSIER_FILTRER);
        map.put(STTemplateConstants.LST_COLONNES, result.getListeColonnes());
        map.put(STTemplateConstants.LST_SORTED_COLONNES, result.getListeSortedColonnes());
        map.put(STTemplateConstants.LST_SORTABLE_COLONNES, result.getListeSortableAndVisibleColonnes());

        map.put(STTemplateConstants.NB_RESULTS, result.getNbTotal());

        Map<String, Object> otherParameter = FiltreUtils.initOtherParamMapWithProviderInfos(context);
        map.put(STTemplateConstants.OTHER_PARAMETER, otherParameter);

        FiltreUtils.buildActions(map, context);

        template.setData(map);

        return template;
    }

    @GET
    @Path("suggestions/ampliation")
    public String getSuggestionsAmpliation(@SwRequired @QueryParam("input") String input)
        throws JsonProcessingException {
        List<String> suggestions;
        if (input == null || input.length() < 3) {
            suggestions = Collections.emptyList();
        } else {
            RequeteurRechercheUtilisateurs reqUtilisateurs = new RequeteurRechercheUtilisateurs.Builder()
                .email("*" + input + "*")
                .build();
            suggestions =
                reqUtilisateurs
                    .execute()
                    .stream()
                    .map(d -> d.getAdapter(STUser.class).getEmail())
                    .collect(Collectors.toList());
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(suggestions);
    }

    @GET
    @Path("mail/contenu")
    public ThTemplate loadDossierMailContent(@SwId @SwRequired @QueryParam("dossierId") String dossierId) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/dossier/dossierMailModalContent", context);

        context.setCurrentDocument(dossierId);

        DossierMailForm dossierMailForm = EpgUIServiceLocator.getEpgArchiveUIService().getDossierMailForm(context);

        Map<String, Object> map = new HashMap<>();
        map.put(EpgTemplateConstants.DOSSIER_MAIL_FORM, dossierMailForm);
        template.setData(map);

        return template;
    }

    @POST
    @Path("mail/sendDossier")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendDossierMail(
        @SwBeanParam DossierMailForm dossierMailForm,
        @SwId @SwRequired @FormParam("dossierId") String dossierId
    ) {
        context.putInContextData(STContextDataKey.DOSSIER_ID, dossierId);
        context.putInContextData(SSContextDataKey.DOSSIER_MAIL_FORM, dossierMailForm);

        SSUIServiceLocator.getSSArchiveUIService().envoyerMailDossier(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("export/zip")
    @POST
    @Produces("application/zip")
    public Response exportDossierList(@SwId @SwRequired @FormParam("dossierId") String dossier) {
        context.setCurrentDocument(dossier);
        StreamingOutput outputStream = getOutputStream(
            context,
            EpgUIServiceLocator.getEpgArchiveUIService()::exportDossier
        );

        final String zipFilename = "export_" + SolonDateConverter.DATE_DASH.formatNow() + ".zip";
        return FileDownloadUtils.getZipResponse(outputStream, zipFilename);
    }

    @POST
    @Path("creer/liste/signature")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createListSignature(@SwRequired @SwId @FormParam(STTemplateConstants.ID_DOSSIER) String idDossier) {
        context.setCurrentDocument(idDossier);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        verifyAction(EpgActionEnum.CREATE_LIST_MISE_EN_SIGNATURE_DOSSIER, "dossier/creer/liste/signature");
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, Collections.singletonList(idDossier));
        SolonEpgActionServiceLocator.getEpgDossierActionService().createListeSignatureDossiers(context);

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put(EpgTemplateConstants.ID_LISTE, context.getFromContextData(EpgContextDataKey.ID_LISTE));

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue(), (Serializable) jsonData).build();
    }

    @Path("relancer/abandonne")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response relancerDossierAbandonne(@FormParam(DOSSIER_ID) @SwRequired @SwNotEmpty @SwId String dossierId) {
        context.setCurrentDocument(dossierId);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        verifyAction(EpgActionEnum.RELANCER_DOSSIER_ABANDONNE, "relancer/abandonne");
        SolonEpgActionServiceLocator.getEpgDossierDistributionActionService().relancerDossierAbandonne(context);
        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Path("periodicite/show/modal")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkShowModalSubstitutionPeriodicite(
        @SwId @SwRequired @FormParam("dossierId") String id,
        @SwRequired @FormParam("periodicite") String periodicite
    ) {
        context.setCurrentDocument(id);
        context.putInContextData(EpgContextDataKey.PERIODICITE_VALUE, periodicite);

        Object jsonData = SolonEpgActionServiceLocator
            .getEpgDossierActionService()
            .checkShowModalSubstitutionPeriodicite(context);
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue(), (Serializable) jsonData).build();
    }

    @Path("periodicite/load/modele")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public ThTemplate loadModeleFdrSubstitutionRapport(@SwId @SwRequired @FormParam("dossierId") String id) {
        context.setCurrentDocument(id);
        List<SelectValueDTO> listModele = SolonEpgActionServiceLocator
            .getEpgDossierActionService()
            .loadModeleFdrSubstitutionRapport(context);

        ThTemplate template = new AjaxLayoutThTemplate("fragments/components/select-modele-fdr", context);
        template.getData().put(EpgTemplateConstants.LIST_MODELE, listModele);
        return template;
    }

    @Path("redemarrer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response redemarrerDossier(@FormParam(DOSSIER_ID) @SwRequired @SwNotEmpty @SwId String dossierId) {
        context.setCurrentDocument(dossierId);
        context.getCurrentDocument().getAdapter(Dossier.class);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        verifyAction(EpgActionEnum.REDEMARRER_DOSSIER, REDEMARRER_PATH);
        context.putInContextData(EpgContextDataKey.NEXT_STATUT, VocabularyConstants.STATUT_LANCE);
        SolonEpgActionServiceLocator.getEpgDossierDistributionActionService().redemarrerDossier(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }
}
