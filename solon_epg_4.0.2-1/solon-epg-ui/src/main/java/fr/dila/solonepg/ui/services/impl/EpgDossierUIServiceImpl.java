package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.ui.enums.EpgContextDataKey.DOSSIER_ACTIONS;
import static fr.dila.solonepg.ui.enums.EpgContextDataKey.ROUTING_ACTIONS;

import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.event.helper.DocumentSearchEventHelper;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgConsultDossierDTO;
import fr.dila.solonepg.ui.bean.EpgDossierActionDTO;
import fr.dila.solonepg.ui.bean.action.EpgRoutingActionDTO;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgDossierUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.actions.EpgDocumentRoutingActionService;
import fr.dila.solonepg.ui.services.actions.EpgDossierActionService;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.ui.bean.actions.CorbeilleActionDTO;
import fr.dila.ss.ui.bean.actions.SSNavigationActionDTO;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.SSFeuilleRouteUIService;
import fr.dila.ss.ui.services.SSUIServiceLocator;
import fr.dila.ss.ui.services.actions.SSActionsServiceLocator;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.ProfileService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.actions.DossierLockActionDTO;
import fr.dila.st.ui.bean.actions.STLockActionDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.usermanager.UserManager;

public class EpgDossierUIServiceImpl implements EpgDossierUIService {
    private static final String DELETABLE_DOSSIERS_ID = "deletableDossiersId";
    private static final STLogger LOGGER = STLogFactory.getLog(EpgDossierCreationUIServiceImpl.class);

    @Override
    public EpgConsultDossierDTO getDossierConsult(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        EpgConsultDossierDTO dto = MapDoc2Bean.docToBean(dossierDoc, EpgConsultDossierDTO.class);

        dto.setTitreActe(dossier.getTitreActe());
        dto.setNumeroNor(dossier.getNumeroNor());

        //MAJ des info de verrou du dossier
        dto.setIsVerrouille(!STActionsServiceLocator.getDossierLockActionService().getCanLockCurrentDossier(context));
        dto.setLockTime(
            STActionsServiceLocator
                .getSTLockActionService()
                .getLockTime(context.getCurrentDocument(), context.getSession())
        );
        dto.setLockOwner(
            STActionsServiceLocator
                .getSTLockActionService()
                .getLockOwnerName(context.getCurrentDocument(), context.getSession())
        );

        dto.setIsDone(dossier.isDone());

        SSFeuilleRouteUIService feuilleRouteUIService = SSUIServiceLocator.getSSFeuilleRouteUIService();
        if (dto.getIsDone()) {
            dto.setDateLastStep(feuilleRouteUIService.getLastStepDate(context, dossier.getLastDocumentRoute()));
        } else {
            dto.setNextStepLabel(feuilleRouteUIService.getNextStepLabels(context, dossier.getLastDocumentRoute()));
            dto.setActualStepLabel(feuilleRouteUIService.getCurrentStepLabel(context));
        }

        initDossierActionContext(context);

        DocumentSearchEventHelper.raiseEvent(context.getSession(), context.getCurrentDocument());
        return dto;
    }

