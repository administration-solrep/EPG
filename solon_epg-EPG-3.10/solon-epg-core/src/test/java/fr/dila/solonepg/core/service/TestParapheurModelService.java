package fr.dila.solonepg.core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.model.PropertyException;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.parapheur.ParapheurModel;
import fr.dila.solonepg.api.service.ParapheurModelService;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;

/**
 * Tests unitaires sur le service des modèles de parapheur.
 * 
 * @author arolin
 */
public class TestParapheurModelService extends SolonEpgRepositoryTestCase {

    private static final Log log = LogFactory.getLog(TestParapheurModelService.class);

    private ParapheurModelService parapheurService;

    @Override
    public void setUp() throws Exception {

        super.setUp();

        // contribution utilisée pour les tests
//        deployBundle("fr.dila.solonepg.core");
//        deployContrib("fr.dila.solonepg.core", "OSGI-INF/service/tree-framework.xml");
//        deployContrib("fr.dila.solonepg.core", "OSGI-INF/service/dossier-distribution-framework.xml");
//        deployContrib("fr.dila.solonepg.core", "OSGI-INF/service/parapheur-model-framework.xml");
//        deployContrib("fr.dila.solonepg.core", "OSGI-INF/solonepg-core-type-contrib.xml");
//        deployContrib("fr.dila.solonepg.core", "OSGI-INF/solonepg-schema-contrib.xml");
//        deployContrib("fr.dila.solonepg.core", "OSGI-INF/solonepg-adapter-contrib.xml");
//        deployContrib("fr.dila.solonepg.core", "OSGI-INF/service/vocabulary-framework.xml");
//        deployContrib("fr.dila.solonepg.core", "OSGI-INF/service/acte-framework.xml");

        openSession();

        // import parapheur model Service
        parapheurService = SolonEpgServiceLocator.getParapheurModelService();
        assertNotNull(parapheurService);
    }
    
    @Override
    public void tearDown() throws Exception{
    	closeSession();
    	super.tearDown();
    }

    /*
     * Test des actions possibles dans l'arborescence des répertoires d'un modèle de parapheur.
     */

    protected DocumentModel creationEtRecuperationModeleParapheur() throws PropertyException, ClientException {
        // creation repertoire par defaut
        DocumentModel racineParapheur = creationRacineParapheur();

        // Initialisation type acte
        String typeActe = TypeActeConstants.TYPE_ACTE_CIRCULAIRE;

        // creatioModel
        DocumentModel modeleFdd = parapheurService.createParapheurModele(racineParapheur, session, typeActe);

        // creation arborescence par default pour le type d'acte
        parapheurService.createParapheurDefaultRepository(modeleFdd, session);

        // récupération modele parapheur
        DocumentModel docModelCourant = parapheurService.getParapheurModelFromTypeActe(session, typeActe);
        assertNotNull(docModelCourant);
        ParapheurModel parapheurModelDoc = docModelCourant.getAdapter(ParapheurModel.class);
        assertEquals(typeActe, parapheurModelDoc.getTypeActe());
        return docModelCourant;
    }

