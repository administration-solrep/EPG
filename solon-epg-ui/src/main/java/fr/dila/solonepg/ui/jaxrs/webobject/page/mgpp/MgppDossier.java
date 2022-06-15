package fr.dila.solonepg.ui.jaxrs.webobject.page.mgpp;

import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.COMMUNICATION_ID;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.FORCE_REFRESH;
import static fr.dila.solonepg.ui.services.mgpp.impl.MgppDossierParlementaireMenuServiceImpl.ACTIVE_KEY;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.ui.bean.EpgConsultDossierDTO;
import fr.dila.solonepg.ui.bean.MgppMessageDTO;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.travail.EpgTravailDossier;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.mgpp.MgppDossierUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.MgppTemplateConstants;
import fr.dila.solonepg.ui.th.model.mgpp.MgppDossierParlementaireTemplate;
import fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName;
import fr.dila.solonmgpp.api.enumeration.MgppGroupementCorbeilleEnum;
import fr.dila.ss.ui.enums.SSActionCategory;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.dila.st.ui.services.actions.STLockActionService;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "MgppDossier")
public class MgppDossier extends SolonWebObject {

    public MgppDossier() {
        super();
    }

    @GET
    public ThTemplate getHome(@QueryParam("dossierParlementaire") String dossierParlementaire) {
        verifyAction(MgppActionEnum.MGPP_DOSSIERS, "/admin/mgpp/dossier");
        template.setName("pages/mgpp/dossiers");
        context.clearNavigationContext();
        template.setContext(context);

        UserSessionHelper.putUserSessionParameter(context, ACTIVE_KEY, "");
        updateNavigationContext(dossierParlementaire);
        context.putInContextData(MgppContextDataKey.DOSSIERS_PARLEMENTAIRES_SELECTED, dossierParlementaire);

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();
        template.setData(map);
        return template;
    }

    @Path("{id}")
    public Object getListeCommunications(@PathParam("id") String id) {
        ThTemplate template = getMyTemplate();
        MgppCorbeilleName corbeilleType = MgppCorbeilleName.fromValue(id);

        if (corbeilleType != null) {
            context.putInContextData(FORCE_REFRESH, true);
            if (!corbeilleType.isDynamic()) {
                template.setName("pages/mgpp/listeCommunications");
            } else {
                template.setName("pages/mgpp/listeResultatsCorbeille");
            }
            MgppGroupementCorbeilleEnum group = MgppGroupementCorbeilleEnum.fromCorbeilleId(
                corbeilleType.getIdCorbeille()
            );

            updateNavigationContext(
                Optional.ofNullable(group).orElse(MgppGroupementCorbeilleEnum.PROCEDURE_LEG).getActionMgppId()
            );
            template.setData(new HashMap<>());
            return newObject("DossierCommunicationAjax", context, template);
        }

        return newObject("MgppDossier", context);
    }

