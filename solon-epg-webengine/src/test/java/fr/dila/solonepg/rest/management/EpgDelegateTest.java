package fr.dila.solonepg.rest.management;

import static fr.dila.st.api.constant.STWebserviceConstant.ATTRIBUER_NOR;
import static fr.dila.st.api.constant.STWebserviceConstant.CHERCHER_DOSSIER;
import static fr.dila.st.api.constant.STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER;
import static fr.dila.st.api.constant.STWebserviceConstant.CREER_DOSSIER_AVIS;
import static fr.dila.st.api.constant.STWebserviceConstant.CREER_DOSSIER_DECRET_PR_IND;
import static fr.dila.st.api.constant.STWebserviceConstant.CREER_DOSSIER_INFOS_PARL;
import static fr.dila.st.api.constant.STWebserviceConstant.DONNER_AVIS_CE;
import static fr.dila.st.api.constant.STWebserviceConstant.MODIFIER_DOSSIER_AVIS;
import static fr.dila.st.api.constant.STWebserviceConstant.MODIFIER_DOSSIER_CE;
import static fr.dila.st.api.constant.STWebserviceConstant.MODIFIER_DOSSIER_DECRET_PR_IND;
import static fr.dila.st.api.constant.STWebserviceConstant.MODIFIER_DOSSIER_INFOS_PARL;
import static fr.sword.xsd.commons.TraitementStatut.KO;
import static fr.sword.xsd.commons.TraitementStatut.OK;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.mailbox.SolonEpgMailbox;
import fr.dila.solonepg.api.parapheur.Parapheur;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.api.service.EpgDocumentRoutingService;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.core.spe.WsUtil;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.solonepg.rest.api.EpgDelegate;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.constant.SSParapheurConstants;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.jeton.JetonDoc;
import fr.dila.st.api.jeton.JetonServiceDto;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.JetonService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.core.organigramme.EntiteNodeImpl;
import fr.dila.st.core.organigramme.PosteNodeImpl;
import fr.dila.st.core.organigramme.UniteStructurelleNodeImpl;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import fr.sword.xsd.solon.epg.ActeType;
import fr.sword.xsd.solon.epg.Amnistie;
import fr.sword.xsd.solon.epg.AttribuerNorRequest;
import fr.sword.xsd.solon.epg.AttribuerNorResponse;
import fr.sword.xsd.solon.epg.Avis;
import fr.sword.xsd.solon.epg.ChercherDossierRequest;
import fr.sword.xsd.solon.epg.ChercherDossierResponse;
import fr.sword.xsd.solon.epg.ChercherModificationDossierRequest;
import fr.sword.xsd.solon.epg.ChercherModificationDossierResponse;
import fr.sword.xsd.solon.epg.CodeErreur;
import fr.sword.xsd.solon.epg.CreerDossierRequest;
import fr.sword.xsd.solon.epg.CreerDossierResponse;
import fr.sword.xsd.solon.epg.DecretPRIndividuel;
import fr.sword.xsd.solon.epg.DonnerAvisCERequest;
import fr.sword.xsd.solon.epg.DonnerAvisCEResponse;
import fr.sword.xsd.solon.epg.DossierEpg;
import fr.sword.xsd.solon.epg.DossierEpgWithFile;
import fr.sword.xsd.solon.epg.InformationPublication;
import fr.sword.xsd.solon.epg.InformationsParlementaires;
import fr.sword.xsd.solon.epg.ModifierDossierCERequest;
import fr.sword.xsd.solon.epg.ModifierDossierCEResponse;
import fr.sword.xsd.solon.epg.ModifierDossierRequest;
import fr.sword.xsd.solon.epg.ModifierDossierResponse;
import fr.sword.xsd.solon.epg.SectionCe;
import fr.sword.xsd.solon.epg.TypeModification;
import fr.sword.xsd.solon.epg.TypeValidationCe;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import javax.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.uidgen.DummyUIDSequencerImpl;
import org.nuxeo.ecm.core.uidgen.UIDGeneratorService;
import org.nuxeo.ecm.core.uidgen.UIDSequencer;
import org.nuxeo.runtime.mockito.RuntimeService;

public class EpgDelegateTest extends AbstractEPGTest {
    @Inject
    private NORService norService;

    @Mock
    private SSPrincipal principal;

    @Mock
    @RuntimeService
    private EpgOrganigrammeService epgOrganigrammeService;

    @Mock
    @RuntimeService
    private STMinisteresService stMinisteresService;

    @Mock
    @RuntimeService
    private STUsAndDirectionService stUsAndDirectionService;

    @Mock
    @RuntimeService
    private STPostesService stPostesService;

    @Mock
    @RuntimeService
    private MailboxPosteService mailboxPosteService;

    @Mock
    @RuntimeService
    private UIDGeneratorService uidGeneratorService;

