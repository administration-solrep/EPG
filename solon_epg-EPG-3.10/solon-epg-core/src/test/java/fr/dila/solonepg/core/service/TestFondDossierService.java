package fr.dila.solonepg.core.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.VersioningOption;
import org.nuxeo.ecm.core.versioning.VersioningService;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.st.core.schema.DublincoreSchemaUtils;

/**
 * Tests unitaires sur le service des modèles de parapheur.
 * 
 * @author arolin
 */
public class TestFondDossierService extends SolonEpgRepositoryTestCase {

	private static final Log		log	= LogFactory.getLog(TestFondDossierService.class);

	private FondDeDossierService	fondDossierService;

	@Override
	public void setUp() throws Exception {

		super.setUp();

		// import FondDossier model Service
		fondDossierService = SolonEpgServiceLocator.getFondDeDossierService();
		assertNotNull(fondDossierService);

		openSession();
	}

	@Override
	public void tearDown() throws Exception {
		closeSession();
		super.tearDown();
	}

	/*
	 * test de création d'un fond dossier dans un dossier
	 */
	public void testCreationFondDossier() throws Exception {
		log.info("testCreationFondDossier");
		String typeActe = TypeActeConstants.TYPE_ACTE_AMNISTIE;
		// création d'un dossier avec un type d'acte
		Dossier dossierDoc = createDefaultDossierAndFondDossier(typeActe);
		// récupération FondDossier
		DocumentModel fondDossierDoc = dossierDoc.getFondDeDossier(session).getDocument();
		assertNotNull(fondDossierDoc);
		List<DocumentModel> fondDossierFolderList = session.getChildren(fondDossierDoc.getRef());
		// verification creation arborescence FondDossier
		assertNotNull(fondDossierFolderList);
		assertEquals(4, fondDossierFolderList.size());

	}

	/*
	 * test de récupération des répertoires racines du FondDossier sous forme de noeud
	 */
	public void getAllVisibleRootFolder() throws Exception {
		log.info("testGetFondDossierRootNode");
		// création d'un dossier avec un type d'acte
		DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
		Dossier dossier = docModel.getAdapter(Dossier.class);
		dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_AMNISTIE);

		// creation du parapheur avec l'arborescence associée au type d'acte
		fondDossierService.createAndFillFondDossier(dossier, session);

		dossier.save(session);

		assertNotNull(dossier);

		List<FondDeDossierFolder> listFondDossierNode = fondDossierService.getAllVisibleRootFolder(session,
				dossier.getDocument(), null);

