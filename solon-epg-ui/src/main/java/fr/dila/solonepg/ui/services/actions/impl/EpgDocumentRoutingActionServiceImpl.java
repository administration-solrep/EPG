package fr.dila.solonepg.ui.services.actions.impl;

import static fr.dila.st.core.util.ResourceHelper.getString;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.feuilleroute.SqueletteStepTypeDestinataire;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.actions.EpgDocumentRoutingActionService;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.core.groupcomputer.MinistereGroupeHelper;
import fr.dila.ss.ui.bean.fdr.CreationEtapeLineDTO;
import fr.dila.ss.ui.enums.EtapeLifeCycle;
import fr.dila.ss.ui.enums.FeuilleRouteEtapeOrder;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.actions.SSActionsServiceLocator;
import fr.dila.ss.ui.services.actions.impl.SSDocumentRoutingActionServiceImpl;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.core.exception.STValidationException;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteElement;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public class EpgDocumentRoutingActionServiceImpl
    extends SSDocumentRoutingActionServiceImpl
    implements EpgDocumentRoutingActionService {
    private static final ImmutableList<String> ETAPE_TYPE_VALIDE_RETOUR_MODIF = ImmutableList.of(
        VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE,
        VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SGG,
        VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO,
        VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO
    );

    private static final ImmutableList<String> ETAPE_TYPE_VALIDE_NON_CONCERNE = ImmutableList.of(
        VocabularyConstants.ROUTING_TASK_TYPE_AVIS,
        VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
        VocabularyConstants.ROUTING_TASK_TYPE_CONTRESEING,
        VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SGG,
        VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION,
        VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_BO,
        VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE
    );

    private static final ImmutableList<String> ETAPE_TYPE_REFUS = ImmutableList.of(
        VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
        VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SGG,
        VocabularyConstants.ROUTING_TASK_TYPE_CONTRESEING,
        VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER,
        VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_BO,
        VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE
    );
    private static final String HIDDEN_SOURCE_DOC_ID = "hiddenSourceDocId";
    private static final String HIDDEN_DOC_ORDER = "hiddenDocOrder";

    @Override
    public boolean isEditableRouteElement(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentDoc = context.getCurrentDocument();

        final SSFeuilleRoute route = getRelatedRoute(session, currentDoc);
        if (route.isFeuilleRouteInstance()) {
            DocumentModelList attachedDocs = route.getAttachedDocuments(session);
            Dossier dos = attachedDocs.get(0).getAdapter(Dossier.class);
            if (VocabularyConstants.STATUT_NOR_ATTRIBUE.equals(dos.getStatut())) {
                return false;
            }
        }

        if (!super.isEditableRouteElement(context)) {
            return false;
        } else if (route.isRunning()) {
            // À l'étape "pour impression", le dossier est en lecture seule sauf pour les administrateurs
            return EpgUIServiceLocator.getEpgDossierDistributionUIService().isEtapePourImpressionUpdater(context);
        } else if (route.isDraft()) {
            // condition sur les modèles de feuilles de route non validé (brouillon)
            NuxeoPrincipal principal = session.getPrincipal();
            if (!principal.isMemberOf(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR)) {
                String ministere = route.getMinistere();
                String groupMinistere = MinistereGroupeHelper.ministereidToGroup(ministere);
                // l'administrateur ministériel ne peut modifier que les feuilles de route affecté à ses ministères sauf
                // ceux crées par un Administrateur fonctionnel
                return (
                    StringUtils.isNotBlank(ministere) &&
                    principal.isMemberOf(groupMinistere) &&
                    principal.isMemberOf(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_UDPATER)
                );
            }
        }
        return true;
    }

    /**
     * Permet la création d'étapes pour l'épreuvage.
     *
     */
    public void createStepsPourEpreuve(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentDocument = context.getCurrentDocument();
        String hiddenSourceDocId = context.getFromContextData(HIDDEN_SOURCE_DOC_ID);
        FeuilleRouteEtapeOrder hiddenDocOrder = context.getFromContextData(HIDDEN_DOC_ORDER);
        // gestion du cas où le currentDoc est un dossier
        DocumentRef routeRef = getRouteRefFromDocument(currentDocument);
        DocumentModel routeDoc = session.getDocument(routeRef);
        // Il faut vérifier que les paramètres pour la génération automatique des étapes d'épreuvage sont renseignés, à
        // savoir : poste DAN, poste publication et poste BDC
        final TableReferenceService tdrService = SolonEpgServiceLocator.getTableReferenceService();
        String posteDanId = tdrService.getPosteDanId(session);
        String postePublicationId = tdrService.getPostePublicationId(session);
        String posteBdcId = getPosteBdcIdFromRoute(session, routeDoc);
        if (
            StringUtils.isNotEmpty(posteBdcId) &&
            !StringUtils.isEmpty(postePublicationId) &&
            !StringUtils.isEmpty(posteDanId)
        ) {
            IdRef sourceDocRef = new IdRef(hiddenSourceDocId);
            DocumentModel sourceDoc = getSourceDocFromRef(session, sourceDocRef);

            String sourceDocName = null;
            String parentPath = null;
            if (STEP_ORDER_IN.equals(hiddenDocOrder.name())) {
                parentPath = sourceDoc.getPathAsString();
            } else {
                DocumentModel parentDoc = session.getParentDocument(sourceDocRef);
                parentPath = parentDoc.getPathAsString();
                sourceDocName = getSourceDocName(session, sourceDoc, parentDoc, hiddenDocOrder);
            }

            SolonEpgServiceLocator
                .getDocumentRoutingService()
                .createStepsPourEpreuve(
                    session,
                    posteBdcId,
                    postePublicationId,
                    posteDanId,
                    routeDoc,
                    sourceDocName,
                    parentPath
                );
            context
                .getMessageQueue()
                .addSuccessToQueue(getString("feedback.solonepg.feuilleRoute.info.add.step.epreuvage"));
        }
    }

    /**
     * Permet la création d'étapes pour l'épreuvage.
     *
     */
    public void createStepsPourEpreuveApresInit(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentDocument = context.getCurrentDocument();
        String hiddenSourceDocId = context.getFromContextData(HIDDEN_SOURCE_DOC_ID);
        FeuilleRouteEtapeOrder hiddenDocOrder = context.getFromContextData(HIDDEN_DOC_ORDER);
        DocumentRef routeRef = getRouteRefFromDocument(currentDocument);
        IdRef sourceDocRef = new IdRef(hiddenSourceDocId);
        DocumentModel sourceDoc = getSourceDocFromRef(session, sourceDocRef);

        String posteDanId = context.getFromContextData("EpreuvePosteDan");
        String postePublicationId = context.getFromContextData("EpreuvePostePublication");
        String posteBdcId = context.getFromContextData("EpreuvePosteBdc");

        String sourceDocName = null;
        String parentPath = null;
        if (STEP_ORDER_IN.equals(hiddenDocOrder.name())) {
            parentPath = sourceDoc.getPathAsString();
        } else {
            DocumentModel parentDoc = session.getParentDocument(sourceDocRef);
            parentPath = parentDoc.getPathAsString();
            sourceDocName = getSourceDocName(session, sourceDoc, parentDoc, hiddenDocOrder);
        }

        if (
            (
                SolonEpgServiceLocator.getDocumentRoutingService().isSqueletteFeuilleRoute(currentDocument) &&
                StringUtils.isEmpty(posteBdcId)
            ) ||
            StringUtils.isEmpty(postePublicationId) ||
            StringUtils.isEmpty(posteDanId)
        ) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("feedback.solonepg.feuilleRoute.error.add.step.epreuvage"));
            return;
        }
        DocumentModel routeDoc = session.getDocument(routeRef);
        SolonEpgServiceLocator
            .getDocumentRoutingService()
            .createStepsPourEpreuve(
                session,
                posteBdcId,
                postePublicationId,
                posteDanId,
                routeDoc,
                sourceDocName,
                parentPath
            );

        context
            .getMessageQueue()
            .addSuccessToQueue(getString("feedback.solonepg.feuilleRoute.info.add.step.epreuvage"));
    }

    public boolean canAddEpreuveSteps(SpecificContext context) {
        return context
            .getSession()
            .getPrincipal()
            .isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_FOURNITURE_EPREUVE_CREATOR);
    }

    @Override
    public String getPosteBdcIdFromRoute(CoreSession session, DocumentModel routeDoc) {
        // Récupération du ministère attributaire du dossier lié à la fdr
        // qui est aussi le ministère de la fdr
        SolonEpgFeuilleRoute route = routeDoc.getAdapter(SolonEpgFeuilleRoute.class);
        String minAttributaireId = route.getMinistere();
        if (StringUtils.isEmpty(minAttributaireId)) {
            // On n'a pas pu récupérer l'id du ministère à partir de la fdr. Il s'agit peut être de la feuille de route
            // par défaut qui n'est liée à aucun ministère. On est obligé de récupérer le dossier
            String query = "select * from Dossier where dos:lastDocumentRoute='" + routeDoc.getId() + "'";
            DocumentModelList dossiersList = QueryUtils.doQuery(session, query, 1, 0);
            if (dossiersList.isEmpty()) {
                return "";
            }
            DocumentModel dossierDoc = dossiersList.get(0);
            Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            minAttributaireId = dossier.getMinistereAttache();
        }

        final STPostesService posteService = STServiceLocator.getSTPostesService();
        PosteNode posteBdc = posteService.getPosteBdcInEntite(minAttributaireId);
        if (posteBdc == null) {
            return "";
        }
        return posteBdc.getId();
    }

    protected boolean checkCompatibilityBeforePaste(SpecificContext context, List<DocumentModel> stepDocsToPaste) {
        for (DocumentModel stepDoc : stepDocsToPaste) {
            if (!checkValidityStep(context, stepDoc)) {
                context.getMessageQueue().addErrorToQueue("Echec du traitement. Le traitement a été annulé.");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkValiditySteps(
        SpecificContext context,
        SolonEpgFeuilleRoute route,
        List<DocumentModel> stepsDoc
    ) {
        for (DocumentModel stepDoc : stepsDoc) {
            SSRouteStep step = stepDoc.getAdapter(SSRouteStep.class);
            if (
                !step.getType().equals(VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION) &&
                !checkStepWithTxtNonPub(context, step, route.getTypeActe())
            ) {
                return false;
            }
        }
        return true;
    }

    protected boolean checkValidityStep(SpecificContext context) {
        return checkValidityStep(context, context.getCurrentDocument());
    }

    protected boolean checkValidityStep(SpecificContext context, DocumentModel stepDoc) {
        SolonEpgRouteStep routeStep = stepDoc.getAdapter(SolonEpgRouteStep.class);

        final boolean isSqueletteStep = SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE.equals(stepDoc.getType());
        final boolean isDestinataireOrganigramme =
            !isSqueletteStep || (SqueletteStepTypeDestinataire.ORGANIGRAMME.equals(routeStep.getTypeDestinataire()));

        if (isSqueletteStep && !checkValidityStepSqueletteMailbox(context, routeStep, isDestinataireOrganigramme)) {
            return false;
        }

        String errorCompatibility = "";

        CoreSession session = context.getSession();
        // vérifie la compatibilité des postes avec l'étape suivante
        if (!isSqueletteStep || isDestinataireOrganigramme) {
            errorCompatibility =
                SolonEpgServiceLocator.getDocumentRoutingService().isNextPostCompatibleWithNextStep(session, routeStep);
        }

        // On vérifie également que l'étape est bien compatible avec le type de dossier
        DocumentModel dossierDoc = context.getFromContextData("dossierRattache");
        if ("".equals(errorCompatibility) && dossierDoc.getId() != null) {
            errorCompatibility =
                SolonEpgServiceLocator
                    .getDocumentRoutingService()
                    .isNextStepCompatibleWithTypeActe(routeStep, session, dossierDoc.getId());
        }
        if (StringUtils.isNotBlank(errorCompatibility)) {
            context.getMessageQueue().addErrorToQueue(errorCompatibility);
            return false;
        }

        // on va voir le type d'acte du dossier rattaché pour gérer le cas où l'instance de fdr
        // est une copie du modele de fdr par défaut.
        // N.B. : la méta donnée type acte d'une fdr est renseignée dans les modèles, mais pas mis à jour dans les
        // instances
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        return checkStepWithTxtNonPub(context, routeStep, dossier.getTypeActe());
    }

    protected boolean checkValidityStepSqueletteMailbox(
        SpecificContext context,
        SolonEpgRouteStep routeStep,
        final boolean isDestinataireOrganigramme
    ) {
        // Si c'est un squelette, le type de destinataire est obligatoire
        if (routeStep.getTypeDestinataire() == null) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("feedback.feuilleRoute.squelette.step.typeDestinataireEmpty"));
            return false;
        }
        // Si c'est un squelette et qu'on a choisi le type de destinataire
        // organigramme, le choix du poste est obligatoire
        if (isDestinataireOrganigramme && StringUtils.isEmpty(routeStep.getDistributionMailboxId())) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("feedback.feuilleRoute.squelette.step.organigramme.posteEmpty"));
            return false;
        }

        return true;
    }

    /**
     * Vérifie la compatibilité de l'acte textes non publiés avec l'étape
     *
     */
    private boolean checkStepWithTxtNonPub(SpecificContext context, SSRouteStep routeStep, String typeActe) {
        final ActeService acteService = SolonEpgServiceLocator.getActeService();
        if (
            Boolean.TRUE.equals(acteService.isActeTexteNonPublie(typeActe)) &&
            Collections.unmodifiableSet(initIncompatibleSteps()).contains(routeStep.getType())
        ) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("feedback.solonepg.feuilleRoute.error.add.step.typeActe"));
            return false;
        }
        return true;
    }

    private static Set<String> initIncompatibleSteps() {
        Set<String> incompatibleSteps = new HashSet<>();
        incompatibleSteps.add(VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE);
        incompatibleSteps.add(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO);
        incompatibleSteps.add(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO);
        return incompatibleSteps;
    }

    /**
     * Retourne vrai si l'utilisateur peut modifier l'instance de feuille de route.
     */
    @Override
    public boolean isFeuilleRouteUpdatable(SpecificContext context, CoreSession session, DocumentModel doc) {
        if (SolonEpgServiceLocator.getDocumentRoutingService().isSqueletteFeuilleRoute(doc)) {
            return true;
        }
        return super.isFeuilleRouteUpdatable(context, session, doc);
    }

    @Override
    public SSFeuilleRoute getRelatedRoute(CoreSession session, DocumentModel currentDocument) {
        // Cas des squelette pour EPG
        if (SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE.equals(currentDocument.getType())) {
            return currentDocument.getAdapter(SolonEpgFeuilleRoute.class);
        }

        if (SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE.equalsIgnoreCase(currentDocument.getType())) {
            FeuilleRouteElement relatedRouteElement = currentDocument.getAdapter(FeuilleRouteElement.class);
            if (relatedRouteElement != null) {
                FeuilleRoute documentRoute = relatedRouteElement.getFeuilleRoute(session);
                return documentRoute != null
                    ? documentRoute.getDocument().getAdapter(SolonEpgFeuilleRoute.class)
                    : null;
            }
        }
        return super.getRelatedRoute(session, currentDocument);
    }

    /**
     * Creation simple d'une squelette routeStep pour la création en masse
     */
    public DocumentModel newSimpleSqueletteRouteStep(SpecificContext context) {
        CoreSession session = context.getSession();
        String hiddenSourceDocId = context.getFromContextData(HIDDEN_SOURCE_DOC_ID);
        FeuilleRouteEtapeOrder hiddenDocOrder = context.getFromContextData(HIDDEN_DOC_ORDER);
        DocumentRef sourceDocRef = new IdRef(hiddenSourceDocId);

        DocumentModel sourceDoc = getSourceDocFromRef(session, sourceDocRef);

        String parentPath = null;
        if (STEP_ORDER_IN.equals(hiddenDocOrder.name())) {
            parentPath = sourceDoc.getPathAsString();
        } else {
            DocumentModel parentDoc = session.getParentDocument(sourceDocRef);
            parentPath = parentDoc.getPathAsString();
        }

        UUID newId = UUID.randomUUID();
        DocumentModel desiredDocument = session.createDocumentModel(
            SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE
        );
        desiredDocument.setPathInfo(parentPath, newId.toString());

        return desiredDocument;
    }

    @Override
    public void saveRouteElement(SpecificContext context, DocumentModel newDocument) {
        if (checkValidityStep(context, newDocument)) {
            super.saveRouteElement(context, newDocument);
        }
    }

    @Override
    public void updateRouteElement(SpecificContext context, DocumentModel changeableDocument) {
        if (checkValidityStep(context, changeableDocument)) {
            super.updateRouteElement(context, changeableDocument);
        }
    }

    @Override
    public boolean isStepInitialisation(SpecificContext context) {
        DocumentModel stepDoc = context.getFromContextData(SSContextDataKey.STEP_DOC);
        return VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION.equals(
            stepDoc.getAdapter(SSRouteStep.class).getType()
        );
    }

    @Override
    public boolean isActiveStepInitialisation(SpecificContext context) {
        DossierLink dossierLink = (DossierLink) SSActionsServiceLocator
            .getSSCorbeilleActionService()
            .getCurrentDossierLink(context);
        return (
            dossierLink != null &&
            VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION.equals(dossierLink.getRoutingTaskType())
        );
    }

    @Override
    public boolean isActiveStepPourBonATirer(SpecificContext context) {
        DossierLink dossierLink = (DossierLink) SSActionsServiceLocator
            .getSSCorbeilleActionService()
            .getCurrentDossierLink(context);
        return (
            dossierLink != null &&
            VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER.equals(dossierLink.getRoutingTaskType())
        );
    }

    @Override
    public boolean isStepRetourModification(SpecificContext context) {
        DossierLink dossierLink = (DossierLink) SSActionsServiceLocator
            .getSSCorbeilleActionService()
            .getCurrentDossierLink(context);
        return (
            dossierLink != null &&
            VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION.equals(dossierLink.getRoutingTaskType())
        );
    }

    @Override
    public boolean hasStepPublicationBO(SpecificContext context) {
        List<DocumentModel> stepsDoc = SolonEpgServiceLocator
            .getEPGFeuilleRouteService()
            .getSteps(context.getSession(), context.getCurrentDocument());

        Predicate<DocumentModel> routeStepPredicate = step ->
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE.equals(step.getType());
        Predicate<DocumentModel> runningRouteStepPredicate = step ->
            FeuilleRouteElement.ElementLifeCycleState.running.name().equals(step.getCurrentLifeCycleState());
        Predicate<DocumentModel> readyRouteStepPredicate = step ->
            FeuilleRouteElement.ElementLifeCycleState.ready.name().equals(step.getCurrentLifeCycleState());

        return stepsDoc
            .stream()
            .filter(routeStepPredicate.and(runningRouteStepPredicate.or(readyRouteStepPredicate)))
            .map(stepDoc -> stepDoc.getAdapter(SSRouteStep.class))
            .anyMatch(step -> VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_BO.equals(step.getType()));
    }

    @Override
    public boolean isStepAvis(SpecificContext context) {
        DossierLink dossierLink = (DossierLink) SSActionsServiceLocator
            .getSSCorbeilleActionService()
            .getCurrentDossierLink(context);
        return (
            dossierLink != null && VocabularyConstants.ROUTING_TASK_TYPE_AVIS.equals(dossierLink.getRoutingTaskType())
        );
    }

    @Override
    public boolean isStepContreseign(SpecificContext context) {
        DossierLink dossierLink = (DossierLink) SSActionsServiceLocator
            .getSSCorbeilleActionService()
            .getCurrentDossierLink(context);
        return (
            dossierLink != null &&
            VocabularyConstants.ROUTING_TASK_TYPE_CONTRESEING.equals(dossierLink.getRoutingTaskType())
        );
    }

    @Override
    public boolean isStepExecutable(SpecificContext context) {
        DossierLink dossierLink = (DossierLink) SSActionsServiceLocator
            .getSSCorbeilleActionService()
            .getCurrentDossierLink(context);

        if (dossierLink == null) {
            return false;
        }

        if (
            !VocabularyConstants.ROUTING_TASK_TYPE_CONTRESEING.equals(dossierLink.getRoutingTaskType()) &&
            !VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SGG.equals(dossierLink.getRoutingTaskType()) &&
            !VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SECTEUR_PARLEMENTAIRE.equals(
                dossierLink.getRoutingTaskType()
            )
        ) {
            return true;
        }

        NuxeoPrincipal principal = context.getSession().getPrincipal();
        return (
            (
                VocabularyConstants.ROUTING_TASK_TYPE_CONTRESEING.equals(dossierLink.getRoutingTaskType()) &&
                principal.isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_POUR_CONTRESEING_EXECUTOR)
            ) ||
            (
                VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SGG.equals(dossierLink.getRoutingTaskType()) &&
                principal.isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_POUR_ATTRIBUTION_SGG_EXECUTOR)
            ) ||
            (
                VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SECTEUR_PARLEMENTAIRE.equals(
                    dossierLink.getRoutingTaskType()
                ) &&
                principal.isMemberOf(SolonEpgBaseFunctionConstant.RESPONSABLE_MGPP)
            )
        );
    }

    @Override
    public boolean canRetourModification(SpecificContext context) {
        DossierLink dossierLink = (DossierLink) SSActionsServiceLocator
            .getSSCorbeilleActionService()
            .getCurrentDossierLink(context);
        return dossierLink != null && ETAPE_TYPE_VALIDE_RETOUR_MODIF.contains(dossierLink.getRoutingTaskType());
    }

    @Override
    public boolean canValideNonConcerne(SpecificContext context) {
        DossierLink dossierLink = (DossierLink) SSActionsServiceLocator
            .getSSCorbeilleActionService()
            .getCurrentDossierLink(context);
        return (dossierLink != null && ETAPE_TYPE_VALIDE_NON_CONCERNE.contains(dossierLink.getRoutingTaskType()));
    }

    @Override
    public boolean canRefusValidation(SpecificContext context) {
        DossierLink dossierLink = (DossierLink) SSActionsServiceLocator
            .getSSCorbeilleActionService()
            .getCurrentDossierLink(context);
        return (dossierLink != null && ETAPE_TYPE_REFUS.contains(dossierLink.getRoutingTaskType()));
    }

    @Override
    public boolean hasRunningStep(SpecificContext context) {
        DossierLink dossierLink = (DossierLink) SSActionsServiceLocator
            .getSSCorbeilleActionService()
            .getCurrentDossierLink(context);
        if (dossierLink != null) {
            DocumentModel stepDoc = context.getSession().getDocument(new IdRef(dossierLink.getRoutingTaskId()));
            return EtapeLifeCycle.RUNNING.getValue().equals(stepDoc.getCurrentLifeCycleState());
        } else {
            return false;
        }
    }

    @Override
    public boolean isDossierLance(SpecificContext context) {
        return VocabularyConstants.STATUT_LANCE.equals(
            context.getCurrentDocument().getAdapter(Dossier.class).getStatut()
        );
    }

    @Override
    public boolean isTypeActeTexteNonPublie(SpecificContext context) {
        ActeService acteService = SolonEpgServiceLocator.getActeService();
        String typeActe = context.getCurrentDocument().getAdapter(Dossier.class).getTypeActe();
        return acteService.isActeTexteNonPublie(typeActe);
    }

    @Override
    public boolean isRapportAuParlement(SpecificContext context) {
        ActeService acteService = SolonEpgServiceLocator.getActeService();
        String typeActe = context.getCurrentDocument().getAdapter(Dossier.class).getTypeActe();
        return acteService.isRapportAuParlement(typeActe);
    }

    @Override
    protected DocumentModel createAndMapRouteStep(
        SpecificContext context,
        CreationEtapeLineDTO changeableDocument,
        String idBranch,
        FeuilleRouteEtapeOrder order
    ) {
        if (SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE.equals(context.getCurrentDocument().getType())) {
            DocumentModel newDocument = createRouteElement(
                context.getSession(),
                context.getCurrentDocument(),
                SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE,
                idBranch,
                order
            );
            SolonEpgRouteStep step = newDocument.getAdapter(SolonEpgRouteStep.class);
            step.setType(changeableDocument.getTypeEtape());
            step.setDistributionMailboxId(changeableDocument.getMailboxId());
            step.setDeadLine(
                StringUtils.isNotEmpty(changeableDocument.getEcheance())
                    ? Long.parseLong(changeableDocument.getEcheance())
                    : null
            );
            step.setAutomaticValidation(changeableDocument.getValAuto());
            step.setObligatoireMinistere(CreationEtapeLineDTO.MINISTERE.equals(changeableDocument.getObligatoire()));
            step.setObligatoireSGG(CreationEtapeLineDTO.SGG.equals(changeableDocument.getObligatoire()));
            step.setTypeDestinataire(SqueletteStepTypeDestinataire.fromValue(changeableDocument.getTypeDestinataire()));
            return newDocument;
        } else {
            return super.createAndMapRouteStep(context, changeableDocument, idBranch, order);
        }
    }

    @Override
    protected boolean validationElementType(CoreSession session, DocumentModel branch) {
        return (
            SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE.equals(branch.getType()) ||
            super.validationElementType(session, branch)
        );
    }

    @Override
    protected void validateDestinataireEtape(SpecificContext context, List<CreationEtapeLineDTO> lines) {
        SolonEpgFeuilleRoute fdr = context.getCurrentDocument().getAdapter(SolonEpgFeuilleRoute.class);
        if (fdr != null && fdr.isSqueletteFeuilleRoute()) {
            lines
                .stream()
                .filter(line -> StringUtils.isAllBlank(line.getDestinataire(), line.getTypeDestinataire()))
                .findFirst()
                .ifPresent(
                    s -> {
                        throw new STValidationException("fdr.form.addstep.requiredDestinataire");
                    }
                );
        } else {
            super.validateDestinataireEtape(context, lines);
        }
    }
}