    @Override
    public boolean canUserUpdateFdd(SpecificContext context) {
        boolean isFondDossierUpdatable = true;

        CoreSession session = context.getSession();
        NuxeoPrincipal principal = session.getPrincipal();
        DocumentModel dossierDoc = context.getCurrentDocument();

        // Vérifie que l'utilisateur possède la fonction unitaire
        if (!principal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_FOND_DOSSIER_UPDATER)) {
            isFondDossierUpdatable = false;
        } else if (!STActionsServiceLocator.getDossierLockActionService().getCanUnlockCurrentDossier(context)) {
            // Vérifie que le dossier en cours est verrouillé
            isFondDossierUpdatable = false;
        } else if (
            !SSActionsServiceLocator.getSSCorbeilleActionService().hasCurrentDossierLinks(context) &&
            !principal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UPDATER)
        ) {
            // Si le dossier n'est pas distribué, seul l'administrateur fonctionnel a le droit modifier le fond de
            // dossier
            isFondDossierUpdatable = false;
        } else if (!EpgUIServiceLocator.getEpgDossierDistributionUIService().isEtapePourImpressionUpdater(context)) {
            isFondDossierUpdatable = false;
        } else if (dossierDoc != null) {
            Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            isFondDossierUpdatable = !VocabularyConstants.STATUT_NOR_ATTRIBUE.equals(dossier.getStatut());
        }

        // Cas précis du dossier à l'étape "pour avis CE" avec l'utilisateur du groupe
        // EnvoiSaisinePieceComplementaireExecutor et que le dossier est verrouillé
        if (!isFondDossierUpdatable && canUserUpdateFddWithPourAvisCEStep(context)) {
            isFondDossierUpdatable = true;
        }

        return isFondDossierUpdatable;
    }

    private static boolean canUserUpdateFddWithPourAvisCEStep(SpecificContext context) {
        NuxeoPrincipal principal = context.getSession().getPrincipal();
        return (
            principal.isMemberOf(
                SolonEpgBaseFunctionConstant.CONSEIL_ETAT_ENVOI_SAISINE_PIECE_COMPLEMENTAIRE_EXECUTOR
            ) &&
            SolonEpgActionServiceLocator.getEpgCorbeilleActionService().isEtapePourAvisCE(context) &&
            STActionsServiceLocator.getDossierLockActionService().getCanUnlockCurrentDossier(context)
        );
    }

    @Override
    public boolean canUserUpdateParapheur(SpecificContext context) {
        boolean isParapheurUpdatable = true;

        CoreSession session = context.getSession();
        NuxeoPrincipal principal = session.getPrincipal();
        DocumentModel dossierDoc = context.getCurrentDocument();

        // Vérifie que l'utilisateur possède la fonction unitaire
        if (!principal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_PARAPHEUR_UPDATER)) {
            isParapheurUpdatable = false;
        } else if (!STActionsServiceLocator.getSTLockActionService().getCanUnlockDoc(dossierDoc, session)) {
            // Vérifie que le dossier en cours est verrouillé
            isParapheurUpdatable = false;
        } else if (
            !SSActionsServiceLocator.getSSCorbeilleActionService().hasCurrentDossierLinks(context) &&
            !principal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UPDATER)
        ) {
            // Si le dossier n'est pas distribué, seul l'administrateur fonctionnel a le droit de modifier le parapheur
            isParapheurUpdatable = false;
        } else if (!EpgUIServiceLocator.getEpgDossierDistributionUIService().isEtapePourImpressionUpdater(context)) {
            // À l'étape "pour impression", le dossier est en lecture seule sauf pour les administrateurs
            isParapheurUpdatable = false;
        } else if (dossierDoc != null) {
            if (SolonEpgServiceLocator.getFondDeDossierService().isDossierEtapePourAvisCE(dossierDoc, session)) {
                // Si le document est à une étape 'Pour avis CE' on ne peut pas modifier le parapheur (FEV505)
                isParapheurUpdatable = false;
            } else {
                Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                isParapheurUpdatable = !VocabularyConstants.STATUT_NOR_ATTRIBUE.equals(dossier.getStatut());
            }
        }

        return isParapheurUpdatable;
    }

    @Override
    public void setActionsInContext(SpecificContext context) {
        EpgDossierActionDTO dossierAction = context.computeFromContextDataIfAbsent(
            DOSSIER_ACTIONS,
            EpgDossierActionDTO::new
        );
        Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);

        if (!dossierAction.getCanUserUpdateParapheur()) {
            dossierAction.setCanUserUpdateParapheur(canUserUpdateParapheur(context));
        }

        if (!dossierAction.getCanUserUpdateFdd()) {
            dossierAction.setCanUserUpdateFdd(canUserUpdateFdd(context));
        }

        if (!dossierAction.getSolonEditAvailable()) {
            Boolean solonEditAvailable = SolonEpgServiceLocator
                .getProfilUtilisateurService()
                .getProfilUtilisateurForCurrentUser(context.getSession())
                .getPresenceSolonEdit();
            dossierAction.setSolonEditAvailable(solonEditAvailable);
        }

        if (!dossierAction.getIsUrgent()) {
            dossierAction.setIsUrgent(dossier.getIsUrgent());
        }

        if (!dossierAction.getIsMesureNominative()) {
            dossierAction.setIsMesureNominative(dossier.isMesureNominative());
        }

        if (!dossierAction.getIsTexteSignale()) {
            dossierAction.setIsTexteSignale(dossier.getDateVersementTS() != null);
        }

        dossierAction.setIsDossierDeleted(dossier.isDossierDeleted());

        EpgRoutingActionDTO routingActionsDto = context.computeFromContextDataIfAbsent(
            ROUTING_ACTIONS,
            EpgRoutingActionDTO::new
        );
        EpgDocumentRoutingActionService epgDocumentRoutingActionService = SolonEpgActionServiceLocator.getEpgDocumentRoutingActionService();
        routingActionsDto.setActeTexteNonPublie(epgDocumentRoutingActionService.isTypeActeTexteNonPublie(context));
        routingActionsDto.setIsRapportAuParlement(epgDocumentRoutingActionService.isRapportAuParlement(context));
    }

    private boolean hasTypeActeMesureNominative(SpecificContext context) {
        final DocumentModel dossierDoc = context.getCurrentDocument();
        if (dossierDoc != null) {
            final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            final ActeService acteService = SolonEpgServiceLocator.getActeService();
            return acteService.hasTypeActeMesureNominative(dossier.getTypeActe());
        }
        return false;
    }

    @Override
    public void initDossierActionContext(SpecificContext context) {
        DossierLockActionDTO lockAction = new DossierLockActionDTO();
        lockAction.setCanLockCurrentDossier(
            STActionsServiceLocator.getDossierLockActionService().getCanLockCurrentDossier(context)
        );
        context.putInContextData(STContextDataKey.DOSSIER_LOCK_ACTIONS, lockAction);

        STLockActionDTO stLockAction = new STLockActionDTO();
        CoreSession session = context.getSession();
        DocumentModel currentDocument = context.getCurrentDocument();
        stLockAction.setCurrentDocIsLockActionnableByCurrentUser(
            STActionsServiceLocator
                .getSTLockActionService()
                .currentDocIsLockActionnableByCurrentUser(session, currentDocument, session.getPrincipal())
        );
        context.putInContextData(STContextDataKey.LOCK_ACTIONS, stLockAction);

        EpgDossierActionService epgDossierActionService = SolonEpgActionServiceLocator.getEpgDossierActionService();
        EpgDossierActionDTO dossierActions = new EpgDossierActionDTO();
        dossierActions.setCanExecuteStep(epgDossierActionService.canExecuteStep(context));
        dossierActions.setIsUrgent(currentDocument.getAdapter(Dossier.class).getIsUrgent());
        dossierActions.setIsMesureNominative(currentDocument.getAdapter(Dossier.class).isMesureNominative());
        dossierActions.setHasTypeActeMesureNominative(hasTypeActeMesureNominative(context));
        dossierActions.setIsTexteSignale(currentDocument.getAdapter(Dossier.class).getDateVersementTS() != null);
        dossierActions.setCanSendSaisineOrPieceComplementaire(
            EpgUIServiceLocator.getEpgDossierDistributionService().canSendSaisineOrPieceComplementaire(context)
        );
        dossierActions.setHasSignataire(currentDocument.getAdapter(TraitementPapier.class).getSignataire() != null);
        dossierActions.setIsDossierAbandonne(
            VocabularyConstants.STATUT_ABANDONNE.equals(currentDocument.getAdapter(Dossier.class).getStatut())
        );

        Boolean solonEditAvailable = SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .getProfilUtilisateurForCurrentUser(session)
            .getPresenceSolonEdit();
        dossierActions.setSolonEditAvailable(solonEditAvailable);
        context.putInContextData(EpgContextDataKey.DOSSIER_ACTIONS, dossierActions);

        CorbeilleActionDTO corbeilleActionsDTO = new CorbeilleActionDTO();
        corbeilleActionsDTO.setIsDossierLoadedInCorbeille(
            SSActionsServiceLocator.getSSCorbeilleActionService().isDossierLoadedInCorbeille(context)
        );
        context.putInContextData(SSContextDataKey.CORBEILLE_ACTIONS, corbeilleActionsDTO);

        EpgDocumentRoutingActionService epgDocumentRoutingActionService = SolonEpgActionServiceLocator.getEpgDocumentRoutingActionService();
        EpgRoutingActionDTO routingActionsDto = new EpgRoutingActionDTO();
        routingActionsDto.setStepAvis(epgDocumentRoutingActionService.isStepAvis(context));
        routingActionsDto.setStepRetourModification(epgDocumentRoutingActionService.isStepRetourModification(context));
        routingActionsDto.setStepInitialisation(epgDocumentRoutingActionService.isActiveStepInitialisation(context));
        routingActionsDto.setStepPourBonATirer(epgDocumentRoutingActionService.isActiveStepPourBonATirer(context));
        routingActionsDto.setStepContreseign(epgDocumentRoutingActionService.isStepContreseign(context));
        routingActionsDto.setCanRetourModification(epgDocumentRoutingActionService.canRetourModification(context));
        routingActionsDto.setCanValideNonConcerne(epgDocumentRoutingActionService.canValideNonConcerne(context));
        routingActionsDto.setCanRefusValidation(epgDocumentRoutingActionService.canRefusValidation(context));
        routingActionsDto.setDossierLance(epgDocumentRoutingActionService.isDossierLance(context));
        routingActionsDto.setActeTexteNonPublie(epgDocumentRoutingActionService.isTypeActeTexteNonPublie(context));
        routingActionsDto.setIsEtapePourAvisCE(
            SolonEpgActionServiceLocator.getEpgCorbeilleActionService().isEtapePourAvisCE(context)
        );
        routingActionsDto.setHasRunningStep(epgDocumentRoutingActionService.hasRunningStep(context));
        routingActionsDto.setFeuilleRouteRestartable(epgDossierActionService.isFeuilleRouteRestartable(context));
        routingActionsDto.setIsRapportAuParlement(epgDocumentRoutingActionService.isRapportAuParlement(context));
        routingActionsDto.setIsFeuilleRouteVisible(
            epgDocumentRoutingActionService.isFeuilleRouteVisible(
                session,
                (SSPrincipal) session.getPrincipal(),
                currentDocument
            )
        );
        routingActionsDto.setIsStepExecutable(epgDocumentRoutingActionService.isStepExecutable(context));
        context.putInContextData(EpgContextDataKey.ROUTING_ACTIONS, routingActionsDto);
        context.putInContextData(
            SSContextDataKey.IS_IN_PROGRESS_STEP,
            epgDocumentRoutingActionService.isInProgressStep(context)
        );

        SSNavigationActionDTO navigationActionDTO = new SSNavigationActionDTO();
        navigationActionDTO.setIsFromEspaceTravail(
            SSActionsServiceLocator.getNavigationActionService().isFromEspaceTravail(context)
        );
        context.putInContextData(SSContextDataKey.NAVIGATION_ACTIONS, navigationActionDTO);
    }

    @Override
    public void deleteDossiers(SpecificContext context) {
        List<String> idDossiers = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
        SolonEpgServiceLocator.getDossierService().deletedDossiers(context.getSession(), idDossiers);
    }

    @Override
    public void createAndAssignLotToDossiersByIds(SpecificContext context) {
        List<String> idDossiers = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);

        SolonEpgAdamantServiceLocator.getExtractionLotService().createAndAssignLotToDocumentsByIds(idDossiers);
    }

    @Override
    public void retirerDossiersArchivageDefinitive(SpecificContext context) {
        ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
        List<String> idDossiers = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
        List<DocumentModel> documentModelList = context.getSession().getDocuments(idDossiers, null);
        archiveService.retirerArchivageDefinitive(documentModelList, context.getSession());
    }

    @Override
    public void removeDossiersCandidatElimination(SpecificContext context) {
        DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        List<String> idDossiers = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
        CoreSession session = context.getSession();
        session
            .getDocuments(idDossiers, null)
            .forEach(doc -> dossierService.retirerDossierFromSuppressionListUnrestricted(session, doc));
    }

    @Override
    public void validateDossiersCandidatElimination(SpecificContext context) {
        DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        List<String> idDossiers = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
        CoreSession session = context.getSession();

        DocumentModelList dossiersDocs = session.getDocuments(idDossiers, null);
        List<Dossier> dossiers = dossiersDocs
            .stream()
            .map(d -> d.getAdapter(Dossier.class))
            .collect(Collectors.toList());

        ProfileService profileService = STServiceLocator.getProfileService();
        final List<STUser> adminMins = profileService.getUsersFromBaseFunction(
            SolonEpgBaseFunctionConstant.ADMIN_MINISTERIEL_EMAIL_RECEIVER
        );
        final UserManager userManager = STServiceLocator.getUserManager();
        Map<SSPrincipal, List<String>> norsByUsers = adminMins
            .stream()
            .filter(admin -> admin.getEmail() != null)
            .map(a -> (SSPrincipal) userManager.getPrincipal(a.getUsername()))
            .collect(
                Collectors.toMap(
                    Function.identity(),
                    principal ->
                        dossiers
                            .stream()
                            .filter(d -> principal.getMinistereIdSet().contains(d.getMinistereAttache()))
                            .map(Dossier::getNumeroNor)
                            .collect(Collectors.toList())
                )
            );

        dossiersDocs.forEach(doc -> dossierService.validerTransmissionToSuppressionListUnrestricted(session, doc));

        sendMailsToAdminMinisteriels(session, norsByUsers);
    }

    private void sendMailsToAdminMinisteriels(CoreSession session, Map<SSPrincipal, List<String>> norsByUsers) {
        Map<SSPrincipal, List<String>> usersWithNotEmptyNors = norsByUsers
            .entrySet()
            .stream()
            .filter(e -> !e.getValue().isEmpty())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (usersWithNotEmptyNors.isEmpty()) {
            return;
        }

        STParametreService parametersService = STServiceLocator.getSTParametreService();
        final String objet = parametersService.getParametreValue(
            session,
            STParametreConstant.OBJET_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN
        );

        String text = parametersService.getParametreValue(
            session,
            STParametreConstant.TEXT_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN
        );
        String lineSeparator = System.getProperty("line.separator");

        usersWithNotEmptyNors.forEach(
            (k, v) -> {
                String message = text + lineSeparator + String.join(lineSeparator, v);
                try {
                    InternetAddress internetAddress = new InternetAddress(k.getEmail());
                    STServiceLocator
                        .getSTMailService()
                        .sendMail(message, objet, "mailSession", new InternetAddress[] { internetAddress });
                } catch (AddressException exc) {
                    LOGGER.warn(session, STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
                }
            }
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public SolonStatus checkUserAndDeleteDossierForMassAction(SpecificContext context) {
        List<String> idsToDelete = UserSessionHelper.getUserSessionParameter(
            context,
            DELETABLE_DOSSIERS_ID,
            List.class
        );
        context.putInContextData(EpgContextDataKey.ID_DOSSIERS, idsToDelete);
        String username = context.getFromContextData(EpgContextDataKey.USERNAME);
        String data = context.getFromContextData(EpgContextDataKey.DATA);
        UserManager manager = STServiceLocator.getUserManager();
        SolonStatus status;

        if (manager.checkUsernamePassword(username, data)) {
            deleteDossiers(context);
            EpgUIServiceLocator.getEpgSelectionToolUIService().deleteIdFromSelectionTool(context);
            context.getMessageQueue().addToastSuccess(ResourceHelper.getString("epg.dossiers.creation.delete.success"));
            status = SolonStatus.OK;
        } else {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("epg.dossiers.creation.delete.error"));
            status = SolonStatus.NOT_AUTORIZED;
        }
        return status;
    }

    @Override
    public void retirerDossiersCandidatsArchivageIntermediaire(SpecificContext context) {
        List<String> idDossiers = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
        int dureeManitienDossierEnProduction = context.getFromContextData(
            EpgContextDataKey.DUREE_MAINTIEN_EN_PRODUCTION
        );
        DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        CoreSession session = context.getSession();
        session
            .getDocuments(idDossiers, null)
            .forEach(
                doc ->
                    dossierService.retirerDossierFromListCandidatsArchivageIntermediaireUnrestricted(
                        session,
                        doc,
                        dureeManitienDossierEnProduction
                    )
            );
    }

    @Override
    public void verserDossierBaseArchivageIntermediaire(SpecificContext context) {
        List<String> idDossiers = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
        DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        CoreSession session = context.getSession();
        session
            .getDocuments(idDossiers, null)
            .forEach(doc -> dossierService.verserDossierDansBaseArchivageIntermediaireUnrestricted(session, doc));
    }

    @Override
    public void retirerAbandonDossiers(SpecificContext context) {
        List<String> idDossiers = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
        DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        CoreSession session = context.getSession();
        session.getDocuments(idDossiers, null).forEach(doc -> dossierService.retirerAbandonDossier(session, doc));
    }

    @Override
    public File generateDossierPdf(SpecificContext context) throws IOException {
        CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        return SolonEpgServiceLocator.getPdfService().generateDossierPdf(session, dossier);
    }

    @Override
    public void loadDossierActions(SpecificContext context, ThTemplate template) {
        EpgDossierUIService.super.loadDossierActions(context, template);

        Map<String, Object> map = template.getData();

        map.put(
            EpgTemplateConstants.REPORT_ACTIONS,
            context.getActions(EpgActionCategory.DOSSIER_TOPBAR_ACTIONS_REPORT)
        );
        map.put(SSTemplateConstants.TYPE_ETAPE, EpgUIServiceLocator.getEpgSelectValueUIService().getRoutingTaskTypes());
        map.put(
            EpgTemplateConstants.CONFIDENTIAL_ACTIONS,
            context.getActions(EpgActionCategory.DOSSIER_TOPBAR_ACTIONS_CONFIDENTIAL)
        );
        map.put(
            EpgTemplateConstants.CREATE_LIST_MISE_EN_SIGNATURE_ACTIONS,
            context.getActions(EpgActionCategory.CREATE_LIST_MISE_EN_SIGNATURE_FOLDER_ACTIONS)
        );
    }
}
