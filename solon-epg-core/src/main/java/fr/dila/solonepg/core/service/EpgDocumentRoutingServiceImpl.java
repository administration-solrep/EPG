package fr.dila.solonepg.core.service;

import static fr.dila.st.core.util.ResourceHelper.getString;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.feuilleroute.SqueletteStepTypeDestinataire;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.EpgDocumentRoutingService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.core.adapter.RouteStepAdapterFactory;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.domain.feuilleroute.StepFolder;
import fr.dila.ss.api.feuilleroute.DocumentRouteTreeElement;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.SSFeuilleRouteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.service.STLockService;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteElement;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteElement.ElementLifeCycleTransistion;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.constant.FeuilleRouteConstant;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.constant.FeuilleRouteEvent;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.constant.FeuilleRouteExecutionType;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.exception.FeuilleRouteException;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.exception.FeuilleRouteNotLockedException;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.service.FeuilleRouteService;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.EventFirer;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import fr.sword.naiad.nuxeo.commons.core.schema.DublincorePropertyUtil;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.LifeCycleConstants;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.pathsegment.PathSegmentService;
import org.nuxeo.ecm.core.uidgen.UIDGeneratorService;
import org.nuxeo.ecm.core.uidgen.UIDSequencer;

/**
 * Implémentation du service de document routing de SOLON EPG.
 *
 * @author jtremeaux
 */
