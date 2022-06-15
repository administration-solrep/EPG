package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModel;
import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.PropertyException;

/**
 * Tests unitaires sur le service des modèles de fond de dossier.
 *
 * @author arolin
 */
public class TestFondDossierModelService extends AbstractEPGTest {
    private static final Log log = LogFactory.getLog(TestFondDossierModelService.class);

    @Inject
    private FondDeDossierModelService fondDeDossierModelService;

    /*
     * Test des actions possibles dans l'arborescence des répertoires d'un modèle de fond de dossier.
     */

    @Test
    public void testContrib() {
        Assert.assertNotNull(fondDeDossierModelService);
    }

    @Test
    public void testCreateNewDefaultFolderInTreeMethod() throws PropertyException {
        log.info("testCreateNewDefaultFolderInTreeMethod");
        // Initialisation variables utilisés et attendues
        String nomRepertoireParDefaut = SolonEpgFondDeDossierConstants.DEFAULT_FDD_FOLDER_NAME;

        DocumentModel docModelCourant = creationEtRecuperationModeleFdd();

        // recuperation du répertoire racine "repertoire réservé au minisère porteur"
        DocumentModelList documentModelList = session.getChildren(docModelCourant.getRef());
        DocumentModel repertoireRacine = documentModelList.get(0);
        // on vérifie que ce répertoire ne contient aucun fils
        DocumentModelList documentModelListTemp = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(0, documentModelListTemp.size());

        // test 1 : création d'un nouveau répertoire dans le répertoire "répertoire réservé au minisère porteur"
        fondDeDossierModelService.createNewDefaultFolderInTree(session, repertoireRacine, null, null);
        // on verifie qu'un répertoire a été créé
        documentModelListTemp = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(1, documentModelListTemp.size());
        // on verifie le nom et le titre du document créé
        DocumentModel docTemp = documentModelListTemp.get(0);
        Assert.assertEquals(nomRepertoireParDefaut, docTemp.getTitle());
        Assert.assertEquals(nomRepertoireParDefaut, docTemp.getName());
        // fin test 1

        // recuperation du répertoire accessible à tous les utilisateurs
        repertoireRacine = documentModelList.get(3);
        documentModelListTemp = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(1, documentModelListTemp.size());

        // test 2 : création d'un nouveau répertoire dans le répertoire "répertoire accesible à tout les utilisateurs"
        fondDeDossierModelService.createNewDefaultFolderInTree(session, repertoireRacine, null, null);
        // on verifie qu'un répertoire a bien été ajouté à la liste
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(2, documentModelList.size());
        // on verifie que le document a été créé en fin de liste,on vérifie aussi son titre et son nom
        docTemp = documentModelList.get(1);
        Assert.assertEquals(nomRepertoireParDefaut, docTemp.getTitle());
        Assert.assertEquals(nomRepertoireParDefaut, docTemp.getName());
        // fin test 2

        // recuperation du répertoire "documentation jointe"
        repertoireRacine = documentModelList.get(0);
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(8, documentModelList.size());

        // test 3 : création d'un nouveau répertoire dans le répertoire "documentation jointe"
        fondDeDossierModelService.createNewDefaultFolderInTree(session, repertoireRacine, null, null);
        // on verifie qu'un répertoire a bien été ajouté à la liste
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(9, documentModelList.size());
        // on verifie que le document a été créé en fin de liste,on vérifie aussi son ittre et don nom
        docTemp = documentModelList.get(8);
        Assert.assertEquals(nomRepertoireParDefaut, docTemp.getTitle());
        Assert.assertEquals(nomRepertoireParDefaut, docTemp.getName());
        // fin test 3

        // recuperation du répertoire rapport de présentation
        // docModelCourant = documentModelList.get(2);
        // documentModelList = session.getChildren(docModelCourant.getRef());
    }

