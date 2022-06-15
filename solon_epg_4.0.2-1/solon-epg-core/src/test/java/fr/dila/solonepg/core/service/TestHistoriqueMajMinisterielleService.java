package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.MajMin;
import fr.dila.solonepg.api.cases.typescomplexe.DossierTransposition;
import fr.dila.solonepg.api.cases.typescomplexe.TranspositionsContainer;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TARGET;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TYPE;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.HistoriqueMajMinisterielleService;
import fr.dila.solonepg.core.cases.typescomplexe.TranspositionsContainerImpl;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.solonepg.core.util.ComplexTypeUtil;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.core.util.DocUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.platform.content.template.service.ContentTemplateService;

/**
 * Teste l'historique des mises à jours ministérielles
 *
 * @author jgomez
 *
 */

public class TestHistoriqueMajMinisterielleService extends AbstractEPGTest {
    private static final Log LOGGER = LogFactory.getLog(TestHistoriqueMajMinisterielleService.class);

    @Inject
    private HistoriqueMajMinisterielleService majService;

    @Inject
    private ContentTemplateService contentTemplateService;

    @Before
    public void setUp() {
        createRootDirectories();
    }

    private void createRootDirectories() {
        DocumentModel caseManagement = session.createDocumentModel("Domain");
        caseManagement.setPathInfo("/", "case-management");
        session.createDocument(caseManagement);
        session.save();

        DocumentModel majMinRoot = session.createDocumentModel("MajMinRoot");
        majMinRoot.setPathInfo("/case-management", "maj-min-root");
        session.createDocument(majMinRoot);
        session.save();
        contentTemplateService.executeFactoryForType(majMinRoot);
    }

    public void testSetup() {
        LOGGER.info("Test service des mises à jour ministérielles");
        Assert.assertNotNull("Le service d'historique des maj ministériel n'existe pas", majService);
        DocumentModel majMinRoot = session.getDocument(new PathRef("/case-management/maj-min-root"));
        Assert.assertNotNull("Le répertoire racine des mises à jours n'est pas présent", majMinRoot);
        Assert.assertEquals(
            "Il doit y avoir 4 répertoires dans maj-min-root",
            5,
            session.getChildren(majMinRoot.getRef()).size()
        );
        DocumentModel ordonnance = session.getDocument(new PathRef("/case-management/maj-min-root/ordonnance"));
        Assert.assertNotNull("Le répertoire racine des ordonnances n'est pas présent", ordonnance);
        DocumentModel appOrdonnance = session.getDocument(
            new PathRef("/case-management/maj-min-root/application_ordonnance")
        );
        Assert.assertNotNull("Le répertoire racine des application d'ordonnances n'est pas présent", appOrdonnance);
    }

    private Dossier createDossierTest() throws Exception {
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
        Dossier dossier = docModel.getAdapter(Dossier.class);
        dossier.setNumeroNor("MMMK1200035L");

        // Initialisation des lois
        DossierTransposition loi1 = ComplexTypeUtil.createGenericTransposition(
            "2011-10",
            "Loi 1",
            "1-2",
            "10",
            "Une première loi",
            "2011"
        );
        DossierTransposition loi2 = ComplexTypeUtil.createGenericTransposition(
            "2011-11",
            "Loi 2",
            "1-2",
            "11",
            "Une seconde loi",
            "2011"
        );
        DossierTransposition loi3 = ComplexTypeUtil.createGenericTransposition(
            "2011-12",
            "Loi 3",
            "1-2",
            "11",
            "Une troisième loi",
            "2011"
        );
        TranspositionsContainer applicationLoi = new TranspositionsContainerImpl();
        applicationLoi.setTranspositions(Arrays.asList(loi1, loi2, loi3));
        dossier.setApplicationLoi(applicationLoi);

        // Initialisation des transpositions
        List<DossierTransposition> transpositionDirectives = new ArrayList<>();
        DossierTransposition transpositionDirective1 = ComplexTypeUtil.createGenericTransposition(
            "2015-10",
            "Transposition 1",
            "1-2",
            "10",
            "Une première transposition",
            "2015"
        );
        DossierTransposition transpositionDirective2 = ComplexTypeUtil.createGenericTransposition(
            "2015-11",
            "Transposition 2",
            "1-2",
            "11",
            "Une seconde transposition",
            "2015"
        );
        transpositionDirectives.add(transpositionDirective1);
        transpositionDirectives.add(transpositionDirective2);
        dossier.setTranspositionDirective(new TranspositionsContainerImpl(transpositionDirectives));

        dossier.save(session);
        return dossier;
    }

