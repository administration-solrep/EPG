package fr.dila.solonepg.ui.jaxrs.webobject.ajax.admin.organigramme;

import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getOrganigrammeInjectionUIService;
import static fr.dila.solonepg.ui.services.impl.OrganigrammeInjectionUIServiceImpl.EXPORT_GOUVERNEMENT_FILE_NAME;
import static fr.dila.st.core.util.ResourceHelper.getString;

import com.google.common.collect.ImmutableList;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.exception.ImportOrganigrammeException;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgOrganigrammeCopyActionEnum;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.OrganigrammeInjectionUIService;
import fr.dila.ss.ui.jaxrs.webobject.ajax.SSOrganigrammeAjax;
import fr.dila.ss.ui.services.SSOrganigrammeManagerService;
import fr.dila.ss.ui.services.SSUIServiceLocator;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.bean.OrganigrammeElementDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.services.OrganigrammeTreeUIService;
import fr.dila.st.ui.services.STUIServiceLocator;
import fr.dila.st.ui.services.impl.OrganigrammeTreeUIServiceImpl;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "OrganigrammeAjax")
public class EpgOrganigrammeAjax extends SSOrganigrammeAjax {
    public static final String ERROR_IMPORT_MESSAGE = "organigramme.error.import";
    private static final STLogger LOGGER = STLogFactory.getLog(EpgOrganigrammeAjax.class);

    public EpgOrganigrammeAjax() {
        super();
    }

    @GET
    @Path("/selectarbre/filtrePoste")
    public ThTemplate getSelectArbreFiltrePoste(
        @QueryParam(STTemplateConstants.TYPE_SELECTION) List<String> typeSelection,
        @QueryParam(STTemplateConstants.SELECT_ID) String selectID,
        @QueryParam(STTemplateConstants.ACTIVATE_POSTE_FILTER) boolean activatePosteFilter,
        @QueryParam("selectedNode") String selectedNode,
        @QueryParam(STTemplateConstants.IS_MULTI) boolean isMulti,
        @QueryParam(STTemplateConstants.DTO_ATTRIBUTE) String dtoAttribute,
        @QueryParam(SSTemplateConstants.TYPE_ETAPE) String typeEtape
    ) {
        Boolean isFiltreCE = UserSessionHelper.getUserSessionParameter(context, EpgUserSessionKey.IS_ETAPE_FILTRE_CE);
        boolean isTypeEtapeAvisCE = isTypeEtapeAvisCE(typeEtape);
        // Si il n'y a pas de filtre ou si le filtre a changé on reset l'organigramme chargé en session
        if (isFiltreCE == null || isFiltreCE != isTypeEtapeAvisCE) {
            UserSessionHelper.clearUserSessionParameter(
                context,
                OrganigrammeTreeUIServiceImpl.ORGANIGRAMME_TREE_OPEN_NODES_KEY
            );
        }
        UserSessionHelper.putUserSessionParameter(context, EpgUserSessionKey.IS_ETAPE_FILTRE_CE, isTypeEtapeAvisCE);
        // Si type étape est Pour avis CE filtrer par poste CE
        return getSelectArbre(
            typeSelection,
            selectID,
            activatePosteFilter,
            selectedNode,
            isMulti,
            dtoAttribute,
            isTypeEtapeAvisCE
        );
    }

    private boolean isTypeEtapeAvisCE(String typeEtape) {
        return VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE.equals(typeEtape);
    }

    @Produces("application/vnd.ms-excel")
    @GET
    @Path("export")
    public Response exportGouvernement() {
        EpgUIServiceLocator.getOrganigrammeInjectionUIService().exportGouvernement(context.getSession());
        return FileDownloadUtils.getResponse(FileUtils.getAppTmpFolder(), EXPORT_GOUVERNEMENT_FILE_NAME);
    }

