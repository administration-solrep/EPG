package fr.dila.solonepg.core.content.template.factories;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.TypeActe;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

/**
 * Tests unitaires sur la création de l'espace d'administration.
 * 
 * @author arolin
 */
public class TestCreationEspaceAdministration extends SolonEpgRepositoryTestCase {

    private static final Log log = LogFactory.getLog(TestCreationEspaceAdministration.class);

    public void testCreationEspaceAdministrationRepositoryFactory() throws ClientException {
    	openSession();
    	
        log.info("testCreationEspaceAdministrationRepositoryFactory");

        ActeService acteService = SolonEpgServiceLocator.getActeService();
        Collection<TypeActe> listTypeActe = acteService.findAll();

        // get root
        DocumentModel root = session.getRootDocument();

        // get "case-management"
        DocumentModelList docList = session.getChildren(root.getRef());
        assertNotNull(docList);
        assertTrue(docList.size() > 0);
        DocumentModel currentDocumentModel = docList.get(0);

        // get "workspaces"
        docList = session.getChildren(currentDocumentModel.getRef());
        assertNotNull(docList);
        assertTrue(docList.size() > 2);
        currentDocumentModel = docList.get(2);

        // get "admin"
        docList = session.getChildren(currentDocumentModel.getRef());
        assertNotNull(docList);
        assertTrue(docList.size() > 0);
        currentDocumentModel = docList.get(0);

        // get "racine modele fond dossier"
        docList = session.getChildren(currentDocumentModel.getRef());
        assertNotNull(docList);
        assertTrue(docList.size() > 5);
        DocumentModel racineFondDossierModel = docList.get(4);
        assertEquals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_ROOT_DOCUMENT_TYPE, racineFondDossierModel.getType());

        // get " modele fond dossier amnistie"
        DocumentModelList listModelesFondDossier = session.getChildren(racineFondDossierModel.getRef());
        assertNotNull(listModelesFondDossier);
        assertEquals(listTypeActe.size(), listModelesFondDossier.size());
        currentDocumentModel = listModelesFondDossier.get(0);

        // get " racine modele parapheur"
        DocumentModel racineParapheurModel = docList.get(3);
        assertEquals(SolonEpgParapheurConstants.PARAPHEUR_MODEL_ROOT_DOCUMENT_TYPE, racineParapheurModel.getType());

        // get " modele parapheur amnistie"
        DocumentModelList listModelesParapheur = session.getChildren(racineParapheurModel.getRef());

        assertNotNull(listModelesParapheur);
        assertEquals(listTypeActe.size() + 1, listModelesParapheur.size());
        currentDocumentModel = listModelesParapheur.get(0);
        
        closeSession();
    }

    @Override
    protected DocumentModel createDocument(String type, String id) throws Exception {
        DocumentModel document = session.createDocumentModel(type);
        document.setPathInfo("/", id);
        document = session.createDocument(document);
        return document;
    }

}