    public void testCreateNewDefaultFolderInTreeMethod() throws PropertyException, ClientException {
        log.info("testCreateNewDefaultFolderInTreeMethod");
        // Initialisation variables utilisés et attendues
        String nomRepertoireParDefaut = SolonEpgParapheurConstants.DEFAULT_PARAPHEUR_FOLDER_NAME;
        Boolean estVide = true;
        Long nbMaxdoc = null;
        List<String> formatIds = new ArrayList<String>();

        DocumentModel docModelCourant = creationEtRecuperationModeleParapheur();

        // recuperation du répertoire racine du parapheur "Acte intégral"
        DocumentModelList documentModelList = session.getChildren(docModelCourant.getRef());
        DocumentModel repertoireRacine = documentModelList.get(0);

        // on vérifie que ce répertoire ne contient aucun fils
        DocumentModelList documentModelListTemp = session.getChildren(repertoireRacine.getRef());
        assertEquals(0, documentModelListTemp.size());

        // test : création d'un nouveau répertoire dans le répertoire "Acte intégral"
        parapheurService.createNewDefaultFolderInTree(repertoireRacine, session);

        // on verifie qu'un répertoire a été créé
        documentModelListTemp = session.getChildren(repertoireRacine.getRef());
        assertEquals(1, documentModelListTemp.size());

        // on verifie le nom et le titre du document créé
        DocumentModel docTemp = documentModelListTemp.get(0);
        assertEquals(nomRepertoireParDefaut, docTemp.getTitle());
        assertEquals(nomRepertoireParDefaut, docTemp.getName());
        // on vérifie les propriétés par défault du parapheur
        ParapheurFolder newParaModelFolder = docTemp.getAdapter(ParapheurFolder.class);
        assertEquals(estVide, newParaModelFolder.getEstNonVide());
        assertEquals(nbMaxdoc, newParaModelFolder.getNbDocAccepteMax());
        // assertEquals(feuilleStyleIds.size(),newParaModelFolder.getFeuilleStyleFiles().size());
        assertEquals(formatIds.size(), newParaModelFolder.getFormatsAutorises().size());
        // fin test

    }

    public void testCreateNewDefaultFolderInTreeMethodAddTwoNewFolder() throws PropertyException, ClientException {
        log.info("testCreateNewDefaultFolderInTreeMethodAddTwoNewFolder");
        // Initialisation variables utilisés et attendues
        String nomRepertoireParDefaut = SolonEpgParapheurConstants.DEFAULT_PARAPHEUR_FOLDER_NAME;

        DocumentModel docModelCourant = creationEtRecuperationModeleParapheur();

        // recuperation du répertoire racine "Acte intégral"
        DocumentModelList documentModelList = session.getChildren(docModelCourant.getRef());

        // recuperation du répertoire "Acte intégral"
        DocumentModel repMinisterePorteurDoc = documentModelList.get(0);
        // on vérifie que ce répertoire ne contient aucun fils
        DocumentModelList documentModelListTemp = session.getChildren(repMinisterePorteurDoc.getRef());
        assertEquals(0, documentModelListTemp.size());

        // test 1 : création d'un nouveau répertoire dans le répertoire "Acte intégral"
        parapheurService.createNewDefaultFolderInTree(repMinisterePorteurDoc, session);
        // on verifie qu'un répertoire a été créé
        documentModelListTemp = session.getChildren(repMinisterePorteurDoc.getRef());
        assertEquals(1, documentModelListTemp.size());
        // on verifie le nom et le titre du document créé
        DocumentModel newRepositoryDoc = documentModelListTemp.get(0);
        assertEquals(nomRepertoireParDefaut, newRepositoryDoc.getTitle());
        assertEquals(nomRepertoireParDefaut, newRepositoryDoc.getName());

        // test 2 : on recree un nouveau répertoire dans le répertoire "répertoire réservé au minisère porteur"
        parapheurService.createNewDefaultFolderInTree(repMinisterePorteurDoc, session);
        // on verifie qu'un répertoire a été créé
        documentModelListTemp = session.getChildren(repMinisterePorteurDoc.getRef());
        assertEquals(2, documentModelListTemp.size());
        // on verifie le titre du document créé est identique
        newRepositoryDoc = documentModelListTemp.get(1);
        // on verifie que le chemin est différent
        assertNotSame(nomRepertoireParDefaut, newRepositoryDoc.getName());
    }

