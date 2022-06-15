package fr.dila.solonepg.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import fr.dila.cm.cases.HasParticipants;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.DossierBordereauService;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.cases.DossierImpl;
import fr.dila.ss.api.domain.feuilleroute.StepFolder;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.ResourceHelper;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.constant.FeuilleRouteConstant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.junit.Ignore;

public class EpgDossierDistributionServiceImplTest {
    private EpgDossierDistributionServiceImpl service;

    @Mock
    private ActeService acteService;

    @Mock
    private DossierBordereauService bordereauService;

    @Mock
    private BulletinOfficielService bulletinService;

    @Mock
    private TableReferenceService tabRefService;

    @Mock
    private VocabularyService vocService;

    @Mock
    private ParapheurService parapheurService;

    @Mock
    private FondDeDossierService fondDeDossierService;

    @Mock
    private EPGFeuilleRouteService feuilleRouteService;

    @Mock
    private CoreSession session;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        service =
            new EpgDossierDistributionServiceImpl(
                bordereauService,
                bulletinService,
                tabRefService,
                vocService,
                parapheurService,
                fondDeDossierService,
                acteService,
                feuilleRouteService
            );
    }

    @Test
    public void testCasCheckDossierFinBeforeValidateOK() {
        // Cas 1 : strict minimum pour valider le dossier sachant que le dossier
        // n'a pas d'étape future
        Dossier dossCas1 = createDossier();
        when(dossCas1.getIsParapheurComplet()).thenReturn(true);
        when(bordereauService.isBordereauComplet(dossCas1)).thenReturn(true);
        try {
            service.checkDossierBeforeValidateStep(session, dossCas1, new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Erreur lors de la validation du dossier : " + e.getMessage());
        }
    }

    @Test
    public void testCasCheckDossierAvantEtapePublicationOK() {
        // Cas 2 : dossier avec sa prochaine étape qui est pour publication dila jo
        Dossier dossCas2 = createDossier();
        // minimum pour que les tests sans les étapes passe
        when(dossCas2.getIsParapheurComplet()).thenReturn(true);
        when(bordereauService.isBordereauComplet(dossCas2)).thenReturn(true);

        // minimum pour que les tests des étapes passent
        RetourDila mockRetour = mock(RetourDila.class);
        Calendar newDate = DateUtil.addMonthsToNow(1);
        when(dossCas2.getDocument().getAdapter(RetourDila.class)).thenReturn(mockRetour);
        when(mockRetour.getModeParution()).thenReturn("Electronique");
        when(dossCas2.getDelaiPublication()).thenReturn(VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE);
        when(dossCas2.getDatePreciseePublication()).thenReturn(newDate);
        when(dossCas2.getPublicationIntegraleOuExtrait()).thenReturn("Intégrale");
        when(dossCas2.getVecteurPublication()).thenReturn(Lists.newArrayList("Journal officiel"));

        try {
            // Validation des vecteurs de publication
            IdRef docRef = new IdRef("Journal officiel");
            when(session.exists(docRef)).thenReturn(true);
            VecteurPublication vecteur = mock(VecteurPublication.class);
            DocumentModel docVecteur = mock(DocumentModel.class);
            when(vecteur.getIntitule()).thenReturn("Journal officiel");
            when(docVecteur.getAdapter(VecteurPublication.class)).thenReturn(vecteur);
            when(docVecteur.getId()).thenReturn("123");
            when(vecteur.getDocument()).thenReturn(docVecteur);
            when(session.getDocument(docRef)).thenReturn(docVecteur);
            when(docVecteur.hasSchema(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA)).thenReturn(true);

            when(bulletinService.isAllVecteurPublicationActif(session, Lists.newArrayList("123"))).thenReturn(true);
            // Fin de validation des vecteurs de publication

            when(parapheurService.getPieceParapheur(session, dossCas2, false))
                .thenReturn(Lists.newArrayList(mock(DocumentModel.class)));
            String[] nextSteps = { VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO };
            service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Erreur lors de la validation du dossier : " + e.getMessage());
        }
    }

    @Test
    public void testCasCheckDossierAvantDerniereEtapeOK() {
        // Cas 2 : dossier avec sa prochaine étape qui n'est pas une étape finale
        // et où l'étape finale est pour publication dila BO
        Dossier dossCas3 = createDossier();
        // minimum pour que les tests sans les étapes passe
        when(dossCas3.getIsParapheurComplet()).thenReturn(true);
        when(bordereauService.isBordereauComplet(dossCas3)).thenReturn(true);

        // minimum pour que les tests des étapes passent
        RetourDila mockRetour = mock(RetourDila.class);
        Calendar newDate = DateUtil.addMonthsToNow(1);
        when(dossCas3.getDocument().getAdapter(RetourDila.class)).thenReturn(mockRetour);
        when(mockRetour.getModeParution()).thenReturn("Electronique");
        when(dossCas3.getDelaiPublication()).thenReturn(VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE);
        when(dossCas3.getDatePreciseePublication()).thenReturn(newDate);
        when(dossCas3.getPublicationIntegraleOuExtrait()).thenReturn("Intégrale");
        String bodmr = "BODMR";
        when(dossCas3.getVecteurPublication()).thenReturn(Lists.newArrayList(bodmr));

        try {
            // Validation des vecteurs de publication
            IdRef docRef = new IdRef(bodmr);
            when(session.exists(docRef)).thenReturn(true);
            VecteurPublication vecteur = mock(VecteurPublication.class);
            DocumentModel docVecteur = mock(DocumentModel.class);
            when(vecteur.getIntitule()).thenReturn(bodmr);
            when(docVecteur.getAdapter(VecteurPublication.class)).thenReturn(vecteur);
            when(docVecteur.getId()).thenReturn(bodmr);
            when(docVecteur.hasSchema(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA)).thenReturn(true);
            when(vecteur.getDocument()).thenReturn(docVecteur);
            when(session.getDocument(docRef)).thenReturn(docVecteur);

            when(bulletinService.isAllVecteurPublicationActif(session, Lists.newArrayList(bodmr))).thenReturn(true);
            // Fin de validation des vecteurs de publication

            when(parapheurService.getPieceParapheur(session, dossCas3, false))
                .thenReturn(Lists.newArrayList(mock(DocumentModel.class)));
            String[] nextSteps = {
                VocabularyConstants.ROUTING_TASK_TYPE_ATTRIBUTION_SECTEUR_PARLEMENTAIRE,
                VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO
            };
            service.checkDossierBeforeValidateStep(session, dossCas3, createListOfSteps(nextSteps));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Erreur lors de la validation du dossier : " + e.getMessage());
        }
    }

    @Test
    public void testCasCheckDossierAvantEtapePublicationKO() {
        // Cas 2 : dossier avec sa prochaine étape qui est pour publication dila jo
        Dossier dossCas2 = createDossier();
        String[] nextSteps = { VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO };
        Calendar oldDate = Calendar.getInstance();
        oldDate.set(2015, 01, 01);
        Calendar newDate = DateUtil.addMonthsToNow(1);

        when(dossCas2.getIsParapheurComplet()).thenReturn(true);
        when(bordereauService.isBordereauComplet(dossCas2)).thenReturn(true);

        try {
            service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
            fail("La validation aurait dû échouer");
        } catch (Exception e) {
            assertEquals("Le dossier ne peut pas être validé sans mode de parution", e.getMessage());
        }

        RetourDila mockRetour = mock(RetourDila.class);
        when(dossCas2.getDocument().getAdapter(RetourDila.class)).thenReturn(mockRetour);
        when(mockRetour.getModeParution()).thenReturn("Electronique");
        try {
            service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
            fail("La validation aurait dû échouer");
        } catch (Exception e) {
            assertEquals(
                "Le répertoire \"Acte intégral\" ou \"Extrait\" du parapheur n'est pas complet",
                e.getMessage()
            );
        }

        try {
            when(parapheurService.getPieceParapheur(session, dossCas2, false))
                .thenReturn(Lists.newArrayList(mock(DocumentModel.class)));
            service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
            fail("La validation aurait dû échouer");
        } catch (Exception e) {
            assertEquals(
                "L'étape suivante n'est pas compatible avec le délai de publication renseigné",
                e.getMessage()
            );
        }

        when(dossCas2.getDelaiPublication()).thenReturn(VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE);
        try {
            when(parapheurService.getPieceParapheur(session, dossCas2, false))
                .thenReturn(Lists.newArrayList(mock(DocumentModel.class)));
            service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
            fail("La validation aurait dû échouer");
        } catch (Exception e) {
            assertEquals("L'étape suivante n'est pas compatible avec la date de publication précisée", e.getMessage());
        }

        when(dossCas2.getDatePreciseePublication()).thenReturn(oldDate);
        try {
            when(parapheurService.getPieceParapheur(session, dossCas2, false))
                .thenReturn(Lists.newArrayList(mock(DocumentModel.class)));
            service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
            fail("La validation aurait dû échouer");
        } catch (Exception e) {
            assertEquals("L'étape suivante n'est pas compatible avec la date de publication précisée", e.getMessage());
        }

        when(dossCas2.getDatePreciseePublication()).thenReturn(newDate);
        try {
            when(parapheurService.getPieceParapheur(session, dossCas2, false))
                .thenReturn(Lists.newArrayList(mock(DocumentModel.class)));
            service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
            fail("La validation aurait dû échouer");
        } catch (Exception e) {
            assertEquals(
                "L'étape suivante n'est pas compatible avec la publication intégrale ou par extrait renseignée",
                e.getMessage()
            );
        }

        when(dossCas2.getPublicationIntegraleOuExtrait()).thenReturn("Intégrale");
        when(dossCas2.getVecteurPublication()).thenReturn(Lists.newArrayList("BODMR"));
        try {
            when(parapheurService.getPieceParapheur(session, dossCas2, false))
                .thenReturn(Lists.newArrayList(mock(DocumentModel.class)));
            service.checkDossierBeforeValidateStep(session, dossCas2, createListOfSteps(nextSteps));
            fail("La validation aurait dû échouer");
        } catch (Exception e) {
            assertEquals(
                "L'étape suivante n'est pas compatible avec les données de vecteurs de publication qui ont été renseignées",
                e.getMessage()
            );
        }
    }

    @Test
    public void testCasCheckDossierFinBeforeValidateKO() {
        // Cas 1 : dossier initialisé pas d'étape suivante dans la FdR
        Dossier dossCas1 = createDossier();
        try {
            service.checkDossierBeforeValidateStep(session, dossCas1, new ArrayList<>());
            fail("La validation aurait dû échouer");
        } catch (Exception e) {
            assertEquals("Il faut obligatoirement ajouter un extrait dans le parapheur", e.getMessage());
        }

        when(dossCas1.getIsParapheurComplet()).thenReturn(true);
        when(bordereauService.isBordereauComplet(dossCas1)).thenReturn(false);
        when(bordereauService.getBordereauMetadonnesObligatoiresManquantes(dossCas1.getDocument())).thenReturn("tout");
        try {
            service.checkDossierBeforeValidateStep(session, dossCas1, new ArrayList<>());
            fail("La validation aurait dû échouer");
        } catch (Exception e) {
            assertEquals(ResourceHelper.getString("bordereau.champs.obligatoires.manquants", "tout"), e.getMessage());
        }
    }

    @Test
    public void testCasIsNextStepFournitureEpreuve() {
        // cas 1 : Pour fourniture d'épreuve en série
        String[] stepsNameEpreuveSerie = { VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE };
        boolean isEpreuve = service.isNextStepFournitureEpreuve(
            createListOfStepsParallele(stepsNameEpreuveSerie, 0),
            session
        );
        assertTrue(isEpreuve);

        // cas 2 : Pour information juste avant Epreuve en série
        String[] stepsNameInfoAvantEpreuveSerie = {
            VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION,
            VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE
        };
        isEpreuve =
            service.isNextStepFournitureEpreuve(createListOfStepsParallele(stepsNameInfoAvantEpreuveSerie, 0), session);
        assertTrue(isEpreuve);

        // cas 3 : Pour Epreuve dans étape parallèle
        String[] stepsNameEpreuveDansParallele = {
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS,
            VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE,
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS
        };
        isEpreuve =
            service.isNextStepFournitureEpreuve(createListOfStepsParallele(stepsNameEpreuveDansParallele, 2), session);
        assertTrue(isEpreuve);

        // cas 4 : Une étape Pour Info dans 2 étapes parallèles avant Epreuve
        String[] stepsNameInfoDansParaAvantEpruve = {
            VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION,
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS,
            VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE
        };
        isEpreuve =
            service.isNextStepFournitureEpreuve(
                createListOfStepsParallele(stepsNameInfoDansParaAvantEpruve, 2),
                session
            );
        assertFalse(isEpreuve);

        // cas 5 : Pour Epreuve après parallèle
        String[] stepsNameApresParallele = {
            VocabularyConstants.ROUTING_TASK_TYPE_IMPRESSION,
            VocabularyConstants.ROUTING_TASK_TYPE_AVIS,
            VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE
        };
        isEpreuve =
            service.isNextStepFournitureEpreuve(createListOfStepsParallele(stepsNameApresParallele, 2), session);
        assertFalse(isEpreuve);

        // cas 6 : Etapes parallèle avec que des étapes pour info avant parallèle
        String[] stepsNameQueInfoDansParaAvantEpreuve = {
            VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION,
            VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION,
            VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE
        };
        isEpreuve =
            service.isNextStepFournitureEpreuve(
                createListOfStepsParallele(stepsNameQueInfoDansParaAvantEpreuve, 2),
                session
            );
        assertTrue(isEpreuve);

        // cas 7 : Pas d'étape Epreuve
        String[] stepsNameSansEpreuve = { VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION };
        isEpreuve = service.isNextStepFournitureEpreuve(createListOfStepsParallele(stepsNameSansEpreuve, 0), session);
        assertFalse(isEpreuve);
    }

    private Dossier createDossier() {
        DocumentModel model = mock(DocumentModel.class);
        when(model.getAdapter(HasParticipants.class)).thenReturn(mock(HasParticipants.class));
        DossierImpl dossier = new DossierImpl(model);

        return dossier;
    }

    private List<DocumentModel> createListOfSteps(String[] stepsName) {
        List<DocumentModel> lstEtapes = new ArrayList<>();

        for (String etape : stepsName) {
            DocumentModel docEtape = mock(DocumentModel.class);
            SolonEpgRouteStep beanEtape = mock(SolonEpgRouteStep.class);
            when(beanEtape.getType()).thenReturn(etape);
            when(docEtape.getAdapter(SolonEpgRouteStep.class)).thenReturn(beanEtape);

            lstEtapes.add(docEtape);
        }

        return lstEtapes;
    }

    private List<DocumentModel> createListOfStepsParallele(String[] stepsName, int nbParalleleStep) {
        List<DocumentModel> lstEtapes = new ArrayList<>();

        for (String etape : stepsName) {
            DocumentModel docEtape = mock(DocumentModel.class);
            DocumentRef parentRef = mock(DocumentRef.class);
            DocumentRef parentParentRef = mock(DocumentRef.class);
            DocumentModel parentEtapeDoc = mock(DocumentModel.class);
            DocumentModel parentParentDoc = mock(DocumentModel.class);
            StepFolder parentParentStepFolder = mock(StepFolder.class);
            SSRouteStep beanEtape = mock(SSRouteStep.class);
            when(beanEtape.getType()).thenReturn(etape);
            when(docEtape.getAdapter(SSRouteStep.class)).thenReturn(beanEtape);
            when(docEtape.getParentRef()).thenReturn(parentRef);
            when(session.getDocument(parentRef)).thenReturn(parentEtapeDoc);
            when(parentEtapeDoc.getParentRef()).thenReturn(parentParentRef);
            when(session.getDocument(parentParentRef)).thenReturn(parentParentDoc);
            if (nbParalleleStep > 0) {
                when(parentEtapeDoc.getType()).thenReturn(FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER);
                when(parentParentDoc.getType()).thenReturn(FeuilleRouteConstant.TYPE_FEUILLE_ROUTE_STEP_FOLDER);
                when(parentParentDoc.getAdapter(StepFolder.class)).thenReturn(parentParentStepFolder);
                when(parentParentStepFolder.isParallel()).thenReturn(true);
                nbParalleleStep--;
            } else {
                when(parentEtapeDoc.getType()).thenReturn("Pas StepFolder");
            }

            lstEtapes.add(docEtape);
        }

        return lstEtapes;
    }
}