    @Test
    public void testDifferenceTransposition() throws Exception {
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
        Dossier dossier = docModel.getAdapter(Dossier.class);
        dossier.setNumeroNor("MMMK1200035L");
        List<ComplexeType> initialList = new ArrayList<>();
        initialList.add(
            ComplexTypeUtil.createGenericTransposition(
                "2015-10",
                "Transposition 1",
                "1-2",
                "10",
                "Une première transposition",
                "2015"
            )
        );
        initialList.add(
            ComplexTypeUtil.createGenericTransposition(
                "2015-11",
                "Transposition 2",
                "1-2",
                "11",
                "Une seconde transposition",
                "2015"
            )
        );
        List<ComplexeType> finalList = new ArrayList<>();
        finalList.add(
            ComplexTypeUtil.createGenericTransposition(
                "2015-11",
                "Transposition 2",
                "1-2",
                "11",
                "Une seconde transposition",
                "2015"
            )
        );
        List<MajMin> results = majService.createDifference(
            session,
            dossier,
            MAJ_TARGET.TRANSPOSITION,
            initialList,
            finalList
        );
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());
        MajMin firstResult = results.get(0);
        Assert.assertNotNull(firstResult.getId());
        Assert.assertEquals(MAJ_TYPE.SUPPRESSION, firstResult.getModification());
        Assert.assertEquals("2015-10", firstResult.getRef());
        Assert.assertEquals("Transposition 1", firstResult.getTitre());
        Assert.assertEquals("1-2", firstResult.getNumeroArticle());
        Assert.assertEquals("2015/10", firstResult.getRefMesure());
        Assert.assertEquals("Une première transposition", firstResult.getCommentMaj());
    }

    @Test
    public void testDifferenceApplicationLoi() throws Exception {
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
        Dossier dossier = docModel.getAdapter(Dossier.class);
        dossier.setNumeroNor("MMMK1200035L");
        List<ComplexeType> initialList = new ArrayList<>();
        initialList.add(
            ComplexTypeUtil.createGenericTransposition(
                "2015-10",
                "Application loi 1",
                "1-2",
                "10",
                "Une première loi",
                "2015"
            )
        );
        initialList.add(
            ComplexTypeUtil.createGenericTransposition(
                "2015-12",
                "Application loi 2",
                "1-2",
                "11",
                "Une seconde loi",
                "2015"
            )
        );
        List<ComplexeType> finalList = new ArrayList<>();
        finalList.add(
            ComplexTypeUtil.createGenericTransposition(
                "2015-12",
                "Application loi 2",
                "1-2",
                "11",
                "Une seconde loi",
                "2015"
            )
        );
        finalList.add(
            ComplexTypeUtil.createGenericTransposition(
                "2016-10",
                "Application loi 3",
                "1-3",
                "11",
                "Une troisième loi",
                "2015"
            )
        );
        List<MajMin> results = majService.createDifference(
            session,
            dossier,
            MAJ_TARGET.APPLICATION_LOI,
            initialList,
            finalList
        );
        Assert.assertNotNull(results);
        Assert.assertEquals(2, results.size());

        MajMin firstResult = results.get(0);
        Assert.assertEquals(MAJ_TYPE.SUPPRESSION, firstResult.getModification());
        Assert.assertEquals("2015-10", firstResult.getRef());
        Assert.assertEquals("Application loi 1", firstResult.getTitre());
        Assert.assertEquals("1-2", firstResult.getNumeroArticle());
        Assert.assertEquals("2015/10", firstResult.getRefMesure());
        Assert.assertEquals("Une première loi", firstResult.getCommentMaj());

        MajMin secondResult = results.get(1);
        Assert.assertEquals(MAJ_TYPE.AJOUT, secondResult.getModification());
        Assert.assertEquals("2016-10", secondResult.getRef());
        Assert.assertEquals("Application loi 3", secondResult.getTitre());
        Assert.assertEquals("1-3", secondResult.getNumeroArticle());
        Assert.assertEquals("2015/11", secondResult.getRefMesure());
        Assert.assertEquals("Une troisième loi", secondResult.getCommentMaj());

        List<DocumentModel> historiqueDoc = majService.getHistoriqueMaj(session, MAJ_TARGET.APPLICATION_LOI);
        List<MajMin> historiqueMajMin = DocUtil.adapt(historiqueDoc, MajMin.class);
        Assert.assertEquals("2016-10", historiqueMajMin.get(0).getRef());
        Assert.assertEquals("2015-10", historiqueMajMin.get(1).getRef());
    }

    @Test
    public void testDifferenceOrdonnance() throws Exception {
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
        Dossier dossier = docModel.getAdapter(Dossier.class);
        dossier.setNumeroNor("MMMK1200035L");
        List<ComplexeType> initialList = new ArrayList<>();
        initialList.add(
            ComplexTypeUtil.createGenericTransposition(
                "2015-10",
                "Ordonnance 1",
                "1-4",
                "10",
                "Une première ordonnance",
                "2015"
            )
        );
        initialList.add(
            ComplexTypeUtil.createGenericTransposition(
                "2015-12",
                "Ordonnance 2",
                "1-3",
                "11",
                "Une second ordonnance",
                "2015"
            )
        );
        List<ComplexeType> finalList = new ArrayList<>();
        finalList.add(
            ComplexTypeUtil.createGenericTransposition(
                "2015-10",
                "Ordonnance 1",
                "1-2",
                "10",
                "Une première ordonnance",
                "2015"
            )
        );
        finalList.add(
            ComplexTypeUtil.createGenericTransposition(
                "2015-12",
                "Ordonnance 2",
                "1-3",
                "11",
                "Une seconde ordonnance",
                "2015"
            )
        );

        List<MajMin> results = majService.createDifference(
            session,
            dossier,
            MAJ_TARGET.ORDONNANCE,
            initialList,
            finalList
        );
        Assert.assertNotNull(results);
        Assert.assertEquals(2, results.size());
        MajMin secondResult = results.get(0);
        Assert.assertEquals(MAJ_TYPE.MODIFICATION, secondResult.getModification());
        Assert.assertEquals("2015-12", secondResult.getRef());
        Assert.assertEquals("Ordonnance 2", secondResult.getTitre());
        Assert.assertEquals("1-3", secondResult.getNumeroArticle());
        Assert.assertEquals("2015/11", secondResult.getRefMesure());
        Assert.assertEquals("Une seconde ordonnance", secondResult.getCommentMaj());
        Assert.assertEquals("MMMK1200035L", secondResult.getNorDossier());
        Assert.assertNotNull(secondResult.getIdDossier());
        Assert.assertNotNull(secondResult.getDateCreation());

        MajMin firstResult = results.get(1);
        Assert.assertEquals(MAJ_TYPE.MODIFICATION, firstResult.getModification());
        Assert.assertEquals("2015-10", firstResult.getRef());
        Assert.assertEquals("Ordonnance 1", firstResult.getTitre());
        Assert.assertEquals("1-2", firstResult.getNumeroArticle());
        Assert.assertEquals("2015/10", firstResult.getRefMesure());
        Assert.assertEquals("Une première ordonnance", firstResult.getCommentMaj());
        Assert.assertNotNull(firstResult.getDateCreation());
    }

    @Test
    public void testRegisterMaj() throws Exception {
        Dossier dossierTest = createDossierTest();
        List<DossierTransposition> loisAppliquees = new ArrayList<>(
            dossierTest.getApplicationLoi().getTranspositions()
        );
        DossierTransposition loi4 = ComplexTypeUtil.createGenericTransposition(
            "2011-13",
            "Loi 4",
            "1-2",
            "11",
            "Une quatrième loi",
            "2011"
        );
        loisAppliquees.add(loi4);
        TranspositionsContainerImpl applicationLoi = new TranspositionsContainerImpl();
        applicationLoi.setTranspositions(loisAppliquees);
        dossierTest.setApplicationLoi(applicationLoi);

        majService.registerMajDossier(session, dossierTest.getDocument());
        Assert.assertEquals(0, session.getChildren(majService.getOrdonnanceRef(session)).size());
        Assert.assertEquals(1, session.getChildren(majService.getApplicationLoiRef(session)).size());
        Assert.assertEquals(0, session.getChildren(majService.getTranspositionRef(session)).size());
        Assert.assertNotNull(majService.getHistoriqueMaj(session, MAJ_TARGET.ORDONNANCE));
        Assert.assertNotNull(majService.getHistoriqueMaj(session, MAJ_TARGET.APPLICATION_LOI));
        Assert.assertNotNull(majService.getHistoriqueMaj(session, MAJ_TARGET.TRANSPOSITION));

        Assert.assertEquals(0, majService.getHistoriqueMaj(session, MAJ_TARGET.ORDONNANCE).size());
        Assert.assertEquals(1, majService.getHistoriqueMaj(session, MAJ_TARGET.APPLICATION_LOI).size());
        Assert.assertEquals(0, majService.getHistoriqueMaj(session, MAJ_TARGET.TRANSPOSITION).size());
        return;
    }
}