		assertNotNull(listFondDossierNode);
		assertEquals(1, listFondDossierNode.size());

	}

	/*
	 * test de modification d'un fond de dossier lors d'un changement de type d'acte
	 */
	public void testUpdateFondDossierTreeDelete() throws Exception {
		log.info("testUpdateFondDossierTree : Folder Deleted");
		String typeActe = TypeActeConstants.TYPE_ACTE_DECRET_CE;
		// création d'un dossier avec un type d'acte
		Dossier dossierDoc = createDefaultDossierAndFondDossier(typeActe);

		// on vérifie que le fond de dossier contient les répertoire spécifique au dossier CE
		DocumentModel fondDossierDoc = dossierDoc.getFondDeDossier(session).getDocument();
		checkNbFolderDocumentationJointe(fondDossierDoc, 13, session);

		session.save();

		// mise à jour du FondDossier
		String newTypeActe = TypeActeConstants.TYPE_ACTE_DECRET_CM;
		fondDossierService.updateFondDossierTree(dossierDoc, newTypeActe, session);

		// on vérifie que ces répertoires ont été supprimés
		fondDossierDoc = dossierDoc.getFondDeDossier(session).getDocument();
		checkNbFolderDocumentationJointe(fondDossierDoc, 8, session);
	}

	/*
	 * test de modification d'un fond de dossier lors d'un changement de type d'acte
	 */
	public void testUpdateFondDossierTreeAdded() throws Exception {
		log.info("testUpdateFondDossierTree : Folder Added");
		String typeActe = TypeActeConstants.TYPE_ACTE_DECRET_CM;
		// création d'un dossier avec un type d'acte
		Dossier dossierDoc = createDefaultDossierAndFondDossier(typeActe);

		// on vérifie que le fond de dossier contient les répertoire spécifique au dossier CM
		DocumentModel fondDossierDoc = dossierDoc.getFondDeDossier(session).getDocument();
		checkNbFolderDocumentationJointe(fondDossierDoc, 8, session);

		session.save();

		// mise à jour du FondDossier
		String newTypeActe = TypeActeConstants.TYPE_ACTE_DECRET_CE;
		fondDossierService.updateFondDossierTree(dossierDoc, newTypeActe, session);

		// on vérifie que ces répertoires ont été supprimés
		fondDossierDoc = dossierDoc.getFondDeDossier(session).getDocument();
		checkNbFolderDocumentationJointe(fondDossierDoc, 13, session);
	}

	/*
	 * test de modification d'un fond de dossier lors d'un changement de type d'acte
	 */
	public void testUpdateFondDossierTreeNoChange() throws Exception {
		log.info("testUpdateFondDossierTree : Folder no change");
		String typeActe = TypeActeConstants.TYPE_ACTE_DECRET_CM;
		// création d'un dossier avec un type d'acte
		Dossier dossierDoc = createDefaultDossierAndFondDossier(typeActe);

		// on vérifie que le fond de dossier contient les répertoire spécifique au dossier CM
		DocumentModel fondDossierDoc = dossierDoc.getFondDeDossier(session).getDocument();
		checkNbFolderDocumentationJointe(fondDossierDoc, 8, session);

		session.save();

		// mise à jour du FondDossier
		String newTypeActe = TypeActeConstants.TYPE_ACTE_DECRET_CM;
		fondDossierService.updateFondDossierTree(dossierDoc, newTypeActe, session);

		// on vérifie que ces répertoires ont été supprimés
		fondDossierDoc = dossierDoc.getFondDeDossier(session).getDocument();
		checkNbFolderDocumentationJointe(fondDossierDoc, 8, session);
	}

	/**
	 * test sur les versions
	 * 
	 * @throws ClientException
	 */
	public void testVersionManipulation() throws ClientException {
		DocumentModel doc = session.createDocumentModel("/", "testfile1",
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE);

		doc = session.createDocument(doc);
		DublincoreSchemaUtils.setTitle(doc, "A");

		doc.putContextData(VersioningService.VERSIONING_OPTION, VersioningOption.MAJOR);
		// doc.setPropertyValue("uid:major_version", new Long(1));

		doc = session.saveDocument(doc);
		// DocumentRef docRef = session.checkIn(doc.getRef(), VersioningOption.MAJOR, "");
		DocumentRef docRef = doc.getRef();
		// assertTrue(doc.isCheckedOut());
		assertVersion("1.0", doc);

		log.debug("LIFECYCLE : " + doc.getLifeCyclePolicy());
		String lifecyclestate = session.getCurrentLifeCycleState(doc.getRef());
		log.debug("LIFECYCLESTATE : " + lifecyclestate);

		// save with no option, use default
		DublincoreSchemaUtils.setTitle(doc, "B");
		doc = session.saveDocument(doc);
		assertTrue(doc.isCheckedOut());
		assertVersion("1.0", doc);

		// change and save with new minor
		DublincoreSchemaUtils.setTitle(doc, "C");
		doc.putContextData(VersioningService.VERSIONING_OPTION, VersioningOption.MINOR);
		doc = session.saveDocument(doc);
		assertFalse(doc.isCheckedOut());
		assertVersion("1.1", doc);

		// DocumentModel v01 = session.getLastDocumentVersion(docRef);
		// assertEquals(v01.getId(), session.getBaseVersion(docRef).reference());

		List<DocumentModel> dml = session.getVersions(docRef);
		for (DocumentModel dm : dml) {
			log.info("doc: " + getMajor(dm) + "." + getMinor(dm) + "-->" + DublincoreSchemaUtils.getTitle(dm));
		}
	}

	protected void assertVersion(String expected, DocumentModel doc) throws ClientException {
		assertEquals(expected, getMajor(doc) + "." + getMinor(doc));
	}

	protected long getMajor(DocumentModel doc) throws ClientException {
		return ((Long) doc.getPropertyValue(VersioningService.MAJOR_VERSION_PROP)).longValue();
	}

	protected long getMinor(DocumentModel doc) throws ClientException {
		return ((Long) doc.getPropertyValue(VersioningService.MINOR_VERSION_PROP)).longValue();
	}

	protected Dossier createDefaultDossierAndFondDossier(String typeActe) throws Exception {
		// création d'un dossier avec un type d'acte
		DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
		Dossier dossier = docModel.getAdapter(Dossier.class);
		dossier.setTypeActe(typeActe);

		// creation du parapheur avec l'arborescence associée au type d'acte
		fondDossierService.createAndFillFondDossier(dossier, session);
		dossier.save(session);
		assertNotNull(dossier);
		return dossier;
	}

	protected void checkNbFolderDocumentationJointe(DocumentModel fondDossierDoc, int nbFolder, CoreSession session)
			throws ClientException {
		assertNotNull(fondDossierDoc);
		List<DocumentModel> fondDossierFolderList = session.getChildren(fondDossierDoc.getRef());
		assertNotNull(fondDossierFolderList);
		assertEquals(4, fondDossierFolderList.size());
		// récupération du répertoire Répertoire accessible à tous les utilisateurs
		DocumentModel fddFolder = fondDossierFolderList.get(3);
		assertNotNull(fddFolder);
		fondDossierFolderList = session.getChildren(fddFolder.getRef());
		assertNotNull(fondDossierFolderList);
		assertEquals(1, fondDossierFolderList.size());
		// récupération du répertoire Documentation jointe
		fddFolder = fondDossierFolderList.get(0);
		fondDossierFolderList = session.getChildren(fddFolder.getRef());
		assertNotNull(fondDossierFolderList);
		assertEquals(nbFolder, fondDossierFolderList.size());
	}
}