package fr.dila.solonepg.ui.jaxrs.webobject.page.mgpp;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.ui.bean.EpgConsultDossierDTO;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppDossiersParlementairesEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppUserSessionKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.mgpp.MgppDossierUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.MgppTemplateConstants;
import fr.dila.solonepg.ui.th.model.mgpp.MgppDossierParlementaireTemplate;
import fr.dila.ss.ui.enums.SSActionCategory;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.dila.st.ui.services.actions.STLockActionService;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "MgppDossierSimple")
public class MgppDossierSimple extends SolonWebObject {

    public MgppDossierSimple() {
        super();
    }

    @Path("{id}/{tab}")
    public Object getDossier(@PathParam("id") String id, final @PathParam("tab") String tab) {
        String currentTab = "Fiche";
        template.setName("pages/mgpp/consult");
        template.setContext(context);

        context.setNavigationContextTitle(
            new Breadcrumb(
                id,
                "/mgpp/dossierSimple/" + id + "/" + tab,
                Breadcrumb.SUBTITLE_ORDER + 1,
                context.getWebcontext().getRequest()
            )
        );
        context.setCurrentDocument(id);
        template.getData().put(MgppTemplateConstants.ID_FICHE, id);

        MgppDossiersParlementairesEnum dossierParlementaire = MgppDossiersParlementairesEnum.fromTypeFiche(
            context.getCurrentDocument().getType()
        );

        if (dossierParlementaire != null) {
            UserSessionHelper.putUserSessionParameter(
                context,
                MgppUserSessionKey.DOSSIER_PARLEMENTAIRE,
                dossierParlementaire.getDossierParlementaire()
            );
            context.putInContextData(
                MgppContextDataKey.DOSSIERS_PARLEMENTAIRES_SELECTED,
                dossierParlementaire.getDossierParlementaire()
            );
        }

        context.putInContextData(
            STContextDataKey.DOSSIER_ID,
            MgppUIServiceLocator.getMgppFicheUIService().getIdDossierFromFiche(context)
        );
        DocumentModel currentFicheDoc = context.getCurrentDocument();
        MgppDossierUIService dossierUIService = MgppUIServiceLocator.getMgppDossierUIService();
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
        } else {
            template.getData().put(MgppTemplateConstants.ID_FICHE, currentFicheDoc.getId());
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

        context.putInContextData(
            MgppContextDataKey.IS_FICHE_LOI_VISIBLE,
            MgppUIServiceLocator.getMgppFicheUIService().isFicheLoiVisible(context)
        );
        context.putInContextData(MgppContextDataKey.FICHE_ID, id);
        context.putInContextData(
            MgppContextDataKey.IS_FOND_DOSSIER_VISIBLE,
            MgppUIServiceLocator.getMgppFondDossierUIService().isFondDossierVisible(context)
        );

        List<Action> actions = context.getActions(MgppActionCategory.ONGLET_DOSSIER_SIMPLE_MGPP);
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

        return newObject("DossierSimple" + currentTab + "Ajax", context, template);
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new MgppDossierParlementaireTemplate();
    }
}