    @Path("{id}/{tab}")
    public Object getDossier(
        @PathParam("id") String id,
        final @PathParam("tab") String tab,
        @QueryParam("version") String version
    ) {
        String currentTab = "Fiche";
        template.setName("pages/mgpp/consult");
        template.setContext(context);

        context.setNavigationContextTitle(
            new Breadcrumb(
                id,
                "/mgpp/dossier/" + id + "/" + tab,
                Breadcrumb.SUBTITLE_ORDER + 1,
                context.getWebcontext().getRequest()
            )
        );

        MgppDossierUIService dossierUIService = MgppUIServiceLocator.getMgppDossierUIService();
        context.putInContextData(FORCE_REFRESH, true);
        context.putInContextData(COMMUNICATION_ID, id);
        MgppMessageDTO message = dossierUIService.getCurrentMessageDTO(context);

        context.putInContextData(
            STContextDataKey.DOSSIER_ID,
            Optional.ofNullable(message).map(MgppMessageDTO::getIdDossier).orElse(id)
        );

        DocumentModel currentFicheDoc = MgppUIServiceLocator.getMgppFicheUIService().getFicheForDossier(context);
        Dossier currentDossier = dossierUIService.getCurrentDossier(context, currentFicheDoc);
        STLockActionService lockActionService = STActionsServiceLocator.getSTLockActionService();
        CoreSession session = context.getSession();

        if (currentDossier != null) {
            DocumentModel dossierDoc = currentDossier.getDocument();
            context.setCurrentDocument(dossierDoc);
            template.getData().put(STTemplateConstants.ID_DOSSIER, dossierDoc.getId());
            template
                .getData()
                .put("monDossier", EpgUIServiceLocator.getEpgDossierUIService().getDossierConsult(context));
            template
                .getData()
                .put(
                    STTemplateConstants.LOCK_ACTIONS,
                    context.getActions(SSActionCategory.DOSSIER_TOPBAR_ACTIONS_LOCKS)
                );
            // Déverrouiller la fiche si elle est déjà verrouillée
            if (currentFicheDoc != null && lockActionService.getCanUnlockDoc(currentFicheDoc, session)) {
                lockActionService.unlockDocumentsUnrestricted(
                    context,
                    Collections.singletonList(currentFicheDoc),
                    currentFicheDoc.getType()
                );
            }
            template.getData().put("currentStep", dossierUIService.getCurrentStepLabels(context));
            template.getData().put("nextStep", dossierUIService.getNextStepLabels(context));
            template
                .getData()
                .put(SSTemplateConstants.FDR_ACTIONS, context.getActions(SSActionCategory.DOSSIER_TOPBAR_ACTIONS_FDR));
            template
                .getData()
                .put(
                    STTemplateConstants.GENERALE_ACTIONS,
                    context.getActions(SSActionCategory.DOSSIER_TOPBAR_ACTIONS_GENERAL)
                );
            template
                .getData()
                .put(
                    EpgTemplateConstants.CREATE_LIST_MISE_EN_SIGNATURE_ACTIONS,
                    context.getActions(EpgActionCategory.CREATE_LIST_MISE_EN_SIGNATURE_FOLDER_ACTIONS)
                );
            template
                .getData()
                .put(
                    SSTemplateConstants.NOTE_ACTIONS,
                    context.getActions(SSActionCategory.DOSSIER_TOPBAR_ACTIONS_NOTE)
                );
            template
                .getData()
                .put(
                    EpgTemplateConstants.CONFIDENTIAL_ACTIONS,
                    context.getActions(EpgActionCategory.DOSSIER_TOPBAR_ACTIONS_CONFIDENTIAL)
                );
            template
                .getData()
                .put(
                    EpgTemplateConstants.REPORT_ACTIONS,
                    context.getActions(EpgActionCategory.DOSSIER_TOPBAR_ACTIONS_REPORT)
                );
            template
                .getData()
                .put(
                    SSTemplateConstants.DIVERS_ACTIONS,
                    context.getActions(SSActionCategory.DOSSIER_TOPBAR_ACTIONS_DIVERS)
                );
            template
                .getData()
                .put(
                    SSTemplateConstants.PRINT_ACTIONS,
                    context.getActions(SSActionCategory.DOSSIER_TOPBAR_ACTIONS_PRINT)
                );
        } else if (currentFicheDoc != null) {
            context.setCurrentDocument(currentFicheDoc);
            boolean canUserLockFiche = lockActionService.getCanLockDoc(currentFicheDoc, session);
            context.putInContextData(MgppContextDataKey.CAN_USER_LOCK_FICHE, canUserLockFiche);
            context.putInContextData(
                MgppContextDataKey.CAN_USER_UNLOCK_FICHE,
                lockActionService.getCanUnlockDoc(currentFicheDoc, session)
            );
            EpgConsultDossierDTO maFiche = new EpgConsultDossierDTO();
            maFiche.setIsVerrouille(!canUserLockFiche);
            maFiche.setLockOwner(lockActionService.getLockOwnerName(currentFicheDoc, session));
            maFiche.setLockTime(lockActionService.getLockTime(currentFicheDoc, session));
            template.getData().put("monDossier", maFiche);
            template
                .getData()
                .put(
                    STTemplateConstants.LOCK_ACTIONS,
                    context.getActions(MgppActionCategory.FICHE_TOPBAR_ACTIONS_LOCKS)
                );
        }

        String idFiche = Optional.ofNullable(currentFicheDoc).map(DocumentModel::getId).orElse("");
        template.getData().put(MgppTemplateConstants.ID_FICHE, idFiche);
        if (message != null) {
            template.getData().put("idMessage", message.getId());
            template.getData().put("message", message);
        }
        context.putInContextData(
            MgppContextDataKey.IS_FICHE_LOI_VISIBLE,
            MgppUIServiceLocator.getMgppFicheUIService().isFicheLoiVisible(context)
        );
        context.putInContextData(MgppContextDataKey.FICHE_ID, idFiche);
        context.putInContextData(
            MgppContextDataKey.IS_FOND_DOSSIER_VISIBLE,
            MgppUIServiceLocator.getMgppFondDossierUIService().isFondDossierVisible(context)
        );
        List<Action> actions = context.getActions(MgppActionCategory.ONGLET_DOSSIER_MGPP);
        OngletConteneur listOnglets = OngletConteneur.actionsToTabs(actions, tab);
        template.getData().put(STTemplateConstants.MY_TABS, listOnglets);

        if (context.getNavigationContext().size() > 1) {
            template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        } else {
            context.removeNavigationContextTitle();
            template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, "");
        }