    @Test
    public void testCreateNewDefaultFolderInTreeMethodAddTwoNewFolder() throws PropertyException {
        log.info("testCreateNewDefaultFolderInTreeMethodAddTwoNewFolder");
        // Initialisation variables utilisés et attendues
        String nomRepertoireParDefaut = SolonEpgFondDeDossierConstants.DEFAULT_FDD_FOLDER_NAME;

        DocumentModel docModelCourant = creationEtRecuperationModeleFdd();

        // recuperation du répertoire racine "repertoire réservé au minisère porteur"
        DocumentModelList documentModelList = session.getChildren(docModelCourant.getRef());

        // recuperation du répertoire réservé au minisère porteur
        DocumentModel repMinisterePorteurDoc = documentModelList.get(0);
        // on vérifie que ce répertoire ne contient aucun fils
        DocumentModelList documentModelListTemp = session.getChildren(repMinisterePorteurDoc.getRef());
        Assert.assertEquals(0, documentModelListTemp.size());

        // test 1 : création d'un nouveau répertoire dans le répertoire "répertoire réservé au minisère porteur"
        fondDeDossierModelService.createNewDefaultFolderInTree(session, repMinisterePorteurDoc, null, null);
        // on verifie qu'un répertoire a été créé
        documentModelListTemp = session.getChildren(repMinisterePorteurDoc.getRef());
        Assert.assertEquals(1, documentModelListTemp.size());
        // on verifie le nom et le titre du document créé
        DocumentModel newRepositoryDoc = documentModelListTemp.get(0);
        Assert.assertEquals(nomRepertoireParDefaut, newRepositoryDoc.getTitle());
        Assert.assertEquals(nomRepertoireParDefaut, newRepositoryDoc.getName());

        // test 2 : on recree un nouveau répertoire dans le répertoire "répertoire réservé au minisère porteur"
        fondDeDossierModelService.createNewDefaultFolderInTree(session, repMinisterePorteurDoc, null, null);
        // on verifie qu'un répertoire a été créé
        documentModelListTemp = session.getChildren(repMinisterePorteurDoc.getRef());
        Assert.assertEquals(2, documentModelListTemp.size());
        // on verifie le titre du document créé est identique
        newRepositoryDoc = documentModelListTemp.get(1);
        Assert.assertEquals(nomRepertoireParDefaut, newRepositoryDoc.getTitle());
        // on verifie que le chemin est différent
        Assert.assertNotSame(nomRepertoireParDefaut, newRepositoryDoc.getName());
    }

    @Test
    public void testCreateNewFolderNodeBeforeMethod() throws PropertyException {
        log.info("testCreateNewFolderNodeBeforeMethod");
        // Initialisation variables utilisés et attendues
        String nomRepertoireParDefaut = SolonEpgFondDeDossierConstants.DEFAULT_FDD_FOLDER_NAME;

        DocumentModel docModelCourant = creationEtRecuperationModeleFdd();

        // recuperation du répertoire accessible à tous les utilisateurs
        DocumentModelList documentModelList = session.getChildren(docModelCourant.getRef());
        DocumentModel repertoireRacine = documentModelList.get(3);
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(1, documentModelList.size());

        // recuperation du répertoire "documentation jointe"
        repertoireRacine = documentModelList.get(0);
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(8, documentModelList.size());

        // recuperation du répertoire "rapport de présentation"
        docModelCourant = documentModelList.get(2);

        // debut du test : on ajoute un nouveau répertoire avant le document "rapport de présentation"
        fondDeDossierModelService.createNewFolderNodeBefore(session, docModelCourant, null, null);
        session.save();
        // on verifie qu'un répertoire a bien été ajouté à la liste
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(9, documentModelList.size());
        // on verifie que le document a été créé avant le répertoire "rapport de présentation" dans la liste,on vérifie
        // aussi son titre et don nom
        DocumentModel newDoc = documentModelList.get(2);
        Assert.assertEquals(nomRepertoireParDefaut, newDoc.getTitle());
        Assert.assertEquals(nomRepertoireParDefaut, newDoc.getName());
        // fin test
    }

