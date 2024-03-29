package fr.dila.solonepg.core.service;

import com.google.common.collect.ImmutableMap;
import fr.dila.cm.caselink.ActionableCaseLink;
import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSFeuilleRouteServiceImpl;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.caselink.STDossierLink;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STDossierLinkConstant;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.service.ProfileService;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.CoreSessionUtil;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import fr.sword.naiad.nuxeo.commons.core.schema.DublincorePropertyUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

/**
 * Implémentation du service permettant d'effectuer des actions spécifiques sur les instances de feuille de route dans
 * SOLON EPG.
 *
 * @author jtremeaux
 */
public class EPGFeuilleRouteServiceImpl extends SSFeuilleRouteServiceImpl implements EPGFeuilleRouteService {
    /**
     * UID.
     */
    private static final long serialVersionUID = -2392698015083550568L;

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog(EPGFeuilleRouteServiceImpl.class);

    public static final Set<String> INCOMPATIBLE_STEPS_ACTE_TXT_NON_PUB = Collections.unmodifiableSet(
        initIncompatibleSteps()
    );

    private static Set<String> initIncompatibleSteps() {
        Set<String> incompatibleSteps = new HashSet<>();
        incompatibleSteps.add(VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE);
        incompatibleSteps.add(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO);
        incompatibleSteps.add(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO);
        return incompatibleSteps;
    }

    /**
     * Default constructor
     */
    public EPGFeuilleRouteServiceImpl() {
        super();
    }

    @Override
    public void initEtapePourInitialisation(final CoreSession session, final FeuilleRoute route, final String posteId) {
        // Recherche l'étape "pour initialisation" de la feuille de route
        final DocumentModel routeStepDoc = getEtapePourInitialisation(session, route);

        // Recherche la mailbox correspondant au poste
        final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
        final Mailbox mailbox = mailboxPosteService.getOrCreateMailboxPoste(session, posteId);
        if (mailbox == null) {
            throw new NuxeoException("Mailbox poste non trouvée");
        }

        // Affecte le poste à l'étape
        final SSRouteStep routeStep = routeStepDoc.getAdapter(SSRouteStep.class);
        routeStep.setDistributionMailboxId(mailbox.getId());
        session.saveDocument(routeStepDoc);
        session.save();
    }

    @Override
    public DocumentModel getEtapePourInitialisation(final CoreSession session, final FeuilleRoute route) {
        final DocumentModel routeDoc = route.getDocument();

        final DocumentModelList list = session.getChildren(routeDoc.getRef());
        if (list == null || list.isEmpty()) {
            throw new NuxeoException(
                "Aucune étape 'Pour initialisation' trouvée pour la feuille de route <" + routeDoc.getId() + ">"
            );
        }
        return list.get(0);
    }

    /**
     * Retourne vrai si la feuille de route est vérouillée par l'utilisateur en cours.
     *
     * @param routeModel
     *            Feuille de route
     * @param session
     *            Session
     * @return Vrai si la feuille de route est vérouillée par l'utilisateur en cours
     */
    protected boolean isLockedByCurrentUser(final FeuilleRoute routeModel, final CoreSession session) {
        final FeuilleRoute feuilleRoute = routeModel.getDocument().getAdapter(FeuilleRoute.class);

        return LockUtils.isLockedByCurrentUser(session, feuilleRoute.getDocument().getRef());
    }

    @Override
    public String getTypeEtapeImpression() {
        return VocabularyConstants.ROUTING_TASK_TYPE_IMPRESSION;
    }

    @Override
    public String getTypeEtapeAttribution() {
        return VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION;
    }

    @Override
    public String getTypeEtapeInformation() {
        return VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION;
    }

    @Override
    public boolean canDistributeStep(
        final CoreSession session,
        final SSRouteStep routeStep,
        final List<DocumentModel> docs
    ) {
        return !VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION.equals(routeStep.getType());
    }

