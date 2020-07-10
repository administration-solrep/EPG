package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.platform.content.template.service.ContentTemplateService;
import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.MajMin;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TARGET;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TYPE;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.HistoriqueMajMinisterielleService;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.solonepg.core.util.ComplexTypeUtil;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.core.util.DocUtil;

/**
 * Teste l'historique des mises à jours ministérielles
 * 
 * @author jgomez
 * 
 */

public class TestHistoriqueMajMinisterielleService extends SolonEpgRepositoryTestCase {

	private static final Log					LOGGER	= LogFactory
																.getLog(TestHistoriqueMajMinisterielleService.class);

	private HistoriqueMajMinisterielleService	majService;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		openSession();
		createRootDirectories();
		majService = SolonEpgServiceLocator.getHistoriqueMajMinisterielleService();
	}

	@Override
	public void tearDown() throws Exception {
		closeSession();
		super.tearDown();
	}

	private void createRootDirectories() throws ClientException {
		DocumentModel caseManagement = session.createDocumentModel("Domain");
		caseManagement.setPathInfo("/", "case-management");
		session.createDocument(caseManagement);
		session.save();

		ContentTemplateService service = Framework.getLocalService(ContentTemplateService.class);
		DocumentModel majMinRoot = session.createDocumentModel("MajMinRoot");
		majMinRoot.setPathInfo("/case-management", "maj-min-root");
		session.createDocument(majMinRoot);
		session.save();
		service.executeFactoryForType(majMinRoot);
	}

	public void testSetup() throws ClientException {
		LOGGER.info("Test service des mises à jour ministérielles");
		assertNotNull("Le service d'historique des maj ministériel n'existe pas", majService);
		DocumentModel majMinRoot = session.getDocument(new PathRef("/case-management/maj-min-root"));
		assertNotNull("Le répertoire racine des mises à jours n'est pas présent", majMinRoot);
		assertEquals("Il doit y avoir 4 répertoires dans maj-min-root", 5, session.getChildren(majMinRoot.getRef())
				.size());
		DocumentModel ordonnance = session.getDocument(new PathRef("/case-management/maj-min-root/ordonnance"));
		assertNotNull("Le répertoire racine des ordonnances n'est pas présent", ordonnance);
		DocumentModel appOrdonnance = session.getDocument(new PathRef(
				"/case-management/maj-min-root/application_ordonnance"));
		assertNotNull("Le répertoire racine des application d'ordonnances n'est pas présent", appOrdonnance);
	}

	private Dossier createDossierTest() throws Exception {
		DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
		Dossier dossier = docModel.getAdapter(Dossier.class);
		dossier.setNumeroNor("MMMK1200035L");

		// Initialisation des lois
		List<ComplexeType> loisAppliquees = new ArrayList<ComplexeType>();
		ComplexeType loi1 = ComplexTypeUtil.createGenericTransposition("2011-10", "Loi 1", "1-2", "10",
				"Une première loi", "2011");
		ComplexeType loi2 = ComplexTypeUtil.createGenericTransposition("2011-11", "Loi 2", "1-2", "11",
				"Une seconde loi", "2011");
		ComplexeType loi3 = ComplexTypeUtil.createGenericTransposition("2011-12", "Loi 3", "1-2", "11",
				"Une troisième loi", "2011");
		loisAppliquees.add(loi1);
		loisAppliquees.add(loi2);
		loisAppliquees.add(loi3);
		dossier.setApplicationLoi(loisAppliquees);

		// Initialisation des transpositions
		List<ComplexeType> transpositionDirectives = new ArrayList<ComplexeType>();
		ComplexeType transpositionDirective1 = ComplexTypeUtil.createGenericTransposition("2015-10", "Transposition 1",
				"1-2", "10", "Une première transposition", "2015");
		ComplexeType transpositionDirective2 = ComplexTypeUtil.createGenericTransposition("2015-11", "Transposition 2",
				"1-2", "11", "Une seconde transposition", "2015");
		transpositionDirectives.add(transpositionDirective1);
		transpositionDirectives.add(transpositionDirective2);
		dossier.setTranspositionDirective(transpositionDirectives);

		dossier.save(session);
		return dossier;
	}

	public void testDifferenceTransposition() throws Exception {
		DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
		Dossier dossier = docModel.getAdapter(Dossier.class);
		dossier.setNumeroNor("MMMK1200035L");
		List<ComplexeType> initialList = new ArrayList<ComplexeType>();
		initialList.add(ComplexTypeUtil.createGenericTransposition("2015-10", "Transposition 1", "1-2", "10",
				"Une première transposition", "2015"));
		initialList.add(ComplexTypeUtil.createGenericTransposition("2015-11", "Transposition 2", "1-2", "11",
				"Une seconde transposition", "2015"));
		List<ComplexeType> finalList = new ArrayList<ComplexeType>();
		finalList.add(ComplexTypeUtil.createGenericTransposition("2015-11", "Transposition 2", "1-2", "11",
				"Une seconde transposition", "2015"));
		List<MajMin> results = majService.createDifference(session, dossier, MAJ_TARGET.TRANSPOSITION, initialList,
				finalList);
		assertNotNull(results);
		assertEquals(1, results.size());
		MajMin firstResult = results.get(0);
		assertNotNull(firstResult.getId());
		assertEquals(MAJ_TYPE.SUPPRESSION, firstResult.getModification());
		assertEquals("2015-10", firstResult.getRef());
		assertEquals("Transposition 1", firstResult.getTitre());
		assertEquals("1-2", firstResult.getNumeroArticle());
		assertEquals("2015/10", firstResult.getRefMesure());
		assertEquals("Une première transposition", firstResult.getCommentMaj());
	}

	public void testDifferenceApplicationLoi() throws Exception {
		DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
		Dossier dossier = docModel.getAdapter(Dossier.class);
		dossier.setNumeroNor("MMMK1200035L");
		List<ComplexeType> initialList = new ArrayList<ComplexeType>();
		initialList.add(ComplexTypeUtil.createGenericTransposition("2015-10", "Application loi 1", "1-2", "10",
				"Une première loi", "2015"));
		initialList.add(ComplexTypeUtil.createGenericTransposition("2015-12", "Application loi 2", "1-2", "11",
				"Une seconde loi", "2015"));
		List<ComplexeType> finalList = new ArrayList<ComplexeType>();
		finalList.add(ComplexTypeUtil.createGenericTransposition("2015-12", "Application loi 2", "1-2", "11",
				"Une seconde loi", "2015"));
		finalList.add(ComplexTypeUtil.createGenericTransposition("2016-10", "Application loi 3", "1-3", "11",
				"Une troisième loi", "2015"));
		List<MajMin> results = majService.createDifference(session, dossier, MAJ_TARGET.APPLICATION_LOI, initialList,
				finalList);
		assertNotNull(results);
		assertEquals(2, results.size());

		MajMin firstResult = results.get(0);
		assertEquals(MAJ_TYPE.SUPPRESSION, firstResult.getModification());
		assertEquals("2015-10", firstResult.getRef());
		assertEquals("Application loi 1", firstResult.getTitre());
		assertEquals("1-2", firstResult.getNumeroArticle());
		assertEquals("2015/10", firstResult.getRefMesure());
		assertEquals("Une première loi", firstResult.getCommentMaj());

		MajMin secondResult = results.get(1);
		assertEquals(MAJ_TYPE.AJOUT, secondResult.getModification());
		assertEquals("2016-10", secondResult.getRef());
		assertEquals("Application loi 3", secondResult.getTitre());
		assertEquals("1-3", secondResult.getNumeroArticle());
		assertEquals("2015/11", secondResult.getRefMesure());
		assertEquals("Une troisième loi", secondResult.getCommentMaj());

		List<DocumentModel> historiqueDoc = majService.getHistoriqueMaj(session, MAJ_TARGET.APPLICATION_LOI);
		List<MajMin> historiqueMajMin = DocUtil.adapt(historiqueDoc, MajMin.class);
		assertEquals("2016-10", historiqueMajMin.get(0).getRef());
		assertEquals("2015-10", historiqueMajMin.get(1).getRef());

	}

	public void testDifferenceOrdonnance() throws Exception {
		DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
		Dossier dossier = docModel.getAdapter(Dossier.class);
		dossier.setNumeroNor("MMMK1200035L");
		List<ComplexeType> initialList = new ArrayList<ComplexeType>();
		initialList.add(ComplexTypeUtil.createGenericTransposition("2015-10", "Ordonnance 1", "1-4", "10",
				"Une première ordonnance", "2015"));
		initialList.add(ComplexTypeUtil.createGenericTransposition("2015-12", "Ordonnance 2", "1-3", "11",
				"Une second ordonnance", "2015"));
		List<ComplexeType> finalList = new ArrayList<ComplexeType>();
		finalList.add(ComplexTypeUtil.createGenericTransposition("2015-10", "Ordonnance 1", "1-2", "10",
				"Une première ordonnance", "2015"));
		finalList.add(ComplexTypeUtil.createGenericTransposition("2015-12", "Ordonnance 2", "1-3", "11",
				"Une seconde ordonnance", "2015"));

		List<MajMin> results = majService.createDifference(session, dossier, MAJ_TARGET.ORDONNANCE, initialList,
				finalList);
		assertNotNull(results);
		assertEquals(2, results.size());
		MajMin secondResult = results.get(0);
		assertEquals(MAJ_TYPE.MODIFICATION, secondResult.getModification());
		assertEquals("2015-12", secondResult.getRef());
		assertEquals("Ordonnance 2", secondResult.getTitre());
		assertEquals("1-3", secondResult.getNumeroArticle());
		assertEquals("2015/11", secondResult.getRefMesure());
		assertEquals("Une seconde ordonnance", secondResult.getCommentMaj());
		assertEquals("MMMK1200035L", secondResult.getNorDossier());
		assertNotNull(secondResult.getIdDossier());
		assertNotNull(secondResult.getDateCreation());

		MajMin firstResult = results.get(1);
		assertEquals(MAJ_TYPE.MODIFICATION, firstResult.getModification());
		assertEquals("2015-10", firstResult.getRef());
		assertEquals("Ordonnance 1", firstResult.getTitre());
		assertEquals("1-2", firstResult.getNumeroArticle());
		assertEquals("2015/10", firstResult.getRefMesure());
		assertEquals("Une première ordonnance", firstResult.getCommentMaj());
		assertNotNull(firstResult.getDateCreation());
	}

	public void testRegisterMaj() throws Exception {
		Dossier dossierTest = createDossierTest();
		List<ComplexeType> loisAppliquees = dossierTest.getApplicationLoi();
		ComplexeType loi4 = ComplexTypeUtil.createGenericTransposition("2011-13", "Loi 4", "1-2", "11",
				"Une quatrième loi", "2011");
		loisAppliquees.add(loi4);
		dossierTest.setApplicationLoi(loisAppliquees);
		majService.registerMajDossier(session, dossierTest.getDocument());
		assertEquals(0, session.getChildren(majService.getOrdonnanceRef(session)).size());
		assertEquals(1, session.getChildren(majService.getApplicationLoiRef(session)).size());
		assertEquals(0, session.getChildren(majService.getTranspositionRef(session)).size());
		assertNotNull(majService.getHistoriqueMaj(session, MAJ_TARGET.ORDONNANCE));
		assertNotNull(majService.getHistoriqueMaj(session, MAJ_TARGET.APPLICATION_LOI));
		assertNotNull(majService.getHistoriqueMaj(session, MAJ_TARGET.TRANSPOSITION));

		assertEquals(0, majService.getHistoriqueMaj(session, MAJ_TARGET.ORDONNANCE).size());
		assertEquals(1, majService.getHistoriqueMaj(session, MAJ_TARGET.APPLICATION_LOI).size());
		assertEquals(0, majService.getHistoriqueMaj(session, MAJ_TARGET.TRANSPOSITION).size());
		return;
	}

}
