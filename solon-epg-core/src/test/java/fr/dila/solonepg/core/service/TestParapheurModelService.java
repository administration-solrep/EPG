package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.parapheur.ParapheurModel;
import fr.dila.solonepg.api.service.ParapheurModelService;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PropertyException;

/**
 * Tests unitaires sur le service des modèles de parapheur.
 *
 * @author arolin
 */
public class TestParapheurModelService extends AbstractEPGTest {
    private static final Log log = LogFactory.getLog(TestParapheurModelService.class);

    @Inject
    private ParapheurModelService parapheurService;

    /*
     * Test des actions possibles dans l'arborescence des répertoires d'un modèle de parapheur.
     */

    protected DocumentModel creationEtRecuperationModeleParapheur() throws PropertyException {
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
        Assert.assertNotNull(docModelCourant);
        ParapheurModel parapheurModelDoc = docModelCourant.getAdapter(ParapheurModel.class);
        Assert.assertEquals(typeActe, parapheurModelDoc.getTypeActe());
        return docModelCourant;
    }

    @Test
    public void testCreateNewDefaultFolderInTreeMethod() throws PropertyException {
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
        Assert.assertEquals(0, documentModelListTemp.size());

        // test : création d'un nouveau répertoire dans le répertoire "Acte intégral"
        parapheurService.createNewDefaultFolderInTree(repertoireRacine, session);

        // on verifie qu'un répertoire a été créé
        documentModelListTemp = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(1, documentModelListTemp.size());

        // on verifie le nom et le titre du document créé
        DocumentModel docTemp = documentModelListTemp.get(0);
        Assert.assertEquals(nomRepertoireParDefaut, docTemp.getTitle());
        Assert.assertEquals(nomRepertoireParDefaut, docTemp.getName());
        // on vérifie les propriétés par défault du parapheur
        ParapheurFolder newParaModelFolder = docTemp.getAdapter(ParapheurFolder.class);
        Assert.assertEquals(estVide, newParaModelFolder.getEstNonVide());
        Assert.assertEquals(nbMaxdoc, newParaModelFolder.getNbDocAccepteMax());
        // Assert.assertEquals(feuilleStyleIds.size(),newParaModelFolder.getFeuilleStyleFiles().size());
        Assert.assertEquals(formatIds.size(), newParaModelFolder.getFormatsAutorises().size());
        // fin test

    }

    @Test
    public void testCreateNewDefaultFolderInTreeMethodAddTwoNewFolder() throws PropertyException {
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
        Assert.assertEquals(0, documentModelListTemp.size());

        // test 1 : création d'un nouveau répertoire dans le répertoire "Acte intégral"
        parapheurService.createNewDefaultFolderInTree(repMinisterePorteurDoc, session);
        // on verifie qu'un répertoire a été créé
        documentModelListTemp = session.getChildren(repMinisterePorteurDoc.getRef());
        Assert.assertEquals(1, documentModelListTemp.size());
        // on verifie le nom et le titre du document créé
        DocumentModel newRepositoryDoc = documentModelListTemp.get(0);
        Assert.assertEquals(nomRepertoireParDefaut, newRepositoryDoc.getTitle());
        Assert.assertEquals(nomRepertoireParDefaut, newRepositoryDoc.getName());

        // test 2 : on recree un nouveau répertoire dans le répertoire "répertoire réservé au minisère porteur"
        parapheurService.createNewDefaultFolderInTree(repMinisterePorteurDoc, session);
        // on verifie qu'un répertoire a été créé
        documentModelListTemp = session.getChildren(repMinisterePorteurDoc.getRef());
        Assert.assertEquals(2, documentModelListTemp.size());
        // on verifie le titre du document créé est identique
        newRepositoryDoc = documentModelListTemp.get(1);
        // on verifie que le chemin est différent
        Assert.assertNotSame(nomRepertoireParDefaut, newRepositoryDoc.getName());
    }