    @Test
    public void testCreateNewFolderNodeAfterMethod() throws PropertyException {
        log.info("testCreateNewFolderNodeAfterMethod");
        // Initialisation variables utilisés et attendues
        String nomRepertoireParDefaut = SolonEpgFondDeDossierConstants.DEFAULT_FDD_FOLDER_NAME;

        DocumentModel docModelCourant = creationEtRecuperationModeleFdd();

        // recuperation du répertoire accessible à tous les utilisateurs
        DocumentModelList documentModelList = session.getChildren(docModelCourant.getRef());
        DocumentModel repertoireRacine = documentModelList.get(3);
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(1, documentModelList.size());

        // recuperation du répertoire "documentation jointe"
        repertoireRacine = documentModelList.get(0);
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(8, documentModelList.size());

        // recuperation du répertoire "rapport de présentation"
        docModelCourant = documentModelList.get(2);

        // test 1 : on ajoute un nouveau répertoire après le document "rapport de présentation"
        fondDeDossierModelService.createNewFolderNodeAfter(session, docModelCourant, null, null);

        // on verifie qu'un répertoire a bien été ajouté à la liste
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(9, documentModelList.size());
        // on verifie que le document a été créé avant le répertoire "rapport de présentation" dans la liste,on vérifie
        // aussi son titre et son nom
        DocumentModel newDoc = documentModelList.get(3);
        Assert.assertEquals(nomRepertoireParDefaut, newDoc.getTitle());
        Assert.assertEquals(nomRepertoireParDefaut, newDoc.getName());
        // fin test 1

        // on recupère le dernier répertoire de la liste
        docModelCourant = documentModelList.get(8);

        // test 2 : on ajoute un nouveau répertoire après le dernier répertoire de la liste (document
        // "textes consolidés")
        fondDeDossierModelService.createNewFolderNodeAfter(session, docModelCourant, null, null);

        // on verifie qu'un répertoire a bien été ajouté à la liste
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(10, documentModelList.size());
        // on verifie que le document a été créé avant le répertoire "rapport de présentation" dans la liste,on vérifie
        // aussi son titre et son nom
        newDoc = documentModelList.get(9);
        Assert.assertEquals(nomRepertoireParDefaut, newDoc.getTitle());
        // le nom du fichier n'est pas identique
        Assert.assertNotSame(nomRepertoireParDefaut, newDoc.getName());
        // fin test 2
    }

    @Test
    public void testEditFolderMethod() throws PropertyException {
        log.info("testEditFolderMethod");
        // Initialisation variables utilisés et attendues
        String nouveauNom = "test 1";
        String nouveauNomExistant = "Avis divers";

        DocumentModel docModelCourant = creationEtRecuperationModeleFdd();

        // recuperation du répertoire accessible à tous les utilisateurs
        DocumentModelList documentModelList = session.getChildren(docModelCourant.getRef());
        DocumentModel repertoireRacine = documentModelList.get(3);
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(1, documentModelList.size());

        // recuperation du répertoire "documentation jointe"
        DocumentModel docTemp = documentModelList.get(0);
        documentModelList = session.getChildren(docTemp.getRef());

        // test 1 : edition du répertoire "Documentation jointe/Annexes non publiées"
        DocumentModel annexesNonPubliees = documentModelList.get(3);
        String ancienNom = annexesNonPubliees.getName();
        fondDeDossierModelService.editFolder(annexesNonPubliees, session, nouveauNom, null);
        // on verifie que le répertoire n'as pas changé dans la lsite mais a change de nom et de titre
        documentModelList = session.getChildren(docTemp.getRef());
        Assert.assertEquals(8, documentModelList.size());
        // on verifie que le document a été créé en fin de liste,on vérifie aussi son titre et don nom
        annexesNonPubliees = documentModelList.get(3);
        Assert.assertEquals(nouveauNom, annexesNonPubliees.getTitle());
        // le nom du chemin n'a pas change
        Assert.assertEquals(ancienNom, annexesNonPubliees.getName());
        // fin test 1
        repertoireRacine = docTemp;
        // récupération des fils du répertoire modifié
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(8, documentModelList.size());

        // recuperation du répertoire "Etude d'impact"
        docTemp = documentModelList.get(5);

        // test 2 : edition du répertoire "Etude d'impact" en "Avis divers" qui est un répertoire déjà existant
        // dans ce niveau de l'arborescence
        fondDeDossierModelService.editFolder(docTemp, session, nouveauNomExistant, null);
        // on verifie que le répertoire n'as pas changé dans la lsite mais a change de nom et de tit
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(8, documentModelList.size());
        // on verifie que le document a été créé en fin de liste,on vérifie aussi son titre et son nom
        docTemp = documentModelList.get(5);
        Assert.assertEquals(nouveauNomExistant, docTemp.getTitle());
        // le nom du fichier n'est pas identique
        Assert.assertNotSame(nouveauNomExistant, docTemp.getName());
        // fin test 2
    }

