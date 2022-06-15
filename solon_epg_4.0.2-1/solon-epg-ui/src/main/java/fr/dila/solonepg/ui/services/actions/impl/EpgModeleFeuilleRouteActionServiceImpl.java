package fr.dila.solonepg.ui.services.actions.impl;

import static fr.dila.st.core.util.ResourceHelper.getString;

import com.google.common.collect.ImmutableSet;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.api.service.EpgDocumentRoutingService;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.actions.EpgModeleFeuilleRouteActionService;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.solonepg.ui.th.bean.EpgCreateMassModeleForm;
import fr.dila.solonepg.ui.th.model.bean.EpgModeleFdrForm;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.constant.SSEventConstant;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.ss.api.criteria.SubstitutionCriteria;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.SSFeuilleRouteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.actions.impl.ModeleFeuilleRouteActionServiceImpl;
import fr.dila.ss.ui.th.bean.ModeleFdrForm;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STLockService;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.api.vocabulary.VocabularyEntry;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.services.actions.impl.STLockActionServiceImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteElement.ElementLifeCycleState;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.exception.FeuilleRouteAlreadyLockedException;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.exception.FeuilleRouteNotLockedException;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.uidgen.UIDGeneratorService;
import org.nuxeo.ecm.core.uidgen.UIDSequencer;

