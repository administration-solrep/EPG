package fr.dila.solonepg.core.service;

import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getCorbeilleService;
import static fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl.UPDATE_STEP_TEC;
import static fr.dila.st.api.constant.STSchemaConstant.DOSSIER_LAST_DOCUMENT_ROUTE_XPATH;
import static fr.dila.st.core.query.QueryHelper.toPrefetchInfo;
import static fr.dila.st.core.service.STServiceLocator.getSTPostesService;
import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableSet;
import fr.dila.cm.caselink.ActionableCaseLink;
import fr.dila.cm.security.CaseManagementSecurityConstants;
import fr.dila.solonepg.api.administration.BulletinOfficiel;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgAclConstant;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgBulletinOfficielConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.dto.VecteurPublicationDTO;
import fr.dila.solonepg.api.enumeration.EpgVecteurPublication;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.parapheur.Parapheur;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.DossierBordereauService;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.api.service.EpgDocumentRoutingService;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.dto.activitenormative.VecteurPublicationDTOImpl;
import fr.dila.solonepg.core.filter.RouteStepValideFilter;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.ss.api.domain.feuilleroute.StepFolder;
import fr.dila.ss.api.exception.SSException;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.api.service.SSFeuilleRouteService;
import fr.dila.ss.api.service.SSJournalService;
import fr.dila.ss.core.service.DossierDistributionServiceImpl;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.caselink.STDossierLink;
import fr.dila.st.api.constant.STAclConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STDossierLinkConstant;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.constant.STLifeCycleConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STLockService;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.UnrestrictedCreateOrSaveDocumentRunner;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteStep;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.constant.FeuilleRouteConstant;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.constant.FeuilleRouteExecutionType;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.exception.FeuilleRouteNotLockedException;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.model.RouteTableElement;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

/**
 * Implémentation du service de distribution des dossiers de SOLON EPG.
 *
 * @author jtremeaux
 */