    @Test
    public void testDeleteFolderMethod() throws PropertyException {
        log.info("testDeleteFolderMethod");
        DocumentModel docModelCourant = creationEtRecuperationModeleFdd();

        // recuperation du répertoire accessible à tous les utilisateurs
        DocumentModelList documentModelList = session.getChildren(docModelCourant.getRef());
        DocumentModel repertoireRacine = documentModelList.get(3);
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(1, documentModelList.size());

        // recuperation du répertoire "documentation jointe"
        DocumentModel docTemp = documentModelList.get(0);
        DocumentModelList documentModelListTemp = session.getChildren(docTemp.getRef());
        Assert.assertEquals(8, documentModelListTemp.size());

        // test 1 : suppression du répertoire "Documentation jointe/Annexes non publiées"
        DocumentModel annexesNonPubliees = documentModelListTemp.get(3);
        fondDeDossierModelService.deleteFolder(annexesNonPubliees, session, null);
        // on verifie que le répertoire a été supprimé de la liste
        documentModelList = session.getChildren(docTemp.getRef());
        Assert.assertEquals(7, documentModelList.size());
    }

    protected DocumentModel creationEtRecuperationModeleFdd() throws PropertyException {
        // creation repertoire par defaut
        DocumentModel racineModelFdd = creationRacineFdd();

        // Initialisation type acte
        String typeActe = TypeActeConstants.TYPE_ACTE_CIRCULAIRE;

        // creatioModel
        DocumentModel modeleFdd = fondDeDossierModelService.createFondDossierModele(racineModelFdd, session, typeActe);

        // creation arborescence par default pour le type d'acte
        fondDeDossierModelService.createFondDeDossierDefaultRepository(modeleFdd, session);

        // récupération modele Fdd
        DocumentModel docModelCourant = fondDeDossierModelService.getFondDossierModelFromTypeActe(session, typeActe);
        Assert.assertNotNull(docModelCourant);
        FondDeDossierModel fondDossierModelDoc = docModelCourant.getAdapter(FondDeDossierModel.class);
        Assert.assertEquals(typeActe, fondDossierModelDoc.getTypeActe());
        return docModelCourant;
    }

    /**
     * Methode protected utilisee
     */

    /**
     * Vérification du modèle de Fond De dossier.
     *
     * @param DocumentModelList
     *            listeDesModelesDuFdd
     *
     * @param int positionInList
     *
     * @param String
     *            titleExpected
     *
     * @param String
     *            nameExpected
     *
     * @param String
     *            typeActeExpected
     *
     * @param int nbChildrenExpected
     *
     */
    protected DocumentModelList VerificationModeleFdd(
        DocumentModelList listeDesModelesDuFdd,
        int positionInList,
        String titleExpected,
        String nameExpected,
        String typeActeExpected,
        int nbChildrenExpected
    )
        throws PropertyException, NuxeoException {
        DocumentModel docModelCourant = listeDesModelesDuFdd.get(positionInList);
        FondDeDossierModel fddModel = docModelCourant.getAdapter(FondDeDossierModel.class);

        // vérification des propriétés du modèle de fond de dossier
        Assert.assertEquals(typeActeExpected, fddModel.getTypeActe());

        // vérification des propriétés du repertoire racine du modèle de fond de dossier
        DocumentModelList documentModelList = session.getChildren(docModelCourant.getRef());
        // on vérifie que la liste contient 4 élément
        Assert.assertEquals(nbChildrenExpected, documentModelList.size());

        return documentModelList;
    }