    @Override
    public void doValidationAutomatiqueOperation(
        final CoreSession session,
        final SSRouteStep routeStep,
        final List<DocumentModel> dossierDocList
    ) {
        if (getTypeEtapeInformation().equals(routeStep.getType())) {
            routeStep.setValidationStatus(
                SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_VALIDE_AUTOMATIQUEMENT_VALUE
            );
            routeStep.setDateFinEtape(Calendar.getInstance());

            updateRouteStepFieldAfterValidation(session, routeStep, dossierDocList, null);

            // On envoie un évenement qui va envoyer un mail de notification aux utilisateurs
            for (final DocumentModel documentModel : dossierDocList) {
                final Dossier dossier = documentModel.getAdapter(Dossier.class);
                final EventProducer eventProducer = STServiceLocator.getEventProducer();
                final Map<String, Serializable> eventProperties = new HashMap<>();
                final String dossierId = documentModel.getId();
                String nor = dossier.getNumeroNor();
                if (nor == null) {
                    nor = "";
                }
                String titreActe = dossier.getTitreActe();
                if (titreActe == null) {
                    titreActe = "";
                }

                // on met le titre de l'acte et le nor du dossier et l'étape de feuille de route en paramètre
                eventProperties.put(
                    SolonEpgEventConstant.AFTER_VALIDATION_POUR_INFORMATION_DOSSIER_ID_PARAM,
                    dossierId
                );
                eventProperties.put(
                    SolonEpgEventConstant.AFTER_VALIDATION_POUR_INFORMATION_DOSSIER_TITRE_ACTE_PARAM,
                    titreActe
                );
                eventProperties.put(SolonEpgEventConstant.AFTER_VALIDATION_POUR_INFORMATION_DOSSIER_NOR_PARAM, nor);
                eventProperties.put(
                    SolonEpgEventConstant.AFTER_VALIDATION_POUR_INFORMATION_ROUTE_STEP_MAILBOX_ID_PARAM,
                    routeStep.getDistributionMailboxId()
                );
                final InlineEventContext inlineEventContext = new InlineEventContext(
                    session,
                    session.getPrincipal(),
                    eventProperties
                );
                eventProducer.fireEvent(
                    inlineEventContext.newEvent(SolonEpgEventConstant.AFTER_VALIDATION_POUR_INFORMATION_EVENT)
                );

                //On journalise la validation automatique de l'étape sans nom d'utilisateur
                CoreSessionUtil.doAction(
                    null,
                    coreSession -> {
                        SSServiceLocator
                            .getSSJournalService()
                            .journaliserActionEtapeFDR(
                                coreSession,
                                routeStep,
                                documentModel,
                                STEventConstant.DOSSIER_AVIS_FAVORABLE,
                                SolonEpgEventConstant.VALIDATION_AUTO_COMMENT
                            );
                    }
                );
            }
        }
    }

    @Override
    public void updateApplicationFieldsAfterValidation(
        final CoreSession session,
        final DocumentModel routeStepDoc,
        final List<DocumentModel> dossierDocList,
        final List<STDossierLink> caseLinkList
    ) {
        final SolonEpgRouteStep routeStep = routeStepDoc.getAdapter(SolonEpgRouteStep.class);

        // Renseigne le numéro de version du dossier au moment de la validation
        if (CollectionUtils.isNotEmpty(dossierDocList)) {
            // dans solon epg, une feuille de route est rattachée à un seul dossier, on prend donc le premier de la
            // liste
            routeStep.setNumeroVersion(dossierDocList.get(0).getAdapter(Dossier.class).getNumeroVersionActeOuExtrait());
        } else if (CollectionUtils.isNotEmpty(caseLinkList)) {
            // si les dossiers ne sont pas chargés, on récupère le dossier à partir de la liste des case links et on le
            // rajoute dans le contexte
            final Dossier dossier = caseLinkList.get(0).getDossier(session, Dossier.class);
            routeStep.setNumeroVersion(dossier.getNumeroVersionActeOuExtrait());
        }
    }

