package fr.dila.solonepg.core.operation.distribution;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.WsSpeService;
import fr.dila.solonepg.core.filter.RouteStepFilter;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.st.api.constant.STOperationConstant;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.api.service.JetonService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.work.SolonWork;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteStep;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.constant.FeuilleRouteConstant;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.core.work.api.WorkManager;

/**
 * Opération appellée à la fin de la chaine de création de CaseLink.
 *
 * @author bgamard
 * @author arolin
 */
@Operation(
    id = CaseLinkCreatedOperation.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "Case Link created",
    description = "Fire event when CaseLink is created."
)
public class CaseLinkCreatedOperation {
    /**
     * Identifiant technique de l'opération.
     */
    public static final String ID = "SolonEpg.CaseLink.Created";

    private static final Log LOGGER = LogFactory.getLog(CaseLinkCreatedOperation.class);

    @Context
    protected OperationContext context;

    @OperationMethod
    public void caseLinkCreated() {
        CoreSession session = context.getCoreSession();
        SSRouteStep routeStep = getSSRouteStepFromContext();

        @SuppressWarnings("unchecked")
        List<DocumentModel> caseDocList = (List<DocumentModel>) context.get(STOperationConstant.OPERATION_CASES_KEY);

        if (CollectionUtils.isNotEmpty(caseDocList)) {
            DocumentModel dossierDoc = caseDocList.get(0);

            fireUserNotificationEvent(session, routeStep, dossierDoc);

            String typeEtape = routeStep.getType();
            Dossier dossier = dossierDoc.getAdapter(Dossier.class);

            if (VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE.equals(typeEtape)) {
                handleEtapeAvisCE(session, routeStep, dossierDoc, dossier);
            } else if (isEtapePublicationDila(typeEtape)) {
                handleEtapePublicationDila(session, routeStep, dossierDoc, typeEtape, dossier);
            }
        }
    }