    public void testCreateNewFolderNodeBeforeMethod() throws PropertyException, ClientException {
        log.info("testCreateNewFolderNodeBeforeMethod");
        // Initialisation variables utilisés et attendues
        String nomRepertoireParDefaut = SolonEpgParapheurConstants.DEFAULT_PARAPHEUR_FOLDER_NAME;
        Boolean estVide = true;
        Long nbMaxdoc = null;
        List<String> formatIds = new ArrayList<String>();

        DocumentModel repertoireRacine = creationEtRecuperationModeleParapheur();

        // recuperation du répertoire racine du parapheur "Acte intégral"
        DocumentModelList documentModelList = session.getChildren(repertoireRacine.getRef());
        DocumentModel docModelCourant = documentModelList.get(0);

        // debut du test : on ajoute un nouveau répertoire avant le document "rapport de présentation"
        parapheurService.createNewFolderNodeBefore(docModelCourant, session);
        session.save();

        // on verifie qu'un répertoire a bien été ajouté à la liste
        documentModelList = session.getChildren(repertoireRacine.getRef());
        assertEquals(4, documentModelList.size());

        // on verifie que le document a été créé avant le répertoire "rapport de présentation" dans la liste,on vérifie aussi son titre et don nom
        DocumentModel newDoc = documentModelList.get(0);
        assertEquals(nomRepertoireParDefaut, newDoc.getTitle());
        assertEquals(nomRepertoireParDefaut, newDoc.getName());

        // on vérifie les propriétés par défault du parapheur
        ParapheurFolder newParaModelFolder = newDoc.getAdapter(ParapheurFolder.class);
        assertEquals(estVide, newParaModelFolder.getEstNonVide());
        assertEquals(nbMaxdoc, newParaModelFolder.getNbDocAccepteMax());
        // assertEquals(feuilleStyleIds.size(),newParaModelFolder.getFeuilleStyleFiles().size());
        assertEquals(formatIds.size(), newParaModelFolder.getFormatsAutorises().size());
        // fin test
    }

    public void testCreateNewFolderNodeAfterMethod() throws PropertyException, ClientException {
        log.info("testCreateNewFolderNodeAfterMethod");
        // Initialisation variables utilisés et attendues
        String nomRepertoireParDefaut = SolonEpgParapheurConstants.DEFAULT_PARAPHEUR_FOLDER_NAME;
        Boolean estVide = true;
        Long nbMaxdoc = null;
        List<String> formatIds = new ArrayList<String>();

        DocumentModel repertoireRacine = creationEtRecuperationModeleParapheur();

        // recuperation du répertoire racine du parapheur "Acte intégral"
        DocumentModelList documentModelList = session.getChildren(repertoireRacine.getRef());
        DocumentModel docModelCourant = documentModelList.get(0);

        // test : on ajoute un nouveau répertoire après le document "rapport de présentation"
        parapheurService.createNewFolderNodeAfter(docModelCourant, session);

        // on verifie qu'un répertoire a bien été ajouté à la liste
        documentModelList = session.getChildren(repertoireRacine.getRef());
        assertEquals(4, documentModelList.size());
        // on verifie que le document a été créé avant le répertoire "rapport de présentation" dans la liste,on vérifie aussi son titre et son nom
        DocumentModel newDoc = documentModelList.get(1);
        assertEquals(nomRepertoireParDefaut, newDoc.getTitle());
        assertEquals(nomRepertoireParDefaut, newDoc.getName());

        // on vérifie les propriétés par défault du parapheur
        ParapheurFolder newParaModelFolder = newDoc.getAdapter(ParapheurFolder.class);
        assertEquals(estVide, newParaModelFolder.getEstNonVide());
        assertEquals(nbMaxdoc, newParaModelFolder.getNbDocAccepteMax());
        // assertEquals(feuilleStyleIds.size(),newParaModelFolder.getFeuilleStyleFiles().size());
        assertEquals(formatIds.size(), newParaModelFolder.getFormatsAutorises().size());
        // fin test

    }