    /**
     * Vérifie les propriétés des répertoires racine du modèle de Fond De dossier.
     *
     * @author antoine Rolin
     *
     * @param DocumentModelList
     *            liste des répertoires racine du modèle de fond de dossier.
     *
     * @return DocumentModelList liste des répertoires de niveau 2
     */
    protected DocumentModelList VerificationRepertoireRacineDuModeleDuFdd(DocumentModelList documentModelList)
        throws PropertyException {
        // vérification répertoire racine "Répertoire réservé au ministère porteur"
        testRepositoryElement(documentModelList, 0, "Répertoire réservé au ministère porteur", 0);
        // vérification répertoire racine "Répertoire réservé au SGG"
        testRepositoryElement(documentModelList, 1, "Répertoire réservé au SGG", 0);
        // vérification répertoire racine "Répertoire réservé au ministère porteur"
        testRepositoryElement(documentModelList, 2, "Répertoire réservé au ministère porteur et au SGG", 0);
        // vérification répertoire racine "Répertoire accessible à tous les utilisateurs"
        DocumentModelList repertoireNiv2 = testRepositoryElement(
            documentModelList,
            3,
            "Répertoire accessible à tous les utilisateurs",
            2
        );
        return repertoireNiv2;
    }

    /**
     * Vérifie les propriétés des répertoires de niveau 2.
     *
     * @author antoine Rolin
     *
     * @param DocumentModelList
     *            liste des répertoires de niveau 2
     *
     * @param int nbRepertoireAttendu
     *
     * @return DocumentModelList liste des répertoires de niveau 3
     */
    protected DocumentModelList VerificationRepertoireParDefautNiv2(
        DocumentModelList documentModelList,
        int nbRepertoireAttendu
    )
        throws PropertyException {
        // vérification répertoire "Epreuves"
        testRepositoryElement(documentModelList, 0, "Epreuves", 0);
        // vérification répertoire "Répertoire accessible à tous les utilisateurs"
        DocumentModelList repertoireParDefautNiv3 = testRepositoryElement(
            documentModelList,
            1,
            "Documentation jointe",
            nbRepertoireAttendu
        );
        return repertoireParDefautNiv3;
    }

    /**
     * Vérifie les propriétés des répertoires par défault de niveau 3
     *
     * @author antoine Rolin
     *
     * @param DocumentModelList
     *            liste des répertoires de niveau 3
     */
    protected void VerificationRepertoireParDefautNiv3(DocumentModelList documentModelList) throws PropertyException {
        // vérification répertoire "Annexes non publiées"
        testRepositoryElement(documentModelList, 0, "Annexes non publiées", 0);
        // vérification répertoire "Note pour le ministère"
        testRepositoryElement(documentModelList, 1, "Note pour le ministère", 0);
        // vérification répertoire "Rapport de présentation"
        testRepositoryElement(documentModelList, 2, "Rapport de présentation", 0);
        // vérification répertoire "Avis divers"
        testRepositoryElement(documentModelList, 3, "Avis divers", 0);
        // vérification répertoire "Textes de références"
        testRepositoryElement(documentModelList, 4, "Textes de références", 0);
        // vérification répertoire "Etude d'impact"
        testRepositoryElement(documentModelList, 5, "Etude d'impact", 0);
        // vérification répertoire "Divers"
        testRepositoryElement(documentModelList, 6, "Divers", 0);
        // vérification répertoire "Textes consolidés"
        testRepositoryElement(documentModelList, 7, "Textes consolidés", 0);
    }