public class EpgDossierDistributionServiceImpl
    extends DossierDistributionServiceImpl
    implements EpgDossierDistributionService {
    private static final long serialVersionUID = -6388450326774418848L;

    private static final STLogger LOGGER = STLogFactory.getLog(EpgDossierDistributionServiceImpl.class);

    private static final Set<String> INCOMPATIBLE_STEPS_ACTE_TXT_NON_PUB = ImmutableSet.of(
        VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE,
        VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO,
        VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO
    );
    private static final String NEXT_STEP_INCOMPATIBLE_ERROR_MESSAGE =
        "L'étape suivante n'est pas compatible avec les données de vecteurs de publication qui ont été renseignées";
    private static final Predicate<VecteurPublicationDTO> IS_VECTEUR_PUBLICATION_BODMR = dto ->
        EpgVecteurPublication.BODMR.getIntitule().equals(dto.getIntitule());

    // On référence les services dans la classe pour pouvoir les mocker pour les tests unitaires
    protected DossierBordereauService bordereauService;
    protected BulletinOfficielService bulletinService;
    protected TableReferenceService tabRefService;
    protected VocabularyService vocService;
    protected ParapheurService parapheurService;
    protected FondDeDossierService fondDeDossierService;
    protected ActeService acteService;
    protected EPGFeuilleRouteService epgFeuilleRouteService;

    /**
     * Requête des textes pour validation automatique de publication
     *
     */
    private static final String QUERY_AUTO_VALIDATION_POUR_PUBLI =
        "SELECT dl.ecm:uuid as id FROM " +
        DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE +
        " as d, " +
        STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE +
        " as dl Where d.ecm:uuid = dl.acslk:caseDocumentId AND d." +
        DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY +
        " = 4 AND dl." +
        DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DELETED +
        " = 0 AND ( dl." +
        STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX +
        ":" +
        STDossierLinkConstant.DOSSIER_LINK_ROUTING_TASK_TYPE_PROPERTY +
        " = " +
        VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO +
        " OR  dl." +
        STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX +
        ":" +
        STDossierLinkConstant.DOSSIER_LINK_ROUTING_TASK_TYPE_PROPERTY +
        " = " +
        VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE +
        " )";

    public EpgDossierDistributionServiceImpl() {
        bordereauService = SolonEpgServiceLocator.getDossierBordereauService();
        bulletinService = SolonEpgServiceLocator.getBulletinOfficielService();
        tabRefService = SolonEpgServiceLocator.getTableReferenceService();
        vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
        parapheurService = SolonEpgServiceLocator.getParapheurService();
        fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
        acteService = SolonEpgServiceLocator.getActeService();
        epgFeuilleRouteService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
    }

    protected EpgDossierDistributionServiceImpl(
        DossierBordereauService bordereau,
        BulletinOfficielService bulletin,
        TableReferenceService tableRef,
        VocabularyService voc,
        ParapheurService para,
        FondDeDossierService fdd,
        ActeService acte,
        EPGFeuilleRouteService feuille
    ) {
        bordereauService = bordereau;
        bulletinService = bulletin;
        tabRefService = tableRef;
        vocService = voc;
        parapheurService = para;
        fondDeDossierService = fdd;
        acteService = acte;
        epgFeuilleRouteService = feuille;
    }

    @Override
    public void initDossierDistributionAcl(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final List<String> mailboxIdList
    ) {
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                final DocumentModel dossierDocToUpdate = dossierDoc;
                final ACP acp = dossierDocToUpdate.getACP();
                ACL mailboxACL = acp.getOrCreateACL(CaseManagementSecurityConstants.ACL_MAILBOX_PREFIX);
                final Set<ACE> aceSet = new HashSet<>(Arrays.asList(mailboxACL.getACEs()));
                aceSet.addAll(getDossierDistributionAce(mailboxIdList));
                acp.removeACL(CaseManagementSecurityConstants.ACL_MAILBOX_PREFIX);
                mailboxACL = acp.getOrCreateACL(CaseManagementSecurityConstants.ACL_MAILBOX_PREFIX);
                mailboxACL.addAll(aceSet);
                acp.addACL(mailboxACL);
                session.setACP(dossierDocToUpdate.getRef(), acp, true);
            }
        }
        .runUnrestricted();
    }

    /**
     * Construit la liste des ACE à ajouter lors de la distribution d'un dossier.
     *
     * @param mailboxIdList
     *            Liste des mailbox de distribution
     * @return Liste des ACE à ajouter
     */
    protected List<ACE> getDossierDistributionAce(final List<String> mailboxIdList) {
        final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
        final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
        final STUsAndDirectionService usService = STServiceLocator.getSTUsAndDirectionService();

        final List<ACE> aceList = new ArrayList<>();
        for (final String mailboxId : mailboxIdList) {
            // Ajoute la mailbox poste destinataire de la distribution
            aceList.add(
                new ACE(CaseManagementSecurityConstants.MAILBOX_PREFIX + mailboxId, SecurityConstants.READ_WRITE, true)
            );

            // Ajoute les administrateurs des ministères destinataires de la distribution
            final String posteId = mailboxPosteService.getPosteIdFromMailboxId(mailboxId);
            final List<EntiteNode> ministereList = ministeresService.getMinistereParentFromPoste(posteId);
            for (final EntiteNode ministere : ministereList) {
                final String ministereId = ministere.getId();
                final String group = SolonEpgAclConstant.DOSSIER_DIST_MIN_ACE_PREFIX + ministereId;
                aceList.add(new ACE(group, SecurityConstants.READ_WRITE, true));
            }

            // Ajoute les administrateurs des directions destinataires de la distribution
            final List<OrganigrammeNode> uniteStructurelleList = usService.getDirectionFromPoste(posteId);
            for (final OrganigrammeNode direction : uniteStructurelleList) {
                final String directionId = direction.getId();
                final String group = SolonEpgAclConstant.DOSSIER_DIST_DIR_ACE_PREFIX + directionId;
                aceList.add(new ACE(group, SecurityConstants.READ_WRITE, true));
            }
        }
        return aceList;
    }

    @Override
    public FeuilleRoute startDefaultRoute(final CoreSession session, final Dossier dossier, final String posteId) {
        final DocumentModel dossierDoc = dossier.getDocument();

        LOGGER.debug(
            session,
            SSLogEnumImpl.START_FDR_TEC,
            "Démarrage de la feuille de route par défaut pour le dossier <" + dossierDoc.getRef() + ">"
        );

        // Récupère le modèle par défaut pour les données du dossier
        final FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();

        final FeuilleRoute routeModel = feuilleRouteModelService.getDefaultRoute(
            session,
            dossier.getTypeActe(),
            dossier.getMinistereResp(),
            dossier.getDirectionResp()
        );
        if (routeModel == null) {
            throw new NuxeoException("Aucune feuille de route par défaut trouvée");
        }

        // Instancie la feuille de route
        final List<String> docIds = new ArrayList<>();
        docIds.add(dossierDoc.getId());
        final EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
        final DocumentModel routeInstanceDoc = documentRoutingService.createNewInstance(
            session,
            routeModel.getDocument(),
            docIds
        );
        final SSFeuilleRoute stRouteInstance = routeInstanceDoc.getAdapter(SSFeuilleRoute.class);
        stRouteInstance.setTypeCreation(STVocabularyConstants.FEUILLE_ROUTE_TYPE_CREATION_INSTANCIATION);
        stRouteInstance.setTitle(dossier.getNumeroNor() + "_" + stRouteInstance.getTitle());
        session.save();

        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                if (stRouteInstance.getDocument().getCoreSession() == null) {
                    stRouteInstance.getDocument().detach(false);
                    stRouteInstance.getDocument().attach(session.getSessionId());
                }
                // Affecte le poste à l'étape "pour initialisation de la feuille de route"
                epgFeuilleRouteService.initEtapePourInitialisation(session, stRouteInstance, posteId);

                // Démarre la feuille de route
                stRouteInstance.run(session);
            }
        }
        .runUnrestricted();

        // Renseigne la dernière feuille de route du dossier exécutée sur le dossier
        dossier.setLastDocumentRoute(routeInstanceDoc.getId());
        dossier.save(session);

        // calcul des échéances de feuille de route
        epgFeuilleRouteService.calculEcheanceFeuilleRoute(session, dossier);

        return stRouteInstance;
    }

    @Override
    public void lancerDossier(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final DocumentModel dossierLinkDoc
    ) {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        final STDossierLink dossierLink = dossierLinkDoc.getAdapter(STDossierLink.class);
        final DocumentModel etapeDoc = session.getDocument(new IdRef(dossierLink.getRoutingTaskId()));

        // check next step compatible with type acte
        checkNextStep(session, dossier, etapeDoc);

        // ABI fev 423
        createEpreuvesParapheur(session, dossier, etapeDoc);

        // Passage du dossier à l'état lancé
        dossierDoc.followTransition(Dossier.DossierTransition.toRunning.toString());

        // Changement du statut du dossier
        dossier.setStatut(VocabularyConstants.STATUT_LANCE);

        // évenement de passage à l'état lancé
        final EventProducer eventProducer = STServiceLocator.getEventProducer();
        final Map<String, Serializable> eventProperties = new HashMap<>();
        eventProperties.put(SolonEpgEventConstant.DOSSIER_EVENT_PARAM, dossier);
        final InlineEventContext inlineEventContext = new InlineEventContext(
            session,
            session.getPrincipal(),
            eventProperties
        );
        eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgEventConstant.DOSSIER_STATUT_CHANGED));

        session.saveDocument(dossierDoc);

        // Met à jour l'étape en cours de la feuille de route
        final String routeStepId = dossierLink.getRoutingTaskId();
        final DocumentModel routeStepDoc = session.getDocument(new IdRef(routeStepId));
        final SolonEpgRouteStep routeStep = routeStepDoc.getAdapter(SolonEpgRouteStep.class);
        routeStep.setAutomaticValidated(false);
        routeStep.setValidationStatus(SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE);
        session.saveDocument(routeStep.getDocument());

        // Valide le CaseLink
        validateDossierLink(session, dossierLinkDoc, dossierDoc);
    }

    @Override
    public void norAttribue(final CoreSession session, final DocumentModel dossierDoc) {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        if (acteService.isActeTexteNonPublie(dossier.getTypeActe())) {
            LOGGER.warn(session, EpgLogEnumImpl.FAIL_UPDATE_STATUT_DOSSIER_TEC, dossierDoc);
            return;
        }

        terminateFeuilleRoute(session, dossierDoc);
        // Changement du statut du dossier
        dossier.setStatut(VocabularyConstants.STATUT_NOR_ATTRIBUE);
        session.saveDocument(dossierDoc);

        // évenement de passage à l'état NOR attribué
        final EventProducer eventProducer = STServiceLocator.getEventProducer();
        final Map<String, Serializable> eventProperties = new HashMap<>();
        eventProperties.put(SolonEpgEventConstant.DOSSIER_EVENT_PARAM, dossier);
        final InlineEventContext inlineEventContext = new InlineEventContext(
            session,
            session.getPrincipal(),
            eventProperties
        );
        eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgEventConstant.DOSSIER_STATUT_CHANGED));

        // Log dans le journal
        final JournalService journalService = STServiceLocator.getJournalService();
        journalService.journaliserActionBordereau(
            session,
            dossierDoc,
            SolonEpgEventConstant.DOSSIER_STATUT_CHANGED,
            SolonEpgEventConstant.STATUT_DOSSIER_NOR_ATTRIBUE_COMMENT
        );
    }

    @Override
    public boolean processCanceledRoute(final CoreSession session, final DocumentModel doc) {
        boolean isDossier = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE.equals(doc.getType());
        Set<String> idsDossier = new HashSet<>();

        String idRoute;
        DocumentRef routeRef;
        if (isDossier) {
            idRoute = doc.getAdapter(Dossier.class).getLastDocumentRoute();
            routeRef = new IdRef(idRoute);
            if (idRoute == null || !session.exists(routeRef)) {
                return false;
            }
            idsDossier.add(doc.getId());
        } else {
            idRoute = doc.getId();
            routeRef = doc.getRef();
        }

        FeuilleRoute route = session.getDocument(routeRef).getAdapter(FeuilleRoute.class);
        if (!route.isCanceled()) {
            return false;
        }
        idsDossier.addAll(route.getAttachedDocuments());

        if (isDossier) {
            removeDossierLinks(session, idsDossier);
        }

        LOGGER.info(session, UPDATE_STEP_TEC, "Annulation des étapes de la feuille de route annulée : " + idRoute);
        SSServiceLocator
            .getSSFeuilleRouteService()
            .findAllSteps(session, idRoute)
            .stream()
            .filter(d -> SSConstant.ROUTE_STEP_DOCUMENT_TYPE.equals(d.getType()))
            .map(SSRouteStep::adapt)
            .filter(r -> !r.isCanceled())
            .forEach(r -> r.cancel(session));

        return true;
    }

    @Override
    public void removeDossierLinks(CoreSession session, Collection<String> dossierIds) {
        dossierIds.forEach(
            dossierId ->
                getCorbeilleService()
                    .findDossierLinkUnrestricted(session, dossierId)
                    .stream()
                    .map(DossierLink::getAdapter)
                    .forEach(
                        dl -> {
                            dl.setDeleted(true);
                            dl.save(session);
                        }
                    )
        );
    }

    @Override
    public void terminateFeuilleRoute(final CoreSession session, final DocumentModel dossierDoc) {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        CoreInstance.doPrivileged(
            session,
            unrestrictedSession -> {
                // Supprimmer les étape a venir
                SolonEpgServiceLocator.getDossierService().deleteFeuilleRouteStepToCome(unrestrictedSession, dossier);

                // Valider toutes les étapes en cours
                getCorbeilleService()
                    .findDossierLinkUnrestricted(unrestrictedSession, dossierDoc.getId())
                    .forEach(dl -> validerEtape(unrestrictedSession, dossierDoc, dl));
            }
        );

        dossierDoc.detach(false);
        dossierDoc.attach(session.getSessionId());
    }

    @Override
    public void validerEtape(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final DocumentModel dossierLinkDoc
    ) {
        validerEtape(
            session,
            dossierDoc,
            dossierLinkDoc,
            SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE
        );
    }

    @Override
    public void validerEtape(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final DocumentModel dossierLinkDoc,
        final String validationStatus
    ) {
        // Met à jour l'étape en cours avec l'avis favorable
        final STDossierLink dossierLink = dossierLinkDoc.getAdapter(STDossierLink.class);
        final DocumentModel etapeDoc = session.getDocument(new IdRef(dossierLink.getRoutingTaskId()));

        // check next step compatible with type acte
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        checkNextStep(session, dossier, etapeDoc);

        // ABI fev 423
        createEpreuvesParapheur(session, dossier, etapeDoc);

        final SSRouteStep etape = etapeDoc.getAdapter(SSRouteStep.class);

        final SSJournalService journalService = SSServiceLocator.getSSJournalService();
        LOGGER.info(
            session,
            SSLogEnumImpl.UPDATE_STEP_TEC,
            "Avis favorable pour l'étape <" + dossierLinkDoc.getName() + ">"
        );

        // Met à jour les champs du dossiers conseillers PM et chargés de mission ( RG-DOS-TRT-23 et RG-DOS-TRT-24 )
        updateChargeMissionAndConseillerPmValidation(session, dossierDoc, etapeDoc);
        updateStepValidationStatus(session, etapeDoc, validationStatus, dossierDoc);
        updateDossierStateRapportParlementaire(session, etapeDoc, validationStatus, dossierDoc);

        // Valide le DossierLink
        validateDossierLink(session, dossierLinkDoc, dossierDoc);

        // Journalise l'action
        journalService.journaliserActionEtapeFDR(
            session,
            etape,
            dossierDoc,
            STEventConstant.DOSSIER_AVIS_FAVORABLE,
            STEventConstant.COMMENT_AVIS_FAVORABLE
        );
    }

    @Override
    public void validerEtapeRefus(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final DocumentModel dossierLinkDoc,
        final String validationStatus
    ) {
        final SSJournalService journalService = SSServiceLocator.getSSJournalService();

        // check next step compatible with type acte
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final STDossierLink dossierLink = dossierLinkDoc.getAdapter(STDossierLink.class);
        final DocumentModel etapeDoc = session.getDocument(new IdRef(dossierLink.getRoutingTaskId()));
        checkNextStep(session, dossier, etapeDoc);

        LOGGER.info(
            session,
            SSLogEnumImpl.UPDATE_STEP_TEC,
            "Avis défavorable pour l'étape <" + dossierLinkDoc.getName() + ">"
        );

        // Met à jour l'étape en cours avec l'avis défavorable
        final SSRouteStep etape = etapeDoc.getAdapter(SSRouteStep.class);
        updateStepValidationStatus(session, etapeDoc, validationStatus, dossierDoc);

        // Invalide le DossierLink
        refuseDossierLink(session, dossierLinkDoc, dossierDoc);

        // Journalise l'action
        journalService.journaliserActionEtapeFDR(
            session,
            etape,
            dossierDoc,
            STEventConstant.DOSSIER_AVIS_DEFAVORABLE,
            STEventConstant.COMMENT_AVIS_DEFAVORABLE
        );
    }

    @Override
    public void avisRectificatif(final CoreSession session, final DocumentModel dossierDoc) {
        final JournalService journalService = STServiceLocator.getJournalService();
        LOGGER.info(session, SSLogEnumImpl.UPDATE_STEP_TEC, "Avis rectificatif");

        // Journalise l'action
        journalService.journaliserActionParapheur(
            session,
            dossierDoc,
            STEventConstant.DOSSIER_AVIS_RECTIFICATIF,
            STEventConstant.COMMENT_AVIS_RECTIFICATIF
        );

        envoyerNotificationAvisRectificatif(session, dossierDoc);
    }

    public void envoyerNotificationAvisRectificatif(final CoreSession session, final DocumentModel dossierDoc) {
        // on crée une liste d'utilisateur pour ne pas envoyer plusieurs mail à la même personne si elle est dans
        // plusieurs postes
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        EPGFeuilleRouteService fdrService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
        SSFeuilleRouteService ssFdrService = SSServiceLocator.getSSFeuilleRouteService();

        List<DocumentModel> stepsDoc = fdrService.getSteps(session, dossierDoc);

        // On récupère les utilisateurs lié au poste ayant initié le dossier
        SSRouteStep firstStep = stepsDoc.get(0).getAdapter(SSRouteStep.class);
        String mailboxId = firstStep.getDistributionMailboxId();
        // récupération de la mailbox
        String posteIdPasse = SSServiceLocator.getMailboxPosteService().getPosteIdFromMailboxId(mailboxId);
        PosteNode posteNode = STServiceLocator.getSTPostesService().getPoste(posteIdPasse);
        Set<STUser> setUserMail = new HashSet<>(posteNode.getUserList());

        //On récupère les utilisateurs "chargé de mission" des étapes passé
        //de la feuille de route du dossier
        List<DocumentModel> allStepsDoc = ssFdrService.findAllSteps(session, dossier.getLastDocumentRoute());
        List<DocumentModel> curentSteps = fdrService.getRunningSteps(session, dossier.getLastDocumentRoute());
        for (DocumentModel stepDoc : allStepsDoc) {
            if (curentSteps.contains(stepDoc)) {
                break;
            }
            SSRouteStep step = stepDoc.getAdapter(SSRouteStep.class);
            if (step != null) {
                mailboxId = step.getDistributionMailboxId();
                // récupération de la mailbox
                posteIdPasse = SSServiceLocator.getMailboxPosteService().getPosteIdFromMailboxId(mailboxId);
                posteNode = STServiceLocator.getSTPostesService().getPoste(posteIdPasse);
                if (posteNode.isChargeMissionSGG()) {
                    setUserMail.addAll(posteNode.getUserList());
                }
            }
        }

        // On récupère les utilisateurs du poste DAN configuré dans la table de référence
        final TableReferenceService tdrService = SolonEpgServiceLocator.getTableReferenceService();
        String posteDanId = tdrService.getPosteDanIdAvisRectificatifCE(session);
        PosteNode posteDanNode = getSTPostesService().getPoste(posteDanId);
        if (posteDanNode != null) {
            setUserMail.addAll(posteDanNode.getUserList());
        }

        // On envoi le mail de notificationà la liste des utilisateurs concernés
        final STMailService mailService = STServiceLocator.getSTMailService();
        final String dossierId = dossier.getDocument().getId();
        final String linkToDossier = mailService.getLinkHtmlToDossiers(session, Collections.singletonList(dossierId));

        final String objetMail = "[SOLON-EPG] Un avis rectificatif a été envoyé par le CE";
        final StringBuilder textMail = new StringBuilder();
        textMail.append("<html><head></head><body>");
        textMail.append("<br/><br/>Bonjour");
        textMail.append("<br/>Un avis rectificatif a été envoyé par le CE.");
        textMail.append("<br/>NOR : ${");
        textMail.append(dossier.getNumeroNor());
        textMail.append("}<br/>Titre de l'acte : ${");
        textMail.append(dossier.getTitreActe());
        textMail.append(linkToDossier);
        textMail.append("</body></html>");
        final String texteMail = textMail.toString();

        try {
            mailService.sendMailNotificationToUserList(new ArrayList<>(setUserMail), objetMail, texteMail);
        } catch (Exception exc) {
            LOGGER.error(STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
        }
    }

    @Override
    public void validerEtapeCorrection(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final DocumentModel dossierLinkDoc
    ) {
        final SSJournalService journalService = SSServiceLocator.getSSJournalService();

        // check next step compatible with type acte
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final STDossierLink dossierLink = dossierLinkDoc.getAdapter(STDossierLink.class);
        final DocumentModel etapeDoc = session.getDocument(new IdRef(dossierLink.getRoutingTaskId()));
        checkNextStep(session, dossier, etapeDoc);

        LOGGER.info(
            session,
            SSLogEnumImpl.UPDATE_STEP_TEC,
            "Avis favorable avec correction pour l'étape <" + dossierLinkDoc.getName() + ">"
        );

        // Met à jour l'étape en cours
        final SSRouteStep etape = etapeDoc.getAdapter(SSRouteStep.class);
        updateStepValidationStatus(
            session,
            etapeDoc,
            SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_CORRECTION_VALUE,
            dossierDoc
        );

        // Met à jour les champs du dossiers conseillers PM et chargés de mission ( RG-DOS-TRT-23 et RG-DOS-TRT-24 )
        updateChargeMissionAndConseillerPmValidation(session, dossierDoc, etapeDoc);

        // Valide le DossierLink (avis favorable)
        validateDossierLink(session, dossierLinkDoc, dossierDoc);

        // Journalise l'action
        journalService.journaliserActionEtapeFDR(
            session,
            etape,
            dossierDoc,
            SolonEpgEventConstant.DOSSIER_VALIDER_AVEC_CORRECTION_EVENT,
            SolonEpgEventConstant.DOSSIER_VALIDER_AVEC_CORRECTION_COMMENT_PARAM
        );
    }

    /**
     * Met à jour les champs du dossiers conseillers PM et chargés de mission ( RG-DOS-TRT-23 et RG-DOS-TRT-24 )
     */
    protected void updateChargeMissionAndConseillerPmValidation(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final DocumentModel etapeDoc
    ) {
        final STUserService stUserService = STServiceLocator.getSTUserService();

        // on récupère le poste de l'étape
        final SSRouteStep routeStep = etapeDoc.getAdapter(SSRouteStep.class);
        final String mailboxId = routeStep.getDistributionMailboxId();
        final String posteId = SSServiceLocator.getMailboxPosteService().getPosteIdFromMailboxId(mailboxId);

        try {
            final PosteNode posteNode = STServiceLocator.getSTPostesService().getPoste(posteId);
            if (posteNode.isChargeMissionSGG() || posteNode.isConseillerPM()) {
                // on ajoute l'utilisateur qui a validé le fichier dans les champs chargés de mission ou conseiller
                // Premier Ministre du Dossier
                final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                final String userId = session.getPrincipal().getName();
                if (posteNode.isChargeMissionSGG()) {
                    List<String> chargeMissionIds = dossier.getNomCompletChargesMissions();
                    chargeMissionIds = addUserToChargeMissionAndConseillerPm(stUserService, chargeMissionIds, userId);
                    dossier.setNomCompletChargesMissions(chargeMissionIds);
                }
                if (posteNode.isConseillerPM()) {
                    List<String> conseillerPmIds = dossier.getNomCompletConseillersPms();
                    conseillerPmIds = addUserToChargeMissionAndConseillerPm(stUserService, conseillerPmIds, userId);
                    dossier.setNomCompletConseillersPms(conseillerPmIds);
                }
                dossier.save(session);
            }
        } catch (final NuxeoException e) {
            LOGGER.error(
                session,
                STLogEnumImpl.FAIL_UPDATE_DOSSIER_TEC,
                "poste de l'étape de feuille de route non trouvée" + posteId,
                e
            );
            return;
        }
    }

    /**
     * Récupère les informations de l'utilisateur et l'ajoute à la liste associée.
     */
    protected List<String> addUserToChargeMissionAndConseillerPm(
        final STUserService stUserService,
        final List<String> usesFullNameList,
        final String userId
    ) {
        List<String> nomCompletConseillersPmsOuChargesMissions = ObjectHelper.requireNonNullElseGet(
            usesFullNameList,
            ArrayList::new
        );
        final String nomCompletUser = stUserService.getUserInfo(userId, STUserService.CIVILITE_ABREGEE_PRENOM_NOM);
        nomCompletConseillersPmsOuChargesMissions.add(nomCompletUser);
        return nomCompletConseillersPmsOuChargesMissions;
    }

    @Override
    public void validerEtapeNonConcerneAjoutEtape(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final DocumentModel dossierLinkDoc
    ) {
        LOGGER.info(
            session,
            SSLogEnumImpl.UPDATE_STEP_TEC,
            "Non concerné avec ajout d'étape pour l'étape <" + dossierLinkDoc.getName() + ">"
        );

        // Met à jour l'étape en cours avec l'avis non concerné
        final STDossierLink dossierLink = dossierLinkDoc.getAdapter(STDossierLink.class);
        final DocumentModel etapeCouranteDoc = session.getDocument(new IdRef(dossierLink.getRoutingTaskId()));

        // Recherche la derniere étape validée
        final DocumentModel etapePrecedenteDoc = findPreviousRouteStep(session, etapeCouranteDoc, true);
        final SSRouteStep etapePrecedente = etapePrecedenteDoc.getAdapter(SSRouteStep.class);

        // Récupère la position de l'étape en cours /!\ renseigné par effet de bord par findPreviousStepInFolder()
        final Integer currentStepPos = (Integer) etapeCouranteDoc.getContextData(STConstant.POS_DOC_CONTEXT);

        // Insère une nouvelle étape "Retour pour modification" après l'étape en cours
        final EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
        try {
            final FeuilleRouteStep newRouteStepDoc = documentRoutingService.createNewRouteStep(
                session,
                etapePrecedente.getDistributionMailboxId(),
                VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION
            );
            documentRoutingService.addRouteElementToRoute(
                etapeCouranteDoc.getParentRef(),
                currentStepPos + 1,
                newRouteStepDoc,
                session
            );
        } catch (FeuilleRouteNotLockedException e) {
            LOGGER.error(STLogEnumImpl.FAIL_CREATE_STEP_TEC, e);
            throw new EPGException(e);
        }

        updateStepValidationStatus(
            session,
            etapeCouranteDoc,
            SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_NON_CONCERNE_VALUE,
            dossierDoc
        );

        // Valide le DossierLink
        validateDossierLink(session, dossierLinkDoc, dossierDoc);

        // Journalise l'action
        SSServiceLocator
            .getSSJournalService()
            .journaliserActionEtapeFDR(
                session,
                etapeCouranteDoc.getAdapter(SSRouteStep.class),
                dossierDoc,
                STEventConstant.DOSSIER_VALIDER_NON_CONCERNE_EVENT,
                STEventConstant.DOSSIER_VALIDER_NON_CONCERNE_COMMENT_PARAM
            );
    }

    /**
     * Recherche l'étape "retour pour modification" à insérer automatiquement, si possible.
     *
     * @param session
     *            Session
     * @param etapeCouranteDoc
     *            Etape en cours à partir de laquelle remonter
     * @return Etape trouvée
     */
    protected DocumentModel findPreviousRouteStep(
        final CoreSession session,
        final DocumentModel etapeCouranteDoc,
        final boolean includedEtapeParallel
    ) {
        final Filter routeStepFilter = new RouteStepValideFilter();
        final DocumentModel etapePrecedenteDoc = epgFeuilleRouteService.findPreviousStepInFolder(
            session,
            etapeCouranteDoc,
            routeStepFilter,
            includedEtapeParallel
        );
        final String feedbackErrorAjoutEtape = "epg.dossier.distribution.ajoutEtapeRetourModification.error";
        if (etapePrecedenteDoc == null) {
            LOGGER.info(
                session,
                SSLogEnumImpl.FAIL_UPDATE_STEP_TEC,
                "Aucune étape trouvée pour l'insertion automatique"
            );
            throw new EPGException(feedbackErrorAjoutEtape);
        }

        // Vérifie que le poste correspondant à la dernière étape n'a pas été supprimé
        final SSRouteStep etapePrecedente = etapePrecedenteDoc.getAdapter(SSRouteStep.class);
        final String distributionMailboxId = etapePrecedente.getDistributionMailboxId();
        final String posteId = SSServiceLocator.getMailboxPosteService().getPosteIdFromMailboxId(distributionMailboxId);
        final PosteNode poste = STServiceLocator.getSTPostesService().getPoste(posteId);
        if (poste.getDeleted()) {
            LOGGER.info(
                session,
                SSLogEnumImpl.FAIL_UPDATE_STEP_TEC,
                "Le poste correspondant à l'étape n'est plus actif"
            );
            throw new EPGException(feedbackErrorAjoutEtape);
        }
        return etapePrecedenteDoc;
    }

    @Override
    public void setDossierLinksFields(final CoreSession session, final DocumentModel dossierLinkDoc) {
        final DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);

        // date de creation denormalisée
        dossierLink.setDateCreation(Calendar.getInstance());

        final Dossier dossier = dossierLink.getDossier(session, Dossier.class);
        final DocumentModel dossierDoc = dossier.getDocument();

        // denormalisation caseDocumentId
        dossierLink.setCaseDocumentId(dossierDoc.getId());

        // Recopie du cycle de vie du Dossier
        dossierLink.setCaseLifeCycleState(dossierDoc.getCurrentLifeCycleState());

        // recopie du statut d'archivage du Dossier : permet de déterminer si le dossier est dans la base d'archivage
        // intermédiaire
        dossierLink.setStatutArchivage(dossier.getStatutArchivage());

        // recopie du titre de l'acte
        dossierLink.setTitreActe(dossier.getTitreActe());

        // Récupère l'étape en cours de la feuille de route à partir du CaseLink
        final ActionableCaseLink acl = dossierLinkDoc.getAdapter(ActionableCaseLink.class);
        final DocumentModel etapeDoc = SSServiceLocator.getDocumentRoutingService().getFeuilleRouteStep(session, acl);
        final SSRouteStep routeStep = etapeDoc.getAdapter(SSRouteStep.class);

        // Renseigne l'identifiant technique de l'étape en cours
        dossierLink.setRoutingTaskId(etapeDoc.getId());

        // Renseigne l'identifiant technique du type de l'étape en cours
        final String routingTaskType = routeStep.getType();
        dossierLink.setRoutingTaskType(routingTaskType);

        // Renseigne le nom de l'étape en cours
        final String routingTaskLabel = STServiceLocator
            .getVocabularyService()
            .getEntryLabel(STVocabularyConstants.ROUTING_TASK_TYPE_VOCABULARY, routingTaskType);
        dossierLink.setRoutingTaskLabel(routingTaskLabel);

        // Renseigne le libellé de la maibox poste de distribution
        final String mailboxId = routeStep.getDistributionMailboxId();
        final String routingTaskMailboxLabel = STServiceLocator.getMailboxService().getMailboxTitle(session, mailboxId);
        dossierLink.setRoutingTaskMailboxLabel(routingTaskMailboxLabel);

        // texte Signalé si on arrive dans un poste SSG on met a true ArriveeSolon
        final String posteId = SSServiceLocator.getMailboxPosteService().getPosteIdFromMailboxId(mailboxId);

        // chercher si poste dans ministere SGG pour l'arrivée SOLON
        final List<EntiteNode> list = STServiceLocator.getSTMinisteresService().getMinistereParentFromPoste(posteId);
        final DocumentModel tableReferenceDoc = tabRefService.getTableReference(session);
        if (tableReferenceDoc != null) {
            final String ministereSgg = tableReferenceDoc.getAdapter(TableReference.class).getMinisterePM();
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(ministereSgg)) {
                for (final EntiteNode entiteNode : list) {
                    if (ministereSgg.equals(entiteNode.getId())) {
                        dossier.setArriveeSolonTS(Boolean.TRUE);
                        dossier.save(session);
                        // on arrete la boucle lorsque on trouve un poste faisant parti du ministère du Sgg
                        break;
                    }
                }
            }
        }

        if (VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(routingTaskType)) {
            // etape pour publication DILA JO, il faut renseigner la date d'envoi
            dossier.setDateEnvoiJOTS(Calendar.getInstance());
            dossier.save(session);
        } else if (VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE.equals(routingTaskType)) {
            // RG-LOI-MAI-33 etape pour "Pour avis CE", il faut renseigner la date d'arrivée
            final ConseilEtat conseilEtat = dossierDoc.getAdapter(ConseilEtat.class);
            conseilEtat.setDateSaisineCE(Calendar.getInstance());
            conseilEtat.setDateTransmissionSectionCe(Calendar.getInstance());

            final List<OrganigrammeNode> directions = STServiceLocator
                .getSTUsAndDirectionService()
                .getDirectionFromPoste(posteId);
            // on prends la premiere direction trouvée
            for (final OrganigrammeNode organigrammeNode : directions) {
                conseilEtat.setSectionCe(organigrammeNode.getLabel());
                break;
            }

            conseilEtat.save(session);
            final String typeActeId = dossier.getTypeActe();

            if (acteService.isDecret(typeActeId)) {
                SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .updateDecretFromFeuilleDeRoute(dossierDoc, session, false);
            } else if (acteService.isLoi(typeActeId)) {
                SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .updateLoiFromFeuilleDeRoute(dossierDoc, session, false);
            }
        }
        dossierLink.save(session);
    }

    @Override
    public void validerEtapeRetourModification(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final DocumentModel dossierLinkDoc
    ) {
        LOGGER.info(
            session,
            SSLogEnumImpl.UPDATE_STEP_TEC,
            "Retour pour modification pour l'étape <" + dossierLinkDoc.getName() + ">"
        );

        // check next step compatible with type acte
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final STDossierLink dossierLink = dossierLinkDoc.getAdapter(STDossierLink.class);
        final DocumentModel etapeDoc = session.getDocument(new IdRef(dossierLink.getRoutingTaskId()));
        checkNextStep(session, dossier, etapeDoc);

        // Met à jour l'étape en cours avec l'avis non concerné
        final SSRouteStep etape = etapeDoc.getAdapter(SSRouteStep.class);
        updateStepValidationStatus(
            session,
            etapeDoc,
            SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETOUR_MODIFICATION_VALUE,
            dossierDoc
        );

        // Valide le DossierLink
        validateDossierLink(session, dossierLinkDoc, dossierDoc);

        // Journalise l'action
        SSServiceLocator
            .getSSJournalService()
            .journaliserActionEtapeFDR(
                session,
                etape,
                dossierDoc,
                SolonEpgEventConstant.DOSSIER_VALIDER_RETOUR_MODIFICATION_EVENT,
                SolonEpgEventConstant.DOSSIER_VALIDER_RETOUR_MODIFICATION_COMMENT_PARAM
            );
    }

    @Override
    public void updateStepValidationStatus(
        final CoreSession session,
        final DocumentModel etapeDoc,
        final String validationStatus,
        final DocumentModel dossierDoc
    ) {
        super.updateStepValidationStatus(session, etapeDoc, validationStatus, dossierDoc);

        final SSRouteStep routeStep = etapeDoc.getAdapter(SSRouteStep.class);
        final String routingTaskType = routeStep.getType();
        // met a jour
        if (VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE.equals(routingTaskType)) {
            // RG-LOI-MAI-35 etape pour "Pour avis CE", il faut renseigner la date de sortie
            final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            final ConseilEtat conseilEtat = dossierDoc.getAdapter(ConseilEtat.class);
            conseilEtat.setDateSortieCE(Calendar.getInstance());
            // note : utilisation d'un session unrestricted
            new UnrestrictedCreateOrSaveDocumentRunner(session).saveDocument(conseilEtat.getDocument());
            final String typeActeId = dossier.getTypeActe();
            if (acteService.isDecret(typeActeId)) {
                SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .updateDecretFromFeuilleDeRoute(dossierDoc, session, false);
            } else if (acteService.isLoi(typeActeId)) {
                SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .updateLoiFromFeuilleDeRoute(dossierDoc, session, false);
            }
        }
    }

    public void updateDossierStateRapportParlementaire(
        final CoreSession session,
        final DocumentModel etapeDoc,
        final String validationStatus,
        final DocumentModel dossierDoc
    ) {
        super.updateStepValidationStatus(session, etapeDoc, validationStatus, dossierDoc);

        final SSRouteStep routeStep = etapeDoc.getAdapter(SSRouteStep.class);
        final String routingTaskType = routeStep.getType();
        boolean isRapportAuParlement = TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT.equals(
            dossierDoc.getAdapter(Dossier.class).getTypeActe()
        );
        if (
            VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SECTEUR_PARLEMENTAIRE.equals(routingTaskType) &&
            isRapportAuParlement
        ) {
            // si on est sur une etape "attribution secteur parlemantaire"
            // alors on passe le statut du dossier a "terminé sans publication"
            final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            dossier.setStatut(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION);
            // note : utilisation d'un session unrestricted
            new UnrestrictedCreateOrSaveDocumentRunner(session).saveDocument(dossier.getDocument());
        }
    }

    @Override
    public void validerEtapeRetourModificationAjoutEtape(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final DocumentModel dossierLinkDoc,
        final String posteID
    ) {
        final EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();

        LOGGER.info(
            session,
            SSLogEnumImpl.UPDATE_STEP_TEC,
            "Retour pour modification avec ajout d'étape pour l'étape <" +
            dossierLinkDoc.getName() +
            "> et le poste " +
            posteID
        );
        String posteWithPrefix = STConstant.PREFIX_POSTE + posteID;

        // Met à jour l'étape en cours avec l'avis non concerné
        final STDossierLink dossierLink = dossierLinkDoc.getAdapter(STDossierLink.class);
        final DocumentModel etapeCouranteDoc = session.getDocument(new IdRef(dossierLink.getRoutingTaskId()));
        final SSRouteStep etapeCourante = etapeCouranteDoc.getAdapter(SSRouteStep.class);

        // Recherche la derniere étape validée
        final DocumentModel etapePrecedenteDoc = findPreviousRouteStep(session, etapeCouranteDoc, true);
        final SSRouteStep etapePrecedente = etapePrecedenteDoc.getAdapter(SSRouteStep.class);

        // Récupère la position de l'étape en cours /!\ renseigné par effet de bord par findPreviousStepInFolder()
        final Integer currentStepPos = (Integer) etapeCouranteDoc.getContextData(STConstant.POS_DOC_CONTEXT);

        final DocumentModel documentRoute = session.getDocument(new IdRef(etapePrecedente.getDocumentRouteId()));

        if (
            FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER.equals(
                session.getDocument(etapeCouranteDoc.getParentRef()).getType()
            )
        ) {
            final DocumentModel parent = getDocumentRouteElementRoot(session, etapeCouranteDoc);
            // Insère une nouvelle étape "Retour pour modification" après l'étape en cours
            final FeuilleRouteStep retourModificationRouteStep = documentRoutingService.createNewRouteStep(
                session,
                posteWithPrefix,
                VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION
            );
            documentRoutingService.addRouteElementToRoute(
                etapePrecedenteDoc.getParentRef(),
                currentStepPos + 1,
                retourModificationRouteStep,
                session
            );
            duplicateDocumentRouteElementInRoute(session, parent, currentStepPos + 2, documentRoute, null);
        } else {
            // Insère une nouvelle étape "Retour pour modification" après l'étape en cours
            final FeuilleRouteStep retourModificationRouteStep = documentRoutingService.createNewRouteStep(
                session,
                posteWithPrefix,
                VocabularyConstants.ROUTING_TASK_TYPE_RETOUR_MODIFICATION
            );
            documentRoutingService.addRouteElementToRoute(
                etapeCouranteDoc.getParentRef(),
                currentStepPos + 1,
                retourModificationRouteStep,
                session
            );

            // Insère une copie de l'étape en cours après l'étape retour pour modification
            final DocumentModel etapeCouranteCopyDoc = documentRoutingService.duplicateRouteStep(
                session,
                etapeCouranteDoc
            );
            final FeuilleRouteStep etapeCouranteCopyDocRoute = etapeCouranteCopyDoc.getAdapter(FeuilleRouteStep.class);

            documentRoutingService.addRouteElementToRoute(
                etapeCouranteDoc.getParentRef(),
                currentStepPos + 2,
                etapeCouranteCopyDocRoute,
                session
            );
        }

        updateStepValidationStatus(
            session,
            etapeCouranteDoc,
            SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_RETOUR_MODIFICATION_VALUE,
            dossierDoc
        );

        validateDossierLink(session, dossierLinkDoc, dossierDoc);

        // Journalise l'action
        SSServiceLocator
            .getSSJournalService()
            .journaliserActionEtapeFDR(
                session,
                etapeCourante,
                dossierDoc,
                SolonEpgEventConstant.DOSSIER_VALIDER_RETOUR_MODIFICATION_EVENT,
                SolonEpgEventConstant.DOSSIER_VALIDER_RETOUR_MODIFICATION_COMMENT_PARAM
            );
    }

    @Override
    public String getPosteForNor(final Set<String> posteIdSet, final String norMinistere, final String norDirection) {
        // Si l'utilisateur a un seul poste, on retourne ce poste
        if (posteIdSet.size() == 1) {
            return posteIdSet.iterator().next();
        }

        // Si l'utilisateur a plusieurs postes, recherche un poste dont les lettres ministère et direction de NOR
        // correspondent
        final String lettreNorDossier = norMinistere + norDirection;
        final EpgOrganigrammeService organigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
        final Set<String> posteIdMatchingSet = new HashSet<>();
        for (final String posteId : posteIdSet) {
            final Set<String> lettreNorPosteSet = organigrammeService.findLettreNorByPoste(posteId);
            if (lettreNorPosteSet.contains(lettreNorDossier)) {
                posteIdMatchingSet.add(posteId);
            }
        }
        if (posteIdMatchingSet.size() == 1) {
            return posteIdMatchingSet.iterator().next();
        }

        // Si aucun poste ne correspond ou que plusieurs postes correspondent, echec
        return null;
    }

    @Override
    public void initDossierLinkAcl(
        final CoreSession session,
        final DocumentModel dossierLinkDoc,
        final DocumentModel dossierDoc
    ) {
        final ACP acp = dossierLinkDoc.getACP();

        // Restriction des mesures nominatives
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        if (dossier.isMesureNominative()) {
            STServiceLocator
                .getSecurityService()
                .addAceToAcpInFirstPosition(
                    acp,
                    SolonEpgAclConstant.MESURE_NOMINATIVE_ACL,
                    SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_DENY_READER,
                    SecurityConstants.READ_WRITE,
                    false
                );
        }
        // Visiblité des ministères
        final List<ACE> aceList = getDossierLinkDistributionAce(dossierLinkDoc);
        final ACL functionAcl = acp.getOrCreateACL(STAclConstant.ACL_SECURITY);
        for (final ACE ace : aceList) {
            functionAcl.add(ace);
        }
        acp.addACL(functionAcl);
        session.setACP(dossierLinkDoc.getRef(), acp, true);
        session.save();
    }

    /**
     * Construit la liste des ACE à ajouter lors de la distribution d'un dossier.
     *
     * @param dossierLinkDoc
     *            DossierLink
     * @return Liste des ACE à ajouter
     */
    protected List<ACE> getDossierLinkDistributionAce(final DocumentModel dossierLinkDoc) {
        final DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);

        // Ajoute les administrateurs fonctionnels
        final List<ACE> aceList = new ArrayList<>();
        aceList.add(new ACE(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_UPDATER, SecurityConstants.READ_WRITE, true));

        // Détermine la liste des ministères de la mailbox de distribution
        final String distributionMailboxId = dossierLink.getDistributionMailbox();
        final String posteId = SSServiceLocator.getMailboxPosteService().getPosteIdFromMailboxId(distributionMailboxId);
        final List<EntiteNode> ministereList = STServiceLocator
            .getSTMinisteresService()
            .getMinistereParentFromPoste(posteId);

        // Ajoute les administrateur du ministère interpellé courant
        for (final EntiteNode ministere : ministereList) {
            final String group = STAclConstant.DOSSIER_LINK_UPDATER_MIN_ACE_PREFIX + ministere.getId();
            aceList.add(new ACE(group, SecurityConstants.READ_WRITE, true));
        }

        return aceList;
    }

    @Override
    public void startRouteAfterSubstitution(
        final CoreSession coreSession,
        final DocumentModel oldFeuilleRouteDoc,
        final DocumentModel newFeuilleRouteDoc,
        final String typeCreation
    ) {
        // Renseigne le type de création de l'instance de feuille de route
        final SSFeuilleRoute newFeuilleRoute = newFeuilleRouteDoc.getAdapter(SSFeuilleRoute.class);
        final SSFeuilleRoute oldFeuilleRoute = oldFeuilleRouteDoc.getAdapter(SSFeuilleRoute.class);
        newFeuilleRoute.setTypeCreation(typeCreation);

        // Récupère l'étape la première "pour initialisation" de l'ancienne instance
        final DocumentModel oldEtapeInitDoc = epgFeuilleRouteService.getEtapePourInitialisation(
            coreSession,
            oldFeuilleRoute
        );
        final SSRouteStep oldEtapeInit = oldEtapeInitDoc.getAdapter(SSRouteStep.class);
        final String distributionMailboxId = oldEtapeInit.getDistributionMailboxId();

        // Affecte le poste à la première étape "pour initialisation" de la nouvelle instance de feuille de route
        final DocumentModel newEtapeInitDoc = epgFeuilleRouteService.getEtapePourInitialisation(
            coreSession,
            newFeuilleRoute
        );
        final SSRouteStep newEtapeInit = newEtapeInitDoc.getAdapter(SSRouteStep.class);
        newEtapeInit.setDistributionMailboxId(distributionMailboxId);
        coreSession.saveDocument(newEtapeInitDoc);
        coreSession.save();

        List<DocumentModel> dossierDocs = newFeuilleRoute
            .getAttachedDocuments()
            .stream()
            .map(id -> coreSession.getDocument(new IdRef(id)))
            .collect(Collectors.toList());
        List<Dossier> dossiers = dossierDocs
            .stream()
            .map(doc -> doc.getAdapter(Dossier.class))
            .collect(Collectors.toList());

        new UnrestrictedSessionRunner(coreSession) {

            @Override
            public void run() {
                String idNewFeuilleRoute = newFeuilleRoute.getDocument().getId();
                dossiers.forEach(dos -> dos.setLastDocumentRoute(idNewFeuilleRoute));
                coreSession.saveDocuments(dossierDocs.toArray(new DocumentModel[0]));
            }
        }
        .runUnrestricted();

        newFeuilleRoute.run(coreSession);
        coreSession.save();

        // Récupère le service de feuilles de route
        final EPGFeuilleRouteService routingService = SolonEpgServiceLocator.getEPGFeuilleRouteService();

        // récupérer les dossiers de nouveau avec le CoreSession de base
        List<String> dossierIds = dossiers
            .stream()
            .map(Dossier::getDocument)
            .map(DocumentModel::getId)
            .collect(toList());
        // calcul des échéances de feuille de route
        coreSession
            .getDocuments(dossierIds, toPrefetchInfo(DOSSIER_LAST_DOCUMENT_ROUTE_XPATH))
            .stream()
            .map(d -> d.getAdapter(Dossier.class))
            .forEach(dossier -> routingService.calculEcheanceFeuilleRoute(coreSession, dossier));
    }

    @Override
    public void updateDossierLinksFields(final CoreSession session, final Dossier dossier) {
        final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();

        final List<DocumentModel> dossierLinkDocs = corbeilleService.findDossierLinkUnrestricted(
            session,
            dossier.getDocument().getId()
        );

        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                for (final DocumentModel documentModel : dossierLinkDocs) {
                    final DossierLink dossierLink = documentModel.getAdapter(DossierLink.class);
                    dossierLink.setTitreActe(dossier.getTitreActe());
                    dossierLink.save(session);
                }
                session.save();
            }
        }
        .run();
    }

    private List<String> getListDocsToModify(Collection<String> idPostes, CoreSession session) {
        List<String> ids = new ArrayList<>();
        if (!idPostes.isEmpty()) {
            try {
                boolean first = true;
                StringBuilder query = new StringBuilder(
                    "SELECT * FROM DossierLink WHERE cmdist:all_action_participant_mailboxes IN ("
                );
                for (String idPoste : idPostes) {
                    if (first) {
                        first = false;
                    } else {
                        query.append(", ");
                    }
                    query.append("'poste-").append(idPoste).append("'");
                }
                query.append(") and ecm:currentLifeCycleState='todo'");

                ids.addAll(QueryUtils.doQueryForIds(session, query.toString()));
            } catch (Exception e) {
                LOGGER.error(STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC, e);
            }
        }
        return ids;
    }

    private boolean checkIfDocumentHasPerm(String permUserLabel, DocumentModel doc) {
        boolean hasPerm = false;
        try {
            ACP listOfPerms = doc.getACP();
            ACL listOfDroits = listOfPerms.getACL("security");
            if (listOfDroits != null) {
                for (ACE listOfDroit : listOfDroits) {
                    // On vérifie que les droits sont correctes pour l'utilisateur (libellé des utilisateur +
                    // lecture/écriture + autorisé
                    if (
                        listOfDroit.getUsername().equals(permUserLabel) &&
                        listOfDroit.isGranted() &&
                        listOfDroit.getPermission().equals("ReadWrite")
                    ) {
                        LOGGER.debug(
                            STLogEnumImpl.DEFAULT,
                            "Le document : " + doc.getId() + " disposait déjà du droit pour le ministère"
                        );
                        hasPerm = true;
                    }
                }
            }
        } catch (NuxeoException e) {
            LOGGER.debug(
                STLogEnumImpl.DEFAULT,
                "Une erreur est survenue lors de la vérification de la présence de la permission dans le document : " +
                doc.getId()
            );
        }
        return hasPerm;
    }

    private boolean addPermissionToDoc(String permLabel, DocumentModel doc, CoreSession session) {
        boolean addPerm = false;
        try {
            ACP listOfPerms = doc.getACP();
            ACL listOfDroits = listOfPerms.getACL("security");
            ACE nouvellePerm = new ACE(permLabel, "ReadWrite", true);
            listOfDroits.add(nouvellePerm);
            session.setACP(doc.getRef(), listOfPerms, false);

            addPerm = true;
        } catch (NuxeoException e) {
            LOGGER.error(STLogEnumImpl.FAIL_UPDATE_DOSSIER_TEC, e);
        }

        return addPerm;
    }

    private void traiterPosteCorbeille(Collection<String> idPostes, String idMinistere, CoreSession session) {
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Début du traitement des postes " + StringUtils.join(idPostes, ","));
        try {
            List<String> lstIds = getListDocsToModify(idPostes, session);

            if (lstIds != null && !lstIds.isEmpty()) {
                List<DocumentModel> docs = QueryHelper.retrieveDocuments(session, lstIds, null);

                for (DocumentModel doc : docs) {
                    String permLabel = "dossier_link_updater_min-" + idMinistere;
                    boolean hasPerm = checkIfDocumentHasPerm(permLabel, doc);
                    if (!hasPerm) {
                        if (!addPermissionToDoc(permLabel, doc, session)) {
                            LOGGER.debug(
                                STLogEnumImpl.DEFAULT,
                                "La permission" + permLabel + " n'a pas été ajoutée au document " + doc.getId()
                            );
                        } else {
                            LOGGER.debug(
                                STLogEnumImpl.DEFAULT,
                                "On a bien rajouté la permission " + permLabel + " au document : " + doc.getId()
                            );
                        }
                    }
                }
                session.save();
            }
        } catch (NuxeoException e) {
            LOGGER.error(STLogEnumImpl.FAIL_UPDATE_DOSSIER_TEC, e);
        }
        LOGGER.debug(STLogEnumImpl.DEFAULT, "Fin du traitement des postes " + StringUtils.join(idPostes, ","));
    }

    @Override
    public void updateDossierLinksACLs(final CoreSession session, String idMinistere) {
        STMinisteresService ministereService = STServiceLocator.getSTMinisteresService();
        EntiteNode minECF = ministereService.getEntiteNode(idMinistere);

        STPostesService postesService = STServiceLocator.getSTPostesService();
        List<String> lstPostes = postesService.getPosteIdInSubNode(minECF);

        if (lstPostes != null && !lstPostes.isEmpty()) {
            LOGGER.debug(STLogEnumImpl.DEFAULT, "On s'apprête à traiter " + lstPostes.size() + " postes");
            traiterPosteCorbeille(lstPostes, idMinistere, session);
        } else {
            LOGGER.debug(STLogEnumImpl.DEFAULT, "Pas de poste trouvé pour le ministère " + idMinistere);
        }
    }

    @Override
    public void validerAutomatiquementEtapePourPublication(final CoreSession session) {
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                final List<String> paramList = new ArrayList<>();

                final List<DocumentModel> dossierLinkList = QueryUtils.doUnrestrictedUFNXQLQueryAndFetchForDocuments(
                    session,
                    QUERY_AUTO_VALIDATION_POUR_PUBLI,
                    paramList.toArray()
                );
                epgFeuilleRouteService.validerAuromatiquementDossier(session, dossierLinkList);
            }
        }
        .run();
    }

    /**
     * get the root container(step folder) for a step
     *
     * @param session
     * @param stepDoc
     *            the step Doc
     * @return the root container for step folder
     */
    private DocumentModel getDocumentRouteElementRoot(final CoreSession session, final DocumentModel stepDoc) {
        final DocumentModel containerRouteElementDoc = session.getDocument(stepDoc.getParentRef());
        if (FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER.equals(containerRouteElementDoc.getType())) {
            return getDocumentRouteElementRoot(session, containerRouteElementDoc);
        }
        return stepDoc;
    }

    /**
     * duplicate Document Route Element InRoute
     *
     * @param session
     *            the session
     * @param stepDoc
     *            the steproute to duplicate
     * @param position
     *            the position where will be duplicated
     * @param relatedDocumentDoc
     *            the related Document Doc
     * @param parentDoc
     *            the parent Doc
     */
    private void duplicateDocumentRouteElementInRoute(
        final CoreSession session,
        final DocumentModel stepDoc,
        final int position,
        final DocumentModel relatedDocumentDoc,
        final DocumentModel parentDoc
    ) {
        final EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
        if (stepDoc.getType().equals(FeuilleRouteConstant.FACET_FEUILLE_ROUTE_STEP) && parentDoc != null) {
            final DocumentModel etapeCouranteCopyDoc = documentRoutingService.duplicateRouteStep(session, stepDoc);
            final FeuilleRouteStep etapeCouranteCopyStep = etapeCouranteCopyDoc.getAdapter(FeuilleRouteStep.class);
            documentRoutingService.addFeuilleRouteElementToRoute(
                session,
                relatedDocumentDoc,
                parentDoc.getRef(),
                null,
                etapeCouranteCopyStep.getDocument()
            );
        } else if (stepDoc.getType().equals(FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER)) {
            final StepFolder step = stepDoc.getAdapter(StepFolder.class);
            DocumentModel stepFolderDoc = session.createDocumentModel(
                FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER
            );
            final StepFolder serialStepFolder = stepFolderDoc.getAdapter(StepFolder.class);
            serialStepFolder.setExecution(FeuilleRouteExecutionType.valueOf(step.getExecution()));
            if (parentDoc == null) {
                documentRoutingService.addRouteElementToRoute(
                    stepDoc.getParentRef(),
                    position,
                    serialStepFolder,
                    session
                );
            } else {
                stepFolderDoc =
                    documentRoutingService.addFeuilleRouteElementToRoute(
                        session,
                        relatedDocumentDoc,
                        parentDoc.getRef(),
                        null,
                        stepFolderDoc
                    );
            }

            final DocumentModelList children = documentRoutingService.getOrderedRouteElement(stepDoc.getId(), session);
            if (children != null) {
                for (final DocumentModel child : children) {
                    duplicateDocumentRouteElementInRoute(session, child, position, relatedDocumentDoc, stepFolderDoc);
                }
            }
        }
    }

    /**
     * Vérifie et valide le dossier link
     */
    private void validateDossierLink(
        final CoreSession session,
        final DocumentModel dossierLinkDoc,
        final DocumentModel dossierDoc
    ) {
        final ActionableCaseLink acl = dossierLinkDoc.getAdapter(ActionableCaseLink.class);
        if (acl.isActionnable() && acl.isTodo()) {
            acl.validate(session);
        } else {
            LOGGER.debug(session, STLogEnumImpl.FAIL_VALIDATE_DL_TEC, dossierDoc, WARN_DL_VALIDATION + acl.getId());
            LOGGER.warn(session, STLogEnumImpl.FAIL_VALIDATE_DL_TEC, WARN_DL_VALIDATION + acl.getId());
        }
    }

    /**
     * Vérifie et refuse le dossier link
     */
    private void refuseDossierLink(
        final CoreSession session,
        final DocumentModel dossierLinkDoc,
        final DocumentModel dossierDoc
    ) {
        final ActionableCaseLink acl = dossierLinkDoc.getAdapter(ActionableCaseLink.class);
        if (acl.isActionnable() && acl.isTodo()) {
            acl.refuse(session);
        } else {
            LOGGER.debug(session, STLogEnumImpl.FAIL_VALIDATE_DL_TEC, dossierDoc, WARN_DL_VALIDATION + acl.getId());
            LOGGER.warn(session, STLogEnumImpl.FAIL_VALIDATE_DL_TEC, WARN_DL_VALIDATION + acl.getId());
        }
    }

    /**
     * Vérifie que l'étape suivante à venir dans la fdr est compatible avec le type d'acte
     */
    private void checkNextStep(final CoreSession session, final Dossier dossier, final DocumentModel etapeDoc) {
        // on vérifie que la prochaine étape est compatible avec l'acte en cours
        if (
            acteService.isActeTexteNonPublie(dossier.getTypeActe()) &&
            !epgFeuilleRouteService.isNextStepCompatibleWithActeTxtNonPub(
                session,
                dossier.getLastDocumentRoute(),
                etapeDoc
            )
        ) {
            String message = "Le type d'acte n'est pas compatible avec l'étape à suivre : " + dossier.getNumeroNor();
            LOGGER.info(session, SSLogEnumImpl.FAIL_UPDATE_STEP_TEC, message);
            throw new EPGException(message);
        }
    }

    /**
     * Supprime les étapes interdites dans une instance de feuille de route
     */
    @Override
    protected void deleteForbiddenSteps(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final DocumentModel routeInstanceDoc
    ) {
        if (session == null || dossierDoc == null || routeInstanceDoc == null) {
            LOGGER.debug(STLogEnumImpl.NPE_PARAM_METH_TEC, "Les paramètres ne doivent pas être nulls");
            return;
        }
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        SSFeuilleRoute route = routeInstanceDoc.getAdapter(SSFeuilleRoute.class);
        if (acteService.isActeTexteNonPublie(dossier.getTypeActe()) && route != null) {
            final List<RouteTableElement> routeElements = SSServiceLocator
                .getDocumentRoutingService()
                .getFeuilleRouteElements(route, session);
            final List<DocumentRef> stepsToRemove = new ArrayList<>();
            for (final RouteTableElement routeElement : routeElements) {
                if (!routeElement.getDocument().isFolder()) {
                    DocumentModel stepDoc = routeElement.getElement().getDocument();
                    SSRouteStep step = stepDoc.getAdapter(SSRouteStep.class);
                    if (step != null && INCOMPATIBLE_STEPS_ACTE_TXT_NON_PUB.contains(step.getType())) {
                        stepsToRemove.add(routeElement.getDocument().getRef());
                    }
                }
            }
            session.removeDocuments(stepsToRemove.toArray(new DocumentRef[stepsToRemove.size()]));
        }
        return;
    }

    @Override
    public void checkDossierBeforeValidateStep(CoreSession session, Dossier dossier, List<DocumentModel> nextStepsDoc) {
        checkDossierBeforeValidateStep(session, dossier, nextStepsDoc, false);
    }

    @Override
    public void checkDossierBeforeValidateStepWithAddingStep(
        CoreSession session,
        Dossier dossier,
        List<DocumentModel> nextStepsDoc
    ) {
        checkDossierBeforeValidateStep(session, dossier, nextStepsDoc, true);
    }

    private void checkDossierBeforeValidateStep(
        CoreSession session,
        Dossier dossier,
        List<DocumentModel> nextStepsDoc,
        boolean isAddingStep
    ) {

        if (!dossier.getIsParapheurComplet()) {

            /*
            Ajout d'une condition supplémentaire qui verifie en premier que le dossier est de type rapport au parlement
            et dans le cas contraire faire le traitement sur les autres type d'actes
            */

            if(dossier.getTypeActe() != null && dossier.getTypeActe().equals(TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT)){
                    throw new EPGException("Il faut obligatoirement ajouter un acte dans le parapheur");
                }else{
                    if (dossier.getIsActeReferenceForNumeroVersion()) {
                        // la pièce manquante est un acte
                        throw new EPGException("Il faut obligatoirement ajouter un acte dans le parapheur");
                    } else{
                        // la pièce manquante est un extrait
                        throw new EPGException("Il faut obligatoirement ajouter un extrait dans le parapheur");
                    }

                }
        }

        if (!bordereauService.isBordereauComplet(dossier)) {
            String donneesManquantes = bordereauService.getBordereauMetadonnesObligatoiresManquantes(
                dossier.getDocument()
            );
            throw new EPGException(
                ResourceHelper.getString("bordereau.champs.obligatoires.manquants", donneesManquantes)
            );
        }

        if (!isAddingStep) {
            // Vérifications supplémentaires si l'étape suivante est "Pour publication DILA JO"
            if (CollectionUtils.isNotEmpty(nextStepsDoc)) {
                checkModeParution(dossier, nextStepsDoc.get(0).getAdapter(SolonEpgRouteStep.class));
            }

            for (final DocumentModel nextStepDoc : nextStepsDoc) {
                final SolonEpgRouteStep nextStep = nextStepDoc.getAdapter(SolonEpgRouteStep.class);
                if (VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(nextStep.getType())) {
                    checkStepPourPublicationDilaJO(session, dossier);
                } else if (VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE.equals(nextStep.getType())) {
                    checkStepPourFournitureEpreuve(dossier);
                } else if (VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO.equals(nextStep.getType())) {
                    checkBODMR(session, dossier);
                } else if (VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_BO.equals(nextStep.getType())) {
                    checkBulletinOfficiel(session, dossier);
                }
            }
        }
    }

    private void checkModeParution(Dossier dossier, SolonEpgRouteStep curStep) {
        if (
            VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(curStep.getType()) ||
            VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO.equals(curStep.getType()) ||
            VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_BO.equals(curStep.getType())
        ) {
            RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
            if (retourDila == null || StringUtils.isBlank(retourDila.getModeParution())) {
                throw new EPGException("Le dossier ne peut pas être validé sans mode de parution");
            }
        }
    }

    private void checkStepPourFournitureEpreuve(Dossier dossier) {
        if (
            dossier.getDatePourFournitureEpreuve() == null ||
            !dossier.getDatePourFournitureEpreuve().after(Calendar.getInstance()) &&
            !DateUtil.isSameDay(dossier.getDatePourFournitureEpreuve(), Calendar.getInstance())
        ) {
            throw new EPGException(
                "L'étape suivante n'est pas compatible avec la date de fourniture d'épreuve renseignée"
            );
        }
    }

    private void checkStepPourPublication(CoreSession session, Dossier dossier) {
        checkDelaiPublication(dossier);
        checkPublicationIntegrale(dossier);
        checkDateSignature(session, dossier);
    }

    private void checkBulletinOfficiel(CoreSession session, Dossier dossier) {
        checkStepPourPublication(session, dossier);

        DocumentModelList activeBulletins = bulletinService.getAllActiveBulletinOfficielOrdered(session);
        List<String> activeBulletinsLabels = activeBulletins
            .stream()
            .map(doc -> doc.getAdapter(BulletinOfficiel.class))
            .map(BulletinOfficiel::getIntitule)
            .collect(toList());
        if (
            activeBulletins.isEmpty() ||
            (
                CollectionUtils.isEmpty(
                    CollectionUtils.intersection(activeBulletins, getBulletinsOfficielsDossier(session, dossier))
                ) &&
                CollectionUtils.isEmpty(
                    CollectionUtils.intersection(activeBulletinsLabels, dossier.getVecteurPublication())
                )
            )
        ) {
            throw new EPGException(NEXT_STEP_INCOMPATIBLE_ERROR_MESSAGE);
        }
    }

    private List<DocumentModel> getBulletinsOfficielsDossier(final CoreSession session, final Dossier dossier) {
        List<DocumentModel> bulletinsOfficiels = new ArrayList<>();
        if (dossier.getVecteurPublication() != null) {
            for (String idVect : dossier.getVecteurPublication()) {
                final IdRef docRef = new IdRef(idVect);
                try {
                    if (session.exists(docRef)) {
                        DocumentModel doc = session.getDocument(docRef);
                        if (doc.hasSchema(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_SCHEMA)) {
                            bulletinsOfficiels.add(doc);
                        }
                    }
                } catch (NuxeoException ce) {
                    LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, idVect, ce);
                }
            }
        }

        return bulletinsOfficiels;
    }

    private void checkBODMR(CoreSession session, Dossier dossier) {
        checkStepPourPublication(session, dossier);
        checkVecteurPublication(session, dossier, IS_VECTEUR_PUBLICATION_BODMR);
    }

    private void checkVecteurPublication(CoreSession session, Dossier dossier) {
        checkVecteurPublication(session, dossier, IS_VECTEUR_PUBLICATION_BODMR.negate());
    }

    private void checkVecteurPublication(
        CoreSession session,
        Dossier dossier,
        Predicate<VecteurPublicationDTO> vecteurPredicate
    ) {
        // Si on n'a pas de vecteurs de publication ou bien que certains sont inactifs on lance une alerte
        List<String> vecteurs = getVecteurPublicationDossier(session, dossier, vecteurPredicate);
        if (vecteurs.isEmpty() || !bulletinService.isAllVecteurPublicationActif(session, vecteurs)) {
            throw new EPGException(NEXT_STEP_INCOMPATIBLE_ERROR_MESSAGE);
        }
    }

    private List<String> getVecteurPublicationDossier(
        CoreSession session,
        Dossier dossier,
        Predicate<VecteurPublicationDTO> vecteurPredicate
    ) {
        return getVecteurPublicationDossier(session, dossier)
            .stream()
            .filter(vecteurPredicate)
            .map(VecteurPublicationDTO::getId)
            .collect(Collectors.toList());
    }

    private List<VecteurPublicationDTO> getVecteurPublicationDossier(final CoreSession session, final Dossier dossier) {
        List<VecteurPublicationDTO> lstIntituleVecteurs = new ArrayList<>();
        if (dossier.getVecteurPublication() != null) {
            for (String idVect : dossier.getVecteurPublication()) {
                final IdRef docRef = new IdRef(idVect);
                try {
                    if (session.exists(docRef)) {
                        DocumentModel doc = session.getDocument(docRef);
                        if (doc.hasSchema(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA)) {
                            VecteurPublication vecteur = doc.getAdapter(VecteurPublication.class);
                            lstIntituleVecteurs.add(new VecteurPublicationDTOImpl(vecteur));
                        }
                    }
                } catch (NuxeoException ce) {
                    LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, idVect, ce);
                }
            }
        }

        return lstIntituleVecteurs;
    }

    private void checkDelaiPublication(Dossier dossier) {
        // Contrôle du délai de publication
        if (StringUtils.isBlank(dossier.getDelaiPublication())) {
            throw new EPGException("L'étape suivante n'est pas compatible avec le délai de publication renseigné");
        } else {
            if (
                dossier.getDelaiPublication().equals(VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE) &&
                (
                    dossier.getDatePreciseePublication() == null ||
                    dossier.getDatePreciseePublication().before(Calendar.getInstance())
                )
            ) {
                throw new EPGException("L'étape suivante n'est pas compatible avec la date de publication précisée");
            }
        }
    }

    private void checkPublicationIntegrale(Dossier dossier) {
        if (StringUtils.isBlank(dossier.getPublicationIntegraleOuExtrait())) {
            throw new EPGException(
                "L'étape suivante n'est pas compatible avec la publication intégrale ou par extrait renseignée"
            );
        }
    }

    private void checkDateSignature(CoreSession session, Dossier dossier) {
        // Vérification de la date de signature pour le type d'acte
        if (dossier.getDateSignature() == null && StringUtils.isNotBlank(dossier.getTypeActe())) {
            TableReference tabRef = tabRefService.getTableReference(session).getAdapter(TableReference.class);
            String libelleTypeActe = vocService.getEntryLabel(
                VocabularyConstants.TYPE_ACTE_VOCABULARY,
                dossier.getTypeActe()
            );
            if (
                StringUtils.isNotBlank(libelleTypeActe) &&
                tabRef.getTypesActe().stream().map(String::trim).collect(Collectors.toList()).contains(libelleTypeActe)
            ) {
                throw new EPGException("L'étape suivante n'est pas compatible avec l'absence de date de signature");
            }
        }
    }

    private void checkStepPourPublicationDilaJO(CoreSession session, Dossier dossier) {
        if (acteService.isActeTexteNonPublie(dossier.getTypeActe())) {
            throw new EPGException("L'étape suivante n'est pas compatible avec l'absence de date de signature");
        }
        if (parapheurService.getPieceParapheur(session, dossier, false).isEmpty()) {
            throw new EPGException("Le répertoire \"Acte intégral\" ou \"Extrait\" du parapheur n'est pas complet");
        }

        if (
            dossier.getPublicationRapportPresentation() &&
            fondDeDossierService.getRapportPresentationFiles(session, dossier).isEmpty()
        ) {
            throw new EPGException(
                "Le répertoire \"Rapport de présentation à publier\" du fond de dossier n'est pas complet"
            );
        }

        if (
            dossier.getTexteEntreprise() &&
            CollectionUtils.isNotEmpty(dossier.getDateEffet()) &&
            dossier.getDateEffet().stream().noneMatch(cal -> cal.after(Calendar.getInstance()))
        ) {
            throw new EPGException(
                ResourceHelper.getString("feedback.solonepg.bordereau.au.moins.une.date.effet.inferieure.date.jour")
            );
        }

        // Contrôle commun aux publication dila ou non dans les BO
        checkStepPourPublication(session, dossier);

        checkVecteurPublication(session, dossier);
    }

    private void createEpreuvesParapheur(CoreSession session, Dossier dossier, DocumentModel etapeDoc) {
        // ABI : on crée le répertoire "Pour épreuves" quand on passe à l'étape pour épreuvage
        final List<DocumentModel> nextSteps = epgFeuilleRouteService.findAllNextSteps(
            session,
            dossier.getLastDocumentRoute(),
            etapeDoc,
            null
        );
        if (nextSteps != null && !nextSteps.isEmpty() && isNextStepFournitureEpreuve(nextSteps, session)) {
            // On regarde si le dossier existe déjà dans le FDD
            List<DocumentModel> fddFilsniv2 = fondDeDossierService.getAllFddFolder(session, dossier);
            boolean estPresentEpreuveFdd = fddFilsniv2
                .stream()
                .anyMatch(
                    noeud ->
                        SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES.equals(noeud.getTitle()) ||
                        SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES.equals(noeud.getName())
                );

            if (!estPresentEpreuveFdd) {
                // On récupère le répertoire parent
                DocumentModel fddFolderAccessAllUser = fondDeDossierService.getFondDossierFolder(
                    dossier.getDocument(),
                    session,
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER,
                    false
                );
                // on crée le répertoire
                DocumentModel myNewFolderEpreuves = fondDeDossierService.createNewFolder(
                    session,
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE,
                    fddFolderAccessAllUser,
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES
                );

                // on le paramètre
                FondDeDossierFolder fddFolderEpreuves = myNewFolderEpreuves.getAdapter(FondDeDossierFolder.class);
                fddFolderEpreuves.setIsDeletable(false);
                fddFolderEpreuves.save(session);
            }

            // On crée le répertoire dans le parapheur
            Parapheur parapheurDossier = dossier.getParapheur(session);

            // on vérifie que le repertoire n'est pas déjà présents
            List<ParapheurFolder> repertoires1erNiveau = parapheurService.getParapheurRootNode(
                session,
                parapheurDossier.getDocument()
            );
            boolean estPresentEpreuveParapheur = false;
            for (ParapheurFolder noeudP : repertoires1erNiveau) {
                if (noeudP.getTitle().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME)) {
                    // si le répertoire est déjà présent on s'arrête
                    estPresentEpreuveParapheur = true;
                }
            }

            if (!estPresentEpreuveParapheur) {
                // on crée le répertoire
                DocumentModel parapheurDocRoot = parapheurDossier.getDocument();
                DocumentModel myNewFolderEpreuves = parapheurService.createNewFolder(
                    session,
                    SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE,
                    parapheurDocRoot,
                    SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME
                );

                // on le paramètre
                ParapheurFolder parapheurFolderEpreuves = myNewFolderEpreuves.getAdapter(ParapheurFolder.class);
                parapheurFolderEpreuves.setNbDocAccepteMax(SolonEpgParapheurConstants.NB_DOC_MAX_REP_EPREUVE);
                parapheurFolderEpreuves.save(session);
            }
        }
    }

    public boolean isNextStepFournitureEpreuve(List<DocumentModel> nextSteps, CoreSession session) {
        List<DocumentModel> paralleleSteps = new ArrayList<>();

        for (DocumentModel nextStep : nextSteps) {
            String nextStepType = nextStep.getAdapter(SSRouteStep.class).getType();
            DocumentModel parentStep = session.getDocument(nextStep.getParentRef());
            DocumentModel secondParentStep = session.getDocument(parentStep.getParentRef());
            // étape parallèle si : premier parent Stepfolder et parent du parent StepFolder parallèle
            // Si étape parallèle on stock
            if (
                FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER.equals(parentStep.getType()) &&
                FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER.equals(secondParentStep.getType()) &&
                secondParentStep.getAdapter(StepFolder.class).isParallel()
            ) {
                paralleleSteps.add(nextStep);
            } else {
                // si non parallèle et pas d'étape parallèle précéédemment stocké : on vérifie si étape epreuve
                if (paralleleSteps.isEmpty()) {
                    if (VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE.equals(nextStepType)) {
                        return true;
                    }
                    // Si pas epreuve on vérifie si étape nformation: Si oui on continue car étape validé automatiquement
                    if (VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION.equals(nextStepType)) {
                        continue;
                    }
                    // sinon return false : étape suivante pas pour fourniture epreuve
                    return false;
                } else {
                    // Si non parallèle mais étape stocké avant : on vérifie les types étapes parallèle stocké
                    // si une étape stocké est pour epreuve -> return true
                    // si toutes les étapes stocké sont information -> tester si suivante est epreuve
                    // sinon return false étape suivante non epreuve
                    boolean isAllInfoStep = true;
                    for (DocumentModel step : paralleleSteps) {
                        String stepType = step.getAdapter(SSRouteStep.class).getType();
                        if (VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE.equals(stepType)) {
                            return true;
                        }
                        if (!VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION.equals(stepType)) {
                            isAllInfoStep = false;
                        }
                    }
                    return (
                        isAllInfoStep && VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE.equals(nextStepType)
                    );
                }
            }
        }
        return false;
    }

    @Override
    public Collection<DocumentModel> findChargesDeMissionSteps(final CoreSession session, DocumentModel stepDoc) {
        List<DocumentModel> results = new ArrayList<>();
        if (stepDoc != null) {
            DocumentModel containerRouteElementDoc = session.getDocument(stepDoc.getParentRef());

            // Récupère toutes les étape du conteneur parent
            final DocumentModelList routeStepList = SSServiceLocator
                .getDocumentRoutingService()
                .getOrderedRouteElement(containerRouteElementDoc.getId(), session);

            // /!\ Renseigne la position de l'étape en cours (effet de bord)
            final int currentStepIndex = routeStepList.indexOf(stepDoc);
            stepDoc.putContextData(STConstant.POS_DOC_CONTEXT, currentStepIndex);

            // Remonte les étapes jusqu'à trouver une étape acceptée
            for (int index = currentStepIndex - 1; index >= 0; --index) {
                final DocumentModel routeElementDoc = routeStepList.get(index);

                if (routeElementDoc.isFolder()) {
                    // Etape // : on recupere les étapes du conteneur. Si au moins une d'elles est
                    // de type "Pour avis" ET affectée à un poste identifié
                    List<DocumentModel> children = session.getChildren(routeElementDoc.getRef());
                    results =
                        children
                            .stream()
                            .map(stepDocModel -> session.getChildren(stepDocModel.getRef()))
                            .map(EpgDossierDistributionServiceImpl::getChargesDeMissionFolderStep)
                            .filter(Objects::nonNull)
                            .collect(toList());

                    if (CollectionUtils.isNotEmpty(results)) {
                        return results;
                    }
                } else {
                    // Etape en série : on vérifie si elle est "Pour avis" ET affectée à un poste identifié
                    if (isChargesDeMissionFolderStep(routeElementDoc.getAdapter(SolonEpgRouteStep.class))) {
                        results.add(routeElementDoc);
                        return results;
                    }
                }
            }
        }
        return results;
    }

    private static DocumentModel getChargesDeMissionFolderStep(DocumentModelList list) {
        DocumentModel result = null;
        if (CollectionUtils.isNotEmpty(list)) {
            DocumentModel folderStep = list.get(0);

            if (isChargesDeMissionFolderStep(folderStep.getAdapter(SolonEpgRouteStep.class))) {
                result = folderStep;
            }
        }

        return result;
    }

    private static boolean isChargesDeMissionFolderStep(SolonEpgRouteStep etape) {
        String posteId = SSServiceLocator
            .getMailboxPosteService()
            .getPosteIdFromMailboxId(etape.getDistributionMailboxId());
        PosteNode posteNode = getSTPostesService().getPoste(posteId);

        return (
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS.equals(etape.getType()) &&
            (posteNode.isChargeMissionSGG() || posteNode.isConseillerPM())
        );
    }

    @Override
    public FeuilleRoute startRouteCopieFromDossier(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final String posteId,
        final String norDossierCopieFdr
    ) {
        NORService norService = SolonEpgServiceLocator.getNORService();
        DocumentModel dossierFdrToCopyDoc = norService.getDossierFromNOR(session, norDossierCopieFdr);
        EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();

        STLockService lockService = STServiceLocator.getSTLockService();
        EpgDocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();

        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        if (session.getLockInfo(dossierDoc.getRef()) == null) {
            lockService.lockDoc(session, dossierDoc);
        }

        String routeId = dossierFdrToCopyDoc.getAdapter(Dossier.class).getLastDocumentRoute();
        DocumentModel routeDocToCopy = session.getDocument(new IdRef(routeId));

        corbeilleService
            .findDossierLink(session, dossierDoc.getId())
            .forEach(
                dossierLinkDoc ->
                    session.followTransition(dossierLinkDoc.getRef(), STLifeCycleConstant.TO_DONE_TRANSITION)
            );

        LOGGER.debug(
            session,
            SSLogEnumImpl.START_FDR_TEC,
            "Récupération du model de feuille de route par défaut pour le dossier <" + dossierDoc.getRef() + ">"
        );

        final FeuilleRoute newRoute = documentRoutingService.duplicateFeuilleRoute(session, dossierDoc, routeDocToCopy);

        // Attacher une session car pas de session suite au duplicate
        newRoute.getDocument().detach(false);
        newRoute.getDocument().attach(session.getSessionId());

        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                // Affecte le poste à l'étape "pour initialisation de la feuille de route"
                epgFeuilleRouteService.initEtapePourInitialisation(session, newRoute, posteId);

                // Démarre la feuille de route
                newRoute.run(session);
            }
        }
        .runUnrestricted();

        // Retirer les attached documents de l'ancienne fdr + cancel si elle existe
        Optional
            .ofNullable(dossier.getLastDocumentRoute())
            .map(IdRef::new)
            .map(session::getDocument)
            .ifPresent(
                oldRouteDoc -> {
                    FeuilleRoute oldRouteInstance = oldRouteDoc.getAdapter(FeuilleRoute.class);
                    oldRouteInstance.setAttachedDocuments(new ArrayList<>());
                    oldRouteInstance.save(session);
                    oldRouteInstance.cancel(session);
                }
            );

        // Renseigne la dernière feuille de route du dossier exécutée sur le dossier
        dossier.setLastDocumentRoute(newRoute.getDocument().getId());

        // calcul des échéances de feuille de route
        epgFeuilleRouteService.calculEcheanceFeuilleRoute(session, dossier);

        lockService.unlockDoc(session, dossierDoc);
        dossier.save(session);

        return newRoute;
    }

    @Override
    public void restartDossier(CoreSession session, DocumentModel dossierDoc, String nextStatut) {
        LOGGER.info(session, STLogEnumImpl.UPDATE_DOSSIER_TEC, "Redémarrage du dossier <" + dossierDoc.getName() + ">");

        // Vérifie que le dossier est à l'état terminé
        if (!dossierDoc.getCurrentLifeCycleState().equals(STDossier.DossierState.done.name())) {
            throw new SSException("Le dossier doit être à l'état done");
        }

        // Vérifie que la feuille de route est à l'état terminé
        DocumentModel routeInstanceDoc = getLastDocumentRouteForDossier(session, dossierDoc);
        if (!routeInstanceDoc.getCurrentLifeCycleState().equals(STDossier.DossierState.done.name())) {
            throw new SSException("La feuille de route doit être à l'état done");
        }

        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        CoreInstance.doPrivileged(
            session,
            systemSession -> {
                dossierDoc.followTransition(STDossier.DossierTransition.backToRunning.name());

                // Redémarre la feuille de route
                SSFeuilleRoute feuilleRouteInstance = routeInstanceDoc.getAdapter(SSFeuilleRoute.class);
                feuilleRouteInstance.backToReady(session);
                feuilleRouteInstance.run(session);
            }
        );

        dossier.setStatut(nextStatut);
        dossier.save(session);
    }
}