    public void testEditFolderMethod() throws PropertyException, ClientException {
        log.info("testEditFolderMethod");
        // Initialisation variables utilisés et attendues
        String nouveauNom = "test 1";
        Boolean estVide = true;
        Long nbMaxdoc = 10L;
        List<File> feuilleStyleIds = new ArrayList<File>();
        List<String> formatIds = new ArrayList<String>();
        formatIds.add("idStyle1");
        formatIds.add("idStyle2");
        String nouveauNomExistant = "Avis divers";
        String ancienNom = "Acte intégral";

        DocumentModel repertoireRacine = creationEtRecuperationModeleParapheur();

        // recuperation du répertoire racine du parapheur "Acte intégral"
        DocumentModelList documentModelList = session.getChildren(repertoireRacine.getRef());
        DocumentModel docModelCourant = documentModelList.get(0);

        // test 1 : edition du répertoire "Acte intégral"
        parapheurService.editFolder(docModelCourant, session, nouveauNom, estVide, nbMaxdoc, feuilleStyleIds, formatIds);
        // on verifie que le répertoire n'as pas changé dans la liste mais a change de nom et de titre
        documentModelList = session.getChildren(repertoireRacine.getRef());
        assertEquals(3, documentModelList.size());

        DocumentModel docTemp = documentModelList.get(0);
        assertEquals(nouveauNom, docTemp.getTitle());
        // le nom du chemin n'a pas change
        assertEquals(ancienNom, docTemp.getName());
        // fin test 1

        // test 2 : edition du répertoire "test 1" en "Extrait" qui est un répertoire déjà existant
        parapheurService.editFolder(docTemp, session, nouveauNomExistant, estVide, nbMaxdoc, feuilleStyleIds, formatIds);
        // on verifie que le répertoire n'as pas changé dans la lsite mais a change de nom
        documentModelList = session.getChildren(repertoireRacine.getRef());
        assertEquals(3, documentModelList.size());
        // on verifie que le document a été créé en fin de liste, on vérifie aussi son titre et son nom
        docTemp = documentModelList.get(0);
        assertEquals(nouveauNomExistant, docTemp.getTitle());
        // le nom du fichier n'est pas identique
        assertNotSame(nouveauNomExistant, docTemp.getName());
        // fin test 2
    }

    public void testDeleteFolderMethod() throws PropertyException, ClientException {
        log.info("testDeleteFolderMethod");

        DocumentModel repertoireRacine = creationEtRecuperationModeleParapheur();

        // recuperation du répertoire racine du parapheur "Acte intégral"
        DocumentModelList documentModelList = session.getChildren(repertoireRacine.getRef());
        DocumentModel docModelCourant = documentModelList.get(0);
        assertEquals(3, documentModelList.size());

        // test : suppression du répertoire "documentation jointe" et de tous ces fils
        parapheurService.deleteFolder(docModelCourant, session);
        // on verifie que le répertoire a été supprimé de la liste
        documentModelList = session.getChildren(repertoireRacine.getRef());
        assertEquals(2, documentModelList.size());

        //
        documentModelList = session.getChildren(repertoireRacine.getRef());
        docModelCourant = documentModelList.get(0);

        // test 2 : suppression du répertoire "Extrait"
        parapheurService.deleteFolder(docModelCourant, session);
        // on verifie que le répertoire a été supprimé de la liste
        documentModelList = session.getChildren(repertoireRacine.getRef());
        assertEquals(1, documentModelList.size());
    }

    /**
     * Methode protected utilisee
     */

    /**
     * création de l'espace d'administration, du répertoire de modèles de feuilles de route et récupèration du répertoire de modèle de feuille de route.
     * 
     * @return DocumentModel le répertoire de modèle de feuille de route
     * 
     * @author ARN
     * 
     */
    protected DocumentModel creationRacineParapheur() throws PropertyException, ClientException {
        // creation de la racine (Workspace Administrateur)
        DocumentModel racine = session.createDocumentModel(DossierSolonEpgConstants.ADMIN_WORKSPACE_DOCUMENT_TYPE);
        racine.setPathInfo("/", "nom_workspace");
        racine = session.createDocument(racine);

        // creation du répertoire des modèles de fonds de dossier
        DocumentModel parapheurModelRacine = session.createDocumentModel(racine.getPathAsString(), "modeles parapheur",
                SolonEpgParapheurConstants.PARAPHEUR_MODEL_ROOT_DOCUMENT_TYPE);
        parapheurModelRacine = session.createDocument(parapheurModelRacine);

        session.save();
        return parapheurModelRacine;
    }

}