    /**
     * Vérifie les propriétés des répertoires de niveau 3 pour les types d'actes ayant des répertoires supplémentaires.
     *
     * @author antoine Rolin
     *
     * @param DocumentModelList
     *            liste des répertoires de niveau 3
     */
    protected void VerificationRepertoireSupplementairesNiv3(DocumentModelList documentModelList)
        throws PropertyException {
        // vérification répertoire "Annexes non publiées"
        testRepositoryElement(documentModelList, 0, "Annexes non publiées", 0);
        // vérification répertoire "Note pour le ministère"
        testRepositoryElement(documentModelList, 1, "Note pour le ministère", 0);
        // vérification répertoire "Rapport de présentation"
        testRepositoryElement(documentModelList, 2, "Rapport de présentation", 0);
        // vérification répertoire "Lettres d'accord"

        testRepositoryElement(documentModelList, 3, "Lettres d'accord", 0);
        // vérification répertoire "Lettre de saisine du Conseil d'Etat"
        testRepositoryElement(documentModelList, 4, "Lettre de saisine du Conseil d'Etat", 0);
        // vérification répertoire "Liste des commissaires du gouvernement"
        testRepositoryElement(documentModelList, 5, "Liste des commissaires du gouvernement", 0);

        // vérification répertoire "Avis divers"
        testRepositoryElement(documentModelList, 6, "Avis divers", 0);
        // vérification répertoire "Textes de références"
        testRepositoryElement(documentModelList, 7, "Textes de références", 0);
        // vérification répertoire "Etude d'impact"
        testRepositoryElement(documentModelList, 8, "Etude d'impact", 0);
        // vérification répertoire "Divers"
        testRepositoryElement(documentModelList, 9, "Divers", 0);

        // vérification répertoire "Avis du Conseil d'Etat"
        testRepositoryElement(documentModelList, 10, "Avis du Conseil d'Etat", 0);

        // vérification répertoire "Textes consolidés"
        testRepositoryElement(documentModelList, 11, "Textes consolidés", 0);
    }

    /**
     * création de l'espace d'administration, du répertoire de modèles de feuilles de route et récupèration du
     * répertoire de modèle de feuille de route.
     *
     * @return DocumentModel le répertoire de modèle de feuille de route
     *
     * @author ARN
     *
     */
    protected DocumentModel creationRacineFdd() throws PropertyException {
        // creation de la racine (Workspace Administrateur)
        DocumentModel racine = session.createDocumentModel(DossierSolonEpgConstants.ADMIN_WORKSPACE_DOCUMENT_TYPE);
        racine.setPathInfo("/", "nom_workspace");
        racine = session.createDocument(racine);

        // creation du répertoire des modèles de fonds de dossier
        DocumentModel fddModelRacine = session.createDocumentModel(
            racine.getPathAsString(),
            "modele fdd",
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_ROOT_DOCUMENT_TYPE
        );
        fddModelRacine = session.createDocument(fddModelRacine);
        session.save();

        return fddModelRacine;
    }

    /**
     * Vérification de la validité d'un répertoire situé dans une DocumentModelList, une DocumentModelList qui contient
     * les fils du répertoire.
     *
     * @author ARN
     *
     * @return DocumentModelList la liste des fils du répertoire
     *
     */
    protected DocumentModelList testRepositoryElement(
        DocumentModelList listOfElement,
        int positionInList,
        String titleExpected,
        int nbChildrenExpected
    )
        throws PropertyException {
        DocumentModel docModelCourant = listOfElement.get(positionInList);
        DocumentModelList documentModelListForTest = session.getChildren(docModelCourant.getRef());
        Assert.assertEquals(titleExpected, docModelCourant.getTitle());
        Assert.assertEquals(nbChildrenExpected, documentModelListForTest.size());
        return documentModelListForTest;
    }
}