    private void fireUserNotificationEvent(CoreSession session, SSRouteStep routeStep, DocumentModel dossierDoc) {
        EventProducer eventProducer = STServiceLocator.getEventProducer();

        Map<String, Serializable> eventProperties = getUserNotificationEventProperties(routeStep, dossierDoc);

        InlineEventContext inlineEventContext = new InlineEventContext(
            session,
            session.getPrincipal(),
            eventProperties
        );
        eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgEventConstant.AFTER_CASE_LINK_CREATED_EVENT));
    }

    private Map<String, Serializable> getUserNotificationEventProperties(
        SSRouteStep routeStep,
        DocumentModel dossierDoc
    ) {
        Map<String, Serializable> eventProperties = new HashMap<>();
        eventProperties.put(SolonEpgEventConstant.AFTER_CASE_LINK_CREATED_DOSSIER_DOCID_PARAM, dossierDoc.getId());
        eventProperties.put(
            SolonEpgEventConstant.AFTER_CASE_LINK_CREATED_ROUTE_STEP_DOCID_PARAM,
            routeStep.getDocument().getId()
        );
        return eventProperties;
    }

    private SSRouteStep getSSRouteStepFromContext() {
        FeuilleRouteStep step = (FeuilleRouteStep) context.get(FeuilleRouteConstant.OPERATION_STEP_DOCUMENT_KEY);
        return step.getDocument().getAdapter(SSRouteStep.class);
    }

    private boolean isEtapePublicationDila(String typeEtape) {
        return (
            VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE.equals(typeEtape) ||
            VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(typeEtape) ||
            VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO.equals(typeEtape)
        );
    }

    private void handleEtapePublicationDila(
        CoreSession session,
        SSRouteStep routeStep,
        DocumentModel dossierDoc,
        String typeEtape,
        Dossier dossier
    ) {
        LOGGER.info(
            "Début de validation d'une étape concernant la publication à la DILA, type de l'étape " + typeEtape
        );

        boolean isInjectionInProgress = isInjectionInProgress();
        boolean isDossierIssuInjection = dossier.getIsDossierIssuInjection();

        if (isInjectionInProgress && isDossierIssuInjection) {
            LOGGER.info(
                "Une injection est en cours, le dossier est issu d'une injection, rien n'est envoyé au service de publication de la DILA "
            );
        } else {
            doActionPublicationDila(
                session,
                routeStep,
                dossierDoc,
                typeEtape,
                dossier,
                isInjectionInProgress,
                isDossierIssuInjection
            );
        }
        LOGGER.info("Fin de validation d'une étape concernant la publication à la DILA, type de l'étape " + typeEtape);
    }

    private void doActionPublicationDila(
        CoreSession session,
        SSRouteStep routeStep,
        DocumentModel dossierDoc,
        String typeEtape,
        Dossier dossier,
        boolean isInjectionInProgress,
        boolean isDossierIssuInjection
    ) {
        LOGGER.info(
            "On effectue une action dans le cadre de la publication à la DILA car injectionEnCours = " +
            isInjectionInProgress +
            ", dossierIssuInjection = " +
            isDossierIssuInjection
        );

        setFlagAfterDemandePublication(session, dossier);

        RetourDila retourDila = dossierDoc.getAdapter(RetourDila.class);

        WsSpeService wsSpeService = SolonEpgServiceLocator.getWsSpeService();
        if (retourDila.getIsPublicationEpreuvageDemandeSuivante()) {
            handleStepIsNotPremiereDemandePublication(session, routeStep, typeEtape, dossier, wsSpeService);
        } else {
            scheduleWorkForPublicationDila(routeStep, typeEtape, dossier);
        }
        LOGGER.info("Fin d'action pour la publication DILA");
    }

    private void scheduleWorkForPublicationDila(SSRouteStep routeStep, String typeEtape, Dossier dossier) {
        LOGGER.info("Création d'un work de publication DILA");

        WorkManager workManager = ServiceUtil.getRequiredService(WorkManager.class);
        workManager.schedule(
            new PublishToDilaWork(routeStep.getDocument().getId(), typeEtape, dossier.getDocument().getId()),
            true
        );
    }

    private void handleStepIsNotPremiereDemandePublication(
        CoreSession session,
        SSRouteStep routeStep,
        String typeEtape,
        Dossier dossier,
        WsSpeService wsSpeService
    ) {
        LOGGER.info("Epreuvage : Pas d'appel au web service de la DILA, envoie d'un mail");
        wsSpeService.envoiDemandePublicationSuivante(dossier, session, typeEtape, routeStep);
    }

    private void setFlagAfterDemandePublication(CoreSession session, Dossier dossier) {
        LOGGER.info("Mise en place du flag de passage dans l'envoi de premiereDemandePublicationPourDila");

        dossier.setIsAfterDemandePublication(true);
        session.saveDocument(dossier.getDocument());
        session.save();
    }

    private boolean isInjectionInProgress() {
        ConfigService configService = STServiceLocator.getConfigService();
        return configService.getBooleanValue(SolonEpgConfigConstant.MODE_INJECTION_IN_PROGRESS);
    }

    private void handleEtapeAvisCE(
        CoreSession session,
        SSRouteStep routeStep,
        DocumentModel dossierDoc,
        Dossier dossier
    ) {
        if (!isEtapePrecedentePourAvisCE(session, routeStep)) {
            final JetonService jetonService = STServiceLocator.getJetonService();
            jetonService.addDocumentInBasket(
                session,
                STWebserviceConstant.CHERCHER_DOSSIER,
                TableReference.MINISTERE_CE,
                dossierDoc,
                dossier.getNumeroNor(),
                null,
                null
            );
        }
    }

    private boolean isEtapePrecedentePourAvisCE(CoreSession session, SSRouteStep routeStep) {
        final DocumentModel previousStep = SolonEpgServiceLocator
            .getEPGFeuilleRouteService()
            .findPreviousStepInFolder(session, routeStep.getDocument(), new RouteStepFilter(), false);
        if (previousStep != null) {
            final SSRouteStep previousStepDoc = previousStep.getAdapter(SSRouteStep.class);
            return previousStepDoc.getType().equals(VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE);
        }
        return false;
    }

    private static class PublishToDilaWork extends SolonWork {
        private static final long serialVersionUID = 5174810807038480999L;

        private static final STLogger WORK_LOG = STLogFactory.getLog(PublishToDilaWork.class);

        private final String routeStepId;
        private final String typeEtape;
        private final String dossierId;

        private PublishToDilaWork(String routeStepId, String typeEtape, String dossierId) {
            this.routeStepId = routeStepId;
            this.typeEtape = typeEtape;
            this.dossierId = dossierId;
        }

        @Override
        public String getCategory() {
            return "PublicationJO";
        }

        @Override
        public String getTitle() {
            return "Publication DILA JO";
        }

        @Override
        public void doWork() {
            openSystemSession();

            WORK_LOG.info(STLogEnumImpl.DEFAULT, "Envoi d'une requête au service de publication DILA");

            WsSpeService wsSpeService = SolonEpgServiceLocator.getWsSpeService();
            wsSpeService.envoiPremiereDemandePublicationPourDila(
                QueryHelper.getDocument(session, dossierId).getAdapter(Dossier.class),
                STWebserviceConstant.ENVOYER_PREMIERE_DEMANDE_PE,
                session,
                typeEtape,
                QueryHelper.getDocument(session, routeStepId).getAdapter(SSRouteStep.class)
            );
        }
    }
}