    @Override
    public String getDossierLinkListToEmailForAutomaticValidationQuery() {
        /**
         * Requête récupérant les dossiers link dont l'option validation automatique n'a pas été sélectionné, dont la
         * date d'échéance est dépassé et qui n'a pas déjà été validée.
         */
        final StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE);
        query.append(" WHERE ");
        query.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
        query.append(":");
        query.append(DossierSolonEpgConstants.DELETED);
        query.append(" = 0 AND ");
        query.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
        query.append(":");
        query.append(STSchemaConstant.ACTIONABLE_CASE_LINK_AUTOMATIC_VALIDATION_PROPERTY);
        query.append(" = 0 AND ");
        query.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
        query.append(":");
        query.append(STDossierLinkConstant.DOSSIER_LINK_IS_MAIL_SEND_PROPERTY);
        query.append(" = 0 AND ");
        query.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
        query.append(":");
        query.append(STSchemaConstant.ACTIONABLE_CASE_LINK_DUE_DATE_PROPERTY);
        query.append(" < TIMESTAMP '%s' ");
        query.append(" and ecm:isProxy = 0 ");

        return query.toString();
    }

    @Override
    public Calendar getDateDebutEcheance(final CoreSession session, final STDossier dossier) {
        // la date de début des échéance pour epg est la date de création du dossier.
        return DublincoreSchemaUtils.getCreatedDate(dossier.getDocument());
    }

    @Override
    protected String getDossierLinkListToValidateQuery() {
        final StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE);
        query.append(" WHERE ");
        query.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
        query.append(":");
        query.append(DossierSolonEpgConstants.DELETED);
        query.append(" = 0 AND  ");
        query.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
        query.append(":");
        query.append(STSchemaConstant.ACTIONABLE_CASE_LINK_AUTOMATIC_VALIDATION_PROPERTY);
        query.append(" = 1 AND ");
        query.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
        query.append(":");
        query.append(STSchemaConstant.ACTIONABLE_CASE_LINK_DUE_DATE_PROPERTY);
        query.append(" < TIMESTAMP '%s' ");
        query.append(" and ecm:isProxy = 0 ");

        return query.toString();
    }

    @Override
    public void validerAuromatiquementDossier(final CoreSession session, final List<DocumentModel> dueDossierLinks) {
        final List<DocumentModel> dossierLinkDocEnErreurList = new ArrayList<>();
        DocumentModel dossierDoc = null;
        for (final DocumentModel dossierLinkdoc : dueDossierLinks) {
            LOGGER.info("Debut de validation automatique du Dossier : " + dossierLinkdoc.getTitle());
            try {
                // si le dossierLink est verrouillé, on le déverrouille
                if (dossierLinkdoc.isLocked()) {
                    session.removeLock(dossierLinkdoc.getRef());
                }
                // si le dossier est verrouillé, on le déverrouille
                final STDossierLink dossierLink = dossierLinkdoc.getAdapter(STDossierLink.class);
                dossierDoc = dossierLink.getDossier(session, Dossier.class).getDocument();
                if (dossierDoc.isLocked()) {
                    session.removeLock(dossierDoc.getRef());
                }

                final ActionableCaseLink acl = dossierLinkdoc.getAdapter(ActionableCaseLink.class);
                final DocumentRoutingService documentRoutingService = SSServiceLocator.getDocumentRoutingService();
                // récupère l'étape courante du dossier lié au dossier link
                final DocumentModel etapeDoc = documentRoutingService.getFeuilleRouteStep(session, acl);
                final SSRouteStep ssRouteStep = etapeDoc.getAdapter(SSRouteStep.class);
                // maj des informations de l'étape de feuille de route
                ssRouteStep.setAutomaticValidated(true);
                ssRouteStep.setValidationStatus(
                    SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_VALIDE_AUTOMATIQUEMENT_VALUE
                );
                ssRouteStep.save(session);
                // validation de l'étape
                if (acl.isActionnable() && acl.isTodo()) {
                    acl.validate(session);
                } else {
                    LOGGER.info(
                        "[REPARATION CL] - Case Link présent dans un état autre que 'todo' ; validation esquivée pour éviter 'Unable to follow transition' - caseLinkId : " +
                        acl.getId()
                    );
                }
                session.save();
                LOGGER.info("Dossier validé automatiquement : " + dossierDoc.getTitle());
            } catch (final Exception e) {
                // note : on récupère l'erreur afin que l'échec d'un dossier ne bloque pas les autres
                LOGGER.error("Erreur lors de la validation automatique du DossierLink " + dossierLinkdoc.getId(), e);
                dossierLinkDocEnErreurList.add(dossierDoc);
                LOGGER.error("erreur lors de la validation de l'etape : ", e);
            }
        }

        if (!dossierLinkDocEnErreurList.isEmpty()) {
            final STMailService mailService = STServiceLocator.getSTMailService();
            try {
                String mailContent, mailObject, email;
                List<STUser> users;
                List<String> emailsList;
                final STParametreService paramService = STServiceLocator.getSTParametreService();
                final ProfileService profileService = STServiceLocator.getProfileService();
                Map<String, Object> param;

                users =
                    profileService.getUsersFromBaseFunction(STBaseFunctionConstant.ADMIN_FONCTIONNEL_EMAIL_RECEIVER);
                emailsList = new ArrayList<>();
                for (final STUser user : users) {
                    email = user.getEmail();
                    if (email != null && !email.isEmpty()) {
                        emailsList.add(email);
                    }
                }

                final StringBuilder dossierNorBuilder = new StringBuilder();
                if (!emailsList.isEmpty()) {
                    for (final DocumentModel doc : dossierLinkDocEnErreurList) {
                        final Dossier dossier = doc.getAdapter(Dossier.class);
                        if (dossierNorBuilder.length() != 0) {
                            dossierNorBuilder.append(",");
                        }
                        dossierNorBuilder.append(dossier.getNumeroNor());
                    }
                    // Envoyer Un seul Mail pour tous les dossier en erreur
                    mailContent =
                        paramService.getParametreValue(
                            session,
                            SolonEpgParametreConstant.NOTIFICATION_ERREUR_VALIDATION_AUTOMATIQUE_TEXTE
                        );
                    mailObject =
                        paramService.getParametreValue(
                            session,
                            SolonEpgParametreConstant.NOTIFICATION_ERREUR_VALIDATION_AUTOMATIQUE_OBJET
                        );
                    param = new HashMap<>();
                    param.put("NOR", dossierNorBuilder.toString());
                    mailService.sendTemplateMail(emailsList, mailObject, mailContent, param);
                }
            } catch (final NuxeoException ce) {
                LOGGER.error("Erreur lors de l'envoi de mail d'erreur validation etape.", ce);
            }
        }
    }

    protected Map<String, Object> getMailTemplateParameters(ActionableCaseLink acl) {
        return Optional
            .ofNullable(acl.getDocument())
            .map(doc -> ImmutableMap.of("NOR", (Object) DublincorePropertyUtil.getTitle(doc)))
            .orElseGet(
                () -> {
                    LOGGER.warn("Erreur de récupération du NOR");
                    return ImmutableMap.of();
                }
            );
    }

    @Override
    public DocumentModel getEtapeInitialisationDossier(final CoreSession session, final DocumentModel dossierDoc) {
        final StringBuilder queryBuilder = new StringBuilder(
            "SELECT rs.ecm:uuid as id FROM RouteStep as rs, FeuilleRoute as fdr, Dossier as d WHERE "
        );
        queryBuilder.append(" rs.rtsk:documentRouteId = fdr.ecm:uuid ");
        queryBuilder.append(" AND rs.rtsk:documentRouteId = d.dos:lastDocumentRoute ");
        queryBuilder.append(" AND d.ecm:uuid = ? ");
        queryBuilder.append(" AND rs.rtsk:type  = ? ");

        final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            "RouteStep",
            queryBuilder.toString(),
            new Object[] { dossierDoc.getId(), VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION },
            1,
            0
        );

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public SolonEpgRouteStep getLastStep(final CoreSession session, final DocumentModel dossierDoc) {
        EPGFeuilleRouteService fdrService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
        List<DocumentModel> routeSteps = fdrService.getSteps(session, dossierDoc);

        return routeSteps.get(routeSteps.size() - 1).getAdapter(SolonEpgRouteStep.class);
    }

    @Override
    public boolean isNextStepCompatibleWithActeTxtNonPub(
        final CoreSession session,
        final String feuilleRouteDocId,
        final DocumentModel stepDoc
    ) {
        final List<DocumentModel> nextSteps = findNextSteps(session, feuilleRouteDocId, stepDoc, null);
        for (final DocumentModel nextStepDoc : nextSteps) {
            final SSRouteStep nextStep = nextStepDoc.getAdapter(SSRouteStep.class);
            if (INCOMPATIBLE_STEPS_ACTE_TXT_NON_PUB.contains(nextStep.getType())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getLastChargeMission(final CoreSession session, final DocumentModel dossierDoc) {
        final StringBuilder queryBuilder = new StringBuilder(
            "SELECT rs.ecm:uuid as id FROM RouteStep as rs, FeuilleRoute as fdr, Dossier as d WHERE "
        );
        queryBuilder.append(" rs.rtsk:documentRouteId = fdr.ecm:uuid ");
        queryBuilder.append(" AND rs.rtsk:documentRouteId = d.dos:lastDocumentRoute ");
        queryBuilder.append(" AND d.ecm:uuid = ? ");
        queryBuilder.append(" AND rs.rtsk:type  = ? ");
        queryBuilder.append(" AND rs.rtsk:validationStatus  = ? ");
        queryBuilder.append(" ORDER BY rs.rtsk:dateFinEtape ");

        final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            "RouteStep",
            queryBuilder.toString(),
            new Object[] {
                dossierDoc.getId(),
                VocabularyConstants.ROUTING_TASK_TYPE_AVIS,
                SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE
            },
            0,
            0
        );

        if (list.isEmpty()) {
            return null;
        }
        SSRouteStep rs = list.get(list.size() - 1).getAdapter(SSRouteStep.class);
        return rs.getValidationUserLabel();
    }

    @Override
    public List<String> getEtapesEnCours(CoreSession session, String feuilleRouteId) {
        List<String> etapesEnCours = new ArrayList<>();
        List<DocumentModel> runningSteps = getRunningSteps(session, feuilleRouteId);

        if (CollectionUtils.isNotEmpty(runningSteps)) {
            etapesEnCours = runningSteps.stream().map(this::getLabelForStep).collect(Collectors.toList());
        }
        return etapesEnCours;
    }

    @Override
    protected String getLabelForStep(DocumentModel stepDoc) {
        SSRouteStep step = stepDoc.getAdapter(SSRouteStep.class);
        String posteId = SSServiceLocator
            .getMailboxPosteService()
            .getPosteIdFromMailboxId(step.getDistributionMailboxId());
        PosteNode poste = STServiceLocator.getSTPostesService().getPoste(posteId);
        String labelPoste = poste.getLabel();
        String labelStepType = SolonEpgServiceLocator
            .getSolonEpgVocabularyService()
            .getEntryLabel(STVocabularyConstants.ROUTING_TASK_TYPE_VOCABULARY, step.getType());
        return labelStepType + " - " + labelPoste;
    }

    @Override
    public Collection<DocumentModel> getRouteStepsInSqueletteFdrForPosteId(
        final CoreSession session,
        final String posteId
    ) {
        Objects.requireNonNull(posteId, "L'identifiant du poste est null");
        final String mailboxId = SSServiceLocator.getMailboxPosteService().getPosteMailboxId(posteId);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
            .append("select r.ecm:uuid as id from RouteStepSquelette AS r ")
            .append("WHERE r.rtsk:distributionMailboxId = ? ")
            .append("AND r.ecm:currentLifeCycleState in ('validated', 'draft')");

        final String query = stringBuilder.toString();
        final Object[] params = new Object[] { mailboxId };

        final DocumentRef[] refs = QueryUtils.doUFNXQLQueryForIds(session, query, params);
        return session.getDocuments(refs);
    }
}