public class EpgDocumentRoutingServiceImpl
    extends fr.dila.ss.core.service.DocumentRoutingServiceImpl
    implements EpgDocumentRoutingService {
    private static final STLogger LOGGER = STLogFactory.getLog(EpgDocumentRoutingServiceImpl.class);
    private static final String ETAPE = "Etape";

    private static final List<String> ROUTING_TASK_TYPES_ALLOWED = ImmutableList.of(
        VocabularyConstants.ROUTING_TASK_TYPE_AVIS,
        VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE,
        VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
        VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SECTEUR_PARLEMENTAIRE,
        VocabularyConstants.ROUTING_TASK_TYPE_IMPRESSION,
        VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER,
        VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE,
        VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO,
        VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO,
        VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION,
        VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_BO
    );

    @Override
    public void validateMoveRouteStepBefore(DocumentModel routeStepDoc) {
        // Une étape ne peut pas être déplacée avant l'étape "Pour initialisation"
        if (!routeStepDoc.hasFacet("Folderish")) {
            SolonEpgRouteStep routeStep = routeStepDoc.getAdapter(SolonEpgRouteStep.class);
            if (routeStep != null && routeStep.isPourInitialisation()) {
                throw new FeuilleRouteException(
                    getString("epg.feuilleRoute.action.moveUp.avantPourInitialisation.error")
                );
            }
        }
    }

    @Override
    public boolean isEtapeObligatoireUpdater(CoreSession session, DocumentModel routeStepDoc) {
        // Traite uniquement les étapes de feuille de route et pas les conteneurs
        if (BooleanUtils.isFalse(routeStepDoc.hasFacet("Folderish"))) {
            final SSRouteStep routeStep = routeStepDoc.getAdapter(SSRouteStep.class);

            // Seul l'administrateur fonctionnel ou ministeriel a le droit de modifier les étapes obligatoires SGG

            if (routeStep.isObligatoireSGG()) {
                final NuxeoPrincipal nuxeoPrincipal = session.getPrincipal();

                boolean isAdminFonctionnel = nuxeoPrincipal.isMemberOf(
                    STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR
                );
                boolean isEtapeObligatoirUpdater = nuxeoPrincipal.isMemberOf(
                    SolonEpgBaseFunctionConstant.FEUILLE_ROUTE_OBLIGATOIRE_SGG_UPDATER
                );
                return isAdminFonctionnel || isEtapeObligatoirUpdater;
            }

            // Seul l'administrateur ministériel a le droit de modifier les étapes obligatoires ministère
            if (routeStep.isObligatoireMinistere()) {
                final NuxeoPrincipal nuxeoPrincipal = session.getPrincipal();

                return nuxeoPrincipal.isMemberOf(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_UDPATER);
            }
        }

        return true;
    }

    /**
     * @author FLT Allow to check if the next step is compatible with the configured CE minister.
     *
     */
    @Override
    public String isNextPostCompatibleWithNextStep(final CoreSession session, final SSRouteStep nextstepDocument) {
        // on vérifie que le post spécifié pour l'étape est un posteCE
        DocumentModel tableReferenceDoc = SolonEpgServiceLocator.getTableReferenceService().getTableReference(session);
        TableReference tableReference = tableReferenceDoc.getAdapter(TableReference.class);
        String ministereCeId = tableReference.getMinistereCE();
        return isNextPostCompatibleWithNextStep(
            ministereCeId,
            nextstepDocument,
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE
        );
    }

    /**
     * @author FLT Allow to check if the next steps are compatibles with the configured CE minister.
     */
    @Override
    public String isNextPostsCompatibleWithNextStep(final CoreSession session, final List<DocumentModel> steps) {
        return steps
            .stream()
            .filter(step -> RouteStepAdapterFactory.authorizedDocumentsType.contains(step.getType()))
            .map(s -> s.getAdapter(SSRouteStep.class))
            .map(s -> isNextPostCompatibleWithNextStep(session, s))
            .filter(StringUtils::isNotEmpty)
            .findFirst()
            .orElse("");
    }

    /**
     * @author FLT Allow to check if the next step is compatible with the asked minister.
     *
     */
    @Override
    public String isNextPostCompatibleWithNextStep(
        String ministereRequisId,
        final SSRouteStep nextstepDocument,
        final String typeAvis
    ) {
        String posteDestinationId = SSServiceLocator
            .getMailboxPosteService()
            .getPosteIdFromMailboxId(nextstepDocument.getDistributionMailboxId());
        final PosteNode posteNode = STServiceLocator.getSTPostesService().getPoste(posteDestinationId);

        final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
        List<EntiteNode> entiteNodeList = ministeresService.getMinisteresParents(posteNode);
        boolean isPosteCE = entiteNodeList.stream().anyMatch(e -> e.getId().equals(ministereRequisId));
        // on vérifie pour le cas de "Pour Avis CE"
        if (nextstepDocument.getType().equals(typeAvis)) {
            if (!isPosteCE) {
                return ResourceHelper.getString("epg.etape.fdr.poste.incompatible.message");
            }
        } else {
            if (isPosteCE) {
                return ResourceHelper.getString("epg.etape.fdr.etape.incompatible.message");
            }
        }
        return "";
    }

    /**
     * @author FLT Allow to check if the next steps are compatibles with the asked minister.
     *
     */
    @Override
    public String isNextPostsCompatibleWithNextStep(
        final String ministereRequisId,
        final List<DocumentModel> steps,
        final String typeAvisCe
    ) {
        return steps
            .stream()
            .filter(step -> RouteStepAdapterFactory.authorizedDocumentsType.contains(step.getType()))
            .map(s -> s.getAdapter(SSRouteStep.class))
            .map(s -> isNextPostCompatibleWithNextStep(ministereRequisId, s, typeAvisCe))
            .filter(StringUtils::isNotEmpty)
            .findFirst()
            .orElse("");
    }

    @Override
    public void createStepsPourEpreuve(
        CoreSession session,
        String posteBdcId,
        String postePublicationId,
        String posteDanId,
        DocumentModel routeDoc,
        String sourceDocName,
        String parentPath
    ) {
        String stepAttributionName = ETAPE + VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION + "-" + posteDanId;
        String stepFournitureName =
            ETAPE + VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE + "-" + postePublicationId;
        String stepBatName;

        if (isSqueletteFeuilleRoute(routeDoc)) {
            stepBatName = ETAPE + VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER;

            // Création du document model de l'étape "Pour fourniture
            // d'épreuves" au poste de publication
            createRouteStepSquelette(
                session,
                parentPath,
                stepFournitureName,
                VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE,
                postePublicationId,
                true,
                routeDoc,
                sourceDocName,
                SqueletteStepTypeDestinataire.ORGANIGRAMME
            );
            // Création du document model de l'étape "Pour attribution" au poste
            // du DAN
            createRouteStepSquelette(
                session,
                parentPath,
                stepAttributionName,
                VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
                posteDanId,
                true,
                routeDoc,
                sourceDocName,
                SqueletteStepTypeDestinataire.ORGANIGRAMME
            );

            // Création du document model de l'étape "Pour bon à tirer" au poste
            // BDC du min attributaire
            createRouteStepSquelette(
                session,
                parentPath,
                stepBatName,
                VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER,
                null,
                false,
                routeDoc,
                sourceDocName,
                SqueletteStepTypeDestinataire.BUREAU_DU_CABINET
            );

            // Création du document model de l'étape "Pour attribution" au poste
            // du DAN
            createRouteStepSquelette(
                session,
                parentPath,
                stepAttributionName,
                VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
                posteDanId,
                true,
                routeDoc,
                sourceDocName,
                SqueletteStepTypeDestinataire.ORGANIGRAMME
            );
        } else {
            stepBatName = ETAPE + VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER + "-" + posteBdcId;

            // Création du document model de l'étape "Pour fourniture
            // d'épreuves" au poste de publication
            createRouteStep(
                session,
                parentPath,
                stepFournitureName,
                VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE,
                postePublicationId,
                true,
                routeDoc,
                sourceDocName
            );
            // Création du document model de l'étape "Pour attribution" au poste
            // du DAN
            createRouteStep(
                session,
                parentPath,
                stepAttributionName,
                VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
                posteDanId,
                true,
                routeDoc,
                sourceDocName
            );

            // Création du document model de l'étape "Pour bon à tirer" au poste
            // BDC du min attributaire
            createRouteStep(
                session,
                parentPath,
                stepBatName,
                VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER,
                posteBdcId,
                false,
                routeDoc,
                sourceDocName
            );

            // Création du document model de l'étape "Pour attribution" au poste
            // du DAN
            createRouteStep(
                session,
                parentPath,
                stepAttributionName,
                VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION,
                posteDanId,
                true,
                routeDoc,
                sourceDocName
            );
        }

        session.save();
    }

    @Override
    public boolean isSqueletteFeuilleRoute(DocumentModel docModel) {
        return (
            SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE.equals(docModel.getType()) ||
            SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE.equals(docModel.getType())
        );
    }

    /**
     * Création d'une étape de feuille de route à l'état ready
     */
    private SolonEpgRouteStep createRouteStep(
        CoreSession session,
        String parentPath,
        String stepName,
        String stepType,
        String posteId,
        boolean obligatoireSgg,
        DocumentModel routeDoc,
        String previousStepName
    ) {
        return createGenericRouteStep(
            session,
            parentPath,
            stepName,
            stepType,
            posteId,
            obligatoireSgg,
            routeDoc,
            previousStepName,
            SqueletteStepTypeDestinataire.ORGANIGRAMME,
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE
        );
    }

    /**
     * Création d'une étape de feuille de route à l'état ready
     */
    private SolonEpgRouteStep createRouteStepSquelette(
        CoreSession session,
        String parentPath,
        String stepName,
        String stepType,
        String posteId,
        boolean obligatoireSgg,
        DocumentModel routeDoc,
        String previousStepName,
        SqueletteStepTypeDestinataire typeDestinataire
    ) {
        return createGenericRouteStep(
            session,
            parentPath,
            stepName,
            stepType,
            posteId,
            obligatoireSgg,
            routeDoc,
            previousStepName,
            typeDestinataire,
            SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE
        );
    }

    private SolonEpgRouteStep createGenericRouteStep(
        CoreSession session,
        String parentPath,
        String stepName,
        String stepType,
        String posteId,
        boolean obligatoireSgg,
        DocumentModel routeDoc,
        String previousStepName,
        SqueletteStepTypeDestinataire typeDestinataire,
        String documentType
    ) {
        // Initialisation du documentmodel du document qui sera créé
        DocumentModel stepFournitureDoc = session.createDocumentModel(parentPath, stepName, documentType);
        SolonEpgRouteStep step = stepFournitureDoc.getAdapter(SolonEpgRouteStep.class);

        if (posteId != null) {
            String mailboxPosteId = SSServiceLocator.getMailboxPosteService().getPosteMailboxId(posteId);
            step.setDistributionMailboxId(mailboxPosteId);
        }

        step.setObligatoireSGG(obligatoireSgg);
        step.setType(stepType);
        step.setDocumentRouteId(routeDoc.getId());
        step.setTypeDestinataire(typeDestinataire);
        // création du document
        stepFournitureDoc = session.createDocument(step.getDocument());
        session.save();
        step = stepFournitureDoc.getAdapter(SolonEpgRouteStep.class);
        step.setValidated(session);
        step.save(session);
        step.setReady(session);
        session.orderBefore(new PathRef(parentPath), stepFournitureDoc.getName(), previousStepName);
        step.save(session);
        return step;
    }

    /**
     * Contrôle pour éviter de créer des étapes sur des types d'actes non compatibles (exemple 'Pour avis CE')
     *
     * @param nextstepDocument
     */
    @Override
    public String isNextStepCompatibleWithTypeActe(
        SSRouteStep nextstepDocument,
        CoreSession documentManager,
        String idDoc
    ) {
        String error = "";
        // on regarde si l'étape qu'on veut créer est de type 'Pour avis CE'
        if (nextstepDocument.getType().equals(VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE)) {
            // On regarde alors si le document a un type qui autorise les étapes pour avis CE
            IdRef docRef = new IdRef(idDoc);
            if (documentManager.exists(docRef)) {
                DocumentModel dossierDoc = documentManager.getDocument(docRef);
                Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                final ActeService acteService = SolonEpgServiceLocator.getActeService();
                boolean hasTypeActeSaisineRectificative = acteService.hasTypeActeSaisineRectificative(
                    dossier.getTypeActe()
                );
                if (!hasTypeActeSaisineRectificative) {
                    error = "Une étape 'Pour avis CE' ne peut être appliquée à ce type d'acte";
                }
            }
        }
        return error;
    }

    @Override
    public SSFeuilleRoute duplicateRouteModel(
        final CoreSession session,
        final DocumentModel routeToCopyDoc,
        final String ministere
    ) {
        // Copie le modèle de feuille de route
        final SSFeuilleRoute routeToCopy = routeToCopyDoc.getAdapter(SSFeuilleRoute.class);
        String newTitle = routeToCopy.getName() + " (Copie)";
        String escapedTitle = newTitle.replace('/', '-');
        escapedTitle = escapedTitle.replace('\\', '-');
        DocumentModel newFeuilleRouteDoc = session.copy(
            routeToCopyDoc.getRef(),
            routeToCopyDoc.getParentRef(),
            escapedTitle
        );

        SSFeuilleRoute newFeuilleRoute = newFeuilleRouteDoc.getAdapter(SSFeuilleRoute.class);
        lockDocumentRoute(newFeuilleRoute, session);

        // Si le modèle copié était validé, invalide le nouveau modèle
        if (newFeuilleRoute.isValidated()) {
            invalidateRouteModel(newFeuilleRoute, session);
            newFeuilleRouteDoc = newFeuilleRoute.getDocument();
        }

        // Réinitialise l'état de la feuille de route
        newFeuilleRoute.setCreator(session.getPrincipal().getName());
        newFeuilleRoute.setDemandeValidation(false);
        newFeuilleRoute.setFeuilleRouteDefaut(false);
        if (StringUtils.isNotEmpty(ministere) && StringUtils.isEmpty(newFeuilleRoute.getMinistere())) {
            newFeuilleRoute.setMinistere(ministere);

            final OrganigrammeService organigrammeService = STServiceLocator.getOrganigrammeService();

            final EntiteNode ministereNode = organigrammeService.getOrganigrammeNodeById(
                ministere,
                OrganigrammeType.MINISTERE
            );
            final String NORMinistere = ministereNode.getNorMinistere();
            newTitle = NORMinistere + " - " + newTitle;
        }
        newFeuilleRoute.setTitle(newTitle);

        newFeuilleRouteDoc = session.saveDocument(newFeuilleRouteDoc);
        session.save();

        // Initialise les étapes de la nouvelle feuille de route
        initFeuilleRouteStep(session, newFeuilleRouteDoc);

        if (!isSqueletteFeuilleRoute(routeToCopyDoc)) {
            // Si un ministère est fourni et que la feuille de route est non affecté
            unlockDocumentRoute(newFeuilleRoute, session);
        }

        return newFeuilleRouteDoc.getAdapter(SSFeuilleRoute.class);
    }

    @Override
    public SolonEpgFeuilleRoute duplicateSquelette(final CoreSession session, final DocumentModel squeletteToCopyDoc) {
        // Copie le modèle de feuille de route
        final SSFeuilleRoute routeToCopy = squeletteToCopyDoc.getAdapter(SSFeuilleRoute.class);
        String newTitle = routeToCopy.getName() + " (Copie)";
        String escapedTitle = newTitle.replace('/', '-');
        escapedTitle = escapedTitle.replace('\\', '-');
        DocumentModel newSqueletteDoc = session.copy(
            squeletteToCopyDoc.getRef(),
            squeletteToCopyDoc.getParentRef(),
            escapedTitle
        );

        SolonEpgFeuilleRoute newSquelette = newSqueletteDoc.getAdapter(SolonEpgFeuilleRoute.class);
        lockDocumentRoute(newSquelette, session);

        // Si le modèle copié était validé, invalide le nouveau modèle
        if (newSquelette.isValidated()) {
            newSquelette = invalidateSquelette(newSquelette, session);
            newSqueletteDoc = newSquelette.getDocument();
        }

        // Réinitialise l'état de la feuille de route
        newSquelette.setCreator(session.getPrincipal().getName());
        newSquelette.setTitle(newTitle);

        session.saveDocument(newSqueletteDoc);
        session.save();

        // Initialise les étapes de la nouvelle feuille de route
        initFeuilleRouteStep(session, newSqueletteDoc);
        unlockDocumentRoute(newSquelette, session);

        return newSquelette;
    }

    @Override
    public SolonEpgFeuilleRoute validateSquelette(final SSFeuilleRoute squelette, final CoreSession session) {
        if (!LockUtils.isLockedByCurrentUser(session, squelette.getDocument().getRef())) {
            throw new FeuilleRouteNotLockedException();
        }

        checkAndMakeAllStateStepDraft(session, squelette);

        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                final SSFeuilleRoute route = session
                    .getDocument(squelette.getDocument().getRef())
                    .getAdapter(SSFeuilleRoute.class);
                EventFirer.fireEvent(session, route.getDocument(), null, FeuilleRouteEvent.beforeRouteValidated.name());

                // Valide la feuille de route
                route.setValidated(session);
                // Annule la demande de validation
                route.setDemandeValidation(false);
                session.saveDocument(route.getDocument());
                session.save();
                EventFirer.fireEvent(session, route.getDocument(), null, FeuilleRouteEvent.afterRouteValidated.name());
            }
        }
        .runUnrestricted();
        return session.getDocument(squelette.getDocument().getRef()).getAdapter(SolonEpgFeuilleRoute.class);
    }

    @Override
    public SolonEpgFeuilleRoute invalidateSquelette(final FeuilleRoute squelette, final CoreSession session) {
        if (!LockUtils.isLockedByCurrentUser(session, squelette.getDocument().getRef())) {
            throw new FeuilleRouteNotLockedException();
        }
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                EventFirer.fireEvent(
                    session,
                    squelette.getDocument(),
                    null,
                    SolonEpgEventConstant.BEFORE_INVALIDATE_SQUELETTE
                );
                squelette.followTransition(ElementLifeCycleTransistion.toDraft, session, false);
                EventFirer.fireEvent(
                    session,
                    squelette.getDocument(),
                    null,
                    SolonEpgEventConstant.AFTER_INVALIDATE_SQUELETTE
                );
            }
        }
        .runUnrestricted();

        squelette.getDocument().detach(false);
        squelette.getDocument().attach(session.getSessionId());

        return squelette.getDocument().getAdapter(SolonEpgFeuilleRoute.class);
    }

    @Override
    public SolonEpgFeuilleRoute createNewModelInstanceFromSquelette(
        SolonEpgFeuilleRoute squel,
        final DocumentModel location,
        final String ministereId,
        final String bdcId,
        final String cdmId,
        final String scdmId,
        final String cpmId,
        CoreSession session
    ) {
        String message = ResourceHelper.getString("epg.etape.fdr.creation.modele.squelette.message");
        LOGGER.info(STLogEnumImpl.DEFAULT, message + " " + squel.getName());

        final FeuilleRouteModelService fdrmService = SolonEpgServiceLocator.getFeuilleRouteModelService();
        final EpgDocumentRoutingService docRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
        final STLockService lockService = STServiceLocator.getSTLockService();

        DocumentModel newModeleFDRDocumentModel = null;
        SolonEpgFeuilleRoute newMFDR = null;

        try {
            boolean defautFDRAlreadyExist = !checkFeuilleRouteParDefautExist(session, ministereId, squel.getTypeActe());

            newModeleFDRDocumentModel =
                session.createDocumentModel(
                    location.getPathAsString(),
                    squel.getName(),
                    SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE
                );

            newMFDR = newModeleFDRDocumentModel.getAdapter(SolonEpgFeuilleRoute.class);
            String intitule = fdrmService.creeTitleModeleFDR(ministereId, null, squel.getTypeActe(), squel.getName());
            newMFDR.setTitle(intitule);
            newMFDR.setMinistere(ministereId);
            newMFDR.setTypeActe(squel.getTypeActe());
            newMFDR.setFeuilleRouteDefaut(defautFDRAlreadyExist);

            // Compteur
            final UIDGeneratorService uidGeneratorService = ServiceUtil.getRequiredService(UIDGeneratorService.class);
            UIDSequencer sequencer = uidGeneratorService.getSequencer();
            newMFDR.setNumero(sequencer.getNextLong("SOLON_EPG_FDR_MODEL") + 1);

            DublincorePropertyUtil.setDescription(newModeleFDRDocumentModel, squel.getDescription());

            newModeleFDRDocumentModel = session.createDocument(newModeleFDRDocumentModel);

            if (defautFDRAlreadyExist) {
                // Valide la feuille de route
                newMFDR = newModeleFDRDocumentModel.getAdapter(SolonEpgFeuilleRoute.class);
                docRoutingService.lockDocumentRoute(newMFDR, session);

                SSFeuilleRoute documentRouteValidated = docRoutingService.validateRouteModel(newMFDR, session);
                session.saveDocument(documentRouteValidated.getDocument());
                session.save();
                docRoutingService.unlockDocumentRoute(newMFDR, session);
            }

            List<DocumentModel> listSteps = docRoutingService.getOrderedRouteElement(
                squel.getDocument().getId(),
                session
            );

            lockService.lockDoc(session, newModeleFDRDocumentModel);

            this.createRouteStepHierarchyFromRouteStepSquelette(
                    session,
                    listSteps,
                    bdcId,
                    cdmId,
                    scdmId,
                    cpmId,
                    newModeleFDRDocumentModel,
                    newModeleFDRDocumentModel
                );

            lockService.unlockDoc(session, newModeleFDRDocumentModel);
        } catch (NuxeoException e) {
            String msg = ResourceHelper.getString("epg.etape.fdr.creation.modele.squelette.error");
            LOGGER.error(STLogEnumImpl.DEFAULT, e, msg);
            try {
                if (
                    newModeleFDRDocumentModel != null &&
                    lockService.isLockByCurrentUser(session, newModeleFDRDocumentModel)
                ) {
                    lockService.unlockDoc(session, newModeleFDRDocumentModel);
                }
            } catch (NuxeoException e1) {
                String msge = ResourceHelper.getString("epg.etape.fdr.creation.modele.squelette.error");
                LOGGER.error(STLogEnumImpl.DEFAULT, e1, msge);
            }
        }
        return newMFDR;
    }

    /**
     * Vérifie si une feuille de route par défaut existe déjà pour ce ministère et ce type d'étape <br/>
     * ministèreId et typeActe sont obligatoire
     * @return
     */
    private boolean checkFeuilleRouteParDefautExist(
        CoreSession session,
        final String ministèreId,
        final String typeActe
    ) {
        final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();

        final List<DocumentModel> lstModeleExistant = feuilleRouteModelService.getNonDeletedFdrModelFromMinistereAndDirection(
            session,
            ministèreId,
            null,
            false
        );

        if (lstModeleExistant != null && !lstModeleExistant.isEmpty()) {
            for (DocumentModel model : lstModeleExistant) {
                final SolonEpgFeuilleRoute solonEpgFeuilleRoute = model.getAdapter(SolonEpgFeuilleRoute.class);
                if (
                    solonEpgFeuilleRoute.isFeuilleRouteDefaut() && typeActe.equals(solonEpgFeuilleRoute.getTypeActe())
                ) {
                    // la feuille de route est par défaut pour ce type d'acte
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Crée les étapes de feuille de route d'un modele à partir des étapes d'un squelette.
     * La méthode est récursive afin de prendre en compte les conteneurs d'étape des étapes en parallele.
     */
    private void createRouteStepHierarchyFromRouteStepSquelette(
        final CoreSession session,
        final List<DocumentModel> stepListToCopy,
        final String bdcId,
        final String cdmId,
        final String scdmId,
        final String cpmId,
        final DocumentModel parentDocument,
        final DocumentModel modelFDRParent
    ) {
        for (DocumentModel stepDocModel : stepListToCopy) {
            if (!FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER.equals(stepDocModel.getType())) {
                SolonEpgRouteStep stepToCopy = stepDocModel.getAdapter(SolonEpgRouteStep.class);
                // Etape de feuille de route
                if (!VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION.equals(stepToCopy.getType())) { // si c'est celle pour initialisation, on ne la copie pas
                    // Etape non imbriquée, cas classique
                    DocumentModel newstp =
                        this.createRouteStepFromRouteStepSquelette(
                                session,
                                stepDocModel,
                                bdcId,
                                cdmId,
                                scdmId,
                                cpmId,
                                parentDocument
                            );

                    this.addFeuilleRouteElementToRoute(session, modelFDRParent, parentDocument.getRef(), null, newstp);
                }
            } else {
                // Conteneur d'étape
                final STLockService lockService = STServiceLocator.getSTLockService();
                StepFolder stepFolder = stepDocModel.getAdapter(StepFolder.class);
                DocumentModel stepFolderDoc = null;
                if (FeuilleRouteExecutionType.parallel.name().equals(stepFolder.getExecution())) {
                    // Conteneur parallele
                    stepFolderDoc = session.createDocumentModel(FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER);
                    StepFolder parallelStepFolder = stepFolderDoc.getAdapter(StepFolder.class);
                    parallelStepFolder.setExecution(FeuilleRouteExecutionType.parallel);
                    stepFolderDoc =
                        this.addFeuilleRouteElementToRoute(
                                session,
                                modelFDRParent,
                                parentDocument.getRef(),
                                null,
                                stepFolderDoc
                            );
                } else {
                    // Conteneur serie
                    stepFolderDoc = session.createDocumentModel(FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER);
                    StepFolder serialStepFolder = stepFolderDoc.getAdapter(StepFolder.class);
                    serialStepFolder.setExecution(FeuilleRouteExecutionType.serial);
                    stepFolderDoc =
                        this.addFeuilleRouteElementToRoute(
                                session,
                                modelFDRParent,
                                parentDocument.getRef(),
                                null,
                                stepFolderDoc
                            );
                }

                final DocumentModelList children = this.getOrderedRouteElement(stepDocModel.getId(), session);
                lockService.lockDoc(session, stepFolderDoc);
                createRouteStepHierarchyFromRouteStepSquelette(
                    session,
                    children,
                    bdcId,
                    cdmId,
                    scdmId,
                    cpmId,
                    stepFolderDoc,
                    modelFDRParent
                );
                lockService.unlockDoc(session, stepFolderDoc);
            }
        }
    }

    /**
     * FEV525
     * Crée un nouveau RouteStep en copiant les valeurs d'un squelette de RouteStep passé en parametre,
     */
    private DocumentModel createRouteStepFromRouteStepSquelette(
        final CoreSession session,
        final DocumentModel squeletteToCopy,
        final String bdcId,
        final String cdmId,
        final String scdmId,
        final String cpmId,
        final DocumentModel parentDocument
    ) {
        DocumentModel newDocument = session.createDocumentModel(
            parentDocument.getPathAsString(),
            squeletteToCopy.getName(),
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE
        );
        DublincorePropertyUtil.setTitle(newDocument, squeletteToCopy.getTitle());

        SolonEpgRouteStep routeStepDesired = newDocument.getAdapter(SolonEpgRouteStep.class);
        SolonEpgRouteStep squelEPG = squeletteToCopy.getAdapter(SolonEpgRouteStep.class);
        SqueletteStepTypeDestinataire destinataire = squelEPG.getTypeDestinataire();

        routeStepDesired.setType(squelEPG.getType());
        routeStepDesired.setAutomaticValidation(squelEPG.isAutomaticValidation());
        routeStepDesired.setObligatoireSGG(squelEPG.isObligatoireSGG());
        routeStepDesired.setObligatoireMinistere(squelEPG.isObligatoireMinistere());
        routeStepDesired.setDeadLine(squelEPG.getDeadLine());

        switch (destinataire) {
            case BUREAU_DU_CABINET:
                routeStepDesired.setDistributionMailboxId(STConstant.PREFIX_POSTE + bdcId);
                break;
            case CHARGE_DE_MISSION:
                routeStepDesired.setDistributionMailboxId(STConstant.PREFIX_POSTE + cdmId);
                break;
            case SECRETARIAT_CHARGE_DE_MISSION:
                routeStepDesired.setDistributionMailboxId(STConstant.PREFIX_POSTE + scdmId);
                break;
            case CONSEILLER_PM:
                routeStepDesired.setDistributionMailboxId(STConstant.PREFIX_POSTE + cpmId);
                break;
            case ORGANIGRAMME:
                routeStepDesired.setDistributionMailboxId(squelEPG.getDistributionMailboxId());
                break;
            default:
                routeStepDesired.setDistributionMailboxId(squelEPG.getDistributionMailboxId());
                break;
        }

        return newDocument;
    }

    @Override
    public DocumentModel duplicateRouteStep(final CoreSession session, final DocumentModel docToCopy) {
        if (!isSqueletteFeuilleRoute(docToCopy)) {
            return super.duplicateRouteStep(session, docToCopy);
        }

        // Cas de l'étape de squelette
        fr.sword.idl.naiad.nuxeo.feuilleroute.core.service.FeuilleRouteServiceImpl frService = (fr.sword.idl.naiad.nuxeo.feuilleroute.core.service.FeuilleRouteServiceImpl) ServiceUtil.getRequiredService(
            FeuilleRouteService.class
        );
        frService.getPersister();
        DocumentModel newDocument = session.createDocumentModel(
            frService.getPersister().getOrCreateRootOfDocumentRouteInstanceStructure(session).getPathAsString(),
            docToCopy.getName() + "_copie",
            SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE
        );
        DublincorePropertyUtil.setTitle(newDocument, docToCopy.getTitle());

        SolonEpgRouteStep routeStepDesired = newDocument.getAdapter(SolonEpgRouteStep.class);
        SolonEpgRouteStep routeStepToCopy = docToCopy.getAdapter(SolonEpgRouteStep.class);

        routeStepDesired.setType(routeStepToCopy.getType());
        routeStepDesired.setDistributionMailboxId(routeStepToCopy.getDistributionMailboxId());
        routeStepDesired.setAutomaticValidation(routeStepToCopy.isAutomaticValidation());
        routeStepDesired.setObligatoireSGG(routeStepToCopy.isObligatoireSGG());
        routeStepDesired.setObligatoireMinistere(routeStepToCopy.isObligatoireMinistere());
        routeStepDesired.setDeadLine(routeStepToCopy.getDeadLine());

        routeStepDesired.setTypeDestinataire(routeStepToCopy.getTypeDestinataire());

        // alreadyDuplicated, automaticValidated ignoré de manière volontaire

        return newDocument;
    }

    @Override
    public SSFeuilleRoute duplicateFeuilleRoute(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final DocumentModel routeDocToCopy
    ) {
        SSFeuilleRouteService feuilleRouteService = SSServiceLocator.getSSFeuilleRouteService();
        FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
        EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
        ActeService acteService = SolonEpgServiceLocator.getActeService();
        STLockService lockService = STServiceLocator.getSTLockService();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        List<DocumentModel> routeStepsDoc = feuilleRouteService.findAllSteps(session, routeDocToCopy.getId());

        FeuilleRoute newRoute = feuilleRouteModelService.getDefaultRouteGlobal(session);

        // Instancie la feuille de route
        List<String> docIds = new ArrayList<>();
        docIds.add(dossierDoc.getId());
        final DocumentModel routeInstanceDoc = documentRoutingService.createNewInstance(
            session,
            newRoute.getDocument(),
            docIds
        );
        final SSFeuilleRoute stRouteInstance = routeInstanceDoc.getAdapter(SSFeuilleRoute.class);
        stRouteInstance.setTypeCreation(STVocabularyConstants.FEUILLE_ROUTE_TYPE_CREATION_INSTANCIATION);
        stRouteInstance.setTitle(dossier.getNumeroNor() + "_" + stRouteInstance.getTitle());
        session.save();

        DocumentModel newFeuilleRoute = stRouteInstance.getDocument();

        if (!LockUtils.isLockedByCurrentUser(session, routeInstanceDoc.getRef())) {
            lockService.lockDoc(session, newFeuilleRoute);
        }

        // Charge l'arborescence complete des documents à copier
        // On suppose que tous les éléments sont du meme arbre (pas de vérification, trop couteux)
        final DocumentRouteTreeElement sourceTree = getDocumentRouteTree(session, routeStepsDoc.get(0));

        // Marque les documents à coller et leurs parents
        final Set<String> documentToPasteIdSet = new HashSet<>();
        for (final DocumentModel doc : routeStepsDoc) {
            SSRouteStep route = doc.getAdapter(SSRouteStep.class);
            if (
                !route.getType().equals(VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION) &&
                !(
                    acteService.isActeTexteNonPublie(dossier.getTypeActe()) &&
                    EPGFeuilleRouteServiceImpl.INCOMPATIBLE_STEPS_ACTE_TXT_NON_PUB.contains(route.getType())
                )
            ) {
                documentToPasteIdSet.add(doc.getId());
            }
        }
        // on initialise la source comme étant a copier, contournement pour mantis.
        sourceTree.setToPaste(true);
        markElementToPaste(sourceTree, documentToPasteIdSet);

        // Construit des nouveaux arbres avec uniquement les éléments à copier
        final List<DocumentRouteTreeElement> destTreeList = pruneDocumentRouteTree(sourceTree, newFeuilleRoute);

        // Duplique les documents du nouvel arbre récursivement
        for (final DocumentRouteTreeElement routeTree : destTreeList) {
            pasteRouteTreeIntoRoute(session, newFeuilleRoute, routeTree, newFeuilleRoute, null, false);
        }

        lockService.unlockDoc(session, newFeuilleRoute);

        return stRouteInstance;
    }

    @Override
    public DocumentModel addFeuilleRouteElementToRoute(
        final CoreSession session,
        final DocumentModel documentRouteDoc,
        final DocumentRef parentDocumentRef,
        final String sourceName,
        DocumentModel routeElementDoc
    ) {
        final STLockService lockService = STServiceLocator.getSTLockService();
        if (!lockService.isLockByCurrentUser(session, documentRouteDoc)) {
            throw new FeuilleRouteNotLockedException();
        }

        // Renseigne l'UUID de la feuille de route dans l'étape (champ dénormalisé)
        String docName = null;
        final PathSegmentService pathSegmentService = STServiceLocator.getPathSegmentService();
        final DocumentModel parentDocument = session.getDocument(parentDocumentRef);
        if (
            SSConstant.ROUTE_STEP_DOCUMENT_TYPE.equals(routeElementDoc.getType()) ||
            SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE.equals(routeElementDoc.getType())
        ) {
            final SSRouteStep routeStep = routeElementDoc.getAdapter(SSRouteStep.class);
            routeStep.setDocumentRouteId(documentRouteDoc.getId());
            docName = pathSegmentService.generatePathSegment(routeElementDoc);
        } else if (FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER.equals(routeElementDoc.getType())) {
            final StepFolder stepFolder = routeElementDoc.getAdapter(StepFolder.class);
            if (stepFolder.isParallel()) {
                docName = DOCUMENT_NAME_PARALLELE;
            } else {
                docName = DOCUMENT_NAME_SERIE;
            }
        }

        routeElementDoc.setPathInfo(parentDocument.getPathAsString(), docName);
        String parentLifecycleState = parentDocument.getCurrentLifeCycleState();
        String lifecycleState;
        if (parentLifecycleState.equals(FeuilleRouteElement.ElementLifeCycleState.draft.name())) {
            lifecycleState = FeuilleRouteElement.ElementLifeCycleState.draft.name();
        } else if (parentLifecycleState.equals(FeuilleRouteElement.ElementLifeCycleState.validated.name())) {
            lifecycleState = FeuilleRouteElement.ElementLifeCycleState.validated.name();
        } else {
            lifecycleState = FeuilleRouteElement.ElementLifeCycleState.ready.name();
        }

        routeElementDoc.putContextData(LifeCycleConstants.INITIAL_LIFECYCLE_STATE_OPTION_NAME, lifecycleState);

        routeElementDoc = session.createDocument(routeElementDoc);
        session.orderBefore(parentDocumentRef, routeElementDoc.getName(), sourceName);
        session.save();

        if (LOGGER.isInfoEnable()) {
            LOGGER.info(
                STLogEnumImpl.DEFAULT,
                "Création dans le conteneur " +
                parentDocument.getName() +
                " de l'élément " +
                routeElementDoc.getName() +
                " " +
                " avant l'élément " +
                sourceName
            );
        }

        return routeElementDoc;
    }

    @Override
    public boolean isRoutingTaskTypeValiderAllowed(SSPrincipal principal, DossierLink dossierLink) {
        if (dossierLink == null) {
            return false;
        } else {
            String routingTaskType = dossierLink.getRoutingTaskType();
            if (routingTaskType == null) {
                return false;
            }

            boolean pourSggEx =
                routingTaskType.equals(VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SGG) &&
                principal.isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_POUR_ATTRIBUTION_SGG_EXECUTOR);

            boolean pourSignatureEx =
                routingTaskType.equals(VocabularyConstants.ROUTING_TASK_TYPE_SIGNATURE) &&
                principal.isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_POUR_SIGNATURE_EXECUTOR);

            boolean pourContreseingEx =
                routingTaskType.equals(VocabularyConstants.ROUTING_TASK_TYPE_CONTRESEING) &&
                principal.isMemberOf(SolonEpgBaseFunctionConstant.ETAPE_POUR_CONTRESEING_EXECUTOR);

            return (
                ROUTING_TASK_TYPES_ALLOWED.contains(routingTaskType) ||
                pourSggEx ||
                pourSignatureEx ||
                pourContreseingEx
            );
        }
    }
}
