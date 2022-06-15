package fr.dila.solonepg.ui.services.actions.impl;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.enumeration.EpgVecteurPublication;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFile;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.SelectionDto;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgSelectionTypeEnum;
import fr.dila.solonepg.ui.services.EpgSelectionToolUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.actions.EpgCorbeilleActionService;
import fr.dila.solonepg.ui.services.actions.EpgDocumentRoutingActionService;
import fr.dila.solonepg.ui.services.actions.EpgDossierDistributionActionService;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.api.service.DossierDistributionService;
import fr.dila.ss.api.service.SSJournalService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.st.api.caselink.STDossierLink;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.jeton.JetonDoc;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.vocabulary.VocabularyEntry;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.services.actions.DossierLockActionService;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.exception.FeuilleRouteAlreadyLockedException;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.exception.FeuilleRouteNotLockedException;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import fr.sword.xsd.solon.epg.PublicationIntOuExtType;
import fr.sword.xsd.solon.epg.TypeModification;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;

public class EpgDossierDistributionActionServiceImpl implements EpgDossierDistributionActionService {
    private static final STLogger LOGGER = STLogFactory.getLog(EpgDossierDistributionActionServiceImpl.class);
    private static final String INIT = "init";

    private static final List<String> VECTEURS_PUBLICATION_JO_LIST = ImmutableList.of(
        EpgVecteurPublication.JOURNAL_OFFICIEL.getIntitule(),
        EpgVecteurPublication.JO_DOCUMENTS_ADMINISTRATIFS.getIntitule(),
        EpgVecteurPublication.DOCUMENTS_ADMINISTRATIFS.getIntitule()
    );

    @Override
    public void viderSelectionTool(SpecificContext context) {
        EpgSelectionToolUIService selectionToolService = EpgUIServiceLocator.getEpgSelectionToolUIService();
        // Stocker la liste des id a mettre à jour au niveau du front
        List<String> deletedIds = selectionToolService
            .getSelectionList(context)
            .stream()
            .map(dto -> dto.getId())
            .collect(Collectors.toList());
        context.putInContextData(EpgContextDataKey.UPDATED_IDS, deletedIds);
        selectionToolService.setSelectionType(context, null);
        selectionToolService.setSelectionList(context, new HashMap<>());
        context.getMessageQueue().addToastSuccess(ResourceHelper.getString("outilSelection.action.vider.message"));
    }

    // Méthode permettant de valider l'étape et afficher les messsages (error ou success) dans le context
    @Override
    public void validerEtapeAndAddMessage(SpecificContext context) {
        try {
            validerEtape(context);
            deverouillerEtAfficherMessage(context, "label.epg.feuilleRoute.message.avisFavorable");
        } catch (EPGException e) {
            LOGGER.error(STLogEnumImpl.DEFAULT, e);
            context.getMessageQueue().addErrorToQueue(e.getMessage());
        }
    }