    @Test
    public void testCreateNewFolderNodeBeforeMethod() throws PropertyException {
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
        Assert.assertEquals(4, documentModelList.size());

        // on verifie que le document a été créé avant le répertoire "rapport de présentation" dans la liste,on vérifie aussi son titre et don nom
        DocumentModel newDoc = documentModelList.get(0);
        Assert.assertEquals(nomRepertoireParDefaut, newDoc.getTitle());
        Assert.assertEquals(nomRepertoireParDefaut, newDoc.getName());

        // on vérifie les propriétés par défault du parapheur
        ParapheurFolder newParaModelFolder = newDoc.getAdapter(ParapheurFolder.class);
        Assert.assertEquals(estVide, newParaModelFolder.getEstNonVide());
        Assert.assertEquals(nbMaxdoc, newParaModelFolder.getNbDocAccepteMax());
        // Assert.assertEquals(feuilleStyleIds.size(),newParaModelFolder.getFeuilleStyleFiles().size());
        Assert.assertEquals(formatIds.size(), newParaModelFolder.getFormatsAutorises().size());
        // fin test
    }

    @Test
    public void testCreateNewFolderNodeAfterMethod() throws PropertyException {
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
        Assert.assertEquals(4, documentModelList.size());
        // on verifie que le document a été créé avant le répertoire "rapport de présentation" dans la liste,on vérifie aussi son titre et son nom
        DocumentModel newDoc = documentModelList.get(1);
        Assert.assertEquals(nomRepertoireParDefaut, newDoc.getTitle());
        Assert.assertEquals(nomRepertoireParDefaut, newDoc.getName());

        // on vérifie les propriétés par défault du parapheur
        ParapheurFolder newParaModelFolder = newDoc.getAdapter(ParapheurFolder.class);
        Assert.assertEquals(estVide, newParaModelFolder.getEstNonVide());
        Assert.assertEquals(nbMaxdoc, newParaModelFolder.getNbDocAccepteMax());
        // Assert.assertEquals(feuilleStyleIds.size(),newParaModelFolder.getFeuilleStyleFiles().size());
        Assert.assertEquals(formatIds.size(), newParaModelFolder.getFormatsAutorises().size());
        // fin test

    }

    @Test
    public void testEditFolderMethod() throws PropertyException {
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
        parapheurService.editFolder(
            docModelCourant,
            session,
            nouveauNom,
            estVide,
            nbMaxdoc,
            feuilleStyleIds,
            formatIds
        );
        // on verifie que le répertoire n'as pas changé dans la liste mais a change de nom et de titre
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(3, documentModelList.size());

        DocumentModel docTemp = documentModelList.get(0);
        Assert.assertEquals(nouveauNom, docTemp.getTitle());
        // le nom du chemin n'a pas change
        Assert.assertEquals(ancienNom, docTemp.getName());
        // fin test 1

        // test 2 : edition du répertoire "test 1" en "Extrait" qui est un répertoire déjà existant
        parapheurService.editFolder(
            docTemp,
            session,
            nouveauNomExistant,
            estVide,
            nbMaxdoc,
            feuilleStyleIds,
            formatIds
        );
        // on verifie que le répertoire n'as pas changé dans la lsite mais a change de nom
        documentModelList = session.getChildren(repertoireRacine.getRef());
        Assert.assertEquals(3, documentModelList.size());
        // on verifie que le document a été créé en fin de liste, on vérifie aussi son titre et son nom
        docTemp = documentModelList.get(0);
        Assert.assertEquals(nouveauNomExistant, docTemp.getTitle());
        // le nom du fichier n'est pas identique
        Assert.assertNotSame(nouveauNomExistant, docTemp.getName());
        // fin test 2
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
    protected DocumentModel creationRacineParapheur() throws PropertyException {
        // creation de la racine (Workspace Administrateur)
        DocumentModel racine = session.createDocumentModel(DossierSolonEpgConstants.ADMIN_WORKSPACE_DOCUMENT_TYPE);
        racine.setPathInfo("/", "nom_workspace");
        racine = session.createDocument(racine);

        // creation du répertoire des modèles de fonds de dossier
        DocumentModel parapheurModelRacine = session.createDocumentModel(
            racine.getPathAsString(),
            "modeles parapheur",
            SolonEpgParapheurConstants.PARAPHEUR_MODEL_ROOT_DOCUMENT_TYPE
        );
        parapheurModelRacine = session.createDocument(parapheurModelRacine);

        session.save();
        return parapheurModelRacine;
    }
}