        if (listOnglets.getOnglets().stream().anyMatch(onglet -> onglet.getId().equals(tab))) {
            currentTab = StringUtils.capitalize(tab);
        }

        return newObject("DossierCommunication" + currentTab + "Ajax", context, template);
    }

    private void updateNavigationContext(String dossierParlementaire) {
        String title = "Dossiers parlementaires ";
        String corbeille = null;
        List<Action> lstActions = context.getActions(MgppActionCategory.DOSSIER_PARLEMENTAIRE_ACTIONS);
        if (StringUtils.isBlank(dossierParlementaire)) {
            corbeille =
                lstActions
                    .stream()
                    .filter(action -> MgppActionEnum.DOSSIER_PROC_LEGIS.getValue().equals(action.getId()))
                    .map(Action::getLabel)
                    .findFirst()
                    .orElse(null);
        } else {
            corbeille =
                lstActions
                    .stream()
                    .filter(action -> action.getId().equals(dossierParlementaire))
                    .map(Action::getLabel)
                    .findFirst()
                    .orElse(null);
        }

        if (StringUtils.isNotBlank(corbeille)) {
            title += "(" + ResourceHelper.getString(corbeille) + ")";
        }

        context.removeNavigationContextTitle();
        context.setNavigationContextTitle(
            new Breadcrumb(
                title,
                "/mgpp/dossier?dossierParlementaire=" + dossierParlementaire + "#main_content",
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
    }

    @GET
    @Path("creation")
    public ThTemplate getCreationDossier(@QueryParam(MgppTemplateConstants.ID_COMMUNICATION) String idCommunication) {
        template.setName("pages/mgpp/creationDossier");
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("mgpp.dossier.rattachement.title"),
                "/mgpp/dossier/creation?idCommunication=" + idCommunication + "#main_content",
                Breadcrumb.SUBTITLE_ORDER + 2,
                context.getWebcontext().getRequest()
            )
        );
        template.setContext(context);

        // Map pour mon contenu spécifique
        Map<String, Object> map = EpgTravailDossier.buildDossierCreationMap(context);
        map.put(MgppTemplateConstants.ID_COMMUNICATION_A_TRAITER, idCommunication);
        template.setData(map);
        return template;
    }

    @GET
    @Path("export/xls/{ficheId}")
    @Produces("application/vnd.ms-excel")
    public Response exportExcel(@PathParam("ficheId") String ficheId) {
        context.setCurrentDocument(ficheId);
        File file = MgppUIServiceLocator.getMgppGenerationFicheUIService().genererFicheXls(context);
        return FileDownloadUtils.getAttachmentXls(file, "fiche.xls");
    }

    @GET
    @Path("export/pdf/{ficheId}")
    @Produces("application/pdf")
    public Response exportPdf(@PathParam("ficheId") String ficheId) {
        context.putInContextData(SSContextDataKey.BIRT_OUTPUT_FORMAT, BirtOutputFormat.PDF);
        context.setCurrentDocument(ficheId);
        File file = MgppUIServiceLocator.getMgppGenerationFicheUIService().genererFichePdf(context);
        return FileDownloadUtils.getInlinePdf(file);
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new MgppDossierParlementaireTemplate();
    }
}