    @Mock
    @RuntimeService
    private EpgDossierDistributionService dossierDistributionService;

    @Mock
    @RuntimeService
    private JetonService jetonService;

    @Mock
    @RuntimeService
    private EPGFeuilleRouteService epgFeuilleRouteService;

    @Mock
    @RuntimeService
    private EpgCorbeilleService corbeilleService;

    @Mock
    @RuntimeService
    private EpgDocumentRoutingService documentRoutingService;

    @Mock
    @RuntimeService
    private STParametreService parametreService;

    private EpgDelegate delegate;
    private CloseableCoreSession session;
    private EntiteNode entiteNode;
    private UniteStructurelleNode directionNode;
    private PosteNode posteNode;
    private SolonEpgMailbox mailbox;
    private UIDSequencer sequencer;

    private final String ID_ENTITE = "60001234";
    private final String ID_DIRECTION = "60003456";
    private final String ID_POSTE = "60005678";
    private final String CODE_ENTITE = "PRM";
    private final String CODE_DIRECTION = "X";
    private final String LABEL_ENTITE = "Premier Ministre";
    private final String LABEL_DIRECTION = "Secrétariat Général du Gouvernement (SGG)";
    private final String LABEL_POSTE = "BDC Premier Ministre";
    private final String YEAR = Year.now().format(DateTimeFormatter.ofPattern("yy"));
    private final String SEQUENCER_NAME = "SOLON_EPG_NOR_SEQUENCER_" + YEAR;

    @Before
    public void setUp() throws Exception {
        session = coreFeature.openCoreSession(principal);
        delegate = new EpgDelegateImpl(session);

        entiteNode = new EntiteNodeImpl();
        directionNode = new UniteStructurelleNodeImpl();
        posteNode = new PosteNodeImpl();
        mailbox = createDocument(SolonEpgConstant.SOLON_EPG_MAILBOX_TYPE, "mailbox0").getAdapter(SolonEpgMailbox.class);
        sequencer = new DummyUIDSequencerImpl();

        entiteNode.setId(ID_ENTITE);
        entiteNode.setLabel(LABEL_ENTITE);
        directionNode.setId(ID_DIRECTION);
        directionNode.setLabel(LABEL_DIRECTION);
        posteNode.setId(ID_POSTE);
        posteNode.setLabel(LABEL_POSTE);
        sequencer.initSequence(SEQUENCER_NAME, 13579L);

        when(principal.getName()).thenReturn("0");
        when(epgOrganigrammeService.getMinistereFromNor(CODE_ENTITE)).thenReturn(entiteNode);
        when(epgOrganigrammeService.getDirectionFromMinistereAndNor(entiteNode, CODE_DIRECTION))
            .thenReturn(directionNode);
        when(stMinisteresService.getEntiteNode(ID_ENTITE)).thenReturn(entiteNode);
        when(stUsAndDirectionService.getUniteStructurelleNode(ID_DIRECTION)).thenReturn(directionNode);
        when(uidGeneratorService.getSequencer()).thenReturn(sequencer);
    }

    @After
    public void tearDown() {
        session.close();
    }

