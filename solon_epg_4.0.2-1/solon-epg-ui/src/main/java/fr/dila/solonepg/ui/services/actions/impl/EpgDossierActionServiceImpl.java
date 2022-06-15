package fr.dila.solonepg.ui.services.actions.impl;

import static java.util.Optional.ofNullable;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.DossierBordereauService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.actions.EpgDossierActionService;
import fr.dila.ss.api.criteria.SubstitutionCriteria;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.api.service.SSFeuilleRouteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.actions.SSActionsServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.dila.st.ui.services.actions.impl.STLockActionServiceImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteElement;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public class EpgDossierActionServiceImpl implements EpgDossierActionService {
    public static final STLogger LOGGER = STLogFactory.getLog(EpgDossierActionServiceImpl.class);

    /**
     * Marque le dossier comme urgent.
     *
     * @param context
     */
    @Override
    public void ajouterUrgenceDossier(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (LockUtils.isLockedByCurrentUser(session, dossierDoc.getRef())) {
            final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            dossier.setIsUrgent(true);
            dossier.save(session);
            context.getMessageQueue().addToastSuccess(ResourceHelper.getString("dossier.ajouter.urgence.success"));
        } else {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(STLockActionServiceImpl.LOCK_LOST_ERROR_MSG));
        }
    }

    /**
     * Marque le dossier comme non urgent.
     *
     * @param context
     */
    @Override
    public void supprimerUrgenceDossier(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (LockUtils.isLockedByCurrentUser(session, dossierDoc.getRef())) {
            final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            dossier.setIsUrgent(false);
            dossier.save(session);
            context.getMessageQueue().addToastSuccess(ResourceHelper.getString("dossier.supprimer.urgence.success"));
        } else {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(STLockActionServiceImpl.LOCK_LOST_ERROR_MSG));
        }
    }

    /**
     * Retire le caractère mesure nominative du dossier courant.
     *
     * @param context
     */
    @Override
    public void annulerMesureNominative(SpecificContext context) {
        CoreSession session = context.getSession();
        final DocumentModel dossierDoc = context.getCurrentDocument();

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (LockUtils.isLockedByCurrentUser(session, dossierDoc.getRef())) {
            // Retire le caractère "mesure nominative"
            final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
            dossierService.annulerMesureNominativeUnrestricted(session, dossierDoc);

            // Affiche un message de confirmation
            context
                .getMessageQueue()
                .addSuccessToQueue(ResourceHelper.getString("epg.dossier.command.annulerMesureNominative.success"));
        } else {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(STLockActionServiceImpl.LOCK_LOST_ERROR_MSG));
        }
    }

    /**
     * Ajouter le caractère mesure nominative du dossier courant.
     *
     * @param context
     */
    @Override
    public void redonnerMesureNominative(SpecificContext context) {
        CoreSession session = context.getSession();
        final DocumentModel dossierDoc = context.getCurrentDocument();

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (LockUtils.isLockedByCurrentUser(session, dossierDoc.getRef())) {
            // Aouter le caractère "mesure nominative"
            final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
            dossierService.ajouterMesureNominativeUnrestricted(session, dossierDoc);

            // Affiche un message de confirmation
            context
                .getMessageQueue()
                .addSuccessToQueue(ResourceHelper.getString("epg.dossier.command.redonnerMesureNominative.success"));
        } else {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(STLockActionServiceImpl.LOCK_LOST_ERROR_MSG));
        }
    }

    public boolean hasTypeActeMesureNominative(SpecificContext context) {
        final DocumentModel dossierDoc = context.getCurrentDocument();
        if (dossierDoc != null) {
            final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            final ActeService acteService = SolonEpgServiceLocator.getActeService();
            return acteService.hasTypeActeMesureNominative(dossier.getTypeActe());
        }
        return false;
    }

    public void publierDossierStub(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();

        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        if (!SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(dossier.getTypeActe())) {
            // Vérifie si le document est verrouillé par l'utilisateur en cours
            if (LockUtils.isLockedByCurrentUser(session, dossierDoc.getRef())) {
                final DossierBordereauService dossierBordereauService = SolonEpgServiceLocator.getDossierBordereauService();
                dossierBordereauService.publierDossierStub(dossierDoc, session);
            } else {
                context
                    .getMessageQueue()
                    .addErrorToQueue(ResourceHelper.getString(STLockActionServiceImpl.LOCK_LOST_ERROR_MSG));
            }
        } else {
            // Affiche un message de non possibilité d'action
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("feedback.solonepg.action.impossible.publier"));
        }
    }

    /**
     * Détermine si le document du parapheur ou du fond de dossier est
     * supprimable.
     *
     * @param nomCreateur
     *            nom du createur du document
     * @param isFichierObligatoire
     *            vrai si le fichier est obligatoire dans le parapheur
     * @return condition
     * @throws ClientException
     */
    public boolean isDocParapheurDeletable(SpecificContext context) {
        CoreSession session = context.getSession();
        NuxeoPrincipal principal = session.getPrincipal();
        final String nomCreateur = context.getFromContextData("nomCreateur");
        final Boolean isFichierObligatoire = context.getFromContextData("isFichierObligatoire");

        // Vérifie que l'utilisateur possède la fonction unitaire ou est le
        // createur du fichier
        if (
            principal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_PARAPHEUR_OR_FOND_DOSSIER_DELETER) &&
            !principal.getName().equals(nomCreateur)
        ) {
            // La suppression est possible tant que le dossier n'est pas au
            // statut "NOR attribué" ou "Publié".
            final DocumentModel dossierDoc = context.getCurrentDocument();
            final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            final String statutDossier = dossier.getStatut();
            if (
                statutDossier == null ||
                statutDossier.equals(VocabularyConstants.STATUT_PUBLIE) ||
                statutDossier.equals(VocabularyConstants.STATUT_NOR_ATTRIBUE)
            ) {
                return false;
            }

            // si le dossier est à l'état lancé et est un fichier obligatoire
            // dans le parapheur, on ne peut pas le supprimer
            return !isFichierObligatoire;
        }
        return false;
    }

    /**
     * Détermine si le document du fond de dossier est supprimable.
     *
     * @param nomCreateur
     *            nom du createur du document
     * @return condition
     * @throws ClientException
     */
    public boolean isDocFondDossierDeletable(SpecificContext context) {
        return isDocParapheurDeletable(context);
    }

    @Override
    public void terminerDossierSansPublication(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();

        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        if (SolonEpgServiceLocator.getActeService().isActeTexteNonPublie(dossier.getTypeActe())) {
            // Affiche un message de non possibilité d'action
            context
                .getMessageQueue()
                .addErrorToQueue(
                    ResourceHelper.getString(
                        "dossier.terminer.sans.publication.error.type.acte",
                        dossier.getNumeroNor()
                    )
                );
        }

        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (LockUtils.isLockedByCurrentUser(session, dossierDoc.getRef())) {
            final DossierBordereauService dossierBordereauService = SolonEpgServiceLocator.getDossierBordereauService();
            dossierBordereauService.terminerDossierSansPublication(dossierDoc, session);
            context
                .getMessageQueue()
                .addToastSuccess(ResourceHelper.getString("dossier.terminer.sans.publication.success"));
            STActionsServiceLocator.getDossierLockActionService().unlockCurrentDossier(context);
        } else {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString(STLockActionServiceImpl.LOCK_LOST_ERROR_MSG));
        }
    }

    @Override
    public boolean canExecuteStep(SpecificContext context) {
        SSPrincipal ssPrincipal = (SSPrincipal) context.getSession().getPrincipal();
        DossierLink dossierLink = (DossierLink) SSActionsServiceLocator
            .getSSCorbeilleActionService()
            .getCurrentDossierLink(context);
        if (dossierLink != null) {
            return SolonEpgServiceLocator
                .getDocumentRoutingService()
                .isRoutingTaskTypeValiderAllowed(ssPrincipal, dossierLink);
        } else {
            return false;
        }
    }

    @Override
    public void addToDossiersSuivis(SpecificContext context) {
        switch (
            SolonEpgServiceLocator
                .getDossierSignaleService()
                .verserDossier(context.getSession(), context.getCurrentDocument())
        ) {
            case 0:
                context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("ajout.dossier.suivis.error"));
                break;
            case -1:
                context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("ajout.dossier.suivi.max.atteint"));
                break;
            default:
                context.getMessageQueue().addToastSuccess(ResourceHelper.getString("ajout.dossier.suivis.success"));
                break;
        }
    }

    @Override
    public void createListeSignatureDossiers(SpecificContext context) {
        List<String> dossierIds = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
        List<DocumentModel> docs = context.getSession().getDocuments(dossierIds, null);
        EpgUIServiceLocator.getEpgSelectionToolUIService().createListeSignatureIfAllow(context, docs, false);
    }

    @Override
    public boolean checkShowModalSubstitutionPeriodicite(SpecificContext context) {
        DocumentModel doc = context.getCurrentDocument();
        String periodicite = context.getFromContextData(EpgContextDataKey.PERIODICITE_VALUE);

        return (
            VocabularyConstants.STATUT_INITIE.equals(doc.getAdapter(Dossier.class).getStatut()) &&
            VocabularyConstants.PERIODICITE_RAPPORT_APPLICATION_LOI_VALUE.equals(periodicite)
        );
    }

    @Override
    public List<SelectValueDTO> loadModeleFdrSubstitutionRapport(SpecificContext context) {
        Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
        SSPrincipal principal = (SSPrincipal) context.getSession().getPrincipal();
        SubstitutionCriteria criteria = new SubstitutionCriteria(
            new ArrayList<>(principal.getMinistereIdSet()),
            dossier.getTypeActe()
        );
        context.putInContextData(SSContextDataKey.SUBSTITUTION_CRITERIA, criteria);
        ModeleFDRList modeleList = EpgUIServiceLocator
            .getEpgModeleFdrListUIService()
            .getModelesFDRSubstitution(context);
        return modeleList
            .getListe()
            .stream()
            .map(modele -> new SelectValueDTO(modele.getId(), modele.getIntitule()))
            .collect(Collectors.toList());
    }

    @Override
    public boolean isFeuilleRouteRestartable(SpecificContext context) {
        DocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
        CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();
        FeuilleRoute route = documentRoutingService.getDocumentRouteForDossier(session, dossierDoc.getId());
        boolean isRestartable = false;

        if (route != null && route.isDone()) {
            SSFeuilleRouteService routeService = SSServiceLocator.getSSFeuilleRouteService();
            List<DocumentModel> stepsDocs = routeService.getSteps(session, dossierDoc);
            FeuilleRouteElement lastStep = CollectionUtils.isNotEmpty(stepsDocs)
                ? stepsDocs.get(stepsDocs.size() - 1).getAdapter(FeuilleRouteElement.class)
                : null;

            isRestartable = !hasRunningStep(stepsDocs) && ofNullable(lastStep).map(ls -> !ls.isDone()).orElse(false);
        }

        return isRestartable;
    }

    private static boolean hasRunningStep(List<DocumentModel> stepsDoc) {
        return stepsDoc
            .stream()
            .map(stepDoc -> stepDoc.getAdapter(FeuilleRouteElement.class))
            .anyMatch(FeuilleRouteElement::isRunning);
    }
}