    @Override
    public void validerEtapeMassSelection(SpecificContext context) {
        List<SelectionDto> selectionList = EpgUIServiceLocator.getEpgSelectionToolUIService().getSelectionList(context);
        List<String> deletedIds = new ArrayList<>();
        EpgSelectionTypeEnum selectionType = EpgUIServiceLocator
            .getEpgSelectionToolUIService()
            .getSelectionType(context);

        if (selectionType == EpgSelectionTypeEnum.DOSSIER) {
            List<String> norErreurs = new ArrayList<>();
            for (SelectionDto dto : selectionList) {
                context.setCurrentDocument(dto.getId());
                final Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
                try {
                    verouillerDossier(context);
                    if (VocabularyConstants.STATUT_INITIE.equals(dossier.getStatut())) {
                        lancerDossier(context);
                    } else {
                        validerEtape(context);
                    }
                    deletedIds.add(EpgUIServiceLocator.getEpgSelectionToolUIService().removeDocIdFromMap(context));
                } catch (EPGException e) {
                    LOGGER.error(STLogEnumImpl.DEFAULT, e);
                    norErreurs.add(dossier.getNumeroNor());
                } finally {
                    if (LockUtils.isLockedByCurrentUser(context.getSession(), context.getCurrentDocument().getRef())) {
                        STActionsServiceLocator.getDossierLockActionService().unlockCurrentDossier(context);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(deletedIds)) {
                // Stocker la liste des id a mettre à jour au niveau du front
                context.putInContextData(EpgContextDataKey.UPDATED_IDS, deletedIds);
            }
            if (CollectionUtils.isEmpty(norErreurs)) {
                context
                    .getMessageQueue()
                    .addToastSuccess(ResourceHelper.getString("outilSelection.action.valider.etape.success"));
            } else {
                context
                    .getMessageQueue()
                    .addErrorToQueue(
                        ResourceHelper.getString(
                            "outilSelection.action.valider.etape.error",
                            norErreurs.stream().collect(Collectors.joining(","))
                        )
                    );
            }
        } else {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("outilSelection.action.valider.etape.error.type.fdr"));
        }
    }

    private void validerEtape(SpecificContext context) {
        final CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        checkValiditeDossier(context);
        checkCompatibilitePoste(context, "");
        checkValiditeTypeActe(context);
        checkTypePublicationFromDossier(context);
        checkLastStepVecteurPublication(context);
        // Change le statut en publié si l'étape en cours est "Pour publication à la
        // DILA JO"
        final EPGFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
        final List<DocumentModel> runningSteps = feuilleRouteService.getRunningSteps(
            session,
            dossier.getLastDocumentRoute()
        );

        boolean dossierLinkHasBeenValidated = false;
        if (CollectionUtils.size(runningSteps) == 1) {
            SSRouteStep routeStep = runningSteps.get(0).getAdapter(SSRouteStep.class);
            if (
                VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(routeStep.getType()) &&
                !VocabularyConstants.STATUT_PUBLIE.equals(dossier.getStatut())
            ) {
                new UnrestrictedSessionRunner(session) {

                    @Override
                    public void run() {
                        DossierService dossierService = SolonEpgServiceLocator.getDossierService();
                        dossierService.publierDossier(dossier, session, true);
                        dossier.save(session);
                    }
                }
                .runUnrestricted();

                dossierLinkHasBeenValidated = true;
            }
        }
        dossierDoc = dossier.getDocument();
        // reattach user session
        dossierDoc.detach(false);
        dossierDoc.attach(session.getSessionId());

        // Valide l'étape avec avis favorable
        if (!dossierLinkHasBeenValidated) {
            final STDossierLink dossierLink = SolonEpgActionServiceLocator
                .getEpgCorbeilleActionService()
                .getCurrentDossierLink(context);
            final DocumentModel dossierLinkDoc = dossierLink.getDocument();
            DossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
            dossierDistributionService.validerEtape(session, dossier.getDocument(), dossierLinkDoc);
        }
    }

    /*
     * Vérifie la présence d'un dossier et d'un DossierLink correspondant en session (blindage).
     */
    private void checkDossierLink(SpecificContext context) {
        final EpgCorbeilleActionService corbeilleActions = SolonEpgActionServiceLocator.getEpgCorbeilleActionService();
        final STDossierLink currentDossierLink = corbeilleActions.getCurrentDossierLink(context);
        final DocumentModel currentDossier = context.getCurrentDocument();
        if (
            currentDossier == null ||
            currentDossierLink == null ||
            !currentDossierLink.getDossierId().equals(currentDossier.getId())
        ) {
            String message = ResourceHelper.getString("label.epg.feuilleRoute.message.checkdossierlink");
            LOGGER.error(STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC, message);
            throw new EPGException(message);
        }
    }

    /*
     * Vérifie si le dossier est toujours verrouillé et peut être déverrouillé par l'utilisateur courant.
     */
    private void checkCanUnlockCurrentDossier(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentRef docRef = context.getCurrentDocument().getRef();
        DossierLockActionService dossierLockActions = STActionsServiceLocator.getDossierLockActionService();
        if (LockUtils.isLocked(session, docRef) && !dossierLockActions.getCanUnlockCurrentDossier(context)) {
            final String message = ResourceHelper.getString("epg.dossier.lock.alreadyLocked.error");
            LOGGER.error(STLogEnumImpl.FAIL_UNLOCK_DOC_TEC, message);
            throw new EPGException(message);
        }
    }

    private boolean checkCanLockCurrentDossier(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentRef docRef = context.getCurrentDocument().getRef();
        Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
        DossierLockActionService dossierLockActions = STActionsServiceLocator.getDossierLockActionService();
        if (
            LockUtils.isLockedByAnotherUser(session, docRef) ||
            !LockUtils.isLocked(session, docRef) &&
            !dossierLockActions.getCanLockCurrentDossier(context)
        ) {
            final String message = ResourceHelper.getString("epg.dossier.lock.right.error", dossier.getNumeroNor());
            context.getMessageQueue().addErrorToQueue(message);
            LOGGER.error(STLogEnumImpl.FAIL_UNLOCK_DOC_TEC, message);
            throw new EPGException(message);
        }
        return true;
    }

    private static PublicationIntOuExtType getTypePublicationFromDossier(Dossier dossier) {
        String publicationIntegraleOuExtrait = dossier.getPublicationIntegraleOuExtrait();
        if (VocabularyConstants.TYPE_PUBLICATION_INTEGRAL.equals(publicationIntegraleOuExtrait)) {
            return PublicationIntOuExtType.IN_EXTENSO;
        } else if (VocabularyConstants.TYPE_PUBLICATION_EXTRAIT.equals(publicationIntegraleOuExtrait)) {
            return PublicationIntOuExtType.EXTRAIT;
        }
        return null;
    }

    @Override
    public void validerEtapeCorrection(SpecificContext context) {
        final CoreSession session = context.getSession();
        try {
            checkValiditeDossier(context);
            checkCompatibilitePoste(context, "");
            checkValiditeTypeActe(context);

            // Valide l'étape avec correction
            EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
            final STDossierLink dossierLink = SolonEpgActionServiceLocator
                .getEpgCorbeilleActionService()
                .getCurrentDossierLink(context);

            final DocumentModel dossierLinkDoc = dossierLink.getDocument();
            dossierDistributionService.validerEtapeCorrection(session, context.getCurrentDocument(), dossierLinkDoc);

            deverouillerEtAfficherMessage(context, "dossier.valider.avec.correction.success");
        } catch (EPGException e) {
            LOGGER.error(STLogEnumImpl.DEFAULT, e);
            context.getMessageQueue().addErrorToQueue(e.getMessage());
        }
    }

    @Override
    public void validerEtapeNonConcerne(SpecificContext context) {
        final CoreSession session = context.getSession();
        final boolean ajoutEtape = BooleanUtils.toBoolean(
            (Boolean) context.getFromContextData(EpgContextDataKey.ADD_STEP)
        );
        final Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
        try {
            checkValiditeDossier(context, ajoutEtape);
            if (
                !ajoutEtape &&
                !checkCompatibilitePoste(
                    context,
                    ResourceHelper.getString("label.epg.feuilleRoute.message.defaut.etape.nonconcerne")
                )
            ) {
                return;
            }
            checkValiditeTypeActe(context);

            // Valide l'étape avec "non concerné"
            final STDossierLink dossierLink = SolonEpgActionServiceLocator
                .getEpgCorbeilleActionService()
                .getCurrentDossierLink(context);
            final DocumentModel dossierLinkDoc = dossierLink.getDocument();

            EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
            if (ajoutEtape) {
                dossierDistributionService.validerEtapeNonConcerneAjoutEtape(
                    session,
                    dossier.getDocument(),
                    dossierLinkDoc
                );
            } else {
                dossierDistributionService.validerEtapeNonConcerne(session, dossier.getDocument(), dossierLinkDoc);
            }

            deverouillerEtAfficherMessage(context, "dossier.valider.non.concerne.message.succes");
        } catch (EPGException e) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("dossier.valider.non.concerne.message.error.add.step"));
            LOGGER.error(STLogEnumImpl.FAIL_CREATE_STEP_TEC, e);
        }
    }

    @Override
    public void validerEtapeRefus(SpecificContext context) throws NuxeoException {
        final CoreSession session = context.getSession();
        final Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
        try {
            checkValiditeDossier(context);
            checkCompatibilitePoste(context, "");
            checkValiditeTypeActe(context);

            // Valide l'étape avec refus de validation
            EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
            final STDossierLink dossierLink = SolonEpgActionServiceLocator
                .getEpgCorbeilleActionService()
                .getCurrentDossierLink(context);
            dossierDistributionService.validerEtapeRefus(session, dossier.getDocument(), dossierLink.getDocument());

            deverouillerEtAfficherMessage(context, "label.epg.feuilleRoute.message.avisDefavorable");
        } catch (EPGException e) {
            LOGGER.error(STLogEnumImpl.DEFAULT, e);
            context.getMessageQueue().addErrorToQueue(e.getMessage());
        }
    }

    @Override
    public void validerEtapeRetourModification(SpecificContext context) {
        final boolean ajoutEtape = context.getFromContextData(EpgContextDataKey.ADD_STEP);
        final String posteDAN = context.getFromContextData(EpgContextDataKey.ID_POSTE_RETOUR_DAN);
        checkValiditeDossier(context);
        checkCompatibilitePoste(context, "");

        final CoreSession session = context.getSession();
        final Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);

        EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        final STDossierLink dossierLink = SolonEpgActionServiceLocator
            .getEpgCorbeilleActionService()
            .getCurrentDossierLink(context);
        try {
            if (ajoutEtape) {
                dossierDistributionService.validerEtapeRetourModificationAjoutEtape(
                    session,
                    dossier.getDocument(),
                    dossierLink.getDocument(),
                    posteDAN
                );
            } else {
                dossierDistributionService.validerEtapeRetourModification(
                    session,
                    dossier.getDocument(),
                    dossierLink.getDocument()
                );
            }
            deverouillerEtAfficherMessage(context, "label.epg.feuilleRoute.message.retourModification");
        } catch (EPGException | FeuilleRouteNotLockedException e) {
            LOGGER.error(STLogEnumImpl.FAIL_CREATE_STEP_TEC, e);
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("dossier.valider.retour.modification.error.modal.message"));
        }
    }

    private void checkValiditeTypeActe(SpecificContext context) {
        final CoreSession session = context.getSession();
        final EpgCorbeilleActionService corbeilleActions = SolonEpgActionServiceLocator.getEpgCorbeilleActionService();
        final STDossierLink dossierLink = corbeilleActions.getCurrentDossierLink(context);

        final DocumentModel stepDoc = session.getDocument(new IdRef(dossierLink.getRoutingTaskId()));
        final Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
        if (
            BooleanUtils.isTrue(SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(dossier.getTypeActe())) &&
            !SolonEpgServiceLocator
                .getEPGFeuilleRouteService()
                .isNextStepCompatibleWithActeTxtNonPub(session, dossier.getLastDocumentRoute(), stepDoc)
        ) {
            String message = ResourceHelper.getString("feedback.solonepg.feuilleRoute.error.nextStep.typeActe");
            context.getMessageQueue().addErrorToQueue(message);
            LOGGER.error(STLogEnumImpl.DEFAULT, message);
        }
    }

    private void checkTypePublicationFromDossier(SpecificContext context) {
        final Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
        final ActeService acteService = SolonEpgServiceLocator.getActeService();
        if (getTypePublicationFromDossier(dossier) == null && acteService.isPublicationVisible(dossier.getTypeActe())) {
            String message = ResourceHelper.getString("feedback.solonepg.feuilleRoute.error.nextStep.typePublication");
            context.getMessageQueue().addErrorToQueue(message);
            LOGGER.error(STLogEnumImpl.DEFAULT, message);
        }
    }

    private void checkValiditeDossier(SpecificContext context) {
        checkValiditeDossier(context, false);
    }

    private void checkValiditeDossier(SpecificContext context, boolean isAddingStep) {
        checkCanUnlockCurrentDossier(context);
        checkDossierLink(context);
        final Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
        final List<DocumentModel> nextStepsDoc = getNextStepsDoc(context);
        EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        // Lance une exception si le dossier n'est pas valide
        if (isAddingStep) {
            dossierDistributionService.checkDossierBeforeValidateStepWithAddingStep(
                context.getSession(),
                dossier,
                nextStepsDoc
            );
        } else {
            dossierDistributionService.checkDossierBeforeValidateStep(context.getSession(), dossier, nextStepsDoc);
        }
    }

    private boolean checkCompatibilitePoste(SpecificContext context, String errorCompatibility) {
        boolean isCompatible = true;

        final CoreSession session = context.getSession();
        final List<DocumentModel> nextStepsDoc = getNextStepsDoc(context);
        if (CollectionUtils.isNotEmpty(nextStepsDoc)) {
            String erreurCompatibility = SolonEpgServiceLocator
                .getDocumentRoutingService()
                .isNextPostCompatibleWithNextStep(session, nextStepsDoc.get(0).getAdapter(SSRouteStep.class));

            if (StringUtils.isNotEmpty(erreurCompatibility)) {
                context.getMessageQueue().addErrorToQueue(erreurCompatibility);
                LOGGER.error(STLogEnumImpl.DEFAULT, erreurCompatibility);
                isCompatible = false;
            }
        } else if (StringUtils.isNotEmpty(errorCompatibility)) {
            context.getMessageQueue().addErrorToQueue(errorCompatibility);
            LOGGER.error(STLogEnumImpl.DEFAULT, errorCompatibility);
            isCompatible = false;
        }
        return isCompatible;
    }

    private void checkNextSteps(SpecificContext context) {
        CoreSession session = context.getSession();
        final EpgCorbeilleActionService corbeilleActions = SolonEpgActionServiceLocator.getEpgCorbeilleActionService();
        final STDossierLink dossierLink = corbeilleActions.getCurrentDossierLink(context);
        SSRouteStep currentStep = session
            .getDocument(new IdRef(dossierLink.getRoutingTaskId()))
            .getAdapter(SSRouteStep.class);
        final List<DocumentModel> nextStepsDoc = getNextStepsDoc(context);
        if (
            VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION.equals(currentStep.getType()) &&
            CollectionUtils.isEmpty(nextStepsDoc)
        ) {
            String message = ResourceHelper.getString("dossier.action.valider.erreur.fdr.vide");
            LOGGER.error(STLogEnumImpl.DEFAULT, message);
            throw new EPGException(message);
        }
    }

    private void deverouillerEtAfficherMessage(SpecificContext context, String key) {
        final Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
        STActionsServiceLocator.getDossierLockActionService().unlockCurrentDossier(context);

        final String message = ResourceHelper.getString(key, dossier.getNumeroNor());
        context.getMessageQueue().addToastSuccess(message);
    }

    private void verouillerDossier(SpecificContext context) {
        if (checkCanLockCurrentDossier(context)) {
            STActionsServiceLocator.getDossierLockActionService().lockCurrentDossier(context);
        }
    }

    private void messageConfirmation(SpecificContext context, String key, Dossier dossier) {
        final String message = ResourceHelper.getString(key, dossier.getNumeroNor());
        context.getMessageQueue().addInfoToQueue(message);
    }

    private List<DocumentModel> getNextStepsDoc(SpecificContext context) {
        final CoreSession session = context.getSession();
        final Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
        final EpgCorbeilleActionService corbeilleActions = SolonEpgActionServiceLocator.getEpgCorbeilleActionService();
        final STDossierLink dossierLink = corbeilleActions.getCurrentDossierLink(context);

        final DocumentModel stepDoc = session.getDocument(new IdRef(dossierLink.getRoutingTaskId()));
        final EPGFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
        return feuilleRouteService.findNextSteps(session, dossier.getLastDocumentRoute(), stepDoc, null);
    }

    @Override
    public boolean canSendSaisineOrPieceComplementaire(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentRef docRef = context.getCurrentDocument().getRef();
        boolean isLocked = false;
        try {
            isLocked = LockUtils.isLockedByCurrentUser(session, docRef);
        } catch (final NuxeoException exc) {
            String message = ResourceHelper.getString("epg.bordereau.error.lock");
            context.getMessageQueue().addErrorToQueue(message);
            LOGGER.error(STLogEnumImpl.DEFAULT, exc, message);
        }
        return (
            session
                .getPrincipal()
                .isMemberOf(SolonEpgBaseFunctionConstant.CONSEIL_ETAT_ENVOI_SAISINE_PIECE_COMPLEMENTAIRE_EXECUTOR) &&
            isLocked
        );
    }

    @Override
    public boolean checkChargesDeMissionAvantDILAJO(SpecificContext context) {
        final CoreSession session = context.getSession();
        final EpgCorbeilleActionService corbeilleActions = SolonEpgActionServiceLocator.getEpgCorbeilleActionService();
        final STDossierLink dossierLink = corbeilleActions.getCurrentDossierLink(context);
        final DocumentModel dossierDoc = context.getCurrentDocument();

        // On ne checke que les documents verrouillés pour optimiser
        if (dossierLink != null && LockUtils.isLocked(session, dossierDoc.getRef())) {
            final DocumentModel stepDoc = session.getDocument(new IdRef(dossierLink.getRoutingTaskId()));

            final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            final EPGFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
            final List<DocumentModel> nextStepsDoc = feuilleRouteService.findNextSteps(
                session,
                dossier.getLastDocumentRoute(),
                stepDoc,
                null
            );

            // SI dernière étape on ne traite pas
            if (CollectionUtils.isNotEmpty(nextStepsDoc)) {
                // Si la prochaine étape n'est pas Publication DILA JO on retourne true
                final SolonEpgRouteStep nextStep = nextStepsDoc.get(0).getAdapter(SolonEpgRouteStep.class);
                if (!VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(nextStep.getType())) {
                    return true;
                }

                // Récupération des étapes conidérées pour le controle
                final EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
                Collection<DocumentModel> controlledSteps = dossierDistributionService.findChargesDeMissionSteps(
                    session,
                    stepDoc
                );

                return controlledSteps
                    .stream()
                    .map(controlledStep -> controlledStep.getAdapter(SolonEpgRouteStep.class))
                    .noneMatch(etape -> etape.isDone() && !isEtapeAvisFavorable(etape));
            }
        }
        return true;
    }

    private boolean isEtapeAvisFavorable(SolonEpgRouteStep etape) {
        return (
            SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_CORRECTION_VALUE.equals(
                etape.getValidationStatus()
            ) ||
            SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE.equals(
                etape.getValidationStatus()
            )
        );
    }

    @Override
    public boolean checkPublicationConjointeAvantDILAJO(SpecificContext context) {
        final CoreSession session = context.getSession();
        DocumentRef docRef = context.getCurrentDocument().getRef();
        final EpgCorbeilleActionService corbeilleActions = SolonEpgActionServiceLocator.getEpgCorbeilleActionService();
        final STDossierLink dossierLink = corbeilleActions.getCurrentDossierLink(context);
        // FEV 391_5: sur une publication conjointe, en "Pour publication DILA JO"
        if (dossierLink != null && LockUtils.isLocked(session, docRef)) {
            final DocumentModel dossierDoc = context.getCurrentDocument();
            final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            final DocumentModel stepDoc = session.getDocument(new IdRef(dossierLink.getRoutingTaskId()));
            List<String> pubsConjointes = dossier.getPublicationsConjointes();
            if (CollectionUtils.isNotEmpty(pubsConjointes)) {
                final EPGFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
                final List<DocumentModel> nextStepsDoc = feuilleRouteService.findNextSteps(
                    session,
                    dossier.getLastDocumentRoute(),
                    stepDoc,
                    null
                );
                return nextStepsDoc
                    .stream()
                    .map(s -> s.getAdapter(SolonEpgRouteStep.class))
                    .anyMatch(s -> VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(s.getType()));
            }
        }
        return false;
    }

    @Override
    public void copierFdrDossier(SpecificContext context) {
        final CoreSession session = context.getSession();
        String norDossierCopieFdr = context.getFromContextData(EpgContextDataKey.NOR_TO_COPIE);
        EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        SSJournalService journalService = SSServiceLocator.getSSJournalService();

        DocumentModel dossierDoc = context.getCurrentDocument();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        String poste = dossier.getCreatorPoste();
        String posteId = poste.substring(6);
        if (isNorFdrCopieValide(context, norDossierCopieFdr)) {
            dossierDistributionService.startRouteCopieFromDossier(session, dossierDoc, posteId, norDossierCopieFdr);
            final String message = ResourceHelper.getString("message.epg.dossier.copie.fdr");
            context.getMessageQueue().addInfoToQueue(message);
            journalService.journaliserActionFDR(
                session,
                dossierDoc,
                STEventConstant.EVENT_COPIE_FDR_DEPUIS_DOSSIER,
                ResourceHelper.getString(STEventConstant.COMMENT_COPIE_FDR_DEPUIS_DOSSIER, norDossierCopieFdr)
            );
            // Relock le dossier
            final DossierLockActionService dossierLockActionService = STActionsServiceLocator.getDossierLockActionService();
            if (dossierLockActionService.getCanLockCurrentDossier(context)) {
                dossierLockActionService.lockCurrentDossier(context);
            }
        }
    }

    @Override
    public void doEnvoiPieceComplementaire(SpecificContext context) {
        final CoreSession session = context.getSession();
        final FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();
        String comment = ResourceHelper.getString("epg.documents.piece.complementaires.envoi");
        List<FondDeDossierFile> listFDDFileModifiesFDD = prepareEnvoiPieceComplementaire(context);
        fddService.sendPiecesComplementaires(session, context.getCurrentDocument(), listFDDFileModifiesFDD, comment);
        context
            .getMessageQueue()
            .addToastSuccess(ResourceHelper.getString("saisine.rectificative.pieces.complementaires.success.message"));
    }

    @Override
    public void doSaisineRectificative(SpecificContext context) {
        final CoreSession session = context.getSession();
        final FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();
        String comment = ResourceHelper.getString("epg.documents.saisine.rectificative.fichiers");
        List<FondDeDossierFile> listFDDFileModifiesSaisineRectificative = prepareSaisineRectificative(context);
        fddService.sendSaisineRectificative(
            session,
            context.getCurrentDocument(),
            listFDDFileModifiesSaisineRectificative,
            comment
        );
        context.getMessageQueue().addToastSuccess(ResourceHelper.getString("saisine.rectificative.success.message"));
    }

    @Override
    public String getActionFeuilleRouteButtonLabel(SpecificContext context, String labelKey) {
        final EpgCorbeilleActionService corbeilleActions = SolonEpgActionServiceLocator.getEpgCorbeilleActionService();
        final STDossierLink currentDossierLink = corbeilleActions.getCurrentDossierLink(context);
        String routingTaskType = "";
        if (currentDossierLink != null) {
            routingTaskType = currentDossierLink.getRoutingTaskType();
        }

        return getActionFeuilleRouteButtonLabel(context, routingTaskType, labelKey);
    }

    @Override
    public String getActionFeuilleRouteButtonLabel(SpecificContext context, String routingTaskType, String labelKey) {
        final String key = labelKey + "." + routingTaskType;
        final String label = ResourceHelper.getString(key);
        if (!key.equals(label)) {
            return label;
        } else {
            return ResourceHelper.getString(labelKey);
        }
    }

    @Override
    public Calendar getDateDerniereSaisineRectificative(SpecificContext context, Dossier dossier) {
        return getDateDerniere(context, dossier, TypeModification.SAISINE_RECTIFICATIVE);
    }

    @Override
    public Calendar getDateDerniereTransmissionPiecesComplementaires(SpecificContext context, Dossier dossier) {
        return getDateDerniere(context, dossier, TypeModification.PIECE_COMPLEMENTAIRE);
    }

    private Calendar getDateDerniere(SpecificContext context, Dossier dossier, TypeModification type) {
        final CoreSession session = context.getSession();
        FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();

        List<DocumentModel> jetonListDoc = fddService.getListSaisineRectificativeOuListTransmissionPiecesComplementaireForDossier(
            dossier,
            session,
            type.name()
        );
        // On renseigne par défaut la date du début de l'étape pour avis CE
        Calendar dateTemporaire = fddService.getStartStepPourAvisCE(dossier.getDocument(), session);
        return jetonListDoc
            .stream()
            .map(doc -> attachSessionIfNecessary(doc, session))
            .map(j -> j.getAdapter(JetonDoc.class))
            .map(JetonDoc::getCreated)
            .max(Calendar::compareTo)
            .orElse(dateTemporaire);
    }

    @Override
    public List<VocabularyEntry> getRetourDANList(SpecificContext context) {
        final CoreSession session = context.getSession();
        final TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();

        return tableReferenceService.getRetoursDAN(session);
    }

    @Override
    public boolean isCurrentDossierPourInitialisation(SpecificContext context) {
        final DocumentModel doc = context.getCurrentDocument();
        return doc.getCurrentLifeCycleState().equals(INIT);
    }

    @Override
    public boolean isEtapePourImpressionUpdater(SpecificContext context) {
        // Si l'utilisateur n'a pas chargé le DossierLink, ce test est non appliquable
        final STDossierLink dossierLink = SolonEpgActionServiceLocator
            .getEpgCorbeilleActionService()
            .getCurrentDossierLink(context);

        final NuxeoPrincipal ssPrincipal = context.getSession().getPrincipal();
        final boolean isAdmin =
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UPDATER) ||
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_MIN_UPDATER);

        return (
            isAdmin ||
            dossierLink == null ||
            !VocabularyConstants.ROUTING_TASK_TYPE_IMPRESSION.equals(dossierLink.getRoutingTaskType())
        );
    }

    @Override
    public boolean isNorFdrCopieValide(SpecificContext context, String norDossierCopieFdr) {
        NORService norService = SolonEpgServiceLocator.getNORService();
        if (norService.getDossierFromNORWithACL(context.getSession(), norDossierCopieFdr) != null) {
            return true;
        } else {
            final String message = ResourceHelper.getString("label.epg.dossier.copier.fdr.error.nor");
            context.getMessageQueue().addErrorToQueue(message);
            LOGGER.error(STLogEnumImpl.DEFAULT, message);
            return false;
        }
    }

    // Méthode permettant de lancer le dossier et ajouter les message (error ou success) dans le context
    @Override
    public void lancerDossierAndAddMessage(SpecificContext context) {
        try {
            lancerDossier(context);
            final DocumentModel dossierDoc = context.getCurrentDocument();
            messageConfirmation(
                context,
                "label.epg.feuilleRoute.message.dossierLance",
                dossierDoc.getAdapter(Dossier.class)
            );
        } catch (EPGException e) {
            LOGGER.error(STLogEnumImpl.DEFAULT, e);
            context.getMessageQueue().addErrorToQueue(e.getMessage());
        }
    }

    private void lancerDossier(SpecificContext context) {
        checkCanUnlockCurrentDossier(context);
        checkDossierLink(context);
        checkNextSteps(context);
        final STDossierLink currentDossierLink = SolonEpgActionServiceLocator
            .getEpgCorbeilleActionService()
            .getCurrentDossierLink(context);

        if (currentDossierLink == null) {
            String message = ResourceHelper.getString("feedback.solonepg.feuilleRoute.message.dossierLance.error");
            LOGGER.error(STLogEnumImpl.DEFAULT, message);
            throw new EPGException(message);
        } else {
            final DocumentModel dossierDoc = context.getCurrentDocument();
            checkValiditeDossier(context);
            STActionsServiceLocator.getDossierLockActionService().unlockCurrentDossier(context);
            // Lance le dossier
            final EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
            final DocumentModel dossierLinkDoc = currentDossierLink.getDocument();
            dossierDistributionService.lancerDossier(context.getSession(), dossierDoc, dossierLinkDoc);
        }
    }

    @Override
    public void norAttribue(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        if (BooleanUtils.isTrue(SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(dossier.getTypeActe()))) {
            // Affiche un message de non possibilité d'action
            final String message = ResourceHelper.getString(
                "feedback.solonepg.action.impossible.norAttribue",
                dossier.getNumeroNor()
            );
            LOGGER.error(STLogEnumImpl.DEFAULT, message);
            context.getMessageQueue().addErrorToQueue(message);
        }

        if (StringUtils.isNotBlank(dossier.getTitreActe())) {
            STActionsServiceLocator.getDossierLockActionService().unlockCurrentDossier(context);

            // Passe le dossier à l'état "NOR attribué"
            final EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
            dossierDistributionService.norAttribue(context.getSession(), dossierDoc);

            messageConfirmation(context, "dossier.action.nor.attribuer", dossier);
        } else {
            String message = ResourceHelper.getString("dossier.action.valider.nor.attribuer.error.titreActe.manquant");
            LOGGER.error(STLogEnumImpl.DEFAULT, message);
            context.getMessageQueue().addErrorToQueue(message);
        }
    }

    @Override
    public void redemarrerDossier(SpecificContext context) {
        // Vérifie si le dossier est toujours verrouillé par l'utilisateur
        CoreSession session = context.getSession();
        try {
            checkCanUnlockCurrentDossier(context);
            // Redémarre le dossier
            final EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
            final DocumentModel dossierDoc = context.getCurrentDocument();
            dossierDoc.getAdapter(Dossier.class);

            dossierDistributionService.restartDossier(
                session,
                dossierDoc,
                ObjectHelper.requireNonNullElse(
                    context.getFromContextData(EpgContextDataKey.NEXT_STATUT),
                    VocabularyConstants.STATUT_LANCE
                )
            );

            SSServiceLocator
                .getSSJournalService()
                .journaliserActionFDR(
                    session,
                    dossierDoc,
                    STEventConstant.EVENT_FEUILLE_ROUTE_RESTART,
                    STEventConstant.COMMENT_FEUILLE_ROUTE_RESTART
                );
            deverouillerEtAfficherMessage(context, "epg.distribution.action.restart.success");
        } catch (EPGException e) {
            LOGGER.error(STLogEnumImpl.DEFAULT, e);
            context.getMessageQueue().addErrorToQueue(e.getMessage());
        }
    }

    @Override
    public void relancerDossierAbandonne(SpecificContext context) {
        STActionsServiceLocator.getDossierLockActionService().unlockCurrentDossier(context);

        // Redémarre le dossier
        final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        final DocumentModel dossierDoc = context.getCurrentDocument();
        dossierService.restartAbandonDossier(context.getSession(), dossierDoc);

        String message = ResourceHelper.getString("epg.distribution.action.restart.success");
        context.getMessageQueue().addSuccessToQueue(message);
    }

    @Override
    public void substituerRoute(SpecificContext context) {
        final CoreSession session = context.getSession();
        DocumentModel newRouteDoc = session.getDocument(
            new IdRef(context.getFromContextData(SSContextDataKey.ID_MODELE))
        );

        if (newRouteDoc == null) {
            // Aucun modèle n'est sélectionné
            context
                .getMessageQueue()
                .addWarnToQueue(ResourceHelper.getString("feedback.reponses.document.route.no.valid.route"));
            return;
        }

        // vérifie la compatibilité des postes avec l'étapde suivante
        final EPGFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
        DocumentModel initStep = feuilleRouteService.getEtapePourInitialisation(
            session,
            newRouteDoc.getAdapter(FeuilleRoute.class)
        );

        List<DocumentModel> steps = feuilleRouteService.findNextSteps(session, newRouteDoc.getId(), initStep, null);
        String errorCompatibility = SolonEpgServiceLocator
            .getDocumentRoutingService()
            .isNextPostsCompatibleWithNextStep(session, steps);

        if (StringUtils.isNotEmpty(errorCompatibility)) {
            LOGGER.error(STLogEnumImpl.DEFAULT, errorCompatibility);
            context.getMessageQueue().addErrorToQueue(errorCompatibility);
            return;
        }

        // Récupère l'ancienne instance de feuille de route
        DocumentModel currentDoc = context.getCurrentDocument();
        EpgDocumentRoutingActionService routingActions = SolonEpgActionServiceLocator.getEpgDocumentRoutingActionService();
        final SSFeuilleRoute oldRoute = routingActions.getRelatedRoute(session, currentDoc);
        final DocumentModel oldRouteDoc = oldRoute.getDocument();
        SSJournalService journalService = SSServiceLocator.getSSJournalService();

        // Substitue la feuille de route
        final EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        final DocumentModel newFeuilleRoute = dossierDistributionService.substituerFeuilleRoute(
            session,
            context.getCurrentDocument(),
            oldRouteDoc,
            newRouteDoc,
            STVocabularyConstants.FEUILLE_ROUTE_TYPE_CREATION_SUBSTITUTION
        );

        // reverrouille la FDR si le dossier est verrouillé
        if (LockUtils.isLocked(session, currentDoc.getRef())) {
            try {
                final DocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
                documentRoutingService.lockDocumentRoute(newFeuilleRoute.getAdapter(SSFeuilleRoute.class), session);
            } catch (FeuilleRouteAlreadyLockedException e) {
                LOGGER.error(STLogEnumImpl.DEFAULT, e);
                context.getMessageQueue().addErrorToQueue(e.getMessage());
            }
        }

        journalService.journaliserActionFDR(
            session,
            context.getCurrentDocument(),
            SolonEpgEventConstant.EVENT_SUBSTITUTION_FDR,
            SolonEpgEventConstant.COMMENT_SUBSTITUTION_FDR
        );
        context
            .getMessageQueue()
            .addToastSuccess(ResourceHelper.getString("epg.dossierDistribution.action.substituer.success"));
    }

    @Override
    public List<FondDeDossierFile> prepareEnvoiPieceComplementaire(SpecificContext context) {
        FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();
        final DocumentModel currentDossier = context.getCurrentDocument();
        Dossier dossier = currentDossier.getAdapter(Dossier.class);

        Calendar dateDernieretransmissionPiecesComplementaires = getDateDerniereTransmissionPiecesComplementaires(
            context,
            dossier
        );
        return fddService.getAllUpdatedFilesFDDNotSaisine(
            context.getSession(),
            dossier,
            dateDernieretransmissionPiecesComplementaires
        );
    }

    @Override
    public List<FondDeDossierFile> prepareSaisineRectificative(SpecificContext context) {
        FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();
        final DocumentModel currentDossier = context.getCurrentDocument();
        Dossier dossier = currentDossier.getAdapter(Dossier.class);
        Calendar dateDerniereSaisineRectificative = getDateDerniereSaisineRectificative(context, dossier);
        return fddService.getAllUpdatedFilesFDDInSaisine(
            context.getSession(),
            dossier,
            dateDerniereSaisineRectificative
        );
    }

    private DocumentModel attachSessionIfNecessary(DocumentModel doc, CoreSession session) {
        if (doc.getCoreSession() == null) {
            doc.detach(false);
            doc.attach(session.getSessionId());
        }
        return doc;
    }

    private void checkLastStepVecteurPublication(SpecificContext context) {
        final List<DocumentModel> nextStepsDoc = getNextStepsDoc(context);
        // Controle uniquement s'il s'agit de la dernière étape (i.e. pas d'étapes suivantes)
        if (CollectionUtils.isEmpty(nextStepsDoc)) {
            final EpgCorbeilleActionService corbeilleActions = SolonEpgActionServiceLocator.getEpgCorbeilleActionService();
            final STDossierLink dossierLink = corbeilleActions.getCurrentDossierLink(context);
            CoreSession session = context.getSession();
            String currentStepType = dossierLink.getRoutingTaskType();
            final BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
            List<String> vecteursPublications = bulletinOfficielService.convertVecteurIdListToLabels(
                session,
                context.getCurrentDocument().getAdapter(Dossier.class).getVecteurPublication()
            );

            if (
                CollectionUtils.isNotEmpty(vecteursPublications) &&
                isVecteurPublicationIncompatibleWithLastStepType(currentStepType, vecteursPublications)
            ) {
                String message = ResourceHelper.getString("dossier.action.valider.erreur.vecteur.publication");
                throw new EPGException(message);
            }
        }
    }

    private boolean isVecteurPublicationIncompatibleWithLastStepType(
        String currentStepType,
        List<String> vecteursPublications
    ) {
        return (
            CollectionUtils.containsAny(VECTEURS_PUBLICATION_JO_LIST, vecteursPublications) &&
            !VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(currentStepType) ||
            vecteursPublications.contains(EpgVecteurPublication.BODMR.getIntitule()) &&
            !VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO.equals(currentStepType)
        );
    }
}
