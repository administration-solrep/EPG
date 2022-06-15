package fr.dila.solonepg.core.service;

import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getEPGFeuilleRouteService;
import static fr.dila.st.api.constant.STLifeCycleConstant.TO_DELETE_TRANSITION;
import static fr.dila.st.core.service.STServiceLocator.getWorkManager;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.cm.cases.CaseTreeHelper;
import fr.dila.cm.mailbox.Mailbox;
import fr.dila.cm.service.CaseDistributionService;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgAclConstant;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.solonepg.api.elastic.service.ElasticRequeteurService;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.recherche.EpgDossierListingDTO;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.DossierSignaleService;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.cases.CreateDossierUnrestricted;
import fr.dila.solonepg.core.cm.work.DeleteDossiersWork;
import fr.dila.solonepg.core.util.DossierPageProviderHelper;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.api.service.SSJournalService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STAclConstant;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.JetonService;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STLockService;
import fr.dila.st.api.service.SecurityService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.CoreSessionUtil;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.PermissionHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.StringHelper;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteElement;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.model.RouteTableElement;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import fr.sword.xsd.solon.epg.TypeModification;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.core.persistence.PersistenceProvider;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunCallback;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunVoid;
import org.nuxeo.ecm.core.persistence.PersistenceProviderFactory;
import org.nuxeo.ecm.core.schema.PrefetchInfo;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * Implémentation du service permettant de gérer le cycle de vie du dossier SOLON EPG.
 *
 * @author jtremeaux
 */
public class DossierServiceImpl extends DefaultComponent implements DossierService {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 7560048177930537867L;

    /**
     * Logger.
     */
    private static final STLogger LOGGER = STLogFactory.getLog(DossierServiceImpl.class);

    private static final PrefetchInfo PREFETCH_CASELINK = new PrefetchInfo(
        "acslk:caseDocumentId,acslk:routingTaskType"
    );

    private static volatile PersistenceProvider persistenceProvider;

    private static final String SUPPRESSION_CONSULTATION_PROVIDER_NAME = "dossiersCandidatElimination";
    private static final String SUPPRESSION_SUIVI_PROVIDER_NAME = "dossiersValidesElimination";
    private static final String ABANDON_PROVIDER_NAME = "dossierAbandon";

    /**
     * Default constructor
     */
    public DossierServiceImpl() {
        super();
    }

