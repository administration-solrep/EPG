package fr.dila.solonepg.core.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;

/**
 * Tests unitaires sur le service des modèles de parapheur.
 * 
 * @author arolin
 */
public class TestParapheurService extends SolonEpgRepositoryTestCase {

    private static final Log log = LogFactory.getLog(TestParapheurService.class);

    private ParapheurService parapheurService;
   
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        //import parapheur model Service 
        parapheurService = SolonEpgServiceLocator.getParapheurService();
        assertNotNull(parapheurService);        
    }
    
    /*
     * test de création d'un parapheur dans un  dossier
     */
    public void testCreationParapheur() throws Exception {
    	openSession();
    	
        log.info("testCreationParapheur");
        //création d'un dossier avec un type d'acte 
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
        Dossier dossier = docModel.getAdapter(Dossier.class);
        dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_AMNISTIE);
        
        dossier.save(session);
        
        //creation du parapheur avec l'arborescence associée au type d'acte
        parapheurService.createAndFillParapheur(dossier, session);
        
        dossier.save(session);
        
        assertNotNull(dossier);
        //récupération parapheur
        DocumentModel parapheurDoc = dossier.getParapheur(session).getDocument();
        assertNotNull(parapheurDoc);
        List<DocumentModel> paraFolderList = session.getChildren(parapheurDoc.getRef());
        //verification creation arborescence parapheur
        assertNotNull(paraFolderList);
        assertEquals(3,paraFolderList.size());
        
        closeSession();
    }
    
    /*
     * test de récupération des répertoires racines du parapheur sous forme de noeud
     */
    public void testGetParapheurRootNode() throws Exception {
    	openSession();
    	
        log.info("testGetParapheurRootNode");
        //création d'un dossier avec un type d'acte 
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
        Dossier dossier = docModel.getAdapter(Dossier.class);
        dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_AMNISTIE);
        
        //creation du parapheur avec l'arborescence associée au type d'acte
        parapheurService.createAndFillParapheur(dossier, session);
        
        dossier.save(session);

         assertNotNull(dossier);
         //récupération parapheur
         DocumentModel parapheurDoc = dossier.getParapheur(session).getDocument();
        
        
        List<ParapheurFolder> listParapheurNode =parapheurService.getParapheurRootNode(session,parapheurDoc);
        
        assertNotNull(listParapheurNode);
        assertEquals(3,listParapheurNode.size());
        
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