    @Test
    public void testAttribuerNor() throws Exception {
        AttribuerNorRequest request = new AttribuerNorRequest();
        AttribuerNorResponse response;

        String expectedNOR = CODE_ENTITE + CODE_DIRECTION + YEAR + sequencer.getNextLong(SEQUENCER_NAME) + "L";

        when(principal.isAdministrator()).thenReturn(true);
        when(dossierDistributionService.startDefaultRoute(any(CoreSession.class), any(Dossier.class), anyString()))
            .thenReturn(null);

        request.setCodeEntite("AAA");
        request.setCodeDirection("B");
        request.setTypeActe(ActeType.LOI);
        response = delegate.attribuerNor(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Vous n'avez pas les droits pour accéder à ce service !", response.getMessageErreur());

        when(principal.getGroups()).thenReturn(singletonList(ATTRIBUER_NOR));

        response = delegate.attribuerNor(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals(CodeErreur.ENTITE_INCONNUE, response.getCodeErreur());

        request.setCodeEntite(CODE_ENTITE);
        response = delegate.attribuerNor(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals(CodeErreur.DROIT_ACCESS_INSUFFISANT, response.getCodeErreur());
        Assert.assertEquals(
            "Vous n'avez pas le droit de créer un dossier dans ce ministère !",
            response.getMessageErreur()
        );

        when(principal.getMinistereIdSet()).thenReturn(singleton(ID_ENTITE));

        response = delegate.attribuerNor(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals(CodeErreur.DIRECTION_INCONNUE, response.getCodeErreur());

        request.setCodeDirection(CODE_DIRECTION);
        response = delegate.attribuerNor(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals(CodeErreur.AUTRE, response.getCodeErreur());
        Assertions
            .assertThat(response.getMessageErreur())
            .isEqualTo("Pas de poste identifié pour ce ministère : %s", LABEL_ENTITE);
        when(principal.getPosteIdSet()).thenReturn(singleton(ID_ENTITE));
        when(stPostesService.getPoste(ID_ENTITE)).thenReturn(posteNode);

        response = delegate.attribuerNor(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals(CodeErreur.AUTRE, response.getCodeErreur());
        Assert.assertTrue(response.getMessageErreur().startsWith("La mailbox du poste bdc n'est pas initialisée"));

        when(mailboxPosteService.getMailboxPoste(session, ID_POSTE)).thenReturn(mailbox);

        request.setTypeActe(ActeType.LOI);
        response = delegate.attribuerNor(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertEquals(expectedNOR, response.getNor());
        DocumentModel dossierDoc = norService.getDossierFromNOR(session, response.getNor());
        Assert.assertNotNull("Aucun dossier portant le numéro NOR " + expectedNOR, dossierDoc);
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        Assert.assertEquals(ID_ENTITE, dossier.getMinistereResp());
        Assert.assertEquals(ID_DIRECTION, dossier.getDirectionResp());
        Assert.assertEquals(WsUtil.getDossierActeTypeFromWsTypeActe(request.getTypeActe()), dossier.getTypeActe());
    }

    @Test
    public void testChercherDossier() throws Exception {
        ChercherDossierRequest request = new ChercherDossierRequest();
        ChercherDossierResponse response;
        long numeroJeton = 123;
        String nor1 = "ECOX2000001A";
        String nor2 = "TREK2000002A";
        Dossier dossier = createDossier();

        when(principal.isAdministrator()).thenReturn(true);

        dossier.setNumeroNor(nor1);
        session.saveDocument(dossier.getDocument());
        session.save();

        response = delegate.chercherDossierEpg(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Vous n'avez pas les droits pour accéder à ce service !", response.getMessageErreur());

        when(principal.getGroups()).thenReturn(singletonList(CHERCHER_DOSSIER));

        response = delegate.chercherDossierEpg(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("aucun numero NOR ni jeton passé en paramètre", response.getMessageErreur());
        Assert.assertTrue(response.isDernierEnvoi());
        Assert.assertEquals("-1", response.getJeton());

        request.setJeton(Long.toString(numeroJeton));
        response = delegate.chercherDossierEpg(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertTrue(response.isDernierEnvoi());
        Assert.assertEquals("-1", response.getJeton());
        Assert.assertEquals(
            "Erreur : Impossible de récupérer des documents. Veuillez saisir un jeton adéquat.",
            response.getMessageErreur()
        );

        when(jetonService.getDocuments(session, TableReference.MINISTERE_CE, numeroJeton, CHERCHER_DOSSIER))
            .thenReturn(new JetonServiceDto(numeroJeton + 1, false, null));

        response = delegate.chercherDossierEpg(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertFalse(response.isDernierEnvoi());
        Assert.assertEquals(Long.toString(numeroJeton + 1), response.getJeton());

        when(jetonService.getDocuments(session, TableReference.MINISTERE_CE, numeroJeton, CHERCHER_DOSSIER))
            .thenReturn(new JetonServiceDto(numeroJeton + 1, false, new ArrayList<>()));

        response = delegate.chercherDossierEpg(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertFalse(response.isDernierEnvoi());
        Assert.assertEquals(Long.toString(numeroJeton + 1), response.getJeton());
        Assert.assertTrue(response.getDossier().isEmpty());

        when(jetonService.getDocuments(session, TableReference.MINISTERE_CE, numeroJeton, CHERCHER_DOSSIER))
            .thenReturn(new JetonServiceDto(numeroJeton + 1, true, singletonList(dossier.getDocument())));

        response = delegate.chercherDossierEpg(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertTrue(response.isDernierEnvoi());
        Assert.assertEquals(Long.toString(numeroJeton + 1), response.getJeton());
        Assert.assertFalse(response.getDossier().isEmpty());
        Assert.assertEquals(nor1, response.getDossier().get(0).getDossierEpg().getAmnistie().getNor());

        request.setJeton(null);
        request.getNor().add(nor2);
        response = delegate.chercherDossierEpg(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertTrue(response.isDernierEnvoi());
        Assert.assertEquals("Aucun résultat retourné par la requête", response.getMessageErreur());

        request.getNor().add(nor1);
        response = delegate.chercherDossierEpg(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertEquals("-1", response.getJeton());
        Assert.assertTrue(response.isDernierEnvoi());
        Assert.assertEquals(nor1, response.getDossier().get(0).getDossierEpg().getAmnistie().getNor());
        Assert.assertEquals(nor2, response.getErreur().get(0).getNor());
    }

    @Test
    public void testDonnerAvisCE() throws Exception {
        DonnerAvisCERequest request = new DonnerAvisCERequest();
        DonnerAvisCEResponse response;
        Dossier dossier = createDossier();
        SectionCe sectionCE = new SectionCe();
        DocumentModel dossierLinkDoc = createDocument(DossierSolonEpgConstants.DOSSIER_LINK_DOCUMENT_TYPE, "0000");
        DocumentModel routeStepDoc = createDocument(SSConstant.ROUTE_STEP_DOCUMENT_TYPE, "1111");

        when(principal.isAdministrator()).thenReturn(true);
        session.saveDocument(dossier.getDocument());
        dossierLinkDoc.getAdapter(DossierLink.class).setRoutingTaskType(VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE);
        session.saveDocument(dossierLinkDoc);
        routeStepDoc.getAdapter(SSRouteStep.class).setType(VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE);
        routeStepDoc.getAdapter(SSRouteStep.class).setDateFinEtape(Calendar.getInstance());
        session.saveDocument(routeStepDoc);
        session.save();
        sectionCE.setSectionCe("");
        sectionCE.getRapporteur().add("");
        sectionCE.setDateTransmissionSectionCe(null);
        sectionCE.setDateSectionCe(null);
        sectionCE.setDateAgCe(null);
        sectionCE.setNumeroIsa("");

        response = delegate.donnerAvisCE(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Vous n'avez pas les droits pour accéder à ce service !", response.getMessageErreur());

        when(principal.getGroups()).thenReturn(singletonList(DONNER_AVIS_CE));

        response = delegate.donnerAvisCE(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Nor non trouvé", response.getMessageErreur());

        request.setNor(dossier.getNumeroNor());
        response = delegate.donnerAvisCE(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals(
            "Le type d'acte ne peut pas recevoir d'avis du Conseil d'Etat",
            response.getMessageErreur()
        );

        dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CE);
        session.saveDocument(dossier.getDocument());
        session.save();
        response = delegate.donnerAvisCE(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Type de validation de l'étape nulle ou incorrecte", response.getMessageErreur());

        request.setTypeValidationCe(TypeValidationCe.AVIS_RECTIFICATIF);
        response = delegate.donnerAvisCE(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals(
            "Le dossier n'est pas à l'état Lancé ! Il n'est pas possible de donner un avis rectificatif du Conseil d'Etat sur ce dossier.",
            response.getMessageErreur()
        );

        when(epgFeuilleRouteService.findAllSteps(any(), anyString())).thenReturn(Collections.emptyList());
        dossier.setStatut(VocabularyConstants.STATUT_LANCE);
        session.saveDocument(dossier.getDocument());
        session.save();
        response = delegate.donnerAvisCE(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals(
            "Aucun avis du conseil d'Etat n'a été rendu sur ce dossier ! Il n'est pas possible de donner un avis rectificatif.",
            response.getMessageErreur()
        );

        when(epgFeuilleRouteService.findAllSteps(any(), anyString())).thenReturn(singletonList(routeStepDoc));
        request.setSectionCe(sectionCE);
        response = delegate.donnerAvisCE(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertEquals(dossier.getNumeroNor(), response.getNor());

        request.setTypeValidationCe(TypeValidationCe.AVIS_RENDU);
        response = delegate.donnerAvisCE(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals(
            "Le dossier n'est pas à l'étape 'Pour avis CE' ! Il n'est pas possible de donner un avis du Conseil d'Etat sur ce dossier !",
            response.getMessageErreur()
        );

        when(corbeilleService.findDossierLink(session, dossier.getDocument().getId()))
            .thenReturn(singletonList(dossierLinkDoc));
        response = delegate.donnerAvisCE(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertEquals(dossier.getNumeroNor(), response.getNor());

        request.setTypeValidationCe(TypeValidationCe.DESSAISISSEMENT);
        response = delegate.donnerAvisCE(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertEquals(dossier.getNumeroNor(), response.getNor());

        request.setTypeValidationCe(TypeValidationCe.REJET);
        response = delegate.donnerAvisCE(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertEquals(dossier.getNumeroNor(), response.getNor());

        request.setTypeValidationCe(TypeValidationCe.RENVOI);
        response = delegate.donnerAvisCE(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertEquals(dossier.getNumeroNor(), response.getNor());

        request.setTypeValidationCe(TypeValidationCe.RETRAIT);
        response = delegate.donnerAvisCE(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertEquals(dossier.getNumeroNor(), response.getNor());
    }

    @Test
    public void testModifierDossierCE() throws Exception {
        ModifierDossierCERequest request = new ModifierDossierCERequest();
        ModifierDossierCEResponse response;
        Dossier dossier = createDossier();
        SectionCe sectionCE = new SectionCe();
        DocumentModel dossierLinkDoc = createDocument(DossierSolonEpgConstants.DOSSIER_LINK_DOCUMENT_TYPE, "0000");
        DocumentModel routeStepDoc = createDocument(SSConstant.ROUTE_STEP_DOCUMENT_TYPE, "1111");
        String numeroISA = "2222";

        when(principal.isAdministrator()).thenReturn(true);
        dossier.getDocument().getAdapter(ConseilEtat.class).setNumeroISA(numeroISA);
        session.saveDocument(dossier.getDocument());
        dossierLinkDoc.getAdapter(DossierLink.class).setRoutingTaskType(VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE);
        session.saveDocument(dossierLinkDoc);
        routeStepDoc.getAdapter(SSRouteStep.class).setType(VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE);
        routeStepDoc.getAdapter(SSRouteStep.class).setDateFinEtape(Calendar.getInstance());
        session.saveDocument(routeStepDoc);
        session.save();
        sectionCE.setSectionCe("");
        sectionCE.getRapporteur().add("");
        sectionCE.setDateTransmissionSectionCe(null);
        sectionCE.setDateSectionCe(null);
        sectionCE.setDateAgCe(null);
        sectionCE.setNumeroIsa("");

        response = delegate.modifierDossierCE(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Vous n'avez pas les droits pour accéder à ce service !", response.getMessageErreur());

        when(principal.getGroups()).thenReturn(singletonList(MODIFIER_DOSSIER_CE));

        response = delegate.modifierDossierCE(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Le dossier est introuvable", response.getMessageErreur());

        request.setNor(dossier.getNumeroNor());
        response = delegate.modifierDossierCE(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals(
            "Le type d'acte ne peut pas recevoir d'avis du Conseil d'Etat",
            response.getMessageErreur()
        );

        dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CE);
        session.saveDocument(dossier.getDocument());
        session.save();
        request.setSectionCe(sectionCE);
        response = delegate.modifierDossierCE(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertEquals(dossier.getNumeroNor(), response.getNor());

        request.setNor(null);
        request.setNumeroIsa(numeroISA);
        response = delegate.modifierDossierCE(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertEquals(dossier.getNumeroNor(), response.getNor());
    }

    @Test
    public void testCreerDossier() {
        CreerDossierRequest request = new CreerDossierRequest();
        CreerDossierResponse response;

        String expectedNOR;

        response = delegate.creerDossier(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Un champ obligatoire est manquant", response.getMessageErreur());

        request.setTypeActe(ActeType.AVIS);
        request.setCodeEntite("AAA");
        request.setCodeDirection("B");
        response = delegate.creerDossier(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Le ministère indiqué n'existe pas", response.getMessageErreur());

        request.setCodeEntite(CODE_ENTITE);
        response = delegate.creerDossier(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("La direction indiquée n'existe pas", response.getMessageErreur());

        request.setCodeDirection(CODE_DIRECTION);
        response = delegate.creerDossier(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Vous n'avez pas les droits pour accéder à ce service !", response.getMessageErreur());

        when(principal.getGroups())
            .thenReturn(Arrays.asList(CREER_DOSSIER_AVIS, CREER_DOSSIER_DECRET_PR_IND, CREER_DOSSIER_INFOS_PARL));
        response = delegate.creerDossier(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("L'utilisateur n'est rattaché à aucun poste", response.getMessageErreur());

        when(principal.getPosteIdSet()).thenReturn(singleton(ID_POSTE));
        response = delegate.creerDossier(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("La mailbox du poste n'est pas initialisée", response.getMessageErreur());

        when(principal.isAdministrator()).thenReturn(true);
        when(mailboxPosteService.getMailboxPoste(session, ID_POSTE)).thenReturn(mailbox);
        expectedNOR = CODE_ENTITE + CODE_DIRECTION + YEAR + sequencer.getNextLong(SEQUENCER_NAME) + "V";
        response = delegate.creerDossier(request);
        Assert.assertEquals(OK, response.getStatut());
        Avis avis = response.getDossier().getAvis();
        Assert.assertEquals(expectedNOR, avis.getNor());
        Assert.assertEquals(LABEL_ENTITE, avis.getEntiteResponsable());
        Assert.assertEquals(LABEL_DIRECTION, avis.getDirectionResponsable());

        request.setTypeActe(ActeType.DECRET_PR_INDIVIDUEL);
        expectedNOR = CODE_ENTITE + CODE_DIRECTION + YEAR + sequencer.getNextLong(SEQUENCER_NAME) + "D";
        response = delegate.creerDossier(request);
        Assert.assertEquals(OK, response.getStatut());
        DecretPRIndividuel decretPRInd = response.getDossier().getDecretPrInd();
        Assert.assertEquals(expectedNOR, decretPRInd.getNor());
        Assert.assertEquals(LABEL_ENTITE, decretPRInd.getEntiteResponsable());
        Assert.assertEquals(LABEL_DIRECTION, decretPRInd.getDirectionResponsable());

        request.setTypeActe(ActeType.INFORMATIONS_PARLEMENTAIRES);
        expectedNOR = CODE_ENTITE + CODE_DIRECTION + YEAR + sequencer.getNextLong(SEQUENCER_NAME) + "X";
        response = delegate.creerDossier(request);
        Assert.assertEquals(OK, response.getStatut());
        InformationsParlementaires infosParl = response.getDossier().getInformationsParlementaires();
        Assert.assertEquals(expectedNOR, infosParl.getNor());
        Assert.assertEquals(LABEL_ENTITE, infosParl.getEntiteResponsable());
        Assert.assertEquals(LABEL_DIRECTION, infosParl.getDirectionResponsable());
    }

    @Test
    public void testModifierDossier() throws Exception {
        ModifierDossierRequest request = new ModifierDossierRequest();
        ModifierDossierResponse response;

        DossierEpgWithFile dossierEpgWithFile = new DossierEpgWithFile();
        Parapheur parapheur = createDocument(SSParapheurConstants.PARAPHEUR_DOCUMENT_TYPE, "0000")
            .getAdapter(Parapheur.class);
        FeuilleRoute feuilleRoute = createDocument(SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE, "1111")
            .getAdapter(FeuilleRoute.class);
        Dossier dossier1 = createDossier();
        Dossier dossier2 = createDossier();
        Dossier dossier3 = createDossier();
        DocumentModel routeStepDoc1 = createDocument(SSConstant.ROUTE_STEP_DOCUMENT_TYPE, "1111");
        DocumentModel routeStepDoc2 = createDocument(SSConstant.ROUTE_STEP_DOCUMENT_TYPE, "2222");

        when(principal.isAdministrator()).thenReturn(true);
        when(mailboxPosteService.getMailboxPosteIdSetFromPosteIdSet(any())).thenReturn(singleton(mailbox.getId()));
        dossierEpgWithFile.setDossierEpg(new DossierEpg());
        dossierEpgWithFile.getDossierEpg().setAmnistie(new Amnistie());
        dossier1.setNumeroNor("PRMX0000001V");
        dossier1.setTypeActe(TypeActeConstants.TYPE_ACTE_AVIS);
        dossier1.setDocumentParapheurId(parapheur.getId());
        dossier2.setNumeroNor("PRMX0000002D");
        dossier2.setTypeActe(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND);
        dossier2.setDocumentParapheurId(parapheur.getId());
        dossier3.setNumeroNor("PRMX0000003X");
        dossier3.setTypeActe(TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES);
        dossier3.setDocumentParapheurId(parapheur.getId());
        dossier3.setStatut(VocabularyConstants.STATUT_PUBLIE);
        dossier3.setLastDocumentRoute(feuilleRoute.getDocument().getId());
        session.saveDocument(parapheur.getDocument());
        session.saveDocument(feuilleRoute.getDocument());
        session.saveDocument(dossier1.getDocument());
        session.saveDocument(dossier2.getDocument());
        session.saveDocument(dossier3.getDocument());
        routeStepDoc1.getAdapter(SSRouteStep.class).setType(VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION);
        routeStepDoc1.getAdapter(SSRouteStep.class).setDistributionMailboxId(mailbox.getId());
        session.saveDocument(routeStepDoc1);
        session.save();

        request.setDossier(dossierEpgWithFile);
        response = delegate.modifierDossier(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Échec de récupération des données du dossier", response.getMessageErreur());

        dossierEpgWithFile.getDossierEpg().setAmnistie(null);
        dossierEpgWithFile.getDossierEpg().setAvis(new Avis());
        dossierEpgWithFile.getDossierEpg().getAvis().setTypeActe(ActeType.AVIS);
        dossierEpgWithFile.getDossierEpg().getAvis().setNor("PRMX0000004X");
        response = delegate.modifierDossier(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Vous n'avez pas les droits pour accéder à ce service !", response.getMessageErreur());

        when(principal.getGroups())
            .thenReturn(
                Arrays.asList(MODIFIER_DOSSIER_AVIS, MODIFIER_DOSSIER_DECRET_PR_IND, MODIFIER_DOSSIER_INFOS_PARL)
            );
        response = delegate.modifierDossier(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("L'utilisateur n'est rattaché à aucun poste", response.getMessageErreur());

        when(principal.getPosteIdSet()).thenReturn(singleton(ID_POSTE));
        response = delegate.modifierDossier(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals(
            "Le dossier avec le NOR PRMX0000004X n'existe pas, il ne peut pas être modifié",
            response.getMessageErreur()
        );

        dossierEpgWithFile = WsUtil.getDossierEpgWithFileFromDossier(session, dossier1, false);
        dossierEpgWithFile.getDossierEpg().getAvis().setTitreActe("Titre avis");
        dossierEpgWithFile.getDossierEpg().getAvis().setIndexation(null);
        request.setDossier(dossierEpgWithFile);
        response = delegate.modifierDossier(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("L'étape n'est en cours dans aucun poste de l'utilisateur", response.getMessageErreur());

        when(epgFeuilleRouteService.getRunningSteps(any(), anyString())).thenReturn(singletonList(routeStepDoc1));
        response = delegate.modifierDossier(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertTrue(response.getMessageErreur().startsWith("Aucune étape à venir après l'étape en cours. "));

        when(epgFeuilleRouteService.findNextSteps(any(), anyString(), any(), any()))
            .thenReturn(singletonList(routeStepDoc2));
        response = delegate.modifierDossier(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertNotNull(response.getDossier());
        Avis avis = response.getDossier().getAvis();
        Assert.assertEquals("Titre avis", avis.getTitreActe());

        dossierEpgWithFile = WsUtil.getDossierEpgWithFileFromDossier(session, dossier2, false);
        dossierEpgWithFile.getDossierEpg().getDecretPrInd().setTitreActe("Titre décret");
        dossierEpgWithFile.getDossierEpg().getDecretPrInd().setIndexation(null);
        request.setDossier(dossierEpgWithFile);
        response = delegate.modifierDossier(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertNotNull(response.getDossier());
        DecretPRIndividuel decretPRInd = response.getDossier().getDecretPrInd();
        Assert.assertEquals("Titre décret", decretPRInd.getTitreActe());

        dossierEpgWithFile = WsUtil.getDossierEpgWithFileFromDossier(session, dossier3, false);
        dossierEpgWithFile.getDossierEpg().getInformationsParlementaires().setTitreActe("Titre infos parl");
        dossierEpgWithFile.getDossierEpg().getInformationsParlementaires().setEmetteur("Emetteur");
        dossierEpgWithFile.getDossierEpg().getInformationsParlementaires().setRubrique("Rubrique");
        dossierEpgWithFile.getDossierEpg().getInformationsParlementaires().setCommentaire("Commentaire");
        dossierEpgWithFile.getDossierEpg().getInformationsParlementaires().setIdEvenement("EVT_00000");
        dossierEpgWithFile.getDossierEpg().getInformationsParlementaires().setIndexation(null);
        request.setDossier(dossierEpgWithFile);
        response = delegate.modifierDossier(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Le dossier est déjà publié, impossible de le modifier", response.getMessageErreur());

        dossier3.setStatut(VocabularyConstants.STATUT_LANCE);
        session.saveDocument(dossier3.getDocument());
        response = delegate.modifierDossier(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertNotNull(response.getDossier());
        InformationsParlementaires infosParl = response.getDossier().getInformationsParlementaires();
        Assert.assertEquals("Titre infos parl", infosParl.getTitreActe());
        Assert.assertEquals("Emetteur", infosParl.getEmetteur());
        Assert.assertEquals("Rubrique", infosParl.getRubrique());
        Assert.assertEquals("Commentaire", infosParl.getCommentaire());
        Assert.assertEquals("EVT_00000", infosParl.getIdEvenement());

        routeStepDoc1.getAdapter(SSRouteStep.class).setType(VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO);
        routeStepDoc1.putContextData(STConstant.POS_DOC_CONTEXT, 6);
        session.saveDocument(routeStepDoc1);
        when(epgFeuilleRouteService.findNextSteps(any(), anyString(), any(), any())).thenReturn(null);
        doNothing().when(documentRoutingService).addRouteElementToRoute(any(), anyInt(), any(), eq(session));
        response = delegate.modifierDossier(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertNotNull(response.getDossier());
    }

    @Test
    public void testChercherModificationDossierEpg() throws Exception {
        ChercherModificationDossierRequest request = new ChercherModificationDossierRequest();
        ChercherModificationDossierResponse response;

        long numeroJeton = 123;
        String nor = "ECOX2000001A";
        String norReattribution = "PRMX2000001A";
        String numeroISA = "123456";
        Dossier dossier = createDossier();
        DocumentModel jetonDoc = createDocument(STConstant.JETON_DOC_TYPE, "0000");

        when(principal.isAdministrator()).thenReturn(true);
        when(
                parametreService.getParametreValue(
                    session,
                    SolonEpgParametreConstant.ACTIVATION_ENVOI_INFO_PUBLICATION_CE
                )
            )
            .thenReturn("true");

        dossier.setNumeroNor(nor);
        dossier.getDocument().getAdapter(ConseilEtat.class).setNumeroISA(numeroISA);
        dossier.getDocument().getAdapter(RetourDila.class).setTitreOfficiel("Titre officiel");
        dossier.getDocument().getAdapter(RetourDila.class).setNumeroTexteParutionJorf("1");
        dossier.getDocument().getAdapter(RetourDila.class).setPageParutionJorf(2L);
        session.saveDocument(dossier.getDocument());
        jetonDoc.getAdapter(JetonDoc.class).setNumeroJeton(123L);
        jetonDoc.getAdapter(JetonDoc.class).setIdDoc(dossier.getDocument().getId());
        jetonDoc.getAdapter(JetonDoc.class).setTypeWebservice(CHERCHER_MODIFICATION_DOSSIER);
        jetonDoc.getAdapter(JetonDoc.class).setTypeModification(TypeModification.PRIORISATION.toString());
        jetonDoc.getAdapter(JetonDoc.class).setIdsComplementaires(singletonList("1"));
        session.saveDocument(jetonDoc);
        session.save();

        response = delegate.chercherModificationDossierEpg(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("Vous n'avez pas les droits pour accéder à ce service !", response.getMessageErreur());

        when(principal.getGroups()).thenReturn(singletonList(CHERCHER_MODIFICATION_DOSSIER));

        response = delegate.chercherModificationDossierEpg(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertEquals("aucun numero NOR ni jeton ni numéro ISA passé en paramètre", response.getMessageErreur());
        Assert.assertTrue(response.isDernierEnvoi());
        Assert.assertEquals("-1", response.getJeton());

        request.setJeton(Long.toString(numeroJeton));
        response = delegate.chercherModificationDossierEpg(request);
        Assert.assertEquals(KO, response.getStatut());
        Assert.assertTrue(response.isDernierEnvoi());
        Assert.assertEquals("-1", response.getJeton());
        Assert.assertEquals(
            "Erreur : Impossible de récupérer des documents. Veuillez saisir un jeton adéquat.",
            response.getMessageErreur()
        );

        JetonServiceDto jetonServiceDto = new JetonServiceDto(
            numeroJeton + 1,
            true,
            singletonList(dossier.getDocument())
        );
        jetonServiceDto.setJetonDocDocList(singletonList(jetonDoc));
        when(
                jetonService.getDocuments(
                    session,
                    TableReference.MINISTERE_CE,
                    numeroJeton,
                    CHERCHER_MODIFICATION_DOSSIER
                )
            )
            .thenReturn(jetonServiceDto);

        response = delegate.chercherModificationDossierEpg(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertTrue(response.isDernierEnvoi());
        Assert.assertEquals(Long.toString(numeroJeton + 1), response.getJeton());
        Assert.assertEquals(nor, response.getDossierModification().get(0).getNumeroNor());
        Assert.assertEquals(numeroISA, response.getDossierModification().get(0).getNumeroIsa());
        Assert.assertEquals(
            TypeModification.PRIORISATION,
            response.getDossierModification().get(0).getTypeModification()
        );
        Assert.assertEquals("1", response.getDossierModification().get(0).getPriorite().toString());

        request.setJeton(null);
        request.getNor().add(nor);
        jetonDoc.getAdapter(JetonDoc.class).setTypeModification(TypeModification.REATTRIBUTION.toString());
        jetonDoc.getAdapter(JetonDoc.class).setIdsComplementaires(Arrays.asList(nor, norReattribution));
        session.saveDocument(jetonDoc);
        response = delegate.chercherModificationDossierEpg(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertTrue(response.isDernierEnvoi());
        Assert.assertEquals("-1", response.getJeton());
        Assert.assertEquals(nor, response.getDossierModification().get(0).getNumeroNor());
        Assert.assertEquals(norReattribution, response.getDossierModification().get(0).getNouveauNor());
        Assert.assertEquals(numeroISA, response.getDossierModification().get(0).getNumeroIsa());
        Assert.assertEquals(
            TypeModification.REATTRIBUTION,
            response.getDossierModification().get(0).getTypeModification()
        );

        request.getNor().clear();
        request.getNumeroIsa().add(numeroISA);
        jetonDoc.getAdapter(JetonDoc.class).setTypeModification(TypeModification.PUBLICATION.toString());
        jetonDoc.getAdapter(JetonDoc.class).setIdsComplementaires(Arrays.asList(nor, norReattribution));
        session.saveDocument(jetonDoc);
        response = delegate.chercherModificationDossierEpg(request);
        Assert.assertEquals(OK, response.getStatut());
        Assert.assertTrue(response.isDernierEnvoi());
        Assert.assertEquals("-1", response.getJeton());
        Assert.assertEquals(nor, response.getDossierModification().get(0).getNumeroNor());
        Assert.assertEquals(numeroISA, response.getDossierModification().get(0).getNumeroIsa());
        Assert.assertEquals(
            TypeModification.PUBLICATION,
            response.getDossierModification().get(0).getTypeModification()
        );
        Assert.assertNotNull(response.getDossierModification().get(0).getRetourPublication());
        InformationPublication informationPublication = response.getDossierModification().get(0).getRetourPublication();
        Assert.assertEquals("Titre officiel", informationPublication.getTitreOfficiel());
        Assert.assertEquals("1", informationPublication.getNumeroTexte());
        Assert.assertEquals(2L, informationPublication.getPage());
    }
}