    /**
     * Récupère le chemin parent pour le futur dossier Cette méthode a été récupérée dans le persister du socle nuxeo
     * car celui-ci ne pouvait être initialisé pour des raisons inconnues
     *
     * @param session
     * @return
     */
    protected String getParentDocumentPathForCase(final CoreSession session) {
        final StringBuilder parentPath = new StringBuilder();
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                // Retrieve the MailRoot folder
                DocumentModel mailRootdoc = session.getDocument(new PathRef(CaseConstants.CASE_ROOT_DOCUMENT_PATH));
                // Create (or retrieve) the current MailRoot folder
                // (/mail/YYYY/MM/DD)
                Date now = new Date();
                DocumentModel parent = CaseTreeHelper.getOrCreateDateTreeFolder(
                    session,
                    mailRootdoc,
                    now,
                    CaseConstants.CASE_TREE_TYPE
                );
                parentPath.append(parent.getPathAsString());
            }
        }
        .runUnrestricted();
        return parentPath.toString();
    }

    @Override
    public Dossier createDossier(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final String posteId,
        final Mailbox userMailBox,
        String norDossierCopieFdr
    ) {
        // création du case lié au dossier
        final CaseDistributionService caseDistribService = SSServiceLocator.getCaseDistributionService();
        final Dossier dossier = caseDistribService.createEmptyCase(session, dossierDoc, userMailBox, Dossier.class);

        // creation du dossier
        return createDossier(session, dossier, posteId, norDossierCopieFdr);
    }

    @Override
    public Dossier createDossierWs(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final String posteId,
        final Mailbox userMailBox
    ) {
        // création du case lié au dossier
        final Dossier dossier = createCaseWS(session, dossierDoc, posteId, userMailBox);

        // creation du dossier
        return createDossier(session, dossier, posteId, null);
    }

    protected Dossier createCaseWS(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final String posteId,
        final Mailbox userMailBox
    ) {
        // création du case lié au dossier
        final String parentPath = getParentDocumentPathForCase(session);
        final CreateDossierUnrestricted dossierCreator = new CreateDossierUnrestricted(
            session,
            dossierDoc,
            parentPath,
            userMailBox
        );
        dossierCreator.runUnrestricted();
        return dossierCreator.getDossier();
    }

    @Override
    public Dossier createDossier(
        final CoreSession session,
        final Dossier dossier,
        final String posteId,
        String norDossierCopieFdr
    ) {
        DocumentModel dossierDoc = dossier.getDocument();
        // Initialise les ministère / direction de rattachement
        dossier.setMinistereAttache(dossier.getMinistereResp());
        dossier.setDirectionAttache(dossier.getDirectionResp());

        // Initialise le statut
        dossier.setStatut(VocabularyConstants.STATUT_INITIE);
        // Initialise le statut d'archivage
        dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_AUCUN);

        dossier.setCandidat(VocabularyConstants.CANDIDAT_AUCUN);

        // Initialise la visibilité du dossier pour l'indexation
        dossier.setIndexationSgg(true);
        dossier.setIndexationSggPub(true);
        dossier.setIndexationMin(true);
        dossier.setIndexationMinPub(true);
        dossier.setIndexationDir(true);
        dossier.setIndexationDirPub(true);

        // date de creation denormalisée
        dossier.setDateCreation(Calendar.getInstance());

        // initialisation du context de creation du dossier
        dossier.setCreatorPoste(SSConstant.MAILBOX_POSTE_ID_PREFIX + posteId);

        // Renseigne le créateur du dossier
        NuxeoPrincipal principal = session.getPrincipal();
        dossier.setIdCreateurDossier(principal.getName());

        ActeService acteService = SolonEpgServiceLocator.getActeService();
        String typeActe = dossier.getTypeActe();
        if (!acteService.isActeTexteNonPublie(typeActe)) {
            // Renseigne le vecteur de publication JO par défaut pour tous les actes autres que les textes non publiés
            String journalOff = SolonEpgServiceLocator.getBulletinOfficielService().getLibelleForJO(session);
            dossier.setVecteurPublication(Collections.singletonList(journalOff));

            // FEV509 - Mode de parution par défaut à électronique
            RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
            TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
            List<String> listTabRef = tableReferenceService.getModesParutionListFromName(session, "Electronique");
            // Récupération de l'id du mode de parution électronique
            if (CollectionUtils.isNotEmpty(listTabRef)) {
                retourDila.setModeParution(listTabRef.get(0));
                dossierDoc = retourDila.save(session);
            }
        }

        // Publication intégrale ou par extrait : par défaut "Intégrale" RG-DOS-TRT-29
        dossier.setPublicationIntegraleOuExtrait(DossierSolonEpgConstants.DOSSIER_INTEGRAL_PROPERTY_VALUE);

        // Initialise l'état "mesure nominative" du dossier
        if (acteService.hasTypeActeMesureNominative(typeActe)) {
            dossier.setMesureNominative(true);
        }

        // Initialise l'affichage du rapport de présentation : si ordonnance, on publie
        // le rapport de présentation par
        // défaut
        if (acteService.isOrdonnance(typeActe)) {
            dossier.setPublicationRapportPresentation(true);
        }

        if (acteService.isDecret(typeActe)) {
            dossier.setDecretNumerote(true);
        }

        // Crée le parapheur
        SolonEpgServiceLocator.getParapheurService().createAndFillParapheur(dossier, session);

        // Crée le fond de dossier
        SolonEpgServiceLocator.getFondDeDossierService().createAndFillFondDossier(dossier, session);

        // Crée l'ampliation du dossier
        SolonEpgServiceLocator.getAmpliationService().createAmpliationDossierDocument(dossier, session);

        final TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);

        try {
            String nor = dossier.getNumeroNor().substring(0, 3);
            Optional
                .ofNullable(SolonEpgServiceLocator.getEpgOrganigrammeService().getMinistereFromNor(nor))
                .ifPresent(ministere -> traitementPapier.setRetourA(ministere.getFormule()));
        } catch (final Exception e) {
            LOGGER.warn(
                session,
                STLogEnumImpl.FAIL_SET_PROPERTY_TEC,
                "le champ 'retour a' n'a pas pu être rempli correctement : " + e.getMessage()
            );
            LOGGER.debug(
                session,
                STLogEnumImpl.FAIL_SET_PROPERTY_TEC,
                "le champ 'retour a' n'a pas pu être rempli correctement : " + e.getMessage(),
                e
            );
        }

        // Sauvegarde le dossier
        dossier.save(session);

        // Lève un événement de fin de création de dossier
        try {
            final Map<String, Serializable> eventProperties = new HashMap<>();
            eventProperties.put(SolonEpgEventConstant.DOSSIER_EVENT_PARAM, dossier);
            eventProperties.put(SolonEpgEventConstant.POSTE_ID_EVENT_PARAM, posteId);
            eventProperties.put(SolonEpgEventConstant.NOR_DOSSIER_COPIE_FDR_PARAM, norDossierCopieFdr);
            final InlineEventContext inlineEventContext = new InlineEventContext(session, principal, eventProperties);

            EventProducer eventProducer = STServiceLocator.getEventProducer();
            eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgEventConstant.AFTER_DOSSIER_CREATED));
        } catch (final RuntimeException e) {
            // Probablement une erreur lors du démarrage de la feuille de route
            throw new NuxeoException(ResourceHelper.getString("epg.distribution.feuilleRoute.error"), e);
        }

        JournalService journalService = STServiceLocator.getJournalService();
        if (StringUtils.isNotBlank(norDossierCopieFdr)) {
            journalService.journaliserActionFDR(
                session,
                dossierDoc,
                STEventConstant.EVENT_COPIE_FDR_DEPUIS_DOSSIER,
                ResourceHelper.getString(STEventConstant.COMMENT_COPIE_FDR_DEPUIS_DOSSIER, norDossierCopieFdr)
            );
        }
        // journalisation de l'action dans les logs
        journalService.journaliserActionBordereau(
            session,
            dossierDoc,
            STEventConstant.EVENT_DOSSIER_CREATION,
            STEventConstant.COMMENT_DOSSIER_CREATION
        );

        return dossier;
    }

    @Override
    public void initDossierIndexationAclUnrestricted(final CoreSession session, final List<Dossier> dossiersList) {
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                for (Dossier dossier : dossiersList) {
                    initDossierIndexationAcl(session, dossier);
                }
            }
        }
        .runUnrestricted();
    }

    private void initDossierIndexationAcl(final CoreSession session, final Dossier dossier) {
        final DocumentModel dossierDoc = dossier.getDocument();

        LOGGER.debug(
            session,
            STLogEnumImpl.LOG_DEBUG_TEC,
            "Initialisation des droits d'indexation du dossier <" + dossierDoc.getId()
        );

        final SecurityService securityService = STServiceLocator.getSecurityService();

        DocumentRef dossierRef = dossierDoc.getRef();
        final ACP acp = session.getACP(dossierRef);
        acp.removeACL(SolonEpgAclConstant.INDEXATION_ACL);

        final String currentStatut = dossier.getStatut();
        final String ministereId = dossier.getMinistereAttache();
        final String directionId = dossier.getDirectionAttache();

        // acl pour profils documentalistes
        if (VocabularyConstants.STATUT_LANCE.equals(currentStatut)) {
            securityService.addAceToAcp(
                acp,
                SolonEpgAclConstant.INDEXATION_ACL,
                SolonEpgAclConstant.INDEXATION_MIN_ACE_PREFIX + ministereId,
                SecurityConstants.READ_WRITE
            );
            securityService.addAceToAcp(
                acp,
                SolonEpgAclConstant.INDEXATION_ACL,
                SolonEpgAclConstant.INDEXATION_DIR_ACE_PREFIX + directionId,
                SecurityConstants.READ_WRITE
            );
            securityService.addAceToAcp(
                acp,
                SolonEpgAclConstant.INDEXATION_ACL,
                SolonEpgBaseFunctionConstant.INDEXATION_SGG_UPDATER,
                SecurityConstants.READ_WRITE
            );
        }

        // acl pour profils documentalistes post publication et profils documentalistes
        if (VocabularyConstants.STATUT_PUBLIE.equals(currentStatut)) {
            securityService.addAceToAcp(
                acp,
                SolonEpgAclConstant.INDEXATION_ACL,
                SolonEpgAclConstant.INDEXATION_MIN_ACE_PREFIX + ministereId,
                SecurityConstants.READ_WRITE
            );
            securityService.addAceToAcp(
                acp,
                SolonEpgAclConstant.INDEXATION_ACL,
                SolonEpgAclConstant.INDEXATION_DIR_ACE_PREFIX + directionId,
                SecurityConstants.READ_WRITE
            );
            securityService.addAceToAcp(
                acp,
                SolonEpgAclConstant.INDEXATION_ACL,
                SolonEpgBaseFunctionConstant.INDEXATION_SGG_UPDATER,
                SecurityConstants.READ_WRITE
            );
            securityService.addAceToAcp(
                acp,
                SolonEpgAclConstant.INDEXATION_ACL,
                SolonEpgAclConstant.INDEXATION_MIN_PUBLI_ACE_PREFIX + ministereId,
                SecurityConstants.READ_WRITE
            );
            securityService.addAceToAcp(
                acp,
                SolonEpgAclConstant.INDEXATION_ACL,
                SolonEpgAclConstant.INDEXATION_DIR_PUBLI_ACE_PREFIX + directionId,
                SecurityConstants.READ_WRITE
            );
            securityService.addAceToAcp(
                acp,
                SolonEpgAclConstant.INDEXATION_ACL,
                SolonEpgBaseFunctionConstant.INDEXATION_SGG_PUBLI_UPDATER,
                SecurityConstants.READ_WRITE
            );
        }

        session.setACP(dossierRef, acp, true);
    }

    private void initDossierCreationAcl(final CoreSession session, final Dossier dossier) {
        LOGGER.debug(
            session,
            STLogEnumImpl.LOG_DEBUG_TEC,
            "Initialisation des droits du dossier <" + dossier.getDocument().getId()
        );

        final SecurityService securityService = STServiceLocator.getSecurityService();
        final ACP acp = dossier.getDocument().getACP();

        if (dossier.isMesureNominative()) {
            acp.removeACL(SolonEpgAclConstant.MESURE_NOMINATIVE_ACL);
            securityService.addAceToAcp(
                acp,
                SolonEpgAclConstant.MESURE_NOMINATIVE_ACL,
                SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_DENY_READER,
                SecurityConstants.READ_WRITE,
                false
            );
        }

        // Les administrateurs du ministère de rattachement voient le dossier
        final String ministereRattachementId = dossier.getMinistereAttache();
        if (!StringUtils.isEmpty(ministereRattachementId)) {
            acp.removeACL(SolonEpgAclConstant.RATTACHEMENT_ACL);
            securityService.addAceToAcp(
                acp,
                SolonEpgAclConstant.RATTACHEMENT_ACL,
                SolonEpgAclConstant.DOSSIER_RATTACH_MIN_ACE_PREFIX + ministereRattachementId,
                SecurityConstants.READ_WRITE
            );
        }

        // Les administrateurs de la direction de rattachement voient le dossier
        final String directionRattachementId = dossier.getDirectionAttache();
        if (!StringUtils.isEmpty(directionRattachementId)) {
            securityService.addAceToAcp(
                acp,
                SolonEpgAclConstant.RATTACHEMENT_ACL,
                SolonEpgAclConstant.DOSSIER_RATTACH_DIR_ACE_PREFIX + directionRattachementId,
                SecurityConstants.READ_WRITE
            );
        }

        // Les utilisateurs possédant la fonction DossierRechercheReader voient le
        // dossier
        acp.removeACL(STAclConstant.ACL_SECURITY);
        securityService.addAceToAcp(
            acp,
            STAclConstant.ACL_SECURITY,
            SolonEpgBaseFunctionConstant.DOSSIER_RECHERCHE_READER,
            SecurityConstants.READ_WRITE
        );

        session.setACP(dossier.getDocument().getRef(), acp, true);
    }

    @Override
    public void initDossierCreationAclUnrestricted(final CoreSession session, final List<Dossier> dossierList) {
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                for (Dossier dossier : dossierList) {
                    initDossierCreationAcl(session, dossier);
                }
            }
        }
        .runUnrestricted();
    }

    @Override
    public Boolean isReattribuable(final Dossier dossier) {
        final String statut = dossier.getStatut();
        // M157545 - Les dossiers doivent être au status initié ou lancé pour être réattribué
        if (
            VocabularyConstants.STATUT_INITIE.equals(statut) ||
            VocabularyConstants.STATUT_LANCE.equals(statut) ||
            VocabularyConstants.STATUT_ABANDONNE.equals(statut)
        ) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean isTransferable(final Dossier dossier) {
        final String statut = dossier.getStatut();
        if (
            VocabularyConstants.STATUT_PUBLIE.equals(statut) ||
            VocabularyConstants.STATUT_NOR_ATTRIBUE.equals(statut) ||
            VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION.equals(statut) ||
            VocabularyConstants.STATUT_CLOS.equals(statut)
        ) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public void transfertDossiersUnrestricted(
        final List<DocumentModel> docList,
        final String ministereAttache,
        final String directionAttachee,
        final CoreSession session
    ) {
        if (docList == null || StringUtils.isEmpty(ministereAttache) || StringUtils.isEmpty(directionAttachee)) {
            return;
        }

        final EntiteNode ministereNode = STServiceLocator.getSTMinisteresService().getEntiteNode(ministereAttache);
        final JournalService journalService = STServiceLocator.getJournalService();

        final UniteStructurelleNode directionNode = STServiceLocator
            .getSTUsAndDirectionService()
            .getUniteStructurelleNode(directionAttachee);
        final String norDirection = directionNode.getNorDirectionForMinistereId(ministereAttache);

        for (final DocumentModel dossierDoc : docList) {
            final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

            // note : on ne sort pas le UnrestrictedSessionRunner à l'extérieur du for afin
            // de récupérer l'utilisateur
            // dans l'événement renvoyé à la transfertDossiersUnrestricted.
            new UnrestrictedSessionRunner(session) {

                @Override
                public void run() {
                    updateDossierWhenTransfert(ministereAttache, directionAttachee, false, dossier, session);
                    final String comment = ResourceHelper.getString(
                        "transfert.dossier.journal.commentaire",
                        ministereNode.getNorMinistere(),
                        ministereNode.getLabel(),
                        norDirection,
                        directionNode.getLabel()
                    );
                    journalService.journaliserActionAdministration(
                        session,
                        dossierDoc,
                        SolonEpgEventConstant.AFTER_DOSSIER_TRANSFERT,
                        comment
                    );
                }
            }
            .runUnrestricted();
        }
    }

    @Override
    public void reattribuerDossiersUnrestricted(
        final List<DocumentModel> docList,
        final String ministere,
        final String direction,
        final CoreSession session
    ) {
        if (docList == null || StringUtils.isBlank(ministere) || StringUtils.isBlank(direction)) {
            return;
        }
        reattribuerDossiersUnrestricted(docList, ministere, direction, session, false);
    }

    /**
     * reattribue des dossiers en unrestricted;
     *
     * @param docList
     * @param ministere
     * @param direction
     * @param session
     * @param ministereOnly
     */
    private void reattribuerDossiersUnrestricted(
        final List<DocumentModel> docList,
        final String ministere,
        final String direction,
        final CoreSession session,
        final boolean ministereOnly
    ) {
        final JournalService journalService = STServiceLocator.getJournalService();
        final NORService norService = SolonEpgServiceLocator.getNORService();

        // Détermine les lettres de NOR du ministère
        final EntiteNode ministereNode = STServiceLocator.getSTMinisteresService().getEntiteNode(ministere);
        final String norMinistere = ministereNode.getNorMinistere();

        String norDirectionTemp = null;
        String directionLabel = null;
        if (!ministereOnly) {
            // Détermine les lettres de NOR de la direction
            final UniteStructurelleNode directionNode = STServiceLocator
                .getSTUsAndDirectionService()
                .getUniteStructurelleNode(direction);
            norDirectionTemp = directionNode.getNorDirection(ministere);
            if (
                "".equals(norDirectionTemp) &&
                directionNode.getUniteStructurelleParentList().size() == 1 &&
                OrganigrammeType.DIRECTION.equals(directionNode.getUniteStructurelleParentList().get(0).getType())
            ) {
                // On a une direction sous une direction et on n'a pas réussi à trouver le NOR
                // On prend donc le NOR de la direction parente
                norDirectionTemp = directionNode.getUniteStructurelleParentList().get(0).getNorDirection(ministere);
            }
            directionLabel = directionNode.getLabel();
        }

        final String norDirection = norDirectionTemp;
        for (final DocumentModel dossierDoc : docList) {
            final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

            // note : on ne sort pas le UnrestrictedSessionRunner à l'extérieur du for afin
            // de récupérer l'utilisateur
            // dans l'événement renvoyé à la réattribution.
            new UnrestrictedSessionRunner(session) {

                @Override
                public void run() {
                    updateDossierWhenReattribution(
                        ministere,
                        direction,
                        ministereOnly,
                        true,
                        norMinistere,
                        norDirection,
                        norService,
                        dossier,
                        session
                    );
                }
            }
            .runUnrestricted();

            final String comment =
                "Réattribution du dossier à [" +
                ministereNode.getNorMinistere() +
                " - " +
                ministereNode.getLabel() +
                ", " +
                norDirection +
                " - " +
                directionLabel +
                "]";
            journalService.journaliserActionAdministration(
                session,
                dossierDoc,
                SolonEpgEventConstant.AFTER_DOSSIER_REATTRIBUTION,
                comment
            );
        }
    }

    @Override
    public void updateDossierWhenReattribution(
        final String ministere,
        final String direction,
        final boolean ministereOnly,
        boolean reattributionNor,
        final String norMinistere,
        final String norDirection,
        final NORService norService,
        final Dossier dossier,
        final CoreSession session
    ) {
        dossier.getDocument().refresh();

        String oldNor = dossier.getNumeroNor();

        final ActiviteNormative activiteNormative = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .findActiviteNormativeByNor(dossier.getNumeroNor(), session);
        boolean hasPublicationsConjointes = updateDossierWhenReattribution(
            ministere,
            direction,
            ministereOnly,
            reattributionNor,
            norMinistere,
            norDirection,
            norService,
            dossier,
            session,
            true,
            activiteNormative
        );

        if (hasPublicationsConjointes) {
            updatePublicationsConjointes(dossier, session, oldNor, dossier.getNumeroNor(), null);
        }

        // Changement du NOR de la fiche loi
        DocumentModel ficheLoiDoc = findFicheLoiDocumentFromMGPP(session, dossier.getIdDossier());
        if (ficheLoiDoc != null) {
            FicheLoi ficheLoi = ficheLoiDoc.getAdapter(FicheLoi.class);
            ficheLoi.setNumeroNor(dossier.getNumeroNor());
        }

        updateDossierWhenReattributionJetonCE(session, dossier, oldNor);
        session.save();
    }

    @Override
    public void updatePublicationsConjointes(
        Dossier dossier,
        CoreSession session,
        String oldNor,
        String newNor,
        Map<String, String> norReattributionsPubConj
    ) {
        List<String> dossiersPubJointe = new ArrayList<>();
        dossiersPubJointe = dossier.getPublicationsConjointesUnrestricted(session);

        List<String> dossiersJointsDuDossierJoint = null;

        if (dossiersPubJointe != null) {
            for (String norDossierJointCurr : dossiersPubJointe) {
                if (norReattributionsPubConj != null && norReattributionsPubConj.containsKey(norDossierJointCurr)) {
                    norDossierJointCurr = norReattributionsPubConj.get(norDossierJointCurr);
                }
                Dossier currDosJoint = null;
                try {
                    currDosJoint =
                        SolonEpgServiceLocator
                            .getNORService()
                            .getDossierFromNOR(session, norDossierJointCurr)
                            .getAdapter(Dossier.class);
                    dossiersJointsDuDossierJoint = currDosJoint.getPublicationsConjointesUnrestricted(session);
                    if (dossiersJointsDuDossierJoint.contains(oldNor)) {
                        dossiersJointsDuDossierJoint.add(newNor);
                        dossiersJointsDuDossierJoint.remove(oldNor);
                        currDosJoint.setPublicationsConjointesUnrestricted(session, dossiersJointsDuDossierJoint);
                    }
                } catch (NullPointerException npe) {
                    LOGGER.error(STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, npe);
                } finally {
                    if (currDosJoint != null) {
                        currDosJoint.save(session);
                    }
                }
            }
        }
    }

    @Override
    public boolean updateDossierWhenReattribution(
        final String ministere,
        final String direction,
        final boolean ministereOnly,
        boolean reattributionNor,
        final String norMinistere,
        final String norDirection,
        final NORService norService,
        final Dossier dossier,
        final CoreSession session,
        boolean checkUnicity,
        final ActiviteNormative activiteNormative
    ) {
        String norDossier = null;
        String norPreReattribution = dossier.getNumeroNor();
        dossier.setMinistereResp(ministere);
        if (ministereOnly) {
            norDossier = norService.reattributionMinistereNOR(dossier, norMinistere);
        } else {
            dossier.setDirectionResp(direction);
            norDossier = norService.reattributionDirectionNOR(dossier, norMinistere, norDirection);
        }

        // Check unicity (uniquement si on a demandé une réattribution du NOR)
        if (reattributionNor && checkUnicity) {
            norDossier = checkUnicity(norService, session, norDossier);
        }

        // mise a jour actvite normative
        if (activiteNormative != null) {
            final TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
            texteMaitre.setNumeroNor(norDossier);
            texteMaitre.setMinisterePilote(ministere);
            session.saveDocument(activiteNormative.getDocument());
        }

        dossier.setNumeroNor(norDossier);
        dossier.setTitle(norDossier);

        boolean hasPublicationsConjointes = checkDossierPublicationsConjointesWhenReattribution(dossier, session);

        updateDossierWhenTransfert(ministere, direction, ministereOnly, dossier, session);

        final EventProducer eventProducer = STServiceLocator.getEventProducer();
        final Map<String, Serializable> eventPropertiesFicheDR = new HashMap<>();
        eventPropertiesFicheDR.put(SolonEpgEventConstant.DOSSIER_EVENT_PARAM, dossier.getDocument());
        if (SolonEpgServiceLocator.getActeService().isRapportAuParlement(dossier.getTypeActe())) {
            eventPropertiesFicheDR.put(
                SolonEpgEventConstant.TYPE_ACTE_EVENT_PARAM,
                SolonEpgEventConstant.TYPE_ACTE_DR_EVENT_VALUE
            );
            eventPropertiesFicheDR.put(SolonEpgEventConstant.NOR_PRE_REATTRIBUTION_VALUE, norPreReattribution);
        } else {
            eventPropertiesFicheDR.put(
                SolonEpgEventConstant.TYPE_ACTE_EVENT_PARAM,
                SolonEpgEventConstant.TYPE_ACTE_FL_EVENT_VALUE
            );
            eventPropertiesFicheDR.put(SolonEpgEventConstant.NOR_PRE_REATTRIBUTION_VALUE, norPreReattribution);
        }

        final InlineEventContext inlineEventContextFP = new InlineEventContext(
            session,
            session.getPrincipal(),
            eventPropertiesFicheDR
        );
        eventProducer.fireEvent(
            inlineEventContextFP.newEvent(SolonEpgEventConstant.BATCH_EVENT_UPDATE_FICHE_AFTER_UPDATE_BORDEREAU)
        );

        return hasPublicationsConjointes;
    }

    @Override
    public boolean checkDossierPublicationsConjointesWhenReattribution(Dossier dossier, final CoreSession session) {
        // modification dans les publications conjointes
        List<String> dossiersPubJointe = dossier.getPublicationsConjointesUnrestricted(session);
        return !dossiersPubJointe.isEmpty();
    }

    private boolean hasEtapeAvisCEEnCours(CoreSession session, Dossier dossier) {
        List<DocumentModel> dossierLinkDocList = null;
        try {
            final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
            dossierLinkDocList = corbeilleService.findDossierLink(session, dossier.getDocument().getId());
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC, dossier.getDocument(), e);
        }
        if (dossierLinkDocList != null) {
            for (final DocumentModel dossierLinkDocElement : dossierLinkDocList) {
                // on verifie que le dossierLink est bien à l'étape "pour avis ce"
                final DossierLink dossierLink = dossierLinkDocElement.getAdapter(DossierLink.class);
                if (VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE.equals(dossierLink.getRoutingTaskType())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasEtapeAvisCEValide(CoreSession session, Dossier dossier) {
        EPGFeuilleRouteService fdrService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
        List<DocumentModel> stepsDoc = fdrService.findAllSteps(session, dossier.getLastDocumentRoute());
        for (DocumentModel stepDoc : stepsDoc) {
            SSRouteStep step = stepDoc.getAdapter(SSRouteStep.class);
            if (
                step.getType().equals(VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE) && step.getDateFinEtape() != null
            ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasEtapeAvisCE(CoreSession session, Dossier dossier) {
        EPGFeuilleRouteService fdrService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
        List<DocumentModel> stepsDoc = fdrService.findAllSteps(session, dossier.getLastDocumentRoute());
        for (DocumentModel stepDoc : stepsDoc) {
            SSRouteStep step = stepDoc.getAdapter(SSRouteStep.class);
            if (step.getType().equals(VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE)) {
                return true;
            }
        }
        return false;
    }

    private void updateDossierWhenReattributionJetonCE(CoreSession session, Dossier dossier, String oldNor) {
        // Si le dossier est à l'étape 'Pour avis CE' création jeton pour le WS
        // chercherModificationDossier (FEV 505)
        // récupère le dossierLink lié au dossier

        if (hasEtapeAvisCEEnCours(session, dossier)) {
            // On est dans l'étape 'Pour avis CE'
            // Création d'un jeton pour le Web Service chercherModificationDossier
            final JetonService jetonService = STServiceLocator.getJetonService();
            List<String> nors = new ArrayList<>();
            nors.add(oldNor);
            nors.add(dossier.getNumeroNor());
            jetonService.addDocumentInBasket(
                session,
                STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER,
                TableReference.MINISTERE_CE,
                dossier.getDocument(),
                dossier.getNumeroNor(),
                TypeModification.REATTRIBUTION.name(),
                nors
            );
        }
    }

    private List<DocumentModel> selectDossierWithEtapeCE(
        CoreSession session,
        List<DocumentModel> dossierToReattribuer
    ) {
        final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();

        Set<String> docIds = new HashSet<>();
        for (DocumentModel doc : dossierToReattribuer) {
            docIds.add(doc.getId());
        }
        List<DocumentModel> dossierLinkDocList = corbeilleService.findDossierLink(session, docIds, PREFETCH_CASELINK);

        List<DocumentModel> selected = new ArrayList<>();
        if (dossierLinkDocList != null) {
            docIds.clear();
            for (final DocumentModel dossierLinkDocElement : dossierLinkDocList) {
                // on verifie que le dossierLink est bien à l'étape "pour avis ce"
                final DossierLink dossierLink = dossierLinkDocElement.getAdapter(DossierLink.class);
                if (VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE.equals(dossierLink.getRoutingTaskType())) {
                    final String docId = dossierLink.getCaseDocumentId();
                    docIds.add(docId);
                }
            }
            for (DocumentModel doc : dossierToReattribuer) {
                if (docIds.contains(doc.getId())) {
                    selected.add(doc);
                }
            }
        }
        return selected;
    }

    @Override
    public void updateDossierWhenReattributionJetonCE(CoreSession session, List<DocumentModel> dossierToReattribuer) {
        List<DocumentModel> dossiersWithEtapeCE = selectDossierWithEtapeCE(session, dossierToReattribuer);

        if (dossiersWithEtapeCE != null && !dossiersWithEtapeCE.isEmpty()) {
            List<String> nors = new ArrayList<>();
            for (DocumentModel doc : dossiersWithEtapeCE) {
                nors.add(doc.getAdapter(Dossier.class).getNumeroNor());
            }

            final JetonService jetonService = STServiceLocator.getJetonService();
            jetonService.addDocumentInBasket(
                session,
                STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER,
                TableReference.MINISTERE_CE,
                dossiersWithEtapeCE,
                nors,
                TypeModification.REATTRIBUTION.name(),
                null
            );
        }
    }

    @Override
    public void updateDossierWhenTransfert(
        final String ministere,
        final String direction,
        final boolean ministereOnly,
        final Dossier dossier,
        final CoreSession session
    ) {
        dossier.setMinistereAttache(ministere);
        if (!ministereOnly) {
            dossier.setDirectionAttache(direction);
        }

        // on met à jour les droits d'indexation
        initDossierIndexationAcl(session, dossier);

        dossier.save(session);
    }

    @Override
    public void retirerAbandonDossier(final CoreSession session, final DocumentModel dossierDoc) {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final STLockService socleLockActionsService = STServiceLocator.getSTLockService();

        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                try {
                    if (dossier.getDocument().getLockInfo() != null) {
                        socleLockActionsService.unlockDoc(session, dossier.getDocument());
                    }
                    socleLockActionsService.lockDoc(session, dossier.getDocument());
                    dossier.setCandidat(VocabularyConstants.CANDIDAT_AUCUN);
                    final Calendar dateCandidature = GregorianCalendar.getInstance();
                    dossier.setDateCandidature(dateCandidature);
                    dossier.save(session);
                } catch (final Exception e) {
                    throw new NuxeoException(e);
                } finally {
                    socleLockActionsService.unlockDoc(session, dossier.getDocument());
                }
            }
        }
        .runUnrestricted();
    }

    @Override
    public void candidateDossierToSuppression(final CoreSession session, final DocumentModel dossierDoc)
        throws Exception {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        dossier.setCandidat(VocabularyConstants.CANDIDAT_ELIMINATION_ADMIN_FONCTIONNEL);
        dossier.save(session);
    }

    @Override
    public void abandonDossier(final CoreSession session, final DocumentModel dossierDoc) {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        abandonnerLeDossier(session, dossierDoc);
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
    }

    @Override
    public void retirerDossierFromSuppressionListUnrestricted(
        final CoreSession session,
        final DocumentModel dossierDoc
    ) {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final STLockService socleLockActionsService = STServiceLocator.getSTLockService();

        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                try {
                    if (dossier.getDocument().getLockInfo() != null) {
                        socleLockActionsService.unlockDoc(session, dossier.getDocument());
                    }
                    socleLockActionsService.lockDoc(session, dossier.getDocument());
                    dossier.setCandidat(VocabularyConstants.CANDIDAT_AUCUN);
                    dossier.save(session);
                } catch (final Exception e) {
                    throw new NuxeoException(e);
                } finally {
                    socleLockActionsService.unlockDoc(session, dossier.getDocument());
                }
            }
        }
        .runUnrestricted();
    }

    @Override
    public void validerTransmissionToSuppressionListUnrestricted(
        final CoreSession session,
        final DocumentModel dossierDoc
    ) {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final STLockService socleLockActionsService = STServiceLocator.getSTLockService();

        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                try {
                    if (dossier.getDocument().getLockInfo() != null) {
                        socleLockActionsService.unlockDoc(session, dossier.getDocument());
                    }
                    socleLockActionsService.lockDoc(session, dossier.getDocument());
                    dossier.setCandidat(VocabularyConstants.CANDIDAT_ELIMINATION_ADMIN_MINISTERIEL);
                    dossier.save(session);
                } catch (final Exception e) {
                    throw new NuxeoException(e);
                } finally {
                    socleLockActionsService.unlockDoc(session, dossier.getDocument());
                }
            }
        }
        .runUnrestricted();
    }

    @Override
    public void restartAbandonDossier(final CoreSession session, final DocumentModel dossierDoc) {
        LOGGER.info(session, STLogEnumImpl.START_DOSSIER_TEC, dossierDoc);

        final JournalService journalService = STServiceLocator.getJournalService();

        // Vérifie que le dossier est à l'état terminé
        if (!dossierDoc.getCurrentLifeCycleState().equals(STDossier.DossierState.done.name())) {
            throw new NuxeoException("Le dossier doit être à l'état done");
        }
        dossierDoc.followTransition(STDossier.DossierTransition.backToRunning.name());
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        dossier.setStatut(VocabularyConstants.STATUT_LANCE);
        session.saveDocument(dossierDoc);

        final String queryDL =
            "SELECT dl.ecm:uuid as id FROM DossierLink as dl where dl.acslk:caseDocumentId = ? AND dl.acslk:deleted = 0 ";
        final List<DocumentModel> resultListDL = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            "DossierLink",
            queryDL,
            new Object[] { dossierDoc.getId() }
        );

        for (final DocumentModel dossierLinkDoc : resultListDL) {
            final DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
            dossierLink.setCaseLifeCycleState(Dossier.DossierState.running.toString());
            session.saveDocument(dossierLinkDoc);

            DocumentModel stepDoc = session.getDocument(new IdRef(dossierLink.getRoutingTaskId()));
            SSRouteStep step = stepDoc.getAdapter(SSRouteStep.class);
            step.setRunning(session);
        }

        journalService.journaliserActionAdministration(
            session,
            dossierDoc,
            SolonEpgEventConstant.DOSSIER_STATUT_CHANGED,
            SolonEpgEventConstant.STATUT_DOSSIER_RELANCE_COMMENT
        );
    }

    @Override
    public void retirerDossierFromListCandidatsArchivageIntermediaireUnrestricted(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final int dureeManitienDossierEnProduction
    ) {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final STLockService socleLockActionsService = STServiceLocator.getSTLockService();

        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                try {
                    if (dossier.getDocument().getLockInfo() != null) {
                        socleLockActionsService.unlockDoc(session, dossier.getDocument());
                    }
                    socleLockActionsService.lockDoc(session, dossier.getDocument());
                    dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_AUCUN);
                    final Calendar dua = DateUtil.addMonthsToNow(dureeManitienDossierEnProduction);
                    dossier.setDateDeMaintienEnProduction(dua);
                    dossier.save(session);
                } catch (final Exception e) {
                    throw new NuxeoException(e);
                } finally {
                    socleLockActionsService.unlockDoc(session, dossier.getDocument());
                }
            }
        }
        .runUnrestricted();
    }

    @Override
    public void verserDossierDansBaseArchivageIntermediaireUnrestricted(
        final CoreSession session,
        final DocumentModel dossierDoc
    ) {
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                if (dossierDoc.getLockInfo() == null) {
                    final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                    dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE);
                    final Calendar dua = Calendar.getInstance();
                    dossier.setDateVersementArchivageIntermediaire(dua);
                    session.saveDocument(dossierDoc);

                    archiveDossiersLinks(session, dossierDoc);
                }
            }
        }
        .runUnrestricted();
    }

    @Override
    public void archiveDossiersLinks(final CoreSession session, final DocumentModel dossierDoc) {
        // récupère les dossiersLink lié au dossier
        final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
        final List<DocumentModel> dossierLinkDocList = corbeilleService.findDossierLink(session, dossierDoc.getId());
        // modifie les dossiersLinks du dossier
        for (final DocumentModel dossierLinkDoc : dossierLinkDocList) {
            final DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
            dossierLink.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE);
            dossierLink.save(session);
        }
    }

    @Override
    public void annulerMesureNominativeUnrestricted(final CoreSession session, final DocumentModel dossierDoc) {
        // Met à jour les droits du dossier
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                // Retire le booléen "mesure nominative"
                final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                if (!dossier.isMesureNominative()) {
                    throw new NuxeoException("Le dossier n'est pas une mesure nominative");
                }
                dossier.setMesureNominative(false);

                final ACP dossierAcp = dossierDoc.getACP();
                dossierAcp.removeACL(SolonEpgAclConstant.MESURE_NOMINATIVE_ACL);
                session.setACP(dossierDoc.getRef(), dossierAcp, true);
                dossier.save(session);

                // Met à jour les droits des DossierLink
                final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
                final List<DocumentModel> dossierLinkDocList = corbeilleService.findDossierLink(
                    session,
                    dossierDoc.getId()
                );
                for (final DocumentModel dossierLinkDoc : dossierLinkDocList) {
                    final ACP dossierLinkAcp = dossierLinkDoc.getACP();
                    dossierLinkAcp.removeACL(SolonEpgAclConstant.MESURE_NOMINATIVE_ACL);
                    session.setACP(dossierLinkDoc.getRef(), dossierLinkAcp, true);
                    session.saveDocument(dossierLinkDoc);
                }
            }
        }
        .runUnrestricted();
    }

    @Override
    public void ajouterMesureNominativeUnrestricted(final CoreSession session, final DocumentModel dossierDoc) {
        // Met à jour les droits du dossier
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                // Ajoute le booléen "mesure nominative"
                final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                if (dossier.isMesureNominative()) {
                    throw new NuxeoException("Le dossier est déjà une mesure nominative");
                }
                dossier.setMesureNominative(true);

                // Restriction des mesures nominatives sur le dossier
                final SecurityService securityService = STServiceLocator.getSecurityService();
                final ACP dossierAcp = dossierDoc.getACP();
                securityService.addAceToAcpInFirstPosition(
                    dossierAcp,
                    SolonEpgAclConstant.MESURE_NOMINATIVE_ACL,
                    SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_DENY_READER,
                    SecurityConstants.READ_WRITE,
                    false
                );
                session.setACP(dossierDoc.getRef(), dossierAcp, true);
                session.saveDocument(dossierDoc);

                // Met à jour les droits des DossierLink
                final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
                final List<DocumentModel> dossierLinkDocList = corbeilleService.findDossierLink(
                    session,
                    dossierDoc.getId()
                );
                for (final DocumentModel dossierLinkDoc : dossierLinkDocList) {
                    final ACP dossierLinkAcp = dossierLinkDoc.getACP();
                    securityService.addAceToAcpInFirstPosition(
                        dossierLinkAcp,
                        SolonEpgAclConstant.MESURE_NOMINATIVE_ACL,
                        SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_DENY_READER,
                        SecurityConstants.READ_WRITE,
                        false
                    );
                    session.setACP(dossierLinkDoc.getRef(), dossierLinkAcp, true);
                    session.saveDocument(dossierLinkDoc);
                }
            }
        }
        .runUnrestricted();
    }

    @Override
    public void abandonDossierPourReprise(final CoreSession session, final DocumentModel dossierDoc) {
        abandonnerLeDossier(session, dossierDoc);
    }

    /**
     * @param session
     * @param dossierDoc
     */
    private void abandonnerLeDossier(final CoreSession session, final DocumentModel dossierDoc) {
        final JournalService journalService = STServiceLocator.getJournalService();
        final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        // Passage du dossier à l'état Done
        dossierDoc.followTransition(Dossier.DossierTransition.toDone.toString());
        final List<DocumentModel> listDocDossierLink = corbeilleService.findDossierLink(session, dossierDoc.getId());
        final DocumentModel docDossierLink = listDocDossierLink.stream().findFirst().orElse(null);
        EPGFeuilleRouteService feuilleRouteService = getEPGFeuilleRouteService();

        if (docDossierLink != null) {
            feuilleRouteService
                .getRunningSteps(session, dossier.getLastDocumentRoute())
                .forEach(item -> setStepBackToReady(item, session));
        }
        for (final DocumentModel dossierLinkDoc : listDocDossierLink) {
            final DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
            dossierLink.setCaseLifeCycleState(Dossier.DossierState.done.toString());
            session.saveDocument(dossierLinkDoc);
        }
        // Changement du statut du dossier
        dossier.setStatut(VocabularyConstants.STATUT_ABANDONNE);
        dossier.setCandidat(VocabularyConstants.CANDIDAT_AUCUN);
        final Calendar dateCandidature = GregorianCalendar.getInstance();
        dossier.setDateCandidature(dateCandidature);
        session.saveDocument(dossierDoc);

        journalService.journaliserActionAdministration(
            session,
            dossierDoc,
            SolonEpgEventConstant.DOSSIER_STATUT_CHANGED,
            SolonEpgEventConstant.STATUT_DOSSIER_ABANDON_COMMENT
        );
    }

    @Override
    public Boolean updateDossierWhenTypeActeUpdated(final DocumentModel dossierDoc, final CoreSession session) {
        final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();

        Boolean typeActeUpdated = false;
        // récupération de l'ancien et du nouveau type d'acte
        final Dossier newDossier = dossierDoc.getAdapter(Dossier.class);
        final String newTypeActe = newDossier.getTypeActe();
        final DocumentModel oldDossierDoc = session.getDocument(dossierDoc.getRef());
        final Dossier oldDossier = oldDossierDoc.getAdapter(Dossier.class);
        final String oldTypeActe = oldDossier.getTypeActe();
        // si le type d'acte a changé on l'enregistre
        if (oldTypeActe != null && !oldTypeActe.equals(newTypeActe)) {
            // mise à jour un champ caché afin de signaler que le type d'acte a été changé
            newDossier.setIsParapheurTypeActeUpdated(true);
            // change la catégroie de l'acte en fonction du nouveau type d'acte
            final ActeService acteService = SolonEpgServiceLocator.getActeService();
            if (acteService.isNonReglementaire(newTypeActe)) {
                newDossier.setCategorieActe(VocabularyConstants.NATURE_NON_REGLEMENTAIRE);
            } else {
                newDossier.setCategorieActe(VocabularyConstants.NATURE_REGLEMENTAIRE);
            }
            // met à jour le champ mesure nominative
            if (acteService.hasTypeActeMesureNominative(newTypeActe)) {
                if (!oldDossier.isMesureNominative()) {
                    // on ajoute la caractère mesure nominative au dossier
                    ajouterMesureNominativeUnrestricted(session, dossierDoc);
                }
            } else if (oldDossier.isMesureNominative()) {
                // on retire le caractère mesure nominative au dossier
                annulerMesureNominativeUnrestricted(session, dossierDoc);
            }
            // mise à jour de l'arborsescence du fond de dossier
            final FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
            fondDeDossierService.updateFondDossierTree(newDossier, newTypeActe, session);
            typeActeUpdated = true;

            // Mise à jour du parapheur
            parapheurService.updateParapheurTree(dossierDoc, newTypeActe, session);
            // enregistrement du dossier
            session.saveDocument(dossierDoc);
        }
        parapheurService.checkParapheurComplet(dossierDoc, session);

        // Substitue la feuille de route si dossier à l'état initié
        if (BooleanUtils.isTrue(typeActeUpdated) && VocabularyConstants.STATUT_INITIE.equals(newDossier.getStatut())) {
            DocumentRoutingService routingService = SSServiceLocator.getDocumentRoutingService();
            FeuilleRoute oldRoute = routingService.getDocumentRouteForDossier(session, dossierDoc.getId());
            DocumentModel oldRouteDoc = oldRoute.getDocument();

            // Récupère le modèle par défaut pour les données du dossier
            FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
            FeuilleRoute routeModel = feuilleRouteModelService.getDefaultRoute(
                session,
                newDossier.getTypeActe(),
                newDossier.getMinistereResp(),
                newDossier.getDirectionResp()
            );

            final EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
            dossierDistributionService.substituerFeuilleRoute(
                session,
                dossierDoc,
                oldRouteDoc,
                routeModel.getDocument(),
                STVocabularyConstants.FEUILLE_ROUTE_TYPE_CREATION_SUBSTITUTION
            );
        }

        return typeActeUpdated;
    }

    @Override
    public void publierDossier(final Dossier dossier, final CoreSession session, final boolean validateCurrentSteps) {
        final DossierSignaleService dsService = SolonEpgServiceLocator.getDossierSignaleService();
        final EpgDossierDistributionService dds = SolonEpgServiceLocator.getDossierDistributionService();
        final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();

        DocumentModel dossierDoc = dossier.getDocument();
        SolonEpgServiceLocator.getDossierService().deleteFeuilleRouteStepToCome(session, dossier);

        if (validateCurrentSteps) {
            corbeilleService
                .findDossierLink(session, dossierDoc.getId())
                .forEach(dossierLinkDoc -> dds.validerEtape(session, dossierDoc, dossierLinkDoc));
        }

        dossier.setStatut(VocabularyConstants.STATUT_PUBLIE);
        dossier.save(session);

        // mise a jour actvite normative
        SolonEpgServiceLocator
            .getActiviteNormativeService()
            .setPublicationInfo(dossier.getDocument().getAdapter(RetourDila.class), session);

        // Met à jour les droits d'indexation du dossier
        initDossierIndexationAcl(session, dossier);

        // Vérification si le dossier est un dossier signalé
        final String query =
            "SELECT * FROM DossiersSignales Where ds:dossiersIds LIKE '" +
            dossierDoc.getId() +
            "'  and ecm:isProxy = 0 ";
        final List<String> dossiersSignalesIds = QueryUtils.doQueryForIds(session, query);
        if (!dossiersSignalesIds.isEmpty()) {
            // Il faut retirer le dossier publié des dossiers signalés
            for (final String id : dossiersSignalesIds) {
                final DocumentModel dossierSignaleDoc = session.getDocument(new IdRef(id));
                dsService.retirer(session, dossierDoc, dossierSignaleDoc);
            }
        }
    }

    @Override
    public List<DocumentModel> getDossierRattacheToMinistereOrDirection(
        final CoreSession session,
        final String nodeId,
        final boolean isDirection
    ) {
        // récupère les dossiers à réattribuer
        final StringBuilder query = new StringBuilder();
        query.append("SELECT d.ecm:uuid as id FROM ");
        query.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
        query.append(" as d WHERE ");
        if (isDirection) {
            query.append("d.dos:");
            query.append(DossierSolonEpgConstants.DOSSIER_DIRECTION_ATTACHE_PROPERTY);
            query.append(" = ?");
        } else {
            query.append("d.dos:");
            query.append(DossierSolonEpgConstants.DOSSIER_MINISTERE_ATTACHE_PROPERTY);
            query.append(" = ?");
        }

        final Object[] params = new Object[] { nodeId };
        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            query.toString(),
            params
        );
    }

    @Override
    public Dossier findDossierFromNumeroNOR(CoreSession session, String numeroNOR) {
        if (StringUtils.isBlank(numeroNOR)) {
            return null;
        }
        CoreSessionUtil.assertSessionNotNull(session);

        final StringBuilder queryBuilder = new StringBuilder(" SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> listDoc = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            queryBuilder.toString(),
            new Object[] { numeroNOR },
            1,
            0
        );
        if (!listDoc.isEmpty()) {
            return listDoc.get(0).getAdapter(Dossier.class);
        }

        return null;
    }

    @Override
    public Dossier findDossierFromIdDossier(final CoreSession session, final String idDossier) {
        if (StringUtils.isBlank(idDossier)) {
            return null;
        }
        CoreSessionUtil.assertSessionNotNull(session);

        final StringBuilder queryBuilder = new StringBuilder(" SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_ID_DOSSIER_PROPERTY);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> listDoc = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            queryBuilder.toString(),
            new Object[] { idDossier },
            1,
            0
        );
        if (!listDoc.isEmpty()) {
            return listDoc.get(0).getAdapter(Dossier.class);
        }

        return null;
    }

    @Override
    public DocumentModel findDossierDocFromIdDossier(final CoreSession session, final String idDossier) {
        if (StringUtils.isBlank(idDossier)) {
            return null;
        }
        CoreSessionUtil.assertSessionNotNull(session);

        final StringBuilder queryBuilder = new StringBuilder(" SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_ID_DOSSIER_PROPERTY);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> listDoc = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            queryBuilder.toString(),
            new Object[] { idDossier },
            1,
            0
        );
        if (!listDoc.isEmpty()) {
            return listDoc.get(0);
        }

        return null;
    }

    @Override
    public List<DocumentModel> findDossiersFromIdsDossier(final CoreSession session, final List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        CoreSessionUtil.assertSessionNotNull(session);

        String[] arrayIds = ids.toArray(new String[ids.size()]);

        final StringBuilder queryBuilder = new StringBuilder(" SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_ID_DOSSIER_PROPERTY);
        queryBuilder.append(" IN (" + StringHelper.join(arrayIds, ",", "'") + ") ");

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            queryBuilder.toString(),
            new Object[] {}
        );
    }

    @Override
    public List<String> findDossierIdsFromDirectionResp(CoreSession session, OrganigrammeNode node) {
        return findDossierIdsFromNode(session, node, DossierSolonEpgConstants.DOSSIER_DIRECTION_RESPONSABLE_XPATH);
    }

    @Override
    public List<String> findDossierIdsFromMinistereResp(CoreSession session, OrganigrammeNode node) {
        return findDossierIdsFromNode(session, node, DossierSolonEpgConstants.DOSSIER_MINISTERE_RESPONSABLE_XPATH);
    }

    @Override
    public List<String> findDossierIdsFromDirectionAttache(CoreSession session, OrganigrammeNode node) {
        return findDossierIdsFromNode(session, node, DossierSolonEpgConstants.DOSSIER_DIRECTION_ATTACHE_XPATH);
    }

    @Override
    public List<String> findDossierIdsFromMinistereAttache(CoreSession session, OrganigrammeNode node) {
        return findDossierIdsFromNode(session, node, DossierSolonEpgConstants.DOSSIER_MINISTERE_ATTACHE_XPATH);
    }

    private List<String> findDossierIdsFromNode(CoreSession session, OrganigrammeNode node, String xpathProperty) {
        if (node == null) {
            return Collections.emptyList();
        }
        CoreSessionUtil.assertSessionNotNull(session);

        String query = String.join(
            "",
            "SELECT d.ecm:uuid as id FROM ",
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            " as d WHERE d.",
            xpathProperty,
            " = ? AND d.dos:deleted = 0"
        );

        return QueryUtils.doUFNXQLQueryForIdsList(session, query, new Object[] { node.getId() });
    }

    @Override
    public DocumentModel findFicheLoiDocumentFromMGPP(final CoreSession session, final String idDossier) {
        synchronized (this) {
            final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
            queryBuilder.append("FicheLoi ").append(" as d WHERE d.floi:idDossier").append(" = ? ");

            final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
                session,
                "FicheLoi",
                queryBuilder.toString(),
                new Object[] { idDossier }
            );
            if (docs == null || docs.isEmpty()) {
                return null;
            } else if (docs.size() > 1) {
                throw new NuxeoException("Plusieurs fiches de loi trouvées pour " + idDossier + ".");
            } else {
                return docs.get(0);
            }
        }
    }

    @Override
    public void cloreDossierUnrestricted(final CoreSession session, final Dossier dossier) {
        if (TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE.equals(dossier.getTypeActe())) {
            final EpgDossierDistributionService dds = SolonEpgServiceLocator.getDossierDistributionService();
            final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
            final JournalService journalService = STServiceLocator.getJournalService();

            final DocumentModel dossierDoc = dossier.getDocument();

            new UnrestrictedSessionRunner(session) {

                @Override
                public void run() {
                    deleteFeuilleRouteStepToCome(session, dossier);
                    final List<DocumentModel> dossierLinkDocList = corbeilleService.findDossierLink(
                        session,
                        dossierDoc.getId()
                    );
                    for (final DocumentModel dossierLinkDoc : dossierLinkDocList) {
                        dds.validerEtape(session, dossierDoc, dossierLinkDoc);
                    }
                    dossier.setStatut(VocabularyConstants.STATUT_CLOS);
                    dossier.save(session);
                    session.save();
                }
            }
            .runUnrestricted();

            journalService.journaliserActionAdministration(
                session,
                dossierDoc,
                SolonEpgEventConstant.DOSSIER_STATUT_CHANGED,
                SolonEpgEventConstant.STATUT_DOSSIER_CLOS_COMMENT
            );
        }
    }

    /**
     * Cette fonction est ajoutée pour eviter la duplication des numeros des nor après migration REF:Taches #8051:Mantis
     * DILA 0150711: [EPG] Migration des NOR - existence du dossier cible
     *
     * @param availableNorList list of available NOR in the table DOSSIER_SOLON_EPG
     * @param numeroNor        to be check
     * @return fixed numeroNor
     */
    private String checkUnicity(NORService norService, CoreSession session, String numeroNor) {
        String newNumeroNor = numeroNor;
        final boolean exist = norService.checkNorUnicity(session, numeroNor);
        if (exist) {
            LOGGER.warn(null, EpgLogEnumImpl.ANO_NOR_MULTI_TEC, "Le NOR :" + numeroNor + " n'est pas unique");
        }
        return newNumeroNor;
    }

    /**
     * Recuperation du persistence provider
     *
     * @return
     */
    private static PersistenceProvider getOrCreatePersistenceProvider() {
        if (persistenceProvider == null) {
            synchronized (DossierServiceImpl.class) {
                if (persistenceProvider == null) {
                    activatePersistenceProvider();
                }
            }
        }
        return persistenceProvider;
    }

    private static void activatePersistenceProvider() {
        final Thread thread = Thread.currentThread();
        final ClassLoader last = thread.getContextClassLoader();
        try {
            thread.setContextClassLoader(PersistenceProvider.class.getClassLoader());
            final PersistenceProviderFactory persistenceProviderFactory = ServiceUtil.getRequiredService(
                PersistenceProviderFactory.class
            );
            persistenceProvider = persistenceProviderFactory.newProvider("SWMIGRATIONLOGGER");
            persistenceProvider.openPersistenceUnit();
        } finally {
            thread.setContextClassLoader(last);
        }
    }

    // Méthode pour inscrire NOR et Id dossier dans table ID_NOR_DOSSIERS_SUPPRIMES
    @Override
    public void saveNorIdDossierSupprime(final String nor, final String id) {
        getOrCreatePersistenceProvider()
            .run(
                true,
                new RunVoid() {

                    @Override
                    public void runWith(EntityManager entityManager) {
                        final StringBuilder query = new StringBuilder(
                            "INSERT INTO ID_NOR_DOSSIERS_SUPPRIMES (ID, NOR) VALUES ('" +
                            id +
                            "', '" +
                            nor.toUpperCase() +
                            "')"
                        );
                        Query queryType = entityManager.createNativeQuery(query.toString());
                        queryType.executeUpdate();
                    }
                }
            );
    }

    @Override
    public String getNorDossierSupprime(final String id) {
        return getOrCreatePersistenceProvider()
            .run(
                true,
                new RunCallback<String>() {

                    @Override
                    public String runWith(EntityManager entityManager) {
                        final StringBuilder query = new StringBuilder(
                            "SELECT NOR FROM ID_NOR_DOSSIERS_SUPPRIMES WHERE ID = '" + id + "'"
                        );
                        Query queryType = entityManager.createNativeQuery(query.toString());
                        return (String) queryType.getSingleResult();
                    }
                }
            );
    }

    @Override
    public List<String> getIdsDossiersSupprimes(final String nor) {
        return getOrCreatePersistenceProvider()
            .run(
                true,
                new RunCallback<List<String>>() {

                    @Override
                    @SuppressWarnings("unchecked")
                    public List<String> runWith(EntityManager entityManager) {
                        String norFormatte = nor.replace("*", "%");
                        final StringBuilder query = new StringBuilder(
                            "SELECT ID FROM ID_NOR_DOSSIERS_SUPPRIMES WHERE NOR LIKE '" + norFormatte + "'"
                        );
                        Query queryType = entityManager.createNativeQuery(query.toString());
                        return queryType.getResultList();
                    }
                }
            );
    }

    @Override
    public List<DocumentModel> findDossiersFromIdExtractionLot(
        CoreSession session,
        Integer idExtractionLot,
        Integer limit
    ) {
        if (idExtractionLot == null) {
            return new ArrayList<>();
        }

        CoreSessionUtil.assertSessionNotNull(session);

        final StringBuilder queryBuilder = new StringBuilder(" SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(DossierSolonEpgConstants.DOSSIER_ID_EXTRACTION_LOT);
        queryBuilder.append(" = " + idExtractionLot);
        queryBuilder.append(" ORDER BY d.ecm:uuid "); // Pour le déterminisme
        if (limit != null) {
            queryBuilder.append(" LIMIT " + limit + " OFFSET 0");
        }

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            queryBuilder.toString(),
            new Object[] {}
        );
    }

    @Override
    public boolean isDossierReprise(Dossier dossier) {
        return dossier.getIndexationRubrique().contains(DossierSolonEpgConstants.RUBRIQUE_REPRISE);
    }

    @Override
    public void removeEditingFlagOnFiles(CoreSession session, DocumentModel dossierDoc) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT f.ecm:uuid as id FROM ");
        queryBuilder.append(DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_TYPE);
        queryBuilder.append(" as f WHERE f.");
        queryBuilder.append(DossierSolonEpgConstants.FILE_SOLON_EPG_PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(DossierSolonEpgConstants.FILE_SOLON_EPG_RELATED_DOCUMENT_PROPERTY);
        queryBuilder.append(" = ?");
        queryBuilder.append(" AND f.");
        queryBuilder.append(DossierSolonEpgConstants.FILE_SOLON_EPG_PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(DossierSolonEpgConstants.FILE_SOLON_EPG_EDITING_PROPERTY);
        queryBuilder.append("=1");
        queryBuilder.append(" AND f.ecm:isVersion=0");

        List<String> fileIds = QueryUtils.doUFNXQLQueryForIdsList(
            session,
            queryBuilder.toString(),
            new String[] { dossierDoc.getId() }
        );

        DocumentModelList documents = session.getDocuments(
            fileIds,
            new PrefetchInfo(DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA)
        );
        documents.stream().map(d -> d.getAdapter(FileSolonEpg.class)).forEach(f -> f.setEditing(false, true, true));

        session.saveDocuments(documents.toArray(new DocumentModel[documents.size()]));
    }

    @Override
    public void deleteFeuilleRouteStepToCome(CoreSession session, Dossier dossier) {
        final SSJournalService journalService = SSServiceLocator.getSSJournalService();

        if (dossier.getLastDocumentRoute() != null) {
            final DocumentModel route = session.getDocument(new IdRef(dossier.getLastDocumentRoute()));
            FeuilleRouteElement feuilleRouteElement = route.getAdapter(FeuilleRouteElement.class);
            SSFeuilleRoute feuilleRoute = feuilleRouteElement.getFeuilleRoute(session);

            Predicate<DocumentModel> isRouteStep = d -> SSConstant.ROUTE_STEP_DOCUMENT_TYPE.equals(d.getType());
            Predicate<DocumentModel> isReady = d ->
                FeuilleRouteElement.ElementLifeCycleState.ready.name().equals(d.getCurrentLifeCycleState());
            final List<DocumentModel> stepsToRemove = SSServiceLocator
                .getDocumentRoutingService()
                .getFeuilleRouteElements(feuilleRoute, session)
                .stream()
                .map(RouteTableElement::getDocument)
                // On supprime les étapes à venir (non validées, annulées ou passées)
                .filter(isRouteStep.and(isReady))
                .collect(Collectors.toList());
            stepsToRemove
                .stream()
                .map(SSRouteStep::adapt)
                .forEach(
                    step ->
                        journalService.journaliserActionEtapeFDR(
                            session,
                            step,
                            dossier.getDocument(),
                            STEventConstant.EVENT_FEUILLE_ROUTE_STEP_DELETE,
                            STEventConstant.COMMENT_FEUILLE_ROUTE_STEP_DELETE
                        )
                );
            stepsToRemove.forEach(s -> session.followTransition(s, TO_DELETE_TRANSITION));
            if (!stepsToRemove.isEmpty()) {
                // flush updated steps to update route
                session.save();
            }
        }
    }

    private void setStepBackToReady(DocumentModel step, CoreSession session) {
        step.getAdapter(FeuilleRouteElement.class).backToReady(session);
    }

    @Override
    public long countAllDossiersSuppressionConsultation(CoreSession session) {
        PageProvider<EpgDossierListingDTO> pageProvider = DossierPageProviderHelper.getPageProvider(
            SUPPRESSION_CONSULTATION_PROVIDER_NAME,
            null,
            1,
            0,
            session,
            getSuppressionConsultationProviderParameter(session)
        );
        pageProvider.getCurrentPage();
        return pageProvider.getResultsCount();
    }

    @Override
    public List<EpgDossierListingDTO> getAllDossiersSuppressionConsultation(CoreSession session) {
        PageProvider<EpgDossierListingDTO> pageProvider = DossierPageProviderHelper.getPageProvider(
            SUPPRESSION_CONSULTATION_PROVIDER_NAME,
            null,
            0,
            0,
            session,
            getSuppressionConsultationProviderParameter(session)
        );
        return pageProvider.getCurrentPage();
    }

    private static Object[] getSuppressionConsultationProviderParameter(CoreSession session) {
        String candidat = null;

        NuxeoPrincipal principal = session.getPrincipal();
        if (PermissionHelper.isAdminFonctionnel(principal)) {
            candidat = VocabularyConstants.CANDIDAT_ELIMINATION_ADMIN_FONCTIONNEL;
        } else if (PermissionHelper.isAdminMinisteriel(principal)) {
            candidat = VocabularyConstants.CANDIDAT_ELIMINATION_ADMIN_MINISTERIEL;
        }

        return new Object[] { candidat };
    }

    @Override
    public long countAllDossiersSuppressionSuivi(CoreSession session) {
        PageProvider<EpgDossierListingDTO> pageProvider = DossierPageProviderHelper.getPageProvider(
            SUPPRESSION_SUIVI_PROVIDER_NAME,
            null,
            1,
            0,
            session
        );
        pageProvider.getCurrentPage();
        return pageProvider.getResultsCount();
    }

    @Override
    public List<EpgDossierListingDTO> getAllDossiersSuppressionSuivi(CoreSession session) {
        PageProvider<EpgDossierListingDTO> pageProvider = DossierPageProviderHelper.getPageProvider(
            SUPPRESSION_SUIVI_PROVIDER_NAME,
            null,
            0,
            0,
            session
        );
        return pageProvider.getCurrentPage();
    }

    @Override
    public long countAllDossiersAbandon(CoreSession session) {
        PageProvider<EpgDossierListingDTO> pageProvider = DossierPageProviderHelper.getPageProvider(
            ABANDON_PROVIDER_NAME,
            null,
            1,
            0,
            session,
            getAbandonProviderParameter(session)
        );
        pageProvider.getCurrentPage();
        return pageProvider.getResultsCount();
    }

    @Override
    public List<EpgDossierListingDTO> getAllDossiersAbandon(CoreSession session) {
        PageProvider<EpgDossierListingDTO> pageProvider = DossierPageProviderHelper.getPageProvider(
            ABANDON_PROVIDER_NAME,
            null,
            0,
            0,
            session,
            getAbandonProviderParameter(session)
        );
        return pageProvider.getCurrentPage();
    }

    private static Object[] getAbandonProviderParameter(CoreSession session) {
        return new Object[] { SSServiceLocator.getSSMinisteresService().getMinisteresQuery(session) };
    }

    @Override
    public void deletedDossiers(CoreSession session, List<String> idDossiers) {
        CoreInstance.doPrivileged(
            session,
            privilegedSession -> {
                DocumentModelList dossierDocs = QueryHelper.getDocuments(privilegedSession, idDossiers);
                dossierDocs.stream().map(Dossier::adapt).forEach(d -> d.setIsDossierDeleted(true));
                privilegedSession.saveDocuments(dossierDocs.toArray(new DocumentModel[0]));
                getWorkManager().schedule(new DeleteDossiersWork(idDossiers, session.getPrincipal().getName()), true);
            }
        );
    }

    @Override
    public void removeDossierReferences(CoreSession session, String id) {
        // retirer le dossier des favoris de consultation et derniers dossiers consultés
        EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
        String userworkspacePath = STServiceLocator
            .getUserWorkspaceService()
            .getCurrentUserPersonalWorkspace(session)
            .getPathAsString();

        Set<String> dossierToRemove = Collections.singleton(id);
        espaceRechercheService.removeDossierFromDerniersResultatsConsultes(session, userworkspacePath, dossierToRemove);
        espaceRechercheService.removeDossierFromFavorisConsultation(session, userworkspacePath, dossierToRemove);
        SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .removeDossierFromListDerniersDossierIntervention(session, id);

        try {
            ServiceUtil.getRequiredService(ElasticRequeteurService.class).deleteDocument(id);
        } catch (IOException e) {
            LOGGER.error(STLogEnumImpl.FAIL_DEL_DOC_TEC, e, id);
        }
    }
}