public class EpgModeleFeuilleRouteActionServiceImpl
    extends ModeleFeuilleRouteActionServiceImpl
    implements EpgModeleFeuilleRouteActionService {
    private static final STLogger LOG = STLogFactory.getLog(EpgModeleFeuilleRouteActionServiceImpl.class);

    private static final Set<String> INCOMPATIBLE_STEPS_ACTE_TXT_NON_PUB = ImmutableSet.of(
        VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE,
        VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO,
        VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO
    );

    @Override
    public boolean canUserDeleteRoute(SpecificContext context) {
        DocumentModel doc = context.getCurrentDocument();
        // La feuille de route par défaut globale ne peut pas être supprimée
        SolonEpgFeuilleRoute route = doc.getAdapter(SolonEpgFeuilleRoute.class);
        return !route.isFeuilleRouteDefautGlobal() && super.canUserDeleteRoute(context);
    }

    @Override
    public DocumentModel duplicateRouteModel(SpecificContext context) {
        final EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
        DocumentModel doc = context.getCurrentDocument();
        CoreSession session = context.getSession();
        SSPrincipal principal = (SSPrincipal) session.getPrincipal();

        final List<String> groups = context.getWebcontext().getPrincipal().getGroups();
        boolean isAdminFonctionnel = groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR);
        String ministere = null;
        if (!isAdminFonctionnel) {
            // Affecte la nouvelle feuille de route au premier ministère de cet administrateur ministériel
            Set<String> ministereSet = principal.getMinistereIdSet();
            if (CollectionUtils.isEmpty(ministereSet)) {
                throw new NuxeoException(getString("admin.modele.message.absence.ministere.adminMin"));
            }
            ministere = ministereSet.iterator().next();
        }
        SSFeuilleRoute newFeuilleRoute = documentRoutingService.duplicateRouteModel(session, doc, ministere);
        DocumentModel newDoc = newFeuilleRoute.getDocument();

        // Compteur
        final UIDGeneratorService uidGeneratorService = ServiceUtil.getRequiredService(UIDGeneratorService.class);
        UIDSequencer sequencer = uidGeneratorService.getSequencer();
        SolonEpgFeuilleRoute feuilleRoute = newDoc.getAdapter(SolonEpgFeuilleRoute.class);
        feuilleRoute.setNumero(sequencer.getNextLong("SOLON_EPG_FDR_MODEL") + 1);
        feuilleRoute.save(session);

        // Affiche un message d'information
        context.getMessageQueue().addInfoToQueue("admin.modele.message.dupliquer.success");

        // on logge l'action de duplication du modèle de feuille de route
        logActionModeleFdr(session, feuilleRoute, SSEventConstant.DUPLICATION_MODELE_FDR_EVENT);

        return feuilleRoute.getDocument();
    }

    @Override
    public String[] splitIntitule(String intitule, String ministereID, String directionID, String typeActeId) {
        if (StringUtils.isEmpty(intitule)) {
            return new String[] { "", "" };
        }

        String fixe = null;
        String libre = null;

        if (StringUtils.isNotBlank(typeActeId)) {
            // le type d'acte est renseigné.
            // Tout ce qui est avant lui dans l'intitulé est la partie fixe, ce qui est après est la partie libre
            String typeActeLabel = null;

            //on récupère les vocabulaires du directory
            final SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
            final List<String> listId = new ArrayList<>();
            listId.add(typeActeId);

            typeActeLabel =
                vocService.getEntryLabel(
                    VocabularyConstants.TYPE_ACTE_VOCABULARY,
                    typeActeId,
                    STVocabularyConstants.COLUMN_LABEL
                );

            if (StringUtils.isEmpty(typeActeLabel)) {
                throw new NuxeoException(getString("admin.modele.message.error.get.voc.typeActe", typeActeId));
            }

            fixe = StringUtils.substringBefore(intitule, typeActeLabel) + typeActeLabel;
            libre = StringUtils.substringAfter(intitule, typeActeLabel);
            libre = StringUtils.substring(libre, 3); // enleve le " - "
        } else {
            if (StringUtils.isNotBlank(directionID)) {
                // Si la direction est renseignée, le ministère l'est aussi obligatoirement
                // la chaine de début est donc du style "XXX - X - " (10 chars)
                fixe = intitule.substring(0, 7);
                libre = intitule.substring(10);
            } else {
                if (StringUtils.isNotBlank(ministereID)) {
                    // seul le trigramme du ministère est renseigné
                    // la chaine de début est donc du style "XXX - " (6 chars)
                    fixe = intitule.substring(0, 3);
                    libre = intitule.substring(6);
                } else {
                    // Rien n'est renseigné Tout va dans le titre libre
                    fixe = "";
                    libre = intitule;
                }
            }
        }
        return new String[] { fixe, libre };
    }

    @Override
    public boolean checkBeforeSave(SpecificContext context) {
        EpgModeleFdrForm modele = context.getFromContextData(EpgContextDataKey.EPG_MODELE_FORM);

        String idMinistere = modele.getIdMinistere();
        String idDirection = modele.getIdDirection();

        // Si le ministère n'est pas renseigné, la direction doit être non renseignée
        if (StringUtils.isBlank(idMinistere) && StringUtils.isNotBlank(idDirection)) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("epg.feuilleRoute.action.save.direction.need.ministere"));
            return false;
        }
        // Il faut que la direction appartienne au ministère
        if (StringUtils.isNotBlank(idMinistere) && StringUtils.isNotBlank(idDirection)) {
            UniteStructurelleNode directionNode = STServiceLocator
                .getOrganigrammeService()
                .getOrganigrammeNodeById(idDirection, OrganigrammeType.DIRECTION);

            if (directionNode != null) {
                if (directionNode.getType() != OrganigrammeType.DIRECTION) {
                    context
                        .getMessageQueue()
                        .addErrorToQueue(getString("epg.feuilleRoute.action.save.direction.not.direction"));
                    return false;
                }

                String norDirection = directionNode.getNorDirectionForMinistereId(idMinistere);
                if (StringUtils.isBlank(norDirection)) {
                    context
                        .getMessageQueue()
                        .addErrorToQueue(getString("epg.feuilleRoute.action.save.direction.not.in.ministere"));
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public DocumentModel updateDocument(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel changeableDocument = context.getCurrentDocument();

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (!LockUtils.isLockedByCurrentUser(session, changeableDocument.getRef())) {
            context.getMessageQueue().addWarnToQueue(getString(STLockActionServiceImpl.LOCK_LOST_ERROR_MSG));
            return null;
        }

        SolonEpgFeuilleRoute route = changeableDocument.getAdapter(SolonEpgFeuilleRoute.class);
        // Nettoyage des données en entrée
        if (StringUtils.isBlank(route.getMinistere())) {
            route.setMinistere(null);
        }
        if (StringUtils.isBlank(route.getDirection())) {
            route.setDirection(null);
        }
        if (StringUtils.isBlank(route.getTypeActe())) {
            route.setTypeActe(null);
        }
        if (checkUniciteData(context, changeableDocument)) {
            // Vérifie la compatibilité des étapes avec le type d'acte
            EPGFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
            List<DocumentModel> stepsDoc = feuilleRouteService.findAllSteps(session, changeableDocument.getId());
            if (
                !SolonEpgActionServiceLocator
                    .getEpgDocumentRoutingActionService()
                    .checkValiditySteps(context, route, stepsDoc)
            ) {
                context
                    .getMessageQueue()
                    .addWarnToQueue(getString("feedback.feuilleRoute.squelette.typeActe.incompatible"));
                return null;
            }

            // on logge l'action de modification du modèle de feuille de route
            logActionModeleFdr(session, route, SSEventConstant.UPDATE_MODELE_FDR_EVENT);

            return session.saveDocument(route.getDocument());
        }
        return null;
    }

    @Override
    protected boolean checkUniciteData(SpecificContext context, DocumentModel doc) {
        // Vérifie l'unicité de l'intitulé de modèle de feuille de route par
        // ministère
        final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
        SolonEpgFeuilleRoute route = doc.getAdapter(SolonEpgFeuilleRoute.class);
        if (!feuilleRouteModelService.isModeleDefautUnique(context.getSession(), route)) {
            if (
                StringUtils.isBlank(route.getMinistere()) &&
                StringUtils.isBlank(route.getDirection()) &&
                StringUtils.isBlank(route.getTypeActe())
            ) {
                // Aucun des 3 critères => Modele par defaut global
                context
                    .getMessageQueue()
                    .addErrorToQueue(getString("epg.feuilleRoute.action.save.duplicateFeuilleRouteGlobal.error"));
            } else if (StringUtils.isBlank(route.getTypeActe()) && StringUtils.isBlank(route.getDirection())) {
                // Ministere non null
                context
                    .getMessageQueue()
                    .addErrorToQueue(getString("epg.feuilleRoute.action.save.duplicateFeuilleRoute.error.min"));
            } else if (StringUtils.isBlank(route.getDirection())) {
                // Ministere et Type acte non null
                context
                    .getMessageQueue()
                    .addErrorToQueue(
                        getString("epg.feuilleRoute.action.save.duplicateFeuilleRoute.error.min.typeActe")
                    );
            } else if (StringUtils.isBlank(route.getTypeActe())) {
                // Ministere et Type acte non null
                context
                    .getMessageQueue()
                    .addErrorToQueue(getString("epg.feuilleRoute.action.save.duplicateFeuilleRoute.error.min.dir"));
            } else {
                // Les 3 critère rempli et autres cas
                context
                    .getMessageQueue()
                    .addErrorToQueue(
                        getString("epg.feuilleRoute.action.save.duplicateFeuilleRoute.error.min.dir.typeActe")
                    );
            }
            return false;
        }
        return super.checkUniciteData(context, doc);
    }

    @Override
    public DocumentModel initFeuilleRoute(CoreSession session, ModeleFdrForm form, String creatorName) {
        EpgModeleFdrForm epgForm = (EpgModeleFdrForm) form;
        form.setIntitule(
            SolonEpgServiceLocator
                .getFeuilleRouteModelService()
                .creeTitleModeleFDR(
                    epgForm.getIdMinistere(),
                    epgForm.getIdDirection(),
                    epgForm.getTypeActe(),
                    epgForm.getIntituleLibre()
                )
        );
        DocumentModel route = super.initFeuilleRoute(session, form, creatorName);
        // Initialiser le numéro de la fdr
        final UIDGeneratorService uidGeneratorService = ServiceUtil.getRequiredService(UIDGeneratorService.class);
        UIDSequencer sequencer = uidGeneratorService.getSequencer();
        route.getAdapter(SolonEpgFeuilleRoute.class).setNumero(sequencer.getNextLong("SOLON_EPG_FDR_MODEL") + 1);
        return route;
    }

    /**
     * on logge l'action du modèle de feuille de route.
     *
     */
    protected void logActionModeleFdr(CoreSession session, SolonEpgFeuilleRoute route, String event) {
        final JournalService journalService = STServiceLocator.getJournalService();
        String comment = "Action sur le modèle de feuille de route " + route.getTitle();
        if (SSEventConstant.UPDATE_MODELE_FDR_EVENT.equals(event)) {
            comment = "Modification du modèle de feuille de route [" + route.getTitle() + "]";
        } else if (SSEventConstant.DUPLICATION_MODELE_FDR_EVENT.equals(event)) {
            comment = "Duplication du modèle de feuille de route [" + route.getTitle() + "]";
        }
        journalService.journaliserActionAdministration(session, event, comment);
    }

    /**
     * Retourne le prédicat permettant de filter les modèles de feuille compatible avec un dossier
     *
     */
    public String getForMassSubstitutionCriteria(SpecificContext context) {
        SSPrincipal principal = (SSPrincipal) context.getSession().getPrincipal();
        Set<String> ministeres = principal.getMinistereIdSet();
        StringBuilder strBuilder = new StringBuilder("AND (");
        strBuilder.append("fdr:ministere IN ('");
        strBuilder.append(StringUtils.join(ministeres, "','"));
        strBuilder.append("') ");
        strBuilder.append(" OR (");
        strBuilder.append("fdr:ministere IS NULL ");
        strBuilder.append("AND fdr:direction IS NULL ");
        strBuilder.append("AND fdr:typeActe IS NULL ");
        strBuilder.append("AND fdr:feuilleRouteDefaut != 1 ");
        strBuilder.append(")");

        strBuilder.append(")");
        return strBuilder.toString();
    }

    /**
     * Retourne le prédicat permettant de filter les modèles de feuille compatible avec un dossier
     *
     */
    public String getForSubstitutionCriteria(boolean includeSggModelForSubstitution, DocumentModel dossierDocModel) {
        Dossier dossier = dossierDocModel.getAdapter(Dossier.class);
        StringBuilder strBuilder = new StringBuilder("AND (");
        if (includeSggModelForSubstitution) {
            strBuilder.append("fdr:ministere = '");
            strBuilder.append(dossier.getMinistereAttache());
            strBuilder.append("' OR (");
            strBuilder.append("fdr:ministere IS NULL ");
            strBuilder.append("AND fdr:direction IS NULL ");
            strBuilder.append("AND fdr:typeActe IS NULL ");
            strBuilder.append("AND fdr:feuilleRouteDefaut != 1 ");
            strBuilder.append(")");
        } else {
            strBuilder.append("(fdr:ministere = '");
            strBuilder.append(dossier.getMinistereAttache());
            strBuilder.append("' AND fdr:direction = '");
            strBuilder.append(dossier.getDirectionAttache());
            strBuilder.append("' AND fdr:typeActe = '");
            strBuilder.append(dossier.getTypeActe());
            strBuilder.append("')");
            // ajout du modèle par défaut commun à tout les ministères
            strBuilder.append(" OR (");
            strBuilder.append("fdr:ministere IS NULL ");
            strBuilder.append("AND fdr:direction IS NULL ");
            strBuilder.append("AND fdr:typeActe IS NULL ");
            strBuilder.append("AND fdr:feuilleRouteDefaut = 1 ");
            strBuilder.append(")");
        }

        strBuilder.append(")");
        return strBuilder.toString();
    }

    public Boolean isObligatoireSggUpdater(SpecificContext context) {
        SSPrincipal principal = (SSPrincipal) context.getSession().getPrincipal();
        final List<String> groups = principal.getGroups();

        boolean isAdminFonctionnel = groups.contains(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR);
        boolean isEtapeObligatoirUpdater = groups.contains(
            SolonEpgBaseFunctionConstant.FEUILLE_ROUTE_OBLIGATOIRE_SGG_UPDATER
        );
        return isAdminFonctionnel || isEtapeObligatoirUpdater;
    }

    /**
     * Surcharge de validateRouteModel pour gérer le cas du type d'acte texte non publié
     */
    @Override
    public void validateRouteModel(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel routeDoc = context.getCurrentDocument();

        SolonEpgFeuilleRoute fdr = routeDoc.getAdapter(SolonEpgFeuilleRoute.class);
        final EpgDocumentRoutingService docRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();

        List<DocumentModel> stepsDoc = docRoutingService.getOrderedRouteElement(routeDoc.getId(), session);

        if (CollectionUtils.isNotEmpty(stepsDoc)) {
            String errorCompatibility = SolonEpgServiceLocator
                .getDocumentRoutingService()
                .isNextPostsCompatibleWithNextStep(session, stepsDoc.subList(1, stepsDoc.size()));
            if (StringUtils.isNotEmpty(errorCompatibility)) {
                context.getMessageQueue().addErrorToQueue(getString(errorCompatibility));
            }
        }

        if (Boolean.TRUE.equals(SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(fdr.getTypeActe()))) {
            for (DocumentModel stepDoc : stepsDoc) {
                // On ne vérifie pas pour les stepFolder
                if (!SSConstant.STEP_FOLDER_DOCUMENT_TYPE.equals(stepDoc.getType())) {
                    SolonEpgRouteStep step = stepDoc.getAdapter(SolonEpgRouteStep.class);
                    String stepType = step.getType();
                    if (INCOMPATIBLE_STEPS_ACTE_TXT_NON_PUB.contains(stepType)) {
                        context
                            .getMessageQueue()
                            .addErrorToQueue(getString("feedback.solonepg.feuilleRoute.cannotValidate"));
                        context
                            .getMessageQueue()
                            .addErrorToQueue(getString("feedback.solonepg.feuilleRoute.acteIncompatible"));
                        return;
                    }
                }
            }
        }

        UniteStructurelleNode directionNode = STServiceLocator
            .getOrganigrammeService()
            .getOrganigrammeNodeById(fdr.getDirection(), OrganigrammeType.DIRECTION);
        if (directionNode != null && directionNode.getType() != OrganigrammeType.DIRECTION) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("epg.feuilleRoute.action.save.direction.not.direction"));
            return;
        }

        super.validateRouteModel(context);
    }

    /**
     * Retourne vrai si l'utilisateur courant peut valider le squelette de
     * feuille de route.
     *
     */
    public boolean canValidateSquelette(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentDoc = context.getCurrentDocument();
        final SSFeuilleRoute feuilleRoute = getRelatedRoute(session, currentDoc);
        DocumentModel feuilleRouteDoc = feuilleRoute.getDocument();

        // Les squelettes verrouillés par un autre utilisateur sont non modifiables
        if (!LockUtils.isLockedByCurrentUser(session, feuilleRouteDoc.getRef())) {
            return false;
        }

        final String lifeCycleState = feuilleRoute.getDocument().getCurrentLifeCycleState();
        return ElementLifeCycleState.draft.name().equals(lifeCycleState);
    }

    /**
     * Retourne vrai si l'utilisateur courant peut invalider le squelette de
     * feuille de route.
     *
     */
    public boolean canInvalidateSquelette(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentDoc = context.getCurrentDocument();
        final SSFeuilleRoute feuilleRoute = getRelatedRoute(session, currentDoc);

        // On peut invalider un squelette de feuille de route uniquement à l'état validé
        final String lifeCycleState = feuilleRoute.getDocument().getCurrentLifeCycleState();
        return ElementLifeCycleState.validated.name().equals(lifeCycleState);
    }

    @Override
    public void validateSquelette(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentDoc = context.getCurrentDocument();
        SSFeuilleRoute feuilleRoute = getRelatedRoute(session, currentDoc);

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (!LockUtils.isLockedByCurrentUser(session, feuilleRoute.getDocument().getRef())) {
            context.getMessageQueue().addErrorToQueue(getString(STLockActionServiceImpl.LOCK_LOST_ERROR_MSG));
            return;
        }

        // Vérifie l'unicité du squelette pour ce type d'acte
        SolonEpgFeuilleRoute epgFeuilleRoute = feuilleRoute.getDocument().getAdapter(SolonEpgFeuilleRoute.class);
        final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
        if (feuilleRouteModelService.existsSqueletteWithTypeActe(session, epgFeuilleRoute.getTypeActe())) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("feedback.feuilleRoute.squelette.validate.error.typeNotUnique"));
            return;
        }

        final EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
        try {
            epgFeuilleRoute = documentRoutingService.validateSquelette(epgFeuilleRoute, session);
        } catch (FeuilleRouteNotLockedException e) {
            context.getMessageQueue().addErrorToQueue(getString("feedback.casemanagement.document.route.not.locked"));
            LOG.warn(STLogEnumImpl.FAIL_UPDATE_FDR, e);
            return;
        }

        try {
            documentRoutingService.unlockDocumentRoute(epgFeuilleRoute, session);
        } catch (FeuilleRouteNotLockedException e) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("feedback.casemanagement.document.route.already.locked"));
            LOG.warn(STLogEnumImpl.FAIL_UNLOCK_DOC_TEC, e);
            return;
        }

        // Affiche un message d'information
        context.getMessageQueue().addSuccessToQueue(getString("admin.squelette.action.validated.success"));
    }

    @Override
    public void updateSquelette(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentDoc = context.getCurrentDocument();
        SolonEpgFeuilleRoute route = currentDoc.getAdapter(SolonEpgFeuilleRoute.class);

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (!LockUtils.isLockedByCurrentUser(session, route.getDocument().getRef())) {
            context.getMessageQueue().addErrorToQueue(getString("st.lock.action.lockLost.error"));
            return;
        }

        // Vérifie la compatibilité des étapes avec le type d'acte
        SSFeuilleRouteService feuilleRouteService = SSServiceLocator.getSSFeuilleRouteService();
        List<DocumentModel> stepsDoc = feuilleRouteService.findAllSteps(session, currentDoc.getId());
        if (
            !SolonEpgActionServiceLocator
                .getEpgDocumentRoutingActionService()
                .checkValiditySteps(context, route, stepsDoc)
        ) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("feedback.feuilleRoute.squelette.typeActe.incompatible"));
            return;
        }

        // on logge l'action de modification du squelette de feuille de route
        logActionSqueletteFdr(session, route, SolonEpgEventConstant.UPDATE_SQUELETTE_FDR_EVENT);

        session.saveDocument(route.getDocument());
    }

    /**
     * on logge l'action du squelette de feuille de route.
     *
     */
    protected void logActionSqueletteFdr(CoreSession session, SolonEpgFeuilleRoute squelette, String event) {
        final JournalService journalService = STServiceLocator.getJournalService();
        String comment = "Action sur le squelette de feuille de route " + squelette.getTitle();
        if (SolonEpgEventConstant.UPDATE_SQUELETTE_FDR_EVENT.equals(event)) {
            comment = "Modification du squelette de feuille de route [" + squelette.getTitle() + "]";
        } else if (SolonEpgEventConstant.DUPLICATION_SQUELETTE_FDR_EVENT.equals(event)) {
            comment = "Duplication du squelette de feuille de route [" + squelette.getTitle() + "]";
        }
        journalService.journaliserActionAdministration(session, event, comment);
    }

    /**
     * Invalide le squelette de feuille de route en cours.
     *
     */
    public void invalidateSquelette(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentDoc = context.getCurrentDocument();
        SolonEpgFeuilleRoute route = currentDoc.getAdapter(SolonEpgFeuilleRoute.class);

        final EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
        try {
            documentRoutingService.lockDocumentRoute(route, session);
        } catch (FeuilleRouteAlreadyLockedException e) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("feedback.casemanagement.document.route.squelette.already.locked"));
            LOG.warn(STLogEnumImpl.FAIL_LOCK_DOC_TEC, e);
            return;
        }
        try {
            documentRoutingService.invalidateSquelette(route, session);
        } catch (FeuilleRouteNotLockedException e) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("feedback.casemanagement.document.route.squelette.not.locked"));
            LOG.warn(EpgLogEnumImpl.FAIL_INVALIDATE_SQUELETTE, e);
            return;
        }

        // on verrouille le squelette
        verrouillerSquelette(context);
        // Affiche un message d'information
        context.getMessageQueue().addSuccessToQueue(getString("feuilleRoute.squelette.action.invalidated.success"));
    }

    /**
     * Détermine si l'utilisateur a le droit de verrouiller le squelette.
     *
     * @return Droit de verrouiller
     */
    public boolean canUserVerrouillerSquelette(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentDoc = context.getCurrentDocument();

        // On peut verrouiller un squelette de feuille de route uniquement à l'état brouillon
        final String lifeCycleState = currentDoc.getCurrentLifeCycleState();
        if (!ElementLifeCycleState.draft.name().equals(lifeCycleState)) {
            return false;
        }
        // Verifie si le document est verrouillé
        return (
            LockUtils.isLockedByAnotherUser(session, currentDoc.getRef()) ||
            !LockUtils.isLocked(session, currentDoc.getRef())
        );
    }

    /**
     * Verrouille le squelette
     */
    public void verrouillerSquelette(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentDoc = context.getCurrentDocument();
        STLockService lockActionService = STServiceLocator.getSTLockService();

        if (LockUtils.isLockedByAnotherUser(session, currentDoc.getRef())) {
            try {
                lockActionService.unlockDocUnrestricted(session, currentDoc);
            } catch (NuxeoException e) {
                context.getMessageQueue().addErrorToQueue(getString("st.lock.action.unlock.error"));
                LOG.warn(STLogEnumImpl.FAIL_UNLOCK_DOC_TEC, e);
                return;
            }
        }
        try {
            lockActionService.lockDoc(session, currentDoc);
        } catch (NuxeoException e) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("feedback.casemanagement.document.route.squelette.already.locked"));
            LOG.warn(STLogEnumImpl.FAIL_LOCK_DOC_TEC, e);
            return;
        }

        // Affiche un message d'information
        context.getMessageQueue().addInfoToQueue(getString("info.organigrammeManager.node.lock"));
    }

    @Override
    public void createModeleFDRMass(SpecificContext context) {
        CoreSession session = context.getSession();
        EpgCreateMassModeleForm massModeleForm = context.getFromContextData(EpgContextDataKey.CREATE_MASS_MODELE_FORM);

        final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
        final EpgOrganigrammeService organigrameService = SolonEpgServiceLocator.getEpgOrganigrammeService();
        final JournalService journalService = STServiceLocator.getJournalService();

        EntiteNode ministereNode = organigrameService.getOrganigrammeNodeById(
            massModeleForm.getMinistere(),
            OrganigrammeType.MINISTERE
        );
        PosteNode bdcNode = organigrameService.getOrganigrammeNodeById(massModeleForm.getBdc(), OrganigrammeType.POSTE);
        PosteNode cdmNode = organigrameService.getOrganigrammeNodeById(
            massModeleForm.getChargeMission(),
            OrganigrammeType.POSTE
        );
        PosteNode scdmNode = organigrameService.getOrganigrammeNodeById(
            massModeleForm.getSecretariatCM(),
            OrganigrammeType.POSTE
        );
        PosteNode cpmNode = organigrameService.getOrganigrammeNodeById(
            massModeleForm.getConseillerPM(),
            OrganigrammeType.POSTE
        );

        if (checkErrorBeforeCreateModeleFDRMass(context, ministereNode, bdcNode, cdmNode, scdmNode, cpmNode)) {
            final SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();

            // si des modèles existent déjà pour le ministère, il faut lister
            // leur Type d'acte
            final List<DocumentModel> lstModeleExistant = feuilleRouteModelService.getNonDeletedFdrModelFromMinistereAndDirection(
                session,
                massModeleForm.getMinistere(),
                null,
                false
            );

            if (CollectionUtils.isNotEmpty(lstModeleExistant)) {
                final List<String> listTypeActeEntry = new ArrayList<>();

                for (DocumentModel model : lstModeleExistant) {
                    final SolonEpgFeuilleRoute solonEpgFeuilleRoute = model.getAdapter(SolonEpgFeuilleRoute.class);
                    if (
                        solonEpgFeuilleRoute.getTypeActe() != null &&
                        solonEpgFeuilleRoute.isFeuilleRouteDefaut() &&
                        !listTypeActeEntry.contains(solonEpgFeuilleRoute.getTypeActe())
                    ) {
                        listTypeActeEntry.add(solonEpgFeuilleRoute.getTypeActe());
                    }
                }

                final List<VocabularyEntry> vocLst = vocService.getVocabularyEntryListFromDirectory(
                    VocabularyConstants.TYPE_ACTE_VOCABULARY,
                    VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA,
                    listTypeActeEntry
                );

                if (CollectionUtils.isNotEmpty(listTypeActeEntry)) {
                    context
                        .getMessageQueue()
                        .addInfoToQueue(
                            getString(
                                "admin.modele.masse.create.action.message.modele.defaut.exist",
                                vocLst.stream().map(VocabularyEntry::getLabel).collect(Collectors.joining(", "))
                            )
                        );
                }
            }

            final Integer nbModeleCrees = feuilleRouteModelService.creeModelesViaSquelette(
                session,
                massModeleForm.getMinistere(),
                massModeleForm.getBdc(),
                massModeleForm.getChargeMission(),
                massModeleForm.getSecretariatCM(),
                massModeleForm.getConseillerPM()
            );

            if (nbModeleCrees > 0) {
                context
                    .getMessageQueue()
                    .addToastSuccess(getString("admin.modele.masse.create.action.message.success"));

                journalService.journaliserActionAdministration(
                    session,
                    SSEventConstant.CREATE_MODELE_FDR_EVENT,
                    getString(
                        "admin.modele.masse.create.action.message.journal",
                        nbModeleCrees,
                        ministereNode.getLabel(),
                        bdcNode.getLabel(),
                        cdmNode.getLabel(),
                        scdmNode.getLabel(),
                        cpmNode.getLabel()
                    )
                );
            } else {
                context
                    .getMessageQueue()
                    .addErrorToQueue(getString("admin.modele.masse.create.action.message.error.no.squelette"));
            }
        }
    }

    /**
     * Vérification des entrant pour la création de modele de feuille de route en masse
     */
    private boolean checkErrorBeforeCreateModeleFDRMass(
        SpecificContext context,
        EntiteNode ministereNode,
        PosteNode bdcNode,
        PosteNode cdmNode,
        PosteNode scdmNode,
        PosteNode cpmNode
    ) {
        final OrganigrammeService organigrameService = SolonEpgServiceLocator.getEpgOrganigrammeService();

        if (ministereNode == null) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("admin.modele.masse.create.action.message.error.ministere"));
        }
        if (bdcNode == null) {
            context.getMessageQueue().addErrorToQueue(getString("admin.modele.masse.create.action.message.error.bdc"));
        }
        if (cdmNode == null) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("admin.modele.masse.create.action.message.error.charge.mission"));
        }
        if (scdmNode == null) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("admin.modele.masse.create.action.message.error.secretariat.cm"));
        }
        if (cpmNode == null) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("admin.modele.masse.create.action.message.error.conseiller.pm"));
        }
        List<PosteNode> listePostesMin = organigrameService.getAllSubPostes(ministereNode);
        if (!listePostesMin.contains(bdcNode)) {
            context
                .getMessageQueue()
                .addErrorToQueue(getString("admin.modele.masse.create.action.message.error.bdc.dans.min"));
        }
        return CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue());
    }

    @Override
    public String getContentViewCriteriaSubstitution(SpecificContext context) {
        SubstitutionCriteria criteria = context.getFromContextData(SSContextDataKey.SUBSTITUTION_CRITERIA);

        StringBuilder str = new StringBuilder("fdr.")
            .append(STSchemaConstant.ECM_SCHEMA_PREFIX)
            .append(":")
            .append(STSchemaConstant.ECM_CURRENT_LIFE_CYCLE_STATE)
            .append(" = '")
            .append(ElementLifeCycleState.validated.name())
            .append("'");
        if (criteria != null) {
            if (CollectionUtils.isNotEmpty(criteria.getListIdMinistereAttributaire())) {
                str
                    .append("AND fdr.")
                    .append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX)
                    .append(":")
                    .append(SSFeuilleRouteConstant.FEUILLE_ROUTE_MINISTERE_PROPERTY)
                    .append(" IN ")
                    .append(
                        criteria
                            .getListIdMinistereAttributaire()
                            .stream()
                            .collect(Collectors.joining("','", "('", "')"))
                    );
            }
            if (StringUtils.isNotEmpty(criteria.getTypeActe())) {
                str
                    .append("AND fdr.")
                    .append(SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA_PREFIX)
                    .append(":")
                    .append(SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY)
                    .append(" = '")
                    .append(criteria.getTypeActe())
                    .append("'");
            }
        }
        return str.toString();
    }

    @Override
    public void deleteMassModele(SpecificContext context) {
        List<String> idsModele = context.getFromContextData(SSContextDataKey.ID_MODELES);

        DocumentRef[] docRefs = idsModele.stream().map(id -> new IdRef(id)).toArray(DocumentRef[]::new);
        context.getSession().removeDocuments(docRefs);

        context.getMessageQueue().addToastSuccess(getString("modeleFDR.action.supprimer.mass.succes.message"));
    }

    @Override
    public void deleteModele(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentDoc = context.getCurrentDocument();

        super.deleteModele(context);

        // retirer le modèle des favoris de consultation et derniers modèles consultés
        Set<String> modeleToRemove = Collections.singleton(currentDoc.getId());
        EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
        String userworkspacePath = STServiceLocator
            .getUserWorkspaceService()
            .getCurrentUserPersonalWorkspace(session)
            .getPathAsString();
        espaceRechercheService.removeModeleFromDerniersResultatsConsultes(session, userworkspacePath, modeleToRemove);
        espaceRechercheService.removeModeleFromFavorisConsultation(session, userworkspacePath, modeleToRemove);
    }
}