    @Path("importer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmImportGouvernementEpg() {
        EpgUIServiceLocator.getOrganigrammeInjectionUIService().executeInjectionEPG(context);
        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            context.getMessageQueue().addSuccessToQueue(ResourceHelper.getString("gouvernement.import.epg.success"));
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("importer/epp")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmImportGouvernementEpp() {
        EpgUIServiceLocator.getOrganigrammeInjectionUIService().executeInjectionEPP(context);
        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            context.getMessageQueue().addSuccessToQueue(ResourceHelper.getString("gouvernement.import.epp.success"));
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("import/file")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importerGouvernement(FormDataMultiPart multipart) throws IOException {
        List<FormDataBodyPart> listFDBP = multipart.getFields("gouvernementFile");
        if (CollectionUtils.isEmpty(listFDBP)) {
            // Rien n'a été uploadé
            return handleImportError(ImmutableList.of(getString(ERROR_IMPORT_MESSAGE)));
        }

        // Récupération et traitement du flux
        FormDataBodyPart formDataBodyPart = listFDBP.get(0);
        // Traitement du fichier
        try (InputStream is = ((BodyPartEntity) formDataBodyPart.getEntity()).getInputStream()) {
            context.putInContextData(STContextDataKey.FILE_CONTENT, is);
            context.putInContextData(STContextDataKey.FILE_DETAILS, formDataBodyPart.getFormDataContentDisposition());
            EpgUIServiceLocator.getOrganigrammeInjectionUIService().saveExcelInjection(context);
        } catch (ImportOrganigrammeException e) {
            Response redirect;
            LOGGER.error(STLogEnumImpl.LOG_EXCEPTION_TEC, e);
            if (CollectionUtils.isNotEmpty(e.getMessageKeys())) {
                redirect = handleImportError(e.getMessageKeys());
            } else {
                redirect = handleImportError(ImmutableList.of(getString(ERROR_IMPORT_MESSAGE)));
            }
            return redirect;
        }

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            context
                .getMessageQueue()
                .addToastSuccess(ResourceHelper.getString("gouvernement.import.toast.success.importFile"));
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    private Response handleImportError(List<String> messages) {
        messages.forEach(m -> context.getMessageQueue().addErrorToQueue(m));
        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());
        return redirect(context.getUrlPreviousPage());
    }

    @POST
    @Path("import/exporter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response exportInjectionGouvernement() {
        context
            .getMessageQueue()
            .addToastSuccess(ResourceHelper.getString("gouvernement.import.toast.success.exportPDF"));
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("appliquer/gouvernement")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response appliquerNouveauGouvernementEpg() {
        OrganigrammeInjectionUIService orgInjUIService = getOrganigrammeInjectionUIService();
        orgInjUIService.injecterGouvernementEPG(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Path("copy")
    @Override
    public ThTemplate copyNode(
        @FormParam("nodeId") String id,
        @FormParam("nodeType") String type,
        @FormParam("curMin") String ministereID
    ) {
        OrganigrammeType organigrammeType = OrganigrammeType.getEnum(type);
        putOrganigrammeNodeInContextData(id, organigrammeType, ministereID);

        EpgOrganigrammeCopyActionEnum organigrammeActionEnum = EpgOrganigrammeCopyActionEnum.getByOrganigrammeType(
            organigrammeType
        );
        STUIServiceLocator.getSTOrganigrammeManagerService().computeOrganigrammeActions(context);
        verifyAction(organigrammeActionEnum.getCopyAction(), "/admin/organigramme/copy");

        OrganigrammeTreeUIService service = STUIServiceLocator.getOrganigrammeTreeService();
        service.copyNode(context, id, type);
        return refreshFullArbre(context);
    }

    @POST
    @Path("paste")
    @Override
    public ThTemplate pasteNode(
        @FormParam("nodeId") String id,
        @FormParam("nodeType") String type,
        @FormParam("curMin") String ministereID,
        @FormParam("withUsers") boolean withUsers
    ) {
        OrganigrammeType organigrammeType = OrganigrammeType.getEnum(type);
        putOrganigrammeNodeInContextData(id, organigrammeType, ministereID);

        SSOrganigrammeManagerService ssOrganigrammeManagerService = SSUIServiceLocator.getSSOrganigrammeManagerService();
        ssOrganigrammeManagerService.computeOrganigrammeActions(context);
        OrganigrammeNode copiedOrganigrammeNode = ssOrganigrammeManagerService.getCopiedOrganigrammeNode();
        EpgOrganigrammeCopyActionEnum copyAction = EpgOrganigrammeCopyActionEnum.getByOrganigrammeType(
            copiedOrganigrammeNode.getType()
        );
        OrganigrammeTreeUIService service = STUIServiceLocator.getOrganigrammeTreeService();
        if (withUsers) {
            verifyAction(
                EpgActionEnum.valueOf(copyAction.getPasteActionPrefix() + "_WITH_USERS"),
                "/admin/organigramme/paste"
            );
            service.pasteNodeWithUsers(context, id, type);
        } else {
            verifyAction(
                EpgActionEnum.valueOf(copyAction.getPasteActionPrefix() + "_WITHOUT_USER"),
                "/admin/organigramme/paste"
            );
            service.pasteNodeWithoutUser(context, id, type);
        }
        return refreshFullArbre(context);
    }

    private void putOrganigrammeNodeInContextData(String id, OrganigrammeType organigrammeType, String ministereID) {
        OrganigrammeService organigrammeService = STServiceLocator.getOrganigrammeService();
        OrganigrammeNode node = organigrammeService.getOrganigrammeNodeById(id, organigrammeType);
        OrganigrammeNode parent = organigrammeService.getParentList(node).stream().findFirst().orElse(null);

        context.putInContextData(
            STContextDataKey.ORGANIGRAMME_NODE,
            new OrganigrammeElementDTO(
                context.getSession(),
                node,
                ministereID,
                new OrganigrammeElementDTO(context.getSession(), parent)
            )
        );
    }
}
